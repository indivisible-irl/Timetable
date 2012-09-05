package com.indivisible.timetable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

public class EventCreator {

	//// vars
	private List<Event> events;
	
	//// constructors
	protected EventCreator(String fileLoc){
		//TODO Take a file location as input and pass InputStream onwards
	}
	/**
	 * Create an array of Events using an internal resource ID
	 * @param context
	 * @param resID int
	 */
	protected EventCreator(Context c, int resID){
		InputStream iStream = c.getResources().openRawResource(resID);
		createEvents(iStream);
	}
	protected EventCreator(File file){
		//TODO Take a File object as input and pass InputStream onwards
	}
	
	
	//// functional methods
	/**
	 * Taking an InputStream this method will cause the List<Event> array to be populated
	 * @param iStream InputStream
	 */
	private void createEvents(InputStream iStream){
		List<String> lines = grabLines(iStream);
		makeEvents(lines);
	}
	/**
	 * Extracts lines from an InputStream and returns them as a List<String>
	 * @exception IOException (catches)
	 * @param iStream InputStream
	 * @return List<String>
	 */
	private List<String> grabLines(InputStream iStream){
		List<String> lines = new ArrayList<String>();
		String line;
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
			while((line = br.readLine()) != null){
				Log.i("line", line);
				if (line.startsWith("#")){
					// ignore comments
					continue;
				} else if (line.trim().length() == 0){
					// ignore empty lines
					continue;
				} else {
					lines.add(line.trim());
				}
			}
			iStream.close();
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("createEvents", "IOException, see console");
			e.printStackTrace();
		}
		
		return lines;
	}
	/**
	 * Populate the List<Event> events array
	 * @param lines List<String>
	 */
	private void makeEvents(List<String> lines){
		this.events = new ArrayList<Event>();
		int[] eventTimes = new int[3];
		
		for (String line : lines){
			eventTimes = getTimesFromLine(line);
			events.add(new Event(eventTimes));
		}
	}
	/**
	 * Extract the departure and arrival times (as ints) from single lines.
	 * @param line
	 * @return int[7]
	 */
	private int[] getTimesFromLine(String line){
		// TODO should prob make a more elegant regex split
		// TODO throw exception if doesn't fit the template
		
		// {day, departHour, departMin, arriveHour, arriveMin, departInMins, arriveInMins}
		int[] times = new int[3];
		
		// split up the line into chunks
		String[] lineParts = line.split(",");
		String[] departs = lineParts[1].split(":");
		String[] arrives = lineParts[2].split(":");
		
		// fill the int array with {day, startMins, endMins}
		times[0] = Integer.parseInt(lineParts[0]);
		times[1] = Integer.parseInt(departs[0]) * 60 + Integer.parseInt(departs[1]);
		times[3] = Integer.parseInt(arrives[0]) * 60 + Integer.parseInt(arrives[1]);
		
		return times;
	}
	/**
	 * @return List<Event> Events from a file.
	 */
	public List<Event> getEvents(){
		return this.events;
	}
}
