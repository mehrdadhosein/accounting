package ir.serajsamaneh.erpcore.contacthesab;

import java.io.Serializable;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity;
import ir.serajsamaneh.accounting.accountingmarkaztemplate.AccountingMarkazTemplateEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.core.base.BaseEntity;
import serajcomponent.DateConverter;
import serajcomponent.SerajDateTimePickerType;

import java.util.HashSet;



/**
 * This is an object that contains data related to the tb_contact_hesab table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_contact_hesab"
 */

public abstract class BaseContactHesabEntity  extends BaseEntity<Long>   implements Serializable {

	public static String REF = "ContactHesabEntity";
	public static String PROP_HESAB_MOEEN = "hesabMoeen";
	public static String PROP_HESAB_TAFSILI = "hesabTafsili";
	public static String PROP_CONTACT = "contact";
	public static String PROP_ID = "id";


	// constructors
	public BaseContactHesabEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseContactHesabEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseContactHesabEntity (
		java.lang.Long id,
		ir.serajsamaneh.core.contact.contact.ContactEntity contact) {

		this.setId(id);
		this.setContact(contact);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// many to one
	private ir.serajsamaneh.core.contact.contact.ContactEntity contact;
	private ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity hesabMoeen;
//	private ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity hesabTafsili;
	private ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity hesabTafsiliOne;
	private ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity hesabTafsiliTwo;
	SaalMaaliEntity saalMaali;

	private AccountingMarkazTemplateEntity accountingMarkazTemplate; 


	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="contact_hesab_id"
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




	public AccountingMarkazTemplateEntity getAccountingMarkazTemplate() {
		return accountingMarkazTemplate;
	}

	public void setAccountingMarkazTemplate(
			AccountingMarkazTemplateEntity accountingMarkazTemplate) {
		this.accountingMarkazTemplate = accountingMarkazTemplate;
	}


	/**
	 * Return the value associated with the column: contact_id
	 */
	public ir.serajsamaneh.core.contact.contact.ContactEntity getContact () {
		return contact;
	}

	/**
	 * Set the value related to the column: contact_id
	 * @param contact the contact_id value
	 */
	public void setContact (ir.serajsamaneh.core.contact.contact.ContactEntity contact) {
		this.contact = contact;
	}



	public SaalMaaliEntity getSaalMaali() {
		return saalMaali;
	}

	public void setSaalMaali(SaalMaaliEntity saalMaali) {
		this.saalMaali = saalMaali;
	}

	/**
	 * Return the value associated with the column: hesab_moeen_id
	 */
	public ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity getHesabMoeen () {
		return hesabMoeen;
	}

	/**
	 * Set the value related to the column: hesab_moeen_id
	 * @param hesabMoeen the hesab_moeen_id value
	 */
	public void setHesabMoeen (ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity hesabMoeen) {
		this.hesabMoeen = hesabMoeen;
	}


	public ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity getHesabTafsiliOne() {
		return hesabTafsiliOne;
	}

	public void setHesabTafsiliOne(ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity hesabTafsiliOne) {
		this.hesabTafsiliOne = hesabTafsiliOne;
	}

	public ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity getHesabTafsiliTwo() {
		return hesabTafsiliTwo;
	}

	public void setHesabTafsiliTwo(ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity hesabTafsiliTwo) {
		this.hesabTafsiliTwo = hesabTafsiliTwo;
	}

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof ir.serajsamaneh.erpcore.contacthesab.ContactHesabEntity)) return false;
		else {
			ir.serajsamaneh.erpcore.contacthesab.ContactHesabEntity contactHesabEntity = (ir.serajsamaneh.erpcore.contacthesab.ContactHesabEntity) obj;
			if (null == this.getId() || null == contactHesabEntity.getId()) return false;
			else return (this.getId().equals(contactHesabEntity.getId()));
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