package com.indivisible.timetable;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
//import android.os.Environment;
import android.util.Log;

public class Timetable {
	
	List<int[]> times;
	
	/**
	 * Constructor
	 * @param fileLocation
	 */
	public Timetable(Context context, int resourceID){
		//file = new File(Environment.getExternalStorageDirectory()+"/data", "timetable.txt");
		this.times = init(context, resourceID);
		Log.d("done", "Timetable()");
	}
	
	/**
	 * Initalise the object and set the int[][] array.
	 * @param File file
	 * @return int[x][5] times.
	 */
	private List<int[]> init(Context c, int rID){
		List<String> timetableLines = parseFile(c, rID);
		List<int[]> timetableTimes = convertToNumbers(timetableLines);
		Log.d("done", "T.init()");
		return  timetableTimes;
	}
	
	/**
	 * Open, and get all desired lines from the timetable file.
	 * @param File
	 */
	private List<String> parseFile(Context c, int rID){
		// stick with File input or reader object better?
		List<String> timetableLines = new ArrayList<String>();
		String line = null;
		
		InputStream iStream = c.getResources().openRawResource(rID);
		BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
		
		try {
			while((line = br.readLine()) != null){
				//Log.i("line", line);
				if (line.startsWith("#")){
					continue;
				} else if (line.trim().length() == 0){
					continue;
				} else {
					timetableLines.add(line.trim());
				}
			}
			iStream.close();
			br.close();
		} catch (IOException e) {
			// Can't read file
			e.printStackTrace();
		}
		
		Log.d("done", "parseFile()");
		return timetableLines;
	}
	
	/**
	 * Convert the String ArrayList to an int[][] for easier usage
	 * @param ArrayList<String> lines
	 * @return int[][] intTimes
	 */
	private List<int[]> convertToNumbers(List<String> lines){
		// array to store individual day, departure and arrival times for one journey
		int[] singleTime = new int[5];
		// and an array to store all of the above times in one 2D array
		List<int[]> intTimes = new ArrayList<int[]>();
		
		// loop through the ArrayList, convert to int[] and add to List<int[]>
		for (int i=0; i<lines.size(); i++){
			singleTime = getTimesFromLine(lines.get(i));
			intTimes.add(singleTime);
		}
		Log.d("done", "convertToNumbers()");
		return intTimes;
	}
	
	/**
	 * Extract the departure and arrival times (as ints) from single lines.
	 * @param line
	 * @return int[7]
	 */
	private int[] getTimesFromLine(String line){
		// TODO should prob make a more elegant regex split
		
		// {day, departHour, departMin, arriveHour, arriveMin, departInMins, arriveInMins}
		int[] times = new int[7];
		
		// split up the line into chunks
		String[] lineParts = line.split(",");
		String[] departs = lineParts[1].split(":");
		String[] arrives = lineParts[2].split(":");
		
		// fill the int array with {day, departHour, departMin, arriveHour, arriveMin, arriveInMins, departInMins}
		times[0] = Integer.parseInt(lineParts[0]);
		times[1] = Integer.parseInt(departs[0]);
		times[2] = Integer.parseInt(departs[1]);
		times[3] = Integer.parseInt(arrives[0]);
		times[4] = Integer.parseInt(arrives[1]);
		
		// work out the minute equivalents for easy comparison
		times[5] = times[1] * 60 + times[2];
		times[6] = times[3] * 60 + times[4];
		
		return times;
	}
	
	/**
	 * Work out today's times and return a formatted string for display
	 * @return String todayTimes
	 */
	public String grabDay(int dayInt){
		List<int[]> todayTimes;
		String todayTimesString;
		
		// separate today's time out and format as a string to display
		todayTimes = getDay(dayInt, this.times);
		//todayTimesString = generateText(todayTimes);
		todayTimesString = generateFormattedText(todayTimes);
		
		//Log.d("done", "grabToday()");
		//Log.i("today:", todayTimesString);
		return todayTimesString;
	}
	
