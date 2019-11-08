package ir.serajsamaneh.accounting.moeentafsili;

import ir.serajsamaneh.core.base.BaseEntityForm;

import javax.faces.model.DataModel;

public class MoeenTafsiliForm   extends BaseEntityForm<MoeenTafsiliEntity,Long>  {






	@Override
	protected MoeenTafsiliService getMyService() {
		return moeenTafsiliService;
	}





	
	MoeenTafsiliService moeenTafsiliService;
	
	public void setMoeenTafsiliService(MoeenTafsiliService moeenTafsiliService) {
		this.moeenTafsiliService = moeenTafsiliService;
	}
	
	public MoeenTafsiliService getMoeenTafsiliService() {
		return moeenTafsiliService;
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