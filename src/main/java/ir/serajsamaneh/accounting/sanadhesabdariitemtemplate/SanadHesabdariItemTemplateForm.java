package ir.serajsamaneh.accounting.sanadhesabdariitemtemplate;

import java.util.List;

import javax.faces.model.DataModel;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import ir.serajsamaneh.core.base.BaseEntityForm;
import ir.serajsamaneh.core.organ.OrganEntity;
@Named("sanadHesabdariItemTemplate")
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class SanadHesabdariItemTemplateForm extends BaseEntityForm<SanadHesabdariItemTemplateEntity, Long> {

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

	public String localSave(OrganEntity currentOrgan) {
		if (getEntity().getOrganId() == null)
			getEntity().setOrganId(currentOrgan.getId());

		save();
		return getLocalViewUrl();
	}

	public DataModel<SanadHesabdariItemTemplateEntity> getLocalDataModel() {
		getFilter().put("organId@eq", getCurrentOrganVO().getId());
		return getDataModel();
	}

	public DataModel<SanadHesabdariItemTemplateEntity> getLocalArchiveDataModel() {
		getFilter().put("organId@eq", getCurrentOrganVO().getId());
		return getLocalDataModel();
	}

	public Boolean getIsCreated() {
		return null;
	}
}