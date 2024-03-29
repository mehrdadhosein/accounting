package ir.serajsamaneh.erpcore.contacthesab;

public class ContactHesabEntity extends BaseContactHesabEntity {
	private static final long serialVersionUID = 1L;

//	private ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity hesabShenavar;

	/* [CONSTRUCTOR MARKER BEGIN] */
	public ContactHesabEntity() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ContactHesabEntity(java.lang.Long id) {
		super(id);
	}

	/* [CONSTRUCTOR MARKER END] */

//	public ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity getHesabShenavar() {
//		return hesabShenavar;
//	}
//
//	public void setHesabShenavar(ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity hesabShenavar) {
//		this.hesabShenavar = hesabShenavar;
//	}

	@Override
	public String toString() {
		if (getId() != null)
			return getContact().getDesc();
		return "";
	}

}