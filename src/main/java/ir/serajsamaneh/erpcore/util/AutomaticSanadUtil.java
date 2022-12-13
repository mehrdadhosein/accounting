package ir.serajsamaneh.erpcore.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity;
import ir.serajsamaneh.accounting.accountstemplate.AccountsTemplateEntity;
import ir.serajsamaneh.accounting.accountstemplate.AccountsTemplateService;
import ir.serajsamaneh.accounting.common.KolMoeenTafsiliVO;
import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.sanadhesabdari.SanadHesabdariUtil;
import ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity;
import ir.serajsamaneh.accounting.sanadhesabdariitemtemplate.SanadHesabdariItemTemplateEntity;
import ir.serajsamaneh.accounting.sanadtype.SanadTypeService;
import ir.serajsamaneh.core.common.SaalMaaliVO;
import ir.serajsamaneh.core.contact.contact.ContactService;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.organ.OrganService;
import ir.serajsamaneh.core.systemconfig.SystemConfigService;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.core.util.SpringUtils;
import ir.serajsamaneh.erpcore.contacthesab.ContactHesabEntity;
import ir.serajsamaneh.erpcore.contacthesab.ContactHesabService;

public class AutomaticSanadUtil extends SanadHesabdariUtil {

	static AccountsTemplateService accountsTemplateService;
	static ContactService contactService;
	static SystemConfigService systemConfigService;
	static ContactHesabService contactHesabService;
	static SanadTypeService sanadTypeService;

	static OrganService organService;

//	public static UserService getUserService() {
//		return SpringUtils.getBean("userService");
//	}

	public static OrganService getOrganService() {
		if(organService == null)
			organService = SpringUtils.getBean("organService");
		return organService;
	}
	public static SanadTypeService getSanadTypeService() {
		if (sanadTypeService == null)
			sanadTypeService = SpringUtils.getBean("sanadTypeService");
		return sanadTypeService;
	}

	public static ContactHesabService getContactHesabService() {
		if (contactHesabService == null)
			contactHesabService = SpringUtils.getBean("contactHesabService");
		return contactHesabService;
	}

	public static SystemConfigService getSystemConfigService() {
		if (systemConfigService == null)
			systemConfigService = SpringUtils.getBean("systemConfigService");
		return systemConfigService;
	}
//
//	public static ContactService getContactService() {
//		if(contactService == null)
//			contactService = SpringUtils.getBean("contactService");	
//		return contactService;
//	}

	public static AccountsTemplateService getAccountsTemplateService() {
		if (accountsTemplateService == null)
			accountsTemplateService = SpringUtils.getBean("accountsTemplateService");
		return accountsTemplateService;
	}
	
	protected static SanadHesabdariItemEntity createBedehkarArticle(Double bedehkarAmount,
			AccountsTemplateEntity accountsTemplateEntity, Long hesabTafsiliTemplateId,
			Long hesabTafsiliTemplateTwoId, AccountingMarkazEntity accountingMarkazEntity,
			String description, Long saalMaaliId) {
		HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity = getHesabTafsiliTemplateService().load(hesabTafsiliTemplateId);
		HesabTafsiliTemplateEntity hesabTafsiliTemplateTwoEntity = getHesabTafsiliTemplateService().load(hesabTafsiliTemplateTwoId);
		return createBedehkarArticle(bedehkarAmount, accountsTemplateEntity, hesabTafsiliTemplateEntity, hesabTafsiliTemplateTwoEntity, accountingMarkazEntity, description, saalMaaliId);
	}

	protected static SanadHesabdariItemEntity createBedehkarArticle(Double bedehkarAmount,
			AccountsTemplateEntity templateEntity, HesabTafsiliTemplateEntity hesabTafsiliTemplate,
			HesabTafsiliTemplateEntity hesabTafsiliTemplateTwo, AccountingMarkazEntity accountingMarkazEntity,
			String description, Long saalMaaliId) {

		HesabTafsiliEntity hesabTafsili = getHesabTafsiliByTemplate(hesabTafsiliTemplate, saalMaaliId);
		HesabTafsiliEntity hesabTafsiliTwo = getHesabTafsiliByTemplate(hesabTafsiliTemplateTwo, saalMaaliId);

		return createBedehkarArticle(bedehkarAmount, templateEntity, accountingMarkazEntity, description, saalMaaliId,
				new ArrayList<>(Arrays.asList(hesabTafsili, hesabTafsiliTwo)));
	}

