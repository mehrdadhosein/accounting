package ir.serajsamaneh.accounting.saalmaali;

import ir.serajsamaneh.accounting.enumeration.MahyatGroupEnum;
import ir.serajsamaneh.accounting.exception.MoreThanOneActiveSaalMaaliFoundException;
import ir.serajsamaneh.accounting.exception.MoreThanOneSaalMaaliFoundException;
import ir.serajsamaneh.accounting.exception.NoActiveSaalMaaliFoundException;
import ir.serajsamaneh.accounting.exception.NoSaalMaaliFoundException;
import ir.serajsamaneh.accounting.exception.SaalMaaliStartDateException;
import ir.serajsamaneh.accounting.exception.SameSaalMaaliException;
import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.month.MonthService;
import ir.serajsamaneh.accounting.sanadhesabdari.SanadHesabdariEntity;
import ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.exception.NoOrganFoundException;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.security.ActionLogUtil;
import ir.serajsamaneh.core.systemconfig.SystemConfigEntity;
import ir.serajsamaneh.core.systemconfig.SystemConfigService;
import ir.serajsamaneh.core.user.UserEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.enumeration.ActionTypeEnum;
import ir.serajsamaneh.enumeration.SaalMaaliStatusEnum;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.springframework.transaction.annotation.Transactional;

public class SaalMaaliService extends BaseEntityService<SaalMaaliEntity, Long> {

	@Override
	protected SaalMaaliDAO getMyDAO() {
		return saalMaaliDAO;
	}

	SaalMaaliDAO saalMaaliDAO;
	SystemConfigService systemConfigService;
	MonthService monthService;

	public MonthService getMonthService() {
		return monthService;
	}

	public void setMonthService(MonthService monthService) {
		this.monthService = monthService;
	}

	public SystemConfigService getSystemConfigService() {
		return systemConfigService;
	}

	public void setSystemConfigService(SystemConfigService systemConfigService) {
		this.systemConfigService = systemConfigService;
	}

	public void setSaalMaaliDAO(SaalMaaliDAO saalMaaliDAO) {
		this.saalMaaliDAO = saalMaaliDAO;
	}

	public SaalMaaliDAO getSaalMaaliDAO() {
		return saalMaaliDAO;
	}

	@Override
	public String getDifferences(SaalMaaliEntity entity	) {
		String diffes = "";
		SaalMaaliEntity oldEntity= (SaalMaaliEntity) entity.getOldEntity();
//		if (entity.getCurrent() != null
//				&& !entity.getCurrent().equals(oldEntity.getCurrent()))
//			diffes += "["
//					+ SerajMessageUtil.getMessage("SaalMaali" + "_"
//							+ entity.PROP_CURRENT) + " : "
//					+ oldEntity.getCurrent() + "" + " --> " + entity.getCurrent()
//					+ "" + "]";

		if (entity.getSaal() != null
				&& !entity.getSaal().equals(oldEntity.getSaal()))
			diffes += "["
					+ SerajMessageUtil.getMessage("SaalMaali" + "_"
							+ SaalMaaliEntity.PROP_SAAL) + " : " + oldEntity.getSaal()
					+ "" + " --> " + entity.getSaal() + "" + "]";

		if (entity.getStartDate() != null
				&& !entity.getStartDate().equals(oldEntity.getStartDate()))
			diffes += "["
					+ SerajMessageUtil.getMessage("SaalMaali" + "_"
							+ SaalMaaliEntity.PROP_START_DATE) + " : " + oldEntity.getStartDate()
					+ "" + " --> " + entity.getStartDate() + "" + "]";

		if (entity.getEndDate() != null
				&& !entity.getEndDate().equals(oldEntity.getEndDate()))
			diffes += "["
					+ SerajMessageUtil.getMessage("SaalMaali" + "_"
							+ SaalMaaliEntity.PROP_END_DATE) + " : " + oldEntity.getEndDate()
					+ "" + " --> " + entity.getEndDate() + "" + "]";

		return diffes;
	}

//	public SaalMaaliEntity getSaalmaaliByDate(Date date) {
//		Map<String, Object> localFilter = new HashMap<String, Object>();
//		localFilter.put("saal@eq", DateConverter.getPersianDatePart(date).get(SerajDatePartEnum.YEAR));
//		SaalMaaliEntity saalMaaliEntity = getMyDAO().load(localFilter);
//		return saalMaaliEntity;
//	}

