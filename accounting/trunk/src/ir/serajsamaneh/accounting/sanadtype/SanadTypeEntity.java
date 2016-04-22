package ir.serajsamaneh.accounting.sanadtype;




public class SanadTypeEntity extends BaseSanadTypeEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public SanadTypeEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public SanadTypeEntity (java.lang.Long id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/
	@Override
	public String toString() {
		return getName();
	}

}
