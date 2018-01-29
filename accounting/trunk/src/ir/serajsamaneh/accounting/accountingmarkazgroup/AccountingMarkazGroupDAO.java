package ir.serajsamaneh.accounting.accountingmarkazgroup;

import ir.serajsamaneh.core.base.BaseHibernateDAO;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class AccountingMarkazGroupDAO extends BaseHibernateDAO<AccountingMarkazGroupEntity,Long> {



	public List<AccountingMarkazGroupEntity> getChilds(Long id, Map localFilter) {
		DetachedCriteria dc = DetachedCriteria.forClass(getEntityClass());
		Criteria criteria = dc.getExecutableCriteria(currentSession());
		criteria.add(Restrictions.eq("parent.id", id));
		addFilterCriteria(localFilter, criteria);
		List<AccountingMarkazGroupEntity> results = criteria.list();
		return results;
	}



}