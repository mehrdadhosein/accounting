package ir.serajsamaneh.accounting.sanadhesabdariitem;

import ir.serajsamaneh.accounting.articleaccountingmarkaz.ArticleAccountingMarkazEntity;
import ir.serajsamaneh.accounting.articletafsili.ArticleTafsiliEntity;
import ir.serajsamaneh.accounting.enumeration.MahyatKolEnum;
import ir.serajsamaneh.core.base.BaseValueObject;

public class SanadHesabdariItemVO extends BaseValueObject {


	protected String id="";
	protected java.lang.String row="";
	protected java.lang.String description="";
	protected java.lang.String type="";
	protected java.lang.Double bestankar;
	protected java.lang.Double bedehkar;
	protected java.lang.String tarikhArticle="";
	protected java.lang.String bestankarFormatted;
	protected java.lang.String bedehkarFormatted;
	
	protected java.lang.String hesabKolID="";
	protected java.lang.String hesabKolName="";

	protected java.lang.String hesabTafsiliID="";
	protected java.lang.String hesabTafsiliName="";
	
	protected java.lang.String hesabMoeenID="";
	protected java.lang.String hesabMoeenName="";
	
	String hesabKolCode;
	String hesabMoeenCode;
	String hesabTafsiliCode;
	
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
	
	Double mandehBedehkarEbtedayDore;
	Double mandehBestankarEbtedayDore;
	Double operationSummaryBedehkar;
	Double operationSummaryBestankar;
	Double mandehBedehkar;
	Double mandehBestankar;
	String mandehByMahiyatHesabStr="";
	Double mandehByMahiyatHesabDbl = 0d;
	MahyatKolEnum hesabKolMahyat;
	
	public SanadHesabdariItemVO(
			SanadHesabdariItemEntity sanadHesabdariItemEntity) {
		setId(sanadHesabdariItemEntity.getId().toString());
		setDescription(convertNullToString(sanadHesabdariItemEntity.getDescription()));
		
		setMandehBedehkar(0d);
		setMandehBestankar(0d);
		
		setBedehkar(sanadHesabdariItemEntity.getBedehkar());
		setBestankar(sanadHesabdariItemEntity.getBestankar());
		
		setHesabKolMahyat(sanadHesabdariItemEntity.getHesabKol().getMahyatKol());
		String bedehkarFormatted = getBigDecimalFormatted(sanadHesabdariItemEntity.getBedehkar(),0);
		setBedehkarFormatted(bedehkarFormatted.equals("0") ? "" : bedehkarFormatted);
		
		String bestankarFormatted = getBigDecimalFormatted(sanadHesabdariItemEntity.getBestankar(),0);
		setBestankarFormatted(bestankarFormatted.equals("0") ? "" : bestankarFormatted);
		
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

	public java.lang.Double getBestankar() {
		return bestankar;
	}

	public void setBestankar(java.lang.Double bestankar) {
		this.bestankar = bestankar;
	}

	public java.lang.Double getBedehkar() {
		return bedehkar;
	}

	public void setBedehkar(java.lang.Double bedehkar) {
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


	public Double getMandehBedehkarEbtedayDore() {
		return mandehBedehkarEbtedayDore;
	}


	public void setMandehBedehkarEbtedayDore(Double mandehBedehkarEbtedayDore) {
		this.mandehBedehkarEbtedayDore = mandehBedehkarEbtedayDore;
	}


	public Double getMandehBestankarEbtedayDore() {
		return mandehBestankarEbtedayDore;
	}


	public void setMandehBestankarEbtedayDore(Double mandehBestankarEbtedayDore) {
		this.mandehBestankarEbtedayDore = mandehBestankarEbtedayDore;
	}


	public Double getOperationSummaryBedehkar() {
		return operationSummaryBedehkar;
	}


	public void setOperationSummaryBedehkar(Double operationSummaryBedehkar) {
		this.operationSummaryBedehkar = operationSummaryBedehkar;
	}


	public Double getOperationSummaryBestankar() {
		return operationSummaryBestankar;
	}


	public void setOperationSummaryBestankar(Double operationSummaryBestankar) {
		this.operationSummaryBestankar = operationSummaryBestankar;
	}


	public Double getMandehBedehkar() {
		return mandehBedehkar;
	}


	public void setMandehBedehkar(Double mandehBedehkar) {
		this.mandehBedehkar = mandehBedehkar;
	}


	public Double getMandehBestankar() {
		return mandehBestankar;
	}


	public void setMandehBestankar(Double mandehBestankar) {
		this.mandehBestankar = mandehBestankar;
	}


	public String getMandehByMahiyatHesabStr() {
		return mandehByMahiyatHesabStr;
	}


	public void setMandehByMahiyatHesabStr(String mandehByMahiyatHesabStr) {
		this.mandehByMahiyatHesabStr = mandehByMahiyatHesabStr;
	}


	public Double getMandehByMahiyatHesabDbl() {
		return mandehByMahiyatHesabDbl;
	}


	public void setMandehByMahiyatHesabDbl(Double mandehByMahiyatHesabDbl) {
		this.mandehByMahiyatHesabDbl = mandehByMahiyatHesabDbl;
	}


	public java.lang.String getBestankarFormatted() {
		return bestankarFormatted;
	}


	public void setBestankarFormatted(java.lang.String bestankarFormatted) {
		this.bestankarFormatted = bestankarFormatted;
	}


	public java.lang.String getBedehkarFormatted() {
		return bedehkarFormatted;
	}


	public void setBedehkarFormatted(java.lang.String bedehkarFormatted) {
		this.bedehkarFormatted = bedehkarFormatted;
	}


	public String getHesabKolCode() {
		return hesabKolCode;
	}


	public void setHesabKolCode(String hesabKolCode) {
		this.hesabKolCode = hesabKolCode;
	}


	public String getHesabMoeenCode() {
		return hesabMoeenCode;
	}


	public void setHesabMoeenCode(String hesabMoeenCode) {
		this.hesabMoeenCode = hesabMoeenCode;
	}


	public String getHesabTafsiliCode() {
		return hesabTafsiliCode;
	}


	public void setHesabTafsiliCode(String hesabTafsiliCode) {
		this.hesabTafsiliCode = hesabTafsiliCode;
	}


	public MahyatKolEnum getHesabKolMahyat() {
		return hesabKolMahyat;
	}


	public void setHesabKolMahyat(MahyatKolEnum hesabKolMahyat) {
		this.hesabKolMahyat = hesabKolMahyat;
	}




	


}
