package ir.serajsamaneh.accounting.articleaccountingmarkaz;

import ir.serajsamaneh.core.base.BaseEntityForm;

import javax.faces.model.DataModel;

public class ArticleAccountingMarkazForm   extends BaseEntityForm<ArticleAccountingMarkazEntity,Long>  {






	@Override
	protected ArticleAccountingMarkazService getMyService() {
		return accountingMarkazService;
	}





	
	ArticleAccountingMarkazService accountingMarkazService;



	public ArticleAccountingMarkazService getAccountingMarkazService() {
		return accountingMarkazService;
	}

	public void setAccountingMarkazService(
			ArticleAccountingMarkazService accountingMarkazService) {
		this.accountingMarkazService = accountingMarkazService;
	}

	public String localSave() {
		save();
		return getLocalViewUrl();
	}

	public DataModel getLocalDataModel() {
		return getDataModel();
	}
	
	public DataModel getLocalArchiveDataModel() {
		return getDataModel();
	}	
	
	public Boolean getIsCreated() {
		return null;
	}
	

}