	/**
	 * Extract all of today's times from full listing and return them
	 * @return int[x][5] todayTimes
	 */
	private List<int[]> getDay(int dayInt, List<int[]> allTimes){
		List<int[]> todayTimes = new ArrayList<int[]>();
		
		// get the current day and time
		Log.i("Today:", String.valueOf(dayInt));
		
		for (int[] time : allTimes){
			if(time[0] == dayInt){
				todayTimes.add(time);
			}
		}
		return todayTimes;
	}
	
	/**
	 * Convert an array of times into a printable string (table like)
	 * @param int[][] times
	 * @return String textTimes
	 */
	public String generateText(List<int[]> times){
		//TODO if extending for other than today display we should use this method.
		String textTimes = "Depart | Arrive\n-------|-------";
		for (int[] time : times){
			textTimes += toPrintableTime(time);
		}
		return textTimes;
	}
	/**
	 * Convert an array of times into a formatted String for display
	 * Uses HTML tags
	 * @param List<int[]> times
	 * @return String
	 */
	public String generateFormattedText(List<int[]> times){
		int pastVarience = -60;		// (now - then) give increasingly neg nums as we go back
		int futureVarience = 100;	// 
		
		String header = "Depart | Arrive\n-------|-------";
		String past="none", present="none", future="none", formattedTextTimes="no text";
		int departMins, arriveMins;
		
		Calendar cal = Calendar.getInstance();
		int nowMins = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
		
		for(int[] time : times){
			departMins = time[5];
			arriveMins = time[6];
			
			past = "";
			present = "";
			future = "";
			
			if (nowMins-departMins < pastVarience || nowMins-arriveMins < pastVarience){
				past += toPrintableTime(time);
			} else if (nowMins-departMins > futureVarience || nowMins-arriveMins > futureVarience){
				future += toPrintableTime(time);
			} else {
				present += toPrintableTime(time);
			}
		} //end for loop
		
		formattedTextTimes = header + "<br />" +
				"<font color='#333333'>" + past + "</font>" +
				"<font color='#333333'>" + present + "</font>" +
				"<font color='#333333'>" + future + "</font>";
		return formattedTextTimes;
	}
	
	/**
	 * Convert a int[] into a printable line of text
	 * @param int[] time
	 * @return
	 */
	public String toPrintableTime(int[] time){
		return "\n"+ String.format("%02d", time[1]) +":"+ String.format("%02d", time[2]) +
				"  |  "+ String.format("%02d", time[3]) +":"+ String.format("%02d", time[4]) + "<br />";
	}
	
//	/**
//	 * Gather the lines for the right day and time.
//	 * @param now
//	 * @return
//	 */
//	public ArrayList<String> getNext(){
//		// new array to store the lines we want
//		// change to work with int[] and take in ArrayList<String>
//		ArrayList<String> nextLines = new ArrayList<String>();
//		int[] times;
//		int haveEnough = 0;
//		for (String line : timetableLines){
//			if (haveEnough == 2){
//				// if we have the next two busses, leave the loop
//				// will move this value to options if further developed
//				break;
//			}
//			if (line.startsWith("#")){
//				// skip commented lines
//				continue;
//			} else if (line.startsWith(String.valueOf(Calendar.DAY_OF_WEEK))){
//				// if the line starts with the correct day:
//				times = getTimesFromLine(line);
//				if (times[0] >= Calendar.HOUR_OF_DAY){
//					// add time if departure is about or after now
//					// the 'haveEnough' limit above will stop collecting so won't list all.
//					nextLines.add(line);
//				} else if (times[2] <= Calendar.HOUR_OF_DAY){
//					// add time if arrival is about or after now.
//					// to catch any busses that have left but not arrived yet
//					nextLines.add(line);
//				} else if (times[0] == 22){
//					// add the last bus departure if not enough collected
//					// means the last departure will be included if late
//					nextLines.add(line);
//				}
//			} //end collect loop
//		} //end file loop
//		return nextLines;
//	}
	

	
}
