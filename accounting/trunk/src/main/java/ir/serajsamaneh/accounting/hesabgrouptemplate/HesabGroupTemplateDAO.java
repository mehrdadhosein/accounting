package ir.serajsamaneh.accounting.hesabgrouptemplate;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.FlushMode;
import org.springframework.transaction.annotation.Transactional;

import ir.serajsamaneh.core.base.BaseHibernateDAO;

public class HesabGroupTemplateDAO  extends BaseHibernateDAO<HesabGroupTemplateEntity,Long> {


	@Transactional
	public HesabGroupTemplateEntity getHesabGroupByCode(Long hesabGroupCode, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", hesabGroupCode);
		localFilter.put("organId@eq", organId);
		HesabGroupTemplateEntity hesabGroupTemplateEntity = load(null, localFilter, FlushMode.MANUAL, false);
		return hesabGroupTemplateEntity;
	}

	@Transactional
	public HesabGroupTemplateEntity getHesabGroupByCode(String hesabGroupCode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", hesabGroupCode);
		localFilter.put("organId@isNull", "ding");
		HesabGroupTemplateEntity hesabGroupTemplateEntity = load(null, localFilter, FlushMode.MANUAL, false);
		return hesabGroupTemplateEntity;
	}
	

	private void checkHesabTemplateUniqueNess(HesabGroupTemplateEntity entity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organId@eq", entity.getOrganId());
		checkUniqueNess(entity, HesabGroupTemplateEntity.PROP_CODE, entity.getCode(), localFilter, false);
		checkUniqueNess(entity, HesabGroupTemplateEntity.PROP_NAME, entity.getName(), localFilter, false);
	}
	
	@Override
	public void saveOrUpdate(HesabGroupTemplateEntity entity) {
		checkHesabTemplateUniqueNess(entity);
		super.saveOrUpdate(entity);
	}
	
	@Override
	public void save(HesabGroupTemplateEntity entity) {
		checkHesabTemplateUniqueNess(entity);
		super.save(entity);
	}

}