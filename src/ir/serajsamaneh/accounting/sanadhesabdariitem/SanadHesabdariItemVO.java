package ir.serajsamaneh.accounting.sanadhesabdariitem;

import ir.serajsamaneh.accounting.articleaccountingmarkaz.ArticleAccountingMarkazEntity;
import ir.serajsamaneh.accounting.articletafsili.ArticleTafsiliEntity;
import ir.serajsamaneh.core.base.BaseValueObject;

public class SanadHesabdariItemVO extends BaseValueObject {


	protected String id="";
	protected java.lang.String row="";
	protected java.lang.String description="";
	protected java.lang.String type="";
	protected java.lang.String bestankar="";
	protected java.lang.String bedehkar="";
	protected java.lang.String tarikhArticle="";
	
	protected java.lang.String hesabKolID="";
	protected java.lang.String hesabKolName="";

	protected java.lang.String hesabTafsiliID="";
	protected java.lang.String hesabTafsiliName="";
	
	protected java.lang.String hesabMoeenID="";
	protected java.lang.String hesabMoeenName="";
	

	
	protected java.lang.String markazHazineID="";
	protected java.lang.String markazHazineName="";
	
	protected java.lang.String projectID="";
	protected java.lang.String projectName="";

	protected java.lang.String hesabTafsiliLevels="";
	protected java.lang.String hesabTafsiliDescs="";
	protected java.lang.String hesabTafsiliLevelNames="";
	
	protected java.lang.String accountingMarkazID="";
	protected java.lang.String accountingMarkazName="";
	protected java.lang.String accountingMarkazLevels="";
	protected java.lang.String accountingMarkazDescs="";
	protected java.lang.String accountingMarkazLevelNames="";
	
	public SanadHesabdariItemVO(
			SanadHesabdariItemEntity sanadHesabdariItemEntity) {
		setId(sanadHesabdariItemEntity.getId().toString());
		setDescription(convertNullToString(sanadHesabdariItemEntity.getDescription()));
		
		String bedehkarFormatted = getBigDecimalFormatted(sanadHesabdariItemEntity.getBedehkar(),0);
		setBedehkar(bedehkarFormatted.equals("0") ? "" : bedehkarFormatted);
		
		String bestankarFormatted = getBigDecimalFormatted(sanadHesabdariItemEntity.getBestankar(),0);
		setBestankar(bestankarFormatted.equals("0") ? "" : bestankarFormatted);
		
//		if(sanadHesabdariItemEntity.getMarkazHazine()!=null && sanadHesabdariItemEntity.getMarkazHazine().getId()!=null){
//			setMarkazHazineID(convertNullToString(sanadHesabdariItemEntity.getMarkazHazine().getId()));
//			setMarkazHazineName(convertNullToString(sanadHesabdariItemEntity.getMarkazHazine().getOnvan()));
//		}


		if(sanadHesabdariItemEntity.getHesabTafsili()!=null){
			setHesabTafsiliName(convertNullToString(sanadHesabdariItemEntity.getHesabTafsili().getDesc()));
			setHesabTafsiliID(convertNullToString(sanadHesabdariItemEntity.getHesabTafsili().getId()));
//			setHesabTafsiliInstanceName(convertNullToString(sanadHesabdariItemEntity.getHesabTafsili().getName()));
//			setHesabTafsiliInstanceID(convertNullToString(sanadHesabdariItemEntity.getHesabTafsili().getId()));
		}else{
			setHesabTafsiliName("");
			setHesabTafsiliID("");
		}

		if(sanadHesabdariItemEntity.getArticleTafsili()!= null && !sanadHesabdariItemEntity.getArticleTafsili().isEmpty()){
			hesabTafsiliLevelNames="";
			for(ArticleTafsiliEntity articleTafsiliEntity : sanadHesabdariItemEntity.getArticleTafsili()){
				hesabTafsiliLevels = hesabTafsiliLevels +",hesabTafsili"+articleTafsiliEntity.getLevel()+"="+articleTafsiliEntity.getHesabTafsili().getId();
				hesabTafsiliDescs = hesabTafsiliDescs +",hesabTafsili"+articleTafsiliEntity.getLevel()+"="+articleTafsiliEntity.getHesabTafsili().getDesc();
				hesabTafsiliLevelNames = hesabTafsiliLevelNames +","+articleTafsiliEntity.getHesabTafsili().getDesc();
			}
		}


		if(sanadHesabdariItemEntity.getAccountingMarkaz()!=null){
			setAccountingMarkazName(convertNullToString(sanadHesabdariItemEntity.getAccountingMarkaz().getDesc()));
			setAccountingMarkazID(convertNullToString(sanadHesabdariItemEntity.getAccountingMarkaz().getId()));
//			setAccountingMarkazInstanceName(convertNullToString(sanadHesabdariItemEntity.getAccountingMarkaz().getName()));
//			setAccountingMarkazInstanceID(convertNullToString(sanadHesabdariItemEntity.getAccountingMarkaz().getId()));
		}else{
			setAccountingMarkazName("");
			setAccountingMarkazID("");
		}

		
		if(sanadHesabdariItemEntity.getArticleAccountingMarkaz()!= null && !sanadHesabdariItemEntity.getArticleAccountingMarkaz().isEmpty()){
			accountingMarkazLevelNames="";
			for(ArticleAccountingMarkazEntity articleAccountingMarkazEntity : sanadHesabdariItemEntity.getArticleAccountingMarkaz()){
				accountingMarkazLevels = accountingMarkazLevels +",accountingMarkaz"+articleAccountingMarkazEntity.getLevel()+"="+articleAccountingMarkazEntity.getAccountingMarkaz().getId();
				accountingMarkazDescs = accountingMarkazDescs +",accountingMarkaz"+articleAccountingMarkazEntity.getLevel()+"="+articleAccountingMarkazEntity.getAccountingMarkaz().getDesc();
				accountingMarkazLevelNames = accountingMarkazLevelNames +","+articleAccountingMarkazEntity.getAccountingMarkaz().getDesc();
			}
		}
		
		setHesabKolName(convertNullToString(sanadHesabdariItemEntity.getHesabKol().getDesc()));
		setHesabKolID(convertNullToString(sanadHesabdariItemEntity.getHesabKol().getId()));

		String moeenDesc = sanadHesabdariItemEntity.getHesabMoeen()!=null ? sanadHesabdariItemEntity.getHesabMoeen().getDesc() : "";
		setHesabMoeenName(convertNullToString(moeenDesc));
		
		setHesabMoeenID(convertNullToString(sanadHesabdariItemEntity.getHesabMoeen()!=null ? sanadHesabdariItemEntity.getHesabMoeen().getId() : ""));
		setTarikhArticle(convertNullToString(sanadHesabdariItemEntity.getTarikhArticle()));
	}
	
	
	public SanadHesabdariItemVO() {
		// TODO Auto-generated constructor stub
	}


