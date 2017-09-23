package ir.serajsamaneh.accounting.sanadtype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import ir.serajsamaneh.accounting.base.BaseAccountingForm;
import ir.serajsamaneh.core.base.BaseEntity;

public class SanadTypeForm extends BaseAccountingForm<SanadTypeEntity, Long> {

	@Override
	protected SanadTypeService getMyService() {
		return sanadTypeService;
	}

	public String localSave() {
		getEntity().setOrgan(getCurrentOrgan());
		getMyService().save(entity);
		return getLocalViewUrl();
	}

	public List<SelectItem> getSanadType() {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("organ.id@eq", getCurrentOrgan().getId());
		List<SanadTypeEntity> list = getSanadTypeService().getDataList(null, filter);

		List<SelectItem> selectItemList = new ArrayList<SelectItem>();
		selectItemList.add(new SelectItem("", "------------"));
		for (SanadTypeEntity sanadTypeEntity : list) {
			selectItemList.add(new SelectItem(sanadTypeEntity.getId(), sanadTypeEntity.getName()));
		}
		return selectItemList;
	}

	SanadTypeService sanadTypeService;

	public void setSanadTypeService(SanadTypeService sanadTypeService) {
		this.sanadTypeService = sanadTypeService;
	}

	public SanadTypeService getSanadTypeService() {
		return sanadTypeService;
	}
	
	@Override
	public List<? extends BaseEntity> getJsonList(String property, String term, boolean all,
			Map<String, String> params) {
		String isHierarchical = params.get("isHierarchical");
		if (isHierarchical !=null && isHierarchical.equals("true")){
			List<Long> topOrganList = getTopOrgansIdList(getCurrentOrgan());
			getFilter().put("organ.id@in", topOrganList);
			
//			this.getFilter().put("organ.code@startlk", getCurrentUserActiveSaalMaali().getOrgan().getCode());
			params.put("isLocal","false");
		}
		return super.getJsonList(property, term, all, params);
	}

}