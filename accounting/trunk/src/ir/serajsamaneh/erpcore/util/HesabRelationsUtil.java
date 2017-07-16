package ir.serajsamaneh.erpcore.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.ListOrderedMap;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity;
import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazService;
import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.hesabkol.HesabKolService;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.core.util.SpringUtils;

public class HesabRelationsUtil {

	static HesabTafsiliService hesabTafsiliService;
	static SaalMaaliService saalMaaliService;
	static HesabKolService hesabKolService;
	static HesabMoeenService hesabMoeenService;
	static AccountingMarkazService accountingMarkazService;
	
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

	//////////////////tafsili moeen map//////////////////////////////////////////
	static Map<String, Map<Long, List<ListOrderedMap<String, Object>>>> organizationalTafsiliMoeenMap = new HashMap<String, Map<Long,List<ListOrderedMap<String, Object>>>>();
	

	public static void resetTafsiliMoeenMap(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan) {
		organizationalTafsiliMoeenMap.put(saalMaaliEntity.getId()+"_"+currentOrgan.getId(), null);
	}
	public static Map<Long, List<ListOrderedMap<String, Object>>> getTafsiliMoeenMap(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan) {
		Map<Long, List<ListOrderedMap<String, Object>>> tafsiliMoeenMap = organizationalTafsiliMoeenMap.get(saalMaaliEntity.getId()+"_"+currentOrgan.getId()+"_"+currentOrgan.getId());
		if (tafsiliMoeenMap == null) {
			tafsiliMoeenMap = getHesabTafsiliService().getTafsiliMoeenMap(saalMaaliEntity, currentOrgan);
			organizationalTafsiliMoeenMap.put(saalMaaliEntity.getId()+"_"+currentOrgan.getId(), tafsiliMoeenMap);
		}
		return tafsiliMoeenMap;
	}
	//////////////////tafsili moeen map//////////////////////////////////////////



	
	//////////////////kol moeen map//////////////////////////////////////////
	static Map<String, Map<Long, List<ListOrderedMap<String, Object>>>> organizationalKolMoeenMap = new HashMap<String, Map<Long,List<ListOrderedMap<String, Object>>>>();
	

	public static void resetKolMoeenMap(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan) {
		organizationalKolMoeenMap.put(saalMaaliEntity.getId()+"_"+currentOrgan.getId(), null);
	}
	
	public static Map<Long, List<ListOrderedMap<String, Object>>> getKolMoeenMap(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan) {
		Map<Long, List<ListOrderedMap<String, Object>>> kolMoeenMap = organizationalKolMoeenMap.get(saalMaaliEntity.getId()+"_"+currentOrgan.getId());
		if (kolMoeenMap == null) {
			kolMoeenMap = new HashMap<Long, List<ListOrderedMap<String, Object>>>();

			List<HesabKolEntity> list = getHesabKolService().getHesabKolList(saalMaaliEntity, currentOrgan);
			for (HesabKolEntity hesabKolEntity : list) {
				List<HesabMoeenEntity> moeenList = getHesabMoeenService().getActiveMoeens(hesabKolEntity.getId(), saalMaaliEntity, currentOrgan);
				
				 List<ListOrderedMap<String, Object>> moeenOrderedList = new ArrayList<ListOrderedMap<String, Object>>();
				 for(HesabMoeenEntity moeenEntity : moeenList){
					 ListOrderedMap<String, Object> moeentemMap = new ListOrderedMap<String, Object>();
					 moeentemMap.put("value",moeenEntity.getID());
					 moeentemMap.put("label",moeenEntity.getDesc());
					 moeenOrderedList.add(moeentemMap);
				 }
				kolMoeenMap.put(hesabKolEntity.getId(),moeenOrderedList);
			}
			organizationalKolMoeenMap.put(saalMaaliEntity.getId()+"_"+currentOrgan.getId(), kolMoeenMap);
		}
		return kolMoeenMap;
	}
	
	////////////////kol moeen map//////////////////////////////////////////

	////////////////moeen kol map//////////////////////////////////////////
	
	static Map<String, Map<Long, ListOrderedMap<String, Object>>> organizationalMoeenKolMap = new HashMap<String, Map<Long,ListOrderedMap<String, Object>>>();
	

	public static void resetMoeenKolMap(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan) {
		organizationalMoeenKolMap.put(saalMaaliEntity.getId()+"_"+currentOrgan.getId(), null);
	}
	
