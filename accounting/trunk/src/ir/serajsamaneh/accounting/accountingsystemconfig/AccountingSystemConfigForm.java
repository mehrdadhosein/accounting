package ir.serajsamaneh.accounting.accountingsystemconfig;

import ir.serajsamaneh.accounting.hesabkol.HesabKolService;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.accounting.sanadtype.SanadTypeEntity;
import ir.serajsamaneh.accounting.sanadtype.SanadTypeService;
import ir.serajsamaneh.core.exception.RequiredFieldNotSetException;
import ir.serajsamaneh.core.security.ActionLogUtil;
import ir.serajsamaneh.core.systemconfig.SystemConfigForm;
import ir.serajsamaneh.core.systemconfig.SystemConfigService;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.core.util.SpringUtils;
import ir.serajsamaneh.enumeration.ActionTypeEnum;
import ir.serajsamaneh.enumeration.YesNoEnum;

import javax.faces.context.FacesContext;

public class AccountingSystemConfigForm extends SystemConfigForm{
	


	SystemConfigService  systemConfigService;
	HesabMoeenTemplateService hesabMoeenTemplateService;
	SanadTypeService sanadTypeService;
	
	SaalMaaliService saalMaaliService;
	
	public SanadTypeService getSanadTypeService() {
		return (SanadTypeService) SpringUtils.getBean("sanadTypeService");
	}

	public SaalMaaliService getSaalMaaliService() {
		return (SaalMaaliService) SpringUtils.getBean("saalMaaliService");
	}

	public void setSaalMaaliService(SaalMaaliService saalMaaliService) {
		this.saalMaaliService = saalMaaliService;
	}

	public HesabKolService getHesabKolService() {
		return (HesabKolService) SpringUtils.getBean("hesabKolService");
	}
	
	public HesabTafsiliTemplateService getHesabTafsiliTemplateService() {
		return (HesabTafsiliTemplateService) SpringUtils.getBean("hesabTafsiliTemplateService");
	}

	public HesabMoeenTemplateService getHesabMoeenTemplateService() {
		return (HesabMoeenTemplateService) SpringUtils.getBean("hesabMoeenTemplateService");
	}
	

	
	public SystemConfigService getSystemConfigService() {
		return (SystemConfigService) SpringUtils.getBean("systemConfigService");
	}

	
	Long defaultSanadTypeId;
	Long hesabSoodVaZyanAnbashtehMoeenId;
	String hesabSoodVaZyanAnbashtehMoeenDesc;
	Long hesabSoodVaZyanAnbashtehTafsiliId;
	String hesabSoodVaZyanAnbashtehTafsiliDesc;
	YesNoEnum validateHesabMoeenHasChild;
	YesNoEnum validateHesabMoeenHasMarkaz;
	Integer maxSanadHesabdariTafsilLevel;
	


	public Integer getMaxSanadHesabdariTafsilLevel() {
		if(maxSanadHesabdariTafsilLevel==null)
			if(getSystemConfigService().getValue(getCurrentOrgan(), null, "maxSanadHesabdariTafsilLevel")!=null){
				maxSanadHesabdariTafsilLevel= new Integer(getSystemConfigService().getValue(getCurrentOrgan(), null, "maxSanadHesabdariTafsilLevel"));
			}else	
				maxSanadHesabdariTafsilLevel = 1;
		return maxSanadHesabdariTafsilLevel;
	}

	public void setMaxSanadHesabdariTafsilLevel(Integer maxSanadHesabdariTafsilLevel) {
		this.maxSanadHesabdariTafsilLevel = maxSanadHesabdariTafsilLevel;
	}

	public YesNoEnum getValidateHesabMoeenHasMarkaz() {
		if(validateHesabMoeenHasMarkaz==null)
			if(getSystemConfigService().getValue(getCurrentOrgan(), null, "validateHesabMoeenHasMarkaz")!=null){
				validateHesabMoeenHasMarkaz= YesNoEnum.getName(new Integer(getSystemConfigService().getValue(getCurrentOrgan(), null, "validateHesabMoeenHasMarkaz")));
		}		
		return validateHesabMoeenHasMarkaz;
	}

	public void setValidateHesabMoeenHasMarkaz(YesNoEnum validateHesabMoeenHasMarkaz) {
		this.validateHesabMoeenHasMarkaz = validateHesabMoeenHasMarkaz;
	}

