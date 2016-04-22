package ir.serajsamaneh.accounting.sanadhesabdariitemtemplate;

import ir.serajsamaneh.core.base.BaseEntityForm;

import java.util.List;

import javax.faces.model.DataModel;

public class SanadHesabdariItemTemplateForm   extends BaseEntityForm<SanadHesabdariItemTemplateEntity,Long>  {






	@Override
	protected SanadHesabdariItemTemplateService getMyService() {
		return sanadHesabdariItemTemplateService;
	}
	
	List tafsiliIds;
	List moeenIds;
	List hesabKolIds;


	public List getTafsiliIds() {
		return tafsiliIds;
	}

	public void setTafsiliIds(List tafsiliIds) {
		this.tafsiliIds = tafsiliIds;
	}



	public List getHesabKolIds() {
		return hesabKolIds;
	}

	public void setHesabKolIds(List hesabKolIds) {
		this.hesabKolIds = hesabKolIds;
	}

	public List getMoeenIds() {
		return moeenIds;
	}

	public void setMoeenIds(List moeenIds) {
		this.moeenIds = moeenIds;
	}

	SanadHesabdariItemTemplateService sanadHesabdariItemTemplateService;


	public SanadHesabdariItemTemplateService getSanadHesabdariItemTemplateService() {
		return sanadHesabdariItemTemplateService;
	}

	public void setSanadHesabdariItemTemplateService(
			SanadHesabdariItemTemplateService sanadHesabdariItemTemplateService) {
		this.sanadHesabdariItemTemplateService = sanadHesabdariItemTemplateService;
	}

	public String localSave() {
		if(getEntity().getOrgan() == null || getEntity().getOrgan().getId() == null)
			getEntity().setOrgan(getCurrentOrgan());
		
		save();
		return getLocalViewUrl();
	}

	

	public DataModel getLocalDataModel() {
		getFilter().put("organ.id@eq", getCurrentOrgan().getId());
		return getDataModel();
	}
	
	public DataModel getLocalArchiveDataModel() {
		getFilter().put("organ.id@eq", getCurrentOrgan().getId());
		return getDataModel();
	}	
	public Boolean getIsCreated() {
		return null;
	}
}