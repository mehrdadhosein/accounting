package ir.serajsamaneh.accounting.moeenaccountingmarkaztemplate;

import ir.serajsamaneh.core.base.BaseEntityForm;


import javax.faces.model.DataModel;

public class MoeenAccountingMarkazTemplateForm   extends BaseEntityForm<MoeenAccountingMarkazTemplateEntity,Long>  {






	@Override
	protected MoeenAccountingMarkazTemplateService getMyService() {
		return moeenAccountingMarkazTemplateService;
	}





	
	MoeenAccountingMarkazTemplateService moeenAccountingMarkazTemplateService;
	



	public MoeenAccountingMarkazTemplateService getMoeenAccountingMarkazTemplateService() {
		return moeenAccountingMarkazTemplateService;
	}

	public void setMoeenAccountingMarkazTemplateService(
			MoeenAccountingMarkazTemplateService moeenAccountingMarkazTemplateService) {
		this.moeenAccountingMarkazTemplateService = moeenAccountingMarkazTemplateService;
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