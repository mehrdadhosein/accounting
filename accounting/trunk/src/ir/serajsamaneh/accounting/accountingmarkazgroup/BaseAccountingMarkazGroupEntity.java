package ir.serajsamaneh.accounting.accountingmarkazgroup;

import ir.serajsamaneh.accounting.accountingmarkaztemplate.AccountingMarkazTemplateEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity;
import ir.serajsamaneh.core.base.BaseEntity;

import java.io.Serializable;


/**
 * This is an object that contains data related to the tb_group_kala table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_group_kala"
 */

public abstract class BaseAccountingMarkazGroupEntity  extends BaseEntity<Long>   implements Serializable {

	public static String REF = "GroupKalaEntity";
	public static String PROP_NAME = "name";
	public static String PROP_PARENT = "parent";
	public static String PROP_ID = "id";
	public static String PROP_ORGAN = "organ";
	public static String PROP_CODE = "code";


	// constructors
	public BaseAccountingMarkazGroupEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseAccountingMarkazGroupEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}
	
	/**
	 * Constructor for required key
	 */
	public BaseAccountingMarkazGroupEntity (java.lang.Long id, 
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
	private java.lang.String code;
	private java.lang.String description;
	private java.lang.Integer level;
	
	// many to one
	private ir.serajsamaneh.core.organ.OrganEntity organ;
	private AccountingMarkazGroupEntity parent;
	private java.util.Set<AccountingMarkazTemplateEntity> accountingMarkazTemplates;


	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="group_kala_id"
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




	public java.lang.Integer getLevel() {
		return level;
	}

	public void setLevel(java.lang.Integer level) {
		this.level = level;
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



	public java.lang.String getDescription() {
		return description;
	}

	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	/**
	 * Return the value associated with the column: code
	 */
	public java.lang.String getCode () {
		return code;
	}

	/**
	 * Set the value related to the column: code
	 * @param code the code value
	 */
	public void setCode (java.lang.String code) {
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



	/**
	 * Return the value associated with the column: parent_id
	 */
	public AccountingMarkazGroupEntity getParent () {
		return parent;
	}

	/**
	 * Set the value related to the column: parent_id
	 * @param parent the parent_id value
	 */
	public void setParent (AccountingMarkazGroupEntity parent) {
		this.parent = parent;
	}




	public java.util.Set<AccountingMarkazTemplateEntity> getAccountingMarkazTemplates() {
		return accountingMarkazTemplates;
	}

	public void setAccountingMarkazTemplates(java.util.Set<AccountingMarkazTemplateEntity> accountingMarkazTemplates) {
		this.accountingMarkazTemplates = accountingMarkazTemplates;
	}

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof AccountingMarkazGroupEntity)) return false;
		else {
			AccountingMarkazGroupEntity groupKalaEntity = (AccountingMarkazGroupEntity) obj;
			if (null == this.getId() || null == groupKalaEntity.getId()) return false;
			else return (this.getId().equals(groupKalaEntity.getId()));
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