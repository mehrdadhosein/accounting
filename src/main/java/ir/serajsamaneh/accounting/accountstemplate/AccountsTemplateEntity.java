package ir.serajsamaneh.accounting.accountstemplate;

import ir.serajsamaneh.core.util.SerajMessageUtil;

public class AccountsTemplateEntity extends BaseAccountsTemplateEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public AccountsTemplateEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public AccountsTemplateEntity (java.lang.Long id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/
	@Override
	public String toString() {
		return getActionId();
	}
	
	@Override
	public String getCompleteInfo() {
		return SerajMessageUtil.getMessage(this.getEntityName()+"_actionName")+":"+this.getActionName()+" , "+
			   SerajMessageUtil.getMessage(this.getEntityName()+"_actionId")+":"+this.getActionId();
	}

}
