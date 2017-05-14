package ir.serajsamaneh.accounting.sanadhesabdariitem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;

import org.hibernate.FlushMode;
import org.springframework.util.StringUtils;

import ir.serajsamaneh.accounting.base.BaseAccountingForm;
import ir.serajsamaneh.accounting.enumeration.HesabTypeEnum;
import ir.serajsamaneh.accounting.enumeration.MahyatKolEnum;
import ir.serajsamaneh.accounting.enumeration.SanadFunctionEnum;
import ir.serajsamaneh.accounting.enumeration.SanadStateEnum;
import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.core.converter.CSVtoListOfLongConverter;
import ir.serajsamaneh.core.exception.MaxExcelRecordExportException;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.security.ActionLogUtil;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.core.util.StringUtil;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import serajcomponent.DateConverter;
import serajcomponent.SerajDateTimePickerType;

public class SanadHesabdariItemForm   extends BaseAccountingForm<SanadHesabdariItemEntity,Long>  {



	@Override
	public SanadHesabdariItemEntity getEntity() {
		if (entity == null){
			String fromDate = getRequest().getParameter("fromDate");
			String toDate = getRequest().getParameter("toDate");
			String selectedOrganIdStr = getRequest().getParameter("selectedOrganId");
			if(StringUtil.hasText(selectedOrganIdStr))
				setSelectedOrganId(new Long(selectedOrganIdStr));
			if(StringUtils.hasText(fromDate))
				setFromDate(DateConverter.convertStringDateToGDate(SerajDateTimePickerType.Date, fromDate, "Shamsi"));
			if(StringUtils.hasText(toDate))
				setToDate(DateConverter.convertStringDateToGDate(SerajDateTimePickerType.Date, toDate, "Shamsi"));
			return super.getEntity();
		}
		
		return entity;
	}



	@Override
	protected SanadHesabdariItemService getMyService() {
		return sanadHesabdariItemService;
	}
	
	Map<String, Object> sanadhesabdariItemFilter = new HashMap<String, Object>();
	Date fromDate;
	Date toDate;
	
	Long fromSerial;
	Long toSerial;
	
	List<Long> tafsiliIds;
	List<Long> articleTafsiliIds;
	List<Long> moeenIds;
	List<Long> hesabKolIds;
	List<Long> accountingMarkazIds;

	Integer articleTafsiliLevel=1;

	public Integer getArticleTafsiliLevel() {
		return articleTafsiliLevel;
	}



	public void setArticleTafsiliLevel(Integer articleTafsiliLevel) {
		this.articleTafsiliLevel = articleTafsiliLevel;
	}



	public Map<String, Object> getSanadhesabdariItemFilter() {
		return sanadhesabdariItemFilter;
	}



	public void setSanadhesabdariItemFilter(
			Map<String, Object> sanadhesabdariItemFilter) {
		this.sanadhesabdariItemFilter = sanadhesabdariItemFilter;
	}



	public List<Long> getTafsiliIds() {
		return tafsiliIds;
	}

	public void setTafsiliIds(List<Long> tafsiliIds) {
		this.tafsiliIds = tafsiliIds;
	}




	public List<Long> getAccountingMarkazIds() {
		return accountingMarkazIds;
	}

	public void setAccountingMarkazIds(List<Long> accountingMarkazIds) {
		this.accountingMarkazIds = accountingMarkazIds;
	}

	public List<Long> getHesabKolIds() {
		return hesabKolIds;
	}

	public void setHesabKolIds(List<Long> hesabKolIds) {
		this.hesabKolIds = hesabKolIds;
	}

	public List<Long> getArticleTafsiliIds() {
		return articleTafsiliIds;
	}

	public void setArticleTafsiliIds(List<Long> articleTafsiliIds) {
		this.articleTafsiliIds = articleTafsiliIds;
	}

	public List<Long> getMoeenIds() {
		return moeenIds;
	}

	public void setMoeenIds(List<Long> moeenIds) {
		this.moeenIds = moeenIds;
	}


	public Long getFromSerial() {
		if (fromSerial == null){
			String fromSerialStr = getRequest().getParameter("fromSerial");
			if(StringUtils.hasText(fromSerialStr))
				fromSerial = new Long(fromSerialStr);
		}
		return fromSerial;
	}



	public void setFromSerial(Long fromSerial) {
		this.fromSerial = fromSerial;
	}



	public Long getToSerial() {
		if (toSerial == null){
			String toSerialStr = getRequest().getParameter("toSerial");
			if(StringUtils.hasText(toSerialStr))
				toSerial = new Long(toSerialStr);
		}
		return toSerial;
	}



	public void setToSerial(Long toSerial) {
		this.toSerial = toSerial;
	}



	public Date getFromDate() {
		if (fromDate == null){
			String fromDateStr = getRequest().getParameter("fromDate");
			if(StringUtils.hasText(fromDateStr))
				fromDate = DateConverter.convertStringDateToGDate(SerajDateTimePickerType.Date, fromDateStr, "Shamsi");
		}
		
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		if (toDate == null){
			String toDateStr = getRequest().getParameter("toDate");
			if(StringUtils.hasText(toDateStr))
				toDate = DateConverter.convertStringDateToGDate(SerajDateTimePickerType.Date, toDateStr, "Shamsi");
		}		
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}


	SanadHesabdariItemService sanadHesabdariItemService;
	
	public void setSanadHesabdariItemService(SanadHesabdariItemService sanadHesabdariItemService) {
		this.sanadHesabdariItemService = sanadHesabdariItemService;
	}
	
	public SanadHesabdariItemService getSanadHesabdariItemService() {
		return sanadHesabdariItemService;
	}


	public String localSave() {
		save();
		return getLocalViewUrl();
	}

	
	private void populateFilterFromRequest() {
		String hesabKolIds = getRequest().getParameter("hesabKolIds");
		if(StringUtils.hasText(hesabKolIds)){
			List<Long> list = CSVtoListOfLongConverter.convert(hesabKolIds);
			setHesabKolIds(list);
			getFilter().put("hesabKol.id@in", getHesabKolIds());
		}else
			getFilter().put("hesabKol.id@in", null);
		
		String moeenIds = getRequest().getParameter("moeenIds");
		if(StringUtils.hasText(moeenIds)){
			List<Long> list = CSVtoListOfLongConverter.convert(moeenIds);
			setMoeenIds(list);
			getFilter().put("hesabMoeen.id@in", getMoeenIds());
		}else
			getFilter().put("hesabMoeen.id@in", null);
		
		String tafsiliIds = getRequest().getParameter("tafsiliIds");
		if(StringUtils.hasText(tafsiliIds)){
			List<Long> list = CSVtoListOfLongConverter.convert(tafsiliIds);
			setTafsiliIds(list);
			
			if(getTafsiliIds()!=null && !getTafsiliIds().isEmpty())
				getFilter().put("hesabTafsili.id@in", getTafsiliIds());
			else
				getFilter().put("hesabTafsili.id@in", null);
		}
		
		String articleTafsiliIds = getRequest().getParameter("articleTafsiliIds");
		if(StringUtils.hasText(articleTafsiliIds)){
			List<Long> list = CSVtoListOfLongConverter.convert(articleTafsiliIds);
			setArticleTafsiliIds(list);
			if(getArticleTafsiliIds()!=null && !articleTafsiliIds.isEmpty())
				getFilter().put("articleTafsili.hesabTafsili.id@in",getArticleTafsiliIds());			
			else
				getFilter().put("articleTafsili.hesabTafsili.id@in",null);
		}
		
		String accountingMarkazIds = getRequest().getParameter("accountingMarkazIds");
		if(StringUtils.hasText(accountingMarkazIds)){
			List<Long> list = CSVtoListOfLongConverter.convert(accountingMarkazIds);
			setAccountingMarkazIds(list);
			if(getAccountingMarkazIds()!=null && !getAccountingMarkazIds().isEmpty())
				getFilter().put("accountingMarkaz.id@in",getAccountingMarkazIds());			
			else
				getFilter().put("accountingMarkaz.id@in",null);
		}
		
		String searchAction = getRequest().getParameter("searchAction");
		if(StringUtils.hasText(searchAction)){
			setSearchAction(Boolean.valueOf(searchAction));
		}
		
	}

	public DataModel<SanadHesabdariItemEntity> getLocalDataModel() {
		getFilter().put("sanadHesabdari.organ.id@eq", getCurrentOrgan().getId());
		return getDataModel();
	}
	
	public DataModel<SanadHesabdariItemEntity> getLocalArchiveDataModel() {
		getFilter().put("sanadHesabdari.organ.id@eq", getCurrentOrgan().getId());
		return getDataModel();
	}	

	public DataModel<SanadHesabdariItemEntity> getDaftarRooznamehDataModel() {
		setRowsPerPage(100);
//		if(!FacesContext.getCurrentInstance().getRenderResponse())
//			return null;
		setSearchAction(true);
		createDaftarLocalFilter(getCurrentOrgan());
		setDefaultSortCol("sanadHesabdari.tarikhSanad");
		setDefaultSortType(true);
		return getDataModel();
	}
	
	public DataModel<SanadHesabdariItemEntity> getDaftarRooznamehHierarchicalDataModel() {
		setRowsPerPage(100);
//		if(!FacesContext.getCurrentInstance().getRenderResponse())
//			return null;
		setSearchAction(true);
//		OrganEntity selectedOrgan = getSelectedOrganId()!=null ? getOrganService().load(getSelectedOrganId()) : null;
		createDaftarHierarchicalFilter();
		setDefaultSortCol("sanadHesabdari.tarikhSanad");
		setDefaultSortType(true);
		return getDataModel();
	}
	
	private void createDaftarSummaryDaftarLocalFilter(OrganEntity organEntity) {
		populateFilterFromRequest();
		
		if(organEntity!=null)
			getFilter().put("sanadHesabdari.organ.id@eq", organEntity.getId());
		else
			getFilter().put("sanadHesabdari.organ.id@eq", null);
		
		
		//getFilter().put("sanadHesabdari.state@eq", SanadStateEnum.MonthlySummary);
		getFilter().put("sanadHesabdari.state@eqORsanadHesabdari.sanadFunction@in", Arrays.asList(SanadStateEnum.MonthlySummary, Arrays.asList(SanadFunctionEnum.EFTETAHIE, SanadFunctionEnum.BASTAN_HESABHA, SanadFunctionEnum.EKHTETAMIE)));
		createCommonDaftarFilter();
	}

	private void createDaftarLocalFilter(OrganEntity organEntity) {
		populateFilterFromRequest();
		
		if(organEntity!=null)
			getFilter().put("sanadHesabdari.organ.id@eq", organEntity.getId());
		else
			getFilter().put("sanadHesabdari.organ.id@eq", null);

		List<SanadStateEnum> sanadStates = new ArrayList<SanadStateEnum>();
		sanadStates.add(SanadStateEnum.DAEM);
		sanadStates.add(SanadStateEnum.BARRESI_SHODE);
		getFilter().put("sanadHesabdari.state@eqORsanadHesabdari.state@eq", sanadStates);
		
		createCommonDaftarFilter();
	}

