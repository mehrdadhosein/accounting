package ir.serajsamaneh.accounting.markazhazine;


import ir.serajsamaneh.core.base.BaseEntityService;

public class MarkazHazineService  extends BaseEntityService<MarkazHazineEntity,Long>  {


	
	@Override
	protected MarkazHazineDAO getMyDAO() {
		return markazHazineDAO;
	}
	
	


MarkazHazineDAO markazHazineDAO;

public void setMarkazHazineDAO(MarkazHazineDAO markazHazineDAO) {
	this.markazHazineDAO = markazHazineDAO;
}
public MarkazHazineDAO getMarkazHazineDAO() {
	return markazHazineDAO;
}



}