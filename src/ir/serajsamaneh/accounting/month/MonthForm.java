package ir.serajsamaneh.accounting.month;

import ir.serajsamaneh.core.base.BaseEntityForm;

import javax.faces.model.DataModel;

public class MonthForm   extends BaseEntityForm<MonthEntity,Long>  {

	@Override
	protected MonthService getMyService() {
		return monthService;
	}
	MonthService monthService;
	
	public void setMonthService(MonthService monthService) {
		this.monthService = monthService;
	}
	
	public MonthService getMonthService() {
		return monthService;
	}


	public String localSave() {
		getEntity().setOrgan(getCurrentOrgan());
		getMyService().save(getEntity(),getCurrentOrgan());
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