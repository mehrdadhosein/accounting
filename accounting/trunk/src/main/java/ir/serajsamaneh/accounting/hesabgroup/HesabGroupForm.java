package ir.serajsamaneh.accounting.hesabgroup;

import javax.faces.model.DataModel;

import ir.serajsamaneh.accounting.base.BaseAccountingForm;

public class HesabGroupForm extends BaseAccountingForm<HesabGroupEntity, Long> {

	@Override
	protected HesabGroupService getMyService() {
		return hesabGroupService;
	}

	@Override
	public String save() {

		getEntity().setOrganId(getCurrentOrganVO().getId());
		getEntity().setOrganName(getCurrentOrganVO().getName());
		getMyService().save(getEntity(), getCurrentUserActiveSaalMaali(), getCurrentOrganVO());
		return getViewUrl();

	}

	public String localSave() {

		getEntity().setOrganId(getCurrentOrganVO().getId());
		getEntity().setOrganName(getCurrentOrganVO().getName());
		getMyService().save(getEntity(), getCurrentUserActiveSaalMaali(), getCurrentOrganVO());
		return getLocalViewUrl();

	}

	HesabGroupService hesabGroupService;

	public void setHesabGroupService(HesabGroupService hesabGroupService) {
		this.hesabGroupService = hesabGroupService;
	}

	public HesabGroupService getHesabGroupService() {
		return hesabGroupService;
	}

	@Override
	public DataModel<HesabGroupEntity> getLocalDataModel() {
		setSearchAction(true);
		return super.getLocalDataModel();
	}
}