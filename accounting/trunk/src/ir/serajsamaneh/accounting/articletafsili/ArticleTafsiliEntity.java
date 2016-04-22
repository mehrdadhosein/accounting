package ir.serajsamaneh.accounting.articletafsili;

import ir.serajsamaneh.accounting.articletafsili.BaseArticleTafsiliEntity;



public class ArticleTafsiliEntity extends BaseArticleTafsiliEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ArticleTafsiliEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ArticleTafsiliEntity (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ArticleTafsiliEntity (
		java.lang.Long id,
		ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity hesabTafsili,
		ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity sanadHesabdariItem) {

		super (
			id,
			hesabTafsili,
			sanadHesabdariItem);
	}

/*[CONSTRUCTOR MARKER END]*/

	@Override
	public String toString() {
		if(getId()!= null)
			return getHesabTafsili().getDesc();
		return "";
	}

}