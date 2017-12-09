package ir.serajsamaneh.accounting.sanadhesabdariitem;

import ir.serajsamaneh.core.util.SerajMessageUtil;



public class SanadHesabdariItemEntity extends BaseSanadHesabdariItemEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public SanadHesabdariItemEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public SanadHesabdariItemEntity (java.lang.Long id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	public String toString() {
		return getBedehkar()>0 ? 
				(getHesabKol()+"-"+getHesabMoeen()+"["+ SerajMessageUtil.getMessage("SanadHesabdariItem_bedehkar") +"("+ getBigDecimalFormatted(getBedehkar(),0)+")]") 
				: (getHesabKol()+"-"+getHesabMoeen()+"["+ SerajMessageUtil.getMessage("SanadHesabdariItem_bestankar") +"("+ getBigDecimalFormatted(getBestankar(),0)+")]");
	}
	
	String entityId;
	Double mandehBedehkarEbtedayDore;
	Double mandehBestankarEbtedayDore;
	Double operationSummaryBedehkar;
	Double operationSummaryBestankar;
	Double mandehBedehkar;
	Double mandehBestankar;
	String mandehByMahiyatHesabStr="";
	Double mandehByMahiyatHesabDbl = 0d;
//	MarkazHazineEntity markazHazine;
	
	public String getHesabKolCode() {
		return getHesabKol().getCode();
	}

	public String getHesabMoeenCode() {
		return getHesabMoeen().getCode();
	}
	
	public String getHesabTafsiliCode() {
		if(getHesabTafsili() == null)
			return "";
		return getHesabTafsili().getCode().toString();
	}
	
	public String getAccountingMarkazCode() {
		if(getAccountingMarkaz() == null)
			return "";
		return getAccountingMarkaz().getCode();
	}
/*
	
	public MarkazHazineEntity getMarkazHazine() {
		return markazHazine;
	}
	public void setMarkazHazine(MarkazHazineEntity markazHazine) {
		this.markazHazine = markazHazine;
	}*/
	
	
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

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

//	public List<ArticleTafsiliEntity> getSortedArticleTafsili() {
//		
//		return null;
//	}
//
//	public ArticleTafsiliEntity getArticleTafsiliByLevel(Integer level) {
//		Set<ArticleTafsiliEntity> set = getArticleTafsili();
//		if(set == null)
//			return null;
//		for (ArticleTafsiliEntity articleTafsiliEntity : set) {
//			if(articleTafsiliEntity.getLevel().intValue() == level)
//				return articleTafsiliEntity;
//		}
//		return null;
//		
//	}

//	public ArticleAccountingMarkazEntity getArticleAccountingMarkaz(Integer level) {
//		Set<ArticleAccountingMarkazEntity> set = getArticleAccountingMarkaz();
//		if(set == null)
//			return null;
//		for (ArticleAccountingMarkazEntity articleAccountingMarkazEntity : set) {
//			if(articleAccountingMarkazEntity.getLevel().intValue() == level)
//				return articleAccountingMarkazEntity;
//		}
//		return null;
//	}

	public String getHesabKolName() {
		return getHesabKol().getName();
	}

	public String getHesabMoeenName() {
		return getHesabMoeen().getName();
	}

	public String getHesabTafsiliName() {
		return getHesabTafsili().getName();
	}
	
	public String getHesabTafsiliTwoName() {
		return getHesabTafsili().getName();
	}
	
	public String getHesabTafsiliTwoDesc() {
		if(getHesabTafsiliTwo()!=null)
			return getHesabTafsiliTwo().getDesc();
		return "";
	}
	
//	public String getHesabShenavarName(Integer level) {
//		if(getArticleTafsili()!= null && !getArticleTafsili().isEmpty()){
//			for(ArticleTafsiliEntity articleTafsiliEntity : getArticleTafsili()){
//				if(articleTafsiliEntity.getLevel().equals(level))
//					return articleTafsiliEntity.getHesabTafsili().getName();
//			}
//		}
//		return null;
//	}
//	
//	public String getHesabShenavarCode(Integer level) {
//		if(getArticleTafsili()!= null && !getArticleTafsili().isEmpty()){
//			for(ArticleTafsiliEntity articleTafsiliEntity : getArticleTafsili()){
//				if(articleTafsiliEntity.getLevel().equals(level))
//					return articleTafsiliEntity.getHesabTafsili().getCode().toString();
//			}
//		}
//		return null;
//	}
//	
//	public String getArticleTafsiliDesc(){
//		String hesabTafsiliLevelNames="";
//		if(getArticleTafsili()!= null && !getArticleTafsili().isEmpty()){
//			for(ArticleTafsiliEntity articleTafsiliEntity : getArticleTafsili()){
//				hesabTafsiliLevelNames = hesabTafsiliLevelNames +","+articleTafsiliEntity.getHesabTafsili().getNameWithCode();
//			}
//		}
//		return hesabTafsiliLevelNames;
//	}
//	
//	public String getIndentedArticleTafsiliDesc(){
//		String hesabTafsiliLevelNames="";
//		if(getArticleTafsili()!= null && !getArticleTafsili().isEmpty()){
//			for(ArticleTafsiliEntity articleTafsiliEntity : getArticleTafsili()){
//				hesabTafsiliLevelNames = hesabTafsiliLevelNames +","+articleTafsiliEntity.getHesabTafsili().getName();
//			}
//		}
//		return hesabTafsiliLevelNames;
//	}
//	
//	public String getIndentedArticleTafsiliCodes(){
//		String hesabTafsiliLevelNames="";
//		if(getArticleTafsili()!= null && !getArticleTafsili().isEmpty()){
//			for(ArticleTafsiliEntity articleTafsiliEntity : getArticleTafsili()){
//				hesabTafsiliLevelNames = hesabTafsiliLevelNames +","+articleTafsiliEntity.getHesabTafsili().getCode();
//			}
//		}
//		return hesabTafsiliLevelNames;
//	}
	
//	public String getArticleAccountingMarkazDesc(){
//		String accountingMarkazLevelNames="";
//		if(getArticleAccountingMarkaz()!= null && !getArticleAccountingMarkaz().isEmpty()){
//			for(ArticleAccountingMarkazEntity accountingMarkazEntity : getArticleAccountingMarkaz()){
//				accountingMarkazLevelNames = accountingMarkazLevelNames +","+accountingMarkazEntity.getAccountingMarkaz().getNameWithCode();
//			}
//		}
//		return accountingMarkazLevelNames;
//	}

	public String getSanadSerial(){
		if(getSanadHesabdari().getId()!=null && getSanadHesabdari().getSerial()!=null)
			return getSanadHesabdari().getSerial().toString();
		return "";
	}
	
	public String getTempSanadSerial(){
		if(getSanadHesabdari().getId()!=null && getSanadHesabdari().getTempSerial()!=null)
			return getSanadHesabdari().getTempSerial().toString();
		return "";
	}
	
	public String getSanadTarikh(){
		if(getSanadHesabdari().getId()!=null && getSanadHesabdari().getTarikhSanad()!=null)
			return getSanadHesabdari().getTarikhSanadFA();
		return "";
	}
}