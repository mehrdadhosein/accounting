package ir.serajsamaneh.accounting.hesabkoltemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Element;

import ir.serajsamaneh.accounting.enumeration.HesabScopeEnum;
import ir.serajsamaneh.accounting.enumeration.HesabTypeEnum;
import ir.serajsamaneh.accounting.enumeration.MahyatGroupEnum;
import ir.serajsamaneh.accounting.enumeration.MahyatKolEnum;
import ir.serajsamaneh.accounting.hesabgroup.HesabGroupService;
import ir.serajsamaneh.accounting.hesabgrouptemplate.HesabGroupTemplateDAO;
import ir.serajsamaneh.accounting.hesabgrouptemplate.HesabGroupTemplateEntity;
import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateDAO;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateDAO;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.core.base.BaseEntityService;

public class HesabKolTemplateService extends
		BaseEntityService<HesabKolTemplateEntity, Long> {

	@Override
	protected HesabKolTemplateDAO getMyDAO() {
		return hesabKolTemplateDAO;
	}

	HesabKolTemplateDAO hesabKolTemplateDAO;
	HesabMoeenTemplateService hesabMoeenTemplateService;
	HesabTafsiliTemplateService hesabTafsiliTemplateService;
	HesabGroupService hesabGroupService;
	SaalMaaliService saalMaaliService;

	HesabGroupTemplateDAO hesabGroupTemplateDAO;
	HesabTafsiliTemplateDAO hesabTafsiliTemplateDAO;
	HesabMoeenTemplateDAO hesabMoeenTemplateDAO;
	public HesabMoeenTemplateDAO getHesabMoeenTemplateDAO() {
		return hesabMoeenTemplateDAO;
	}

	public void setHesabMoeenTemplateDAO(HesabMoeenTemplateDAO hesabMoeenTemplateDAO) {
		this.hesabMoeenTemplateDAO = hesabMoeenTemplateDAO;
	}


	public HesabTafsiliTemplateDAO getHesabTafsiliTemplateDAO() {
		return hesabTafsiliTemplateDAO;
	}

	public void setHesabTafsiliTemplateDAO(
			HesabTafsiliTemplateDAO hesabTafsiliTemplateDAO) {
		this.hesabTafsiliTemplateDAO = hesabTafsiliTemplateDAO;
	}


	public HesabGroupTemplateDAO getHesabGroupTemplateDAO() {
		return hesabGroupTemplateDAO;
	}

	public void setHesabGroupTemplateDAO(HesabGroupTemplateDAO hesabGroupTemplateDAO) {
		this.hesabGroupTemplateDAO = hesabGroupTemplateDAO;
	}



	public HesabMoeenTemplateService getHesabMoeenTemplateService() {
		return hesabMoeenTemplateService;
	}

	public void setHesabMoeenTemplateService(
			HesabMoeenTemplateService hesabMoeenTemplateService) {
		this.hesabMoeenTemplateService = hesabMoeenTemplateService;
	}

	public HesabTafsiliTemplateService getHesabTafsiliTemplateService() {
		return hesabTafsiliTemplateService;
	}

	public void setHesabTafsiliTemplateService(
			HesabTafsiliTemplateService hesabTafsiliTemplateService) {
		this.hesabTafsiliTemplateService = hesabTafsiliTemplateService;
	}

	public HesabGroupService getHesabGroupService() {
		return hesabGroupService;
	}

	public void setHesabGroupService(HesabGroupService hesabGroupService) {
		this.hesabGroupService = hesabGroupService;
	}

	public SaalMaaliService getSaalMaaliService() {
		return saalMaaliService;
	}

	public void setSaalMaaliService(SaalMaaliService saalMaaliService) {
		this.saalMaaliService = saalMaaliService;
	}

	public void setHesabKolTemplateDAO(HesabKolTemplateDAO hesabKolTemplateDAO) {
		this.hesabKolTemplateDAO = hesabKolTemplateDAO;
	}

	public HesabKolTemplateDAO getHesabKolTemplateDAO() {
		return hesabKolTemplateDAO;
	}

	@Transactional(readOnly = false)
	public void init() {
//		getLogger().info("initializing accounts");
//		createDefaultAccounts();
	}
	
	@Transactional(readOnly = false)
	public void createHesabGroup(Element hesbaGroupElem, Long organId, String organName) {
		String typeEnum = hesbaGroupElem.getAttribute("type");
		Long code = new Long(hesbaGroupElem.getAttribute("code"));
		String mahyatEnum = hesbaGroupElem.getAttribute("mahyat");
		String hesabGroupName = hesbaGroupElem.getAttribute("name");
		HesabGroupTemplateEntity hesabGroupTemplateEntity = getHesabGroupTemplateDAO().getHesabGroupByCode(code, organId);
		if (hesabGroupTemplateEntity == null)
			hesabGroupTemplateEntity = new HesabGroupTemplateEntity();
		hesabGroupTemplateEntity.setName(hesabGroupName);
		hesabGroupTemplateEntity.setMahyatGroup(MahyatGroupEnum.valueOf(mahyatEnum));
		hesabGroupTemplateEntity.setType(HesabTypeEnum.valueOf(typeEnum));
		hesabGroupTemplateEntity.setCode(code);
		hesabGroupTemplateEntity.setOrganId(organId);
		hesabGroupTemplateEntity.setOrganName(organName);
		getHesabGroupTemplateDAO().saveOrUpdate(hesabGroupTemplateEntity);
		getLogger().info("hesabGroupTempate created : "+hesabGroupName);
	}

	@Transactional(readOnly = false)
	public void createHesabKolTemplate(Element hesbaKolElem, Long organId, String organName) {
		String hesabKolCode = hesbaKolElem.getAttribute("code");
		String hesabKolName = hesbaKolElem.getAttribute("name");
		Long hesabGroupCode = new Long(hesbaKolElem.getAttribute("HesabGroup"));
		String mahyatKol = hesbaKolElem.getAttribute("mahyat");
		
		createHesabKolTemplate(hesabKolCode, hesabKolName, hesabGroupCode, mahyatKol, organId, organName);
	}

	@Transactional(readOnly = false)
	public void createHesabTafsiliTemplate(Element hesabTafsiliElem, Long organId, String organName) {
		String hesabMoeenCode = hesabTafsiliElem.getAttribute("hesabMoeen");
		String hesabTafsiliCode = hesabTafsiliElem.getAttribute("code");
		String hesabTafsiliName = hesabTafsiliElem.getAttribute("name");
		getHesabTafsiliTemplateDAO().save(new Long(hesabTafsiliCode), hesabTafsiliName, hesabMoeenCode, organId, 1, organName);
		getLogger().info("hesabTafsili created : "+hesabTafsiliCode);
		
	}

	@Transactional(readOnly = false)
	public void createHesabMoeenTemplate(Element hesabMoeenElem, Long organId, String organName) {
		String hesabKolCode = hesabMoeenElem.getAttribute("hesabKol");
		String hesabMoeenCode = hesabMoeenElem.getAttribute("code");
		String hesabMoeenName = hesabMoeenElem.getAttribute("name");
		
		HesabMoeenTemplateEntity hesabMoeenTemplateEntity = getHesabMoeenTemplateDAO().getHesabMoeenTemplateByCode(hesabMoeenCode, organId);
		if (hesabMoeenTemplateEntity == null)
			hesabMoeenTemplateEntity = new HesabMoeenTemplateEntity();
		hesabMoeenTemplateEntity.setScope(HesabScopeEnum.GLOBAL);
		hesabMoeenTemplateEntity.setName(hesabMoeenName);
		hesabMoeenTemplateEntity.setCode(hesabMoeenCode);
		hesabMoeenTemplateEntity.setHidden(false);
		hesabMoeenTemplateEntity.setHesabKolTemplate(getHesabKolTemplateByCode(hesabKolCode, organId));
		if(hesabMoeenTemplateEntity.getHesabKolTemplate() == null)
			System.out.println(hesabMoeenTemplateEntity.getHesabKolTemplate());
		
		hesabMoeenTemplateEntity.setOrganId(organId);
		hesabMoeenTemplateEntity.setOrganName(organName);
		
		getHesabMoeenTemplateDAO().saveOrUpdate(hesabMoeenTemplateEntity);
		getLogger().info("hesabMoeen created : "+hesabMoeenCode);
		
	}

//	@Transactional(readOnly = false)
//	public HesabKolTemplateEntity createHesabKolTemplate(String hesabKolCode, String hesabKolName,
//			String hesabGroupCode, String mahyatKol, OrganEntity organEntity) {
//		return getMyDAO().createHesabKolTemplate(hesabKolCode, hesabKolName, hesabGroupCode, mahyatKol, organEntity);
//	}

	@Transactional(readOnly = false)
	public HesabKolTemplateEntity createHesabKolTemplate(String hesabKolCode, String hesabKolName,
			Long hesabGroupCode, String mahyatKol, Long organId, String organName) {
		HesabGroupTemplateEntity hesabGroupTemplateEntity = getHesabGroupTemplateDAO().getHesabGroupByCode(hesabGroupCode, organId);
		HesabKolTemplateEntity hesabKolTemplateEntity = getHesabKolTemplateByCode(hesabKolCode, organId);
		if (hesabKolTemplateEntity == null){
			hesabKolTemplateEntity = getHesabKolTemplateByName(hesabKolName, organId);
			if(hesabKolTemplateEntity == null) {
				hesabKolTemplateEntity = new HesabKolTemplateEntity();
				hesabKolTemplateEntity.setHidden(false);
			}
		}
		hesabKolTemplateEntity.setName(hesabKolName);
		hesabKolTemplateEntity.setCode(hesabKolCode);
		hesabKolTemplateEntity.setHesabGroupTemplate(hesabGroupTemplateEntity);
		hesabKolTemplateEntity.setMahyatKol(MahyatKolEnum.valueOf(mahyatKol));
		hesabKolTemplateEntity.setOrganId(organId);
		hesabKolTemplateEntity.setOrganName(organName);
		saveOrUpdate(hesabKolTemplateEntity);
		getLogger().info("hesabKol created : "+hesabKolCode);
		return hesabKolTemplateEntity;
	}
	

	


	@Override
	public void saveOrUpdateStateLess(HesabKolTemplateEntity entity) {
		if(entity.getHidden() == null)
			entity.setHidden(false);
		super.saveOrUpdateStateLess(entity);
	}
	
	@Transactional
	public HesabKolTemplateEntity getHesabKolTemplateByCode(String hesabCode, Long organId) {
		return getMyDAO().getHesabKolTemplateByCode(hesabCode, organId);
	}
	
	@Transactional
	public HesabKolTemplateEntity getHesabKolTemplateByName(String hesabKolName, Long organId) {
		return getMyDAO().getHesabKolTemplateByName(hesabKolName, organId);
	}

	public HesabKolTemplateEntity loadHierarchical(String code, String topOrganCode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		localFilter.put("organ.code@startlk", topOrganCode);
		return load(null, localFilter);
	}

	public HesabKolTemplateEntity loadLocal(String code, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		localFilter.put("organId@eq", organId);
		return load(null, localFilter);
	}
	

	public List<HesabKolTemplateEntity> getCurrentHesabKolTemplateList(String topOrganCode) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		//localFilter.put("organId@eq",organId);
		localFilter.put("organ.code@startlk", topOrganCode);
		localFilter.put("hidden@eq",Boolean.FALSE);
		List<HesabKolTemplateEntity> hesabKolList = getDataList(null, localFilter, HesabKolEntity.PROP_CODE, true,false);
		return hesabKolList;
	}
	@Transactional
	public void save(HesabKolTemplateEntity entity) {
		Boolean isNew=(entity.getId()!=null?false:true);
		super.save(entity);
		logAction(isNew, entity);
	}

	public HesabKolTemplateEntity loadByCodeInCurrentOrgan(String code, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		localFilter.put("organId@eq", organId);
		return load(null, localFilter);
	}
	
	public HesabKolTemplateEntity loadByNameInCurrentOrgan(String name, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq", name);
		localFilter.put("organId@eq", organId);
		return load(null, localFilter);
	}

	@Transactional(readOnly=true)
	public List<ListOrderedMap<String, String>> getRootHesabs(Long organId, List<Long> topOrganList) {
		List<ListOrderedMap<String, String>> rootHesabsList;
		List<HesabMoeenTemplateEntity> rootHesabMoeens = getHesabMoeenTemplateService().getRootHesabs(organId, topOrganList);
		
		rootHesabsList = new ArrayList<ListOrderedMap<String, String>>(); 
		
		for (HesabMoeenTemplateEntity hesabMoeenEntity : rootHesabMoeens) {
			ListOrderedMap<String, String> hesabMoeenMap = new ListOrderedMap<String, String>();
			hesabMoeenMap.put("value","Moeen_"+hesabMoeenEntity.getId());
			hesabMoeenMap.put("label",hesabMoeenEntity.getDesc());
			rootHesabsList.add(hesabMoeenMap);
		}
		List<HesabTafsiliTemplateEntity> rootHesabTafsilies = getHesabTafsiliTemplateService().getRootHesabs(topOrganList);
		for (HesabTafsiliTemplateEntity hesabTafsiliEntity : rootHesabTafsilies) {
			ListOrderedMap<String, String> hesabTafsiliMap = new ListOrderedMap<String, String>();
			hesabTafsiliMap.put("value","Tafsili_"+hesabTafsiliEntity.getId());
			hesabTafsiliMap.put("label",hesabTafsiliEntity.getDesc());
			rootHesabsList.add(hesabTafsiliMap);
		}
		return rootHesabsList;
	}
}