	private void createDaftarHierarchicalFilter() {
		populateFilterFromRequest();
		
		if(getSelectedOrganId()!=null)
			getFilter().put("sanadHesabdari.organ.id@eq", getSelectedOrganId());		
		else
			getFilter().put("sanadHesabdari.organ.id@eq", null);
		getFilter().put("sanadHesabdari.organ.code@startlk",	getTopOrgan().getCode());

		List<SanadStateEnum> sanadStates = new ArrayList<SanadStateEnum>();
		sanadStates.add(SanadStateEnum.DAEM);
		sanadStates.add(SanadStateEnum.BARRESI_SHODE);
		getFilter().put("sanadHesabdari.state@eqORsanadHesabdari.state@eq", sanadStates);
		
		createCommonDaftarFilter();
	}
	


	private void createCommonDaftarFilter() {

		
		if(getHesabKolIds()!=null && !getHesabKolIds().isEmpty())
			getFilter().put("hesabKol.id@in", getHesabKolIds());
		
		if(getMoeenIds()!=null && !getMoeenIds().isEmpty())
			getFilter().put("hesabMoeen.id@in", getMoeenIds());
		
		if(getTafsiliIds()!=null && !getTafsiliIds().isEmpty())
			getFilter().put("hesabTafsili.id@in", getTafsiliIds());
		
		if(getArticleTafsiliIds()!=null && !getArticleTafsiliIds().isEmpty())
			getFilter().put("articleTafsili.hesabTafsili.id@in",getArticleTafsiliIds());
		
		if(getAccountingMarkazIds()!=null && !getAccountingMarkazIds().isEmpty())
			getFilter().put("accountingMarkaz.id@in",getAccountingMarkazIds());	
		
		if(getSelectedSaalMaali()!=null)
			getFilter().put("sanadHesabdari.saalMaali.id@eq",	getSelectedSaalMaali().getId());
		else
			getFilter().put("sanadHesabdari.saalMaali.id@eq",	null);

		getFilter().put("sanadHesabdari.tarikhSanad@ge",getFromDate());
		getFilter().put("sanadHesabdari.tarikhSanad@le", getToDate());
		
		getFilter().put("sanadHesabdari.serial@ge",fromSerial);
		getFilter().put("sanadHesabdari.serial@le",toSerial);
		
		getFilter().putAll(getSanadhesabdariItemFilter());
	}
	
	public DataModel<SanadHesabdariItemEntity> getDaftarTafsiliDataModel() {
		setRowsPerPage(100);
//		if(!FacesContext.getCurrentInstance().getRenderResponse())
//			return null;
		createDaftarLocalFilter(getCurrentOrgan());
		return getDataModel();
	}
	
	public DataModel<SanadHesabdariItemEntity> getDaftarTafsiliHierarchicalDataModel() {
		setRowsPerPage(100);
//		if(!FacesContext.getCurrentInstance().getRenderResponse())
//			return null;
//		OrganEntity selectedOrgan = getSelectedOrganId()!=null ? getOrganService().load(getSelectedOrganId()) : null;
		createDaftarHierarchicalFilter();
		return getDataModel();
	}
	
	public DataModel<SanadHesabdariItemEntity> getDaftarShenavarDataModel() {
		setRowsPerPage(100);
//		if(!FacesContext.getCurrentInstance().getRenderResponse())
//			return null;
		createDaftarLocalFilter(getCurrentOrgan());
		return getDataModel();
	}
	
	public DataModel<SanadHesabdariItemEntity> getDaftarShenavarHierarchicalDataModel() {
		setRowsPerPage(100);
//		if(!FacesContext.getCurrentInstance().getRenderResponse())
//			return null;
//		OrganEntity selectedOrgan = getSelectedOrganId()!=null ? getOrganService().load(getSelectedOrganId()) : null;
		createDaftarHierarchicalFilter();
		return getDataModel();
	}
	
	public DataModel<SanadHesabdariItemEntity> getDaftarAccountingMarkazDataModel() {
		setRowsPerPage(100);
//		if(!FacesContext.getCurrentInstance().getRenderResponse())
//			return null;
		createDaftarLocalFilter(getCurrentOrgan());
		return getDataModel();
	}
	
	public DataModel<SanadHesabdariItemEntity> getDaftarAccountingMarkazHierarchicalDataModel() {
		setRowsPerPage(100);
//		if(!FacesContext.getCurrentInstance().getRenderResponse())
//			return null;
//		OrganEntity selectedOrgan = getSelectedOrganId()!=null ? getOrganService().load(getSelectedOrganId()) : null;
		createDaftarHierarchicalFilter();
		return getDataModel();
	}
	
	public DataModel<SanadHesabdariItemEntity> getDaftarKolDataModel() {
		setRowsPerPage(100);
		createDaftarLocalFilter(getCurrentOrgan());
		return getDataModel();
	}
	
	public DataModel<SanadHesabdariItemEntity> getDaftarKolSummaryDataModel() {
		setRowsPerPage(100);
		createDaftarSummaryDaftarLocalFilter(getCurrentOrgan());
		return getDataModel();
	}
	
	public DataModel<SanadHesabdariItemEntity> getDaftarKolHierarchicalDataModel() {
		setRowsPerPage(100);
//		if(!FacesContext.getCurrentInstance().getRenderResponse())
//			return null;		
//		OrganEntity selectedOrgan = getSelectedOrganId()!=null ? getOrganService().load(getSelectedOrganId()) : null;
		createDaftarHierarchicalFilter();
		return getDataModel();
	}
	
	public DataModel<SanadHesabdariItemEntity> getDaftarMoeenDataModel() {
		setRowsPerPage(100);
//		if(!FacesContext.getCurrentInstance().getRenderResponse())
//			return null;		
		createDaftarLocalFilter(getCurrentOrgan());
		return getDataModel();
	}
	
	public DataModel<SanadHesabdariItemEntity> getDaftarMoeenHierarchicalDataModel() {
		setRowsPerPage(100);
//		if(!FacesContext.getCurrentInstance().getRenderResponse())
//			return null;	
//		OrganEntity selectedOrgan = getSelectedOrganId()!=null ? getOrganService().load(getSelectedOrganId()) : null;
		createDaftarHierarchicalFilter();
		return getDataModel();
	}

	Double totalBedehkar =0d;
	Double totalBestankar =0d;
	Double totalMandehBedehkar =0d;
	Double totalMandehBestankar =0d;


	
	public Double getTotalMandehBedehkar() {
		return totalMandehBedehkar;
	}

	public void setTotalMandehBedehkar(Double totalMandehBedehkar) {
		this.totalMandehBedehkar = totalMandehBedehkar;
	}

	public Double getTotalMandehBestankar() {
		return totalMandehBestankar;
	}

	public void setTotalMandehBestankar(Double totalMandehBestankar) {
		this.totalMandehBestankar = totalMandehBestankar;
	}

	public Double getTotalBedehkar() {
		return totalBedehkar;
	}

	public void setTotalBedehkar(Double totalBedehkar) {
		this.totalBedehkar = totalBedehkar;
	}

	public Double getTotalBestankar() {
		return totalBestankar;
	}

	public void setTotalBestankar(Double totalBestankar) {
		this.totalBestankar = totalBestankar;
	}

	Boolean searchBySaalMaali=false;
	Long saalMaaliId;

	public Boolean getSearchBySaalMaali() {
		return searchBySaalMaali;
	}

	public void setSearchBySaalMaali(Boolean searchBySaalMaali) {
		this.searchBySaalMaali = searchBySaalMaali;
	}


	public Long getSaalMaaliId() {
		return saalMaaliId;
	}

	public void setSaalMaaliId(Long saalMaaliId) {
		this.saalMaaliId = saalMaaliId;
	}
	
	public SaalMaaliEntity getSelectedSaalMaali(){
		if(getSearchBySaalMaali())
			if(getSaalMaaliId()!=null)
				return getSaalMaaliService().load(getSaalMaaliId());
			else
				return null;
		return getCurrentUserActiveSaalMaali();
	}
	public List<SanadHesabdariItemEntity> getTarazKolAzmayeshi() {
		if(!FacesContext.getCurrentInstance().getRenderResponse())
			return new ArrayList<SanadHesabdariItemEntity>();
		setSearchAction(true);
		List<SanadHesabdariItemEntity> tarazKolAzmayeshi = extractTarazKolAzmayeshi(getCurrentOrgan());
		return tarazKolAzmayeshi;
	}

	public List<SanadHesabdariItemEntity> getHierarchicalTarazKolAzmayeshi() {
		if(!FacesContext.getCurrentInstance().getRenderResponse())
			return new ArrayList<SanadHesabdariItemEntity>();
		setSearchAction(true);
		OrganEntity selectedOrgan = getSelectedOrganId()!=null ? getOrganService().load(getSelectedOrganId()) : null;
		List<SanadHesabdariItemEntity> tarazKolAzmayeshi = extractTarazKolAzmayeshi(selectedOrgan);
		return tarazKolAzmayeshi; 
	}

