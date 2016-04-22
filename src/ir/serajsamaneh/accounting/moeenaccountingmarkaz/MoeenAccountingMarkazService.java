package ir.serajsamaneh.accounting.moeenaccountingmarkaz;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.exception.FatalException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;

public class MoeenAccountingMarkazService extends
		BaseEntityService<MoeenAccountingMarkazEntity, Long> {

	@Override
	protected MoeenAccountingMarkazDAO getMyDAO() {
		return moeenAccountingMarkazDAO;
	}

	MoeenAccountingMarkazDAO moeenAccountingMarkazDAO;


	public MoeenAccountingMarkazDAO getMoeenAccountingMarkazDAO() {
		return moeenAccountingMarkazDAO;
	}


	public void setMoeenAccountingMarkazDAO(
			MoeenAccountingMarkazDAO moeenAccountingMarkazDAO) {
		this.moeenAccountingMarkazDAO = moeenAccountingMarkazDAO;
	}


	public MoeenAccountingMarkazEntity load(AccountingMarkazEntity accountingMarkazEntity,
			HesabMoeenEntity hesabMoeenEntity, Integer level, FlushMode flushMode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("accountingMarkaz.id@eq", accountingMarkazEntity.getId());
		localFilter.put("hesabMoeen.id@eq", hesabMoeenEntity.getId());
		localFilter.put("level@eq", level);
		List<MoeenAccountingMarkazEntity> dataList = getDataList(null, localFilter, flushMode);
		if(dataList.size() == 1)
			return dataList.get(0);
		else if(dataList.size() == 0)
			return null;
		throw new FatalException();
	}

}