	public YesNoEnum getValidateHesabMoeenHasChild() {
		if(validateHesabMoeenHasChild==null)
			if(getSystemConfigService().getValue(getCurrentOrgan(), null, "validateHesabMoeenHasChild")!=null){
				validateHesabMoeenHasChild= YesNoEnum.getName(new Integer(getSystemConfigService().getValue(getCurrentOrgan(), null, "validateHesabMoeenHasChild")));
		}			
		return validateHesabMoeenHasChild;
	}

	public void setValidateHesabMoeenHasChild(YesNoEnum validateHesabMoeenHasChild) {
		this.validateHesabMoeenHasChild = validateHesabMoeenHasChild;
	}

	public Long getHesabSoodVaZyanAnbashtehMoeenId() {
		if(hesabSoodVaZyanAnbashtehMoeenId==null && FacesContext.getCurrentInstance().getRenderResponse()){
			if(getSystemConfigService().getValue(getCurrentOrgan(), null, "hesabSoodVaZyanAnbashtehMoeenId")!=null) {
				HesabMoeenTemplateEntity hesabMoeenTemplateEntity = getHesabMoeenTemplateService().load(new Long(getSystemConfigService().getValue(getCurrentOrgan(), null, "hesabSoodVaZyanAnbashtehMoeenId")));
				hesabSoodVaZyanAnbashtehMoeenId = hesabMoeenTemplateEntity.getId();
			}
		}
		return hesabSoodVaZyanAnbashtehMoeenId;
	}

	public String getHesabSoodVaZyanAnbashtehMoeenDesc() {
		if (hesabSoodVaZyanAnbashtehMoeenDesc==null)
			if(getSystemConfigService().getValue(getCurrentOrgan(), null, "hesabSoodVaZyanAnbashtehMoeenId")!=null) {
				HesabMoeenTemplateEntity hesabMoeenTemplateEntity = getHesabMoeenTemplateService().load(new Long(getSystemConfigService().getValue(getCurrentOrgan(), null, "hesabSoodVaZyanAnbashtehMoeenId")));
				hesabSoodVaZyanAnbashtehMoeenDesc = hesabMoeenTemplateEntity.getDesc();
			}
		
		return hesabSoodVaZyanAnbashtehMoeenDesc;
	}
	
	
	public void setHesabSoodVaZyanAnbashtehMoeenId(Long hesabSoodVaZyanAnbashtehMoeenId) {
		this.hesabSoodVaZyanAnbashtehMoeenId = hesabSoodVaZyanAnbashtehMoeenId;
	}
	
	public void setHesabSoodVaZyanAnbashtehMoeenDesc(String hesabSoodVaZyanAnbashtehMoeenDesc) {
		this.hesabSoodVaZyanAnbashtehMoeenDesc = hesabSoodVaZyanAnbashtehMoeenDesc;
	}
	
	public Long getHesabSoodVaZyanAnbashtehTafsiliId() {
		if(hesabSoodVaZyanAnbashtehTafsiliId==null  && FacesContext.getCurrentInstance().getRenderResponse()){
			if(getSystemConfigService().getValue(getCurrentOrgan(), null, "hesabSoodVaZyanAnbashtehTafsiliId")!=null){
				HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity = getHesabTafsiliTemplateService().load(new Long(getSystemConfigService().getValue(getCurrentOrgan(), null, "hesabSoodVaZyanAnbashtehTafsiliId")));
				hesabSoodVaZyanAnbashtehTafsiliId = hesabTafsiliTemplateEntity.getId();
			}
		}
		return hesabSoodVaZyanAnbashtehTafsiliId;
	}

	public String getHesabSoodVaZyanAnbashtehTafsiliDesc() {
		if(hesabSoodVaZyanAnbashtehTafsiliDesc==null){
			if(getSystemConfigService().getValue(getCurrentOrgan(), null, "hesabSoodVaZyanAnbashtehTafsiliId")!=null){
				HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity = getHesabTafsiliTemplateService().load(new Long(getSystemConfigService().getValue(getCurrentOrgan(), null, "hesabSoodVaZyanAnbashtehTafsiliId")));
				hesabSoodVaZyanAnbashtehTafsiliDesc = hesabTafsiliTemplateEntity.getDesc();
			}
		}
		return hesabSoodVaZyanAnbashtehTafsiliDesc;
	}

	public void setHesabSoodVaZyanAnbashtehTafsiliId(
			Long hesabSoodVaZyanAnbashtehTafsiliId) {
		this.hesabSoodVaZyanAnbashtehTafsiliId = hesabSoodVaZyanAnbashtehTafsiliId;
	}

