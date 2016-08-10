package ir.serajsamaneh.accounting.month;

import ir.serajsamaneh.accounting.month.BaseMonthEntity;



public class MonthEntity extends BaseMonthEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public MonthEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public MonthEntity (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public MonthEntity (
		java.lang.Long id,
		ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity saalMaali) {

		super (
			id,
			saalMaali);
	}

/*[CONSTRUCTOR MARKER END]*/


}