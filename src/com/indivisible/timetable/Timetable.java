package com.indivisible.timetable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;

public class Timetable {

	/////////////////////////////////////////////////////////////
	//// vars
	List<Event> events;
	int pastLimit = -45;	// (mins) then - now < limit | count as past
	int futureLimit = 90;	// (mins) then - now > limit | count as future
	
	/////////////////////////////////////////////////////////////
	//// constructors
	/**
	 * Create a new Timetable using an Android resource
	 * @param Context Activity context
	 * @param resID int of android resource 
	 */
	public Timetable(Context c, int resID){
		EventCreator ec = new EventCreator(c, resID);
		setEvents(ec.getEvents());
	}
	
	/////////////////////////////////////////////////////////////
	//// getters & setters
	public void setEvents(List<Event> events){
		this.events = events;
	}
	public List<Event> getEvents(){
		return this.events;
	}
	
	/////////////////////////////////////////////////////////////
	//// functional methods
	/**
	 * Return all events for a particular day of the week
	 * @param day int [1-7] = [sun-sat] or 0 for none.
	 * @return List<Event> events
	 */
	public List<Event> getDayEvents(int day){
		List<Event> dayEvents = new ArrayList<Event>();
		
		for (Event e : getEvents()){
			if (e.getDay() == day){
				dayEvents.add(e);
			}
		}
		return dayEvents;
	}
	public Event getNextEvent(){
		Event nextEvent = new Event();
		int[] now = getCurrentDayMins();
		
		for (Event e : getEvents()){
			
		}
		
		return nextEvent;
	}
	/**
	 * Get the current day and time (in minutes)
	 * @return
	 */
	public int[] getCurrentDayMins(){
		Calendar cal = Calendar.getInstance();
		int[] now = new int[2];
		
		now[0] = cal.get(Calendar.DAY_OF_WEEK);
		now[1] = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
		
		return now;		
	}
}
