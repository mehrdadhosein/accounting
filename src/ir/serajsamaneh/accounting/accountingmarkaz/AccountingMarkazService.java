package ir.serajsamaneh.accounting.accountingmarkaz;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.FlushMode;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import ir.serajsamaneh.accounting.accountingmarkaztemplate.AccountingMarkazTemplateEntity;
import ir.serajsamaneh.accounting.accountingmarkaztemplate.AccountingMarkazTemplateService;
import ir.serajsamaneh.accounting.exception.CycleInHesabTafsiliException;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateService;
import ir.serajsamaneh.accounting.moeenaccountingmarkaz.MoeenAccountingMarkazEntity;
import ir.serajsamaneh.accounting.moeenaccountingmarkaz.MoeenAccountingMarkazService;
import ir.serajsamaneh.accounting.moeenaccountingmarkaztemplate.MoeenAccountingMarkazTemplateEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.exception.FieldMustContainOnlyNumbersException;
import ir.serajsamaneh.core.exception.MoreThanOneRecordFoundException;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;

public class AccountingMarkazService extends
BaseEntityService<AccountingMarkazEntity, Long> {

	@Override
	protected AccountingMarkazDAO getMyDAO() {
		return accountingMarkazDAO;
	}

	AccountingMarkazDAO accountingMarkazDAO;
	HesabMoeenService hesabMoeenService;
	SaalMaaliService saalMaaliService;
	HesabTafsiliTemplateService hesabTafsiliTemplateService;
	MoeenAccountingMarkazService moeenAccountingMarkazService;
	AccountingMarkazTemplateService accountingMarkazTemplateService;
	HesabMoeenTemplateService hesabMoeenTemplateService;
	

	public HesabMoeenTemplateService getHesabMoeenTemplateService() {
		return hesabMoeenTemplateService;
	}

	public void setHesabMoeenTemplateService(
			HesabMoeenTemplateService hesabMoeenTemplateService) {
		this.hesabMoeenTemplateService = hesabMoeenTemplateService;
	}

	public AccountingMarkazTemplateService getAccountingMarkazTemplateService() {
		return accountingMarkazTemplateService;
	}

	public void setAccountingMarkazTemplateService(
			AccountingMarkazTemplateService accountingMarkazTemplateService) {
		this.accountingMarkazTemplateService = accountingMarkazTemplateService;
	}

	public AccountingMarkazDAO getAccountingMarkazDAO() {
		return accountingMarkazDAO;
	}

	public void setAccountingMarkazDAO(AccountingMarkazDAO accountingMarkazDAO) {
		this.accountingMarkazDAO = accountingMarkazDAO;
	}
	
	public MoeenAccountingMarkazService getMoeenAccountingMarkazService() {
		return moeenAccountingMarkazService;
	}

	public void setMoeenAccountingMarkazService(
			MoeenAccountingMarkazService moeenAccountingMarkazService) {
		this.moeenAccountingMarkazService = moeenAccountingMarkazService;
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

	@Override
	public String getDifferences(AccountingMarkazEntity entity) {
		String diffes = "";

		AccountingMarkazEntity oldEntity = (AccountingMarkazEntity) entity.getOldEntity();
		if (entity.getCode() != null
				&& !entity.getCode().equals(oldEntity.getCode()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabTafsili" + "_"
							+ AccountingMarkazEntity.PROP_CODE) + " : " + oldEntity.getCode()
					+ "" + " --> " + entity.getCode() + "" + "]";

		if (entity.getName() != null
				&& !entity.getName().equals(oldEntity.getName()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabTafsili" + "_"
							+ AccountingMarkazEntity.PROP_NAME) + " : " + oldEntity.getName()
					+ "" + " --> " + entity.getName() + "" + "]";

		if (entity.getTafsilType() != null
				&& !entity.getTafsilType().equals(oldEntity.getTafsilType()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabTafsili" + "_"
							+ AccountingMarkazEntity.PROP_TAFSIL_TYPE) + " : "
					+ oldEntity.getTafsilType() + "" + " --> "
					+ entity.getTafsilType() + "" + "]";


		if (entity.getDescription() != null
				&& !entity.getDescription().equals(oldEntity.getDescription()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabTafsili" + "_"
							+ AccountingMarkazEntity.PROP_DESCRIPTION) + " : "
					+ oldEntity.getDescription() + "" + " --> "
					+ entity.getDescription() + "" + "]";

		if (entity.getHidden() != null
				&& !entity.getHidden().equals(oldEntity.getHidden()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabTafsili" + "_"
							+ AccountingMarkazEntity.PROP_HIDDEN) + " : "
					+ oldEntity.getHidden() + "" + " --> " + entity.getHidden()
					+ "" + "]";

		return diffes;
	}

	@Transactional
	public void save(AccountingMarkazEntity entity, List<Long> moeenIds, List<Long> childAccountingMarkazIds, SaalMaaliEntity activeSaalMaaliEntity, Boolean applyMoeenOnSubMarkaz) {
		if (entity.getId() == null) {
			entity.setBedehkar(0d);
			entity.setBestankr(0d);
		}
		if (entity.getMoeenAccountingMarkaz() == null) {
			entity.setMoeenAccountingMarkaz(new HashSet<MoeenAccountingMarkazEntity>());
		}
		
		if(entity.getSaalMaali() == null || entity.getSaalMaali().getId() == null)
			entity.setSaalMaali(activeSaalMaaliEntity);
		
		if (entity.getChilds() == null) {
			entity.setChilds(new HashSet<AccountingMarkazEntity>());
		}
		entity.getMoeenAccountingMarkaz().clear();
		for (Long moeenId : moeenIds) {
			MoeenAccountingMarkazEntity moeenAccountingMarkazEntity = null;//getMoeenAccountingMarkazService().load(entity, moeenId);
			if(moeenAccountingMarkazEntity == null){
				moeenAccountingMarkazEntity = new MoeenAccountingMarkazEntity();
				moeenAccountingMarkazEntity.setHesabMoeen(getHesabMoeenService().load(moeenId));
				moeenAccountingMarkazEntity.setAccountingMarkaz(entity);
				entity.getMoeenAccountingMarkaz().add(moeenAccountingMarkazEntity);
			}
		}
		
		entity.getChilds().clear();
		for (Long accountingMarkazId : childAccountingMarkazIds) {
			entity.getChilds().add(load(accountingMarkazId));
		}
		
		save(entity);
		boolean isNew=(entity.getId()!=null?false:true);
		logAction(isNew, entity);
		createAccountingMarkazTemplateFromAccountingMarkaz(entity, activeSaalMaaliEntity.getOrgan());
		
		if(applyMoeenOnSubMarkaz){
			applyMoeenOnSubMarkaz(entity, moeenIds);
		}
		
		checkCycleInChildAccountingMarkazHierarchy(entity, childAccountingMarkazIds);
	}
	
	private void checkCycleInChildAccountingMarkazHierarchy(AccountingMarkazEntity entity,
			List<Long> childTafsiliIds) {
		for (Long tafsiliId : childTafsiliIds) {
			AccountingMarkazEntity mainChildEntity = load(tafsiliId);
			
			try{
				checkCycleInChildAccountingMarkazHierarchy(entity, mainChildEntity);
			}catch(FatalException e){
				throw new FatalException(SerajMessageUtil.getMessage("AccountingMarkaz_cycleDetected", entity.getDesc(), mainChildEntity.getDesc()));
			}
		}
		
	}
	
	private void checkCycleInChildAccountingMarkazHierarchy(AccountingMarkazEntity mainEntity,
			AccountingMarkazEntity mainChildEntity) {
		if(mainEntity.getId()!=null && mainEntity.getId().equals(mainChildEntity.getId()))
			throw new FatalException();
		Set<AccountingMarkazEntity> childs = mainChildEntity.getChilds();
		for (AccountingMarkazEntity childAccountingMarkaz : childs) {
			checkCycleInChildAccountingMarkazHierarchy(mainEntity, childAccountingMarkaz);
		}
	}

	@Transactional
	private void applyMoeenOnSubMarkaz(AccountingMarkazEntity entity, List<Long> moeenIds) {
		Set<AccountingMarkazEntity> childs = entity.getChilds();
		for (AccountingMarkazEntity accountingMarkazEntity : childs) {
			for (Long moeenId : moeenIds) {
				try{
					MoeenAccountingMarkazEntity moeenAccountingMarkazEntity = getMoeenAccountingMarkazService().load(accountingMarkazEntity, moeenId);
					if(moeenAccountingMarkazEntity == null){
						moeenAccountingMarkazEntity = new MoeenAccountingMarkazEntity();
						moeenAccountingMarkazEntity.setHesabMoeen(getHesabMoeenService().load(moeenId));
						moeenAccountingMarkazEntity.setAccountingMarkaz(accountingMarkazEntity);
						accountingMarkazEntity.addTomoeenAccountingMarkaz(moeenAccountingMarkazEntity);
					}
				}catch(MoreThanOneRecordFoundException e){
					e.printStackTrace();
				}
			}
			save(accountingMarkazEntity);
		}
	}

	@Transactional
	private void createAccountingMarkazTemplateFromAccountingMarkaz(AccountingMarkazEntity entity, OrganEntity organEntity) {
		AccountingMarkazTemplateEntity hesabKolTemplateEntity = getAccountingMarkazTemplateService().load(entity.getCode(), organEntity);
		if(hesabKolTemplateEntity == null){
			getAccountingMarkazTemplateService().createAccountingMarkazTemplate(entity.getCode(), entity.getName(), organEntity);
		}
	}
	
	@Override
	public void saveOrUpdate(AccountingMarkazEntity entity) {
		if(!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("AccountingMarkaz_code"));
		if(entity.getHidden() == null)
			entity.setHidden(false);
		super.saveOrUpdate(entity);
	}
	
	private synchronized String generateAccountingMarkazCode(SaalMaaliEntity saalMaaliEntity) {
			Long maxHesabTafsiliCode = getMyDAO().getMaxAccountingMarkazCode(saalMaaliEntity);
			return new Long(++maxHesabTafsiliCode).toString();
	}
	
	//@Override
	@Transactional
	public void save(AccountingMarkazEntity entity,SaalMaaliEntity activeSaalMaaliEntity) {
		commonSave(entity, activeSaalMaaliEntity);
		save(entity);
	}
	public void saveStateLess(AccountingMarkazEntity entity,SaalMaaliEntity activeSaalMaaliEntity) {
		commonSave(entity, activeSaalMaaliEntity);
		super.saveStateLess(entity);
	}

	@Transactional
	private void commonSave(AccountingMarkazEntity entity,
			SaalMaaliEntity activeSaalMaaliEntity) {

		if(!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("AccountingMarkaz_code"));
		
		if(entity.getHidden() == null)
			entity.setHidden(false);

		if(entity.getSaalMaali() == null || entity.getSaalMaali().getId() == null)
			entity.setSaalMaali(activeSaalMaaliEntity);
		
		if (!StringUtils.hasText(entity.getCode())) {
			
			entity.setCode(generateAccountingMarkazCode(activeSaalMaaliEntity));
		}
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eq", activeSaalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq",activeSaalMaaliEntity.getId());
		
		checkUniqueNess(entity, AccountingMarkazEntity.PROP_CODE, entity.getCode(),	localFilter, false);
		

		
		createOrUpdateRelatedAccountingMarkazTemplate(entity, activeSaalMaaliEntity.getOrgan());
	}

	@Transactional
	private void createOrUpdateRelatedAccountingMarkazTemplate(AccountingMarkazEntity entity, OrganEntity organEntity) {
		AccountingMarkazTemplateEntity accountingMarkazTemplateEntity = getAccountingMarkazTemplateService().load(entity.getCode(), organEntity);
		if(accountingMarkazTemplateEntity == null)
			accountingMarkazTemplateEntity = getAccountingMarkazTemplateService().createAccountingMarkazTemplate(entity.getCode(), entity.getName(), organEntity);
		
		if(accountingMarkazTemplateEntity.getMoeenAccountingMarkazTemplate() == null)
			accountingMarkazTemplateEntity.setMoeenAccountingMarkazTemplate(new HashSet<MoeenAccountingMarkazTemplateEntity>());
		else
			accountingMarkazTemplateEntity.getMoeenAccountingMarkazTemplate().clear();
		
		Set<MoeenAccountingMarkazEntity> moeenAccountingMarkazSet = entity.getMoeenAccountingMarkaz();
		if(moeenAccountingMarkazSet!=null)
			for (MoeenAccountingMarkazEntity moeenAccountingMarkazEntity : moeenAccountingMarkazSet) {
				MoeenAccountingMarkazTemplateEntity moeenAccountingMarkazTemplateEntity = new MoeenAccountingMarkazTemplateEntity();
				moeenAccountingMarkazTemplateEntity.setHesabMoeenTemplate(getHesabMoeenTemplateService().loadByCode(moeenAccountingMarkazEntity.getHesabMoeen().getCode(), organEntity));
				moeenAccountingMarkazTemplateEntity.setAccountingMarkazTemplate(accountingMarkazTemplateEntity);
				moeenAccountingMarkazTemplateEntity.setLevel(moeenAccountingMarkazEntity.getLevel());
				accountingMarkazTemplateEntity.getMoeenAccountingMarkazTemplate().add(moeenAccountingMarkazTemplateEntity);
			}

		if(accountingMarkazTemplateEntity.getChilds() == null)
			accountingMarkazTemplateEntity.setChilds(new HashSet<AccountingMarkazTemplateEntity>());
		else
			accountingMarkazTemplateEntity.getChilds().clear();
		
		Set<AccountingMarkazEntity> childs = entity.getChilds();
		if(childs!=null)
			for (AccountingMarkazEntity accountingMarkazEntity : childs) {
				accountingMarkazTemplateEntity.getChilds().add(getAccountingMarkazTemplateService().load(accountingMarkazEntity.getCode(), organEntity));			
			}
		
		getAccountingMarkazTemplateService().save(accountingMarkazTemplateEntity);
	}
	
	@Transactional
	public void updateValues(AccountingMarkazEntity entity) {
		save(entity);
	}



	
	public List<AccountingMarkazEntity> getActiveAccountingMarkaz(SaalMaaliEntity currentSaalMaali, OrganEntity curentOrgan) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		
		List<Long> topOrganList = getTopOrgansIdList(curentOrgan);
		localFilter.put("organ.id@in", topOrganList);
		
		localFilter.put("hidden@eq", Boolean.FALSE);
		localFilter.put("saalMaali.id@eq", currentSaalMaali.getId());
		return getDataList(null, localFilter);
	}
	
	public List<AccountingMarkazEntity> getActiveAccountingMarkaz(SaalMaaliEntity currentSaalMaali) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		
		localFilter.put("hidden@eq", Boolean.FALSE);
		localFilter.put("saalMaali.id@eq", currentSaalMaali.getId());
		return getDataList(null, localFilter);
	}

	
	@Transactional
	public AccountingMarkazEntity createAccountingMarkaz(SaalMaaliEntity activeSaalMaaliEntity,
			AccountingMarkazEntity srcAccountingMarkazEntity) {
		AccountingMarkazEntity accountingMarkazEntity = loadAccountingMarkazByCode(srcAccountingMarkazEntity.getCode(), activeSaalMaaliEntity);
		if(accountingMarkazEntity == null){
			accountingMarkazEntity = populateAccountingMarkaz(activeSaalMaaliEntity, srcAccountingMarkazEntity);
			save(accountingMarkazEntity, activeSaalMaaliEntity);
		}
		return accountingMarkazEntity;
	}
	
	private AccountingMarkazEntity populateAccountingMarkaz(SaalMaaliEntity activeSaalMaaliEntity,
			AccountingMarkazEntity srcAccountingMarkazEntity) {
		AccountingMarkazEntity accountingMarkazEntity;
		accountingMarkazEntity = new AccountingMarkazEntity();
		accountingMarkazEntity.setBedehkar(0d);
		accountingMarkazEntity.setBestankr(0d);
		accountingMarkazEntity.setCode(srcAccountingMarkazEntity.getCode());
		accountingMarkazEntity.setDescription(srcAccountingMarkazEntity.getDescription());
		accountingMarkazEntity.setHidden(srcAccountingMarkazEntity.getHidden());
		accountingMarkazEntity.setOrgan(activeSaalMaaliEntity.getOrgan());
		accountingMarkazEntity.setSaalMaali(activeSaalMaaliEntity);
		accountingMarkazEntity.setName(srcAccountingMarkazEntity.getName());
		accountingMarkazEntity.setScope(srcAccountingMarkazEntity.getScope());
		accountingMarkazEntity.setTafsilType(srcAccountingMarkazEntity.getTafsilType());
		return accountingMarkazEntity;
	}
	
	public AccountingMarkazEntity loadAccountingMarkazByCode(String code,	SaalMaaliEntity saalMaaliEntity) {
		return loadAccountingMarkazByCode(code, saalMaaliEntity, FlushMode.MANUAL);
	}
	public AccountingMarkazEntity loadAccountingMarkazByCode(String code,	SaalMaaliEntity saalMaaliEntity, FlushMode flushMode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq",code);
//		localFilter.put("organ.id@eq",saalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq",saalMaaliEntity.getId());
		List<AccountingMarkazEntity> dataList = getDataList(null, localFilter, flushMode);
		if(dataList.size() == 1)
			return dataList.get(0);
		if(dataList.size() == 0)
			return null;
		throw new FatalException("More Than one AccountingMarkaz Recore Found");
	}

	@Transactional
	public void createAccountingMarkazRelatedEntities(
			AccountingMarkazEntity srcAccountingMarkazEntity,
			AccountingMarkazEntity destAccountingMarkazEntity,
			SaalMaaliEntity destSaalMaali) {
		
		if(destAccountingMarkazEntity.getChilds() == null)
			destAccountingMarkazEntity.setChilds(new HashSet<AccountingMarkazEntity>());
		
		if(destAccountingMarkazEntity.getParents() == null)
			destAccountingMarkazEntity.setParents(new HashSet<AccountingMarkazEntity>());
		
		Set<AccountingMarkazEntity> srcChilds = srcAccountingMarkazEntity.getChilds();
		
		//saving childs
		for (AccountingMarkazEntity childAccountingMarkazEntity : srcChilds) {
//			if(childHesabTafsiliEntity == null)
//				childHesabTafsiliEntity = createHesabTafsili(activeSaalMaaliEntity, childHesabTafsiliEntity);
			AccountingMarkazEntity destChild = getAccountingMarkazByCodeAndSaalMaali(childAccountingMarkazEntity.getCode(), destSaalMaali);
			if(!destAccountingMarkazEntity.getChilds().contains(destChild))
				destAccountingMarkazEntity.addTochilds(destChild);
		}
		
		Set<AccountingMarkazEntity> srcParents = srcAccountingMarkazEntity.getParents();
		for (AccountingMarkazEntity parentAccountingMarkazEntity : srcParents) {
//			if(parentHesabTafsiliEntity == null)
//				parentHesabTafsiliEntity = createHesabTafsili(activeSaalMaaliEntity, parentHesabTafsiliEntity);
			AccountingMarkazEntity destParent = getAccountingMarkazByCodeAndSaalMaali(parentAccountingMarkazEntity.getCode(), destSaalMaali);
			if(!destAccountingMarkazEntity.getParents().contains(destParent))
				destAccountingMarkazEntity.addToparents(destParent);
		}
		
		Set<MoeenAccountingMarkazEntity> srcMoeenAccountingMarkazs = srcAccountingMarkazEntity.getMoeenAccountingMarkaz();
		for (MoeenAccountingMarkazEntity srcMoeenAccoutingMarkazEntity : srcMoeenAccountingMarkazs) {
			HesabMoeenEntity srcHesabMoeenEntity = srcMoeenAccoutingMarkazEntity.getHesabMoeen();
//			if(hesabMoeenEntity == null)
//				hesabMoeenEntity = getHesabMoeenService().createHesabMoeen(activeSaalMaaliEntity, entity.getHesabMoeen());
			
			HesabMoeenEntity destHesabMoeen = getHesabMoeenService().getHesabMoeenByCodeAndSaalMaali(srcHesabMoeenEntity.getCode(), destSaalMaali);
			MoeenAccountingMarkazEntity destMoeenAccountingMarkazEntity = getMoeenAccountingMarkazService().load(destAccountingMarkazEntity, destHesabMoeen, srcMoeenAccoutingMarkazEntity.getLevel(), FlushMode.ALWAYS);
			if(destMoeenAccountingMarkazEntity == null){
				destMoeenAccountingMarkazEntity = new MoeenAccountingMarkazEntity();
				destMoeenAccountingMarkazEntity.setAccountingMarkaz(destAccountingMarkazEntity);
				destMoeenAccountingMarkazEntity.setHesabMoeen(destHesabMoeen);
				
				destMoeenAccountingMarkazEntity.setLevel(srcMoeenAccoutingMarkazEntity.getLevel());
				getMoeenAccountingMarkazService().save(destMoeenAccountingMarkazEntity);
			}
		}
		
		update(destAccountingMarkazEntity);
		createOrUpdateRelatedAccountingMarkazTemplate(destAccountingMarkazEntity, destSaalMaali.getOrgan());
	}	

	public AccountingMarkazEntity getAccountingMarkazByCodeAndSaalMaali(String hesabCode, SaalMaaliEntity saalMaaliEntity) {
		
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", hesabCode);
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		AccountingMarkazEntity accountingMarkazEntity = load(null, localFilter);
		return accountingMarkazEntity;
	}
	
	@Override
	public void save(AccountingMarkazEntity entity) {
		if(!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("AccountingMarkaz_code"));
		super.save(entity);
	}
}