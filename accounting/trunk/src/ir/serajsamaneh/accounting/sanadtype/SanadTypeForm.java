package ir.serajsamaneh.accounting.sanadtype;

import ir.serajsamaneh.accounting.base.BaseAccountingForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;


public class SanadTypeForm extends BaseAccountingForm<SanadTypeEntity,Long> {




	@Override
	protected SanadTypeService getMyService() {
		return sanadTypeService;
	}



	@Override
	public DataModel<SanadTypeEntity> getDataModel() {
		this.getFilter().put("organ.id@eq",getCurrentOrgan().getId());
		return super.getDataModel();
	};
	
	
	public String localSave() {
		getEntity().setOrgan(getCurrentOrgan());
		getMyService().save(entity);
		return getLocalViewUrl();
	}
	

	
	public List<SelectItem> getSanadType(){
		Map<String, Object> filter=new HashMap<String, Object>();
		filter.put("organ.id@eq",getCurrentOrgan().getId());
		List<SanadTypeEntity> list =getSanadTypeService().getDataList(null, filter);
		
		List<SelectItem> selectItemList = new ArrayList<SelectItem>();
		selectItemList.add(new SelectItem("", "------------"));
		for(SanadTypeEntity sanadTypeEntity: list){
			selectItemList.add(new SelectItem(sanadTypeEntity.getId(),sanadTypeEntity.getName()));
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

	 

}