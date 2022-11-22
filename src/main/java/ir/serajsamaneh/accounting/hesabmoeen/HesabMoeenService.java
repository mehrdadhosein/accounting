package ir.serajsamaneh.accounting.hesabmoeen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import ir.serajsamaneh.accounting.enumeration.HesabScopeEnum;
import ir.serajsamaneh.accounting.enumeration.SanadStateEnum;
import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.hesabkol.HesabKolService;
import ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateEntity;
import ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateService;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliService;
import ir.serajsamaneh.accounting.moeentafsili.MoeenTafsiliEntity;
import ir.serajsamaneh.accounting.moeentafsili.MoeenTafsiliService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemService;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.exception.DuplicateException;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.exception.FieldMustContainOnlyNumbersException;
import ir.serajsamaneh.core.util.SerajMessageUtil;
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class HesabMoeenService extends BaseEntityService<HesabMoeenEntity, Long> {

	@Override
	protected HesabMoeenDAO getMyDAO() {
		return hesabMoeenDAO;
	}

	@Autowired
	HesabMoeenDAO hesabMoeenDAO;
	@Autowired
	HesabMoeenTemplateService hesabMoeenTemplateService;
	@Autowired
	HesabKolTemplateService hesabKolTemplateService;
	@Autowired
	HesabKolService hesabKolService;
	@Autowired
	SaalMaaliService saalMaaliService;
	@Autowired
	HesabTafsiliService hesabTafsiliService;
	@Autowired
	MoeenTafsiliService moeenTafsiliService;
	@Autowired
	SanadHesabdariItemService sanadHesabdariItemService;

	@Transactional
	public void save(HesabMoeenEntity entity, SaalMaaliEntity activeSaalMaaliEntity, Long currentOrganId,
			HesabKolEntity oldHesabKolEntity, List<Long> topOrganList, String currentOrganName) {
		if (entity.getId() != null && !entity.getHesabKol().getId().equals(oldHesabKolEntity.getId()))
			validateMoeenKol(entity, oldHesabKolEntity);
		save(entity, activeSaalMaaliEntity, currentOrganId, topOrganList, currentOrganName);
	}

	@Transactional
	private void save(HesabMoeenEntity entity, SaalMaaliEntity activeSaalMaaliEntity, Long currentOrganId,
			List<Long> topOrganList, String currentOrganName) {
		commonSave(entity, activeSaalMaaliEntity, currentOrganId, topOrganList, currentOrganName);
		save(entity);
		boolean isNew = (entity.getID() != null ? false : true);
		logAction(isNew, entity);
	}

	private void validateMoeenKol(HesabMoeenEntity entity, HesabKolEntity oldHesabKolEntity) {
		Map<String, Object> filter = new HashMap<>();
		filter.put("sanadHesabdari.state@neq", SanadStateEnum.EBTAL);
		filter.put("sanadHesabdari.saalMaali.id@eq", entity.getSaalMaali().getId());
		filter.put("hesabMoeen.id@eq", entity.getId());
		filter.put("hesabKol.id@eq", oldHesabKolEntity.getId());
		Integer rowCount = sanadHesabdariItemService.getRowCount(filter);
		if (rowCount > 0)
			throw new FatalException(
					SerajMessageUtil.getMessage("HesabMoeen_KolIsUsedInSomeArticles", oldHesabKolEntity));

	}

	@Transactional
	private void commonSave(HesabMoeenEntity entity, SaalMaaliEntity activeSaalMaaliEntity, Long currentOrganId,
			List<Long> topOrganList, String currentOrganName) {

		if (entity.getId() != null && entity.getSaalMaali() != null && entity.getSaalMaali().getId() != null
				&& !entity.getSaalMaali().equals(activeSaalMaaliEntity))
			throw new FatalException(SerajMessageUtil.getMessage("SaalMaali_hesabConflict"));

		if (entity.getId() == null) {
			entity.setBedehkar(0d);
			entity.setBestankr(0d);
		}

		if (!StringUtils.hasText(entity.getCode())
				&& !systemConfigService.getValue(currentOrganId, null, "HesabMoeenCodingType").equals("MANUAL")) {
			entity.setCode(generateHesabMoeenCode(entity, currentOrganId, activeSaalMaaliEntity));
		}

		if (entity.getSaalMaali() == null || entity.getSaalMaali().getId() == null)
			entity.setSaalMaali(activeSaalMaaliEntity);

//		if (!StringUtils.hasText(entity.getCode())) {
//			
//			entity.setCode(generateHesabCode(entity, currentOrgan, activeSaalMaaliEntity));
//		}
		if (entity.getHidden() == null)
			entity.setHidden(Boolean.FALSE);
		checkHesabUniqueNess(entity, activeSaalMaaliEntity, topOrganList);

		createOrUpdateRelatedHesabMoeenTemplate(entity, currentOrganId, currentOrganName);
	}

	private String generateHesabMoeenCode(HesabMoeenEntity entity, Long organId,
			SaalMaaliEntity activeSaalMaaliEntity) {
		String maxHesabMoeenCode = null;
		if (systemConfigService.getValue(organId, null, "HesabMoeenCodingType").equals("SERIAL")) {
			maxHesabMoeenCode = getMyDAO().getMaxHesabMoeenCode(null, 0, organId, activeSaalMaaliEntity);
			return maxHesabMoeenCode;
		} else if (systemConfigService.getValue(organId, null, "HesabMoeenCodingType")
				.equals("VARIABLE_HIERARCHICAL")) {
			String maxHierArchicalHesabMoeenCode = getMyDAO().getMaxHesabMoeenCode(entity.getHesabKol(), 0, organId,
					activeSaalMaaliEntity);
			return maxHierArchicalHesabMoeenCode;
		} else if (systemConfigService.getValue(organId, null, "HesabMoeenCodingType")
				.equals("CONSTANT_HIERARCHICAL")) {
			Integer HesabMoeenCodeCharactersNumber = Integer
					.parseInt(systemConfigService.getValue(organId, null, "hesabMoeenCodeCharactersNumber"));
			String maxHierArchicalHesabMoeenCode = getMyDAO().getMaxHesabMoeenCode(entity.getHesabKol(),
					HesabMoeenCodeCharactersNumber, organId, activeSaalMaaliEntity);

			return maxHierArchicalHesabMoeenCode;
		}
		throw new IllegalStateException("HesabMoeenCodingType is required");

	}

	@Transactional
	public void createOrUpdateRelatedHesabMoeenTemplate(HesabMoeenEntity entity, Long organId, String organName) {

		HesabMoeenTemplateEntity hesabMoeenTemplateEntity = hesabMoeenTemplateService
				.loadByCodeInCurrentOrgan(entity.getCode(), organId);
		if (hesabMoeenTemplateEntity == null) {
			hesabMoeenTemplateEntity = hesabMoeenTemplateService.loadByNameInCurrentOrgan(entity.getName(), organId);
			if (hesabMoeenTemplateEntity != null) {
				HesabMoeenTemplateEntity currentEntityHesabMoeenTemplate = entity.getHesabMoeenTemplate();

				// code of hesabTafsili has changed
				if (currentEntityHesabMoeenTemplate.getId() != null
						&& currentEntityHesabMoeenTemplate.equals(hesabMoeenTemplateEntity)) {
					hesabMoeenTemplateEntity.setCode(entity.getCode().toString());
					hesabMoeenTemplateEntity.setName(entity.getName());
					hesabMoeenTemplateEntity.setDescription(entity.getDescription());
					updateRelatedHesbMoeens(hesabMoeenTemplateEntity);
				} else
					throw new FatalException(SerajMessageUtil.getMessage(
							"HesabMoeenTemplate_cantCreateHesabMoeenTemplateWithDuplicateNameAndnewCode",
							entity.getCode(), entity.getName()));
			} else {
				HesabKolEntity hesabKolEntity = hesabKolService.load(entity.getHesabKol().getID());
				hesabMoeenTemplateEntity = hesabMoeenTemplateService.createHesabMoeenTemplate(entity.getCode(),
						entity.getName(), hesabKolEntity.getCode(), organId, false, organName);
			}

		} else {
			HesabMoeenTemplateEntity hesabMoeenTemplateEntityByName = hesabMoeenTemplateService
					.loadByNameInCurrentOrgan(entity.getName(), organId);
			if (hesabMoeenTemplateEntityByName != null
					&& !hesabMoeenTemplateEntityByName.getId().equals(hesabMoeenTemplateEntity.getId()))
				throw new FatalException(SerajMessageUtil.getMessage(
						"HesabMoeenTemplate_cantCreateHesabMoeenTemplateWithDuplicateNameAndnewCode", entity.getCode(),
						entity.getName()));
			HesabKolTemplateEntity hesabKolTemplateEntity = hesabKolTemplateService
					.loadByCodeInCurrentOrgan(entity.getHesabKol().getCode(), organId);
			hesabMoeenTemplateEntity.setHesabKolTemplate(hesabKolTemplateEntity);
			hesabMoeenTemplateEntity.setCode(entity.getCode().toString());
			hesabMoeenTemplateEntity.setName(entity.getName());
			hesabMoeenTemplateEntity.setDescription(entity.getDescription());
		}

		hesabMoeenTemplateService.save(hesabMoeenTemplateEntity);
		entity.setHesabMoeenTemplate(hesabMoeenTemplateEntity);
		save(entity);
	}

	@Transactional
	private void updateRelatedHesbMoeens(HesabMoeenTemplateEntity hesabMoeenTemplateEntity) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("hesabMoeenTemplate.id@eq", hesabMoeenTemplateEntity.getId());
		List<HesabMoeenEntity> dataList = getDataList(null, filter);
		for (HesabMoeenEntity hesabMoeenEntity : dataList) {
			hesabMoeenEntity.setCode(hesabMoeenTemplateEntity.getCode());
			hesabMoeenEntity.setName(hesabMoeenTemplateEntity.getName());
			save(hesabMoeenEntity);
		}

	}

	private void checkHesabUniqueNess(HesabMoeenEntity entity, SaalMaaliEntity activeSaalMaaliEntity,
			List<Long> topOrganList) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
