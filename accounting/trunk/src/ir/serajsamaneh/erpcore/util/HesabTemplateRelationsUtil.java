package ir.serajsamaneh.erpcore.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.ListOrderedMap;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazService;
import ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateEntity;
import ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateService;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateService;
import ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.util.SpringUtils;

public class HesabTemplateRelationsUtil {

	static HesabTafsiliTemplateService hesabTafsiliService;
	static SaalMaaliService saalMaaliService;
	static HesabKolTemplateService hesabKolService;
	static HesabMoeenTemplateService hesabMoeenTemplateService;
	static AccountingMarkazService accountingMarkazService;
	
	public static AccountingMarkazService getAccountingMarkazService() {
		if(accountingMarkazService == null)
			accountingMarkazService = SpringUtils.getBean("accountingMarkazService");		
		return accountingMarkazService;
	}


	public static HesabMoeenTemplateService getHesabMoeenTemplateService() {
		if(hesabMoeenTemplateService == null)
			hesabMoeenTemplateService = SpringUtils.getBean("hesabMoeenTemplateService");		
		return hesabMoeenTemplateService;
	}

	public static HesabKolTemplateService getHesabKolTemplateService() {
		if(hesabKolService == null)
			hesabKolService = SpringUtils.getBean("hesabKolTemplateService");		
		return hesabKolService;
	}

	public static SaalMaaliService getSaalMaaliService() {
		if(saalMaaliService == null)
			saalMaaliService = SpringUtils.getBean("saalMaaliService");
		
		return saalMaaliService;
	}

	public static HesabTafsiliTemplateService getHesabTafsiliTemplateService() {
		if(hesabTafsiliService == null)
			hesabTafsiliService = SpringUtils.getBean("hesabTafsiliTemplateService");
		return hesabTafsiliService;
	}

	//////////////////tafsili moeen map//////////////////////////////////////////
	static Map<Long, Map<Long, List<ListOrderedMap<String, Object>>>> organizationalTafsiliMoeenTemplateMap = new HashMap<Long, Map<Long,List<ListOrderedMap<String, Object>>>>();
	

	public static void resetTafsiliMoeenTemplateMap(OrganEntity organEntity) {
		organizationalTafsiliMoeenTemplateMap.put(organEntity.getId(), null);
	}
	public static Map<Long, List<ListOrderedMap<String, Object>>> getTafsiliMoeenTemplateMap(OrganEntity organEntity) {
		Map<Long, List<ListOrderedMap<String, Object>>> tafsiliMoeenTemplateMap = organizationalTafsiliMoeenTemplateMap.get(organEntity.getId());
		if (tafsiliMoeenTemplateMap == null) {
			tafsiliMoeenTemplateMap = new HashMap<Long, List<ListOrderedMap<String, Object>>>();

			List<HesabTafsiliTemplateEntity> list = getHesabTafsiliTemplateService().getActiveTafsilis(organEntity);
			for (HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity : list) {
				Set<MoeenTafsiliTemplateEntity> moeenTafsiliSet = hesabTafsiliTemplateEntity.getMoeenTafsiliTemplate();
				List<ListOrderedMap<String, Object>> hesabMoeenTemplateList = new ArrayList<ListOrderedMap<String, Object>>();
				for (MoeenTafsiliTemplateEntity moeenTafsiliEntity : moeenTafsiliSet) {
					ListOrderedMap<String, Object> moeenItemTemplateMap = new ListOrderedMap<String, Object>();
					moeenItemTemplateMap.put("value",moeenTafsiliEntity.getHesabMoeenTemplate().getID());
					moeenItemTemplateMap.put("label",moeenTafsiliEntity.getHesabMoeenTemplate().getDesc());
					hesabMoeenTemplateList.add(moeenItemTemplateMap);
				}
				tafsiliMoeenTemplateMap.put(hesabTafsiliTemplateEntity.getId(),hesabMoeenTemplateList);
			}
			organizationalTafsiliMoeenTemplateMap.put(organEntity.getId(), tafsiliMoeenTemplateMap);
		}
		return tafsiliMoeenTemplateMap;
	}
	//////////////////tafsili moeen map//////////////////////////////////////////
	