	public void setHesabSoodVaZyanAnbashtehTafsiliDesc(
			String hesabSoodVaZyanAnbashtehTafsiliDesc) {
		this.hesabSoodVaZyanAnbashtehTafsiliDesc = hesabSoodVaZyanAnbashtehTafsiliDesc;
	}
	public void setDefaultSanadTypeId(Long defaultSanadTypeId) {
		this.defaultSanadTypeId = defaultSanadTypeId;
	}
	public Long getDefaultSanadTypeId() {
		if(defaultSanadTypeId==null)
			if(getSystemConfigService().getValue(getCurrentOrgan(), null, "defaultSanadTypeId")!=null){
				defaultSanadTypeId= new Long(getSystemConfigService().getValue(getCurrentOrgan(), null, "defaultSanadTypeId"));
		}		
		return defaultSanadTypeId;
	}
	

	public void itemdesc(String newvalue) {
		String entityName = SerajMessageUtil.getMessage(getEntityName() + "_title");
		ActionLogUtil.logAction(
				SerajMessageUtil.getMessage(ActionTypeEnum.CREATE.nameWithClass()),
				entityName, newvalue,"","");
	}
	public void configSave(){
		///Get Old Values
		String differences ="";
		String olddefaultSanadTypeId=getSystemConfigService().getValue(getCurrentOrgan(), null, "defaultSanadTypeId");
		String oldhesabSoodVaZyanAnbashtehMoeenId=getSystemConfigService().getValue(getCurrentOrgan(), null, "hesabSoodVaZyanAnbashtehMoeenId");
		String oldhesabSoodVaZyanAnbashtehTafsiliId=getSystemConfigService().getValue(getCurrentOrgan(), null, "hesabSoodVaZyanAnbashtehTafsiliId");
		String oldvalidateHesabMoeenHasChild=getSystemConfigService().getValue(getCurrentOrgan(), null, "validateHesabMoeenHasChild");
		String oldvalidateHesabMoeenHasMarkaz=getSystemConfigService().getValue(getCurrentOrgan(), null, "validateHesabMoeenHasMarkaz");
		String oldmaxSanadHesabdariTafsilLevel=getSystemConfigService().getValue(getCurrentOrgan(), null, "maxSanadHesabdariTafsilLevel");
		//Insert New Value
		getSystemConfigService().insertKeyValue("defaultSanadTypeId",getDefaultSanadTypeId().toString(), null, getCurrentOrgan());
		SanadTypeEntity defaultSanadTypeIdValue = getSanadTypeService().load(getDefaultSanadTypeId());
		itemdesc(defaultSanadTypeIdValue.toString());
		
		getSystemConfigService().insertKeyValue("hesabSoodVaZyanAnbashtehMoeenId",  getHesabSoodVaZyanAnbashtehMoeenId()!=null ? getHesabSoodVaZyanAnbashtehMoeenId().toString() : null, null, getCurrentOrgan());
 
		if(getHesabSoodVaZyanAnbashtehMoeenId()!=null){
			HesabMoeenTemplateEntity hesabSoodVaZyanAnbashtehMoeenIdValue = getHesabMoeenTemplateService().load(getHesabSoodVaZyanAnbashtehMoeenId());
			itemdesc(hesabSoodVaZyanAnbashtehMoeenIdValue.toString());
		}
		
		getSystemConfigService().insertKeyValue("hesabSoodVaZyanAnbashtehTafsiliId",getHesabSoodVaZyanAnbashtehTafsiliId()!=null ? getHesabSoodVaZyanAnbashtehTafsiliId().toString(): null , null, getCurrentOrgan());
		if(getHesabSoodVaZyanAnbashtehTafsiliId()!=null){
			HesabTafsiliTemplateEntity hesabSoodVaZyanAnbashtehTafsiliIdValue = getHesabTafsiliTemplateService().load(getHesabSoodVaZyanAnbashtehTafsiliId());
			itemdesc(hesabSoodVaZyanAnbashtehTafsiliIdValue.toString());
		}
		
		getSystemConfigService().insertKeyValue("validateHesabMoeenHasChild",	getValidateHesabMoeenHasChild().value().toString(), null, getCurrentOrgan());
//		YesNoEnum oldvalidateHesabMoeenHasChildValue = YesNoEnum.getName(new Integer(oldvalidateHesabMoeenHasChild));
//		itemdesc(SerajMessageUtil.getMessage(oldvalidateHesabMoeenHasChildValue.nameWithClass()));
//		
		getSystemConfigService().insertKeyValue("validateHesabMoeenHasMarkaz",	getValidateHesabMoeenHasMarkaz().value().toString(), null, getCurrentOrgan());
		getSystemConfigService().insertKeyValue("maxSanadHesabdariTafsilLevel",	getMaxSanadHesabdariTafsilLevel().toString(), null, getCurrentOrgan());
//		 YesNoEnum oldvalidateHesabMoeenHasMarkazValue = YesNoEnum.getName(new Integer(oldvalidateHesabMoeenHasMarkaz)); 
//		itemdesc(SerajMessageUtil.getMessage(oldvalidateHesabMoeenHasMarkazValue.nameWithClass()));
		
		if(olddefaultSanadTypeId!=null&&!olddefaultSanadTypeId.equals(getDefaultSanadTypeId().toString())){
			HesabMoeenTemplateEntity oldEntity = getHesabMoeenTemplateService().load(new Long(olddefaultSanadTypeId));
			HesabMoeenTemplateEntity newEntity = getHesabMoeenTemplateService().load(getDefaultSanadTypeId());
			differences+="["+SerajMessageUtil.getMessage("AccountingSystemConfig_defaultSanadType")+" : "+oldEntity.getDesc()+"-->"+newEntity.getDesc()+"]";
		}
		
		if(oldhesabSoodVaZyanAnbashtehMoeenId!=null&&!oldhesabSoodVaZyanAnbashtehMoeenId.equals(getHesabSoodVaZyanAnbashtehMoeenId().toString())){
			HesabMoeenTemplateEntity oldEntity = getHesabMoeenTemplateService().load(new Long(oldhesabSoodVaZyanAnbashtehMoeenId));
			HesabMoeenTemplateEntity newEntity = getHesabMoeenTemplateService().load(getHesabSoodVaZyanAnbashtehMoeenId());
			differences+="["+SerajMessageUtil.getMessage("AccountingSystemConfig_hesabSoodVaZyanAnbashtehMoeen")+" : "+oldEntity.getDesc()+"-->"+newEntity.getDesc()+"]";
		}
		
		if(oldhesabSoodVaZyanAnbashtehTafsiliId!=null&&!oldhesabSoodVaZyanAnbashtehTafsiliId.equals(getHesabSoodVaZyanAnbashtehTafsiliId().toString())){
			HesabTafsiliTemplateEntity oldEntity = getHesabTafsiliTemplateService().load(new Long(oldhesabSoodVaZyanAnbashtehTafsiliId));
			HesabTafsiliTemplateEntity newEntity = getHesabTafsiliTemplateService().load(getHesabSoodVaZyanAnbashtehTafsiliId());
			differences+="["+SerajMessageUtil.getMessage("AccountingSystemConfig_hesabSoodVaZyanAnbashtehTafsili")+" : "+oldEntity.getDesc()+"-->"+newEntity.getDesc()+"]";
		}
		if(oldvalidateHesabMoeenHasChild!=null&&!oldvalidateHesabMoeenHasChild.equals(getValidateHesabMoeenHasChild().value().toString())){
			YesNoEnum yesNoEnum = YesNoEnum.getName(new Integer(oldvalidateHesabMoeenHasChild));
			differences+="["+SerajMessageUtil.getMessage("AccountingSystemConfig_validateHesabMoeenHasChild")+" : "+SerajMessageUtil.getMessage(yesNoEnum.nameWithClass())+"-->"+SerajMessageUtil.getMessage(getValidateHesabMoeenHasChild().nameWithClass())+"]";
		}
		if(oldvalidateHesabMoeenHasMarkaz!=null&&!oldvalidateHesabMoeenHasMarkaz.equals(getValidateHesabMoeenHasMarkaz().value().toString())){
			 YesNoEnum yesNoEnum = YesNoEnum.getName(new Integer(oldvalidateHesabMoeenHasMarkaz)); 
			differences+="["+SerajMessageUtil.getMessage("AccountingSystemConfig_validateHesabMoeenHasMarkaz")+" : "+SerajMessageUtil.getMessage(yesNoEnum.nameWithClass())+"-->"+SerajMessageUtil.getMessage(getValidateHesabMoeenHasMarkaz().nameWithClass())+"]";
		}

		if(oldmaxSanadHesabdariTafsilLevel!=null && !oldmaxSanadHesabdariTafsilLevel.equals(getMaxSanadHesabdariTafsilLevel())){
			differences+="["+SerajMessageUtil.getMessage("AccountingSystemConfig_maxSanadHesabdariTafsilLevel")+" : "+oldmaxSanadHesabdariTafsilLevel+"-->"+maxSanadHesabdariTafsilLevel+"]";
		}
		
		addInfoMessage("SUCCESSFUL_ACTION");
		//Log Action
		ActionLogUtil.logAction(SerajMessageUtil.getMessage(ActionTypeEnum.EDIT.nameWithClass()),
				SerajMessageUtil.getMessage("PayrollSystemConfig_configPayroll") , 
				"", 
				"", 
				differences);
	}

	
	

