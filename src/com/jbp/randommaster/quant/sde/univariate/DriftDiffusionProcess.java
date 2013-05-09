package com.jbp.randommaster.quant.sde.univariate;

/**
 * A generic definition of univariate Drift Diffusion Process
 */
public class DriftDiffusionProcess<T1 extends DriftTerm, T2 extends DiffusionTerm> {

	protected T1 drift;
	protected T2 diffusion;
	
	public DriftDiffusionProcess() {
		
	}

	public T1 getDrift() {
		return drift;
	}

	protected void setDrift(T1 drift) {
		this.drift = drift;
	}

	public T2 getDiffusion() {
		return diffusion;
	}

	protected void setDiffusion(T2 diffusion) {
		this.diffusion = diffusion;
	}
	
	@Override
	public String toString() {
		StringBuilder buf=new StringBuilder();
		buf.append("Process { drift=");
		buf.append(drift);
		buf.append(", diffusion=");
		buf.append(diffusion);
		buf.append(" }");
		return buf.toString();
	}
}
