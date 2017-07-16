package ir.serajsamaneh.accounting.hesabgroup;

import ir.serajsamaneh.accounting.hesabgrouptemplate.HesabGroupTemplateEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.organ.OrganEntity;

import java.io.Serializable;



/**
 * This is an object that contains data related to the tb_hesab_group table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_hesab_group"
 */

public abstract class BaseHesabGroupEntity  extends BaseEntity<Long>   implements Serializable {

	public static String REF = "HesabGroupEntity";
	public static String PROP_NAME = "name";
	public static String PROP_MAHYAT_GROUP = "mahyatGroup";
	public static String PROP_LENGTH = "length";
	public static String PROP_ID = "id";
	public static String PROP_TYPE = "type";	
	public static String PROP_CODE = "code";


	// constructors
	public BaseHesabGroupEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseHesabGroupEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String name;
	private java.lang.Integer length;
	
	SaalMaaliEntity saalMaali;
	OrganEntity organ;
	HesabGroupTemplateEntity hesabGroupTemplate;

	public HesabGroupTemplateEntity getHesabGroupTemplate() {
		return hesabGroupTemplate;
	}

	public void setHesabGroupTemplate(HesabGroupTemplateEntity hesabGroupTemplate) {
		this.hesabGroupTemplate = hesabGroupTemplate;
	}



	private Long code;
	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="hesab_group_id"
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



 
	public SaalMaaliEntity getSaalMaali() {
		return saalMaali;
	}

	public void setSaalMaali(SaalMaaliEntity saalMaali) {
		this.saalMaali = saalMaali;
	}

	public OrganEntity getOrgan() {
		return organ;
	}

	public void setOrgan(OrganEntity organ) {
		this.organ = organ;
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
	 * Return the value associated with the column: length
	 */
	public java.lang.Integer getLength () {
		return length;
	}

	/**
	 * Set the value related to the column: length
	 * @param length the length value
	 */
	public void setLength (java.lang.Integer length) {
		this.length = length;
	}


	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof ir.serajsamaneh.accounting.hesabgroup.HesabGroupEntity)) return false;
		else {
			ir.serajsamaneh.accounting.hesabgroup.HesabGroupEntity hesabGroupEntity = (ir.serajsamaneh.accounting.hesabgroup.HesabGroupEntity) obj;
			if (null == this.getId() || null == hesabGroupEntity.getId()) return false;
			else return (this.getId().equals(hesabGroupEntity.getId()));
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