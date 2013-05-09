package com.jbp.randommaster.quant.sde.univariate;

/**
 * A generic definition of univariate Drift Diffusion Process
 */
public abstract class DriftDiffusionProcess {

	protected DriftTerm drift;
	protected DiffusionTerm diffusion;
	
	public DriftDiffusionProcess() {
		
	}

	public DriftTerm getDrift() {
		return drift;
	}

	public void setDrift(DriftTerm drift) {
		this.drift = drift;
	}

	public DiffusionTerm getDiffusion() {
		return diffusion;
	}

	public void setDiffusion(DiffusionTerm diffusion) {
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
