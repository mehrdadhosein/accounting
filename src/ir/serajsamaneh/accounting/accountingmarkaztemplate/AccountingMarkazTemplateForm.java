package ir.serajsamaneh.accounting.accountingmarkaztemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;

import org.apache.commons.collections4.map.ListOrderedMap;

import ir.serajsamaneh.accounting.base.BaseAccountingForm;
import ir.serajsamaneh.accounting.exception.NoSaalMaaliFoundException;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
import ir.serajsamaneh.accounting.moeenaccountingmarkaztemplate.MoeenAccountingMarkazTemplateEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.erpcore.util.HesabRelationsUtil;
import ir.serajsamaneh.erpcore.util.HesabTemplateRelationsUtil;


public class AccountingMarkazTemplateForm extends BaseAccountingForm<AccountingMarkazTemplateEntity,Long> {




	@Override
	protected AccountingMarkazTemplateService getMyService() {
		return accountingMarkazTemplateService;
	}
	
	SaalMaaliService saalMaaliService;


	public SaalMaaliService getSaalMaaliService() {
		return saalMaaliService;
	}

	public void setSaalMaaliService(SaalMaaliService saalMaaliService) {
		this.saalMaaliService = saalMaaliService;
	}

	
	public DataModel<AccountingMarkazTemplateEntity> getGlobalDataModel() {
		setSearchAction(true);
		return getDataModel();
	}
		
	HesabMoeenTemplateService hesabMoeenTemplateService;
	
	public HesabMoeenTemplateService getHesabMoeenTemplateService() {
		return hesabMoeenTemplateService;
	}

	public void setHesabMoeenTemplateService(
			HesabMoeenTemplateService hesabMoeenTemplateService) {
		this.hesabMoeenTemplateService = hesabMoeenTemplateService;
	}


	AccountingMarkazTemplateService accountingMarkazTemplateService;
	


	public AccountingMarkazTemplateService getAccountingMarkazTemplateService() {
		return accountingMarkazTemplateService;
	}

	public void setAccountingMarkazTemplateService(
			AccountingMarkazTemplateService accountingMarkazTemplateService) {
		this.accountingMarkazTemplateService = accountingMarkazTemplateService;
	}

	List<Long> moeenIds;
	List<Long> childAccountingMarkazIds;

	public List<Long> getChildAccountingMarkazIds() {
		if(childAccountingMarkazIds==null){
			childAccountingMarkazIds = new ArrayList<Long>();
			Set<AccountingMarkazTemplateEntity> accountingMarkazes = getEntity().getChilds();
			if(accountingMarkazes!=null){
				for (AccountingMarkazTemplateEntity accountingMarkazEntity : accountingMarkazes) {
					childAccountingMarkazIds.add(accountingMarkazEntity.getId());
				}
			}
		}
		return childAccountingMarkazIds;
	}


	public void setChildAccountingMarkazIds(List<Long> childAccountingMarkazIds) {
		this.childAccountingMarkazIds = childAccountingMarkazIds;
	}


	public List<Long> getMoeenIds() {
		if(moeenIds==null){
			moeenIds = new ArrayList<Long>();
			List<HesabMoeenTemplateEntity> moeens = getEntity().getHesabMoeenList();
			if(moeens!=null){
				for (HesabMoeenTemplateEntity moeen : moeens) {
					moeenIds.add(moeen.getId());
				}
			}
		}
		return moeenIds;
	}

	public void setMoeenIds(List<Long> moeenIds) {
		this.moeenIds = moeenIds;
	}
	@Override
	public String save() {
		getEntity().setOrgan(getCurrentOrgan()); 
		getMyService().save(getEntity(), getMoeenIds(), getChildAccountingMarkazIds(),getCurrentOrgan());
		HesabRelationsUtil.resetAccountingMarkazMap(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		HesabRelationsUtil.resetTafsiliAccountingMarkazChildMap(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		HesabRelationsUtil.resetAccountingMarkazChildMap(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		return getViewUrl();
	}
	
	
	public String localSave() {
		getEntity().setOrgan(getCurrentOrgan());
		save();
		return getLocalViewUrl();
	}
	
	public DataModel<AccountingMarkazTemplateEntity> getActiveDataModel() {
		getFilter().put("hidden@eq", Boolean.FALSE); 
		getFilter().put("hesabMoeenTemplate.hidden@eq", Boolean.FALSE);
		return super.getLocalDataModel();
	}
	
	public List<SelectItem> getHesabMoeenSelectItems(){
		List<HesabMoeenEntity> moeenList = getHesabMoeenService().getDataList();
		List<SelectItem> resultList = new ArrayList<SelectItem>();
				
		for (HesabMoeenEntity hesabMoeenEntity: moeenList){
			resultList.add(new SelectItem(hesabMoeenEntity.getId(), hesabMoeenEntity.getName()));
		}
		
		return resultList;
	}

	@Override
	public List<? extends BaseEntity> getJsonList(String property, String term,
			boolean all, Map<String, String> params) {
		
		try{
			String isHierarchical = params.get("isHierarchical");
			
			if (isHierarchical !=null && isHierarchical.equals("true")){
				this.getFilter().put("organ.code@startlk", getCurrentUserActiveSaalMaali().getOrgan().getCode());
				params.put("isLocal","false");
			}
			
			return super.getJsonList(property, term, all, params);
		}catch(NoSaalMaaliFoundException e){
			//e.printStackTrace();
			System.out.println(e.getMessage());
			return new ArrayList<>();
		}
	}

	public Map<Long, List<ListOrderedMap<String, Object>>> getAccountingMarkazChildMap() {
		return HesabRelationsUtil.getAccountingMarkazChildMap(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
	}

	public Map<Long, List<ListOrderedMap<String, Object>>> getAccountingMarkazChildTemplateMap() {
		return HesabTemplateRelationsUtil.getAccountingMarkazChildTemplateMap(getCurrentOrgan());
	}
	

	
	static Map<Long, List<ListOrderedMap<String, Object>>> accountingMarkazMoeenMap;

	public  void resetAccountingMarkazMoeenMap() {
		accountingMarkazMoeenMap = null;
	}
	public Map<Long, List<ListOrderedMap<String, Object>>> getAccountingMarkazMoeenMap() {
		//if (accountingMarkazMoeenMap == null) {
			accountingMarkazMoeenMap = new HashMap<Long, List<ListOrderedMap<String, Object>>>();

			List<AccountingMarkazTemplateEntity> list = getMyService().getActiveAccountingMarkazTemplates(getCurrentOrgan());
			for (AccountingMarkazTemplateEntity accountingMarkazEntity : list) {
				Set<MoeenAccountingMarkazTemplateEntity> moeenAccountingMarkazSet = accountingMarkazEntity.getMoeenAccountingMarkazTemplate();
				List<ListOrderedMap<String, Object>> moeenAccountingMarkazList = new ArrayList<ListOrderedMap<String, Object>>();
				for (MoeenAccountingMarkazTemplateEntity moeenAccountingMarkazEntityEntity : moeenAccountingMarkazSet) {
					ListOrderedMap<String, Object> moeenItemMap = new ListOrderedMap<String, Object>();
					moeenItemMap.put("value",moeenAccountingMarkazEntityEntity.getHesabMoeenTemplate().getID());
					moeenItemMap.put("label",moeenAccountingMarkazEntityEntity.getHesabMoeenTemplate().getDesc());
					moeenAccountingMarkazList.add(moeenItemMap);
				}
				accountingMarkazMoeenMap.put(accountingMarkazEntity.getId(),moeenAccountingMarkazList);
			}

		//}
		return accountingMarkazMoeenMap;
	}

	
}