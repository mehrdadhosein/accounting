package ir.serajsamaneh.accounting.moeenaccountingmarkaz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.exception.FatalException;
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MoeenAccountingMarkazService extends BaseEntityService<MoeenAccountingMarkazEntity, Long> {

	@Override
	protected MoeenAccountingMarkazDAO getMyDAO() {
		return moeenAccountingMarkazDAO;
	}

	@Autowired
	MoeenAccountingMarkazDAO moeenAccountingMarkazDAO;

	public MoeenAccountingMarkazEntity load(AccountingMarkazEntity accountingMarkazEntity,
			HesabMoeenEntity hesabMoeenEntity, Integer level, FlushMode flushMode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("accountingMarkaz.id@eq", accountingMarkazEntity.getId());
		localFilter.put("hesabMoeen.id@eq", hesabMoeenEntity.getId());
		localFilter.put("level@eq", level);
		List<MoeenAccountingMarkazEntity> dataList = getDataList(localFilter, flushMode);
		if (dataList.size() == 1)
			return dataList.get(0);
		else if (dataList.size() == 0)
			return null;
		throw new FatalException();
	}

	public MoeenAccountingMarkazEntity load(AccountingMarkazEntity accountingMarkazEntity, Long moeenId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("accountingMarkaz.id@eq", accountingMarkazEntity.getId());
		localFilter.put("hesabMoeen.id@eq", moeenId);
		return load(localFilter);
	}

}