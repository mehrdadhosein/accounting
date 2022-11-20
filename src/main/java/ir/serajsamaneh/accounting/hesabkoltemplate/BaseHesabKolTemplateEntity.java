package ir.serajsamaneh.accounting.hesabkoltemplate;

import java.io.Serializable;

import ir.serajsamaneh.core.base.BaseEntity;

/**
 * This is an object that contains data related to the tb_hesab_kol_template
 * table. Do not modify this class because it will be overwritten if the
 * configuration file related to this class is modified.
 *
 * @hibernate.class table="tb_hesab_kol_template"
 */

public abstract class BaseHesabKolTemplateEntity extends BaseEntity<Long> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 444256700175722350L;
	public static String REF = "HesabKolTemplateEntity";
	public static String PROP_NAME = "name";
	public static String PROP_DESCRIPTION = "description";
	public static String PROP_HESAB_GROUP = "hesabGroup";
	public static String PROP_MAHYAT_KOL = "mahyatKol";
	public static String PROP_HIDDEN = "hidden";
	public static String PROP_ID = "id";
	public static String PROP_CODE = "code";

	// constructors
	public BaseHesabKolTemplateEntity() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseHesabKolTemplateEntity(java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String code;
	private java.lang.String name;
	private java.lang.String description;
	Long organId;
	String organName;

	// many to one
	private ir.serajsamaneh.accounting.hesabgrouptemplate.HesabGroupTemplateEntity hesabGroupTemplate;
//	OrganEntity organ;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="increment" column="hesab_kol_template_id"
	 */
	public java.lang.Long getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param id the new ID
	 */
	public void setId(java.lang.Long id) {
		this.id = id;
		setID(id);
		this.hashCode = Integer.MIN_VALUE;
	}

	public ir.serajsamaneh.accounting.hesabgrouptemplate.HesabGroupTemplateEntity getHesabGroupTemplate() {
		return hesabGroupTemplate;
	}

	public void setHesabGroupTemplate(
			ir.serajsamaneh.accounting.hesabgrouptemplate.HesabGroupTemplateEntity hesabGroupTemplate) {
		this.hesabGroupTemplate = hesabGroupTemplate;
	}

	public Long getOrganId() {
		return organId;
	}

	public void setOrganId(Long organId) {
		this.organId = organId;
	}

//	public OrganEntity getOrgan() {
//		return organ;
//	}
//
//	public void setOrgan(OrganEntity organ) {
//		this.organ = organ;
//	}

	public String getOrganName() {
		return organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	/**
	 * Return the value associated with the column: code
	 */
	public java.lang.String getCode() {
		return code;
	}

	/**
	 * Set the value related to the column: code
	 * 
	 * @param code the code value
	 */
	public void setCode(java.lang.String code) {
		this.code = code;
	}

	/**
	 * Return the value associated with the column: name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Set the value related to the column: name
	 * 
	 * @param name the name value
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Return the value associated with the column: description
	 */
	public java.lang.String getDescription() {
		return description;
	}

	/**
	 * Set the value related to the column: description
	 * 
	 * @param description the description value
	 */
	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateEntity))
			return false;
		else {
			ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateEntity hesabKolTemplateEntity = (ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateEntity) obj;
			if (null == this.getId() || null == hesabKolTemplateEntity.getId())
				return false;
			else
				return (this.getId().equals(hesabKolTemplateEntity.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

}