package ir.serajsamaneh.accounting.accountingmarkaz;

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
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.moeenaccountingmarkaz.MoeenAccountingMarkazEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.core.util.SpringUtils;
import ir.serajsamaneh.erpcore.util.HesabRelationsUtil;
import ir.serajsamaneh.erpcore.util.HesabTemplateRelationsUtil;


public class AccountingMarkazForm extends BaseAccountingForm<AccountingMarkazEntity,Long> {




	@Override
	protected AccountingMarkazService getMyService() {
		return accountingMarkazService;
	}
	
	SaalMaaliService saalMaaliService;


	public SaalMaaliService getSaalMaaliService() {
		return saalMaaliService;
	}

	public void setSaalMaaliService(SaalMaaliService saalMaaliService) {
		this.saalMaaliService = saalMaaliService;
	}

	public DataModel<AccountingMarkazEntity> getLocalDataModel() {
		setSearchAction(true);
		this.getFilter().put("organ.id@eq",getCurrentOrgan().getId());
		this.getFilter().put("saalMaali.id@eq",getCurrentUserActiveSaalMaali().getId());
		return getDataModel();
	}
	
	public DataModel<AccountingMarkazEntity> getGlobalDataModel() {
		setSearchAction(true);
		return super.getDataModel();
	}
		



	HesabMoeenService hesabMoeenService;
	
	public HesabMoeenService getHesabMoeenService() {
		return 	SpringUtils.getBean("hesabMoeenService");      
	}
	AccountingMarkazService accountingMarkazService;
	
	public void setAccountingMarkazService(AccountingMarkazService accountingMarkazService) {
		this.accountingMarkazService = accountingMarkazService;
	}
	
	public AccountingMarkazService getAccountingMarkazService() {
		return accountingMarkazService;
	}

	List<Long> moeenIds;
	List childAccountingMarkazIds;

	public List getChildAccountingMarkazIds() {
		if(childAccountingMarkazIds==null){
			childAccountingMarkazIds = new ArrayList();
			Set<AccountingMarkazEntity> accountingMarkazes = getEntity().getChilds();
			if(accountingMarkazes!=null){
				for (AccountingMarkazEntity accountingMarkazEntity : accountingMarkazes) {
					childAccountingMarkazIds.add(accountingMarkazEntity.getId());
				}
			}
		}
		return childAccountingMarkazIds;
	}


	public void setChildAccountingMarkazIds(List childAccountingMarkazIds) {
		this.childAccountingMarkazIds = childAccountingMarkazIds;
	}


	public List<Long> getMoeenIds() {
		if(moeenIds==null){
			moeenIds = new ArrayList<Long>();
			List<HesabMoeenEntity> moeens = getEntity().getHesabMoeenList();
			if(moeens!=null){
				for (HesabMoeenEntity moeen : moeens) {
					moeenIds.add(moeen.getId());
				}
			}
		}
		return moeenIds;
	}

	public void setMoeenIds(List moeenIds) {
		this.moeenIds = moeenIds;
	}
	@Override
	public String save() {
		getEntity().setOrgan(getCurrentOrgan()); 
		getMyService().save(getEntity(), getMoeenIds(), getChildAccountingMarkazIds(),getCurrentUserActiveSaalMaali(), getApplyMoeenOnSubMarkaz());
		HesabRelationsUtil.resetAccountingMarkazMap(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		HesabRelationsUtil.resetTafsiliAccountingMarkazChildMap(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		HesabRelationsUtil.resetAccountingMarkazChildMap(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		addInfoMessage("SUCCESSFUL_ACTION");
		return getViewUrl();
	}
	
	Boolean applyMoeenOnSubMarkaz=false;
	public Boolean getApplyMoeenOnSubMarkaz() {
		return applyMoeenOnSubMarkaz;
	}

	public void setApplyMoeenOnSubMarkaz(Boolean applyMoeenOnSubMarkaz) {
		this.applyMoeenOnSubMarkaz = applyMoeenOnSubMarkaz;
	}

	public String localSave() {
		getEntity().setOrgan(getCurrentOrgan());
		save();
		return getLocalViewUrl();
	}
	
	public DataModel<AccountingMarkazEntity> getActiveDataModel() {
		getFilter().put("hidden@eq", Boolean.FALSE); 
		getFilter().put("hesabMoeen.hidden@eq", Boolean.FALSE);
		return super.getDataModel();
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
			getFilter().put("saalMaali.id@eq",getCurrentUserActiveSaalMaali().getId());
			
			if (isHierarchical !=null && isHierarchical.equals("true")){
				List<Long> topOrganList = getTopOrgansIdList(getCurrentOrgan());
				getFilter().put("organ.id@in", topOrganList);
				
//				this.getFilter().put("organ.code@startlk", getCurrentUserActiveSaalMaali().getOrgan().getCode());
				params.put("isLocal","false");
			}
			
			return super.getJsonList(property, term, all, params);
		}catch(NoSaalMaaliFoundException e){
			return new ArrayList<>();
		}		

		

	}

	public Map<Long, List<ListOrderedMap<String, Object>>> getAccountingMarkazChildMap() {
		return HesabRelationsUtil.getAccountingMarkazChildMap(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
	}

	public Map<Long, List<ListOrderedMap<String, Object>>> getAccountingMarkazChildTemplateMap() {
		return HesabTemplateRelationsUtil.getAccountingMarkazChildTemplateMap(getCurrentOrgan());
	}
	

	
	static Map<Long, List> accountingMarkazMoeenMap;

	public  void resetAccountingMarkazMoeenMap() {
		accountingMarkazMoeenMap = null;
	}
	public Map<Long, List> getAccountingMarkazMoeenMap() {
		//if (accountingMarkazMoeenMap == null) {
			accountingMarkazMoeenMap = new HashMap<Long, List>();

			List<AccountingMarkazEntity> list = getMyService().getActiveAccountingMarkaz(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
			for (AccountingMarkazEntity accountingMarkazEntity : list) {
				Set<MoeenAccountingMarkazEntity> moeenAccountingMarkazSet = accountingMarkazEntity.getMoeenAccountingMarkaz();
				List moeenAccountingMarkazList = new ArrayList();
				for (MoeenAccountingMarkazEntity moeenAccountingMarkazEntityEntity : moeenAccountingMarkazSet) {
					ListOrderedMap moeenItemMap = new ListOrderedMap();
					moeenItemMap.put("value",moeenAccountingMarkazEntityEntity.getHesabMoeen().getID());
					moeenItemMap.put("label",moeenAccountingMarkazEntityEntity.getHesabMoeen().getDesc());
					moeenAccountingMarkazList.add(moeenItemMap);
				}
				accountingMarkazMoeenMap.put(accountingMarkazEntity.getId(),moeenAccountingMarkazList);
			}

		//}
		return accountingMarkazMoeenMap;
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
		return getEntity().getOrgan().getId().equals(getCurrentOrgan().getId());
	}

}