	public SaalMaaliEntity getSaalmaaliByDate(Date date, OrganEntity organEntity) {
		if(organEntity == null || organEntity.getId() == null)
			throw new FatalException("Organ id is null");
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eq",organEntity.getId());
		localFilter.put("startDate@le",date);
		localFilter.put("endDate@ge",date);
		List<SaalMaaliEntity> dataList = getDataList(null, localFilter,FlushMode.MANUAL, true);
		if(dataList.size() == 0){
			if(getTopOrgan(organEntity) == null || getTopOrgan(organEntity).equals(organEntity))
				throw new NoSaalMaaliFoundException();
			return getSaalmaaliByDate(date, getTopOrgan(organEntity));
		}
		else if(dataList.size() > 1)
			throw new MoreThanOneSaalMaaliFoundException();
		return dataList.get(0);
	}

	
	public SaalMaaliEntity getActiveSaalmaali(OrganEntity organEntity ) {
		if(organEntity == null || organEntity.getId() == null)
			throw new NoOrganFoundException("");
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eq",organEntity.getId());
		localFilter.put("isActive@eq", Boolean.TRUE);
		List<SaalMaaliEntity> dataList = getDataList(null, localFilter, "", null,FlushMode.MANUAL,true);
		if(dataList.size() == 0){
			if(getTopOrgan(organEntity) == null || getTopOrgan(organEntity).equals(organEntity))
				throw new NoSaalMaaliFoundException();
			return getActiveSaalmaali(getTopOrgan(organEntity));
		}
		else if(dataList.size() > 1)
			throw new MoreThanOneActiveSaalMaaliFoundException();
		return dataList.get(0);
	}

	public SaalMaaliEntity getUserActiveSaalMaali(OrganEntity currentOrgan, /*OrganEntity topOrgan,*/ UserEntity userEntity) {
		
		
		SaalMaaliEntity currentUserSaalMaaliEntity = null;
		String saalMaaliIdStr = getSystemConfigService().getValue(currentOrgan, userEntity, "saalMaaliId");
		if(saalMaaliIdStr!=null)
			currentUserSaalMaaliEntity = load(new Long(saalMaaliIdStr));
		
		if(currentUserSaalMaaliEntity == null){
			try{
				currentUserSaalMaaliEntity = getActiveSaalmaali(currentOrgan);
			}catch(NoSaalMaaliFoundException e){
				
			}catch(NoActiveSaalMaaliFoundException e){
				
			}
		}

		if(currentUserSaalMaaliEntity == null)
			currentUserSaalMaaliEntity = getActiveSaalmaali(getTopOrgan(currentOrgan));
		
		return currentUserSaalMaaliEntity;
	}
	
//	public SaalMaaliEntity getCurrentSaalmaali() {
//		Map<String, Object> localFilter = new HashMap<String, Object>();
//		localFilter.put("organ.id@eq",getCurrentOrgan().getId());
//		localFilter.put("startDate@le",DateConverter.getCurrentDate());
//		localFilter.put("endDate@ge",DateConverter.getCurrentDate());
//		List<SaalMaaliEntity> dataList = getDataList(null, localFilter,FlushMode.MANUAL, true);
//		if(dataList.size() == 0)
//			throw new NoSaalMaaliFoundException();
//		else if(dataList.size() > 1)
//			throw new MoreThanOneSaalMaaliFoundException();
//		return dataList.get(0);
//	}
	
	public SaalMaaliEntity getSaalMaaliByYear(int year, OrganEntity organEntity)
	{ 
		Map<String, Object> filterSaal = new HashMap<String, Object>();
		filterSaal.put("saal@eq", year);
		filterSaal.put("organ.id@eq", organEntity.getId());
		
		List<SaalMaaliEntity> list = getDataList(null,
					filterSaal);
		if (list != null && list.size() > 1)
			 throw new SameSaalMaaliException("SaalMaali_SystemError");
		else if (list!=null && list.size()==0){
			return null;
		}
		SaalMaaliEntity saalMaaliEntity = list.get(0); 
			
		return saalMaaliEntity;
	}
	
	public Date getFirstDateOfSaalMaali(SaalMaaliEntity saalMaaliEntity) {
		Date startDate = saalMaaliEntity.getStartDate();
		return startDate;
	}

	public Date getLastDateOfSaalMaali(SaalMaaliEntity saalMaaliEntity) {
		Date endDate = saalMaaliEntity.getEndDate();
		return endDate;
	}

	
	public SaalMaaliEntity getPreviousSaalMaali(SaalMaaliEntity saalMaaliEntity, OrganEntity currentOrgan){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(saalMaaliEntity.getStartDate());
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return getSaalmaaliByDate(calendar.getTime(), currentOrgan);
	}
	
