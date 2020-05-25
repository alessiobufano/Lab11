package it.polito.tdp.rivers.model;

import java.util.*;

public class Simulator {

	//system status
	private double occupation;
	
	//handle queue
	private PriorityQueue<Event> queue;
	
	//input values
	private double maxCapacity;
	private double minFlow;
	private double overflow;
	private final double overflowProbability = 0.05;
	private int totalDays;
	private List<Flow> flows;
	
	//output values
	private int underflowDays;
	private double totalOccupation;
	private double averageOccupation;
	
	
	public void init(River r, double maxCapacity, double minFlow) {
		
		this.maxCapacity = maxCapacity;
		this.minFlow = minFlow;
		this.overflow = 10.0*minFlow;
		this.totalDays = r.getFlows().size();
		this.flows = new LinkedList<>(r.getFlows());
		
		this.occupation = maxCapacity/2.0;
		
		this.underflowDays = 0;
		this.totalOccupation = maxCapacity/2.0;
		this.averageOccupation = 0.0;
		
		this.queue = new PriorityQueue<>();
		
		for(int d=0; d<totalDays; d++)
		{
			Event e = new Event(d, flows.get(d));
			this.queue.add(e);
		}
	}
	
	public void run() {
		
		Event e;
		while( (e = this.queue.poll())!=null ) 
		{
			double q = e.getFlow().getFlow()*86400;
			this.occupation += q;
			
			double overflowDay = Math.random();
			if(overflowDay<=this.overflowProbability)
			{
				if(this.occupation>=this.overflow)
					this.occupation -= this.overflow;
				else
					this.underflowDays++;
			}	
			else if(this.occupation>=this.minFlow)
				this.occupation -= this.minFlow;
			else
				this.underflowDays++;
			
			double difference = this.occupation - this.maxCapacity;
			if(difference>0)
				this.occupation -= difference;
			
			this.totalOccupation += this.occupation;
		}
		
		this.averageOccupation = (double) (this.totalOccupation/((double) (this.totalDays)));
	}

	public int getUnderflowDays() {
		return underflowDays;
	}

	public double getAverageOccupation() {
		return averageOccupation;
	}

	public double getMaxCapacity() {
		return maxCapacity;
	}

	public double getMinFlow() {
		return minFlow;
	}

	public int getTotalDays() {
		return totalDays;
	}
	
}
