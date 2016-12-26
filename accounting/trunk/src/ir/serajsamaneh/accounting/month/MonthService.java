package ir.serajsamaneh.accounting.month;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import ir.serajsamaneh.accounting.enumeration.SanadFunctionEnum;
import ir.serajsamaneh.accounting.enumeration.SanadStateEnum;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.sanadhesabdari.SanadHesabdariEntity;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;

public class MonthService extends BaseEntityService<MonthEntity, Long> {

	@Override
	protected MonthDAO getMyDAO() {
		return monthDAO;
	}

	@Transactional
	public void save(MonthEntity monthEntity, OrganEntity currentOrgan) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eq", currentOrgan.getId());
		localFilter.put("name@eq", monthEntity.getName());
		localFilter.put("saalMaali.id@eq", monthEntity.getSaalMaali().getId());
		checkUniqueNess(monthEntity, localFilter, false);
		checkMonthDurationWithOtherMonths(monthEntity);
		save(monthEntity);
	}

	public void checkMonthDurationWithOtherMonths(MonthEntity monthEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eq", monthEntity.getOrgan().getId());
		localFilter.put("(startDate@geANDendDate@le)OR(startDate@leANDendDate@gt)OR(startDate@ltANDendDate@ge)",
				Arrays.asList(Arrays.asList(monthEntity.getStartDate(), monthEntity.getEndDate()),
						Arrays.asList(monthEntity.getStartDate(), monthEntity.getStartDate()),
						Arrays.asList(monthEntity.getEndDate(), monthEntity.getEndDate())));
		checkUniqueNess(monthEntity, localFilter, false);
	}

	MonthDAO monthDAO;

	public void setMonthDAO(MonthDAO monthDAO) {
		this.monthDAO = monthDAO;
	}

	public MonthDAO getMonthDAO() {
		return monthDAO;
	}

	public List<MonthEntity> getList(SaalMaaliEntity currentUserActiveSaalMaali) {
		Map<String, Object> filter = new HashMap<>();
		filter.put("saalMaali.id@eq", currentUserActiveSaalMaali.getId());
		List<MonthEntity> dataList = getDataList(null, filter);
		return dataList;

	}
	
	@Transactional
	public void createDefaultMonthForCurrentSaalMaali(SaalMaaliEntity saalMaaliEntity, OrganEntity organEntity) {
		
		for(int radif =1 ; radif<=12; radif++){
			
			Map<String, Object> monthFilter =new HashMap<>();
			monthFilter.put("radif@eq", radif);
			monthFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
			monthFilter.put("organ.id@eq", organEntity.getId());
			MonthEntity monthEntity = load(null, monthFilter);
			if(monthEntity!=null)
				continue;
			
			monthEntity = new MonthEntity();
			monthEntity.setSaalMaali(saalMaaliEntity);
			monthEntity.setOrgan(organEntity);
			monthEntity.setRadif(radif);
			monthEntity.setName(SerajMessageUtil.getMessage("Month_"+radif+"_name"));
			monthEntity.setDescription(SerajMessageUtil.getMessage("Month_"+radif+"_description"));
//			String startDayOfMonth = SerajMessageUtil.getMessage("Month_"+radif+"startDayOfMonth");
//			String startMonthOfYear = SerajMessageUtil.getMessage("Month_"+radif+"startMonthOfYear");
//			Calendar c = Calendar.getInstance();
//			c.setTime(saalMaaliEntity.getStartDate());
//			String year = saalMaaliEntity.getStartDate().getYear(); 
//			monthEntity.setStartDate(startDate);
//			monthEntity.setEndDate(endDate);
			
			save(monthEntity, organEntity);
		}
		
	}

}