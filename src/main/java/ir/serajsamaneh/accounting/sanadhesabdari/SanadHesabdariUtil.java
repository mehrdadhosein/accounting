package ir.serajsamaneh.accounting.sanadhesabdari;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.util.StringUtils;

import com.itextpdf.text.pdf.PdfWriter;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity;
import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazService;
import ir.serajsamaneh.accounting.accountingmarkaztemplate.AccountingMarkazTemplateEntity;
import ir.serajsamaneh.accounting.enumeration.SanadStateEnum;
import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.hesabkol.HesabKolService;
import ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliService;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity;
import ir.serajsamaneh.accounting.sanadtype.SanadTypeEntity;
import ir.serajsamaneh.accounting.sanadtype.SanadTypeService;
import ir.serajsamaneh.core.exception.DuplicateException;
import ir.serajsamaneh.core.systemconfig.SystemConfigService;
import ir.serajsamaneh.core.user.UserEntity;
import ir.serajsamaneh.core.util.ActionLogUtil;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.core.util.SpringUtils;
import ir.serajsamaneh.enumeration.YesNoEnum;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import serajcomponent.DateConverter;

public class SanadHesabdariUtil {


	static String TEMP_SANAD_TYPE="TEMP_SANAD";
	static String MERGED_SANAD_TYPE="MERGED_SANAD_TYPE";

	static SanadHesabdariService sanadHesabdariService;
	static SanadTypeService sanadTypeService;
	static HesabTafsiliService hesabTafsiliService;
	static SaalMaaliService saalMaaliService;
	static HesabMoeenService hesabMoeenService;
	static HesabKolService hesabKolService;
	static AccountingMarkazService accountingMarkazService;
	static SystemConfigService systemConfigService;
	
	public static SystemConfigService getSystemConfigService() {
		if(systemConfigService == null)
			systemConfigService = SpringUtils.getBean("systemConfigService");		
		return systemConfigService;
	}


	public static AccountingMarkazService getAccountingMarkazService() {
		if(accountingMarkazService == null)
			accountingMarkazService = SpringUtils.getBean("accountingMarkazService");		
		return accountingMarkazService;
	}


	public static HesabMoeenService getHesabMoeenService() {
		if(hesabMoeenService == null)
			hesabMoeenService = SpringUtils.getBean("hesabMoeenService");
		return hesabMoeenService;
	}


	public static HesabKolService getHesabKolService() {
		if(hesabKolService == null)
			hesabKolService = SpringUtils.getBean("hesabKolService");
		return hesabKolService;
	}




	public static SaalMaaliService getSaalMaaliService() {
		if(saalMaaliService == null)
			saalMaaliService = SpringUtils.getBean("saalMaaliService");			
		return saalMaaliService;
	}


	public static HesabTafsiliService getHesabTafsiliService() {
		if(hesabTafsiliService == null)
			hesabTafsiliService = SpringUtils.getBean("hesabTafsiliService");			
		return hesabTafsiliService;
	}
	
	public static SanadTypeService getSanadTypeService() {
		if(sanadTypeService == null)
			sanadTypeService = SpringUtils.getBean("sanadTypeService");		
		return sanadTypeService;
	}

	public static SanadHesabdariService getSanadHesabdariService() {
		if(sanadHesabdariService == null)
			sanadHesabdariService = SpringUtils.getBean("sanadHesabdariService");
		return sanadHesabdariService;
	}
	
	public static SanadHesabdariEntity createMergedSanadHesabdari(Long organId,
			Date sanadHesabdariDate, List<SanadHesabdariItemEntity> articles, String description, SanadTypeEntity sanadType, boolean concatDescriptions, SanadStateEnum sanadStateEnum, SaalMaaliEntity saalMaali, YesNoEnum deletable, int numberOfDecimals, UserEntity currentUser, String organDesc) {
		return createMergedSanadHesabdari(organId, sanadHesabdariDate, articles, description, sanadType, concatDescriptions, sanadStateEnum, true, saalMaali, deletable, numberOfDecimals, currentUser, organDesc);
	}
	
