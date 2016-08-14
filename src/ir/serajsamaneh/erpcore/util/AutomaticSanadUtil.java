package ir.serajsamaneh.erpcore.util;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity;
import ir.serajsamaneh.accounting.accountstemplate.AccountsTemplateEntity;
import ir.serajsamaneh.accounting.accountstemplate.AccountsTemplateService;
import ir.serajsamaneh.accounting.articletafsili.ArticleTafsiliEntity;
import ir.serajsamaneh.accounting.common.KolMoeenTafsiliVO;
import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.sanadhesabdari.SanadHesabdariUtil;
import ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity;
import ir.serajsamaneh.accounting.sanadhesabdariitemtemplate.SanadHesabdariItemTemplateEntity;
import ir.serajsamaneh.accounting.sanadtype.SanadTypeService;
import ir.serajsamaneh.core.contact.contact.ContactService;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.systemconfig.SystemConfigService;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.core.util.SpringUtils;
import ir.serajsamaneh.erpcore.contacthesab.ContactHesabEntity;
import ir.serajsamaneh.erpcore.contacthesab.ContactHesabService;

import java.util.Date;

public class AutomaticSanadUtil extends SanadHesabdariUtil {

	
	static AccountsTemplateService accountsTemplateService;
	static ContactService contactService;
	static SystemConfigService systemConfigService;
	static ContactHesabService contactHesabService;
	static SanadTypeService sanadTypeService;


	public static SanadTypeService getSanadTypeService() {
		if(sanadTypeService == null)
			sanadTypeService = SpringUtils.getBean("sanadTypeService");			
		return sanadTypeService;
	}

	public static ContactHesabService getContactHesabService() {
		if(contactHesabService == null)
			contactHesabService = SpringUtils.getBean("contactHesabService");	
		return contactHesabService;
	}

	public static SystemConfigService getSystemConfigService() {
		if(systemConfigService == null)
			systemConfigService = SpringUtils.getBean("systemConfigService");	
		return systemConfigService;
	}

	public static ContactService getContactService() {
		if(contactService == null)
			contactService = SpringUtils.getBean("contactService");	
		return contactService;
	}

	public static AccountsTemplateService getAccountsTemplateService() {
		if(accountsTemplateService == null)
			accountsTemplateService = SpringUtils.getBean("accountsTemplateService");		
		return accountsTemplateService;
	}







	protected static SanadHesabdariItemEntity createBedehkarArticle(Double bedehkarAmount, AccountsTemplateEntity templateEntity, boolean createArticleTafsili, HesabTafsiliTemplateEntity hesabTafsiliTemplateTWO,AccountingMarkazEntity accountingMarkazEntity, String description, OrganEntity organEntity, SaalMaaliEntity saalMaaliEntity) {
		HesabTafsiliEntity hesabTafsiliTWO = getHesabTafsiliByTemplate(hesabTafsiliTemplateTWO, saalMaaliEntity);
		return createBedehkarArticle(bedehkarAmount, templateEntity, createArticleTafsili, hesabTafsiliTWO, accountingMarkazEntity, description, saalMaaliEntity);
	}
	
