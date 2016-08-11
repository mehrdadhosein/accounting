package ir.serajsamaneh.accounting.month;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.organ.OrganEntity;

public class MonthService  extends BaseEntityService<MonthEntity,Long>  {


	
	@Override
	protected MonthDAO getMyDAO() {
		return monthDAO;
	}
	
	
@Transactional
public void save(MonthEntity monthEntity,OrganEntity currentOrgan){
	Map<String,Object> localFilter=new HashMap<String, Object>();
	localFilter.put("organ.id@eq", currentOrgan.getId());
	localFilter.put("name@eq", monthEntity.getName());
	localFilter.put("saalMaali.id@eq", monthEntity.getSaalMaali().getId());
	checkUniqueNess(monthEntity, localFilter, false);
	checkMonthDurationWithOtherMonths(monthEntity);
	save(monthEntity);
}
public void checkMonthDurationWithOtherMonths(MonthEntity monthEntity){
	Map<String, Object> localFilter = new HashMap<String, Object>();
	localFilter.put("organ.id@eq", monthEntity.getOrgan().getId());
	localFilter
			.put("(startDate@geANDendDate@le)OR(startDate@leANDendDate@gt)OR(startDate@ltANDendDate@ge)",
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



}