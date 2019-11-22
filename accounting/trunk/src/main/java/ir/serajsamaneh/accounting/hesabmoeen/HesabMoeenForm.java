package ir.serajsamaneh.accounting.hesabmoeen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.springframework.util.StringUtils;

import ir.serajsamaneh.accounting.base.BaseAccountingForm;
import ir.serajsamaneh.accounting.enumeration.HesabTypeEnum;
import ir.serajsamaneh.accounting.exception.NoSaalMaaliFoundException;
import ir.serajsamaneh.accounting.hesabkol.HesabKolService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliService;
import ir.serajsamaneh.accounting.moeentafsili.MoeenTafsiliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.common.OrganVO;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.exception.InCorrectInputException;
import ir.serajsamaneh.core.util.JQueryUtil;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.erpcore.util.HesabRelationsUtil;


public class HesabMoeenForm extends BaseAccountingForm<HesabMoeenEntity,Long> {




//	private static final int TAFSILI_DEPTH = 6;

	HesabTafsiliService hesabTafsiliService;
	SaalMaaliService saalMaaliService;
	HesabKolService hesabKolService;
	public HesabKolService getHesabKolService() {
		return hesabKolService;
	}

	public List<SelectItem> getLocalHesabMoeenSelectItems(){
		try{
			Map<String, Object> filter = new HashMap<String, Object>();
			
			OrganVO organVO = organService.getOrganVO(getCurrentUserActiveSaalMaali().getOrganId());
			filter.put("organ.code@startlk", organVO.getCode());
			List<HesabMoeenEntity>  list = getMyService().getDataList(null,filter);
			List<SelectItem> resultList = new ArrayList<SelectItem>();
			for (HesabMoeenEntity entity: list){
				resultList.add(new SelectItem(entity.getId(), entity.getDesc()));
			}
			return resultList;
		}catch(NoSaalMaaliFoundException e){
			e.printStackTrace();
			return new ArrayList<SelectItem>();
		}
	}
	public void setHesabKolService(HesabKolService hesabKolService) {
		this.hesabKolService = hesabKolService;
	}


	public SaalMaaliService getSaalMaaliService() {
		return saalMaaliService;
	}


	public void setSaalMaaliService(SaalMaaliService saalMaaliService) {
		this.saalMaaliService = saalMaaliService;
	}


	public HesabTafsiliService getHesabTafsiliService() {
		return hesabTafsiliService;
	}


	public void setHesabTafsiliService(HesabTafsiliService hesabTafsiliService) {
		this.hesabTafsiliService = hesabTafsiliService;
	}


	@Override
	protected HesabMoeenService getMyService() {
		return hesabMoeenService;
	}


	@Override
	public DataModel<HesabMoeenEntity> getDataModel() {
		setSearchAction(true);
		//this.getFilter().put("organ.code@startlk", getTopOrgan().getCode());
		populateTopOrgansIdListFilter();

		this.getFilter().put("saalMaali.id@eq",getCurrentUserActiveSaalMaali().getId());
		return super.getDataModel();
	}



	@Override
	public DataModel<HesabMoeenEntity> getHierarchicalDataModel() {
		setSearchAction(true);
		getFilter().put("saalMaali.id@eq",getCurrentUserActiveSaalMaali().getId());
		return super.getHierarchicalDataModel();
	}
	
	@Override
	public Integer getHierarchicalDataModelCount() {
		getFilter().put("saalMaali.id@eq",getCurrentUserActiveSaalMaali().getId());
		return super.getHierarchicalDataModelCount();
	}
	