	public java.lang.String getRow() {
		return row;
	}

	public void setRow(java.lang.String row) {
		this.row = row;
	}

	public java.lang.String getDescription() {
		return description;
	}

	public void setDescription(java.lang.String description) {
		description = removeControlChars(description);
		this.description = description;
	}

	public java.lang.String getType() {
		return type;
	}

	public void setType(java.lang.String type) {
		this.type = type;
	}

	public java.lang.String getBestankar() {
		return bestankar;
	}

	public void setBestankar(java.lang.String bestankar) {
		this.bestankar = bestankar;
	}

	public java.lang.String getBedehkar() {
		return bedehkar;
	}

	public void setBedehkar(java.lang.String bedehkar) {
		this.bedehkar = bedehkar;
	}

	public java.lang.String getTarikhArticle() {
		return tarikhArticle;
	}

	public void setTarikhArticle(java.lang.String tarikhArticle) {
		this.tarikhArticle = tarikhArticle;
	}

	public java.lang.String getHesabTafsiliName() {
		return hesabTafsiliName;
	}

	public void setHesabTafsiliName(java.lang.String hesabTafsiliName) {
		this.hesabTafsiliName = hesabTafsiliName;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	public java.lang.String getHesabKolID() {
		return hesabKolID;
	}


	public void setHesabKolID(java.lang.String hesabKolID) {
		this.hesabKolID = hesabKolID;
	}


	public java.lang.String getHesabKolName() {
		return hesabKolName;
	}


	public void setHesabKolName(java.lang.String hesabKolName) {
		this.hesabKolName = hesabKolName;
	}


	public java.lang.String getHesabTafsiliID() {
		return hesabTafsiliID;
	}


	public void setHesabTafsiliID(java.lang.String hesabTafsiliID) {
		this.hesabTafsiliID = hesabTafsiliID;
	}

	public java.lang.String getHesabMoeenID() {
		return hesabMoeenID;
	}


	public void setHesabMoeenID(java.lang.String hesabMoeenID) {
		this.hesabMoeenID = hesabMoeenID;
	}


	public java.lang.String getHesabMoeenName() {
		return hesabMoeenName;
	}


	public void setHesabMoeenName(java.lang.String hesabMoeenName) {
		this.hesabMoeenName = hesabMoeenName;
	}
	

	
	public java.lang.String getMarkazHazineID() {
		return markazHazineID;
	}
	
	public void setMarkazHazineID(java.lang.String markazHazineID) {
		this.markazHazineID = markazHazineID;
	}
	
	public java.lang.String getMarkazHazineName() {
		return markazHazineName;
	}
	
	public void setMarkazHazineName(java.lang.String markazHazineName) {
		this.markazHazineName = markazHazineName;
	}
	
	public java.lang.String getProjectID() {
		return projectID;
	}
	
	public void setProjectID(java.lang.String projectID) {
		this.projectID = projectID;
	}
	
	public java.lang.String getProjectName() {
		return projectName;
	}
	
	public void setProjectName(java.lang.String projectName) {
		this.projectName = projectName;
	}


	public java.lang.String getHesabTafsiliLevels() {
		return hesabTafsiliLevels;
	}


	public void setHesabTafsiliLevels(java.lang.String hesabTafsiliLevels) {
		this.hesabTafsiliLevels = hesabTafsiliLevels;
	}


	public java.lang.String getHesabTafsiliDescs() {
		return hesabTafsiliDescs;
	}


	public void setHesabTafsiliDescs(java.lang.String hesabTafsiliDescs) {
		this.hesabTafsiliDescs = hesabTafsiliDescs;
	}


	public java.lang.String getHesabTafsiliLevelNames() {
		return hesabTafsiliLevelNames;
	}


	public void setHesabTafsiliLevelNames(java.lang.String hesabTafsiliLevelNames) {
		this.hesabTafsiliLevelNames = hesabTafsiliLevelNames;
	}


	public java.lang.String getAccountingMarkazID() {
		return accountingMarkazID;
	}


	public void setAccountingMarkazID(java.lang.String accountingMarkazID) {
		this.accountingMarkazID = accountingMarkazID;
	}


	public java.lang.String getAccountingMarkazName() {
		return accountingMarkazName;
	}


	public void setAccountingMarkazName(java.lang.String accountingMarkazName) {
		this.accountingMarkazName = accountingMarkazName;
	}


	public java.lang.String getAccountingMarkazLevels() {
		return accountingMarkazLevels;
	}


	public void setAccountingMarkazLevels(java.lang.String accountingMarkazLevels) {
		this.accountingMarkazLevels = accountingMarkazLevels;
	}


	public java.lang.String getAccountingMarkazDescs() {
		return accountingMarkazDescs;
	}


	public void setAccountingMarkazDescs(java.lang.String accountingMarkazDescs) {
		this.accountingMarkazDescs = accountingMarkazDescs;
	}


	public java.lang.String getAccountingMarkazLevelNames() {
		return accountingMarkazLevelNames;
	}


	public void setAccountingMarkazLevelNames(
			java.lang.String accountingMarkazLevelNames) {
		this.accountingMarkazLevelNames = accountingMarkazLevelNames;
	}


/*	public java.lang.String getHesabTafsiliInstanceID() {
		return hesabTafsiliInstanceID;
	}


	public void setHesabTafsiliInstanceID(java.lang.String hesabTafsiliInstanceID) {
		this.hesabTafsiliInstanceID = hesabTafsiliInstanceID;
	}


	public java.lang.String getHesabTafsiliInstanceName() {
		return hesabTafsiliInstanceName;
	}


	public void setHesabTafsiliInstanceName(
			java.lang.String hesabTafsiliInstanceName) {
		this.hesabTafsiliInstanceName = hesabTafsiliInstanceName;
	}


	public java.lang.String getHesabMoeenInstanceID() {
		return hesabMoeenInstanceID;
	}


	public void setHesabMoeenInstanceID(java.lang.String hesabMoeenInstanceID) {
		this.hesabMoeenInstanceID = hesabMoeenInstanceID;
	}


	public java.lang.String getHesabMoeenInstanceName() {
		return hesabMoeenInstanceName;
	}


	public void setHesabMoeenInstanceName(java.lang.String hesabMoeenInstanceName) {
		this.hesabMoeenInstanceName = hesabMoeenInstanceName;
	}*/

	


}
