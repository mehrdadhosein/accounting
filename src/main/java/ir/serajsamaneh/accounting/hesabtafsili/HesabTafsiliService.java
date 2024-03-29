package ir.serajsamaneh.accounting.hesabtafsili;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity;
import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazService;
import ir.serajsamaneh.accounting.enumeration.SanadStateEnum;
import ir.serajsamaneh.accounting.exception.CycleInHesabTafsiliException;
import ir.serajsamaneh.accounting.hesabclassification.HesabClassificationService;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateService;
import ir.serajsamaneh.accounting.moeentafsili.MoeenTafsiliEntity;
import ir.serajsamaneh.accounting.moeentafsili.MoeenTafsiliService;
import ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemService;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.exception.DuplicateException;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.exception.FieldMustContainOnlyNumbersException;
import ir.serajsamaneh.core.exception.RequiredFieldNotSetException;
import ir.serajsamaneh.core.util.SerajMessageUtil;
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class HesabTafsiliService extends BaseEntityService<HesabTafsiliEntity, Long> {

	@Override
	protected HesabTafsiliDAO getMyDAO() {
		return hesabTafsiliDAO;
	}

	@Autowired
	HesabTafsiliDAO hesabTafsiliDAO;
	@Autowired
	HesabMoeenService hesabMoeenService;
	@Autowired
	SaalMaaliService saalMaaliService;
	@Autowired
	HesabTafsiliTemplateService hesabTafsiliTemplateService;
	@Autowired
	HesabMoeenTemplateService hesabMoeenTemplateService;
	@Autowired
	MoeenTafsiliService moeenTafsiliService;
	@Autowired
	AccountingMarkazService accountingMarkazService;
	@Autowired
	HesabClassificationService hesabClassificationService;
	@Autowired
	SanadHesabdariItemService sanadHesabdariItemService;

	@Override
	public String getDifferences(HesabTafsiliEntity entity) {
		String diffes = "";
		HesabTafsiliEntity oldEntity = (HesabTafsiliEntity) entity.getOldEntity();
		if (entity.getCode() != null && !entity.getCode().equals(oldEntity.getCode()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabTafsili" + "_" + HesabTafsiliEntity.PROP_CODE) + " : "
					+ oldEntity.getCode() + "" + " --> " + entity.getCode() + "" + "]";

		if (entity.getName() != null && !entity.getName().equals(oldEntity.getName()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabTafsili" + "_" + HesabTafsiliEntity.PROP_NAME) + " : "
					+ oldEntity.getName() + "" + " --> " + entity.getName() + "" + "]";

		if (entity.getTafsilType() != null && !entity.getTafsilType().equals(oldEntity.getTafsilType()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabTafsili" + "_" + HesabTafsiliEntity.PROP_TAFSIL_TYPE)
					+ " : " + oldEntity.getTafsilType() + "" + " --> " + entity.getTafsilType() + "" + "]";

		if (entity.getDescription() != null && !entity.getDescription().equals(oldEntity.getDescription()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabTafsili" + "_" + HesabTafsiliEntity.PROP_DESCRIPTION)
					+ " : " + oldEntity.getDescription() + "" + " --> " + entity.getDescription() + "" + "]";

		if (entity.getHidden() != null && !entity.getHidden().equals(oldEntity.getHidden()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabTafsili" + "_" + HesabTafsiliEntity.PROP_HIDDEN) + " : "
					+ oldEntity.getHidden() + "" + " --> " + entity.getHidden() + "" + "]";

		return diffes;
	}

	private void checkHesabUniqueNess(HesabTafsiliEntity entity, SaalMaaliEntity activeSaalMaaliEntity,
			List<Long> topOrganList) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
//		localFilter.put("organId@eq", activeSaalMaaliEntity.getOrgan().getId());
//		List<Long> topOrganList = getTopOrgansIdList(currentOrgan);
		localFilter.put("organId@in", topOrganList);

		localFilter.put("saalMaali.id@eq", activeSaalMaaliEntity.getId());
		checkUniqueNess(entity, HesabTafsiliEntity.PROP_NAME, entity.getName(), localFilter, false);
		checkUniqueNess(entity, HesabTafsiliEntity.PROP_CODE, entity.getCode(), localFilter, false);
	}

	@Override
	public void saveOrUpdate(HesabTafsiliEntity entity) {
		if (!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("HesabMoeen_code"));
		if (entity.getHidden() == null)
			entity.setHidden(false);
		super.saveOrUpdate(entity);
	}

	/*
	 * private synchronized Long generateHesabTafsiliCode(HesabTafsiliEntity entity,
	 * OrganEntity currentOrgan, SaalMaaliEntity currentUserSaalMaaliEntity) { Long
	 * maxHesabTafsiliCode = getMyDAO().getMaxHesabTafsiliCode(currentOrgan); //
	 * return Long.valueOf(++maxHesabTafsiliCode).toString(); return
	 * ++maxHesabTafsiliCode; }
	 */

	@Transactional
	public void save(HesabTafsiliEntity entity, List<Long> moeenIds, List<Long> childTafsiliIds,
			List<Long> parentTafsiliIds, List<Long> childAccountingMarkazIds, SaalMaaliEntity activeSaalMaaliEntity,
			Long currentOrganId, List<Long> topOrganList, String topOrganCode, String currentOrganName) {
		commonSave(entity, moeenIds, childTafsiliIds, parentTafsiliIds, childAccountingMarkazIds, activeSaalMaaliEntity,
				currentOrganId, topOrganList, topOrganCode, currentOrganName);
		save(entity);
	}

	@Transactional
	public void save(HesabTafsiliEntity entity, SaalMaaliEntity activeSaalMaaliEntity, Long currentOrganId,
			List<Long> topOrganList, String topOrganCode, String currentOrganName) {
		commonSave(entity, new ArrayList<Long>(), new ArrayList<Long>(), new ArrayList<Long>(), new ArrayList<Long>(),
				activeSaalMaaliEntity, currentOrganId, topOrganList, topOrganCode, currentOrganName);
		save(entity);
	}

	public void saveStateLess(HesabTafsiliEntity entity, SaalMaaliEntity activeSaalMaaliEntity, Long currentOrganId,
			List<Long> topOrganList, String topOrganCode, String currentOrganName) {
		commonSave(entity, new ArrayList<Long>(), new ArrayList<Long>(), new ArrayList<Long>(), new ArrayList<Long>(),
				activeSaalMaaliEntity, currentOrganId, topOrganList, topOrganCode, currentOrganName);
		super.saveStateLess(entity);
	}

	@Override
	public void saveStateLess(HesabTafsiliEntity entity) {
		if (!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("HesabMoeen_code"));

		super.saveStateLess(entity);
	}

	private void checkCycleInChildTafsiliHierarchy(HesabTafsiliEntity entity, List<Long> childTafsiliIds) {
		for (Long tafsiliId : childTafsiliIds) {
			HesabTafsiliEntity mainChildEntity = load(tafsiliId);

			try {
				checkCycleInChildTafsiliHierarchy(entity, mainChildEntity);
			} catch (FatalException e) {
				throw new CycleInHesabTafsiliException(entity, mainChildEntity);
			}
		}

	}

	private void checkCycleInChildTafsiliHierarchy(HesabTafsiliEntity mainEntity, HesabTafsiliEntity mainChildEntity) {
		if (mainEntity.getId() != null && mainEntity.getId().equals(mainChildEntity.getId()))
			throw new FatalException();
		Set<HesabTafsiliEntity> childs = mainChildEntity.getChilds();
		for (HesabTafsiliEntity childTfsili : childs) {
			checkCycleInChildTafsiliHierarchy(mainEntity, childTfsili);
		}
	}

	private void checkCycleInParentTafsiliHierarchy(HesabTafsiliEntity entity, List<Long> parentTafsiliIds) {
		for (Long tafsiliId : parentTafsiliIds) {
			HesabTafsiliEntity mainParentEntity = load(tafsiliId);

			try {
				checkCycleInParentTafsiliHierarchy(entity, mainParentEntity);
			} catch (FatalException e) {
				throw new CycleInHesabTafsiliException(entity, mainParentEntity);
			}
		}

	}

	private void checkCycleInParentTafsiliHierarchy(HesabTafsiliEntity mainEntity,
			HesabTafsiliEntity mainParentEntity) {
		if (mainEntity.getId() != null && mainEntity.getId().equals(mainParentEntity.getId()))
			throw new FatalException();
		Set<HesabTafsiliEntity> parents = mainParentEntity.getParents();
		for (HesabTafsiliEntity parentTfsili : parents) {
			checkCycleInParentTafsiliHierarchy(mainEntity, parentTfsili);
		}
	}

	private Long generateHesabTafsiliCode(HesabTafsiliEntity entity, Long organId,
			SaalMaaliEntity activeSaalMaaliEntity) {
		Long maxHesabTafsiliCode = null;
		if (systemConfigService.getValue(organId, null, "HesabTafsiliCodingType").equals("SERIAL")) {
			maxHesabTafsiliCode = getMyDAO().getMaxHesabTafsiliCode(null, 0, organId, activeSaalMaaliEntity);
			return maxHesabTafsiliCode;
		} else if (systemConfigService.getValue(organId, null, "HesabTafsiliCodingType")
				.equals("VARIABLE_HIERARCHICAL")) {
			Long maxHierArchicalHesabTafsiliCode = getMyDAO().getMaxHesabTafsiliCode(entity.getHesabClassification(), 0,
					organId, activeSaalMaaliEntity);
			return maxHierArchicalHesabTafsiliCode;
		} else if (systemConfigService.getValue(organId, null, "HesabTafsiliCodingType")
				.equals("CONSTANT_HIERARCHICAL")) {
			Integer HesabTafsiliCodeCharactersNumber = Integer
					.parseInt(systemConfigService.getValue(organId, null, "hesabTafsiliCodeCharactersNumber"));
			Long maxHierArchicalHesabTafsiliCode = getMyDAO().getMaxHesabTafsiliCode(entity.getHesabClassification(),
					HesabTafsiliCodeCharactersNumber, organId, activeSaalMaaliEntity);

			return maxHierArchicalHesabTafsiliCode;
		}
		throw new IllegalStateException("HesabTafsiliCodingType is required");

	}

	@Transactional
	private void commonSave(HesabTafsiliEntity entity, List<Long> moeenIds, List<Long> childTafsiliIds,
			List<Long> parentTafsiliIds, List<Long> childAccountingMarkazIds, SaalMaaliEntity activeSaalMaaliEntity,
			Long currentOrganId, List<Long> topOrganList, String topOrganCode, String currentOrganName) {

		if (entity.getId() != null && entity.getSaalMaali() != null && entity.getSaalMaali().getId() != null
				&& !entity.getSaalMaali().equals(activeSaalMaaliEntity))
			throw new FatalException(SerajMessageUtil.getMessage("SaalMaali_hesabConflict"));
		if (entity.getId() == null) {
			entity.setBedehkar(0d);
			entity.setBestankr(0d);
		}

		String hesabTafsiliCodingType = systemConfigService.getValue(currentOrganId, null, "HesabTafsiliCodingType");
		if (entity.getCode() == null && hesabTafsiliCodingType != null && !hesabTafsiliCodingType.equals("MANUAL")) {
			entity.setCode(generateHesabTafsiliCode(entity, currentOrganId, activeSaalMaaliEntity));
		}

		if (entity.getCode() == null) {
			throw new RequiredFieldNotSetException(SerajMessageUtil.getMessage("HesabTafsili_code"));
		}

		if (entity.getMoeenTafsili() == null) {
			entity.setMoeenTafsili(new HashSet<MoeenTafsiliEntity>());
		}
		if (entity.getSaalMaali() == null || entity.getSaalMaali().getId() == null)
			entity.setSaalMaali(activeSaalMaaliEntity);

		if (entity.getChilds() == null)
			entity.setChilds(new HashSet<HesabTafsiliEntity>());

		if (entity.getChildAccountingMarkaz() == null)
			entity.setChildAccountingMarkaz(new HashSet<AccountingMarkazEntity>());

		validateMoeenTafsili(entity, moeenIds);
		entity.getMoeenTafsili().clear();
		for (Long moeenId : moeenIds)
			addMoeenToMoeenTafsiliSet(entity, moeenId);

		validateTafsiliChild(entity, childTafsiliIds);
		entity.getChilds().clear();
		for (Long tafsiliId : childTafsiliIds) {
			entity.getChilds().add(load(tafsiliId));
		}

		validateTafsiliParent(entity, parentTafsiliIds);
		if (entity.getParents() != null)
			entity.getParents().clear();

		for (Long tafsiliId : parentTafsiliIds) {
			HesabTafsiliEntity hesabTafsiliEntity = load(tafsiliId);
			entity.addToparents(hesabTafsiliEntity);
		}

		entity.getChildAccountingMarkaz().clear();
		for (Long accountingMarkazId : childAccountingMarkazIds) {
			entity.getChildAccountingMarkaz().add(accountingMarkazService.load(accountingMarkazId));
		}

		if (entity.getHesabClassification() != null && entity.getHesabClassification().getId() != null) {
			entity.setHesabClassification(hesabClassificationService.load(entity.getHesabClassification().getId()));
			Set<HesabMoeenTemplateEntity> hesabMoeenTemplateSet = entity.getHesabClassification()
					.getHesabMoeenTemplate();
			if (hesabMoeenTemplateSet != null)
				for (HesabMoeenTemplateEntity hesabMoeenTemplateEntity : hesabMoeenTemplateSet) {
					HesabMoeenEntity hesabMoeenEntity = hesabMoeenService
							.loadHesabMoeenByCode(hesabMoeenTemplateEntity.getCode(), activeSaalMaaliEntity.getId());
					if (hesabMoeenEntity != null)// maybe there no hesabMoeen for this template in activeSaalMaaliEntity
						addMoeenToMoeenTafsiliSet(entity, hesabMoeenEntity.getId());
				}

			Set<HesabTafsiliTemplateEntity> hesabTafsiliTemplateSet = entity.getHesabClassification()
					.getHesabTafsiliTemplate();
			if (hesabTafsiliTemplateSet != null)
				for (HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity : hesabTafsiliTemplateSet) {
					HesabTafsiliEntity hesabTafsiliByCode = loadHesabTafsiliByCode(
							Long.valueOf(hesabTafsiliTemplateEntity.getCode()), activeSaalMaaliEntity.getId());

					if (hesabTafsiliByCode != null) {// maybe there no hesabTafsili for this template in
														// activeSaalMaaliEntity
						entity.addToparents(hesabTafsiliByCode);
//						hesabTafsiliByCode.addTochilds(entity);
						save(hesabTafsiliByCode);
					}
				}
		}

//		if (!StringUtils.hasText(entity.getCode())) {
//		if (entity.getCode()==null) {
//			
//			entity.setCode(generateHesabTafsiliCode(entity,currentOrgan, activeSaalMaaliEntity));
//		}
		checkHesabUniqueNess(entity, activeSaalMaaliEntity, topOrganList);

		checkCycleInChildTafsiliHierarchy(entity, childTafsiliIds);
		checkCycleInParentTafsiliHierarchy(entity, parentTafsiliIds);

		createOrUpdateRelatedHesabTafsiliTemplate(entity, currentOrganId, topOrganList, currentOrganName);
	}

	private void validateTafsiliChild(HesabTafsiliEntity entity, List<Long> childTafsiliIds) {
		Set<HesabTafsiliEntity> removedList = new HashSet<>();
		Set<HesabTafsiliEntity> childs = entity.getChilds();

		if (childs == null)
			return;

		for (HesabTafsiliEntity hesabTafsiliEntity : childs) {
			if (!childTafsiliIds.contains(hesabTafsiliEntity.getId()))
				removedList.add(hesabTafsiliEntity);
		}

		for (HesabTafsiliEntity hesabTafsiliEntity : removedList) {
			Map<String, Object> filter = new HashMap<>();
			filter.put("sanadHesabdari.state@neq", SanadStateEnum.EBTAL);
			filter.put("sanadHesabdari.saalMaali.id@eq", entity.getSaalMaali().getId());
			filter.put("hesabTafsili.id@eq", entity.getId());
			filter.put("hesabTafsiliTwo.id@eq", hesabTafsiliEntity.getId());

			Integer rowCount = sanadHesabdariItemService.getRowCount(filter);
			if (rowCount > 0)
				throw new FatalException(
						SerajMessageUtil.getMessage("HesabTafsili_childIsUsedInSomeArticles", hesabTafsiliEntity));
		}
	}

	private void validateTafsiliParent(HesabTafsiliEntity entity, List<Long> parentTafsiliIds) {
		Set<HesabTafsiliEntity> removedList = new HashSet<>();
		Set<HesabTafsiliEntity> parents = entity.getParents();
		if (parents == null)
			return;
		for (HesabTafsiliEntity hesabTafsiliEntity : parents) {
			if (!parentTafsiliIds.contains(hesabTafsiliEntity.getId()))
				removedList.add(hesabTafsiliEntity);
		}

		for (HesabTafsiliEntity hesabTafsiliEntity : removedList) {
			Map<String, Object> filter = new HashMap<>();
			filter.put("sanadHesabdari.state@neq", SanadStateEnum.EBTAL);
			filter.put("sanadHesabdari.saalMaali.id@eq", entity.getSaalMaali().getId());
			filter.put("hesabTafsili.id@eq", hesabTafsiliEntity.getId());
			filter.put("articleTafsili.hesabTafsili.id@eq", entity.getId());
			filter.put("articleTafsili.level@eq", 1);
			Integer rowCount = sanadHesabdariItemService.getRowCount(filter);
			if (rowCount > 0)
				throw new FatalException(
						SerajMessageUtil.getMessage("HesabTafsili_parentIsUsedInSomeArticles", hesabTafsiliEntity));
		}
	}

	private void validateMoeenTafsili(HesabTafsiliEntity entity, List<Long> moeenIds) {
		Set<HesabMoeenEntity> removedMoeenList = new HashSet<>();
		List<HesabMoeenEntity> hesabMoeenList = entity.getHesabMoeenList();
		for (HesabMoeenEntity hesabMoeenEntity : hesabMoeenList) {
			if (!moeenIds.contains(hesabMoeenEntity.getId()))
				removedMoeenList.add(hesabMoeenEntity);
		}

		for (HesabMoeenEntity hesabMoeenEntity : removedMoeenList) {
			Map<String, Object> filter = new HashMap<>();
			filter.put("sanadHesabdari.saalMaali.id@eq", entity.getSaalMaali().getId());
			filter.put("hesabMoeen.id@eq", hesabMoeenEntity.getId());
			filter.put("hesabTafsili.id@eq", entity.getId());
			Integer rowCount = sanadHesabdariItemService.getRowCount(filter);
			if (rowCount > 0)
				throw new FatalException(
						SerajMessageUtil.getMessage("HesabTafsili_moeenIsUsedInSomeArticles", hesabMoeenEntity));
		}

	}

	@Transactional
	public void createOrUpdateRelatedHesabTafsiliTemplate(HesabTafsiliEntity entity, Long organId,
			List<Long> topOrganList, String organName) {
		HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity = hesabTafsiliTemplateService
				.loadByCodeInCurrentOrgan(entity.getCode(), organId);
		if (hesabTafsiliTemplateEntity == null) {
			hesabTafsiliTemplateEntity = hesabTafsiliTemplateService.loadByNameInCurrentOrgan(entity.getName(),
					organId);
			if (hesabTafsiliTemplateEntity != null) {
				HesabTafsiliTemplateEntity currentEntityHesabTafsiliTemplate = entity.getHesabTafsiliTemplate();

				if (currentEntityHesabTafsiliTemplate == null)
					entity.setHesabTafsiliTemplate(hesabTafsiliTemplateEntity);
				// code of hesabTafsili has changed
				if (currentEntityHesabTafsiliTemplate == null
						|| currentEntityHesabTafsiliTemplate.equals(hesabTafsiliTemplateEntity)) {
					hesabTafsiliTemplateEntity.setCode(entity.getCode());
					hesabTafsiliTemplateEntity.setName(entity.getName());
					hesabTafsiliTemplateEntity.setTafsilType(entity.getTafsilType());
					hesabTafsiliTemplateEntity.setDescription(entity.getDescription());
					hesabTafsiliTemplateEntity.setLevel(entity.getLevel());
					updateRelatedHesbTafsilies(hesabTafsiliTemplateEntity);
				} else
					throw new FatalException(SerajMessageUtil.getMessage(
							"HesabTafsiliTemplate_cantCreateHesabTafsiliTemplateWithDuplicateNameAndnewCode",
							entity.getCode(), entity.getName()));
			} else
				hesabTafsiliTemplateEntity = hesabTafsiliTemplateService.createHesabTafsiliTemplate(entity.getCode(),
						entity.getName(), organId, entity.getTafsilType(), entity.getDescription(), entity.getLevel(),
						organName);
		} else {

			HesabTafsiliTemplateEntity hesabTafsiliTemplateEntityByName = hesabTafsiliTemplateService
					.loadByNameInCurrentOrgan(entity.getName(), organId);
			if (hesabTafsiliTemplateEntityByName != null
					&& !hesabTafsiliTemplateEntityByName.getId().equals(hesabTafsiliTemplateEntity.getId()))
				throw new FatalException(SerajMessageUtil.getMessage(
						"HesabTafsiliTemplate_cantCreateHesabTafsiliTemplateWithDuplicateNameAndnewCode",
						entity.getCode(), hesabTafsiliTemplateEntityByName.getName() + "("
								+ hesabTafsiliTemplateEntityByName.getCode() + ")"));

			hesabTafsiliTemplateEntity.setCode(entity.getCode());
			hesabTafsiliTemplateEntity.setName(entity.getName());
			hesabTafsiliTemplateEntity.setTafsilType(entity.getTafsilType());
			hesabTafsiliTemplateEntity.setDescription(entity.getDescription());
			hesabTafsiliTemplateEntity.setLevel(entity.getLevel());
		}

		if (hesabTafsiliTemplateEntity.getMoeenTafsiliTemplate() == null)
			hesabTafsiliTemplateEntity.setMoeenTafsiliTemplate(new HashSet<MoeenTafsiliTemplateEntity>());
		else
			hesabTafsiliTemplateEntity.getMoeenTafsiliTemplate().clear();

		Set<MoeenTafsiliEntity> moeenTafsili = entity.getMoeenTafsili();
		for (MoeenTafsiliEntity moeenTafsiliEntity : moeenTafsili) {
			MoeenTafsiliTemplateEntity moeenTafsiliTemplateEntity = new MoeenTafsiliTemplateEntity();
			moeenTafsiliTemplateEntity.setHesabMoeenTemplate(hesabMoeenTemplateService.loadByCodeInCurrentOrgan(
					moeenTafsiliEntity.getHesabMoeen().getCode(), moeenTafsiliEntity.getHesabMoeen().getOrganId()));
			moeenTafsiliTemplateEntity.setHesabTafsiliTemplate(hesabTafsiliTemplateEntity);
			moeenTafsiliTemplateEntity.setLevel(moeenTafsiliEntity.getLevel());
			if (moeenTafsiliTemplateEntity.getHesabMoeenTemplate() == null)
				throw new FatalException();
			hesabTafsiliTemplateEntity.getMoeenTafsiliTemplate().add(moeenTafsiliTemplateEntity);
		}

		if (hesabTafsiliTemplateEntity.getChilds() == null)
			hesabTafsiliTemplateEntity.setChilds(new HashSet<HesabTafsiliTemplateEntity>());
		else
			hesabTafsiliTemplateEntity.getChilds().clear();

		Set<HesabTafsiliEntity> childs = entity.getChilds();
		for (HesabTafsiliEntity hesabTafsiliEntity : childs) {
			HesabTafsiliTemplateEntity templateByCode = hesabTafsiliTemplateService
					.loadByCode(hesabTafsiliEntity.getCode(), topOrganList);
			if (templateByCode != null)
				hesabTafsiliTemplateEntity.getChilds().add(templateByCode);
		}
		try {
			hesabTafsiliTemplateService.save(hesabTafsiliTemplateEntity);
			entity.setHesabTafsiliTemplate(hesabTafsiliTemplateEntity);
			save(entity);
		} catch (DuplicateException e) {
			throw new FatalException(
					SerajMessageUtil.getMessage("HesabTafsili_hesabTafsiliTemplateWithSameNameAndDuplicateCodeExists",
							entity.getName() + " (" + entity.getCode() + ")"));
		}
	}

	@Transactional
	private void updateRelatedHesbTafsilies(HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("hesabTafsiliTemplate.id@eq", hesabTafsiliTemplateEntity.getId());
		List<HesabTafsiliEntity> dataList = getDataList(filter);
		for (HesabTafsiliEntity hesabTafsiliEntity : dataList) {
			hesabTafsiliEntity.setCode(Long.valueOf(hesabTafsiliTemplateEntity.getCode()));
			hesabTafsiliEntity.setName(hesabTafsiliTemplateEntity.getName());
			save(hesabTafsiliEntity);
		}

	}

	private void addMoeenToMoeenTafsiliSet(HesabTafsiliEntity entity, Long moeenId) {
		for (MoeenTafsiliEntity moeenTafsiliEntity : entity.getMoeenTafsili()) {
			if (moeenTafsiliEntity.getHesabMoeen().getId().equals(moeenId))
				return;
		}
		MoeenTafsiliEntity moeenTafsiliEntity = new MoeenTafsiliEntity();
		moeenTafsiliEntity.setHesabMoeen(hesabMoeenService.load(moeenId));
		moeenTafsiliEntity.setHesabTafsili(entity);
		moeenTafsiliEntity.setLevel(1);
		entity.getMoeenTafsili().add(moeenTafsiliEntity);
	}

	@Transactional
	public void updateValues(HesabTafsiliEntity entity) {
		save(entity);
	}

//	public HesabTafsiliEntity getHesabTafsiliByCode(String hesabCode, OrganEntity currentOrganEntity){
//		Map<String, Object> localFilter = new HashMap<String, Object>();
//		localFilter.put("code@eq", hesabCode);
//		localFilter.put("organId@eq", currentOrganEntity.getId());
//		List<HesabTafsiliEntity> dataList = getDataList(localFilter, FlushMode.MANUAL);
//		if(dataList.size() == 1)
//			return dataList.get(0);
//		else if(dataList.size() == 0)
//			return null;
//		else throw new IllegalStateException();
//	}

	public HesabTafsiliEntity getHesabTafsiliByCodeAndSaalMaali(Long hesabCode, SaalMaaliEntity saalMaaliEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", hesabCode);
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		HesabTafsiliEntity hesabTafsiliEntity = load(localFilter);
		return hesabTafsiliEntity;
	}

	@Transactional(readOnly = true)
	public List<HesabTafsiliEntity> getActiveTafsilis(SaalMaaliEntity currentSaalMaali, List<Long> topOrganList) {
		Map<String, Object> localFilter = new HashMap<String, Object>();

//		List<Long> topOrganList = getTopOrgansIdList(curentOrgan);
		localFilter.put("organId@in", topOrganList);

		localFilter.put("hidden@eq", Boolean.FALSE);
		localFilter.put("saalMaali.id@eq", currentSaalMaali.getId());
		return getDataList(localFilter);
	}

	public List<HesabTafsiliEntity> getActiveTafsilis(SaalMaaliEntity currentSaalMaali) {
		Map<String, Object> localFilter = new HashMap<String, Object>();

		localFilter.put("hidden@eq", Boolean.FALSE);
		localFilter.put("saalMaali.id@eq", currentSaalMaali.getId());
		return getDataList(localFilter);
	}

	public List<HesabTafsiliEntity> getActiveTafsilis(SaalMaaliEntity currentSaalMaali, Integer level) {
		Map<String, Object> localFilter = new HashMap<String, Object>();

		localFilter.put("level@eq", level);
		localFilter.put("hidden@eq", Boolean.FALSE);
		localFilter.put("saalMaali.id@eq", currentSaalMaali.getId());
		return getDataList(localFilter);
	}

	@Transactional(readOnly = false)
	public void importFromHesabTafsiliTemplateList(SaalMaaliEntity activeSaalMaaliEntity, Long currentOrganId,
			List<Long> topOrganList, String topOrganCode, String currentOrganName) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organId@eq", activeSaalMaaliEntity.getOrganId());
		List<HesabTafsiliTemplateEntity> dataList = hesabTafsiliTemplateService.getDataList(localFilter);
		for (HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity : dataList) {
			HesabTafsiliEntity hesabTafsili = loadHesabTafsiliByTemplate(hesabTafsiliTemplateEntity,
					activeSaalMaaliEntity);
			if (hesabTafsili == null) {
				try {
					createHesabTafsili(activeSaalMaaliEntity, hesabTafsiliTemplateEntity, currentOrganId, topOrganList,
							topOrganCode, currentOrganName);
				} catch (DuplicateException e) {
					System.out.println(e.getDesc());
				}
			}
		}

		/*
		 * getMyDAO().flush(); for (HesabTafsiliTemplateEntity
		 * hesabTafsiliTemplateEntity : dataList) { try{ HesabTafsiliEntity
		 * hesabTafsiliEntity = loadHesabTafsiliByTemplate(hesabTafsiliTemplateEntity,
		 * activeSaalMaaliEntity);
		 * createHesabTafsiliRelatedEntities(hesabTafsiliTemplateEntity,
		 * hesabTafsiliEntity, activeSaalMaaliEntity); }catch(DuplicateException e){
		 * System.out.println(e.getMessage()); }catch(Exception e){
		 * System.out.println(e.getMessage()); }
		 * 
		 * }
		 */
		System.out.println("end");
	}

	@Transactional(readOnly = false)
	public void createHesabTafsiliRelatedEntities(HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity,
			HesabTafsiliEntity hesabTafsiliEntity, SaalMaaliEntity activeSaalMaaliEntity, Long currentOrganId,
			List<Long> topOrganList, String topOrganCode, String currentOrganName) {

		Set<HesabTafsiliTemplateEntity> templateChilds = hesabTafsiliTemplateEntity.getChilds();

		// saving childs
		for (HesabTafsiliTemplateEntity childTemplateTafsiliEntity : templateChilds) {
			HesabTafsiliEntity childHesabTafsiliEntity = loadHesabTafsiliByTemplate(childTemplateTafsiliEntity,
					activeSaalMaaliEntity);
			if (childHesabTafsiliEntity == null)
				childHesabTafsiliEntity = createHesabTafsili(activeSaalMaaliEntity, childTemplateTafsiliEntity,
						currentOrganId, topOrganList, topOrganCode, currentOrganName);
			if (!hesabTafsiliEntity.getChilds().contains(childHesabTafsiliEntity))
				hesabTafsiliEntity.addTochilds(childHesabTafsiliEntity);
		}

		Set<HesabTafsiliTemplateEntity> templateParents = hesabTafsiliTemplateEntity.getParent();
		for (HesabTafsiliTemplateEntity parentTemplateHesabTafsiliEntity : templateParents) {
			HesabTafsiliEntity parentHesabTafsiliEntity = loadHesabTafsiliByTemplate(parentTemplateHesabTafsiliEntity,
					activeSaalMaaliEntity);
			if (parentHesabTafsiliEntity == null)
				parentHesabTafsiliEntity = createHesabTafsili(activeSaalMaaliEntity, parentTemplateHesabTafsiliEntity,
						currentOrganId, topOrganList, topOrganCode, currentOrganName);
			if (!hesabTafsiliEntity.getParents().contains(parentHesabTafsiliEntity))
				hesabTafsiliEntity.addToparents(parentHesabTafsiliEntity);
		}

		Set<MoeenTafsiliTemplateEntity> templateMoeenTafsili = hesabTafsiliTemplateEntity.getMoeenTafsiliTemplate();
		for (MoeenTafsiliTemplateEntity templateMoeenTafsiliEntity : templateMoeenTafsili) {
			HesabMoeenEntity hesabMoeenEntity = hesabMoeenService.loadHesabMoeenByTemplate(
					templateMoeenTafsiliEntity.getHesabMoeenTemplate(), activeSaalMaaliEntity);
			if (hesabMoeenEntity == null)
				hesabMoeenEntity = hesabMoeenService.createHesabMoeen(activeSaalMaaliEntity,
						templateMoeenTafsiliEntity.getHesabMoeenTemplate(), currentOrganId, topOrganList,
						currentOrganName);

			MoeenTafsiliEntity moeenTafsiliEntity = moeenTafsiliService.load(hesabTafsiliEntity, hesabMoeenEntity,
					templateMoeenTafsiliEntity.getLevel(), FlushMode.ALWAYS);
			if (moeenTafsiliEntity == null) {
				moeenTafsiliEntity = new MoeenTafsiliEntity();
				moeenTafsiliEntity.setHesabTafsili(hesabTafsiliEntity);
				moeenTafsiliEntity.setHesabMoeen(hesabMoeenEntity);

				moeenTafsiliEntity.setLevel(templateMoeenTafsiliEntity.getLevel());
				moeenTafsiliService.save(moeenTafsiliEntity);
			}
		}

		update(hesabTafsiliEntity);
	}

	@Transactional(readOnly = false)
	public void createHesabTafsiliRelatedEntities(HesabTafsiliEntity srcHesabTafsiliEntity,
			HesabTafsiliEntity destHesabTafsiliEntity, SaalMaaliEntity destSaalMaali, List<Long> topOrganList) {

		createHesabTafsiliRelatedChilds(srcHesabTafsiliEntity, destHesabTafsiliEntity, destSaalMaali);

		createHesabTafsiliRelatedParents(srcHesabTafsiliEntity, destHesabTafsiliEntity, destSaalMaali);

		createHesabTafsiliRelatedMoeenTafsilis(srcHesabTafsiliEntity, destHesabTafsiliEntity, destSaalMaali);

		update(destHesabTafsiliEntity);
		createOrUpdateRelatedHesabTafsiliTemplate(destHesabTafsiliEntity, destSaalMaali.getOrganId(), topOrganList,
				destSaalMaali.getOrganName());
	}

	@Transactional(readOnly = false)
	public void copyHesabTafsiliRelatedEntities(HesabTafsiliEntity srcHesabTafsiliEntity,
			HesabTafsiliEntity destHesabTafsiliEntity, SaalMaaliEntity destSaalMaali, List<Long> topOrganList) {

		createHesabTafsiliRelatedChilds(srcHesabTafsiliEntity, destHesabTafsiliEntity, destSaalMaali);

		createHesabTafsiliRelatedMoeenTafsilis(srcHesabTafsiliEntity, destHesabTafsiliEntity, destSaalMaali);

		update(destHesabTafsiliEntity);
		createOrUpdateRelatedHesabTafsiliTemplate(destHesabTafsiliEntity, srcHesabTafsiliEntity.getOrganId(),
				topOrganList, srcHesabTafsiliEntity.getOrganName());
	}

	@Transactional(readOnly = false)
	private void createHesabTafsiliRelatedMoeenTafsilis(HesabTafsiliEntity srcHesabTafsiliEntity,
			HesabTafsiliEntity destHesabTafsiliEntity, SaalMaaliEntity destSaalMaali) {
		Set<MoeenTafsiliEntity> srcMoeenTafsilies = srcHesabTafsiliEntity.getMoeenTafsili();
		for (MoeenTafsiliEntity srcMoeenTafsiliEntity : srcMoeenTafsilies) {
			HesabMoeenEntity srcHesabMoeenEntity = srcMoeenTafsiliEntity.getHesabMoeen();

			HesabMoeenEntity destHesabMoeen = hesabMoeenService
					.getHesabMoeenByCodeAndSaalMaali(srcHesabMoeenEntity.getCode(), destSaalMaali);
			MoeenTafsiliEntity destMoeenTafsiliEntity = moeenTafsiliService.load(destHesabTafsiliEntity, destHesabMoeen,
					srcMoeenTafsiliEntity.getLevel(), FlushMode.ALWAYS);
			if (destMoeenTafsiliEntity == null) {
				destMoeenTafsiliEntity = new MoeenTafsiliEntity();
				destMoeenTafsiliEntity.setHesabTafsili(destHesabTafsiliEntity);
				destMoeenTafsiliEntity.setHesabMoeen(destHesabMoeen);

				destMoeenTafsiliEntity.setLevel(srcMoeenTafsiliEntity.getLevel());
				moeenTafsiliService.save(destMoeenTafsiliEntity);
			}
		}
	}

	@Transactional(readOnly = false)
	private void createHesabTafsiliRelatedParents(HesabTafsiliEntity srcHesabTafsiliEntity,
			HesabTafsiliEntity destHesabTafsiliEntity, SaalMaaliEntity destSaalMaali) {
		if (destHesabTafsiliEntity.getParents() == null)
			destHesabTafsiliEntity.setParents(new HashSet<HesabTafsiliEntity>());
		Set<HesabTafsiliEntity> srcParents = srcHesabTafsiliEntity.getParents();
		for (HesabTafsiliEntity parentHesabTafsiliEntity : srcParents) {
			HesabTafsiliEntity destParent = getHesabTafsiliByCodeAndSaalMaali(parentHesabTafsiliEntity.getCode(),
					destSaalMaali);
			if (!destHesabTafsiliEntity.getParents().contains(destParent))
				destHesabTafsiliEntity.addToparents(destParent);
		}
	}

	@Transactional(readOnly = false)
	private void createHesabTafsiliRelatedChilds(HesabTafsiliEntity srcHesabTafsiliEntity,
			HesabTafsiliEntity destHesabTafsiliEntity, SaalMaaliEntity destSaalMaali) {
		if (destHesabTafsiliEntity.getChilds() == null)
			destHesabTafsiliEntity.setChilds(new HashSet<HesabTafsiliEntity>());

		Set<HesabTafsiliEntity> srcChilds = srcHesabTafsiliEntity.getChilds();

		// saving childs
		for (HesabTafsiliEntity childHesabTafsiliEntity : srcChilds) {
			HesabTafsiliEntity destChild = getHesabTafsiliByCodeAndSaalMaali(childHesabTafsiliEntity.getCode(),
					destSaalMaali);
			if (!destHesabTafsiliEntity.getChilds().contains(destChild))
				destHesabTafsiliEntity.addTochilds(destChild);
		}
	}

	public HesabTafsiliEntity createHesabTafsili(SaalMaaliEntity activeSaalMaaliEntity,
			HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity, Long organId, List<Long> topOrganList,
			String topOrganCode, String organName) {
		HesabTafsiliEntity hesabTafsiliEntity = loadHesabTafsiliByTemplate(hesabTafsiliTemplateEntity,
				activeSaalMaaliEntity);
		if (hesabTafsiliEntity == null) {
			hesabTafsiliEntity = populateHesabTafsili(activeSaalMaaliEntity, hesabTafsiliTemplateEntity);
			save(hesabTafsiliEntity, activeSaalMaaliEntity, organId, topOrganList, topOrganCode, organName);
		}
		return hesabTafsiliEntity;
	}

	@Transactional
	public HesabTafsiliEntity createHesabTafsili(SaalMaaliEntity destSaalMaaliEntity,
			HesabTafsiliEntity srcHesabTafsiliEntity, Long organId, List<Long> topOrganList, String topOrganCode,
			String organName) {
		HesabTafsiliEntity hesabTafsiliEntity = loadHesabTafsiliByCode(srcHesabTafsiliEntity.getCode(),
				destSaalMaaliEntity.getId(), FlushMode.MANUAL);
		if (hesabTafsiliEntity == null)
			hesabTafsiliEntity = loadHesabTafsiliByName(srcHesabTafsiliEntity.getName(), destSaalMaaliEntity,
					FlushMode.MANUAL);

		if (hesabTafsiliEntity == null) {
			hesabTafsiliEntity = populateHesabTafsili(destSaalMaaliEntity, srcHesabTafsiliEntity, organId);
			save(hesabTafsiliEntity, destSaalMaaliEntity, organId, topOrganList, topOrganCode, organName);
		}
		return hesabTafsiliEntity;
	}

	private HesabTafsiliEntity populateHesabTafsili(SaalMaaliEntity activeSaalMaaliEntity,
			HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity) {
		HesabTafsiliEntity hesabTafsiliEntity;
		hesabTafsiliEntity = new HesabTafsiliEntity();
		hesabTafsiliEntity.setBedehkar(0d);
		hesabTafsiliEntity.setBestankr(0d);
		hesabTafsiliEntity.setCode(Long.valueOf(hesabTafsiliTemplateEntity.getCode()));
		hesabTafsiliEntity.setDescription(hesabTafsiliTemplateEntity.getDescription());
		hesabTafsiliEntity.setHesabTafsiliTemplate(hesabTafsiliTemplateEntity);
		hesabTafsiliEntity.setHidden(hesabTafsiliTemplateEntity.getHidden());
		hesabTafsiliEntity.setOrganId(activeSaalMaaliEntity.getOrganId());
		hesabTafsiliEntity.setSaalMaali(activeSaalMaaliEntity);
		hesabTafsiliEntity.setName(hesabTafsiliTemplateEntity.getName());
		hesabTafsiliEntity.setScope(hesabTafsiliTemplateEntity.getScope());
		hesabTafsiliEntity.setTafsilType(hesabTafsiliTemplateEntity.getTafsilType());
		hesabTafsiliEntity.setLevel(hesabTafsiliTemplateEntity.getLevel());
		return hesabTafsiliEntity;
	}

	private HesabTafsiliEntity populateHesabTafsili(SaalMaaliEntity destSaalMaaliEntity,
			HesabTafsiliEntity srcHesabTafsiliEntity, Long organId) {
		HesabTafsiliEntity hesabTafsiliEntity;
		hesabTafsiliEntity = new HesabTafsiliEntity();
		hesabTafsiliEntity.setBedehkar(0d);
		hesabTafsiliEntity.setBestankr(0d);
		hesabTafsiliEntity.setCode(srcHesabTafsiliEntity.getCode());
		hesabTafsiliEntity.setDescription(srcHesabTafsiliEntity.getDescription());
		hesabTafsiliEntity.setHesabTafsiliTemplate(srcHesabTafsiliEntity.getHesabTafsiliTemplate());
		hesabTafsiliEntity.setHidden(srcHesabTafsiliEntity.getHidden());
		hesabTafsiliEntity.setOrganId(organId);
		hesabTafsiliEntity.setSaalMaali(destSaalMaaliEntity);
		hesabTafsiliEntity.setName(srcHesabTafsiliEntity.getName());
		hesabTafsiliEntity.setScope(srcHesabTafsiliEntity.getScope());
		hesabTafsiliEntity.setTafsilType(srcHesabTafsiliEntity.getTafsilType());
		if (srcHesabTafsiliEntity.getLevel() != null)
			hesabTafsiliEntity.setLevel(srcHesabTafsiliEntity.getLevel());
		else
			hesabTafsiliEntity.setLevel(1);
		return hesabTafsiliEntity;
	}

	public HesabTafsiliEntity createHesabTafsiliStateLess(SaalMaaliEntity activeSaalMaaliEntity,
			HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity, Long currentOrganId, List<Long> topOrganList,
			String topOrganCode, String currentOrganName) {
		HesabTafsiliEntity hesabTafsiliEntity = loadHesabTafsiliByTemplate(hesabTafsiliTemplateEntity,
				activeSaalMaaliEntity);
		if (hesabTafsiliEntity == null) {
			hesabTafsiliEntity = populateHesabTafsili(activeSaalMaaliEntity, hesabTafsiliTemplateEntity);
			saveStateLess(hesabTafsiliEntity, activeSaalMaaliEntity, currentOrganId, topOrganList, topOrganCode,
					currentOrganName);
		}
		return hesabTafsiliEntity;
	}

	public HesabTafsiliEntity loadHesabTafsiliByTemplate(HesabTafsiliTemplateEntity tafsiliTemplateEntity,
			SaalMaaliEntity activeSaalMaaliEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("hesabTafsiliTemplate.id@eq", tafsiliTemplateEntity.getId());
		localFilter.put("organId@eq", activeSaalMaaliEntity.getOrganId());
		localFilter.put("saalMaali.id@eq", activeSaalMaaliEntity.getId());
		HesabTafsiliEntity hesabTafsiliEntity = load(localFilter);
		return hesabTafsiliEntity;
	}

	public HesabTafsiliEntity loadHesabTafsiliByCode(String code, SaalMaaliEntity saalMaaliEntity) {
		return loadHesabTafsiliByCode(Long.valueOf(code), saalMaaliEntity.getId(), FlushMode.MANUAL);
	}

	public HesabTafsiliEntity loadHesabTafsiliByTemplateId(Long hesabTafsiliTemplateId, Long saalMaaliId) {
		HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity = hesabTafsiliTemplateService
				.load(hesabTafsiliTemplateId);
		return loadHesabTafsiliByCode(hesabTafsiliTemplateEntity.getId(), saalMaaliId, FlushMode.MANUAL);
	}

	public HesabTafsiliEntity loadHesabTafsiliByCode(Long code, Long saalMaaliId) {
		return loadHesabTafsiliByCode(code, saalMaaliId, FlushMode.MANUAL);
	}

	public HesabTafsiliEntity loadHesabTafsiliByName(String name, SaalMaaliEntity saalMaaliEntity) {
		return loadHesabTafsiliByName(name, saalMaaliEntity, FlushMode.MANUAL);
	}

	public HesabTafsiliEntity loadHesabTafsiliByCode(Long code, Long saalMaaliId, FlushMode flushMode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
//		localFilter.put("organId@eq",saalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq", saalMaaliId);
		HesabTafsiliEntity hesabTafsiliEntity = load(localFilter, flushMode);
		return hesabTafsiliEntity;
	}

//	public HesabTafsiliEntity loadHesabTafsiliByCode(Long code,	SaalMaaliEntity saalMaaliEntity, FlushMode flushMode) {
//		Map<String, Object> localFilter = new HashMap<String, Object>();
//		localFilter.put("code@eq",code);
////		localFilter.put("organId@eq",organId);
//		localFilter.put("saalMaali.id@eq",saalMaaliEntity.getId());
//		HesabTafsiliEntity hesabTafsiliEntity = load(localFilter, flushMode);
//		return hesabTafsiliEntity;
//	}

	public HesabTafsiliEntity loadHesabTafsiliByName(String name, SaalMaaliEntity saalMaaliEntity,
			FlushMode flushMode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq", name);
//		localFilter.put("organId@eq",saalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		HesabTafsiliEntity hesabTafsiliEntity = load(localFilter, flushMode);
		return hesabTafsiliEntity;
	}

//	public HesabTafsiliEntity loadHesabTafsiliByName(String name,	SaalMaaliEntity saalMaaliEntity, FlushMode flushMode, Long organId) {
//		Map<String, Object> localFilter = new HashMap<String, Object>();
//		localFilter.put("name@eq",name);
//		localFilter.put("organId@eq",organId);
//		localFilter.put("saalMaali.id@eq",saalMaaliEntity.getId());
//		HesabTafsiliEntity hesabTafsiliEntity = load(localFilter, flushMode);
//		return hesabTafsiliEntity;
//	}

	@Transactional(readOnly = false)
	public void disableHesab(Long hesabId) {
		HesabTafsiliEntity hesabTafsiliEntity = load(hesabId);
		hesabTafsiliEntity.setHidden(true);
		save(hesabTafsiliEntity);
	}

	@Override
	public void save(HesabTafsiliEntity entity) {
		if (entity.getLevel() == null)
			throw new RequiredFieldNotSetException(SerajMessageUtil.getMessage("HesabTafsili_level"));
		if (!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("HesabTafsili_code"));
		super.save(entity);
	}

	@Transactional(readOnly = true)
	public List<HesabTafsiliEntity> getRootTafsiliOneHesabs(SaalMaaliEntity saalMaaliEntity) {
		List<HesabTafsiliEntity> rootList = new ArrayList<HesabTafsiliEntity>();
		List<HesabTafsiliEntity> levelOneActiveTafsilis = getActiveTafsilis(saalMaaliEntity, 1);
		for (HesabTafsiliEntity hesabTafsiliEntity : levelOneActiveTafsilis) {
			if (hesabTafsiliEntity.getChilds() == null || hesabTafsiliEntity.getChilds().isEmpty())
				if (hesabTafsiliEntity.getMoeenTafsili() != null && !hesabTafsiliEntity.getMoeenTafsili().isEmpty())
					rootList.add(hesabTafsiliEntity);
		}
		return rootList;
	}

//	@Transactional(readOnly=true)
//	public List<HesabTafsiliEntity> getRootShenavarHesabs(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan){
//		List<HesabTafsiliEntity> rootList = new ArrayList<HesabTafsiliEntity>();
//		List<HesabTafsiliEntity> activeTafsilis = getActiveTafsilis(saalMaaliEntity, Boolean.TRUE);
//		for (HesabTafsiliEntity hesabTafsiliEntity : activeTafsilis) {
//			if(hesabTafsiliEntity.getChilds()==null || hesabTafsiliEntity.getChilds().isEmpty())
//				if(hesabTafsiliEntity.getMoeenTafsili()!= null && !hesabTafsiliEntity.getMoeenTafsili().isEmpty())
//					rootList.add(hesabTafsiliEntity);
//		}
//		return rootList;
//	}

	@Transactional(readOnly = true)
	public Map<Long, List<ListOrderedMap<String, Object>>> getTafsiliMoeenMap(SaalMaaliEntity saalMaaliEntity,
			List<Long> topOrganList) {
		Map<Long, List<ListOrderedMap<String, Object>>> tafsiliMoeenMap;
		tafsiliMoeenMap = new HashMap<Long, List<ListOrderedMap<String, Object>>>();

		List<HesabTafsiliEntity> list = getActiveTafsilis(saalMaaliEntity, topOrganList);
		for (HesabTafsiliEntity hesabTafsiliEntity : list) {
			Set<MoeenTafsiliEntity> moeenTafsiliSet = hesabTafsiliEntity.getMoeenTafsili();
			List<ListOrderedMap<String, Object>> hesabMoeenList = new ArrayList<ListOrderedMap<String, Object>>();
			for (MoeenTafsiliEntity moeenTafsiliEntity : moeenTafsiliSet) {
				ListOrderedMap<String, Object> moeenItemMap = new ListOrderedMap<String, Object>();
				moeenItemMap.put("value", moeenTafsiliEntity.getHesabMoeen().getID());
				moeenItemMap.put("label", moeenTafsiliEntity.getHesabMoeen().getDesc());
				hesabMoeenList.add(moeenItemMap);
			}
			tafsiliMoeenMap.put(hesabTafsiliEntity.getId(), hesabMoeenList);
		}
		return tafsiliMoeenMap;
	}

}