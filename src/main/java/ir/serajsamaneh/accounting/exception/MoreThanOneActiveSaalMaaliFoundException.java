package ir.serajsamaneh.accounting.exception;

import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.util.SerajMessageUtil;

public class MoreThanOneActiveSaalMaaliFoundException extends FatalException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 563157078681287128L;

	public MoreThanOneActiveSaalMaaliFoundException() {
	}

	@Override
	public String getDesc() {
		return SerajMessageUtil.getMessage("SaalMaali_MoreThanOneActiveSaalMaaliFound");
	}
	
}
