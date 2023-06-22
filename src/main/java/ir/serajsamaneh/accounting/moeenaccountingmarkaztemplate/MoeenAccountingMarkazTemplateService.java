package ir.serajsamaneh.accounting.moeenaccountingmarkaztemplate;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import ir.serajsamaneh.accounting.accountingmarkaztemplate.AccountingMarkazTemplateEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.core.base.BaseEntityService;
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MoeenAccountingMarkazTemplateService extends BaseEntityService<MoeenAccountingMarkazTemplateEntity, Long> {

	@Override
	protected MoeenAccountingMarkazTemplateDAO getMyDAO() {
		return moeenAccountingMarkazTemplateDAO;
	}

	@Autowired
	MoeenAccountingMarkazTemplateDAO moeenAccountingMarkazTemplateDAO;

	public MoeenAccountingMarkazTemplateEntity load(AccountingMarkazTemplateEntity accountingMarkazEntity,
			HesabMoeenTemplateEntity hesabMoeenEntity, Integer level, FlushMode flushMode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("accountingMarkazTemplate.id@eq", accountingMarkazEntity.getId());
		localFilter.put("hesabMoeenTemplate.id@eq", hesabMoeenEntity.getId());
		localFilter.put("level@eq", level);
		return load(localFilter);
	}

}