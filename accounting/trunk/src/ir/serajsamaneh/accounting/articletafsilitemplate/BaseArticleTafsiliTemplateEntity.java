package ir.serajsamaneh.accounting.articletafsilitemplate;

import java.io.Serializable;

import ir.serajsamaneh.core.base.BaseEntity;



/**
 * This is an object that contains data related to the tb_article_tafsili table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_article_tafsili"
 */

public abstract class BaseArticleTafsiliTemplateEntity  extends BaseEntity<Long>   implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8959305635354432658L;
	public static String REF = "ArticleTafsiliEntity";
	public static String PROP_SANAD_HESABDARI_ITEM = "sanadHesabdariItem";
	public static String PROP_HESAB_TAFSILI = "hesabTafsili";
	public static String PROP_ID = "id";
	public static String PROP_LEVEL = "level";


	// constructors
	public BaseArticleTafsiliTemplateEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseArticleTafsiliTemplateEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}



	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.Integer level;

	// many to one
	private ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity hesabTafsiliTemplate;
	private ir.serajsamaneh.accounting.sanadhesabdariitemtemplate.SanadHesabdariItemTemplateEntity sanadHesabdariItemTemplate;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="article_tafsili_id"
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



 
	/**
	 * Return the value associated with the column: level
	 */
	public java.lang.Integer getLevel () {
		return level;
	}

	/**
	 * Set the value related to the column: level
	 * @param level the level value
	 */
	public void setLevel (java.lang.Integer level) {
		this.level = level;
	}




	/**
	 * Return the value associated with the column: hesab_tafsili_id
	 */
	public ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity getHesabTafsiliTemplate () {
		return hesabTafsiliTemplate;
	}

	/**
	 * Set the value related to the column: hesab_tafsili_id
	 * @param hesabTafsili the hesab_tafsili_id value
	 */
	public void setHesabTafsiliTemplate (ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity hesabTafsiliTemplate) {
		this.hesabTafsiliTemplate = hesabTafsiliTemplate;
	}



	/**
	 * Return the value associated with the column: sanad_hesabdari_item_id
	 */
	public ir.serajsamaneh.accounting.sanadhesabdariitemtemplate.SanadHesabdariItemTemplateEntity getSanadHesabdariItemTemplate () {
		return sanadHesabdariItemTemplate;
	}

	/**
	 * Set the value related to the column: sanad_hesabdari_item_id
	 * @param sanadHesabdariItem the sanad_hesabdari_item_id value
	 */
	public void setSanadHesabdariItemTemplate (ir.serajsamaneh.accounting.sanadhesabdariitemtemplate.SanadHesabdariItemTemplateEntity sanadHesabdariItemTemplate) {
		this.sanadHesabdariItemTemplate = sanadHesabdariItemTemplate;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof ir.serajsamaneh.accounting.articletafsilitemplate.ArticleTafsiliTemplateEntity)) return false;
		else {
			ir.serajsamaneh.accounting.articletafsilitemplate.ArticleTafsiliTemplateEntity articleTafsiliTemplateEntity = (ir.serajsamaneh.accounting.articletafsilitemplate.ArticleTafsiliTemplateEntity) obj;
			if (null == this.getId() || null == articleTafsiliTemplateEntity.getId()) return false;
			else return (this.getId().equals(articleTafsiliTemplateEntity.getId()));
		}
	}

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