package ir.serajsamaneh.accounting.articletafsilitemplate;

public class ArticleTafsiliTemplateEntity extends BaseArticleTafsiliTemplateEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ArticleTafsiliTemplateEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ArticleTafsiliTemplateEntity (java.lang.Long id) {
		super(id);
	}



/*[CONSTRUCTOR MARKER END]*/

	@Override
	public String toString() {
		if(getId()!= null)
			return getHesabTafsiliTemplate().getDesc();
		return "";
	}

}