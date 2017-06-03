package ir.serajsamaneh.accounting.hesabtafsili;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.hibernate.FlushMode;
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
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;

public class HesabTafsiliService extends
BaseEntityService<HesabTafsiliEntity, Long> {

	@Override
	protected HesabTafsiliDAO getMyDAO() {
		return hesabTafsiliDAO;
	}

	HesabTafsiliDAO hesabTafsiliDAO;
	HesabMoeenService hesabMoeenService;
	SaalMaaliService saalMaaliService;
	HesabTafsiliTemplateService hesabTafsiliTemplateService;
	HesabMoeenTemplateService hesabMoeenTemplateService;
	MoeenTafsiliService moeenTafsiliService;
	AccountingMarkazService accountingMarkazService;
	HesabClassificationService hesabClassificationService;
	SanadHesabdariItemService sanadHesabdariItemService;
	
	public SanadHesabdariItemService getSanadHesabdariItemService() {
		return sanadHesabdariItemService;
	}

	public void setSanadHesabdariItemService(SanadHesabdariItemService sanadHesabdariItemService) {
		this.sanadHesabdariItemService = sanadHesabdariItemService;
	}

	public HesabMoeenTemplateService getHesabMoeenTemplateService() {
		return hesabMoeenTemplateService;
	}

	public void setHesabMoeenTemplateService(
			HesabMoeenTemplateService hesabMoeenTemplateService) {
		this.hesabMoeenTemplateService = hesabMoeenTemplateService;
	}

	public HesabClassificationService getHesabClassificationService() {
		return hesabClassificationService;
	}

	public void setHesabClassificationService(
			HesabClassificationService hesabClassificationService) {
		this.hesabClassificationService = hesabClassificationService;
	}

	public AccountingMarkazService getAccountingMarkazService() {
		return accountingMarkazService;
	}

	public void setAccountingMarkazService(
			AccountingMarkazService accountingMarkazService) {
		this.accountingMarkazService = accountingMarkazService;
	}

	public MoeenTafsiliService getMoeenTafsiliService() {
		return moeenTafsiliService;
	}

	public void setMoeenTafsiliService(MoeenTafsiliService moeenTafsiliService) {
		this.moeenTafsiliService = moeenTafsiliService;
	}

	public HesabTafsiliTemplateService getHesabTafsiliTemplateService() {
		return hesabTafsiliTemplateService;
	}

	public void setHesabTafsiliTemplateService(
			HesabTafsiliTemplateService hesabTafsiliTemplateService) {
		this.hesabTafsiliTemplateService = hesabTafsiliTemplateService;
	}

	public SaalMaaliService getSaalMaaliService() {
		return saalMaaliService;
	}

	public void setSaalMaaliService(SaalMaaliService saalMaaliService) {
		this.saalMaaliService = saalMaaliService;
	}

	public HesabMoeenService getHesabMoeenService() {
		return hesabMoeenService;
	}

	public void setHesabMoeenService(HesabMoeenService hesabMoeenService) {
		this.hesabMoeenService = hesabMoeenService;
	}

	public void setHesabTafsiliDAO(HesabTafsiliDAO hesabTafsiliDAO) {
		this.hesabTafsiliDAO = hesabTafsiliDAO;
	}

	public HesabTafsiliDAO getHesabTafsiliDAO() {
		return hesabTafsiliDAO;
	}

	@Override
	public String getDifferences(HesabTafsiliEntity entity
			) {
		String diffes = "";
		HesabTafsiliEntity oldEntity=(HesabTafsiliEntity) entity.getOldEntity();
		if (entity.getCode() != null
				&& !entity.getCode().equals(oldEntity.getCode()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabTafsili" + "_"
							+ HesabTafsiliEntity.PROP_CODE) + " : " + oldEntity.getCode()
					+ "" + " --> " + entity.getCode() + "" + "]";

		if (entity.getName() != null
				&& !entity.getName().equals(oldEntity.getName()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabTafsili" + "_"
							+ HesabTafsiliEntity.PROP_NAME) + " : " + oldEntity.getName()
					+ "" + " --> " + entity.getName() + "" + "]";

		if (entity.getTafsilType() != null
				&& !entity.getTafsilType().equals(oldEntity.getTafsilType()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabTafsili" + "_"
							+ HesabTafsiliEntity.PROP_TAFSIL_TYPE) + " : "
					+ oldEntity.getTafsilType() + "" + " --> "
					+ entity.getTafsilType() + "" + "]";


		if (entity.getDescription() != null
				&& !entity.getDescription().equals(oldEntity.getDescription()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabTafsili" + "_"
							+ HesabTafsiliEntity.PROP_DESCRIPTION) + " : "
					+ oldEntity.getDescription() + "" + " --> "
					+ entity.getDescription() + "" + "]";

		if (entity.getHidden() != null
				&& !entity.getHidden().equals(oldEntity.getHidden()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabTafsili" + "_"
							+ HesabTafsiliEntity.PROP_HIDDEN) + " : "
					+ oldEntity.getHidden() + "" + " --> " + entity.getHidden()
					+ "" + "]";

		return diffes;
	}


	private void checkHesabUniqueNess(HesabTafsiliEntity entity,	SaalMaaliEntity activeSaalMaaliEntity, OrganEntity currentOrgan) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
//		localFilter.put("organ.id@eq", activeSaalMaaliEntity.getOrgan().getId());
		List<Long> topOrganList = getTopOrgansIdList(currentOrgan);
		localFilter.put("organ.id@in", topOrganList);

		localFilter.put("saalMaali.id@eq", activeSaalMaaliEntity.getId());
		checkUniqueNess(entity, HesabTafsiliEntity.PROP_NAME, entity.getName(),	localFilter, false);
		checkUniqueNess(entity, HesabTafsiliEntity.PROP_CODE, entity.getCode(),	localFilter, false);
	}
	
	@Override
	public void saveOrUpdate(HesabTafsiliEntity entity) {
		if(!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("HesabMoeen_code"));
		if(entity.getHidden() == null)
			entity.setHidden(false);
		super.saveOrUpdate(entity);
	}
	
	private synchronized Long generateHesabTafsiliCode(HesabTafsiliEntity entity, OrganEntity currentOrgan, SaalMaaliEntity currentUserSaalMaaliEntity) {
			Long maxHesabTafsiliCode = getMyDAO().getMaxHesabTafsiliCode(currentOrgan, currentUserSaalMaaliEntity);
//			return new Long(++maxHesabTafsiliCode).toString();
			return ++maxHesabTafsiliCode;
	}
	
	@Transactional
	public void save(HesabTafsiliEntity entity, List<Long> moeenIds, List<Long> childTafsiliIds, List<Long> parentTafsiliIds, List<Long> childAccountingMarkazIds, SaalMaaliEntity activeSaalMaaliEntity, OrganEntity currentOrgan) {
		commonSave(entity, moeenIds, childTafsiliIds, parentTafsiliIds, childAccountingMarkazIds, activeSaalMaaliEntity, currentOrgan);
		save(entity);
	}
	
	@Transactional
	public void save(HesabTafsiliEntity entity,SaalMaaliEntity activeSaalMaaliEntity, OrganEntity currentOrgan) {
		commonSave(entity, new ArrayList<Long>(), new ArrayList<Long>(), new ArrayList<Long>(), new ArrayList<Long>(), activeSaalMaaliEntity, currentOrgan);
		save(entity);
	}
	
	public void saveStateLess(HesabTafsiliEntity entity,SaalMaaliEntity activeSaalMaaliEntity, OrganEntity currentOrgan) {
		commonSave(entity, new ArrayList<Long>(), new ArrayList<Long>(), new ArrayList<Long>(), new ArrayList<Long>(), activeSaalMaaliEntity, currentOrgan);
		super.saveStateLess(entity);
	}
	
	@Override
	public void saveStateLess(HesabTafsiliEntity entity) {
		if(!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("HesabMoeen_code"));

		super.saveStateLess(entity);
	}


	private void checkCycleInChildTafsiliHierarchy(HesabTafsiliEntity entity,
			List<Long> childTafsiliIds) {
		for (Long tafsiliId : childTafsiliIds) {
			HesabTafsiliEntity mainChildEntity = load(tafsiliId);
			
			try{
				checkCycleInChildTafsiliHierarchy(entity, mainChildEntity);
			}catch(FatalException e){
				throw new CycleInHesabTafsiliException(entity, mainChildEntity);
			}
		}
		
	}

	private void checkCycleInChildTafsiliHierarchy(HesabTafsiliEntity mainEntity,
			HesabTafsiliEntity mainChildEntity) {
		if(mainEntity.getId()!=null && mainEntity.getId().equals(mainChildEntity.getId()))
			throw new FatalException();
		Set<HesabTafsiliEntity> childs = mainChildEntity.getChilds();
		for (HesabTafsiliEntity childTfsili : childs) {
			checkCycleInChildTafsiliHierarchy(mainEntity, childTfsili);
		}
	}
	

	private void checkCycleInParentTafsiliHierarchy(HesabTafsiliEntity entity,
			List<Long> parentTafsiliIds) {
		for (Long tafsiliId : parentTafsiliIds) {
			HesabTafsiliEntity mainParentEntity = load(tafsiliId);
			
			try{
				checkCycleInParentTafsiliHierarchy(entity, mainParentEntity);
			}catch(FatalException e){
				throw new CycleInHesabTafsiliException(entity, mainParentEntity);
			}
		}
		
	}

	private void checkCycleInParentTafsiliHierarchy(HesabTafsiliEntity mainEntity,
			HesabTafsiliEntity mainParentEntity) {
		if(mainEntity.getId()!=null && mainEntity.getId().equals(mainParentEntity.getId()))
			throw new FatalException();
		Set<HesabTafsiliEntity> parents = mainParentEntity.getParents();
		for (HesabTafsiliEntity parentTfsili : parents) {
			checkCycleInParentTafsiliHierarchy(mainEntity, parentTfsili);
		}
	}
	@Transactional
	private void commonSave(HesabTafsiliEntity entity, List<Long> moeenIds, List<Long> childTafsiliIds, List<Long> parentTafsiliIds, List<Long> childAccountingMarkazIds, SaalMaaliEntity activeSaalMaaliEntity, OrganEntity currentOrgan) {
		
		
		if(entity.getId()!=null && entity.getSaalMaali()!=null && entity.getSaalMaali().getId()!=null && !entity.getSaalMaali().equals(activeSaalMaaliEntity))
			throw new FatalException(SerajMessageUtil.getMessage("SaalMaali_hesabConflict"));
		if (entity.getId() == null) {
			entity.setBedehkar(0d);
			entity.setBestankr(0d);
		}
		if (entity.getMoeenTafsili() == null) {
			entity.setMoeenTafsili(new HashSet<MoeenTafsiliEntity>());
		}
		if(entity.getSaalMaali() == null || entity.getSaalMaali().getId() == null)
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
		if(entity.getParents()!=null)
			entity.getParents().clear();
		
		for (Long tafsiliId : parentTafsiliIds) {
			HesabTafsiliEntity hesabTafsiliEntity = load(tafsiliId);
			entity.addToparents(hesabTafsiliEntity);
		}
		
		entity.getChildAccountingMarkaz().clear();
		for (Long accountingMarkazId : childAccountingMarkazIds) {
			entity.getChildAccountingMarkaz().add(getAccountingMarkazService().load(accountingMarkazId));
		}
		
		if(entity.getHesabClassification()!=null && entity.getHesabClassification().getId()!=null){
			entity.setHesabClassification(getHesabClassificationService().load(entity.getHesabClassification().getId()));
			Set<HesabMoeenTemplateEntity> hesabMoeenTemplateSet = entity.getHesabClassification().getHesabMoeenTemplate();
			if(hesabMoeenTemplateSet!=null)
				for (HesabMoeenTemplateEntity hesabMoeenTemplateEntity : hesabMoeenTemplateSet){
					HesabMoeenEntity hesabMoeenEntity = getHesabMoeenService().loadHesabMoeenByCode(hesabMoeenTemplateEntity.getCode(), activeSaalMaaliEntity);
					if(hesabMoeenEntity!=null)//maybe there no hesabMoeen for this template in activeSaalMaaliEntity
						addMoeenToMoeenTafsiliSet(entity, hesabMoeenEntity.getId());
				}
			
			Set<HesabTafsiliTemplateEntity> hesabTafsiliTemplateSet = entity.getHesabClassification().getHesabTafsiliTemplate();
			if(hesabTafsiliTemplateSet!=null)
				for (HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity : hesabTafsiliTemplateSet) {
					HesabTafsiliEntity hesabTafsiliByCode = loadHesabTafsiliByCode(new Long(hesabTafsiliTemplateEntity.getCode()), activeSaalMaaliEntity);

					if(hesabTafsiliByCode!=null){//maybe there no hesabTafsili for this template in activeSaalMaaliEntity
						entity.addToparents(hesabTafsiliByCode); 
//						hesabTafsiliByCode.addTochilds(entity);
						save(hesabTafsiliByCode);
					}
				}
		}
		
//		if (!StringUtils.hasText(entity.getCode())) {
		if (entity.getCode()==null) {
			
			entity.setCode(generateHesabTafsiliCode(entity,currentOrgan, activeSaalMaaliEntity));
		}
		checkHesabUniqueNess(entity, activeSaalMaaliEntity, currentOrgan);
		
		checkCycleInChildTafsiliHierarchy(entity, childTafsiliIds);
		checkCycleInParentTafsiliHierarchy(entity, parentTafsiliIds);
		
		createOrUpdateRelatedHesabTafsiliTemplate(entity, currentOrgan);
	}

	
	private void validateTafsiliChild(HesabTafsiliEntity entity, List<Long> childTafsiliIds) {
		Set<HesabTafsiliEntity> removedList = new HashSet<>();
		Set<HesabTafsiliEntity> childs = entity.getChilds();
		
		if(childs == null)
			return;

		for (HesabTafsiliEntity hesabTafsiliEntity : childs) {
			if(!childTafsiliIds.contains(hesabTafsiliEntity.getId()))
				removedList.add(hesabTafsiliEntity);
		}
		
		
		for (HesabTafsiliEntity hesabTafsiliEntity : removedList) {
			Map<String, Object> filter = new HashMap<>();
			filter.put("sanadHesabdari.state@neq", SanadStateEnum.EBTAL);
			filter.put("sanadHesabdari.saalMaali.id@eq", entity.getSaalMaali().getId());
			filter.put("hesabTafsili.id@eq", entity.getId());
			filter.put("articleTafsili.hesabTafsili.id@eq", hesabTafsiliEntity.getId());
			filter.put("articleTafsili.level@eq", 1);
			Integer rowCount = getSanadHesabdariItemService().getRowCount(null,filter);
			if(rowCount>0)
				throw new FatalException(SerajMessageUtil.getMessage("HesabTafsili_childIsUsedInSomeArticles",hesabTafsiliEntity));
		}
	}

	private void validateTafsiliParent(HesabTafsiliEntity entity, List<Long> parentTafsiliIds) {
		Set<HesabTafsiliEntity> removedList = new HashSet<>();
		Set<HesabTafsiliEntity> parents = entity.getParents();
		if(parents == null)
			return;
		for (HesabTafsiliEntity hesabTafsiliEntity : parents) {
			if(!parentTafsiliIds.contains(hesabTafsiliEntity.getId()))
				removedList.add(hesabTafsiliEntity);
		}
		
		
		for (HesabTafsiliEntity hesabTafsiliEntity : removedList) {
			Map<String, Object> filter = new HashMap<>();
			filter.put("sanadHesabdari.state@neq", SanadStateEnum.EBTAL);
			filter.put("sanadHesabdari.saalMaali.id@eq", entity.getSaalMaali().getId());
			filter.put("hesabTafsili.id@eq", hesabTafsiliEntity.getId());
			filter.put("articleTafsili.hesabTafsili.id@eq", entity.getId());
			filter.put("articleTafsili.level@eq", 1);
			Integer rowCount = getSanadHesabdariItemService().getRowCount(null,filter);
			if(rowCount>0)
				throw new FatalException(SerajMessageUtil.getMessage("HesabTafsili_parentIsUsedInSomeArticles",hesabTafsiliEntity));
		}
	}
	
	private void validateMoeenTafsili(HesabTafsiliEntity entity, List<Long> moeenIds) {
		Set<HesabMoeenEntity> removedMoeenList = new HashSet<>();
		List<HesabMoeenEntity> hesabMoeenList = entity.getHesabMoeenList();
		for (HesabMoeenEntity hesabMoeenEntity : hesabMoeenList) {
			if(!moeenIds.contains(hesabMoeenEntity.getId()))
				removedMoeenList.add(hesabMoeenEntity);
		}
		
		for (HesabMoeenEntity hesabMoeenEntity : removedMoeenList) {
			Map<String, Object> filter = new HashMap<>();
			filter.put("sanadHesabdari.saalMaali.id@eq", entity.getSaalMaali().getId());
			filter.put("hesabMoeen.id@eq", hesabMoeenEntity.getId());
			filter.put("hesabTafsili.id@eq", entity.getId());
			Integer rowCount = getSanadHesabdariItemService().getRowCount(null,filter);
			if(rowCount>0)
				throw new FatalException(SerajMessageUtil.getMessage("HesabTafsili_moeenIsUsedInSomeArticles",hesabMoeenEntity));
		}
		
	}

	@Transactional
	public void createOrUpdateRelatedHesabTafsiliTemplate(HesabTafsiliEntity entity, OrganEntity organEntity) {
		HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity = getHesabTafsiliTemplateService().loadByCodeInCurrentOrgan(entity.getCode(), organEntity);
		if(hesabTafsiliTemplateEntity == null){
			hesabTafsiliTemplateEntity = getHesabTafsiliTemplateService().loadByNameInCurrentOrgan(entity.getName(), organEntity);
			if(hesabTafsiliTemplateEntity!=null){
				HesabTafsiliTemplateEntity currentEntityHesabTafsiliTemplate = entity.getHesabTafsiliTemplate();
				
				if(currentEntityHesabTafsiliTemplate == null)
					entity.setHesabTafsiliTemplate(hesabTafsiliTemplateEntity);
				//code of hesabTafsili has changed
				if(currentEntityHesabTafsiliTemplate == null || currentEntityHesabTafsiliTemplate.equals(hesabTafsiliTemplateEntity)){
					hesabTafsiliTemplateEntity.setCode(entity.getCode());
					hesabTafsiliTemplateEntity.setName(entity.getName());
					hesabTafsiliTemplateEntity.setTafsilType(entity.getTafsilType());
					hesabTafsiliTemplateEntity.setDescription(entity.getDescription());
					updateRelatedHesbTafsilies(hesabTafsiliTemplateEntity);
				}else
					throw new FatalException(SerajMessageUtil.getMessage("HesabTafsiliTemplate_cantCreateHesabTafsiliTemplateWithDuplicateNameAndnewCode", entity.getCode(),entity.getName()));
			}else
				hesabTafsiliTemplateEntity = getHesabTafsiliTemplateService().createHesabTafsiliTemplate(entity.getCode(), entity.getName(), organEntity, entity.getTafsilType(), entity.getDescription());
		}
		else{
			
			HesabTafsiliTemplateEntity hesabTafsiliTemplateEntityByName = getHesabTafsiliTemplateService().loadByNameInCurrentOrgan(entity.getName(), organEntity);
			if(hesabTafsiliTemplateEntityByName!=null && !hesabTafsiliTemplateEntityByName.getId().equals(hesabTafsiliTemplateEntity.getId()))
				throw new FatalException(SerajMessageUtil.getMessage("HesabTafsiliTemplate_cantCreateHesabTafsiliTemplateWithDuplicateNameAndnewCode", entity.getCode(),entity.getName()));
 
			hesabTafsiliTemplateEntity.setCode(entity.getCode());
			hesabTafsiliTemplateEntity.setName(entity.getName());
			hesabTafsiliTemplateEntity.setTafsilType(entity.getTafsilType());
			hesabTafsiliTemplateEntity.setDescription(entity.getDescription());
		}
		
		if(hesabTafsiliTemplateEntity.getMoeenTafsiliTemplate() == null)
			hesabTafsiliTemplateEntity.setMoeenTafsiliTemplate(new HashSet<MoeenTafsiliTemplateEntity>());
		else
			hesabTafsiliTemplateEntity.getMoeenTafsiliTemplate().clear();
		
		Set<MoeenTafsiliEntity> moeenTafsili = entity.getMoeenTafsili();
		for (MoeenTafsiliEntity moeenTafsiliEntity : moeenTafsili) {
			MoeenTafsiliTemplateEntity moeenTafsiliTemplateEntity = new MoeenTafsiliTemplateEntity();
			moeenTafsiliTemplateEntity.setHesabMoeenTemplate(getHesabMoeenTemplateService().loadByCode(moeenTafsiliEntity.getHesabMoeen().getCode(), organEntity));
			moeenTafsiliTemplateEntity.setHesabTafsiliTemplate(hesabTafsiliTemplateEntity);
			moeenTafsiliTemplateEntity.setLevel(moeenTafsiliEntity.getLevel());
			if(moeenTafsiliTemplateEntity.getHesabMoeenTemplate() == null)
				throw new FatalException();
			hesabTafsiliTemplateEntity.getMoeenTafsiliTemplate().add(moeenTafsiliTemplateEntity);
		}

		if(hesabTafsiliTemplateEntity.getChilds() == null)
			hesabTafsiliTemplateEntity.setChilds(new HashSet<HesabTafsiliTemplateEntity>());
		else
			hesabTafsiliTemplateEntity.getChilds().clear();
		
		Set<HesabTafsiliEntity> childs = entity.getChilds();
		for (HesabTafsiliEntity hesabTafsiliEntity : childs) {
			HesabTafsiliTemplateEntity templateByCode = getHesabTafsiliTemplateService().loadByCode(hesabTafsiliEntity.getCode(), organEntity);
			if(templateByCode!=null)
				hesabTafsiliTemplateEntity.getChilds().add(templateByCode);
		}
		try{
			getHesabTafsiliTemplateService().save(hesabTafsiliTemplateEntity);
			entity.setHesabTafsiliTemplate(hesabTafsiliTemplateEntity);
			save(entity);
		}catch(DuplicateException e){
			throw new FatalException(SerajMessageUtil.getMessage("HesabTafsili_hesabTafsiliTemplateWithSameNameAndDuplicateCodeExists",entity.getName()+" ("+entity.getCode()+")"));
		}
	}

	@Transactional
	private void updateRelatedHesbTafsilies(
			HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("hesabTafsiliTemplate.id@eq", hesabTafsiliTemplateEntity.getId());
		List<HesabTafsiliEntity> dataList = getDataList(null, filter);
		for (HesabTafsiliEntity hesabTafsiliEntity : dataList) {
			hesabTafsiliEntity.setCode(new Long(hesabTafsiliTemplateEntity.getCode()));
			hesabTafsiliEntity.setName(hesabTafsiliTemplateEntity.getName());
			save(hesabTafsiliEntity);
		}
		
	}

	private void addMoeenToMoeenTafsiliSet(HesabTafsiliEntity entity, Long moeenId) {
		for(MoeenTafsiliEntity moeenTafsiliEntity : entity.getMoeenTafsili()){
			if(moeenTafsiliEntity.getHesabMoeen().getId().equals(moeenId))
				return;
		}
		MoeenTafsiliEntity moeenTafsiliEntity = new MoeenTafsiliEntity();
		moeenTafsiliEntity.setHesabMoeen(getHesabMoeenService().load(moeenId));
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
//		localFilter.put("organ.id@eq", currentOrganEntity.getId());
//		List<HesabTafsiliEntity> dataList = getDataList(null, localFilter, FlushMode.MANUAL);
//		if(dataList.size() == 1)
//			return dataList.get(0);
//		else if(dataList.size() == 0)
//			return null;
//		else throw new IllegalStateException();
//	}

	public HesabTafsiliEntity getHesabTafsiliByCodeAndSaalMaali(Long hesabCode, SaalMaaliEntity saalMaaliEntity){
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", hesabCode);
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		HesabTafsiliEntity hesabTafsiliEntity = load(null, localFilter);
		return hesabTafsiliEntity;
	}
	
	@Transactional(readOnly=true)
	public List<HesabTafsiliEntity> getActiveTafsilis(SaalMaaliEntity currentSaalMaali, OrganEntity curentOrgan) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		
		List<Long> topOrganList = getTopOrgansIdList(curentOrgan);
		localFilter.put("organ.id@in", topOrganList);
		
		localFilter.put("hidden@eq", Boolean.FALSE);
		localFilter.put("saalMaali.id@eq", currentSaalMaali.getId());
		return getDataList(null, localFilter);
	}
	
	public List<HesabTafsiliEntity> getActiveTafsilis(SaalMaaliEntity currentSaalMaali) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		
		localFilter.put("hidden@eq", Boolean.FALSE);
		localFilter.put("saalMaali.id@eq", currentSaalMaali.getId());
		return getDataList(null, localFilter);
	}
	
	@Transactional(readOnly=false)
	public void importFromHesabTafsiliTemplateList(SaalMaaliEntity activeSaalMaaliEntity, OrganEntity currentOrgan){
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eqORorgan.id@isNull",Arrays.asList(activeSaalMaaliEntity.getOrgan().getId(), "ding"));
		List<HesabTafsiliTemplateEntity> dataList = getHesabTafsiliTemplateService().getDataList(null, localFilter);
		for (HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity : dataList) {
			HesabTafsiliEntity hesabTafsili = loadHesabTafsiliByTemplate(hesabTafsiliTemplateEntity, activeSaalMaaliEntity);
			if(hesabTafsili == null){
				try{
					createHesabTafsili(activeSaalMaaliEntity,	hesabTafsiliTemplateEntity, currentOrgan);
				}catch(DuplicateException e){
					System.out.println(e.getDesc());
				}				
			}
		}
		
/*		getMyDAO().flush();
		for (HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity : dataList) {
			try{
				HesabTafsiliEntity hesabTafsiliEntity = loadHesabTafsiliByTemplate(hesabTafsiliTemplateEntity, activeSaalMaaliEntity);
				createHesabTafsiliRelatedEntities(hesabTafsiliTemplateEntity, hesabTafsiliEntity, activeSaalMaaliEntity);
			}catch(DuplicateException e){
				System.out.println(e.getMessage());
			}catch(Exception e){
				System.out.println(e.getMessage());
			}				

		}*/
		System.out.println("end");
	}

	@Transactional(readOnly=false)
	public void createHesabTafsiliRelatedEntities(HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity,
			HesabTafsiliEntity hesabTafsiliEntity, SaalMaaliEntity activeSaalMaaliEntity, OrganEntity currentOrgan) {

		
		Set<HesabTafsiliTemplateEntity> templateChilds = hesabTafsiliTemplateEntity.getChilds();
		
		//saving childs
		for (HesabTafsiliTemplateEntity childTemplateTafsiliEntity : templateChilds) {
			HesabTafsiliEntity childHesabTafsiliEntity = loadHesabTafsiliByTemplate(childTemplateTafsiliEntity, activeSaalMaaliEntity);
			if(childHesabTafsiliEntity == null)
				childHesabTafsiliEntity = createHesabTafsili(activeSaalMaaliEntity, childTemplateTafsiliEntity, currentOrgan);
			if(!hesabTafsiliEntity.getChilds().contains(childHesabTafsiliEntity))
				hesabTafsiliEntity.addTochilds(childHesabTafsiliEntity);
		}
		
		Set<HesabTafsiliTemplateEntity> templateParents = hesabTafsiliTemplateEntity.getParent();
		for (HesabTafsiliTemplateEntity parentTemplateHesabTafsiliEntity : templateParents) {
			HesabTafsiliEntity parentHesabTafsiliEntity = loadHesabTafsiliByTemplate(parentTemplateHesabTafsiliEntity, activeSaalMaaliEntity);
			if(parentHesabTafsiliEntity == null)
				parentHesabTafsiliEntity = createHesabTafsili(activeSaalMaaliEntity, parentTemplateHesabTafsiliEntity, currentOrgan);
			if(!hesabTafsiliEntity.getParents().contains(parentHesabTafsiliEntity))
				hesabTafsiliEntity.addToparents(parentHesabTafsiliEntity);
		}
		
		Set<MoeenTafsiliTemplateEntity> templateMoeenTafsili = hesabTafsiliTemplateEntity.getMoeenTafsiliTemplate();
		for (MoeenTafsiliTemplateEntity templateMoeenTafsiliEntity : templateMoeenTafsili) {
			HesabMoeenEntity hesabMoeenEntity = getHesabMoeenService().loadHesabMoeenByTemplate(templateMoeenTafsiliEntity.getHesabMoeenTemplate(), activeSaalMaaliEntity);
			if(hesabMoeenEntity == null)
				hesabMoeenEntity = getHesabMoeenService().createHesabMoeen(activeSaalMaaliEntity, templateMoeenTafsiliEntity.getHesabMoeenTemplate(), currentOrgan);

			MoeenTafsiliEntity moeenTafsiliEntity = getMoeenTafsiliService().load(hesabTafsiliEntity, hesabMoeenEntity, templateMoeenTafsiliEntity.getLevel(), FlushMode.ALWAYS);
			if(moeenTafsiliEntity == null){
				moeenTafsiliEntity = new MoeenTafsiliEntity();
				moeenTafsiliEntity.setHesabTafsili(hesabTafsiliEntity);
				moeenTafsiliEntity.setHesabMoeen(hesabMoeenEntity);
	
				moeenTafsiliEntity.setLevel(templateMoeenTafsiliEntity.getLevel());
				getMoeenTafsiliService().save(moeenTafsiliEntity);
			}
		}
		
		update(hesabTafsiliEntity);
	}
	
	@Transactional(readOnly=false)
	public void createHesabTafsiliRelatedEntities(HesabTafsiliEntity srcHesabTafsiliEntity,
			HesabTafsiliEntity destHesabTafsiliEntity, SaalMaaliEntity destSaalMaali) {
		
		if(destHesabTafsiliEntity.getChilds() == null)
			destHesabTafsiliEntity.setChilds(new HashSet<HesabTafsiliEntity>());
		
		if(destHesabTafsiliEntity.getParents() == null)
			destHesabTafsiliEntity.setParents(new HashSet<HesabTafsiliEntity>());
		
		Set<HesabTafsiliEntity> srcChilds = srcHesabTafsiliEntity.getChilds();
		
		//saving childs
		for (HesabTafsiliEntity childHesabTafsiliEntity : srcChilds) {
//			if(childHesabTafsiliEntity == null)
//				childHesabTafsiliEntity = createHesabTafsili(activeSaalMaaliEntity, childHesabTafsiliEntity);
			HesabTafsiliEntity destChild = getHesabTafsiliByCodeAndSaalMaali(childHesabTafsiliEntity.getCode(), destSaalMaali);
			if(!destHesabTafsiliEntity.getChilds().contains(destChild))
				destHesabTafsiliEntity.addTochilds(destChild);
		}
		
		Set<HesabTafsiliEntity> srcParents = srcHesabTafsiliEntity.getParents();
		for (HesabTafsiliEntity parentHesabTafsiliEntity : srcParents) {
//			if(parentHesabTafsiliEntity == null)
//				parentHesabTafsiliEntity = createHesabTafsili(activeSaalMaaliEntity, parentHesabTafsiliEntity);
			HesabTafsiliEntity destParent = getHesabTafsiliByCodeAndSaalMaali(parentHesabTafsiliEntity.getCode(), destSaalMaali);
			if(!destHesabTafsiliEntity.getParents().contains(destParent))
				destHesabTafsiliEntity.addToparents(destParent);
		}
		
		Set<MoeenTafsiliEntity> srcMoeenTafsilies = srcHesabTafsiliEntity.getMoeenTafsili();
		for (MoeenTafsiliEntity srcMoeenTafsiliEntity : srcMoeenTafsilies) {
			HesabMoeenEntity srcHesabMoeenEntity = srcMoeenTafsiliEntity.getHesabMoeen();
//			if(hesabMoeenEntity == null)
//				hesabMoeenEntity = getHesabMoeenService().createHesabMoeen(activeSaalMaaliEntity, entity.getHesabMoeen());
			
			HesabMoeenEntity destHesabMoeen = getHesabMoeenService().getHesabMoeenByCodeAndSaalMaali(srcHesabMoeenEntity.getCode(), destSaalMaali);
			MoeenTafsiliEntity destMoeenTafsiliEntity = getMoeenTafsiliService().load(destHesabTafsiliEntity, destHesabMoeen, srcMoeenTafsiliEntity.getLevel(), FlushMode.ALWAYS);
			if(destMoeenTafsiliEntity == null){
				destMoeenTafsiliEntity = new MoeenTafsiliEntity();
				destMoeenTafsiliEntity.setHesabTafsili(destHesabTafsiliEntity);
				destMoeenTafsiliEntity.setHesabMoeen(destHesabMoeen);
				
				destMoeenTafsiliEntity.setLevel(srcMoeenTafsiliEntity.getLevel());
				getMoeenTafsiliService().save(destMoeenTafsiliEntity);
			}
		}
		
		update(destHesabTafsiliEntity);
		createOrUpdateRelatedHesabTafsiliTemplate(destHesabTafsiliEntity, destSaalMaali.getOrgan());
	}

	public HesabTafsiliEntity createHesabTafsili(SaalMaaliEntity activeSaalMaaliEntity,
			HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity, OrganEntity currentOrgan) {
		HesabTafsiliEntity hesabTafsiliEntity = loadHesabTafsiliByTemplate(hesabTafsiliTemplateEntity, activeSaalMaaliEntity);
		if(hesabTafsiliEntity == null){
			hesabTafsiliEntity = populateHesabTafsili(activeSaalMaaliEntity,
					hesabTafsiliTemplateEntity);
			save(hesabTafsiliEntity, activeSaalMaaliEntity, currentOrgan);
		}
		return hesabTafsiliEntity;
	}
	
	@Transactional
	public HesabTafsiliEntity createHesabTafsili(SaalMaaliEntity activeSaalMaaliEntity,
			HesabTafsiliEntity srcHesabTafsiliEntity, OrganEntity currentOrgan) {
		HesabTafsiliEntity hesabTafsiliEntity = loadHesabTafsiliByCode(srcHesabTafsiliEntity.getCode(), activeSaalMaaliEntity);
		if(hesabTafsiliEntity == null)
			hesabTafsiliEntity = loadHesabTafsiliByName(srcHesabTafsiliEntity.getName(), activeSaalMaaliEntity);
		
		if(hesabTafsiliEntity == null){
			hesabTafsiliEntity = populateHesabTafsili(activeSaalMaaliEntity, srcHesabTafsiliEntity);
			save(hesabTafsiliEntity, activeSaalMaaliEntity, currentOrgan);
		}
		return hesabTafsiliEntity;
	}

	private HesabTafsiliEntity populateHesabTafsili(SaalMaaliEntity activeSaalMaaliEntity,
			HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity) {
		HesabTafsiliEntity hesabTafsiliEntity;
		hesabTafsiliEntity = new HesabTafsiliEntity();
		hesabTafsiliEntity.setBedehkar(0d);
		hesabTafsiliEntity.setBestankr(0d);
		hesabTafsiliEntity.setCode(new Long(hesabTafsiliTemplateEntity.getCode()));
		hesabTafsiliEntity.setDescription(hesabTafsiliTemplateEntity.getDescription());
		hesabTafsiliEntity.setHesabTafsiliTemplate(hesabTafsiliTemplateEntity);
		hesabTafsiliEntity.setHidden(hesabTafsiliTemplateEntity.getHidden());
		hesabTafsiliEntity.setOrgan(activeSaalMaaliEntity.getOrgan());
		hesabTafsiliEntity.setSaalMaali(activeSaalMaaliEntity);
		hesabTafsiliEntity.setName(hesabTafsiliTemplateEntity.getName());
		hesabTafsiliEntity.setScope(hesabTafsiliTemplateEntity.getScope());
		hesabTafsiliEntity.setTafsilType(hesabTafsiliTemplateEntity.getTafsilType());
		return hesabTafsiliEntity;
	}
	
	private HesabTafsiliEntity populateHesabTafsili(SaalMaaliEntity activeSaalMaaliEntity,
			HesabTafsiliEntity srcHesabTafsiliEntity) {
		HesabTafsiliEntity hesabTafsiliEntity;
		hesabTafsiliEntity = new HesabTafsiliEntity();
		hesabTafsiliEntity.setBedehkar(0d);
		hesabTafsiliEntity.setBestankr(0d);
		hesabTafsiliEntity.setCode(srcHesabTafsiliEntity.getCode());
		hesabTafsiliEntity.setDescription(srcHesabTafsiliEntity.getDescription());
		hesabTafsiliEntity.setHesabTafsiliTemplate(srcHesabTafsiliEntity.getHesabTafsiliTemplate());
		hesabTafsiliEntity.setHidden(srcHesabTafsiliEntity.getHidden());
		hesabTafsiliEntity.setOrgan(activeSaalMaaliEntity.getOrgan());
		hesabTafsiliEntity.setSaalMaali(activeSaalMaaliEntity);
		hesabTafsiliEntity.setName(srcHesabTafsiliEntity.getName());
		hesabTafsiliEntity.setScope(srcHesabTafsiliEntity.getScope());
		hesabTafsiliEntity.setTafsilType(srcHesabTafsiliEntity.getTafsilType());
		return hesabTafsiliEntity;
	}
	
	public HesabTafsiliEntity createHesabTafsiliStateLess(SaalMaaliEntity activeSaalMaaliEntity,
			HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity, OrganEntity currentOrgan) {
		HesabTafsiliEntity hesabTafsiliEntity = loadHesabTafsiliByTemplate(hesabTafsiliTemplateEntity, activeSaalMaaliEntity);
		if(hesabTafsiliEntity == null){
			hesabTafsiliEntity = populateHesabTafsili(activeSaalMaaliEntity,
					hesabTafsiliTemplateEntity);
			saveStateLess(hesabTafsiliEntity, activeSaalMaaliEntity, currentOrgan);
		}
		return hesabTafsiliEntity;
	}
	
	public HesabTafsiliEntity loadHesabTafsiliByTemplate(HesabTafsiliTemplateEntity tafsiliTemplateEntity, SaalMaaliEntity activeSaalMaaliEntity){
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("hesabTafsiliTemplate.id@eq",tafsiliTemplateEntity.getId());
		localFilter.put("organ.id@eq",activeSaalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq",activeSaalMaaliEntity.getId());
		HesabTafsiliEntity hesabTafsiliEntity = load(null, localFilter );
		return hesabTafsiliEntity;
	}

	public HesabTafsiliEntity loadHesabTafsiliByCode(String code,	SaalMaaliEntity saalMaaliEntity) {
		return loadHesabTafsiliByCode(new Long(code), saalMaaliEntity, FlushMode.MANUAL);
	}
	
	public HesabTafsiliEntity loadHesabTafsiliByCode(Long code,	SaalMaaliEntity saalMaaliEntity) {
		return loadHesabTafsiliByCode(code, saalMaaliEntity, FlushMode.MANUAL);
	}
	
	public HesabTafsiliEntity loadHesabTafsiliByName(String name,	SaalMaaliEntity saalMaaliEntity) {
		return loadHesabTafsiliByName(name, saalMaaliEntity, FlushMode.MANUAL);
	}
	public HesabTafsiliEntity loadHesabTafsiliByCode(Long code,	SaalMaaliEntity saalMaaliEntity, FlushMode flushMode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq",code);
//		localFilter.put("organ.id@eq",saalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq",saalMaaliEntity.getId());
		HesabTafsiliEntity hesabTafsiliEntity = load(null, localFilter, flushMode);
		return hesabTafsiliEntity;
	}

	public HesabTafsiliEntity loadHesabTafsiliByName(String name,	SaalMaaliEntity saalMaaliEntity, FlushMode flushMode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq",name);
//		localFilter.put("organ.id@eq",saalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq",saalMaaliEntity.getId());
		HesabTafsiliEntity hesabTafsiliEntity = load(null, localFilter, flushMode);
		return hesabTafsiliEntity;
	}
	

	@Transactional(readOnly=false)
	public void disableHesab(Long hesabId){
		HesabTafsiliEntity hesabTafsiliEntity = load(hesabId);
		hesabTafsiliEntity.setHidden(true);
		save(hesabTafsiliEntity);
	}

	@Override
	public void save(HesabTafsiliEntity entity) {
		if(!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("HesabTafsili_code"));
		super.save(entity);
	}
	
	@Transactional(readOnly=true)
	public List<HesabTafsiliEntity> getRootHesabs(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan){
		List<HesabTafsiliEntity> rootList = new ArrayList<HesabTafsiliEntity>();
		List<HesabTafsiliEntity> activeTafsilis = getActiveTafsilis(saalMaaliEntity);
		for (HesabTafsiliEntity hesabTafsiliEntity : activeTafsilis) {
			if(hesabTafsiliEntity.getChilds()==null || hesabTafsiliEntity.getChilds().isEmpty())
				if(hesabTafsiliEntity.getMoeenTafsili()!= null && !hesabTafsiliEntity.getMoeenTafsili().isEmpty())
				rootList.add(hesabTafsiliEntity);
		}
		return rootList;
	}

	@Transactional(readOnly=true)
	public Map<Long, List<ListOrderedMap>> getTafsiliMoeenMap(SaalMaaliEntity saalMaaliEntity,
			OrganEntity currentOrgan) {
		Map<Long, List<ListOrderedMap>> tafsiliMoeenMap;
		tafsiliMoeenMap = new HashMap<Long, List<ListOrderedMap>>();

		List<HesabTafsiliEntity> list = getActiveTafsilis(saalMaaliEntity, currentOrgan);
		for (HesabTafsiliEntity hesabTafsiliEntity : list) {
			Set<MoeenTafsiliEntity> moeenTafsiliSet = hesabTafsiliEntity.getMoeenTafsili();
			List<ListOrderedMap> hesabMoeenList = new ArrayList<ListOrderedMap>();
			for (MoeenTafsiliEntity moeenTafsiliEntity : moeenTafsiliSet) {
				ListOrderedMap moeenItemMap = new ListOrderedMap();
				moeenItemMap.put("value",moeenTafsiliEntity.getHesabMoeen().getID());
				moeenItemMap.put("label",moeenTafsiliEntity.getHesabMoeen().getDesc());
				hesabMoeenList.add(moeenItemMap);
			}
			tafsiliMoeenMap.put(hesabTafsiliEntity.getId(),hesabMoeenList);
		}
		return tafsiliMoeenMap;
	}


}