	public static Map<Long, ListOrderedMap<String, Object>> getMoeenKolMap(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan) {
		Map<Long, ListOrderedMap<String, Object>> moeenKolMap = organizationalMoeenKolMap.get(saalMaaliEntity.getId()+"_"+currentOrgan.getId());
		
		if (moeenKolMap == null) {
			moeenKolMap = new HashMap<Long, ListOrderedMap<String, Object>>();

			List<HesabMoeenEntity> list = getHesabMoeenService().getActiveMoeens(saalMaaliEntity, currentOrgan);
			for (HesabMoeenEntity hesabMoeenEntity : list) {
				ListOrderedMap<String, Object> moeenItemMap = new ListOrderedMap<String, Object>();
				moeenItemMap.put("value",hesabMoeenEntity.getHesabKol()!=null ? hesabMoeenEntity.getHesabKol().getID() : SerajMessageUtil.getMessage("common_undefined"));
				moeenItemMap.put("label",hesabMoeenEntity.getHesabKol()!=null ? hesabMoeenEntity.getHesabKol().getDesc() : SerajMessageUtil.getMessage("common_undefined"));
				moeenKolMap.put(hesabMoeenEntity.getId(),moeenItemMap);
			}
			organizationalMoeenKolMap.put(saalMaaliEntity.getId()+"_"+currentOrgan.getId(), moeenKolMap);
		}
		return moeenKolMap;
	}
	////////////////moeen kol map//////////////////////////////////////////

	////////////////moeen tafsili map//////////////////////////////////////////

	static Map<String, Map<Long, List<ListOrderedMap<String, Object>>>> organizationalMoeenTafsiliMap = new HashMap<String, Map<Long,List<ListOrderedMap<String, Object>>>>();
	
	
	public static void resetmoeenTafsiliMap(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan){
		organizationalMoeenTafsiliMap.put(saalMaaliEntity.getId()+"_"+currentOrgan.getId(), null);
	}
	
	public static Map<Long, List<ListOrderedMap<String, Object>>> getMoeenTafsiliMap(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan) {
		
		Map<Long, List<ListOrderedMap<String, Object>>> moeenTafsiliMap = organizationalMoeenTafsiliMap.get(saalMaaliEntity.getId()+"_"+currentOrgan.getId());
		
		if(moeenTafsiliMap == null){
			List<HesabMoeenEntity> moeenList = getHesabMoeenService().getActiveMoeens(saalMaaliEntity, currentOrgan);
			moeenTafsiliMap = new HashMap<Long, List<ListOrderedMap<String, Object>>>();
			for (HesabMoeenEntity hesabMoeenEntity : moeenList) {
				List<ListOrderedMap<String, Object>> hesabTafsiliList = new ArrayList<ListOrderedMap<String, Object>>();
				List<HesabTafsiliEntity> activeTafsilies = getHesabMoeenService().getActiveTafsilies(hesabMoeenEntity, saalMaaliEntity, currentOrgan);
				for (HesabTafsiliEntity tafsiliEntity : activeTafsilies) {
					if(tafsiliEntity.getHidden().equals(false)){
						ListOrderedMap<String, Object> bankBranchItemMap = new ListOrderedMap<String, Object>();
						bankBranchItemMap.put("value",tafsiliEntity.getID());
						bankBranchItemMap.put("label",tafsiliEntity.getDesc());
						hesabTafsiliList.add(bankBranchItemMap);
					}
				}
				moeenTafsiliMap.put(hesabMoeenEntity.getId(), hesabTafsiliList);
			}
			organizationalMoeenTafsiliMap.put(saalMaaliEntity.getId()+"_"+currentOrgan.getId(), moeenTafsiliMap);
		}
		return moeenTafsiliMap;
	}
	////////////////moeen tafsili map//////////////////////////////////////////

	////////////////moeen accountingMarkaz map//////////////////////////////////////////
	static Map<String, Map<Long, List<ListOrderedMap<String, Object>>>> organizationalMoeenAccountingMarkazMap = new HashMap<String, Map<Long,List<ListOrderedMap<String, Object>>>>();
	
	public static void resetAccountingMarkazMap(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan){
		organizationalMoeenAccountingMarkazMap.put(saalMaaliEntity.getId()+"_"+currentOrgan.getId(), null);
	}
	