	protected static SanadHesabdariItemEntity createBedehkarArticle(Double bedehkarAmount, AccountsTemplateEntity templateEntity, boolean createArticleTafsili, HesabTafsiliEntity hesabTafsiliTWO,AccountingMarkazEntity accountingMarkazEntity, String description, SaalMaaliEntity saalMaaliEntity) {
		
		if(bedehkarAmount ==null || bedehkarAmount ==0)
			return null;

		SanadHesabdariItemTemplateEntity sanadHesabdariItemTemplateBedehkar = templateEntity.getSanadHesabdariItemTemplateBedehkar();
		
		if(sanadHesabdariItemTemplateBedehkar == null)
			throw new FatalException(SerajMessageUtil.getMessage("AccountsTemplate_BedehkarNotDefined", templateEntity.getActionName()));


		if(sanadHesabdariItemTemplateBedehkar.getHesabKolTemplate()==null || sanadHesabdariItemTemplateBedehkar.getHesabKolTemplate().getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("AccountsTemplate_HesabKolNotDefined",templateEntity.getActionName()));
		if(sanadHesabdariItemTemplateBedehkar.getHesabMoeenTemplate()==null || sanadHesabdariItemTemplateBedehkar.getHesabMoeenTemplate().getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("AccountsTemplate_HesabMoeenNotDefined",templateEntity.getActionName()));

		
		HesabKolEntity hesabKolEntity = getHesabKolByTemplate(sanadHesabdariItemTemplateBedehkar.getHesabKolTemplate(), saalMaaliEntity);
		HesabMoeenEntity hesabMoeenEntity = getHesabMoeenByTemplate(sanadHesabdariItemTemplateBedehkar.getHesabMoeenTemplate(), saalMaaliEntity);
		HesabTafsiliEntity hesabTafsiliEntityONE = getHesabTafsiliByTemplate(sanadHesabdariItemTemplateBedehkar.getHesabTafsiliTemplate(), saalMaaliEntity);

		if(hesabKolEntity==null || hesabKolEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("HesabKol_notDefinedForOrgan",sanadHesabdariItemTemplateBedehkar.getHesabKolTemplate().getName()));
		if(hesabMoeenEntity==null || hesabMoeenEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("HesabMoeen_notDefinedForOrgan",sanadHesabdariItemTemplateBedehkar.getHesabMoeenTemplate().getName()));

		
		return createBedehkarArticle(hesabKolEntity, hesabMoeenEntity, hesabTafsiliEntityONE,hesabTafsiliTWO, bedehkarAmount,  createArticleTafsili,
				accountingMarkazEntity, description);
	}

	public static SanadHesabdariItemEntity createBedehkarArticle(HesabKolEntity hesabKolEntity, HesabMoeenEntity hesabMoeenEntity,	HesabTafsiliEntity hesabTafsiliEntityONE, 
			HesabTafsiliEntity hesabTafsiliTWO, Double bedehkarAmount, boolean createArticleTafsili,
			AccountingMarkazEntity accountingMarkazEntity, String description) {

		if(hesabKolEntity == null){
			new Throwable().printStackTrace();
			throw new FatalException("HesabKol is null");
		}
		SanadHesabdariItemEntity article = new SanadHesabdariItemEntity();
		article.setAccountingMarkaz(accountingMarkazEntity);
		article.setHesabKol(hesabKolEntity);
		article.setHesabMoeen(hesabMoeenEntity);
		if(hesabTafsiliEntityONE != null && hesabTafsiliEntityONE.getId() != null){
			article.setHesabTafsili(hesabTafsiliEntityONE);
			
			if(createArticleTafsili && hesabTafsiliTWO!=null && hesabTafsiliTWO.getId()!=null && hesabTafsiliEntityONE.getId().longValue()!=hesabTafsiliTWO.getId().longValue()){
				ArticleTafsiliEntity articleTafsiliEntity = new ArticleTafsiliEntity();
				articleTafsiliEntity.setHesabTafsili(hesabTafsiliTWO);
				articleTafsiliEntity.setLevel(1);
				articleTafsiliEntity.setSanadHesabdariItem(article);
				article.addToarticleTafsili(articleTafsiliEntity);
			}
		}else{
			article.setHesabTafsili(hesabTafsiliTWO);
		}
		article.setBedehkar(bedehkarAmount);
		article.setBestankar(0d);
		article.setTarikhArticle(new Date());
		article.setDescription(description);
		return article;
	}
	
	protected static SanadHesabdariItemEntity createBestankarArticle(Double bestanKarAmount,
			AccountsTemplateEntity templateEntity, boolean createArticleTafsili, HesabTafsiliTemplateEntity hesabTafsiliTemplateTWO, AccountingMarkazEntity accountingMarkazEntity, String description, OrganEntity organEntity, SaalMaaliEntity saalMaaliEntity) {
		HesabTafsiliEntity hesabTafsiliTWO = getHesabTafsiliByTemplate(hesabTafsiliTemplateTWO, saalMaaliEntity);
		return createBestankarArticle(bestanKarAmount, templateEntity, createArticleTafsili, hesabTafsiliTWO, accountingMarkazEntity, description, saalMaaliEntity);
	}
	
