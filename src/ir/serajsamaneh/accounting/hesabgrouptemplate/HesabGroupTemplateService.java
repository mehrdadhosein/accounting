package ir.serajsamaneh.accounting.hesabgrouptemplate;

import ir.serajsamaneh.accounting.enumeration.MahyatGroupEnum;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.organ.OrganEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

public class HesabGroupTemplateService extends
		BaseEntityService<HesabGroupTemplateEntity, Long> {

	@Override
	protected HesabGroupTemplateDAO getMyDAO() {
		return hesabGroupTemplateDAO;
	}

	HesabGroupTemplateDAO hesabGroupTemplateDAO;

	public void setHesabGroupTemplateDAO(
			HesabGroupTemplateDAO hesabGroupTemplateDAO) {
		this.hesabGroupTemplateDAO = hesabGroupTemplateDAO;
	}

	public HesabGroupTemplateDAO getHesabGroupTemplateDAO() {
		return hesabGroupTemplateDAO;
	}

	public HesabGroupTemplateEntity load(Long code, OrganEntity organ) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		localFilter.put("organ.id@eqORorgan@isNull", Arrays.asList(organ.getId(),"ding"));
		return load(null, localFilter);
	}

	@Transactional(readOnly = false)
	public void createHesabGroupTemplate(Long hesabGroupCode, String hesabGroupName, String mahyatGroup, OrganEntity organEntity) {
		
		HesabGroupTemplateEntity hesabGroupTemplateEntity = load(hesabGroupCode, organEntity);
		if (hesabGroupTemplateEntity == null){
			hesabGroupTemplateEntity = new HesabGroupTemplateEntity();
		}
		hesabGroupTemplateEntity.setName(hesabGroupName);
		hesabGroupTemplateEntity.setCode(hesabGroupCode);
		hesabGroupTemplateEntity.setMahyatGroup(MahyatGroupEnum.valueOf(mahyatGroup));
		hesabGroupTemplateEntity.setOrgan(organEntity);
		saveOrUpdate(hesabGroupTemplateEntity);
		getLogger().info("hesabGroup created : "+hesabGroupCode);
	}
	@Transactional
	public void save(HesabGroupTemplateEntity entity) {
		Boolean isNew=(entity.getId()!=null?false:true);
		super.save(entity);
		logAction(isNew, entity);
	}
}