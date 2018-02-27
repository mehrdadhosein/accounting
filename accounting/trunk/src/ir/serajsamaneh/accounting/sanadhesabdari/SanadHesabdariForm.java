package ir.serajsamaneh.accounting.sanadhesabdari;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;

import com.opencsv.CSVReader;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazService;
import ir.serajsamaneh.accounting.base.BaseAccountingForm;
import ir.serajsamaneh.accounting.enumeration.SanadFunctionEnum;
import ir.serajsamaneh.accounting.enumeration.SanadStateEnum;
import ir.serajsamaneh.accounting.exception.NoActiveSaalMaaliFoundException;
import ir.serajsamaneh.accounting.exception.NoSaalMaaliFoundException;
import ir.serajsamaneh.accounting.hesabkol.HesabKolService;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliService;
import ir.serajsamaneh.accounting.month.MonthService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity;
import ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemService;
import ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemVO;
import ir.serajsamaneh.accounting.sanadtype.SanadTypeEntity;
import ir.serajsamaneh.accounting.sanadtype.SanadTypeService;
import ir.serajsamaneh.core.exception.DuplicateException;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.exception.InCorrectInputException;
import ir.serajsamaneh.core.exception.MaxExcelRecordExportException;
import ir.serajsamaneh.core.exception.NoOrganFoundException;
import ir.serajsamaneh.core.file.FileEntity;
import ir.serajsamaneh.core.file.FileService;
import ir.serajsamaneh.core.security.ActionLogUtil;
import ir.serajsamaneh.core.security.SecurityUtil;
import ir.serajsamaneh.core.tempuploadedfile.TempUploadedFileEntity;
import ir.serajsamaneh.core.tempuploadedfile.TempUploadedFileService;
import ir.serajsamaneh.core.util.JQueryUtil;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.core.util.StringUtil;
import ir.serajsamaneh.enumeration.YesNoEnum;
import net.sf.jasperreports.engine.JRException;
import serajcomponent.DateConverter;
import serajcomponent.SerajDateTimePickerType;

