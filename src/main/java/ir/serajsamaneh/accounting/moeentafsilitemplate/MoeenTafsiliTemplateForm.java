package ir.serajsamaneh.accounting.moeentafsilitemplate;

import javax.faces.model.DataModel;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import ir.serajsamaneh.core.base.BaseEntityForm;
@Named("moeenTafsiliTemplate")
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class MoeenTafsiliTemplateForm extends BaseEntityForm<MoeenTafsiliTemplateEntity, Long> {

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