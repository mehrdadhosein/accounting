package ir.serajsamaneh.accounting.moeentafsilitemplate;

import java.io.Serializable;
import ir.serajsamaneh.core.base.BaseEntity;
import serajcomponent.DateConverter;
import serajcomponent.SerajDateTimePickerType;

import java.util.HashSet;



/**
 * This is an object that contains data related to the tb_moeen_tafsili_template table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_moeen_tafsili_template"
 */

public abstract class BaseMoeenTafsiliTemplateEntity  extends BaseEntity<Long>   implements Serializable {

	public static String REF = "MoeenTafsiliTemplateEntity";
	public static String PROP_HESAB_TAFSILI_TEMPLATE = "hesabTafsiliTemplate";
	public static String PROP_ID = "id";
	public static String PROP_LEVEL = "level";
	public static String PROP_HESAB_MOEEN_TEMPLATE = "hesabMoeenTemplate";


	// constructors
	public BaseMoeenTafsiliTemplateEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMoeenTafsiliTemplateEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseMoeenTafsiliTemplateEntity (
		java.lang.Long id,
		ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity hesabTafsiliTemplate,
		ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity hesabMoeenTemplate) {

		this.setId(id);
		this.setHesabTafsiliTemplate(hesabTafsiliTemplate);
		this.setHesabMoeenTemplate(hesabMoeenTemplate);
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
	private ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity hesabMoeenTemplate;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="moeen_tafsili_template_id"
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
	 * Return the value associated with the column: hesab_tafsili_template_id
	 */
	public ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity getHesabTafsiliTemplate () {
		return hesabTafsiliTemplate;
	}

	/**
	 * Set the value related to the column: hesab_tafsili_template_id
	 * @param hesabTafsiliTemplate the hesab_tafsili_template_id value
	 */
	public void setHesabTafsiliTemplate (ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity hesabTafsiliTemplate) {
		this.hesabTafsiliTemplate = hesabTafsiliTemplate;
	}



	/**
	 * Return the value associated with the column: hesab_moeen_template_id
	 */
	public ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity getHesabMoeenTemplate () {
		return hesabMoeenTemplate;
	}

	/**
	 * Set the value related to the column: hesab_moeen_template_id
	 * @param hesabMoeenTemplate the hesab_moeen_template_id value
	 */
	public void setHesabMoeenTemplate (ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity hesabMoeenTemplate) {
		this.hesabMoeenTemplate = hesabMoeenTemplate;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity)) return false;
		else {
			ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity moeenTafsiliTemplateEntity = (ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity) obj;
			if (null == this.getId() || null == moeenTafsiliTemplateEntity.getId()) return false;
			else return (this.getId().equals(moeenTafsiliTemplateEntity.getId()));
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