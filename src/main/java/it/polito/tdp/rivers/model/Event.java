package it.polito.tdp.rivers.model;

public class Event implements Comparable<Event>{
	
	private int t;
	private Flow flow;
	
	public Event(int t, Flow flow) {
		super();
		this.t = t;
		this.flow = flow;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
	}

	@Override
	public int compareTo(Event e2) {
		return this.t - e2.t;
	}
	
	

}
