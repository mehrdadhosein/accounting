package ir.serajsamaneh.accounting.hesabkol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.hibernate.FlushMode;
import org.springframework.transaction.annotation.Transactional;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity;
import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazService;
import ir.serajsamaneh.accounting.accountingmarkaztemplate.AccountingMarkazTemplateEntity;
import ir.serajsamaneh.accounting.accountingmarkaztemplate.AccountingMarkazTemplateService;
import ir.serajsamaneh.accounting.hesabgroup.HesabGroupEntity;
import ir.serajsamaneh.accounting.hesabgroup.HesabGroupService;
import ir.serajsamaneh.accounting.hesabgrouptemplate.HesabGroupTemplateService;
import ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateEntity;
import ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateService;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliService;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.exception.DuplicateException;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.exception.FieldMustContainOnlyNumbersException;
import ir.serajsamaneh.core.exception.NoRecordFoundException;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.erpcore.contacthesab.ContactHesabEntity;
import ir.serajsamaneh.erpcore.contacthesab.ContactHesabService;
import ir.serajsamaneh.erpcore.util.HesabTreeUtil;

public class HesabKolService extends
		BaseEntityService<HesabKolEntity, Long> {

	HesabMoeenService hesabMoeenService;
	HesabTafsiliService hesabTafsiliService;
	HesabGroupService hesabGroupService;
	HesabKolTemplateService hesabKolTemplateService;
	SaalMaaliService saalMaaliService;
	AccountingMarkazService accountingMarkazService;
	ContactHesabService contactHesabService;
	AccountingMarkazTemplateService accountingMarkazTemplateService;
	HesabTafsiliTemplateService hesabTafsiliTemplateService;
	HesabGroupTemplateService hesabGroupTemplateService;
	
	public HesabGroupTemplateService getHesabGroupTemplateService() {
		return hesabGroupTemplateService;
	}

	public void setHesabGroupTemplateService(
			HesabGroupTemplateService hesabGroupTemplateService) {
		this.hesabGroupTemplateService = hesabGroupTemplateService;
	}

	public HesabTafsiliTemplateService getHesabTafsiliTemplateService() {
		return hesabTafsiliTemplateService;
	}

	public void setHesabTafsiliTemplateService(
			HesabTafsiliTemplateService hesabTafsiliTemplateService) {
		this.hesabTafsiliTemplateService = hesabTafsiliTemplateService;
	}

	public AccountingMarkazTemplateService getAccountingMarkazTemplateService() {
		return accountingMarkazTemplateService;
	}

	public void setAccountingMarkazTemplateService(
			AccountingMarkazTemplateService accountingMarkazTemplateService) {
		this.accountingMarkazTemplateService = accountingMarkazTemplateService;
	}

	public ContactHesabService getContactHesabService() {
		return contactHesabService;
	}

	public void setContactHesabService(ContactHesabService contactHesabService) {
		this.contactHesabService = contactHesabService;
	}

	public AccountingMarkazService getAccountingMarkazService() {
		return accountingMarkazService;
	}

	public void setAccountingMarkazService(
			AccountingMarkazService accountingMarkazService) {
		this.accountingMarkazService = accountingMarkazService;
	}

	public SaalMaaliService getSaalMaaliService() {
		return saalMaaliService;
	}

	public void setSaalMaaliService(SaalMaaliService saalMaaliService) {
		this.saalMaaliService = saalMaaliService;
	}

	public HesabKolTemplateService getHesabKolTemplateService() {
		return hesabKolTemplateService;
	}

	public void setHesabKolTemplateService(
			HesabKolTemplateService hesabKolTemplateService) {
		this.hesabKolTemplateService = hesabKolTemplateService;
	}

	public HesabGroupService getHesabGroupService() {
		return hesabGroupService;
	}

	public void setHesabGroupService(HesabGroupService hesabGroupService) {
		this.hesabGroupService = hesabGroupService;
	}

	public HesabTafsiliService getHesabTafsiliService() {
		return hesabTafsiliService;
	}

	public void setHesabTafsiliService(HesabTafsiliService hesabTafsiliService) {
		this.hesabTafsiliService = hesabTafsiliService;
	}

	public HesabMoeenService getHesabMoeenService() {
		return hesabMoeenService;
	}

	public void setHesabMoeenService(HesabMoeenService hesabMoeenService) {
		this.hesabMoeenService = hesabMoeenService;
	}

	@Override
	protected HesabKolDAO getMyDAO() {
		return hesabKolDAO;
	}

	HesabKolDAO hesabKolDAO;

	public void setHesabKolDAO(HesabKolDAO hesabKolDAO) {
		this.hesabKolDAO = hesabKolDAO;
	}

	public HesabKolDAO getHesabKolDAO() {
		return hesabKolDAO;
	}

	@Override
	public String getDifferences(HesabKolEntity entity) {
		String diffes = "";
		HesabKolEntity oldEntity=(HesabKolEntity) entity.getOldEntity();
		if (entity.getCode() != null
				&& !entity.getCode().equals(oldEntity.getCode()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabKol" + "_"
							+ HesabKolEntity.PROP_CODE) + " : " + oldEntity.getCode()
					+ "" + " --> " + entity.getCode() + "" + "]";

		if (entity.getName() != null
				&& !entity.getName().equals(oldEntity.getName()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabKol" + "_"
							+ HesabKolEntity.PROP_NAME) + " : " + oldEntity.getName()
					+ "" + " --> " + entity.getName() + "" + "]";



		if (entity.getDescription() != null
				&& !entity.getDescription().equals(oldEntity.getDescription()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabKol" + "_"
							+ HesabKolEntity.PROP_DESCRIPTION) + " : "
					+ oldEntity.getDescription() + "" + " --> "
					+ entity.getDescription() + "" + "]";

		if (entity.getHidden() != null
				&& !entity.getHidden().equals(oldEntity.getHidden()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabKol" + "_"
							+ HesabKolEntity.PROP_HIDDEN) + " : "
					+ oldEntity.getHidden() + "" + " --> " + entity.getHidden()
					+ "" + "]";

		return diffes;
	}

	//@Override
	@Transactional
	public void save(HesabKolEntity entity,SaalMaaliEntity activeSaalMaaliEntity, OrganEntity currentOrgan) {
		commonSave(entity, activeSaalMaaliEntity, currentOrgan);

		save(entity);
		boolean isNew=(entity.getID()!=null?false:true);
		logAction(isNew, entity);
	}
	@Transactional
	public void saveStateLess(HesabKolEntity entity,SaalMaaliEntity activeSaalMaaliEntity, OrganEntity currentOrgan) {
		commonSave(entity, activeSaalMaaliEntity, currentOrgan);
		
		super.saveStateLess(entity);
	}
 
	private void commonSave(HesabKolEntity entity,
			SaalMaaliEntity activeSaalMaaliEntity, OrganEntity currentOrgan) {
		
		if(!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("HesabKol_code"));

		if(entity.getId()!=null && entity.getSaalMaali()!=null && entity.getSaalMaali().getId()!=null && !entity.getSaalMaali().equals(activeSaalMaaliEntity))
			throw new FatalException(SerajMessageUtil.getMessage("SaalMaali_hesabConflict"));
		
		if (entity.getId() == null) {
			entity.setBedehkar(0d);
			entity.setBestankr(0d);
		}
		if(entity.getSaalMaali() == null || entity.getSaalMaali().getId() == null)
			entity.setSaalMaali(activeSaalMaaliEntity);
		if(entity.getHidden() == null)
			entity.setHidden(Boolean.FALSE);
		checkHesabUniqueNess(entity, activeSaalMaaliEntity, currentOrgan);
		
		createHesabKolTemplateFromHesabKol(entity, currentOrgan);
		
	}

	private void createHesabKolTemplateFromHesabKol(HesabKolEntity entity, OrganEntity organEntity) {
		HesabKolTemplateEntity hesabKolTemplateEntity = getHesabKolTemplateService().loadByCodeInCurrentOrgan(entity.getCode(), organEntity);
//		if(hesabKolTemplateEntity == null){
//			HesabGroupEntity hesabGroupEntity = getHesabGroupService().load(entity.getHesabGroup().getID());
//			hesabKolTemplateEntity = getHesabKolTemplateService().createHesabKolTemplate(entity.getCode(), entity.getName(), hesabGroupEntity.getCode(), entity.getMahyatKol().name(), organEntity);
//		}else
//			hesabKolTemplateEntity.setName(entity.getName());

		if(hesabKolTemplateEntity == null){
			hesabKolTemplateEntity = getHesabKolTemplateService().loadByNameInCurrentOrgan(entity.getName(), organEntity);
			if(hesabKolTemplateEntity!=null){
				HesabKolTemplateEntity currentEntityHesabKolTemplate = entity.getHesabKolTemplate();
				
				//code of kol has changed
				if(currentEntityHesabKolTemplate.getId()!=null && currentEntityHesabKolTemplate.equals(hesabKolTemplateEntity)){
					hesabKolTemplateEntity.setCode(entity.getCode().toString());
					hesabKolTemplateEntity.setName(entity.getName());
					hesabKolTemplateEntity.setDescription(entity.getDescription());
				}else
					throw new FatalException(SerajMessageUtil.getMessage("HesabKolTemplate_cantCreateHesabKolTemplateWithDuplicateNameAndnewCode", entity.getCode(),entity.getName()));
			}else{
				HesabGroupEntity hesabGroupEntity = getHesabGroupService().load(entity.getHesabGroup().getID());
				hesabKolTemplateEntity = getHesabKolTemplateService().createHesabKolTemplate(entity.getCode(), entity.getName(), hesabGroupEntity.getCode(), entity.getMahyatKol().name(), organEntity);
			}

		}
		else{
			HesabKolTemplateEntity hesabKolTemplateEntityByName = getHesabKolTemplateService().loadByNameInCurrentOrgan(entity.getName(), organEntity);
			if(hesabKolTemplateEntityByName!=null && !hesabKolTemplateEntityByName.getId().equals(hesabKolTemplateEntity.getId()))
				throw new FatalException(SerajMessageUtil.getMessage("HesabKolTemplate_cantCreateHesabKolTemplateWithDuplicateNameAndnewCode", entity.getCode(),entity.getName()));

			hesabKolTemplateEntity.setCode(entity.getCode().toString());
			hesabKolTemplateEntity.setName(entity.getName());
			hesabKolTemplateEntity.setDescription(entity.getDescription());
		}
		
		getHesabKolTemplateService().save(hesabKolTemplateEntity);
		entity.setHesabKolTemplate(hesabKolTemplateEntity);
		save(entity);
	}

	private void checkHesabUniqueNess(HesabKolEntity entity,
			SaalMaaliEntity activeSaalMaaliEntity, OrganEntity currentOrgan) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
//		localFilter.put("organ.id@eq", activeSaalMaaliEntity.getOrgan().getId());
		
		List<Long> topOrganList = getTopOrgansIdList(currentOrgan);
		localFilter.put("organ.id@in", topOrganList);
		
		localFilter.put("saalMaali.id@eq", activeSaalMaaliEntity.getId());
		checkUniqueNess(entity, HesabKolEntity.PROP_NAME, entity.getName(),
				localFilter, false);
		checkUniqueNess(entity, HesabKolEntity.PROP_CODE, entity.getCode(),
				localFilter, false);
	}
	
	@Transactional
	public void updateValues(HesabKolEntity entity) {
		save(entity);
	}
	
	@Override
	public void save(HesabKolEntity entity) {
		if(!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException("("+entity.getName()+" : "+ SerajMessageUtil.getMessage("HesabKol_code")+")");
		super.save(entity);
	}
//	public HesabKolEntity getHesabKolByCode(String hesabCode, OrganEntity organEntity) {
//
//		Map<String, Object> localFilter = new HashMap<String, Object>();
//		localFilter.put("code@eq", hesabCode);
//		localFilter.put("organ.id@eq", organEntity.getId());
//		List<HesabKolEntity> dataList = getDataList(null, localFilter,
//				FlushMode.MANUAL);
//		if (dataList.size() == 1)
//			return dataList.get(0);
//		else if (dataList.size() == 0)
//			return null;
//		else
//			throw new IllegalStateException();
//	}


	
	@Override
	public void saveOrUpdate(HesabKolEntity entity) {
		if(entity.getHidden() == null)
			entity.setHidden(false);
		super.saveOrUpdate(entity);
	}
	
	@Override
	public void saveOrUpdateStateLess(HesabKolEntity entity) {
		if(entity.getHidden() == null)
			entity.setHidden(false);
		super.saveOrUpdateStateLess(entity);
	}
	
	@Transactional(readOnly=false)
	public void importFromHesabKolTemplateList(SaalMaaliEntity activeSaalMaaliEntity, OrganEntity currentOrgan) {
		
//		getHesabKolTemplateService().createDefaultAccounts(activeSaalMaaliEntity.getOrgan());
		
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eq",activeSaalMaaliEntity.getOrgan().getId());
		List<HesabKolTemplateEntity> dataList = getHesabKolTemplateService().getDataList(null, localFilter);
		
		for (HesabKolTemplateEntity hesabKolTemplateEntity : dataList) {
			HesabKolEntity loadHesabKol = loadHesabKolByTemplate(hesabKolTemplateEntity, activeSaalMaaliEntity);
			if(loadHesabKol == null){
				try{
					createHesabKol(activeSaalMaaliEntity, hesabKolTemplateEntity, currentOrgan);
				}catch(DuplicateException e){
					e.printStackTrace();
				}
			}
		}
		
	}

	public HesabKolEntity createHesabKol(SaalMaaliEntity activeSaalMaaliEntity,
			HesabKolTemplateEntity hesabKolTemplateEntity, OrganEntity currentOrgan) {
		HesabKolEntity hesabKolEntity = loadHesabKolByTemplate(hesabKolTemplateEntity, activeSaalMaaliEntity);
		if(hesabKolEntity == null){
			hesabKolEntity = populateHesabKolEntity(activeSaalMaaliEntity, hesabKolTemplateEntity);
			save(hesabKolEntity, activeSaalMaaliEntity, currentOrgan);
		}
		return hesabKolEntity;
	}
	
	public HesabKolEntity createHesabKol(SaalMaaliEntity destSaalMaaliEntity,	HesabKolEntity srcHesabKolEntity) {
		HesabKolEntity hesabKolEntity = loadHesabKolByCode(srcHesabKolEntity.getCode(), destSaalMaaliEntity);
		if(hesabKolEntity == null)
			hesabKolEntity = loadHesabKolByName(srcHesabKolEntity.getName(), destSaalMaaliEntity, FlushMode.MANUAL);
		if(hesabKolEntity == null){
			hesabKolEntity = populateHesabKolEntity(destSaalMaaliEntity, srcHesabKolEntity, srcHesabKolEntity.getOrgan());
			save(hesabKolEntity, destSaalMaaliEntity, srcHesabKolEntity.getOrgan());
		}
		return hesabKolEntity;
	}
	
	public HesabKolEntity createHesabKolStateLess(SaalMaaliEntity activeSaalMaaliEntity,
			HesabKolTemplateEntity hesabKolTemplateEntity, OrganEntity currentOrgan) {
		HesabKolEntity hesabKolEntity = loadHesabKolByTemplate(hesabKolTemplateEntity, activeSaalMaaliEntity);
		if(hesabKolEntity == null){
			hesabKolEntity = populateHesabKolEntity(activeSaalMaaliEntity, hesabKolTemplateEntity);
			saveStateLess(hesabKolEntity, activeSaalMaaliEntity,currentOrgan);
		}
		return hesabKolEntity;
	}
	
	public HesabKolEntity createHesabKolStateLess(SaalMaaliEntity destSaalMaaliEntity, HesabKolEntity srcHesabKolEntity) {
		HesabKolEntity hesabKolEntity = loadHesabKolByCode(srcHesabKolEntity.getCode(), destSaalMaaliEntity);
		if(hesabKolEntity == null)
			hesabKolEntity = loadHesabKolByName(srcHesabKolEntity.getName(), destSaalMaaliEntity, FlushMode.MANUAL);

		if(hesabKolEntity == null){
			hesabKolEntity = populateHesabKolEntity(destSaalMaaliEntity, srcHesabKolEntity, srcHesabKolEntity.getOrgan());
			saveStateLess(hesabKolEntity, destSaalMaaliEntity, srcHesabKolEntity.getOrgan());
		}
		return hesabKolEntity;
	}

	private HesabKolEntity populateHesabKolEntity(SaalMaaliEntity destSaalMaaliEntity,
			HesabKolEntity srcHesabKolEntity, OrganEntity organ) {
		HesabKolEntity hesabKolEntity;
		hesabKolEntity = new HesabKolEntity();
		hesabKolEntity.setHesabGroup(srcHesabKolEntity.getHesabGroup());
		hesabKolEntity.setCode(srcHesabKolEntity.getCode());
		hesabKolEntity.setDescription(srcHesabKolEntity.getDescription());
		hesabKolEntity.setHidden(srcHesabKolEntity.getHidden());
		hesabKolEntity.setMahyatKol(srcHesabKolEntity.getMahyatKol());
		hesabKolEntity.setName(srcHesabKolEntity.getName());
		hesabKolEntity.setOrgan(organ);
		hesabKolEntity.setSaalMaali(destSaalMaaliEntity);
		hesabKolEntity.setBedehkar(0d);
		hesabKolEntity.setBestankr(0d);
		hesabKolEntity.setHesabKolTemplate(srcHesabKolEntity.getHesabKolTemplate());
		return hesabKolEntity;
	}
	
	private HesabKolEntity populateHesabKolEntity(SaalMaaliEntity activeSaalMaaliEntity,
			HesabKolTemplateEntity  hesabKolTemplateEntity) {
		HesabKolEntity hesabKolEntity;
		hesabKolEntity = new HesabKolEntity();
		if(hesabKolTemplateEntity.getHesabGroupTemplate()!=null)
			hesabKolEntity.setHesabGroup(getHesabGroupService().loadHesabByCode(hesabKolTemplateEntity.getHesabGroupTemplate().getCode(),activeSaalMaaliEntity));
		
		hesabKolEntity.setCode(hesabKolTemplateEntity.getCode());
		hesabKolEntity.setDescription(hesabKolTemplateEntity.getDescription());
		hesabKolEntity.setHidden(hesabKolTemplateEntity.getHidden());
		hesabKolEntity.setMahyatKol(hesabKolTemplateEntity.getMahyatKol());
		hesabKolEntity.setName(hesabKolTemplateEntity.getName());
		hesabKolEntity.setOrgan(new OrganEntity(activeSaalMaaliEntity.getOrgan().getId()));
		hesabKolEntity.setSaalMaali(activeSaalMaaliEntity);
		hesabKolEntity.setBedehkar(0d);
		hesabKolEntity.setBestankr(0d);
		hesabKolEntity.setHesabKolTemplate(hesabKolTemplateEntity);
		return hesabKolEntity;
	}
	
	public HesabKolEntity loadHesabKolByTemplate(HesabKolTemplateEntity hesabKolTemplateEntity, SaalMaaliEntity activeSaalMaaliEntity){
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("hesabKolTemplate.id@eq",hesabKolTemplateEntity.getId());
//		localFilter.put("organ.id@eq",activeSaalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq",activeSaalMaaliEntity.getId());
		HesabKolEntity hesabKolEntity = load(null, localFilter );
		return hesabKolEntity;
	}
	
	public HesabKolEntity loadHesabKolByCode(String code, SaalMaaliEntity saalMaaliEntity){
		return loadHesabKolByCode(code, saalMaaliEntity, FlushMode.MANUAL);
	}
	public HesabKolEntity loadHesabKolByCode(String code, SaalMaaliEntity saalMaaliEntity, FlushMode flushMode){
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq",code);
//		localFilter.put("organ.id@eq",saalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq",saalMaaliEntity.getId());
		return load(null, localFilter, flushMode);
//		List<HesabKolEntity> dataList = getDataList(null, localFilter, flushMode);
//		if(dataList.size() == 1)
//			return dataList.get(0);
//		if(dataList.size() == 0)
//			return null;
//		throw new FatalException("More Than one Record Found");
	}

	public HesabKolEntity loadHesabKolByName(String name, SaalMaaliEntity saalMaaliEntity, FlushMode flushMode){
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq",name);
//		localFilter.put("organ.id@eq",saalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq",saalMaaliEntity.getId());
		return load(null, localFilter, flushMode);
//		List<HesabKolEntity> dataList = getDataList(null, localFilter, flushMode);
//		if(dataList.size() == 1)
//			return dataList.get(0);
//		if(dataList.size() == 0)
//			return null;
//		throw new FatalException("More Than one Record Found");
	}
	
	public List<HesabVO> createHesabHierarchy(OrganEntity organEntity, SaalMaaliEntity activeSaalMaaliEntity) {
		List<HesabKolEntity> hesabKolList = getHesabKolList(activeSaalMaaliEntity, organEntity);
		
		List<HesabVO> hesabVOs = new ArrayList<HesabVO>();
		for (HesabKolEntity hesabKolEntity : hesabKolList) {
			HesabVO hesabKolVO = new HesabVO(hesabKolEntity, HesabKolEntity.class.getSimpleName(), "folder_vector16.png");
			List<HesabMoeenEntity> activeMoeens = getHesabMoeenService().getActiveMoeens(hesabKolEntity.getId(), activeSaalMaaliEntity, organEntity);
			HesabTreeUtil.addHesabMoeensToHesabHierarchy(hesabKolVO, activeMoeens, hesabVOs, activeSaalMaaliEntity, organEntity);
			hesabVOs.add(hesabKolVO);
		}
		return hesabVOs;
	}

	public List<HesabKolEntity> getHesabKolList(SaalMaaliEntity saalMaaliEntity, OrganEntity curentOrgan) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		
		List<Long> topOrganList = getTopOrgansIdList(curentOrgan);
		localFilter.put("organ.id@in", topOrganList);
		
		localFilter.put("hidden@eq",Boolean.FALSE);
		localFilter.put("saalMaali.id@eq",saalMaaliEntity.getId());
		List<HesabKolEntity> hesabKolList = getDataList(null, localFilter, HesabKolEntity.PROP_CODE, true,false);
		return hesabKolList;
	}

	public List<HesabKolEntity> getHesabKolList(SaalMaaliEntity saalMaaliEntity) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		
		localFilter.put("hidden@eq",Boolean.FALSE);
		localFilter.put("saalMaali.id@eq",saalMaaliEntity.getId());
		List<HesabKolEntity> hesabKolList = getDataList(null, localFilter, HesabKolEntity.PROP_CODE, true,false);
		return hesabKolList;
	}
	


	
	@Transactional(readOnly=false)
	public void disableHesab(Long hesabId){
		HesabKolEntity hesabKolEntity = load(hesabId);
		hesabKolEntity.setHidden(true);
		super.save(hesabKolEntity);
	}

	@Transactional(readOnly=false)
	public void copyAccountingMarkazhaFromSourceSaalMaaliToDestSaalMaali(SaalMaaliEntity srcSaalMaali, SaalMaaliEntity destSaalMaali){
		List<AccountingMarkazEntity> srcActiveAccountingMarkaz = getAccountingMarkazService().getActiveAccountingMarkaz(srcSaalMaali);
		for (AccountingMarkazEntity srcAccountingMarkazEntity : srcActiveAccountingMarkaz) {
			AccountingMarkazEntity destAccountingMarkazEntity = getAccountingMarkazService().loadAccountingMarkazByCode(srcAccountingMarkazEntity.getCode(), destSaalMaali,FlushMode.ALWAYS);
			if(destAccountingMarkazEntity == null || destAccountingMarkazEntity.getId() == null){
				destAccountingMarkazEntity = getAccountingMarkazService().createAccountingMarkaz(destSaalMaali, srcAccountingMarkazEntity);
			}

			AccountingMarkazTemplateEntity accountingMarkazTemplateEntity = getAccountingMarkazTemplateService().load(destAccountingMarkazEntity.getCode(), destSaalMaali.getOrgan());
			if(accountingMarkazTemplateEntity == null)
				accountingMarkazTemplateEntity = getAccountingMarkazTemplateService().createAccountingMarkazTemplate(destAccountingMarkazEntity.getCode(), destAccountingMarkazEntity.getName(), destSaalMaali.getOrgan());
		}
		
		for (AccountingMarkazEntity srcAccountingMarkazEntity : srcActiveAccountingMarkaz) {
			AccountingMarkazEntity destAccountingMarkazEntity = getAccountingMarkazService().loadAccountingMarkazByCode(srcAccountingMarkazEntity.getCode(), destSaalMaali,FlushMode.ALWAYS);
			getAccountingMarkazService().createAccountingMarkazRelatedEntities(srcAccountingMarkazEntity, destAccountingMarkazEntity, destSaalMaali);
		}

	}
	@Transactional(readOnly=false)
	public void copycontactHesabsFromSourceSaalMaaliToDestSaalMaali(SaalMaaliEntity srcSaalMaali, SaalMaaliEntity destSaalMaali){
		
		
		List<ContactHesabEntity> contactHesabList = getContactHesabService().getListBySaalMaali(srcSaalMaali);
		for (ContactHesabEntity contactHesabEntity : contactHesabList) {
			try{
				getContactHesabService().getContactHesabByContactIdAndSaalMaali(contactHesabEntity.getContact().getId(), destSaalMaali);
			}catch(NoRecordFoundException e){
					
					HesabMoeenEntity destHsabMoeenEntity = null;
					HesabTafsiliEntity destHesabTafsiliEntity = null;
					
					if(contactHesabEntity.getHesabMoeen()!=null && contactHesabEntity.getHesabMoeen().getId()!=null)
						destHsabMoeenEntity = getHesabMoeenService().getHesabMoeenByCodeAndSaalMaali(contactHesabEntity.getHesabMoeen().getCode(), destSaalMaali);
					if(contactHesabEntity.getHesabTafsiliOne()!=null && contactHesabEntity.getHesabTafsiliOne().getId()!=null)
						destHesabTafsiliEntity = getHesabTafsiliService().getHesabTafsiliByCodeAndSaalMaali(contactHesabEntity.getHesabTafsiliOne().getCode(), destSaalMaali);
					
					getContactHesabService().createContactHesab(contactHesabEntity.getAccountingMarkazTemplate(), contactHesabEntity.getContact(), destHsabMoeenEntity, destHesabTafsiliEntity, destSaalMaali);
			}
		}
	}

	public void copyHesabTafsiliRelatedEntities(SaalMaaliEntity srcSaalMaali, SaalMaaliEntity destSaalMaali) {
		List<HesabTafsiliEntity> srcActiveTafsilis = getHesabTafsiliService().getActiveTafsilis(srcSaalMaali);
		for (HesabTafsiliEntity srcHesabTafsiliEntity : srcActiveTafsilis) {
			HesabTafsiliEntity destHesabTafsiliEntity = getHesabTafsiliService().loadHesabTafsiliByCode(srcHesabTafsiliEntity.getCode(), destSaalMaali, FlushMode.MANUAL, srcHesabTafsiliEntity.getOrgan());
			getHesabTafsiliService().copyHesabTafsiliRelatedEntities(srcHesabTafsiliEntity, destHesabTafsiliEntity, destSaalMaali);
			
		}
	}

	@Transactional(readOnly=false)
	public void copyHesabTafsilissFromSourceSaalMaaliToDestSaalMaali(SaalMaaliEntity srcSaalMaali, SaalMaaliEntity destSaalMaali) {
		List<HesabTafsiliEntity> srcActiveTafsilis = getHesabTafsiliService().getActiveTafsilis(srcSaalMaali);
		for (HesabTafsiliEntity srcHesabTafsiliEntity : srcActiveTafsilis) {

			HesabTafsiliEntity destHesabTafsiliEntity = getHesabTafsiliService().loadHesabTafsiliByCode(srcHesabTafsiliEntity.getCode(), destSaalMaali,FlushMode.ALWAYS, srcHesabTafsiliEntity.getOrgan());
			
			if(destHesabTafsiliEntity == null){
				destHesabTafsiliEntity = getHesabTafsiliService().loadHesabTafsiliByName(srcHesabTafsiliEntity.getName(), destSaalMaali,FlushMode.ALWAYS, srcHesabTafsiliEntity.getOrgan());
				if(destHesabTafsiliEntity!=null)
					throw new FatalException(SerajMessageUtil.getMessage("HesabTafsili_cantImportHesabWithDuplicateNameAndnewCode", srcHesabTafsiliEntity.getCode(),destHesabTafsiliEntity.getDesc(), srcHesabTafsiliEntity.getOrgan()));				

			}
			
			if(destHesabTafsiliEntity == null || destHesabTafsiliEntity.getId() == null){
//				try{ 
					destHesabTafsiliEntity = getHesabTafsiliService().createHesabTafsili(destSaalMaali, srcHesabTafsiliEntity, srcHesabTafsiliEntity.getOrgan());
//				}catch(DuplicateException e){
//					System.out.println(e.getDesc());
//					continue;//not important exception
//				}
			}else
				getHesabTafsiliService().createOrUpdateRelatedHesabTafsiliTemplate(srcHesabTafsiliEntity, srcHesabTafsiliEntity.getOrgan());
			
		}
	}



	@Transactional(readOnly=false)
	public void copyHesabKolsFromSourceSaalMaaliToDestSaalMaali(SaalMaaliEntity srcSaalMaali, SaalMaaliEntity destSaalMaali) {
		List<HesabKolEntity> hesabKolList = getHesabKolList(srcSaalMaali);
		for (HesabKolEntity srcHesabKolEntity : hesabKolList) {
			
			HesabKolEntity destHesabKol = loadHesabKolByCode(srcHesabKolEntity.getCode(), destSaalMaali, FlushMode.MANUAL);
			if(destHesabKol == null){
				destHesabKol = loadHesabKolByName(srcHesabKolEntity.getName(), destSaalMaali, FlushMode.MANUAL);
				if(destHesabKol!=null)
					throw new FatalException(SerajMessageUtil.getMessage("HesabKol_cantImportHesabWithDuplicateNameAndnewCode", srcHesabKolEntity.getCode(),destHesabKol.getName()));				

			}
			
			if(destHesabKol == null){
				try{
//					destHesabKol = createHesabKolStateLess(destSaalMaali, srcHesabKolEntity); 
					destHesabKol = createHesabKol(destSaalMaali, srcHesabKolEntity); 
				}catch(DuplicateException e){
					System.out.println(e.getDesc());
					continue;
				}
			}	
			
			createHesabKolTemplateFromHesabKol(srcHesabKolEntity, srcSaalMaali.getOrgan());
		}
	}

	
	public void copyHesabMoeensFromSourceSaalMaaliToDestSaalMaali(SaalMaaliEntity srcSaalMaali,	SaalMaaliEntity destSaalMaali) {
		List<HesabMoeenEntity> activeMoeens = getHesabMoeenService().getActiveMoeens(srcSaalMaali);
		for (HesabMoeenEntity srcHesabMoeenEntity : activeMoeens) {
			
			HesabMoeenEntity destHesabMoeenEntity = getHesabMoeenService().loadHesabMoeenByCode(srcHesabMoeenEntity.getCode(),destSaalMaali, FlushMode.MANUAL, srcHesabMoeenEntity.getOrgan());
			if(destHesabMoeenEntity == null){
				destHesabMoeenEntity = getHesabMoeenService().loadHesabMoeenByName(srcHesabMoeenEntity.getName(),destSaalMaali, FlushMode.MANUAL, srcHesabMoeenEntity.getOrgan());
				if(destHesabMoeenEntity!=null)
					throw new FatalException(SerajMessageUtil.getMessage("HesabMoeen_cantImportHesabWithDuplicateNameAndnewCode", srcHesabMoeenEntity.getCode(),destHesabMoeenEntity.getName()));				
			}
			
			if(destHesabMoeenEntity == null || destHesabMoeenEntity.getId() == null){
//				try{
					destHesabMoeenEntity = getHesabMoeenService().createHesabMoeen(destSaalMaali, srcHesabMoeenEntity, srcHesabMoeenEntity.getOrgan());
//				}catch(DuplicateException e){
//					System.out.println(e.getDesc());
//					continue;
//				}
			}
			getHesabMoeenService().createOrUpdateRelatedHesabMoeenTemplate(srcHesabMoeenEntity, srcHesabMoeenEntity.getOrgan());
		}
	}

	@Transactional(readOnly=true)
	public List<ListOrderedMap<String, Object>> getRootHesabs(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan) {
		List<ListOrderedMap<String, Object>> rootHesabsList;
		List<HesabMoeenEntity> rootHesabMoeens = getHesabMoeenService().getRootHesabs(saalMaaliEntity, currentOrgan);
		
		rootHesabsList = new ArrayList<ListOrderedMap<String, Object>>(); 
		
		for (HesabMoeenEntity hesabMoeenEntity : rootHesabMoeens) {
			ListOrderedMap<String, Object> hesabMoeenMap = new ListOrderedMap<String, Object>();
			hesabMoeenMap.put("value","Moeen_"+hesabMoeenEntity.getId());
			hesabMoeenMap.put("label",hesabMoeenEntity.getDesc());
			rootHesabsList.add(hesabMoeenMap);
		}
		List<HesabTafsiliEntity> rootHesabTafsilies = getHesabTafsiliService().getRootTafsiliOneHesabs(saalMaaliEntity, currentOrgan);
		for (HesabTafsiliEntity hesabTafsiliEntity : rootHesabTafsilies) {
			ListOrderedMap<String, Object> hesabTafsiliMap = new ListOrderedMap<String, Object>();
			hesabTafsiliMap.put("value","Tafsili_"+hesabTafsiliEntity.getId());
			hesabTafsiliMap.put("label",hesabTafsiliEntity.getDesc());
			rootHesabsList.add(hesabTafsiliMap);
		}
//		List<HesabTafsiliEntity> rootHesabShenavars = getHesabTafsiliService().getRootShenavarHesabs(saalMaaliEntity, currentOrgan);
//		for (HesabTafsiliEntity hesabTafsiliEntity : rootHesabShenavars) {
//			ListOrderedMap<String, Object> hesabTafsiliMap = new ListOrderedMap<String, Object>();
//			hesabTafsiliMap.put("value","Shenavar_"+hesabTafsiliEntity.getId());
//			hesabTafsiliMap.put("label",hesabTafsiliEntity.getDesc());
//			rootHesabsList.add(hesabTafsiliMap);
//		}
		return rootHesabsList;
	}

}