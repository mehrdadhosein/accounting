package ir.serajsamaneh.accounting.hesabtafsili;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity;
import ir.serajsamaneh.accounting.base.BaseAccountingForm;
import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.contact.contact.ContactService;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.core.util.SpringUtils;
import ir.serajsamaneh.erpcore.contacthesab.ContactHesabService;
import ir.serajsamaneh.erpcore.util.HesabRelationsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.util.StringUtils;


public class HesabTafsiliForm extends BaseAccountingForm<HesabTafsiliEntity,Long> {




	@Override
	protected HesabTafsiliService getMyService() {
		return hesabTafsiliService;
	}
	
	SaalMaaliService saalMaaliService;
	ContactService contactService;
	ContactHesabService contactHesabService;


	public ContactHesabService getContactHesabService() {
		return contactHesabService;
	}

	public void setContactHesabService(ContactHesabService contactHesabService) {
		this.contactHesabService = contactHesabService;
	}

	public ContactService getContactService() {
		return contactService;
	}

	public void setContactService(ContactService contactService) {
		this.contactService = contactService;
	}

	public SaalMaaliService getSaalMaaliService() {
		return saalMaaliService;
	}

	public void setSaalMaaliService(SaalMaaliService saalMaaliService) {
		this.saalMaaliService = saalMaaliService;
	}

	public DataModel<HesabTafsiliEntity> getLocalDataModel() {
		setSearchAction(true);
		getFilter().put("organ.code@startlk", getTopOrgan().getCode());
		getFilter().put("saalMaali.id@eq",getCurrentUserActiveSaalMaali().getId());
		return getDataModel();
	}

	@Override
	public DataModel<HesabTafsiliEntity> getHierarchicalDataModel() {
		setSearchAction(true);
		getFilter().put("saalMaali.id@eq",getCurrentUserActiveSaalMaali().getId());
		return super.getHierarchicalDataModel();
	}
	
	@Override
	public Integer getHierarchicalDataModelCount() {
		getFilter().put("saalMaali.id@eq",getCurrentUserActiveSaalMaali().getId());
		return super.getHierarchicalDataModelCount();
	}
	
	
	public DataModel<HesabTafsiliEntity> getGlobalDataModel() {
		setSearchAction(true);
		return super.getDataModel();
	}
		



	HesabMoeenService hesabMoeenService;
	
	public HesabMoeenService getHesabMoeenService() {
		return 	SpringUtils.getBean("hesabMoeenService");      
	}
	HesabTafsiliService hesabTafsiliService;
	
	public void setHesabTafsiliService(HesabTafsiliService hesabTafsiliService) {
		this.hesabTafsiliService = hesabTafsiliService;
	}
	
	public HesabTafsiliService getHesabTafsiliService() {
		return hesabTafsiliService;
	}

	List<Long> moeenIds;
	List<Long> childTafsiliIds;
	List<Long> childAccountingMarkazIds;

	public List<Long> getChildAccountingMarkazIds() {
		if(childAccountingMarkazIds==null){
			childAccountingMarkazIds = new ArrayList<Long>();
			Set<AccountingMarkazEntity> childAccountingMarkazSet = getEntity().getChildAccountingMarkaz();
			if(childAccountingMarkazSet!=null){
				for (AccountingMarkazEntity accountingMarkazEntity : childAccountingMarkazSet) {
					childAccountingMarkazIds.add(accountingMarkazEntity.getId());
				}
			}
		}
		return childAccountingMarkazIds;
	}

	public void setChildAccountingMarkazIds(List<Long> childAccountingMarkazIds) {
		this.childAccountingMarkazIds = childAccountingMarkazIds;
	}

	public List<Long> getChildTafsiliIds() {
		if(childTafsiliIds==null){
			childTafsiliIds = new ArrayList<Long>();
			Set<HesabTafsiliEntity> tafsilies = getEntity().getChilds();
			if(tafsilies!=null){
				for (HesabTafsiliEntity tafsiliEntity : tafsilies) {
					childTafsiliIds.add(tafsiliEntity.getId());
				}
			}
		}
		return childTafsiliIds;
	}


	public void setChildTafsiliIds(List<Long> childTafsiliIds) {
		this.childTafsiliIds = childTafsiliIds;
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

	public void setMoeenIds(List<Long> moeenIds) {
		this.moeenIds = moeenIds;
	}
	@Override
	public String save() {
		getEntity().setOrgan(getCurrentOrgan()); 
		getMyService().save(getEntity(), getMoeenIds(), getChildTafsiliIds(), getChildAccountingMarkazIds(),getCurrentUserActiveSaalMaali());
		HesabRelationsUtil.resetTafsiliMoeenMap(getCurrentUserActiveSaalMaali());
		HesabRelationsUtil.resetmoeenTafsiliMap(getCurrentUserActiveSaalMaali());
		HesabRelationsUtil.resetTafsiliChildMap(getCurrentUserActiveSaalMaali());
		HesabRelationsUtil.resetTafsiliAccountingMarkazChildMap(getCurrentUserActiveSaalMaali());
		return getViewUrl();
	}
	
	
	public String localSave() {
		getEntity().setOrgan(getCurrentOrgan());
		save();
		return getLocalViewUrl();
	}
	
	public DataModel<HesabTafsiliEntity> getActiveDataModel() {
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
	public List<SelectItem> getLocalHesabTafsiliSelectItems(){
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("organ.id@eq", getCurrentOrgan().getId());
		List<HesabTafsiliEntity>  list = getMyService().getDataList(null,filter);
		List<SelectItem> resultList = new ArrayList<SelectItem>();
		for (HesabTafsiliEntity entity: list){
			resultList.add(new SelectItem(entity.getId(), entity.getName()));
		}
		
		return resultList;
	}
	@Override
	public List<? extends BaseEntity> getJsonList(String property, String term,
			boolean all, Map<String, String> params) {
		String isLocalUser = params.get("isCurrentOrgan");
		
		this.getFilter().put("saalMaali.id@eq",getCurrentUserActiveSaalMaali().getId());
		
		if (isLocalUser !=null && isLocalUser.equals("true")){
			getFilter().put("organ.id@eq",getCurrentOrgan().getId());
		}

		String isHierarchical = params.get("isHierarchical");
		String hidden = params.get("hidden");
		
		if(StringUtils.hasText(hidden) && hidden.equals("false")){
			this.getFilter().put("hidden@eq",false);
		}
		
		if (isHierarchical !=null && isHierarchical.equals("true")){
			this.getFilter().put("organ.code@startlk", getCurrentUserActiveSaalMaali().getOrgan().getCode());
			params.put("isLocal","false");
		}
		
		return super.getJsonList(property, term, all, params);
	}
	
	public Map<Long, List<ListOrderedMap>> getTafsiliMoeenMap() {
		return HesabRelationsUtil.getTafsiliMoeenMap(getCurrentUserActiveSaalMaali());
	}
	
	public Map<Long, List<ListOrderedMap>> getTafsiliChildMap() {
		return HesabRelationsUtil.getTafsiliChildMap(getCurrentUserActiveSaalMaali());
	}
	
	public Map<Long, List<ListOrderedMap>> getTafsiliAccountingMarkazChildMap() {
		return HesabRelationsUtil.getTafsiliAccountingMarkazChildMap(getCurrentUserActiveSaalMaali());
	}

	public String importFromHesabTafsiliTemplateList(){
		try{
		getMyService().importFromHesabTafsiliTemplateList(getCurrentUserActiveSaalMaali());
		setDataModel(null);
		return null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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