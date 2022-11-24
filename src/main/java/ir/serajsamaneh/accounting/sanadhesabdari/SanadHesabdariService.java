package ir.serajsamaneh.accounting.sanadhesabdari;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity;
import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazService;
import ir.serajsamaneh.accounting.base.BaseAccountingService;
import ir.serajsamaneh.accounting.enumeration.HesabTypeEnum;
import ir.serajsamaneh.accounting.enumeration.MahyatGroupEnum;
import ir.serajsamaneh.accounting.enumeration.MahyatKolEnum;
import ir.serajsamaneh.accounting.enumeration.SanadFunctionEnum;
import ir.serajsamaneh.accounting.enumeration.SanadStateEnum;
import ir.serajsamaneh.accounting.exception.ConflictSaalMaaliTarikhException;
import ir.serajsamaneh.accounting.exception.MahyatKolNotDefinedException;
import ir.serajsamaneh.accounting.exception.NoSaalMaaliFoundException;
import ir.serajsamaneh.accounting.exception.SanadIsNotBalancedException;
import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.hesabkol.HesabKolService;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenDAO;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliDAO;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliService;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateService;
import ir.serajsamaneh.accounting.month.MonthEntity;
import ir.serajsamaneh.accounting.month.MonthService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity;
import ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemService;
import ir.serajsamaneh.accounting.sanadtype.SanadTypeEntity;
import ir.serajsamaneh.core.common.OrganVO;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.exception.RequiredFieldNotSetException;
import ir.serajsamaneh.core.file.FileEntity;
import ir.serajsamaneh.core.tempuploadedfile.TempUploadedFileEntity;
import ir.serajsamaneh.core.user.UserEntity;
import ir.serajsamaneh.core.util.ActionLogUtil;
import ir.serajsamaneh.core.util.NumberUtil;
import ir.serajsamaneh.core.util.SerajLogger;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.enumeration.ActionTypeEnum;
import ir.serajsamaneh.enumeration.SaalMaaliStatusEnum;
import ir.serajsamaneh.enumeration.YesNoEnum;
import ir.serajsamaneh.erpcore.util.AutomaticSanadUtil;
import serajcomponent.DateConverter;
import serajcomponent.SerajDatePartEnum;
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SanadHesabdariService extends BaseAccountingService<SanadHesabdariEntity, Long> {

	@Override
	protected SanadHesabdariDAO getMyDAO() {
		return sanadHesabdariDAO;
	}

	@Autowired
	SanadHesabdariDAO sanadHesabdariDAO;
	@Autowired
	HesabTafsiliDAO hesabTafsiliDAO;
	@Autowired
	HesabMoeenDAO hesabMoeenDAO;
	@Autowired
	SaalMaaliService saalMaaliService;
	@Autowired
	HesabKolService hesabKolService;
	@Autowired
	HesabMoeenService hesabMoeenService;
	@Autowired
	HesabTafsiliService hesabTafsiliService;
	@Autowired
	HesabMoeenTemplateService hesabMoeenTemplateService;
	@Autowired
	HesabTafsiliTemplateService hesabTafsiliTemplateService;
	@Autowired
	AccountingMarkazService accountingMarkazService;
	@Autowired
	SanadHesabdariItemService sanadHesabdariItemService;
	@Autowired
	MonthService monthService;


	protected static Map<Long, Long> organSanadHesabdariIds = new HashMap<Long, Long>();

	@Transactional(readOnly = false)
	public synchronized Long getNextSanadHesabdariSerial(SaalMaaliEntity saalMaaliEntity, Long organId) {
//		if (organSanadHesabdariIds.get(saalMaaliEntity.getId()) == null) {
//			Long maxSerial = null;
//			maxSerial = sanadHesabdariDAO.getMaxSanadSerial(saalMaaliEntity);
//			if (maxSerial == null) {
//				maxSerial = 1l;
//			} else
//				++maxSerial;
//			organSanadHesabdariIds.put(saalMaaliEntity.getId(), maxSerial);
//		}
//		Long currentMaxSerial = organSanadHesabdariIds.get(saalMaaliEntity.getId());
//		checkSanadHesabdariSerialUniqueNess(currentMaxSerial, saalMaaliEntity);
//		organSanadHesabdariIds.put(saalMaaliEntity.getId(), currentMaxSerial + 1);
//
//		return currentMaxSerial;

		Long maxSerial = null;
		maxSerial = sanadHesabdariDAO.getMaxSanadSerial(saalMaaliEntity, organId);
		return ++maxSerial;
	}

	protected static Map<Long, Long> organTempSanadHesabdariIds = new HashMap<Long, Long>();

	@Transactional(readOnly = false)
	public synchronized Long getNextTempSanadHesabdariSerial(SaalMaaliEntity saalMaaliEntity) {
		if (organTempSanadHesabdariIds.get(saalMaaliEntity.getId()) == null) {
			Long maxTempSerial = null;
			maxTempSerial = sanadHesabdariDAO.getMaxTempSanadSerial(saalMaaliEntity);
			if (maxTempSerial == null) {
				maxTempSerial = 1l;
			} else
				++maxTempSerial;
			organTempSanadHesabdariIds.put(saalMaaliEntity.getId(), maxTempSerial);
		}
		Long currentMaxTempSerial = organTempSanadHesabdariIds.get(saalMaaliEntity.getId());
		// checkSanadHesabdariSerialUniqueNess(currentMaxTempSerial,organEntity.getId());
		organTempSanadHesabdariIds.put(saalMaaliEntity.getId(), currentMaxTempSerial + 1);

		return currentMaxTempSerial;
	}

//	private void checkSanadHesabdariSerialUniqueNess(Long newSerial, SaalMaaliEntity saalMaaliEntity) {
//		Map<String, Object> filter = new HashMap<String, Object>();
//		filter.put("serial@eq", newSerial);
//		filter.put("saalMaali.id@eq", saalMaaliEntity.getId());
//		Integer rowCount = getRowCount(filter);
//		if (rowCount >0)
//			throw new FatalException("serial_already_generated");
//	}

	@Transactional
	public void save(SanadHesabdariEntity entity, String organDesc) {
		save(entity, null, true, organDesc);
	}

	@Transactional
	public void save(SanadHesabdariEntity entity, boolean validateHesabDependencies, String organDesc) {
		save(entity, null, validateHesabDependencies, organDesc);
	}

	@Transactional
	public void save(SanadHesabdariEntity entity, Long organId, String organDesc) {
		save(entity, organId, null, true, organDesc);
	}

	@Transactional
	public void save(SanadHesabdariEntity entity, Long organId, boolean validateHesabDependencies, String organDesc) {
		save(entity, organId, null, validateHesabDependencies, organDesc);
	}

	@Transactional
	public void save(SanadHesabdariEntity entity, Long organId, SaalMaaliEntity saalMaaliEntity, String organDesc) {
		save(entity, organId, saalMaaliEntity, true, organDesc);
	}

	@Transactional
	public void save(SanadHesabdariEntity entity, Long organId, SaalMaaliEntity saalMaaliEntity,
			boolean validateHesabDependencies, String organDesc) {
		boolean validateSaalMaaliInProgress = true;
		if (entity.getSanadFunction().equals(SanadFunctionEnum.EKHTETAMIE))
			validateSaalMaaliInProgress = false;
		save(entity, organId, saalMaaliEntity, validateHesabDependencies, validateSaalMaaliInProgress, organDesc);
	}

	@Transactional
	public void save(SanadHesabdariEntity entity, Long organId, SaalMaaliEntity saalMaaliEntity,
			boolean validateHesabDependencies, boolean validateSaalMaaliInProgress, String organName) {

		if (entity.getSanadFunction() == null)
			entity.setSanadFunction(SanadFunctionEnum.OMOMI);
		if (entity.getTarikhSanad() == null)
			entity.setTarikhSanad(new Date());
		if (entity.getDeletable() == null)
			entity.setDeletable(YesNoEnum.YES);

		if (saalMaaliEntity != null)
			entity.setSaalMaali(saalMaaliEntity);

		if (entity.getSaalMaali() == null || entity.getSaalMaali().getId() == null)
			throw new RequiredFieldNotSetException(SerajMessageUtil.getMessage("SaalMaali_title"));

		entity.setSaalMaali(saalMaaliService.load(entity.getSaalMaali().getId()));

		if (entity.getTarikhSanad().after(entity.getSaalMaali().getEndDate())
				|| entity.getTarikhSanad().before(entity.getSaalMaali().getStartDate())) {
			Integer tarikhSanadYear = DateConverter.getPersianDatePart(entity.getTarikhSanad())
					.get(SerajDatePartEnum.YEAR);
			throw new ConflictSaalMaaliTarikhException(entity.getSaalMaali().getSaal(), tarikhSanadYear);
		}

		if (validateHesabDependencies) {
			OrganVO organVO = organService.getOrganVO(organId);
			checkHesabDependencies(entity, organVO);
		}

		checkSaalMaaliAndTarikhSanadConflict(entity);

		if (entity.getId() == null) {
			entity.setOrganId(organId);
			entity.setOrganName(organName);
		}

		if (entity.getTempSerial() == null && !entity.getState().equals(SanadStateEnum.TEMP)) {
			entity.setTempSerial(getNextTempSanadHesabdariSerial(entity.getSaalMaali()));
			Map<String, Object> localFilter = new HashMap<String, Object>();
			localFilter.put("tempSerial@eq", entity.getTempSerial());
			localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());

			checkUniqueNess(entity, localFilter, false);
		}

		if (entity.getSanadType() == null || entity.getSanadType().getId() == null)
			entity.setSanadType(getDefaultSanadType(organId));

		updateBedehkarBestankarSum(entity);

		if (validateSaalMaaliInProgress) {
			checkSaalMaaliIsInProgress(entity.getSaalMaali(), entity);
		}

		super.save(entity);

	}

	public SanadTypeEntity getDefaultSanadType(Long organId) {
		String defaultSanadTypeId = systemConfigService.getValue(organId, null, "defaultSanadTypeId");
		if (defaultSanadTypeId == null)
			throw new FatalException(SerajMessageUtil.getMessage("AccountingNotConfigured"));
		return sanadTypeService.load(Long.valueOf(defaultSanadTypeId));
	}

	private void updateBedehkarBestankarSum(SanadHesabdariEntity entity) {

		Double bedehkarSum = 0d;
		Double bestankarSum = 0d;
		if (entity.getSanadHesabdariItem() != null)
			for (SanadHesabdariItemEntity sanadHesabdariItemEntity : entity.getSanadHesabdariItem())
				bedehkarSum += sanadHesabdariItemEntity.getBedehkar();
		if (entity.getSanadHesabdariItem() != null)
			for (SanadHesabdariItemEntity sanadHesabdariItemEntity : entity.getSanadHesabdariItem())
				bestankarSum += sanadHesabdariItemEntity.getBestankar();

		entity.setBedehkarSum(bedehkarSum);
		entity.setBestankarSum(bestankarSum);
	}

	@Transactional
	public void saveTemp(SanadHesabdariEntity entity, List<TempUploadedFileEntity> zamimeha, Long organId,
			SaalMaaliEntity saalMaaliEntity, boolean validateSaalMaaliInProgress, UserEntity currentUser,
			String organDesc) {
		entity.setState(SanadStateEnum.TEMP);
		saveTempORMovaghat(entity, zamimeha, organId, saalMaaliEntity, validateSaalMaaliInProgress, currentUser,
				organDesc);
	}

	@Transactional
	public void saveMovaghat(SanadHesabdariEntity entity, List<TempUploadedFileEntity> zamimeha, Long organId,
			SaalMaaliEntity saalMaaliEntity, UserEntity currentUser, String organDesc) {
		saveMovaghat(entity, zamimeha, organId, saalMaaliEntity, true, currentUser, organDesc);
	}

	@Transactional
	public void saveMovaghat(SanadHesabdariEntity entity, List<TempUploadedFileEntity> zamimeha, Long organId,
			SaalMaaliEntity saalMaaliEntity, boolean validateSaalMaaliInProgress, UserEntity currentUser,
			String organDesc) {

		if (entity.getState() == null)
			entity.setState(SanadStateEnum.MOVAGHAT);
		saveTempORMovaghat(entity, zamimeha, organId, saalMaaliEntity, validateSaalMaaliInProgress, currentUser,
				organDesc);
	}

	// just for administrative operations
	@Transactional
	public void saveAdmin(SanadHesabdariEntity entity, List<TempUploadedFileEntity> zamimeha, Long organId,
			SaalMaaliEntity saalMaaliEntity, String organDesc) {

		Boolean isNew = false;
		if (entity.getID() == null)
			isNew = true;

		save(entity, organId, saalMaaliEntity, organDesc);
		checkSanadArticlesSaalMaaliSameLess(entity);
		manageZamimeh(entity, zamimeha);
		logSanadHesabdariAction(entity, isNew, SerajMessageUtil.getMessage(ActionTypeEnum.EDIT.nameWithClass()));
	}

	@Transactional
	public void logSanadHesabdariAction(SanadHesabdariEntity sanadHesabdariEntity, boolean isNew, String actionName) {

		try {
			if (isNew) {
				ActionLogUtil.logAction(SerajMessageUtil.getMessage(ActionTypeEnum.CREATE.nameWithClass()),
						getEntityTitle(), "", "", sanadHesabdariEntity.getCompleteInfo());
			} else {
				String differences = getDifferences(sanadHesabdariEntity);
//				String actionName = SerajMessageUtil.getMessage(ActionTypeEnum.EDIT.nameWithClass());
				ActionLogUtil.logAction(actionName, getEntityTitle(), sanadHesabdariEntity.getCompleteInfo(),
						sanadHesabdariEntity.getOldEntity().getCompleteInfo(), differences);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Transactional
	private void saveTempORMovaghat(SanadHesabdariEntity entity, List<TempUploadedFileEntity> zamimeha, Long organId,
			SaalMaaliEntity saalMaaliEntity, boolean validateSaalMaaliInProgress, UserEntity currentUser,
			String organDesc) {

		Boolean isNew = false;
		if (entity.getID() == null)
			isNew = true;
		entity.setTanzimKonnadeSanad(currentUser);
		save(entity, organId, saalMaaliEntity, true, validateSaalMaaliInProgress, organDesc);
		checkSanadArticlesSaalMaaliSameLess(entity);
		manageZamimeh(entity, zamimeha);
		logSanadHesabdariAction(entity, isNew, SerajMessageUtil.getMessage(ActionTypeEnum.EDIT.nameWithClass()));
		logSanadHesabdariItemAction(entity);
	}

	@Transactional
	private void saveMonthlySummary(SanadHesabdariEntity entity, List<TempUploadedFileEntity> zamimeha, Long organId,
			SaalMaaliEntity saalMaaliEntity, boolean validateSaalMaaliInProgress, UserEntity currentUser,
			String organDesc) {

		entity.setState(SanadStateEnum.MonthlySummary);
		Boolean isNew = false;
		if (entity.getID() == null)
			isNew = true;
		entity.setTanzimKonnadeSanad(currentUser);
		save(entity, organId, saalMaaliEntity, false, validateSaalMaaliInProgress, organDesc);
		checkSanadArticlesSaalMaaliSameLess(entity);
		manageZamimeh(entity, zamimeha);
		logSanadHesabdariAction(entity, isNew, SerajMessageUtil.getMessage(ActionTypeEnum.EDIT.nameWithClass()));
		logSanadHesabdariItemAction(entity);
	}

	public void logSanadHesabdariItemAction(SanadHesabdariEntity entity) {
//		String itemDesc="[";
//		for (SanadHesabdariItemEntity SHIE : entity.getSanadHesabdariItem()) {
//			itemDesc+="["+SerajMessageUtil.getMessage("SanadHesabdariItem_hesabKol")+":"+SHIE.getHesabKol()+" , ";
//			itemDesc+=SerajMessageUtil.getMessage("SanadHesabdariItem_hesabMoeen")+":"+SHIE.getHesabMoeen()+" , ";
//			if(SHIE.getHesabTafsili()!=null)
//			itemDesc+=SerajMessageUtil.getMessage("SanadHesabdariItem_hesabTafsili")+":"+SHIE.getHesabTafsili()+" , ";
//			itemDesc+=SerajMessageUtil.getMessage("SanadHesabdariItem_bedehkar")+":"+SHIE.getBedehkar()+" , ";
//			itemDesc+=SerajMessageUtil.getMessage("SanadHesabdariItem_bestankar")+":"+SHIE.getBestankar()+" , ";
//			itemDesc+=SerajMessageUtil.getMessage("SanadHesabdariItem_description")+":"+SHIE.getDescription()+" , ";
//			itemDesc+="]";
//		}
//		String action=(entity.getID()!=null?(SerajMessageUtil.getMessage(ActionTypeEnum.EDIT.nameWithClass())):(SerajMessageUtil.getMessage(ActionTypeEnum.CREATE.nameWithClass())));
//		ActionLogUtil.logAction(action,
//				SerajMessageUtil.getMessage("SanadHesabdariItem_list"),
//				"",
//				SerajMessageUtil.getMessage("SanadHesabdariItem_sanadHesabdari_desc",entity.getTempSerial()),
//				itemDesc);
	}

	@Transactional
	private void checkSanadArticlesSaalMaaliSameLess(SanadHesabdariEntity entity) {
		List<SanadHesabdariItemEntity> sanadHesabdariItem = entity.getSanadHesabdariItem();
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItem) {
			HesabKolEntity hesabKol = sanadHesabdariItemEntity.getHesabKol();
			HesabMoeenEntity hesabMoeen = sanadHesabdariItemEntity.getHesabMoeen();
			HesabTafsiliEntity hesabTafsili = sanadHesabdariItemEntity.getHesabTafsili();
			HesabTafsiliEntity hesabTafsiliTwo = sanadHesabdariItemEntity.getHesabTafsiliTwo();
			AccountingMarkazEntity accountingMarkaz = sanadHesabdariItemEntity.getAccountingMarkaz();
//			Set<ArticleTafsiliEntity> articleTafsiliSet = sanadHesabdariItemEntity.getArticleTafsili();
//			Set<ArticleAccountingMarkazEntity> articleAccountingMarkazSet = sanadHesabdariItemEntity.getArticleAccountingMarkaz();

			boolean hesabMoeenValidation = hesabMoeen != null ? !hesabMoeen.getSaalMaali().equals(entity.getSaalMaali())
					: false;
			if (!hesabKol.getSaalMaali().equals(entity.getSaalMaali()) || hesabMoeenValidation
					|| (hesabTafsili != null && hesabTafsili.getId() != null
							&& !hesabTafsili.getSaalMaali().equals(entity.getSaalMaali()))
					|| (hesabTafsiliTwo != null && hesabTafsiliTwo.getId() != null
							&& !hesabTafsiliTwo.getSaalMaali().equals(entity.getSaalMaali()))
					|| (accountingMarkaz != null && accountingMarkaz.getId() != null
							&& !accountingMarkaz.getSaalMaali().equals(entity.getSaalMaali())))
				throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_articleSaalMaaliConflict",
						entity.getDesc(), sanadHesabdariItemEntity.getDesc()));

//			if(articleTafsiliSet!=null){
//				for (ArticleTafsiliEntity articleTafsiliEntity : articleTafsiliSet) {
//					HesabTafsiliEntity tafsili = articleTafsiliEntity.getHesabTafsili();
//					if(tafsili!=null && !tafsili.getSaalMaali().equals(entity.getSaalMaali()))
//						throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_articleSaalMaaliConflict", entity.getDesc(), sanadHesabdariItemEntity.getDesc()));
//				}
//			}

//			if(articleAccountingMarkazSet!=null)
//				for (ArticleAccountingMarkazEntity articleAccountingMarkazEntity : articleAccountingMarkazSet) {
//					AccountingMarkazEntity markaz = articleAccountingMarkazEntity.getAccountingMarkaz();
//					if(markaz!=null && !markaz.getSaalMaali().equals(entity.getSaalMaali()))
//						throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_articleSaalMaaliConflict", entity.getDesc(), sanadHesabdariItemEntity.getDesc()));
//					
//				}
		}

	}

	// hazfe zamimeh ghabli va ijad zamimeh jadid
	private void manageZamimeh(SanadHesabdariEntity sanadHesabdariEntity, List<TempUploadedFileEntity> zamimeha) {

		if (zamimeha == null)
			return;
		for (TempUploadedFileEntity tempUploadedFileEntity : zamimeha) {
			FileEntity fileEntity = this.fileService.createEntity();
			this.fileService.initializeEntity(fileEntity);
			// zamimehEntity.setNamehPedar(namehPedarEntity);
			fileEntity.setTitle(tempUploadedFileEntity.getTitle());
			fileEntity.setContectType(tempUploadedFileEntity.getContentType());
			fileEntity.setFile(tempUploadedFileEntity.getContent());

			this.fileService.save(fileEntity);
			if (sanadHesabdariEntity.getZamimeh() == null)
				sanadHesabdariEntity.setZamimeh(new HashSet<FileEntity>());
			sanadHesabdariEntity.getZamimeh().add(fileEntity);
			tempUploadedFileService.delete(tempUploadedFileEntity.getId());
		}
	}

	public void checkSaalMaaliAndTarikhSanadConflict(SanadHesabdariEntity sanadHesabdariEntity) {
		Integer saalMaaliYear = sanadHesabdariEntity.getSaalMaali().getSaal();

		if (!compareWithCurrentYear(saalMaaliYear) && sanadHesabdariEntity.getTarikhSanad() == null) {
			throw new RequiredFieldNotSetException("SanadHesabdariEntity", "tarikhSanad");

		}

		if (sanadHesabdariEntity.getTarikhSanad() != null) {
			Integer tarikhSanadYear = DateConverter.getPersianDatePart(sanadHesabdariEntity.getTarikhSanad())
					.get(SerajDatePartEnum.YEAR);
			SerajLogger.getLogger(getClass()).info("daftarYear is : " + saalMaaliYear);
			SerajLogger.getLogger(getClass()).info("tarikhNamehYear is : " + tarikhSanadYear);
			if (!saalMaaliYear.equals(tarikhSanadYear)) {
				throw new ConflictSaalMaaliTarikhException(saalMaaliYear, tarikhSanadYear);
			}
		}

	}

	public boolean compareWithCurrentYear(Integer toCompareYear) {
		Map<SerajDatePartEnum, Integer> persianCurrentDatePart = DateConverter
				.getPersianDatePart(DateConverter.getCurrentDate());
		Integer currentYear = persianCurrentDatePart.get(SerajDatePartEnum.YEAR);

		if (currentYear.equals(toCompareYear))
			return true;
		return false;
	}

	@Override
	public String getDifferences(SanadHesabdariEntity entity) {
		String diffes = "";
		SanadHesabdariEntity oldEntity = (SanadHesabdariEntity) entity.getOldEntity();
		if (entity.getTempSerial() != null && !entity.getTempSerial().equals(oldEntity.getTempSerial()))
			diffes += "[" + SerajMessageUtil.getMessage("SanadHesabdari" + "_" + SanadHesabdariEntity.PROP_TEMP_SERIAL)
					+ " : " + oldEntity.getTempSerial() + "" + " --> " + entity.getTempSerial() + "" + "]";

		if (entity.getSerial() != null && !entity.getSerial().equals(oldEntity.getSerial()))
			diffes += "[" + SerajMessageUtil.getMessage("SanadHesabdari" + "_" + SanadHesabdariEntity.PROP_SERIAL)
					+ " : " + oldEntity.getSerial() + "" + " --> " + entity.getSerial() + "" + "]";

		if (entity.getTarikhSanad() != null && !entity.getTarikhSanad().equals(oldEntity.getTarikhSanad()))
			diffes += "[" + SerajMessageUtil.getMessage("SanadHesabdari" + "_" + SanadHesabdariEntity.PROP_TARIKH_SANAD)
					+ " : " + oldEntity.getTarikhSanadFA() + "" + " --> " + entity.getTarikhSanadFA() + "" + "]";

		if (entity.getDescription() != null && !entity.getDescription().equals(oldEntity.getDescription()))
			diffes += "[" + SerajMessageUtil.getMessage("SanadHesabdari" + "_" + SanadHesabdariEntity.PROP_DESCRIPTION)
					+ ":" + oldEntity.getDescription() + "" + "-->" + entity.getDescription() + "" + "]";

		if (entity.getSaalMaali() != null && !entity.getSaalMaali().equals(oldEntity.getSaalMaali()))
			diffes += "[" + SerajMessageUtil.getMessage("SanadHesabdari" + "_" + SanadHesabdariEntity.PROP_SAAL_MAALI)
					+ ":" + oldEntity.getSaalMaali() + "" + "-->" + entity.getSaalMaali() + "" + "]";

		if (entity.getAtfNumber() != null && !entity.getAtfNumber().equals(oldEntity.getAtfNumber()))
			diffes += "[" + SerajMessageUtil.getMessage("SanadHesabdari" + "_" + SanadHesabdariEntity.PROP_ATF_NUMBER)
					+ ":" + oldEntity.getAtfNumber() + "" + "-->" + entity.getAtfNumber() + "" + "]";

		if (entity.getFarieNumber() != null && !entity.getFarieNumber().equals(oldEntity.getFarieNumber()))
			diffes += "[" + SerajMessageUtil.getMessage("SanadHesabdari" + "_" + SanadHesabdariEntity.PROP_FARIE_NUMBER)
					+ ":" + oldEntity.getFarieNumber() + "" + "-->" + entity.getFarieNumber() + "" + "]";

		if (entity.getSaalMaali() != null && !entity.getSaalMaali().equals(oldEntity.getSaalMaali()))
			diffes += "[" + SerajMessageUtil.getMessage("SanadHesabdari" + "_" + SanadHesabdariEntity.PROP_SAAL_MAALI)
					+ ":" + oldEntity.getSaalMaali() + "" + "-->" + entity.getSaalMaali() + "" + "]";

//		ArrayList<SanadHesabdariItemEntity> sanadHesabdariItemList = new ArrayList<SanadHesabdariItemEntity>();
//		ArrayList<SanadHesabdariItemEntity> sanadHesabdariOldItemList = new ArrayList<SanadHesabdariItemEntity>();

//		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : entity
//				.getSanadHesabdariItem()) {
//			boolean check = true;
//			while (check) {
//				if (oldEntity.getSanadHesabdariItem() != null && oldEntity.getSanadHesabdariItem().contains(
//						sanadHesabdariItemEntity)) {
//					check = false;
//				} else {
//					sanadHesabdariItemList.add(sanadHesabdariItemEntity);
//					check = false;
//
//				}
//			}
//		}

//		for (SanadHesabdariItemEntity sanadHesabdariItemOldEntity : oldEntity
//				.getSanadHesabdariItem()) {
//			boolean check = true;
//			while (check) {
//				if (entity.getSanadHesabdariItem().contains(
//						sanadHesabdariItemOldEntity)) {
//					check = false;
//				} else {
//					sanadHesabdariOldItemList.add(sanadHesabdariItemOldEntity);
//					check = false;
//
//				}
//			}
//		}

		return diffes;
	}

	// converts from movaghat to barrasi shode
	@Transactional
	public void saveBarrasiShode(SanadHesabdariEntity entity, Long organId, boolean isInMultipleLevelMode,
			boolean validateSaalMaaliInProgress, int numberOfDecimals, UserEntity currentUser, String organDesc) {

		Boolean isNew = false;
		if (entity.getID() == null)
			isNew = true;

		entity.setTaiedKonnadeSanad(currentUser);
		checkSanadIsBalanced(entity, numberOfDecimals);

		List<SanadHesabdariItemEntity> sanadHesabdariItems = entity.getSanadHesabdariItem();
		checkSanadArticlesSaalMaaliSameLess(entity);

		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItems) {
			Double bedehkar = sanadHesabdariItemEntity.getBedehkar();
			Double bestankar = sanadHesabdariItemEntity.getBestankar();

			HesabKolEntity hesabKol = hesabKolService.load(sanadHesabdariItemEntity.getHesabKol().getId());
			MahyatKolEnum mahyatKol = hesabKol.getMahyatKol();

			if (hesabKol.getHesabGroup() == null)
				throw new FatalException(SerajMessageUtil.getMessage("HesabKol_hesabGroupNotDefined", hesabKol));

			if (hesabKol.getHesabGroup().getType() == null)
				throw new FatalException(
						SerajMessageUtil.getMessage("HesabGroup_typeNotDefined", hesabKol.getHesabGroup()));

			if (hesabKol.getHesabGroup().getType().equals(HesabTypeEnum.EXPENSE)
					|| hesabKol.getHesabGroup().getType().equals(HesabTypeEnum.INCOME))
				if (mahyatKol.equals(MahyatKolEnum.Undefined))
					throw new MahyatKolNotDefinedException(hesabKol);

			hesabKol.setBedehkar(hesabKol.getBedehkar() != null ? hesabKol.getBedehkar() + bedehkar : bedehkar);
			hesabKol.setBestankr(hesabKol.getBestankr() != null ? hesabKol.getBestankr() + bestankar : bestankar);

			hesabKolService.updateValues(hesabKol);

			HesabMoeenEntity hesabMoeen = hesabMoeenService.load(sanadHesabdariItemEntity.getHesabMoeen().getId());
			hesabMoeen.setBedehkar(hesabMoeen.getBedehkar() != null ? hesabMoeen.getBedehkar() + bedehkar : bedehkar);
			hesabMoeen.setBestankr(hesabMoeen.getBestankr() != null ? hesabMoeen.getBestankr() + bestankar : bestankar);

			hesabMoeenService.updateValues(hesabMoeen);

			if (sanadHesabdariItemEntity.getHesabTafsili() != null
					&& sanadHesabdariItemEntity.getHesabTafsili().getId() != null) {
				HesabTafsiliEntity hesabTafsili = hesabTafsiliService
						.load(sanadHesabdariItemEntity.getHesabTafsili().getId());

				hesabTafsili.setBedehkar(
						hesabTafsili.getBedehkar() != null ? hesabTafsili.getBedehkar() + bedehkar : bedehkar);
				hesabTafsili.setBestankr(
						hesabTafsili.getBestankr() != null ? hesabTafsili.getBestankr() + bestankar : bestankar);
				hesabTafsiliService.updateValues(hesabTafsili);

				if (sanadHesabdariItemEntity.getHesabTafsiliTwo() != null
						&& sanadHesabdariItemEntity.getHesabTafsiliTwo().getId() != null) {
					HesabTafsiliEntity hesabTafsiliTwo = hesabTafsiliService
							.load(sanadHesabdariItemEntity.getHesabTafsiliTwo().getId());
					hesabTafsiliTwo.setBedehkar(
							hesabTafsiliTwo.getBedehkar() != null ? hesabTafsiliTwo.getBedehkar() + bedehkar
									: bedehkar);
					hesabTafsiliTwo.setBestankr(
							hesabTafsiliTwo.getBestankr() != null ? hesabTafsiliTwo.getBestankr() + bestankar
									: bestankar);
					hesabTafsiliService.updateValues(hesabTafsili);
				}

//				if(isInMultipleLevelMode && sanadHesabdariItemEntity.getArticleTafsili()!=null){
//					for(ArticleTafsiliEntity articleTafsiliEntity : sanadHesabdariItemEntity.getArticleTafsili()){
//						HesabTafsiliEntity tafsili = hesabTafsiliService.load(articleTafsiliEntity.getHesabTafsili().getId());
//						tafsili.setBedehkar(tafsili.getBedehkar()!= null ?hesabTafsili.getBedehkar()+bedehkar  : bedehkar);
//						tafsili.setBestankr(tafsili.getBestankr()!= null ?hesabTafsili.getBestankr()+bestankar :bestankar);
//	
//						hesabTafsiliService.updateValues(tafsili);
//					}
//				}
			}

		}
		entity.setState(SanadStateEnum.BARRESI_SHODE);
		save(entity, organId, null, true, validateSaalMaaliInProgress, organDesc);
		checkSanadArticlesSaalMaaliSameLess(entity);
		logSanadHesabdariAction(entity, isNew, SerajMessageUtil.getMessage("CONFIRMED_SANAD"));
	}

	private void checkHesabDependencies(SanadHesabdariEntity sanadHesabdariEntity, OrganVO organEntity) {

		List<SanadHesabdariItemEntity> sanadHesabdariItems = sanadHesabdariEntity.getSanadHesabdariItem();
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItems) {
			HesabMoeenEntity hesabMoeen = hesabMoeenService.load(sanadHesabdariItemEntity.getHesabMoeen().getId());
			HesabTafsiliEntity hesabTafsili = null;
			HesabTafsiliEntity hesabTafsiliTwo = null;
			AccountingMarkazEntity accountingMarkazEntity = null;

			if (sanadHesabdariItemEntity.getHesabTafsili() != null
					&& sanadHesabdariItemEntity.getHesabTafsili().getId() != null) {
				hesabTafsili = hesabTafsiliService.load(sanadHesabdariItemEntity.getHesabTafsili().getId());
				if (hesabTafsili.getLevel().intValue() != 1)
					throw new FatalException(SerajMessageUtil.getMessage("HesabTafsili_notApplicableForLevel",
							sanadHesabdariEntity, hesabTafsili, SerajMessageUtil.getMessage("common_one")));
			}

			if (sanadHesabdariItemEntity.getHesabTafsiliTwo() != null
					&& sanadHesabdariItemEntity.getHesabTafsiliTwo().getId() != null) {
				hesabTafsiliTwo = hesabTafsiliService.load(sanadHesabdariItemEntity.getHesabTafsiliTwo().getId());
				if (hesabTafsiliTwo.getLevel().intValue() != 2)
					throw new FatalException(SerajMessageUtil.getMessage("HesabTafsili_notApplicableForLevel",
							sanadHesabdariEntity, hesabTafsiliTwo, SerajMessageUtil.getMessage("common_two")));
			}

			if (sanadHesabdariItemEntity.getAccountingMarkaz() != null
					&& sanadHesabdariItemEntity.getAccountingMarkaz().getId() != null)
				accountingMarkazEntity = accountingMarkazService
						.load(sanadHesabdariItemEntity.getAccountingMarkaz().getId());

			String tempSerial = sanadHesabdariEntity.getTempSerial() != null
					? sanadHesabdariEntity.getTempSerial().toString()
					: "";
			if (checkIfMustValidateHesabMoeenHasChild(organEntity)
					&& (hesabTafsili == null || hesabTafsili.getId() == null) && hesabMoeen.getTafsiliList().size() > 0)
				throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_hesabMoeenMustHaveChildTafsilis",
						hesabMoeen.getDesc(),
						tempSerial + " (" + DateConverter.toShamsiDate(sanadHesabdariEntity.getTarikhSanad()) + ")",
						sanadHesabdariItemEntity.getDescription(), sanadHesabdariEntity.getSaalMaali()));

			if (checkIfMustValidateHesabMoeenHasMarkaz(organEntity)
					&& (accountingMarkazEntity == null || accountingMarkazEntity.getId() == null)
					&& hesabMoeen.getAccountingMarkazList().size() > 0)
				throw new FatalException(
						SerajMessageUtil.getMessage("SanadHesabdari_hesabMoeenMustHaveMarkaz", hesabMoeen.getDesc(),
								tempSerial + " (" + DateConverter.toShamsiDate(sanadHesabdariEntity.getTarikhSanad())
										+ ")",
								sanadHesabdariItemEntity.getDescription(), sanadHesabdariEntity.getSaalMaali()));

			if (hesabTafsili != null && !hesabTafsili.getHesabMoeenList().contains(hesabMoeen))
				throw new FatalException(SerajMessageUtil.getMessage("HesabMoeen_tafsiliDoesnotBelongToMoeen",
						tempSerial + " (" + DateConverter.toShamsiDate(sanadHesabdariEntity.getTarikhSanad()) + ")",
						sanadHesabdariItemEntity.getDescription(), hesabTafsili.getDesc(), hesabMoeen.getDesc()));

			if (accountingMarkazEntity != null && !accountingMarkazEntity.getHesabMoeenList().contains(hesabMoeen))
				throw new FatalException(
						SerajMessageUtil.getMessage("HesabMoeen_accountingMarkazDoesnotBelongToMoeen",
								tempSerial + " (" + DateConverter.toShamsiDate(sanadHesabdariEntity.getTarikhSanad())
										+ ")",
								sanadHesabdariItemEntity.getDescription(), accountingMarkazEntity.getDesc(),
								hesabMoeen.getDesc()));

			if (checkIfMustValidateTafsiliOneAndTwoAreRelated(organEntity.getId())) {
				if (hesabTafsiliTwo != null) {
//					HesabTafsiliEntity hesabTafsiliTwo = hesabTafsiliService.load(sanadHesabdariItemEntity.getHesabTafsiliTwo().getId());
//					if(hesabTafsiliTwo.getLevel().intValue() != 2)
//						throw new FatalException(SerajMessageUtil.getMessage("HesabTafsili_notApplicableForLevel",hesabTafsiliTwo,SerajMessageUtil.getMessage("common_two")));

					if (!hesabTafsiliTwo.getParents().contains(hesabTafsili))
						throw new FatalException(SerajMessageUtil.getMessage(
								"HesabTafsili_tafsiliShenavarDoesnotBelongToTafsili", hesabTafsiliTwo.getDesc(),
								hesabTafsiliTwo.getSaalMaali(), hesabTafsili.getDesc(), hesabTafsili.getSaalMaali()));

				}
//				ArticleTafsiliEntity articleTafsiliEntityTWO = sanadHesabdariItemEntity.getArticleTafsiliByLevel(1);
//				if(hesabTafsili!=null && articleTafsiliEntityTWO!=null && articleTafsiliEntityTWO.getHesabTafsili()!=null && articleTafsiliEntityTWO.getHesabTafsili().getId()!=null /*&& articleTafsiliEntityTWO.getId()!=null*/){
//					HesabTafsiliEntity hesabTafsiliShenavar = hesabTafsiliService.load(articleTafsiliEntityTWO.getHesabTafsili().getId());
//					if(!hesabTafsiliShenavar.getParents().contains(hesabTafsili))
//						throw new FatalException(SerajMessageUtil.getMessage("HesabTafsili_tafsiliShenavarDoesnotBelongToTafsili",hesabTafsiliShenavar.getDesc(),hesabTafsiliShenavar.getSaalMaali(), hesabTafsili.getDesc(), hesabTafsili.getSaalMaali()));
//				}
			}
			if (checkIfMustValidateHesabTafsiliHasChild(organEntity) && hesabTafsili != null
					&& hesabTafsili.getChilds() != null && hesabTafsili.getChilds().size() > 0) {
				throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_hesabTafsiliMustHaveShenavar",
						hesabTafsili.getDesc(),
						tempSerial + " (" + DateConverter.toShamsiDate(sanadHesabdariEntity.getTarikhSanad()) + ")",
						sanadHesabdariItemEntity.getDescription(), sanadHesabdariEntity.getSaalMaali()));
			}

