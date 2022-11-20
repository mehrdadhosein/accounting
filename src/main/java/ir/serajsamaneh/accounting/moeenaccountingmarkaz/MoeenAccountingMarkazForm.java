package ir.serajsamaneh.accounting.moeenaccountingmarkaz;

import javax.faces.model.DataModel;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import ir.serajsamaneh.core.base.BaseEntityForm;
@Named("moeenAccountingMarkaz")
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class MoeenAccountingMarkazForm extends BaseEntityForm<MoeenAccountingMarkazEntity, Long> {

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