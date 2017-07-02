package ir.serajsamaneh.accounting.accountstemplate;

import ir.serajsamaneh.accounting.articletafsili.ArticleTafsiliEntity;
import ir.serajsamaneh.accounting.articletafsilitemplate.ArticleTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemVO;
import ir.serajsamaneh.accounting.sanadhesabdariitemtemplate.SanadHesabdariItemTemplateEntity;

public class SanadHesabdariItemTemplateVO extends SanadHesabdariItemVO {

	String templateType=""; //bedehkar/bestankar
	String hesabTafsiliTemplateCode;
	String hesabTafsiliTemplateName;
	String hesabTafsiliTemplateID;
	
	String hesabMoeenTemplateName;
	String hesabMoeenTemplateID;
	
	String hesabKolTemplateName;
	String hesabKolTemplateID;
	
	String accountingMarkazTemplateName;
	String accountingMarkazTemplateID;
	
	private String applyAutomaticTafsili="";
	private String applyAutomaticTafsiliName="";
	
	protected java.lang.String hesabTafsiliTemplateLevels="";
	protected java.lang.String hesabTafsiliTemplateDescs="";
	protected java.lang.String hesabTafsiliTemplateLevelNames="";
	
	public String getApplyAutomaticTafsili() {
		return applyAutomaticTafsili;
	}

	public void setApplyAutomaticTafsili(String applyAutomaticTafsili) {
		this.applyAutomaticTafsili = applyAutomaticTafsili;
	}

	public String getApplyAutomaticTafsiliName() {
		return applyAutomaticTafsiliName;
	}

	public void setApplyAutomaticTafsiliName(String applyAutomaticTafsiliName) {
		this.applyAutomaticTafsiliName = applyAutomaticTafsiliName;
	}

	public String getAccountingMarkazTemplateName() {
		return accountingMarkazTemplateName;
	}

	public void setAccountingMarkazTemplateName(String accountingMarkazTemplateName) {
		this.accountingMarkazTemplateName = accountingMarkazTemplateName;
	}

	public String getAccountingMarkazTemplateID() {
		return accountingMarkazTemplateID;
	}

	public void setAccountingMarkazTemplateID(String accountingMarkazTemplateID) {
		this.accountingMarkazTemplateID = accountingMarkazTemplateID;
	}

	public java.lang.String getHesabTafsiliTemplateLevels() {
		return hesabTafsiliTemplateLevels;
	}

	public void setHesabTafsiliTemplateLevels(
			java.lang.String hesabTafsiliTemplateLevels) {
		this.hesabTafsiliTemplateLevels = hesabTafsiliTemplateLevels;
	}

	public java.lang.String getHesabTafsiliTemplateDescs() {
		return hesabTafsiliTemplateDescs;
	}

	public void setHesabTafsiliTemplateDescs(
			java.lang.String hesabTafsiliTemplateDescs) {
		this.hesabTafsiliTemplateDescs = hesabTafsiliTemplateDescs;
	}

	public java.lang.String getHesabTafsiliTemplateLevelNames() {
		return hesabTafsiliTemplateLevelNames;
	}

	public void setHesabTafsiliTemplateLevelNames(
			java.lang.String hesabTafsiliTemplateLevelNames) {
		this.hesabTafsiliTemplateLevelNames = hesabTafsiliTemplateLevelNames;
	}

	public String getHesabMoeenTemplateName() {
		return hesabMoeenTemplateName;
	}

	public void setHesabMoeenTemplateName(String hesabMoeenTemplateName) {
		this.hesabMoeenTemplateName = hesabMoeenTemplateName;
	}

	public String getHesabMoeenTemplateID() {
		return hesabMoeenTemplateID;
	}

	public void setHesabMoeenTemplateID(String hesabMoeenTemplateID) {
		this.hesabMoeenTemplateID = hesabMoeenTemplateID;
	}

	public String getHesabKolTemplateName() {
		return hesabKolTemplateName;
	}

	public void setHesabKolTemplateName(String hesabKolTemplateName) {
		this.hesabKolTemplateName = hesabKolTemplateName;
	}

	public String getHesabKolTemplateID() {
		return hesabKolTemplateID;
	}

	public void setHesabKolTemplateID(String hesabKolTemplateID) {
		this.hesabKolTemplateID = hesabKolTemplateID;
	}

	public String getHesabTafsiliTemplateID() {
		return hesabTafsiliTemplateID;
	}

	public void setHesabTafsiliTemplateID(String hesabTafsiliTemplateID) {
		this.hesabTafsiliTemplateID = hesabTafsiliTemplateID;
	}

	public String getHesabTafsiliTemplateName() {
		return hesabTafsiliTemplateName;
	}

	public void setHesabTafsiliTemplateName(String hesabTafsiliTemplateName) {
		this.hesabTafsiliTemplateName = hesabTafsiliTemplateName;
	}

	public String getHesabTafsiliTemplateCode() {
		return hesabTafsiliTemplateCode;
	}

