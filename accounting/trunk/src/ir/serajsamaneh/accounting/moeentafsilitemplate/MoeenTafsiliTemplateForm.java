package ir.serajsamaneh.accounting.moeentafsilitemplate;

import ir.serajsamaneh.core.base.BaseEntityForm;

import javax.faces.model.DataModel;

public class MoeenTafsiliTemplateForm   extends BaseEntityForm<MoeenTafsiliTemplateEntity,Long>  {






	@Override
	protected MoeenTafsiliTemplateService getMyService() {
		return moeenTafsiliTemplateService;
	}





	
	MoeenTafsiliTemplateService moeenTafsiliTemplateService;
	
	public void setMoeenTafsiliTemplateService(MoeenTafsiliTemplateService moeenTafsiliTemplateService) {
		this.moeenTafsiliTemplateService = moeenTafsiliTemplateService;
	}
	
	public MoeenTafsiliTemplateService getMoeenTafsiliTemplateService() {
		return moeenTafsiliTemplateService;
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