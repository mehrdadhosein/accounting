package ir.serajsamaneh.accounting.hesabgrouptemplate;

import java.io.Serializable;
import ir.serajsamaneh.core.base.BaseEntity;
import serajcomponent.DateConverter;
import serajcomponent.SerajDateTimePickerType;

import java.util.HashSet;



/**
 * This is an object that contains data related to the tb_hesab_group_template table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_hesab_group_template"
 */

public abstract class BaseHesabGroupTemplateEntity  extends BaseEntity<Long>   implements Serializable {

	public static String REF = "HesabGroupTemplateEntity";
	public static String PROP_NAME = "name";
	public static String PROP_ORGAN = "organ";
	public static String PROP_MAHYAT_GROUP = "mahyatGroup";
	public static String PROP_ID = "id";
	public static String PROP_CODE = "code";


	// constructors
	public BaseHesabGroupTemplateEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseHesabGroupTemplateEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String name;
	private java.lang.Long code;

	// many to one
	private ir.serajsamaneh.core.organ.OrganEntity organ;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="hesab_group_template_id"
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




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof ir.serajsamaneh.accounting.hesabgrouptemplate.HesabGroupTemplateEntity)) return false;
		else {
			ir.serajsamaneh.accounting.hesabgrouptemplate.HesabGroupTemplateEntity hesabGroupTemplateEntity = (ir.serajsamaneh.accounting.hesabgrouptemplate.HesabGroupTemplateEntity) obj;
			if (null == this.getId() || null == hesabGroupTemplateEntity.getId()) return false;
			else return (this.getId().equals(hesabGroupTemplateEntity.getId()));
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