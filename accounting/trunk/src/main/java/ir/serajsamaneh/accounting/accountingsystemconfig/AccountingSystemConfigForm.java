package ir.serajsamaneh.accounting.accountingsystemconfig;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import ir.serajsamaneh.accounting.enumeration.HesabMoeenCodingTypeEnum;
import ir.serajsamaneh.accounting.enumeration.HesabTafsiliCodingTypeEnum;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.accounting.sanadtype.SanadTypeEntity;
import ir.serajsamaneh.accounting.sanadtype.SanadTypeService;
import ir.serajsamaneh.core.systemconfig.SystemConfigForm;
import ir.serajsamaneh.core.systemconfig.SystemConfigService;
import ir.serajsamaneh.core.util.ActionLogUtil;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.enumeration.ActionTypeEnum;
import ir.serajsamaneh.enumeration.YesNoEnum;

public class AccountingSystemConfigForm extends SystemConfigForm{
	

	@Autowired
	SystemConfigService  systemConfigService;
	
	@Autowired
	HesabMoeenTemplateService hesabMoeenTemplateService;
	
	@Autowired
	SanadTypeService sanadTypeService;
	
	@Autowired
	SaalMaaliService saalMaaliService;
	
	@Autowired
	HesabTafsiliTemplateService hesabTafsiliTemplateService;
	
	Long defaultSanadTypeId;
	Long hesabSoodVaZyanAnbashtehMoeenId;
	String hesabSoodVaZyanAnbashtehMoeenDesc;
	Long hesabSoodVaZyanAnbashtehTafsiliId;
	String hesabSoodVaZyanAnbashtehTafsiliDesc;
	YesNoEnum validateHesabTafsiliHasChild;
	YesNoEnum validateHesabMoeenHasChild;
	YesNoEnum validateHesabMoeenHasMarkaz;
	Integer maxSanadHesabdariTafsilLevel;
	Integer hesabTafsiliCodeCharactersNumber;
	Integer numberOfDecimalsConfig;
	HesabTafsiliCodingTypeEnum hesabTafsiliCodingType;
	
	Integer hesabMoeenCodeCharactersNumber;
	HesabMoeenCodingTypeEnum hesabMoeenCodingType;
	


