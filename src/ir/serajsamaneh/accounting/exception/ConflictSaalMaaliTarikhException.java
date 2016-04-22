package ir.serajsamaneh.accounting.exception;

import ir.serajsamaneh.core.exception.SerajException;
import ir.serajsamaneh.core.util.SerajMessageUtil;

public class ConflictSaalMaaliTarikhException extends SerajException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6766340526222796584L;




	Integer saalMaaliYear;
	public Integer getSaalMaaliYear() {
		return saalMaaliYear;
	}


	public void setSaalMaaliYear(Integer saalMaaliYear) {
		this.saalMaaliYear = saalMaaliYear;
	}


	public Integer getTarikhSanadYear() {
		return tarikhSanadYear;
	}


	public void setTarikhSanadYear(Integer tarikhSanadYear) {
		this.tarikhSanadYear = tarikhSanadYear;
	}


	Integer tarikhSanadYear;
	
	public ConflictSaalMaaliTarikhException(Integer saalMaaliYear, Integer tarikhSanadYear){
		this.saalMaaliYear = saalMaaliYear;
		this.tarikhSanadYear = tarikhSanadYear;
	}


	@Override
	public String getDesc() {
		return SerajMessageUtil.getMessage("Sanad_tanaghozSaalMaaliAndTarikhSanad", getSaalMaaliYear().toString(), getTarikhSanadYear().toString() );
	}

}