	public static SanadHesabdariEntity createMergedEkhtetamiehSanadHesabdari(Long organId,
			Date sanadHesabdariDate, List<SanadHesabdariItemEntity> articles, String description, SanadTypeEntity sanadType, boolean concatDescriptions, SanadStateEnum sanadStateEnum, boolean validateSaalMaaliInProgress, SaalMaaliEntity currentUserActiveSaalMaali, YesNoEnum deletable, int numberOfDecimals, UserEntity currentUser, String organDesc) {
		
		List<SanadHesabdariItemEntity> mergedArticles = createMergedArticles(articles,	concatDescriptions, organId);
//		SaalMaaliEntity saalMaaliEntity = getSaalMaaliService().getSaalmaaliByDate(sanadHesabdariDate, organEntity);
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : mergedArticles) {
			sanadHesabdariItemEntity.setDescription(SerajMessageUtil.getMessage("SanadHesabdari_createSanadEkhtetamieh", currentUserActiveSaalMaali.getDesc()));
		}
		return createSanadHesabdari(organId, sanadHesabdariDate, mergedArticles, description, sanadType, sanadStateEnum, validateSaalMaaliInProgress, null, currentUserActiveSaalMaali, deletable, numberOfDecimals, currentUser, organDesc);
	}
	
	public static SanadHesabdariEntity createMergedSanadHesabdari(Long organId,
			Date sanadHesabdariDate, List<SanadHesabdariItemEntity> articles, String description, SanadTypeEntity sanadType, boolean concatDescriptions, SanadStateEnum sanadStateEnum, boolean validateSaalMaaliInProgress, SaalMaaliEntity saalMaali, YesNoEnum deletable, int numberOfDecimals, UserEntity currentUser, String organDesc) {
		
		List<SanadHesabdariItemEntity> mergedArticles = createMergedArticles(articles,	concatDescriptions, organId);
		
		return createSanadHesabdari(organId, sanadHesabdariDate, mergedArticles, description, sanadType, sanadStateEnum, validateSaalMaaliInProgress, null, saalMaali, deletable, numberOfDecimals, currentUser, organDesc);
	}

	public static List<SanadHesabdariItemEntity> createMergedArticles(List<SanadHesabdariItemEntity> articles, boolean concatDescriptions, Long organId) {
		
		Map<String, SanadHesabdariItemEntity> articlesMap = mergeArtcilesToArticleMap(articles, concatDescriptions, organId);
		
		List<SanadHesabdariItemEntity> mergedArticles = extractSanadArticles(articlesMap);
		return mergedArticles;
	}
	
	public static List<SanadHesabdariItemEntity> createMergedArticlesKeepingBedehkarBestankar(List<SanadHesabdariItemEntity> articles, boolean concatDescriptions, Long organId) {
		
		Map<String, SanadHesabdariItemEntity> articlesMap = mergeArtcilesKeepingBedehkarBestankarToArticleMap(articles, concatDescriptions, organId);
		
		List<SanadHesabdariItemEntity> mergedArticles = extractSanadArticles(articlesMap);
		return mergedArticles;
	}
	
	
//	public static SanadHesabdariEntity createSanadHesabdari(OrganEntity organEntity,
//			Date sanadHesabdariDate,
//			List<SanadHesabdariItemEntity> articles, String description, SanadStateEnum sanadStateEnum) {
//		return createSanadHesabdari(organEntity, sanadHesabdariDate, articles, description, sanadStateEnum, true);
//	}

