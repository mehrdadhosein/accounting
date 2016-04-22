package ir.serajsamaneh.accounting.saalmaali;

import ir.serajsamaneh.accounting.base.BaseAccountingForm;
import ir.serajsamaneh.accounting.exception.NoActiveSaalMaaliFoundException;
import ir.serajsamaneh.accounting.exception.NoSaalMaaliFoundException;
import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.exception.NoOrganFoundException;
import ir.serajsamaneh.enumeration.SaalMaaliStatusEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;

public class SaalMaaliForm extends BaseAccountingForm<SaalMaaliEntity, Long> {

	@Override
	protected SaalMaaliService getMyService() {
		return saalMaaliService;
	}

	SaalMaaliService saalMaaliService;

	public void setSaalMaaliService(SaalMaaliService saalMaaliService) {
		this.saalMaaliService = saalMaaliService;
	}

	public SaalMaaliService getSaalMaaliService() {
		return saalMaaliService;
	}

	@Override
	public DataModel<SaalMaaliEntity> getDataModel() {
		setSearchAction(true);
		this.getFilter().put("organ.id@eq", getCurrentOrgan().getId());
		return super.getDataModel();
	};

	Long saalMaaliID;

	public Long getSaalMaaliID() {
		if (saalMaaliID == null) {
			Integer saalMaali = (Integer) getRequest().getSession()
					.getAttribute("SAAL");
			Map<String, Object> filter = new HashMap<String, Object>();
			filter.put("saal@eq", saalMaali);
			if (saalMaali != null) {
				List<SaalMaaliEntity> list = getMyService().getDataList(null,
						filter);
				if (list != null && list.size() > 0)
					saalMaaliID = list.get(0).getId();
			}
		}
		return saalMaaliID;
	}

	public void setSaalMaaliID(Long saalMaaliID) {
		this.saalMaaliID = saalMaaliID;
	}

//	public String selectSaal() {
//		getRequest().getSession().setAttribute("SAAL",
//				saalMaaliService.load(saalMaaliID).getSaal());
//		return null;
//	}

	public List<SelectItem> getActiveSaalList() {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("isActive@eq", Boolean.TRUE);
		filter.put("organ.code@startlk", getTopOrgan().getCode());
		filter.put("organ.id@isNotNull", "ding");
		List<SaalMaaliEntity> dataList = getMyService().getDataList(null,filter);
		
		return getSaalmaaliSelectedItems(dataList);
	}

	public List<SelectItem> getSaalMaaliList() {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("organ.code@startlk", getTopOrgan().getCode());
		filter.put("organ.id@isNotNull", "ding");
		List<SaalMaaliEntity> dataList = getMyService().getDataList(null, filter);

		return getSaalmaaliSelectedItems(dataList);
	}

	private List<SelectItem> getSaalmaaliSelectedItems(List<SaalMaaliEntity> dataList) {
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem(null, "----------"));
		for (SaalMaaliEntity saalMaali : dataList) {
			items.add(new SelectItem(saalMaali.getId(), saalMaali.getNameWithOrgan()));
		}
		return items;
	}



	@Override
	public List<? extends BaseEntity> getJsonList(String property, String term,
			boolean all, Map<String, String> params) {
		// TODO Auto-generated method stub
		String activesaalMaali = params.get("activesaalMaali");
		if(activesaalMaali.equals("true")){
			getFilter().put("isActive@eq", true);
		}
		return super.getJsonList(property, term, all, params);
	}


	@Override
	public String save() {
		
		if(getEntity().getId() == null)
			getMyService().checkSaalMaaliStartDate(getEntity(), getCurrentOrgan());
		//boolean checkEndDay=getMyService().checkSaalMaaliEndDate(getEntity());
		getEntity().setOrgan(getCurrentOrgan());
		

		getMyService().save(getEntity(), getCurrentOrgan());
		return getViewUrl();
	}
	
	public Boolean getIsInProgress(){
		return getCurrentUserActiveSaalMaali().getStatus().equals(SaalMaaliStatusEnum.InProgress);
	}
	
	public Boolean getIsTemporalAccountsClosed(){
		return getCurrentUserActiveSaalMaali().getStatus().equals(SaalMaaliStatusEnum.TemporalAccountsClosed);
	}
	
	public Boolean getIsSanadEkhtetamiehCreated(){
		return getCurrentUserActiveSaalMaali().getStatus().equals(SaalMaaliStatusEnum.SanadEkhtetamiehCreated);
	}

	public String getCurrentUserActiveSaalMaaliDesc() {
		try{
			SaalMaaliEntity currentUserActiveSaalMaali = getCurrentUserActiveSaalMaali();
			if(currentUserActiveSaalMaali == null)
				return "";
			return currentUserActiveSaalMaali.getNameWithOrgan();
		}catch(NoActiveSaalMaaliFoundException e){
			e.printStackTrace();
			return "";
		}catch(NoOrganFoundException e){
			System.out.println(e.getMessage()); 
			return "";
		}catch(NoSaalMaaliFoundException  e){
			e.printStackTrace();
			return "";
		}
	}

}
