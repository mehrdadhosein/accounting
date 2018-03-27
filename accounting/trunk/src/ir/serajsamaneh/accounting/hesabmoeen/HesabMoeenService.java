package ir.serajsamaneh.accounting.hesabmoeen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.FlushMode;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import ir.serajsamaneh.accounting.enumeration.HesabScopeEnum;
import ir.serajsamaneh.accounting.enumeration.SanadStateEnum;
import ir.serajsamaneh.accounting.hesabkol.BaseHesabKolEntity;
import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.hesabkol.HesabKolService;
import ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateEntity;
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
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.systemconfig.SystemConfigService;
import ir.serajsamaneh.core.util.SerajMessageUtil;

public class HesabMoeenService extends
		BaseEntityService<HesabMoeenEntity, Long> {

	@Override
	protected HesabMoeenDAO getMyDAO() {
		return hesabMoeenDAO;
	}

	HesabMoeenDAO hesabMoeenDAO;
	HesabMoeenTemplateService hesabMoeenTemplateService;
	HesabKolService hesabKolService;
	SaalMaaliService saalMaaliService;
	HesabTafsiliService hesabTafsiliService;
	MoeenTafsiliService moeenTafsiliService;
	SanadHesabdariItemService sanadHesabdariItemService;
	SystemConfigService systemConfigService;

	public SystemConfigService getSystemConfigService() {
		return systemConfigService;
	}

	public void setSystemConfigService(SystemConfigService systemConfigService) {
		this.systemConfigService = systemConfigService;
	}

	public SanadHesabdariItemService getSanadHesabdariItemService() {
		return sanadHesabdariItemService;
	}

	public void setSanadHesabdariItemService(SanadHesabdariItemService sanadHesabdariItemService) {
		this.sanadHesabdariItemService = sanadHesabdariItemService;
	}

	public MoeenTafsiliService getMoeenTafsiliService() {
		return moeenTafsiliService;
	}

	public void setMoeenTafsiliService(MoeenTafsiliService moeenTafsiliService) {
		this.moeenTafsiliService = moeenTafsiliService;
	}

	public HesabTafsiliService getHesabTafsiliService() {
		return hesabTafsiliService;
	}

	public void setHesabTafsiliService(HesabTafsiliService hesabTafsiliService) {
		this.hesabTafsiliService = hesabTafsiliService;
	}

	public SaalMaaliService getSaalMaaliService() {
		return saalMaaliService;
	}

	public void setSaalMaaliService(SaalMaaliService saalMaaliService) {
		this.saalMaaliService = saalMaaliService;
	}

	public HesabKolService getHesabKolService() {
		return hesabKolService;
	}

	public void setHesabKolService(HesabKolService hesabKolService) {
		this.hesabKolService = hesabKolService;
	}

	public HesabMoeenTemplateService getHesabMoeenTemplateService() {
		return hesabMoeenTemplateService;
	}

	public void setHesabMoeenTemplateService(
			HesabMoeenTemplateService hesabMoeenTemplateService) {
		this.hesabMoeenTemplateService = hesabMoeenTemplateService;
	}

	public void setHesabMoeenDAO(HesabMoeenDAO hesabMoeenDAO) {
		this.hesabMoeenDAO = hesabMoeenDAO;
	}

	public HesabMoeenDAO getHesabMoeenDAO() {
		return hesabMoeenDAO;
	}

	@Transactional
	public void save(HesabMoeenEntity entity,SaalMaaliEntity activeSaalMaaliEntity, OrganEntity currentOrgan, HesabKolEntity oldHesabKolEntity) {
		if(entity.getId()!=null && !entity.getHesabKol().getId().equals(oldHesabKolEntity.getId()))
			validateMoeenKol(entity, oldHesabKolEntity);
		save(entity, activeSaalMaaliEntity, currentOrgan);
	}

	@Transactional
	private void save(HesabMoeenEntity entity,SaalMaaliEntity activeSaalMaaliEntity, OrganEntity currentOrgan) {
		commonSave(entity, activeSaalMaaliEntity, currentOrgan);
		save(entity);
		boolean isNew=(entity.getID()!=null?false:true);
		logAction(isNew, entity);
	}

	private void validateMoeenKol(HesabMoeenEntity entity, HesabKolEntity oldHesabKolEntity) {
		Map<String, Object> filter = new HashMap<>();
		filter.put("sanadHesabdari.state@neq", SanadStateEnum.EBTAL);
		filter.put("sanadHesabdari.saalMaali.id@eq", entity.getSaalMaali().getId());
		filter.put("hesabMoeen.id@eq", entity.getId());
		filter.put("hesabKol.id@eq", oldHesabKolEntity.getId());
		Integer rowCount = getSanadHesabdariItemService().getRowCount(null,filter);
		if(rowCount>0)
			throw new FatalException(SerajMessageUtil.getMessage("HesabMoeen_KolIsUsedInSomeArticles",oldHesabKolEntity));
		
	}
	
//	public void saveStateLess(HesabMoeenEntity entity,SaalMaaliEntity activeSaalMaaliEntity) {
//		commonSave(entity, activeSaalMaaliEntity);
//		super.saveStateLess(entity);
//	}

	@Transactional
	private void commonSave(HesabMoeenEntity entity,
			SaalMaaliEntity activeSaalMaaliEntity, OrganEntity currentOrgan) {

		if(entity.getId()!=null && entity.getSaalMaali()!=null && entity.getSaalMaali().getId()!=null && !entity.getSaalMaali().equals(activeSaalMaaliEntity))
			throw new FatalException(SerajMessageUtil.getMessage("SaalMaali_hesabConflict"));

		if (entity.getId() == null) {
			entity.setBedehkar(0d);
			entity.setBestankr(0d);
		}
		
		if (!StringUtils.hasText(entity.getCode()) && !getSystemConfigService().getValue(currentOrgan, null, "HesabMoeenCodingType").equals("MANUAL")) {
			entity.setCode(generateHesabMoeenCode(entity, currentOrgan, activeSaalMaaliEntity));
		}
		
		if(entity.getSaalMaali() == null || entity.getSaalMaali().getId() == null)
			entity.setSaalMaali(activeSaalMaaliEntity);
		
//		if (!StringUtils.hasText(entity.getCode())) {
//			
//			entity.setCode(generateHesabCode(entity, currentOrgan, activeSaalMaaliEntity));
//		}
		if(entity.getHidden() == null)
			entity.setHidden(Boolean.FALSE);
		checkHesabUniqueNess(entity, activeSaalMaaliEntity, currentOrgan);
		
		createOrUpdateRelatedHesabMoeenTemplate(entity, currentOrgan);
	}

	private  String generateHesabMoeenCode(HesabMoeenEntity entity, OrganEntity organEntity, SaalMaaliEntity activeSaalMaaliEntity) {
		String maxHesabMoeenCode = null;
		if (getSystemConfigService().getValue(organEntity, null, "HesabMoeenCodingType").equals("SERIAL")) {
			maxHesabMoeenCode = getMyDAO().getMaxHesabMoeenCode(null, 0, organEntity, activeSaalMaaliEntity);
			return maxHesabMoeenCode;
		} else if (getSystemConfigService().getValue(organEntity, null, "HesabMoeenCodingType").equals("VARIABLE_HIERARCHICAL")) {
			String maxHierArchicalHesabMoeenCode = getMyDAO().getMaxHesabMoeenCode(entity.getHesabKol(),0,organEntity, activeSaalMaaliEntity);
			return maxHierArchicalHesabMoeenCode;
		} else if (getSystemConfigService().getValue(organEntity, null, "HesabMoeenCodingType").equals("CONSTANT_HIERARCHICAL")) {
			Integer HesabMoeenCodeCharactersNumber=Integer.parseInt(getSystemConfigService().getValue(organEntity, null, "hesabMoeenCodeCharactersNumber"));
			String maxHierArchicalHesabMoeenCode = getMyDAO().getMaxHesabMoeenCode(entity.getHesabKol(),HesabMoeenCodeCharactersNumber,organEntity, activeSaalMaaliEntity);
			
			return maxHierArchicalHesabMoeenCode;
		}
			throw new IllegalStateException("HesabMoeenCodingType is required");

	}
	
	@Transactional
	public void createOrUpdateRelatedHesabMoeenTemplate(HesabMoeenEntity entity,OrganEntity organEntity) {

		
		HesabMoeenTemplateEntity hesabMoeenTemplateEntity = getHesabMoeenTemplateService().loadByCodeInCurrentOrgan(entity.getCode(), organEntity);
		if(hesabMoeenTemplateEntity == null){
			hesabMoeenTemplateEntity = getHesabMoeenTemplateService().loadByNameInCurrentOrgan(entity.getName(), organEntity);
			if(hesabMoeenTemplateEntity!=null){
				HesabMoeenTemplateEntity currentEntityHesabMoeenTemplate = entity.getHesabMoeenTemplate();
				
				//code of hesabTafsili has changed
				if(currentEntityHesabMoeenTemplate.getId()!=null && currentEntityHesabMoeenTemplate.equals(hesabMoeenTemplateEntity)){
					hesabMoeenTemplateEntity.setCode(entity.getCode().toString());
					hesabMoeenTemplateEntity.setName(entity.getName());
					hesabMoeenTemplateEntity.setDescription(entity.getDescription());
					updateRelatedHesbMoeens(hesabMoeenTemplateEntity);
				}else
					throw new FatalException(SerajMessageUtil.getMessage("HesabMoeenTemplate_cantCreateHesabMoeenTemplateWithDuplicateNameAndnewCode", entity.getCode(),entity.getName()));
			}else{
				HesabKolEntity hesabKolEntity = getHesabKolService().load(entity.getHesabKol().getID());
				hesabMoeenTemplateEntity = getHesabMoeenTemplateService().createHesabMoeenTemplate(entity.getCode(), entity.getName(), hesabKolEntity.getCode(), organEntity, false);
			}

		}
		else{
			HesabMoeenTemplateEntity hesabMoeenTemplateEntityByName = getHesabMoeenTemplateService().loadByNameInCurrentOrgan(entity.getName(), organEntity);
			if(hesabMoeenTemplateEntityByName!=null && !hesabMoeenTemplateEntityByName.getId().equals(hesabMoeenTemplateEntity.getId()))
				throw new FatalException(SerajMessageUtil.getMessage("HesabMoeenTemplate_cantCreateHesabMoeenTemplateWithDuplicateNameAndnewCode", entity.getCode(),entity.getName()));
			hesabMoeenTemplateEntity.setCode(entity.getCode().toString());
			hesabMoeenTemplateEntity.setName(entity.getName());
			hesabMoeenTemplateEntity.setDescription(entity.getDescription());
		}
		
		getHesabMoeenTemplateService().save(hesabMoeenTemplateEntity);
		entity.setHesabMoeenTemplate(hesabMoeenTemplateEntity);
		save(entity);
	}

	@Transactional
	private void updateRelatedHesbMoeens(
			HesabMoeenTemplateEntity hesabMoeenTemplateEntity) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("hesabMoeenTemplate.id@eq", hesabMoeenTemplateEntity.getId());
		List<HesabMoeenEntity> dataList = getDataList(null, filter);
		for (HesabMoeenEntity hesabMoeenEntity : dataList) {
			hesabMoeenEntity.setCode(hesabMoeenTemplateEntity.getCode());
			hesabMoeenEntity.setName(hesabMoeenTemplateEntity.getName());
			save(hesabMoeenEntity);			
		}

	}

	private void checkHesabUniqueNess(HesabMoeenEntity entity,	SaalMaaliEntity activeSaalMaaliEntity, OrganEntity currentOrgan) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
