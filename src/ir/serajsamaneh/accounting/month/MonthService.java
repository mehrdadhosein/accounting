package ir.serajsamaneh.accounting.month;


import ir.serajsamaneh.core.base.BaseEntityService;

public class MonthService  extends BaseEntityService<MonthEntity,Long>  {


	
	@Override
	protected MonthDAO getMyDAO() {
		return monthDAO;
	}
	
	


MonthDAO monthDAO;

public void setMonthDAO(MonthDAO monthDAO) {
	this.monthDAO = monthDAO;
}
public MonthDAO getMonthDAO() {
	return monthDAO;
}



}