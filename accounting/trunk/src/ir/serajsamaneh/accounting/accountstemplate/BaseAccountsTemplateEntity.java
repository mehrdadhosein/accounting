package ir.serajsamaneh.accounting.accountstemplate;

import ir.serajsamaneh.accounting.accountingmarkaztemplate.AccountingMarkazTemplateEntity;
import ir.serajsamaneh.accounting.sanadhesabdariitemtemplate.SanadHesabdariItemTemplateEntity;
import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.organ.OrganEntity;

import java.io.Serializable;


/**
 * This is an object that contains data related to the tb_sanad_type table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_sanad_type"
 */

public abstract class BaseAccountsTemplateEntity  extends BaseEntity<Long>   implements Serializable {

	public static String REF = "AccountsTemplateEntity";
	public static String PROP_ACTION_ID = "actionId";
	public static String PROP_ACTION_NAME = "actionName";
	public static String PROP_ID = "id";


	// constructors
	public BaseAccountsTemplateEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseAccountsTemplateEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String actionId;
	private java.lang.String actionName;
	private java.lang.String description;
	private SanadHesabdariItemTemplateEntity sanadHesabdariItemTemplateBedehkar;  
	private SanadHesabdariItemTemplateEntity sanadHesabdariItemTemplateBestankar;  
	private AccountingMarkazTemplateEntity accountingMarkazTemplate;  

	private OrganEntity organ;


	public AccountingMarkazTemplateEntity getAccountingMarkazTemplate() {
		return accountingMarkazTemplate;
	}

	public void setAccountingMarkazTemplate(
			AccountingMarkazTemplateEntity accountingMarkazTemplate) {
		this.accountingMarkazTemplate = accountingMarkazTemplate;
	}

	public SanadHesabdariItemTemplateEntity getSanadHesabdariItemTemplateBedehkar() {
		return sanadHesabdariItemTemplateBedehkar;
	}

	public void setSanadHesabdariItemTemplateBedehkar(
			SanadHesabdariItemTemplateEntity sanadHesabdariItemTemplateBedehkar) {
		this.sanadHesabdariItemTemplateBedehkar = sanadHesabdariItemTemplateBedehkar;
	}

	public SanadHesabdariItemTemplateEntity getSanadHesabdariItemTemplateBestankar() {
		return sanadHesabdariItemTemplateBestankar;
	}

	public void setSanadHesabdariItemTemplateBestankar(
			SanadHesabdariItemTemplateEntity sanadHesabdariItemTemplateBestankar) {
		this.sanadHesabdariItemTemplateBestankar = sanadHesabdariItemTemplateBestankar;
	}


	public java.lang.String getActionName() {
		return actionName;
	}

	public void setActionName(java.lang.String actionName) {
		this.actionName = actionName;
	}

	public java.lang.String getDescription() {
		return description;
	}

	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	public java.lang.String getActionId() {
		return actionId;
	}

	public void setActionId(java.lang.String actionId) {
		this.actionId = actionId;
	}
	public OrganEntity getOrgan() {
		return organ;
	}

	public void setOrgan(OrganEntity organ) {
		this.organ = organ;
	}
	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="sanad_type_id"
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





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof ir.serajsamaneh.accounting.sanadtype.SanadTypeEntity)) return false;
		else {
			ir.serajsamaneh.accounting.sanadtype.SanadTypeEntity sanadTypeEntity = (ir.serajsamaneh.accounting.sanadtype.SanadTypeEntity) obj;
			if (null == this.getId() || null == sanadTypeEntity.getId()) return false;
			else return (this.getId().equals(sanadTypeEntity.getId()));
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