	public static Map<Long, List<ListOrderedMap<String, Object>>> getAccountingMarkazMap(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan) {
		
		Map<Long, List<ListOrderedMap<String, Object>>> moeenAccountingMarkazMap = organizationalMoeenAccountingMarkazMap.get(saalMaaliEntity.getId()+"_"+currentOrgan.getId());
		
		if(moeenAccountingMarkazMap == null){
			List<HesabMoeenEntity> moeenList = getHesabMoeenService().getActiveMoeens(saalMaaliEntity, currentOrgan);
			moeenAccountingMarkazMap = new HashMap<Long, List<ListOrderedMap<String, Object>>>();
			for (HesabMoeenEntity hesabMoeenEntity : moeenList) {
				List<ListOrderedMap<String, Object>> accountingMarkazList = new ArrayList<ListOrderedMap<String, Object>>();
				for (AccountingMarkazEntity accountingMarkazEntity : hesabMoeenEntity.getAccountingMarkazList()) {
					if(accountingMarkazEntity.getHidden().equals(false)){
						ListOrderedMap<String, Object> accountingMarkazItemMap = new ListOrderedMap<String, Object>();
						accountingMarkazItemMap.put("value",accountingMarkazEntity.getID());
						accountingMarkazItemMap.put("label",accountingMarkazEntity.getDesc());
						accountingMarkazList.add(accountingMarkazItemMap);
					}
				}
				moeenAccountingMarkazMap.put(hesabMoeenEntity.getId(), accountingMarkazList);
			}
			organizationalMoeenAccountingMarkazMap.put(saalMaaliEntity.getId()+"_"+currentOrgan.getId(), moeenAccountingMarkazMap);
		}
		return moeenAccountingMarkazMap;
	}
	////////////////moeen accountingMarkaz map//////////////////////////////////////////
	
	////////////////tafsili child map//////////////////////////////////////////
	
	public static Map<String, Map<Long, List<ListOrderedMap<String, Object>>>> organizationalTafsiliChildMap = new HashMap<String, Map<Long,List<ListOrderedMap<String, Object>>>>();
	
	public static void resetTafsiliChildMap(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan){
		organizationalTafsiliChildMap.put(saalMaaliEntity.getId()+"_"+currentOrgan.getId(), null);
	}
	
	public static Map<Long, List<ListOrderedMap<String, Object>>> getTafsiliChildMap(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan) {

		Map<Long, List<ListOrderedMap<String, Object>>> tafsiliChildMap = organizationalTafsiliChildMap.get(saalMaaliEntity.getId()+"_"+currentOrgan.getId());
		if(tafsiliChildMap == null){
			tafsiliChildMap = new HashMap<Long, List<ListOrderedMap<String, Object>>>();
			List<HesabTafsiliEntity> tafsiliList = getHesabTafsiliService().getActiveTafsilis(saalMaaliEntity, currentOrgan);
			for (HesabTafsiliEntity hesabTafsiliEntity : tafsiliList) {
				List<ListOrderedMap<String, Object>> hesabTafsiliList = new ArrayList<ListOrderedMap<String, Object>>();
				for (HesabTafsiliEntity tafsiliEntity : hesabTafsiliEntity.getChilds()) {
					if(tafsiliEntity.getHidden().equals(false)){
						ListOrderedMap<String, Object> bankBranchItemMap = new ListOrderedMap<String, Object>();
						bankBranchItemMap.put("value",tafsiliEntity.getID());
						bankBranchItemMap.put("label",tafsiliEntity.getDesc());
						hesabTafsiliList.add(bankBranchItemMap);
					}
				}
				tafsiliChildMap.put(hesabTafsiliEntity.getId(), hesabTafsiliList);
			}
			
			organizationalTafsiliChildMap.put(saalMaaliEntity.getId()+"_"+currentOrgan.getId(), tafsiliChildMap);
		}
		return tafsiliChildMap;
	}	
	////////////////tafsili child map//////////////////////////////////////////

	////////////////tafsili accounting markaz map//////////////////////////////////////////
	
	public static Map<String, Map<Long, List<ListOrderedMap<String, Object>>>> organizationalTafsiliMap = new HashMap<String, Map<Long,List<ListOrderedMap<String, Object>>>>();
	
	public static void resetTafsiliAccountingMarkazChildMap(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan){
		organizationalTafsiliMap.put(saalMaaliEntity.getId()+"_"+currentOrgan.getId(), null);
	}
	
