package ir.serajsamaneh.accounting.month;

import ir.serajsamaneh.accounting.base.BaseAccountingForm;
import ir.serajsamaneh.core.base.BaseEntityForm;

import javax.faces.model.DataModel;

public class MonthForm   extends BaseAccountingForm<MonthEntity,Long>  {

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
		if(getEntity().getId() == null){
			getEntity().setOrgan(getCurrentOrgan());
			getEntity().setSaalMaali(getCurrentUserActiveSaalMaali());
		}
		
		getMyService().save(getEntity(),getCurrentOrgan());
		return getLocalViewUrl();
	}
	
	public void createDefaultMonthForCurrentSaalMaali(){
		getMyService().createDefaultMonthForCurrentSaalMaali(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
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