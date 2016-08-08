package ir.serajsamaneh.accounting.hesabgrouptemplate;

import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.core.base.BaseHibernateDAO;
import ir.serajsamaneh.core.organ.OrganEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.FlushMode;
import org.springframework.transaction.annotation.Transactional;

public class HesabGroupTemplateDAO  extends BaseHibernateDAO<HesabGroupTemplateEntity,Long> {


	@Transactional
	public HesabGroupTemplateEntity getHesabGroupByCode(String hesabGroupCode, OrganEntity organ) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", hesabGroupCode);
		localFilter.put("organ.id@eqORorgan.id@isNull", Arrays.asList(organ.getId(),"ding"));
		HesabGroupTemplateEntity hesabGroupTemplateEntity = load(null, localFilter, FlushMode.MANUAL, false);
		return hesabGroupTemplateEntity;
	}

	@Transactional
	public HesabGroupTemplateEntity getHesabGroupByCode(String hesabGroupCode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", hesabGroupCode);
		localFilter.put("organ.id@isNull", "ding");
		HesabGroupTemplateEntity hesabGroupTemplateEntity = load(null, localFilter, FlushMode.MANUAL, false);
		return hesabGroupTemplateEntity;
	}
	

	private void checkHesabTemplateUniqueNess(HesabGroupTemplateEntity entity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eq", entity.getOrgan().getId());
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