	//////////////////kol moeen map//////////////////////////////////////////
	static Map<Long, Map<Long, List<ListOrderedMap<String, Object>>>> organizationalKolMoeenTemplateMap = new HashMap<Long, Map<Long,List<ListOrderedMap<String, Object>>>>();
	

	public static void resetKolMoeenTemplateMap(OrganEntity organEntity) {
		organizationalKolMoeenTemplateMap.put(organEntity.getId(), null);
	}
	
	public static Map<Long, List<ListOrderedMap<String, Object>>> getKolMoeenTemplateMap(OrganEntity organEntity) {
		Map<Long, List<ListOrderedMap<String, Object>>> kolMoeenTemplateMap = organizationalKolMoeenTemplateMap.get(organEntity.getId());
		if (kolMoeenTemplateMap == null) {
			kolMoeenTemplateMap = new HashMap<Long, List<ListOrderedMap<String, Object>>>();

			List<HesabKolTemplateEntity> list = getHesabKolTemplateService().getCurrentHesabKolTemplateList(organEntity);
			for (HesabKolTemplateEntity hesabKolEntity : list) {
				List<HesabMoeenTemplateEntity> moeenList = getHesabMoeenTemplateService().getActiveMoeens(hesabKolEntity.getId(), organEntity);
				
				 List<ListOrderedMap<String, Object>> moeenOrderedList = new ArrayList<ListOrderedMap<String, Object>>();
				 for(HesabMoeenTemplateEntity moeenEntity : moeenList){
					 ListOrderedMap<String, Object> moeentemMap = new ListOrderedMap<String, Object>();
					 moeentemMap.put("value",moeenEntity.getID());
					 moeentemMap.put("label",moeenEntity.getDesc());
					 moeenOrderedList.add(moeentemMap);
				 }
				kolMoeenTemplateMap.put(hesabKolEntity.getId(),moeenOrderedList);
			}
			organizationalKolMoeenTemplateMap.put(organEntity.getId(), kolMoeenTemplateMap);
		}
		return kolMoeenTemplateMap;
	}
	
	////////////////kol moeen map//////////////////////////////////////////

	////////////////moeen kol map//////////////////////////////////////////
	
	static Map<Long, Map<Long, ListOrderedMap<String, Object>>> organizationalMoeenKolTemplateMap = new HashMap<Long, Map<Long,ListOrderedMap<String, Object>>>();
	

	public static void resetMoeenKolTemplateMap(OrganEntity organEntity) {
		organizationalMoeenKolTemplateMap.put(organEntity.getId(), null);
	}
	
	public static Map<Long, ListOrderedMap<String, Object>> getMoeenKolTemplateMap(OrganEntity organEntity) {
		Map<Long, ListOrderedMap<String, Object>> moeenKolTemplateMap = organizationalMoeenKolTemplateMap.get(organEntity.getId());
		
		if (moeenKolTemplateMap == null) {
			moeenKolTemplateMap = new HashMap<Long, ListOrderedMap<String, Object>>();

			List<HesabMoeenTemplateEntity> list = getHesabMoeenTemplateService().getActiveMoeens(organEntity);
			for (HesabMoeenTemplateEntity hesabMoeenTemplateEntity : list) {
				ListOrderedMap<String, Object> moeenItemTemplateMap = new ListOrderedMap<String, Object>();
				if(hesabMoeenTemplateEntity.getHesabKolTemplate() == null)
					continue;
				moeenItemTemplateMap.put("value",hesabMoeenTemplateEntity.getHesabKolTemplate().getID());
				moeenItemTemplateMap.put("label",hesabMoeenTemplateEntity.getHesabKolTemplate().getDesc());
				moeenKolTemplateMap.put(hesabMoeenTemplateEntity.getId(),moeenItemTemplateMap);
			}
			organizationalMoeenKolTemplateMap.put(organEntity.getId(), moeenKolTemplateMap);
		}
		return moeenKolTemplateMap;
	}
	////////////////moeen kol map//////////////////////////////////////////

