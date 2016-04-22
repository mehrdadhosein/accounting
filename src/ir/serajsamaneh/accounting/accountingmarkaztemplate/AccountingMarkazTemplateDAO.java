package ir.serajsamaneh.accounting.accountingmarkaztemplate;

import ir.serajsamaneh.core.base.BaseHibernateDAO;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.security.SecurityUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.springframework.util.StringUtils;

public class AccountingMarkazTemplateDAO extends BaseHibernateDAO<AccountingMarkazTemplateEntity,Long> {



	public Long getMaxAccountingMarkazCode(OrganEntity organEntity) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("organ.id@eq", organEntity.getId());
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
		if (maxCode == null)
			return getLocationStart() + 0l;
		return maxCode;

	}
	
	private void addLocationFilter(Map<String, Object> filter) {

	}


}