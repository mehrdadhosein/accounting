package ir.serajsamaneh.erpcore.contacthesab;

import ir.serajsamaneh.erpcore.contacthesab.BaseContactHesabEntity;



public class ContactHesabEntity extends BaseContactHesabEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ContactHesabEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ContactHesabEntity (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ContactHesabEntity (
		java.lang.Long id,
		ir.serajsamaneh.core.contact.contact.ContactEntity contact) {

		super (
			id,
			contact);
	}

/*[CONSTRUCTOR MARKER END]*/

	@Override
	public String toString() {
		if(getId()!=null)
			return getContact().getDesc();
		return "";
	}

}