	public static SanadHesabdariItemEntity createBedehkarArticle(Double bedehkarAmount,
			AccountsTemplateEntity templateEntity, HesabTafsiliEntity hesabTafsiliTWO,
			AccountingMarkazEntity accountingMarkazEntity, String description, Long saalMaaliId) {

		if (bedehkarAmount == null || bedehkarAmount == 0)
			return null;

		SanadHesabdariItemTemplateEntity sanadHesabdariItemTemplateBedehkar = templateEntity
				.getSanadHesabdariItemTemplateBedehkar();

		if (sanadHesabdariItemTemplateBedehkar == null)
			throw new FatalException(
					SerajMessageUtil.getMessage("AccountsTemplate_BedehkarNotDefined", templateEntity.getActionName()));

		if (sanadHesabdariItemTemplateBedehkar.getHesabKolTemplate() == null
				|| sanadHesabdariItemTemplateBedehkar.getHesabKolTemplate().getId() == null)
			throw new FatalException(
					SerajMessageUtil.getMessage("AccountsTemplate_HesabKolNotDefined", templateEntity.getActionName()));
		if (sanadHesabdariItemTemplateBedehkar.getHesabMoeenTemplate() == null
				|| sanadHesabdariItemTemplateBedehkar.getHesabMoeenTemplate().getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("AccountsTemplate_HesabMoeenNotDefined",
					templateEntity.getActionName()));

		HesabKolEntity hesabKolEntity = getHesabKolByTemplate(sanadHesabdariItemTemplateBedehkar.getHesabKolTemplate(),
				saalMaaliId);
		HesabMoeenEntity hesabMoeenEntity = getHesabMoeenByTemplate(
				sanadHesabdariItemTemplateBedehkar.getHesabMoeenTemplate(), saalMaaliId);
		HesabTafsiliEntity hesabTafsiliEntityONE = getHesabTafsiliByTemplate(
				sanadHesabdariItemTemplateBedehkar.getHesabTafsiliTemplate(), saalMaaliId);

		if (hesabKolEntity == null || hesabKolEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("HesabKol_notDefinedForOrgan",
					sanadHesabdariItemTemplateBedehkar.getHesabKolTemplate()));
		if (hesabMoeenEntity == null || hesabMoeenEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("HesabMoeen_notDefinedForOrgan",
					sanadHesabdariItemTemplateBedehkar.getHesabMoeenTemplate()));

		return createBedehkarArticle(hesabKolEntity, hesabMoeenEntity, hesabTafsiliEntityONE, hesabTafsiliTWO,
				bedehkarAmount, accountingMarkazEntity, description);
	}

	protected static SanadHesabdariItemEntity createBedehkarArticle(Double bedehkarAmount,
			AccountsTemplateEntity templateEntity, AccountingMarkazEntity accountingMarkazEntity, String description,
			Long saalMaaliId, List<HesabTafsiliEntity> hesabTafsilis) {

		if (bedehkarAmount == null || bedehkarAmount == 0)
			return null;

		SanadHesabdariItemTemplateEntity sanadHesabdariItemTemplateBedehkar = templateEntity
				.getSanadHesabdariItemTemplateBedehkar();

		if (sanadHesabdariItemTemplateBedehkar == null)
			throw new FatalException(
					SerajMessageUtil.getMessage("AccountsTemplate_BedehkarNotDefined", templateEntity.getActionName()));

		if (sanadHesabdariItemTemplateBedehkar.getHesabKolTemplate() == null
				|| sanadHesabdariItemTemplateBedehkar.getHesabKolTemplate().getId() == null)
			throw new FatalException(
					SerajMessageUtil.getMessage("AccountsTemplate_HesabKolNotDefined", templateEntity.getActionName()));
		if (sanadHesabdariItemTemplateBedehkar.getHesabMoeenTemplate() == null
				|| sanadHesabdariItemTemplateBedehkar.getHesabMoeenTemplate().getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("AccountsTemplate_HesabMoeenNotDefined",
					templateEntity.getActionName()));

		HesabKolEntity hesabKolEntity = getHesabKolByTemplate(sanadHesabdariItemTemplateBedehkar.getHesabKolTemplate(),
				saalMaaliId);
		HesabMoeenEntity hesabMoeenEntity = getHesabMoeenByTemplate(
				sanadHesabdariItemTemplateBedehkar.getHesabMoeenTemplate(), saalMaaliId);
		HesabTafsiliEntity hesabTafsiliEntityONE = getHesabTafsiliByTemplate(
				sanadHesabdariItemTemplateBedehkar.getHesabTafsiliTemplate(), saalMaaliId);

		if (hesabKolEntity == null || hesabKolEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("HesabKol_notDefinedForOrgan",
					sanadHesabdariItemTemplateBedehkar.getHesabKolTemplate()));
		if (hesabMoeenEntity == null || hesabMoeenEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("HesabMoeen_notDefinedForOrgan",
					sanadHesabdariItemTemplateBedehkar.getHesabMoeenTemplate()));

		ArrayList<HesabTafsiliEntity> hesabs = new ArrayList<HesabTafsiliEntity>();
		if (null != hesabTafsiliEntityONE)
			hesabs.add(hesabTafsiliEntityONE);
		if (null != hesabTafsilis)
			for (HesabTafsiliEntity hesabTafsiliEntity : hesabTafsilis) {
				if (hesabTafsiliEntity != null)
					hesabs.add(hesabTafsiliEntity);
			}

		return createBedehkarArticle(hesabKolEntity, hesabMoeenEntity, bedehkarAmount, accountingMarkazEntity,
				description, hesabs);
	}

