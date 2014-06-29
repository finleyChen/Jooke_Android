
package com.jooketechnologies.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jooketechnologies.event.Event;
import com.jooketechnologies.jooke.Constants;
import com.jooketechnologies.jooke.CreatePlaylistActivity;
import com.jooketechnologies.jooke.InEventMainActivity;
import com.jooketechnologies.jooke.JookeApplication;
import com.jooketechnologies.jooke.R;
import com.jooketechnologies.jooke.SharedPreferenceUtils;
import com.jooketechnologies.jooke.Utils;
import com.jooketechnologies.network.ServerUtilities;
import com.jooketechnologies.user.MySelf;
/**
	 * A fragment that launches other parts of the demo application.
	 */
	public class CreateEventFragment extends Fragment {
		
		private Context mContext;
		public JookeApplication jookeApplication;
		public MySelf mMe;
		
		private Button mAddListButton;
		private Button mStartEventButton;
		private EditText mEventNameEditText;
		private TextView mLogoTextView;
		public ToggleButton mAllowAddSongsToggleButton;
		public ToggleButton mAllowVotingToggleButton;
		
		private String mEventNameString; 
		private String mEventModeString; 
		private String mEventZipCodeString;
		private String mEventTimeString;
		private String mHostIdString;
		private String mHostIpString;
		private String mLatString;
		private String mLonString;
		private String mAllowAddSongsString;
		public boolean isAllowAddSongs=true;
		public boolean isAllowVoting=true;
		
		private View root;
		
		public static final int SONG_SELECT_RESULT = 1000;

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			// Check which request we're responding to
			if (requestCode == SONG_SELECT_RESULT) {
				// Make sure the request was successful
				if (resultCode == Activity.RESULT_OK) {
					int num_of_songs = data.getIntExtra("num_of_songs", 0);
//					showStarred(true, true, num_of_songs);
				}
			}
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			mContext = getActivity();
			jookeApplication = (JookeApplication) getActivity().getApplication();
			mMe = new MySelf(SharedPreferenceUtils.getStoredJookeUserId(mContext),Utils.getIPAddress(true));
			jookeApplication.mMe = mMe;
			
			final View rootView = inflater.inflate(
					R.layout.fragment_createevent, container, false);
			root = (View) rootView.findViewById(R.id.create_event_root);
			FontHelper.applyFont(getActivity(), root, "fonts/gillsans_light.ttf");
			
			mEventNameEditText = (EditText) rootView
					.findViewById(R.id.event_name_text_field);
			mLogoTextView = (TextView) rootView.findViewById(R.id.logo);
			mAllowAddSongsToggleButton = (ToggleButton)rootView.findViewById(R.id.allow_addsongs);
			mAllowVotingToggleButton = (ToggleButton)rootView.findViewById(R.id.allow_voting);
			
			mAddListButton = (Button) rootView
					.findViewById(R.id.add_play_list_button);
			mStartEventButton = (Button) rootView
					.findViewById(R.id.start_event_button);
			
			
			
			
			mLogoTextView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),
                    "fonts/Roboto-Thin.ttf"));

			mAllowAddSongsToggleButton.setOnClickListener(new OnClickListener()
			{
			    @Override
			    public void onClick(View v)
			    {
			    	isAllowAddSongs = !isAllowAddSongs;
			    }
			});
			mAllowVotingToggleButton.setOnClickListener(new OnClickListener()
			{
			    @Override
			    public void onClick(View v)
			    {
			    	isAllowVoting = !isAllowVoting;
			    }
			});
			mAddListButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {

					Intent selectSongIntent = new Intent();
					selectSongIntent.setClass(getActivity(),
							CreatePlaylistActivity.class);
					startActivityForResult(selectSongIntent, SONG_SELECT_RESULT);

				}
			});
			mStartEventButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					
					if(mEventNameEditText.getText().toString()==null || mEventNameEditText.getText().toString().equals("")){
						new AlertDialog.Builder(mContext)
					    .setTitle("Missing info:")
					    .setMessage("Please name your event to continue.")
					    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) { 
					            dialog.cancel();
					        }
					     })
					    .setIcon(android.R.drawable.ic_dialog_alert)
					     .show();
					}
					else{
						
						new createEventTask().execute();
						SharedPreferences settings = mContext.getSharedPreferences("EventSettings", Context.MODE_PRIVATE);
					    SharedPreferences.Editor prefEditor = settings.edit();
					    prefEditor.putString("EventName", mEventNameEditText.getText().toString());
					    prefEditor.commit(); 
					}
					
				}
			});
		

			return rootView;
		}
		
		class createEventTask extends AsyncTask<Void, Void, String> {

			protected String doInBackground(Void... none) {
				try {
					String userId = SharedPreferenceUtils.getStoredJookeUserId(mContext);
					
					
					mEventNameString      = mEventNameEditText.getText().toString();
					mEventModeString      = String.valueOf(isAllowVoting);
					mAllowAddSongsString  = String.valueOf(isAllowAddSongs);
					
					mEventZipCodeString   = jookeApplication.currentZipCode;
					mEventTimeString      = String.valueOf(System.currentTimeMillis());
					mHostIdString         = userId;
					mHostIpString         = Utils.getIPAddress(true);
					mLatString            = String.valueOf(jookeApplication.currentLocation.getLatitude());
					mLonString            = String.valueOf(jookeApplication.currentLocation.getLongitude());
					
					
					return ServerUtilities.createEvent(mEventNameString, mEventModeString, mAllowAddSongsString, 
							mEventZipCodeString, mLatString,mLonString, mEventTimeString, mHostIdString, mHostIpString);
				} catch (Exception e) {
					return null;
				}
			}
			protected void onPostExecute(String event_id){
				if(event_id!=null){
					Log.e("event_id",event_id);
					
					// successfully add an event:
					
					Toast.makeText(getActivity(),
							"Event Created!", Toast.LENGTH_LONG).show();
					SharedPreferenceUtils.storeEventStatus(mContext, event_id,
							mEventNameEditText.getText().toString(), true,isAllowVoting, isAllowAddSongs);
				    Intent inEventMainIntent = new Intent(getActivity(),InEventMainActivity.class);
				    inEventMainIntent.putExtra(Constants.KEY_EVENT_NAME, mEventNameEditText.getText().toString());
				    inEventMainIntent.putExtra(Constants.KEY_EVENT_ID, event_id);
				    inEventMainIntent.putExtra(Constants.KEY_EVENT_MODE, Boolean.valueOf(mEventModeString));
				    inEventMainIntent.putExtra(Constants.KEY_ALLOW_ADDSONGS, Boolean.valueOf(mAllowAddSongsString));
				    inEventMainIntent.putExtra(Constants.KEY_HOST_ID, mHostIdString);
					inEventMainIntent.putExtra(Constants.KEY_IS_HOST,true);
				    
					
					Event newEvent = new Event(mEventNameString, mHostIdString, 
							SharedPreferenceUtils.getStoredJookeFullName(mContext),SharedPreferenceUtils.getUserProfileImgUrl(mContext) , 
							event_id, mEventModeString, mAllowAddSongsString);
					
					mMe.isHost = true;
					mMe.setEvent(newEvent);
					
					
				    startActivity(inEventMainIntent);
					getActivity().finish();
				}
			}

		};

	}

