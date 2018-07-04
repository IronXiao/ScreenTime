package com.example.screentime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
	private final static String TAG = "SCREEN_TIME";
	private TextView text;
	private static final int TYPE_SINCE_CHARGED = 0x1;
	private static final int TYPE_SINCE_UNPLUGED = 0x2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		text = (TextView) findViewById(R.id.screen_time);
		text.setText(getScreenTime(TYPE_SINCE_UNPLUGED));

	}

	private String getScreenTime(int type) {
		try {
			Process process = Runtime.getRuntime().exec("dumpsys batterystats");
			int wF = process.waitFor();
			Log.d(TAG, "wF is :" + wF);
			BufferedReader inputreader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			String line = null;
			int which = -1;
			while ((line = inputreader.readLine()) != null) {
				Log.d(TAG, "line is " + line);
				if (line.startsWith("Statistics since last charge")) {
					which = TYPE_SINCE_CHARGED;
					continue;
				} else if (line.startsWith("Statistics since last unplugged")) {
					which = TYPE_SINCE_UNPLUGED;
					continue;

				}
				if (line.contains("Screen on")) {
					String tmp[] = line.split(",");
					switch (which & type) {
					case TYPE_SINCE_CHARGED:
						return tmp[0];
					case TYPE_SINCE_UNPLUGED:
						return tmp[0];
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "unknown time";

	}
}