	public SaalMaaliEntity getPreviousSaalMaali(Date tarikh, OrganEntity currentOrgan){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(tarikh);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return getSaalmaaliByDate(calendar.getTime(), currentOrgan);
	}
	
	@Transactional
	public void save(SaalMaaliEntity entity, OrganEntity currentOrgan) {
		checkCycleInSaalMaali(entity, currentOrgan);
//		checkSaalMaaliStartDate(entity, currentOrgan);
		if(entity.getId() == null)
			entity.setStatus(SaalMaaliStatusEnum.InProgress);
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("organ.id@eq", currentOrgan.getId());
		checkUniqueNess(entity, "saal", entity.getSaal(),filter,false);
		if(entity.getStartDate().after(entity.getEndDate()))
			throw new FatalException(SerajMessageUtil.getMessage("SaalMaali_startDate_after_endDate"));
		manageActiveSatatusOfOtherSaalMaaliEntities(entity, currentOrgan);
		
		super.save(entity);
		getMonthService().createDefaultMonthForCurrentSaalMaali(entity, currentOrgan);
		String action = (entity.getId()!=null?(SerajMessageUtil.getMessage(ActionTypeEnum.EDIT.nameWithClass())):(SerajMessageUtil.getMessage(ActionTypeEnum.CREATE.nameWithClass())));
		ActionLogUtil.logAction(action, 
				SerajMessageUtil.getMessage("SaalMaali_title"),
				"",
				"", 
				entity.getLog());
	}

	private void checkCycleInSaalMaali(SaalMaaliEntity entity, OrganEntity currentOrgan) {
		Map<String, Object> filter = new HashMap<>();
		filter.put("organ.id@eq", currentOrgan.getId());
		List<SaalMaaliEntity> dataList = getDataList(filter);
		for (SaalMaaliEntity saalMaaliEntity : dataList) {
			if(entity.getId()!=null &&  saalMaaliEntity.getId().equals(entity.getId()))
				continue;
			if(entity.getStartDate().after(saalMaaliEntity.getEndDate()) && entity.getEndDate().after(saalMaaliEntity.getEndDate()) || 
					entity.getStartDate().before(saalMaaliEntity.getStartDate()) && entity.getEndDate().before(saalMaaliEntity.getStartDate()))
				continue;
			else
				throw new FatalException(SerajMessageUtil.getMessage("SaalMaali_overlapWith",saalMaaliEntity));
				
		}
		
	}

	@Transactional
	public void globalSave(SaalMaaliEntity entity) {
		if(entity.getId() == null)
			entity.setStatus(SaalMaaliStatusEnum.InProgress);
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("organ.id@eq", entity.getOrgan().getId());
		checkUniqueNess(entity, "saal", entity.getSaal(),filter,false);
		if(entity.getStartDate().after(entity.getEndDate()))
			throw new FatalException(SerajMessageUtil.getMessage("SaalMaali_startDate_after_endDate"));
		manageActiveSatatusOfOtherSaalMaaliEntities(entity, entity.getOrgan());
		
		super.save(entity);
		String action = (entity.getId()!=null?(SerajMessageUtil.getMessage(ActionTypeEnum.EDIT.nameWithClass())):(SerajMessageUtil.getMessage(ActionTypeEnum.CREATE.nameWithClass())));
		ActionLogUtil.logAction(action, 
				SerajMessageUtil.getMessage("SaalMaali_title"),
				"",
				"", 
				entity.getLog());
	}
	
	private void manageActiveSatatusOfOtherSaalMaaliEntities(SaalMaaliEntity entity, OrganEntity currentOrgan) {
		if (entity.getIsActive() != null	&& entity.getIsActive().equals(true)) {
			Map<String, Object> filter = new HashMap<String, Object>();
			filter.put("organ.id@eq", currentOrgan.getId());
			filter.put("isActive@eq", Boolean.TRUE);
			List<SaalMaaliEntity> dataList = getDataList(null, filter);
			for (SaalMaaliEntity saalMaali : dataList) {
				if (entity.getId() == null || saalMaali.getId().longValue() != entity.getId().longValue()) {
					saalMaali.setIsActive(false);
					save(saalMaali);
				}
			}
		}
	}
	
