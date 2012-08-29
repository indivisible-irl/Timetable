package com.indivisible.timetable;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

import android.os.Environment;

public class Timetable {

	File file;
	ArrayList<String> timetableLines;
	
	/**
	 * Constructor
	 * @param fileLocation
	 */
	public Timetable(){
		file = new File(Environment.getExternalStorageDirectory()+"/data", "timetable.txt");
		timetableLines = new ArrayList<String>();
		parseFile(file);
	}
	
	/**
	 * Open, and get all desired lines from the timetable file.
	 * @param File
	 */
	public void parseFile(File file){
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			
			try {
				while((line = br.readLine()) != null){
					if (line.startsWith("#")){
						continue;
					} else if (line.trim().length() == 0){
						continue;
					} else {
						timetableLines.add(line.trim());
					}
				}
			} catch (IOException e) {
				// Can't read file
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// Can't find file
			e.printStackTrace();
			// copy file from assets to sdcard as not exists (future)
		}

	}
	
	/**
	 * Gather the lines for the right day and time.
	 * @param now
	 * @return
	 */
	public ArrayList<String> getNext(){
		// new array to store the lines we want
		ArrayList<String> nextLines = new ArrayList<String>();
		int[] times;
		int haveEnough = 0;
		for (String line : timetableLines){
			if (haveEnough == 2){
				// if we have the next two busses, leave the loop
				// will move this value to options if further developed
				break;
			}
			if (line.startsWith("#")){
				// skip commented lines
				continue;
			} else if (line.startsWith(String.valueOf(Calendar.DAY_OF_WEEK))){
				// if the line starts with the correct day:
				times = getTimesFromLine(line);
				if (times[0] >= Calendar.HOUR_OF_DAY){
					// add time if departure is about or after now
					// the 'haveEnough' limit above will stop collecting so won't list all.
					nextLines.add(line);
				} else if (times[2] <= Calendar.HOUR_OF_DAY){
					// add time if arrival is about or after now.
					// to catch any busses that have left but not arrived yet
					nextLines.add(line);
				} else if (times[0] == 22){
					// add the last bus departure if not enough collected
					// means the last departure will be included if late
					nextLines.add(line);
				}
			} //end collect loop
		} //end file loop
		return nextLines;
	}
	
	/**
	 * Extract the departure and arrival times (as ints) from single lines.
	 * @param line
	 * @return int[4]
	 */
	private int[] getTimesFromLine(String line){
		int[] times = new int[4];
		
		// split up the line into its pieces
		String[] lineParts = line.split(",");
		String[] departs = lineParts[1].split(":");
		String[] arrives = lineParts[2].split(":");
		// fill an array with {departHour, departMin, arriveHour, arriveMin}
		times[0] = Integer.parseInt(departs[0]);
		times[1] = Integer.parseInt(departs[1]);
		times[2] = Integer.parseInt(arrives[0]);
		times[3] = Integer.parseInt(arrives[1]);
		
		return times;
	}
	
}
