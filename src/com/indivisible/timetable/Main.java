package com.indivisible.timetable;

import java.io.*;
//import java.util.ArrayList;
//import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity implements View.OnClickListener{

	Timetable ttable;
	//ToastNext toast;
	int recourceID = R.raw.timetable;	// hard coded for the moment
	
	//ArrayList<String> nextLines;
	//int day, hour, minute;
	
	Button bClose = null;
	TextView tvCurrent = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// run when our app first loads (may have been auto killed prev)
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_disp);
		//testFileExists();
		init();
		updateListing();
	}

	@Override
	protected void onPause() {
		// run whenever we leave our app 
		super.onPause();
		finish();
	}
	
	public void onClick(View v) {
		// Only one button; when clicked close.
		finish();
//		switch (v.getId()){
//		case R.id.b_close:
//			finish();
//		}
	}
	
	@SuppressWarnings("unused")
	public void init(){
		Button bClose = (Button) this.findViewById(R.id.b_close);
		TextView tvCurrent = (TextView) this.findViewById(R.id.tv_current);
		Log.d("done", "init()");
	}
	
	public void updateListing(){
		String currentTimesString;
		currentTimesString = "FILLER";
		ttable = new Timetable(this.getApplicationContext(), this.recourceID);
		currentTimesString = ttable.grabToday();
		Log.d("after", "T.grabToday()");
		tvCurrent.setText(currentTimesString);
		Log.d("done", "updateListing()");
	}
	
	/**
	 * Check for timetable file and if not exists place default on sdcard
	 *   ! currently unused
	 */
	@SuppressWarnings("unused")
	private void testFileExists() {
		File testFile = new java.io.File(Environment.getExternalStorageDirectory().getPath()+"/data" , "timetable.txt");
		if (!(testFile.exists())){
			// timetable does not exist on sdcard, let's create it
			copyFile();
		} else {
			Log.i("testFile", "File found");
		}
	}
	
	/**
	 * Will copy the default timetable to the sdcard
	 *   ! currently unused
	 */
	private void copyFile() {
		// create directory on sdcard
		File sdcard = Environment.getExternalStorageDirectory();
		File dir = new File(sdcard.getAbsolutePath() + "/data/timetable");
		dir.mkdir();
		
		// read the file from the apk (raw folder)
		InputStream iStream = getResources().openRawResource(R.raw.timetable);
		ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		int size = 0;
		
		// Read the entire resource into a local byte buffer.
		byte[] buffer = new byte[1024];
		try {
			while((size = iStream.read(buffer, 0, 1024)) >= 0){
			  oStream.write(buffer, 0, size);
			}
			iStream.close();
		} catch (IOException e) {
			// couldn't read file (internal)
			e.printStackTrace();
		}
		buffer = oStream.toByteArray();
		
		// dump the buffer onto the sdcard
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(dir+"timetable.txt");
			fos.write(buffer);
			fos.close();
		} catch (FileNotFoundException e) {
			// can't open external file
			e.printStackTrace();
		} catch (IOException e) {
			// can't write into the external file
			e.printStackTrace();
		}
		
	} //end copyFile


	
}