package com.indivisible.timetable;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to hold past, present and future groups of Events
 * @author indivisible-irl, Dave A
 * @version 0.01
 */
public class EventGroups {

	/////////////////////////////////////////////////////////////
	//// vars
	private List<Event> past;
	private List<Event> present;
	private List<Event> future;
	
	/////////////////////////////////////////////////////////////
	//// constructors
	/**
	 * Create empty EventGrouping.
	 */
	protected EventGroups(){
		this.setPast(new ArrayList<Event>());
		this.setPresent(new ArrayList<Event>());
		this.setFuture(new ArrayList<Event>());
	}
	/**
	 * Create new EventGrouping.
	 * @param past List(Event), Events that are in the past
	 * @param present List(Event), Events that are in the present
	 * @param future List(Event), Events that are in the future
	 */
	protected EventGroups(List<Event> past, List<Event> present, List<Event> future){
		this.setPast(past);
		this.setPresent(present);
		this.setFuture(future);
	}
	
	/////////////////////////////////////////////////////////////
	//// getters & setters
	/**
	 * @return the past
	 */
	public List<Event> getPast() {
		return past;
	}
	/**
	 * @param past the past to set
	 */
	public void setPast(List<Event> past) {
		this.past = past;
	}
	/**
	 * @return the present
	 */
	public List<Event> getPresent() {
		return present;
	}
	/**
	 * @param present the present to set
	 */
	public void setPresent(List<Event> present) {
		this.present = present;
	}
	/**
	 * @return the future
	 */
	public List<Event> getFuture() {
		return future;
	}
	/**
	 * @param future the future to set
	 */
	public void setFuture(List<Event> future) {
		this.future = future;
	}
	
	/////////////////////////////////////////////////////////////
	//// functional methods
	/**
	 * Get one of the three groups of Events (-1: past, 0: present, 1: future)
	 * @param group int, the 
	 * @return List(Event)
	 */
	public List<Event> getGroup(int group){
		switch (group){
		case -1:
			return getPast();
		case 0:
			return getPresent();
		case 1:
			return getFuture();
		default:
			return new ArrayList<Event>();
		}
	}
	/**
	 * Get a HTML formatted string of events grouped into colors
	 * @return String
	 */
	public String makeColoredString(){
		String pastStr = "<br /><font color='#333333'>" + makeGroupString(getPast()) + "</font>";
		String presentStr = "<br /><font color='#FF0000'>" + makeGroupString(getPresent()) + "</font>";
		String futureStr = "<br /><font color='#009900'>" + makeGroupString(getFuture()) + "</font>";
		String headerStr = "Depart | Arrive<br />-------|-------";
		
		return headerStr + pastStr + presentStr + futureStr;
	}
	/**
	 * Get a HTML formatted string of events
	 * @param events List(Event)
	 * @return String
	 */
	public String makeGroupString(List<Event> events){
		String strOut = "";
		for (Event e : events){
			strOut += "<br />" + e.getFormatedString();
		}
		return strOut;
	}
	
}
