package ir.serajsamaneh.accounting.hesabkoltemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
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
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class HesabKolTemplateService extends BaseEntityService<HesabKolTemplateEntity, Long> {

	@Override
	protected HesabKolTemplateDAO getMyDAO() {
		return hesabKolTemplateDAO;
	}

	@Autowired
	HesabKolTemplateDAO hesabKolTemplateDAO;
	@Autowired
	HesabMoeenTemplateService hesabMoeenTemplateService;
	@Autowired
	HesabTafsiliTemplateService hesabTafsiliTemplateService;
	@Autowired
	HesabGroupService hesabGroupService;
	@Autowired
	SaalMaaliService saalMaaliService;
	@Autowired
	HesabGroupTemplateDAO hesabGroupTemplateDAO;
	@Autowired
	HesabTafsiliTemplateDAO hesabTafsiliTemplateDAO;
	@Autowired
	HesabMoeenTemplateDAO hesabMoeenTemplateDAO;

	@Transactional(readOnly = false)
	public void init() {
//		getLogger().info("initializing accounts");
//		createDefaultAccounts();
	}

	@Transactional(readOnly = false)
	public void createHesabGroup(Element hesbaGroupElem, Long organId, String organName) {
		String typeEnum = hesbaGroupElem.getAttribute("type");
		Long code = Long.valueOf(hesbaGroupElem.getAttribute("code"));
		String mahyatEnum = hesbaGroupElem.getAttribute("mahyat");
		String hesabGroupName = hesbaGroupElem.getAttribute("name");
		HesabGroupTemplateEntity hesabGroupTemplateEntity = hesabGroupTemplateDAO.getHesabGroupByCode(code, organId);
		if (hesabGroupTemplateEntity == null)
			hesabGroupTemplateEntity = new HesabGroupTemplateEntity();
		hesabGroupTemplateEntity.setName(hesabGroupName);
		hesabGroupTemplateEntity.setMahyatGroup(MahyatGroupEnum.valueOf(mahyatEnum));
		hesabGroupTemplateEntity.setType(HesabTypeEnum.valueOf(typeEnum));
		hesabGroupTemplateEntity.setCode(code);
		hesabGroupTemplateEntity.setOrganId(organId);
		hesabGroupTemplateEntity.setOrganName(organName);
		hesabGroupTemplateDAO.saveOrUpdate(hesabGroupTemplateEntity);
		getLogger().info("hesabGroupTempate created : " + hesabGroupName);
	}

	@Transactional(readOnly = false)
	public void createHesabKolTemplate(Element hesbaKolElem, Long organId, String organName) {
		String hesabKolCode = hesbaKolElem.getAttribute("code");
		String hesabKolName = hesbaKolElem.getAttribute("name");
		Long hesabGroupCode = Long.valueOf(hesbaKolElem.getAttribute("HesabGroup"));
		String mahyatKol = hesbaKolElem.getAttribute("mahyat");

		createHesabKolTemplate(hesabKolCode, hesabKolName, hesabGroupCode, mahyatKol, organId, organName);
	}

	@Transactional(readOnly = false)
	public void createHesabTafsiliTemplate(Element hesabTafsiliElem, Long organId, String organName) {
		String hesabMoeenCode = hesabTafsiliElem.getAttribute("hesabMoeen");
		String hesabTafsiliCode = hesabTafsiliElem.getAttribute("code");
		String hesabTafsiliName = hesabTafsiliElem.getAttribute("name");
		hesabTafsiliTemplateDAO.save(Long.valueOf(hesabTafsiliCode), hesabTafsiliName, hesabMoeenCode, organId, 1,
				organName);
		getLogger().info("hesabTafsili created : " + hesabTafsiliCode);

	}

	@Transactional(readOnly = false)
	public void createHesabMoeenTemplate(Element hesabMoeenElem, Long organId, String organName) {
		String hesabKolCode = hesabMoeenElem.getAttribute("hesabKol");
		String hesabMoeenCode = hesabMoeenElem.getAttribute("code");
		String hesabMoeenName = hesabMoeenElem.getAttribute("name");

		HesabMoeenTemplateEntity hesabMoeenTemplateEntity = hesabMoeenTemplateDAO
				.getHesabMoeenTemplateByCode(hesabMoeenCode, organId);
		if (hesabMoeenTemplateEntity == null)
			hesabMoeenTemplateEntity = new HesabMoeenTemplateEntity();
		hesabMoeenTemplateEntity.setScope(HesabScopeEnum.GLOBAL);
		hesabMoeenTemplateEntity.setName(hesabMoeenName);
		hesabMoeenTemplateEntity.setCode(hesabMoeenCode);
		hesabMoeenTemplateEntity.setHidden(false);
		hesabMoeenTemplateEntity.setHesabKolTemplate(getHesabKolTemplateByCode(hesabKolCode, organId));
		if (hesabMoeenTemplateEntity.getHesabKolTemplate() == null)
			System.out.println(hesabMoeenTemplateEntity.getHesabKolTemplate());

		hesabMoeenTemplateEntity.setOrganId(organId);
		hesabMoeenTemplateEntity.setOrganName(organName);

		hesabMoeenTemplateDAO.saveOrUpdate(hesabMoeenTemplateEntity);
		getLogger().info("hesabMoeen created : " + hesabMoeenCode);

	}

//	@Transactional(readOnly = false)
//	public HesabKolTemplateEntity createHesabKolTemplate(String hesabKolCode, String hesabKolName,
//			String hesabGroupCode, String mahyatKol, OrganEntity organEntity) {
//		return getMyDAO().createHesabKolTemplate(hesabKolCode, hesabKolName, hesabGroupCode, mahyatKol, organEntity);
//	}

	@Transactional(readOnly = false)
	public HesabKolTemplateEntity createHesabKolTemplate(String hesabKolCode, String hesabKolName, Long hesabGroupCode,
			String mahyatKol, Long organId, String organName) {
		HesabGroupTemplateEntity hesabGroupTemplateEntity = hesabGroupTemplateDAO.getHesabGroupByCode(hesabGroupCode,
				organId);
		HesabKolTemplateEntity hesabKolTemplateEntity = getHesabKolTemplateByCode(hesabKolCode, organId);
		if (hesabKolTemplateEntity == null) {
			hesabKolTemplateEntity = getHesabKolTemplateByName(hesabKolName, organId);
			if (hesabKolTemplateEntity == null) {
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
		getLogger().info("hesabKol created : " + hesabKolCode);
		return hesabKolTemplateEntity;
	}

	@Override
	public void saveOrUpdateStateLess(HesabKolTemplateEntity entity) {
		if (entity.getHidden() == null)
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

	public List<HesabKolTemplateEntity> getCurrentHesabKolTemplateList(List<Long> topOrganList) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		// localFilter.put("organId@eq",organId);
		localFilter.put("organId@in", topOrganList);
		localFilter.put("hidden@eq", Boolean.FALSE);
		List<HesabKolTemplateEntity> hesabKolList = getDataList(null, localFilter, HesabKolEntity.PROP_CODE, true,
				false);
		return hesabKolList;
	}

	@Transactional
	public void save(HesabKolTemplateEntity entity) {
		Boolean isNew = (entity.getId() != null ? false : true);
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

	@Transactional(readOnly = true)
	public List<ListOrderedMap<String, String>> getRootHesabs(Long organId, List<Long> topOrganList) {
		List<ListOrderedMap<String, String>> rootHesabsList;
		List<HesabMoeenTemplateEntity> rootHesabMoeens = hesabMoeenTemplateService.getRootHesabs(organId, topOrganList);

		rootHesabsList = new ArrayList<ListOrderedMap<String, String>>();

		for (HesabMoeenTemplateEntity hesabMoeenEntity : rootHesabMoeens) {
			ListOrderedMap<String, String> hesabMoeenMap = new ListOrderedMap<String, String>();
			hesabMoeenMap.put("value", "Moeen_" + hesabMoeenEntity.getId());
			hesabMoeenMap.put("label", hesabMoeenEntity.getDesc());
			rootHesabsList.add(hesabMoeenMap);
		}
		List<HesabTafsiliTemplateEntity> rootHesabTafsilies = hesabTafsiliTemplateService.getRootHesabs(topOrganList);
		for (HesabTafsiliTemplateEntity hesabTafsiliEntity : rootHesabTafsilies) {
			ListOrderedMap<String, String> hesabTafsiliMap = new ListOrderedMap<String, String>();
			hesabTafsiliMap.put("value", "Tafsili_" + hesabTafsiliEntity.getId());
			hesabTafsiliMap.put("label", hesabTafsiliEntity.getDesc());
			rootHesabsList.add(hesabTafsiliMap);
		}
		return rootHesabsList;
	}
}