public class SanadHesabdariForm extends
		BaseAccountingForm<SanadHesabdariEntity, Long> {

	@Override
	protected SanadHesabdariService getMyService() {
		return sanadHesabdariService;
	}

	SanadHesabdariService sanadHesabdariService;
	SaalMaaliService saalMaaliService;
	HesabTafsiliService hesabTafsiliService;
	AccountingMarkazService accountingMarkazService;
	HesabKolService hesabKolService;
//	MarkazHazineService markazHazineService;
	HesabMoeenService hesabMoeenService;
	FileService fileService;
	SanadHesabdariItemService sanadHesabdariItemService;
	MonthService monthService;

	public MonthService getMonthService() {
		return monthService;
	}

	public void setMonthService(MonthService monthService) {
		this.monthService = monthService;
	}

	String sanadItems;
	String sanadItemsXML;
	List<SelectItem> sanadTypes = null;
	List<Long> sanadTypeIds = null;
	


	public List<Long> getSanadTypeIds() {
		return sanadTypeIds;
	}

	public void setSanadTypeIds(List<Long> sanadTypeIds) {
		this.sanadTypeIds = sanadTypeIds;
	}

	
	public List<SelectItem> getHierarchicalSanadTypes() {
		if(sanadTypes == null){
			sanadTypes = new ArrayList<SelectItem>();
			Map<String, Object> localFilter = new HashMap<>();
			List<Long> topOrganList = getTopOrgansIdList(getCurrentOrgan());
			localFilter.put("organ.id@in", topOrganList);

			List<SanadTypeEntity> sanadTypeList =  getSanadTypeService().getDataList(null, localFilter);
//			sanadTypes.add(new SelectItem(null, "-------------"));
			for(SanadTypeEntity entity : sanadTypeList)
				sanadTypes.add(new SelectItem(entity.getId(), entity.getName()));
		}
		return sanadTypes;
	}

	public List<SelectItem> getSanadTypes() {
		if(sanadTypes == null){
			sanadTypes = new ArrayList<SelectItem>();
			List<SanadTypeEntity> sanadTypeList =  getSanadTypeService().getDataList();
//			sanadTypes.add(new SelectItem(null, "-------------"));
			for(SanadTypeEntity entity : sanadTypeList)
				sanadTypes.add(new SelectItem(entity.getId(), entity.getName()));
		}
		return sanadTypes;
	}

	public void setSanadTypes(List<SelectItem> sanadTypes) {
		this.sanadTypes = sanadTypes;
	}

	public SanadHesabdariItemService getSanadHesabdariItemService() {
		return sanadHesabdariItemService;
	}

	public void setSanadHesabdariItemService(
			SanadHesabdariItemService sanadHesabdariItemService) {
		this.sanadHesabdariItemService = sanadHesabdariItemService;
	}

	public HesabKolService getHesabKolService() {
		return hesabKolService;
	}

	public void setHesabKolService(HesabKolService hesabKolService) {
		this.hesabKolService = hesabKolService;
	}

	public AccountingMarkazService getAccountingMarkazService() {
		return accountingMarkazService;
	}

	public void setAccountingMarkazService(
			AccountingMarkazService accountingMarkazService) {
		this.accountingMarkazService = accountingMarkazService;
	}

/*	
	public MarkazHazineService getMarkazHazineService() {
		return markazHazineService;
	}
	
	public void setMarkazHazineService(MarkazHazineService markazHazineService) {
		this.markazHazineService = markazHazineService;
	}*/

	public FileService getFileService() {
		return fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	public HesabMoeenService getHesabMoeenService() {
		return hesabMoeenService;
	}

	public void setHesabMoeenService(HesabMoeenService hesabMoeenService) {
		this.hesabMoeenService = hesabMoeenService;
	}

	public HesabTafsiliService getHesabTafsiliService() {
		return hesabTafsiliService;
	}

	public void setHesabTafsiliService(HesabTafsiliService hesabTafsiliService) {
		this.hesabTafsiliService = hesabTafsiliService;
	}

	public void setSaalMaaliService(SaalMaaliService saalMaaliService) {
		this.saalMaaliService = saalMaaliService;
	}

	public SaalMaaliService getSaalMaaliService() {
		return saalMaaliService;
	}

	public SanadHesabdariService getSanadHesabdariService() {
		return sanadHesabdariService;
	}

	public void setSanadHesabdariService(
			SanadHesabdariService sanadHesabdariService) {
		this.sanadHesabdariService = sanadHesabdariService;
	}
	
	public SanadHesabdariEntity getLastSanadEntity(){
		if(entity == null){
			Long sanadId = getMyService().getLastSanadEntityID(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
			setID(sanadId);
		}
		
		return entity;
	}

	Integer incorrectInputExceptionRow;
	protected String incorectItem;

	protected SanadHesabdariItemEntity populateSanadHesabdariItem(
			Map<String, String> map,
			SanadHesabdariItemEntity sanadHesabdariItemEntity, Boolean isInMultipleLevelMode) {

		incorectItem = "entityId";
		if (StringUtils.hasText(map.get("entityId")))
			sanadHesabdariItemEntity.setEntityId(map.get("entityId"));

		incorectItem = "SanadHesabdariItem_hesabTafsili";
		if(isInMultipleLevelMode && StringUtils.hasText(map.get("hesabTafsiliOneID")))
			sanadHesabdariItemEntity.setHesabTafsili(getHesabTafsiliService().load(new Long(map.get("hesabTafsiliOneID"))));
		else if(!isInMultipleLevelMode)
			sanadHesabdariItemEntity.setHesabTafsili(getHesabTafsiliService().load(new Long(map.get("hesabTafsiliOneID"))));

		incorectItem = "SanadHesabdariItem_hesabTafsiliTwo";
		if(isInMultipleLevelMode && StringUtils.hasText(map.get("hesabTafsiliTwoID")))
			sanadHesabdariItemEntity.setHesabTafsiliTwo(getHesabTafsiliService().load(new Long(map.get("hesabTafsiliTwoID"))));
		else if(!isInMultipleLevelMode)
			sanadHesabdariItemEntity.setHesabTafsiliTwo(getHesabTafsiliService().load(new Long(map.get("hesabTafsiliTwoID"))));
		
		incorectItem = "SanadHesabdariItem_accountingMarkaz";
		if(isInMultipleLevelMode && StringUtils.hasText(map.get("accountingMarkazID")))
			sanadHesabdariItemEntity.setAccountingMarkaz(getAccountingMarkazService().load(new Long(map.get("accountingMarkazID"))));
		else if(!isInMultipleLevelMode)
			sanadHesabdariItemEntity.setAccountingMarkaz(getAccountingMarkazService().load(new Long(map.get("accountingMarkazID"))));


		incorectItem = "SanadHesabdariItem_hesabMoeen";
		HesabMoeenEntity hesabMoeenEntity = getHesabMoeenService().load(new Long(map.get("hesabMoeenID")));
		sanadHesabdariItemEntity.setHesabMoeen(hesabMoeenEntity);
		sanadHesabdariItemEntity.setHesabKol(hesabMoeenEntity.getHesabKol());
		
/*		if (StringUtils.hasText(map.get("markazHazineID"))){
			MarkazHazineEntity markazHazineEntity = getMarkazHazineService().load(new Long(map.get("markazHazineID")));
			sanadHesabdariItemEntity.setMarkazHazine(markazHazineEntity);
		}*/
		
		
//		if (StringUtils.hasText(map.get("hesabTafsiliLevels"))){
//			String hesabTafsiliLevels = map.get("hesabTafsiliLevels");
//			String[] splited = hesabTafsiliLevels.split(",");
//			for (String hesabTafsiliLevel : splited) {
//				if(hesabTafsiliLevel.isEmpty())
//					continue;
//				String[] keyValue = hesabTafsiliLevel.split("=");
//				if(keyValue.length == 1)
//					continue;
//				String key = keyValue[0];
//				String value = keyValue[1];
//				
//				Integer level = new Integer(key.substring("hesabTafsili".length()));
//				ArticleTafsiliEntity articleTafsiliEntity = new ArticleTafsiliEntity();
//				articleTafsiliEntity.setHesabTafsili(getHesabTafsiliService().load(new Long(value)));
//				articleTafsiliEntity.setLevel(level);
//				articleTafsiliEntity.setSanadHesabdariItem(sanadHesabdariItemEntity);
//				sanadHesabdariItemEntity.addToarticleTafsili(articleTafsiliEntity);
//				
//			}
//		}
		
		
//		if (StringUtils.hasText(map.get("accountingMarkazLevels"))){
//			String accountingMarkazLevels = map.get("accountingMarkazLevels");
//			String[] splited = accountingMarkazLevels.split(",");
//			for (String accountingMarkazLevel : splited) {
//				if(accountingMarkazLevel.isEmpty())
//					continue;
//				String[] keyValue = accountingMarkazLevel.split("=");
//				if(keyValue.length == 1)
//					continue;
//				String key = keyValue[0];
//				String value = keyValue[1];
//				
//				Integer level = new Integer(key.substring("accountingMarkaz".length()));
//				ArticleAccountingMarkazEntity articleAccountingMarkazEntity = new ArticleAccountingMarkazEntity();
//				articleAccountingMarkazEntity.setAccountingMarkaz(getAccountingMarkazService().load(new Long(value)));
//				articleAccountingMarkazEntity.setLevel(level);
//				articleAccountingMarkazEntity.setSanadHesabdariItem(sanadHesabdariItemEntity);
//				sanadHesabdariItemEntity.addToarticleAccountingMarkaz(articleAccountingMarkazEntity);
//				
//			}
//		}
		
		

		incorectItem = "SanadHesabdariItem_bestankar";
		if (StringUtils.hasText(map.get("bestankar")))
			sanadHesabdariItemEntity.setBestankar(new Double(map
					.get("bestankar").replaceAll(",","")));
		else sanadHesabdariItemEntity.setBestankar(0d);

		incorectItem = "SanadHesabdariItem_bedehkar";
		if (StringUtils.hasText(map.get("bedehkar")))
			sanadHesabdariItemEntity
					.setBedehkar(new Double(map.get("bedehkar").replaceAll(",","")));
		else sanadHesabdariItemEntity.setBedehkar(0d);

		if(sanadHesabdariItemEntity.getBedehkar()>0 && sanadHesabdariItemEntity.getBestankar()>0){
			incorectItem = "SanadHesabdari_BestankarBedehkarValue" ;
			throw new FatalException();
		}
		
		incorectItem = "SanadHesabdariItem_tarikhArticle";
		sanadHesabdariItemEntity.setTarikhArticle(DateConverter
				.convertStringDateToGDate(SerajDateTimePickerType.Date,
						map.get("tarikhArticle"), "Shamsi"));

		incorectItem = "common_sharh";
		String description = map.get("description");

		if (StringUtils.hasText(map.get("description"))){
			description =description.replaceAll("\\p{Cntrl}", "");
			sanadHesabdariItemEntity.setDescription(description);
		}else throw new FatalException();

		return sanadHesabdariItemEntity;

	}

	protected List<SanadHesabdariItemEntity> initSanadHesabdariItemList(String sanadItemInput, Boolean isInMultipleLevelMode, SanadHesabdariEntity sanadHesabdariEntity){
		List<SanadHesabdariItemEntity> sanadHesabdariItemList = getSanadHesabdariItemList(sanadItemInput, isInMultipleLevelMode);
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItemList) {
			sanadHesabdariItemEntity.setSanadHesabdari(sanadHesabdariEntity);
		}
		
		return sanadHesabdariItemList;
	}

	private List<SanadHesabdariItemEntity> getSanadHesabdariItemList(
			String sanadItemInput, Boolean isInMultipleLevelMode) {
		List<Map<String, String>> list = JQueryUtil
				.convertJQueryXMLToList(sanadItemInput);
		if (list == null)
			return null;

		List<SanadHesabdariItemEntity> sanadHesabdariItemEntities = new ArrayList<SanadHesabdariItemEntity>();
		try {
			incorrectInputExceptionRow = 0;
			for (Map<String, String> map : list) {
				++incorrectInputExceptionRow;
				SanadHesabdariItemEntity sanadHesabdariItemEntity = new SanadHesabdariItemEntity();
				sanadHesabdariItemEntity = populateSanadHesabdariItem(map,
						sanadHesabdariItemEntity, isInMultipleLevelMode);
				// sanadKalaEntity.setSanad(sanadEntity);
				// sanadEntity.addToSanadKala(sanadKalaEntity);
				sanadHesabdariItemEntities.add(sanadHesabdariItemEntity);
			}
		} catch (NumberFormatException e) {
			throw new InCorrectInputException("Common_incorrectInput",
					SerajMessageUtil.getMessage(incorectItem),
					incorrectInputExceptionRow);
		} catch (FatalException e) {
			throw new InCorrectInputException("Common_incorrectInput",
					SerajMessageUtil.getMessage(incorectItem),
					incorrectInputExceptionRow);
		}
		return sanadHesabdariItemEntities;
	}

	protected void populateParameters_all(Map<String, Object> parameters) {


	}

	public String printSanad() {
		
		try {
			
			byte[] pdf=SanadHesabdariUtil.printSanad(getEntity());
			String contentType = "application/pdf";
			downloadStream(pdf, contentType, "Sanad_" + getEntity().getDesc()
					+ ".pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}
		logPrintAction(getEntity());
		return null;
	}
	public String printSanad(String reportFileName) {

		try {
			
			byte[] pdf=SanadHesabdariUtil.printSanad(getEntity(), reportFileName);
			String contentType = "application/pdf";
			downloadStream(pdf, contentType, "Sanad_" + getEntity().getDesc()
					+ ".pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}
		logPrintAction(getEntity());
		return null;
	}
	
	public String exportSanadSanadToExcel() {
		try{

			ByteArrayOutputStream byteArrayOutputStream = getSanadHesabdariItemService().exportToXLSX(getEntity().getSanadHesabdariItem());
			downloadStream(byteArrayOutputStream.toByteArray(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", getEntityName() + ".xlsx");
			
		}catch(IOException e){
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (MaxExcelRecordExportException e) {
			e.printStackTrace();
		}
		return null;
	}

/*	@Override
	public String save() {
		// Map<Long, SanadHesabdariItemVO> map = convertToSanadItems(items);
		try {
			List<SanadHesabdariItemEntity> sanadHesabdariItemList = getSanadHesabdariItemList(getSanadItemsXML());
			if (getEntity().getSanadHesabdariItem() == null)
				getEntity().setSanadHesabdariItem(
						new ArrayList<SanadHesabdariItemEntity>());
			else
				getEntity().getSanadHesabdariItem().clear();

			for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItemList) {
				sanadHesabdariItemEntity.setSanadHesabdari(getEntity());
				getEntity().getSanadHesabdariItem().add(
						sanadHesabdariItemEntity);
			}

			getEntity().setSaalMaali(
					getSaalMaaliService().load(
							getEntity().getSaalMaali().getId()));
			getMyService().save(getEntity());
		} catch (FatalException e) {
			addErrorMessage("SaalMaali_not_defined");
			return null;
		}
		return getLocalListUrl();
	}*/

//	public SaalMaaliEntity getActiveSaalmaali() {
//		return getCurrentUsertActiveSaalMaali();
//	}

/*	public String saveYaddasht() {
		getEntity().setState(SanadStateEnum.YADDASHT);
		return save();
	}*/

	Boolean isInMultipleLevelMode = true;
	public Boolean getIsInMultipleLevelMode() {
		return isInMultipleLevelMode;
	}

	public String saveMovaghat() {
		if(getEntity().getSanadFunction() == null)
			getEntity().setSanadFunction(SanadFunctionEnum.OMOMI);

//		List<SanadHesabdariItemEntity> sanadHesabdariItemList = getSanadHesabdariItemList(getSanadItemsXML(), getIsInMultipleLevelMode());
		List<SanadHesabdariItemEntity> sanadHesabdariItemList = initSanadHesabdariItemList(getSanadItemsXML(), getIsInMultipleLevelMode(), getEntity());
		if (getEntity().getSanadHesabdariItem() == null)
			getEntity().setSanadHesabdariItem(new ArrayList<SanadHesabdariItemEntity>());
		else
			getEntity().getSanadHesabdariItem().clear();

		List<SanadHesabdariItemEntity> sortedSanadHesabdariItem = SanadHesabdariUtil.getSortedSanadHesabdariItem(sanadHesabdariItemList);
		getEntity().getSanadHesabdariItem().addAll(sortedSanadHesabdariItem);
		

		getMyService().saveMovaghat(getEntity(), getTemporalZamimeha(),getCurrentOrgan(), getCurrentUserActiveSaalMaali());
		
		addInfoMessage("SUCCESSFUL_SAVE", SerajMessageUtil.getMessage("SanadHesabdari_title"));
		return back();
	}
	
	//just for administrative operations
	public String saveAdmin() {
		
//		List<SanadHesabdariItemEntity> sanadHesabdariItemList = getSanadHesabdariItemList(getSanadItemsXML(), getIsInMultipleLevelMode());
		List<SanadHesabdariItemEntity> sanadHesabdariItemList = initSanadHesabdariItemList(getSanadItemsXML(), getIsInMultipleLevelMode(), getEntity());
		if (getEntity().getSanadHesabdariItem() == null)
			getEntity().setSanadHesabdariItem(new ArrayList<SanadHesabdariItemEntity>());
		else
			getEntity().getSanadHesabdariItem().clear();
		
		List<SanadHesabdariItemEntity> sortedSanadHesabdariItem = SanadHesabdariUtil.getSortedSanadHesabdariItem(sanadHesabdariItemList);
		getEntity().getSanadHesabdariItem().addAll(sortedSanadHesabdariItem);
		
		
		getMyService().saveMovaghat(getEntity(), getTemporalZamimeha(),getCurrentOrgan(), getCurrentUserActiveSaalMaali());
		
		addInfoMessage("SUCCESSFUL_SAVE", SerajMessageUtil.getMessage("SanadHesabdari_title"));
		return getLocalArchiveViewUrl();
	}
	
	
	public String saveEftetahiehMovaghat() {
		if(getEntity().getSanadFunction() == null)
			getEntity().setSanadFunction(SanadFunctionEnum.EFTETAHIE);
		
		checkEftetahiehIsFirstSanad();
		checkEftetahiehUniqueNess();
		saveMovaghat();
		return localEftetahiehView();
	}
	
	public String saveEkhtetamiehMovaghat() {
		if(getEntity().getSanadFunction() == null)
			getEntity().setSanadFunction(SanadFunctionEnum.EKHTETAMIE);
		
		checkEkhtetamiehIsLastSanad();
		checkEkhtetamiehUniqueNess();
		saveMovaghat();
		return localEkhtetamiehView();
	}

	private void checkEftetahiehIsFirstSanad() {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("saalMaali.id@eq", getCurrentUserActiveSaalMaali().getId());
		localFilter.put("sanadFunction@neq", SanadFunctionEnum.EFTETAHIE);
		localFilter.put("organ.id@neq", getCurrentOrgan().getId());
		if(getMyService().getDataList(null, localFilter).size() > 0){
			throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_allready_sanad_created",getSaalMaaliService().load(getEntity().getSaalMaali().getId())));
		}
		
	}

	private void checkEkhtetamiehIsLastSanad() {
	}
	
	private void checkEftetahiehUniqueNess() {
		try{
			Map<String, Object> localFilter = new HashMap<String, Object>();
			localFilter.put("saalMaali.id@eq", getEntity().getSaalMaali().getId());
			localFilter.put("organ.id@eq", getCurrentOrgan().getId());
			checkUniqueNess(getEntity(), SanadHesabdariEntity.PROP_SANAD_FUNCTION, SanadFunctionEnum.EFTETAHIE, localFilter);
		}catch(DuplicateException e){
			throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_eftetahieh_allready_created",getSaalMaaliService().load(getEntity().getSaalMaali().getId())));
		}
	}
	

	private void checkEkhtetamiehUniqueNess() {
		try{
			Map<String, Object> localFilter = new HashMap<String, Object>();
			localFilter.put("saalMaali.id@eq", getEntity().getSaalMaali().getId());
			localFilter.put("organ.id@eq", getCurrentOrgan().getId());
			checkUniqueNess(getEntity(), SanadHesabdariEntity.PROP_SANAD_FUNCTION, SanadFunctionEnum.EKHTETAMIE);
		}catch(DuplicateException e){
			throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_ekhtetamieh_allready_created",getSaalMaaliService().load(getEntity().getSaalMaali().getId())));
		}
	}
	
	

	//converts from daemi to barrasi shode
	public String saveBarresiShode() {

		
//		List<SanadHesabdariItemEntity> sanadHesabdariItemList = getSanadHesabdariItemList(getSanadItemsXML(), getIsInMultipleLevelMode());
		List<SanadHesabdariItemEntity> sanadHesabdariItemList = initSanadHesabdariItemList(getSanadItemsXML(), getIsInMultipleLevelMode(), getEntity());
		if (getEntity().getSanadHesabdariItem() == null)
			getEntity().setSanadHesabdariItem(new ArrayList<SanadHesabdariItemEntity>());
		else
			getEntity().getSanadHesabdariItem().clear();		
		List<SanadHesabdariItemEntity> sortedSanadHesabdariItem = SanadHesabdariUtil.getSortedSanadHesabdariItem(sanadHesabdariItemList);
		getEntity().getSanadHesabdariItem().addAll(sortedSanadHesabdariItem);
		
		boolean validateSaalMaaliInProgress = true;
		if(getEntity().getSanadFunction().equals(SanadFunctionEnum.EKHTETAMIE))
			validateSaalMaaliInProgress=false;
			
		getMyService().saveBarrasiShode(getEntity(),getCurrentOrgan(), getIsInMultipleLevelMode(), validateSaalMaaliInProgress);
		addInfoMessage("SUCCESSFUL_ACTION");
		return getLocalListUrl();
	}
	
	//converts from movaghat to barrasi shode
	public String saveBarresiShodeBatch() {
		
		List<SanadHesabdariEntity> sanadHesabdariList = getSelectedItems();
		
		
		for (SanadHesabdariEntity sanadHesabdariEntity : sanadHesabdariList) {
			
			boolean validateSaalMaaliInProgress = true;
			if(sanadHesabdariEntity.getSanadFunction().equals(SanadFunctionEnum.EKHTETAMIE))
				validateSaalMaaliInProgress=false;
			
			SanadHesabdariEntity e = getMyService().load(sanadHesabdariEntity.getId());
			getMyService().duplicateEntity(e.getOldEntity(), e);
			getMyService().saveBarrasiShode(e,getCurrentOrgan(), getIsInMultipleLevelMode(), validateSaalMaaliInProgress);
		}		
		clearPage();
		addInfoMessage("SUCCESSFUL_ACTION");
		return getLocalListUrl();
	}
	
	public String saveBarresiShodeSimple() {

		
		getMyService().saveBarrasiShode(getEntity(),getCurrentOrgan(), getIsInMultipleLevelMode(), true);
		return getLocalListUrl();
	}

	public String saveEftetahiehBarresiShode() {
		if(getEntity().getSanadFunction() == null)
			getEntity().setSanadFunction(SanadFunctionEnum.EFTETAHIE);

		checkEftetahiehUniqueNess();
		saveBarresiShode();
		return getFacesUrl("/sanadhesabdari/sanadHesabdari.localEftetahiehView.xhtml");
	}
	
	public String saveEkhtetamiehBarresiShode() {
		if(getEntity().getSanadFunction() == null)
			getEntity().setSanadFunction(SanadFunctionEnum.EKHTETAMIE);
		
		checkEkhtetamiehUniqueNess();
		saveBarresiShode();
		return getFacesUrl("/sanadhesabdari/sanadHesabdari.localEkhtetamiehView.xhtml");
	}
	String items;

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

/*	private Map<Long, SanadHesabdariItemVO> convertToSanadItems(String str) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<Long, SanadHesabdariItemVO> map = mapper.readValue(str,
					new TypeReference<Map<Long, SanadHesabdariItemVO>>() {
					});
			return map;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}*/

/*	@Override
	public String edit() {
		super.edit();
		if (getEntity().getId() != null) {
			List<SanadHesabdariItemEntity> sanadItems = getEntity()
					.getSanadHesabdariItem();
			Map<Long, SanadHesabdariItemVO> map = new HashMap<Long, SanadHesabdariItemVO>();
			long i = 1;
			for (SanadHesabdariItemEntity sanadItem : sanadItems) {
				SanadHesabdariItemVO dto = getMyService().convertEntity2DTO(
						sanadItem);
				map.put(i++, dto);
			}
			ObjectMapper mapper = new ObjectMapper();
			StringWriter str = new StringWriter();
			try {
				mapper.writeValue(str, map);
				setItems(str.toString());
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return getEditUrl();
	}*/

	@Override
	public String localEdit() {
		//edit();
		return getLocalEditUrl();
	}

	public String localAdvancedEdit() {
		return getFacesUrl("/sanadhesabdari/sanadHesabdari.localEdit.advanced.xhtml");
	}
	
	public String localEftetahiehEdit() {
		return getFacesUrl("/sanadhesabdari/sanadHesabdari.localEftetahiehEdit.xhtml");
	}
	
	public String localEkhtetamiehEdit() {
		return getFacesUrl("/sanadhesabdari/sanadHesabdari.localEkhtetamiehEdit.xhtml");
	}
	
	public DataModel<SanadHesabdariEntity> getLocalEftetahiehDataModel() {
		
		getFilter().put("state@eq", SanadStateEnum.MOVAGHAT);
		getFilter().put("saalMaali.id@eq", getCurrentUserActiveSaalMaali().getId());
		getFilter().put("sanadFunction@eq", SanadFunctionEnum.EFTETAHIE);
		return getLocalDataModel();
	}
	
	public DataModel<SanadHesabdariEntity> getLocalEkhtetamiehDataModel() {
		
		getFilter().put("state@eq", SanadStateEnum.MOVAGHAT);
		getFilter().put("saalMaali.id@eq", getCurrentUserActiveSaalMaali().getId());
		getFilter().put("sanadFunction@eq", SanadFunctionEnum.EKHTETAMIE);
		return getLocalDataModel();
	}
	
	public DataModel<SanadHesabdariEntity> getLocalMovaghatDataModel() {
		setSearchAction(true);
		setRowsPerPage(-1);
		getFilter().put("state@eq", SanadStateEnum.MOVAGHAT);
		
		getFilter().put("saalMaali.id@eq", getCurrentUserActiveSaalMaali().getId());
		
		return getLocalDataModel();
	}
	
	String sanadRoles;
	public String getSanadRoles() {
		if (sanadRoles == null){
			sanadRoles = getRequest().getParameter("sanadRoles");
		}

		return sanadRoles;
	}

	public void setSanadRoles(String sanadRoles) {
		this.sanadRoles = sanadRoles;
	}


	
	public DataModel<SanadHesabdariEntity> getLocalTempDataModel() {
		
		setRowsPerPage(-1);
		if(StringUtil.hasText(getSanadRoles())) {
			String[] roles = getSanadRoles().split(",");
			getFilter().put("sanadRole@in", Arrays.asList(roles));
		}else
			return getEmptyDataModel();
		getFilter().put("state@eq", SanadStateEnum.TEMP);
		getFilter().put("saalMaali.id@eq", getCurrentUserActiveSaalMaali().getId());
		return getLocalDataModel();
	}
	
	public DataModel<SanadHesabdariEntity> getLocalMergedDataModel() {
		
//		setRowsPerPage(-1);
		if(StringUtil.hasText(getSanadRoles())) {
			String[] roles = getSanadRoles().split(",");
			getFilter().put("sanadRole@in", Arrays.asList(roles));
		}else
			return getEmptyDataModel();		
		getFilter().put("state@eq", SanadStateEnum.MERGED);
		getFilter().put("saalMaali.id@eq",
				getCurrentUserActiveSaalMaali().getId());
		return getLocalDataModel();
	}

	public Integer getLocalMovaghatDataModelCount() {
		try{
			getFilter().put("state@eq", SanadStateEnum.MOVAGHAT);
			getFilter().put("saalMaali.id@eq",
				getCurrentUserActiveSaalMaali().getId());
		}catch (NoSaalMaaliFoundException e) {
			return -1;			
		}catch (NoActiveSaalMaaliFoundException e) {
			return -1;
		}catch (NoOrganFoundException e) {
			return -1;
		}

		return getLocalDataModelCount();
	}

	public DataModel<SanadHesabdariEntity> getLocalBarrasiShodehDataModel() {
		getFilter().put("state@eq", SanadStateEnum.BARRESI_SHODE);
		
		getFilter().put("saalMaali.id@eq",
				getCurrentUserActiveSaalMaali().getId());
		return getLocalDataModel();
	}

	public Integer getLocalBarrasiShodehDataModelCount() {
		try{
			getFilter().put("state@eq", SanadStateEnum.BARRESI_SHODE);
			getFilter().put("saalMaali.id@eq",
					getCurrentUserActiveSaalMaali().getId());
		}catch (NoSaalMaaliFoundException e) {
			return -1;
		}catch (NoActiveSaalMaaliFoundException e) {
			return -1;
		}catch (NoOrganFoundException e) {
			return -1;
		}catch (NullPointerException e) {
			return -1;
		}

		return getLocalDataModelCount();
	}

	public DataModel<SanadHesabdariEntity> getLocalDaemiDataModel() {
		getFilter().put("state@eq", SanadStateEnum.DAEM);
		
		getFilter().put("saalMaali.id@eq",	getCurrentUserActiveSaalMaali().getId());
		return getLocalDataModel();
	}

	public DataModel<SanadHesabdariEntity> getLocalMonthlySummaryDataModel() {
		setRowsPerPage(-1);
		getFilter().put("state@eq", SanadStateEnum.MonthlySummary);
		getFilter().put("saalMaali.id@eq",	getCurrentUserActiveSaalMaali().getId());
		return getLocalDataModel();
	}
	
	@Override
	@PreAuthorize("asdasdhasRole('ROLEE_USER_')")
	public DataModel<SanadHesabdariEntity> getLocalDataModel() {
		getFilter().put("sanadType.id@in", getSanadTypeIds());
		return super.getLocalDataModel();
	}
	
	public Integer getLocalDaemiDataModelCount() {

		try{
			getFilter().put("state@eq", SanadStateEnum.DAEM);
			getFilter().put("saalMaali.id@eq",	getCurrentUserActiveSaalMaali().getId());
		}catch (NoSaalMaaliFoundException e) {
			return -1;			
		}catch (NoActiveSaalMaaliFoundException e) {
			return -1;
		}catch (NoOrganFoundException e) {
			return -1;
		}
		return getLocalDataModelCount();
	}

	public DataModel<SanadHesabdariEntity> getLocalArchiveDataModel() {

		getFilter().put("state@notIn", Arrays.asList(SanadStateEnum.TEMP, SanadStateEnum.MERGED));
		return getLocalDataModel();
	}
	
	public DataModel<SanadHesabdariEntity> getLocalDeletedDataModel() {
		
		getFilter().put("state@eq", SanadStateEnum.EBTAL);
		return getLocalDataModel();
	}
	
	@Override
	public DataModel<SanadHesabdariEntity> getHierarchicalDataModel() {
		getFilter().put("state@notIn", Arrays.asList(SanadStateEnum.TEMP, SanadStateEnum.MERGED));
		getFilter().put("sanadType.id@in", getSanadTypeIds());
		return super.getHierarchicalDataModel();
	}

	@Override
	public List getJsonList(String property, String term,
			boolean all, Map<String, String> params) {
		String isLocalUser = params.get("isCurrentOrgan");
		if (isLocalUser != null && isLocalUser.equals("true")) {
			getFilter().put("organ.id@eq", getCurrentOrgan().getId());
		}

		return super.getJsonList(property, term, all, params);
	}

	public String getSanadItemsXML() {
		return getSanadItemsXML(getEntity().getId());
	}

	public void setSanadItemsXML(String sanadItemsXML) {
		this.sanadItemsXML = sanadItemsXML;
	}

	public String getSanadItemsXML(Long sanadHesabdariId) {
		if (sanadItemsXML == null) {
			sanadItemsXML = "<?xml version='1.0' encoding='UTF-8'?>\n";

			if (sanadHesabdariId == null)
				return "<?xml version='1.0' encoding='UTF-8'?><rows>\n<page>1</page>\n<total>1</total>\n<records>0</records>\n</rows>\n";
			SanadHesabdariEntity sanadHesabdariEntity = getMyService().load(
					sanadHesabdariId);
			sanadItemsXML += "<rows>\n";
			sanadItemsXML += "<page>1</page>\n";
			sanadItemsXML += "<total>1</total>\n";
			sanadItemsXML += "<records>"
					+ sanadHesabdariEntity.getSanadHesabdariItem().size()
					+ "</records>\n";
			Integer index = 1;
			for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariEntity.getSanadHesabdariItem()) {
				sanadItemsXML += "<row id='" + index + "'>";

				SanadHesabdariItemVO sanadHesabdariItemVO = new SanadHesabdariItemVO(
						sanadHesabdariItemEntity);
				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getId() + "</cell>";
				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabKolID()+ "</cell>";
				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabKolName()+ "</cell>";
				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabMoeenID()+ "</cell>";
				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabMoeenName()+ "</cell>";

				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabTafsiliOneID()+ "</cell>";
				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabTafsiliOneName()+ "</cell>";
				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabTafsiliTwoID()+ "</cell>";
				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabTafsiliTwoName()+ "</cell>";
//				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabTafsiliLevelNames()+ "</cell>";
//				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabTafsiliLevels()+ "</cell>";
//				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabTafsiliDescs()+ "</cell>";

				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getAccountingMarkazID()+ "</cell>";
				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getAccountingMarkazName()+ "</cell>";
//				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getAccountingMarkazLevelNames()+ "</cell>";
//				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getAccountingMarkazLevels()+ "</cell>";
//				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getAccountingMarkazDescs()+ "</cell>";
				
				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getMarkazHazineID()+ "</cell>";
				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getMarkazHazineName()+ "</cell>";
						
				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getProjectID()+ "</cell>";
				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getProjectName()+ "</cell>";
						
				sanadItemsXML += "<cell>" +getBigDecimalFormatted(sanadHesabdariItemVO.getBedehkar(), 0)  + "</cell>";
				sanadItemsXML += "<cell>" +getBigDecimalFormatted(sanadHesabdariItemVO.getBestankar(), 0) + "</cell>";
				sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getDescription() + "</cell>";

				String deleteFunction = "deleteRowData(getSanadHesabdariGridId(),"
						+ index + ");";
				String del = "<input style=\'\' type=\'image\' src=\'"
						+ getServletContext().getContextPath()
						+ "/images/remove.png\'  value=\'Delete\' onclick=\'return "
						+ deleteFunction + "\' />";
				sanadItemsXML += "<cell><![CDATA[ " + del + "]]></cell>";

				String editFunction = "editSanadHesabdariRowData(getSanadHesabdariGridId(),"
						+ index + ",getEditDialogTitle());";
				String edit = "<input style=\'\' type=\'image\' src=\'"
						+ getServletContext().getContextPath()
						+ "/images/edit.png\'  value=\'Edit\' onclick=\'return "
						+ editFunction + "\' />";
				sanadItemsXML += "<cell><![CDATA[ " + edit + "]]></cell>";
				sanadItemsXML += "</row>";
				++index;
			}
			sanadItemsXML += "</rows>\n";
		}
		return sanadItemsXML;
	}

	public List<SanadHesabdariItemVO> getSanadHesabdariItemList(
			Long sanadHesabdariId) {
		try {
			ArrayList<SanadHesabdariItemVO> SanadHesabdariItemList = new ArrayList<SanadHesabdariItemVO>();
			if (sanadHesabdariId == null)
				return SanadHesabdariItemList;
			SanadHesabdariEntity sanadHesabdariEntity = getMyService().load(
					sanadHesabdariId);
			Integer index = 0;
			for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariEntity
					.getSanadHesabdariItem()) {
				SanadHesabdariItemVO itemVO = new SanadHesabdariItemVO(
						sanadHesabdariItemEntity);
				++index;
				SanadHesabdariItemList.add(itemVO);
			}
			return SanadHesabdariItemList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FatalException();
		}
	}

	public String getSanadItems() {
		return sanadItems;
	}

	public void setSanadItems(String sanadItems) {
		this.sanadItems = sanadItems;
	}

	public String localAdvancedView() {
		return getFacesUrl("/sanadhesabdari/sanadHesabdari.localAdvancedView.xhtml");
	}
	
	public String localMonthlySummaryView() {
		return getFacesUrl("/sanadhesabdari/sanadHesabdari.summayView.xhtml");
	}
	
	public String localBarrasiShodehView() {
		return getFacesUrl("/sanadhesabdari/sanadHesabdari.localBarrasiShodehView.xhtml");
	}

	public String localBarrasiShodehAdvancedView() {
		return getFacesUrl("/sanadhesabdari/sanadHesabdari.localBarrasiShodehView.advanced.xhtml");
	}
	
	public String localDaemiView() {
		return getFacesUrl("/sanadhesabdari/sanadHesabdari.localDaemiView.xhtml");
	}

	public String localDaemiAdvancedView() {
		return getFacesUrl("/sanadhesabdari/sanadHesabdari.localDaemiView.advanced.xhtml");
	}
	
	public String localEftetahiehView() {
		return getFacesUrl("/sanadhesabdari/sanadHesabdari.localEftetahiehView.xhtml");
	}
	
	public String localEkhtetamiehView() {
		return getFacesUrl("/sanadhesabdari/sanadHesabdari.localEkhtetamiehView.xhtml");
	}
	
	public Boolean getIsMovaghat(){
		if(getEntity().getState() != null && getEntity().getState().equals(SanadStateEnum.MOVAGHAT))
			return true;
		return false;
	}
	
	public Boolean getIsBarrasiShode(){
		if(getEntity().getState() != null && getEntity().getState().equals(SanadStateEnum.BARRESI_SHODE))
			return true;
		return false;
	}
	
	public Boolean getIsDaemi(){
		if(getEntity().getState() != null && getEntity().getState().equals(SanadStateEnum.DAEM))
			return true;
		return false;
	}
	// frayande daemi kardane asnad
	Date tarikhSanadTo;
	Long serialTo;

	public Long getSerialTo() {
		return serialTo;
	}

	public void setSerialTo(Long serialTo) {
		this.serialTo = serialTo;
	}

	public Date getTarikhSanadTo() {
		return tarikhSanadTo;
	}

	public void setTarikhSanadTo(Date tarikhSanadTo) {
		this.tarikhSanadTo = tarikhSanadTo;
	}

	public String getlastDaemiSerial() {
		Long maxDaemSanadSerial = getMyService().getlastDaemiSerial(getCurrentOrgan(),getCurrentUserActiveSaalMaali());
		if (maxDaemSanadSerial == null)
			return "";
		return String.valueOf(maxDaemSanadSerial);
	}

	public String getLastDaemiTarikhSanadFA() {
		Date tarikhSanad = getMyService().getlastDaemiTarikhSanad(getCurrentOrgan(), getCurrentUserActiveSaalMaali());
		if (tarikhSanad == null)
			return "";
		return DateConverter.toShamsiDate(tarikhSanad,
				SerajDateTimePickerType.Date);
	}
	
	public String tabdilBeMovghat() {
		getMyService().tabdilBeMovghat(getEntity());
		addInfoMessage("SUCCESSFUL_ACTION");
		return  back();
	}

	
	public String tabdilBeMovghatBatch() {
		List<SanadHesabdariEntity> selectedItems = getSelectedItems();
		getMyService().tabdilBeMovghat(selectedItems);
		clearPage();
		addInfoMessage("SUCCESSFUL_ACTION");
		return  null;
	}
	
	public String tabdilBeDaemiBatch() {
		List<SanadHesabdariEntity> selectedItems = getSelectedItems();
		SaalMaaliEntity activeSaalmaali = getCurrentUserActiveSaalMaali();
		getMyService().tabdilBeDaemi(selectedItems, activeSaalmaali, getCurrentOrgan());
		addInfoMessage("SUCCESSFUL_ACTION");
		clearPage();
		return  null;
	}
	
	//converts from daemi to barrasi shode
	public String tabdilBeBarrasiShodeBatch() {
		List<SanadHesabdariEntity> selectedItems = getSelectedItems();
		SaalMaaliEntity activeSaalmaali = getCurrentUserActiveSaalMaali();
		getMyService().tabdilBeBarrasiShode(selectedItems, activeSaalmaali);
		addInfoMessage("SUCCESSFUL_ACTION");
		clearPage();
		return  null;		
	}
	
	public String tabdilBeBarrasiShode() {
		getMyService().tabdilBeBarrasiShode(getEntity(), getCurrentOrgan());
		addInfoMessage("SUCCESSFUL_ACTION");
		return  back();
	}

	
	public String tabdilBeDaemi() {
		SaalMaaliEntity activeSaalmaali = getCurrentUserActiveSaalMaali();
		getMyService().tabdilBeDaemi(getEntity(), activeSaalmaali, getCurrentOrgan());
		addInfoMessage("SUCCESSFUL_ACTION");
		return  back();
	}

	public String resetSerialDaemi(){
		getMyService().resetSerialDaemi(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		clearPage();
		addInfoMessage("SUCCESSFUL_ACTION");
		return null;
	}
	
	public String tabdilBeDaemiByTarikh() {
		//getMyService().tabdilBeDaemi(getTarikhSanadTo(),getCurrentOrgan());
		SaalMaaliEntity activeSaalmaali = getCurrentUserActiveSaalMaali();
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("tarikhSanad@le", tarikhSanadTo);
		localFilter.put("saalMaali.id@eq", activeSaalmaali.getId());
		localFilter.put("organ.id@eq", getCurrentOrgan().getId());
		localFilter.put("state@eq", SanadStateEnum.BARRESI_SHODE);
		List<SanadHesabdariEntity> dataList = getMyService().getDataList(null, localFilter, SanadHesabdariEntity.PROP_TARIKH_SANAD, true, false);
		for (SanadHesabdariEntity sanadHesabdariEntity : dataList) {
			System.out.println(sanadHesabdariEntity.getTarikhSanadFA());
			getMyService().duplicateEntity(sanadHesabdariEntity.getOldEntity(), sanadHesabdariEntity);
			getMyService().tabdilBeDaemi(sanadHesabdariEntity, activeSaalmaali, getCurrentOrgan());
		}

//		return dataList.size();
		addInfoMessage("SUCCESSFUL_ACTION");
		return null;
	}

	public String tabdilBeDaemiBySerial() {
		SaalMaaliEntity activeSaalmaali = getCurrentUserActiveSaalMaali();
		getMyService().tabdilBeDaemi(getSerialTo(),activeSaalmaali, getCurrentOrgan());
		addInfoMessage("SUCCESSFUL_ACTION");
		return null;
	}
	
	
	public String deleteBatch() {
		List<SanadHesabdariEntity> sanadHesabdariList = getSelectedItems();
		//getMyService().delete(selectedItems, getCurrentOrgan());
		for (SanadHesabdariEntity sanadHesabdariEntity : sanadHesabdariList) {
			if(sanadHesabdariEntity.getIsSanadHesabdariManualyDeletable())
				getMyService().delete(sanadHesabdariEntity.getId());
			else
				throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_cannotDelete",sanadHesabdariEntity));
		}		
		addInfoMessage("SUCCESSFUL_ACTION");
		clearPage();
		return  null;
	}
	
	

	TempUploadedFileService tempUploadedFileService;
	public TempUploadedFileService getTempUploadedFileService() {
		return tempUploadedFileService;
	}

	public void setTempUploadedFileService(
			TempUploadedFileService tempUploadedFileService) {
		this.tempUploadedFileService = tempUploadedFileService;
	}

	List<TempUploadedFileEntity> temporalZamimeha;
	String zamimehID;
	Long tempFileId;

	public Long getTempFileId() {
		return tempFileId;
	}

	public void setTempFileId(Long tempFileId) {
		this.tempFileId = tempFileId;
	}

	public String getZamimehID() {
		return zamimehID;
	}

	public void setZamimehID(String zamimehID) {
		this.zamimehID = zamimehID;
	}

	public List<TempUploadedFileEntity> getTemporalZamimeha() {
		if (temporalZamimeha == null) {
			temporalZamimeha = new ArrayList<TempUploadedFileEntity>();
			TempUploadedFileEntity localExample = new TempUploadedFileEntity();
			localExample.setGuid(getUniqueGUID());
			temporalZamimeha = getTempUploadedFileService().getDataList(localExample, null);
		}
		return temporalZamimeha;
	}


	public void setTemporalZamimeha(
			List<TempUploadedFileEntity> temporalZamimeha) {
		this.temporalZamimeha = temporalZamimeha;
	}
	
	public void deleteTempZamimeh() {
		getMyService().cleanNullRelations(getEntity());
		getTempUploadedFileService().delete(getTempFileId());
		setTemporalZamimeha(null);
	}

	public String deleteZamimeh() {
		getMyService().cleanNullRelations(getEntity());
		FileEntity fileEntity = getFileService().load(getZamimehID());
		getEntity().getZamimeh().remove(fileEntity);
		getEntity().setZamimehList(null);
		getMyService().save(getEntity(), getCurrentOrgan());
		return null;
	}
	
//	public void uploadlistener(FileUploadEvent event) {
//		UploadedFile item = event.getUploadedFile();
//		String contentType = item.getContentType();
//		
//		String separator = System.getProperty("file.separator");
//		
//		String editedFileName = item.getName().substring(item.getName().lastIndexOf(separator)+1);
//		createTempUploadFile(editedFileName, item.getData(), contentType, "zamimeh",
//				getUniqueGUID());
//	}
	
	
//	private TempUploadedFileEntity createTempUploadFile(String fileName,
//			byte[] data, String contentType, String useCase, String guid) {
//		getMyService().cleanNullRelations(getEntity());
//		return getMyService().createTempUploadFile(fileName, data, contentType,
//				getSession().getId(), useCase, guid);
//	}
	
	public Boolean getHasZamimeh() {
		if (getEntity().getZamimeh() != null
				&& getEntity().getZamimeh().size() > 0)
			return true;
		return false;
	}


	
	public void logPrintAction(SanadHesabdariEntity sanadHesabdariEntity) {
		String message = SerajMessageUtil
				.getMessage(getEntityName() + "_title");
		try {
			
				ActionLogUtil.logActionStateLess(SerajMessageUtil.getMessage("PRINT_SANAD"), message,
						sanadHesabdariEntity.getDesc(), null,"");
			} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public String deleteTemporalSanadHesabdari(){
		getMyService().deleteTemporalSanadHesabdari(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		addInfoMessage("SUCCESSFUL_ACTION");
		return null;
	}
	
	public String deleteSanadEftetahieh(){
		getMyService().deleteSanadEftetahieh(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		addInfoMessage("SUCCESSFUL_ACTION");
		return null;
	}
	
	public String deleteSanadEkhtetamieh(){
		getMyService().deleteSanadEkhtetamieh(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		addInfoMessage("SUCCESSFUL_ACTION");
		return null;
	}

	public String deleteMonthlySummarySanad(){
		getMyService().deleteMonthlySummarySanad(getEntity());
		clearPage();
		addInfoMessage("SUCCESSFUL_ACTION");
		return null;
	}
	
	public void closeTemporalAccounts(){
		if(getTarikhSanad() == null)
			setTarikhSanad(getCurrentUserActiveSaalMaali().getEndDate());
		getMyService().closeTemporalAccounts(getCurrentOrgan(), getTarikhSanad(), getIsInMultipleLevelMode());
		addInfoMessage("SUCCESSFUL_ACTION");
	}
	
	Date tarikhSanad;

	public Date getTarikhSanad() {
		return tarikhSanad;
	}

	public void setTarikhSanad(Date tarikhSanad) {
		this.tarikhSanad = tarikhSanad;
	}

	public void createSanadEkhtetamieh(){
		getMyService().createSanadEkhtetamieh(getCurrentOrgan(), getTarikhSanad(), getIsInMultipleLevelMode());
		addInfoMessage("SUCCESSFUL_SAVE", SerajMessageUtil.getMessage("SanadHesabdari_title"));
	}

	public String editSanadEkhtetamieh(){
		SanadHesabdariEntity sanadEkhtetamieh = getMyService().getSanadEkhtetamieh(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		//setEntity(sanadEkhtetamieh);
		setID(sanadEkhtetamieh.getId());
		return getFacesUrl("/sanadhesabdari/sanadHesabdari.localEkhtetamiehEdit.xhtml");
	}
	
	public String viewSanadEkhtetamieh(){
		SanadHesabdariEntity sanadEkhtetamieh = getMyService().getSanadEkhtetamieh(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		//setEntity(sanadEkhtetamieh);
		setID(sanadEkhtetamieh.getId());
		return getFacesUrl("/sanadhesabdari/sanadHesabdari.localEkhtetamiehView.xhtml");
	}
	
//	public void deleteSanadEkhtetamieh(){
//		getMyService().deleteSanadEkhtetamieh(getCurrentUsertActiveSaalMaali(), getCurrentOrgan());
//		
//	}
	

	
	public void createSanadEftetahieh(){
		getMyService().createSanadEftetahieh(getCurrentOrgan(), getTarikhSanad(), getIsInMultipleLevelMode());
		addInfoMessage("SUCCESSFUL_SAVE", SerajMessageUtil.getMessage("SanadHesabdari_title"));
	}
	
	public String editSanadEftetahieh(){
		SanadHesabdariEntity sanadEftetahieh = getMyService().getSanadEftetahieh(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		setID(sanadEftetahieh.getId());
		return getFacesUrl("/sanadhesabdari/sanadHesabdari.localEftetahiehEdit.xhtml");
	}
	
	public String viewSanadEftetahieh(){
		SanadHesabdariEntity sanadEftetahieh = getMyService().getSanadEftetahieh(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		setID(sanadEftetahieh.getId());
		return getFacesUrl("/sanadhesabdari/sanadHesabdari.localEftetahiehView.xhtml");
	}
	
//	public void deleteSanadEftetahieh(){
//		getMyService().deleteSanadEftetahieh(getCurrentUsertActiveSaalMaali(), getCurrentOrgan());
//		
//	}

	public Boolean getHasSanadEftetahieh(){
		SanadHesabdariEntity sanadEftetahieh = getMyService().getSanadEftetahieh(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		return sanadEftetahieh != null;
	}
	
	public Boolean getHasSanadEftetahiehMovaghat(){
		SanadHesabdariEntity sanadEftetahieh = getMyService().getSanadEftetahieh(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		return sanadEftetahieh != null && sanadEftetahieh.getState().equals(SanadStateEnum.MOVAGHAT);
	}
	
	public Boolean getHasSanadEftetahiehBarresiShode(){
		SanadHesabdariEntity sanadEftetahieh = getMyService().getSanadEftetahieh(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		return sanadEftetahieh != null && sanadEftetahieh.getState().equals(SanadStateEnum.BARRESI_SHODE);
	}
	
	public Boolean getHasSanadEftetahiehDaemi(){
		SanadHesabdariEntity sanadEftetahieh = getMyService().getSanadEftetahieh(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		return sanadEftetahieh != null && sanadEftetahieh.getState().equals(SanadStateEnum.DAEM);
	}

	public Boolean getHasSanadEkhtetamieh(){
		SanadHesabdariEntity sanadEkhtetamieh = getMyService().getSanadEkhtetamieh(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		return sanadEkhtetamieh != null;
	}
	
	public Boolean getHasSanadEkhtetamiehMovaghat(){
		SanadHesabdariEntity sanadEkhtetamieh = getMyService().getSanadEkhtetamieh(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		return sanadEkhtetamieh != null && sanadEkhtetamieh.getState().equals(SanadStateEnum.MOVAGHAT);
	}
	
	public Boolean getHasSanadEkhtetamiehBarresiShode(){
		SanadHesabdariEntity sanadEkhtetamieh = getMyService().getSanadEkhtetamieh(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		return sanadEkhtetamieh != null && sanadEkhtetamieh.getState().equals(SanadStateEnum.BARRESI_SHODE);
	}
	
	public Boolean getHasSanadEkhtetamiehDaemi(){
		SanadHesabdariEntity sanadEkhtetamieh = getMyService().getSanadEkhtetamieh(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		return sanadEkhtetamieh != null && sanadEkhtetamieh.getState().equals(SanadStateEnum.DAEM);
	}

	public Boolean getEntityEditable() {
		if(getEntity().getId() == null || getEntity().getSanadType().getId().longValue() == getMyService().getDefaultSanadType(getCurrentOrgan()).getId().longValue())
			return super.getEditable();
		else
			return super.getEditable() && SecurityUtil.hasPermission("sanadHesabdari.automaticSanadEdit");
	}
	
	private SanadTypeService sanadTypeService;
	
	public SanadTypeService getSanadTypeService() {
		return sanadTypeService;
	}

	public void setSanadTypeService(SanadTypeService sanadTypeService) {
		this.sanadTypeService = sanadTypeService;
	}
	
	public Boolean getIsSanadDeletable(){
		return getEntity().getDeletable().equals(YesNoEnum.YES);
	}
	
//	public void importSanadHesabdariFromCSVFileV1(){
//        String csvFile = "/Users/mkyong/csv/country.csv";
//        BufferedReader br = null;
//        String line = "";
//        String cvsSplitBy = ",";
//        try {
//
//            br = new BufferedReader(new FileReader(csvFile));
//            while ((line = br.readLine()) != null) {
//
//                // use comma as separator
//                String[] country = line.split(cvsSplitBy);
//
//                System.out.println("Country [code= " + country[4] + " , name=" + country[5] + "]");
//
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//	}
	
	public static void main(String[] args) {
//		importSanadHesabdariFromCSVFileV2();
	}
	

	
	public void importSanadHesabdariFromCSVFileBatch(){
		URL resource = getClass().getResource("/sanadImport");
		if (resource == null)
			return;
		File dir = new File(resource.getFile());

		FilenameFilter filter = new WildcardFileFilter("sanad*.csv");
		String[] list = dir.list(filter);

		for (String fileName : list) {
			String csvFile = dir.getAbsolutePath() + "/" + fileName;
			importSanadHesabdariFromCSVFileV2(csvFile);
		}
		
		clearPage();
	}
	public void importSanadHesabdariFromCSVFileV2(String csvFile){
		System.out.println("importing : "+csvFile);
//		String csvFile = getClass().getResource("/sanadImport/sanad1.csv").getFile();
//        String csvFile = "E:\\temp\\sanad1.csv";

//        CSVReader reader = null;
        try {
//        	BufferedReader in = new BufferedReader(
//        	           new InputStreamReader(
//        	                      new FileInputStream(csvFile), "UTF-8"));
//            reader = new CSVReader(in,';');
            
            CSVReader reader=new CSVReader(new InputStreamReader(new FileInputStream(csvFile), "UTF-8"),',', '\'', 1);
            	    
            String[] line;
            Long sanadHesabdariSerial = null;
            
            List<SanadHesabdariItemEntity> sanadHesabdariItemList = new ArrayList<SanadHesabdariItemEntity>();
            while ((line = reader.readNext()) != null) {
            	int radif = new Integer(removeUTF8BOM(line[0]));
            	int atf = new Integer(line[1]);
            	sanadHesabdariSerial = new Long(line[2]);
            	String moeenCode = line[3];
            	Long tafsilCode = null;
            	
            	if(StringUtil.hasText(line[4]))
            		tafsilCode = new Long(line[4]);
            	
            	String markazCode = line[5];
            	String hesabDescription = line[6];
            	String hesabName= line[7];
            	String tarikh = line[8];
            	String articleDescription = line[9];
            	Double bedehkar = StringUtil.hasText(line[10]) ? new Double(line[10].replace(",", "").replace("\"", "")) : 0d;
            	Double bestankar = StringUtil.hasText(line[11]) ? new Double(line[11].replace(",", "").replace("\"", "")) : 0d;
//                System.out.println("Country [id= " + line[0] + ", code= " + line[1] + " , name=" + line[2] + "]");
            	
            	SanadHesabdariItemEntity sanadHesabdariItemEntity = new SanadHesabdariItemEntity();
            	HesabMoeenEntity hesabMoeenEntity = getHesabMoeenService().loadHesabMoeenByCode(moeenCode, getCurrentUserActiveSaalMaali());
            	if(hesabMoeenEntity == null)
            		throw new FatalException("moeenCode not found : "+moeenCode);
				sanadHesabdariItemEntity.setHesabMoeen(hesabMoeenEntity);
				sanadHesabdariItemEntity.setHesabKol(hesabMoeenEntity.getHesabKol());
            	if(tafsilCode != null)
            		sanadHesabdariItemEntity.setHesabTafsili(getHesabTafsiliService().loadHesabTafsiliByCode(tafsilCode, getCurrentUserActiveSaalMaali()));
            	if(StringUtil.hasText(markazCode))
            		sanadHesabdariItemEntity.setAccountingMarkaz(getAccountingMarkazService().loadAccountingMarkazByCode(markazCode, getCurrentUserActiveSaalMaali()));
            	sanadHesabdariItemEntity.setDescription(articleDescription);
            	sanadHesabdariItemEntity.setBedehkar(bedehkar);
            	sanadHesabdariItemEntity.setBestankar(bestankar);
            	
            	sanadHesabdariItemList.add(sanadHesabdariItemEntity);
            }
  
            
        	SanadHesabdariEntity sanadHesabdariEntity = getMyService().loadBySerial(sanadHesabdariSerial, getCurrentOrgan(), getCurrentUserActiveSaalMaali());
        	if(sanadHesabdariEntity == null){
        		sanadHesabdariEntity = new SanadHesabdariEntity();
        		sanadHesabdariEntity.setSaalMaali(getCurrentUserActiveSaalMaali());
        		sanadHesabdariEntity.setSerial(sanadHesabdariSerial);
        		sanadHesabdariEntity.setTarikhSanad(getCurrentUserActiveSaalMaali().getStartDate());
        		if (sanadHesabdariEntity.getSanadHesabdariItem() == null)
        			sanadHesabdariEntity.setSanadHesabdariItem(new ArrayList<SanadHesabdariItemEntity>());
        		else
        			sanadHesabdariEntity.getSanadHesabdariItem().clear();

        		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItemList) {
        			sanadHesabdariItemEntity.setSanadHesabdari(sanadHesabdariEntity);
        		}

        		List<SanadHesabdariItemEntity> sortedSanadHesabdariItem = SanadHesabdariUtil.getSortedSanadHesabdariItem(sanadHesabdariItemList);
        		sanadHesabdariEntity.getSanadHesabdariItem().addAll(sortedSanadHesabdariItem);

        		
        		getMyService().saveMovaghat(sanadHesabdariEntity, getTemporalZamimeha(),getCurrentOrgan(), getCurrentUserActiveSaalMaali());
        	}

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        }
	}
	
	public void createMonthlySummarySanad(){
		getMyService().createMonthlySummarySanad(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		addInfoMessage("SUCCESSFUL_ACTION");
	}
	
	public String backToMovaghatList() {
		return getFacesUrl("/sanadhesabdari/sanadHesabdari.localList.xhtml");
	}

}
