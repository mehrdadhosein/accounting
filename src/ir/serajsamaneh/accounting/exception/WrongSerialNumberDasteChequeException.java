package ir.serajsamaneh.accounting.exception;

import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.util.SerajMessageUtil;

public class WrongSerialNumberDasteChequeException extends FatalException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 563157078681287128L;

	public WrongSerialNumberDasteChequeException() {
	}

	@Override
	public String getDesc() {
		return SerajMessageUtil.getMessage("Serial_Number_Validate");
	}
	
}