	public void initMoeenTafsiliItems(HesabMoeenEntity hesabMoeenEntity,
			String itemsInput) {
		if (hesabMoeenEntity.getMoeenTafsili() == null)
			hesabMoeenEntity.setMoeenTafsili(new HashSet<MoeenTafsiliEntity>());
		else
			hesabMoeenEntity.getMoeenTafsili().clear();
		List<TafsiliLevelVo> tafsiliLevelVOList = getMoeenTafsiliList(itemsInput);
		for (TafsiliLevelVo tafsiliLevelVo : tafsiliLevelVOList) {
			
			String hesabTafsiliListIds = tafsiliLevelVo.getHesabTafsiliListIds();
			StringTokenizer tokenizer = new StringTokenizer(hesabTafsiliListIds,",");
			while(tokenizer.hasMoreTokens()){
				String hesabTafsiliId = tokenizer.nextToken();
				
				HesabTafsiliEntity hesabTafsiliEntity = getHesabTafsiliService().load(new Long(hesabTafsiliId));
				
				MoeenTafsiliEntity moeenTafsiliEntity = new MoeenTafsiliEntity();
				moeenTafsiliEntity.setLevel(tafsiliLevelVo.getLevel());
				moeenTafsiliEntity.setHesabMoeen(hesabMoeenEntity);
				moeenTafsiliEntity.setHesabTafsili(hesabTafsiliEntity);
				hesabMoeenEntity.addTomoeenTafsili(moeenTafsiliEntity);
			}

		}
	}


	
	@Override
	public String save() {
		if(getEntity().getOrganId() == null) {
			getEntity().setOrganId(getCurrentOrganVO().getId());
			getEntity().setOrganName(getCurrentOrganVO().getName());
		}
		
//		initMoeenTafsiliItems(getEntity(), getTafsiliLevelsXML());

		getEntity().setHesabKol(getHesabKolService().load(getEntity().getHesabKol().getId()));
		getMyService().save(getEntity(), getCurrentUserActiveSaalMaali(), getCurrentOrganVO().getId(), getOldEntity().getHesabKol(), getCurrentOrganVO().getTopOrgansIdList(), getCurrentOrganVO().getName());
		resetHesabRelations();
		addInfoMessage("SUCCESSFUL_ACTION");
		return getViewUrl();
	}

	private void resetHesabRelations() {
		List<Long> subsetOrganIds = getRelatedOrganIds();
		for (Long organId : subsetOrganIds) {
//			OrganEntity organEntity = organService.load(organId);
			HesabRelationsUtil.resetKolMoeenMap(getCurrentUserActiveSaalMaali(), organId);
			HesabRelationsUtil.resetMoeenKolMap(getCurrentUserActiveSaalMaali(), organId);
			HesabRelationsUtil.resetmoeenTafsiliOneMap(getCurrentUserActiveSaalMaali(), organId);
			HesabRelationsUtil.resetmoeenTafsiliTwoMap(getCurrentUserActiveSaalMaali(), organId);
			HesabRelationsUtil.resetAccountingMarkazMap(getCurrentUserActiveSaalMaali(), organId);
			HesabRelationsUtil.resetRootHesabsMap(getCurrentUserActiveSaalMaali(), organId);
			
		}
	}


	
	HesabMoeenService hesabMoeenService;
	
	public void setHesabMoeenService(HesabMoeenService hesabMoeenService) {
		this.hesabMoeenService = hesabMoeenService;
	}
	
	public HesabMoeenService getHesabMoeenService() {
		return hesabMoeenService;
	}
	
	@Override
	public List<? extends BaseEntity> getJsonList(String property, String term,
			boolean all, Map<String, String> params) {
			try{
				String isHierarchical = params.get("isHierarchical");
				String hidden = params.get("hidden");
				String hesabType = params.get("hesabType");
				
				if(StringUtils.hasText(hesabType) && hesabType.equals(HesabTypeEnum.EXPENSE.name()))
					this.getFilter().put("hesabKol.hesabGroup.type@eq",HesabTypeEnum.EXPENSE);
				
				if(StringUtils.hasText(hesabType) && hesabType.equals(HesabTypeEnum.INCOME.name()))
					this.getFilter().put("hesabKol.hesabGroup.type@eq",HesabTypeEnum.INCOME);
				
				this.getFilter().put("saalMaali.id@eq",getCurrentUserActiveSaalMaali().getId());
				
				if(StringUtils.hasText(hidden) && hidden.equals("false")){
					this.getFilter().put("hidden@eq",false);
				}
				
				if (isHierarchical !=null && isHierarchical.equals("true")){
					
					List<Long> topOrganList = getCurrentOrganVO().getTopOrgansIdList();
					getFilter().put("organId@in", topOrganList);
					
	//				this.getFilter().put("organ.code@startlk", getCurrentUserActiveSaalMaali().getOrgan().getCode());
					params.put("isLocal","false");
				}
			return super.getJsonList(property, term, all, params);
		}catch(NoSaalMaaliFoundException e){
			//e.printStackTrace();
			System.out.println(e.getMessage());
			return new ArrayList<>();
		}
	}
	
	
	public Map<Long, ListOrderedMap<String, Object>> getMoeenKolMap() {
		return HesabRelationsUtil.getMoeenKolMap(getCurrentUserActiveSaalMaali(), getCurrentOrganVO().getId(), getCurrentOrganVO().getTopOrgansIdList());
	}
	
//	public List<ListOrderedMap<String, Object>> getRootHesabsMap() {
//		return HesabRelationsUtil.getRootHesabs(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
//	}
	
