package ir.serajsamaneh.accounting.exception;

import ir.serajsamaneh.core.exception.SerajException;
import ir.serajsamaneh.core.util.SerajMessageUtil;

public class NoActiveSaalMaaliFoundException extends SerajException {

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

	public NoActiveSaalMaaliFoundException(String msg,String year,String organ) {
		super(msg);
		setYear(year);
		setOrgan(organ);
		setDesc(SerajMessageUtil.getMessage("SaalMaali_NoActiveSaalMaaliFound", getYear(),getOrgan()));
	}
	
	public NoActiveSaalMaaliFoundException(String organ) {
		super();
		setOrgan(organ);
		setDesc(SerajMessageUtil.getMessage("SaalMaali_NoActiveSaalMaaliFound"));
	}

	public NoActiveSaalMaaliFoundException() {
		setDesc(SerajMessageUtil.getMessage("SaalMaali_NoActiveSaalMaaliFound"));
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
