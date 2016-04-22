package ir.serajsamaneh.erpcore.util;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity;
import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazService;
import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.hesabkol.HesabKolService;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliService;
import ir.serajsamaneh.accounting.moeentafsili.MoeenTafsiliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.util.SpringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.ListOrderedMap;

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
	static Map<Long, Map<Long, List<ListOrderedMap>>> organizationalTafsiliMoeenMap = new HashMap<Long, Map<Long,List<ListOrderedMap>>>();
	

	public static void resetTafsiliMoeenMap(SaalMaaliEntity saalMaaliEntity) {
		organizationalTafsiliMoeenMap.put(saalMaaliEntity.getId(), null);
	}
	public static Map<Long, List<ListOrderedMap>> getTafsiliMoeenMap(SaalMaaliEntity saalMaaliEntity) {
		Map<Long, List<ListOrderedMap>> tafsiliMoeenMap = organizationalTafsiliMoeenMap.get(saalMaaliEntity.getId());
		if (tafsiliMoeenMap == null) {
			tafsiliMoeenMap = new HashMap<Long, List<ListOrderedMap>>();

			List<HesabTafsiliEntity> list = getHesabTafsiliService().getActiveTafsilis(saalMaaliEntity);
			for (HesabTafsiliEntity hesabTafsiliEntity : list) {
				Set<MoeenTafsiliEntity> moeenTafsiliSet = hesabTafsiliEntity.getMoeenTafsili();
				List<ListOrderedMap> hesabMoeenList = new ArrayList<ListOrderedMap>();
				for (MoeenTafsiliEntity moeenTafsiliEntity : moeenTafsiliSet) {
					ListOrderedMap moeenItemMap = new ListOrderedMap();
					moeenItemMap.put("value",moeenTafsiliEntity.getHesabMoeen().getID());
					moeenItemMap.put("label",moeenTafsiliEntity.getHesabMoeen().getDesc());
					hesabMoeenList.add(moeenItemMap);
				}
				tafsiliMoeenMap.put(hesabTafsiliEntity.getId(),hesabMoeenList);
			}
			organizationalTafsiliMoeenMap.put(saalMaaliEntity.getId(), tafsiliMoeenMap);
		}
		return tafsiliMoeenMap;
	}
	//////////////////tafsili moeen map//////////////////////////////////////////
	
	//////////////////kol moeen map//////////////////////////////////////////
	static Map<Long, Map<Long, List<ListOrderedMap>>> organizationalKolMoeenMap = new HashMap<Long, Map<Long,List<ListOrderedMap>>>();
	

	public static void resetKolMoeenMap(SaalMaaliEntity saalMaaliEntity) {
		organizationalKolMoeenMap.put(saalMaaliEntity.getId(), null);
	}
	
	public static Map<Long, List<ListOrderedMap>> getKolMoeenMap(SaalMaaliEntity saalMaaliEntity) {
		Map<Long, List<ListOrderedMap>> kolMoeenMap = organizationalKolMoeenMap.get(saalMaaliEntity.getId());
		if (kolMoeenMap == null) {
			kolMoeenMap = new HashMap<Long, List<ListOrderedMap>>();

			List<HesabKolEntity> list = getHesabKolService().getHesabKolList(saalMaaliEntity);
			for (HesabKolEntity hesabKolEntity : list) {
				List<HesabMoeenEntity> moeenList = getHesabMoeenService().getActiveMoeens(hesabKolEntity.getId(), saalMaaliEntity);
				
				 List<ListOrderedMap> moeenOrderedList = new ArrayList<ListOrderedMap>();
				 for(HesabMoeenEntity moeenEntity : moeenList){
					 ListOrderedMap moeentemMap = new ListOrderedMap();
					 moeentemMap.put("value",moeenEntity.getID());
					 moeentemMap.put("label",moeenEntity.getDesc());
					 moeenOrderedList.add(moeentemMap);
				 }
				kolMoeenMap.put(hesabKolEntity.getId(),moeenOrderedList);
			}
			organizationalKolMoeenMap.put(saalMaaliEntity.getId(), kolMoeenMap);
		}
		return kolMoeenMap;
	}
	
	////////////////kol moeen map//////////////////////////////////////////

	////////////////moeen kol map//////////////////////////////////////////
	
	static Map<Long, Map<Long, ListOrderedMap>> organizationalMoeenKolMap = new HashMap<Long, Map<Long,ListOrderedMap>>();
	

	public static void resetMoeenKolMap(SaalMaaliEntity saalMaaliEntity) {
		organizationalMoeenKolMap.put(saalMaaliEntity.getId(), null);
	}
	
	public static Map<Long, ListOrderedMap> getMoeenKolMap(SaalMaaliEntity saalMaaliEntity) {
		Map<Long, ListOrderedMap> moeenKolMap = organizationalMoeenKolMap.get(saalMaaliEntity.getId());
		
		if (moeenKolMap == null) {
			moeenKolMap = new HashMap<Long, ListOrderedMap>();

			List<HesabMoeenEntity> list = getHesabMoeenService().getActiveMoeens(saalMaaliEntity);
			for (HesabMoeenEntity hesabMoeenEntity : list) {
				ListOrderedMap moeenItemMap = new ListOrderedMap();
				moeenItemMap.put("value",hesabMoeenEntity.getHesabKol().getID());
				moeenItemMap.put("label",hesabMoeenEntity.getHesabKol().getDesc());
				moeenKolMap.put(hesabMoeenEntity.getId(),moeenItemMap);
			}
			organizationalMoeenKolMap.put(saalMaaliEntity.getId(), moeenKolMap);
		}
		return moeenKolMap;
	}
	////////////////moeen kol map//////////////////////////////////////////

	////////////////moeen tafsili map//////////////////////////////////////////

	static Map<Long, Map<Long, List<ListOrderedMap>>> organizationalMoeenTafsiliMap = new HashMap<Long, Map<Long,List<ListOrderedMap>>>();
	
	
	public static void resetmoeenTafsiliMap(SaalMaaliEntity saalMaaliEntity){
		organizationalMoeenTafsiliMap.put(saalMaaliEntity.getId(), null);
	}
	
	public static Map<Long, List<ListOrderedMap>> getMoeenTafsiliMap(SaalMaaliEntity saalMaaliEntity) {
		
		Map<Long, List<ListOrderedMap>> moeenTafsiliMap = organizationalMoeenTafsiliMap.get(saalMaaliEntity.getId());
		
		if(moeenTafsiliMap == null){
			List<HesabMoeenEntity> moeenList = getHesabMoeenService().getActiveMoeens(saalMaaliEntity);
			moeenTafsiliMap = new HashMap<Long, List<ListOrderedMap>>();
			for (HesabMoeenEntity hesabMoeenEntity : moeenList) {
				List<ListOrderedMap> hesabTafsiliList = new ArrayList<ListOrderedMap>();
				for (HesabTafsiliEntity tafsiliEntity : hesabMoeenEntity.getTafsiliList()) {
					if(tafsiliEntity.getHidden().equals(false)){
						ListOrderedMap bankBranchItemMap = new ListOrderedMap();
						bankBranchItemMap.put("value",tafsiliEntity.getID());
						bankBranchItemMap.put("label",tafsiliEntity.getDesc());
						hesabTafsiliList.add(bankBranchItemMap);
					}
				}
				moeenTafsiliMap.put(hesabMoeenEntity.getId(), hesabTafsiliList);
			}
			organizationalMoeenTafsiliMap.put(saalMaaliEntity.getId(), moeenTafsiliMap);
		}
		return moeenTafsiliMap;
	}
	////////////////moeen tafsili map//////////////////////////////////////////

	////////////////moeen accountingMarkaz map//////////////////////////////////////////
	static Map<Long, Map<Long, List<ListOrderedMap>>> organizationalMoeenAccountingMarkazMap = new HashMap<Long, Map<Long,List<ListOrderedMap>>>();
	
	public static void resetAccountingMarkazMap(SaalMaaliEntity saalMaaliEntity){
		organizationalMoeenAccountingMarkazMap.put(saalMaaliEntity.getId(), null);
	}
	
	public static Map<Long, List<ListOrderedMap>> getAccountingMarkazMap(SaalMaaliEntity saalMaaliEntity) {
		
		Map<Long, List<ListOrderedMap>> moeenAccountingMarkazMap = organizationalMoeenAccountingMarkazMap.get(saalMaaliEntity.getId());
		
		if(moeenAccountingMarkazMap == null){
			List<HesabMoeenEntity> moeenList = getHesabMoeenService().getActiveMoeens(saalMaaliEntity);
			moeenAccountingMarkazMap = new HashMap<Long, List<ListOrderedMap>>();
			for (HesabMoeenEntity hesabMoeenEntity : moeenList) {
				List<ListOrderedMap> accountingMarkazList = new ArrayList<ListOrderedMap>();
				for (AccountingMarkazEntity accountingMarkazEntity : hesabMoeenEntity.getAccountingMarkazList()) {
					if(accountingMarkazEntity.getHidden().equals(false)){
						ListOrderedMap accountingMarkazItemMap = new ListOrderedMap();
						accountingMarkazItemMap.put("value",accountingMarkazEntity.getID());
						accountingMarkazItemMap.put("label",accountingMarkazEntity.getDesc());
						accountingMarkazList.add(accountingMarkazItemMap);
					}
				}
				moeenAccountingMarkazMap.put(hesabMoeenEntity.getId(), accountingMarkazList);
			}
			organizationalMoeenAccountingMarkazMap.put(saalMaaliEntity.getId(), moeenAccountingMarkazMap);
		}
		return moeenAccountingMarkazMap;
	}
	////////////////moeen accountingMarkaz map//////////////////////////////////////////
	
	////////////////tafsili child map//////////////////////////////////////////
	
	public static Map<Long, Map<Long, List<ListOrderedMap>>> organizationalTafsiliChildMap = new HashMap<Long, Map<Long,List<ListOrderedMap>>>();
	
	public static void resetTafsiliChildMap(SaalMaaliEntity saalMaaliEntity){
		organizationalTafsiliChildMap.put(saalMaaliEntity.getId(), null);
	}
	
	public static Map<Long, List<ListOrderedMap>> getTafsiliChildMap(SaalMaaliEntity saalMaaliEntity) {

		Map<Long, List<ListOrderedMap>> tafsiliChildMap = organizationalTafsiliChildMap.get(saalMaaliEntity.getId());
		if(tafsiliChildMap == null){
			tafsiliChildMap = new HashMap<Long, List<ListOrderedMap>>();
			List<HesabTafsiliEntity> tafsiliList = getHesabTafsiliService().getActiveTafsilis(saalMaaliEntity);
			for (HesabTafsiliEntity hesabTafsiliEntity : tafsiliList) {
				List<ListOrderedMap> hesabTafsiliList = new ArrayList<ListOrderedMap>();
				for (HesabTafsiliEntity tafsiliEntity : hesabTafsiliEntity.getChilds()) {
					if(tafsiliEntity.getHidden().equals(false)){
						ListOrderedMap bankBranchItemMap = new ListOrderedMap();
						bankBranchItemMap.put("value",tafsiliEntity.getID());
						bankBranchItemMap.put("label",tafsiliEntity.getDesc());
						hesabTafsiliList.add(bankBranchItemMap);
					}
				}
				tafsiliChildMap.put(hesabTafsiliEntity.getId(), hesabTafsiliList);
			}
			
			organizationalTafsiliChildMap.put(saalMaaliEntity.getId(), tafsiliChildMap);
		}
		return tafsiliChildMap;
	}	
	////////////////tafsili child map//////////////////////////////////////////

	////////////////tafsili accounting markaz map//////////////////////////////////////////
	
	public static Map<Long, Map<Long, List<ListOrderedMap>>> organizationalTafsiliMap = new HashMap<Long, Map<Long,List<ListOrderedMap>>>();
	
	public static void resetTafsiliAccountingMarkazChildMap(SaalMaaliEntity saalMaaliEntity){
		organizationalTafsiliMap.put(saalMaaliEntity.getId(), null);
	}
	
	public static Map<Long, List<ListOrderedMap>> getTafsiliAccountingMarkazChildMap(SaalMaaliEntity saalMaaliEntity) {
		
		Map<Long, List<ListOrderedMap>> tafsiliMap = organizationalTafsiliMap.get(saalMaaliEntity.getId());
		if(tafsiliMap == null){
			tafsiliMap = new HashMap<Long, List<ListOrderedMap>>();
			List<HesabTafsiliEntity> tafsiliList = getHesabTafsiliService().getActiveTafsilis(saalMaaliEntity);
			for (HesabTafsiliEntity hesabTafsiliEntity : tafsiliList) {
				List<ListOrderedMap> accountingMarkazList = new ArrayList<ListOrderedMap>();
				for (AccountingMarkazEntity accountingMarkazEntity : hesabTafsiliEntity.getChildAccountingMarkaz()) {
					if(accountingMarkazEntity.getHidden().equals(false)){
						ListOrderedMap accountingMarkazItemMap = new ListOrderedMap();
						accountingMarkazItemMap.put("value",accountingMarkazEntity.getID());
						accountingMarkazItemMap.put("label",accountingMarkazEntity.getDesc());
						accountingMarkazList.add(accountingMarkazItemMap);
					}
				}
				tafsiliMap.put(hesabTafsiliEntity.getId(), accountingMarkazList);
			}
			organizationalTafsiliMap.put(saalMaaliEntity.getId(), tafsiliMap);
		}
		
		return tafsiliMap;
	}	
	////////////////tafsili accounting markaz map//////////////////////////////////////////
	
	//////////////// accounting markaz child map//////////////////////////////////////////

	static Map<Long, Map<Long, List<ListOrderedMap>>> organizationalAccountingMarkazChildMap = new HashMap<Long, Map<Long,List<ListOrderedMap>>>();
	
	public static void resetAccountingMarkazChildMap(SaalMaaliEntity saalMaaliEntity){
		organizationalAccountingMarkazChildMap.put(saalMaaliEntity.getId(), null);
	}
	
	public static Map<Long, List<ListOrderedMap>> getAccountingMarkazChildMap(SaalMaaliEntity saalMaaliEntity) {

		Map<Long, List<ListOrderedMap>> accountingMarkazChildMap = organizationalAccountingMarkazChildMap.get(saalMaaliEntity.getId());
		if(accountingMarkazChildMap == null){
			accountingMarkazChildMap = new HashMap<Long, List<ListOrderedMap>>();
			List<AccountingMarkazEntity> accountingMarkazList = getAccountingMarkazService().getActiveAccountingMarkaz(saalMaaliEntity);
			for (AccountingMarkazEntity accountingMarkazEntity : accountingMarkazList) {
				List<ListOrderedMap> localAccountingMarkazList = new ArrayList<ListOrderedMap>(); 
				for (AccountingMarkazEntity localAccountingMarkazEntity : accountingMarkazEntity.getChilds()) {
					if(localAccountingMarkazEntity.getHidden().equals(false)){
						ListOrderedMap bankBranchItemMap = new ListOrderedMap();
						bankBranchItemMap.put("value",localAccountingMarkazEntity.getID());
						bankBranchItemMap.put("label",localAccountingMarkazEntity.getDesc());
						localAccountingMarkazList.add(bankBranchItemMap);
					}
				}
				accountingMarkazChildMap.put(accountingMarkazEntity.getId(), localAccountingMarkazList);
			}
			organizationalAccountingMarkazChildMap.put(saalMaaliEntity.getId(), accountingMarkazChildMap);
		}

		return accountingMarkazChildMap;
	}	
	//////////////// accounting markaz child map//////////////////////////////////////////
	
}
