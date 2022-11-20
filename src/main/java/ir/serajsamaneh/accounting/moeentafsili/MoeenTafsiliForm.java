package ir.serajsamaneh.accounting.moeentafsili;

import javax.faces.model.DataModel;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import ir.serajsamaneh.core.base.BaseEntityForm;
@Named("moeenTafsili")
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class MoeenTafsiliForm extends BaseEntityForm<MoeenTafsiliEntity, Long> {

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