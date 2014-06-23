package com.jooketechnologies.network;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import android.util.Log;

public class JookeWebSocketClient{

	private WebSocketClient cc;

	public JookeWebSocketClient() {
		
		try{
			cc = new WebSocketClient( new URI("ws://localhost:8063"), new Draft_17() ){

				@Override
				public void onMessage( String message ) {
					Log.e("onMessage","onMessage");
				}

				@Override
				public void onOpen( ServerHandshake handshake ) {
					Log.e("onOpen","onOpen");
				}

				@Override
				public void onClose( int code, String reason, boolean remote ) {
					Log.e("onClose","onClose"+reason);
				}

				@Override
				public void onError( Exception ex ) {
					Log.e("onError","onError"+ex.getMessage());
				}
			};

		}catch ( URISyntaxException ex ) {
			
		}
		cc.connect();
	
	}

}
