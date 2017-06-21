package ir.serajsamaneh.accounting.hesabtafsilitemplate;

import ir.serajsamaneh.accounting.base.BaseAccountingForm;
import ir.serajsamaneh.accounting.exception.NoSaalMaaliFoundException;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.erpcore.util.HesabTemplateRelationsUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.model.DataModel;

import org.apache.commons.collections4.map.ListOrderedMap;

public class HesabTafsiliTemplateForm extends
		BaseAccountingForm<HesabTafsiliTemplateEntity, Long> {

	@Override
	protected HesabTafsiliTemplateService getMyService() {
		return hesabTafsiliTemplateService;
	}

	HesabTafsiliTemplateService hesabTafsiliTemplateService;

	public void setHesabTafsiliTemplateService(
			HesabTafsiliTemplateService hesabTafsiliTemplateService) {
		this.hesabTafsiliTemplateService = hesabTafsiliTemplateService;
	}

	public HesabTafsiliTemplateService getHesabTafsiliTemplateService() {
		return hesabTafsiliTemplateService;
	}

	public String localSave() {
		save();
		return getLocalViewUrl();
	}

	public DataModel<HesabTafsiliTemplateEntity> getLocalArchiveDataModel() {
		return getLocalDataModel();
	}

	public Boolean getIsCreated() {
		return null;
	}

	String moeenTafsiliInstanceIds;

	public String getMoeenTafsiliInstanceIds() {
		return moeenTafsiliInstanceIds;
	}

	public void setMoeenTafsiliInstanceIds(String moeenTafsiliInstanceIds) {
		this.moeenTafsiliInstanceIds = moeenTafsiliInstanceIds;
	}

	String parentIds;

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	String childsIds;

	public String getChildsIds() {
		return childsIds;
	}

	public void setChildsIds(String childsIds) {
		this.childsIds = childsIds;
	}

	List<Long> moeenIds;
	List<Long> childTafsiliIds;

	public List<Long> getChildTafsiliIds() {
		if (childTafsiliIds == null) {
			childTafsiliIds = new ArrayList<Long>();
			Set<HesabTafsiliTemplateEntity> tafsilies = getEntity().getChilds();
			if (tafsilies != null) {
				for (HesabTafsiliTemplateEntity tafsiliEntity : tafsilies) {
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
		if (moeenIds == null) {
			moeenIds = new ArrayList<Long>();
			List<HesabMoeenTemplateEntity> moeens = getEntity()
					.getHesabMoeenTemplateList();
			if (moeens != null) {
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
		getMyService().save(getEntity(), getMoeenIds(), getChildTafsiliIds(), getCurrentOrgan());
		return getViewUrl();
	}

	public Map<Long, List<ListOrderedMap>> getTafsiliMoeenTemplateMap() {
		return HesabTemplateRelationsUtil
				.getTafsiliMoeenTemplateMap(getCurrentOrgan());
	}

	public Map<Long, List<ListOrderedMap>> getTafsiliChildTemplateMap() {
		return HesabTemplateRelationsUtil
				.getTafsiliChildTemplateMap(getCurrentOrgan());
	}

	public Map<Long, List<ListOrderedMap>> getTafsiliAccountingMarkazChildTemplateMap() {
		return HesabTemplateRelationsUtil
				.getTafsiliAccountingMarkazChildTemplateMap(getCurrentOrgan());
	}

	@Override
	public List<? extends BaseEntity> getJsonList(String property, String term,
			boolean all, Map<String, String> params) {

		try{
			String isHierarchical = params.get("isHierarchical");
	
			// params.put("isLocal","false");
			// getFilter().put("organ.id@eqORorgan.id@isNull",Arrays.asList(getCurrentOrgan().getId(),
			// "ding"));
	
			if (isHierarchical != null && isHierarchical.equals("true")) {
				List<Long> topOrganList = getTopOrgansIdList(getCurrentUserActiveSaalMaali().getOrgan());
				getFilter().put("organ.id@in", topOrganList);
				
//				this.getFilter().put("organ.code@startlk",
//						getCurrentUserActiveSaalMaali().getOrgan().getCode());
				params.put("isLocal", "false");
			}
	
			return super.getJsonList(property, term, all, params);
		}catch(NoSaalMaaliFoundException e){
			//e.printStackTrace();
			System.out.println(e.getMessage());
			return new ArrayList<>();
		}
	}

	@Override
	public String delete() {
		if (!getIsForMyOrgan())
			throw new FatalException(
					SerajMessageUtil.getMessage("common_deleteNotAllowed"));
		return super.delete();
	}

	public boolean getIsForMyOrgan() {
		if (getEntity() == null || getEntity().getId() == null)
			return true;
		return getEntity().getOrgan().getId().equals(getCurrentOrgan().getId());
	}
}