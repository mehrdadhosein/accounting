package ir.serajsamaneh.accounting.hesabmoeen;

import ir.serajsamaneh.accounting.enumeration.HesabScopeEnum;
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
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.exception.DuplicateException;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.exception.FieldMustContainOnlyNumbersException;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

	//@Override
	@Transactional
	public void save(HesabMoeenEntity entity,SaalMaaliEntity activeSaalMaaliEntity) {
		commonSave(entity, activeSaalMaaliEntity);
		save(entity);
		boolean isNew=(entity.getID()!=null?false:true);
		logAction(isNew, entity);
	}

//	public void saveStateLess(HesabMoeenEntity entity,SaalMaaliEntity activeSaalMaaliEntity) {
//		commonSave(entity, activeSaalMaaliEntity);
//		super.saveStateLess(entity);
//	}

	@Transactional
	private void commonSave(HesabMoeenEntity entity,
			SaalMaaliEntity activeSaalMaaliEntity) {

		if(entity.getId()!=null && entity.getSaalMaali()!=null && entity.getSaalMaali().getId()!=null && !entity.getSaalMaali().equals(activeSaalMaaliEntity))
			throw new FatalException(SerajMessageUtil.getMessage("SaalMaali_hesabConflict"));

		if (entity.getId() == null) {
			entity.setBedehkar(0d);
			entity.setBestankr(0d);
		}
		if(entity.getSaalMaali() == null || entity.getSaalMaali().getId() == null)
			entity.setSaalMaali(activeSaalMaaliEntity);
		
		if (!StringUtils.hasText(entity.getCode())) {
			
			entity.setCode(generateHesabCode(entity));
		}
		if(entity.getHidden() == null)
			entity.setHidden(Boolean.FALSE);
		checkHesabUniqueNess(entity, activeSaalMaaliEntity);
		
		createHesabMoeenTemplateFromHesabMoeen(entity, activeSaalMaaliEntity.getOrgan());
	}

	@Transactional
	public void createHesabMoeenTemplateFromHesabMoeen(HesabMoeenEntity entity,OrganEntity organEntity) {

		
		HesabMoeenTemplateEntity hesabMoeenTemplateEntity = getHesabMoeenTemplateService().loadByCode(entity.getCode(), organEntity);
		if(hesabMoeenTemplateEntity == null){
			hesabMoeenTemplateEntity = getHesabMoeenTemplateService().loadByName(entity.getName(), organEntity);
			if(hesabMoeenTemplateEntity!=null)
				throw new FatalException(SerajMessageUtil.getMessage("HesabMoeenTemplate_cantCreateHesabMoeenTemplateWithDuplicateNameAndnewCode", entity.getCode(),entity.getName()));

			HesabKolEntity hesabKolEntity = getHesabKolService().load(entity.getHesabKol().getID());
			getHesabMoeenTemplateService().createHesabMoeenTemplate(entity.getCode(), entity.getName(), hesabKolEntity.getCode(), organEntity, false);

		}

		
//		HesabMoeenTemplateEntity hesabMoeenTemplateEntity = getHesabMoeenTemplateService().load(entity.getCode(), organEntity);
//		if(hesabMoeenTemplateEntity == null){
//			HesabKolEntity hesabKolEntity = getHesabKolService().load(entity.getHesabKol().getID());
//			getHesabMoeenTemplateService().createHesabMoeenTemplate(entity.getCode(), entity.getName(), hesabKolEntity.getCode(), organEntity, false);
//		}
		
		entity.setHesabMoeenTemplate(hesabMoeenTemplateEntity);
		save(entity);
	}

	private void checkHesabUniqueNess(HesabMoeenEntity entity,	SaalMaaliEntity activeSaalMaaliEntity) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eq", activeSaalMaaliEntity.getOrgan().getId());
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
	
	static Long maxKalaCode = null;
	private synchronized String generateHesabCode(HesabMoeenEntity entity) {
		String maxHierArchicalHesabMoeenCode = getMyDAO().getMaxHesabMoeenCode(
					entity.getHesabKol());
		return maxHierArchicalHesabMoeenCode;
	}
	
	@Override
	public String getDifferences(HesabMoeenEntity entity
			) {
		String diffes = "";
		HesabMoeenEntity oldEntity=(HesabMoeenEntity) entity.getOldEntity();
		if (entity.getCode() != null
				&& !entity.getCode().equals(oldEntity.getCode()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabMoeen" + "_"
							+ entity.PROP_CODE) + " : " + oldEntity.getCode()
					+ "" + " --> " + entity.getCode() + "" + "]";

		if (entity.getName() != null
				&& !entity.getName().equals(oldEntity.getName()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabMoeen" + "_"
							+ entity.PROP_NAME) + " : " + oldEntity.getName()
					+ "" + " --> " + entity.getName() + "" + "]";

		if (entity.getDescription() != null
				&& !entity.getDescription().equals(oldEntity.getDescription()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabMoeen" + "_"
							+ entity.PROP_DESCRIPTION) + " : "
					+ oldEntity.getDescription() + "" + " --> "
					+ entity.getDescription() + "" + "]";

		if (entity.getHidden() != null
				&& !entity.getHidden().equals(oldEntity.getHidden()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabMoeen" + "_"
							+ entity.PROP_HIDDEN) + " : "
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
		Map<String, Object> localFilter = new HashMap<String, Object>();
		List<Long> topOrganList = getTopOrgansIdList(curentOrgan);
		localFilter.put("hesabTafsili.organ.id@in", topOrganList);
		
		localFilter.put("hesabTafsili.hidden@eq", Boolean.FALSE);
		localFilter.put("hesabTafsili.saalMaali.id@eq", saalMaaliEntity.getId());
		
		localFilter.put("hesabMoeen.id@eq", hesabMoeenEntity.getId());
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
	public void importFromHesabMoeenTemplateList(SaalMaaliEntity activeSaalMaaliEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eqORorgan.id@isNull",Arrays.asList(activeSaalMaaliEntity.getOrgan().getId(), "ding"));		
		List<HesabMoeenTemplateEntity> dataList = getHesabMoeenTemplateService().getDataList(null, localFilter);
		for (HesabMoeenTemplateEntity hesabMoeenTemplateEntity : dataList) {
			HesabMoeenEntity hesabMoeen = loadHesabMoeenByTemplate(hesabMoeenTemplateEntity, activeSaalMaaliEntity);
			if(hesabMoeen == null){
				try{
					createHesabMoeen(activeSaalMaaliEntity, hesabMoeenTemplateEntity);
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
			HesabMoeenTemplateEntity hesabMoeenTemplateEntity) {
		HesabMoeenEntity hesabMoeenEntity = loadHesabMoeenByTemplate(hesabMoeenTemplateEntity, activeSaalMaaliEntity);
		if(hesabMoeenEntity == null){
			hesabMoeenEntity = populateHesabKol(activeSaalMaaliEntity,
					hesabMoeenTemplateEntity);
			
			save(hesabMoeenEntity, activeSaalMaaliEntity);
		}
		return hesabMoeenEntity;
	}
	
	@Transactional
	public HesabMoeenEntity createHesabMoeen(SaalMaaliEntity activeSaalMaaliEntity,
			HesabMoeenEntity srcHesabMoeenEntity) {
		HesabMoeenEntity hesabMoeenEntity = loadHesabMoeenByCode(srcHesabMoeenEntity.getCode(), activeSaalMaaliEntity);
		if(hesabMoeenEntity == null)
			hesabMoeenEntity = loadHesabMoeenByName(srcHesabMoeenEntity.getName(),activeSaalMaaliEntity, FlushMode.MANUAL);
		if(hesabMoeenEntity == null){
			hesabMoeenEntity = populateHesabKol(activeSaalMaaliEntity,	srcHesabMoeenEntity);
			save(hesabMoeenEntity, activeSaalMaaliEntity);
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

	private HesabMoeenEntity populateHesabKol(SaalMaaliEntity activeSaalMaaliEntity,
			HesabMoeenTemplateEntity hesabMoeenTemplateEntity) {
		HesabMoeenEntity hesabMoeenEntity;
		hesabMoeenEntity = new HesabMoeenEntity();
		hesabMoeenEntity.setCode(hesabMoeenTemplateEntity.getCode());
		hesabMoeenEntity.setDescription(hesabMoeenTemplateEntity.getDescription());
		HesabKolEntity hesabKolEntity = null;
		
		HesabKolTemplateEntity hesabKolTemplate = hesabMoeenTemplateEntity.getHesabKolTemplate();
		if(hesabKolTemplate!=null)
			hesabKolEntity = getHesabKolService().loadHesabKolByCode(hesabKolTemplate.getCode(), activeSaalMaaliEntity);
		
		if(hesabKolEntity == null)
			hesabKolEntity = getHesabKolService().createHesabKolStateLess(activeSaalMaaliEntity, hesabKolTemplate);
		hesabMoeenEntity.setHesabKol(hesabKolEntity);
		hesabMoeenEntity.setHesabMoeenTemplate(hesabMoeenTemplateEntity);
		hesabMoeenEntity.setHidden(hesabMoeenTemplateEntity.getHidden());
		hesabMoeenEntity.setName(hesabMoeenTemplateEntity.getName());
		hesabMoeenEntity.setOrgan(new OrganEntity(activeSaalMaaliEntity.getOrgan().getId()));
		hesabMoeenEntity.setSaalMaali(activeSaalMaaliEntity);
		hesabMoeenEntity.setScope(hesabMoeenTemplateEntity.getScope());
		
		hesabMoeenEntity.setBedehkar(0d);
		hesabMoeenEntity.setBestankr(0d);
		hesabMoeenEntity.setMoeenTafsili(new HashSet<MoeenTafsiliEntity>());
		return hesabMoeenEntity;
	}
	
	private HesabMoeenEntity populateHesabKol(SaalMaaliEntity activeSaalMaaliEntity,
			HesabMoeenEntity srcHesabMoeenEntity) {
		HesabMoeenEntity hesabMoeenEntity;
		hesabMoeenEntity = new HesabMoeenEntity();
		hesabMoeenEntity.setCode(srcHesabMoeenEntity.getCode());
		hesabMoeenEntity.setDescription(srcHesabMoeenEntity.getDescription());
		HesabKolEntity hesabKolEntity = getHesabKolService().loadHesabKolByCode(srcHesabMoeenEntity.getHesabKol().getCode(), activeSaalMaaliEntity);
		if(hesabKolEntity == null)
			hesabKolEntity = getHesabKolService().createHesabKolStateLess(activeSaalMaaliEntity, srcHesabMoeenEntity.getHesabKol());
		hesabMoeenEntity.setHesabKol(hesabKolEntity);
		hesabMoeenEntity.setHesabMoeenTemplate(srcHesabMoeenEntity.getHesabMoeenTemplate());
		hesabMoeenEntity.setHidden(srcHesabMoeenEntity.getHidden());
		hesabMoeenEntity.setName(srcHesabMoeenEntity.getName());
		hesabMoeenEntity.setOrgan(new OrganEntity(activeSaalMaaliEntity.getOrgan().getId()));
		hesabMoeenEntity.setSaalMaali(activeSaalMaaliEntity);
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
		List<HesabMoeenEntity> dataList = getDataList(null, localFilter );
		if(dataList.size() == 1)
			return dataList.get(0);
		if(dataList.size() == 0)
			return null;
		throw new FatalException("More Than one HesabMoeen Recore Found");
	}
	
	public HesabMoeenEntity loadHesabMoeenByCode(String code, SaalMaaliEntity saalMaaliEntity){
		return loadHesabMoeenByCode(code, saalMaaliEntity, FlushMode.MANUAL);
	}

	public HesabMoeenEntity loadHesabMoeenByCode(String code, SaalMaaliEntity saalMaaliEntity, FlushMode flushModel){
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq",code);
//		localFilter.put("organ.id@eq",saalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq",saalMaaliEntity.getId());
		List<HesabMoeenEntity> dataList = getDataList(null, localFilter, flushModel);
		if(dataList.size() == 1)
			return dataList.get(0);
		if(dataList.size() == 0)
			return null;
		throw new FatalException("More Than one HesabMoeen Recore Found");
	}
	
	public HesabMoeenEntity loadHesabMoeenByName(String name, SaalMaaliEntity saalMaaliEntity, FlushMode flushModel){
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq",name);
//		localFilter.put("organ.id@eq",saalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq",saalMaaliEntity.getId());
		List<HesabMoeenEntity> dataList = getDataList(null, localFilter, flushModel);
		if(dataList.size() == 1)
			return dataList.get(0);
		if(dataList.size() == 0)
			return null;
		throw new FatalException("More Than one HesabMoeen Recore Found");
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
}