package ir.serajsamaneh.accounting.accountingmarkazgroup;




public class AccountingMarkazGroupEntity extends BaseAccountingMarkazGroupEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public AccountingMarkazGroupEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public AccountingMarkazGroupEntity (java.lang.Long id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/


	@Override
	public String toString() {
		return getName();
	}

	public String getParentDesc() {
		if(getId()!=null){
			String parentDesc = "(";
			AccountingMarkazGroupEntity parentEntity = getParent();
			while(parentEntity!=null && parentEntity.getId() != null){
				parentDesc += parentEntity.getName()+",";
				parentEntity = parentEntity.getParent();
			}
			return parentDesc+")";
		}
				
		return "";
	}
}