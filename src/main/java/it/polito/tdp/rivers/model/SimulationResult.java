package it.polito.tdp.rivers.model;

public class SimulationResult {

	double avgC;
	int numberOfDays;

	public SimulationResult(double avgC, int numberOfDays) {
		this.avgC = avgC;
		this.numberOfDays = numberOfDays;
	}

	public double getAvgC() {
		return avgC;
	}

	public void setAvgC(double avgC) {
		this.avgC = avgC;
	}

	public int getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

}