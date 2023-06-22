package ir.serajsamaneh.accounting.accountingmarkaz;

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

import ir.serajsamaneh.accounting.accountingmarkaztemplate.AccountingMarkazTemplateEntity;
import ir.serajsamaneh.accounting.accountingmarkaztemplate.AccountingMarkazTemplateService;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
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
import ir.serajsamaneh.core.util.SerajMessageUtil;
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AccountingMarkazService extends BaseEntityService<AccountingMarkazEntity, Long> {

	@Override
	protected AccountingMarkazDAO getMyDAO() {
		return accountingMarkazDAO;
	}

	@Autowired
	AccountingMarkazDAO accountingMarkazDAO;
	@Autowired
	HesabMoeenService hesabMoeenService;
	@Autowired
	SaalMaaliService saalMaaliService;
	@Autowired
	HesabTafsiliTemplateService hesabTafsiliTemplateService;
	@Autowired
	MoeenAccountingMarkazService moeenAccountingMarkazService;
	@Autowired
	AccountingMarkazTemplateService accountingMarkazTemplateService;
	@Autowired
	HesabMoeenTemplateService hesabMoeenTemplateService;

	@Override
	public String getDifferences(AccountingMarkazEntity entity) {
		String diffes = "";

		AccountingMarkazEntity oldEntity = (AccountingMarkazEntity) entity.getOldEntity();
		if (entity.getCode() != null && !entity.getCode().equals(oldEntity.getCode()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabTafsili" + "_" + AccountingMarkazEntity.PROP_CODE) + " : "
					+ oldEntity.getCode() + "" + " --> " + entity.getCode() + "" + "]";

		if (entity.getName() != null && !entity.getName().equals(oldEntity.getName()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabTafsili" + "_" + AccountingMarkazEntity.PROP_NAME) + " : "
					+ oldEntity.getName() + "" + " --> " + entity.getName() + "" + "]";

		if (entity.getTafsilType() != null && !entity.getTafsilType().equals(oldEntity.getTafsilType()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabTafsili" + "_" + AccountingMarkazEntity.PROP_TAFSIL_TYPE)
					+ " : " + oldEntity.getTafsilType() + "" + " --> " + entity.getTafsilType() + "" + "]";

		if (entity.getDescription() != null && !entity.getDescription().equals(oldEntity.getDescription()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabTafsili" + "_" + AccountingMarkazEntity.PROP_DESCRIPTION)
					+ " : " + oldEntity.getDescription() + "" + " --> " + entity.getDescription() + "" + "]";

		if (entity.getHidden() != null && !entity.getHidden().equals(oldEntity.getHidden()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabTafsili" + "_" + AccountingMarkazEntity.PROP_HIDDEN)
					+ " : " + oldEntity.getHidden() + "" + " --> " + entity.getHidden() + "" + "]";

		return diffes;
	}

	@Transactional
	public void save(AccountingMarkazEntity entity, List<Long> moeenIds, List<Long> childAccountingMarkazIds,
			SaalMaaliEntity activeSaalMaaliEntity, Boolean applyMoeenOnSubMarkaz) {
		if (entity.getId() == null) {
			entity.setBedehkar(0d);
			entity.setBestankr(0d);
		}
		if (entity.getMoeenAccountingMarkaz() == null) {
			entity.setMoeenAccountingMarkaz(new HashSet<MoeenAccountingMarkazEntity>());
		}

		if (entity.getSaalMaali() == null || entity.getSaalMaali().getId() == null)
			entity.setSaalMaali(activeSaalMaaliEntity);

		if (entity.getChilds() == null) {
			entity.setChilds(new HashSet<AccountingMarkazEntity>());
		}
		entity.getMoeenAccountingMarkaz().clear();
		for (Long moeenId : moeenIds) {
			MoeenAccountingMarkazEntity moeenAccountingMarkazEntity = null;// moeenAccountingMarkazService.load(entity,
																			// moeenId);
			if (moeenAccountingMarkazEntity == null) {
				moeenAccountingMarkazEntity = new MoeenAccountingMarkazEntity();
				moeenAccountingMarkazEntity.setHesabMoeen(hesabMoeenService.load(moeenId));
				moeenAccountingMarkazEntity.setAccountingMarkaz(entity);
				entity.getMoeenAccountingMarkaz().add(moeenAccountingMarkazEntity);
			}
		}

		entity.getChilds().clear();
		for (Long accountingMarkazId : childAccountingMarkazIds) {
			entity.getChilds().add(load(accountingMarkazId));
		}

		save(entity);
		boolean isNew = (entity.getId() != null ? false : true);
		logAction(isNew, entity);
		createAccountingMarkazTemplateFromAccountingMarkaz(entity, activeSaalMaaliEntity.getOrganId());

		if (applyMoeenOnSubMarkaz) {
			applyMoeenOnSubMarkaz(entity, moeenIds);
		}

		checkCycleInChildAccountingMarkazHierarchy(entity, childAccountingMarkazIds);
	}

	private void checkCycleInChildAccountingMarkazHierarchy(AccountingMarkazEntity entity, List<Long> childTafsiliIds) {
		for (Long tafsiliId : childTafsiliIds) {
			AccountingMarkazEntity mainChildEntity = load(tafsiliId);

			try {
				checkCycleInChildAccountingMarkazHierarchy(entity, mainChildEntity);
			} catch (FatalException e) {
				throw new FatalException(SerajMessageUtil.getMessage("AccountingMarkaz_cycleDetected", entity.getDesc(),
						mainChildEntity.getDesc()));
			}
		}

	}

	private void checkCycleInChildAccountingMarkazHierarchy(AccountingMarkazEntity mainEntity,
			AccountingMarkazEntity mainChildEntity) {
		if (mainEntity.getId() != null && mainEntity.getId().equals(mainChildEntity.getId()))
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
				try {
					MoeenAccountingMarkazEntity moeenAccountingMarkazEntity = moeenAccountingMarkazService
							.load(accountingMarkazEntity, moeenId);
					if (moeenAccountingMarkazEntity == null) {
						moeenAccountingMarkazEntity = new MoeenAccountingMarkazEntity();
						moeenAccountingMarkazEntity.setHesabMoeen(hesabMoeenService.load(moeenId));
						moeenAccountingMarkazEntity.setAccountingMarkaz(accountingMarkazEntity);
						accountingMarkazEntity.addTomoeenAccountingMarkaz(moeenAccountingMarkazEntity);
					}
				} catch (MoreThanOneRecordFoundException e) {
					e.printStackTrace();
				}
			}
			save(accountingMarkazEntity);
		}
	}

	@Transactional
	private void createAccountingMarkazTemplateFromAccountingMarkaz(AccountingMarkazEntity entity, Long organId) {
		AccountingMarkazTemplateEntity hesabKolTemplateEntity = accountingMarkazTemplateService.load(entity.getCode(),
				organId);
		if (hesabKolTemplateEntity == null) {
			accountingMarkazTemplateService.createAccountingMarkazTemplate(entity.getCode(), entity.getName(), organId);
		}
	}

	@Override
	public void saveOrUpdate(AccountingMarkazEntity entity) {
		if (!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("AccountingMarkaz_code"));
		if (entity.getHidden() == null)
			entity.setHidden(false);
		super.saveOrUpdate(entity);
	}

	private synchronized String generateAccountingMarkazCode(SaalMaaliEntity saalMaaliEntity) {
		Long maxHesabTafsiliCode = getMyDAO().getMaxAccountingMarkazCode(saalMaaliEntity);
		return Long.valueOf(++maxHesabTafsiliCode).toString();
	}

	// @Override
	@Transactional
	public void save(AccountingMarkazEntity entity, SaalMaaliEntity activeSaalMaaliEntity, String topOrganCode) {
		commonSave(entity, activeSaalMaaliEntity, topOrganCode);
		save(entity);
	}

	public void saveStateLess(AccountingMarkazEntity entity, SaalMaaliEntity activeSaalMaaliEntity,
			String topOrganCode) {
		commonSave(entity, activeSaalMaaliEntity, topOrganCode);
		super.saveStateLess(entity);
	}

	@Transactional
	private void commonSave(AccountingMarkazEntity entity, SaalMaaliEntity activeSaalMaaliEntity, String topOrganCode) {

		if (!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("AccountingMarkaz_code"));

		if (entity.getHidden() == null)
			entity.setHidden(false);

		if (entity.getSaalMaali() == null || entity.getSaalMaali().getId() == null)
			entity.setSaalMaali(activeSaalMaaliEntity);

		if (!StringUtils.hasText(entity.getCode())) {

			entity.setCode(generateAccountingMarkazCode(activeSaalMaaliEntity));
		}
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organId@eq", activeSaalMaaliEntity.getOrganId());
		localFilter.put("saalMaali.id@eq", activeSaalMaaliEntity.getId());

		checkUniqueNess(entity, AccountingMarkazEntity.PROP_CODE, entity.getCode(), localFilter, false);

		createOrUpdateRelatedAccountingMarkazTemplate(entity, activeSaalMaaliEntity.getOrganId(), topOrganCode);
	}

	@Transactional
	private void createOrUpdateRelatedAccountingMarkazTemplate(AccountingMarkazEntity entity, Long organId,
			String topOrganCode) {
		AccountingMarkazTemplateEntity accountingMarkazTemplateEntity = accountingMarkazTemplateService
				.load(entity.getCode(), organId);
		if (accountingMarkazTemplateEntity == null)
			accountingMarkazTemplateEntity = accountingMarkazTemplateService
					.createAccountingMarkazTemplate(entity.getCode(), entity.getName(), organId);

		if (accountingMarkazTemplateEntity.getMoeenAccountingMarkazTemplate() == null)
			accountingMarkazTemplateEntity
					.setMoeenAccountingMarkazTemplate(new HashSet<MoeenAccountingMarkazTemplateEntity>());
		else
			accountingMarkazTemplateEntity.getMoeenAccountingMarkazTemplate().clear();

		Set<MoeenAccountingMarkazEntity> moeenAccountingMarkazSet = entity.getMoeenAccountingMarkaz();
		if (moeenAccountingMarkazSet != null)
			for (MoeenAccountingMarkazEntity moeenAccountingMarkazEntity : moeenAccountingMarkazSet) {
				MoeenAccountingMarkazTemplateEntity moeenAccountingMarkazTemplateEntity = new MoeenAccountingMarkazTemplateEntity();
				moeenAccountingMarkazTemplateEntity.setHesabMoeenTemplate(hesabMoeenTemplateService
						.loadByCode(moeenAccountingMarkazEntity.getHesabMoeen().getCode(), topOrganCode));
				moeenAccountingMarkazTemplateEntity.setAccountingMarkazTemplate(accountingMarkazTemplateEntity);
				moeenAccountingMarkazTemplateEntity.setLevel(moeenAccountingMarkazEntity.getLevel());
				accountingMarkazTemplateEntity.getMoeenAccountingMarkazTemplate()
						.add(moeenAccountingMarkazTemplateEntity);
			}

		if (accountingMarkazTemplateEntity.getChilds() == null)
			accountingMarkazTemplateEntity.setChilds(new HashSet<AccountingMarkazTemplateEntity>());
		else
			accountingMarkazTemplateEntity.getChilds().clear();

		Set<AccountingMarkazEntity> childs = entity.getChilds();
		if (childs != null)
			for (AccountingMarkazEntity accountingMarkazEntity : childs) {
				accountingMarkazTemplateEntity.getChilds()
						.add(accountingMarkazTemplateService.load(accountingMarkazEntity.getCode(), organId));
			}

		accountingMarkazTemplateService.save(accountingMarkazTemplateEntity);
	}

	@Transactional
	public void updateValues(AccountingMarkazEntity entity) {
		save(entity);
	}

	public List<AccountingMarkazEntity> getActiveAccountingMarkaz(SaalMaaliEntity currentSaalMaali,
			List<Long> topOrganList) {
		Map<String, Object> localFilter = new HashMap<String, Object>();

//		List<Long> topOrganList = curentOrgan.getTopOrgansIdList();
		localFilter.put("organId@in", topOrganList);

		localFilter.put("hidden@eq", Boolean.FALSE);
		localFilter.put("saalMaali.id@eq", currentSaalMaali.getId());
		return getDataList(localFilter);
	}

	public List<AccountingMarkazEntity> getActiveAccountingMarkaz(SaalMaaliEntity currentSaalMaali) {
		Map<String, Object> localFilter = new HashMap<String, Object>();

		localFilter.put("hidden@eq", Boolean.FALSE);
		localFilter.put("saalMaali.id@eq", currentSaalMaali.getId());
		return getDataList(localFilter);
	}

	@Transactional
	public AccountingMarkazEntity createAccountingMarkaz(SaalMaaliEntity activeSaalMaaliEntity,
			AccountingMarkazEntity srcAccountingMarkazEntity, String topOrganCode) {
		AccountingMarkazEntity accountingMarkazEntity = loadAccountingMarkazByCode(srcAccountingMarkazEntity.getCode(),
				activeSaalMaaliEntity.getId());
		if (accountingMarkazEntity == null) {
			accountingMarkazEntity = populateAccountingMarkaz(activeSaalMaaliEntity, srcAccountingMarkazEntity);
			save(accountingMarkazEntity, activeSaalMaaliEntity, topOrganCode);
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
		accountingMarkazEntity.setOrganId(activeSaalMaaliEntity.getOrganId());
		accountingMarkazEntity.setSaalMaali(activeSaalMaaliEntity);
		accountingMarkazEntity.setName(srcAccountingMarkazEntity.getName());
		accountingMarkazEntity.setScope(srcAccountingMarkazEntity.getScope());
		accountingMarkazEntity.setTafsilType(srcAccountingMarkazEntity.getTafsilType());
		return accountingMarkazEntity;
	}

	public AccountingMarkazEntity loadAccountingMarkazByCode(String code, Long saalMaaliId) {
		return loadAccountingMarkazByCode(code, saalMaaliId, FlushMode.MANUAL);
	}

	public AccountingMarkazEntity loadAccountingMarkazByCode(String code, Long saalMaaliId, FlushMode flushMode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
//		localFilter.put("organId@eq",saalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq", saalMaaliId);
		List<AccountingMarkazEntity> dataList = getDataList(localFilter, flushMode);
		if (dataList.size() == 1)
			return dataList.get(0);
		if (dataList.size() == 0)
			return null;
		throw new FatalException("More Than one AccountingMarkaz Recore Found");
	}

	@Transactional
	public void createAccountingMarkazRelatedEntities(AccountingMarkazEntity srcAccountingMarkazEntity,
			AccountingMarkazEntity destAccountingMarkazEntity, SaalMaaliEntity destSaalMaali, String topOrganCode) {

		if (destAccountingMarkazEntity.getChilds() == null)
			destAccountingMarkazEntity.setChilds(new HashSet<AccountingMarkazEntity>());

		if (destAccountingMarkazEntity.getParents() == null)
			destAccountingMarkazEntity.setParents(new HashSet<AccountingMarkazEntity>());

		Set<AccountingMarkazEntity> srcChilds = srcAccountingMarkazEntity.getChilds();

		// saving childs
		for (AccountingMarkazEntity childAccountingMarkazEntity : srcChilds) {
//			if(childHesabTafsiliEntity == null)
//				childHesabTafsiliEntity = createHesabTafsili(activeSaalMaaliEntity, childHesabTafsiliEntity);
			AccountingMarkazEntity destChild = getAccountingMarkazByCodeAndSaalMaali(
					childAccountingMarkazEntity.getCode(), destSaalMaali);
			if (!destAccountingMarkazEntity.getChilds().contains(destChild))
				destAccountingMarkazEntity.addTochilds(destChild);
		}

		Set<AccountingMarkazEntity> srcParents = srcAccountingMarkazEntity.getParents();
		for (AccountingMarkazEntity parentAccountingMarkazEntity : srcParents) {
//			if(parentHesabTafsiliEntity == null)
//				parentHesabTafsiliEntity = createHesabTafsili(activeSaalMaaliEntity, parentHesabTafsiliEntity);
			AccountingMarkazEntity destParent = getAccountingMarkazByCodeAndSaalMaali(
					parentAccountingMarkazEntity.getCode(), destSaalMaali);
			if (!destAccountingMarkazEntity.getParents().contains(destParent))
				destAccountingMarkazEntity.addToparents(destParent);
		}

		Set<MoeenAccountingMarkazEntity> srcMoeenAccountingMarkazs = srcAccountingMarkazEntity
				.getMoeenAccountingMarkaz();
		for (MoeenAccountingMarkazEntity srcMoeenAccoutingMarkazEntity : srcMoeenAccountingMarkazs) {
			HesabMoeenEntity srcHesabMoeenEntity = srcMoeenAccoutingMarkazEntity.getHesabMoeen();
//			if(hesabMoeenEntity == null)
//				hesabMoeenEntity = hesabMoeenService.createHesabMoeen(activeSaalMaaliEntity, entity.getHesabMoeen());

			HesabMoeenEntity destHesabMoeen = hesabMoeenService
					.getHesabMoeenByCodeAndSaalMaali(srcHesabMoeenEntity.getCode(), destSaalMaali);
			MoeenAccountingMarkazEntity destMoeenAccountingMarkazEntity = moeenAccountingMarkazService.load(
					destAccountingMarkazEntity, destHesabMoeen, srcMoeenAccoutingMarkazEntity.getLevel(),
					FlushMode.ALWAYS);
			if (destMoeenAccountingMarkazEntity == null) {
				destMoeenAccountingMarkazEntity = new MoeenAccountingMarkazEntity();
				destMoeenAccountingMarkazEntity.setAccountingMarkaz(destAccountingMarkazEntity);
				destMoeenAccountingMarkazEntity.setHesabMoeen(destHesabMoeen);

				destMoeenAccountingMarkazEntity.setLevel(srcMoeenAccoutingMarkazEntity.getLevel());
				moeenAccountingMarkazService.save(destMoeenAccountingMarkazEntity);
			}
		}

		update(destAccountingMarkazEntity);
		createOrUpdateRelatedAccountingMarkazTemplate(destAccountingMarkazEntity, destSaalMaali.getOrganId(),
				topOrganCode);
	}

	public AccountingMarkazEntity getAccountingMarkazByCodeAndSaalMaali(String hesabCode,
			SaalMaaliEntity saalMaaliEntity) {

		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", hesabCode);
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		AccountingMarkazEntity accountingMarkazEntity = load(localFilter);
		return accountingMarkazEntity;
	}

	@Override
	public void save(AccountingMarkazEntity entity) {
		if (!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("AccountingMarkaz_code"));
		super.save(entity);
	}
}