package com.indivisible.timetable;

import java.util.List;

import android.content.Context;

public class Timetable {

	List<Event> events;
	
	public Timetable(Context c, int resID){
		EventCreator ec = new EventCreator(c, resID);
		this.events = ec.getEvents();
	}
	
	
}
