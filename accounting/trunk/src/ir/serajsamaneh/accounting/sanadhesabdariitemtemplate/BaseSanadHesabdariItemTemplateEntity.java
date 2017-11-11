package ir.serajsamaneh.accounting.sanadhesabdariitemtemplate;

import java.io.Serializable;

import ir.serajsamaneh.accounting.accountingmarkaztemplate.AccountingMarkazTemplateEntity;
import ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity;
import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.organ.OrganEntity;
import serajcomponent.DateConverter;
import serajcomponent.SerajDateTimePickerType;



/**
 * This is an object that contains data related to the tb_sanad_hesabdari_item table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_sanad_hesabdari_item"
 */

public abstract class BaseSanadHesabdariItemTemplateEntity  extends BaseEntity<Long>   implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6012549268108506327L;
	public static String REF = "SanadHesabdariItemEntity";
	public static String PROP_HESAB_KOL = "hesabKol";
	public static String PROP_TARIKH_ARTICLE = "tarikhArticle";
	public static String PROP_SANAD_HESABDARI = "sanadHesabdari";
	public static String PROP_DESCRIPTION = "description";
	public static String PROP_HESAB_MOEEN = "hesabMoeen";
	public static String PROP_HESAB_TAFSILI = "hesabTafsili";
	public static String PROP_HESAB_TAFSILI_TWO = "hesabTafsiliTemplateTwo";
	public static String PROP_TYPE = "type";
	public static String PROP_ROW = "row";
	public static String PROP_BEDEHKAR = "bedehkar";
	public static String PROP_ID = "id";
	public static String PROP_BESTANKAR = "bestankar";


	// constructors
	public BaseSanadHesabdariItemTemplateEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseSanadHesabdariItemTemplateEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

//	/**
//	 * Constructor for required fields
//	 */
//	public BaseSanadHesabdariItemTemplateEntity (
//		java.lang.Long id,
//		ir.serajsamaneh.accounting.sanadhesabdari.SanadHesabdariEntity sanadHesabdari) {
//
//		this.setId(id);
//		this.setSanadHesabdari(sanadHesabdari);
//		initialize();
//	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.Integer row;
	private java.lang.String description;
	private java.lang.String type;
	private java.lang.Double bestankar;
	private java.lang.Double bedehkar;
	private java.util.Date tarikhArticle;
	private java.lang.String templateType;  //bedehkar OR bestankar

	// many to one
	private AccountingMarkazTemplateEntity accountingMarkazTemplate; 
//	private ir.serajsamaneh.accounting.sanadhesabdari.SanadHesabdariEntity sanadHesabdari;

	private HesabKolTemplateEntity hesabKolTemplate;
	private HesabMoeenTemplateEntity hesabMoeenTemplate;
	private HesabTafsiliTemplateEntity hesabTafsiliTemplate;
	private HesabTafsiliTemplateEntity hesabTafsiliTemplateTwo;

//	private java.util.Set<ArticleTafsiliTemplateEntity> articleTafsiliTemplate;
	
	OrganEntity organ;
	// collections
//	private java.util.Set<ir.serajsamaneh.accounting.articletafsili.ArticleTafsiliEntity> articleTafsili;



	public java.lang.String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(java.lang.String templateType) {
		this.templateType = templateType;
	}

	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="sanad_hesabdari_item_id"
     */
	public java.lang.Long getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Long id) {
		this.id = id;
		setID(id);
		this.hashCode = Integer.MIN_VALUE;
	}



 
	public OrganEntity getOrgan() {
		return organ;
	}

	public void setOrgan(OrganEntity organ) {
		this.organ = organ;
	}

	public AccountingMarkazTemplateEntity getAccountingMarkazTemplate() {
		return accountingMarkazTemplate;
	}

	public void setAccountingMarkazTemplate(
			AccountingMarkazTemplateEntity accountingMarkazTemplate) {
		this.accountingMarkazTemplate = accountingMarkazTemplate;
	}

	/**
	 * Return the value associated with the column: row
	 */
	public java.lang.Integer getRow () {
		return row;
	}

	/**
	 * Set the value related to the column: row
	 * @param row the row value
	 */
	public void setRow (java.lang.Integer row) {
		this.row = row;
	}



 
	/**
	 * Return the value associated with the column: description
	 */
	public java.lang.String getDescription () {
		return description;
	}

	/**
	 * Set the value related to the column: description
	 * @param description the description value
	 */
	public void setDescription (java.lang.String description) {
		this.description = description;
	}



 
	/**
	 * Return the value associated with the column: type
	 */
	public java.lang.String getType () {
		return type;
	}

	/**
	 * Set the value related to the column: type
	 * @param type the type value
	 */
	public void setType (java.lang.String type) {
		this.type = type;
	}



 
	/**
	 * Return the value associated with the column: bestankar
	 */
	public java.lang.Double getBestankar () {
		return bestankar;
	}

	/**
	 * Set the value related to the column: bestankar
	 * @param bestankar the bestankar value
	 */
	public void setBestankar (java.lang.Double bestankar) {
		this.bestankar = bestankar;
	}



 
	/**
	 * Return the value associated with the column: bedehkar
	 */
	public java.lang.Double getBedehkar () {
		return bedehkar;
	}

	/**
	 * Set the value related to the column: bedehkar
	 * @param bedehkar the bedehkar value
	 */
	public void setBedehkar (java.lang.Double bedehkar) {
		this.bedehkar = bedehkar;
	}



 
	/**
	 * Return the value associated with the column: tarikh_article
	 */
	public java.util.Date getTarikhArticle () {
		return tarikhArticle;
	}

	/**
	 * Set the value related to the column: tarikh_article
	 * @param tarikhArticle the tarikh_article value
	 */
	public void setTarikhArticle (java.util.Date tarikhArticle) {
		this.tarikhArticle = tarikhArticle;
	}



	public String  getTarikhArticleFA() {
			if(tarikhArticle == null)
			return null;
			return DateConverter.toShamsiDate(tarikhArticle,SerajDateTimePickerType.Date );
	}

	public HesabKolTemplateEntity getHesabKolTemplate() {
		return hesabKolTemplate;
	}

	public void setHesabKolTemplate(HesabKolTemplateEntity hesabKolTemplate) {
		this.hesabKolTemplate = hesabKolTemplate;
	}

	public HesabMoeenTemplateEntity getHesabMoeenTemplate() {
		return hesabMoeenTemplate;
	}

	public void setHesabMoeenTemplate(HesabMoeenTemplateEntity hesabMoeenTemplate) {
		this.hesabMoeenTemplate = hesabMoeenTemplate;
	}

	public HesabTafsiliTemplateEntity getHesabTafsiliTemplate() {
		return hesabTafsiliTemplate;
	}

	public void setHesabTafsiliTemplate(
			HesabTafsiliTemplateEntity hesabTafsiliTemplate) {
		this.hesabTafsiliTemplate = hesabTafsiliTemplate;
	}