	////////////////moeen tafsili map//////////////////////////////////////////

	static Map<Long, Map<Long, List<ListOrderedMap<String, Object>>>> organizationalMoeenTafsiliTemplateMap = new HashMap<Long, Map<Long,List<ListOrderedMap<String, Object>>>>();
	
	
	public static void resetmoeenTafsiliTemplateMap(OrganEntity organEntity){
		organizationalMoeenTafsiliTemplateMap.put(organEntity.getId(), null);
	}
	
	public static Map<Long, List<ListOrderedMap<String, Object>>> getMoeenTafsiliTemplateMap(OrganEntity organEntity) {
		
		Map<Long, List<ListOrderedMap<String, Object>>> moeenTafsiliTemplateMap = organizationalMoeenTafsiliTemplateMap.get(organEntity.getId());
		
		if(moeenTafsiliTemplateMap == null){
			List<HesabMoeenTemplateEntity> moeenList = getHesabMoeenTemplateService().getActiveMoeens(organEntity);
			moeenTafsiliTemplateMap = new HashMap<Long, List<ListOrderedMap<String, Object>>>();
			for (HesabMoeenTemplateEntity hesabMoeenTemplateEntity : moeenList) {
				List<ListOrderedMap<String, Object>> hesabTafsiliTemplateList = new ArrayList<ListOrderedMap<String, Object>>();
				for (HesabTafsiliTemplateEntity tafsiliEntity : hesabMoeenTemplateEntity.getTafsiliTemplateList()) {
					if(tafsiliEntity.getHidden().equals(false)){
						ListOrderedMap<String, Object> bankBranchItemMap = new ListOrderedMap<String, Object>();
						bankBranchItemMap.put("value",tafsiliEntity.getID());
						bankBranchItemMap.put("label",tafsiliEntity.getDesc());
						hesabTafsiliTemplateList.add(bankBranchItemMap);
					}
				}
				moeenTafsiliTemplateMap.put(hesabMoeenTemplateEntity.getId(), hesabTafsiliTemplateList);
			}
			organizationalMoeenTafsiliTemplateMap.put(organEntity.getId(), moeenTafsiliTemplateMap);
		}
		return moeenTafsiliTemplateMap;
	}
	////////////////moeen tafsili map//////////////////////////////////////////

	////////////////moeen accountingMarkaz map//////////////////////////////////////////
	static Map<Long, Map<Long, List<ListOrderedMap<String, Object>>>> organizationalMoeenAccountingMarkazTemplateMap = new HashMap<Long, Map<Long,List<ListOrderedMap<String, Object>>>>();
	
	public static void resetAccountingMarkazTemplateMap(OrganEntity organEntity){
		organizationalMoeenAccountingMarkazTemplateMap.put(organEntity.getId(), null);
	}
	
