package com.jooketechnologies.network;

import java.io.IOException;
import android.os.Handler;
import android.util.Log;

import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;


/**
 * Consumes messages from a RabbitMQ broker
 * 
 */
public class MessageConsumer extends IConnectToRabbitMQ {

	private String routingKey;
	
	public MessageConsumer(String server, String exchange, String exchangeType, String routingKey) {
		super(server, exchange, exchangeType);
		this.routingKey = routingKey;
	}

	// The Queue name for this consumer
	private String mQueue;
	private QueueingConsumer MySubscription;

	// last message to post back
	private byte[] mLastMessage;

	// An interface to be implemented by an object that is interested in
	// messages(listener)
	public interface OnReceiveMessageHandler {
		public void onReceiveMessage(byte[] message);
	};

	// A reference to the listener, we can only have one at a time(for now)
	private OnReceiveMessageHandler mOnReceiveMessageHandler;

	/**
	 * 
	 * Set the callback for received messages
	 * 
	 * @param handler
	 *            The callback
	 */
	public void setOnReceiveMessageHandler(OnReceiveMessageHandler handler) {
		mOnReceiveMessageHandler = handler;
	};

	private Handler mMessageHandler = new Handler();
	private Handler mConsumeHandler = new Handler();

	// Create runnable for posting back to main thread
	final Runnable mReturnMessage = new Runnable() {
		public void run() {
			mOnReceiveMessageHandler.onReceiveMessage(mLastMessage);
		}
	};

	final Runnable mConsumeRunner = new Runnable() {
		public void run() {
			Consume();
		}
	};

	/**
	 * Create Exchange and then start consuming. A binding needs to be added
	 * before any messages will be delivered
	 */
	@Override
	public boolean connectToRabbitMQ() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				if (MessageConsumer.super.connectToRabbitMQ()) {

					try {
						// get the temporary queue name
						mQueue = mModel.queueDeclare().getQueue();
						
						// bind the queue to the exchange
						MessageConsumer.this.AddBinding(routingKey);
						
						MySubscription = new QueueingConsumer(mModel);
						
						mModel.basicConsume(mQueue, false, MySubscription);
						Log.d("Jooke", "Waiting for the message....");
					} catch (IOException e) {
						e.printStackTrace();
					}
//					if (MyExchangeType == "fanout") {
//						MessageConsumer.this.AddBinding("");// fanout has
//															// default binding
//					}
					Running = true;
					mConsumeHandler.post(mConsumeRunner);

				}
			}
		};

		thread.start();
		
		return true;
	}

	/**
	 * Add a binding between this consumers Queue and the Exchange with
	 * routingKey
	 * 
	 * @param routingKey
	 *            the binding key eg GOOG
	 */
	public void AddBinding(String routingKey) {
		try {
			mModel.queueBind(mQueue, mExchange, routingKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Remove binding between this consumers Queue and the Exchange with
	 * routingKey
	 * 
	 * @param routingKey
	 *            the binding key eg GOOG
	 */
	public void RemoveBinding(String routingKey) {
		try {
			mModel.queueUnbind(mQueue, mExchange, routingKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void Consume() {
		Thread thread = new Thread() {

			@Override
			public void run() {
				while (Running) {
					QueueingConsumer.Delivery delivery;
					try {
						delivery = MySubscription.nextDelivery();
						mLastMessage = delivery.getBody();
						mMessageHandler.post(mReturnMessage);
						try {
							mModel.basicAck(delivery.getEnvelope()
									.getDeliveryTag(), false);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					} catch(ShutdownSignalException sse){
						Log.e("ShutdownSignalException","ShutdownSignalException,"+sse.getMessage());
						sse.printStackTrace();

						Dispose();
					}
				}
			}
		};
		thread.start();

	}

	public void dispose() {
		Running = false;
	}
}