	private List<SanadHesabdariItemEntity> extractTarazKolAzmayeshi(OrganEntity organEntity) {
		populateFilterFromRequest();
		List<SanadHesabdariItemEntity> tarazKolAzmayeshi = getMyService().getTarazKolAzmayeshi(getSelectedSaalMaali(), getFromDate(), getToDate(), getHesabKolIds(),getMoeenIds(), getTafsiliIds(), getArticleTafsiliIds(), getAccountingMarkazIds(),null,organEntity, getFromSerial(), getToSerial(), getSanadhesabdariItemFilter());
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : tarazKolAzmayeshi) {
			totalBedehkar += sanadHesabdariItemEntity.getBedehkar();
			totalBestankar += sanadHesabdariItemEntity.getBestankar();			
			totalMandehBedehkar += sanadHesabdariItemEntity.getMandehBedehkar();
			totalMandehBestankar += sanadHesabdariItemEntity.getMandehBestankar();
		}
		return tarazKolAzmayeshi;
	}

	
	public List<SanadHesabdariItemEntity> getTarazMoeenAzmayeshi() {
		if(!FacesContext.getCurrentInstance().getRenderResponse())
			return new ArrayList<SanadHesabdariItemEntity>();
		setSearchAction(true);
		List<SanadHesabdariItemEntity> tarazMoeenAzmayeshi = extractTarazMoeenAzmayeshi(getCurrentOrgan());
		return tarazMoeenAzmayeshi;
	}

	public List<SanadHesabdariItemEntity> getHierarchicalTarazMoeenAzmayeshi() {
		if(!FacesContext.getCurrentInstance().getRenderResponse())
			return new ArrayList<SanadHesabdariItemEntity>();
		setSearchAction(true);
		OrganEntity selectedOrgan = getSelectedOrganId()!=null ? getOrganService().load(getSelectedOrganId()) : null;
		List<SanadHesabdariItemEntity> tarazMoeenAzmayeshi = extractTarazMoeenAzmayeshi(selectedOrgan);
		return tarazMoeenAzmayeshi; 
	}

	private List<SanadHesabdariItemEntity> extractTarazMoeenAzmayeshi(OrganEntity organEntity) {
		populateFilterFromRequest();
		List<SanadHesabdariItemEntity> tarazMoeenAzmayeshi = getMyService().getTarazMoeenAzmayeshi(getSelectedSaalMaali(), getFromDate(), getToDate(), getHesabKolIds(),getMoeenIds(), getTafsiliIds(), getArticleTafsiliIds(), getAccountingMarkazIds(),null,organEntity, getFromSerial(), getToSerial(), getSanadhesabdariItemFilter());
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : tarazMoeenAzmayeshi) {
			totalBedehkar += sanadHesabdariItemEntity.getBedehkar();
			totalBestankar += sanadHesabdariItemEntity.getBestankar();	
			totalMandehBedehkar += sanadHesabdariItemEntity.getMandehBedehkar();
			totalMandehBestankar += sanadHesabdariItemEntity.getMandehBestankar();		
		}
		return tarazMoeenAzmayeshi;
	}
	
	public List<SanadHesabdariItemEntity> getTarazTafsiliAzmayeshi() {
		System.out.println(FacesContext.getCurrentInstance().getRenderResponse());
		if(!FacesContext.getCurrentInstance().getRenderResponse())
			return new ArrayList<SanadHesabdariItemEntity>();
		setSearchAction(true);
		List<SanadHesabdariItemEntity> tarazTafsiliAzmayeshi = extractTarazTafsiliAzmayeshi(getCurrentOrgan());
		return tarazTafsiliAzmayeshi;
	}


	public List<SanadHesabdariItemEntity> getHierarchicalTarazTafsilAzmayeshi() {
		if(!FacesContext.getCurrentInstance().getRenderResponse())
			return new ArrayList<SanadHesabdariItemEntity>();
		setSearchAction(true);
		OrganEntity selectedOrgan = getSelectedOrganId()!=null ? getOrganService().load(getSelectedOrganId()) : null;
		List<SanadHesabdariItemEntity> tarazTafsiliAzmayeshi = extractTarazTafsiliAzmayeshi(selectedOrgan);
		return tarazTafsiliAzmayeshi; 
	}
	
	public List<SanadHesabdariItemEntity> getTarazShenavarAzmayeshi() {
		System.out.println(FacesContext.getCurrentInstance().getRenderResponse());
		if(!FacesContext.getCurrentInstance().getRenderResponse())
			return new ArrayList<SanadHesabdariItemEntity>();
		setSearchAction(true);
		List<SanadHesabdariItemEntity> tarazTafsiliAzmayeshi = extractTarazShenavarAzmayeshi(getCurrentOrgan(), getArticleTafsiliLevel());
		return tarazTafsiliAzmayeshi;
	}


	public List<SanadHesabdariItemEntity> getHierarchicalTarazShenavarAzmayeshi() {
		if(!FacesContext.getCurrentInstance().getRenderResponse())
			return new ArrayList<SanadHesabdariItemEntity>();
		setSearchAction(true);
		OrganEntity selectedOrgan = getSelectedOrganId()!=null ? getOrganService().load(getSelectedOrganId()) : null;
		List<SanadHesabdariItemEntity> tarazTafsiliAzmayeshi = extractTarazShenavarAzmayeshi(selectedOrgan, getArticleTafsiliLevel());
		return tarazTafsiliAzmayeshi; 
	}
	
	public List<SanadHesabdariItemEntity> getTarazAccountingMarkazAzmayeshi() {
//		System.out.println(FacesContext.getCurrentInstance().getRenderResponse());
		if(!FacesContext.getCurrentInstance().getRenderResponse())
			return new ArrayList<SanadHesabdariItemEntity>();
		setSearchAction(true);
		List<SanadHesabdariItemEntity> tarazAccountingMarkazAzmayeshi = extractTarazAccountingMarkazAzmayeshi(getCurrentOrgan());
		return tarazAccountingMarkazAzmayeshi;
	}

	public List<SanadHesabdariItemEntity> getTarazAccountingMarkazShenavarAzmayeshi() {
//		System.out.println(FacesContext.getCurrentInstance().getRenderResponse());
		if(!FacesContext.getCurrentInstance().getRenderResponse())
			return new ArrayList<SanadHesabdariItemEntity>();
		setSearchAction(true);
		List<SanadHesabdariItemEntity> tarazAccountingMarkazAzmayeshi = extractTarazAccountingMarkazShenavarAzmayeshi(getCurrentOrgan());
		return tarazAccountingMarkazAzmayeshi;
	}
	

	public List<SanadHesabdariItemEntity> getHierarchicalTarazAccountingMarkazAzmayeshi() {
		if(!FacesContext.getCurrentInstance().getRenderResponse())
			return new ArrayList<SanadHesabdariItemEntity>();
		setSearchAction(true);
		OrganEntity selectedOrgan = getSelectedOrganId()!=null ? getOrganService().load(getSelectedOrganId()) : null;
		List<SanadHesabdariItemEntity> tarazAccountingMarkazAzmayeshi = extractTarazAccountingMarkazAzmayeshi(selectedOrgan);
		return tarazAccountingMarkazAzmayeshi; 
	}

	private List<SanadHesabdariItemEntity> extractTarazTafsiliAzmayeshi(OrganEntity organEntity) {
		populateFilterFromRequest();
		List<SanadHesabdariItemEntity> tarazTafsiliAzmayeshi = getMyService().getTarazTafsiliAzmayeshi(getSelectedSaalMaali(), getFromDate(), getToDate(), getHesabKolIds(),getMoeenIds(), getTafsiliIds(), getArticleTafsiliIds(), getAccountingMarkazIds(),null,organEntity, getFromSerial(), getToSerial(), getSanadhesabdariItemFilter());
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : tarazTafsiliAzmayeshi) {
			totalBedehkar += sanadHesabdariItemEntity.getBedehkar();
			totalBestankar += sanadHesabdariItemEntity.getBestankar();
			totalMandehBedehkar += sanadHesabdariItemEntity.getMandehBedehkar();
			totalMandehBestankar += sanadHesabdariItemEntity.getMandehBestankar();		
		}
		return tarazTafsiliAzmayeshi;
	}
	
	private List<SanadHesabdariItemEntity> extractTarazShenavarAzmayeshi(OrganEntity organEntity, Integer level) {
		populateFilterFromRequest();
		List<SanadHesabdariItemEntity> tarazTafsiliAzmayeshi = getMyService().getTarazTafsiliShenavarAzmayeshi(getSelectedSaalMaali(), getFromDate(), getToDate(), getHesabKolIds(),getMoeenIds(), getTafsiliIds(), getArticleTafsiliIds(), getAccountingMarkazIds(),null,organEntity, getFromSerial(), getToSerial(), getSanadhesabdariItemFilter(), level);
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : tarazTafsiliAzmayeshi) {
			totalBedehkar += sanadHesabdariItemEntity.getBedehkar();
			totalBestankar += sanadHesabdariItemEntity.getBestankar();
			totalMandehBedehkar += sanadHesabdariItemEntity.getMandehBedehkar();
			totalMandehBestankar += sanadHesabdariItemEntity.getMandehBestankar();		
		}
		return tarazTafsiliAzmayeshi;
	}
	
	private List<SanadHesabdariItemEntity> extractTarazAccountingMarkazAzmayeshi(OrganEntity organEntity) {
		populateFilterFromRequest();
		List<SanadHesabdariItemEntity> tarazAccountingMarkazAzmayeshi = getMyService().getTarazAccountingMarkazAzmayeshi(getSelectedSaalMaali(), getFromDate(), getToDate(), getHesabKolIds(),getMoeenIds(), getTafsiliIds(), getArticleTafsiliIds(), getAccountingMarkazIds(),null,organEntity, getFromSerial(), getToSerial(), getSanadhesabdariItemFilter());
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : tarazAccountingMarkazAzmayeshi) {
			totalBedehkar += sanadHesabdariItemEntity.getBedehkar();
			totalBestankar += sanadHesabdariItemEntity.getBestankar();
			totalMandehBedehkar += sanadHesabdariItemEntity.getMandehBedehkar();
			totalMandehBestankar += sanadHesabdariItemEntity.getMandehBestankar();		
		}
		return tarazAccountingMarkazAzmayeshi;
	}
	
	private List<SanadHesabdariItemEntity> extractTarazAccountingMarkazShenavarAzmayeshi(OrganEntity organEntity) {
		populateFilterFromRequest();
		List<SanadHesabdariItemEntity> tarazAccountingMarkazAzmayeshi = getMyService().getTarazAccountingMarkazShenavarAzmayeshi(getSelectedSaalMaali(), getFromDate(), getToDate(), getHesabKolIds(),getMoeenIds(), getTafsiliIds(), getArticleTafsiliIds(), getAccountingMarkazIds(),null,organEntity, getFromSerial(), getToSerial(), getSanadhesabdariItemFilter());
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : tarazAccountingMarkazAzmayeshi) {
			totalBedehkar += sanadHesabdariItemEntity.getBedehkar();
			totalBestankar += sanadHesabdariItemEntity.getBestankar();
			totalMandehBedehkar += sanadHesabdariItemEntity.getMandehBedehkar();
			totalMandehBestankar += sanadHesabdariItemEntity.getMandehBestankar();		
		}
		return tarazAccountingMarkazAzmayeshi;
	}
	
	public List<SanadHesabdariItemEntity> getTarazKolDarayi() {
		return getMyService().getTarazKolAzmayeshi(getSelectedSaalMaali(), getFromDate(), getToDate(), getHesabKolIds(),getMoeenIds(), getTafsiliIds(), getArticleTafsiliIds(), getAccountingMarkazIds(),HesabTypeEnum.ASSET,getCurrentOrgan(), getFromSerial(), getToSerial(), getSanadhesabdariItemFilter());
//		return getMyService().getTarazKolAzmayeshi(getSelectedSaalMaali(), getHesabKolIds(), getFromDate(), getToDate(), HesabTypeEnum.ASSET,getCurrentOrgan());
	}
	
	public List<SanadHesabdariItemEntity> getTarazKolBedehi() {
		return getMyService().getTarazKolAzmayeshi(getSelectedSaalMaali(), getFromDate(), getToDate(), getHesabKolIds(),getMoeenIds(), getTafsiliIds(), getArticleTafsiliIds(), getAccountingMarkazIds(),HesabTypeEnum.LIABILITY,getCurrentOrgan(), getFromSerial(), getToSerial(), getSanadhesabdariItemFilter());
//		return getMyService().getTarazKolAzmayeshi(getSelectedSaalMaali(), getHesabKolIds(), getFromDate(), getToDate(), HesabTypeEnum.LIABILITY,getCurrentOrgan());
	}
	
	public List<SanadHesabdariItemEntity> getTarazKolSarmaye() {
		return getMyService().getTarazKolAzmayeshi(getSelectedSaalMaali(), getFromDate(), getToDate(), getHesabKolIds(),getMoeenIds(), getTafsiliIds(), getArticleTafsiliIds(), getAccountingMarkazIds(),HesabTypeEnum.EQUITY,getCurrentOrgan(), getFromSerial(), getToSerial(), getSanadhesabdariItemFilter());
//		return getMyService().getTarazKolAzmayeshi(getSelectedSaalMaali(), getHesabKolIds(), getFromDate(), getToDate(), HesabTypeEnum.EQUITY,getCurrentOrgan());
	}
	
	public List<SanadHesabdariItemEntity> getIncomeGroupByHesabKol( ){
		return getMyService().getTarazKolAzmayeshi(getSelectedSaalMaali(), getFromDate(), getToDate(), getHesabKolIds(),getMoeenIds(), getTafsiliIds(), getArticleTafsiliIds(), getAccountingMarkazIds(),HesabTypeEnum.INCOME,getCurrentOrgan(), getFromSerial(), getToSerial(), getSanadhesabdariItemFilter());
//		return getMyService().getTarazKolAzmayeshi(getSelectedSaalMaali(), getHesabKolIds(), getFromDate(), getToDate(), HesabTypeEnum.INCOME,getCurrentOrgan());
	}
	
	public List<SanadHesabdariItemEntity> getIncomeGroupByHesabMoeen( ){
		return getMyService().getTarazMoeenAzmayeshi(getSelectedSaalMaali(), getFromDate(), getToDate(), getHesabKolIds(),getMoeenIds(), getTafsiliIds(), getArticleTafsiliIds(), getAccountingMarkazIds(),HesabTypeEnum.INCOME,getCurrentOrgan(), getFromSerial(), getToSerial(), getSanadhesabdariItemFilter());
//		return getMyService().getTarazKolAzmayeshi(getSelectedSaalMaali(), getHesabKolIds(), getFromDate(), getToDate(), HesabTypeEnum.INCOME,getCurrentOrgan());
	}
	
	public List<SanadHesabdariItemEntity> getIncomeGroupByHesabTafsili( ){
		return getMyService().getTarazTafsiliAzmayeshi(getSelectedSaalMaali(), getFromDate(), getToDate(), getHesabKolIds(),getMoeenIds(), getTafsiliIds(), getArticleTafsiliIds(), getAccountingMarkazIds(),HesabTypeEnum.INCOME,getCurrentOrgan(), getFromSerial(), getToSerial(), getSanadhesabdariItemFilter());
//		return getMyService().getTarazKolAzmayeshi(getSelectedSaalMaali(), getHesabKolIds(), getFromDate(), getToDate(), HesabTypeEnum.INCOME,getCurrentOrgan());
	}
	
	public List<SanadHesabdariItemEntity> getExpenseGroupByHesabKol(){
		return getMyService().getTarazKolAzmayeshi(getSelectedSaalMaali(), getFromDate(), getToDate(), getHesabKolIds(),getMoeenIds(), getTafsiliIds(), getArticleTafsiliIds(), getAccountingMarkazIds(),HesabTypeEnum.EXPENSE,getCurrentOrgan(), getFromSerial(), getToSerial(), getSanadhesabdariItemFilter());
//		return getMyService().getTarazKolAzmayeshi(getSelectedSaalMaali(), getHesabKolIds(), getFromDate(), getToDate(), HesabTypeEnum.EXPENSE,getCurrentOrgan());
	}

	public List<SanadHesabdariItemEntity> getExpenseGroupByHesabMoeen(){
		return getMyService().getTarazMoeenAzmayeshi(getSelectedSaalMaali(), getFromDate(), getToDate(), getHesabKolIds(),getMoeenIds(), getTafsiliIds(), getArticleTafsiliIds(), getAccountingMarkazIds(),HesabTypeEnum.EXPENSE,getCurrentOrgan(), getFromSerial(), getToSerial(), getSanadhesabdariItemFilter());
//		return getMyService().getTarazKolAzmayeshi(getSelectedSaalMaali(), getHesabKolIds(), getFromDate(), getToDate(), HesabTypeEnum.EXPENSE,getCurrentOrgan());
	}
	
	public List<SanadHesabdariItemEntity> getExpenseGroupByHesabTafsili(){
		return getMyService().getTarazTafsiliAzmayeshi(getSelectedSaalMaali(), getFromDate(), getToDate(), getHesabKolIds(),getMoeenIds(), getTafsiliIds(), getArticleTafsiliIds(), getAccountingMarkazIds(),HesabTypeEnum.EXPENSE,getCurrentOrgan(), getFromSerial(), getToSerial(), getSanadhesabdariItemFilter());
//		return getMyService().getTarazKolAzmayeshi(getSelectedSaalMaali(), getHesabKolIds(), getFromDate(), getToDate(), HesabTypeEnum.EXPENSE,getCurrentOrgan());
	}
	
	
	public Double getSoodeNaVijeh(){
		Double soodeVijeh = 0D;
		// daramad bestankar ast
		if(getIncomeGroupByHesabKol()!=null)
			for (SanadHesabdariItemEntity income : getIncomeGroupByHesabKol()) {
				Double bedehkar = income.getBedehkar();
				Double bestankar = income.getBestankar();
				if(bedehkar!=null)
					soodeVijeh -= bedehkar;
				if(bestankar!=null)
					soodeVijeh += bestankar;
			}
		// hazine bedehkar ast
		if(getExpenseGroupByHesabKol()!=null)
			for (SanadHesabdariItemEntity expense : getExpenseGroupByHesabKol()) {
				Double bedehkar = expense.getBedehkar();
				Double bestankar = expense.getBestankar();
				if(bedehkar!=null)
					soodeVijeh -= bedehkar;
				if(bestankar!=null)
					soodeVijeh += bestankar;
			}
		return soodeVijeh;
	}
	
	public Boolean getIsCreated() {
		return null;
	}
	
	public String printDaftarMoeen() {
		
		createDaftarLocalFilter(getCurrentOrgan());
		String organName = getCurrentOrgan().getName();
		return printDaftarMoeen(organName);
	}
	
	public String printHierarchicalDaftarMoeen() {
		
		createDaftarHierarchicalFilter();
		String organName = getHierarchicalOrganName();
		//setDefaultSortCol("sanadHesabdari.tarikhSanad");
		return printDaftarMoeen(organName);
	}



	private String printDaftarMoeen(String organName) {
		setDefaultSortType(true);
		
		List<String> orderByCols = Arrays.asList("hesabMoeen.id","sanadHesabdari.tarikhSanad","sanadHesabdari.tempSerial","id");
		List<SanadHesabdariItemEntity> daftarMoeenList = getMyService().getDataList(null, getFilter(), orderByCols, getDefaultSortType(), FlushMode.MANUAL, false);
		setFromDate((Date) getFilter().get("sanadHesabdari.tarikhSanad@ge"));
		setToDate((Date) getFilter().get("sanadHesabdari.tarikhSanad@le"));		
		Map<String, Object> parameters = populateReportParameters(organName);
		
		String reportPath = getLocalFilePath("/WEB-INF/classes/report/daftarMoeen.jrxml");
		
		JasperReport jasperReport;
		try {
			jasperReport = JasperCompileManager.compileReport(reportPath);
			
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(daftarMoeenList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			
			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			String contentType = "application/pdf";
			downloadStream(pdf, contentType, SerajMessageUtil.getMessage("daftarMoeen")+"_"+ organName+".pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String printMonthlySummaryDaftarKol(){
		createDaftarSummaryDaftarLocalFilter(getCurrentOrgan());
		String organName = getCurrentOrgan().getName();
		return printMonthlySummaryDaftarKol(organName);
		
	}
	
	public String printDaftarKol() {
		
		createDaftarLocalFilter(getCurrentOrgan());
		String organName = getCurrentOrgan().getName();
		return printDaftarKol(organName);
	}
	
	public String printHierarchicalDaftarKol() {
		
		createDaftarHierarchicalFilter();
		String organName = getHierarchicalOrganName();
		//setDefaultSortCol("sanadHesabdari.tarikhSanad");
		return printDaftarKol(organName);
	}



	private String printDaftarKol(String organName) {
		setDefaultSortType(true);
		
		List<String> orderByCols = Arrays.asList("hesabKol.id","sanadHesabdari.tarikhSanad","sanadHesabdari.tempSerial","id");
		List<SanadHesabdariItemEntity> daftarKolList = getMyService().getDataList(null, getFilter(), orderByCols, getDefaultSortType(), FlushMode.MANUAL, false);
		List<SanadHesabdariItemEntity> daftarKolListSortedByMahiat = getDaftarKolListSortedByMahiat(daftarKolList);
		return printDaftarKol(organName, daftarKolListSortedByMahiat);
	}

	
	private String printMonthlySummaryDaftarKol(String organName) {
		setDefaultSortType(true);
		
		List<String> orderByCols = Arrays.asList("hesabKol.id","sanadHesabdari.tarikhSanad","sanadHesabdari.serial","id");
		List<SanadHesabdariItemEntity> daftarKolList = getMyService().getDataList(null, getFilter(), orderByCols, getDefaultSortType(), FlushMode.MANUAL, false);
		List<SanadHesabdariItemEntity> daftarKolListSortedByMahiat = getDaftarKolSummaryListSortedByMahiat(daftarKolList);
		return printDaftarKol(organName, daftarKolListSortedByMahiat);
	}

	private String printDaftarKol(String organName, List<SanadHesabdariItemEntity> sanadHesabdariItemEntities) {
		setFromDate((Date) getFilter().get("sanadHesabdari.tarikhSanad@ge"));
		setToDate((Date) getFilter().get("sanadHesabdari.tarikhSanad@le"));
		Map<String, Object> parameters = populateReportParameters(organName);
		
		String reportPath = getLocalFilePath("/WEB-INF/classes/report/daftarKol.jrxml"); 
		
		JasperReport jasperReport;
		try {
			jasperReport = JasperCompileManager.compileReport(reportPath);
			
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(sanadHesabdariItemEntities);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			
			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			String contentType = "application/pdf";
			downloadStream(pdf, contentType,SerajMessageUtil.getMessage("daftarKol")+"_"+ organName+".pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}
		
		return null;
	}




	private List<SanadHesabdariItemEntity> getDaftarKolListSortedByMahiat(List<SanadHesabdariItemEntity> daftarKolList) {
		List<SanadHesabdariItemEntity> daftarKolListSortedByMahiat = new ArrayList<>(); 
		
		List<SanadHesabdariItemEntity> firstList = new ArrayList<>();
		List<SanadHesabdariItemEntity> lastList = new ArrayList<>();
		
		Long tempSerial = null; 
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : daftarKolList) {
			Long currentTempSerial = sanadHesabdariItemEntity.getSanadHesabdari().getTempSerial();
			if(tempSerial == null)
				tempSerial = currentTempSerial;

			if(tempSerial != currentTempSerial){
				daftarKolListSortedByMahiat.addAll(firstList);
				daftarKolListSortedByMahiat.addAll(lastList);
				firstList = new ArrayList<>();
				lastList =new ArrayList<>();
				tempSerial = currentTempSerial;
			}
			
			if(sanadHesabdariItemEntity.getHesabKol().getMahyatKol().equals(MahyatKolEnum.Bedehkar) && sanadHesabdariItemEntity.getBedehkar() > 0)
				firstList.add(sanadHesabdariItemEntity);
			else if(sanadHesabdariItemEntity.getHesabKol().getMahyatKol().equals(MahyatKolEnum.Bestankar) && sanadHesabdariItemEntity.getBestankar() > 0)
				firstList.add(sanadHesabdariItemEntity);
			else if(sanadHesabdariItemEntity.getHesabKol().getMahyatKol().equals(MahyatKolEnum.Bestankar) && sanadHesabdariItemEntity.getBedehkar() > 0)
				lastList.add(sanadHesabdariItemEntity);
			else if(sanadHesabdariItemEntity.getHesabKol().getMahyatKol().equals(MahyatKolEnum.Bedehkar) && sanadHesabdariItemEntity.getBestankar() > 0)
				lastList.add(sanadHesabdariItemEntity);
		}
		
		daftarKolListSortedByMahiat.addAll(firstList);
		daftarKolListSortedByMahiat.addAll(lastList);
		
		return daftarKolListSortedByMahiat;
	}
	
	private List<SanadHesabdariItemEntity> getDaftarKolSummaryListSortedByMahiat(List<SanadHesabdariItemEntity> daftarKolList) {
		List<SanadHesabdariItemEntity> daftarKolListSortedByMahiat = new ArrayList<>(); 
		
		List<SanadHesabdariItemEntity> firstList = new ArrayList<>();
		List<SanadHesabdariItemEntity> lastList = new ArrayList<>();
		
		Long serial = null; 
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : daftarKolList) {
			Long currentSerial = sanadHesabdariItemEntity.getSanadHesabdari().getSerial();
			if(serial == null)
				serial = currentSerial;
			
			if(serial != currentSerial){
				daftarKolListSortedByMahiat.addAll(firstList);
				daftarKolListSortedByMahiat.addAll(lastList);
				firstList = new ArrayList<>();
				lastList =new ArrayList<>();
				serial = currentSerial;
			}
			
			if(sanadHesabdariItemEntity.getHesabKol().getMahyatKol().equals(MahyatKolEnum.Bedehkar) && sanadHesabdariItemEntity.getBedehkar() > 0)
				firstList.add(sanadHesabdariItemEntity);
			else if(sanadHesabdariItemEntity.getHesabKol().getMahyatKol().equals(MahyatKolEnum.Bestankar) && sanadHesabdariItemEntity.getBestankar() > 0)
				firstList.add(sanadHesabdariItemEntity);
			else if(sanadHesabdariItemEntity.getHesabKol().getMahyatKol().equals(MahyatKolEnum.Bestankar) && sanadHesabdariItemEntity.getBedehkar() > 0)
				lastList.add(sanadHesabdariItemEntity);
			else if(sanadHesabdariItemEntity.getHesabKol().getMahyatKol().equals(MahyatKolEnum.Bedehkar) && sanadHesabdariItemEntity.getBestankar() > 0)
				lastList.add(sanadHesabdariItemEntity);
		}
		
		daftarKolListSortedByMahiat.addAll(firstList);
		daftarKolListSortedByMahiat.addAll(lastList);
		
		return daftarKolListSortedByMahiat;
	}
	
	public String printMonthlySummaryDaftarRooznameh() {
		
		createDaftarSummaryDaftarLocalFilter(getCurrentOrgan());
		String organName = getCurrentOrgan().getName();
		return printMonthlySummaryDaftarRooznameh(organName);
	}
	
	public String printDaftarRooznameh() {
		
		createDaftarLocalFilter(getCurrentOrgan());
		String organName = getCurrentOrgan().getName();
		return printDaftarRooznameh(organName);
	}
	
	public String printHierarchicalDaftarRooznameh() {
		
		createDaftarHierarchicalFilter();
		String organName = getHierarchicalOrganName();
//		setDefaultSortCol("sanadHesabdari.tarikhSanad");
		return printDaftarRooznameh(organName);
	}



	private String printDaftarRooznameh(String organName) {
		setDefaultSortType(true);
		
		List<String> orderByCols = Arrays.asList("sanadHesabdari.tarikhSanad","sanadHesabdari.tempSerial","id");
		List<SanadHesabdariItemEntity> daftarRooznamehList = getMyService().getDataList(null, getFilter(), orderByCols, getDefaultSortType(), FlushMode.MANUAL, false);
		setFromDate((Date) getFilter().get("sanadHesabdari.tarikhSanad@ge"));
		setToDate((Date) getFilter().get("sanadHesabdari.tarikhSanad@le"));		
		Map<String, Object> parameters = populateReportParameters(organName);
		
		String reportPath = getLocalFilePath("/WEB-INF/classes/report/daftarRooznameh.jrxml");
		
		JasperReport jasperReport;
		try {
			jasperReport = JasperCompileManager.compileReport(reportPath);
			
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(daftarRooznamehList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			
			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			String contentType = "application/pdf";
			downloadStream(pdf, contentType, SerajMessageUtil.getMessage("daftarRooznameh")+"_"+ organName+".pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String printMonthlySummaryDaftarRooznameh(String organName) {
		setDefaultSortType(true);
		
		List<String> orderByCols = Arrays.asList("sanadHesabdari.tarikhSanad","hesabKol.id");
		List<SanadHesabdariItemEntity> daftarRooznamehList = getMyService().getDataList(null, getFilter(), orderByCols, getDefaultSortType(), FlushMode.MANUAL, false);
		
		Collections.sort(daftarRooznamehList, new Comparator<SanadHesabdariItemEntity>() {
		    public int compare(SanadHesabdariItemEntity m1, SanadHesabdariItemEntity m2) {
		    	int myInt=0;
		    	try{
			    	if (m1.getSanadHesabdari().getTarikhSanad()!=null &&  m2.getSanadHesabdari().getTarikhSanad()!=null) 
			    		if (m1.getSanadHesabdari().getTarikhSanad().equals(m2.getSanadHesabdari().getTarikhSanad())){
			    			if(m1.getBestankar() > 0 && m2.getBestankar() > 0)
			    				myInt = (m1.getHesabKol().getId()).compareTo( m2.getHesabKol().getId()) ;
			    			else if(m1.getBedehkar() > 0 && m2.getBedehkar() > 0)
			    				myInt = (m1.getHesabKol().getId()).compareTo( m2.getHesabKol().getId());
			    			else
			    				myInt = (m1.getBestankar()).compareTo( m2.getBestankar()) ;
			    		}
		        
		    	}catch (Exception e) {
					e.printStackTrace();
				}
		    	return  myInt ;
		    }
		});
		
//		Collections.sort(daftarRooznamehList, new Comparator<SanadHesabdariItemEntity>() {
//			public int compare(SanadHesabdariItemEntity m1, SanadHesabdariItemEntity m2) {
//				int myInt=0;
//				try{
//					if (m1.getSanadHesabdari().getTarikhSanad()!=null &&  m2.getSanadHesabdari().getTarikhSanad()!=null) 
//						if (m1.getSanadHesabdari().getTarikhSanad().equals(m2.getSanadHesabdari().getTarikhSanad())) 
//							myInt = (m1.getHesabKol().getCode()).compareTo( m2.getHesabKol().getCode()) ;
//					
//				}catch (Exception e) {
//					e.printStackTrace();
//				}
//				return  myInt ;
//			}
//		});
		
		setFromDate((Date) getFilter().get("sanadHesabdari.tarikhSanad@ge"));
		setToDate((Date) getFilter().get("sanadHesabdari.tarikhSanad@le"));		
		Map<String, Object> parameters = populateReportParameters(organName);
		
		String reportPath = getLocalFilePath("/WEB-INF/classes/report/daftarRooznamehKolSummary.jrxml");
		
		JasperReport jasperReport;
		try {
			jasperReport = JasperCompileManager.compileReport(reportPath);
			
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(daftarRooznamehList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			
			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			String contentType = "application/pdf";
			downloadStream(pdf, contentType, SerajMessageUtil.getMessage("monthlySummaryDaftarRooznameh")+"_"+ organName+".pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public String printDaftarTafsili() {

		createDaftarLocalFilter(getCurrentOrgan());
		String organName = getCurrentOrgan().getName();
		//setDefaultSortCol("sanadHesabdari.tarikhSanad");
		return printDaftarTafsili(organName);
	}


	public String printHierarchicalDaftarTafsili() {
		
		createDaftarHierarchicalFilter();
		String organName = getHierarchicalOrganName();
		return printDaftarTafsili(organName);
	}

	private String printDaftarTafsili(String organName) {
		setDefaultSortType(true);
		
		List<String> orderByCols = Arrays.asList("hesabTafsili.id","sanadHesabdari.tarikhSanad","sanadHesabdari.tempSerial","id");
		List<SanadHesabdariItemEntity> daftarTafsiliList = getMyService().getDataList(null, getFilter(), orderByCols, getDefaultSortType(), FlushMode.MANUAL, false);
		
		setFromDate((Date) getFilter().get("sanadHesabdari.tarikhSanad@ge"));
		setToDate((Date) getFilter().get("sanadHesabdari.tarikhSanad@le"));		
		Map<String, Object> parameters = populateReportParameters(organName);

		String reportPath = getLocalFilePath("/WEB-INF/classes/report/daftarTafsili.jrxml");

		JasperReport jasperReport;
		try {
			jasperReport = JasperCompileManager.compileReport(reportPath);

			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(daftarTafsiliList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);

			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			String contentType = "application/pdf";
			downloadStream(pdf, contentType, SerajMessageUtil.getMessage("daftarTafsili")+"_"+ organName+".pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}

		return null;
	}

	


//	private List<SanadHesabdariItemEntity> getLocalDetailedList() {
//		createDaftarLocalFilter(getCurrentOrgan());
//		//setDefaultSortCol("sanadHesabdari.tarikhSanad");
//		setDefaultSortType(true);
//		
//		List<String> orderByCols = Arrays.asList("sanadHesabdari.tarikhSanad","sanadHesabdari.tempSerial","id");
//		List<SanadHesabdariItemEntity> daftarTafsiliList = getMyService().getDataList(null, getFilter(), orderByCols, getDefaultSortType(), FlushMode.MANUAL, false);
//		return daftarTafsiliList;
//	}
//	
	public String printDaftarAccountingMarkaz() {
		
		createDaftarLocalFilter(getCurrentOrgan());
		String organName = getCurrentOrgan().getName();
		return printDaftarAccountingMarkaz(organName);
	}
	public String printHierarchicalDaftarAccountingMarkaz() {
		
		createDaftarHierarchicalFilter();
		String organName = getHierarchicalOrganName();
		//setDefaultSortCol("sanadHesabdari.tarikhSanad");
		return printDaftarAccountingMarkaz(organName);
	}



	private String printDaftarAccountingMarkaz(String organName) {
		setDefaultSortType(true);
		
		List<String> orderByCols = Arrays.asList("accountingMarkaz.id","sanadHesabdari.tarikhSanad","sanadHesabdari.tempSerial","id");
		List<SanadHesabdariItemEntity> daftarAccountingMarkazList = getMyService().getDataList(null, getFilter(), orderByCols, getDefaultSortType(), FlushMode.MANUAL, false);
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : daftarAccountingMarkazList) {
			if(sanadHesabdariItemEntity.getAccountingMarkaz()!=null)
				System.out.println(sanadHesabdariItemEntity.getAccountingMarkaz().getCode());
		}
		setFromDate((Date) getFilter().get("sanadHesabdari.tarikhSanad@ge"));
		setToDate((Date) getFilter().get("sanadHesabdari.tarikhSanad@le"));		
		Map<String, Object> parameters = populateReportParameters(organName);
		
		String reportPath = getLocalFilePath("/WEB-INF/classes/report/daftarAccountingMarkaz.jrxml");
		
		JasperReport jasperReport;
		try {
			jasperReport = JasperCompileManager.compileReport(reportPath);
			
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(daftarAccountingMarkazList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			
			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			String contentType = "application/pdf";
			downloadStream(pdf, contentType, SerajMessageUtil.getMessage("daftarAccountingMarkaz")+"_"+ organName+".pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	String columnsCount;
	
	public String getColumnsCount() {
		return columnsCount;
	}

	public void setColumnsCount(String columnsCount) {
		this.columnsCount = columnsCount;
	}
	
	public void exportTarazKolAzmayeshiToExcel() {
		List<String> columnsToShow = Arrays.asList(
				"hesabKolCode",
				"hesabKolName",
				"mandehBedehkarEbtedayDore",
				"mandehBestankarEbtedayDore",
				"bedehkar",
				"bestankar",
				"operationSummaryBedehkar",
				"operationSummaryBestankar",
				"mandehBedehkar",
				"mandehBestankar"/*,
				"state",
				"confirmationDate",
				"pardakhtKonnadeh",
				"mablaghDaryaft",
				"mablaghPardakht",
				"serial"*/);

		
		Map<String, String> columnsLabel = new HashMap<String, String>();
		columnsLabel.put("hesabKolCode", SerajMessageUtil.getMessage("HesabKol_code"));
		columnsLabel.put("hesabKolName", SerajMessageUtil.getMessage("HesabKol_title"));
		columnsLabel.put("mandehBedehkarEbtedayDore", SerajMessageUtil.getMessage("SanadHesabdariItem_mandeh_bedehkar_ebtedaye_dore"));
		columnsLabel.put("mandehBestankarEbtedayDore", SerajMessageUtil.getMessage("SanadHesabdariItem_mandeh_bestankar_ebtedaye_dore"));
		columnsLabel.put("bedehkar", SerajMessageUtil.getMessage("SanadHesabdariItem_bedehkarGardesh"));
		columnsLabel.put("bestankar", SerajMessageUtil.getMessage("SanadHesabdariItem_bestankarGardesh"));
		columnsLabel.put("operationSummaryBedehkar", SerajMessageUtil.getMessage("SanadHesabdariItem_operationSummaryBedehkar"));
		columnsLabel.put("operationSummaryBestankar", SerajMessageUtil.getMessage("SanadHesabdariItem_operationSummaryBestankar"));
		columnsLabel.put("mandehBedehkar", SerajMessageUtil.getMessage("SanadHesabdariItem_mandeh_bedehkar"));
		columnsLabel.put("mandehBestankar", SerajMessageUtil.getMessage("SanadHesabdariItem_mandeh_bestankar"));
		List<SanadHesabdariItemEntity> tarazKolAzmayehiList = extractTarazKolAzmayeshi(getCurrentOrgan());
		try {
			ByteArrayOutputStream byteArrayOutputStream = getMyService().exportSimpleObjectListToXLSX(tarazKolAzmayehiList, columnsToShow, columnsLabel);
			downloadStream(byteArrayOutputStream.toByteArray(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "tarazKolAzmayeshi.xlsx");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MaxExcelRecordExportException e) {
			e.printStackTrace();
		}
		
	}
	public String printLocalTarazKolAzmayeshi() {
		
		String organName = getCurrentOrgan().getName();
		return printTarazKolAzmayeshi(organName, getCurrentOrgan());
	}
	
	public String printHierarchicalTarazKolAzmayeshi() {
		
		OrganEntity selectedOrgan = getSelectedOrganId()!=null ? getOrganService().load(getSelectedOrganId()) : null;
		String organName = getHierarchicalOrganName();
		return printTarazKolAzmayeshi(organName, selectedOrgan);
	}



	private String printTarazKolAzmayeshi(String organName, OrganEntity organEntity) {
		List<SanadHesabdariItemEntity> tarazKolAzmayehiList = extractTarazKolAzmayeshi(organEntity);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("organName", organName); 
		parameters.put("creatorUser", getCurrentUser().getShakhsName()); 
		
		if(getSelectedSaalMaali()!=null)
			parameters.put("saalMaali", getSelectedSaalMaali().getSaal().toString());
		
		parameters.put("fromDate", DateConverter.toShamsiDate(getFromDate(),SerajDateTimePickerType.Date));
		parameters.put("toDate", DateConverter.toShamsiDate(getToDate(),SerajDateTimePickerType.Date));
		parameters.put("creationDate", DateConverter.toShamsiDate(DateConverter.getCurrentDateTime(),SerajDateTimePickerType.DateHorMin));
		
		String reportPath = getLocalFilePath("/WEB-INF/classes/report/taraz/tarazKolAzmayeshi-"+getColumnsCount()+"_col.jrxml");
		
		JasperReport jasperReport;
		try {
			jasperReport = JasperCompileManager.compileReport(reportPath);
			
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(tarazKolAzmayehiList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			
			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			String contentType = "application/pdf";
			downloadStream(pdf, contentType,SerajMessageUtil.getMessage("tarazKolAzmayehi_"+getColumnsCount())+"_"+ organName+".pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}
		logPrintTarazKolAzmayeshiAction();
		return null;
	}
	
	public void logPrintTarazKolAzmayeshiAction() {
		String message = SerajMessageUtil
		
				.getMessage(getEntityName() + "_title");
		try {
			
				ActionLogUtil.logAction(SerajMessageUtil
						.getMessage(SerajMessageUtil.getMessage("TarazKolAzmayeshi_PRINT_SANAD")), message,
						null, null,"");
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void exportTarazMoeenAzmayeshiToExcel() {
		List<String> columnsToShow = Arrays.asList(
				"hesabKolName",
				"hesabMoeenCode",
				"hesabMoeenName",
				"mandehBedehkarEbtedayDore",
				"mandehBestankarEbtedayDore",
				"bedehkar",
				"bestankar",
				"operationSummaryBedehkar",
				"operationSummaryBestankar",
				"mandehBedehkar",
				"mandehBestankar"/*,
				"state",
				"confirmationDate",
				"pardakhtKonnadeh",
				"mablaghDaryaft",
				"mablaghPardakht",
				"serial"*/);

		
		Map<String, String> columnsLabel = new HashMap<String, String>();
		columnsLabel.put("hesabKolName", SerajMessageUtil.getMessage("HesabKol_title"));
		columnsLabel.put("hesabMoeenCode", SerajMessageUtil.getMessage("HesabMoeen_code"));
		columnsLabel.put("hesabMoeenName", SerajMessageUtil.getMessage("HesabMoeen_title"));
		columnsLabel.put("mandehBedehkarEbtedayDore", SerajMessageUtil.getMessage("SanadHesabdariItem_mandeh_bedehkar_ebtedaye_dore"));
		columnsLabel.put("mandehBestankarEbtedayDore", SerajMessageUtil.getMessage("SanadHesabdariItem_mandeh_bestankar_ebtedaye_dore"));
		columnsLabel.put("bedehkar", SerajMessageUtil.getMessage("SanadHesabdariItem_bedehkarGardesh"));
		columnsLabel.put("bestankar", SerajMessageUtil.getMessage("SanadHesabdariItem_bestankarGardesh"));
		columnsLabel.put("operationSummaryBedehkar", SerajMessageUtil.getMessage("SanadHesabdariItem_operationSummaryBedehkar"));
		columnsLabel.put("operationSummaryBestankar", SerajMessageUtil.getMessage("SanadHesabdariItem_operationSummaryBestankar"));
		columnsLabel.put("mandehBedehkar", SerajMessageUtil.getMessage("SanadHesabdariItem_mandeh_bedehkar"));
		columnsLabel.put("mandehBestankar", SerajMessageUtil.getMessage("SanadHesabdariItem_mandeh_bestankar"));
		
		List<SanadHesabdariItemEntity> tarazMoeenAzmayehiList = extractTarazMoeenAzmayeshi(getCurrentOrgan());
		try {
			ByteArrayOutputStream byteArrayOutputStream = getMyService().exportSimpleObjectListToXLSX(tarazMoeenAzmayehiList, columnsToShow, columnsLabel);
			downloadStream(byteArrayOutputStream.toByteArray(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "tarazMoeenAzmayeshi.xlsx");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MaxExcelRecordExportException e) {
			e.printStackTrace();
		}
		
	}
	
	public String printLocalTarazMoeenAzmayeshi() {
		
		String organName = getCurrentOrgan().getName();
		return printTarazMoeenAzmayeshi(organName, getCurrentOrgan());
	}

	
	public String printHierarchicalTarazMoeenAzmayeshi() {
		
		OrganEntity selectedOrgan = getSelectedOrganId()!=null ? getOrganService().load(getSelectedOrganId()) : null;
		String organName = getHierarchicalOrganName();
		return printTarazMoeenAzmayeshi(organName, selectedOrgan);
	}

	private String printTarazMoeenAzmayeshi(String organName, OrganEntity organEntity) {
		List<SanadHesabdariItemEntity> tarazMoeenAzmayehiList = extractTarazMoeenAzmayeshi(organEntity);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("organName", organName);
		parameters.put("creatorUser", getCurrentUser().getShakhsName()); 
		
		if(getSelectedSaalMaali()!=null)
			parameters.put("saalMaali", getSelectedSaalMaali().getSaal().toString());
		
		parameters.put("fromDate", DateConverter.toShamsiDate(getFromDate(),SerajDateTimePickerType.Date));
		parameters.put("toDate", DateConverter.toShamsiDate(getToDate(),SerajDateTimePickerType.Date));
		parameters.put("creationDate", DateConverter.toShamsiDate(DateConverter.getCurrentDateTime(),SerajDateTimePickerType.DateHorMin));
		List<Long> ids = getHesabKolIds();
		String hesabKolNames="";
		for (Long kolId : ids) {
			hesabKolNames=hesabKolNames+getHesabKolService().load(kolId)+" - ";
		}
		parameters.put("hesabKol", hesabKolNames);
		
		String reportPath = getLocalFilePath("/WEB-INF/classes/report/taraz/tarazMoeenAzmayeshi-"+getColumnsCount()+"_col.jrxml");
		
		JasperReport jasperReport;
		try {
			jasperReport = JasperCompileManager.compileReport(reportPath);
			
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(tarazMoeenAzmayehiList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			
			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			String contentType = "application/pdf";
			downloadStream(pdf, contentType,SerajMessageUtil.getMessage("tarazMoeenAzmayehi_"+getColumnsCount())+"_"+ organName+".pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}
		logPrintTarazMoeenAzmayeshiAction();
		
		return null;
	}

	
	public void logPrintTarazMoeenAzmayeshiAction() {
		String message = SerajMessageUtil
		
				.getMessage(getEntityName() + "_title");
		try {
			
				ActionLogUtil.logAction(SerajMessageUtil
						.getMessage(SerajMessageUtil.getMessage("TarazMoeenAzmayeshi_PRINT_SANAD")), message,
						null, null,"");
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void exportTarazTafsiliAzmayeshiToExcel() {
		List<String> columnsToShow = Arrays.asList(
				"hesabTafsiliCode",
				"hesabTafsiliName",
				"mandehBedehkarEbtedayDore",
				"mandehBestankarEbtedayDore",
				"bedehkar",
				"bestankar",
				"operationSummaryBedehkar",
				"operationSummaryBestankar",
				"mandehBedehkar",
				"mandehBestankar"/*,
				"state",
				"confirmationDate",
				"pardakhtKonnadeh",
				"mablaghDaryaft",
				"mablaghPardakht",
				"serial"*/);

		
		Map<String, String> columnsLabel = new HashMap<String, String>();
		columnsLabel.put("hesabTafsiliCode", SerajMessageUtil.getMessage("HesabTafsili_code"));
		columnsLabel.put("hesabTafsiliName", SerajMessageUtil.getMessage("HesabTafsili_title"));
		columnsLabel.put("mandehBedehkarEbtedayDore", SerajMessageUtil.getMessage("SanadHesabdariItem_mandeh_bedehkar_ebtedaye_dore"));
		columnsLabel.put("mandehBestankarEbtedayDore", SerajMessageUtil.getMessage("SanadHesabdariItem_mandeh_bestankar_ebtedaye_dore"));
		columnsLabel.put("bedehkar", SerajMessageUtil.getMessage("SanadHesabdariItem_bedehkarGardesh"));
		columnsLabel.put("bestankar", SerajMessageUtil.getMessage("SanadHesabdariItem_bestankarGardesh"));
		columnsLabel.put("operationSummaryBedehkar", SerajMessageUtil.getMessage("SanadHesabdariItem_operationSummaryBedehkar"));
		columnsLabel.put("operationSummaryBestankar", SerajMessageUtil.getMessage("SanadHesabdariItem_operationSummaryBestankar"));
		columnsLabel.put("mandehBedehkar", SerajMessageUtil.getMessage("SanadHesabdariItem_mandeh_bedehkar"));
		columnsLabel.put("mandehBestankar", SerajMessageUtil.getMessage("SanadHesabdariItem_mandeh_bestankar"));
		List<SanadHesabdariItemEntity> tarazTafsiliAzmayehiList = extractTarazTafsiliAzmayeshi(getCurrentOrgan());
		try {
			ByteArrayOutputStream byteArrayOutputStream = getMyService().exportSimpleObjectListToXLSX(tarazTafsiliAzmayehiList, columnsToShow, columnsLabel);
			downloadStream(byteArrayOutputStream.toByteArray(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "tarazTafsiliAzmayeshi.xlsx");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MaxExcelRecordExportException e) {
			e.printStackTrace();
		}
		
	}
	public void exportTarazShenavarAzmayeshiToExcel() {
		List<String> columnsToShow = Arrays.asList(
				"hesabTafsiliCode",
				"hesabTafsiliName",
				"mandehBedehkarEbtedayDore",
				"mandehBestankarEbtedayDore",
				"bedehkar",
				"bestankar",
				"operationSummaryBedehkar",
				"operationSummaryBestankar",
				"mandehBedehkar",
				"mandehBestankar"/*,
				"state",
				"confirmationDate",
				"pardakhtKonnadeh",
				"mablaghDaryaft",
				"mablaghPardakht",
				"serial"*/);
		
		
		Map<String, String> columnsLabel = new HashMap<String, String>();
		columnsLabel.put("hesabTafsiliCode", SerajMessageUtil.getMessage("HesabTafsili_code"));
		columnsLabel.put("hesabTafsiliName", SerajMessageUtil.getMessage("HesabTafsili_title"));
		columnsLabel.put("mandehBedehkarEbtedayDore", SerajMessageUtil.getMessage("SanadHesabdariItem_mandeh_bedehkar_ebtedaye_dore"));
		columnsLabel.put("mandehBestankarEbtedayDore", SerajMessageUtil.getMessage("SanadHesabdariItem_mandeh_bestankar_ebtedaye_dore"));
		columnsLabel.put("bedehkar", SerajMessageUtil.getMessage("SanadHesabdariItem_bedehkarGardesh"));
		columnsLabel.put("bestankar", SerajMessageUtil.getMessage("SanadHesabdariItem_bestankarGardesh"));
		columnsLabel.put("operationSummaryBedehkar", SerajMessageUtil.getMessage("SanadHesabdariItem_operationSummaryBedehkar"));
		columnsLabel.put("operationSummaryBestankar", SerajMessageUtil.getMessage("SanadHesabdariItem_operationSummaryBestankar"));
		columnsLabel.put("mandehBedehkar", SerajMessageUtil.getMessage("SanadHesabdariItem_mandeh_bedehkar"));
		columnsLabel.put("mandehBestankar", SerajMessageUtil.getMessage("SanadHesabdariItem_mandeh_bestankar"));
		List<SanadHesabdariItemEntity> tarazTafsiliAzmayehiList = extractTarazShenavarAzmayeshi(getCurrentOrgan(), getArticleTafsiliLevel());
		try {
			ByteArrayOutputStream byteArrayOutputStream = getMyService().exportSimpleObjectListToXLSX(tarazTafsiliAzmayehiList, columnsToShow, columnsLabel);
			downloadStream(byteArrayOutputStream.toByteArray(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "tarazShenavarAzmayeshi.xlsx");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MaxExcelRecordExportException e) {
			e.printStackTrace();
		}
		
	}
	public void exportTarazAccountingMarkazAzmayeshiToExcel() {
		List<String> columnsToShow = Arrays.asList(
				"accountingMarkazCode",
				"accountingMarkazName",
				"mandehBedehkarEbtedayDore",
				"mandehBestankarEbtedayDore",
				"bedehkar",
				"bestankar",
				"operationSummaryBedehkar",
				"operationSummaryBestankar",
				"mandehBedehkar",
				"mandehBestankar"/*,
				"state",
				"confirmationDate",
				"pardakhtKonnadeh",
				"mablaghDaryaft",
				"mablaghPardakht",
				"serial"*/);
		
		
		Map<String, String> columnsLabel = new HashMap<String, String>();
		columnsLabel.put("accountingMarkazCode", SerajMessageUtil.getMessage("AccountingMarkaz_code"));
		columnsLabel.put("accountingMarkazName", SerajMessageUtil.getMessage("AccountingMarkaz_title"));
		columnsLabel.put("mandehBedehkarEbtedayDore", SerajMessageUtil.getMessage("SanadHesabdariItem_mandeh_bedehkar_ebtedaye_dore"));
		columnsLabel.put("mandehBestankarEbtedayDore", SerajMessageUtil.getMessage("SanadHesabdariItem_mandeh_bestankar_ebtedaye_dore"));
		columnsLabel.put("bedehkar", SerajMessageUtil.getMessage("SanadHesabdariItem_bedehkarGardesh"));
		columnsLabel.put("bestankar", SerajMessageUtil.getMessage("SanadHesabdariItem_bestankarGardesh"));
		columnsLabel.put("operationSummaryBedehkar", SerajMessageUtil.getMessage("SanadHesabdariItem_operationSummaryBedehkar"));
		columnsLabel.put("operationSummaryBestankar", SerajMessageUtil.getMessage("SanadHesabdariItem_operationSummaryBestankar"));
		columnsLabel.put("mandehBedehkar", SerajMessageUtil.getMessage("SanadHesabdariItem_mandeh_bedehkar"));
		columnsLabel.put("mandehBestankar", SerajMessageUtil.getMessage("SanadHesabdariItem_mandeh_bestankar"));
		List<SanadHesabdariItemEntity> tarazTafsiliAzmayehiList = extractTarazTafsiliAzmayeshi(getCurrentOrgan());
		try {
			ByteArrayOutputStream byteArrayOutputStream = getMyService().exportSimpleObjectListToXLSX(tarazTafsiliAzmayehiList, columnsToShow, columnsLabel);
			downloadStream(byteArrayOutputStream.toByteArray(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "tarazTafsiliAzmayeshi.xlsx");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MaxExcelRecordExportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String printLocalTarazTafsiliAzmayeshi() {
		
		OrganEntity currentOrgan = getCurrentOrgan();
		return printTarazTafsiliAzmayeshi(currentOrgan.getName(), currentOrgan);
	}
	public String printHierarchicalTarazTafsiliAzmayeshi() {
		
		OrganEntity selectedOrgan = getSelectedOrganId()!=null ? getOrganService().load(getSelectedOrganId()) : null;
		String organName = getHierarchicalOrganName();
		return printTarazTafsiliAzmayeshi(organName, selectedOrgan);
	}



	private String printTarazTafsiliAzmayeshi(String organName, OrganEntity organEntity) {
		List<SanadHesabdariItemEntity> tarazTafsiliAzmayehiList = extractTarazTafsiliAzmayeshi(organEntity);
		Map<String, Object> parameters = populateReportParameters(organName);
		
		String reportPath = getLocalFilePath("/WEB-INF/classes/report/taraz/tarazTafsiliAzmayeshi-"+getColumnsCount()+"_col.jrxml");
		
		JasperReport jasperReport;
		try {
			jasperReport = JasperCompileManager.compileReport(reportPath);
			
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(tarazTafsiliAzmayehiList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			
			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			String contentType = "application/pdf";
			downloadStream(pdf, contentType,SerajMessageUtil.getMessage("tarazTafsiliAzmayeshi_"+getColumnsCount())+"_"+ organName+".pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}
		
		logPrintTarazTafsiliAzmayeshiAction();
		
		return null;
	}
	
	public String printLocalTarazShenavarAzmayeshi() {
		
		String organName = getCurrentOrgan().getName();
		return printTarazShenavarAzmayeshi(organName, getCurrentOrgan());
	}
	
	public String printHierarchicalTarazShenavarAzmayeshi() {
		
		OrganEntity selectedOrgan = getSelectedOrganId()!=null ? getOrganService().load(getSelectedOrganId()) : null;
		String organName = getHierarchicalOrganName();
		return printTarazShenavarAzmayeshi(organName, selectedOrgan);
	}


	private String printTarazShenavarAzmayeshi(String organName, OrganEntity organEntity) {
		List<SanadHesabdariItemEntity> tarazTafsiliAzmayehiList = extractTarazShenavarAzmayeshi(organEntity, getArticleTafsiliLevel());
		Map<String, Object> parameters = populateReportParameters(organName);
		
		String reportPath = getLocalFilePath("/WEB-INF/classes/report/taraz/tarazTafsiliAzmayeshi-"+getColumnsCount()+"_col.jrxml");
		
		JasperReport jasperReport;
		try {
			jasperReport = JasperCompileManager.compileReport(reportPath);
			
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(tarazTafsiliAzmayehiList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			
			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			String contentType = "application/pdf";
			downloadStream(pdf, contentType,SerajMessageUtil.getMessage("tarazShenavarAzmayeshi_"+getColumnsCount())+"_"+ organName+".pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}
		
		logPrintTarazTafsiliAzmayeshiAction();
		
		return null;
	}

	
	public String printLocalTarazAccountingMarkazAzmayeshi() {
		
		OrganEntity organEntity = getCurrentOrgan();
		String organName = organEntity.getName();
		return printTarazAccountingMarkazAzmayeshi(organName, organEntity);
	}
	
	public String printLocalTarazAccountingMarkazShenavarAzmayeshi() {
		
		OrganEntity organEntity = getCurrentOrgan();
		String organName = organEntity.getName();
		return printTarazAccountingMarkazShenavarAzmayeshi(organName, organEntity);
	}
	
	public String printHierarchicalTarazAccountingMarkazAzmayeshi() {
		
		OrganEntity selectedOrgan = getSelectedOrganId()!=null ? getOrganService().load(getSelectedOrganId()) : null;
		String organName = getHierarchicalOrganName();
		return printTarazAccountingMarkazAzmayeshi(organName, selectedOrgan);
	}

	public String printHierarchicalTarazAccountingMarkazShenavarAzmayeshi() {
		
		OrganEntity selectedOrgan = getSelectedOrganId()!=null ? getOrganService().load(getSelectedOrganId()) : null;
		String organName = getHierarchicalOrganName();
		return printTarazAccountingMarkazShenavarAzmayeshi(organName, selectedOrgan);
	}
	


	private String printTarazAccountingMarkazAzmayeshi(String organName, OrganEntity organEntity) {
		List<SanadHesabdariItemEntity> tarazAccountingMarkazAzmayehiList = extractTarazAccountingMarkazAzmayeshi(organEntity);
		Map<String, Object> parameters = populateReportParameters(organName);
		
		String reportPath = getLocalFilePath("/WEB-INF/classes/report/taraz/tarazAccountingMarkazAzmayeshi-"+getColumnsCount()+"_col.jrxml");
		
		JasperReport jasperReport;
		try {
			jasperReport = JasperCompileManager.compileReport(reportPath);
			
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(tarazAccountingMarkazAzmayehiList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			
			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			String contentType = "application/pdf";
			downloadStream(pdf, contentType,SerajMessageUtil.getMessage("tarazAccountingMarkazAzmayeshi_"+getColumnsCount())+"_"+ organName+".pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}
		
		logPrintTarazTafsiliAzmayeshiAction();
		
		return null;
	}

	private String printTarazAccountingMarkazShenavarAzmayeshi(String organName, OrganEntity organEntity) {
		List<SanadHesabdariItemEntity> tarazAccountingMarkazAzmayehiList = extractTarazAccountingMarkazShenavarAzmayeshi(organEntity);
		Map<String, Object> parameters = populateReportParameters(organName);
		
		String reportPath = getLocalFilePath("/WEB-INF/classes/report/taraz/tarazAccountingMarkazAzmayeshi-"+getColumnsCount()+"_col.jrxml");
		
		JasperReport jasperReport;
		try {
			jasperReport = JasperCompileManager.compileReport(reportPath);
			
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(tarazAccountingMarkazAzmayehiList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			
			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			String contentType = "application/pdf";
			downloadStream(pdf, contentType,SerajMessageUtil.getMessage("tarazAccountingMarkazAzmayeshi_"+getColumnsCount())+"_"+ organName+".pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}
		
		logPrintTarazTafsiliAzmayeshiAction();
		
		return null;
	}
	
	private Boolean isCalculateMandehAzGhabl;
	
	public Boolean getIsCalculateMandehAzGhabl() {
		return isCalculateMandehAzGhabl;
	}

	public void setIsCalculateMandehAzGhabl(Boolean isCalculateMandehAzGhabl) {
		this.isCalculateMandehAzGhabl = isCalculateMandehAzGhabl;
	}

	private Map<String, Object> populateReportParameters(String organName) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		
		if(getFromDate() == null)
			if(getSelectedSaalMaali()!=null)
				setFromDate(getSelectedSaalMaali().getStartDate());
		
		if(getToDate() == null)
			setToDate(DateConverter.getCurrentDate());
		
		
		parameters.put("organName", organName);
		parameters.put("creatorUser", getCurrentUser().getShakhsName()); 
		if(getSelectedSaalMaali()!=null)
			parameters.put("saalMaali", getSelectedSaalMaali().getSaal().toString());
		
		parameters.put("fromDate", DateConverter.toShamsiDate(getFromDate(),SerajDateTimePickerType.Date));
		parameters.put("toDate", DateConverter.toShamsiDate(getToDate(),SerajDateTimePickerType.Date));
		parameters.put("creationDate", DateConverter.toShamsiDate(DateConverter.getCurrentDateTime(),SerajDateTimePickerType.DateHorMin));
		List<Long> kolIds = getHesabKolIds();
		String hesabKolNames="";
		for (Long kolId : kolIds) {
			hesabKolNames=hesabKolNames+getHesabKolService().load(kolId)+" - ";
		}
		parameters.put("hesabKol", hesabKolNames);
		
		List<Long> moeenIds = getMoeenIds();
		String hesabMoeenNames="";
		for (Long moeenId : moeenIds) {
			hesabMoeenNames=hesabMoeenNames+getHesabMoeenService().load(moeenId)+" - ";
		}
		parameters.put("hesabMoeen", hesabMoeenNames);
		
		List<Long> tafsiliIds = getTafsiliIds();
		String hesabtafsiliNames="";
		for (Long tafsiliId : tafsiliIds) {
			hesabtafsiliNames=hesabtafsiliNames+getHesabTafsiliService().load(tafsiliId)+" - ";
		}
		parameters.put("hesabTafsili", hesabtafsiliNames);
		
		if( getArticleTafsiliIds()!= null){
			List<Long> shenavarIds = getArticleTafsiliIds();
			String hesabtafsiliShenavarNames="";
			for (Long tafsiliId : shenavarIds) {
				hesabtafsiliShenavarNames=hesabtafsiliShenavarNames+getHesabTafsiliService().load(tafsiliId)+" - ";
			}
			parameters.put("hesabShenavar", hesabtafsiliShenavarNames);
		}
		
		Double bedehkarMandehAzGhabl = 0.0;
		Double bestankarMandehAzGhabl = 0.0;
		
		if(getIsCalculateMandehAzGhabl() == null)
			setIsCalculateMandehAzGhabl(false);
		
		if(getFromDate() != null && getIsCalculateMandehAzGhabl()){
			Map<String,Object> mandehFilter = new HashMap<String, Object>();
			mandehFilter.putAll(getFilter());
			mandehFilter.put("sanadHesabdari.tarikhSanad@le", null);
			mandehFilter.put("sanadHesabdari.tarikhSanad@ge", null);
			mandehFilter.put("sanadHesabdari.tarikhSanad@lt", getFromDate());
			
			if(getSelectedSaalMaali()!=null)
				mandehFilter.put("sanadHesabdari.tarikhSanad@ge", getSelectedSaalMaali().getStartDate());
			
			List<SanadHesabdariItemEntity> mandehAzGhablList = getMyService().getDataList(null, mandehFilter, getDefaultSortCol(), getDefaultSortType(), FlushMode.MANUAL, false);
			
			if(mandehAzGhablList != null)
				for(SanadHesabdariItemEntity sanadHesabdariItemEntity : mandehAzGhablList){
					if(sanadHesabdariItemEntity.getBedehkar() != null)
						bedehkarMandehAzGhabl += sanadHesabdariItemEntity.getBedehkar();
					
					if(sanadHesabdariItemEntity.getBestankar() != null)
						bestankarMandehAzGhabl += sanadHesabdariItemEntity.getBestankar();
				}
			}
		
		parameters.put("bedehkarMandehAzGhabl", bedehkarMandehAzGhabl);
		parameters.put("bestankarMandehAzGhabl", bestankarMandehAzGhabl);
		
		
		return parameters;
	}
	
	public void logPrintTarazTafsiliAzmayeshiAction() {
		String message = SerajMessageUtil
		
				.getMessage(getEntityName() + "_title");
		try {
			
				ActionLogUtil.logAction(SerajMessageUtil
						.getMessage(SerajMessageUtil.getMessage("TarazTafsiliAzmayeshi_PRINT_SANAD")), message,
						null, null,"");
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


	public String printTarazKol() {
		
		List<SanadHesabdariItemEntity> tarazKolBedehi = getTarazKolBedehi();
		List<SanadHesabdariItemEntity> tarazKolDarayi = getTarazKolDarayi();
		List<SanadHesabdariItemEntity> tarazKolSarmaye = getTarazKolSarmaye();
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("organName", getCurrentOrgan().getName());
		parameters.put("tarazKolBedehi", new JRBeanCollectionDataSource(tarazKolBedehi));
		parameters.put("tarazKolDarayi", new JRBeanCollectionDataSource(tarazKolDarayi));
		parameters.put("tarazKolSarmaye", new JRBeanCollectionDataSource(tarazKolSarmaye));
		
		parameters.put("SUBREPORT_DIR", getLocalFilePath("/WEB-INF/classes/report/"));
		
		String reportPath = getLocalFilePath("/WEB-INF/classes/report/tarazKol.jrxml");
		
		JasperReport jasperReport;
		try {
			jasperReport = JasperCompileManager.compileReport(reportPath);
			JasperReport jasperReport_darayi = JasperCompileManager.compileReport(getLocalFilePath("/WEB-INF/classes/report/tarazKol_subreport_darayi.jrxml"));
			parameters.put("tarazKolDarayiJasperReport", jasperReport_darayi);
			JasperReport jasperReport_bedehi = JasperCompileManager.compileReport(getLocalFilePath("/WEB-INF/classes/report/tarazKol_subreport_bedehi.jrxml"));
			parameters.put("tarazKolBedehiJasperReport", jasperReport_bedehi);
			JasperReport jasperReport_sarmayeh = JasperCompileManager.compileReport(getLocalFilePath("/WEB-INF/classes/report/tarazKol_subreport_sarmaye.jrxml"));
			parameters.put("tarazKolSarmayehJasperReport", jasperReport_sarmayeh);
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, new JREmptyDataSource());
			
			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			String contentType = "application/pdf";
			downloadStream(pdf, contentType, SerajMessageUtil.getMessage("tarazKol")+"_"+ getCurrentOrgan().getName()+".pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}
		logPrintTarazKolAction();
		
		return null;
	}
	
	public void logPrintTarazKolAction() {
		String message = SerajMessageUtil
		
				.getMessage(getEntityName() + "_title");
		try {
			
				ActionLogUtil.logAction(SerajMessageUtil
						.getMessage(SerajMessageUtil.getMessage("TarazKol_PRINT_SANAD")), message,
						null, null,"");
			} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String goTodDftarRooznamehLocalList(){
		return getFacesUrl("/sanadhesabdariitem/daftar/sanadHesabdariItem.daftarRooznamehLocalList.xhtml");
	}
	
	public String goToTafsiliLocalList(){
		return getFacesUrl("/sanadhesabdariitem/daftar/sanadHesabdariItem.tafsiliLocalList.xhtml");
	}
	
	public String goToMoeenLocalList(){
		return getFacesUrl("/sanadhesabdariitem/daftar/sanadHesabdariItem.moeenLocalList.xhtml");
	}
	
	public String goToKolLocalList(){
		return getFacesUrl("/sanadhesabdariitem/daftar/sanadHesabdariItem.kolLocalList.xhtml");
	}
	
	public String goTodDftarRooznamehHierarchicalList(){
		return getFacesUrl("/sanadhesabdariitem/daftar/hierarchical/sanadHesabdariItem.daftarRooznamehHierarchicalList.xhtml");
	}
	
	public String goToTafsiliHierarchicalList(){
		return getFacesUrl("/sanadhesabdariitem/daftar/hierarchical/sanadHesabdariItem.tafsiliHierarchicalList.xhtml");
	}
	
	public String goToMoeenHierarchicalList(){
		return getFacesUrl("/sanadhesabdariitem/daftar/hierarchical/sanadHesabdariItem.moeenHierarchicalList.xhtml");
	}
	
	public String goToKolHierarchicalList(){
		return getFacesUrl("/sanadhesabdariitem/daftar/hierarchical/sanadHesabdariItem.kolHierarchicalList.xhtml");
	}
	
	@Override
	public String exportToHierarchicalExcel() {

		if(getSelectedOrganId()!=null)
			getFilter().put("sanadHesabdari.organ.id@eq", getSelectedOrganId());		
		else
			getFilter().put("sanadHesabdari.organ.id@eq", null);
		getFilter().put("sanadHesabdari.organ.code@startlk",	getTopOrgan().getCode());

		exportToExcel();
		return null;
	}
	
	public String exportKolSummaryToExcel() {
		List<String> columnsToShow = new ArrayList<>();
		columnsToShow.add("sanadSerial");
		columnsToShow.add("tempSanadSerial");
		columnsToShow.add("sanadTarikh");
		columnsToShow.add("hesabKol");
		columnsToShow.add("hesabKolCode");
		columnsToShow.add("bedehkar");
		columnsToShow.add("bestankar");
		columnsToShow.add("description");
		return super.exportToExcel(columnsToShow);
	}
}