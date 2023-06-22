package ir.serajsamaneh.accounting.hesabgroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ir.serajsamaneh.core.base.BaseHibernateDAO;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class HesabGroupDAO extends BaseHibernateDAO<HesabGroupEntity, Long> {

	@Transactional
	public HesabGroupEntity getHesabGroupByCode(String hesabGroupCode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", hesabGroupCode);
		List<HesabGroupEntity> dataList = getDataList(localFilter, null, null, FlushMode.MANUAL, false);
		if (dataList.size() == 1)
			return dataList.get(0);
		else if (dataList.size() == 0)
			return null;
		else
			throw new IllegalStateException();
	}

}