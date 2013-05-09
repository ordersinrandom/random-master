package com.jbp.randommaster.quant.sde.univariate;

import com.jbp.randommaster.quant.sde.TimeDomain;

/**
 * A generic definition of univariate Drift Diffusion Process
 */
public class DriftDiffusionProcess<T1 extends DriftTerm, T2 extends DiffusionTerm> implements UnivariateStochasticProcess {

	protected T1 drift;
	protected T2 diffusion;
	
	protected TimeDomain timeDomain;
	
	public DriftDiffusionProcess() {
		timeDomain=new TimeDomain();
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
	
	public TimeDomain getTimeDomain() {
		return timeDomain;
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
