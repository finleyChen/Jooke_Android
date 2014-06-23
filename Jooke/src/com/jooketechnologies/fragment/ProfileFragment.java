package com.jooketechnologies.fragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jooketechnologies.adapter.ProfileHistoryAdapter;
import com.jooketechnologies.jooke.Constants;
import com.jooketechnologies.jooke.JookeApplication;
import com.jooketechnologies.jooke.R;
import com.jooketechnologies.style.RoundedAvatarDrawable;
import com.jooketechnologies.style.TypefaceSpan;

// Extra credit: add a birthday field using DatePicker, can save and load
// You can find sample code here:
// http://developer.android.com/resources/tutorials/views/hello-datepicker.html

public class ProfileFragment extends ListFragment {

	public static final int REQUEST_CODE_TAKE_FROM_CAMERA = 0;
	public static final int REQUEST_CODE_SELECT_FROM_GALLERY = 1;
	public static final int REQUEST_CODE_CROP_PHOTO = 2;
	public boolean isInEvent;

	private static final String IMAGE_UNSPECIFIED = "image/*";
	private static final String URI_INSTANCE_STATE_KEY = "saved_uri";
	private static final String TMP_PROFILE_IMG_KEY = "saved_uri";
	// For photo picker selection:
	public static final int ID_PHOTO_PICKER_FROM_CAMERA = 0;
	public static final int ID_PHOTO_PICKER_FROM_GALLERY = 1;
	private Uri mImageCaptureUri;
	private ImageView mImageView;
	private TextView mEventCount;
	private TextView mFollowerCount;
	private TextView mFollowingCount;
	private TextView mFullName;
	private TextView mFollow;
	private TextView mJoinDate;
	private TextView mJoinText;
	private TextView mFeedSubject;
	public JookeApplication jookeApplication;
	private boolean isFollow=false;
    private LayoutParams lp;
	// private boolean isTakenFromCamera;

