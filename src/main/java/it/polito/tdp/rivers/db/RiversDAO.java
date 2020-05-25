package it.polito.tdp.rivers.db;

import java.util.*;

import it.polito.tdp.rivers.model.Flow;
import it.polito.tdp.rivers.model.River;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RiversDAO {

	public void getAllRivers(Map<Integer, River> rIdMap) {
		
		final String sql = "SELECT id, name FROM river";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				
				if(!rIdMap.containsKey(res.getInt("id"))) {
					River r = new River(res.getInt("id"), res.getString("name"));
					rIdMap.put(r.getId(), r);
				}
			}

			conn.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

	}
	
	public void getAllFlows(Map<Integer, River> rIdMap, int riverId) {
		
		final String sql = "SELECT id, day, flow FROM flow WHERE river=? ORDER BY day";
		
		List<Flow> list = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, riverId);
			
			ResultSet res = st.executeQuery();
			
			River r = null;
			if(rIdMap.containsKey(riverId))
				r = rIdMap.get(riverId);

			while (res.next()) {
				
				Flow f = new Flow(res.getInt("id"), res.getDate("day").toLocalDate(), res.getDouble("flow"), r);
				list.add(f);
			}
			
			Collections.sort(list);
			r.setFlows(list);
			
			conn.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
	}
	
	public void getAverageFlow(Map<Integer, River> rIdMap, int riverId) {
		
		final String sql = "SELECT AVG(flow) AS f FROM flow WHERE river=?";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, riverId);
			
			ResultSet res = st.executeQuery();

			if (res.next() && rIdMap.containsKey(riverId)) {
				
				River r = rIdMap.get(riverId);
				r.setFlowAvg(res.getDouble("f"));
			}

			conn.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
	}
}