	public void setHesabTafsiliTemplateCode(String hesabTafsiliTemplateCode) {
		this.hesabTafsiliTemplateCode = hesabTafsiliTemplateCode;
	}

	
	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public SanadHesabdariItemTemplateVO(SanadHesabdariItemTemplateEntity sanadHesabdariItemTemplateEntity, String templateType) {
		
		setTemplateType(templateType);
		setId(sanadHesabdariItemTemplateEntity.getId()!=null ? sanadHesabdariItemTemplateEntity.getId().toString() : "");
		setDescription(convertNullToString(sanadHesabdariItemTemplateEntity.getDescription()));
		if(sanadHesabdariItemTemplateEntity.getApplyAutomaticTafsili()!=null){
			setApplyAutomaticTafsili(convertNullToString(sanadHesabdariItemTemplateEntity.getApplyAutomaticTafsili().name()));
			setApplyAutomaticTafsiliName(convertNullToString(sanadHesabdariItemTemplateEntity.getApplyAutomaticTafsiliName()));
		}
		
		if(sanadHesabdariItemTemplateEntity.getHesabTafsiliTemplate()!=null){
			setHesabTafsiliTemplateName(convertNullToString(sanadHesabdariItemTemplateEntity.getHesabTafsiliTemplate().getDesc()));
			setHesabTafsiliTemplateID(convertNullToString(sanadHesabdariItemTemplateEntity.getHesabTafsiliTemplate().getId()));
		}else{
			setHesabTafsiliTemplateName("");
			setHesabTafsiliTemplateID("");
		}

		if(sanadHesabdariItemTemplateEntity.getArticleTafsiliTemplate()!= null && !sanadHesabdariItemTemplateEntity.getArticleTafsiliTemplate().isEmpty()){
			hesabTafsiliTemplateLevelNames="";
			for(ArticleTafsiliTemplateEntity articleTafsiliTemplateEntity : sanadHesabdariItemTemplateEntity.getArticleTafsiliTemplate()){
				hesabTafsiliTemplateLevels = hesabTafsiliTemplateLevels +",hesabTafsiliTemplate"+articleTafsiliTemplateEntity.getLevel()+"="+articleTafsiliTemplateEntity.getHesabTafsiliTemplate().getId();
				hesabTafsiliTemplateDescs = hesabTafsiliTemplateDescs +",hesabTafsiliTemplate"+articleTafsiliTemplateEntity.getLevel()+"="+articleTafsiliTemplateEntity.getHesabTafsiliTemplate().getDesc();
				hesabTafsiliTemplateLevelNames = hesabTafsiliTemplateLevelNames +","+articleTafsiliTemplateEntity.getHesabTafsiliTemplate().getDesc();
			}
		}


		if(sanadHesabdariItemTemplateEntity.getAccountingMarkazTemplate()!=null){
			setAccountingMarkazTemplateName(convertNullToString(sanadHesabdariItemTemplateEntity.getAccountingMarkazTemplate().getDesc()));
			setAccountingMarkazTemplateID(convertNullToString(sanadHesabdariItemTemplateEntity.getAccountingMarkazTemplate().getId()));
		}else{
			setAccountingMarkazTemplateName("");
			setAccountingMarkazTemplateID("");
		}

		/*Commented because of incomplete development*/
		
/*		if(sanadHesabdariItemEntity.getArticleTafsili()!= null && !sanadHesabdariItemEntity.getArticleTafsili().isEmpty()){
			accountingMarkazLevelNames="";
			for(ArticleTafsiliEntity articleTafsiliEntity : sanadHesabdariItemEntity.getArticleTafsili()){
				accountingMarkazLevels = accountingMarkazLevels +",accountingMarkaz"+articleTafsiliEntity.getLevel()+"="+articleTafsiliEntity.getAccountingMarkaz().getId();
				accountingMarkazDescs = accountingMarkazDescs +",accountingMarkaz"+articleTafsiliEntity.getLevel()+"="+articleTafsiliEntity.getAccountingMarkaz().getDesc();
				accountingMarkazLevelNames = accountingMarkazLevelNames +","+articleTafsiliEntity.getAccountingMarkaz().getDesc();
			}
		}*/
		
		setHesabKolTemplateName(convertNullToString(sanadHesabdariItemTemplateEntity.getHesabKolTemplate()!= null ? sanadHesabdariItemTemplateEntity.getHesabKolTemplate().getDesc() : ""));
		setHesabKolTemplateID(convertNullToString(sanadHesabdariItemTemplateEntity.getHesabKolTemplate()!=null ? sanadHesabdariItemTemplateEntity.getHesabKolTemplate().getId() : ""));

		setHesabMoeenTemplateName(convertNullToString(sanadHesabdariItemTemplateEntity.getHesabMoeenTemplate()!=null ? sanadHesabdariItemTemplateEntity.getHesabMoeenTemplate().getDesc() : ""));
		setHesabMoeenTemplateID(convertNullToString(sanadHesabdariItemTemplateEntity.getHesabMoeenTemplate()!=null ? sanadHesabdariItemTemplateEntity.getHesabMoeenTemplate().getId() : ""));
		setTarikhArticle(convertNullToString(sanadHesabdariItemTemplateEntity.getTarikhArticle()));
	}
}
