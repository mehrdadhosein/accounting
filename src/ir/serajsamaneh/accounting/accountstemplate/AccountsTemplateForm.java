package ir.serajsamaneh.accounting.accountstemplate;

import ir.serajsamaneh.accounting.accountingmarkaztemplate.AccountingMarkazTemplateService;
import ir.serajsamaneh.accounting.base.BaseAccountingForm;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliService;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateService;
import ir.serajsamaneh.accounting.sanadhesabdariitemtemplate.SanadHesabdariItemTemplateEntity;
import ir.serajsamaneh.core.base.model.SerajDataModel;
import ir.serajsamaneh.core.exception.InCorrectInputException;
import ir.serajsamaneh.core.util.JQueryUtil;
import ir.serajsamaneh.core.util.SerajMessageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.model.DataModel;

import org.springframework.util.StringUtils;

import serajcomponent.DateConverter;
import serajcomponent.SerajDateTimePickerType;

public class AccountsTemplateForm extends
		BaseAccountingForm<AccountsTemplateEntity, Long> {

	@Override
	protected AccountsTemplateService getMyService() {
		return accountsTemplateService;
	}
	
	HesabTafsiliService  hesabTafsiliService;
	AccountingMarkazTemplateService accountingMarkazTemplateService;
	HesabMoeenService hesabMoeenService;
//	MarkazHazineService markazHazineService;
	HesabTafsiliTemplateService hesabTafsiliTemplateService;
	HesabMoeenTemplateService hesabMoeenTemplateService;


	public AccountingMarkazTemplateService getAccountingMarkazTemplateService() {
		return accountingMarkazTemplateService;
	}

	public void setAccountingMarkazTemplateService(
			AccountingMarkazTemplateService accountingMarkazTemplateService) {
		this.accountingMarkazTemplateService = accountingMarkazTemplateService;
	}

	public HesabTafsiliTemplateService getHesabTafsiliTemplateService() {
		return hesabTafsiliTemplateService;
	}

	public void setHesabTafsiliTemplateService(
			HesabTafsiliTemplateService hesabTafsiliTemplateService) {
		this.hesabTafsiliTemplateService = hesabTafsiliTemplateService;
	}

	public HesabMoeenTemplateService getHesabMoeenTemplateService() {
		return hesabMoeenTemplateService;
	}

	public void setHesabMoeenTemplateService(
			HesabMoeenTemplateService hesabMoeenTemplateService) {
		this.hesabMoeenTemplateService = hesabMoeenTemplateService;
	}
/*
	public MarkazHazineService getMarkazHazineService() {
		return markazHazineService;
	}

	public void setMarkazHazineService(MarkazHazineService markazHazineService) {
		this.markazHazineService = markazHazineService;
	}*/

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


	public String localSave() {
		getEntity().setOrgan(getCurrentOrgan());
		List<SanadHesabdariItemTemplateEntity> sanadHesabdariItemList = getSanadHesabdariItemList(getSanadItemsXML(), true);
		getMyService().updateTemplate(getEntity(), sanadHesabdariItemList);
		
		if (getEntity().getID() != null)
			setIsNew(false);
			//getMyService().save(getEntity());

			if (getIsNew()) {
				doExtraSaveAction();
			} else {
				doExtraEditAction();
			}
		return getLocalViewUrl();
	}
	
	public void doExtraSaveAction(){
		String message = SerajMessageUtil.getMessage(getEntityName() + "_title");
		addInfoMessage("SUCCESSFUL_SAVE", message);
	}

	public void doExtraEditAction(){
		String message = SerajMessageUtil.getMessage(getEntityName() + "_title");
		addInfoMessage("SUCCESSFUL_EDIT", message);
	}

	/*
	 * public List<SelectItem> getSanadType(){ Map<String, Object> filter=new
	 * HashMap<String, Object>();
	 * filter.put("organ.id@eq",getCurrentOrgan().getId());
	 * List<AccountsTemplateEntity> list
	 * =getSanadTypeService().getDataList(null, filter);
	 * 
	 * List<SelectItem> selectItemList = new ArrayList<SelectItem>();
	 * selectItemList.add(new SelectItem("", "------------"));
	 * for(AccountsTemplateEntity sanadTypeEntity: list){ selectItemList.add(new
	 * SelectItem(sanadTypeEntity.getId(),sanadTypeEntity.getName())); } return
	 * selectItemList; }
	 */

	AccountsTemplateService accountsTemplateService;

	public AccountsTemplateService getAccountsTemplateService() {
		return accountsTemplateService;
	}

	public void setAccountsTemplateService(
			AccountsTemplateService accountsTemplateService) {
		this.accountsTemplateService = accountsTemplateService;
	}

	Integer incorrectInputExceptionRow;
	protected String incorectItem;
	
	protected SanadHesabdariItemTemplateEntity populateSanadKala(
			Map<String, String> map,
			SanadHesabdariItemTemplateEntity sanadHesabdariItemEntity, Boolean isInMultipleLevelMode) {

		incorectItem = "entityId";
		if (StringUtils.hasText(map.get("entityId")))
			sanadHesabdariItemEntity.setEntityId(map.get("entityId"));

		incorectItem = "templateType";
		if (StringUtils.hasText(map.get("templateType")))
			sanadHesabdariItemEntity.setTemplateType(map.get("templateType"));
		
		incorectItem = "SanadHesabdariItem_hesabTafsili";
		if(isInMultipleLevelMode && StringUtils.hasText(map.get("hesabTafsiliTemplateID")))
			sanadHesabdariItemEntity.setHesabTafsiliTemplate(getHesabTafsiliTemplateService().load(new Long(map.get("hesabTafsiliTemplateID"))));
		else if(!isInMultipleLevelMode)
			sanadHesabdariItemEntity.setHesabTafsiliTemplate(getHesabTafsiliTemplateService().load(new Long(map.get("hesabTafsiliTemplateID"))));

		incorectItem = "SanadHesabdariItem_accountingMarkaz";
		if(isInMultipleLevelMode && StringUtils.hasText(map.get("accountingMarkazTemplateID")))
			sanadHesabdariItemEntity.setAccountingMarkazTemplate(getAccountingMarkazTemplateService().load(new Long(map.get("accountingMarkazTemplateID"))));
		else if(!isInMultipleLevelMode)
			sanadHesabdariItemEntity.setAccountingMarkazTemplate(getAccountingMarkazTemplateService().load(new Long(map.get("accountingMarkazTemplateID"))));


		incorectItem = "SanadHesabdariItem_hesabMoeen";
		HesabMoeenTemplateEntity hesabMoeenTemplateEntity = getHesabMoeenTemplateService().load(new Long(map.get("hesabMoeenTemplateID")));
		sanadHesabdariItemEntity.setHesabMoeenTemplate(hesabMoeenTemplateEntity);
		sanadHesabdariItemEntity.setHesabKolTemplate(hesabMoeenTemplateEntity.getHesabKolTemplate());
		
//		if (StringUtils.hasText(map.get("markazHazineID"))){
//			MarkazHazineEntity markazHazineEntity = getMarkazHazineService().load(new Long(map.get("markazHazineID")));
//			sanadHesabdariItemEntity.setMarkazHazine(markazHazineEntity);
//		}

		
/*		if (StringUtils.hasText(map.get("hesabTafsiliLevels"))){
			String hesabTafsiliLevels = map.get("hesabTafsiliLevels");
			String[] splited = hesabTafsiliLevels.split(",");
			for (String hesabTafsiliLevel : splited) {
				if(hesabTafsiliLevel.isEmpty())
					continue;
				String[] keyValue = hesabTafsiliLevel.split("=");
				if(keyValue.length == 1)
					continue;
				String key = keyValue[0];
				String value = keyValue[1];
				
				Integer level = new Integer(key.substring("hesabTafsili".length()));
				ArticleTafsiliEntity articleTafsiliEntity = new ArticleTafsiliEntity();
				articleTafsiliEntity.setHesabTafsili(getHesabTafsiliService().load(new Long(value)));
				articleTafsiliEntity.setLevel(level);
				articleTafsiliEntity.setSanadHesabdariItem(sanadHesabdariItemEntity);
				sanadHesabdariItemEntity.addToarticleTafsili(articleTafsiliEntity);
				
			}
		}*/
		
		
/*		if (StringUtils.hasText(map.get("accountingMarkazLevels"))){
			String accountingMarkazLevels = map.get("accountingMarkazLevels");
			String[] splited = accountingMarkazLevels.split(",");
			for (String accountingMarkazLevel : splited) {
				if(accountingMarkazLevel.isEmpty())
					continue;
				String[] keyValue = accountingMarkazLevel.split("=");
				if(keyValue.length == 1)
					continue;
				String key = keyValue[0];
				String value = keyValue[1];
				
				Integer level = new Integer(key.substring("accountingMarkaz".length()));
				ArticleTafsiliEntity articleTafsiliEntity = new ArticleTafsiliEntity();
				articleTafsiliEntity.setAccountingMarkaz(getAccountingMarkazService().load(new Long(value)));
				articleTafsiliEntity.setLevel(level);
				articleTafsiliEntity.setSanadHesabdariItem(sanadHesabdariItemEntity);
				sanadHesabdariItemEntity.addToarticleTafsili(articleTafsiliEntity);
				
			}
		}*/
		
		

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

		incorectItem = "SanadHesabdariItem_tarikhArticle";
		sanadHesabdariItemEntity.setTarikhArticle(DateConverter
				.convertStringDateToGDate(SerajDateTimePickerType.Date,
						map.get("tarikhArticle"), "Shamsi"));

		incorectItem = "common_sharh";
		String description = map.get("description");
		description =description.replaceAll("\\p{Cntrl}", "");
		sanadHesabdariItemEntity.setDescription(description);

		return sanadHesabdariItemEntity;

	}
	
	protected List<SanadHesabdariItemTemplateEntity> getSanadHesabdariItemList(
			String sanadItemInput, Boolean isInMultipleLevelMode) {
		List<Map<String, String>> list = JQueryUtil
				.convertJQueryXMLToList(sanadItemInput);
		if (list == null)
			return null;

		List<SanadHesabdariItemTemplateEntity> sanadHesabdariItemEntities = new ArrayList<SanadHesabdariItemTemplateEntity>();
		try {
			incorrectInputExceptionRow = 0;
			for (Map<String, String> map : list) {
				++incorrectInputExceptionRow;
				SanadHesabdariItemTemplateEntity sanadHesabdariItemEntity = new SanadHesabdariItemTemplateEntity();
				sanadHesabdariItemEntity = populateSanadKala(map,
						sanadHesabdariItemEntity, isInMultipleLevelMode);
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
	
	String sanadItemsXML;

	public void setSanadItemsXML(String sanadItemsXML) {
		this.sanadItemsXML = sanadItemsXML;
	}

	public String getSanadItemsXML() {
		if (sanadItemsXML == null) {
			sanadItemsXML = "<?xml version='1.0' encoding='UTF-8'?>\n";

			sanadItemsXML += "<rows>\n";
			sanadItemsXML += "<page>1</page>\n";
			sanadItemsXML += "<total>1</total>\n";
			sanadItemsXML += "<records>2</records>\n";


			SanadHesabdariItemTemplateEntity sanadHesabdariItemTemplateBedehkar = getEntity().getSanadHesabdariItemTemplateBedehkar();
			SanadHesabdariItemTemplateEntity sanadHesabdariItemTemplateBestankar = getEntity().getSanadHesabdariItemTemplateBestankar();
			
			if(sanadHesabdariItemTemplateBedehkar.getId() !=null || getEntity().getId() == null){
				SanadHesabdariItemTemplateVO bedehkarVO = new SanadHesabdariItemTemplateVO(sanadHesabdariItemTemplateBedehkar,"Bedehkar");
				addItemToSanadItemXML(bedehkarVO,"1");
			}
			
			if(sanadHesabdariItemTemplateBestankar.getId() !=null || getEntity().getId() == null){
				SanadHesabdariItemTemplateVO bestankarVO = new SanadHesabdariItemTemplateVO(sanadHesabdariItemTemplateBestankar,"Bestankar");
				addItemToSanadItemXML(bestankarVO,"2");
			}

			sanadItemsXML += "</rows>\n";
		}
		return sanadItemsXML;
	}

	private void addItemToSanadItemXML(
			SanadHesabdariItemTemplateVO sanadHesabdariItemVO, String index) {
		sanadItemsXML += "<row id='" + index + "'>";

		sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getId() + "</cell>";
		sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getTemplateType() + "</cell>";
		sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabKolTemplateID()
				+ "</cell>";
		sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabKolTemplateName()
				+ "</cell>";
		sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabMoeenTemplateID()
				+ "</cell>";
		sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabMoeenTemplateName()
				+ "</cell>";

		sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabTafsiliTemplateID()
				+ "</cell>";
		sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabTafsiliTemplateName()
				+ "</cell>";
		sanadItemsXML += "<cell>"
				+ sanadHesabdariItemVO.getHesabTafsiliTemplateLevelNames() + "</cell>";
		sanadItemsXML += "<cell>"
				+ sanadHesabdariItemVO.getHesabTafsiliTemplateLevels() + "</cell>";
		sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getHesabTafsiliTemplateDescs()
				+ "</cell>";

		sanadItemsXML += "<cell>"
				+ sanadHesabdariItemVO.getAccountingMarkazTemplateID() + "</cell>";
		sanadItemsXML += "<cell>"
				+ sanadHesabdariItemVO.getAccountingMarkazTemplateName() + "</cell>";
		sanadItemsXML += "<cell>"
				+ sanadHesabdariItemVO.getAccountingMarkazLevelNames()
				+ "</cell>";
		sanadItemsXML += "<cell>"	+ sanadHesabdariItemVO.getAccountingMarkazLevels() + "</cell>";
		sanadItemsXML += "<cell>"	+ sanadHesabdariItemVO.getAccountingMarkazDescs() + "</cell>";

		sanadItemsXML += "<cell>"	+ sanadHesabdariItemVO.getApplyAutomaticTafsili() + "</cell>";
		sanadItemsXML += "<cell>"	+ sanadHesabdariItemVO.getApplyAutomaticTafsiliName() + "</cell>";
		

		sanadItemsXML += "<cell>" + sanadHesabdariItemVO.getDescription()	+ "</cell>";

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
	}
	
	public void createAutomaticSanadTemplates() {
		getLogger().info("initializing accounts tempaltes");
		getMyService().createAutomaticSanadTemplates(getCurrentOrgan());
		((SerajDataModel)getLocalDataModel()).clearPage();
		addInfoMessage("SUCCESSFUL_ACTION");
	}

}