//	/**
//	 * Return the value associated with the column: sanad_hesabdari_id
//	 */
//	public ir.serajsamaneh.accounting.sanadhesabdari.SanadHesabdariEntity getSanadHesabdari () {
//		return sanadHesabdari;
//	}
//
//	/**
//	 * Set the value related to the column: sanad_hesabdari_id
//	 * @param sanadHesabdari the sanad_hesabdari_id value
//	 */
//	public void setSanadHesabdari (ir.serajsamaneh.accounting.sanadhesabdari.SanadHesabdariEntity sanadHesabdari) {
//		this.sanadHesabdari = sanadHesabdari;
//	}



//	public java.util.Set<ArticleTafsiliTemplateEntity> getArticleTafsiliTemplate() {
//		return articleTafsiliTemplate;
//	}
//
//	public void setArticleTafsiliTemplate(java.util.Set<ArticleTafsiliTemplateEntity> articleTafsiliTemplate) {
//		this.articleTafsiliTemplate = articleTafsiliTemplate;
//	}

	public HesabTafsiliTemplateEntity getHesabTafsiliTemplateTwo() {
		return hesabTafsiliTemplateTwo;
	}

	public void setHesabTafsiliTemplateTwo(HesabTafsiliTemplateEntity hesabTafsiliTemplateTwo) {
		this.hesabTafsiliTemplateTwo = hesabTafsiliTemplateTwo;
	}

	/*	*//**
	 * Return the value associated with the column: articleTafsili
	 *//*
	public java.util.Set<ir.serajsamaneh.accounting.articletafsili.ArticleTafsiliEntity> getArticleTafsili () {
		return articleTafsili;
	}

	*//**
	 * Set the value related to the column: articleTafsili
	 * @param articleTafsili the articleTafsili value
	 *//*
	public void setArticleTafsili (java.util.Set<ir.serajsamaneh.accounting.articletafsili.ArticleTafsiliEntity> articleTafsili) {
		this.articleTafsili = articleTafsili;
	}*/

/*	public void addToarticleTafsili (ir.serajsamaneh.accounting.articletafsili.ArticleTafsiliEntity articleTafsiliEntity) {
		if (null == getArticleTafsili()) setArticleTafsili(new java.util.TreeSet<ir.serajsamaneh.accounting.articletafsili.ArticleTafsiliEntity>());
		getArticleTafsili().add(articleTafsiliEntity);
	}*/

//	public void addToarticleTafsiliTemplate (ir.serajsamaneh.accounting.articletafsilitemplate.ArticleTafsiliTemplateEntity articleTafsiliTemplateEntity) {
//		if (null == getArticleTafsiliTemplate()) setArticleTafsiliTemplate(new java.util.TreeSet<ir.serajsamaneh.accounting.articletafsilitemplate.ArticleTafsiliTemplateEntity>());
//		getArticleTafsiliTemplate().add(articleTafsiliTemplateEntity);
//	}


//	public boolean equals (Object obj) {
//		if (null == obj) return false;
//		if (!(obj instanceof ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity)) return false;
//		else {
//			ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity sanadHesabdariItemEntity = (ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity) obj;
//			if (null == this.getId() || null == sanadHesabdariItemEntity.getId()) return false;
//			else return (this.getId().equals(sanadHesabdariItemEntity.getId()));
//		}
//	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}


	public String toString () {
		return super.toString();
	}


}