	public static Map<Long, List<ListOrderedMap<String, Object>>> getAccountingMarkazTemplateMap(OrganEntity organEntity) {
		return new HashMap<Long, List<ListOrderedMap<String, Object>>>();
//		Map<Long, List<ListOrderedMap>> moeenAccountingMarkazTemplateMap = organizationalMoeenAccountingMarkazTemplateMap.get(organEntity.getId());
//		
//		if(moeenAccountingMarkazTemplateMap == null){
//			List<HesabMoeenTemplateEntity> moeenList = getHesabMoeenTemplateService().getActiveMoeens(organEntity);
//			moeenAccountingMarkazTemplateMap = new HashMap<Long, List<ListOrderedMap>>();
//			for (HesabMoeenTemplateEntity hesabMoeenTemplateEntity : moeenList) {
//				List<ListOrderedMap> accountingMarkazList = new ArrayList<ListOrderedMap>();
//				for (AccountingMarkazEntity accountingMarkazEntity : hesabMoeenTemplateEntity.getAccountingMarkazList()) {
//					if(accountingMarkazEntity.getHidden().equals(false)){
//						ListOrderedMap accountingMarkazItemMap = new ListOrderedMap();
//						accountingMarkazItemMap.put("value",accountingMarkazEntity.getID());
//						accountingMarkazItemMap.put("label",accountingMarkazEntity.getDesc());
//						accountingMarkazList.add(accountingMarkazItemMap);
//					}
//				}
//				moeenAccountingMarkazTemplateMap.put(hesabMoeenTemplateEntity.getId(), accountingMarkazList);
//			}
//			organizationalMoeenAccountingMarkazTemplateMap.put(organEntity.getId(), moeenAccountingMarkazTemplateMap);
//		}
//		return moeenAccountingMarkazTemplateMap;
	}
	////////////////moeen accountingMarkaz map//////////////////////////////////////////
	
	////////////////tafsili child map//////////////////////////////////////////
	
	public static Map<Long, Map<Long, List<ListOrderedMap<String, Object>>>> organizationalTafsiliChildTemplateMap = new HashMap<Long, Map<Long,List<ListOrderedMap<String, Object>>>>();
	
	public static void resetTafsiliChildTemplateMap(OrganEntity organEntity){
		organizationalTafsiliChildTemplateMap.put(organEntity.getId(), null);
	}
	
	public static Map<Long, List<ListOrderedMap<String, Object>>> getTafsiliChildTemplateMap(OrganEntity organEntity) {

		Map<Long, List<ListOrderedMap<String, Object>>> tafsiliChildTemplateMap = organizationalTafsiliChildTemplateMap.get(organEntity.getId());
		if(tafsiliChildTemplateMap == null){
			tafsiliChildTemplateMap = new HashMap<Long, List<ListOrderedMap<String, Object>>>();
			tafsiliChildTemplateMap = getHesabTafsiliTemplateService().getTafsiliChildTemplateMap(organEntity);
			
			organizationalTafsiliChildTemplateMap.put(organEntity.getId(), tafsiliChildTemplateMap);
		}
		return tafsiliChildTemplateMap;
	}	
	////////////////tafsili child map//////////////////////////////////////////




	////////////////tafsili accounting markaz map//////////////////////////////////////////
	
	public static Map<Long, Map<Long, List<ListOrderedMap<String, Object>>>> organizationalTafsiliTemplateMap = new HashMap<Long, Map<Long,List<ListOrderedMap<String, Object>>>>();
	
	public static void resetTafsiliAccountingMarkazChildTemplateMap(OrganEntity organEntity){
		organizationalTafsiliTemplateMap.put(organEntity.getId(), null);
	}
	
