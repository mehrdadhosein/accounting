package ir.serajsamaneh.accounting.saalmaali;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;

import ir.serajsamaneh.accounting.base.BaseAccountingForm;
import ir.serajsamaneh.accounting.exception.NoActiveSaalMaaliFoundException;
import ir.serajsamaneh.accounting.exception.NoSaalMaaliFoundException;
import ir.serajsamaneh.accounting.hesabgroup.HesabGroupService;
import ir.serajsamaneh.accounting.hesabkol.HesabKolService;
import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.exception.NoOrganFoundException;
import ir.serajsamaneh.core.exception.RequiredFieldNotSetException;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.enumeration.SaalMaaliStatusEnum;

public class SaalMaaliForm extends BaseAccountingForm<SaalMaaliEntity, Long> {

	@Override
	protected SaalMaaliService getMyService() {
		return saalMaaliService;
	}

	SaalMaaliService saalMaaliService;
	
	@Autowired
	HesabGroupService hesabGroupService;
	
	@Autowired
	HesabKolService hesabKolService;

	public void setSaalMaaliService(SaalMaaliService saalMaaliService) {
		this.saalMaaliService = saalMaaliService;
	}

	public SaalMaaliService getSaalMaaliService() {
		return saalMaaliService;
	}


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
		if(getTopOrgan().getCode() == null)
			throw new FatalException("organ code not defined");
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

	
	public String globalSave() {
		
		if(getEntity().getId() == null)
			getMyService().checkSaalMaaliStartDate(getEntity(), getCurrentOrgan());
		
		getMyService().globalSave(getEntity());
		addInfoMessage("SUCCESSFUL_ACTION");
		return null;
	}

	@Override
	public String save() {
		
//		if(getEntity().getId() == null)
//		getMyService().checkSaalMaaliStartDate(getEntity(), getCurrentOrgan());
		//boolean checkEndDay=getMyService().checkSaalMaaliEndDate(getEntity());
		if(getEntity().getOrgan() == null || getEntity().getOrgan().getId() == null)
			getEntity().setOrgan(getCurrentOrgan());
		

		getMyService().save(getEntity(), getCurrentOrgan());
		addInfoMessage("SUCCESSFUL_ACTION");
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
			System.out.println(e.getDesc());
			return SerajMessageUtil.getMessage("common_undefined");
		}catch(NoOrganFoundException e){
			System.out.println(e.getDesc()); 
			return SerajMessageUtil.getMessage("common_undefined");
		}catch(NoSaalMaaliFoundException  e){
			System.out.println(e.getDesc());
			return SerajMessageUtil.getMessage("common_undefined");
		}
	}
	
	SaalMaaliEntity srcSaalMaali = new SaalMaaliEntity();
	
	public SaalMaaliEntity getSrcSaalMaali() {
		return srcSaalMaali;
	}

	public void setSrcSaalMaali(SaalMaaliEntity srcSaalMaali) {
		this.srcSaalMaali = srcSaalMaali;
	}


	public void copyHesabsFromSourceSaalMaaliToDestSaalMaali(){
		if(getSrcSaalMaali().getId() == null)
			throw new RequiredFieldNotSetException(SerajMessageUtil.getMessage("AccountingSystemConfig_srcSaalMaali"));

//		if(getDestSaalMaali().getId() == null)
//			throw new RequiredFieldNotSetException(SerajMessageUtil.getMessage("AccountingSystemConfig_destSaalMaali"));
		
		srcSaalMaali = getSaalMaaliService().load(getSrcSaalMaali().getId());
//		destSaalMaali = getSaalMaaliService().load(getDestSaalMaali().getId());
		
		getHesabKolService().copyHesabKolsFromSourceSaalMaaliToDestSaalMaali(getSrcSaalMaali(), getEntity());
		getHesabKolService().copyHesabMoeensFromSourceSaalMaaliToDestSaalMaali(getSrcSaalMaali(), getEntity());
		getHesabKolService().copyHesabTafsilissFromSourceSaalMaaliToDestSaalMaali(getSrcSaalMaali(), getEntity());
		getHesabKolService().copyHesabTafsiliRelatedEntities(getSrcSaalMaali(), getEntity());
		getHesabKolService().copyAccountingMarkazhaFromSourceSaalMaaliToDestSaalMaali(getSrcSaalMaali(), getEntity());
		getHesabKolService().copycontactHesabsFromSourceSaalMaaliToDestSaalMaali(getSrcSaalMaali(), getEntity());
		addInfoMessage("SUCCESSFUL_ACTION");
	}
	
	@Override
	public DataModel<SaalMaaliEntity> getLocalDataModel() {
		setSearchAction(true);
		return super.getLocalDataModel();
	}

	public String importFromHesabKolTemplateList(){
		hesabKolService.createDefaultAccounts(getEntity().getOrgan());
		hesabGroupService.importFromHesabGroupTemplateList(getEntity(), getCurrentOrgan());
		hesabKolService.importFromHesabKolTemplateList(getEntity(), getCurrentOrgan());
		getHesabMoeenService().importFromHesabMoeenTemplateList(getEntity(), getCurrentOrgan());
		getHesabTafsiliService().importFromHesabTafsiliTemplateList(getEntity(), getCurrentOrgan());
		

		setDataModel(null); 
		addInfoMessage("SUCCESSFUL_ACTION");
		return null;
	}
}