package ir.serajsamaneh.accounting.base;

import ir.serajsamaneh.accounting.hesabkol.HesabKolService;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.accounting.sanadtype.SanadTypeService;
import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.exception.NoOrganFoundException;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.systemconfig.SystemConfigService;
import ir.serajsamaneh.core.user.UserEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.enumeration.YesNoEnum;

import java.io.Serializable;
import java.util.Date;

public abstract class BaseAccountingService <T extends BaseEntity<U>, U extends Serializable> extends BaseEntityService<T, U>{
	
	SaalMaaliService saalMaaliService;
	SanadTypeService sanadTypeService;
	SystemConfigService systemConfigService;
	HesabKolService hesabKolService;
	HesabMoeenService hesabMoeenService;
	HesabTafsiliService hesabTafsiliService;
	
	public HesabKolService getHesabKolService() {
		return hesabKolService;
	}

	public void setHesabKolService(HesabKolService hesabKolService) {
		this.hesabKolService = hesabKolService;
	}

	public HesabMoeenService getHesabMoeenService() {
		return hesabMoeenService;
	}

	public void setHesabMoeenService(HesabMoeenService hesabMoeenService) {
		this.hesabMoeenService = hesabMoeenService;
	}

	public HesabTafsiliService getHesabTafsiliService() {
		return hesabTafsiliService;
	}

	public void setHesabTafsiliService(HesabTafsiliService hesabTafsiliService) {
		this.hesabTafsiliService = hesabTafsiliService;
	}

	public SystemConfigService getSystemConfigService() {
		return systemConfigService;
	}

	public void setSystemConfigService(SystemConfigService systemConfigService) {
		this.systemConfigService = systemConfigService;
	}

	public SanadTypeService getSanadTypeService() {
		return sanadTypeService;
	}

	public void setSanadTypeService(SanadTypeService sanadTypeService) {
		this.sanadTypeService = sanadTypeService;
	}

	public SaalMaaliService getSaalMaaliService() {
		return saalMaaliService;
	}

	public void setSaalMaaliService(SaalMaaliService saalMaaliService) {
		this.saalMaaliService = saalMaaliService;
	}


	public SaalMaaliEntity getSaalmaaliByDate(Date date, OrganEntity organEntity) {
		return getSaalMaaliService().getSaalmaaliByDate(date, organEntity);
	}
	
	public boolean checkIfMustValidateTafsiliOneAndTwoAreRelated(OrganEntity organEntity) {
		return false;
	}
	
	public Boolean checkIfMustValidateHesabTafsiliHasChild(OrganEntity organ){
		String validateHesabTafsiliHasChild = getSystemConfigService().getValue(organ, null, "validateHesabTafsiliHasChild");
		if(validateHesabTafsiliHasChild == null)
			throw new FatalException(SerajMessageUtil.getMessage("Accounting_system_config_is_not_compelete", organ));
		return YesNoEnum.getName(new Integer(validateHesabTafsiliHasChild)).equals(YesNoEnum.YES);
	}
	
	public Boolean checkIfMustValidateHesabMoeenHasChild(OrganEntity organ){
		String validateHesabMoeenHasChild = getSystemConfigService().getValue(organ, null, "validateHesabMoeenHasChild");
		if(validateHesabMoeenHasChild == null)
			throw new FatalException(SerajMessageUtil.getMessage("Accounting_system_config_is_not_compelete", organ));
		return YesNoEnum.getName(new Integer(validateHesabMoeenHasChild)).equals(YesNoEnum.YES);
	}
	
	public Boolean checkIfMustValidateHesabMoeenHasMarkaz(OrganEntity organ){
		String validateHesabMoeenHasMarkaz = getSystemConfigService().getValue(organ, null, "validateHesabMoeenHasMarkaz");
		if(validateHesabMoeenHasMarkaz == null)
			throw new FatalException("validateHesabMoeenHasMarkaz is null");
		return YesNoEnum.getName(new Integer(validateHesabMoeenHasMarkaz)).equals(YesNoEnum.YES);
	}
	
	public SaalMaaliEntity getCurrentUserActiveSaalMaali(OrganEntity organEntity, UserEntity currentUser) {
		if(organEntity.getId() == null)
			throw new NoOrganFoundException("");
		SaalMaaliEntity currentUserSaalMaaliEntity = getSaalMaaliService().getUserActiveSaalMaali(organEntity, /*getTopOrgan(organEntity),*/ currentUser);
		
		return currentUserSaalMaaliEntity;
	}
	
}
