package ir.serajsamaneh.accounting.hesabgrouptemplate;

import ir.serajsamaneh.core.base.BaseEntityForm;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.util.SerajMessageUtil;

import javax.faces.model.DataModel;

public class HesabGroupTemplateForm   extends BaseEntityForm<HesabGroupTemplateEntity,Long>  {


	@Override
	protected HesabGroupTemplateService getMyService() {
		return hesabGroupTemplateService;
	}





	
	HesabGroupTemplateService hesabGroupTemplateService;
	
	public void setHesabGroupTemplateService(HesabGroupTemplateService hesabGroupTemplateService) {
		this.hesabGroupTemplateService = hesabGroupTemplateService;
	}
	
	public HesabGroupTemplateService getHesabGroupTemplateService() {
		return hesabGroupTemplateService;
	}


	public String localSave() {
		if(getEntity().getOrgan() == null || getEntity().getOrgan().getId() == null)
			getEntity().setOrgan(getCurrentOrgan());
		getMyService().save(entity);
		return getLocalViewUrl();
	}

	public DataModel<HesabGroupTemplateEntity> getLocalArchiveDataModel() {
		return getLocalDataModel();
	}	
	
	public Boolean getIsCreated() {
		return null;
	}

	@Override
	public String delete() {
		if(!getIsForMyOrgan())
			throw new FatalException(SerajMessageUtil.getMessage("common_deleteNotAllowed"));
		return super.delete();
	}
	
	public boolean getIsForMyOrgan() {
		if(getEntity() == null || getEntity().getId() == null)
			return true;
		return getEntity().getOrgan().getId().equals(getCurrentOrgan().getId());
	}

}