package ir.serajsamaneh.accounting.hesabmoeentemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import ir.serajsamaneh.accounting.enumeration.HesabScopeEnum;
import ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateService;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.moeentafsili.MoeenTafsiliEntity;
import ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.organ.OrganEntity;

public class HesabMoeenTemplateService extends
		BaseEntityService<HesabMoeenTemplateEntity, Long> {

	@Override
	protected HesabMoeenTemplateDAO getMyDAO() {
		return hesabMoeenTemplateDAO;
	}

	HesabMoeenTemplateDAO hesabMoeenTemplateDAO;
	HesabKolTemplateService hesabKolTemplateService;
	MoeenTafsiliTemplateService moeenTafsiliTemplateService;

	public MoeenTafsiliTemplateService getMoeenTafsiliTemplateService() {
		return moeenTafsiliTemplateService;
	}

	public void setMoeenTafsiliTemplateService(MoeenTafsiliTemplateService moeenTafsiliTemplateService) {
		this.moeenTafsiliTemplateService = moeenTafsiliTemplateService;
	}

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
		List<Long> topOrganList = getTopOrgansIdList(organEntity);
		
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@in", topOrganList);
		localFilter.put("hidden@eq", Boolean.FALSE);
		return getDataList(null, localFilter);
	}

	public List<HesabMoeenTemplateEntity> getActiveMoeens(Long hesabKolTemplateId, OrganEntity organEntity) {
		List<Long> topOrganList = getTopOrgansIdList(organEntity);
		
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@in", topOrganList);
		localFilter.put("hidden@eq", Boolean.FALSE);
		localFilter.put("hesabKolTemplate.id@eq", hesabKolTemplateId);
		return getDataList(null, localFilter);
	}

	@Transactional(readOnly=true)
	public List<HesabMoeenTemplateEntity> getRootHesabs(OrganEntity currentOrgan){
		
		List<HesabMoeenTemplateEntity> rootList = new ArrayList<HesabMoeenTemplateEntity>();
		Set<HesabMoeenTemplateEntity> removeList = new HashSet<HesabMoeenTemplateEntity>();
		
		List<HesabMoeenTemplateEntity> hesabMoeenList = getActiveMoeens(currentOrgan);
		
		Map<String, Object> moeenTafsiliFilter = new HashMap<String, Object>();
		moeenTafsiliFilter.put("hesabMoeenTemplate.organ.id@eq", currentOrgan.getId());
		List<MoeenTafsiliTemplateEntity> moeenTafsiliList = getMoeenTafsiliTemplateService().getDataList(null, moeenTafsiliFilter);
		
		for (MoeenTafsiliTemplateEntity moeenTafsiliEntity : moeenTafsiliList) {
			HesabMoeenTemplateEntity hesabMoeenEntity = moeenTafsiliEntity.getHesabMoeenTemplate();
			if(hesabMoeenList.contains(hesabMoeenEntity))
				removeList.add(hesabMoeenEntity);
		}
		
		rootList.addAll(hesabMoeenList);
		rootList.removeAll(removeList);
		return rootList;
	}
	
}