	protected static SanadHesabdariItemEntity createBestankarArticle(Double bestanKarAmount,
			AccountsTemplateEntity templateEntity, boolean createArticleTafsili, HesabTafsiliEntity hesabTafsiliTWO, AccountingMarkazEntity accountingMarkazEntity, String description, SaalMaaliEntity saalMaaliEntity) {
		
		if(bestanKarAmount ==null || bestanKarAmount ==0)
			return null;
		
		SanadHesabdariItemTemplateEntity sanadHesabdariItemTemplateBestankar = templateEntity.getSanadHesabdariItemTemplateBestankar();
		if(sanadHesabdariItemTemplateBestankar == null)
			throw new FatalException(SerajMessageUtil.getMessage("AccountsTemplate_BestankarNotDefined", templateEntity.getActionName()));
		
		if(sanadHesabdariItemTemplateBestankar.getHesabKolTemplate()==null || sanadHesabdariItemTemplateBestankar.getHesabKolTemplate().getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("AccountsTemplate_HesabKolNotDefined", templateEntity.getActionName()));
		if(sanadHesabdariItemTemplateBestankar.getHesabMoeenTemplate()==null || sanadHesabdariItemTemplateBestankar.getHesabMoeenTemplate().getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("AccountsTemplate_HesabMoeenNotDefined", templateEntity.getActionName()));
		
		HesabKolEntity hesabKolEntity = getHesabKolByTemplate(sanadHesabdariItemTemplateBestankar.getHesabKolTemplate(), saalMaaliEntity);
		HesabMoeenEntity hesabMoeenEntity = getHesabMoeenByTemplate(sanadHesabdariItemTemplateBestankar.getHesabMoeenTemplate(), saalMaaliEntity);
		HesabTafsiliEntity hesabTafsiliEntityONE = getHesabTafsiliByTemplate(sanadHesabdariItemTemplateBestankar.getHesabTafsiliTemplate(), saalMaaliEntity);

		if(hesabKolEntity==null || hesabKolEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("HesabKol_notDefinedForOrgan",sanadHesabdariItemTemplateBestankar.getHesabKolTemplate().getName()));
		if(hesabMoeenEntity==null || hesabMoeenEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("HesabMoeen_notDefinedForOrgan",sanadHesabdariItemTemplateBestankar.getHesabMoeenTemplate().getName()));
		
		return createBestankarArticle(hesabKolEntity, hesabMoeenEntity, hesabTafsiliEntityONE,hesabTafsiliTWO, bestanKarAmount,  createArticleTafsili,
				accountingMarkazEntity, description);
	}

