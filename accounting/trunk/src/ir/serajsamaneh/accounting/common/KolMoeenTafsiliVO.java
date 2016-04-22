package ir.serajsamaneh.accounting.common;

import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.core.base.BaseValueObject;

public class KolMoeenTafsiliVO extends BaseValueObject {

	HesabKolEntity hesabKolEntity;
	HesabMoeenEntity hesabMoeenEntity;
	HesabTafsiliEntity hesabTafsiliEntityONE;
	HesabTafsiliEntity hesabTafsiliEntityTWO = null;
	public HesabKolEntity getHesabKolEntity() {
		return hesabKolEntity;
	}
	public void setHesabKolEntity(HesabKolEntity hesabKolEntity) {
		this.hesabKolEntity = hesabKolEntity;
	}
	public HesabMoeenEntity getHesabMoeenEntity() {
		return hesabMoeenEntity;
	}
	public void setHesabMoeenEntity(HesabMoeenEntity hesabMoeenEntity) {
		this.hesabMoeenEntity = hesabMoeenEntity;
	}
	public HesabTafsiliEntity getHesabTafsiliEntityONE() {
		return hesabTafsiliEntityONE;
	}
	public void setHesabTafsiliEntityONE(HesabTafsiliEntity hesabTafsiliEntityONE) {
		this.hesabTafsiliEntityONE = hesabTafsiliEntityONE;
	}
	public HesabTafsiliEntity getHesabTafsiliEntityTWO() {
		return hesabTafsiliEntityTWO;
	}
	public void setHesabTafsiliEntityTWO(HesabTafsiliEntity hesabTafsiliEntityTWO) {
		this.hesabTafsiliEntityTWO = hesabTafsiliEntityTWO;
	}
}
