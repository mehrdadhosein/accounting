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
			getEntity().setOrganId(getCurrentOrganVO().getId());
			getEntity().setOrganName(getCurrentOrganVO().getName());
			getEntity().setSaalMaali(getCurrentUserActiveSaalMaali());
		}
		
		getMyService().save(getEntity(),getCurrentOrganVO().getId());
		return getLocalViewUrl();
	}
	
	public void createDefaultMonthForCurrentSaalMaali(){
		getMyService().createDefaultMonthForCurrentSaalMaali(getCurrentUserActiveSaalMaali(), getCurrentOrganVO().getId());
	}
	
	public DataModel<MonthEntity> getLocalDataModel() {
//		setRowsPerPage(-1);
		getFilter().put("organId@eq", getCurrentOrganVO().getId());
		return getDataModel();
	}
	
	public DataModel<MonthEntity> getSaalMaaliDataModel() {
//		setRowsPerPage(-1);
		getFilter().put("saalMaali.id@eq", getCurrentUserActiveSaalMaali().getId());
		return getDataModel();
	}
	
	public DataModel<MonthEntity> getLocalArchiveDataModel() {
		return getDataModel();
	}	
	
	public Boolean getIsCreated() {
		return null;
	}
	

}