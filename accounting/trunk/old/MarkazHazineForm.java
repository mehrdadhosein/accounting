package ir.serajsamaneh.accounting.markazhazine;

import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.base.BaseEntityForm;
import ir.serajsamaneh.enumeration.YesNoEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;

import org.springframework.util.StringUtils;

public class MarkazHazineForm extends
		BaseEntityForm<MarkazHazineEntity, Long> {

	@Override
	protected MarkazHazineService getMyService() {
		return markazHazineService;
	}

	MarkazHazineService markazHazineService;

	public void setMarkazHazineService(MarkazHazineService markazHazineService) {
		this.markazHazineService = markazHazineService;
	}

	public MarkazHazineService getMarkazHazineService() {
		return markazHazineService;
	}

	@Override
	public List<? extends BaseEntity> getJsonList(String property, String term,
			boolean all, Map<String, String> params) {
		String isLocalUser = params.get("isCurrentOrgan");
		/*String hidden = params.get("hidden");
		
		if(StringUtils.hasText(hidden) && hidden.equals("false")){
			this.getFilter().put("hidden@eq",false);
		}*/
		
		if (isLocalUser !=null && isLocalUser.equals("true")){
			this.getFilter().put("organ.id@eq",getCurrentOrgan().getId());
		}
		// TODO Auto-generated method stub
		return super.getJsonList(property, term, all, params);
	}
	
	
	public List<SelectItem> getLocalMarkazHazineList(){
		
		List<SelectItem> localMarkazHazineList = new ArrayList<SelectItem>();
		
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eq", getCurrentUser().getOrgan().getId());
		List<MarkazHazineEntity> dataList = getMyService().getDataList(null, localFilter);
		SelectItem item = new SelectItem(null,"---------");
		localMarkazHazineList.add(item);
		for (MarkazHazineEntity markazHazineEntity : dataList) {
			item = new SelectItem(markazHazineEntity.getId(),
					markazHazineEntity.getOnvan());
			localMarkazHazineList.add(item);
		}
		return localMarkazHazineList;
	}
	public String localSave() {
		getEntity().setOrgan(getCurrentOrgan());
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("organ.id@eq", getCurrentOrgan().getId());
		checkUniqueNess(getEntity(), MarkazHazineEntity.PROP_ONVAN, getEntity().getOnvan(),filter);
		save();
		return getLocalViewUrl();
	}

	public DataModel getLocalDataModel() {
		setSearchAction(true);
		getFilter().put("organ.id@eq", getCurrentOrgan().getId());
		return getDataModel();
	}

	public DataModel getLocalArchiveDataModel() {
		return getDataModel();
	}

	public Boolean getIsCreated() {
		return null;
	}

	
	public DataModel getGlobalDataModel() {
		
		return super.getDataModel();
	}

}