	public static SanadHesabdariItemEntity createBedehkarArticle(HesabKolEntity hesabKolEntity,
			HesabMoeenEntity hesabMoeenEntity, HesabTafsiliEntity hesabTafsiliEntityONE,
			HesabTafsiliEntity hesabTafsiliTWO, Double bedehkarAmount, AccountingMarkazEntity accountingMarkazEntity,
			String description) {

		if (hesabKolEntity == null) {
			new Throwable().printStackTrace();
			throw new FatalException("HesabKol is null");
		}
		SanadHesabdariItemEntity article = new SanadHesabdariItemEntity();
		article.setAccountingMarkaz(accountingMarkazEntity);
		article.setHesabKol(hesabKolEntity);
		article.setHesabMoeen(hesabMoeenEntity);
		if (hesabTafsiliEntityONE != null && hesabTafsiliEntityONE.getId() != null) {
			article.setHesabTafsili(hesabTafsiliEntityONE);

			if (hesabTafsiliTWO != null && hesabTafsiliTWO.getId() != null
					&& hesabTafsiliEntityONE.getId().longValue() != hesabTafsiliTWO.getId().longValue()) {
				// createAddSubArticle(hesabTafsiliTWO, article, 1);
				article.setHesabTafsiliTwo(hesabTafsiliTWO);
			}
		} else {
			article.setHesabTafsili(hesabTafsiliTWO);
		}
		article.setBedehkar(bedehkarAmount);
		article.setBestankar(0d);
		article.setTarikhArticle(new Date());
		article.setDescription(description);
		return article;
	}

	/**
	 * Create a Bedehkar Article Sanad Hesabdari with various level of detail
	 * 
	 * @param hesabKolEntity         Hesab Kol
	 * @param hesabMoeenEntity       Hesab Moeen
	 * @param bedehkarAmount         Amount of money owed
	 * @param createArticleTafsili   if true creates new tafsili articles for each
	 *                               hesab
	 * @param accountingMarkazEntity Markaz hesabdari
	 * @param description            More information
	 * @param hesbTafsilis           All hesab tafsilis in order, first one is Top
	 *                               level Tafsilit, second is shenavar and so on...
	 * @return Sanad hesabdari bedehkar
	 * @author Armin
	 */
	public static SanadHesabdariItemEntity createBedehkarArticle(HesabKolEntity hesabKolEntity,
			HesabMoeenEntity hesabMoeenEntity, Double bedehkarAmount, AccountingMarkazEntity accountingMarkazEntity,
			String description, List<HesabTafsiliEntity> hesbTafsilis) {

		if (hesabKolEntity == null) {
			new Throwable().printStackTrace();
			throw new FatalException("HesabKol is null");
		}

		SanadHesabdariItemEntity article = new SanadHesabdariItemEntity();
		article.setAccountingMarkaz(accountingMarkazEntity);
		article.setHesabKol(hesabKolEntity);
		article.setHesabMoeen(hesabMoeenEntity);

		if (hesbTafsilis != null && !hesbTafsilis.isEmpty()) {
			if (hesbTafsilis.size() == 1)
				article.setHesabTafsili(hesbTafsilis.get(0));
			else if (hesbTafsilis.size() == 2) {
				article.setHesabTafsili(hesbTafsilis.get(0));
				article.setHesabTafsiliTwo(hesbTafsilis.get(1));
			} else {
				article.setHesabTafsili(hesbTafsilis.get(0));
				article.setHesabTafsiliTwo(hesbTafsilis.get(1));
			}
		}
//		articleAttachTafsili(article, hesbTafsilis, createArticleTafsili);

		article.setBedehkar(bedehkarAmount);
		article.setBestankar(0d);
		article.setTarikhArticle(new Date());
		article.setDescription(description);
		return article;
	}

