package ir.serajsamaneh.accounting.exception;

import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.core.exception.SerajException;
import ir.serajsamaneh.core.util.SerajMessageUtil;

public class MahyatKolNotDefinedException extends SerajException {

	HesabKolEntity hesabKol;
	public HesabKolEntity getHesabKol() {
		return hesabKol;
	}

	public void setHesabKol(HesabKolEntity hesabKol) {
		this.hesabKol = hesabKol;
	}

	public MahyatKolNotDefinedException(HesabKolEntity hesabKol) {
		this.hesabKol = hesabKol;
	}

	@Override
	public String getDesc() {
		return SerajMessageUtil.getMessage("HesabKol_MahyatKolNotDefined",getHesabKol().getName());
	}

}
