package ir.serajsamaneh.accounting.hesabclassification;

import ir.serajsamaneh.core.base.BaseEntity;

import java.io.Serializable;



/**
 * This is an object that contains data related to the tb_hesab_classification table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_hesab_classification"
 */

public abstract class BaseHesabClassificationEntity  extends BaseEntity<Long>   implements Serializable {

	public static String REF = "HesabClassificationEntity";
	public static String PROP_NAME = "name";
	public static String PROP_DESCRIPTION = "description";
	public static String PROP_ORGAN = "organ";
	public static String PROP_ID = "id";


	// constructors
	public BaseHesabClassificationEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseHesabClassificationEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseHesabClassificationEntity (
		java.lang.Long id,
		ir.serajsamaneh.core.organ.OrganEntity organ) {

		this.setId(id);
		this.setOrgan(organ);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String name;
	private java.lang.String description;
	Long code;

	// many to one
	private ir.serajsamaneh.core.organ.OrganEntity organ;

	// collections
	private java.util.Set<ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity> hesabTafsiliTemplate;
	private java.util.Set<ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity> hesabMoeenTemplate;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="hesab_classification_id"
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



 
	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
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
	 * Return the value associated with the column: organ_id
	 */
	public ir.serajsamaneh.core.organ.OrganEntity getOrgan () {
		return organ;
	}

	/**
	 * Set the value related to the column: organ_id
	 * @param organ the organ_id value
	 */
	public void setOrgan (ir.serajsamaneh.core.organ.OrganEntity organ) {
		this.organ = organ;
	}



	public java.util.Set<ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity> getHesabTafsiliTemplate() {
		return hesabTafsiliTemplate;
	}

	public void setHesabTafsiliTemplate(
			java.util.Set<ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity> hesabTafsiliTemplate) {
		this.hesabTafsiliTemplate = hesabTafsiliTemplate;
	}

	public java.util.Set<ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity> getHesabMoeenTemplate() {
		return hesabMoeenTemplate;
	}

	public void setHesabMoeenTemplate(
			java.util.Set<ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity> hesabMoeenTemplate) {
		this.hesabMoeenTemplate = hesabMoeenTemplate;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof ir.serajsamaneh.accounting.hesabclassification.HesabClassificationEntity)) return false;
		else {
			ir.serajsamaneh.accounting.hesabclassification.HesabClassificationEntity hesabClassificationEntity = (ir.serajsamaneh.accounting.hesabclassification.HesabClassificationEntity) obj;
			if (null == this.getId() || null == hesabClassificationEntity.getId()) return false;
			else return (this.getId().equals(hesabClassificationEntity.getId()));
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