//		localFilter.put("organ.id@eq", activeSaalMaaliEntity.getOrgan().getId());
		
		List<Long> topOrganList = getTopOrgansIdList(currentOrgan);
		localFilter.put("organ.id@in", topOrganList);
		
		localFilter.put("saalMaali.id@eq", activeSaalMaaliEntity.getId());
		checkUniqueNess(entity, HesabTafsiliEntity.PROP_NAME, entity.getName(),	localFilter, false);
		checkUniqueNess(entity, HesabTafsiliEntity.PROP_CODE, entity.getCode(),	localFilter, false);
	}
	
	@Transactional
	public void updateValues(HesabMoeenEntity entity) {
		save(entity);
	}
	

	
	@Transactional
	public void saveOrUpdateGlobal(HesabMoeenEntity entity) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("scope@eq", HesabScopeEnum.GLOBAL);
		checkUniqueNess(entity, HesabMoeenEntity.PROP_NAME, entity.getName(),
				localFilter, false);
		checkUniqueNess(entity, HesabMoeenEntity.PROP_CODE, entity.getCode(),
				localFilter, false);
		saveOrUpdate(entity);
	}
	
//	static Long maxKalaCode = null;
//	private synchronized String generateHesabCode(HesabMoeenEntity entity, OrganEntity currentOrgan, SaalMaaliEntity activeSaalMaaliEntity) {
//		String maxHierArchicalHesabMoeenCode = getMyDAO().getMaxHesabMoeenCode(entity.getHesabKol(), currentOrgan, activeSaalMaaliEntity);
//		return maxHierArchicalHesabMoeenCode;
//	}
	
	@Override
	public String getDifferences(HesabMoeenEntity entity
			) {
		String diffes = "";
		HesabMoeenEntity oldEntity=(HesabMoeenEntity) entity.getOldEntity();
		if (entity.getCode() != null
				&& !entity.getCode().equals(oldEntity.getCode()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabMoeen" + "_"
							+ HesabMoeenEntity.PROP_CODE) + " : " + oldEntity.getCode()
					+ "" + " --> " + entity.getCode() + "" + "]";

		if (entity.getName() != null
				&& !entity.getName().equals(oldEntity.getName()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabMoeen" + "_"
							+ HesabMoeenEntity.PROP_NAME) + " : " + oldEntity.getName()
					+ "" + " --> " + entity.getName() + "" + "]";

		if (entity.getDescription() != null
				&& !entity.getDescription().equals(oldEntity.getDescription()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabMoeen" + "_"
							+ HesabMoeenEntity.PROP_DESCRIPTION) + " : "
					+ oldEntity.getDescription() + "" + " --> "
					+ entity.getDescription() + "" + "]";

		if (entity.getHidden() != null
				&& !entity.getHidden().equals(oldEntity.getHidden()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabMoeen" + "_"
							+ HesabMoeenEntity.PROP_HIDDEN) + " : "
					+ oldEntity.getHidden() + "" + " --> " + entity.getHidden()
					+ "" + "]";

		return diffes;
	}

//	public HesabMoeenEntity getHesabMoeenByCode(String hesabCode) {
//
//		Map<String, Object> localFilter = new HashMap<String, Object>();
//		localFilter.put("code@eq", hesabCode);
//		localFilter.put("organ.id@eq", getCurrentOrgan().getId());
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
		if(!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("HesabMoeen_code"));
		if(entity.getHidden() == null)
			entity.setHidden(false);
		super.saveOrUpdate(entity);
	}

	@Override
	@Transactional
	public void saveOrUpdateStateLess(HesabMoeenEntity entity) {
		if(!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("HesabMoeen_code"));
		if(entity.getHidden() == null)
			entity.setHidden(false);
		super.saveOrUpdateStateLess(entity);
	}
	
	public List<HesabMoeenEntity> getActiveMoeens(Long hesabKolId, SaalMaaliEntity currentSaalMaali, OrganEntity curentOrgan) {
		Map<String, Object> localFilter = new HashMap<String, Object>();

		List<Long> topOrganList = getTopOrgansIdList(curentOrgan);
		localFilter.put("organ.id@in", topOrganList);
		
		localFilter.put("hidden@eq", Boolean.FALSE);
		localFilter.put("hesabKol.id@eq", hesabKolId);
		localFilter.put("saalMaali.id@eq", currentSaalMaali.getId());
		return getDataList(null, localFilter);
	}
	public List<HesabMoeenEntity> getActiveMoeens(SaalMaaliEntity currentSaalMaali, OrganEntity curentOrgan) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		List<Long> topOrganList = getTopOrgansIdList(curentOrgan);
		localFilter.put("organ.id@in", topOrganList);
		
		localFilter.put("hidden@eq", Boolean.FALSE);
		localFilter.put("saalMaali.id@eq", currentSaalMaali.getId());
		return getDataList(null, localFilter);
	}

	public List<HesabTafsiliEntity> getActiveTafsilies(HesabMoeenEntity hesabMoeenEntity, SaalMaaliEntity saalMaaliEntity, OrganEntity curentOrgan) {
		return getActiveTafsilies(hesabMoeenEntity, saalMaaliEntity, curentOrgan, null);
	}
	public List<HesabTafsiliEntity> getActiveTafsilies(HesabMoeenEntity hesabMoeenEntity, SaalMaaliEntity saalMaaliEntity, OrganEntity curentOrgan, Integer level) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		List<Long> topOrganList = getTopOrgansIdList(curentOrgan);
		localFilter.put("hesabTafsili.organ.id@in", topOrganList);
		
		localFilter.put("hesabTafsili.level@eq", level);
		localFilter.put("hesabTafsili.hidden@eq", Boolean.FALSE);
		localFilter.put("hesabTafsili.saalMaali.id@eq", saalMaaliEntity.getId());
		
		localFilter.put("hesabMoeen.id@eq", hesabMoeenEntity.getId());
//		localFilter.put("hesabTafsili.isShenavar@eq", Boolean.FALSE);
		List<MoeenTafsiliEntity> dataList = getMoeenTafsiliService().getDataList(null, localFilter);
		
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
	
	@Transactional(readOnly=false)
	public void importFromHesabMoeenTemplateList(SaalMaaliEntity activeSaalMaaliEntity, OrganEntity currentOrgan) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eq",activeSaalMaaliEntity.getOrgan().getId());		
		List<HesabMoeenTemplateEntity> dataList = getHesabMoeenTemplateService().getDataList(null, localFilter);
		for (HesabMoeenTemplateEntity hesabMoeenTemplateEntity : dataList) {
			HesabMoeenEntity hesabMoeen = loadHesabMoeenByTemplate(hesabMoeenTemplateEntity, activeSaalMaaliEntity);
			if(hesabMoeen == null){
				try{
					createHesabMoeen(activeSaalMaaliEntity, hesabMoeenTemplateEntity, currentOrgan);
				}catch(DuplicateException e){
					System.out.println(e.getMessage());
					//e.printStackTrace();
				}
			}else{
				if(hesabMoeen.getHesabKol() == null){
					HesabKolEntity hesabKolEntity = getHesabKolService().loadHesabKolByTemplate(hesabMoeenTemplateEntity.getHesabKolTemplate(), activeSaalMaaliEntity);
					hesabMoeen.setHesabKol(hesabKolEntity);
				}
			}
		}
		
	}

	@Transactional
	public HesabMoeenEntity createHesabMoeen(SaalMaaliEntity activeSaalMaaliEntity,
			HesabMoeenTemplateEntity hesabMoeenTemplateEntity, OrganEntity currentOrgan) {
		HesabMoeenEntity hesabMoeenEntity = loadHesabMoeenByTemplate(hesabMoeenTemplateEntity, activeSaalMaaliEntity);
		if(hesabMoeenEntity == null){
			hesabMoeenEntity = populateHesabMoeen(activeSaalMaaliEntity,
					hesabMoeenTemplateEntity, currentOrgan);
			
			save(hesabMoeenEntity, activeSaalMaaliEntity, currentOrgan);
		}
		return hesabMoeenEntity;
	}
	
	@Transactional
	public HesabMoeenEntity createHesabMoeen(SaalMaaliEntity destSaalMaali,	HesabMoeenEntity srcHesabMoeenEntity, OrganEntity organ) {
		HesabMoeenEntity hesabMoeenEntity = loadHesabMoeenByCode(srcHesabMoeenEntity.getCode(), destSaalMaali, FlushMode.MANUAL, organ);
		if(hesabMoeenEntity == null)
			hesabMoeenEntity = loadHesabMoeenByName(srcHesabMoeenEntity.getName(),destSaalMaali, FlushMode.MANUAL, organ);
		if(hesabMoeenEntity == null){
			hesabMoeenEntity = populateHesabMoeen(destSaalMaali,srcHesabMoeenEntity, organ);
			save(hesabMoeenEntity, destSaalMaali, organ);
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
			HesabMoeenTemplateEntity hesabMoeenTemplateEntity, OrganEntity organ) {
		HesabMoeenEntity hesabMoeenEntity;
		hesabMoeenEntity = new HesabMoeenEntity();
		hesabMoeenEntity.setCode(hesabMoeenTemplateEntity.getCode());
		hesabMoeenEntity.setDescription(hesabMoeenTemplateEntity.getDescription());
		HesabKolEntity hesabKolEntity = null;
		
		HesabKolTemplateEntity hesabKolTemplate = hesabMoeenTemplateEntity.getHesabKolTemplate();
		if(hesabKolTemplate!=null)
			hesabKolEntity = getHesabKolService().loadHesabKolByCode(hesabKolTemplate.getCode(), destSaalMaaliEntity);
		
		if(hesabKolEntity == null && hesabKolTemplate!=null)
			hesabKolEntity = getHesabKolService().createHesabKol(destSaalMaaliEntity, hesabKolTemplate, organ);
		hesabMoeenEntity.setHesabKol(hesabKolEntity);
		hesabMoeenEntity.setHesabMoeenTemplate(hesabMoeenTemplateEntity);
		hesabMoeenEntity.setHidden(hesabMoeenTemplateEntity.getHidden());
		hesabMoeenEntity.setName(hesabMoeenTemplateEntity.getName());
		hesabMoeenEntity.setOrgan(new OrganEntity(destSaalMaaliEntity.getOrgan().getId()));
		hesabMoeenEntity.setSaalMaali(destSaalMaaliEntity);
		hesabMoeenEntity.setScope(hesabMoeenTemplateEntity.getScope());
		
		hesabMoeenEntity.setBedehkar(0d);
		hesabMoeenEntity.setBestankr(0d);
		hesabMoeenEntity.setMoeenTafsili(new HashSet<MoeenTafsiliEntity>());
		return hesabMoeenEntity;
	}
	
	private HesabMoeenEntity populateHesabMoeen(SaalMaaliEntity destSaalMaaliEntity, HesabMoeenEntity srcHesabMoeenEntity, OrganEntity organ) {
		HesabMoeenEntity hesabMoeenEntity;
		hesabMoeenEntity = new HesabMoeenEntity();
		hesabMoeenEntity.setCode(srcHesabMoeenEntity.getCode());
		hesabMoeenEntity.setDescription(srcHesabMoeenEntity.getDescription());
		HesabKolEntity hesabKolEntity = getHesabKolService().loadHesabKolByCode(srcHesabMoeenEntity.getHesabKol().getCode(), destSaalMaaliEntity);
		if(hesabKolEntity == null)
			hesabKolEntity = getHesabKolService().createHesabKolStateLess(destSaalMaaliEntity, srcHesabMoeenEntity.getHesabKol());
		hesabMoeenEntity.setHesabKol(hesabKolEntity);
		hesabMoeenEntity.setHesabMoeenTemplate(srcHesabMoeenEntity.getHesabMoeenTemplate());
		hesabMoeenEntity.setHidden(srcHesabMoeenEntity.getHidden());
		hesabMoeenEntity.setName(srcHesabMoeenEntity.getName());
		hesabMoeenEntity.setOrgan(organ);
		hesabMoeenEntity.setSaalMaali(destSaalMaaliEntity);
		hesabMoeenEntity.setScope(srcHesabMoeenEntity.getScope());
		
		hesabMoeenEntity.setBedehkar(0d);
		hesabMoeenEntity.setBestankr(0d);
		hesabMoeenEntity.setMoeenTafsili(new HashSet<MoeenTafsiliEntity>());
		return hesabMoeenEntity;
	}
	
	public HesabMoeenEntity loadHesabMoeenByTemplate(HesabMoeenTemplateEntity moeenTemplateEntity, SaalMaaliEntity activeSaalMaaliEntity){
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("hesabMoeenTemplate.id@eq",moeenTemplateEntity.getId());
		localFilter.put("organ.id@eq",activeSaalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq",activeSaalMaaliEntity.getId());
		HesabMoeenEntity hesabMoeenEntity = load(null, localFilter );
		return hesabMoeenEntity;
	}
	
	public HesabMoeenEntity loadHesabMoeenByCode(String code, SaalMaaliEntity saalMaaliEntity){
		return loadHesabMoeenByCode(code, saalMaaliEntity, FlushMode.MANUAL);
	}

	public HesabMoeenEntity loadHesabMoeenByCode(String code, SaalMaaliEntity saalMaaliEntity, FlushMode flushModel){
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq",code);
//		localFilter.put("organ.id@eq",saalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq",saalMaaliEntity.getId());
		HesabMoeenEntity hesabMoeenEntity = load(null, localFilter, flushModel);
		return hesabMoeenEntity;
	}
	
	public HesabMoeenEntity loadHesabMoeenByCode(String code, SaalMaaliEntity saalMaaliEntity, FlushMode flushModel, OrganEntity organ){
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq",code);
		localFilter.put("organ.id@eq",organ.getId());
		localFilter.put("saalMaali.id@eq",saalMaaliEntity.getId());
		HesabMoeenEntity hesabMoeenEntity = load(null, localFilter, flushModel);
		return hesabMoeenEntity;
	}
	
	public HesabMoeenEntity loadHesabMoeenByName(String name, SaalMaaliEntity saalMaaliEntity, FlushMode flushModel){
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq",name);
//		localFilter.put("organ.id@eq",saalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq",saalMaaliEntity.getId());
		HesabMoeenEntity hesabMoeenEntity = load(null, localFilter, flushModel);
		return hesabMoeenEntity;
	}
	
	public HesabMoeenEntity loadHesabMoeenByName(String name, SaalMaaliEntity saalMaaliEntity, FlushMode flushModel, OrganEntity organ){
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq",name);
		localFilter.put("organ.id@eq",organ.getId());
		localFilter.put("saalMaali.id@eq",saalMaaliEntity.getId());
		HesabMoeenEntity hesabMoeenEntity = load(null, localFilter, flushModel);
		return hesabMoeenEntity;
	}
	

	@Transactional(readOnly=false)
	public void disableHesab(Long hesabId){
		HesabMoeenEntity hesabMoeenEntity = load(hesabId);
		hesabMoeenEntity.setHidden(true);
		save(hesabMoeenEntity);
	}
	
	@Override
	public void save(HesabMoeenEntity entity) {
		if(!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("HesabMoeen_code"));

		super.save(entity);
	}
	


	@Transactional(readOnly=true)
	public List<HesabMoeenEntity> getRootHesabs(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan){
		
		List<HesabMoeenEntity> rootList = new ArrayList<HesabMoeenEntity>();
		Set<HesabMoeenEntity> removeList = new HashSet<HesabMoeenEntity>();
		
		List<HesabMoeenEntity> hesabMoeenList = getActiveMoeens(saalMaaliEntity);
		
		Map<String, Object> moeenTafsiliFilter = new HashMap<String, Object>();
		moeenTafsiliFilter.put("hesabMoeen.saalMaali.id@eq", saalMaaliEntity.getId());
		List<MoeenTafsiliEntity> moeenTafsiliList = getMoeenTafsiliService().getDataList(null, moeenTafsiliFilter);
		
		for (MoeenTafsiliEntity moeenTafsiliEntity : moeenTafsiliList) {
			HesabMoeenEntity hesabMoeenEntity = moeenTafsiliEntity.getHesabMoeen();
			if(hesabMoeenList.contains(hesabMoeenEntity))
				removeList.add(hesabMoeenEntity);
		}
		
		rootList.addAll(hesabMoeenList);
		rootList.removeAll(removeList);
		return rootList;
	}

}