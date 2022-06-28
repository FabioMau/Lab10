package it.polito.tdp.rivers.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.rivers.db.RiversDAO;

public class Model {

	private List<River> rivers;
	private PriorityQueue<Flow> queue;
	
	public Model() {
		RiversDAO riversDao = new RiversDAO();
		// Get all the data from the DB
		rivers = riversDao.getAllRivers();
		for (River river : rivers) {
			riversDao.getFlows(river);
		}
	}
	
	public List<River> getRivers() {
		return rivers;
	}
	
	public LocalDate getStartDate(River river) {
		if(!river.getFlows().isEmpty())
			return river.getFlows().get(0).getDay();
		return null;
	}
	
	public LocalDate getEndDate(River river) {
		if(!river.getFlows().isEmpty())
			return river.getFlows().get(river.getFlows().size() -1).getDay();
		return null;
	}
	
	public int getNumMeasurements(River river) {
		return river.getFlows().size();
	}
	
	public double getFlowAvg(River river) {
		return river.getFlowAvg();
	}
	
	public SimulationResult simulate(River river, double k) {
		
		queue = new PriorityQueue<Flow>();
		queue.addAll(river.getFlows());
		
		List<Double> capacity = new ArrayList<Double>();
		
		// Capienza bacino
		double Q = k * 30.0 *  convertM3SecToDay(river.getFlowAvg());
		// Occupazione iniziale
		double C = Q/2;
		// Flusso in uscita
		double fOutMin = convertM3SecToDay(0.8 * river.getFlowAvg());
		// Numero giorni critici
		int numberOfDays = 0;
		
		Flow flow;
		while((flow = this.queue.poll()) != null) {
			
			double fOut = fOutMin;
			
			if(Math.random() > 0.95) 
				fOut *= 10;
			
			C += flow.getFlow();
			
			if(C>Q) {
				C=Q;
			} else if(C<fOut) {
				C=0;
				numberOfDays++;
			} else {
				C-=fOut;
			}
			
			capacity.add(C);
			
		}
		
		double CAvg = 0;
		for (Double d : capacity) {
			CAvg += d;
		}
		CAvg = CAvg / capacity.size();
		return new SimulationResult(CAvg, numberOfDays);
		
	}
	
	public double convertM3SecToDay(double M3Sec) {
		return M3Sec * 60 * 60 * 24;
	}
	
	public double convertM3DayToSec(double M3Day) {
		return M3Day / 60 / 60 / 24;
	}
	
}