	public static Map<Long, List<ListOrderedMap<String, Object>>> getTafsiliAccountingMarkazChildMap(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan) {
		
		Map<Long, List<ListOrderedMap<String, Object>>> tafsiliMap = organizationalTafsiliMap.get(saalMaaliEntity.getId()+"_"+currentOrgan.getId());
		if(tafsiliMap == null){
			tafsiliMap = new HashMap<Long, List<ListOrderedMap<String, Object>>>();
			List<HesabTafsiliEntity> tafsiliList = getHesabTafsiliService().getActiveTafsilis(saalMaaliEntity, currentOrgan);
			for (HesabTafsiliEntity hesabTafsiliEntity : tafsiliList) {
				List<ListOrderedMap<String, Object>> accountingMarkazList = new ArrayList<ListOrderedMap<String, Object>>();
				for (AccountingMarkazEntity accountingMarkazEntity : hesabTafsiliEntity.getChildAccountingMarkaz()) {
					if(accountingMarkazEntity.getHidden().equals(false)){
						ListOrderedMap<String, Object> accountingMarkazItemMap = new ListOrderedMap<String, Object>();
						accountingMarkazItemMap.put("value",accountingMarkazEntity.getID());
						accountingMarkazItemMap.put("label",accountingMarkazEntity.getDesc());
						accountingMarkazList.add(accountingMarkazItemMap);
					}
				}
				tafsiliMap.put(hesabTafsiliEntity.getId(), accountingMarkazList);
			}
			organizationalTafsiliMap.put(saalMaaliEntity.getId()+"_"+currentOrgan.getId(), tafsiliMap);
		}
		
		return tafsiliMap;
	}	
	////////////////tafsili accounting markaz map//////////////////////////////////////////
	
	//////////////// accounting markaz child map//////////////////////////////////////////

	static Map<String, Map<Long, List<ListOrderedMap<String, Object>>>> organizationalAccountingMarkazChildMap = new HashMap<String, Map<Long,List<ListOrderedMap<String, Object>>>>();
	
	public static void resetAccountingMarkazChildMap(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan){
		organizationalAccountingMarkazChildMap.put(saalMaaliEntity.getId()+"_"+currentOrgan.getId(), null);
	}
	
	public static Map<Long, List<ListOrderedMap<String, Object>>> getAccountingMarkazChildMap(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan) {

		Map<Long, List<ListOrderedMap<String, Object>>> accountingMarkazChildMap = organizationalAccountingMarkazChildMap.get(saalMaaliEntity.getId()+"_"+currentOrgan.getId());
		if(accountingMarkazChildMap == null){
			accountingMarkazChildMap = new HashMap<Long, List<ListOrderedMap<String, Object>>>();
			List<AccountingMarkazEntity> accountingMarkazList = getAccountingMarkazService().getActiveAccountingMarkaz(saalMaaliEntity, currentOrgan);
			for (AccountingMarkazEntity accountingMarkazEntity : accountingMarkazList) {
				List<ListOrderedMap<String, Object>> localAccountingMarkazList = new ArrayList<ListOrderedMap<String, Object>>(); 
				for (AccountingMarkazEntity localAccountingMarkazEntity : accountingMarkazEntity.getChilds()) {
					if(localAccountingMarkazEntity.getHidden().equals(false)){
						ListOrderedMap<String, Object> bankBranchItemMap = new ListOrderedMap<String, Object>();
						bankBranchItemMap.put("value",localAccountingMarkazEntity.getID());
						bankBranchItemMap.put("label",localAccountingMarkazEntity.getDesc());
						localAccountingMarkazList.add(bankBranchItemMap);
					}
				}
				accountingMarkazChildMap.put(accountingMarkazEntity.getId(), localAccountingMarkazList);
			}
			organizationalAccountingMarkazChildMap.put(saalMaaliEntity.getId()+"_"+currentOrgan.getId(), accountingMarkazChildMap);
		}

		return accountingMarkazChildMap;
	}	
	//////////////// accounting markaz child map//////////////////////////////////////////

	static Map<String, List<ListOrderedMap<String, Object>>> organizationalRootHesabsMap = new HashMap<String, List<ListOrderedMap<String, Object>>>();
	public static void resetRootHesabsMap(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan){
		organizationalRootHesabsMap.put(saalMaaliEntity.getId()+"_"+currentOrgan.getId(), null);
	}
	public static List<ListOrderedMap<String, Object>> getRootHesabs(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan){
		List<ListOrderedMap<String, Object>> rootHesabsList = organizationalRootHesabsMap.get(saalMaaliEntity.getId()+"_"+currentOrgan.getId());
		
		if(rootHesabsList == null){
			rootHesabsList = getHesabKolService().getRootHesabs(saalMaaliEntity, currentOrgan);
			
			organizationalRootHesabsMap.put(saalMaaliEntity.getId()+"_"+currentOrgan.getId(), rootHesabsList);
		}
		return rootHesabsList;
	}



}