	SaalMaaliEntity srcSaalMaali = new SaalMaaliEntity();
	SaalMaaliEntity destSaalMaali = new SaalMaaliEntity();
	
	public SaalMaaliEntity getSrcSaalMaali() {
		return srcSaalMaali;
	}

	public void setSrcSaalMaali(SaalMaaliEntity srcSaalMaali) {
		this.srcSaalMaali = srcSaalMaali;
	}

	public SaalMaaliEntity getDestSaalMaali() {
		return destSaalMaali;
	}

	public void setDestSaalMaali(SaalMaaliEntity destSaalMaali) {
		this.destSaalMaali = destSaalMaali;
	}

	public void copyHesabsFromSourceSaalMaaliToDestSaalMaali(){
		if(getSrcSaalMaali().getId() == null)
			throw new RequiredFieldNotSetException(SerajMessageUtil.getMessage("AccountingSystemConfig_srcSaalMaali"));

		if(getDestSaalMaali().getId() == null)
			throw new RequiredFieldNotSetException(SerajMessageUtil.getMessage("AccountingSystemConfig_destSaalMaali"));
		
		srcSaalMaali = getSaalMaaliService().load(getSrcSaalMaali().getId());
		destSaalMaali = getSaalMaaliService().load(getDestSaalMaali().getId());
		
		getHesabKolService().copyHesabKolsFromSourceSaalMaaliToDestSaalMaali(getSrcSaalMaali(), getDestSaalMaali(), getCurrentOrgan());
		getHesabKolService().copyHesabMoeensFromSourceSaalMaaliToDestSaalMaali(getSrcSaalMaali(), getDestSaalMaali(), getCurrentOrgan());
		getHesabKolService().copyHesabTafsilissFromSourceSaalMaaliToDestSaalMaali(getSrcSaalMaali(), getDestSaalMaali(), getCurrentOrgan());
		getHesabKolService().createHesabTafsiliRelatedEntities(getSrcSaalMaali(), getDestSaalMaali());
		getHesabKolService().copyAccountingMarkazhaFromSourceSaalMaaliToDestSaalMaali(getSrcSaalMaali(), getDestSaalMaali());
		getHesabKolService().copycontactHesabsFromSourceSaalMaaliToDestSaalMaali(getSrcSaalMaali(), getDestSaalMaali());
		addInfoMessage("SUCCESSFUL_ACTION");
	}