	protected static SanadHesabdariItemEntity createBestankarArticle(HesabKolEntity hesabKolEntity, HesabMoeenEntity hesabMoeenEntity,	HesabTafsiliEntity hesabTafsiliEntityONE, 
			HesabTafsiliEntity hesabTafsiliTWO, Double bestanKarAmount, boolean createArticleTafsili,
			AccountingMarkazEntity accountingMarkazEntity, String description) {

		if(hesabKolEntity==null || hesabKolEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_HesabKolNotDefined"));
		if(hesabMoeenEntity==null || hesabMoeenEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_HesabMoeenNotDefined"));

		
		SanadHesabdariItemEntity article = new SanadHesabdariItemEntity();
		article.setAccountingMarkaz(accountingMarkazEntity);
		article.setHesabKol(hesabKolEntity);
		article.setHesabMoeen(hesabMoeenEntity);
		if(hesabTafsiliEntityONE != null && hesabTafsiliEntityONE.getId() != null){
			article.setHesabTafsili(hesabTafsiliEntityONE);
			
			if(createArticleTafsili && hesabTafsiliTWO!=null && hesabTafsiliTWO.getId().longValue()!=hesabTafsiliEntityONE.getId().longValue()){
				ArticleTafsiliEntity articleTafsiliEntity = new ArticleTafsiliEntity();
				articleTafsiliEntity.setHesabTafsili(hesabTafsiliTWO);
				articleTafsiliEntity.setLevel(1);
				articleTafsiliEntity.setSanadHesabdariItem(article);
				article.addToarticleTafsili(articleTafsiliEntity);
			}
		}else{
			article.setHesabTafsili(hesabTafsiliTWO);
		}
		article.setBedehkar(0d);
		article.setBestankar(bestanKarAmount);
		article.setTarikhArticle(new Date());
		article.setDescription(description);
		return article;
	}
	
	
	protected static SanadHesabdariItemEntity createBestankarArticle(HesabKolEntity hesabKolEntity, HesabMoeenEntity hesabMoeenEntity,	HesabTafsiliEntity hesabTafsiliEntityONE, 
			HesabTafsiliEntity hesabTafsiliTWO,Double bedehkarAmount, boolean createArticleTafsili, AccountingMarkazEntity accountingMarkazEntity, String description, HesabTafsiliEntity sandoghDar) {
		SanadHesabdariItemEntity bestankarArticle = createBestankarArticle(hesabKolEntity, hesabMoeenEntity, hesabTafsiliEntityONE, hesabTafsiliTWO, bedehkarAmount, createArticleTafsili, accountingMarkazEntity, description);
		if(bestankarArticle.getHesabTafsili() == null)
			bestankarArticle.setHesabTafsili(sandoghDar);
		else if(bestankarArticle.getArticleTafsili() == null || bestankarArticle.getArticleTafsili().isEmpty()){
			if(createArticleTafsili && sandoghDar!=null){
				ArticleTafsiliEntity articleTafsiliEntity = new ArticleTafsiliEntity();
				articleTafsiliEntity.setHesabTafsili(sandoghDar);
				articleTafsiliEntity.setLevel(1);
				articleTafsiliEntity.setSanadHesabdariItem(bestankarArticle);
				bestankarArticle.addToarticleTafsili(articleTafsiliEntity);
			}
		} else if(bestankarArticle.getArticleTafsili() != null || !bestankarArticle.getArticleTafsili().isEmpty()){
			if(createArticleTafsili && sandoghDar!=null){
				ArticleTafsiliEntity articleTafsiliEntity = new ArticleTafsiliEntity();
				articleTafsiliEntity.setHesabTafsili(sandoghDar);
				articleTafsiliEntity.setLevel(2);
				articleTafsiliEntity.setSanadHesabdariItem(bestankarArticle);
				bestankarArticle.addToarticleTafsili(articleTafsiliEntity);
			}
		}
		
		return bestankarArticle;
	}

	protected static SanadHesabdariItemEntity createBedehkarArticle(HesabKolEntity hesabKolEntity, HesabMoeenEntity hesabMoeenEntity,	HesabTafsiliEntity hesabTafsiliEntityONE, 
			HesabTafsiliEntity hesabTafsiliTWO,Double bedehkarAmount, boolean createArticleTafsili, AccountingMarkazEntity accountingMarkazEntity, String description, HesabTafsiliEntity actorTafsili) {
		//SanadHesabdariItemEntity bedehkarArticle = createBedehkarArticle(bedehkarAmount, templateEntity, createArticleTafsili, hesabTafsili, accountingMarkazEntity, description);
		SanadHesabdariItemEntity bedehkarArticle = createBedehkarArticle(hesabKolEntity, hesabMoeenEntity, hesabTafsiliEntityONE, hesabTafsiliTWO, bedehkarAmount, createArticleTafsili, accountingMarkazEntity, description);
		if(bedehkarArticle.getHesabTafsili() == null)
			bedehkarArticle.setHesabTafsili(actorTafsili);
		else if(bedehkarArticle.getArticleTafsili() == null || bedehkarArticle.getArticleTafsili().isEmpty()){
			if(createArticleTafsili && actorTafsili!=null){
				ArticleTafsiliEntity articleTafsiliEntity = new ArticleTafsiliEntity();
				articleTafsiliEntity.setHesabTafsili(actorTafsili);
				articleTafsiliEntity.setLevel(1);
				articleTafsiliEntity.setSanadHesabdariItem(bedehkarArticle);
				bedehkarArticle.addToarticleTafsili(articleTafsiliEntity);
			}
		} else if(bedehkarArticle.getArticleTafsili() != null || !bedehkarArticle.getArticleTafsili().isEmpty()){
			if(createArticleTafsili && actorTafsili!=null){
				ArticleTafsiliEntity articleTafsiliEntity = new ArticleTafsiliEntity();
				articleTafsiliEntity.setHesabTafsili(actorTafsili);
				articleTafsiliEntity.setLevel(2);
				articleTafsiliEntity.setSanadHesabdariItem(bedehkarArticle);
				bedehkarArticle.addToarticleTafsili(articleTafsiliEntity);
			}
		}
			
		return bedehkarArticle;
	}

	protected static String convertNullToString(Object value) {
		if(value == null)
			return "";
		return value.toString();
	}

/*	public static Boolean getIsAutomaticSanadActive(OrganEntity organEntity){
		String automaticSanadActive = getSystemConfigService().getValue(organEntity, null, "automaticSanadActive");
		return automaticSanadActive!=null ? new Boolean(automaticSanadActive) : false;
		//return false;
	}
*/


	protected static KolMoeenTafsiliVO extractKolMoeenTafsili(HesabMoeenEntity hesabMoeenParam, HesabTafsiliEntity hesabTafsiliParam, ContactHesabEntity contactHesab, SaalMaaliEntity saalMaaliEntity) {
		KolMoeenTafsiliVO kolMoeenTafsiliVO = extractKolMoeenTafsili(hesabMoeenParam, hesabTafsiliParam, contactHesab);
		
		KolMoeenTafsiliVO newKolMoeenTafsiliVO = new KolMoeenTafsiliVO();
		
		HesabKolEntity hesabKolEntity = kolMoeenTafsiliVO.getHesabKolEntity();
		HesabKolEntity hesabKolByCodeAndSaalMaali = getHesabKolByCodeAndSaalMaali(hesabKolEntity.getCode(), saalMaaliEntity);
		newKolMoeenTafsiliVO.setHesabKolEntity(hesabKolByCodeAndSaalMaali);
		
		HesabMoeenEntity hesabMoeenEntity = kolMoeenTafsiliVO.getHesabMoeenEntity();
		HesabMoeenEntity hesabMoeenByCodeAndSaalMaali = getHesabMoeenByCodeAndSaalMaali(hesabMoeenEntity.getCode(), saalMaaliEntity);
		newKolMoeenTafsiliVO.setHesabMoeenEntity(hesabMoeenByCodeAndSaalMaali);
		
		if(kolMoeenTafsiliVO.getHesabTafsiliEntityONE()!=null){
			newKolMoeenTafsiliVO.setHesabTafsiliEntityONE(getHesabTafsiliByCodeAndSaalMaali(kolMoeenTafsiliVO.getHesabTafsiliEntityONE().getCode(), saalMaaliEntity));
		}
		
		if(kolMoeenTafsiliVO.getHesabTafsiliEntityTWO()!=null){
			newKolMoeenTafsiliVO.setHesabTafsiliEntityTWO(getHesabTafsiliByCodeAndSaalMaali(kolMoeenTafsiliVO.getHesabTafsiliEntityTWO().getCode(), saalMaaliEntity));
		}
		
		return newKolMoeenTafsiliVO;
	}
	
	protected static KolMoeenTafsiliVO extractKolMoeenTafsili(HesabMoeenEntity hesabMoeenParam, HesabTafsiliEntity hesabTafsiliParam, ContactHesabEntity contactHesab) {
		KolMoeenTafsiliVO kolMoeenTafsiliVO = new KolMoeenTafsiliVO();
		HesabKolEntity hesabKolEntity;
		HesabMoeenEntity hesabMoeenEntity;
		HesabTafsiliEntity hesabTafsiliEntityONE;
		HesabTafsiliEntity hesabTafsiliEntityTWO = null;
		
		if(hesabMoeenParam!=null && hesabMoeenParam.getId()!=null){
			hesabKolEntity = hesabMoeenParam.getHesabKol(); 
			hesabMoeenEntity = hesabMoeenParam;
		}else{
			if(contactHesab.getHesabMoeen() == null || contactHesab.getHesabMoeen().getId() == null)
				throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_HesabMoeenNotDefined",contactHesab.getDesc()));
			hesabKolEntity = contactHesab.getHesabMoeen().getHesabKol();
			hesabMoeenEntity = contactHesab.getHesabMoeen();
		}

		if(hesabTafsiliParam!=null){
			hesabTafsiliEntityONE = hesabTafsiliParam;
			hesabTafsiliEntityTWO = contactHesab.getHesabTafsili();
		}else
			hesabTafsiliEntityONE = contactHesab.getHesabTafsili();

		kolMoeenTafsiliVO.setHesabKolEntity(hesabKolEntity);
		kolMoeenTafsiliVO.setHesabMoeenEntity(hesabMoeenEntity);
		kolMoeenTafsiliVO.setHesabTafsiliEntityONE(hesabTafsiliEntityONE);
		kolMoeenTafsiliVO.setHesabTafsiliEntityTWO(hesabTafsiliEntityTWO);
		
		return kolMoeenTafsiliVO;
	}
	

}