//	public static SanadHesabdariEntity createSanadHesabdari(OrganEntity organEntity,
//			Date sanadHesabdariDate,
//			List<SanadHesabdariItemEntity> articles, String description, SanadStateEnum sanadStateEnum, boolean validateSaalMaaliInProgress) {
//		return createSanadHesabdari(organEntity, sanadHesabdariDate, articles, description, null, sanadStateEnum, validateSaalMaaliInProgress, null);
//	}
	

	
	public static SanadHesabdariEntity createSanadHesabdari(Long organId,
			Date sanadHesabdariDate,
			List<SanadHesabdariItemEntity> articles, String description, SanadTypeEntity sanadType, SanadStateEnum sanadStateEnum, SaalMaaliEntity saalMaaliEntity, YesNoEnum deletable, int numberOfDecimals, UserEntity currentUser, String organDesc) {
		return createSanadHesabdari(organId, sanadHesabdariDate, articles, description, sanadType, sanadStateEnum, true, null, saalMaaliEntity, deletable, numberOfDecimals, currentUser, organDesc);
	}


	public static SanadHesabdariEntity createSanadHesabdari(Long organId, Date sanadHesabdariDate,
			List<SanadHesabdariItemEntity> articles, String description, SanadTypeEntity sanadType,
			SanadStateEnum sanadStateEnum, boolean validateSaalMaaliInProgress, String sanadRole,
			SaalMaaliEntity saalMaaliEntity, YesNoEnum deletable, int numberOfDecimals, UserEntity currentUser, String organDesc) {
		SanadHesabdariEntity totalSanadHesabdariEntity = new SanadHesabdariEntity();
		totalSanadHesabdariEntity.setSanadHesabdariItem(new ArrayList<SanadHesabdariItemEntity>());
		totalSanadHesabdariEntity.setTarikhSanad(sanadHesabdariDate);
		
		List<SanadHesabdariItemEntity> sortedSanadHesabdariItem = getSortedSanadHesabdariItem(articles);
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sortedSanadHesabdariItem) {
			sanadHesabdariItemEntity.setSanadHesabdari(totalSanadHesabdariEntity);
		}
		
		totalSanadHesabdariEntity.getSanadHesabdariItem().addAll(sortedSanadHesabdariItem);
		totalSanadHesabdariEntity.setDescription(description);
		totalSanadHesabdariEntity.setSanadType(sanadType);
		totalSanadHesabdariEntity.setSanadRole(sanadRole);
		totalSanadHesabdariEntity.setDeletable(deletable);
		
		
		if(sanadStateEnum == null)
			getSanadHesabdariService().saveMovaghat(totalSanadHesabdariEntity, null, organId, saalMaaliEntity, validateSaalMaaliInProgress, currentUser, organDesc);
		else if(sanadStateEnum.equals(SanadStateEnum.TEMP))
			getSanadHesabdariService().saveTemp(totalSanadHesabdariEntity, null, organId, saalMaaliEntity, validateSaalMaaliInProgress, currentUser, organDesc);
		else if(sanadStateEnum.equals(SanadStateEnum.MOVAGHAT))
			getSanadHesabdariService().saveMovaghat(totalSanadHesabdariEntity, null, organId, saalMaaliEntity, validateSaalMaaliInProgress, currentUser, organDesc);
		else if(sanadStateEnum.equals(SanadStateEnum.BARRESI_SHODE))
			getSanadHesabdariService().saveBarrasiShode(totalSanadHesabdariEntity, organId, true, validateSaalMaaliInProgress, numberOfDecimals, currentUser, organDesc);
		return totalSanadHesabdariEntity;
	}
	
	protected static List<SanadHesabdariItemEntity> extractSanadArticles(
			Map<String, SanadHesabdariItemEntity> articlesMap) {
		List<SanadHesabdariItemEntity> sanadHesabdariItemList = new ArrayList<SanadHesabdariItemEntity>();
		for (Map.Entry<String, SanadHesabdariItemEntity> entry : articlesMap.entrySet()) {
			SanadHesabdariItemEntity sanadHesabdariItemEntity = entry.getValue();
			sanadHesabdariItemList.add(sanadHesabdariItemEntity);
		}
		return sanadHesabdariItemList;
	}
	
	protected static Map<String, SanadHesabdariItemEntity> mergeArtcilesToArticleMap(List<SanadHesabdariItemEntity> articles, boolean concatDescriptions, Long organId) {
		
		Map<String, SanadHesabdariItemEntity> articlesMap = new HashMap<String, SanadHesabdariItemEntity>();
		
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : articles) {
			String mapKey = createMapKey(sanadHesabdariItemEntity, organId);
		
			
			mergeArtcilesToArticleMap(concatDescriptions, articlesMap, sanadHesabdariItemEntity, mapKey);
			
		}
		
		return articlesMap;
	}

	
	protected static Map<String, SanadHesabdariItemEntity> mergeArtcilesKeepingBedehkarBestankarToArticleMap(List<SanadHesabdariItemEntity> articles, boolean concatDescriptions, Long organId) {
		
		Map<String, SanadHesabdariItemEntity> articlesMap = new HashMap<String, SanadHesabdariItemEntity>();
		
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : articles) {
			String mapKey = createMapKey(sanadHesabdariItemEntity, organId);
			if(sanadHesabdariItemEntity.getBedehkar() > 0)
				mapKey = mapKey + "_bed";
			else if(sanadHesabdariItemEntity.getBestankar() > 0)
				mapKey = mapKey + "_bes";
			
			mergeArtcilesToArticleMap(concatDescriptions, articlesMap, sanadHesabdariItemEntity, mapKey);
			
		}
		
		return articlesMap;
	}
	
	private static void mergeArtcilesToArticleMap(boolean concatDescriptions, Map<String, SanadHesabdariItemEntity> articlesMap,
			SanadHesabdariItemEntity sanadHesabdariItemEntity, String mapKey) {
		SanadHesabdariItemEntity previousSanad = articlesMap.get(mapKey);
		if(previousSanad == null){

			
			double bedehkar = sanadHesabdariItemEntity.getBedehkar();
			double bestankar = sanadHesabdariItemEntity.getBestankar();
			Double mandeh = bestankar - bedehkar;
			if(mandeh > 0){
				sanadHesabdariItemEntity.setBestankar(Math.abs(mandeh));
				sanadHesabdariItemEntity.setBedehkar(0d);
			}else if(mandeh < 0){
				sanadHesabdariItemEntity.setBestankar(0d);
				sanadHesabdariItemEntity.setBedehkar(Math.abs(mandeh));
			}else{
				sanadHesabdariItemEntity.setBestankar(0d);
				sanadHesabdariItemEntity.setBedehkar(0d);
			}
			
			if(!StringUtils.hasText(sanadHesabdariItemEntity.getDescription()))
				sanadHesabdariItemEntity.setDescription(" ");
			else if(!concatDescriptions)
				sanadHesabdariItemEntity.setDescription(" ");
			
			articlesMap.put(mapKey, sanadHesabdariItemEntity);
		}else{
			double bedehkar = previousSanad.getBedehkar() + sanadHesabdariItemEntity.getBedehkar();
			double bestankar = previousSanad.getBestankar() + sanadHesabdariItemEntity.getBestankar();
			Double mandeh = bestankar - bedehkar;
			
			if(mandeh > 0){
				previousSanad.setBestankar(Math.abs(mandeh));
				previousSanad.setBedehkar(0d);
			}else if(mandeh < 0){
				previousSanad.setBestankar(0d);
				previousSanad.setBedehkar(Math.abs(mandeh));
			}else{
				previousSanad.setBestankar(0d);
				previousSanad.setBedehkar(0d);
			}
			if(StringUtils.hasText(sanadHesabdariItemEntity.getDescription()) && concatDescriptions)
				previousSanad.setDescription(previousSanad.getDescription()+","+sanadHesabdariItemEntity.getDescription());
			
		}
	}


