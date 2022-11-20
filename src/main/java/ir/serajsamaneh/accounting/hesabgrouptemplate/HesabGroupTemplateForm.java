package ir.serajsamaneh.accounting.hesabgrouptemplate;

import javax.faces.model.DataModel;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import ir.serajsamaneh.accounting.base.BaseAccountingForm;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.util.SerajMessageUtil;
@Named("hesabGroupTemplate")
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class HesabGroupTemplateForm extends BaseAccountingForm<HesabGroupTemplateEntity, Long> {

	@Override
	protected HesabGroupTemplateService getMyService() {
		return hesabGroupTemplateService;
	}

	HesabGroupTemplateService hesabGroupTemplateService;

	public void setHesabGroupTemplateService(HesabGroupTemplateService hesabGroupTemplateService) {
		this.hesabGroupTemplateService = hesabGroupTemplateService;
	}

	public HesabGroupTemplateService getHesabGroupTemplateService() {
		return hesabGroupTemplateService;
	}

	public String localSave() {
		if (getEntity().getOrganId() == null) {
			getEntity().setOrganId(getCurrentOrganVO().getId());
			getEntity().setOrganName(getCurrentOrganVO().getName());
		}
		getMyService().save(entity);
		return getLocalViewUrl();
	}

	public DataModel<HesabGroupTemplateEntity> getLocalArchiveDataModel() {
		return getLocalDataModel();
	}

	public Boolean getIsCreated() {
		return null;
	}

	@Override
	public String delete() {
		if (!getIsForMyOrgan())
			throw new FatalException(SerajMessageUtil.getMessage("common_deleteNotAllowed"));
		return super.delete();
	}

	public boolean getIsForMyOrgan() {
		if (getEntity() == null || getEntity().getId() == null)
			return true;
		return getEntity().getOrganId().equals(getCurrentOrganVO().getId());
	}

}