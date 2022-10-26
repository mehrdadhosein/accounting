package ir.serajsamaneh.erpcore.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import ir.serajsamaneh.accounting.accountingmarkaztemplate.AccountingMarkazTemplateEntity;
import ir.serajsamaneh.accounting.accountingmarkaztemplate.AccountingMarkazTemplateService;
import ir.serajsamaneh.accounting.accountstemplate.SanadHesabdariItemTemplateVO;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateService;
import ir.serajsamaneh.accounting.sanadhesabdariitemtemplate.SanadHesabdariItemTemplateEntity;
import ir.serajsamaneh.core.base.BaseForm;
import ir.serajsamaneh.core.exception.InCorrectInputException;
import ir.serajsamaneh.core.util.JQueryUtil;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.core.util.SpringUtils;
import ir.serajsamaneh.erpcore.common.SimpleSanadHesabdariItemTemplateVO;
import serajcomponent.DateConverter;
import serajcomponent.SerajDateTimePickerType;

public class SanadHesabdariTemplateUtil extends BaseForm{
	


	
	static Integer incorrectInputExceptionRow = null;
	static String incorectItem = null;
	
	public static List<SanadHesabdariItemTemplateEntity> getSanadHesabdariItemList(String sanadItemInput) {
		
		List<Map<String, String>> list = JQueryUtil
				.convertJQueryXMLToList(sanadItemInput);
		if (list == null)
			return null;

		List<SanadHesabdariItemTemplateEntity> sanadHesabdariItemEntities = new ArrayList<SanadHesabdariItemTemplateEntity>();
		try {
			incorrectInputExceptionRow = 0;
			for (Map<String, String> map : list) {
				++incorrectInputExceptionRow;
				
				SanadHesabdariItemTemplateEntity sanadHesabdariItemEntity = populateSanadHesabdariItem(map);
				// sanadKalaEntity.setSanad(sanadEntity);
				// sanadEntity.addToSanadKala(sanadKalaEntity);
				sanadHesabdariItemEntities.add(sanadHesabdariItemEntity);
			}
		} catch (NumberFormatException e) {
			throw new InCorrectInputException("Common_incorrectInput",
					SerajMessageUtil.getMessage(incorectItem),
					incorrectInputExceptionRow);
		}
		return sanadHesabdariItemEntities;
	}
	





	
	public static String addItemToSanadItemXML(SanadHesabdariItemTemplateEntity sanadHesabdariItemVO, String index) {
		String sanadItemsXML="";
		sanadItemsXML += "<row id='" + index + "'>";

		sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getId() + "</cell>";
		sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getTemplateType() + "</cell>";
		sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabKolTemplate().getId()+ "</cell>";
		sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabKolTemplate().getName()+ "</cell>";
		sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabMoeenTemplate().getId()+ "</cell>";
		sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabMoeenTemplate().getName()+ "</cell>";

		sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabTafsiliTemplate().getId()+ "</cell>";
		sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabTafsiliTemplate().getName()+ "</cell>";
		
		sanadItemsXML += "<cell>"+ sanadHesabdariItemVO.getAccountingMarkazTemplate().getId() + "</cell>";
		sanadItemsXML += "<cell>"+ sanadHesabdariItemVO.getAccountingMarkazTemplate().getName() + "</cell>";
		sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getDescription()+ "</cell>";

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
		return sanadItemsXML;
	}
	
