package ir.serajsamaneh.accounting.moeenaccountingmarkaz;

import ir.serajsamaneh.core.base.BaseEntityForm;

import javax.faces.model.DataModel;

public class MoeenAccountingMarkazForm   extends BaseEntityForm<MoeenAccountingMarkazEntity,Long>  {






	@Override
	protected MoeenAccountingMarkazService getMyService() {
		return moeenTafsiliService;
	}





	
	MoeenAccountingMarkazService moeenTafsiliService;
	
	public void setMoeenTafsiliService(MoeenAccountingMarkazService moeenTafsiliService) {
		this.moeenTafsiliService = moeenTafsiliService;
	}
	
	public MoeenAccountingMarkazService getMoeenTafsiliService() {
		return moeenTafsiliService;
	}


	public String localSave() {
		save();
		return getLocalViewUrl();
	}

	public DataModel<MoeenAccountingMarkazEntity> getLocalArchiveDataModel() {
		return getLocalDataModel();
	}	
	
	public Boolean getIsCreated() {
		return null;
	}
	

}