package ir.serajsamaneh.accounting.accountingmarkaztemplate;

import ir.serajsamaneh.accounting.enumeration.HesabScopeEnum;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateService;
import ir.serajsamaneh.accounting.moeenaccountingmarkaz.MoeenAccountingMarkazEntity;
import ir.serajsamaneh.accounting.moeenaccountingmarkaz.MoeenAccountingMarkazService;
import ir.serajsamaneh.accounting.moeenaccountingmarkaztemplate.MoeenAccountingMarkazTemplateEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.FlushMode;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

public class AccountingMarkazTemplateService extends
BaseEntityService<AccountingMarkazTemplateEntity, Long> {

	@Override
	protected AccountingMarkazTemplateDAO getMyDAO() {
		return accountingMarkazTemplateDAO;
	}

	AccountingMarkazTemplateDAO accountingMarkazTemplateDAO;
	HesabMoeenTemplateService hesabMoeenTemplateService;
	SaalMaaliService saalMaaliService;
	HesabTafsiliTemplateService hesabTafsiliTemplateService;
	MoeenAccountingMarkazService moeenAccountingMarkazService;


	
	public HesabMoeenTemplateService getHesabMoeenTemplateService() {
		return hesabMoeenTemplateService;
	}

	public void setHesabMoeenTemplateService(
			HesabMoeenTemplateService hesabMoeenTemplateService) {
		this.hesabMoeenTemplateService = hesabMoeenTemplateService;
	}

	public AccountingMarkazTemplateDAO getAccountingMarkazTemplateDAO() {
		return accountingMarkazTemplateDAO;
	}

	public void setAccountingMarkazTemplateDAO(
			AccountingMarkazTemplateDAO accountingMarkazTemplateDAO) {
		this.accountingMarkazTemplateDAO = accountingMarkazTemplateDAO;
	}

	public MoeenAccountingMarkazService getMoeenAccountingMarkazService() {
		return moeenAccountingMarkazService;
	}

	public void setMoeenAccountingMarkazService(
			MoeenAccountingMarkazService moeenAccountingMarkazService) {
		this.moeenAccountingMarkazService = moeenAccountingMarkazService;
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


	@Override
	public String getDifferences(AccountingMarkazTemplateEntity entity) {
		String diffes = "";

		AccountingMarkazTemplateEntity oldEntity = (AccountingMarkazTemplateEntity) entity.getOldEntity();
		if (entity.getCode() != null
				&& !entity.getCode().equals(oldEntity.getCode()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabTafsili" + "_"
							+ AccountingMarkazTemplateEntity.PROP_CODE) + " : " + oldEntity.getCode()
					+ "" + " --> " + entity.getCode() + "" + "]";

		if (entity.getName() != null
				&& !entity.getName().equals(oldEntity.getName()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabTafsili" + "_"
							+ AccountingMarkazTemplateEntity.PROP_NAME) + " : " + oldEntity.getName()
					+ "" + " --> " + entity.getName() + "" + "]";

		if (entity.getTafsilType() != null
				&& !entity.getTafsilType().equals(oldEntity.getTafsilType()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabTafsili" + "_"
							+ AccountingMarkazTemplateEntity.PROP_TAFSIL_TYPE) + " : "
					+ oldEntity.getTafsilType() + "" + " --> "
					+ entity.getTafsilType() + "" + "]";


		if (entity.getDescription() != null
				&& !entity.getDescription().equals(oldEntity.getDescription()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabTafsili" + "_"
							+ AccountingMarkazTemplateEntity.PROP_DESCRIPTION) + " : "
					+ oldEntity.getDescription() + "" + " --> "
					+ entity.getDescription() + "" + "]";

		if (entity.getHidden() != null
				&& !entity.getHidden().equals(oldEntity.getHidden()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabTafsili" + "_"
							+ AccountingMarkazTemplateEntity.PROP_HIDDEN) + " : "
					+ oldEntity.getHidden() + "" + " --> " + entity.getHidden()
					+ "" + "]";

		return diffes;
	}

	@Transactional
	public void save(AccountingMarkazTemplateEntity entity, List<Long> moeenIds, List<Long> childTafsiliIds, OrganEntity organEntity) {

		if (entity.getMoeenAccountingMarkazTemplate() == null) {
			entity.setMoeenAccountingMarkazTemplate(new HashSet<MoeenAccountingMarkazTemplateEntity>());
		}
		
		if(entity.getOrgan() == null || entity.getOrgan().getId() == null)
			entity.setOrgan(organEntity);
		
		if (entity.getChilds() == null) {
			entity.setChilds(new HashSet<AccountingMarkazTemplateEntity>());
		}
		entity.getMoeenAccountingMarkazTemplate().clear();
		for (Long moeenId : moeenIds) {
			MoeenAccountingMarkazTemplateEntity moeenAccountingMarkazEntity = new MoeenAccountingMarkazTemplateEntity();
			moeenAccountingMarkazEntity.setHesabMoeenTemplate(getHesabMoeenTemplateService().load(moeenId));
			moeenAccountingMarkazEntity.setAccountingMarkazTemplate(entity);
			entity.getMoeenAccountingMarkazTemplate().add(moeenAccountingMarkazEntity);
		}
		
		entity.getChilds().clear();
		for (Long tafsiliId : childTafsiliIds) {
			entity.getChilds().add(load(tafsiliId));
		}
		
		save(entity);
	}


	
	@Override
	public void saveOrUpdate(AccountingMarkazTemplateEntity entity) {
		if(entity.getHidden() == null)
			entity.setHidden(false);
		super.saveOrUpdate(entity);
	}
	
	private synchronized String generateAccountingMarkazCode(OrganEntity organEntity) {
			Long maxHesabTafsiliCode = getMyDAO().getMaxAccountingMarkazCode(organEntity);
			return new Long(++maxHesabTafsiliCode).toString();
	}
	
	//@Override
	@Transactional
	public void save(AccountingMarkazTemplateEntity entity,OrganEntity organEntity) {
		commonSave(entity, organEntity);
		super.save(entity);
	}
	public void saveStateLess(AccountingMarkazTemplateEntity entity, OrganEntity organEntity) {
		commonSave(entity, organEntity);
		super.saveStateLess(entity);
	}

	private void commonSave(AccountingMarkazTemplateEntity entity, OrganEntity organEntity) {
		if(entity.getOrgan() == null || entity.getOrgan().getId() == null)
			entity.setOrgan(organEntity);
		
		if (!StringUtils.hasText(entity.getCode())) {
			
			entity.setCode(generateAccountingMarkazCode(entity.getOrgan()));
		}
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eq", organEntity.getId());
		
		checkUniqueNess(entity, AccountingMarkazTemplateEntity.PROP_CODE, entity.getCode(),	localFilter, false);
	}
	
	@Transactional
	public void updateValues(AccountingMarkazTemplateEntity entity) {
		super.save(entity);
	}



	
	public List<AccountingMarkazTemplateEntity> getActiveAccountingMarkazTemplates(OrganEntity organEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eqORorgan.id@isNull", Arrays.asList(organEntity.getId(),"ding"));
		localFilter.put("hidden@eq", Boolean.FALSE);
		return getDataList(null, localFilter);
	}



	public AccountingMarkazTemplateEntity loadAccountingMarkazTemplateByCode(String code,	OrganEntity organEntity) {
		return loadAccountingMarkazTemplateByCode(code, organEntity, FlushMode.MANUAL);
	}
	
	public AccountingMarkazTemplateEntity loadAccountingMarkazTemplateByCode(String code,	OrganEntity organEntity, FlushMode flushMode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq",code);
		localFilter.put("organ.id@eq",organEntity.getId());
		List<AccountingMarkazTemplateEntity> dataList = getDataList(null, localFilter, flushMode);
		if(dataList.size() == 1)
			return dataList.get(0);
		if(dataList.size() == 0)
			return null;
		throw new FatalException("More Than one AccountingMarkaz Recore Found");
	}

	public AccountingMarkazTemplateEntity load(String code, OrganEntity organ) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		localFilter.put("organ.id@eqORorgan@isNull", Arrays.asList(organ.getId(),"ding"));
		return load(null, localFilter);
	}

	@Transactional
	public AccountingMarkazTemplateEntity createAccountingMarkazTemplate(
			String code, String name, OrganEntity organ) {
		AccountingMarkazTemplateEntity accountingMarkazTemplateEntity = new AccountingMarkazTemplateEntity();
		accountingMarkazTemplateEntity.setScope(HesabScopeEnum.LCAOL);
		accountingMarkazTemplateEntity.setName(name);
		accountingMarkazTemplateEntity.setCode(code);
		accountingMarkazTemplateEntity.setOrgan(organ);
		accountingMarkazTemplateEntity.setHidden(false);
		save(accountingMarkazTemplateEntity);
		return accountingMarkazTemplateEntity;
	}


	
/*	@Transactional(readOnly=false)
	public void importFromHesabTafsiliTemplateList(SaalMaaliEntity activeSaalMaaliEntity){
		Map<String, Object> localFilter = new HashMap<String, Object>();
		List<HesabTafsiliTemplateEntity> dataList = getHesabTafsiliTemplateService().getDataList(null, localFilter );
		for (HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity : dataList) {
			AccountingMarkazEntity hesabTafsili = loadHesabTafsiliByTemplate(hesabTafsiliTemplateEntity, activeSaalMaaliEntity);
			if(hesabTafsili == null){
				try{
					AccountingMarkazEntity hesabTafsiliEntity = createHesabTafsili(activeSaalMaaliEntity,	hesabTafsiliTemplateEntity);
				}catch(DuplicateException e){
					e.printStackTrace();
				}				
			}
		}
		
		getMyDAO().flush();
		for (HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity : dataList) {
			AccountingMarkazEntity hesabTafsiliEntity = loadHesabTafsiliByTemplate(hesabTafsiliTemplateEntity, activeSaalMaaliEntity);
			createHesabTafsiliRelatedEntities(hesabTafsiliTemplateEntity, hesabTafsiliEntity, activeSaalMaaliEntity);

		}
		System.out.println("end");
	}*/

/*	@Transactional(readOnly=false)
	public void createHesabTafsiliRelatedEntities(HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity,
			AccountingMarkazEntity accountingMarkazEntity, SaalMaaliEntity activeSaalMaaliEntity) {
		
//		if(hesabTafsiliEntity.getChilds()!=null)	hesabTafsiliEntity.getChilds().clear();
//		if(hesabTafsiliEntity.getParents()!=null)	hesabTafsiliEntity.getParents().clear();
		
		Set<HesabTafsiliTemplateEntity> templateChilds = hesabTafsiliTemplateEntity.getChilds();
		
		//saving childs
		for (HesabTafsiliTemplateEntity childTemplateTafsiliEntity : templateChilds) {
			AccountingMarkazEntity childHesabTafsiliEntity = loadHesabTafsiliByTemplate(childTemplateTafsiliEntity, activeSaalMaaliEntity);
			if(childHesabTafsiliEntity == null)
				childHesabTafsiliEntity = createHesabTafsili(activeSaalMaaliEntity, childTemplateTafsiliEntity);
			if(!accountingMarkazEntity.getChilds().contains(childHesabTafsiliEntity))
				accountingMarkazEntity.addTochilds(childHesabTafsiliEntity);
		}
		
		Set<HesabTafsiliTemplateEntity> templateParents = hesabTafsiliTemplateEntity.getParent();
		for (HesabTafsiliTemplateEntity parentTemplateHesabTafsiliEntity : templateParents) {
			AccountingMarkazEntity parentHesabTafsiliEntity = loadHesabTafsiliByTemplate(parentTemplateHesabTafsiliEntity, activeSaalMaaliEntity);
			if(parentHesabTafsiliEntity == null)
				parentHesabTafsiliEntity = createHesabTafsili(activeSaalMaaliEntity, parentTemplateHesabTafsiliEntity);
			if(!accountingMarkazEntity.getParents().contains(parentHesabTafsiliEntity))
				accountingMarkazEntity.addToparents(parentHesabTafsiliEntity);
		}
		
		Set<MoeenTafsiliTemplateEntity> templateMoeenTafsili = hesabTafsiliTemplateEntity.getMoeenTafsiliTemplate();
		for (MoeenTafsiliTemplateEntity templateMoeenTafsiliEntity : templateMoeenTafsili) {
			HesabMoeenEntity hesabMoeenEntity = getHesabMoeenService().loadHesabMoeenByTemplate(templateMoeenTafsiliEntity.getHesabMoeenTemplate(), activeSaalMaaliEntity);
			if(hesabMoeenEntity == null)
				hesabMoeenEntity = getHesabMoeenService().createHesabMoeen(activeSaalMaaliEntity, templateMoeenTafsiliEntity.getHesabMoeenTemplate());

			MoeenAccountingMarkazEntity moeenAccountingMarkazEntity = getMoeenAccountingMarkazService().load(accountingMarkazEntity, hesabMoeenEntity, templateMoeenTafsiliEntity.getLevel(), FlushMode.ALWAYS);
			if(moeenAccountingMarkazEntity == null){
				//getMyDAO().replicateBaseEntity(hesabTafsiliEntity);
				//getMyDAO().replicateBaseEntity(hesabMoeenEntity);
				moeenAccountingMarkazEntity = new MoeenAccountingMarkazEntity();
				moeenAccountingMarkazEntity.setAccountingMarkaz(accountingMarkazEntity);
				moeenAccountingMarkazEntity.setHesabMoeen(hesabMoeenEntity);
	
				moeenAccountingMarkazEntity.setLevel(templateMoeenTafsiliEntity.getLevel());
				getMoeenAccountingMarkazService().save(moeenAccountingMarkazEntity);
			}
		}
		
		update(accountingMarkazEntity);
	}*/

/*	public AccountingMarkazEntity createHesabTafsili(SaalMaaliEntity activeSaalMaaliEntity,
			HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity) {
		AccountingMarkazEntity hesabTafsiliEntity = loadHesabTafsiliByTemplate(hesabTafsiliTemplateEntity, activeSaalMaaliEntity);
		if(hesabTafsiliEntity == null){
			hesabTafsiliEntity = populateHesabTafsili(activeSaalMaaliEntity,
					hesabTafsiliTemplateEntity);
			save(hesabTafsiliEntity, activeSaalMaaliEntity);
		}
		return hesabTafsiliEntity;
	}*/

/*	private AccountingMarkazEntity populateHesabTafsili(SaalMaaliEntity activeSaalMaaliEntity,
			HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity) {
		AccountingMarkazEntity hesabTafsiliEntity;
		hesabTafsiliEntity = new AccountingMarkazEntity();
		hesabTafsiliEntity.setBedehkar(0d);
		hesabTafsiliEntity.setBestankr(0d);
		hesabTafsiliEntity.setCode(hesabTafsiliTemplateEntity.getCode());
		hesabTafsiliEntity.setDescription(hesabTafsiliTemplateEntity.getDescription());
		hesabTafsiliEntity.setHesabTafsiliTemplate(hesabTafsiliTemplateEntity);
		hesabTafsiliEntity.setHidden(hesabTafsiliTemplateEntity.getHidden());
		hesabTafsiliEntity.setOrgan(activeSaalMaaliEntity.getOrgan());
		hesabTafsiliEntity.setSaalMaali(activeSaalMaaliEntity);
		hesabTafsiliEntity.setName(hesabTafsiliTemplateEntity.getName());
		hesabTafsiliEntity.setScope(hesabTafsiliTemplateEntity.getScope());
		hesabTafsiliEntity.setTafsilType(hesabTafsiliTemplateEntity.getTafsilType());
		return hesabTafsiliEntity;
	}*/
	
/*	public AccountingMarkazEntity createHesabTafsiliStateLess(SaalMaaliEntity activeSaalMaaliEntity,
			HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity) {
		AccountingMarkazEntity hesabTafsiliEntity = loadHesabTafsiliByTemplate(hesabTafsiliTemplateEntity, activeSaalMaaliEntity);
		if(hesabTafsiliEntity == null){
			hesabTafsiliEntity = populateHesabTafsili(activeSaalMaaliEntity,
					hesabTafsiliTemplateEntity);
			saveStateLess(hesabTafsiliEntity, activeSaalMaaliEntity);
		}
		return hesabTafsiliEntity;
	}*/
	
/*	public AccountingMarkazEntity loadHesabTafsiliByTemplate(HesabTafsiliTemplateEntity tafsiliTemplateEntity, SaalMaaliEntity activeSaalMaaliEntity){
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("hesabTafsiliTemplate.id@eq",tafsiliTemplateEntity.getId());
		localFilter.put("organ.id@eq",activeSaalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq",activeSaalMaaliEntity.getId());
		List<AccountingMarkazEntity> dataList = getDataList(null, localFilter );
		if(dataList.size() == 1)
			return dataList.get(0);
		if(dataList.size() == 0)
			return null;
		throw new FatalException("More Than one AccountingMarkaz Found");
		
	}*/

/*	public AccountingMarkazEntity loadHesabTafsiliByCode(String code,	OrganEntity organEntity, SaalMaaliEntity saalMaaliEntity) {
		return loadHesabTafsiliByCode(code, organEntity, saalMaaliEntity, FlushMode.MANUAL);
	}*/
	
/*	public AccountingMarkazEntity loadHesabTafsiliByCode(String code,	OrganEntity organEntity, SaalMaaliEntity saalMaaliEntity, FlushMode flushMode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq",code);
		localFilter.put("organ.id@eq",organEntity.getId());
		localFilter.put("saalMaali.id@eq",saalMaaliEntity.getId());
		List<AccountingMarkazEntity> dataList = getDataList(null, localFilter, flushMode);
		if(dataList.size() == 1)
			return dataList.get(0);
		if(dataList.size() == 0)
			return null;
		throw new FatalException("More Than one Recore Found");
	}*/
/*

	@Transactional(readOnly=false)
	public void disableHesab(Long hesabId){
		AccountingMarkazEntity hesabTafsiliEntity = load(hesabId);
		hesabTafsiliEntity.setHidden(true);
		super.save(hesabTafsiliEntity);
	}*/

}