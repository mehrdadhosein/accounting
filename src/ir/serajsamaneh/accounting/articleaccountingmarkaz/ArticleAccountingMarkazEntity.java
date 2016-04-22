package ir.serajsamaneh.accounting.articleaccountingmarkaz;




public class ArticleAccountingMarkazEntity extends BaseArticleAccountingMarkazEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ArticleAccountingMarkazEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ArticleAccountingMarkazEntity (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ArticleAccountingMarkazEntity (
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
			return getAccountingMarkaz().getDesc();
		return "";
	}


}