	private byte[] mProfilePictureArray;
	// All static variables
	static final String URL = "http://api.androidhive.info/music/music.xml";
	// XML node keys
	static final String KEY_SONG = "song"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_ARTIST = "artist";
	static final String KEY_DURATION = "duration";
	static final String KEY_THUMB_URL = "thumb_url";
	public static final String ARG_SECTION_NUMBER = "section_number";
	ArrayList<HashMap<String, String>> historyList;
	NodeList nl;
	ListView list;
	ProfileHistoryAdapter adapter;
	Context mContextThemeWrapper;
	public void populateList() {
		historyList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map1 = new HashMap<String, String>();

		// adding each child node to HashMap key => value
		map1.put(Constants.KEY_HOME_FEED_ACTION, "joins");
		map1.put(Constants.KEY_HOME_FEED_SUBJECT1, "JJMA");
		map1.put(Constants.KEY_HOME_FEED_SUBJECT2, "Life Concert");
		map1.put(Constants.KEY_HOME_PASS_TIME, "10s");
		map1.put(Constants.KEY_HOME_SUBJECT1_PROFILE_IMG, "jjma");

		// adding HashList to ArrayList
		historyList.add(map1);
		HashMap<String, String> map2 = new HashMap<String, String>();
		// adding each child node to HashMap key => value
		map2.put(Constants.KEY_HOME_FEED_ACTION, "is following");
		map2.put(Constants.KEY_HOME_FEED_SUBJECT1, "JJMA");
		map2.put(Constants.KEY_HOME_FEED_SUBJECT2, "RIHANNA");
		map2.put(Constants.KEY_HOME_PASS_TIME, "1d");
		map2.put(Constants.KEY_HOME_SUBJECT1_PROFILE_IMG, "jjma");
		historyList.add(map2);
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if(isInEvent){
			inflater.inflate(R.menu.in_event_profile_settings, menu);
		}
		else{
			inflater.inflate(R.menu.out_event_profile_settings, menu);
		}
	    
		
	    
	    super.onCreateOptionsMenu(menu,inflater);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		jookeApplication = (JookeApplication) getActivity().getApplication();
		isInEvent=jookeApplication.isInEvent;
		populateList();
		adapter = new ProfileHistoryAdapter(getActivity(), historyList);
		setListAdapter(adapter);
		
		final View rootView = inflater.inflate(R.layout.fragment_profile,
				container, false);
		
		mFollow = (TextView) rootView.findViewById(R.id.row_profile_header_button_follow);
		mEventCount = (TextView) rootView.findViewById(R.id.row_profile_header_textview_events_count);
		mFollowerCount = (TextView) rootView.findViewById(R.id.row_profile_header_textview_followers_count);
		mFollowingCount = (TextView) rootView.findViewById(R.id.row_profile_header_textview_following_count);
		mFullName = (TextView) rootView.findViewById(R.id.row_profile_header_textview_fullname);
		mJoinDate = (TextView) rootView.findViewById(R.id.row_profile_header_textview_jointdate);
		mJoinText = (TextView) rootView.findViewById(R.id.join_text);
		SpannableString eventCount = new SpannableString("33");
		eventCount.setSpan(new TypefaceSpan(getActivity(), "Overpass_Regular.ttf", 50), 0,
				eventCount.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mEventCount.setText(eventCount);
		SpannableString followerCount = new SpannableString("87");
		followerCount.setSpan(new TypefaceSpan(getActivity(), "Overpass_Regular.ttf", 50), 0,
				followerCount.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mFollowerCount.setText(followerCount);
		SpannableString followingCount = new SpannableString("107");
		followingCount.setSpan(new TypefaceSpan(getActivity(), "Overpass_Regular.ttf", 50), 0,
				followingCount.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mFollowingCount.setText(followingCount);
		SpannableString fullname = new SpannableString("JOHN HOPKINS");
		fullname.setSpan(new TypefaceSpan(getActivity(), "Roboto-Thin.ttf", 60), 0,
				fullname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mFullName.setText(fullname);
		final Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

	    //lp.gravity= Gravity.CENTER;

		mImageView = (ImageView) rootView.findViewById(R.id.default_profile);
		
		mFollow.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v){
			Log.e("jfjghv","fdsafdsa");
			
				isFollow=!isFollow;
				if (isFollow==true) {
					
					mFollow.startAnimation(shake);
					mFollow.setText("FOLLOWING");
					mFollow.setTextSize(14);
					//mFollow.setMargins(20);
					if (isInEvent)
					{
						mFollow.setBackgroundResource(R.drawable.following_window_inevent);
					}
					else
					{
						mFollow.setBackgroundResource(R.drawable.following_window);
					}
					mFollow.setTextColor(getResources().getColor(R.color.inactive_white));
				}
				else {
					mFollow.setText("+  FOLLOW");
					//mFollow.setLayoutParams(lp);
					mFollow.setTextSize(14);
					mFollow.setBackgroundResource(R.drawable.follow_window);
					mFollow.setTextColor(getResources().getColor(R.color.jooke_gray));
				}
		}
		});
			
		mImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// changing the profile image, show the dialog asking the user
				// to choose between taking a picture and picking from gallery
				// Go to MyRunsDialogFragment for details.
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle(R.string.ui_profile_photo_picker_title);
				// Set up click listener, firing intents open camera or gallery
				// based on
				// choice.
				builder.setItems(R.array.ui_profile_photo_picker_items,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								// Item can be: ID_PHOTO_PICKER_FROM_CAMERA
								// or ID_PHOTO_PICKER_FROM_GALLERY
								Intent intent;

								switch (item) {

								case ID_PHOTO_PICKER_FROM_CAMERA:
									// Take photo from camera								// Construct an intent with action
									// MediaStore.ACTION_IMAGE_CAPTURE
									intent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									// Construct temporary image path and name
									// to save the taken
									// photo
									mImageCaptureUri = getPhotoUri();
									intent.putExtra(
											android.provider.MediaStore.EXTRA_OUTPUT,
											mImageCaptureUri);
									intent.putExtra("return-data", true);
									try {
										// Start a camera capturing activity
										// REQUEST_CODE_TAKE_FROM_CAMERA is an
										// integer tag you
										// defined to identify the activity in
										// onActivityResult()
										// when it returns
										startActivityForResult(intent,
												REQUEST_CODE_TAKE_FROM_CAMERA);
									} catch (ActivityNotFoundException e) {
										e.printStackTrace();
									}
									// isTakenFromCamera = true;
									break;

								case ID_PHOTO_PICKER_FROM_GALLERY:
									// Select from gallery
									// intent = new Intent();
									// intent.setType(IMAGE_UNSPECIFIED);
									// intent.setAction(Intent.ACTION_GET_CONTENT);
									// intent.addCategory(Intent.CATEGORY_OPENABLE);
									intent = new Intent(
											Intent.ACTION_PICK,
											android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

									// Start a gallery choosing activity
									// REQUEST_CODE_SELECT_FROM_GALLERY is an
									// integer tag you
									// defined to identify the activity in
									// onActivityResult()
									// when it returns
									startActivityForResult(intent,
											REQUEST_CODE_SELECT_FROM_GALLERY);
									break;

								default:
									return;
								}
							}
						});

				builder.show();
			}
			
			
				
		});
		
		if (isInEvent)
		{ 
			mFullName.setTextColor(getResources().getColor(R.color.jooke_orange));
			mEventCount.setTextColor(getResources().getColor(R.color.jooke_orange));
			mFollowerCount.setTextColor(getResources().getColor(R.color.jooke_orange));
			mFollowingCount.setTextColor(getResources().getColor(R.color.jooke_orange));
			mJoinDate.setTextColor(getResources().getColor(R.color.jooke_orange));
			mJoinText.setTextColor(getResources().getColor(R.color.jooke_orange));
		}
		
		loadProfile();
		setHasOptionsMenu(true);
		setMenuVisibility(true); 
		return rootView;
	}

	// Handle date after activity returns.
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK)
			return;

		switch (requestCode) {
		case REQUEST_CODE_TAKE_FROM_CAMERA:
			// Send image taken from camera for cropping
			cropImage();
			break;

		case REQUEST_CODE_SELECT_FROM_GALLERY:
			// Send selected image from gallery for cropping
			Uri srcUri = data.getData();
			mImageCaptureUri = getPhotoUri();

			String destPath = mImageCaptureUri.getPath();

			cropImage();
			break;

		case REQUEST_CODE_CROP_PHOTO:
			// Update image view after image crop
			Bundle extras = data.getExtras();
			// Set the profile image in UI
			if (extras != null) {
				Bitmap photo = (Bitmap) extras.getParcelable("data");
				dumpImage(photo);
				loadImageToView();
			}

			// Delete temporary image taken by camera after crop.
			// if (true || isTakenFromCamera) {
			String filepath = mImageCaptureUri.getPath();
			String backupfile = filepath.replaceAll(".jpg$", "~2.jpg");
			File f = new File(filepath);
			if (f.exists())
				f.delete();

			f = new File(backupfile);
			if (f.exists())
				f.delete();

			// }

			break;
		}
	}


	// ****************** private helper functions ***************************//

	private void loadProfile() {

		// Load profile photo from internal storage
		try {
			FileInputStream fis = getActivity().openFileInput(
					getString(R.string.profile_photo_file_name));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			byte[] buffer = new byte[5 * 1024];
			int n;
			while ((n = fis.read(buffer)) > -1) {
				bos.write(buffer, 0, n); // Don't allow any extra bytes to creep
											// in, final write
			}
			fis.close();
			mProfilePictureArray = bos.toByteArray();
		} catch (IOException e) {
		}

		loadImageToView();
	}

	// Helper function to get the URI of the captured image
	private Uri getPhotoUri() {
		return Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
				(new StringBuilder("curr_"))
						.append(String.valueOf(System.currentTimeMillis()))
						.append(".jpg").toString()));
	}

	private void saveProfile() {

		String key, str_val;
		int int_val;

		key = getString(R.string.preference_name);
		SharedPreferences prefs = getActivity().getSharedPreferences(key,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();

		// Commit all the changes into preference file
		editor.apply();

		// Save profile image into internal storage.
		try {
			if (mProfilePictureArray != null) {
				FileOutputStream fos = getActivity().openFileOutput(
						getString(R.string.profile_photo_file_name),
						Context.MODE_PRIVATE);
				fos.write(mProfilePictureArray);
				fos.flush();
				fos.close();
			}
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
	}

	// Crop and resize the image for profile
	private void cropImage() {
		// Use existing crop activity.
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(mImageCaptureUri, IMAGE_UNSPECIFIED);

		// Specify image size
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);

		// Specify aspect ratio, 1:1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", true);
		// REQUEST_CODE_CROP_PHOTO is an integer tag you defined to
		// identify the activity in onActivityResult() when it returns
		startActivityForResult(intent, REQUEST_CODE_CROP_PHOTO);
	}

	private void loadImageToView() {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(
					mProfilePictureArray);
			Bitmap bmap = BitmapFactory.decodeStream(bis);
			mImageView.setImageDrawable(new RoundedAvatarDrawable(bmap));
			bis.close();
		} catch (Exception ex) {
			// Default profile photo if no photo saved before.
			mImageView.setImageResource(R.drawable.default_profile);
			Bitmap bmap = ((BitmapDrawable) getActivity().getResources()
					.getDrawable(R.drawable.default_profile)).getBitmap();
			mImageView.setImageDrawable(new RoundedAvatarDrawable(bmap));
		}
	}

	private void dumpImage(Bitmap bmap) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
			mProfilePictureArray = bos.toByteArray();
			bos.close();
		} catch (IOException ioe) {

		}
	}
}