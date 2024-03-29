package ir.serajsamaneh.accounting.hesabgrouptemplate;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ir.serajsamaneh.accounting.enumeration.MahyatGroupEnum;
import ir.serajsamaneh.core.base.BaseEntityService;
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class HesabGroupTemplateService extends BaseEntityService<HesabGroupTemplateEntity, Long> {

	@Override
	protected HesabGroupTemplateDAO getMyDAO() {
		return hesabGroupTemplateDAO;
	}

	@Autowired
	HesabGroupTemplateDAO hesabGroupTemplateDAO;

	public HesabGroupTemplateEntity load(Long code, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		localFilter.put("organId@eq", organId);
		return load(localFilter);
	}

	@Transactional(readOnly = false)
	public void createHesabGroupTemplate(Long hesabGroupCode, String hesabGroupName, String mahyatGroup, Long organId,
			String organName) {

		HesabGroupTemplateEntity hesabGroupTemplateEntity = load(hesabGroupCode, organId);
		if (hesabGroupTemplateEntity == null) {
			hesabGroupTemplateEntity = new HesabGroupTemplateEntity();
		}
		hesabGroupTemplateEntity.setName(hesabGroupName);
		hesabGroupTemplateEntity.setCode(hesabGroupCode);
		hesabGroupTemplateEntity.setMahyatGroup(MahyatGroupEnum.valueOf(mahyatGroup));
		hesabGroupTemplateEntity.setOrganId(organId);
		hesabGroupTemplateEntity.setOrganName(organName);
		saveOrUpdate(hesabGroupTemplateEntity);
		getLogger().info("hesabGroup created : " + hesabGroupCode);
	}

	@Transactional
	public void save(HesabGroupTemplateEntity entity) {
		Boolean isNew = (entity.getId() != null ? false : true);
		super.save(entity);
		logAction(isNew, entity);
	}
}