package ir.serajsamaneh.accounting.month;

import javax.faces.model.DataModel;

import ir.serajsamaneh.accounting.base.BaseAccountingForm;

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
//		setRowsPerPage(-1);
		getFilter().put("organ.id@eq", getCurrentOrgan().getId());
		return getDataModel();
	}
	
	public DataModel getSaalMaaliDataModel() {
//		setRowsPerPage(-1);
		getFilter().put("saalMaali.id@eq", getCurrentUserActiveSaalMaali().getId());
		return getDataModel();
	}
	
	public DataModel getLocalArchiveDataModel() {
		return getDataModel();
	}	
	
	public Boolean getIsCreated() {
		return null;
	}
	

}