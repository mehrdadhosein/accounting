package ir.serajsamaneh.accounting.hesabtafsilitemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.springframework.transaction.annotation.Transactional;

import ir.serajsamaneh.accounting.enumeration.HesabScopeEnum;
import ir.serajsamaneh.accounting.enumeration.TafsilTypeEnum;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.exception.FatalException;

public class HesabTafsiliTemplateService extends
		BaseEntityService<HesabTafsiliTemplateEntity, Long> {

	@Override
	protected HesabTafsiliTemplateDAO getMyDAO() {
		return hesabTafsiliTemplateDAO;
	}

	HesabTafsiliTemplateDAO hesabTafsiliTemplateDAO;
	HesabMoeenTemplateService hesabMoeenTemplateService;

	public HesabMoeenTemplateService getHesabMoeenTemplateService() {
		return hesabMoeenTemplateService;
	}

	public void setHesabMoeenTemplateService(
			HesabMoeenTemplateService hesabMoeenTemplateService) {
		this.hesabMoeenTemplateService = hesabMoeenTemplateService;
	}

	public void setHesabTafsiliTemplateDAO(
			HesabTafsiliTemplateDAO hesabTafsiliTemplateDAO) {
		this.hesabTafsiliTemplateDAO = hesabTafsiliTemplateDAO;
	}

	public HesabTafsiliTemplateDAO getHesabTafsiliTemplateDAO() {
		return hesabTafsiliTemplateDAO;
	}

//	@Transactional
//	public void saveGlobal(String hesabTafsiliCode, String hesabTafsiliName, String hesabMoeenCode, OrganEntity organEntity) {
//		getMyDAO().saveGlobal(hesabTafsiliCode, hesabTafsiliName, hesabMoeenCode, organEntity);
//	}
	
	
	public HesabTafsiliTemplateEntity getGlobalHesabTafsiliByCode(Long hesabCode){
		return getMyDAO().getGlobalHesabTafsiliByCode(hesabCode);
	}
	
	@Transactional
	public void save(HesabTafsiliTemplateEntity entity, List<Long> moeenIds, List<Long> childTafsiliIds, Long organId) {
		if (entity.getMoeenTafsiliTemplate() == null) {
			entity.setMoeenTafsiliTemplate(new HashSet<MoeenTafsiliTemplateEntity>());
		}
		if (entity.getChilds() == null) {
			entity.setChilds(new HashSet<HesabTafsiliTemplateEntity>());
		}
		entity.getMoeenTafsiliTemplate().clear();
		for (Long moeenId : moeenIds) {
			MoeenTafsiliTemplateEntity moeenTafsiliTemplateEntity = new MoeenTafsiliTemplateEntity();
			moeenTafsiliTemplateEntity.setHesabMoeenTemplate(getHesabMoeenTemplateService().load(moeenId));
			moeenTafsiliTemplateEntity.setHesabTafsiliTemplate(entity);
			if(moeenTafsiliTemplateEntity.getHesabMoeenTemplate() == null)
				throw new FatalException();
			entity.getMoeenTafsiliTemplate().add(moeenTafsiliTemplateEntity);
		}
		
		entity.getChilds().clear();
		for (Long tafsiliId : childTafsiliIds) {
			entity.getChilds().add(load(tafsiliId));
		}
		
		checkHesabUniqueNess(entity, organId);
		
		save(entity);
	}
	
	@Override
	@Transactional
	public void save(HesabTafsiliTemplateEntity entity) {
		checkHesabUniqueNess(entity, entity.getOrganId());
		super.save(entity);
	}
	
	private void checkHesabUniqueNess(HesabTafsiliTemplateEntity entity,	Long organId) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organId@eq", organId);
		checkUniqueNess(entity, HesabTafsiliEntity.PROP_NAME, entity.getName(),	localFilter, false);
		checkUniqueNess(entity, HesabTafsiliEntity.PROP_CODE, entity.getCode(),	localFilter, false);
	}

	
	@Transactional
	public HesabTafsiliTemplateEntity createHesabTafsiliTemplate(Long hesabTafsiliCode, String hesabTafsiliName, Long organId, TafsilTypeEnum tafsilType, String description, Integer level, String organName) {
		
		HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity = new HesabTafsiliTemplateEntity();
		hesabTafsiliTemplateEntity.setScope(HesabScopeEnum.LCAOL);
		hesabTafsiliTemplateEntity.setName(hesabTafsiliName);
		hesabTafsiliTemplateEntity.setCode(hesabTafsiliCode);
		hesabTafsiliTemplateEntity.setOrganId(organId);
		hesabTafsiliTemplateEntity.setOrganName(organName);
		hesabTafsiliTemplateEntity.setTafsilType(tafsilType);
		hesabTafsiliTemplateEntity.setDescription(description);
		hesabTafsiliTemplateEntity.setHidden(false);
		hesabTafsiliTemplateEntity.setLevel(level);
		save(hesabTafsiliTemplateEntity);
		return hesabTafsiliTemplateEntity;
	}

	public HesabTafsiliTemplateEntity loadByCode(Long code, List<Long> topOrganList) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		localFilter.put("organId@in", topOrganList);
		return load(null, localFilter);
	}

	public HesabTafsiliTemplateEntity loadByCodeInCurrentOrgan(Long code, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		localFilter.put("organId@eq", organId);
		return load(null, localFilter);
	}
	
	public HesabTafsiliTemplateEntity loadByName(String name, String topOrganCode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq", name);
		localFilter.put("organ.code@startlk", topOrganCode);
		return load(null, localFilter);
	}
	
	public HesabTafsiliTemplateEntity loadByNameInCurrentOrgan(String name, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq", name);
		localFilter.put("organId@eq", organId);
		return load(null, localFilter);
	}
	
	@Transactional(readOnly=true)
	public List<HesabTafsiliTemplateEntity> getActiveTafsilis(List<Long> topOrganList) {
		
//		List<Long> topOrganList = getTopOrgansIdList(organId);
		
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organId@in", topOrganList);
		
		localFilter.put("hidden@eq", Boolean.FALSE);
		return getDataList(null, localFilter);
	}

	@Transactional(readOnly=true)
	public Map<Long, List<ListOrderedMap<String, Object>>> getTafsiliChildTemplateMap(List<Long> topOrganList) {
		Map<Long, List<ListOrderedMap<String, Object>>> tafsiliChildTemplateMap = new HashMap<>();
		List<HesabTafsiliTemplateEntity> tafsiliList = getActiveTafsilis(topOrganList);
		for (HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity : tafsiliList) {
			List<ListOrderedMap<String, Object>> hesabTafsiliTemplateList = new ArrayList<ListOrderedMap<String, Object>>();
			for (HesabTafsiliTemplateEntity tafsiliEntity : hesabTafsiliTemplateEntity.getChilds()) {
				if(tafsiliEntity.getHidden().equals(false)){
					ListOrderedMap<String, Object> bankBranchItemMap = new ListOrderedMap<String, Object>();
					bankBranchItemMap.put("value",tafsiliEntity.getID());
					bankBranchItemMap.put("label",tafsiliEntity.getDesc());
					hesabTafsiliTemplateList.add(bankBranchItemMap);
				}
			}
			tafsiliChildTemplateMap.put(hesabTafsiliTemplateEntity.getId(), hesabTafsiliTemplateList);
		}
		return tafsiliChildTemplateMap;
	}

	@Transactional(readOnly=true)
	public List<HesabTafsiliTemplateEntity> getRootHesabs(List<Long> topOrganList) {
		List<HesabTafsiliTemplateEntity> rootList = new ArrayList<HesabTafsiliTemplateEntity>();
		List<HesabTafsiliTemplateEntity> activeTafsilis = getActiveTafsilis(topOrganList);
		for (HesabTafsiliTemplateEntity hesabTafsiliEntity : activeTafsilis) {
			if(hesabTafsiliEntity.getChilds()==null || hesabTafsiliEntity.getChilds().isEmpty())
				if(hesabTafsiliEntity.getMoeenTafsiliTemplate()!= null && !hesabTafsiliEntity.getMoeenTafsiliTemplate().isEmpty())
				rootList.add(hesabTafsiliEntity);
		}
		return rootList;
	}
}