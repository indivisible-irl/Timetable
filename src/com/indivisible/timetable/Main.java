package com.indivisible.timetable;

import java.io.*;
import java.util.ArrayList;
//import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class Main extends Activity{

	Timetable ttable;
	ToastNext toast;
	File file;
	
	ArrayList<String> nextLines;
	int day, hour, minute;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// run when our app first loads (may have been auto killed prev)
		super.onCreate(savedInstanceState);
		ttable = new Timetable();
		testFileExists();
	}

	@Override
	protected void onResume() {
		// run whenever we return to our app 
		super.onResume();
		nextLines =	ttable.getNext();
		toast = new ToastNext(this, nextLines);
		toast.DispNotice();
		//finish();
	}
	
	/**
	 * Check for timetable file and if not exists place default on sdcard
	 */
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
		
	}


	
}