	public static SanadHesabdariItemEntity createBestankarArticle(Double bestanKarAmount,
			AccountsTemplateEntity templateEntity, Long hesabTafsiliTemplateTWOId, Long hesabShenavarTemlateId,
			AccountingMarkazEntity accountingMarkazEntity, String description, Long saalMaaliId) {

		HesabTafsiliEntity hesabTafsiliTWO = getHesabTafsiliByTemplateId(hesabTafsiliTemplateTWOId, saalMaaliId);
		HesabTafsiliEntity hesabShenavar = getHesabTafsiliByTemplateId(hesabShenavarTemlateId, saalMaaliId);

		return createBestankarArticle(bestanKarAmount, templateEntity, accountingMarkazEntity, description, saalMaaliId,
				Arrays.asList(hesabTafsiliTWO, hesabShenavar));
	}

	public static SanadHesabdariItemEntity createBestankarArticle(Double bestanKarAmount,
			AccountsTemplateEntity templateEntity, AccountingMarkazEntity accountingMarkazEntity, String description,
			Long saalMaaliId, List<HesabTafsiliEntity> hesabTafsilis) {

		if (bestanKarAmount == null || bestanKarAmount == 0)
			return null;

		SanadHesabdariItemTemplateEntity sanadHesabdariItemTemplateBestankar = templateEntity
				.getSanadHesabdariItemTemplateBestankar();
		if (sanadHesabdariItemTemplateBestankar == null)
			throw new FatalException(SerajMessageUtil.getMessage("AccountsTemplate_BestankarNotDefined",
					templateEntity.getActionName()));

		if (sanadHesabdariItemTemplateBestankar.getHesabKolTemplate() == null
				|| sanadHesabdariItemTemplateBestankar.getHesabKolTemplate().getId() == null)
			throw new FatalException(
					SerajMessageUtil.getMessage("AccountsTemplate_HesabKolNotDefined", templateEntity.getActionName()));
		if (sanadHesabdariItemTemplateBestankar.getHesabMoeenTemplate() == null
				|| sanadHesabdariItemTemplateBestankar.getHesabMoeenTemplate().getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("AccountsTemplate_HesabMoeenNotDefined",
					templateEntity.getActionName()));

		HesabKolEntity hesabKolEntity = getHesabKolByTemplate(sanadHesabdariItemTemplateBestankar.getHesabKolTemplate(),
				saalMaaliId);
		HesabMoeenEntity hesabMoeenEntity = getHesabMoeenByTemplate(
				sanadHesabdariItemTemplateBestankar.getHesabMoeenTemplate(), saalMaaliId);
		HesabTafsiliEntity hesabTafsiliEntityONE = getHesabTafsiliByTemplate(
				sanadHesabdariItemTemplateBestankar.getHesabTafsiliTemplate(), saalMaaliId);

		if (hesabKolEntity == null || hesabKolEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("HesabKol_notDefinedForOrgan",
					sanadHesabdariItemTemplateBestankar.getHesabKolTemplate()));
		if (hesabMoeenEntity == null || hesabMoeenEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("HesabMoeen_notDefinedForOrgan",
					sanadHesabdariItemTemplateBestankar.getHesabMoeenTemplate()));

		ArrayList<HesabTafsiliEntity> tafsilHesabs = new ArrayList<HesabTafsiliEntity>();

		if (null != hesabTafsiliEntityONE)
			tafsilHesabs.add(hesabTafsiliEntityONE);

		if (null != hesabTafsilis)
			for (HesabTafsiliEntity hesabTafsiliEntity : hesabTafsilis) {
				if (hesabTafsiliEntity != null)
					tafsilHesabs.add(hesabTafsiliEntity);
			}

		return createBestankarArticle(hesabKolEntity, hesabMoeenEntity, bestanKarAmount, accountingMarkazEntity,
				description, tafsilHesabs);
	}

