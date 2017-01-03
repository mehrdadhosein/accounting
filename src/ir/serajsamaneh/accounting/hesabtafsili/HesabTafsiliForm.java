package ir.serajsamaneh.accounting.hesabtafsili;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.util.StringUtils;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity;
import ir.serajsamaneh.accounting.base.BaseAccountingForm;
import ir.serajsamaneh.accounting.exception.NoSaalMaaliFoundException;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.contact.contact.ContactService;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.core.util.SpringUtils;
import ir.serajsamaneh.erpcore.contacthesab.ContactHesabService;
import ir.serajsamaneh.erpcore.util.HesabRelationsUtil;


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
		
		populateTopOrgansIdListFilter();

//		getFilter().put("organ.code@startlk", getTopOrgan().getCode());
		getFilter().put("saalMaali.id@eq",getCurrentUserActiveSaalMaali().getId());
		return getDataModel();
	}

	@Override
	public DataModel<HesabTafsiliEntity> getUpsetDataModel() {
		setSearchAction(true);
//		populateTopOrgansIdListFilter();
		getFilter().put("saalMaali.id@eq",getCurrentUserActiveSaalMaali().getId());
		return super.getUpsetDataModel();
	}
	
	@Override
	public Integer getUpsetDataModelCount() {
		
		populateTopOrgansIdListFilter();
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
	List<Long> parentTafsiliIds;
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


	public List<Long> getParentTafsiliIds() {
		if(parentTafsiliIds==null){
			parentTafsiliIds = new ArrayList<Long>();
			Set<HesabTafsiliEntity> tafsilies = getEntity().getParents();
			if(tafsilies!=null){
				for (HesabTafsiliEntity tafsiliEntity : tafsilies) {
					parentTafsiliIds.add(tafsiliEntity.getId());
				}
			}
		}
		return parentTafsiliIds;
	}

	public void setParentTafsiliIds(List<Long> parentTafsiliIds) {
		this.parentTafsiliIds = parentTafsiliIds;
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
		getMyService().save(getEntity(), getMoeenIds(), getChildTafsiliIds(), getParentTafsiliIds(), getChildAccountingMarkazIds(),getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		resetHesabRelations();
		HesabRelationsUtil.resetTafsiliAccountingMarkazChildMap(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		addInfoMessage("SUCCESSFUL_ACTION");
		return getViewUrl();
	}
	
	private void resetHesabRelations() {
		List<Long> subsetOrganIds = getSubsetOrganIds(getCurrentOrgan());
		for (Long organId : subsetOrganIds) {
			OrganEntity organEntity = getOrganService().load(organId);
			HesabRelationsUtil.resetTafsiliMoeenMap(getCurrentUserActiveSaalMaali(), organEntity);
			HesabRelationsUtil.resetmoeenTafsiliMap(getCurrentUserActiveSaalMaali(), organEntity);
			HesabRelationsUtil.resetTafsiliChildMap(getCurrentUserActiveSaalMaali(), organEntity);
			HesabRelationsUtil.resetTafsiliAccountingMarkazChildMap(getCurrentUserActiveSaalMaali(), organEntity);
			HesabRelationsUtil.resetRootHesabsMap(getCurrentUserActiveSaalMaali(), organEntity);
		}
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
		try{
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
				List<Long> topOrganList = getTopOrgansIdList(getCurrentOrgan());
				getFilter().put("organ.id@in", topOrganList);
				
	//			this.getFilter().put("organ.code@startlk", getCurrentUserActiveSaalMaali().getOrgan().getCode());
				params.put("isLocal","false");
			}
			
			return super.getJsonList(property, term, all, params);
		}catch(NoSaalMaaliFoundException e){
			//e.printStackTrace();
			System.out.println(e.getMessage());
			return new ArrayList<>();
		}
	}
	
	public Map<Long, List<ListOrderedMap>> getTafsiliMoeenMap() {
		return HesabRelationsUtil.getTafsiliMoeenMap(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
	}
	
	public Map<Long, List<ListOrderedMap>> getTafsiliChildMap() {
		return HesabRelationsUtil.getTafsiliChildMap(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
	}
	
	public Map<Long, List<ListOrderedMap>> getTafsiliAccountingMarkazChildMap() {
		return HesabRelationsUtil.getTafsiliAccountingMarkazChildMap(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
	}

	public String importFromHesabTafsiliTemplateList(){
		try{
		getMyService().importFromHesabTafsiliTemplateList(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
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