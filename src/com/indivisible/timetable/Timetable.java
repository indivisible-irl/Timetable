package com.indivisible.timetable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;

/**
 * Class to hold an array of Events.
 * @author indivisible-irl, Dave A
 * @version 0.12
 */
public class Timetable implements Serializable{

	/////////////////////////////////////////////////////////////
	//// vars
	private List<Event> events;
	private EventGroups eventGroups;
	
	private static final int PAST_LIMIT = -45;	// (mins) then - now < limit | count as past
	private static final int FUTURE_LIMIT = 90;	// (mins) then - now > limit | count as future
	
	private static final long serialVersionUID = 1L;
	
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
	
	/**
	 * @return the eventGroups
	 */
	public EventGroups getEventGroups() {
		return eventGroups;
	}

	/**
	 * @param eventGroups the eventGroups to set
	 */
	public void setEventGroups(EventGroups eventGroups) {
		this.eventGroups = eventGroups;
	}

	/////////////////////////////////////////////////////////////
	//// functional methods
	/**
	 * Return all events for a particular day of the week (optional exclude every-day events)
	 * @param day int [1-7] = [sun-sat] or 0 for none.
	 * @param onlyToday boolean, true to exclude every-day events (i.e. day == 0)
	 * @return List<Event> events
	 */
	public List<Event> getDayEvents(int day, boolean onlyToday){
		List<Event> dayEvents = new ArrayList<Event>();
		
		for (Event e : getEvents()){
			if (onlyToday){
				if (e.getDay() == day){
					dayEvents.add(e);
				}
			} else {
				if (e.getDay() == 0 || e.getDay() == day){
					dayEvents.add(e);
				}
			}
		}
		return dayEvents;
	}
	/**
	 * Get all (incl repeating) events for today
	 * @return List(Event)
	 */
	public List<Event> getTodaysEvents(){
		return new ArrayList<Event>(getDayEvents(getCurrentDayMins()[0], false));
	}
	/**
	 * Return the index of the next event to start
	 * @param events List(Event), List of Events to use
	 * @param timeToCompare int[2], array of day, timeMins to compare against
	 * @param isStart boolean, true if comparing Event start times, false for Event end times
	 * @return int, index of desired event within List
	 */
	public int getNextEvent(List<Event> events, int[] timeToCompare, boolean isStart){
		int diff = 1441, eventTime;
		//TODO best value? if no next event return first? Issues if out of range: catch elsewhere?
		int returnIndex = 0;
		
		for (int i=0; i<events.size(); i++){
			// loop through List<Event>
			if (!(timeToCompare[0] == 0 || timeToCompare[0] == events.get(i).getDay())){
				// if wrong day, skip (FYI: 0 = every day)
				continue;
			}
			if (isStart){	// looking at start times
				eventTime = events.get(i).getMinsStart();
			} else {		// looking at end times
				eventTime = events.get(i).getMinsEnd();
				if (eventTime == -1){
					// escape loop if no end time set
					break;
				}
			}
			if (eventTime - timeToCompare[1] > 0 && eventTime - timeToCompare[1] < diff){
				returnIndex = i;
				diff = eventTime - timeToCompare[1];
			}
		}
		return returnIndex;
	}
	/**
	 * Get the most recent event to start. Possibly still active.
	 * @param events List(Event), List of Events to use
	 * @param timeToCompare int[2], array of day, timeMins to compare against
	 * @param isStart boolean, true if comparing Event start times, false for Event end times
	 * @return int, index of desired event within List
	 */
	public int getPreviousEvent(List<Event> events, int[] timeToCompare, boolean isStart){
		int[] now = getCurrentDayMins();
		int diff = -1441, eventTime;
		//TODO best value? if no prev event return first? Issues if out of range: catch elsewhere?
		int returnIndex = 0;
		
		for (int i=0; i<events.size(); i++){
			// loop through List<Event>
			if (!(timeToCompare[0] == 0 || timeToCompare[0] == events.get(i).getDay())){
				// if wrong day, skip (FYI: 0 = every day)
				continue;
			}
			if (isStart){
				eventTime = events.get(i).getMinsStart();
			} else {
				eventTime = events.get(i).getMinsEnd();
				if (eventTime == -1){
					// escape loop if no end time set
					break;
				}
			}
			if (eventTime - now[1] < 0 && eventTime - now[1] > diff){
				returnIndex = i;
				diff = eventTime - now[1];
			}
		}
		return returnIndex;
	}
	
	public EventGroups divideEventsByTime(List<Event> events, boolean isStart){
		//TODO would sets be a better choice here? 
		List<Event> past = new ArrayList<Event>();
		List<Event> present = new ArrayList<Event>();
		List<Event> future = new ArrayList<Event>();
		
		int[] pastLimit = getCurrentDayMins();
		int[] futureLimit = getCurrentDayMins();
		pastLimit[1] = pastLimit[1] + PAST_LIMIT;
		futureLimit[1] = futureLimit[1] + FUTURE_LIMIT;
		
		int pastIndex = getPreviousEvent(events, pastLimit, isStart);
		int futureIndex = getNextEvent(events, futureLimit, isStart);
		
		for(int i=0; i<events.size(); i++){
			if (i <= pastIndex){
				past.add(events.get(i));
			} else if (i < futureIndex){
				present.add(events.get(i));
			} else {
				future.add(events.get(i));
			}
		}
		
		return new EventGroups(past, present, future);
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
