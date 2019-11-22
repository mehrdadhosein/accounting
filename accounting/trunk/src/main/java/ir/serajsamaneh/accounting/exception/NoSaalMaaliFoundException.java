package ir.serajsamaneh.accounting.exception;

import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.util.SerajMessageUtil;

public class NoSaalMaaliFoundException extends FatalException {

	String year;
	String organ;
	String desc;

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getOrgan() {
		return organ;
	}

	public void setOrgan(String organ) {
		this.organ = organ;
	}

	public NoSaalMaaliFoundException(String msg,String year,String organ) {
		super(msg);
		setYear(year);
		setOrgan(organ);
		setDesc(SerajMessageUtil.getMessage("SaalMaali_saalMaaliNotDefined", getYear(),getOrgan()));
	}
	
	public NoSaalMaaliFoundException(String organ) {
		super();
		setOrgan(organ);
		setDesc(SerajMessageUtil.getMessage("SaalMaali_saalMaaliNotDefined"));
	}

	public NoSaalMaaliFoundException() {
		setDesc(SerajMessageUtil.getMessage("SaalMaali_saalMaaliNotDefined"));
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8933044708274619384L;
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Override
	public String getDesc() {
		return desc;
	}

}
