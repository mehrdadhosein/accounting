package ir.serajsamaneh.accounting.base;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import ir.serajsamaneh.accounting.hesabkol.HesabKolService;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.accounting.sanadtype.SanadTypeService;
import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.common.OrganVO;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.exception.NoOrganFoundException;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.organ.OrganService;
import ir.serajsamaneh.core.systemconfig.SystemConfigService;
import ir.serajsamaneh.core.user.UserService;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.enumeration.YesNoEnum;

public abstract class BaseAccountingService <T extends BaseEntity<U>, U extends Serializable> extends BaseEntityService<T, U>{
	
	@Autowired
	protected UserService userService;
	@Autowired
	protected OrganService organService;
	
	SaalMaaliService saalMaaliService;
	SanadTypeService sanadTypeService;
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


	public SaalMaaliEntity getSaalmaaliByDate(Date date, OrganVO organEntity) {
		return getSaalMaaliService().getSaalmaaliByDate(date, organEntity);
	}
	
	public SaalMaaliEntity getActiveSaalmaali(OrganVO organEntity) {
		return getSaalMaaliService().getActiveSaalmaali(organEntity);
	}
	
	public boolean checkIfMustValidateTafsiliOneAndTwoAreRelated(Long organId) {
		return false;
	}
	
	public Boolean checkIfMustValidateHesabTafsiliHasChild(OrganVO organ){
		String validateHesabTafsiliHasChild = systemConfigService.getValue(organ.getId(), null, "validateHesabTafsiliHasChild");
		if(validateHesabTafsiliHasChild == null)
			throw new FatalException(SerajMessageUtil.getMessage("Accounting_system_config_is_not_compelete", organ.getDesc()));
		return YesNoEnum.getName(new Integer(validateHesabTafsiliHasChild)).equals(YesNoEnum.YES);
	}
	
	public Boolean checkIfMustValidateHesabMoeenHasChild(OrganVO organ){
		String validateHesabMoeenHasChild = systemConfigService.getValue(organ.getId(), null, "validateHesabMoeenHasChild");
		if(validateHesabMoeenHasChild == null)
			throw new FatalException(SerajMessageUtil.getMessage("Accounting_system_config_is_not_compelete", organ));
		return YesNoEnum.getName(new Integer(validateHesabMoeenHasChild)).equals(YesNoEnum.YES);
	}
	
	public Boolean checkIfMustValidateHesabMoeenHasMarkaz(OrganVO organ){
		String validateHesabMoeenHasMarkaz = systemConfigService.getValue(organ.getId(), null, "validateHesabMoeenHasMarkaz");
		if(validateHesabMoeenHasMarkaz == null)
			throw new FatalException("validateHesabMoeenHasMarkaz is null");
		return YesNoEnum.getName(new Integer(validateHesabMoeenHasMarkaz)).equals(YesNoEnum.YES);
	}
	
	public SaalMaaliEntity getCurrentUserActiveSaalMaali(Long organId, Long userId) {
		if(organId == null)
			throw new NoOrganFoundException("");
		OrganEntity organEntity = organService.load(organId);
		OrganVO organVO = organService.getOrganVO(organEntity);
		SaalMaaliEntity currentUserSaalMaaliEntity = getSaalMaaliService().getUserActiveSaalMaali(organVO, /*getTopOrgan(organEntity),*/ userId);
		
		return currentUserSaalMaaliEntity;
	}
	
	public SaalMaaliEntity getCurrentUserActiveSaalMaali(OrganVO organVO, Long userId) {
		if(organVO == null)
			throw new NoOrganFoundException("");
		SaalMaaliEntity currentUserSaalMaaliEntity = getSaalMaaliService().getUserActiveSaalMaali(organVO, /*getTopOrgan(organEntity),*/ userId);
		
		return currentUserSaalMaaliEntity;
	}

}