//		localFilter.put("organId@eq", activeSaalMaaliEntity.getOrgan().getId());

//		List<Long> topOrganList = getTopOrgansIdList(currentOrgan);
		localFilter.put("organId@in", topOrganList);

		localFilter.put("saalMaali.id@eq", activeSaalMaaliEntity.getId());
		checkUniqueNess(entity, HesabTafsiliEntity.PROP_NAME, entity.getName(), localFilter, false);
		checkUniqueNess(entity, HesabTafsiliEntity.PROP_CODE, entity.getCode(), localFilter, false);
	}

	@Transactional
	public void updateValues(HesabMoeenEntity entity) {
		save(entity);
	}

	@Transactional
	public void saveOrUpdateGlobal(HesabMoeenEntity entity) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("scope@eq", HesabScopeEnum.GLOBAL);
		checkUniqueNess(entity, HesabMoeenEntity.PROP_NAME, entity.getName(), localFilter, false);
		checkUniqueNess(entity, HesabMoeenEntity.PROP_CODE, entity.getCode(), localFilter, false);
		saveOrUpdate(entity);
	}

//	static Long maxKalaCode = null;
//	private synchronized String generateHesabCode(HesabMoeenEntity entity, OrganEntity currentOrgan, SaalMaaliEntity activeSaalMaaliEntity) {
//		String maxHierArchicalHesabMoeenCode = getMyDAO().getMaxHesabMoeenCode(entity.getHesabKol(), currentOrgan, activeSaalMaaliEntity);
//		return maxHierArchicalHesabMoeenCode;
//	}

	@Override
	public String getDifferences(HesabMoeenEntity entity) {
		String diffes = "";
		HesabMoeenEntity oldEntity = (HesabMoeenEntity) entity.getOldEntity();
		if (entity.getCode() != null && !entity.getCode().equals(oldEntity.getCode()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabMoeen" + "_" + HesabMoeenEntity.PROP_CODE) + " : "
					+ oldEntity.getCode() + "" + " --> " + entity.getCode() + "" + "]";

		if (entity.getName() != null && !entity.getName().equals(oldEntity.getName()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabMoeen" + "_" + HesabMoeenEntity.PROP_NAME) + " : "
					+ oldEntity.getName() + "" + " --> " + entity.getName() + "" + "]";

		if (entity.getDescription() != null && !entity.getDescription().equals(oldEntity.getDescription()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabMoeen" + "_" + HesabMoeenEntity.PROP_DESCRIPTION) + " : "
					+ oldEntity.getDescription() + "" + " --> " + entity.getDescription() + "" + "]";

		if (entity.getHidden() != null && !entity.getHidden().equals(oldEntity.getHidden()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabMoeen" + "_" + HesabMoeenEntity.PROP_HIDDEN) + " : "
					+ oldEntity.getHidden() + "" + " --> " + entity.getHidden() + "" + "]";

		return diffes;
	}

//	public HesabMoeenEntity getHesabMoeenByCode(String hesabCode) {
//
//		Map<String, Object> localFilter = new HashMap<String, Object>();
//		localFilter.put("code@eq", hesabCode);
//		localFilter.put("organId@eq", getCurrentOrgan().getId());
//		List<HesabMoeenEntity> dataList = getDataList(null, localFilter,
//				FlushMode.MANUAL);
//		if (dataList.size() == 1)
//			return dataList.get(0);
//		else if (dataList.size() == 0)
//			return null;
//		else
//			throw new IllegalStateException();
//	}

	public HesabMoeenEntity getHesabMoeenByCodeAndSaalMaali(String hesabCode, SaalMaaliEntity saalMaaliEntity) {

		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", hesabCode);
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		HesabMoeenEntity hesabMoeenEntity = load(null, localFilter);
		return hesabMoeenEntity;
	}

	@Override
	@Transactional
	public void saveOrUpdate(HesabMoeenEntity entity) {
		if (!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("HesabMoeen_code"));
		if (entity.getHidden() == null)
			entity.setHidden(false);
		super.saveOrUpdate(entity);
	}

	@Override
	@Transactional
	public void saveOrUpdateStateLess(HesabMoeenEntity entity) {
		if (!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("HesabMoeen_code"));
		if (entity.getHidden() == null)
			entity.setHidden(false);
		super.saveOrUpdateStateLess(entity);
	}

	public List<HesabMoeenEntity> getActiveMoeens(Long hesabKolId, SaalMaaliEntity currentSaalMaali,
			List<Long> topOrganList) {
		Map<String, Object> localFilter = new HashMap<String, Object>();

//		List<Long> topOrganList = getTopOrgansIdList(curentOrgan);
		localFilter.put("organId@in", topOrganList);

		localFilter.put("hidden@eq", Boolean.FALSE);
		localFilter.put("hesabKol.id@eq", hesabKolId);
		localFilter.put("saalMaali.id@eq", currentSaalMaali.getId());
		return getDataList(null, localFilter);
	}

	public List<HesabMoeenEntity> getActiveMoeens(SaalMaaliEntity currentSaalMaali, List<Long> topOrganList) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
//		List<Long> topOrganList = getTopOrgansIdList(curentOrgan);
		localFilter.put("organId@in", topOrganList);

		localFilter.put("hidden@eq", Boolean.FALSE);
		localFilter.put("saalMaali.id@eq", currentSaalMaali.getId());
		return getDataList(null, localFilter);
	}

	public List<HesabTafsiliEntity> getActiveTafsilies(HesabMoeenEntity hesabMoeenEntity,
			SaalMaaliEntity saalMaaliEntity, List<Long> topOrganList) {
		return getActiveTafsilies(hesabMoeenEntity, saalMaaliEntity, topOrganList, null);
	}

	public List<HesabTafsiliEntity> getActiveTafsilies(HesabMoeenEntity hesabMoeenEntity,
			SaalMaaliEntity saalMaaliEntity, List<Long> topOrganList, Integer level) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
//		List<Long> topOrganList = getTopOrgansIdList(curentOrgan);
		localFilter.put("hesabTafsili.organId@in", topOrganList);

		localFilter.put("hesabTafsili.level@eq", level);
		localFilter.put("hesabTafsili.hidden@eq", Boolean.FALSE);
		localFilter.put("hesabTafsili.saalMaali.id@eq", saalMaaliEntity.getId());

		localFilter.put("hesabMoeen.id@eq", hesabMoeenEntity.getId());
//		localFilter.put("hesabTafsili.isShenavar@eq", Boolean.FALSE);
		List<MoeenTafsiliEntity> dataList = moeenTafsiliService.getDataList(null, localFilter);

		List<HesabTafsiliEntity> hesabTafsiliEntities = new ArrayList<HesabTafsiliEntity>();
		for (MoeenTafsiliEntity moeenTafsiliEntity : dataList) {
			hesabTafsiliEntities.add(moeenTafsiliEntity.getHesabTafsili());
		}

		return hesabTafsiliEntities;
	}

	public List<HesabMoeenEntity> getActiveMoeens(SaalMaaliEntity currentSaalMaali) {
		Map<String, Object> localFilter = new HashMap<String, Object>();

		localFilter.put("hidden@eq", Boolean.FALSE);
		localFilter.put("saalMaali.id@eq", currentSaalMaali.getId());
		return getDataList(null, localFilter);
	}

	@Transactional(readOnly = false)
	public void importFromHesabMoeenTemplateList(SaalMaaliEntity activeSaalMaaliEntity, Long currentOrganId,
			List<Long> topOrganList, String currentOrganName) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organId@eq", activeSaalMaaliEntity.getOrganId());
		List<HesabMoeenTemplateEntity> dataList = hesabMoeenTemplateService.getDataList(null, localFilter);
		for (HesabMoeenTemplateEntity hesabMoeenTemplateEntity : dataList) {
			HesabMoeenEntity hesabMoeen = loadHesabMoeenByTemplate(hesabMoeenTemplateEntity, activeSaalMaaliEntity);
			if (hesabMoeen == null) {
				try {
					createHesabMoeen(activeSaalMaaliEntity, hesabMoeenTemplateEntity, currentOrganId, topOrganList,
							currentOrganName);
				} catch (DuplicateException e) {
					System.out.println(e.getMessage());
					// e.printStackTrace();
				}
			} else {
				if (hesabMoeen.getHesabKol() == null) {
					if (hesabMoeenTemplateEntity.getHesabKolTemplate() != null) {
						HesabKolEntity hesabKolEntity = hesabKolService.loadHesabKolByTemplate(
								hesabMoeenTemplateEntity.getHesabKolTemplate(), activeSaalMaaliEntity);
						hesabMoeen.setHesabKol(hesabKolEntity);
					}
				}
			}
		}

	}

	@Transactional
	public HesabMoeenEntity createHesabMoeen(SaalMaaliEntity activeSaalMaaliEntity,
			HesabMoeenTemplateEntity hesabMoeenTemplateEntity, Long currentOrganId, List<Long> topOrganList,
			String currentOrganName) {
		HesabMoeenEntity hesabMoeenEntity = loadHesabMoeenByTemplate(hesabMoeenTemplateEntity, activeSaalMaaliEntity);
		if (hesabMoeenEntity == null) {
			hesabMoeenEntity = populateHesabMoeen(activeSaalMaaliEntity, hesabMoeenTemplateEntity, currentOrganId,
					topOrganList, currentOrganName);

			save(hesabMoeenEntity, activeSaalMaaliEntity, currentOrganId, topOrganList, currentOrganName);
		}
		return hesabMoeenEntity;
	}

	@Transactional
	public HesabMoeenEntity createHesabMoeen(SaalMaaliEntity destSaalMaali, HesabMoeenEntity srcHesabMoeenEntity,
			Long organId, List<Long> topOrganList, String organName) {
		HesabMoeenEntity hesabMoeenEntity = loadHesabMoeenByCode(srcHesabMoeenEntity.getCode(), destSaalMaali.getId(),
				FlushMode.MANUAL);
		if (hesabMoeenEntity == null)
			hesabMoeenEntity = loadHesabMoeenByName(srcHesabMoeenEntity.getName(), destSaalMaali, FlushMode.MANUAL);
		if (hesabMoeenEntity == null) {
			hesabMoeenEntity = populateHesabMoeen(destSaalMaali, srcHesabMoeenEntity, organId, topOrganList);
			save(hesabMoeenEntity, destSaalMaali, organId, topOrganList, organName);
		}
		return hesabMoeenEntity;
	}

//	public HesabMoeenEntity createHesabMoeenStateLess(SaalMaaliEntity activeSaalMaaliEntity,
//			HesabMoeenTemplateEntity hesabMoeenTemplateEntity) {
//		HesabMoeenEntity hesabMoeenEntity = loadHesabMoeenByTemplate(hesabMoeenTemplateEntity, activeSaalMaaliEntity);
//		if(hesabMoeenEntity == null){
//			hesabMoeenEntity = populateHesabKol(activeSaalMaaliEntity,
//					hesabMoeenTemplateEntity);
//			
//			saveStateLess(hesabMoeenEntity, activeSaalMaaliEntity);
//		}
//		return hesabMoeenEntity;
//	}

	private HesabMoeenEntity populateHesabMoeen(SaalMaaliEntity destSaalMaaliEntity,
			HesabMoeenTemplateEntity hesabMoeenTemplateEntity, Long organId, List<Long> topOrganList,
			String organName) {
		HesabMoeenEntity hesabMoeenEntity;
		hesabMoeenEntity = new HesabMoeenEntity();
		hesabMoeenEntity.setCode(hesabMoeenTemplateEntity.getCode());
		hesabMoeenEntity.setDescription(hesabMoeenTemplateEntity.getDescription());
		HesabKolEntity hesabKolEntity = null;

		HesabKolTemplateEntity hesabKolTemplate = hesabMoeenTemplateEntity.getHesabKolTemplate();
		if (hesabKolTemplate != null)
			hesabKolEntity = hesabKolService.loadHesabKolByCode(hesabKolTemplate.getCode(),
					destSaalMaaliEntity.getId());

		if (hesabKolEntity == null && hesabKolTemplate != null)
			hesabKolEntity = hesabKolService.createHesabKol(destSaalMaaliEntity, hesabKolTemplate, organId,
					topOrganList, organName);
		hesabMoeenEntity.setHesabKol(hesabKolEntity);
		hesabMoeenEntity.setHesabMoeenTemplate(hesabMoeenTemplateEntity);
		hesabMoeenEntity.setHidden(hesabMoeenTemplateEntity.getHidden());
		hesabMoeenEntity.setName(hesabMoeenTemplateEntity.getName());
		hesabMoeenEntity.setOrganId(destSaalMaaliEntity.getOrganId());
		hesabMoeenEntity.setSaalMaali(destSaalMaaliEntity);
		hesabMoeenEntity.setScope(hesabMoeenTemplateEntity.getScope());

		hesabMoeenEntity.setBedehkar(0d);
		hesabMoeenEntity.setBestankr(0d);
		hesabMoeenEntity.setMoeenTafsili(new HashSet<MoeenTafsiliEntity>());
		return hesabMoeenEntity;
	}

	private HesabMoeenEntity populateHesabMoeen(SaalMaaliEntity destSaalMaaliEntity,
			HesabMoeenEntity srcHesabMoeenEntity, Long organId, List<Long> topOrganList) {
		HesabMoeenEntity hesabMoeenEntity;
		hesabMoeenEntity = new HesabMoeenEntity();
		hesabMoeenEntity.setCode(srcHesabMoeenEntity.getCode());
		hesabMoeenEntity.setDescription(srcHesabMoeenEntity.getDescription());
		HesabKolEntity hesabKolEntity = hesabKolService.loadHesabKolByCode(srcHesabMoeenEntity.getHesabKol().getCode(),
				destSaalMaaliEntity.getId());
		if (hesabKolEntity == null)
			hesabKolEntity = hesabKolService.createHesabKolStateLess(destSaalMaaliEntity,
					srcHesabMoeenEntity.getHesabKol(), topOrganList);
		hesabMoeenEntity.setHesabKol(hesabKolEntity);
		hesabMoeenEntity.setHesabMoeenTemplate(srcHesabMoeenEntity.getHesabMoeenTemplate());
		hesabMoeenEntity.setHidden(srcHesabMoeenEntity.getHidden());
		hesabMoeenEntity.setName(srcHesabMoeenEntity.getName());
		hesabMoeenEntity.setOrganId(organId);
		hesabMoeenEntity.setSaalMaali(destSaalMaaliEntity);
		hesabMoeenEntity.setScope(srcHesabMoeenEntity.getScope());

		hesabMoeenEntity.setBedehkar(0d);
		hesabMoeenEntity.setBestankr(0d);
		hesabMoeenEntity.setMoeenTafsili(new HashSet<MoeenTafsiliEntity>());
		return hesabMoeenEntity;
	}

	public HesabMoeenEntity loadHesabMoeenByTemplate(HesabMoeenTemplateEntity moeenTemplateEntity,
			SaalMaaliEntity activeSaalMaaliEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("hesabMoeenTemplate.id@eq", moeenTemplateEntity.getId());
		localFilter.put("organId@eq", activeSaalMaaliEntity.getOrganId());
		localFilter.put("saalMaali.id@eq", activeSaalMaaliEntity.getId());
		HesabMoeenEntity hesabMoeenEntity = load(null, localFilter);
		return hesabMoeenEntity;
	}

	public HesabMoeenEntity loadHesabMoeenByTemplateId(Long moeenTemplateId, Long saalMaaliId) {
		HesabMoeenTemplateEntity hesabMoeenTemplateEntity = hesabMoeenTemplateService.load(moeenTemplateId);
		return loadHesabMoeenByCode(hesabMoeenTemplateEntity.getCode(), saalMaaliId, FlushMode.MANUAL);
	}

	public HesabMoeenEntity loadHesabMoeenByCode(String code, Long saalMaaliId) {
		return loadHesabMoeenByCode(code, saalMaaliId, FlushMode.MANUAL);
	}

	public HesabMoeenEntity loadHesabMoeenByCode(String code, Long saalMaaliId, FlushMode flushModel) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
//		localFilter.put("organId@eq",saalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq", saalMaaliId);
		HesabMoeenEntity hesabMoeenEntity = load(null, localFilter, flushModel);
		return hesabMoeenEntity;
	}

	public HesabMoeenEntity loadHesabMoeenByCode(String code, SaalMaaliEntity saalMaaliEntity, FlushMode flushModel,
			Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		localFilter.put("organId@eq", organId);
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		HesabMoeenEntity hesabMoeenEntity = load(null, localFilter, flushModel);
		return hesabMoeenEntity;
	}

	public HesabMoeenEntity loadHesabMoeenByName(String name, SaalMaaliEntity saalMaaliEntity, FlushMode flushModel) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq", name);
//		localFilter.put("organId@eq",saalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		HesabMoeenEntity hesabMoeenEntity = load(null, localFilter, flushModel);
		return hesabMoeenEntity;
	}

	public HesabMoeenEntity loadHesabMoeenByName(String name, SaalMaaliEntity saalMaaliEntity, FlushMode flushModel,
			Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq", name);
		localFilter.put("organId@eq", organId);
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		HesabMoeenEntity hesabMoeenEntity = load(null, localFilter, flushModel);
		return hesabMoeenEntity;
	}

	@Transactional(readOnly = false)
	public void disableHesab(Long hesabId) {
		HesabMoeenEntity hesabMoeenEntity = load(hesabId);
		hesabMoeenEntity.setHidden(true);
		save(hesabMoeenEntity);
	}

	@Override
	public void save(HesabMoeenEntity entity) {
		if (!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("HesabMoeen_code"));

		super.save(entity);
	}

	@Transactional(readOnly = true)
	public List<HesabMoeenEntity> getRootHesabs(SaalMaaliEntity saalMaaliEntity) {

		List<HesabMoeenEntity> rootList = new ArrayList<HesabMoeenEntity>();
		Set<HesabMoeenEntity> removeList = new HashSet<HesabMoeenEntity>();

		List<HesabMoeenEntity> hesabMoeenList = getActiveMoeens(saalMaaliEntity);

		Map<String, Object> moeenTafsiliFilter = new HashMap<String, Object>();
		moeenTafsiliFilter.put("hesabMoeen.saalMaali.id@eq", saalMaaliEntity.getId());
		List<MoeenTafsiliEntity> moeenTafsiliList = moeenTafsiliService.getDataList(null, moeenTafsiliFilter);

		for (MoeenTafsiliEntity moeenTafsiliEntity : moeenTafsiliList) {
			HesabMoeenEntity hesabMoeenEntity = moeenTafsiliEntity.getHesabMoeen();
			if (hesabMoeenList.contains(hesabMoeenEntity))
				removeList.add(hesabMoeenEntity);
		}

		rootList.addAll(hesabMoeenList);
		rootList.removeAll(removeList);
		return rootList;
	}

}