package ir.serajsamaneh.accounting.hesabtafsilitemplate;

import java.io.Serializable;

import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.organ.OrganEntity;
import serajcomponent.DateConverter;
import serajcomponent.SerajDateTimePickerType;

import java.util.HashSet;



/**
 * This is an object that contains data related to the tb_hesab_tafsili_template table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_hesab_tafsili_template"
 */

public abstract class BaseHesabTafsiliTemplateEntity  extends BaseEntity<Long>   implements Serializable {

	public static String REF = "HesabTafsiliTemplateEntity";
	public static String PROP_NAME = "name";
	public static String PROP_DESCRIPTION = "description";
	public static String PROP_TAFSIL_TYPE = "tafsilType";
	public static String PROP_HIDDEN = "hidden";
	public static String PROP_ID = "id";
	public static String PROP_CODE = "code";
	public static String PROP_SCOPE = "scope";


	// constructors
	public BaseHesabTafsiliTemplateEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseHesabTafsiliTemplateEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.Long code;
	private java.lang.String name;
	private java.lang.String description;
	OrganEntity organ;
	
	// collections
	private java.util.Set<ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity> moeenTafsiliTemplate;
	private java.util.Set<ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity> parent;
	private java.util.Set<ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity> childs;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="hesab_tafsili_template_id"
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

	/**
	 * Return the value associated with the column: code
	 */
	public java.lang.Long getCode () {
		return code;
	}

	/**
	 * Set the value related to the column: code
	 * @param code the code value
	 */
	public void setCode (java.lang.Long code) {
		this.code = code;
	}



 
	/**
	 * Return the value associated with the column: name
	 */
	public java.lang.String getName () {
		return name;
	}

	/**
	 * Set the value related to the column: name
	 * @param name the name value
	 */
	public void setName (java.lang.String name) {
		this.name = name;
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
	 * Return the value associated with the column: moeenTafsiliTemplate
	 */
	public java.util.Set<ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity> getMoeenTafsiliTemplate () {
		return moeenTafsiliTemplate;
	}

	/**
	 * Set the value related to the column: moeenTafsiliTemplate
	 * @param moeenTafsiliTemplate the moeenTafsiliTemplate value
	 */
	public void setMoeenTafsiliTemplate (java.util.Set<ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity> moeenTafsiliTemplate) {
		this.moeenTafsiliTemplate = moeenTafsiliTemplate;
	}

	public void addTomoeenTafsiliTemplate (ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity moeenTafsiliTemplateEntity) {
		if (null == getMoeenTafsiliTemplate()) setMoeenTafsiliTemplate(new java.util.TreeSet<ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity>());
		getMoeenTafsiliTemplate().add(moeenTafsiliTemplateEntity);
	}



	/**
	 * Return the value associated with the column: parent
	 */
	public java.util.Set<ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity> getParent () {
		return parent;
	}

	/**
	 * Set the value related to the column: parent
	 * @param parent the parent value
	 */
	public void setParent (java.util.Set<ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity> parent) {
		this.parent = parent;
	}

	public void addToparent (ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity) {
		if (null == getParent()) setParent(new java.util.TreeSet<ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity>());
		getParent().add(hesabTafsiliTemplateEntity);
	}



	/**
	 * Return the value associated with the column: childs
	 */
	public java.util.Set<ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity> getChilds () {
		return childs;
	}

	/**
	 * Set the value related to the column: childs
	 * @param childs the childs value
	 */
	public void setChilds (java.util.Set<ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity> childs) {
		this.childs = childs;
	}

	public void addTochilds (ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity) {
		if (null == getChilds()) setChilds(new java.util.TreeSet<ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity>());
		getChilds().add(hesabTafsiliTemplateEntity);
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity)) return false;
		else {
			ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity = (ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity) obj;
			if (null == this.getId() || null == hesabTafsiliTemplateEntity.getId()) return false;
			else return (this.getId().equals(hesabTafsiliTemplateEntity.getId()));
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