package ir.serajsamaneh.accounting.exception;

import ir.serajsamaneh.accounting.sanadhesabdari.SanadHesabdariEntity;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.util.SerajMessageUtil;

public class SanadIsNotBalancedException extends FatalException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 563157078681287128L;

	SanadHesabdariEntity entity;
	
	public SanadIsNotBalancedException() {
	}

	public SanadIsNotBalancedException(SanadHesabdariEntity entity) {
		this.entity=entity;
	}
	
	@Override
	public String getDesc() {
		return SerajMessageUtil.getMessage("Sanad_notBalanced", entity);
	}
	
}
