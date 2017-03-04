package ir.serajsamaneh.accounting.hesabmoeentemplate;

import ir.serajsamaneh.accounting.enumeration.HesabScopeEnum;
import ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateService;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.organ.OrganEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

public class HesabMoeenTemplateService extends
		BaseEntityService<HesabMoeenTemplateEntity, Long> {

	@Override
	protected HesabMoeenTemplateDAO getMyDAO() {
		return hesabMoeenTemplateDAO;
	}

	HesabMoeenTemplateDAO hesabMoeenTemplateDAO;
	HesabKolTemplateService hesabKolTemplateService;

	public HesabKolTemplateService getHesabKolTemplateService() {
		return hesabKolTemplateService;
	}

	public void setHesabKolTemplateService(
			HesabKolTemplateService hesabKolTemplateService) {
		this.hesabKolTemplateService = hesabKolTemplateService;
	}

	public void setHesabMoeenTemplateDAO(
			HesabMoeenTemplateDAO hesabMoeenTemplateDAO) {
		this.hesabMoeenTemplateDAO = hesabMoeenTemplateDAO;
	}

	public HesabMoeenTemplateDAO getHesabMoeenTemplateDAO() {
		return hesabMoeenTemplateDAO;
	}

	@Override
	@Transactional
	public void saveOrUpdateStateLess(HesabMoeenTemplateEntity entity) {
		if(entity.getHidden() == null)
			entity.setHidden(false);
		super.saveOrUpdateStateLess(entity);
	}
	
	@Transactional
	public void saveGlobal(HesabMoeenTemplateEntity entity) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("scope@eq", HesabScopeEnum.GLOBAL);
		checkUniqueNess(entity, HesabMoeenEntity.PROP_NAME, entity.getName(),
				localFilter, false);
		checkUniqueNess(entity, HesabMoeenEntity.PROP_CODE, entity.getCode(),
				localFilter, false);
		save(entity);
	}
	
	@Override
	@Transactional
	public void save(HesabMoeenTemplateEntity entity) {
		if(entity.getHidden() == null)
			entity.setHidden(false);
		Boolean isNew=(entity.getId()!=null?false:true);
		super.save(entity);
		logAction(isNew, entity);
	}
	
	private void checkHesabUniqueNess(HesabMoeenTemplateEntity entity,	OrganEntity organ) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eq", organ.getId());
		checkUniqueNess(entity, HesabTafsiliEntity.PROP_NAME, entity.getName(),	localFilter, false);
		checkUniqueNess(entity, HesabTafsiliEntity.PROP_CODE, entity.getCode(),	localFilter, false);
	}
//	public HesabMoeenTemplateEntity getGlobalHesabMoeenTemplateByCode(String hesabCode) {
//		return getMyDAO().getHesabMoeenTemplateByCode(hesabCode);
//	}

	@Transactional(readOnly = false)
	public HesabMoeenTemplateEntity createHesabMoeenTemplate(String hesabMoeenCode, String hesabMoeenName, String hesabKolCode, OrganEntity organEntity, Boolean hidden) {
		HesabMoeenTemplateEntity hesabMoeenTemplateEntity = new HesabMoeenTemplateEntity();
		hesabMoeenTemplateEntity.setScope(HesabScopeEnum.LCAOL);
		hesabMoeenTemplateEntity.setName(hesabMoeenName);
		hesabMoeenTemplateEntity.setCode(hesabMoeenCode);
		hesabMoeenTemplateEntity.setOrgan(organEntity);
		hesabMoeenTemplateEntity.setHidden(hidden);
		hesabMoeenTemplateEntity.setHesabKolTemplate(getHesabKolTemplateService().getHesabKolTemplateByCode(hesabKolCode, organEntity));
		checkHesabUniqueNess(hesabMoeenTemplateEntity, organEntity);
		getHesabMoeenTemplateDAO().saveOrUpdate(hesabMoeenTemplateEntity);
		getLogger().info("hesabMoeen created : "+hesabMoeenCode);
		return hesabMoeenTemplateEntity;
		
	}
 
	public HesabMoeenTemplateEntity loadByCode(String code, OrganEntity organEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		//localFilter.put("organ.id@eqORorgan@isNull", Arrays.asList(organ.getId(),"ding"));
		localFilter.put("organ.code@startlk", getTopOrgan(organEntity).getCode());
		return load(null, localFilter);
	}

	public HesabMoeenTemplateEntity loadByName(String name, OrganEntity organEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq", name);
		//localFilter.put("organ.id@eqORorgan@isNull", Arrays.asList(organ.getId(),"ding"));
		localFilter.put("organ.code@startlk", getTopOrgan(organEntity).getCode());
		return load(null, localFilter);
	}
	

	public HesabMoeenTemplateEntity loadByCodeInCurrentOrgan(String code, OrganEntity organEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		//localFilter.put("organ.id@eqORorgan@isNull", Arrays.asList(organ.getId(),"ding"));
		localFilter.put("organ.id@eq", organEntity.getId());
		return load(null, localFilter);
	}
	
	public HesabMoeenTemplateEntity loadByNameInCurrentOrgan(String name, OrganEntity organEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq", name);
		//localFilter.put("organ.id@eqORorgan@isNull", Arrays.asList(organ.getId(),"ding"));
		localFilter.put("organ.id@eq", organEntity.getId());
		return load(null, localFilter);
	}
	public List<HesabMoeenTemplateEntity> getActiveMoeens(OrganEntity organEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.code@startlk", getTopOrgan(organEntity).getCode());
//		localFilter.put("organ.id@eqORorgan.id@isNull", Arrays.asList(organEntity.getId(),"ding"));
		localFilter.put("hidden@eq", Boolean.FALSE);
		return getDataList(null, localFilter);
	}

	public List<HesabMoeenTemplateEntity> getActiveMoeens(Long hesabKolTemplateId, OrganEntity organEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		//localFilter.put("organ.id@eqORorgan.id@isNull", Arrays.asList(organEntity.getId(),"ding"));
		localFilter.put("organ.code@startlk", getTopOrgan(organEntity).getCode());
		localFilter.put("hidden@eq", Boolean.FALSE);
		localFilter.put("hesabKolTemplate.id@eq", hesabKolTemplateId);
		return getDataList(null, localFilter);
	}
	
}