	public void checkSaalMaaliStartDate(SaalMaaliEntity entity, OrganEntity currentOrgan){
		try{
			SaalMaaliEntity previousSaalMaali = getPreviousSaalMaali(entity.getStartDate(), currentOrgan);
			if(entity.getId()!=null && previousSaalMaali.getId().equals(entity.getId()))
				return;

//		SaalMaaliEntity lastSaalMaali = getMyDAO().getLastSaalMaali(currentOrgan);
//		if(previousSaalMaali == null)
//			return;

			Calendar previousSaalMaaliEndDateCal = Calendar.getInstance();
			previousSaalMaaliEndDateCal.setTime(previousSaalMaali.getEndDate());
			previousSaalMaaliEndDateCal.set(Calendar.HOUR, 0);
			previousSaalMaaliEndDateCal.set(Calendar.MINUTE, 0);
			previousSaalMaaliEndDateCal.set(Calendar.SECOND, 0);
			previousSaalMaaliEndDateCal.set(Calendar.MILLISECOND, 0);
			previousSaalMaaliEndDateCal.set(Calendar.AM_PM,Calendar.AM);
			
			Calendar currentSaalMaaliCal = Calendar.getInstance();
			currentSaalMaaliCal.setTime(entity.getStartDate());
			currentSaalMaaliCal.set(Calendar.HOUR, 0);
			currentSaalMaaliCal.set(Calendar.MINUTE, 0);
			currentSaalMaaliCal.set(Calendar.SECOND, 0);
			currentSaalMaaliCal.set(Calendar.MILLISECOND, 0);
			currentSaalMaaliCal.set(Calendar.AM_PM,Calendar.AM);
			
	
			currentSaalMaaliCal.add(Calendar.DAY_OF_YEAR, -1);
			if(!currentSaalMaaliCal.getTime().equals(previousSaalMaaliEndDateCal.getTime()))
				throw new SaalMaaliStartDateException();
		}catch(NoSaalMaaliFoundException e){
			return;
		}		
	}

//	public void checkSaalMaaliIsInProgress(SaalMaaliEntity saalmaali) {
//		if(!(saalmaali.getStatus().equals(SaalMaaliStatusEnum.InProgress) || saalmaali.getStatus().equals(SaalMaaliStatusEnum.TemporalAccountsClosed)))
//			throw new FatalException(SerajMessageUtil.getMessage("SaalMaali_operationNotAllowed", saalmaali.getSaal()));
//	}

	public void checkSaalMaaliIsInProgress(SaalMaaliEntity saalmaali, SanadHesabdariEntity sanadHesabdariEntity) {
		if(!(saalmaali.getStatus().equals(SaalMaaliStatusEnum.InProgress) || saalmaali.getStatus().equals(SaalMaaliStatusEnum.TemporalAccountsClosed)))
			throw new FatalException(SerajMessageUtil.getMessage("SaalMaali_operationNotAllowed", saalmaali.getSaal()));
		
		if(saalmaali.getStatus().equals(SaalMaaliStatusEnum.TemporalAccountsClosed)){
			List<SanadHesabdariItemEntity> sanadHesabdariItems = sanadHesabdariEntity.getSanadHesabdariItem();
			for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItems) {
				HesabKolEntity hesabKol = sanadHesabdariItemEntity.getHesabKol();
				if(!hesabKol.getHesabGroup().getMahyatGroup().equals(MahyatGroupEnum.TARAZNAMEH))
					throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_cantDoOperationOnArticleThatArentOfTypeTARAZNAMEH",sanadHesabdariItemEntity));
			}
		}
	}
	
	public void checkSaalMaaliIsInTemporalAccountsClosed(SaalMaaliEntity saalmaali) {
		if(!saalmaali.getStatus().equals(SaalMaaliStatusEnum.TemporalAccountsClosed))
			throw new FatalException(SerajMessageUtil.getMessage("SaalMaali_operationNotAllowed", saalmaali.getSaal()));
	}
	

	@Override
	@Transactional
	public void delete(Long id) {
		
		SaalMaaliEntity saalMaaliEntity = load(id);
		
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("key@eq", "saalMaaliId"); 
		filter.put("value@eq", saalMaaliEntity.getId().toString());
		List<SystemConfigEntity> dataList = getSystemConfigService().getDataList(null, filter);
		for (SystemConfigEntity systemConfigEntity : dataList) {
			getSystemConfigService().delete(systemConfigEntity.getId());
		}
		super.delete(id);
	}
	
}