//	public static  List<Integer> getLevels(OrganEntity currentOrgan){
//		String maxSanadHesabdariTafsilLevel = systemConfigService.getValue(currentOrgan.getId(), null, "maxSanadHesabdariTafsilLevel");
//		ArrayList<Integer> levelList = new ArrayList<Integer>();
//		if(maxSanadHesabdariTafsilLevel!=null){
//			Integer maxLevels = new Integer(maxSanadHesabdariTafsilLevel);
//			for(int i=1; i<=maxLevels; ++i)
//				levelList.add(i);
//			return levelList;
//		}
//		
//		return levelList;
//	}
	
	public static String createMapKey(
			SanadHesabdariItemEntity sanadHesabdariItemEntity, Long organId) {
		String mapKey ="";
		
		String kolCode = sanadHesabdariItemEntity.getHesabKol().getCode();
		mapKey+="_"+kolCode;
		
		if(sanadHesabdariItemEntity.getHesabMoeen() != null){
			String moeenCode = sanadHesabdariItemEntity.getHesabMoeen().getCode();
			mapKey+="_"+moeenCode;
		}
		
		String tafsiliCode = sanadHesabdariItemEntity.getHesabTafsili()!=null ? sanadHesabdariItemEntity.getHesabTafsili().getCode().toString() : "";
		mapKey+="_"+tafsiliCode;
		
		String tafsiliTwoCode = sanadHesabdariItemEntity.getHesabTafsiliTwo()!=null ? sanadHesabdariItemEntity.getHesabTafsiliTwo().getCode().toString() : "";
		mapKey+="_"+tafsiliTwoCode;
		
//		for(Integer level : getLevels(currentOrgan)) {
//			ArticleTafsiliEntity articleTafsiliEntity = sanadHesabdariItemEntity.getArticleTafsiliByLevel(level);
//			mapKey+="_"+(articleTafsiliEntity!=null ? articleTafsiliEntity.getHesabTafsili().getCode() : "");
//		}
		
		String markazCode = sanadHesabdariItemEntity.getAccountingMarkaz() != null ? sanadHesabdariItemEntity.getAccountingMarkaz().getCode() :"";
		mapKey+="_"+markazCode;
		
//		for(Integer level : getLevels(currentOrgan)) {
//			ArticleAccountingMarkazEntity articleAccountingMarkazEntity = sanadHesabdariItemEntity.getArticleAccountingMarkaz(level);
//			mapKey+="_"+(articleAccountingMarkazEntity !=null ? articleAccountingMarkazEntity.getAccountingMarkaz().getCode() : "");
//		}
		return mapKey;
	}	

	public static List<SanadHesabdariItemEntity> getSortedSanadHesabdariItem(List<SanadHesabdariItemEntity> sanadHesabdariItemList) {
		List<SanadHesabdariItemEntity> result = new ArrayList<SanadHesabdariItemEntity>();
		List<SanadHesabdariItemEntity> listBedehkar = getSortedBedehkarSanadHesabdariItem(sanadHesabdariItemList);
		List<SanadHesabdariItemEntity> listBestankar = getSortedBestankarSanadHesabdariItem(sanadHesabdariItemList);
		
		result.addAll(listBedehkar);
		result.addAll(listBestankar);
		return result;
		
	}
	public static List<SanadHesabdariItemEntity> getSortedBedehkarSanadHesabdariItem(List<SanadHesabdariItemEntity> sanadHesabdariItemList) {
		List<SanadHesabdariItemEntity> result = new ArrayList<SanadHesabdariItemEntity>();
		for(SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItemList){
			if(sanadHesabdariItemEntity.getBedehkar() == 0)
				continue;
			if(result.isEmpty()){
				result.add(sanadHesabdariItemEntity);
				continue;
			}
			int index=0;
			for (; index<result.size(); index++) {
				if(new Long(sanadHesabdariItemEntity.getHesabKol().getCode()) < new Long(result.get(index).getHesabKol().getCode()))
					break;
			}
			result.add(index, sanadHesabdariItemEntity);
		}
		
		return result;
	}
	
	public static List<SanadHesabdariItemEntity> getSortedBestankarSanadHesabdariItem(List<SanadHesabdariItemEntity> sanadHesabdariItemList) {
		List<SanadHesabdariItemEntity> result = new ArrayList<SanadHesabdariItemEntity>();
		for(SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItemList){
			if(sanadHesabdariItemEntity.getBestankar() == 0)
				continue;
			if(result.isEmpty()){
				result.add(sanadHesabdariItemEntity);
				continue;
			}
			int index=0;
			for (; index<result.size(); index++) {
				if(new Long(sanadHesabdariItemEntity.getHesabKol().getCode()) < new Long(result.get(index).getHesabKol().getCode()))
					break;
			}
			result.add(index, sanadHesabdariItemEntity);
		}
		
		return result;
	}

	public static void checkSanadHesabdariUniqueness(Date sanadHesabDariDate, Long sanadTypeId, Long organId) {
		if(sanadTypeId == null)
			return;
		
		SanadHesabdariEntity sanadHesabdariEntity = getLocalSanadHesabdariByDateAndType(sanadHesabDariDate, sanadTypeId, organId);
		if(sanadHesabdariEntity!=null){
			SanadTypeEntity sanadTypeEntity = getSanadTypeService().load(sanadTypeId);
			throw new DuplicateException(SerajMessageUtil.getMessage("sanadAutomaticAlreadyCreated",sanadTypeEntity.getName(), DateConverter.toShamsiDate(sanadHesabDariDate),sanadHesabdariEntity.getTempSerial()));
		}
	}

	public static SanadHesabdariEntity getLocalSanadHesabdariByDateAndType(Date sanadHesabDariDate,
			Long sanadTypeId, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("tarikhSanad@eq", sanadHesabDariDate);
		localFilter.put("sanadType.id@eq", sanadTypeId);
		localFilter.put("organId@eq", organId);
		localFilter.put("state@neq", SanadStateEnum.EBTAL);
		SanadHesabdariEntity sanadHesabdariEntity = getSanadHesabdariService().load(null, localFilter);
		return sanadHesabdariEntity;
	}


	public static HesabKolEntity getHesabKolByTemplate(HesabKolTemplateEntity hesabKolTemplateEntity, Long saalMaaliId){
		return getHesabKolService().loadHesabKolByCode(hesabKolTemplateEntity.getCode(), saalMaaliId);
	}
	
	public static HesabKolEntity getHesabKolByCodeAndSaalMaali(String code, SaalMaaliEntity saalMaaliEntity){
		return getHesabKolService().loadHesabKolByCode(code, saalMaaliEntity.getId());
	}
	
	public static HesabMoeenEntity getHesabMoeenByTemplate(HesabMoeenTemplateEntity hesabMoeenTemplateEntity, Long saalMaaliId){
		return getHesabMoeenService().loadHesabMoeenByCode(hesabMoeenTemplateEntity.getCode(), saalMaaliId);
	}
	
	public static HesabMoeenEntity getHesabMoeenByTemplateId(Long hesabMoeenTemplateId, Long saalMaaliId){
		return getHesabMoeenService().loadHesabMoeenByTemplateId(hesabMoeenTemplateId, saalMaaliId);
	}
	
	public static HesabMoeenEntity getHesabMoeenByCodeAndSaalMaali(String code, SaalMaaliEntity saalMaaliEntity){
		return getHesabMoeenService().loadHesabMoeenByCode(code, saalMaaliEntity.getId());
	}
	
	public static HesabTafsiliEntity getHesabTafsiliByTemplate(HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity, Long saalMaaliId){
		if(hesabTafsiliTemplateEntity==null)
			return null;
		return getHesabTafsiliService().loadHesabTafsiliByCode(new Long(hesabTafsiliTemplateEntity.getCode()), saalMaaliId);
	}
	
	public static HesabTafsiliEntity getHesabTafsiliByTemplateId(Long hesabTafsiliTemplateId, Long saalMaaliId){
		if(hesabTafsiliTemplateId==null)
			return null;
		return getHesabTafsiliService().loadHesabTafsiliByTemplateId(hesabTafsiliTemplateId, saalMaaliId);
	}
	
	public static HesabTafsiliEntity getHesabTafsiliByCodeAndSaalMaali(Long code, SaalMaaliEntity saalMaaliEntity){
		return getHesabTafsiliService().loadHesabTafsiliByCode(code, saalMaaliEntity.getId());
	}
	
	public static AccountingMarkazEntity getAccountingMarkazByTemplate(AccountingMarkazTemplateEntity accountingMarkazTemplateEntity , Long saalMaaliId){
		if(accountingMarkazTemplateEntity == null)
			return null;
		return getAccountingMarkazService().loadAccountingMarkazByCode(accountingMarkazTemplateEntity.getCode(), saalMaaliId);
	}

	public static byte[] printSanad(SanadHesabdariEntity sanadHesabdariEntity) throws JRException {
//		String reportFileName = "PrintSanad2";
		String reportFileName = "PrintSanad-indentation";
		return printSanad(sanadHesabdariEntity, reportFileName);
	}
	public static byte[] printSanad(SanadHesabdariEntity sanadHesabdariEntity, String reportFileName) throws JRException {

		Map<String, Object> parameters = new HashMap<String, Object>();
//		try {
			parameters.put("sanadHesabdari", sanadHesabdariEntity);
			parameters.put("organ_name", sanadHesabdariEntity.getOrganName());
			parameters.put("tanzimKonandeh", sanadHesabdariEntity.getTanzimKonnadeSanad() != null ? sanadHesabdariEntity.getTanzimKonnadeSanad().getShakhsName() : "");
			parameters.put("tayidKonandeh", sanadHesabdariEntity.getTaiedKonnadeSanad()!=null ? sanadHesabdariEntity.getTaiedKonnadeSanad().getShakhsName() : "");
			parameters.put("sanadDesc",  sanadHesabdariEntity.getDescription());
			
			if(sanadHesabdariEntity.getSanadType() != null)
				parameters.put("SanadType", sanadHesabdariEntity.getSanadType().getName());
			

			
			List<SanadHesabdariItemEntity> sanadItemList = sanadHesabdariEntity
					.getSanadHesabdariItem();
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(
					sanadItemList);

//			String filePath = "/WEB-INF/classes/report/PrintSanad.jrxml";
			
			String reportPath = SanadHesabdariUtil.class.getClassLoader().getResource("/report/" + reportFileName+".jrxml").getFile();
//			String reportPath = getLocalFilePath(filePath);

			JasperReport jasperReport;
			jasperReport = JasperCompileManager.compileReport(reportPath);

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			configuration.setPermissions(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
			
//			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			return outputStream.toByteArray();
//			return pdf;
//			String contentType = "application/pdf";
//			downloadStream(pdf, contentType, "Sanad_" + sanadHesabdariEntity.getDesc()
//					+ ".pdf");
//		} catch (JRException e) {
//			e.printStackTrace();
//			return null;
//		}
//		logPrintAction(sanadHesabdariEntity);
//		return null;
	}
	
	public static void logPrintAction(SanadHesabdariEntity sanadHesabdariEntity) {
		String message = SerajMessageUtil.getMessage("SanadHesabdariEntity_title");
		try {
			
				ActionLogUtil.logActionStateLess(SerajMessageUtil.getMessage("PRINT_SANAD"), message,
						sanadHesabdariEntity.getDesc(), null,"");
			} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