	public Map<Long, List<ListOrderedMap<String, Object>>> getMoeenTafsiliOneMap() {
		return HesabRelationsUtil.getMoeenTafsiliOneMap(getCurrentUserActiveSaalMaali(), getCurrentOrganVO().getId(), getCurrentOrganVO().getTopOrgansIdList());
	}
	
	public Map<Long, List<ListOrderedMap<String, Object>>> getMoeenTafsiliTwoMap() {
		return HesabRelationsUtil.getMoeenTafsiliTwoMap(getCurrentUserActiveSaalMaali(), getCurrentOrganVO().getId(), getCurrentOrganVO().getTopOrgansIdList());
	}
	
	public Map<Long, List<ListOrderedMap<String, Object>>> getTafsiliChildrenMap() {
		return HesabRelationsUtil.getTafsiliChildMap(getCurrentUserActiveSaalMaali(), getCurrentOrganVO().getId(), getCurrentOrganVO().getTopOrgansIdList());
	}

	public Map<Long, List<ListOrderedMap<String, Object>>> getAccountingMarkazMap() {
		return HesabRelationsUtil.getAccountingMarkazMap(getCurrentUserActiveSaalMaali(), getCurrentOrganVO().getId(), getCurrentOrganVO().getTopOrgansIdList());
	}
	
//	private List<TafsiliLevelVo> getTafsiliLevelsList(Long mooenId){
//		HesabMoeenEntity hesabMoeenEntity = getMyService().load(mooenId);
//		List<TafsiliLevelVo> tafsiliLevelsList = new ArrayList<TafsiliLevelVo>();
//		Map<Integer, List<HesabTafsiliEntity>> tafsiliLevelMap = new HashMap<Integer, List<HesabTafsiliEntity>>();
//
//		for(int level = 1 ; level<=getLevelsSize();level++)
//			tafsiliLevelMap.put(level, new ArrayList<HesabTafsiliEntity>());
//
//		
//		Set<MoeenTafsiliEntity> moeenTafsiliSet = hesabMoeenEntity.getMoeenTafsili();
//		for (MoeenTafsiliEntity moeenTafsiliEntity : moeenTafsiliSet) {
//			tafsiliLevelMap.get(moeenTafsiliEntity.getLevel()).add(moeenTafsiliEntity.getHesabTafsili());
//		}
//		
//		Set<Entry<Integer, List<HesabTafsiliEntity>>> entrySet = tafsiliLevelMap.entrySet();
//		for (Entry<Integer, List<HesabTafsiliEntity>> entry : entrySet) {
//			tafsiliLevelsList.add(new TafsiliLevelVo(entry.getKey(), entry.getValue()));
//		}
//		
//		return tafsiliLevelsList;
//	}


	String tafsiliLevelsXML = null;
	public String getTafsiliLevelsXML() {
		if(tafsiliLevelsXML == null)
			tafsiliLevelsXML = getXMLString(getEntity().getId());
		return tafsiliLevelsXML;
	}

	public void setTafsiliLevelsXML(String factorItemsXML) {
		this.tafsiliLevelsXML = factorItemsXML;
	}
	