	String saalMaaliIdStr = getSystemConfigService().getValue(getCurrentOrgan(), getCurrentUser(), "saalMaaliId");

	Long saalMaaliId;

	public Long getSaalMaaliId() {
		if(saalMaaliId == null && saalMaaliIdStr!=null  && FacesContext.getCurrentInstance().getRenderResponse())
			saalMaaliId = new Long(saalMaaliIdStr);
		return saalMaaliId;
	}

	public void setSaalMaaliId(Long saalMaaliId) {
		this.saalMaaliId = saalMaaliId;
	}


	public String userConfigSave(){
		SaalMaaliEntity saalMaaliEntity = null;
		if(getSaalMaaliId()!=null)
			saalMaaliEntity = getSaalMaaliService().get(getSaalMaaliId());
		
		getSystemConfigService().insertKeyValue("saalMaaliId", getSaalMaaliId()!=null ? getSaalMaaliId().toString() : null , getCurrentUser(), getCurrentOrgan());
		String entityName = SerajMessageUtil.getMessage(getSaalMaaliService().getEntityName() + "_title");
		ActionLogUtil.logAction(SerajMessageUtil.getMessage(ActionTypeEnum.EDIT.nameWithClass()), entityName, 
				saalMaaliEntity!=null ? saalMaaliEntity.getSaal().toString() : "" , "", saalMaaliEntity!=null ? saalMaaliEntity.getLog() : "", getCurrentUser());
		addInfoMessage("SUCCESSFUL_ACTION");
		setSuccessfullAction(true);
		return null;
	}
}
