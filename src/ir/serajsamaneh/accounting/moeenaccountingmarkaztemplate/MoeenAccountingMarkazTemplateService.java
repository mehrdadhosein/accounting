package ir.serajsamaneh.accounting.moeenaccountingmarkaztemplate;


import ir.serajsamaneh.accounting.accountingmarkaztemplate.AccountingMarkazTemplateEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.exception.FatalException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;

public class MoeenAccountingMarkazTemplateService extends
		BaseEntityService<MoeenAccountingMarkazTemplateEntity, Long> {

	@Override
	protected MoeenAccountingMarkazTemplateDAO getMyDAO() {
		return moeenAccountingMarkazTemplateDAO;
	}

	MoeenAccountingMarkazTemplateDAO moeenAccountingMarkazTemplateDAO;


	public MoeenAccountingMarkazTemplateDAO getMoeenAccountingMarkazTemplateDAO() {
		return moeenAccountingMarkazTemplateDAO;
	}


	public void setMoeenAccountingMarkazTemplateDAO(
			MoeenAccountingMarkazTemplateDAO moeenAccountingMarkazTemplateDAO) {
		this.moeenAccountingMarkazTemplateDAO = moeenAccountingMarkazTemplateDAO;
	}



	public MoeenAccountingMarkazTemplateEntity load(AccountingMarkazTemplateEntity accountingMarkazEntity,
			HesabMoeenTemplateEntity hesabMoeenEntity, Integer level, FlushMode flushMode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("accountingMarkazTemplate.id@eq", accountingMarkazEntity.getId());
		localFilter.put("hesabMoeenTemplate.id@eq", hesabMoeenEntity.getId());
		localFilter.put("level@eq", level);
		return load(null, localFilter);
	}

}