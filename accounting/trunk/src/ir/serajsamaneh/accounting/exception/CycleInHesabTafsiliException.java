package ir.serajsamaneh.accounting.exception;

import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.core.exception.SerajException;
import ir.serajsamaneh.core.util.SerajMessageUtil;

public class CycleInHesabTafsiliException extends SerajException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3991101953754112764L;

	HesabTafsiliEntity mainEntity;
	HesabTafsiliEntity childEntity;
	public CycleInHesabTafsiliException(HesabTafsiliEntity mainEntity,
			HesabTafsiliEntity childEntity) {
		this.mainEntity= mainEntity;
		this.childEntity= childEntity;
	}

	@Override
	public String getDesc() {
		return SerajMessageUtil.getMessage("HesabTafsili_cycleDetected", mainEntity.getDesc(), childEntity.getDesc());
	}
	
	

}
