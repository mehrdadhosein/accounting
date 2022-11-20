package ir.serajsamaneh.accounting.moeenaccountingmarkaztemplate;

import javax.faces.model.DataModel;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import ir.serajsamaneh.core.base.BaseEntityForm;
@Named("moeenAccountingMarkazTemplate")
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class MoeenAccountingMarkazTemplateForm extends BaseEntityForm<MoeenAccountingMarkazTemplateEntity, Long> {

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