	public static SanadHesabdariItemEntity createBestankarArticle(Double bestanKarAmount,
			AccountsTemplateEntity templateEntity, HesabTafsiliEntity extraHesabTafsili,
			AccountingMarkazEntity accountingMarkazEntity, String description, Long saalMaaliId) {

		if (bestanKarAmount == null || bestanKarAmount == 0)
			return null;

		SanadHesabdariItemTemplateEntity sanadHesabdariItemTemplateBestankar = templateEntity
				.getSanadHesabdariItemTemplateBestankar();
		if (sanadHesabdariItemTemplateBestankar == null)
			throw new FatalException(SerajMessageUtil.getMessage("AccountsTemplate_BestankarNotDefined",
					templateEntity.getActionName()));

		if (sanadHesabdariItemTemplateBestankar.getHesabKolTemplate() == null
				|| sanadHesabdariItemTemplateBestankar.getHesabKolTemplate().getId() == null)
			throw new FatalException(
					SerajMessageUtil.getMessage("AccountsTemplate_HesabKolNotDefined", templateEntity.getActionName()));
		if (sanadHesabdariItemTemplateBestankar.getHesabMoeenTemplate() == null
				|| sanadHesabdariItemTemplateBestankar.getHesabMoeenTemplate().getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("AccountsTemplate_HesabMoeenNotDefined",
					templateEntity.getActionName()));

		HesabKolEntity hesabKolEntity = getHesabKolByTemplate(sanadHesabdariItemTemplateBestankar.getHesabKolTemplate(),
				saalMaaliId);
		HesabMoeenEntity hesabMoeenEntity = getHesabMoeenByTemplate(
				sanadHesabdariItemTemplateBestankar.getHesabMoeenTemplate(), saalMaaliId);
		HesabTafsiliEntity hesabTafsiliEntityONE = getHesabTafsiliByTemplate(
				sanadHesabdariItemTemplateBestankar.getHesabTafsiliTemplate(), saalMaaliId);

		if (hesabKolEntity == null || hesabKolEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("HesabKol_notDefinedForOrgan",
					sanadHesabdariItemTemplateBestankar.getHesabKolTemplate()));
		if (hesabMoeenEntity == null || hesabMoeenEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("HesabMoeen_notDefinedForOrgan",
					sanadHesabdariItemTemplateBestankar.getHesabMoeenTemplate()));

		return createBestankarArticle(hesabKolEntity, hesabMoeenEntity, hesabTafsiliEntityONE, extraHesabTafsili,
				bestanKarAmount, accountingMarkazEntity, description);
	}