//			ArticleTafsiliEntity articleTafsiliEntityThree = sanadHesabdariItemEntity.getArticleTafsiliByLevel(2);
//			if(articleTafsiliEntityThree!=null)
//				throw new FatalException("unImplementedTask : "+sanadHesabdariItemEntity);

//			if(sanadHesabdariItemEntity.getArticleAccountingMarkaz()!=null)
//				for (ArticleAccountingMarkazEntity articleAccountingMarkazEntity : sanadHesabdariItemEntity.getArticleAccountingMarkaz()) {
//	
//					if(sanadHesabdariItemEntity.getAccountingMarkaz()==null || articleAccountingMarkazEntity.getAccountingMarkaz().getId() ==sanadHesabdariItemEntity.getAccountingMarkaz().getId())
//						throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_articleIncorrect",sanadHesabdariItemEntity.getDesc(), sanadHesabdariEntity.getCompleteInfo()));
//
//				}
		}
	}

	private void checkSanadIsBalanced(SanadHesabdariEntity entity, int numberOfDecimals) {
		Double bedehkarSum = 0d;
		Double bestankarSum = 0d;
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : entity.getSanadHesabdariItem()) {
			bedehkarSum += sanadHesabdariItemEntity.getBedehkar();
			bestankarSum += sanadHesabdariItemEntity.getBestankar();
		}

		bedehkarSum = NumberUtil.round(bedehkarSum, numberOfDecimals);
		bestankarSum = NumberUtil.round(bestankarSum, numberOfDecimals);

		if (bedehkarSum.doubleValue() != bestankarSum.doubleValue())
			throw new SanadIsNotBalancedException(entity);
	}

	public Long getlastDaemiSerial(Long organId, SaalMaaliEntity activeSaalmaali) {
//		SaalMaaliEntity activeSaalmaali = saalMaaliService.getActiveSaalmaali(currentOrgan);
		Long maxDaemSanadSerial = getMyDAO().getMaxDaemSanadSerial(organId, activeSaalmaali);
		return maxDaemSanadSerial;
	}

	public Date getlastDaemiTarikhSanad(Long organId, SaalMaaliEntity activeSaalmaali) {
//		SaalMaaliEntity activeSaalmaali = saalMaaliService.getActiveSaalmaali(currentOrgan);
		Date maxDaemSanadTarikh = getMyDAO().getMaxDaemSanadTarikh(organId, activeSaalmaali);
		return maxDaemSanadTarikh;
	}

	@Transactional
	public void tabdilBeDaemi(SanadHesabdariEntity entity, SaalMaaliEntity saalMaaliEntity, Long organId,
			UserEntity currentUser, String organDesc) {
		boolean validateSaalMaaliInProgress = true;
		if (entity.getSanadFunction().equals(SanadFunctionEnum.EKHTETAMIE))
			validateSaalMaaliInProgress = false;
		tabdilBeDaemi(entity, saalMaaliEntity, validateSaalMaaliInProgress, organId, currentUser, organDesc);
	}

	@Transactional
	public void tabdilBeDaemi(SanadHesabdariEntity entity, SaalMaaliEntity saalMaaliEntity,
			boolean validateSaalMaaliInProgress, Long organId, UserEntity currentUser, String organDesc) {
		entity.setDaeemKonnadeSanad(currentUser);
		checkSaalMaaliAndTarikhSanadConflict(entity);

//		OrganEntity organEntity = organService.load(saalMaaliEntity.getOrganId());
		save(entity, saalMaaliEntity.getOrganId(), saalMaaliEntity, true, validateSaalMaaliInProgress, organDesc);

		if (entity.getSerial() == null) {
			entity.setSerial(getNextSanadHesabdariSerial(saalMaaliEntity, organId));
			Map<String, Object> localFilter = new HashMap<String, Object>();
			localFilter.put("serial@eq", entity.getSerial());
			localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
			localFilter.put("organId@eq", organId);
			checkUniqueNess(entity, localFilter, false);

		}

		entity.setState(SanadStateEnum.DAEM);

		update(entity, false);
		checkSanadArticlesSaalMaaliSameLess(entity);
		logSanadHesabdariAction(entity, false, SerajMessageUtil.getMessage("Tabdil_To_Daemi"));
	}

