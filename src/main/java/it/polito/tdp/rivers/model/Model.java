package it.polito.tdp.rivers.model;

import java.text.*;
import java.util.*;

import it.polito.tdp.rivers.db.RiversDAO;

public class Model {
	
	private RiversDAO dao;
	private Map<Integer, River> rIdMap;
	private Simulator simulator;
	
	public Model() {
		dao = new RiversDAO();
		rIdMap = new HashMap<>();
		dao.getAllRivers(rIdMap);
		simulator = new Simulator();
	}
	
	public River getRiverInformations(int riverId) {
		
		dao.getAllFlows(rIdMap, riverId);
		dao.getAverageFlow(rIdMap, riverId);
		River r = rIdMap.get(riverId);
		
		return r;
	}

	public Collection<River> getRivers() {
		return rIdMap.values();
	}
	
	public void doSimulation(River r, double maxCapacity, double minFlow) {
		simulator.init(r, maxCapacity, minFlow);
		simulator.run();
	}
	
	public String getSimulationResult() {
		
		Format df = new DecimalFormat("0.00");
		
		double underflow = (double) (simulator.getUnderflowDays()/((double) simulator.getTotalDays()));
		
		String result = "Simulation results are:\n"
				+ "- Max Capacity possible = "+df.format(simulator.getMaxCapacity())+" cube meters per day;\n"
				+ "- Min Flow = "+df.format(simulator.getMinFlow())+" cube meters per day;\n"
				+ "- Average Occupation = "+df.format(simulator.getAverageOccupation())+" cube meters per day;\n"
			    + "- Total days = "+simulator.getTotalDays()+";\n"
				+ "- Underflow days = "+simulator.getUnderflowDays()+" ("+df.format(underflow)+"%)\n";
		
		return result;
	}

}
