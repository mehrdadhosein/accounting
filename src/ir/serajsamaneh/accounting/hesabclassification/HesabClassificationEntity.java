package ir.serajsamaneh.accounting.hesabclassification;

import ir.serajsamaneh.accounting.hesabclassification.BaseHesabClassificationEntity;



public class HesabClassificationEntity extends BaseHesabClassificationEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public HesabClassificationEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public HesabClassificationEntity (java.lang.Long id) {
		super(id);
	}



/*[CONSTRUCTOR MARKER END]*/

	@Override
	public String toString() {
		if(getId()!=null)
			return getName();
		return "";
	}

}