package com.indivisible.timetable;

import java.io.Serializable;

public class Event implements Serializable{

	//// vars
	private static final long serialVersionUID = 1L;
	private int day, minsStart, minsEnd;
	//private String title;
	//private boolean active;
	
	//// constructors
	protected Event(){
		this.day = 0;
		this.minsStart = 0;
		this.minsEnd = 0;
	}
	protected Event(int solo){
		this.day = 0;
		this.minsStart = solo;
	}
	protected Event(int start, int end){
		this.day = 0;
		this.setMinsStart(start);
		this.setMinsEnd(end);
	}
	protected Event(int day, int start, int end){
		this.day = day;
		this.setMinsStart(start);
		this.setMinsEnd(end);
	}
	protected Event(int[] multiInfo){
		//TODO test for int[] length?
		this.day = multiInfo[0];
		this.setMinsStart(multiInfo[1]);
		this.setMinsEnd(multiInfo[2]);
	}
	
	//// getters & setters
	/**
	 * @return the timeMinsStart
	 */
	public int getMinsStart() {
		return minsStart;
	}
	/**
	 * @param timeMinsStart the timeMinsStart to set
	 */
	public void setMinsStart(int timeMinsStart) {
		this.minsStart = timeMinsStart;
	}
	/**
	 * @return the timeMinsEnd
	 */
	public int getMinsEnd() {
		return minsEnd;
	}
	/**
	 * @param timeMinsEnd the timeMinsEnd to set
	 */
	public void setMinsEnd(int timeMinsEnd) {
		this.minsEnd = timeMinsEnd;
	}
	
	//// functional methods
	/**
	 * Get the Event's start time in hours and minutes
	 * @return int[2] hourMinsStart
	 */
	public int[] getHourMinsStart(){
		int[] hourMins = new int[2];
		hourMins[0] = getMinsStart() / 60;
		hourMins[1] = getMinsStart() % 60;
		return hourMins;
	}
	/**
	 * Get the Event's end time in hours and minutes
	 * @return int[2] hourMinsEnd
	 */
	public int[] getHourMinsEnd(){
		int[] hourMins = new int[2];
		hourMins[0] = getMinsEnd() / 60;
		hourMins[1] = getMinsEnd() % 60;
		return hourMins;
	}
	/**
	 * Get the start and end times in hours and minutes
	 * @return int[4] hourMinsAll
	 */
	public int[] getHourMinsAll(){
		int[] hourMins = new int[4];
		hourMins[0] = getMinsStart() / 60;
		hourMins[1] = getMinsStart() % 60;
		hourMins[2] = getMinsEnd() / 60;
		hourMins[3] = getMinsEnd() % 60;
		return hourMins;
	}
}