	public static Map<Long, List<ListOrderedMap<String, Object>>> getTafsiliAccountingMarkazChildTemplateMap(OrganEntity organEntity) {
		return new HashMap<Long, List<ListOrderedMap<String, Object>>>();
//		Map<Long, List<ListOrderedMap>> tafsiliTemplateMap = organizationalTafsiliTemplateMap.get(organEntity.getId());
//		if(tafsiliTemplateMap == null){
//			tafsiliTemplateMap = new HashMap<Long, List<ListOrderedMap>>();
//			List<HesabTafsiliTemplateEntity> tafsiliList = getHesabTafsiliTemplateService().getActiveTafsilis();
//			for (HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity : tafsiliList) {
//				List<ListOrderedMap> accountingMarkazList = new ArrayList<ListOrderedMap>();
//				for (AccountingMarkazEntity accountingMarkazEntity : hesabTafsiliTemplateEntity.getChildAccountingMarkaz()) {
//					if(accountingMarkazEntity.getHidden().equals(false)){
//						ListOrderedMap accountingMarkazItemMap = new ListOrderedMap();
//						accountingMarkazItemMap.put("value",accountingMarkazEntity.getID());
//						accountingMarkazItemMap.put("label",accountingMarkazEntity.getDesc());
//						accountingMarkazList.add(accountingMarkazItemMap);
//					}
//				}
//				tafsiliTemplateMap.put(hesabTafsiliTemplateEntity.getId(), accountingMarkazList);
//			}
//			organizationalTafsiliTemplateMap.put(organEntity.getId(), tafsiliTemplateMap);
//		}
//		
//		return tafsiliTemplateMap;
	}	
	////////////////tafsili accounting markaz map//////////////////////////////////////////
	
	//////////////// accounting markaz child map//////////////////////////////////////////

	static Map<Long, Map<Long, List<ListOrderedMap<String, Object>>>> organizationalAccountingMarkazChildTemplateMap = new HashMap<Long, Map<Long,List<ListOrderedMap<String, Object>>>>();
	
	public static void resetAccountingMarkazChildTemplateMap(OrganEntity organEntity){
		organizationalAccountingMarkazChildTemplateMap.put(organEntity.getId(), null);
	}
	
	public static Map<Long, List<ListOrderedMap<String, Object>>> getAccountingMarkazChildTemplateMap(OrganEntity organEntity) {

		return new HashMap<Long, List<ListOrderedMap<String, Object>>>();
//		Map<Long, List<ListOrderedMap>> accountingMarkazChildTemplateMap = organizationalAccountingMarkazChildTemplateMap.get(organEntity.getId());
//		if(accountingMarkazChildTemplateMap == null){
//			accountingMarkazChildTemplateMap = new HashMap<Long, List<ListOrderedMap>>();
//			List<AccountingMarkazEntity> accountingMarkazList = getAccountingMarkazService().getActiveAccountingMarkaz();
//			for (AccountingMarkazEntity accountingMarkazEntity : accountingMarkazList) {
//				List<ListOrderedMap> localAccountingMarkazList = new ArrayList<ListOrderedMap>(); 
//				for (AccountingMarkazEntity localAccountingMarkazEntity : accountingMarkazEntity.getChilds()) {
//					if(localAccountingMarkazEntity.getHidden().equals(false)){
//						ListOrderedMap bankBranchItemMap = new ListOrderedMap();
//						bankBranchItemMap.put("value",localAccountingMarkazEntity.getID());
//						bankBranchItemMap.put("label",localAccountingMarkazEntity.getDesc());
//						localAccountingMarkazList.add(bankBranchItemMap);
//					}
//				}
//				accountingMarkazChildTemplateMap.put(accountingMarkazEntity.getId(), localAccountingMarkazList);
//			}
//			organizationalAccountingMarkazChildTemplateMap.put(organEntity.getId(), accountingMarkazChildTemplateMap);
//		}
//
//		return accountingMarkazChildTemplateMap;
	}	
	//////////////// accounting markaz child map//////////////////////////////////////////

	static Map<Long, List<ListOrderedMap<String, String>>> organizationalRootHesabsMap = new HashMap<Long, List<ListOrderedMap<String, String>>>();
	public static List<ListOrderedMap<String, String>> getRootHesabs(OrganEntity currentOrgan) {
		List<ListOrderedMap<String, String>> rootHesabsList = organizationalRootHesabsMap.get(currentOrgan.getId());
		
		if(rootHesabsList == null){
			rootHesabsList = getHesabKolTemplateService().getRootHesabs(currentOrgan);
			
			organizationalRootHesabsMap.put(currentOrgan.getId(), rootHesabsList);
		}
		return rootHesabsList;
	}
	
}
