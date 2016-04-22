package ir.serajsamaneh.accounting.accountingmarkaz;

import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.core.base.BaseHibernateDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.springframework.util.StringUtils;

public class AccountingMarkazDAO extends BaseHibernateDAO<AccountingMarkazEntity,Long> {



	public Long getMaxAccountingMarkazCode(SaalMaaliEntity saalMaaliEntity) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		addLocationFilter(filter);

		Criteria criteria = getEmptyCriteria();
		addFilterCriteria(filter, criteria);

		// criteria.setProjection(Projections.projectionList().add(
		// Projections.max(KalaEntity.PROP_CODE)));

		List<AccountingMarkazEntity> list = criteria.setFlushMode(FlushMode.MANUAL).list();
		// .uniqueResult();

		Long maxCode = 0l;
		for (AccountingMarkazEntity kalaEntity : list) {
			String codeStr = kalaEntity.getCode();
			if(StringUtils.hasText(codeStr)){
				Long codeLong = new Long(codeStr);
				maxCode = Math.max(maxCode.longValue(), codeLong.longValue());
			}

		}
		if (maxCode == null)
			return getLocationStart() + 0l;
		return maxCode;

	}
	
	private void addLocationFilter(Map<String, Object> filter) {

	}


}