	/**
	 * Creates a bestankar article with multiple level hesab Tafsili attachments
	 * This method is a copy/refactor from previous overload
	 * 
	 * @param hesabKolEntity         Hessabdari: Hesab kol
	 * @param hesabMoeenEntity       Hessabdari: Hesab Moeen
	 * @param bestanKarAmount        Hessabdari: Amount of money for bestankar
	 * @param createArticleTafsili   if true creates new article for each tafsili
	 * @param accountingMarkazEntity Hessabdari: Markaz daramad
	 * @param description            Text description for hesabdari
	 * @param hesbTafsilis           Ordered hesab tafsili
	 * @return New sanad hesabdari with attachments, full configured
	 * @author Armin
	 */
	public static SanadHesabdariItemEntity createBestankarArticle(HesabKolEntity hesabKolEntity,
			HesabMoeenEntity hesabMoeenEntity, Double bestanKarAmount, AccountingMarkazEntity accountingMarkazEntity,
			String description, List<HesabTafsiliEntity> hesbTafsilis) {

		if (hesabKolEntity == null || hesabKolEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_HesabKolNotDefined"));
		if (hesabMoeenEntity == null || hesabMoeenEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_HesabMoeenNotDefined"));

		SanadHesabdariItemEntity article = new SanadHesabdariItemEntity();
		article.setAccountingMarkaz(accountingMarkazEntity);
		article.setHesabKol(hesabKolEntity);
		article.setHesabMoeen(hesabMoeenEntity);

		if (hesbTafsilis != null && !hesbTafsilis.isEmpty()) {
			if (hesbTafsilis.size() == 1)
				article.setHesabTafsili(hesbTafsilis.get(0));
			else if (hesbTafsilis.size() == 2) {
				article.setHesabTafsili(hesbTafsilis.get(0));
				article.setHesabTafsiliTwo(hesbTafsilis.get(1));
			} else {
				article.setHesabTafsili(hesbTafsilis.get(0));
				article.setHesabTafsiliTwo(hesbTafsilis.get(1));
			}
		}
//		articleAttachTafsili(article, hesbTafsilis, createArticleTafsili);

		article.setBedehkar(0d);
		article.setBestankar(bestanKarAmount);
		article.setTarikhArticle(new Date());
		article.setDescription(description);
		return article;
	}

	/**
	 * Validate and attach hesab tafsili to article in multiple level
	 * 
	 * @param article              Sanad hesabdari to attach to
	 * @param hesbTafsilis         ordered list of hesab tafsilis fist item is 1st
	 *                             level hesab
	 * @param createArticleTafsili
	 * @author Armin
	 */
//	private static void articleAttachTafsili(SanadHesabdariItemEntity article, List<HesabTafsiliEntity> hesbTafsilis,
//			boolean createArticleTafsili) {
//		LinkedList<HesabTafsiliEntity> hesabs = new LinkedList<HesabTafsiliEntity>();
//
//		// Validate and queue every hesab tafsili
//		for (int i = 0; i < hesbTafsilis.size(); i++) {
//			HesabTafsiliEntity hesTafsili = hesbTafsilis.get(i);
//			if (null != hesTafsili)
//				if (!hesabs.isEmpty()) {
//					if (null != hesTafsili.getId()
//							&& hesabs.peekLast().getId().longValue() != hesTafsili.getId().longValue())
//						hesabs.add(hesTafsili);
//				} else {
//					hesabs.add(hesTafsili);
//				}
//
//		}
//
//		if (!hesabs.isEmpty()) {
//			// Top level hesab tafsili
//			article.setHesabTafsili(hesabs.remove());
//
//			// Add other levels
//			if (createArticleTafsili)
//				for (Integer level = 1; !hesabs.isEmpty(); level++) {
//					createAddSubArticle(hesabs.remove(), article, level);
//				}
//		}
//	}

	/**
	 * Add a sub article to the sanad This method is an extracted method from
	 * createBedehkarArticle and createBedehkarArticle
	 * 
	 * @param hesabTafsili value item
	 * @param article      Owner article for parent
	 * @param level        starts with 1 to higher values in order
	 * @author Armin
	 */
//	private static void createAddSubArticle(HesabTafsiliEntity hesabTafsili, SanadHesabdariItemEntity article, Integer level) {
//		ArticleTafsiliEntity articleTafsiliEntity = new ArticleTafsiliEntity();
//		articleTafsiliEntity.setHesabTafsili(hesabTafsili);
//		articleTafsiliEntity.setLevel(level);
//		articleTafsiliEntity.setSanadHesabdariItem(article);
//		if(article.getArticleTafsili() == null)
//			article.setArticleTafsili(new HashSet<>());
//		article.getArticleTafsili().add(articleTafsiliEntity);
//	}

	public static SanadHesabdariItemEntity createBestankarArticle(HesabKolEntity hesabKolEntity,
			HesabMoeenEntity hesabMoeenEntity, HesabTafsiliEntity hesabTafsiliEntityONE,
			HesabTafsiliEntity hesabTafsiliTWO, Double bestanKarAmount, AccountingMarkazEntity accountingMarkazEntity,
			String description) {

		if (hesabKolEntity == null || hesabKolEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_HesabKolNotDefined"));
		if (hesabMoeenEntity == null || hesabMoeenEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_HesabMoeenNotDefined"));

		SanadHesabdariItemEntity article = new SanadHesabdariItemEntity();
		article.setAccountingMarkaz(accountingMarkazEntity);
		article.setHesabKol(hesabKolEntity);
		article.setHesabMoeen(hesabMoeenEntity);
		if (hesabTafsiliEntityONE != null && hesabTafsiliEntityONE.getId() != null) {
			article.setHesabTafsili(hesabTafsiliEntityONE);

			if (hesabTafsiliTWO != null && hesabTafsiliTWO.getId() != null
					&& hesabTafsiliTWO.getId().longValue() != hesabTafsiliEntityONE.getId().longValue()) {
//				createAddSubArticle(hesabTafsiliTWO, article, 1);
				article.setHesabTafsiliTwo(hesabTafsiliTWO);
			}
		} else {
			article.setHesabTafsili(hesabTafsiliTWO);
		}
		article.setBedehkar(0d);
		article.setBestankar(bestanKarAmount);
		article.setTarikhArticle(new Date());
		article.setDescription(description);
		return article;
	}

	public static SanadHesabdariItemEntity createBestankarArticle(HesabKolEntity hesabKolEntity,
			HesabMoeenEntity hesabMoeenEntity, HesabTafsiliEntity hesabTafsiliEntityONE,
			HesabTafsiliEntity hesabTafsiliTWO, Double bedehkarAmount, AccountingMarkazEntity accountingMarkazEntity,
			String description, HesabTafsiliEntity extraHesabTafsili) {
		SanadHesabdariItemEntity bestankarArticle = createBestankarArticle(hesabKolEntity, hesabMoeenEntity,
				hesabTafsiliEntityONE, hesabTafsiliTWO, bedehkarAmount, accountingMarkazEntity, description);
		if (bestankarArticle.getHesabTafsili() == null)
			bestankarArticle.setHesabTafsili(extraHesabTafsili);
		else if (bestankarArticle.getHesabTafsiliTwo() == null) {
			if (extraHesabTafsili != null) {
				bestankarArticle.setHesabTafsiliTwo(extraHesabTafsili);
			}
		} else
			throw new FatalException();

		return bestankarArticle;
	}

	public static SanadHesabdariItemEntity createBedehkarArticle(HesabKolEntity hesabKolEntity,
			HesabMoeenEntity hesabMoeenEntity, HesabTafsiliEntity hesabTafsiliEntityONE,
			HesabTafsiliEntity hesabTafsiliTWO, Double bedehkarAmount, AccountingMarkazEntity accountingMarkazEntity,
			String description, HesabTafsiliEntity actorTafsili) {
		// SanadHesabdariItemEntity bedehkarArticle =
		// createBedehkarArticle(bedehkarAmount, templateEntity, createArticleTafsili,
		// hesabTafsili, accountingMarkazEntity, description);
		SanadHesabdariItemEntity bedehkarArticle = createBedehkarArticle(hesabKolEntity, hesabMoeenEntity,
				hesabTafsiliEntityONE, hesabTafsiliTWO, bedehkarAmount, accountingMarkazEntity, description);
		if (bedehkarArticle.getHesabTafsili() == null)
			bedehkarArticle.setHesabTafsili(actorTafsili);
		else if (bedehkarArticle.getHesabTafsiliTwo() == null) {
			if (actorTafsili != null) {
				bedehkarArticle.setHesabTafsiliTwo(actorTafsili);
			}
		} else
			throw new FatalException();

		return bedehkarArticle;
	}

	protected static String convertNullToString(Object value) {
		if (value == null)
			return "";
		return value.toString();
	}

	/*
	 * public static Boolean getIsAutomaticSanadActive(OrganEntity organEntity){
	 * String automaticSanadActive =
	 * systemConfigService.getValue(organEntity.getId(), null,
	 * "automaticSanadActive"); return automaticSanadActive!=null ? new
	 * Boolean(automaticSanadActive) : false; //return false; }
	 */

	protected static KolMoeenTafsiliVO extractKolMoeenTafsili(HesabMoeenEntity hesabMoeenParam,
			HesabTafsiliEntity hesabTafsiliParam, HesabTafsiliEntity hesabShenavarParam,
			ContactHesabEntity contactHesab, SaalMaaliVO saalMaaliEntity) {
		KolMoeenTafsiliVO kolMoeenTafsiliVO = extractKolMoeenTafsili(hesabMoeenParam, hesabTafsiliParam,
				hesabShenavarParam, contactHesab);

		KolMoeenTafsiliVO newKolMoeenTafsiliVO = new KolMoeenTafsiliVO();

		HesabKolEntity hesabKolEntity = kolMoeenTafsiliVO.getHesabKolEntity();
		HesabKolEntity hesabKolByCodeAndSaalMaali = getHesabKolByCodeAndSaalMaali(hesabKolEntity.getCode(),
				saalMaaliEntity);
		newKolMoeenTafsiliVO.setHesabKolEntity(hesabKolByCodeAndSaalMaali);

		HesabMoeenEntity hesabMoeenEntity = kolMoeenTafsiliVO.getHesabMoeenEntity();
		HesabMoeenEntity hesabMoeenByCodeAndSaalMaali = getHesabMoeenByCodeAndSaalMaali(hesabMoeenEntity.getCode(),
				saalMaaliEntity);
		newKolMoeenTafsiliVO.setHesabMoeenEntity(hesabMoeenByCodeAndSaalMaali);

		if (kolMoeenTafsiliVO.getHesabTafsiliEntityONE() != null) {
			newKolMoeenTafsiliVO.setHesabTafsiliEntityONE(getHesabTafsiliByCodeAndSaalMaali(
					kolMoeenTafsiliVO.getHesabTafsiliEntityONE().getCode(), saalMaaliEntity));
		}

		if (kolMoeenTafsiliVO.getHesabTafsiliEntityTWO() != null) {
			newKolMoeenTafsiliVO.setHesabTafsiliEntityTWO(getHesabTafsiliByCodeAndSaalMaali(
					kolMoeenTafsiliVO.getHesabTafsiliEntityTWO().getCode(), saalMaaliEntity));
		}

		return newKolMoeenTafsiliVO;
	}

	public static KolMoeenTafsiliVO extractKolMoeenTafsili(Long hesabMoeenIdParam, Long hesabTafsiliIdParam,
			Long hesabTafsiliTwoIdParam, ContactHesabEntity contactHesab) {
		HesabMoeenEntity hesabMoeenEntity = getHesabMoeenService().load(hesabMoeenIdParam);
		HesabTafsiliEntity hesabTafsiliOneEntity = getHesabTafsiliService().load(hesabTafsiliIdParam);
		HesabTafsiliEntity hesabTafsiliTwoEntity = getHesabTafsiliService().load(hesabTafsiliTwoIdParam);
		return extractKolMoeenTafsili(hesabMoeenEntity, hesabTafsiliOneEntity, hesabTafsiliTwoEntity, contactHesab);
	}

	protected static KolMoeenTafsiliVO extractKolMoeenTafsili(HesabMoeenEntity hesabMoeenParam,
			HesabTafsiliEntity hesabTafsiliParam, HesabTafsiliEntity hesabShenavarParam,
			ContactHesabEntity contactHesab) {

		KolMoeenTafsiliVO kolMoeenTafsiliVO = new KolMoeenTafsiliVO();
		HesabKolEntity hesabKolEntity;
		HesabMoeenEntity hesabMoeenEntity;
		HesabTafsiliEntity hesabTafsiliEntityONE;
		HesabTafsiliEntity hesabTafsiliEntityTWO = null;

		/**
		 * Before current mali system we had contact hesab. And the in the case we
		 * hadn't real hesab we extract it from contact.
		 */
		if (hesabMoeenParam != null && hesabMoeenParam.getId() != null) {
			hesabKolEntity = hesabMoeenParam.getHesabKol();
			hesabMoeenEntity = hesabMoeenParam;
		} else {
			if (contactHesab.getHesabMoeen() == null || contactHesab.getHesabMoeen().getId() == null)
				throw new FatalException(
						SerajMessageUtil.getMessage("SanadHesabdari_HesabMoeenNotDefined", contactHesab.getDesc()));
			hesabKolEntity = contactHesab.getHesabMoeen().getHesabKol();
			hesabMoeenEntity = contactHesab.getHesabMoeen();
		}

		if (hesabTafsiliParam != null && hesabTafsiliParam.getId() != null) {
			hesabTafsiliEntityONE = hesabTafsiliParam;
			if (null != hesabShenavarParam && null != hesabShenavarParam.getId())
				hesabTafsiliEntityTWO = hesabShenavarParam;
			else
				hesabTafsiliEntityTWO = contactHesab.getHesabTafsiliTwo();
		} else
			hesabTafsiliEntityONE = contactHesab.getHesabTafsiliOne();

		kolMoeenTafsiliVO.setHesabKolEntity(hesabKolEntity);
		kolMoeenTafsiliVO.setHesabMoeenEntity(hesabMoeenEntity);
		kolMoeenTafsiliVO.setHesabTafsiliEntityONE(hesabTafsiliEntityONE);
		kolMoeenTafsiliVO.setHesabTafsiliEntityTWO(hesabTafsiliEntityTWO);

		return kolMoeenTafsiliVO;
	}

}
