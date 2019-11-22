package ir.serajsamaneh.accounting.exception;

import ir.serajsamaneh.core.exception.SerajException;
import ir.serajsamaneh.core.util.SerajMessageUtil;

public class SaalMaaliStartDateException extends SerajException{

	
	public SaalMaaliStartDateException()
	{
		
	}
	
	@Override
	public String getDesc() {
		
		return SerajMessageUtil.getMessage("SaalMaaliStartDateError");
	}

}
