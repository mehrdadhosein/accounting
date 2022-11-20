package ir.serajsamaneh.accounting.hesabmoeentemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ir.serajsamaneh.accounting.enumeration.HesabScopeEnum;
import ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateService;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateService;
import ir.serajsamaneh.core.base.BaseEntityService;
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class HesabMoeenTemplateService extends BaseEntityService<HesabMoeenTemplateEntity, Long> {

	@Override
	protected HesabMoeenTemplateDAO getMyDAO() {
		return hesabMoeenTemplateDAO;
	}

	@Autowired
	HesabMoeenTemplateDAO hesabMoeenTemplateDAO;
	@Autowired
	HesabKolTemplateService hesabKolTemplateService;
	@Autowired
	MoeenTafsiliTemplateService moeenTafsiliTemplateService;

	@Override
	@Transactional
	public void saveOrUpdateStateLess(HesabMoeenTemplateEntity entity) {
		if (entity.getHidden() == null)
			entity.setHidden(false);
		super.saveOrUpdateStateLess(entity);
	}

	@Transactional
	public void saveGlobal(HesabMoeenTemplateEntity entity) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("scope@eq", HesabScopeEnum.GLOBAL);
		checkUniqueNess(entity, HesabMoeenEntity.PROP_NAME, entity.getName(), localFilter, false);
		checkUniqueNess(entity, HesabMoeenEntity.PROP_CODE, entity.getCode(), localFilter, false);
		save(entity);
	}

	@Override
	@Transactional
	public void save(HesabMoeenTemplateEntity entity) {
		if (entity.getHidden() == null)
			entity.setHidden(false);
		Boolean isNew = (entity.getId() != null ? false : true);
		super.save(entity);
		logAction(isNew, entity);
	}

	private void checkHesabUniqueNess(HesabMoeenTemplateEntity entity, Long organId) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organId@eq", organId);
		checkUniqueNess(entity, HesabTafsiliEntity.PROP_NAME, entity.getName(), localFilter, false);
		checkUniqueNess(entity, HesabTafsiliEntity.PROP_CODE, entity.getCode(), localFilter, false);
	}
//	public HesabMoeenTemplateEntity getGlobalHesabMoeenTemplateByCode(String hesabCode) {
//		return getMyDAO().getHesabMoeenTemplateByCode(hesabCode);
//	}

	@Transactional(readOnly = false)
	public HesabMoeenTemplateEntity createHesabMoeenTemplate(String hesabMoeenCode, String hesabMoeenName,
			String hesabKolCode, Long organId, Boolean hidden, String organName) {
		HesabMoeenTemplateEntity hesabMoeenTemplateEntity = new HesabMoeenTemplateEntity();
		hesabMoeenTemplateEntity.setScope(HesabScopeEnum.LCAOL);
		hesabMoeenTemplateEntity.setName(hesabMoeenName);
		hesabMoeenTemplateEntity.setCode(hesabMoeenCode);
		hesabMoeenTemplateEntity.setOrganId(organId);
		hesabMoeenTemplateEntity.setOrganName(organName);
		hesabMoeenTemplateEntity.setHidden(hidden);
		hesabMoeenTemplateEntity
				.setHesabKolTemplate(hesabKolTemplateService.getHesabKolTemplateByCode(hesabKolCode, organId));
		checkHesabUniqueNess(hesabMoeenTemplateEntity, organId);
		hesabMoeenTemplateDAO.saveOrUpdate(hesabMoeenTemplateEntity);
		getLogger().info("hesabMoeen created : " + hesabMoeenCode);
		return hesabMoeenTemplateEntity;

	}

	public HesabMoeenTemplateEntity loadByCode(String organCode, String topOrganCode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", organCode);
		localFilter.put("organ.code@startlk", topOrganCode);
		return load(null, localFilter);
	}

	public HesabMoeenTemplateEntity loadByName(String name, String topOrganCode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq", name);
		localFilter.put("organ.code@startlk", topOrganCode);
		return load(null, localFilter);
	}

	public HesabMoeenTemplateEntity loadByCodeInCurrentOrgan(String code, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		localFilter.put("organId@eq", organId);
		return load(null, localFilter);
	}

	public HesabMoeenTemplateEntity loadByNameInCurrentOrgan(String name, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq", name);
		localFilter.put("organId@eq", organId);
		return load(null, localFilter);
	}

	public List<HesabMoeenTemplateEntity> getActiveMoeens(List<Long> topOrganList) {
//		List<Long> topOrganList = getTopOrgansIdList(organId);

		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organId@in", topOrganList);
		localFilter.put("hidden@eq", Boolean.FALSE);
		return getDataList(null, localFilter);
	}

	public List<HesabMoeenTemplateEntity> getActiveMoeens(Long hesabKolTemplateId, List<Long> topOrganList) {
//		List<Long> topOrganList = getTopOrgansIdList(organEntity);

		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organId@in", topOrganList);
		localFilter.put("hidden@eq", Boolean.FALSE);
		localFilter.put("hesabKolTemplate.id@eq", hesabKolTemplateId);
		return getDataList(null, localFilter);
	}

	@Transactional(readOnly = true)
	public List<HesabMoeenTemplateEntity> getRootHesabs(Long organId, List<Long> topOrganList) {

		List<HesabMoeenTemplateEntity> rootList = new ArrayList<HesabMoeenTemplateEntity>();
		Set<HesabMoeenTemplateEntity> removeList = new HashSet<HesabMoeenTemplateEntity>();

		List<HesabMoeenTemplateEntity> hesabMoeenList = getActiveMoeens(topOrganList);

		Map<String, Object> moeenTafsiliFilter = new HashMap<String, Object>();
		moeenTafsiliFilter.put("hesabMoeenTemplate.organId@eq", organId);
		List<MoeenTafsiliTemplateEntity> moeenTafsiliList = moeenTafsiliTemplateService.getDataList(null,
				moeenTafsiliFilter);

		for (MoeenTafsiliTemplateEntity moeenTafsiliEntity : moeenTafsiliList) {
			HesabMoeenTemplateEntity hesabMoeenEntity = moeenTafsiliEntity.getHesabMoeenTemplate();
			if (hesabMoeenList.contains(hesabMoeenEntity))
				removeList.add(hesabMoeenEntity);
		}

		rootList.addAll(hesabMoeenList);
		rootList.removeAll(removeList);
		return rootList;
	}

}