//	@Transactional
//	public Integer tabdilBeDaemi(Date tarikhSanadTo,OrganEntity currentOrgan) {
//		SaalMaaliEntity activeSaalmaali = saalMaaliService.getActiveSaalmaali(currentOrgan);
//		Map<String, Object> localFilter = new HashMap<String, Object>();
//		localFilter.put("tarikhSanad@le", tarikhSanadTo);
//		localFilter.put("saalMaali.id@eq", activeSaalmaali.getId());
//		localFilter.put("state@eq", SanadStateEnum.BARRESI_SHODE);
//		List<SanadHesabdariEntity> dataList = getDataList(null, localFilter);
//		for (SanadHesabdariEntity sanadHesabdariEntity : dataList) {
//			tabdilBeDaemi(sanadHesabdariEntity, currentOrgan);
//		}
//
//		return dataList.size();
//	}

//	public void logtabdilBeMovaghatAction(List<SanadHesabdariEntity> sanads) {
//		String message = SerajMessageUtil
//				.getMessage(getEntityName() + "_title");
//		try {
//			String list = ListSanads(sanads);
//			ActionLogUtil.logAction(SerajMessageUtil
//					.getMessage(SerajMessageUtil
//							.getMessage("Tabdil_To_Movaghat")), message, null,
//					list, "");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//	public String ListSanads(List<SanadHesabdariEntity> sanads) {
//		String list = "";
//		for (SanadHesabdariEntity sanadHesabdariEntity : sanads) {
//			list += "["
//					+ SerajMessageUtil.getMessage("SanadHesabdari" + "_"
//							+ SanadHesabdariEntity.PROP_SERIAL) + " : "
//					+ sanadHesabdariEntity.getTempSerial() + "" + "]";
//		}
//		return list;
//	}

	@Transactional
	public Integer tabdilBeDaemi(Long serialSanadTo, SaalMaaliEntity saalMaaliEntity, Long organId,
			UserEntity currentUser, String organDesc) {
//		SaalMaaliEntity activeSaalmaali = getSaalMaaliService()
//				.getActiveSaalmaali(currentOrgan);
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("serial@le", serialSanadTo);
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		localFilter.put("state@eq", SanadStateEnum.BARRESI_SHODE);
		List<SanadHesabdariEntity> dataList = getDataList(null, localFilter);
		for (SanadHesabdariEntity sanadHesabdariEntity : dataList) {
			tabdilBeDaemi(sanadHesabdariEntity, saalMaaliEntity, organId, currentUser, organDesc);
		}
		return dataList.size();
	}

	// convert from barresi shode be movaghat
	@Transactional
	public void tabdilBeMovghat(SanadHesabdariEntity entity, String organDesc) {

		Boolean isNew = false;
		if (entity.getID() == null)
			isNew = true;

		List<SanadHesabdariItemEntity> sanadHesabdariItems = entity.getSanadHesabdariItem();
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItems) {
			Double bedehkar = sanadHesabdariItemEntity.getBedehkar();
			Double bestankar = sanadHesabdariItemEntity.getBestankar();

			HesabKolEntity hesabKol = sanadHesabdariItemEntity.getHesabKol();
			MahyatKolEnum mahyatKol = hesabKol.getMahyatKol();

			if (hesabKol.getHesabGroup().getType().equals(HesabTypeEnum.EXPENSE)
					|| hesabKol.getHesabGroup().getType().equals(HesabTypeEnum.INCOME))
				if (mahyatKol.equals(MahyatKolEnum.Undefined))
					throw new MahyatKolNotDefinedException(hesabKol);

			hesabKol.setBedehkar(hesabKol.getBedehkar() - bedehkar);
			hesabKol.setBestankr(hesabKol.getBestankr() - bestankar);
			hesabKolService.save(hesabKol);

			HesabMoeenEntity hesabMoeen = sanadHesabdariItemEntity.getHesabMoeen();
			if (hesabMoeen != null && hesabMoeen.getId() != null) {
				hesabMoeen.setBedehkar(hesabMoeen.getBedehkar() - bedehkar);
				hesabMoeen.setBestankr(hesabMoeen.getBestankr() - bestankar);

				hesabMoeenService.save(hesabMoeen);
			}

			HesabTafsiliEntity hesabTafsili = sanadHesabdariItemEntity.getHesabTafsili();

			if (hesabTafsili != null) {
				Double bedehkarAmount = hesabTafsili.getBedehkar() != null ? hesabTafsili.getBedehkar() : 0;
				Double bestankrAmount = hesabTafsili.getBestankr() != null ? hesabTafsili.getBestankr() : 0;
				hesabTafsili.setBedehkar(bedehkarAmount - bedehkar);
				hesabTafsili.setBestankr(bestankrAmount - bestankar);

				hesabTafsiliService.save(hesabTafsili);
			}

		}
		entity.setState(SanadStateEnum.MOVAGHAT);
		save(entity, false, organDesc);
		logSanadHesabdariAction(entity, isNew, SerajMessageUtil.getMessage("Tabdil_To_Movaghat"));

	}

	@Transactional
	public void tabdilBeDaemi(List<SanadHesabdariEntity> sanads, SaalMaaliEntity saalMaaliEntity, Long organId,
			UserEntity currentUser, String organDesc) {
		for (SanadHesabdariEntity sanadHesabdariEntity : sanads) {
			SanadHesabdariEntity entity = load(sanadHesabdariEntity.getId());
			duplicateEntity(entity.getOldEntity(), entity);
			tabdilBeDaemi(entity, saalMaaliEntity, organId, currentUser, organDesc);
		}
	}

	// converts from daemi to barrasi shode
	@Transactional
	public void tabdilBeBarrasiShode(List<SanadHesabdariEntity> sanads, SaalMaaliEntity saalMaaliEntity) {
		for (SanadHesabdariEntity sanadHesabdariEntity : sanads) {
			SanadHesabdariEntity entity = load(sanadHesabdariEntity.getId());
			duplicateEntity(entity.getOldEntity(), entity);
//			OrganEntity organEntity = organService.load(saalMaaliEntity.getOrganId());
			tabdilBeBarrasiShode(entity, saalMaaliEntity.getOrganId(), saalMaaliEntity.getOrganName());
		}
	}

	@Transactional
	public void tabdilBeMovghat(List<SanadHesabdariEntity> sanads, String organDesc) {
		for (SanadHesabdariEntity sanadHesabdariEntity : sanads) {
			SanadHesabdariEntity entity = load(sanadHesabdariEntity.getId());
			duplicateEntity(entity.getOldEntity(), entity);
			tabdilBeMovghat(entity, organDesc);
		}

	}

	// converts from daemi to barrasi shode
	@Transactional
	public void tabdilBeBarrasiShode(SanadHesabdariEntity entity, Long organId, String organDesc) {
		entity.setState(SanadStateEnum.BARRESI_SHODE);
		save(entity, organId, false, organDesc);
		logSanadHesabdariAction(entity, false, SerajMessageUtil.getMessage("Tabdil_To_BarrasiShode"));
	}

	@Transactional(readOnly = false)
	public void deleteTemporalSanadHesabdari(SaalMaaliEntity saalMaaliEntity, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		localFilter.put("organId@eq", organId);
		localFilter.put("sanadFunction@eq", SanadFunctionEnum.BASTAN_HESABHA);
		SanadHesabdariEntity sanadHesabdariCloseTemporalAccountsEntity = load(null, localFilter);
		if (sanadHesabdariCloseTemporalAccountsEntity != null) {
			// deleteWithoutDeletableValidation(sanadHesabdariCloseTemporalAccountsEntity.getId());
			super.delete(sanadHesabdariCloseTemporalAccountsEntity.getId());
		}
		saalMaaliEntity.setStatus(SaalMaaliStatusEnum.InProgress);
		saalMaaliService.save(saalMaaliEntity);
	}

	@Transactional(readOnly = false)
	public SanadHesabdariEntity closeTemporalAccounts(Long organId, Date tarikhSanad, boolean isInMultipleLevelMode,
			SaalMaaliEntity saalMaaliEntity, int numberOfDecimals, UserEntity currentUser, String organDesc) {

//		SaalMaaliEntity saalMaaliEntity = getSaalmaaliByDate(tarikhSanad, organEntity);
		checkIfSanadTempExists(saalMaaliEntity, organId);
		checkIfSanadDaemiNashodeExists(saalMaaliEntity, organId);

		validateTemporalSanadCreation(saalMaaliEntity, organId);
		List<SanadHesabdariEntity> sanadHesabdariList = getListOfSanadHesabdariDaemi(saalMaaliEntity, organId);
		Map<String, SanadHesabdariItemEntity> sanadHesabdariCloseTemporalAccountsItemsMap = new HashMap<String, SanadHesabdariItemEntity>();

		String description = SerajMessageUtil.getMessage("SanadHesabdariItem_closeTemporalAccounts");
		SanadFunctionEnum sanadFunction = SanadFunctionEnum.BASTAN_HESABHA;
		SanadHesabdariEntity sanadHesabdariCloseTemporalAccountsEntity = createSanadEntity(saalMaaliEntity, organId,
				tarikhSanad, description, sanadFunction, organDesc);

		Double sanadHesabdariItemCloseTemporalAccountsBedehkar = 0d;
		Double sanadHesabdariItemCloseTemporalAccountsBestankar = 0d;

		for (SanadHesabdariEntity sanadHesabdariEntity : sanadHesabdariList) {
			Map<String, Double> resultMap = addSingleSanadToTemporalSanad(sanadHesabdariCloseTemporalAccountsItemsMap,
					sanadHesabdariCloseTemporalAccountsEntity, sanadHesabdariEntity, organId);
			Double sanadHesabdariItemCloseTemporalAccountsBestankarLocal = resultMap
					.get("sanadHesabdariItemCloseTemporalAccountsBestankarLocal");
			Double sanadHesabdariItemCloseTemporalAccountsBedehkarLocal = resultMap
					.get("sanadHesabdariItemCloseTemporalAccountsBedehkarLocal");

			sanadHesabdariItemCloseTemporalAccountsBestankar += sanadHesabdariItemCloseTemporalAccountsBestankarLocal;
			sanadHesabdariItemCloseTemporalAccountsBedehkar += sanadHesabdariItemCloseTemporalAccountsBedehkarLocal;

		}

		Set<Entry<String, SanadHesabdariItemEntity>> entrySet = sanadHesabdariCloseTemporalAccountsItemsMap.entrySet();

		List<SanadHesabdariItemEntity> sanadHesabdariItemList = new ArrayList<SanadHesabdariItemEntity>();
		for (Entry<String, SanadHesabdariItemEntity> entry : entrySet) {
			SanadHesabdariItemEntity value = entry.getValue();
			sanadHesabdariItemList.add(value);
		}

		HesabMoeenEntity hesabSoodVaZyanAnbashtehMoeen = getHesabSoodVaZyanAnbashtehMoeen(saalMaaliEntity);
		HesabKolEntity hesabSoodVaZyanAnbashtehKol = hesabSoodVaZyanAnbashtehMoeen.getHesabKol();
		HesabTafsiliEntity hesabSoodVaZyanAnbashtehTafsili = getHesabSoodVaZyanAnbashtehTafsiliId(saalMaaliEntity);

		SanadHesabdariItemEntity soodVaZyanAnbashtehItemEntity = createamAlkardItemEntity(
				sanadHesabdariCloseTemporalAccountsEntity, sanadHesabdariItemCloseTemporalAccountsBedehkar,
				sanadHesabdariItemCloseTemporalAccountsBestankar, hesabSoodVaZyanAnbashtehTafsili,
				hesabSoodVaZyanAnbashtehMoeen, hesabSoodVaZyanAnbashtehKol);
		sanadHesabdariItemList.add(soodVaZyanAnbashtehItemEntity);

		/**************** Merging Articles ***************************/
		List<SanadHesabdariItemEntity> mergedArticles = SanadHesabdariUtil.createMergedArticles(sanadHesabdariItemList,
				false, organId);
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : mergedArticles) {
			sanadHesabdariItemEntity.setDescription(
					SerajMessageUtil.getMessage("SanadHesabdari_closeTemporalAccounts", saalMaaliEntity.getDesc()));
		}

		sanadHesabdariCloseTemporalAccountsEntity.getSanadHesabdariItem().addAll(mergedArticles);

		/********************************************/
		return saveTemporalSanadEntity(saalMaaliEntity, organId, isInMultipleLevelMode,
				sanadHesabdariCloseTemporalAccountsEntity, numberOfDecimals, currentUser, organDesc);
	}

	public HesabMoeenEntity getHesabSoodVaZyanAnbashtehMoeen(SaalMaaliEntity saalMaaliEntity) {
		String hesabSoodVaZyanAnbashtehMoeenTemplateId = systemConfigService.getValue(saalMaaliEntity.getOrganId(),
				null, "hesabSoodVaZyanAnbashtehMoeenId");
		if (!StringUtils.hasText(hesabSoodVaZyanAnbashtehMoeenTemplateId))
			throw new FatalException(SerajMessageUtil.getMessage("hesabSoodVaZyanAnbashtehMoeen_notDefined"));

		HesabMoeenEntity hesabMoeenEntity = hesabMoeenService.loadHesabMoeenByCode(
				hesabMoeenTemplateService.load(Long.valueOf(hesabSoodVaZyanAnbashtehMoeenTemplateId)).getCode(),
				saalMaaliEntity.getId());
		if (hesabMoeenEntity == null)
			throw new FatalException(SerajMessageUtil.getMessage("hesabSoodVaZyanAnbashtehMoeen_notDefined"));

		return hesabMoeenEntity;
	}

	public HesabTafsiliEntity getHesabSoodVaZyanAnbashtehTafsiliId(SaalMaaliEntity saalMaaliEntity) {
		String hesabSoodVaZyanAnbashtehTafsiliTemplateId = systemConfigService.getValue(saalMaaliEntity.getOrganId(),
				null, "hesabSoodVaZyanAnbashtehTafsiliId");
		if (!StringUtils.hasText(hesabSoodVaZyanAnbashtehTafsiliTemplateId))
			return null;
		Long code = hesabTafsiliTemplateService.load(Long.valueOf(hesabSoodVaZyanAnbashtehTafsiliTemplateId)).getCode();
		return hesabTafsiliService.loadHesabTafsiliByCode(code, saalMaaliEntity.getId());
	}

	private SanadHesabdariItemEntity createamAlkardItemEntity(
			SanadHesabdariEntity sanadHesabdariCloseTemporalAccountsEntity,
			Double sanadHesabdariItemCloseTemporalAccountsBedehkar,
			Double sanadHesabdariItemCloseTemporalAccountsBestankar, HesabTafsiliEntity hesabAmalkardTafsili,
			HesabMoeenEntity hesabAmalkardMoeen, HesabKolEntity hesabAmalkardKol) {
		SanadHesabdariItemEntity amalkardItemEntityOne = new SanadHesabdariItemEntity();
		amalkardItemEntityOne.setHesabKol(hesabAmalkardKol);
		amalkardItemEntityOne.setHesabMoeen(hesabAmalkardMoeen);
		amalkardItemEntityOne.setHesabTafsili(hesabAmalkardTafsili);
		amalkardItemEntityOne.setBedehkar(sanadHesabdariItemCloseTemporalAccountsBedehkar);
		amalkardItemEntityOne.setBestankar(sanadHesabdariItemCloseTemporalAccountsBestankar);
		amalkardItemEntityOne.setSanadHesabdari(sanadHesabdariCloseTemporalAccountsEntity);
		amalkardItemEntityOne.setDescription("-");
		return amalkardItemEntityOne;
	}

	@Transactional(readOnly = false)
	private Map<String, Double> addSingleSanadToTemporalSanad(
			Map<String, SanadHesabdariItemEntity> sanadHesabdariCloseTemporalAccountsItemsMap,
			SanadHesabdariEntity sanadHesabdariCloseTemporalAccountsEntity, SanadHesabdariEntity sanadHesabdariEntity,
			Long currentOrganId) {

		List<SanadHesabdariItemEntity> sanadHesabdariItem = sanadHesabdariEntity.getSanadHesabdariItem();

		Double sanadHesabdariItemCloseTemporalAccountsBestankarLocal = 0d;
		Double sanadHesabdariItemCloseTemporalAccountsBedehkarLocal = 0d;
		Map<String, Double> resultMap = new HashMap<String, Double>();
		System.out.println(sanadHesabdariEntity.getTarikhSanadFA());

		OrganVO organVO = organService.getOrganVO(sanadHesabdariEntity.getOrganId());
		checkHesabDependencies(sanadHesabdariEntity, organVO);
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItem) {

			HesabKolEntity hesabKol = sanadHesabdariItemEntity.getHesabKol();
			if (hesabKol.getHesabGroup().getType().equals(HesabTypeEnum.EXPENSE)
					|| hesabKol.getHesabGroup().getType().equals(HesabTypeEnum.INCOME)) {

				if (hesabKol.getMahyatKol().equals(MahyatKolEnum.Undefined))
					throw new MahyatKolNotDefinedException(hesabKol);

				HesabMoeenEntity hesabMoeen = sanadHesabdariItemEntity.getHesabMoeen();
				HesabTafsiliEntity hesabTafsili = sanadHesabdariItemEntity.getHesabTafsili();
				HesabTafsiliEntity hesabTafsiliTwo = sanadHesabdariItemEntity.getHesabTafsiliTwo();
				AccountingMarkazEntity accountingMarkaz = sanadHesabdariItemEntity.getAccountingMarkaz();
				if (accountingMarkaz != null && accountingMarkaz.getId() != null
						&& !sanadHesabdariCloseTemporalAccountsEntity.getSaalMaali()
								.equals(accountingMarkaz.getSaalMaali()))
					throw new FatalException(
							SerajMessageUtil.getMessage("SanadHesabdari_saalMaali_Not_equal_accountingMarkaz_saalMaali",
									sanadHesabdariCloseTemporalAccountsEntity, accountingMarkaz));
				String mapKey = SanadHesabdariUtil.createMapKey(sanadHesabdariItemEntity, currentOrganId);

//				String mapKey = hesabKol.getCode()+"_"+hesabMoeen.getCode()+"-"+(hesabTafsili!=null && hesabTafsili.getId()!=null ? hesabTafsili.getCode() : "");

				SanadHesabdariItemEntity entity = sanadHesabdariCloseTemporalAccountsItemsMap.get(mapKey);
				if (entity == null) {
					entity = new SanadHesabdariItemEntity();
					entity.setHesabKol(hesabKol);
					entity.setHesabMoeen(hesabMoeen);
					entity.setHesabTafsili(hesabTafsili);
					entity.setHesabTafsiliTwo(hesabTafsiliTwo);
					entity.setAccountingMarkaz(accountingMarkaz);

//					Set<ArticleTafsiliEntity> articleTafsiliSet = sanadHesabdariItemEntity.getArticleTafsili();
//					if(articleTafsiliSet!=null){
//						entity.setArticleTafsili(new HashSet<ArticleTafsiliEntity>());
//						for (ArticleTafsiliEntity articleTafsiliEntity : articleTafsiliSet) {
//							
//							if(hesabTafsili==null || articleTafsiliEntity.getHesabTafsili().getId() ==hesabTafsili.getId())
//								throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_articleIncorrect",sanadHesabdariItemEntity.getDesc(), sanadHesabdariEntity.getCompleteInfo()));
//							
//							ArticleTafsiliEntity ate = new ArticleTafsiliEntity();
//							ate.setHesabTafsili(articleTafsiliEntity.getHesabTafsili());
//							ate.setLevel(articleTafsiliEntity.getLevel());
//							ate.setSanadHesabdariItem(entity);
//							entity.getArticleTafsili().add(ate);
//						}
//					}

//					Set<ArticleAccountingMarkazEntity> articleAccountingMarkazSet = sanadHesabdariItemEntity.getArticleAccountingMarkaz();
//					if(articleAccountingMarkazSet!=null){
//						entity.setArticleAccountingMarkaz(new HashSet<ArticleAccountingMarkazEntity>());
//						for (ArticleAccountingMarkazEntity articleAccountingMarkazEntity : articleAccountingMarkazSet) {
//
//							if(accountingMarkaz==null || articleAccountingMarkazEntity.getAccountingMarkaz().getId() ==accountingMarkaz.getId())
//								throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_articleIncorrect",sanadHesabdariItemEntity.getDesc(), sanadHesabdariEntity.getCompleteInfo()));
//
//							ArticleAccountingMarkazEntity aame = new ArticleAccountingMarkazEntity();
//							aame.setAccountingMarkaz(articleAccountingMarkazEntity.getAccountingMarkaz());
//							aame.setLevel(articleAccountingMarkazEntity.getLevel());
//							aame.setSanadHesabdariItem(entity);
//							entity.getArticleAccountingMarkaz().add(aame);
//						}
//					}

					entity.setBedehkar(sanadHesabdariItemEntity.getBestankar());
					entity.setDescription("-");
					sanadHesabdariItemCloseTemporalAccountsBestankarLocal += sanadHesabdariItemEntity.getBestankar();

					entity.setBestankar(sanadHesabdariItemEntity.getBedehkar());
					sanadHesabdariItemCloseTemporalAccountsBedehkarLocal += sanadHesabdariItemEntity.getBedehkar();

				} else {
					entity.setBedehkar(entity.getBedehkar() + sanadHesabdariItemEntity.getBestankar());
					sanadHesabdariItemCloseTemporalAccountsBestankarLocal += sanadHesabdariItemEntity.getBestankar();

					entity.setBestankar(entity.getBestankar() + sanadHesabdariItemEntity.getBedehkar());
					sanadHesabdariItemCloseTemporalAccountsBedehkarLocal += sanadHesabdariItemEntity.getBedehkar();
				}

				entity.setSanadHesabdari(sanadHesabdariCloseTemporalAccountsEntity);
				sanadHesabdariCloseTemporalAccountsItemsMap.put(mapKey, entity);
			}
		}
		resultMap.put("sanadHesabdariItemCloseTemporalAccountsBestankarLocal",
				sanadHesabdariItemCloseTemporalAccountsBestankarLocal);
		resultMap.put("sanadHesabdariItemCloseTemporalAccountsBedehkarLocal",
				sanadHesabdariItemCloseTemporalAccountsBedehkarLocal);
		return resultMap;
	}

	@Transactional(readOnly = false)
	private SanadHesabdariEntity saveTemporalSanadEntity(SaalMaaliEntity saalMaaliEntity, Long organId,
			boolean isInMultipleLevelMode, SanadHesabdariEntity sanadHesabdariCloseTemporalAccountsEntity,
			int numberOfDecimals, UserEntity currentUser, String organDesc) {

		duplicateEntity(sanadHesabdariCloseTemporalAccountsEntity.getOldEntity(),
				sanadHesabdariCloseTemporalAccountsEntity);

		saveMovaghat(sanadHesabdariCloseTemporalAccountsEntity, new ArrayList<TempUploadedFileEntity>(), organId,
				saalMaaliEntity, currentUser, organDesc);
		duplicateEntity(sanadHesabdariCloseTemporalAccountsEntity.getOldEntity(),
				sanadHesabdariCloseTemporalAccountsEntity);

		saveBarrasiShode(sanadHesabdariCloseTemporalAccountsEntity, organId, isInMultipleLevelMode, true,
				numberOfDecimals, currentUser, organDesc);
		duplicateEntity(sanadHesabdariCloseTemporalAccountsEntity.getOldEntity(),
				sanadHesabdariCloseTemporalAccountsEntity);

		tabdilBeDaemi(sanadHesabdariCloseTemporalAccountsEntity, saalMaaliEntity, organId, currentUser, organDesc);
		SaalMaaliEntity loadedSaalMaaliEntity = saalMaaliService.load(saalMaaliEntity.getId());
		loadedSaalMaaliEntity.setStatus(SaalMaaliStatusEnum.TemporalAccountsClosed);
		saalMaaliService.save(loadedSaalMaaliEntity);

		return sanadHesabdariCloseTemporalAccountsEntity;
	}

	private void validateTemporalSanadCreation(SaalMaaliEntity saalMaaliEntity, Long organId) {
		checkIfSanadTempExists(saalMaaliEntity, organId);
		checkIfSanadDaemiNashodeExists(saalMaaliEntity, organId);

		if (!saalMaaliEntity.getStatus().equals(SaalMaaliStatusEnum.InProgress))
			throw new FatalException("cannot_close_temporal_accounts");
		if (saalMaaliEntity.getStatus().equals(SaalMaaliStatusEnum.TemporalAccountsClosed))
			throw new FatalException("Temporal_accounts_already_closed");
	}

	private List<SanadHesabdariEntity> getListOfSanadHesabdariDaemi(SaalMaaliEntity saalMaaliEntity, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		localFilter.put("organId@eq", organId);
		localFilter.put("state@eq", SanadStateEnum.DAEM);
		List<SanadHesabdariEntity> sanadHesabdariList = getDataList(null, localFilter,
				SanadHesabdariEntity.PROP_TARIKH_SANAD, true, false);
		return sanadHesabdariList;
	}

	private List<SanadHesabdariEntity> getListOfSanadHesabdariDaemiToCreateMonthlySummarySanad(
			SaalMaaliEntity saalMaaliEntity, Long organId, Date fromDate, Date toDate) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		localFilter.put("organId@eq", organId);
		localFilter.put("state@eq", SanadStateEnum.DAEM);
		localFilter.put("sanadFunction@eq", SanadFunctionEnum.OMOMI);
		localFilter.put("tarikhSanad@ge", fromDate);
		localFilter.put("tarikhSanad@lt", toDate);
		List<SanadHesabdariEntity> sanadHesabdariList = getDataList(null, localFilter,
				SanadHesabdariEntity.PROP_TARIKH_SANAD, true, false);
		return sanadHesabdariList;
	}

	private SanadHesabdariEntity createSanadEntity(SaalMaaliEntity saalMaaliEntity, Long organId, Date tarikhSanad,
			String description, SanadFunctionEnum sanadFunction, String organName) {
		SanadHesabdariEntity sanadHesabdariCloseTemporalAccountsEntity = new SanadHesabdariEntity();
		sanadHesabdariCloseTemporalAccountsEntity.setTarikhSanad(tarikhSanad);
		sanadHesabdariCloseTemporalAccountsEntity.setOrganId(organId);
		sanadHesabdariCloseTemporalAccountsEntity.setOrganName(organName);
		sanadHesabdariCloseTemporalAccountsEntity.setSaalMaali(saalMaaliEntity);
		// SanadFunctionEnum sanadFunction = SanadFunctionEnum.BASTAN_HESABHA;
		sanadHesabdariCloseTemporalAccountsEntity.setSanadFunction(sanadFunction);
		sanadHesabdariCloseTemporalAccountsEntity.setState(SanadStateEnum.MOVAGHAT);
//		String description = SerajMessageUtil.getMessage("SanadHesabdariItem_closeTemporalAccounts");
		sanadHesabdariCloseTemporalAccountsEntity.setDescription(description);
		sanadHesabdariCloseTemporalAccountsEntity.setSanadHesabdariItem(new ArrayList<SanadHesabdariItemEntity>());
		return sanadHesabdariCloseTemporalAccountsEntity;
	}

	@Transactional(readOnly = false)
	public SanadHesabdariEntity createSanadEkhtetamieh(Long organId, Date tarikhSanad, Boolean isInMultipleLevelMode,
			SaalMaaliEntity saalMaaliEntity, int numberOfDecimals, UserEntity currentUser, String organDesc) {
//		SaalMaaliEntity saalMaaliEntity = getSaalmaaliByDate(tarikhSanad, organEntity);
		checkIfSanadTempExists(saalMaaliEntity, organId);
		checkIfSanadDaemiNashodeExists(saalMaaliEntity, organId);
		boolean checkAllHesabKolRecordsHaveDefinedTheirMahyat = true;// checkAllHesabKolRecordsHaveDefinedTheirMahyat(saalMaaliEntity,
																		// getCurrentOrgan());
		if (saalMaaliEntity.getStatus().equals(SaalMaaliStatusEnum.InProgress))
			throw new FatalException("temporal_accounts_unclosed");
		if (saalMaaliEntity.getStatus().equals(SaalMaaliStatusEnum.SanadEkhtetamiehCreated))
			throw new FatalException("Sanad_ekhtetamieh_already_created");
		if (checkAllHesabKolRecordsHaveDefinedTheirMahyat) {

			List<SanadHesabdariEntity> sanadHesabdariList = getListOfSanadHesabdariDaemi(saalMaaliEntity, organId);

			List<SanadHesabdariItemEntity> articles = new ArrayList<SanadHesabdariItemEntity>();
			for (SanadHesabdariEntity sanadHesabdariEntity : sanadHesabdariList) {
				List<SanadHesabdariItemEntity> sanadHesabdariItems = sanadHesabdariEntity.getSanadHesabdariItem();
				for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItems) {

					HesabKolEntity hesabKol = sanadHesabdariItemEntity.getHesabKol();

					if (!hesabKol.getHesabGroup().getMahyatGroup().equals(MahyatGroupEnum.TARAZNAMEH)) // faghat
																										// hesabhaye
																										// taraznamehyi
																										// dar sanad
																										// ekhtetamieh
																										// miyayad
						continue;

					if (hesabKol.getMahyatKol().equals(MahyatKolEnum.Undefined))
						throw new MahyatKolNotDefinedException(hesabKol);

					HesabMoeenEntity hesabMoeen = sanadHesabdariItemEntity.getHesabMoeen();
					HesabTafsiliEntity hesabTafsili = sanadHesabdariItemEntity.getHesabTafsili();
					HesabTafsiliEntity hesabTafsiliTwo = sanadHesabdariItemEntity.getHesabTafsiliTwo();

					SanadHesabdariItemEntity entity = new SanadHesabdariItemEntity();
					entity.setHesabKol(hesabKol);
					entity.setHesabMoeen(hesabMoeen);
					entity.setHesabTafsili(hesabTafsili);
					entity.setHesabTafsiliTwo(hesabTafsiliTwo);

					entity.setBedehkar(sanadHesabdariItemEntity.getBestankar());
					entity.setBestankar(sanadHesabdariItemEntity.getBedehkar());
					entity.setDescription("-");

					articles.add(entity);

				}
			}

			SanadHesabdariEntity sanadHesabdariEkhtetamiehEntity = SanadHesabdariUtil
					.createMergedEkhtetamiehSanadHesabdari(organId, tarikhSanad, articles,
							SerajMessageUtil.getMessage("SanadHesabdari_sanadEkhtetamieh"), null, false,
							SanadStateEnum.MOVAGHAT, false, saalMaaliEntity, YesNoEnum.NO, numberOfDecimals,
							currentUser, organDesc);
			sanadHesabdariEkhtetamiehEntity.setSanadFunction(SanadFunctionEnum.EKHTETAMIE);

			duplicateEntity(sanadHesabdariEkhtetamiehEntity.getOldEntity(), sanadHesabdariEkhtetamiehEntity);
			saveMovaghat(sanadHesabdariEkhtetamiehEntity, new ArrayList<TempUploadedFileEntity>(), organId,
					saalMaaliEntity, false, currentUser, organDesc);

			saalMaaliEntity.setStatus(SaalMaaliStatusEnum.SanadEkhtetamiehCreated);
			saalMaaliService.save(saalMaaliEntity);

			return sanadHesabdariEkhtetamiehEntity;
		}
		throw new FatalException();
	}

	private void checkIfSanadDaemiNashodeExists(SaalMaaliEntity saalMaaliEntity, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		localFilter.put("organId@eq", organId);
		localFilter.put("state@in", Arrays.asList(SanadStateEnum.MOVAGHAT, SanadStateEnum.BARRESI_SHODE,
				SanadStateEnum.YADDASHT, SanadStateEnum.TEMP));
		Integer rowCount = getRowCount(localFilter);
		if (rowCount > 0)
			throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_SanadDaemiNashodeExists"));

	}

	private void checkIfSanadTempExists(SaalMaaliEntity saalMaaliEntity, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		localFilter.put("organId@eq", organId);
		localFilter.put("state@in", Arrays.asList(SanadStateEnum.TEMP));
		Integer rowCount = getRowCount(localFilter);
		if (rowCount > 0)
			throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_SanadTempExists"));

	}

	private void checkIfSanadDaemiNashodeExists(SaalMaaliEntity saalMaaliEntity, Long organId, Date fromDate,
			Date toDate) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		localFilter.put("organId@eq", organId);
		localFilter.put("state@in", Arrays.asList(SanadStateEnum.MOVAGHAT, SanadStateEnum.BARRESI_SHODE,
				SanadStateEnum.YADDASHT, SanadStateEnum.TEMP));
		localFilter.put("tarikhSanad@ge", fromDate);
		localFilter.put("tarikhSanad@le", toDate);
		Integer rowCount = getRowCount(localFilter);
		if (rowCount > 0)
			throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_SanadDaemiNashodeExists"));

	}

	/*
	 * private boolean checkAllHesabKolRecordsHaveDefinedTheirMahyat(
	 * SaalMaaliEntity saalMaaliEntity, OrganEntity organEntity) { Map<String,
	 * Object> localFilter = new HashMap<String, Object>();
	 * localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
	 * localFilter.put("organId@eq", organEntity.getId());
	 * localFilter.put("mahyatKol@eq", MahyatKolEnum.Undefined); Integer rowCount =
	 * hesabKolService.getRowCount(localFilter); if(rowCount > 0) return
	 * false; return true; }
	 */

	public SanadHesabdariEntity getSanadEftetahieh(SaalMaaliEntity saalMaaliEntity, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		localFilter.put("organId@eq", organId);
		localFilter.put("sanadFunction@eq", SanadFunctionEnum.EFTETAHIE);
		SanadHesabdariEntity sanadHesabdariEntity = load(null, localFilter);
		return sanadHesabdariEntity;
	}

	public List<SanadHesabdariEntity> getSanadEftetahiehha(SaalMaaliEntity saalMaaliEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		localFilter.put("sanadFunction@eq", SanadFunctionEnum.EFTETAHIE);
		return getDataList(null, localFilter);
	}

	public SanadHesabdariEntity getSanadEkhtetamieh(SaalMaaliEntity saalMaaliEntity, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		localFilter.put("organId@eq", organId);
		localFilter.put("sanadFunction@eq", SanadFunctionEnum.EKHTETAMIE);
		SanadHesabdariEntity sanadHesabdariEntity = load(null, localFilter);
		return sanadHesabdariEntity;
	}

	@Transactional(readOnly = false)
	public void deleteSanadEkhtetamieh(SaalMaaliEntity activeSaalmaali, Long organId) {
		SanadHesabdariEntity sanadEkhtetamieh = getSanadEkhtetamieh(activeSaalmaali, organId);
		activeSaalmaali.setStatus(SaalMaaliStatusEnum.TemporalAccountsClosed);
//		deleteWithoutDeletableValidation(sanadEkhtetamieh.getId());
		super.delete(sanadEkhtetamieh.getId());
		saalMaaliService.save(activeSaalmaali);

	}

	@Transactional(readOnly = false)
	public void deleteSanadEftetahieh(SaalMaaliEntity activeSaalmaali, Long organId) {
		SanadHesabdariEntity sanadEftetahieh = getSanadEftetahieh(activeSaalmaali, organId);
		deleteWithoutDeletableValidation(sanadEftetahieh.getId());
		saalMaaliService.save(activeSaalmaali);

	}

	@Transactional(readOnly = false)
	public void createSanadEftetahieh(Date tarikhSanadEftetahieh, Boolean isInMultipleLevelMode,
			SaalMaaliEntity activeSaalmaali, int numberOfDecimals, OrganVO currentOrganVO, UserEntity currentUser) {

//		SaalMaaliEntity activeSaalmaali = getSaalmaaliByDate(tarikhSanadEftetahieh, currentOrgan);
		List<SanadHesabdariItemEntity> sanadEftetahiehArticles = new ArrayList<SanadHesabdariItemEntity>();

		try {
			SaalMaaliEntity previousSaalMaali = saalMaaliService.getPreviousSaalMaali(activeSaalmaali, currentOrganVO);
			if (previousSaalMaali == null)
				throw new FatalException(SerajMessageUtil.getMessage("previousSaalMaali_doesnot_exists"));
			SanadHesabdariEntity previousSaalMaaliSanadEkhtetamieh = getSanadEkhtetamieh(previousSaalMaali,
					currentOrganVO.getId());

			if (previousSaalMaaliSanadEkhtetamieh == null
					|| !previousSaalMaaliSanadEkhtetamieh.getState().equals(SanadStateEnum.DAEM))
				throw new FatalException(SerajMessageUtil.getMessage("previousYearSanadEkhtetamieh_doesnot_exists",
						previousSaalMaali.getSaal()));
			List<SanadHesabdariItemEntity> sanadHesabdariItems = previousSaalMaaliSanadEkhtetamieh
					.getSanadHesabdariItem();

			for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItems) {
				SanadHesabdariItemEntity itemEntity = new SanadHesabdariItemEntity();
				itemEntity.setBedehkar(sanadHesabdariItemEntity.getBestankar());
				itemEntity.setBestankar(sanadHesabdariItemEntity.getBedehkar());
				itemEntity.setDescription("-");
				HesabKolEntity hesabKol = hesabKolService.loadHesabKolByCode(sanadHesabdariItemEntity.getHesabKolCode(),
						activeSaalmaali.getId(), FlushMode.ALWAYS);
				if (hesabKol == null)
					hesabKol = hesabKolService.createHesabKolStateLess(activeSaalmaali,
							sanadHesabdariItemEntity.getHesabKol(), currentOrganVO.getTopOrgansIdList());

				itemEntity.setHesabKol(hesabKol);

				HesabMoeenEntity hesabMoeenEntity = hesabMoeenService.loadHesabMoeenByCode(
						sanadHesabdariItemEntity.getHesabMoeenCode(), activeSaalmaali.getId(), FlushMode.ALWAYS);
				if (hesabMoeenEntity == null)
					hesabMoeenEntity = hesabMoeenService.createHesabMoeen(activeSaalmaali,
							sanadHesabdariItemEntity.getHesabMoeen(), currentOrganVO.getId(),
							currentOrganVO.getTopOrgansIdList(), currentOrganVO.getName());
				itemEntity.setHesabMoeen(hesabMoeenEntity);

				if (sanadHesabdariItemEntity.getHesabTafsili() != null
						&& sanadHesabdariItemEntity.getHesabTafsili().getId() != null) {
					HesabTafsiliEntity hesabTafsiliEntity = hesabTafsiliService.loadHesabTafsiliByCode(
							sanadHesabdariItemEntity.getHesabTafsili().getCode(), activeSaalmaali.getId(),
							FlushMode.ALWAYS);
					if (hesabTafsiliEntity == null) {
						hesabTafsiliEntity = hesabTafsiliService.createHesabTafsili(activeSaalmaali,
								sanadHesabdariItemEntity.getHesabTafsili(), currentOrganVO.getId(),
								currentOrganVO.getTopOrgansIdList(), currentOrganVO.getTopParentCode(),
								currentOrganVO.getName());
						hesabTafsiliService.createHesabTafsiliRelatedEntities(
								sanadHesabdariItemEntity.getHesabTafsili(), hesabTafsiliEntity, activeSaalmaali,
								currentOrganVO.getTopOrgansIdList());
					}
					itemEntity.setHesabTafsili(hesabTafsiliEntity);
				}

				if (sanadHesabdariItemEntity.getHesabTafsiliTwo() != null
						&& sanadHesabdariItemEntity.getHesabTafsiliTwo().getId() != null) {
					HesabTafsiliEntity hesabTafsiliTwoEntity = hesabTafsiliService.loadHesabTafsiliByCode(
							sanadHesabdariItemEntity.getHesabTafsiliTwo().getCode(), activeSaalmaali.getId(),
							FlushMode.ALWAYS);
					if (hesabTafsiliTwoEntity == null) {
						hesabTafsiliTwoEntity = hesabTafsiliService.createHesabTafsili(activeSaalmaali,
								sanadHesabdariItemEntity.getHesabTafsiliTwo(), currentOrganVO.getId(),
								currentOrganVO.getTopOrgansIdList(), currentOrganVO.getTopParentCode(),
								currentOrganVO.getName());
						hesabTafsiliService.createHesabTafsiliRelatedEntities(
								sanadHesabdariItemEntity.getHesabTafsili(), hesabTafsiliTwoEntity, activeSaalmaali,
								currentOrganVO.getTopOrgansIdList());
					}
					itemEntity.setHesabTafsiliTwo(hesabTafsiliTwoEntity);
				}

				itemEntity.setTarikhArticle(DateConverter.getCurrentDate());
				sanadEftetahiehArticles.add(itemEntity);
			}
		} catch (NoSaalMaaliFoundException e) {
			System.out.println(e.getDesc());
		}

		List<SanadHesabdariItemEntity> mergedArticles = SanadHesabdariUtil.createMergedArticles(sanadEftetahiehArticles,
				false, currentOrganVO.getId());
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : mergedArticles) {
			sanadHesabdariItemEntity.setDescription(
					SerajMessageUtil.getMessage("SanadHesabdari_createSanadEftetahieh", activeSaalmaali.getDesc()));
		}

		SanadHesabdariEntity sanadHesabdariEftetahiehEntity = SanadHesabdariUtil.createSanadHesabdari(
				currentOrganVO.getId(), tarikhSanadEftetahieh, mergedArticles,
				SerajMessageUtil.getMessage("SanadHesabdari_sanadEftetahieh"), null, SanadStateEnum.MOVAGHAT, false,
				null, activeSaalmaali, YesNoEnum.NO, numberOfDecimals, currentUser, currentOrganVO.getName());

		sanadHesabdariEftetahiehEntity.setSanadFunction(SanadFunctionEnum.EFTETAHIE);

		duplicateEntity(sanadHesabdariEftetahiehEntity.getOldEntity(), sanadHesabdariEftetahiehEntity);
		saveMovaghat(sanadHesabdariEftetahiehEntity, new ArrayList<TempUploadedFileEntity>(), currentOrganVO.getId(),
				activeSaalmaali, false, currentUser, currentOrganVO.getName());

	}

	public SanadHesabdariEntity loadLastSanadEntity(SaalMaaliEntity activeSaalmaali, Long currentOrganId) {
		return getMyDAO().loadLastSanadEntity(currentOrganId, activeSaalmaali);
	}

	public Long getLastSanadEntityID(SaalMaaliEntity activeSaalmaali, Long organId) {
		return getMyDAO().getLastSanadEntityID(organId, activeSaalmaali);
	}

	public List<SanadHesabdariEntity> getSanadHesabdariBySanadType(Long organId, Date fromDate, Date toDate,
			SanadTypeEntity sanadType) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("tarikhSanad@ge", fromDate);
		filter.put("tarikhSanad@le", toDate);
		filter.put("organId@eq", organId);
		filter.put("sanadType.id@eq", sanadType.getId());
		List<SanadHesabdariEntity> dataList = getDataList(null, filter);
		return dataList;

	}

	@Override
	public void doExtraDeleteAction(SanadHesabdariEntity entity) {
		// TODO Auto-generated method stub
		super.doExtraDeleteAction(entity);
	}

	@Transactional(readOnly = false)
	public void deleteWithoutDeletableValidation(Long id) {

		SanadHesabdariEntity sanadHesabdariEntity = load(id);
		checkSaalMaaliIsInProgress(sanadHesabdariEntity.getSaalMaali(), sanadHesabdariEntity);
		if (sanadHesabdariEntity.getState().equals(SanadStateEnum.BARRESI_SHODE)
				|| sanadHesabdariEntity.getState().equals(SanadStateEnum.DAEM))
			throw new FatalException(
					SerajMessageUtil.getMessage("SanadHesabdari_confirmedCannotDelete", sanadHesabdariEntity));
//		sanadHesabdariEntity.getSanadHesabdariItem().clear();
//		update(sanadHesabdariEntity);
//		getMyDAO().flush();
		super.delete(id);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Long id) {
		SanadHesabdariEntity sanadHesabdariEntity = load(id);
		checkSaalMaaliIsInProgress(sanadHesabdariEntity.getSaalMaali(), sanadHesabdariEntity);
		if (sanadHesabdariEntity.getIsSanadHesabdariManualyDeletable())
			doEbtalSanad(sanadHesabdariEntity); // super.delete(id);
		else
			throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_cannotDelete", sanadHesabdariEntity));
	}

	@Transactional(readOnly = false)
	private void doEbtalSanad(SanadHesabdariEntity entity) {
		entity.setState(SanadStateEnum.EBTAL);
		update(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public void update(SanadHesabdariEntity entity) {
		update(entity, true);
	}

	@Transactional(readOnly = false)
	public void update(SanadHesabdariEntity entity, boolean validateSaalMaaliInProgress) {
		if (validateSaalMaaliInProgress)
			checkSaalMaaliIsInProgress(entity.getSaalMaali(), entity);
		super.update(entity);
	}

	@Override
	public SanadHesabdariEntity load(Long id) {
		// TODO Auto-generated method stub
		return super.load(id);
	}

	public SanadHesabdariEntity loadBySerial(Long sanadHesabdariSerial, Long currentOrganId,
			SaalMaaliEntity activeSaalMaali) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("serial@eq", sanadHesabdariSerial);
		localFilter.put("organId@eq", currentOrganId);
		localFilter.put("saalMaali.id@eq", activeSaalMaali.getId());
		localFilter.put("state@notIn", Arrays.asList(SanadStateEnum.EBTAL, SanadStateEnum.MonthlySummary));
		return load(null, localFilter);
	}

	@Transactional
	public SanadHesabdariEntity mergeTempSanadHesabdaris(Long organId, Date sanadHesabdariDate,
			SanadTypeEntity sanadTypeEntity, String description, SaalMaaliEntity saalMaali, int numberOfDecimals,
			UserEntity currentUser, String organDesc) {
		Date startOfToday = DateConverter.getStartOfToday(sanadHesabdariDate).getTime();
		Date startOfTommorow = DateConverter.getStartOfTommorow(sanadHesabdariDate).getTime();
		List<SanadHesabdariEntity> tempSanadHesabdariList = getSanadHesabdariBySanadType(organId, startOfToday,
				startOfTommorow, sanadTypeEntity);

		List<SanadHesabdariItemEntity> mergedArticles = new ArrayList<SanadHesabdariItemEntity>();

		for (SanadHesabdariEntity sanadHesabdariEntity : tempSanadHesabdariList) {
			List<SanadHesabdariItemEntity> sanadHesabdariItem = sanadHesabdariEntity.getSanadHesabdariItem();

			List<SanadHesabdariItemEntity> tempList = new ArrayList<SanadHesabdariItemEntity>();
			for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItem) {
				SanadHesabdariItemEntity tempEntity = new SanadHesabdariItemEntity();
				sanadHesabdariItemService.duplicateEntityWithoutId(tempEntity, sanadHesabdariItemEntity);
				sanadHesabdariItemService.cleanNullRelations(tempEntity);
				tempList.add(tempEntity);

			}
			List<SanadHesabdariItemEntity> articles = tempList;// MaaliAutomaticSanadUtil.createMergedArticles(tempList,
																// true);
			mergedArticles.addAll(articles);

			deleteWithoutDeletableValidation(sanadHesabdariEntity.getId());
		}

		if (mergedArticles.size() > 0) {
			SanadHesabdariEntity sanadHesabdariEntity = AutomaticSanadUtil.createSanadHesabdari(organId,
					sanadHesabdariDate, mergedArticles, description, sanadTypeEntity, SanadStateEnum.MOVAGHAT,
					saalMaali, YesNoEnum.NO, numberOfDecimals, currentUser, organDesc);
			sanadHesabdariEntity.setDeletable(YesNoEnum.NO);
			return sanadHesabdariEntity;
		}

		return null;
	}

