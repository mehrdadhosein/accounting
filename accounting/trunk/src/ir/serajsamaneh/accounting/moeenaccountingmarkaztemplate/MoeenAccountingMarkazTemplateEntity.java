package ir.serajsamaneh.accounting.moeenaccountingmarkaztemplate;




public class MoeenAccountingMarkazTemplateEntity extends BaseMoeenAccountingMarkazTemplateEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public MoeenAccountingMarkazTemplateEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public MoeenAccountingMarkazTemplateEntity (java.lang.Long id) {
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