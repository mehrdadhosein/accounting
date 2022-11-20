package ir.serajsamaneh.accounting.hesabkol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.core.util.XMLUtil;
import ir.serajsamaneh.erpcore.contacthesab.ContactHesabEntity;
import ir.serajsamaneh.erpcore.contacthesab.ContactHesabService;
import ir.serajsamaneh.erpcore.util.HesabTreeUtil;
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class HesabKolService extends BaseEntityService<HesabKolEntity, Long> {

	@Override
	protected HesabKolDAO getMyDAO() {
		return hesabKolDAO;
	}

	@Autowired
	HesabKolDAO hesabKolDAO;
	@Autowired
	HesabMoeenService hesabMoeenService;
	@Autowired
	HesabTafsiliService hesabTafsiliService;
	@Autowired
	HesabGroupService hesabGroupService;
	@Autowired
	HesabKolTemplateService hesabKolTemplateService;
	@Autowired
	SaalMaaliService saalMaaliService;
	@Autowired
	AccountingMarkazService accountingMarkazService;
	@Autowired
	ContactHesabService contactHesabService;
	@Autowired
	AccountingMarkazTemplateService accountingMarkazTemplateService;
	@Autowired
	HesabTafsiliTemplateService hesabTafsiliTemplateService;
	@Autowired
	HesabGroupTemplateService hesabGroupTemplateService;

	@Override
	public String getDifferences(HesabKolEntity entity) {
		String diffes = "";
		HesabKolEntity oldEntity = (HesabKolEntity) entity.getOldEntity();
		if (entity.getCode() != null && !entity.getCode().equals(oldEntity.getCode()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabKol" + "_" + HesabKolEntity.PROP_CODE) + " : "
					+ oldEntity.getCode() + "" + " --> " + entity.getCode() + "" + "]";

		if (entity.getName() != null && !entity.getName().equals(oldEntity.getName()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabKol" + "_" + HesabKolEntity.PROP_NAME) + " : "
					+ oldEntity.getName() + "" + " --> " + entity.getName() + "" + "]";

		if (entity.getDescription() != null && !entity.getDescription().equals(oldEntity.getDescription()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabKol" + "_" + HesabKolEntity.PROP_DESCRIPTION) + " : "
					+ oldEntity.getDescription() + "" + " --> " + entity.getDescription() + "" + "]";

		if (entity.getHidden() != null && !entity.getHidden().equals(oldEntity.getHidden()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabKol" + "_" + HesabKolEntity.PROP_HIDDEN) + " : "
					+ oldEntity.getHidden() + "" + " --> " + entity.getHidden() + "" + "]";

		return diffes;
	}

	// @Override
	@Transactional
	public void save(HesabKolEntity entity, SaalMaaliEntity activeSaalMaaliEntity, Long currentOrganId,
			List<Long> topOrganList, String currentOrganName) {
		commonSave(entity, activeSaalMaaliEntity, currentOrganId, topOrganList, currentOrganName);

		save(entity);
		boolean isNew = (entity.getID() != null ? false : true);
		logAction(isNew, entity);
	}

	@Transactional
	public void saveStateLess(HesabKolEntity entity, SaalMaaliEntity activeSaalMaaliEntity, Long currentOrganId,
			List<Long> topOrganList, String currentOrganName) {
		commonSave(entity, activeSaalMaaliEntity, currentOrganId, topOrganList, currentOrganName);

		super.saveStateLess(entity);
	}

	private void commonSave(HesabKolEntity entity, SaalMaaliEntity activeSaalMaaliEntity, Long currentOrganId,
			List<Long> topOrganList, String currentOrganName) {

		if (!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("HesabKol_code"));

		if (entity.getId() != null && entity.getSaalMaali() != null && entity.getSaalMaali().getId() != null
				&& !entity.getSaalMaali().equals(activeSaalMaaliEntity))
			throw new FatalException(SerajMessageUtil.getMessage("SaalMaali_hesabConflict"));

		if (entity.getId() == null) {
			entity.setBedehkar(0d);
			entity.setBestankr(0d);
		}
		if (entity.getSaalMaali() == null || entity.getSaalMaali().getId() == null)
			entity.setSaalMaali(activeSaalMaaliEntity);
		if (entity.getHidden() == null)
			entity.setHidden(Boolean.FALSE);
		checkHesabUniqueNess(entity, activeSaalMaaliEntity, topOrganList);

		createHesabKolTemplateFromHesabKol(entity, currentOrganId, currentOrganName);

	}

	private void createHesabKolTemplateFromHesabKol(HesabKolEntity entity, Long organId, String organName) {
		HesabKolTemplateEntity hesabKolTemplateEntity = hesabKolTemplateService
				.loadByCodeInCurrentOrgan(entity.getCode(), organId);
//		if(hesabKolTemplateEntity == null){
//			HesabGroupEntity hesabGroupEntity = hesabGroupService.load(entity.getHesabGroup().getID());
//			hesabKolTemplateEntity = hesabKolTemplateService.createHesabKolTemplate(entity.getCode(), entity.getName(), hesabGroupEntity.getCode(), entity.getMahyatKol().name(), organEntity);
//		}else
//			hesabKolTemplateEntity.setName(entity.getName());

		if (hesabKolTemplateEntity == null) {
			hesabKolTemplateEntity = hesabKolTemplateService.loadByNameInCurrentOrgan(entity.getName(), organId);
			if (hesabKolTemplateEntity != null) {
				HesabKolTemplateEntity currentEntityHesabKolTemplate = entity.getHesabKolTemplate();

				// code of kol has changed
				if (currentEntityHesabKolTemplate.getId() != null
						&& currentEntityHesabKolTemplate.equals(hesabKolTemplateEntity)) {
					hesabKolTemplateEntity.setCode(entity.getCode().toString());
					hesabKolTemplateEntity.setName(entity.getName());
					hesabKolTemplateEntity.setDescription(entity.getDescription());
				} else
					throw new FatalException(SerajMessageUtil.getMessage(
							"HesabKolTemplate_cantCreateHesabKolTemplateWithDuplicateNameAndnewCode", entity.getCode(),
							entity.getName()));
			} else {
				if (entity.getHesabGroup() == null || entity.getHesabGroup().getID() == null)
					throw new FatalException(SerajMessageUtil.getMessage("HesabKol_hesabGroupNotDefined",
							entity.getName() + "_" + entity.getCode()));
				HesabGroupEntity hesabGroupEntity = hesabGroupService.load(entity.getHesabGroup().getID());
				hesabKolTemplateEntity = hesabKolTemplateService.createHesabKolTemplate(entity.getCode(),
						entity.getName(), hesabGroupEntity.getCode(), entity.getMahyatKol().name(), organId, organName);
			}

		} else {
			HesabKolTemplateEntity hesabKolTemplateEntityByName = hesabKolTemplateService
					.loadByNameInCurrentOrgan(entity.getName(), organId);
			if (hesabKolTemplateEntityByName != null
					&& !hesabKolTemplateEntityByName.getId().equals(hesabKolTemplateEntity.getId()))
				throw new FatalException(SerajMessageUtil.getMessage(
						"HesabKolTemplate_cantCreateHesabKolTemplateWithDuplicateNameAndnewCode", entity.getCode(),
						entity.getName()));

			hesabKolTemplateEntity.setCode(entity.getCode().toString());
			hesabKolTemplateEntity.setName(entity.getName());
			hesabKolTemplateEntity.setDescription(entity.getDescription());
		}

		hesabKolTemplateService.save(hesabKolTemplateEntity);
		entity.setHesabKolTemplate(hesabKolTemplateEntity);
		save(entity);
	}

	private void checkHesabUniqueNess(HesabKolEntity entity, SaalMaaliEntity activeSaalMaaliEntity,
			List<Long> topOrganList) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
//		localFilter.put("organId@eq", activeSaalMaaliEntity.getOrgan().getId());

//		List<Long> topOrganList = getTopOrgansIdList(currentOrgan);
		localFilter.put("organId@in", topOrganList);

		localFilter.put("saalMaali.id@eq", activeSaalMaaliEntity.getId());
		checkUniqueNess(entity, HesabKolEntity.PROP_NAME, entity.getName(), localFilter, false);
		checkUniqueNess(entity, HesabKolEntity.PROP_CODE, entity.getCode(), localFilter, false);
	}

	@Transactional
	public void updateValues(HesabKolEntity entity) {
		save(entity);
	}

	@Override
	public void save(HesabKolEntity entity) {
		if (!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(
					"(" + entity.getName() + " : " + SerajMessageUtil.getMessage("HesabKol_code") + ")");
		super.save(entity);
	}
//	public HesabKolEntity getHesabKolByCode(String hesabCode, OrganEntity organEntity) {
//
//		Map<String, Object> localFilter = new HashMap<String, Object>();
//		localFilter.put("code@eq", hesabCode);
//		localFilter.put("organId@eq", organEntity.getId());
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
		if (entity.getHidden() == null)
			entity.setHidden(false);
		super.saveOrUpdate(entity);
	}

	@Override
	public void saveOrUpdateStateLess(HesabKolEntity entity) {
		if (entity.getHidden() == null)
			entity.setHidden(false);
		super.saveOrUpdateStateLess(entity);
	}

	@Transactional(readOnly = false)
	public void importFromHesabKolTemplateList(SaalMaaliEntity activeSaalMaaliEntity, Long currentOrganId,
			List<Long> topOrganList, String currentOrganName) {

//		hesabKolTemplateService.createDefaultAccounts(activeSaalMaaliEntity.getOrgan());

		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organId@eq", activeSaalMaaliEntity.getOrganId());
		List<HesabKolTemplateEntity> dataList = hesabKolTemplateService.getDataList(null, localFilter);

		for (HesabKolTemplateEntity hesabKolTemplateEntity : dataList) {
			HesabKolEntity loadHesabKol = loadHesabKolByTemplate(hesabKolTemplateEntity, activeSaalMaaliEntity);
			if (loadHesabKol == null) {
				try {
					createHesabKol(activeSaalMaaliEntity, hesabKolTemplateEntity, currentOrganId, topOrganList,
							currentOrganName);
				} catch (DuplicateException e) {
					e.printStackTrace();
				}
			} else {
				loadHesabKol.setCode(hesabKolTemplateEntity.getCode());
				loadHesabKol.setName(hesabKolTemplateEntity.getName());
				save(loadHesabKol);
			}
		}

	}

	public HesabKolEntity createHesabKol(SaalMaaliEntity activeSaalMaaliEntity,
			HesabKolTemplateEntity hesabKolTemplateEntity, Long currentOrganId, List<Long> topOrganList,
			String currentOrganName) {
		HesabKolEntity hesabKolEntity = loadHesabKolByTemplate(hesabKolTemplateEntity, activeSaalMaaliEntity);
		if (hesabKolEntity == null) {
			hesabKolEntity = populateHesabKolEntity(activeSaalMaaliEntity, hesabKolTemplateEntity);
			save(hesabKolEntity, activeSaalMaaliEntity, currentOrganId, topOrganList, currentOrganName);
		}
		return hesabKolEntity;
	}

	public HesabKolEntity createHesabKol(SaalMaaliEntity destSaalMaaliEntity, HesabKolEntity srcHesabKolEntity,
			List<Long> topOrganList) {
		HesabKolEntity hesabKolEntity = loadHesabKolByCode(srcHesabKolEntity.getCode(), destSaalMaaliEntity.getId());
		if (hesabKolEntity == null)
			hesabKolEntity = loadHesabKolByName(srcHesabKolEntity.getName(), destSaalMaaliEntity, FlushMode.MANUAL);
		if (hesabKolEntity == null) {
			hesabKolEntity = populateHesabKolEntity(destSaalMaaliEntity, srcHesabKolEntity,
					srcHesabKolEntity.getOrganId());
			save(hesabKolEntity, destSaalMaaliEntity, srcHesabKolEntity.getOrganId(), topOrganList,
					srcHesabKolEntity.getOrganName());
		}
		return hesabKolEntity;
	}

	public HesabKolEntity createHesabKolStateLess(SaalMaaliEntity activeSaalMaaliEntity,
			HesabKolTemplateEntity hesabKolTemplateEntity, Long currentOrganId, List<Long> topOrganList,
			String currentOrganName) {
		HesabKolEntity hesabKolEntity = loadHesabKolByTemplate(hesabKolTemplateEntity, activeSaalMaaliEntity);
		if (hesabKolEntity == null) {
			hesabKolEntity = populateHesabKolEntity(activeSaalMaaliEntity, hesabKolTemplateEntity);
			saveStateLess(hesabKolEntity, activeSaalMaaliEntity, currentOrganId, topOrganList, currentOrganName);
		}
		return hesabKolEntity;
	}

	public HesabKolEntity createHesabKolStateLess(SaalMaaliEntity destSaalMaaliEntity, HesabKolEntity srcHesabKolEntity,
			List<Long> topOrganList) {
		HesabKolEntity hesabKolEntity = loadHesabKolByCode(srcHesabKolEntity.getCode(), destSaalMaaliEntity.getId());
		if (hesabKolEntity == null)
			hesabKolEntity = loadHesabKolByName(srcHesabKolEntity.getName(), destSaalMaaliEntity, FlushMode.MANUAL);

		if (hesabKolEntity == null) {
			hesabKolEntity = populateHesabKolEntity(destSaalMaaliEntity, srcHesabKolEntity,
					srcHesabKolEntity.getOrganId());
			saveStateLess(hesabKolEntity, destSaalMaaliEntity, srcHesabKolEntity.getOrganId(), topOrganList,
					srcHesabKolEntity.getOrganName());
		}
		return hesabKolEntity;
	}

	private HesabKolEntity populateHesabKolEntity(SaalMaaliEntity destSaalMaaliEntity, HesabKolEntity srcHesabKolEntity,
			Long organId) {
		HesabKolEntity hesabKolEntity;
		hesabKolEntity = new HesabKolEntity();
		hesabKolEntity.setHesabGroup(srcHesabKolEntity.getHesabGroup());
		hesabKolEntity.setCode(srcHesabKolEntity.getCode());
		hesabKolEntity.setDescription(srcHesabKolEntity.getDescription());
		hesabKolEntity.setHidden(srcHesabKolEntity.getHidden());
		hesabKolEntity.setMahyatKol(srcHesabKolEntity.getMahyatKol());
		hesabKolEntity.setName(srcHesabKolEntity.getName());
		hesabKolEntity.setOrganId(organId);
		hesabKolEntity.setSaalMaali(destSaalMaaliEntity);
		hesabKolEntity.setBedehkar(0d);
		hesabKolEntity.setBestankr(0d);
		hesabKolEntity.setHesabKolTemplate(srcHesabKolEntity.getHesabKolTemplate());
		return hesabKolEntity;
	}

	private HesabKolEntity populateHesabKolEntity(SaalMaaliEntity activeSaalMaaliEntity,
			HesabKolTemplateEntity hesabKolTemplateEntity) {
		HesabKolEntity hesabKolEntity;
		hesabKolEntity = new HesabKolEntity();
		if (hesabKolTemplateEntity.getHesabGroupTemplate() != null)
			hesabKolEntity.setHesabGroup(hesabGroupService
					.loadHesabByCode(hesabKolTemplateEntity.getHesabGroupTemplate().getCode(), activeSaalMaaliEntity));

		hesabKolEntity.setCode(hesabKolTemplateEntity.getCode());
		hesabKolEntity.setDescription(hesabKolTemplateEntity.getDescription());
		hesabKolEntity.setHidden(hesabKolTemplateEntity.getHidden());
		hesabKolEntity.setMahyatKol(hesabKolTemplateEntity.getMahyatKol());
		hesabKolEntity.setName(hesabKolTemplateEntity.getName());
		hesabKolEntity.setOrganId(activeSaalMaaliEntity.getOrganId());
		hesabKolEntity.setSaalMaali(activeSaalMaaliEntity);
		hesabKolEntity.setBedehkar(0d);
		hesabKolEntity.setBestankr(0d);
		hesabKolEntity.setHesabKolTemplate(hesabKolTemplateEntity);
		return hesabKolEntity;
	}

	public HesabKolEntity loadHesabKolByTemplate(HesabKolTemplateEntity hesabKolTemplateEntity,
			SaalMaaliEntity activeSaalMaaliEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("hesabKolTemplate.id@eq", hesabKolTemplateEntity.getId());
//		localFilter.put("organId@eq",activeSaalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq", activeSaalMaaliEntity.getId());
		HesabKolEntity hesabKolEntity = load(null, localFilter);
		return hesabKolEntity;
	}

	public HesabKolEntity loadHesabKolByCode(String code, Long saalMaaliId) {
		return loadHesabKolByCode(code, saalMaaliId, FlushMode.MANUAL);
	}

	public HesabKolEntity loadHesabKolByCode(String code, Long saalMaaliId, FlushMode flushMode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
//		localFilter.put("organId@eq",saalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq", saalMaaliId);
		return load(null, localFilter, flushMode);
//		List<HesabKolEntity> dataList = getDataList(null, localFilter, flushMode);
//		if(dataList.size() == 1)
//			return dataList.get(0);
//		if(dataList.size() == 0)
//			return null;
//		throw new FatalException("More Than one Record Found");
	}

	public HesabKolEntity loadHesabKolByName(String name, SaalMaaliEntity saalMaaliEntity, FlushMode flushMode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq", name);
//		localFilter.put("organId@eq",saalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		return load(null, localFilter, flushMode);
//		List<HesabKolEntity> dataList = getDataList(null, localFilter, flushMode);
//		if(dataList.size() == 1)
//			return dataList.get(0);
//		if(dataList.size() == 0)
//			return null;
//		throw new FatalException("More Than one Record Found");
	}

	public List<HesabVO> createHesabHierarchy(SaalMaaliEntity activeSaalMaaliEntity, List<Long> topOrganList) {
		List<HesabKolEntity> hesabKolList = getHesabKolList(activeSaalMaaliEntity, topOrganList);

		List<HesabVO> hesabVOs = new ArrayList<HesabVO>();
		for (HesabKolEntity hesabKolEntity : hesabKolList) {
			HesabVO hesabKolVO = new HesabVO(hesabKolEntity, HesabKolEntity.class.getSimpleName(),
					"folder_vector16.png");
			List<HesabMoeenEntity> activeMoeens = hesabMoeenService.getActiveMoeens(hesabKolEntity.getId(),
					activeSaalMaaliEntity, topOrganList);
			HesabTreeUtil.addHesabMoeensToHesabHierarchy(hesabKolVO, activeMoeens, hesabVOs, activeSaalMaaliEntity,
					topOrganList);
			hesabVOs.add(hesabKolVO);
		}
		return hesabVOs;
	}

	public List<HesabKolEntity> getHesabKolList(SaalMaaliEntity saalMaaliEntity, List<Long> topOrganList) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();

//		List<Long> topOrganList = getTopOrgansIdList(curentOrgan);
		localFilter.put("organId@in", topOrganList);

		localFilter.put("hidden@eq", Boolean.FALSE);
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		List<HesabKolEntity> hesabKolList = getDataList(null, localFilter, HesabKolEntity.PROP_CODE, true, false);
		return hesabKolList;
	}

	public List<HesabKolEntity> getHesabKolList(SaalMaaliEntity saalMaaliEntity) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();

		localFilter.put("hidden@eq", Boolean.FALSE);
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		List<HesabKolEntity> hesabKolList = getDataList(null, localFilter, HesabKolEntity.PROP_CODE, true, false);
		return hesabKolList;
	}

	@Transactional(readOnly = false)
	public void disableHesab(Long hesabId) {
		HesabKolEntity hesabKolEntity = load(hesabId);
		hesabKolEntity.setHidden(true);
		super.save(hesabKolEntity);
	}

	@Transactional(readOnly = false)
	public void copyAccountingMarkazhaFromSourceSaalMaaliToDestSaalMaali(SaalMaaliEntity srcSaalMaali,
			SaalMaaliEntity destSaalMaali, String topOrganCode) {
		List<AccountingMarkazEntity> srcActiveAccountingMarkaz = accountingMarkazService
				.getActiveAccountingMarkaz(srcSaalMaali);
		for (AccountingMarkazEntity srcAccountingMarkazEntity : srcActiveAccountingMarkaz) {
			AccountingMarkazEntity destAccountingMarkazEntity = accountingMarkazService.loadAccountingMarkazByCode(
					srcAccountingMarkazEntity.getCode(), destSaalMaali.getId(), FlushMode.ALWAYS);
			if (destAccountingMarkazEntity == null || destAccountingMarkazEntity.getId() == null) {
				destAccountingMarkazEntity = accountingMarkazService.createAccountingMarkaz(destSaalMaali,
						srcAccountingMarkazEntity, topOrganCode);
			}

			AccountingMarkazTemplateEntity accountingMarkazTemplateEntity = accountingMarkazTemplateService
					.load(destAccountingMarkazEntity.getCode(), destSaalMaali.getOrganId());
			if (accountingMarkazTemplateEntity == null)
				accountingMarkazTemplateEntity = accountingMarkazTemplateService.createAccountingMarkazTemplate(
						destAccountingMarkazEntity.getCode(), destAccountingMarkazEntity.getName(),
						destSaalMaali.getOrganId());
		}

		for (AccountingMarkazEntity srcAccountingMarkazEntity : srcActiveAccountingMarkaz) {
			AccountingMarkazEntity destAccountingMarkazEntity = accountingMarkazService.loadAccountingMarkazByCode(
					srcAccountingMarkazEntity.getCode(), destSaalMaali.getId(), FlushMode.ALWAYS);
			accountingMarkazService.createAccountingMarkazRelatedEntities(srcAccountingMarkazEntity,
					destAccountingMarkazEntity, destSaalMaali, topOrganCode);
		}

	}

	@Transactional(readOnly = false)
	public void copycontactHesabsFromSourceSaalMaaliToDestSaalMaali(SaalMaaliEntity srcSaalMaali,
			SaalMaaliEntity destSaalMaali) {

		List<ContactHesabEntity> contactHesabList = contactHesabService.getListBySaalMaali(srcSaalMaali);
		for (ContactHesabEntity contactHesabEntity : contactHesabList) {
			try {
				contactHesabService.getContactHesabByContactIdAndSaalMaali(contactHesabEntity.getContact().getId(),
						destSaalMaali.getId());
			} catch (NoRecordFoundException e) {

				HesabMoeenEntity destHsabMoeenEntity = null;
				HesabTafsiliEntity destHesabTafsiliEntity = null;

				if (contactHesabEntity.getHesabMoeen() != null && contactHesabEntity.getHesabMoeen().getId() != null)
					destHsabMoeenEntity = hesabMoeenService.getHesabMoeenByCodeAndSaalMaali(
							contactHesabEntity.getHesabMoeen().getCode(), destSaalMaali);
				if (contactHesabEntity.getHesabTafsiliOne() != null
						&& contactHesabEntity.getHesabTafsiliOne().getId() != null)
					destHesabTafsiliEntity = hesabTafsiliService.getHesabTafsiliByCodeAndSaalMaali(
							contactHesabEntity.getHesabTafsiliOne().getCode(), destSaalMaali);

				contactHesabService.createContactHesab(contactHesabEntity.getAccountingMarkazTemplate(),
						contactHesabEntity.getContact(), destHsabMoeenEntity, destHesabTafsiliEntity, destSaalMaali);
			}
		}
	}

	public void copyHesabTafsiliRelatedEntities(SaalMaaliEntity srcSaalMaali, SaalMaaliEntity destSaalMaali,
			List<Long> topOrganList) {
		List<HesabTafsiliEntity> srcActiveTafsilis = hesabTafsiliService.getActiveTafsilis(srcSaalMaali);
		for (HesabTafsiliEntity srcHesabTafsiliEntity : srcActiveTafsilis) {
			HesabTafsiliEntity destHesabTafsiliEntity = hesabTafsiliService
					.loadHesabTafsiliByCode(srcHesabTafsiliEntity.getCode(), destSaalMaali.getId(), FlushMode.MANUAL);
			hesabTafsiliService.copyHesabTafsiliRelatedEntities(srcHesabTafsiliEntity, destHesabTafsiliEntity,
					destSaalMaali, topOrganList);

		}
	}

	@Transactional(readOnly = false)
	public void copyHesabTafsilissFromSourceSaalMaaliToDestSaalMaali(SaalMaaliEntity srcSaalMaali,
			SaalMaaliEntity destSaalMaali, List<Long> topOrganList, String topOrganCode) {
		List<HesabTafsiliEntity> srcActiveTafsilis = hesabTafsiliService.getActiveTafsilis(srcSaalMaali);
		for (HesabTafsiliEntity srcHesabTafsiliEntity : srcActiveTafsilis) {

			HesabTafsiliEntity destHesabTafsiliEntity = hesabTafsiliService
					.loadHesabTafsiliByCode(srcHesabTafsiliEntity.getCode(), destSaalMaali.getId(), FlushMode.ALWAYS);

			if (destHesabTafsiliEntity == null) {
				destHesabTafsiliEntity = hesabTafsiliService.loadHesabTafsiliByName(srcHesabTafsiliEntity.getName(),
						destSaalMaali, FlushMode.ALWAYS);
				if (destHesabTafsiliEntity != null) {
					// throw new
					// FatalException(SerajMessageUtil.getMessage("HesabTafsili_cantImportHesabWithDuplicateNameAndnewCode",
					// srcHesabTafsiliEntity.getCode(),destHesabTafsiliEntity.getDesc(),
					// srcHesabTafsiliEntity.getOrgan()));
					new FatalException(SerajMessageUtil.getMessage(
							"HesabTafsili_cantImportHesabWithDuplicateNameAndnewCode", srcHesabTafsiliEntity.getCode(),
							destHesabTafsiliEntity.getDesc(), srcHesabTafsiliEntity.getOrganId())).printStackTrace();
					continue;
				}

			}

			if (destHesabTafsiliEntity == null || destHesabTafsiliEntity.getId() == null) {
//				try{ 
				destHesabTafsiliEntity = hesabTafsiliService.createHesabTafsili(destSaalMaali, srcHesabTafsiliEntity,
						srcHesabTafsiliEntity.getOrganId(), topOrganList, topOrganCode,
						srcHesabTafsiliEntity.getOrganName());
//				}catch(DuplicateException e){
//					System.out.println(e.getDesc());
//					continue;//not important exception
//				}
			} else
				hesabTafsiliService.createOrUpdateRelatedHesabTafsiliTemplate(srcHesabTafsiliEntity,
						srcHesabTafsiliEntity.getOrganId(), topOrganList, srcHesabTafsiliEntity.getOrganName());

		}
	}

	@Transactional(readOnly = false)
	public void copyHesabKolsFromSourceSaalMaaliToDestSaalMaali(SaalMaaliEntity srcSaalMaali,
			SaalMaaliEntity destSaalMaali, List<Long> topOrganList) {
		List<HesabKolEntity> hesabKolList = getHesabKolList(srcSaalMaali);
		for (HesabKolEntity srcHesabKolEntity : hesabKolList) {

			HesabKolEntity destHesabKol = loadHesabKolByCode(srcHesabKolEntity.getCode(), destSaalMaali.getId(),
					FlushMode.MANUAL);
			if (destHesabKol == null) {
				destHesabKol = loadHesabKolByName(srcHesabKolEntity.getName(), destSaalMaali, FlushMode.MANUAL);
				if (destHesabKol != null)
					throw new FatalException(
							SerajMessageUtil.getMessage("HesabKol_cantImportHesabWithDuplicateNameAndnewCode",
									srcHesabKolEntity.getCode(), destHesabKol.getName()));

			}

			if (destHesabKol == null) {
				try {
//					destHesabKol = createHesabKolStateLess(destSaalMaali, srcHesabKolEntity); 
					destHesabKol = createHesabKol(destSaalMaali, srcHesabKolEntity, topOrganList);
				} catch (DuplicateException e) {
					System.out.println(e.getDesc());
					continue;
				}
			}

			createHesabKolTemplateFromHesabKol(srcHesabKolEntity, srcSaalMaali.getOrganId(),
					srcSaalMaali.getOrganName());
		}
	}

	public void copyHesabMoeensFromSourceSaalMaaliToDestSaalMaali(SaalMaaliEntity srcSaalMaali,
			SaalMaaliEntity destSaalMaali, List<Long> topOrganList) {
		List<HesabMoeenEntity> activeMoeens = hesabMoeenService.getActiveMoeens(srcSaalMaali);
		for (HesabMoeenEntity srcHesabMoeenEntity : activeMoeens) {
			srcHesabMoeenEntity = hesabMoeenService.load(srcHesabMoeenEntity.getId());
			if (srcHesabMoeenEntity.getHesabKol() == null || srcHesabMoeenEntity.getHesabKol().getId() == null) {
				throw new FatalException(SerajMessageUtil.getMessage("HesabMoeen_hesabKolNotDefined",
						srcHesabMoeenEntity, srcSaalMaali));
			}

			HesabMoeenEntity destHesabMoeenEntity = hesabMoeenService.loadHesabMoeenByCode(
					srcHesabMoeenEntity.getCode(), destSaalMaali, FlushMode.MANUAL, srcHesabMoeenEntity.getOrganId());
			if (destHesabMoeenEntity == null) {
				destHesabMoeenEntity = hesabMoeenService.loadHesabMoeenByName(srcHesabMoeenEntity.getName(),
						destSaalMaali, FlushMode.MANUAL, srcHesabMoeenEntity.getOrganId());
				if (destHesabMoeenEntity != null)
					throw new FatalException(
							SerajMessageUtil.getMessage("HesabMoeen_cantImportHesabWithDuplicateNameAndnewCode",
									srcHesabMoeenEntity.getCode(), destHesabMoeenEntity.getName()));
			}

			if (destHesabMoeenEntity == null || destHesabMoeenEntity.getId() == null) {
//				try{
				destHesabMoeenEntity = hesabMoeenService.createHesabMoeen(destSaalMaali, srcHesabMoeenEntity,
						srcHesabMoeenEntity.getOrganId(), topOrganList, srcHesabMoeenEntity.getOrganName());
//				}catch(DuplicateException e){
//					System.out.println(e.getDesc());
//					continue;
//				}
			}
			hesabMoeenService.createOrUpdateRelatedHesabMoeenTemplate(srcHesabMoeenEntity,
					srcHesabMoeenEntity.getOrganId(), srcHesabMoeenEntity.getOrganName());
		}
	}

	@Transactional(readOnly = true)
	public List<ListOrderedMap<String, Object>> getRootHesabs(SaalMaaliEntity saalMaaliEntity, Long organId) {
		List<ListOrderedMap<String, Object>> rootHesabsList;
		List<HesabMoeenEntity> rootHesabMoeens = hesabMoeenService.getRootHesabs(saalMaaliEntity);

		rootHesabsList = new ArrayList<ListOrderedMap<String, Object>>();

		for (HesabMoeenEntity hesabMoeenEntity : rootHesabMoeens) {
			ListOrderedMap<String, Object> hesabMoeenMap = new ListOrderedMap<String, Object>();
			hesabMoeenMap.put("value", "Moeen_" + hesabMoeenEntity.getId());
			hesabMoeenMap.put("label", hesabMoeenEntity.getDesc());
			rootHesabsList.add(hesabMoeenMap);
		}
		List<HesabTafsiliEntity> rootHesabTafsilies = hesabTafsiliService.getRootTafsiliOneHesabs(saalMaaliEntity);
		for (HesabTafsiliEntity hesabTafsiliEntity : rootHesabTafsilies) {
			ListOrderedMap<String, Object> hesabTafsiliMap = new ListOrderedMap<String, Object>();
			hesabTafsiliMap.put("value", "Tafsili_" + hesabTafsiliEntity.getId());
			hesabTafsiliMap.put("label", hesabTafsiliEntity.getDesc());
			rootHesabsList.add(hesabTafsiliMap);
		}
//		List<HesabTafsiliEntity> rootHesabShenavars = hesabTafsiliService.getRootShenavarHesabs(saalMaaliEntity, currentOrgan);
//		for (HesabTafsiliEntity hesabTafsiliEntity : rootHesabShenavars) {
//			ListOrderedMap<String, Object> hesabTafsiliMap = new ListOrderedMap<String, Object>();
//			hesabTafsiliMap.put("value","Shenavar_"+hesabTafsiliEntity.getId());
//			hesabTafsiliMap.put("label",hesabTafsiliEntity.getDesc());
//			rootHesabsList.add(hesabTafsiliMap);
//		}
		return rootHesabsList;
	}

	public void createDefaultAccounts(Long organId, String organName) throws NumberFormatException {
		InputStream fileInputStream;
		URL resource = getClass().getResource("/config/accounts");
		if (resource == null)
			return;
		File dir = new File(resource.getFile());

		FilenameFilter filter = new WildcardFileFilter("general-accounts.xml");
		String[] list = dir.list(filter);

		for (String fileName : list) {
			String localFilePath = dir.getAbsolutePath() + "/" + fileName;
			try {
				fileInputStream = new FileInputStream(localFilePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new IllegalStateException();
			}
			// parse XML file -> XML document will be build
			Document doc = XMLUtil.parseFile(fileInputStream);
			NodeList rootNodes = doc.getElementsByTagName("accounts");
			Node item = rootNodes.item(0);
			Element accounts = (Element) item;

			NodeList childNodes = accounts.getChildNodes();
			createDefaultAccounts(childNodes, organId, organName);
		}

	}

	public void createDefaultAccounts(NodeList childNodes, Long organId, String organName) {
		for (int s = 0; s < childNodes.getLength(); s++) {
			try {
				Node accountNode = childNodes.item(s);
				if (accountNode.getNodeType() == Node.ELEMENT_NODE) {
					Element nodeElem = (Element) accountNode;
					if (nodeElem.getTagName().equals("HesabGroup")) {
						hesabKolTemplateService.createHesabGroup(nodeElem, organId, organName);
					} else if (nodeElem.getTagName().equals("HesabKol")) {
						hesabKolTemplateService.createHesabKolTemplate(nodeElem, organId, organName);
					} else if (nodeElem.getTagName().equals("HesabMoeen")) {
						hesabKolTemplateService.createHesabMoeenTemplate(nodeElem, organId, organName);
					} else if (nodeElem.getTagName().equals("HesabTafsili")) {
						hesabKolTemplateService.createHesabTafsiliTemplate(nodeElem, organId, organName);
					}

				}
			} catch (DuplicateException e) {
				System.out.println(e.getDesc());
			}
		}
	}

}