	public HesabMoeenCodingTypeEnum getHesabMoeenCodingType() {
		if (hesabMoeenCodingType == null && FacesContext.getCurrentInstance().getRenderResponse())
			if (getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "HesabMoeenCodingType") != null) {
				if (getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "HesabMoeenCodingType").equals(
						"MANUAL")) {
					setHesabMoeenCodingType(HesabMoeenCodingTypeEnum.MANUAL);
				} else if (getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "HesabMoeenCodingType")
						.equals("SERIAL")) {
					setHesabMoeenCodingType(HesabMoeenCodingTypeEnum.SERIAL);
				} else if (getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "HesabMoeenCodingType")
						.equals("CONSTANT_HIERARCHICAL")) {
					setHesabMoeenCodingType(HesabMoeenCodingTypeEnum.CONSTANT_HIERARCHICAL);
				} else if (getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "HesabMoeenCodingType")
						.equals("VARIABLE_HIERARCHICAL")) {
					setHesabMoeenCodingType(HesabMoeenCodingTypeEnum.VARIABLE_HIERARCHICAL);
				}
			}		
		return hesabMoeenCodingType;
	}
	
	public void setHesabMoeenCodingType(HesabMoeenCodingTypeEnum hesabMoeenCodingType) {
		this.hesabMoeenCodingType = hesabMoeenCodingType;
	}
	
	public Integer getHesabMoeenCodeCharactersNumber() {
		if (hesabMoeenCodeCharactersNumber == null && FacesContext.getCurrentInstance().getRenderResponse())
			if (getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "hesabMoeenCodeCharactersNumber") != null) {
				hesabMoeenCodeCharactersNumber = Integer
						.parseInt(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "hesabMoeenCodeCharactersNumber"));
			}		
		return hesabMoeenCodeCharactersNumber;
	}
	
	public void setHesabMoeenCodeCharactersNumber(Integer hesabMoeenCodeCharactersNumber) {
		this.hesabMoeenCodeCharactersNumber = hesabMoeenCodeCharactersNumber;
	}
	public HesabTafsiliCodingTypeEnum getHesabTafsiliCodingType() {
		if (hesabTafsiliCodingType == null && FacesContext.getCurrentInstance().getRenderResponse())
			if (getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "HesabTafsiliCodingType") != null) {
				if (getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "HesabTafsiliCodingType").equals(
						"MANUAL")) {
					setHesabTafsiliCodingType(HesabTafsiliCodingTypeEnum.MANUAL);
				} else if (getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "HesabTafsiliCodingType")
						.equals("SERIAL")) {
					setHesabTafsiliCodingType(HesabTafsiliCodingTypeEnum.SERIAL);
				} else if (getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "HesabTafsiliCodingType")
						.equals("CONSTANT_HIERARCHICAL")) {
					setHesabTafsiliCodingType(HesabTafsiliCodingTypeEnum.CONSTANT_HIERARCHICAL);
				} else if (getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "HesabTafsiliCodingType")
						.equals("VARIABLE_HIERARCHICAL")) {
					setHesabTafsiliCodingType(HesabTafsiliCodingTypeEnum.VARIABLE_HIERARCHICAL);
				}
			}		
		return hesabTafsiliCodingType;
	}

	public void setHesabTafsiliCodingType(HesabTafsiliCodingTypeEnum hesabTafsiliCodingType) {
		this.hesabTafsiliCodingType = hesabTafsiliCodingType;
	}

	public Integer getHesabTafsiliCodeCharactersNumber() {
		if (hesabTafsiliCodeCharactersNumber == null && FacesContext.getCurrentInstance().getRenderResponse())
			if (getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "hesabTafsiliCodeCharactersNumber") != null) {
				hesabTafsiliCodeCharactersNumber = Integer
						.parseInt(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "hesabTafsiliCodeCharactersNumber"));
			}		
		return hesabTafsiliCodeCharactersNumber;
	}

	public void setHesabTafsiliCodeCharactersNumber(Integer hesabTafsiliCodeCharactersNumber) {
		this.hesabTafsiliCodeCharactersNumber = hesabTafsiliCodeCharactersNumber;
	}

	public Integer getNumberOfDecimalsConfig() {
		if(numberOfDecimalsConfig==null)
			if(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "numberOfDecimals")!=null){
				numberOfDecimalsConfig= new Integer(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "numberOfDecimals"));
			}else	
				numberOfDecimalsConfig = 0;		
		return numberOfDecimalsConfig;
	}

	public void setNumberOfDecimalsConfig(Integer numberOfDecimalsConfig) {
		this.numberOfDecimalsConfig = numberOfDecimalsConfig;
	}

	public Integer getMaxSanadHesabdariTafsilLevel() {
		if(maxSanadHesabdariTafsilLevel==null)
			if(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "maxSanadHesabdariTafsilLevel")!=null){
				maxSanadHesabdariTafsilLevel= new Integer(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "maxSanadHesabdariTafsilLevel"));
			}else	
				maxSanadHesabdariTafsilLevel = 1;
		return maxSanadHesabdariTafsilLevel;
	}

	public void setMaxSanadHesabdariTafsilLevel(Integer maxSanadHesabdariTafsilLevel) {
		this.maxSanadHesabdariTafsilLevel = maxSanadHesabdariTafsilLevel;
	}

	public YesNoEnum getValidateHesabMoeenHasMarkaz() {
		if(validateHesabMoeenHasMarkaz==null)
			if(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "validateHesabMoeenHasMarkaz")!=null){
				validateHesabMoeenHasMarkaz= YesNoEnum.getName(new Integer(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "validateHesabMoeenHasMarkaz")));
		}		
		return validateHesabMoeenHasMarkaz;
	}

	public void setValidateHesabMoeenHasMarkaz(YesNoEnum validateHesabMoeenHasMarkaz) {
		this.validateHesabMoeenHasMarkaz = validateHesabMoeenHasMarkaz;
	}

	public YesNoEnum getValidateHesabMoeenHasChild() {
		if(validateHesabMoeenHasChild==null)
			if(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "validateHesabMoeenHasChild")!=null){
				validateHesabMoeenHasChild= YesNoEnum.getName(new Integer(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "validateHesabMoeenHasChild")));
		}			
		return validateHesabMoeenHasChild;
	}

	public void setValidateHesabMoeenHasChild(YesNoEnum validateHesabMoeenHasChild) {
		this.validateHesabMoeenHasChild = validateHesabMoeenHasChild;
	}

	public YesNoEnum getValidateHesabTafsiliHasChild() {
		if(validateHesabTafsiliHasChild==null)
			if(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "validateHesabTafsiliHasChild")!=null){
				validateHesabTafsiliHasChild= YesNoEnum.getName(new Integer(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "validateHesabTafsiliHasChild")));
		}			

		return validateHesabTafsiliHasChild;
	}

	public void setValidateHesabTafsiliHasChild(YesNoEnum validateHesabTafsiliHasChild) {
		this.validateHesabTafsiliHasChild = validateHesabTafsiliHasChild;
	}

	public Long getHesabSoodVaZyanAnbashtehMoeenId() {
		if(hesabSoodVaZyanAnbashtehMoeenId==null && FacesContext.getCurrentInstance().getRenderResponse()){
			if(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "hesabSoodVaZyanAnbashtehMoeenId")!=null) {
				HesabMoeenTemplateEntity hesabMoeenTemplateEntity = hesabMoeenTemplateService.load(new Long(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "hesabSoodVaZyanAnbashtehMoeenId")));
				hesabSoodVaZyanAnbashtehMoeenId = hesabMoeenTemplateEntity.getId();
			}
		}
		return hesabSoodVaZyanAnbashtehMoeenId;
	}

	public String getHesabSoodVaZyanAnbashtehMoeenDesc() {
		if (hesabSoodVaZyanAnbashtehMoeenDesc==null)
			if(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "hesabSoodVaZyanAnbashtehMoeenId")!=null) {
				HesabMoeenTemplateEntity hesabMoeenTemplateEntity = hesabMoeenTemplateService.load(new Long(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "hesabSoodVaZyanAnbashtehMoeenId")));
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
			if(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "hesabSoodVaZyanAnbashtehTafsiliId")!=null){
				HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity = hesabTafsiliTemplateService.load(new Long(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "hesabSoodVaZyanAnbashtehTafsiliId")));
				hesabSoodVaZyanAnbashtehTafsiliId = hesabTafsiliTemplateEntity.getId();
			}
		}
		return hesabSoodVaZyanAnbashtehTafsiliId;
	}

	public String getHesabSoodVaZyanAnbashtehTafsiliDesc() {
		if(hesabSoodVaZyanAnbashtehTafsiliDesc==null){
			if(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "hesabSoodVaZyanAnbashtehTafsiliId")!=null){
				HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity = hesabTafsiliTemplateService.load(new Long(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "hesabSoodVaZyanAnbashtehTafsiliId")));
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
			if(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "defaultSanadTypeId")!=null){
				defaultSanadTypeId= new Long(getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "defaultSanadTypeId"));
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
		String olddefaultSanadTypeId=getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "defaultSanadTypeId");
		String oldhesabSoodVaZyanAnbashtehMoeenId=getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "hesabSoodVaZyanAnbashtehMoeenId");
		String oldhesabSoodVaZyanAnbashtehTafsiliId=getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "hesabSoodVaZyanAnbashtehTafsiliId");
		String oldvalidateHesabMoeenHasChild=getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "validateHesabMoeenHasChild");
		String oldvalidateHesabTafsiliHasChild=getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "validateHesabTafsiliHasChild");
		String oldvalidateHesabMoeenHasMarkaz=getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "validateHesabMoeenHasMarkaz");
		String oldmaxSanadHesabdariTafsilLevel=getSystemConfigService().getValue(getCurrentOrganVO().getId(), null, "maxSanadHesabdariTafsilLevel");
		//Insert New Value
		getSystemConfigService().insertKeyValue("defaultSanadTypeId",getDefaultSanadTypeId().toString(), null, getCurrentOrganVO().getId(),getCurrentUserVO().getDesc());
		SanadTypeEntity defaultSanadTypeIdValue = sanadTypeService.load(getDefaultSanadTypeId());
		itemdesc(defaultSanadTypeIdValue.toString());
		
		getSystemConfigService().insertKeyValue("hesabSoodVaZyanAnbashtehMoeenId",  getHesabSoodVaZyanAnbashtehMoeenId()!=null ? getHesabSoodVaZyanAnbashtehMoeenId().toString() : null, null, getCurrentOrganVO().getId(),getCurrentUserVO().getDesc());
 
		if(getHesabSoodVaZyanAnbashtehMoeenId()!=null){
			HesabMoeenTemplateEntity hesabSoodVaZyanAnbashtehMoeenIdValue = hesabMoeenTemplateService.load(getHesabSoodVaZyanAnbashtehMoeenId());
			itemdesc(hesabSoodVaZyanAnbashtehMoeenIdValue.toString());
		}
		
		getSystemConfigService().insertKeyValue("hesabSoodVaZyanAnbashtehTafsiliId",getHesabSoodVaZyanAnbashtehTafsiliId()!=null ? getHesabSoodVaZyanAnbashtehTafsiliId().toString(): null , null, getCurrentOrganVO().getId(),getCurrentUserVO().getDesc());
		if(getHesabSoodVaZyanAnbashtehTafsiliId()!=null){
			HesabTafsiliTemplateEntity hesabSoodVaZyanAnbashtehTafsiliIdValue = hesabTafsiliTemplateService.load(getHesabSoodVaZyanAnbashtehTafsiliId());
			itemdesc(hesabSoodVaZyanAnbashtehTafsiliIdValue.toString());
		}
		
		
		if(getHesabTafsiliCodeCharactersNumber()!=null)
			getSystemConfigService().insertKeyValue("hesabTafsiliCodeCharactersNumber",	getHesabTafsiliCodeCharactersNumber().toString(), null, getCurrentOrganVO().getId(),getCurrentUserVO().getDesc());
	
		getSystemConfigService().insertKeyValue("HesabTafsiliCodingType",	getHesabTafsiliCodingType().toString(), null, getCurrentOrganVO().getId(),getCurrentUserVO().getDesc());

		if(getHesabMoeenCodeCharactersNumber()!=null)
			getSystemConfigService().insertKeyValue("hesabMoeenCodeCharactersNumber",	getHesabMoeenCodeCharactersNumber().toString(), null, getCurrentOrganVO().getId(),getCurrentUserVO().getDesc());
		
		getSystemConfigService().insertKeyValue("HesabMoeenCodingType",	getHesabMoeenCodingType().toString(), null, getCurrentOrganVO().getId(),getCurrentUserVO().getDesc());
		
		
		getSystemConfigService().insertKeyValue("validateHesabMoeenHasChild",	getValidateHesabMoeenHasChild().value().toString(), null, getCurrentOrganVO().getId(),getCurrentUserVO().getDesc());
		getSystemConfigService().insertKeyValue("validateHesabTafsiliHasChild",	getValidateHesabTafsiliHasChild().value().toString(), null, getCurrentOrganVO().getId(),getCurrentUserVO().getDesc());
