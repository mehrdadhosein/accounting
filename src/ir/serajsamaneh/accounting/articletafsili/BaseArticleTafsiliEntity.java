package ir.serajsamaneh.accounting.articletafsili;

import java.io.Serializable;
import ir.serajsamaneh.core.base.BaseEntity;
import serajcomponent.DateConverter;
import serajcomponent.SerajDateTimePickerType;

import java.util.HashSet;



/**
 * This is an object that contains data related to the tb_article_tafsili table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_article_tafsili"
 */

public abstract class BaseArticleTafsiliEntity  extends BaseEntity<Long>   implements Serializable {

	public static String REF = "ArticleTafsiliEntity";
	public static String PROP_SANAD_HESABDARI_ITEM = "sanadHesabdariItem";
	public static String PROP_HESAB_TAFSILI = "hesabTafsili";
	public static String PROP_ID = "id";
	public static String PROP_LEVEL = "level";


	// constructors
	public BaseArticleTafsiliEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseArticleTafsiliEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseArticleTafsiliEntity (
		java.lang.Long id,
		ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity hesabTafsili,
		ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity sanadHesabdariItem) {

		this.setId(id);
		this.setHesabTafsili(hesabTafsili);
		this.setSanadHesabdariItem(sanadHesabdariItem);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.Integer level;

	// many to one
	private ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity hesabTafsili;
	private ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity sanadHesabdariItem;



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
	public ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity getHesabTafsili () {
		return hesabTafsili;
	}

	/**
	 * Set the value related to the column: hesab_tafsili_id
	 * @param hesabTafsili the hesab_tafsili_id value
	 */
	public void setHesabTafsili (ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity hesabTafsili) {
		this.hesabTafsili = hesabTafsili;
	}



	/**
	 * Return the value associated with the column: sanad_hesabdari_item_id
	 */
	public ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity getSanadHesabdariItem () {
		return sanadHesabdariItem;
	}

	/**
	 * Set the value related to the column: sanad_hesabdari_item_id
	 * @param sanadHesabdariItem the sanad_hesabdari_item_id value
	 */
	public void setSanadHesabdariItem (ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity sanadHesabdariItem) {
		this.sanadHesabdariItem = sanadHesabdariItem;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof ir.serajsamaneh.accounting.articletafsili.ArticleTafsiliEntity)) return false;
		else {
			ir.serajsamaneh.accounting.articletafsili.ArticleTafsiliEntity articleTafsiliEntity = (ir.serajsamaneh.accounting.articletafsili.ArticleTafsiliEntity) obj;
			if (null == this.getId() || null == articleTafsiliEntity.getId()) return false;
			else return (this.getId().equals(articleTafsiliEntity.getId()));
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