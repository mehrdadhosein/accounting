package ir.serajsamaneh.accounting.moeentafsili;

import ir.serajsamaneh.accounting.moeentafsili.BaseMoeenTafsiliEntity;



public class MoeenTafsiliEntity extends BaseMoeenTafsiliEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public MoeenTafsiliEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public MoeenTafsiliEntity (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public MoeenTafsiliEntity (
		java.lang.Long id,
		ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity hesabMoeen,
		ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity hesabTafsili) {

		super (
			id,
			hesabMoeen,
			hesabTafsili);
	}

/*[CONSTRUCTOR MARKER END]*/
	
	public String entityId;

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}


}