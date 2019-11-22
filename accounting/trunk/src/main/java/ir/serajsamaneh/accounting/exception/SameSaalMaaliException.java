package ir.serajsamaneh.accounting.exception;

import ir.serajsamaneh.core.exception.SerajException;
import ir.serajsamaneh.core.util.SerajMessageUtil;

public class SameSaalMaaliException extends SerajException {

	String year;
	String message;

	@Override
	public String getMessage() {
		
		return super.getMessage();
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getYear() {
		return year;
	}

	public SameSaalMaaliException(String year, String Message) {
		setYear(year);
		setMessage(Message);
	}

	public SameSaalMaaliException(String Message) {

		setMessage(Message);
		

	}

	@Override
	public String getDesc() {
		
		return SerajMessageUtil.getMessage(getMessage(), getYear());
	}

}
