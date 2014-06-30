package com.jooketechnologies.jooke;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jooketechnologies.music.Song;

public class Utils {
	public final static int THEME_JOOKE_BLUE = 0;
	public final static int THEME_JOOKE_ORANGE = 1;
	public final static int MIN_PORT_NUMBER = 1;
	public final static int MAX_PORT_NUMBER = 49151;

	public static boolean hasICS() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}

	public static String call(String url) throws Exception {
		URL serverAddress = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) serverAddress
				.openConnection();
		connection.connect();

		BufferedReader responseReader = new BufferedReader(
				new InputStreamReader(connection.getInputStream()));
		String line;
		StringBuilder bodyBuilder /* lol */= new StringBuilder();
		while ((line = responseReader.readLine()) != null) {
			bodyBuilder.append(line);
			bodyBuilder.append("\n");
		}
		connection.disconnect();

		return bodyBuilder.toString().trim();
	}

	public static void getIP() {

		Log.e("getIP",
				"Retrieving your public IP address from http://icanhazip.com");
		try {
			String result = call("http://icanhazip.com");
			Log.e("result",result);
			if (!result.matches("^[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+$"))
				throw new Exception("Response is not a valid IP address: "
						+ result);
			Log.e("getIP", "Your IP address is " + result);
		} catch (Exception e) {
			Log.e("getIP", "An error occured retrieving IP: " + e.toString());
		}
	}

	public static boolean isPortAvailable(int port) {
		if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
			throw new IllegalArgumentException("Invalid start port: " + port);
		}

		ServerSocket ss = null;
		DatagramSocket ds = null;
		try {
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);
			return true;
		} catch (IOException e) {
		} finally {
			if (ds != null) {
				ds.close();
			}

			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					/* should not be thrown */
				}
			}
		}

		return false;
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuilder sbuf = new StringBuilder();
		for (int idx = 0; idx < bytes.length; idx++) {
			int intVal = bytes[idx] & 0xff;
			if (intVal < 0x10)
				sbuf.append("0");
			sbuf.append(Integer.toHexString(intVal).toUpperCase());
		}
		return sbuf.toString();
	}

	/**
	 * Get utf8 byte array.
	 * 
	 * @param str
	 * @return array of NULL if error was found
	 */
	public static byte[] getUTF8Bytes(String str) {
		try {
			return str.getBytes("UTF-8");
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Load UTF8withBOM or any ansi text file.
	 * 
	 * @param filename
	 * @return
	 * @throws java.io.IOException
	 */
	public static String loadFileAsString(String filename)
			throws java.io.IOException {
		final int BUFLEN = 1024;
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(
				filename), BUFLEN);
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
			byte[] bytes = new byte[BUFLEN];
			boolean isUTF8 = false;
			int read, count = 0;
			while ((read = is.read(bytes)) != -1) {
				if (count == 0 && bytes[0] == (byte) 0xEF
						&& bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF) {
					isUTF8 = true;
					baos.write(bytes, 3, read - 3); // drop UTF8 bom marker
				} else {
					baos.write(bytes, 0, read);
				}
				count += read;
			}
			return isUTF8 ? new String(baos.toByteArray(), "UTF-8")
					: new String(baos.toByteArray());
		} finally {
			try {
				is.close();
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Returns MAC address of the given interface name.
	 * 
	 * @param interfaceName
	 *            eth0, wlan0 or NULL=use first interface
	 * @return mac address or empty string
	 */
	public static String getMACAddress(String interfaceName) {
		try {
			List<NetworkInterface> interfaces = Collections
					.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				if (interfaceName != null) {
					if (!intf.getName().equalsIgnoreCase(interfaceName))
						continue;
				}
				byte[] mac = intf.getHardwareAddress();
				if (mac == null)
					return "";
				StringBuilder buf = new StringBuilder();
				for (int idx = 0; idx < mac.length; idx++)
					buf.append(String.format("%02X:", mac[idx]));
				if (buf.length() > 0)
					buf.deleteCharAt(buf.length() - 1);
				return buf.toString();
			}
		} catch (Exception ex) {
		} // for now eat exceptions
		return "";
		/*
		 * try { // this is so Linux hack return
		 * loadFileAsString("/sys/class/net/" +interfaceName +
		 * "/address").toUpperCase().trim(); } catch (IOException ex) { return
		 * null; }
		 */
	}

	/**
	 * Get IP address from first non-localhost interface
	 * 
	 * @param ipv4
	 *            true=return ipv4, false=return ipv6
	 * @return address or empty string
	 */
	public static String getIPAddress(boolean useIPv4) {
		try {
			List<NetworkInterface> interfaces = Collections
					.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				List<InetAddress> addrs = Collections.list(intf
						.getInetAddresses());
				for (InetAddress addr : addrs) {
					if (!addr.isLoopbackAddress()) {
						String sAddr = addr.getHostAddress().toUpperCase();
						boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
						if (useIPv4) {
							if (isIPv4)
								return sAddr;
						} else {
							if (!isIPv4) {
								int delim = sAddr.indexOf('%'); // drop ip6 port
																// suffix
								return delim < 0 ? sAddr : sAddr.substring(0,
										delim);
							}
						}
					}
				}
			}
		} catch (Exception ex) {
		} // for now eat exceptions
		return "";
	}

	public static void onActivityCreateSetTheme(Activity activity, boolean in) {
		if (in) {
			activity.setTheme(R.style.Theme_JookeOrange);
		} else {
			activity.setTheme(R.style.Theme_JookeBlue);
		}

	}

	public static Bitmap getRoundedBitmap(Bitmap bitmap) {
		final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(output);

		final int color = Color.RED;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawOval(rectF, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		// bitmap.recycle();

		return output;
	}

	public static void storeSelectedArtistIndex(Activity currentActivity,
			ArrayList<Integer> artistIndexList) {
		SharedPreferences mPrefs = currentActivity
				.getPreferences(Context.MODE_PRIVATE);
		mPrefs.edit().clear().commit();
		StringBuilder str = new StringBuilder();
		for (int index : artistIndexList) {
			str.append(index).append(",");
		}
		Editor prefsEditor = mPrefs.edit();
		prefsEditor.putString("selected_artists", str.toString());
		prefsEditor.commit();
	}

	public static ArrayList<Integer> getStoredArtistIndex(
			Activity currentActivity) {

		SharedPreferences mPrefs = currentActivity
				.getPreferences(Context.MODE_PRIVATE);
		String savedSelectedArtists = mPrefs.getString("selected_artists", "");

		List<String> items = Arrays.asList(savedSelectedArtists
				.split("\\s*,\\s*"));
		ArrayList<Integer> artistIndexList = new ArrayList<Integer>();
		for (String item : items) {
			if (!item.equals("")) {
				artistIndexList.add(Integer.valueOf(item));
			}

		}
		return artistIndexList;
	}

	public static void storeSongList(Activity currentActivity,
			ArrayList<Song> songList) {
		SharedPreferences mPrefs = currentActivity
				.getPreferences(Context.MODE_PRIVATE);
		mPrefs.edit().clear().commit();
		Editor prefsEditor = mPrefs.edit();
		Gson gson = new Gson();
		String json = gson.toJson(songList);
		prefsEditor.putString("selected_songs", json);
		prefsEditor.commit();

	}

	public static ArrayList<Song> getStoredSongList(Activity currentActivity) {
		Gson gson = new Gson();
		SharedPreferences mPrefs = currentActivity
				.getPreferences(Context.MODE_PRIVATE);
		String json = mPrefs.getString("selected_songs", "");
		if (json.equals("")) {
			return null;
		}
		Type type = new TypeToken<ArrayList<Song>>() {
		}.getType();
		ArrayList<Song> storedSelectedSongs = gson.fromJson(json, type);
		return storedSelectedSongs;
	}

	public static Bitmap getAlbumBitmap(Context context, long album_id) {
		Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
		Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
		ContentResolver res = context.getContentResolver();
		InputStream in;
		try {
			in = res.openInputStream(uri);
			Bitmap artwork = BitmapFactory.decodeStream(in);
			return artwork;
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			return null;
		}

	}

	public static String getTitleFromPosition(Context context, int position) {
		switch (position) {
		case 0:
			return "jooke";
		case 1:
			return context.getResources().getString(
					R.string.discover_fragment_title);
		case 2:
			return context.getResources().getString(
					R.string.create_event_fragment_title);
		case 3:
			return context.getResources().getString(
					R.string.profile_fragment_title);
		default:
			return null;
		}

	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static int convertDpToPixel(Context context, float dp) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float px1 = dp * (metrics.densityDpi / 160f);
		float px = metrics.widthPixels - px1;
		return (int) px;
	}

	public static Drawable getCategoryIconDrawable(Context context, String uri) {
		int imageResource = context.getResources().getIdentifier(uri, null,
				context.getPackageName());
		Drawable image = context.getResources().getDrawable(imageResource);
		return image;
	}
}