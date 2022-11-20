package ir.serajsamaneh.accounting.hesabkoltemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ir.serajsamaneh.accounting.hesabgroup.HesabGroupDAO;
import ir.serajsamaneh.core.base.BaseHibernateDAO;
import ir.serajsamaneh.erpcore.util.HesabTemplateRelationsUtil;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class HesabKolTemplateDAO extends BaseHibernateDAO<HesabKolTemplateEntity, Long> {

	@Autowired
	HesabGroupDAO hesabGroupDAO;

	@Transactional
	public HesabKolTemplateEntity getHesabKolTemplateByCode(String hesabCode, Long organId) {

		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", hesabCode);
		localFilter.put("organId@eq", organId);
		List<HesabKolTemplateEntity> dataList = getDataList(null, localFilter, null, null, FlushMode.MANUAL, false);
		if (dataList.size() == 1)
			return dataList.get(0);
		else if (dataList.size() == 0)
			return null;
		else
			throw new IllegalStateException();
	}

	public HesabKolTemplateEntity getHesabKolTemplateByName(String hesabKolName, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq", hesabKolName);
		localFilter.put("organId@eq", organId);
		HesabKolTemplateEntity hesabKolTemplateEntity = load(null, localFilter, FlushMode.MANUAL, false);
		return hesabKolTemplateEntity;

	}

	@Override
	public void saveOrUpdate(HesabKolTemplateEntity entity) {
		if (entity.getHidden() == null)
			entity.setHidden(false);

		if (entity.getOrganId() != null)
			HesabTemplateRelationsUtil.resetKolMoeenTemplateMap(entity.getOrganId());

		checkHesabTemplateUniqueNess(entity);

		super.saveOrUpdate(entity);
	}

	private void checkHesabTemplateUniqueNess(HesabKolTemplateEntity entity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organId@eq", entity.getOrganId());
		checkUniqueNess(entity, HesabKolTemplateEntity.PROP_CODE, entity.getCode(), localFilter, false);
		checkUniqueNess(entity, HesabKolTemplateEntity.PROP_NAME, entity.getName(), localFilter, false);
	}

	@Override
	public void save(HesabKolTemplateEntity entity) {
		if (entity.getHidden() == null)
			entity.setHidden(false);

		if (entity.getOrganId() != null)
			HesabTemplateRelationsUtil.resetKolMoeenTemplateMap(entity.getOrganId());

		checkHesabTemplateUniqueNess(entity);

		super.save(entity);
	}

}