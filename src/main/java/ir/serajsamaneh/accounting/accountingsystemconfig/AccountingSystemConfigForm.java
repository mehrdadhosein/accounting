package ir.serajsamaneh.accounting.accountingsystemconfig;

import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

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
import ir.serajsamaneh.core.util.ActionLogUtil;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.enumeration.ActionTypeEnum;
import ir.serajsamaneh.enumeration.YesNoEnum;
@Named("accountingSystemConfig")
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class AccountingSystemConfigForm extends SystemConfigForm {

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
			if (systemConfigService.getValue(getCurrentOrganVO().getId(), null, "HesabMoeenCodingType") != null) {
				if (systemConfigService.getValue(getCurrentOrganVO().getId(), null, "HesabMoeenCodingType")
						.equals("MANUAL")) {
					setHesabMoeenCodingType(HesabMoeenCodingTypeEnum.MANUAL);
				} else if (systemConfigService.getValue(getCurrentOrganVO().getId(), null, "HesabMoeenCodingType")
						.equals("SERIAL")) {
					setHesabMoeenCodingType(HesabMoeenCodingTypeEnum.SERIAL);
				} else if (systemConfigService.getValue(getCurrentOrganVO().getId(), null, "HesabMoeenCodingType")
						.equals("CONSTANT_HIERARCHICAL")) {
					setHesabMoeenCodingType(HesabMoeenCodingTypeEnum.CONSTANT_HIERARCHICAL);
				} else if (systemConfigService.getValue(getCurrentOrganVO().getId(), null, "HesabMoeenCodingType")
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
			if (systemConfigService.getValue(getCurrentOrganVO().getId(), null,
					"hesabMoeenCodeCharactersNumber") != null) {
				hesabMoeenCodeCharactersNumber = Integer.parseInt(systemConfigService
						.getValue(getCurrentOrganVO().getId(), null, "hesabMoeenCodeCharactersNumber"));
			}
		return hesabMoeenCodeCharactersNumber;
	}

	public void setHesabMoeenCodeCharactersNumber(Integer hesabMoeenCodeCharactersNumber) {
		this.hesabMoeenCodeCharactersNumber = hesabMoeenCodeCharactersNumber;
	}

	public HesabTafsiliCodingTypeEnum getHesabTafsiliCodingType() {
		if (hesabTafsiliCodingType == null && FacesContext.getCurrentInstance().getRenderResponse())
			if (systemConfigService.getValue(getCurrentOrganVO().getId(), null, "HesabTafsiliCodingType") != null) {
				if (systemConfigService.getValue(getCurrentOrganVO().getId(), null, "HesabTafsiliCodingType")
						.equals("MANUAL")) {
					setHesabTafsiliCodingType(HesabTafsiliCodingTypeEnum.MANUAL);
				} else if (systemConfigService.getValue(getCurrentOrganVO().getId(), null, "HesabTafsiliCodingType")
						.equals("SERIAL")) {
					setHesabTafsiliCodingType(HesabTafsiliCodingTypeEnum.SERIAL);
				} else if (systemConfigService.getValue(getCurrentOrganVO().getId(), null, "HesabTafsiliCodingType")
						.equals("CONSTANT_HIERARCHICAL")) {
					setHesabTafsiliCodingType(HesabTafsiliCodingTypeEnum.CONSTANT_HIERARCHICAL);
				} else if (systemConfigService.getValue(getCurrentOrganVO().getId(), null, "HesabTafsiliCodingType")
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
			if (systemConfigService.getValue(getCurrentOrganVO().getId(), null,
					"hesabTafsiliCodeCharactersNumber") != null) {
				hesabTafsiliCodeCharactersNumber = Integer.parseInt(systemConfigService
						.getValue(getCurrentOrganVO().getId(), null, "hesabTafsiliCodeCharactersNumber"));
			}
		return hesabTafsiliCodeCharactersNumber;
	}

	public void setHesabTafsiliCodeCharactersNumber(Integer hesabTafsiliCodeCharactersNumber) {
		this.hesabTafsiliCodeCharactersNumber = hesabTafsiliCodeCharactersNumber;
	}

	public Integer getNumberOfDecimalsConfig() {
		if (numberOfDecimalsConfig == null)
			if (systemConfigService.getValue(getCurrentOrganVO().getId(), null, "numberOfDecimals") != null) {
				numberOfDecimalsConfig = Integer
						.valueOf(systemConfigService.getValue(getCurrentOrganVO().getId(), null, "numberOfDecimals"));
			} else
				numberOfDecimalsConfig = 0;
		return numberOfDecimalsConfig;
	}

	public void setNumberOfDecimalsConfig(Integer numberOfDecimalsConfig) {
		this.numberOfDecimalsConfig = numberOfDecimalsConfig;
	}

	public Integer getMaxSanadHesabdariTafsilLevel() {
		if (maxSanadHesabdariTafsilLevel == null)
			if (systemConfigService.getValue(getCurrentOrganVO().getId(), null,
					"maxSanadHesabdariTafsilLevel") != null) {
				maxSanadHesabdariTafsilLevel = Integer.valueOf(systemConfigService.getValue(getCurrentOrganVO().getId(),
						null, "maxSanadHesabdariTafsilLevel"));
			} else
				maxSanadHesabdariTafsilLevel = 1;
		return maxSanadHesabdariTafsilLevel;
	}

	public void setMaxSanadHesabdariTafsilLevel(Integer maxSanadHesabdariTafsilLevel) {
		this.maxSanadHesabdariTafsilLevel = maxSanadHesabdariTafsilLevel;
	}

	public YesNoEnum getValidateHesabMoeenHasMarkaz() {
		if (validateHesabMoeenHasMarkaz == null)
			if (systemConfigService.getValue(getCurrentOrganVO().getId(), null,
					"validateHesabMoeenHasMarkaz") != null) {
				validateHesabMoeenHasMarkaz = YesNoEnum.getName(Integer.valueOf(systemConfigService
						.getValue(getCurrentOrganVO().getId(), null, "validateHesabMoeenHasMarkaz")));
			}
		return validateHesabMoeenHasMarkaz;
	}

	public void setValidateHesabMoeenHasMarkaz(YesNoEnum validateHesabMoeenHasMarkaz) {
		this.validateHesabMoeenHasMarkaz = validateHesabMoeenHasMarkaz;
	}

	public YesNoEnum getValidateHesabMoeenHasChild() {
		if (validateHesabMoeenHasChild == null)
			if (systemConfigService.getValue(getCurrentOrganVO().getId(), null, "validateHesabMoeenHasChild") != null) {
				validateHesabMoeenHasChild = YesNoEnum.getName(Integer.valueOf(
						systemConfigService.getValue(getCurrentOrganVO().getId(), null, "validateHesabMoeenHasChild")));
			}
		return validateHesabMoeenHasChild;
	}

	public void setValidateHesabMoeenHasChild(YesNoEnum validateHesabMoeenHasChild) {
		this.validateHesabMoeenHasChild = validateHesabMoeenHasChild;
	}

	public YesNoEnum getValidateHesabTafsiliHasChild() {
		if (validateHesabTafsiliHasChild == null)
			if (systemConfigService.getValue(getCurrentOrganVO().getId(), null,
					"validateHesabTafsiliHasChild") != null) {
				validateHesabTafsiliHasChild = YesNoEnum.getName(Integer.valueOf(systemConfigService
						.getValue(getCurrentOrganVO().getId(), null, "validateHesabTafsiliHasChild")));
			}

		return validateHesabTafsiliHasChild;
	}

	public void setValidateHesabTafsiliHasChild(YesNoEnum validateHesabTafsiliHasChild) {
		this.validateHesabTafsiliHasChild = validateHesabTafsiliHasChild;
	}

	public Long getHesabSoodVaZyanAnbashtehMoeenId() {
		if (hesabSoodVaZyanAnbashtehMoeenId == null && FacesContext.getCurrentInstance().getRenderResponse()) {
			if (systemConfigService.getValue(getCurrentOrganVO().getId(), null,
					"hesabSoodVaZyanAnbashtehMoeenId") != null) {
				HesabMoeenTemplateEntity hesabMoeenTemplateEntity = hesabMoeenTemplateService
						.load(Long.valueOf(systemConfigService.getValue(getCurrentOrganVO().getId(), null,
								"hesabSoodVaZyanAnbashtehMoeenId")));
				hesabSoodVaZyanAnbashtehMoeenId = hesabMoeenTemplateEntity.getId();
			}
		}
		return hesabSoodVaZyanAnbashtehMoeenId;
	}

	public String getHesabSoodVaZyanAnbashtehMoeenDesc() {
		if (hesabSoodVaZyanAnbashtehMoeenDesc == null)
			if (systemConfigService.getValue(getCurrentOrganVO().getId(), null,
					"hesabSoodVaZyanAnbashtehMoeenId") != null) {
				HesabMoeenTemplateEntity hesabMoeenTemplateEntity = hesabMoeenTemplateService
						.load(Long.valueOf(systemConfigService.getValue(getCurrentOrganVO().getId(), null,
								"hesabSoodVaZyanAnbashtehMoeenId")));
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
		if (hesabSoodVaZyanAnbashtehTafsiliId == null && FacesContext.getCurrentInstance().getRenderResponse()) {
			if (systemConfigService.getValue(getCurrentOrganVO().getId(), null,
					"hesabSoodVaZyanAnbashtehTafsiliId") != null) {
				HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity = hesabTafsiliTemplateService
						.load(Long.valueOf(systemConfigService.getValue(getCurrentOrganVO().getId(), null,
								"hesabSoodVaZyanAnbashtehTafsiliId")));
				hesabSoodVaZyanAnbashtehTafsiliId = hesabTafsiliTemplateEntity.getId();
			}
		}
		return hesabSoodVaZyanAnbashtehTafsiliId;
	}

	public String getHesabSoodVaZyanAnbashtehTafsiliDesc() {
		if (hesabSoodVaZyanAnbashtehTafsiliDesc == null) {
			if (systemConfigService.getValue(getCurrentOrganVO().getId(), null,
					"hesabSoodVaZyanAnbashtehTafsiliId") != null) {
				HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity = hesabTafsiliTemplateService
						.load(Long.valueOf(systemConfigService.getValue(getCurrentOrganVO().getId(), null,
								"hesabSoodVaZyanAnbashtehTafsiliId")));
				hesabSoodVaZyanAnbashtehTafsiliDesc = hesabTafsiliTemplateEntity.getDesc();
			}
		}
		return hesabSoodVaZyanAnbashtehTafsiliDesc;
	}

	public void setHesabSoodVaZyanAnbashtehTafsiliId(Long hesabSoodVaZyanAnbashtehTafsiliId) {
		this.hesabSoodVaZyanAnbashtehTafsiliId = hesabSoodVaZyanAnbashtehTafsiliId;
	}

	public void setHesabSoodVaZyanAnbashtehTafsiliDesc(String hesabSoodVaZyanAnbashtehTafsiliDesc) {
		this.hesabSoodVaZyanAnbashtehTafsiliDesc = hesabSoodVaZyanAnbashtehTafsiliDesc;
	}

	public void setDefaultSanadTypeId(Long defaultSanadTypeId) {
		this.defaultSanadTypeId = defaultSanadTypeId;
	}

	public Long getDefaultSanadTypeId() {
		if (defaultSanadTypeId == null)
			if (systemConfigService.getValue(getCurrentOrganVO().getId(), null, "defaultSanadTypeId") != null) {
				defaultSanadTypeId = Long
						.valueOf(systemConfigService.getValue(getCurrentOrganVO().getId(), null, "defaultSanadTypeId"));
			}
		return defaultSanadTypeId;
	}

	public void itemdesc(String newvalue) {
		String entityName = SerajMessageUtil.getMessage(getEntityName() + "_title");
		ActionLogUtil.logAction(SerajMessageUtil.getMessage(ActionTypeEnum.CREATE.nameWithClass()), entityName,
				newvalue, "", "");
	}

	public void configSave() {
		/// Get Old Values
		String differences = "";
		String olddefaultSanadTypeId = systemConfigService.getValue(getCurrentOrganVO().getId(), null,
				"defaultSanadTypeId");
		String oldhesabSoodVaZyanAnbashtehMoeenId = systemConfigService.getValue(getCurrentOrganVO().getId(), null,
				"hesabSoodVaZyanAnbashtehMoeenId");
		String oldhesabSoodVaZyanAnbashtehTafsiliId = systemConfigService.getValue(getCurrentOrganVO().getId(), null,
				"hesabSoodVaZyanAnbashtehTafsiliId");
		String oldvalidateHesabMoeenHasChild = systemConfigService.getValue(getCurrentOrganVO().getId(), null,
				"validateHesabMoeenHasChild");
		String oldvalidateHesabTafsiliHasChild = systemConfigService.getValue(getCurrentOrganVO().getId(), null,
				"validateHesabTafsiliHasChild");
		String oldvalidateHesabMoeenHasMarkaz = systemConfigService.getValue(getCurrentOrganVO().getId(), null,
				"validateHesabMoeenHasMarkaz");
		String oldmaxSanadHesabdariTafsilLevel = systemConfigService.getValue(getCurrentOrganVO().getId(), null,
				"maxSanadHesabdariTafsilLevel");
		// Insert New Value
		systemConfigService.insertKeyValue("defaultSanadTypeId", getDefaultSanadTypeId().toString(), null,
				getCurrentOrganVO().getId(), getCurrentUserVO().getDesc());
		SanadTypeEntity defaultSanadTypeIdValue = sanadTypeService.load(getDefaultSanadTypeId());
		itemdesc(defaultSanadTypeIdValue.toString());

		systemConfigService.insertKeyValue("hesabSoodVaZyanAnbashtehMoeenId",
				getHesabSoodVaZyanAnbashtehMoeenId() != null ? getHesabSoodVaZyanAnbashtehMoeenId().toString() : null,
				null, getCurrentOrganVO().getId(), getCurrentUserVO().getDesc());

		if (getHesabSoodVaZyanAnbashtehMoeenId() != null) {
			HesabMoeenTemplateEntity hesabSoodVaZyanAnbashtehMoeenIdValue = hesabMoeenTemplateService
					.load(getHesabSoodVaZyanAnbashtehMoeenId());
			itemdesc(hesabSoodVaZyanAnbashtehMoeenIdValue.toString());
		}

		systemConfigService.insertKeyValue("hesabSoodVaZyanAnbashtehTafsiliId",
				getHesabSoodVaZyanAnbashtehTafsiliId() != null ? getHesabSoodVaZyanAnbashtehTafsiliId().toString()
						: null,
				null, getCurrentOrganVO().getId(), getCurrentUserVO().getDesc());
		if (getHesabSoodVaZyanAnbashtehTafsiliId() != null) {
			HesabTafsiliTemplateEntity hesabSoodVaZyanAnbashtehTafsiliIdValue = hesabTafsiliTemplateService
					.load(getHesabSoodVaZyanAnbashtehTafsiliId());
			itemdesc(hesabSoodVaZyanAnbashtehTafsiliIdValue.toString());
		}

		if (getHesabTafsiliCodeCharactersNumber() != null)
			systemConfigService.insertKeyValue("hesabTafsiliCodeCharactersNumber",
					getHesabTafsiliCodeCharactersNumber().toString(), null, getCurrentOrganVO().getId(),
					getCurrentUserVO().getDesc());

		systemConfigService.insertKeyValue("HesabTafsiliCodingType", getHesabTafsiliCodingType().toString(), null,
				getCurrentOrganVO().getId(), getCurrentUserVO().getDesc());

		if (getHesabMoeenCodeCharactersNumber() != null)
			systemConfigService.insertKeyValue("hesabMoeenCodeCharactersNumber",
					getHesabMoeenCodeCharactersNumber().toString(), null, getCurrentOrganVO().getId(),
					getCurrentUserVO().getDesc());

		systemConfigService.insertKeyValue("HesabMoeenCodingType", getHesabMoeenCodingType().toString(), null,
				getCurrentOrganVO().getId(), getCurrentUserVO().getDesc());

		systemConfigService.insertKeyValue("validateHesabMoeenHasChild",
				getValidateHesabMoeenHasChild().value().toString(), null, getCurrentOrganVO().getId(),
				getCurrentUserVO().getDesc());
		systemConfigService.insertKeyValue("validateHesabTafsiliHasChild",
				getValidateHesabTafsiliHasChild().value().toString(), null, getCurrentOrganVO().getId(),
				getCurrentUserVO().getDesc());
//		YesNoEnum oldvalidateHesabMoeenHasChildValue = YesNoEnum.getName(Integer.valueOf(oldvalidateHesabMoeenHasChild));
//		itemdesc(SerajMessageUtil.getMessage(oldvalidateHesabMoeenHasChildValue.nameWithClass()));
//		
		systemConfigService.insertKeyValue("validateHesabMoeenHasMarkaz",
				getValidateHesabMoeenHasMarkaz().value().toString(), null, getCurrentOrganVO().getId(),
				getCurrentUserVO().getDesc());
		systemConfigService.insertKeyValue("maxSanadHesabdariTafsilLevel", getMaxSanadHesabdariTafsilLevel().toString(),
				null, getCurrentOrganVO().getId(), getCurrentUserVO().getDesc());
		systemConfigService.insertKeyValue("numberOfDecimals", getNumberOfDecimalsConfig().toString(), null,
				getCurrentOrganVO().getId(), getCurrentUserVO().getDesc());
//		 YesNoEnum oldvalidateHesabMoeenHasMarkazValue = YesNoEnum.getName(Integer.valueOf(oldvalidateHesabMoeenHasMarkaz)); 
//		itemdesc(SerajMessageUtil.getMessage(oldvalidateHesabMoeenHasMarkazValue.nameWithClass()));

		if (olddefaultSanadTypeId != null && !olddefaultSanadTypeId.equals(getDefaultSanadTypeId().toString())) {
			SanadTypeEntity oldEntity = sanadTypeService.load(Long.valueOf(olddefaultSanadTypeId));
			SanadTypeEntity newEntity = sanadTypeService.load(getDefaultSanadTypeId());
			differences += "[" + SerajMessageUtil.getMessage("AccountingSystemConfig_defaultSanadType") + " : "
					+ oldEntity.getDesc() + "-->" + newEntity.getDesc() + "]";
		}

		if (oldhesabSoodVaZyanAnbashtehMoeenId != null
				&& !oldhesabSoodVaZyanAnbashtehMoeenId.equals(getHesabSoodVaZyanAnbashtehMoeenId().toString())) {
			HesabMoeenTemplateEntity oldEntity = hesabMoeenTemplateService
					.load(Long.valueOf(oldhesabSoodVaZyanAnbashtehMoeenId));
			HesabMoeenTemplateEntity newEntity = hesabMoeenTemplateService.load(getHesabSoodVaZyanAnbashtehMoeenId());
			differences += "[" + SerajMessageUtil.getMessage("AccountingSystemConfig_hesabSoodVaZyanAnbashtehMoeen")
					+ " : " + oldEntity.getDesc() + "-->" + newEntity.getDesc() + "]";
		}

		if (oldhesabSoodVaZyanAnbashtehTafsiliId != null
				&& !oldhesabSoodVaZyanAnbashtehTafsiliId.equals(getHesabSoodVaZyanAnbashtehTafsiliId().toString())) {
			HesabTafsiliTemplateEntity oldEntity = hesabTafsiliTemplateService
					.load(Long.valueOf(oldhesabSoodVaZyanAnbashtehTafsiliId));
			HesabTafsiliTemplateEntity newEntity = hesabTafsiliTemplateService
					.load(getHesabSoodVaZyanAnbashtehTafsiliId());
			differences += "[" + SerajMessageUtil.getMessage("AccountingSystemConfig_hesabSoodVaZyanAnbashtehTafsili")
					+ " : " + oldEntity.getDesc() + "-->" + newEntity.getDesc() + "]";
		}
		if (oldvalidateHesabMoeenHasChild != null
				&& !oldvalidateHesabMoeenHasChild.equals(getValidateHesabMoeenHasChild().value().toString())) {
			YesNoEnum yesNoEnum = YesNoEnum.getName(Integer.valueOf(oldvalidateHesabMoeenHasChild));
			differences += "[" + SerajMessageUtil.getMessage("AccountingSystemConfig_validateHesabMoeenHasChild")
					+ " : " + SerajMessageUtil.getMessage(yesNoEnum.nameWithClass()) + "-->"
					+ SerajMessageUtil.getMessage(getValidateHesabMoeenHasChild().nameWithClass()) + "]";
		}
		if (oldvalidateHesabTafsiliHasChild != null
				&& !oldvalidateHesabTafsiliHasChild.equals(getValidateHesabTafsiliHasChild().value().toString())) {
			YesNoEnum yesNoEnum = YesNoEnum.getName(Integer.valueOf(oldvalidateHesabMoeenHasChild));
			differences += "[" + SerajMessageUtil.getMessage("AccountingSystemConfig_validateHesabTafsiliHasChild")
					+ " : " + SerajMessageUtil.getMessage(yesNoEnum.nameWithClass()) + "-->"
					+ SerajMessageUtil.getMessage(getValidateHesabTafsiliHasChild().nameWithClass()) + "]";
		}
		if (oldvalidateHesabMoeenHasMarkaz != null
				&& !oldvalidateHesabMoeenHasMarkaz.equals(getValidateHesabMoeenHasMarkaz().value().toString())) {
			YesNoEnum yesNoEnum = YesNoEnum.getName(Integer.valueOf(oldvalidateHesabMoeenHasMarkaz));
			differences += "[" + SerajMessageUtil.getMessage("AccountingSystemConfig_validateHesabMoeenHasMarkaz")
					+ " : " + SerajMessageUtil.getMessage(yesNoEnum.nameWithClass()) + "-->"
					+ SerajMessageUtil.getMessage(getValidateHesabMoeenHasMarkaz().nameWithClass()) + "]";
		}

		if (oldmaxSanadHesabdariTafsilLevel != null
				&& !oldmaxSanadHesabdariTafsilLevel.equals(getMaxSanadHesabdariTafsilLevel())) {
			differences += "[" + SerajMessageUtil.getMessage("AccountingSystemConfig_maxSanadHesabdariTafsilLevel")
					+ " : " + oldmaxSanadHesabdariTafsilLevel + "-->" + maxSanadHesabdariTafsilLevel + "]";
		}

		addInfoMessage("SUCCESSFUL_ACTION");
		// Log Action
		ActionLogUtil.logAction(SerajMessageUtil.getMessage(ActionTypeEnum.EDIT.nameWithClass()),
				SerajMessageUtil.getMessage("PayrollSystemConfig_configPayroll"), "", "", differences);
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

	Long saalMaaliId;

	public Long getSaalMaaliId() {
		String saalMaaliIdStr = systemConfigService.getValue(getCurrentOrganVO().getId(), getCurrentUserVO().getId(),
				"saalMaaliId");
		if (saalMaaliId == null && saalMaaliIdStr != null && FacesContext.getCurrentInstance().getRenderResponse())
			saalMaaliId = Long.valueOf(saalMaaliIdStr);
		return saalMaaliId;
	}

	public void setSaalMaaliId(Long saalMaaliId) {
		this.saalMaaliId = saalMaaliId;
	}

	public String userConfigSave() {
		SaalMaaliEntity saalMaaliEntity = null;
		if (getSaalMaaliId() != null)
			saalMaaliEntity = saalMaaliService.get(getSaalMaaliId());

		systemConfigService.insertKeyValue("saalMaaliId", getSaalMaaliId() != null ? getSaalMaaliId().toString() : null,
				getCurrentUserVO().getId(), getCurrentOrganVO().getId(), getCurrentUserVO().getDesc());
		String entityName = SerajMessageUtil.getMessage(saalMaaliService.getEntityName() + "_title");
		ActionLogUtil.logAction(SerajMessageUtil.getMessage(ActionTypeEnum.EDIT.nameWithClass()), entityName,
				saalMaaliEntity != null ? saalMaaliEntity.getSaal().toString() : "", "",
				saalMaaliEntity != null ? saalMaaliEntity.getLog() : "", getCurrentUserVO());
		addInfoMessage("SUCCESSFUL_ACTION");
		setSuccessfullAction(true);
		return null;
	}
}