//	private List<SanadHesabdariEntity> getTempSanadHesabdariList(OrganEntity organ, Date fromDate, Date toDate, SanadTypeEntity sanadTypeEntity) {
//		Map<String, Object> sanadFilter = new HashMap<String, Object>();
//		sanadFilter.put("state@eq", SanadStateEnum.TEMP);
//		sanadFilter.put("sanadType.id@eq", sanadTypeEntity.getId());
//		sanadFilter.put("organId@eq", organ.getId());
//		sanadFilter.put("tarikhSanad@ge", fromDate);
//		sanadFilter.put("tarikhSanad@lt", toDate);
//		
//		List<SanadHesabdariEntity> dataList = getDataList(null, sanadFilter,SanadHesabdariEntity.PROP_TARIKH_SANAD,true,false);
//		return dataList;
//	}

	@Transactional
	public void createMonthlySummarySanad(SaalMaaliEntity saalMaaliEntity, Long organId, UserEntity currentUser,
			String organDesc) {
		List<MonthEntity> list = monthService.getList(saalMaaliEntity.getId());
		for (MonthEntity monthEntity : list) {

			if (monthEntity.getStartDate() == null || monthEntity.getEndDate() == null)
				throw new FatalException(
						SerajMessageUtil.getMessage("Month_notConfiguredForSaalMaali", saalMaaliEntity.getDesc()));

			Map<String, Object> sanadFilter = new HashMap<>();
			sanadFilter.put("sanadFunction@eq", SanadFunctionEnum.MonthlySummary);
			sanadFilter.put("state@eq", SanadStateEnum.MonthlySummary);
			sanadFilter.put("tarikhSanad@eq", monthEntity.getEndDate());
			sanadFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
			sanadFilter.put("organId@eq", organId);
			SanadHesabdariEntity monthlySummarySanad = load(null, sanadFilter);
			if (monthlySummarySanad != null)
				continue;

			if (monthEntity.getStartDate() == null || monthEntity.getEndDate() == null)
				throw new FatalException(
						SerajMessageUtil.getMessage("Month_notConfiguredForSaalMaali", saalMaaliEntity.getDesc()));

			Calendar fromDate = DateConverter.getStartOfToday(monthEntity.getStartDate());
			Calendar toDate = DateConverter.getStartOfTommorow(monthEntity.getEndDate());

			checkIfSanadDaemiNashodeExists(saalMaaliEntity, organId, fromDate.getTime(), toDate.getTime());
			List<SanadHesabdariEntity> sanadHesabdariList = getListOfSanadHesabdariDaemiToCreateMonthlySummarySanad(
					saalMaaliEntity, organId, fromDate.getTime(), toDate.getTime());

			String description = SerajMessageUtil.getMessage("SanadHesabdari_monthlySummarySanad",
					monthEntity.getName());

			monthlySummarySanad = createSanadEntity(saalMaaliEntity, organId, monthEntity.getEndDate(), description,
					SanadFunctionEnum.MonthlySummary, organDesc);

			List<SanadHesabdariItemEntity> summaryItems = new ArrayList<SanadHesabdariItemEntity>();

			for (SanadHesabdariEntity sanadHesabdariEntity : sanadHesabdariList) {
				List<SanadHesabdariItemEntity> sanadHesabdariItems = sanadHesabdariEntity.getSanadHesabdariItem();
				for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItems) {
					SanadHesabdariItemEntity itemEntity = new SanadHesabdariItemEntity();
					itemEntity.setHesabKol(sanadHesabdariItemEntity.getHesabKol());
					itemEntity.setBedehkar(sanadHesabdariItemEntity.getBedehkar());
					itemEntity.setBestankar(sanadHesabdariItemEntity.getBestankar());
					itemEntity.setSanadHesabdari(monthlySummarySanad);
					summaryItems.add(itemEntity);
				}
			}
			List<SanadHesabdariItemEntity> mergedArticles = AutomaticSanadUtil
					.createMergedArticlesKeepingBedehkarBestankar(summaryItems, false, organId);
			monthlySummarySanad.setSanadHesabdariItem(mergedArticles);
			monthlySummarySanad.setSerial(monthEntity.getRadif().longValue());
			saveMonthlySummary(monthlySummarySanad, null, organId, saalMaaliEntity, false, currentUser, organDesc);
		}

		createEftetahiehSummarySanad(saalMaaliEntity, organId, currentUser, organDesc);
		createEkhtetamiehSummarySanad(saalMaaliEntity, organId, currentUser, organDesc);
		createBastanHesabhaSummarySanad(saalMaaliEntity, organId, currentUser, organDesc);
	}

	@Transactional
	private void createEftetahiehSummarySanad(SaalMaaliEntity saalMaaliEntity, Long organId, UserEntity currentUser,
			String organDesc) {
		Map<String, Object> sanadEFTETAHIEMonthlySummaryFilter = new HashMap<>();
		sanadEFTETAHIEMonthlySummaryFilter.put("sanadFunction@eq", SanadFunctionEnum.EFTETAHIESummary);
		sanadEFTETAHIEMonthlySummaryFilter.put("state@eq", SanadStateEnum.MonthlySummary);
		sanadEFTETAHIEMonthlySummaryFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		sanadEFTETAHIEMonthlySummaryFilter.put("organId@eq", organId);
		SanadHesabdariEntity eftetahiehSummarySanad = load(sanadEFTETAHIEMonthlySummaryFilter);
		if (eftetahiehSummarySanad == null) {
			Map<String, Object> sanadEFTETAHIEFilter = new HashMap<>();
			sanadEFTETAHIEFilter.put("sanadFunction@eq", SanadFunctionEnum.EFTETAHIE);
			sanadEFTETAHIEFilter.put("state@eq", SanadStateEnum.DAEM);
			sanadEFTETAHIEFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
			sanadEFTETAHIEFilter.put("organId@eq", organId);
			SanadHesabdariEntity eftetahiehSanad = load(sanadEFTETAHIEFilter);
			if (eftetahiehSanad == null)
				return;

			String description = SerajMessageUtil.getMessage("SanadHesabdari_eftetahieSummarySanad");
			eftetahiehSummarySanad = createSanadEntity(saalMaaliEntity, organId, saalMaaliEntity.getStartDate(),
					description, SanadFunctionEnum.EFTETAHIESummary, organDesc);
			eftetahiehSummarySanad.setTarikhSanad(eftetahiehSanad.getTarikhSanad());

			List<SanadHesabdariItemEntity> summaryItems = new ArrayList<SanadHesabdariItemEntity>();

			List<SanadHesabdariItemEntity> sanadHesabdariItems = eftetahiehSanad.getSanadHesabdariItem();
			for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItems) {
				SanadHesabdariItemEntity itemEntity = new SanadHesabdariItemEntity();
				itemEntity.setHesabKol(sanadHesabdariItemEntity.getHesabKol());
				itemEntity.setBedehkar(sanadHesabdariItemEntity.getBedehkar());
				itemEntity.setBestankar(sanadHesabdariItemEntity.getBestankar());
				itemEntity.setSanadHesabdari(eftetahiehSummarySanad);
				summaryItems.add(itemEntity);
			}
			List<SanadHesabdariItemEntity> mergedArticles = AutomaticSanadUtil
					.createMergedArticlesKeepingBedehkarBestankar(summaryItems, false, organId);
			eftetahiehSummarySanad.setSanadHesabdariItem(mergedArticles);
			eftetahiehSummarySanad.setSerial(1l);
			saveMonthlySummary(eftetahiehSummarySanad, null, organId, saalMaaliEntity, false, currentUser, organDesc);
		}
	}

	@Transactional
	private void createEkhtetamiehSummarySanad(SaalMaaliEntity saalMaaliEntity, Long organId, UserEntity currentUser,
			String organDesc) {
		Map<String, Object> sanadEKHTETAMIEMonthlySummaryFilter = new HashMap<>();
		sanadEKHTETAMIEMonthlySummaryFilter.put("sanadFunction@eq", SanadFunctionEnum.EKHTETAMIESummary);
		sanadEKHTETAMIEMonthlySummaryFilter.put("state@eq", SanadStateEnum.MonthlySummary);
		sanadEKHTETAMIEMonthlySummaryFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		sanadEKHTETAMIEMonthlySummaryFilter.put("organId@eq", organId);
		SanadHesabdariEntity ekhtetamiehSummarySanad = load(sanadEKHTETAMIEMonthlySummaryFilter);
		if (ekhtetamiehSummarySanad == null) {
			Map<String, Object> sanadEKHTETAMIEFilter = new HashMap<>();
			sanadEKHTETAMIEFilter.put("sanadFunction@eq", SanadFunctionEnum.EKHTETAMIE);
			sanadEKHTETAMIEFilter.put("state@eq", SanadStateEnum.DAEM);
			sanadEKHTETAMIEFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
			sanadEKHTETAMIEFilter.put("organId@eq", organId);
			SanadHesabdariEntity ekhtetamiehSanad = load(sanadEKHTETAMIEFilter);
			if (ekhtetamiehSanad == null)
				return;

			String description = SerajMessageUtil.getMessage("SanadHesabdari_ekhtetamieSummarySanad");

			ekhtetamiehSummarySanad = createSanadEntity(saalMaaliEntity, organId, saalMaaliEntity.getStartDate(),
					description, SanadFunctionEnum.EKHTETAMIESummary, organDesc);
			ekhtetamiehSummarySanad.setTarikhSanad(ekhtetamiehSanad.getTarikhSanad());

			List<SanadHesabdariItemEntity> summaryItems = new ArrayList<SanadHesabdariItemEntity>();

			List<SanadHesabdariItemEntity> sanadHesabdariItems = ekhtetamiehSanad.getSanadHesabdariItem();
			for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItems) {
				SanadHesabdariItemEntity itemEntity = new SanadHesabdariItemEntity();
				itemEntity.setHesabKol(sanadHesabdariItemEntity.getHesabKol());
				itemEntity.setBedehkar(sanadHesabdariItemEntity.getBedehkar());
				itemEntity.setBestankar(sanadHesabdariItemEntity.getBestankar());
				itemEntity.setSanadHesabdari(ekhtetamiehSummarySanad);
				summaryItems.add(itemEntity);
			}
			List<SanadHesabdariItemEntity> mergedArticles = AutomaticSanadUtil
					.createMergedArticlesKeepingBedehkarBestankar(summaryItems, false, organId);
			ekhtetamiehSummarySanad.setSanadHesabdariItem(mergedArticles);
			ekhtetamiehSummarySanad.setSerial(15l);
			saveMonthlySummary(ekhtetamiehSummarySanad, null, organId, saalMaaliEntity, false, currentUser, organDesc);
		}
	}

	@Transactional
	private void createBastanHesabhaSummarySanad(SaalMaaliEntity saalMaaliEntity, Long organId, UserEntity currentUser,
			String organDesc) {
		Map<String, Object> sanadBastanHesabhaSummaryFilter = new HashMap<>();
		sanadBastanHesabhaSummaryFilter.put("sanadFunction@eq", SanadFunctionEnum.BASTAN_HESABHASummary);
		sanadBastanHesabhaSummaryFilter.put("state@eq", SanadStateEnum.MonthlySummary);
		sanadBastanHesabhaSummaryFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		sanadBastanHesabhaSummaryFilter.put("organId@eq", organId);
		SanadHesabdariEntity bastanHesabhaSummarySanad = load(sanadBastanHesabhaSummaryFilter);
		if (bastanHesabhaSummarySanad == null) {
			Map<String, Object> BastanHesabhaFilter = new HashMap<>();
			BastanHesabhaFilter.put("sanadFunction@eq", SanadFunctionEnum.BASTAN_HESABHA);
			BastanHesabhaFilter.put("state@eq", SanadStateEnum.DAEM);
			BastanHesabhaFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
			BastanHesabhaFilter.put("organId@eq", organId);
			SanadHesabdariEntity BastanHesabhaSanad = load(BastanHesabhaFilter);
			if (BastanHesabhaSanad == null)
				return;

			String description = SerajMessageUtil.getMessage("SanadHesabdari_bastanHesabhaSummarySanad");

			bastanHesabhaSummarySanad = createSanadEntity(saalMaaliEntity, organId, saalMaaliEntity.getStartDate(),
					description, SanadFunctionEnum.BASTAN_HESABHASummary, organDesc);
			bastanHesabhaSummarySanad.setTarikhSanad(BastanHesabhaSanad.getTarikhSanad());

			List<SanadHesabdariItemEntity> summaryItems = new ArrayList<SanadHesabdariItemEntity>();

			List<SanadHesabdariItemEntity> sanadHesabdariItems = BastanHesabhaSanad.getSanadHesabdariItem();
			for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItems) {
				SanadHesabdariItemEntity itemEntity = new SanadHesabdariItemEntity();
				itemEntity.setHesabKol(sanadHesabdariItemEntity.getHesabKol());
				itemEntity.setBedehkar(sanadHesabdariItemEntity.getBedehkar());
				itemEntity.setBestankar(sanadHesabdariItemEntity.getBestankar());
				itemEntity.setSanadHesabdari(bastanHesabhaSummarySanad);
				summaryItems.add(itemEntity);
			}
			List<SanadHesabdariItemEntity> mergedArticles = AutomaticSanadUtil
					.createMergedArticlesKeepingBedehkarBestankar(summaryItems, false, organId);
			bastanHesabhaSummarySanad.setSanadHesabdariItem(mergedArticles);
			bastanHesabhaSummarySanad.setSerial(14l);
			saveMonthlySummary(bastanHesabhaSummarySanad, null, organId, saalMaaliEntity, false, currentUser,
					organDesc);
		}
	}

	@Transactional
	public void deleteMonthlySummarySanad(SanadHesabdariEntity entity) {
		super.delete(entity.getId());
	}

	@Transactional
	public void resetSerialDaemi(SaalMaaliEntity activeSaalmaali, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("saalMaali.id@eq", activeSaalmaali.getId());
		localFilter.put("organId@eq", organId);
		List<SanadHesabdariEntity> dataList = getDataList(null, localFilter, SanadHesabdariEntity.PROP_TARIKH_SANAD,
				true, false);
		for (SanadHesabdariEntity sanadHesabdariEntity : dataList) {
			sanadHesabdariEntity.setSerial(null);
			update(sanadHesabdariEntity);

		}
	}
}