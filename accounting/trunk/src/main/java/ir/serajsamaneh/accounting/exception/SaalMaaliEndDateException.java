package ir.serajsamaneh.accounting.exception;

import ir.serajsamaneh.core.exception.SerajException;
import ir.serajsamaneh.core.util.SerajMessageUtil;

public class SaalMaaliEndDateException extends SerajException{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SaalMaaliEndDateException()
	{
		
	}
	
	@Override
	public String getDesc() {
		
		return SerajMessageUtil.getMessage("SaalMaaliEndDateError");
	}

}
