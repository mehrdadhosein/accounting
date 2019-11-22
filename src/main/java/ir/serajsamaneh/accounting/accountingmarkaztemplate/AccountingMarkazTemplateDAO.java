package ir.serajsamaneh.accounting.accountingmarkaztemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.springframework.util.StringUtils;

import ir.serajsamaneh.core.base.BaseHibernateDAO;

public class AccountingMarkazTemplateDAO extends BaseHibernateDAO<AccountingMarkazTemplateEntity,Long> {



	public Long getMaxAccountingMarkazCode(Long organId) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("organId@eq", organId);
		addLocationFilter(filter);

		Criteria criteria = getEmptyCriteria();
		addFilterCriteria(filter, criteria);

		// criteria.setProjection(Projections.projectionList().add(
		// Projections.max(KalaEntity.PROP_CODE)));

		List<AccountingMarkazTemplateEntity> list = criteria.setFlushMode(FlushMode.MANUAL).list();
		// .uniqueResult();

		Long maxCode = 0l;
		for (AccountingMarkazTemplateEntity kalaEntity : list) {
			String codeStr = kalaEntity.getCode();
			if(StringUtils.hasText(codeStr)){
				Long codeLong = new Long(codeStr);
				maxCode = Math.max(maxCode.longValue(), codeLong.longValue());
			}

		}
		return maxCode;

	}
	
	private void addLocationFilter(Map<String, Object> filter) {

	}


}