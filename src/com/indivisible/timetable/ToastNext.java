package com.indivisible.timetable;

import java.util.ArrayList;

import android.content.Context;
import android.widget.Toast;

public class ToastNext {

	Context context;
	ArrayList<String> lines;
	String notice;
	boolean empty;
	
	/**
	 * Constructor
	 * @param ArrayList<String> lines
	 */
	public ToastNext(Context context, ArrayList<String> lines){
		this.context = context;
		this.lines = lines;
		this.notice = "Depart\tArrive";
		this.empty = true;
		
		CreateNotice();
		//DispNotice();
	}
	/**
	 * Make the string for the notification
	 */
	private void CreateNotice(){
		int[] times;
		for (String line : lines){
			// loop through the times
			times = getTimesFromLine(line);
			addTime(times);
			if(empty){
				noTimes();
			}
		}
	}
	/**
	 * Add an individual time to the notification
	 * @param int[] times
	 */
	private void addTime(int[] times){
		this.notice +=
				"\n"+times[0]+":"+times[1]+
				"\t"+times[2]+":"+times[3];
		this.empty = false;
	}
	/**
	 * To be used when no times were found to display the failure...
	 */
	private void noTimes(){
		this.notice +=
				"\n\n  !!  No times found  !!";
	}
	
	/**
	 * Get the times out of the line.
	 * @param String line
	 * @return int[] times
	 */
	private int[] getTimesFromLine(String line){
		// need to move this out and remove duplication
		// should really probably be doing this at the file read stage and passing int[][5] around
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
	
	public void DispNotice(){
		Toast toast = Toast.makeText(this.context, this.notice, Toast.LENGTH_LONG);
		toast.show();
	}
	
}
