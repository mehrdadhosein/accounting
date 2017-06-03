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
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateDAO;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateDAO;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.organ.OrganEntity;

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
	public void createHesabGroup(Element hesbaGroupElem, OrganEntity organ) {
		String typeEnum = hesbaGroupElem.getAttribute("type");
		String code = hesbaGroupElem.getAttribute("code");
		String mahyatEnum = hesbaGroupElem.getAttribute("mahyat");
		String hesabGroupName = hesbaGroupElem.getAttribute("name");
		HesabGroupTemplateEntity hesabGroupTemplateEntity = getHesabGroupTemplateDAO().getHesabGroupByCode(code, organ);
		if (hesabGroupTemplateEntity == null)
			hesabGroupTemplateEntity = new HesabGroupTemplateEntity();
		hesabGroupTemplateEntity.setName(hesabGroupName);
		hesabGroupTemplateEntity.setMahyatGroup(MahyatGroupEnum.valueOf(mahyatEnum));
		hesabGroupTemplateEntity.setType(HesabTypeEnum.valueOf(typeEnum));
		hesabGroupTemplateEntity.setCode(code);
		hesabGroupTemplateEntity.setOrgan(organ);
		getHesabGroupTemplateDAO().saveOrUpdate(hesabGroupTemplateEntity);
		getLogger().info("hesabGroupTempate created : "+hesabGroupName);
	}

	@Transactional(readOnly = false)
	public void createHesabKolTemplate(Element hesbaKolElem, OrganEntity organEntity) {
		String hesabKolCode = hesbaKolElem.getAttribute("code");
		String hesabKolName = hesbaKolElem.getAttribute("name");
		String hesabGroupCode = hesbaKolElem.getAttribute("HesabGroup");
		String mahyatKol = hesbaKolElem.getAttribute("mahyat");
		
		createHesabKolTemplate(hesabKolCode, hesabKolName, hesabGroupCode, mahyatKol, organEntity);
	}

	@Transactional(readOnly = false)
	public void createHesabTafsiliTemplate(Element hesabTafsiliElem, OrganEntity organEntity) {
		String hesabMoeenCode = hesabTafsiliElem.getAttribute("hesabMoeen");
		String hesabTafsiliCode = hesabTafsiliElem.getAttribute("code");
		String hesabTafsiliName = hesabTafsiliElem.getAttribute("name");
		getHesabTafsiliTemplateDAO().save(new Long(hesabTafsiliCode), hesabTafsiliName, hesabMoeenCode, organEntity);
		getLogger().info("hesabTafsili created : "+hesabTafsiliCode);
		
	}

	@Transactional(readOnly = false)
	public void createHesabMoeenTemplate(Element hesabMoeenElem, OrganEntity organ) {
		String hesabKolCode = hesabMoeenElem.getAttribute("hesabKol");
		String hesabMoeenCode = hesabMoeenElem.getAttribute("code");
		String hesabMoeenName = hesabMoeenElem.getAttribute("name");
		
		HesabMoeenTemplateEntity hesabMoeenTemplateEntity = getHesabMoeenTemplateDAO().getHesabMoeenTemplateByCode(hesabMoeenCode, organ);
		if (hesabMoeenTemplateEntity == null)
			hesabMoeenTemplateEntity = new HesabMoeenTemplateEntity();
		hesabMoeenTemplateEntity.setScope(HesabScopeEnum.GLOBAL);
		hesabMoeenTemplateEntity.setName(hesabMoeenName);
		hesabMoeenTemplateEntity.setCode(hesabMoeenCode);
		hesabMoeenTemplateEntity.setHidden(false);
		hesabMoeenTemplateEntity.setHesabKolTemplate(getHesabKolTemplateByCode(hesabKolCode, organ));
		if(hesabMoeenTemplateEntity.getHesabKolTemplate() == null)
			System.out.println(hesabMoeenTemplateEntity.getHesabKolTemplate());
		
		hesabMoeenTemplateEntity.setOrgan(organ);
		
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
			String hesabGroupCode, String mahyatKol, OrganEntity organEntity) {
		HesabGroupTemplateEntity hesabGroupTemplateEntity = getHesabGroupTemplateDAO().getHesabGroupByCode(hesabGroupCode, organEntity);
		HesabKolTemplateEntity hesabKolTemplateEntity = getHesabKolTemplateByCode(hesabKolCode, organEntity);
		if (hesabKolTemplateEntity == null){
			hesabKolTemplateEntity = new HesabKolTemplateEntity();
			hesabKolTemplateEntity.setHidden(false);
		}
		hesabKolTemplateEntity.setName(hesabKolName);
		hesabKolTemplateEntity.setCode(hesabKolCode);
		hesabKolTemplateEntity.setHesabGroupTemplate(hesabGroupTemplateEntity);
		hesabKolTemplateEntity.setMahyatKol(MahyatKolEnum.valueOf(mahyatKol));
		hesabKolTemplateEntity.setOrgan(organEntity);
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
	public HesabKolTemplateEntity getHesabKolTemplateByCode(String hesabCode, OrganEntity organEntity) {
		return getMyDAO().getHesabKolTemplateByCode(hesabCode, organEntity);
	}

	public HesabKolTemplateEntity loadHierarchical(String code, OrganEntity organEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		//localFilter.put("organ.id@eqORorgan@isNull", Arrays.asList(organ.getId(),"ding"));
		localFilter.put("organ.code@startlk", getTopOrgan(organEntity).getCode());
		return load(null, localFilter);
	}

	public HesabKolTemplateEntity loadLocal(String code, OrganEntity organEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		//localFilter.put("organ.id@eqORorgan@isNull", Arrays.asList(organ.getId(),"ding"));
		localFilter.put("organ.id@eq", organEntity.getId());
		return load(null, localFilter);
	}
	

	public List<HesabKolTemplateEntity> getCurrentHesabKolTemplateList(OrganEntity organEntity) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		//localFilter.put("organ.id@eq",organEntity.getId());
		localFilter.put("organ.code@startlk", getTopOrgan(organEntity).getCode());
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

	public HesabKolTemplateEntity loadByCodeInCurrentOrgan(String code, OrganEntity organEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		//localFilter.put("organ.id@eqORorgan@isNull", Arrays.asList(organ.getId(),"ding"));
		localFilter.put("organ.id@eq", organEntity.getId());
		return load(null, localFilter);
	}
	
	public HesabKolTemplateEntity loadByNameInCurrentOrgan(String name, OrganEntity organEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq", name);
		//localFilter.put("organ.id@eqORorgan@isNull", Arrays.asList(organ.getId(),"ding"));
		localFilter.put("organ.id@eq", organEntity.getId());
		return load(null, localFilter);
	}

	@Transactional(readOnly=true)
	public List<ListOrderedMap> getRootHesabs(OrganEntity currentOrgan) {
		List<ListOrderedMap> rootHesabsList;
		List<HesabMoeenTemplateEntity> rootHesabMoeens = getHesabMoeenTemplateService().getRootHesabs(currentOrgan);
		
		rootHesabsList = new ArrayList<ListOrderedMap>(); 
		
		for (HesabMoeenTemplateEntity hesabMoeenEntity : rootHesabMoeens) {
			ListOrderedMap hesabMoeenMap = new ListOrderedMap();
			hesabMoeenMap.put("value","Moeen_"+hesabMoeenEntity.getId());
			hesabMoeenMap.put("label",hesabMoeenEntity.getDesc());
			rootHesabsList.add(hesabMoeenMap);
		}
		List<HesabTafsiliTemplateEntity> rootHesabTafsilies = getHesabTafsiliTemplateService().getRootHesabs(currentOrgan);
		for (HesabTafsiliTemplateEntity hesabTafsiliEntity : rootHesabTafsilies) {
			ListOrderedMap hesabTafsiliMap = new ListOrderedMap();
			hesabTafsiliMap.put("value","Tafsili_"+hesabTafsiliEntity.getId());
			hesabTafsiliMap.put("label",hesabTafsiliEntity.getDesc());
			rootHesabsList.add(hesabTafsiliMap);
		}
		return rootHesabsList;
	}
}