	protected static SanadHesabdariItemTemplateEntity populateSanadHesabdariItem(Map<String, String> map) {
		SanadHesabdariItemTemplateEntity sanadHesabdariItemTemplateEntity = new SanadHesabdariItemTemplateEntity();
		incorectItem = "entityId";
		if (StringUtils.hasText(map.get("entityId")))
			sanadHesabdariItemTemplateEntity.setEntityId(map.get("entityId"));

		incorectItem = "templateType";
		if (StringUtils.hasText(map.get("templateType")))
			sanadHesabdariItemTemplateEntity.setTemplateType(map.get("templateType"));
		
		incorectItem = "SanadHesabdariItem_hesabTafsili";
		if(StringUtils.hasText(map.get("hesabTafsiliTemplateOneID"))) {
			HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity = getHesabTafsiliTemplateService().load(Long.valueOf(map.get("hesabTafsiliTemplateOneID")));
			sanadHesabdariItemTemplateEntity.setHesabTafsiliTemplate(hesabTafsiliTemplateEntity);
//			sanadHesabdariItemTemplateEntity.setHesabTafsiliTemplateOneID(Long.valueOf(map.get("hesabTafsiliTemplateOneID")));
		}

		incorectItem = "SanadHesabdariItem_hesabTafsiliTwo";
		if(StringUtils.hasText(map.get("hesabTafsiliTemplateTwoID"))) {
			HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity = getHesabTafsiliTemplateService().load(Long.valueOf(map.get("hesabTafsiliTemplateTwoID")));
			sanadHesabdariItemTemplateEntity.setHesabTafsiliTemplateTwo(hesabTafsiliTemplateEntity);
//			sanadHesabdariItemTemplateEntity.setHesabTafsiliTemplateTwoID(Long.valueOf(map.get("hesabTafsiliTemplateTwoID")));
		}
		
		incorectItem = "SanadHesabdariItem_accountingMarkaz";
		if(StringUtils.hasText(map.get("accountingMarkazTemplateID"))) {
			AccountingMarkazTemplateEntity accountingMarkazTemplateEntity = getAccountingMarkazTemplateService().load(Long.valueOf(map.get("accountingMarkazTemplateID")));
			sanadHesabdariItemTemplateEntity.setAccountingMarkazTemplate(accountingMarkazTemplateEntity);
//			sanadHesabdariItemTemplateEntity.setAccountingMarkazTemplateID(Long.valueOf(map.get("accountingMarkazTemplateID")));
		}


		incorectItem = "SanadHesabdariItem_hesabMoeen";
		HesabMoeenTemplateEntity hesabMoeenTemplateEntity = getHesabMoeenTemplateService().load(Long.valueOf(map.get("hesabMoeenTemplateID")));
//		sanadHesabdariItemTemplateEntity.setHesabMoeenTemplateID(Long.valueOf(map.get("hesabMoeenTemplateID")));
		sanadHesabdariItemTemplateEntity.setHesabKolTemplate(hesabMoeenTemplateEntity.getHesabKolTemplate());

//		if (StringUtils.hasText(map.get("hesabTafsiliTemplateLevels"))){
//			String hesabTafsiliTemplateLevels = map.get("hesabTafsiliTemplateLevels");
//			String[] splited = hesabTafsiliTemplateLevels.split(",");
//			for (String hesabTafsiliTemplateLevel : splited) {
//				if(hesabTafsiliTemplateLevel.isEmpty())
//					continue;
//				String[] keyValue = hesabTafsiliTemplateLevel.split("=");
//				if(keyValue.length == 1)
//					continue;
//				String key = keyValue[0];
//				String value = keyValue[1];
//				
//				Integer level = new Integer(key.substring("hesabTafsiliTemplate".length()));
//				ArticleTafsiliTemplateEntity articleTafsiliTemplateEntity = new ArticleTafsiliTemplateEntity();
//				articleTafsiliTemplateEntity.setHesabTafsiliTemplate(getHesabTafsiliTemplateService().load(Long.valueOf(value)));
//				articleTafsiliTemplateEntity.setLevel(level);
//				articleTafsiliTemplateEntity.setSanadHesabdariItemTemplate(sanadHesabdariItemTemplateEntity);
//				sanadHesabdariItemTemplateEntity.addToarticleTafsiliTemplate(articleTafsiliTemplateEntity);
//				
//			}
//		}
		
		incorectItem = "SanadHesabdariItem_bestankar";
		if (StringUtils.hasText(map.get("bestankar")))
			sanadHesabdariItemTemplateEntity.setBestankar(Double.valueOf(map.get("bestankar").replaceAll(",","")));
		else sanadHesabdariItemTemplateEntity.setBestankar(0d);

		incorectItem = "SanadHesabdariItem_bedehkar";
		if (StringUtils.hasText(map.get("bedehkar")))
			sanadHesabdariItemTemplateEntity
					.setBedehkar(Double.valueOf(map.get("bedehkar").replaceAll(",","")));
		else sanadHesabdariItemTemplateEntity.setBedehkar(0d);

		incorectItem = "SanadHesabdariItem_tarikhArticle";
		sanadHesabdariItemTemplateEntity.setTarikhArticle(DateConverter
				.convertStringDateToGDate(SerajDateTimePickerType.Date,
						map.get("tarikhArticle"), "Shamsi"));

//		incorectItem = "SanadHesabdariItemTemplate_applyAutomaticTafsili";
//		String applyAutomaticTafsili = map.get("applyAutomaticTafsili");
//		if (StringUtils.hasText(applyAutomaticTafsili))
//			sanadHesabdariItemTemplateEntity.setApplyAutomaticTafsili(YesNoEnum.valueOf(applyAutomaticTafsili));
		
		incorectItem = "common_sharh";
		String description = map.get("description");
		description =description.replaceAll("\\p{Cntrl}", "");
		sanadHesabdariItemTemplateEntity.setDescription(description);

		return sanadHesabdariItemTemplateEntity;

	}






	
	private static AccountingMarkazTemplateService getAccountingMarkazTemplateService() {
		return SpringUtils.getBean("accountingMarkazTemplateService");
	}
	
	private static HesabTafsiliTemplateService getHesabTafsiliTemplateService() {
		return SpringUtils.getBean("hesabTafsiliTemplateService");
	}

	private static HesabMoeenTemplateService getHesabMoeenTemplateService() {
		return SpringUtils.getBean("hesabMoeenTemplateService");
	}
}