	private String getXMLString(Long hesabMoeenId) {

		if (hesabMoeenId == null)
			return "<?xml version='1.0' encoding='UTF-8'?><rows>\n<page>1</page>\n<total>1</total>\n<records>0</records>\n</rows>\n";

		String xmlString = "<?xml version='1.0' encoding='UTF-8'?>\n";
		HesabMoeenEntity hesabMoeenEntity = getMyService().load(hesabMoeenId);
		xmlString += "<rows>\n";
		xmlString += "<page>1</page>\n";
		xmlString += "<total>1</total>\n";
		xmlString += "<records>0</records>\n";
		Integer index = 1;
		Set<MoeenTafsiliEntity> moeenTafsiliSet = hesabMoeenEntity.getMoeenTafsili();
		for (MoeenTafsiliEntity moeenTafsiliEntity : moeenTafsiliSet) {
			
			xmlString += "<row id='" + index + "'>";

			xmlString += "<cell>" + index + "</cell>";
			xmlString += "<cell>" + moeenTafsiliEntity.getHesabTafsili().getLevel() + "</cell>";
			xmlString += "<cell>" + moeenTafsiliEntity.getHesabTafsili().getName() + "</cell>";
			xmlString += "<cell></cell>";
			String editFunction = "";
			String edit = "";
			xmlString += "<cell><![CDATA[ " + edit + "]]></cell>";
			xmlString += "</row>";
			++index;
		}
		xmlString += "</rows>\n";
		return xmlString;
	}
	

	public static List<TafsiliLevelVo> getMoeenTafsiliList(String kalahaInput) {
		List<Map<String, String>> tafsiliLevelsList = JQueryUtil
				.convertJQueryXMLToList(kalahaInput);
		if (tafsiliLevelsList == null)
			return null;

		List<TafsiliLevelVo> tafsiliLevelsVO = new ArrayList<TafsiliLevelVo>();
		Integer incorrectInputExceptionRow;
		incorrectInputExceptionRow = 0;
		for (Map<String, String> tafsiliLevelMap : tafsiliLevelsList) {
			++incorrectInputExceptionRow;
			TafsiliLevelVo tafsiliLevelVo = new TafsiliLevelVo();
			tafsiliLevelVo = populateTafsiliLevel(tafsiliLevelMap, tafsiliLevelVo, incorrectInputExceptionRow);
			tafsiliLevelsVO.add(tafsiliLevelVo);
		}

		return tafsiliLevelsVO;
	}
	
	protected static TafsiliLevelVo populateTafsiliLevel(
			Map<String, String> tafsiliLevelMap, TafsiliLevelVo tafsiliLevelVO, Integer incorrectInputExceptionRow) {

		String incorectItem="";
		try {
			incorectItem = "level";
			if (StringUtils.hasText(tafsiliLevelMap.get("level")))
				tafsiliLevelVO.setLevel(new Integer(tafsiliLevelMap.get("level")));

			incorectItem = "MoeenTafsili_list";
			if (StringUtils.hasText(tafsiliLevelMap.get("hesabTafsiliIds"))){
				tafsiliLevelVO.setHesabTafsiliListIds(tafsiliLevelMap.get("hesabTafsiliIds"));
			}
			
		} catch (Exception e) {
			throw new InCorrectInputException("Common_incorrectInput",
					SerajMessageUtil.getMessage(incorectItem),
					incorrectInputExceptionRow);
		}
		return tafsiliLevelVO;

	}


	
//	public  Integer getMaxLevel(){
//		return 5;
//	}
	
	
	public String importFromHesabMoeenTemplateList(){
		getMyService().importFromHesabMoeenTemplateList(getCurrentUserActiveSaalMaali(), getCurrentOrganVO().getId(), getCurrentOrganVO().getTopOrgansIdList(), getCurrentOrganVO().getName());
		setDataModel(null);
		return null;
	}
	
	@Override
	public String delete() {
		if(!getIsForMyOrgan())
			throw new FatalException(SerajMessageUtil.getMessage("common_deleteNotAllowed"));
		return super.delete();
	}
	
	public boolean getIsForMyOrgan() {
		if(getEntity() == null || getEntity().getId() == null)
			return true;
		return getEntity().getOrganId().equals(getCurrentOrganVO().getId());
	}

}