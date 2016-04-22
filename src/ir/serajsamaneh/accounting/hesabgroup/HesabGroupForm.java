package ir.serajsamaneh.accounting.hesabgroup;

import ir.serajsamaneh.accounting.base.BaseAccountingForm;


public class HesabGroupForm extends BaseAccountingForm<HesabGroupEntity,Long> {




	@Override
	protected HesabGroupService getMyService() {
		return hesabGroupService;
	}

	
	@Override
	public String save() {
		
		getEntity().setOrgan(getCurrentOrgan()); 
		getMyService().save(getEntity(), getCurrentUserActiveSaalMaali());
		return getViewUrl();

	}
	
	public String localSave() {
		
		getEntity().setOrgan(getCurrentOrgan()); 
		getMyService().save(getEntity(), getCurrentUserActiveSaalMaali());
		return getLocalViewUrl();

	}
	
	
	HesabGroupService hesabGroupService;
	
	public void setHesabGroupService(HesabGroupService hesabGroupService) {
		this.hesabGroupService = hesabGroupService;
	}
	
	public HesabGroupService getHesabGroupService() {
		return hesabGroupService;
	}


}