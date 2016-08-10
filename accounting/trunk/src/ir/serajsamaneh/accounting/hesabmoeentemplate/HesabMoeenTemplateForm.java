package ir.serajsamaneh.accounting.hesabmoeentemplate;

import ir.serajsamaneh.accounting.base.BaseAccountingForm;
import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.erpcore.util.HesabTemplateRelationsUtil;

import java.util.List;
import java.util.Map;

import javax.faces.model.DataModel;

import org.apache.commons.collections.map.ListOrderedMap;

public class HesabMoeenTemplateForm extends
		BaseAccountingForm<HesabMoeenTemplateEntity, Long> {

	@Override
	protected HesabMoeenTemplateService getMyService() {
		return hesabMoeenTemplateService;
	}

	HesabMoeenTemplateService hesabMoeenTemplateService;
	

	public void setHesabMoeenTemplateService(
			HesabMoeenTemplateService hesabMoeenTemplateService) {
		this.hesabMoeenTemplateService = hesabMoeenTemplateService;
	}

	public HesabMoeenTemplateService getHesabMoeenTemplateService() {
		return hesabMoeenTemplateService;
	}

	public String localSave() {
		HesabTemplateRelationsUtil.resetMoeenKolTemplateMap(getCurrentOrgan());
		HesabTemplateRelationsUtil.resetmoeenTafsiliTemplateMap(getCurrentOrgan());
		HesabTemplateRelationsUtil.resetAccountingMarkazTemplateMap(getCurrentOrgan());
		getMyService().save(entity);
		return getLocalViewUrl();
	}

	public DataModel<HesabMoeenTemplateEntity> getLocalArchiveDataModel() {
		return getLocalDataModel();
	}

	public Boolean getIsCreated() {
		return null;
	}

//	String moeenTafsiliInstanceIds;
//
//	public String getMoeenTafsiliInstanceIds() {
//		return moeenTafsiliInstanceIds;
//	}
//
//	public void setMoeenTafsiliInstanceIds(String moeenTafsiliInstanceIds) {
//		this.moeenTafsiliInstanceIds = moeenTafsiliInstanceIds;
//	}

	
	public Map<Long, ListOrderedMap> getMoeenKolTemplateMap() {
		return HesabTemplateRelationsUtil.getMoeenKolTemplateMap(getCurrentUserActiveSaalMaali().getOrgan());
	}
	
	public Map<Long, List<ListOrderedMap>> getMoeenTafsiliTemplateMap() {
		return HesabTemplateRelationsUtil.getMoeenTafsiliTemplateMap(getCurrentUserActiveSaalMaali().getOrgan()); 
	}

	public Map<Long, List<ListOrderedMap>> getAccountingMarkazTemplateMap() {
		return HesabTemplateRelationsUtil.getAccountingMarkazTemplateMap(getCurrentUserActiveSaalMaali().getOrgan());
	}
	
	@Override
	public List<? extends BaseEntity> getJsonList(String property, String term,
			boolean all, Map<String, String> params) {
		
		String isHierarchical = params.get("isHierarchical");
		
		params.put("isLocal","false");
		//getFilter().put("organ.id@eqORorgan.id@isNull",Arrays.asList(getCurrentOrgan().getId(), "ding"));

		if (isHierarchical !=null && isHierarchical.equals("true")){
			getFilter().put("organ.code@startlk", getCurrentUserActiveSaalMaali().getOrgan().getCode());
//			getFilter().put("saalMaali.id@eq",getCurrentUserActiveSaalMaali().getId());
			params.put("isLocal","false");
		}
		
		return super.getJsonList(property, term, all, params);
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