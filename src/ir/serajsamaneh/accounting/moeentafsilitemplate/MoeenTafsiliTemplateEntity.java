package ir.serajsamaneh.accounting.moeentafsilitemplate;

import ir.serajsamaneh.accounting.moeentafsilitemplate.BaseMoeenTafsiliTemplateEntity;



public class MoeenTafsiliTemplateEntity extends BaseMoeenTafsiliTemplateEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public MoeenTafsiliTemplateEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public MoeenTafsiliTemplateEntity (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public MoeenTafsiliTemplateEntity (
		java.lang.Long id,
		ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity hesabTafsiliInstance,
		ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity hesabMoeenInstance) {

		super (
			id,
			hesabTafsiliInstance,
			hesabMoeenInstance);
	}

/*[CONSTRUCTOR MARKER END]*/


}