//		YesNoEnum oldvalidateHesabMoeenHasChildValue = YesNoEnum.getName(new Integer(oldvalidateHesabMoeenHasChild));
//		itemdesc(SerajMessageUtil.getMessage(oldvalidateHesabMoeenHasChildValue.nameWithClass()));
//		
		getSystemConfigService().insertKeyValue("validateHesabMoeenHasMarkaz",	getValidateHesabMoeenHasMarkaz().value().toString(), null, getCurrentOrganVO().getId(),getCurrentUserVO().getDesc());
		getSystemConfigService().insertKeyValue("maxSanadHesabdariTafsilLevel",	getMaxSanadHesabdariTafsilLevel().toString(), null, getCurrentOrganVO().getId(),getCurrentUserVO().getDesc());
		getSystemConfigService().insertKeyValue("numberOfDecimals",	getNumberOfDecimalsConfig().toString(), null, getCurrentOrganVO().getId(),getCurrentUserVO().getDesc());
//		 YesNoEnum oldvalidateHesabMoeenHasMarkazValue = YesNoEnum.getName(new Integer(oldvalidateHesabMoeenHasMarkaz)); 
//		itemdesc(SerajMessageUtil.getMessage(oldvalidateHesabMoeenHasMarkazValue.nameWithClass()));
		
		if(olddefaultSanadTypeId!=null&&!olddefaultSanadTypeId.equals(getDefaultSanadTypeId().toString())){
			SanadTypeEntity oldEntity = sanadTypeService.load(new Long(olddefaultSanadTypeId));
			SanadTypeEntity newEntity = sanadTypeService.load(getDefaultSanadTypeId());
			differences+="["+SerajMessageUtil.getMessage("AccountingSystemConfig_defaultSanadType")+" : "+oldEntity.getDesc()+"-->"+newEntity.getDesc()+"]";
		}
		
		if(oldhesabSoodVaZyanAnbashtehMoeenId!=null&&!oldhesabSoodVaZyanAnbashtehMoeenId.equals(getHesabSoodVaZyanAnbashtehMoeenId().toString())){
			HesabMoeenTemplateEntity oldEntity = hesabMoeenTemplateService.load(new Long(oldhesabSoodVaZyanAnbashtehMoeenId));
			HesabMoeenTemplateEntity newEntity = hesabMoeenTemplateService.load(getHesabSoodVaZyanAnbashtehMoeenId());
			differences+="["+SerajMessageUtil.getMessage("AccountingSystemConfig_hesabSoodVaZyanAnbashtehMoeen")+" : "+oldEntity.getDesc()+"-->"+newEntity.getDesc()+"]";
		}
		
		if(oldhesabSoodVaZyanAnbashtehTafsiliId!=null&&!oldhesabSoodVaZyanAnbashtehTafsiliId.equals(getHesabSoodVaZyanAnbashtehTafsiliId().toString())){
			HesabTafsiliTemplateEntity oldEntity = hesabTafsiliTemplateService.load(new Long(oldhesabSoodVaZyanAnbashtehTafsiliId));
			HesabTafsiliTemplateEntity newEntity = hesabTafsiliTemplateService.load(getHesabSoodVaZyanAnbashtehTafsiliId());
			differences+="["+SerajMessageUtil.getMessage("AccountingSystemConfig_hesabSoodVaZyanAnbashtehTafsili")+" : "+oldEntity.getDesc()+"-->"+newEntity.getDesc()+"]";
		}
		if(oldvalidateHesabMoeenHasChild!=null&&!oldvalidateHesabMoeenHasChild.equals(getValidateHesabMoeenHasChild().value().toString())){
			YesNoEnum yesNoEnum = YesNoEnum.getName(new Integer(oldvalidateHesabMoeenHasChild));
			differences+="["+SerajMessageUtil.getMessage("AccountingSystemConfig_validateHesabMoeenHasChild")+" : "+SerajMessageUtil.getMessage(yesNoEnum.nameWithClass())+"-->"+SerajMessageUtil.getMessage(getValidateHesabMoeenHasChild().nameWithClass())+"]";
		}
		if(oldvalidateHesabTafsiliHasChild!=null&&!oldvalidateHesabTafsiliHasChild.equals(getValidateHesabTafsiliHasChild().value().toString())){
			YesNoEnum yesNoEnum = YesNoEnum.getName(new Integer(oldvalidateHesabMoeenHasChild));
			differences+="["+SerajMessageUtil.getMessage("AccountingSystemConfig_validateHesabTafsiliHasChild")+" : "+SerajMessageUtil.getMessage(yesNoEnum.nameWithClass())+"-->"+SerajMessageUtil.getMessage(getValidateHesabTafsiliHasChild().nameWithClass())+"]";
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

	
	

//	SaalMaaliEntity destSaalMaali = new SaalMaaliEntity();
//
//	public SaalMaaliEntity getDestSaalMaali() {
//		return destSaalMaali;
//	}
//
//	public void setDestSaalMaali(SaalMaaliEntity destSaalMaali) {
//		this.destSaalMaali = destSaalMaali;
//	}
//

	String saalMaaliIdStr = getSystemConfigService().getValue(getCurrentOrganVO().getId(), getCurrentUserVO().getId(), "saalMaaliId");

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
			saalMaaliEntity = saalMaaliService.get(getSaalMaaliId());
		
		getSystemConfigService().insertKeyValue("saalMaaliId", getSaalMaaliId()!=null ? getSaalMaaliId().toString() : null , getCurrentUserVO().getId(), getCurrentOrganVO().getId(), getCurrentUserVO().getDesc());
		String entityName = SerajMessageUtil.getMessage(saalMaaliService.getEntityName() + "_title");
		ActionLogUtil.logAction(SerajMessageUtil.getMessage(ActionTypeEnum.EDIT.nameWithClass()), entityName, 
				saalMaaliEntity!=null ? saalMaaliEntity.getSaal().toString() : "" , "", saalMaaliEntity!=null ? saalMaaliEntity.getLog() : "", getCurrentUserVO());
		addInfoMessage("SUCCESSFUL_ACTION");
		setSuccessfullAction(true);
		return null;
	}
}
