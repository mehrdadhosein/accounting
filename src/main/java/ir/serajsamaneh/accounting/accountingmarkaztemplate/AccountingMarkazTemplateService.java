package ir.serajsamaneh.accounting.accountingmarkaztemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import ir.serajsamaneh.accounting.enumeration.HesabScopeEnum;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateService;
import ir.serajsamaneh.accounting.moeenaccountingmarkaz.MoeenAccountingMarkazService;
import ir.serajsamaneh.accounting.moeenaccountingmarkaztemplate.MoeenAccountingMarkazTemplateEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.util.SerajMessageUtil;
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AccountingMarkazTemplateService extends BaseEntityService<AccountingMarkazTemplateEntity, Long> {

	@Override
	protected AccountingMarkazTemplateDAO getMyDAO() {
		return accountingMarkazTemplateDAO;
	}

	@Autowired
	AccountingMarkazTemplateDAO accountingMarkazTemplateDAO;
	@Autowired
	HesabMoeenTemplateService hesabMoeenTemplateService;
	@Autowired
	SaalMaaliService saalMaaliService;
	@Autowired
	HesabTafsiliTemplateService hesabTafsiliTemplateService;
	@Autowired
	MoeenAccountingMarkazService moeenAccountingMarkazService;

	@Override
	public String getDifferences(AccountingMarkazTemplateEntity entity) {
		String diffes = "";

		AccountingMarkazTemplateEntity oldEntity = (AccountingMarkazTemplateEntity) entity.getOldEntity();
		if (entity.getCode() != null && !entity.getCode().equals(oldEntity.getCode()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabTafsili" + "_" + AccountingMarkazTemplateEntity.PROP_CODE)
					+ " : " + oldEntity.getCode() + "" + " --> " + entity.getCode() + "" + "]";

		if (entity.getName() != null && !entity.getName().equals(oldEntity.getName()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabTafsili" + "_" + AccountingMarkazTemplateEntity.PROP_NAME)
					+ " : " + oldEntity.getName() + "" + " --> " + entity.getName() + "" + "]";

		if (entity.getTafsilType() != null && !entity.getTafsilType().equals(oldEntity.getTafsilType()))
			diffes += "["
					+ SerajMessageUtil
							.getMessage("HesabTafsili" + "_" + AccountingMarkazTemplateEntity.PROP_TAFSIL_TYPE)
					+ " : " + oldEntity.getTafsilType() + "" + " --> " + entity.getTafsilType() + "" + "]";

		if (entity.getDescription() != null && !entity.getDescription().equals(oldEntity.getDescription()))
			diffes += "["
					+ SerajMessageUtil
							.getMessage("HesabTafsili" + "_" + AccountingMarkazTemplateEntity.PROP_DESCRIPTION)
					+ " : " + oldEntity.getDescription() + "" + " --> " + entity.getDescription() + "" + "]";

		if (entity.getHidden() != null && !entity.getHidden().equals(oldEntity.getHidden()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabTafsili" + "_" + AccountingMarkazTemplateEntity.PROP_HIDDEN)
					+ " : " + oldEntity.getHidden() + "" + " --> " + entity.getHidden() + "" + "]";

		return diffes;
	}

	@Transactional
	public void save(AccountingMarkazTemplateEntity entity, List<Long> moeenIds, List<Long> childTafsiliIds,
			Long organId) {

		if (entity.getMoeenAccountingMarkazTemplate() == null) {
			entity.setMoeenAccountingMarkazTemplate(new HashSet<MoeenAccountingMarkazTemplateEntity>());
		}

		if (entity.getOrganId() == null)
			entity.setOrganId(organId);

		if (entity.getChilds() == null) {
			entity.setChilds(new HashSet<AccountingMarkazTemplateEntity>());
		}
		entity.getMoeenAccountingMarkazTemplate().clear();
		for (Long moeenId : moeenIds) {
			MoeenAccountingMarkazTemplateEntity moeenAccountingMarkazEntity = new MoeenAccountingMarkazTemplateEntity();
			moeenAccountingMarkazEntity.setHesabMoeenTemplate(hesabMoeenTemplateService.load(moeenId));
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
		if (entity.getHidden() == null)
			entity.setHidden(false);
		super.saveOrUpdate(entity);
	}

	private synchronized String generateAccountingMarkazCode(Long organId) {
		Long maxHesabTafsiliCode = getMyDAO().getMaxAccountingMarkazCode(organId);
		return Long.valueOf(++maxHesabTafsiliCode).toString();
	}

	// @Override
	@Transactional
	public void save(AccountingMarkazTemplateEntity entity, Long organId) {
		commonSave(entity, organId);
		super.save(entity);
	}

	public void saveStateLess(AccountingMarkazTemplateEntity entity, Long organId) {
		commonSave(entity, organId);
		super.saveStateLess(entity);
	}

	private void commonSave(AccountingMarkazTemplateEntity entity, Long organId) {
		if (entity.getOrganId() == null)
			entity.setOrganId(organId);

		if (!StringUtils.hasText(entity.getCode())) {

			entity.setCode(generateAccountingMarkazCode(organId));
		}
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organId@eq", organId);

		checkUniqueNess(entity, AccountingMarkazTemplateEntity.PROP_CODE, entity.getCode(), localFilter, false);
	}

	@Transactional
	public void updateValues(AccountingMarkazTemplateEntity entity) {
		super.save(entity);
	}

	public List<AccountingMarkazTemplateEntity> getActiveAccountingMarkazTemplates(Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organId@eq", organId);
		localFilter.put("hidden@eq", Boolean.FALSE);
		return getDataList(localFilter);
	}

	public AccountingMarkazTemplateEntity loadAccountingMarkazTemplateByCode(String code, Long organId) {
		return loadAccountingMarkazTemplateByCode(code, organId, FlushMode.MANUAL);
	}

	public AccountingMarkazTemplateEntity loadAccountingMarkazTemplateByCode(String code, Long organId,
			FlushMode flushMode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		localFilter.put("organId@eq", organId);
		List<AccountingMarkazTemplateEntity> dataList = getDataList(localFilter, flushMode);
		if (dataList.size() == 1)
			return dataList.get(0);
		if (dataList.size() == 0)
			return null;
		throw new FatalException("More Than one AccountingMarkaz Recore Found");
	}

	public AccountingMarkazTemplateEntity load(String code, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		localFilter.put("organId@eq", organId);
		return load(localFilter);
	}

	@Transactional
	public AccountingMarkazTemplateEntity createAccountingMarkazTemplate(String code, String name, Long organId) {
		AccountingMarkazTemplateEntity accountingMarkazTemplateEntity = new AccountingMarkazTemplateEntity();
		accountingMarkazTemplateEntity.setScope(HesabScopeEnum.LCAOL);
		accountingMarkazTemplateEntity.setName(name);
		accountingMarkazTemplateEntity.setCode(code);
		accountingMarkazTemplateEntity.setOrganId(organId);
		accountingMarkazTemplateEntity.setHidden(false);
		save(accountingMarkazTemplateEntity);
		return accountingMarkazTemplateEntity;
	}

	/*
	 * @Transactional(readOnly=false) public void
	 * importFromHesabTafsiliTemplateList(SaalMaaliEntity activeSaalMaaliEntity){
	 * Map<String, Object> localFilter = new HashMap<String, Object>();
	 * List<HesabTafsiliTemplateEntity> dataList =
	 * hesabTafsiliTemplateService.getDataList(localFilter ); for
	 * (HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity : dataList) {
	 * AccountingMarkazEntity hesabTafsili =
	 * loadHesabTafsiliByTemplate(hesabTafsiliTemplateEntity,
	 * activeSaalMaaliEntity); if(hesabTafsili == null){ try{ AccountingMarkazEntity
	 * hesabTafsiliEntity = createHesabTafsili(activeSaalMaaliEntity,
	 * hesabTafsiliTemplateEntity); }catch(DuplicateException e){
	 * e.printStackTrace(); } } }
	 * 
	 * getMyDAO().flush(); for (HesabTafsiliTemplateEntity
	 * hesabTafsiliTemplateEntity : dataList) { AccountingMarkazEntity
	 * hesabTafsiliEntity = loadHesabTafsiliByTemplate(hesabTafsiliTemplateEntity,
	 * activeSaalMaaliEntity);
	 * createHesabTafsiliRelatedEntities(hesabTafsiliTemplateEntity,
	 * hesabTafsiliEntity, activeSaalMaaliEntity);
	 * 
	 * } System.out.println("end"); }
	 */

	/*
	 * @Transactional(readOnly=false) public void
	 * createHesabTafsiliRelatedEntities(HesabTafsiliTemplateEntity
	 * hesabTafsiliTemplateEntity, AccountingMarkazEntity accountingMarkazEntity,
	 * SaalMaaliEntity activeSaalMaaliEntity) {
	 * 
	 * // if(hesabTafsiliEntity.getChilds()!=null)
	 * hesabTafsiliEntity.getChilds().clear(); //
	 * if(hesabTafsiliEntity.getParents()!=null)
	 * hesabTafsiliEntity.getParents().clear();
	 * 
	 * Set<HesabTafsiliTemplateEntity> templateChilds =
	 * hesabTafsiliTemplateEntity.getChilds();
	 * 
	 * //saving childs for (HesabTafsiliTemplateEntity childTemplateTafsiliEntity :
	 * templateChilds) { AccountingMarkazEntity childHesabTafsiliEntity =
	 * loadHesabTafsiliByTemplate(childTemplateTafsiliEntity,
	 * activeSaalMaaliEntity); if(childHesabTafsiliEntity == null)
	 * childHesabTafsiliEntity = createHesabTafsili(activeSaalMaaliEntity,
	 * childTemplateTafsiliEntity);
	 * if(!accountingMarkazEntity.getChilds().contains(childHesabTafsiliEntity))
	 * accountingMarkazEntity.addTochilds(childHesabTafsiliEntity); }
	 * 
	 * Set<HesabTafsiliTemplateEntity> templateParents =
	 * hesabTafsiliTemplateEntity.getParent(); for (HesabTafsiliTemplateEntity
	 * parentTemplateHesabTafsiliEntity : templateParents) { AccountingMarkazEntity
	 * parentHesabTafsiliEntity =
	 * loadHesabTafsiliByTemplate(parentTemplateHesabTafsiliEntity,
	 * activeSaalMaaliEntity); if(parentHesabTafsiliEntity == null)
	 * parentHesabTafsiliEntity = createHesabTafsili(activeSaalMaaliEntity,
	 * parentTemplateHesabTafsiliEntity);
	 * if(!accountingMarkazEntity.getParents().contains(parentHesabTafsiliEntity))
	 * accountingMarkazEntity.addToparents(parentHesabTafsiliEntity); }
	 * 
	 * Set<MoeenTafsiliTemplateEntity> templateMoeenTafsili =
	 * hesabTafsiliTemplateEntity.getMoeenTafsiliTemplate(); for
	 * (MoeenTafsiliTemplateEntity templateMoeenTafsiliEntity :
	 * templateMoeenTafsili) { HesabMoeenEntity hesabMoeenEntity =
	 * hesabMoeenService.loadHesabMoeenByTemplate(templateMoeenTafsiliEntity.
	 * getHesabMoeenTemplate(), activeSaalMaaliEntity); if(hesabMoeenEntity == null)
	 * hesabMoeenEntity = hesabMoeenService.createHesabMoeen(activeSaalMaaliEntity,
	 * templateMoeenTafsiliEntity.getHesabMoeenTemplate());
	 * 
	 * MoeenAccountingMarkazEntity moeenAccountingMarkazEntity =
	 * moeenAccountingMarkazService.load(accountingMarkazEntity, hesabMoeenEntity,
	 * templateMoeenTafsiliEntity.getLevel(), FlushMode.ALWAYS);
	 * if(moeenAccountingMarkazEntity == null){
	 * //getMyDAO().replicateBaseEntity(hesabTafsiliEntity);
	 * //getMyDAO().replicateBaseEntity(hesabMoeenEntity);
	 * moeenAccountingMarkazEntity = new MoeenAccountingMarkazEntity();
	 * moeenAccountingMarkazEntity.setAccountingMarkaz(accountingMarkazEntity);
	 * moeenAccountingMarkazEntity.setHesabMoeen(hesabMoeenEntity);
	 * 
	 * moeenAccountingMarkazEntity.setLevel(templateMoeenTafsiliEntity.getLevel());
	 * moeenAccountingMarkazService.save(moeenAccountingMarkazEntity); } }
	 * 
	 * update(accountingMarkazEntity); }
	 */

	/*
	 * public AccountingMarkazEntity createHesabTafsili(SaalMaaliEntity
	 * activeSaalMaaliEntity, HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity)
	 * { AccountingMarkazEntity hesabTafsiliEntity =
	 * loadHesabTafsiliByTemplate(hesabTafsiliTemplateEntity,
	 * activeSaalMaaliEntity); if(hesabTafsiliEntity == null){ hesabTafsiliEntity =
	 * populateHesabTafsili(activeSaalMaaliEntity, hesabTafsiliTemplateEntity);
	 * save(hesabTafsiliEntity, activeSaalMaaliEntity); } return hesabTafsiliEntity;
	 * }
	 */

	/*
	 * private AccountingMarkazEntity populateHesabTafsili(SaalMaaliEntity
	 * activeSaalMaaliEntity, HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity)
	 * { AccountingMarkazEntity hesabTafsiliEntity; hesabTafsiliEntity = new
	 * AccountingMarkazEntity(); hesabTafsiliEntity.setBedehkar(0d);
	 * hesabTafsiliEntity.setBestankr(0d);
	 * hesabTafsiliEntity.setCode(hesabTafsiliTemplateEntity.getCode());
	 * hesabTafsiliEntity.setDescription(hesabTafsiliTemplateEntity.getDescription()
	 * ); hesabTafsiliEntity.setHesabTafsiliTemplate(hesabTafsiliTemplateEntity);
	 * hesabTafsiliEntity.setHidden(hesabTafsiliTemplateEntity.getHidden());
	 * hesabTafsiliEntity.setOrgan(activeSaalMaaliEntity.getOrgan());
	 * hesabTafsiliEntity.setSaalMaali(activeSaalMaaliEntity);
	 * hesabTafsiliEntity.setName(hesabTafsiliTemplateEntity.getName());
	 * hesabTafsiliEntity.setScope(hesabTafsiliTemplateEntity.getScope());
	 * hesabTafsiliEntity.setTafsilType(hesabTafsiliTemplateEntity.getTafsilType());
	 * return hesabTafsiliEntity; }
	 */

	/*
	 * public AccountingMarkazEntity createHesabTafsiliStateLess(SaalMaaliEntity
	 * activeSaalMaaliEntity, HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity)
	 * { AccountingMarkazEntity hesabTafsiliEntity =
	 * loadHesabTafsiliByTemplate(hesabTafsiliTemplateEntity,
	 * activeSaalMaaliEntity); if(hesabTafsiliEntity == null){ hesabTafsiliEntity =
	 * populateHesabTafsili(activeSaalMaaliEntity, hesabTafsiliTemplateEntity);
	 * saveStateLess(hesabTafsiliEntity, activeSaalMaaliEntity); } return
	 * hesabTafsiliEntity; }
	 */

	/*
	 * public AccountingMarkazEntity
	 * loadHesabTafsiliByTemplate(HesabTafsiliTemplateEntity tafsiliTemplateEntity,
	 * SaalMaaliEntity activeSaalMaaliEntity){ Map<String, Object> localFilter = new
	 * HashMap<String, Object>();
	 * localFilter.put("hesabTafsiliTemplate.id@eq",tafsiliTemplateEntity.getId());
	 * localFilter.put("organId@eq",activeSaalMaaliEntity.getOrgan().getId());
	 * localFilter.put("saalMaali.id@eq",activeSaalMaaliEntity.getId());
	 * List<AccountingMarkazEntity> dataList = getDataList(null, localFilter );
	 * if(dataList.size() == 1) return dataList.get(0); if(dataList.size() == 0)
	 * return null; throw new
	 * FatalException("More Than one AccountingMarkaz Found");
	 * 
	 * }
	 */

	/*
	 * public AccountingMarkazEntity loadHesabTafsiliByCode(String code, OrganEntity
	 * organEntity, SaalMaaliEntity saalMaaliEntity) { return
	 * loadHesabTafsiliByCode(code, organEntity, saalMaaliEntity, FlushMode.MANUAL);
	 * }
	 */

	/*
	 * public AccountingMarkazEntity loadHesabTafsiliByCode(String code, OrganEntity
	 * organEntity, SaalMaaliEntity saalMaaliEntity, FlushMode flushMode) {
	 * Map<String, Object> localFilter = new HashMap<String, Object>();
	 * localFilter.put("code@eq",code);
	 * localFilter.put("organId@eq",organEntity.getId());
	 * localFilter.put("saalMaali.id@eq",saalMaaliEntity.getId());
	 * List<AccountingMarkazEntity> dataList = getDataList(localFilter,
	 * flushMode); if(dataList.size() == 1) return dataList.get(0);
	 * if(dataList.size() == 0) return null; throw new
	 * FatalException("More Than one Recore Found"); }
	 */
	/*
	 * 
	 * @Transactional(readOnly=false) public void disableHesab(Long hesabId){
	 * AccountingMarkazEntity hesabTafsiliEntity = load(hesabId);
	 * hesabTafsiliEntity.setHidden(true); super.save(hesabTafsiliEntity); }
	 */

}