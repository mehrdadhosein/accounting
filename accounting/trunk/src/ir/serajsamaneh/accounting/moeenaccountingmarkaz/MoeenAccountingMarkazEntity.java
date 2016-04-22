package ir.serajsamaneh.accounting.moeenaccountingmarkaz;




public class MoeenAccountingMarkazEntity extends BaseMoeenAccountingMarkazEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public MoeenAccountingMarkazEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public MoeenAccountingMarkazEntity (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */


/*[CONSTRUCTOR MARKER END]*/
	
	public String entityId;

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}


}