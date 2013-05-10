package com.jbp.randommaster.quant.pde;

import org.apache.commons.math3.analysis.UnivariateFunction;


public class BlackScholesPdeFiniteDifferenceSolver {

	private UnivariateFunction boundaryConditionAtT;
	private UnivariateFunction boundaryConditionAtMaxX;
	private UnivariateFunction boundaryConditionAtMinX;
	
	private int spaceSteps;
	private int timeSteps;
	
	private BlackScholesPde pde;
	
	public BlackScholesPdeFiniteDifferenceSolver(
				BlackScholesPde pde,
				UnivariateFunction boundaryConditionAtT,
				UnivariateFunction boundaryConditionAtMaxX,
				UnivariateFunction boundaryConditionAtMinX,
				int spaceSteps,
				int timeSteps) {
		this.pde=pde;
		this.boundaryConditionAtT=boundaryConditionAtT;
		this.boundaryConditionAtMaxX=boundaryConditionAtMaxX;
		this.boundaryConditionAtMinX=boundaryConditionAtMinX;
		this.spaceSteps=spaceSteps;
		this.timeSteps=timeSteps;
	}
	
	
	public double solve(double x0) {
		// TODO: not yet implemented
		return 0.0;
	}

	
	
	public BlackScholesPde getPde() {
		return pde;
	}

	public int getSpaceSteps() {
		return spaceSteps;
	}

	public int getTimeSteps() {
		return timeSteps;
	}

	public UnivariateFunction getBoundaryConditionAtT() {
		return boundaryConditionAtT;
	}
	
	public UnivariateFunction getBoundaryConditionAtMaxX() {
		return boundaryConditionAtMaxX;
	}
	
	public UnivariateFunction getBoundaryConditionAtMinX() {
		return boundaryConditionAtMinX;
	}
	
}
