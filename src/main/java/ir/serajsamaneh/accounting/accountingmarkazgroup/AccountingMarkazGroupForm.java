package ir.serajsamaneh.accounting.accountingmarkazgroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.model.DataModel;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import ir.serajsamaneh.accounting.accountingmarkaztemplate.AccountingMarkazTemplateEntity;
import ir.serajsamaneh.accounting.base.BaseAccountingForm;
import ir.serajsamaneh.core.base.BaseEntity;
@Named("accountingMarkazGroup")
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class AccountingMarkazGroupForm extends BaseAccountingForm<AccountingMarkazGroupEntity, Long> {

	@Override
	protected AccountingMarkazGroupService getMyService() {
		return accountingMarkazGroupService;
	}

	AccountingMarkazGroupService accountingMarkazGroupService;

	public AccountingMarkazGroupService getAccountingMarkazGroupService() {
		return accountingMarkazGroupService;
	}

	public void setAccountingMarkazGroupService(AccountingMarkazGroupService accountingMarkazGroupService) {
		this.accountingMarkazGroupService = accountingMarkazGroupService;
	}

	List<Long> accountingMarkazTemplateIds;

	public List<Long> getAccountingMarkazTemplateIds() {
		if (accountingMarkazTemplateIds == null) {
			accountingMarkazTemplateIds = new ArrayList<Long>();
			Set<AccountingMarkazTemplateEntity> markazTemplates = getEntity().getAccountingMarkazTemplates();
			if (markazTemplates != null) {
				for (AccountingMarkazTemplateEntity markazTemplateEntity : markazTemplates) {
					accountingMarkazTemplateIds.add(markazTemplateEntity.getId());
				}
			}
		}
		return accountingMarkazTemplateIds;
	}

	@Override
	public DataModel<AccountingMarkazGroupEntity> getLocalDataModel() {
		setSearchAction(true);
		return super.getLocalDataModel();
	}

	public void setAccountingMarkazTemplateIds(List<Long> accountingMarkazTemplateIds) {
		this.accountingMarkazTemplateIds = accountingMarkazTemplateIds;
	}

	public String localSave() {
		getEntity().setOrganId(getCurrentOrganVO().getId());
		getEntity().setOrganName(getCurrentOrganVO().getName());
		save();
		return getLocalViewUrl();
	}

	@Override
	public List<? extends BaseEntity> getJsonList(String property, String term, boolean all,
			Map<String, String> params) {

		String isHierarchical = params.get("isHierarchical");
		if (isHierarchical != null && isHierarchical.equals("true")) {
			List<Long> topOrganList = getCurrentOrganVO().getTopOrgansIdList();
			getFilter().put("organId@in", topOrganList);

			params.put("isLocal", "false");
		}
		return super.getJsonList(property, term, all, params);
	}

}