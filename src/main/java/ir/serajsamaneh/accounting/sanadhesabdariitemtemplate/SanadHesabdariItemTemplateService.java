package ir.serajsamaneh.accounting.sanadhesabdariitemtemplate;

import ir.serajsamaneh.core.base.BaseEntityService;

public class SanadHesabdariItemTemplateService extends
		BaseEntityService<SanadHesabdariItemTemplateEntity, Long> {

	@Override
	protected SanadHesabdariItemTemplateDAO getMyDAO() {
		return sanadHesabdariItemTemplateDAO;
	}

	SanadHesabdariItemTemplateDAO sanadHesabdariItemTemplateDAO;

	public SanadHesabdariItemTemplateDAO getSanadHesabdariItemTemplateDAO() {
		return sanadHesabdariItemTemplateDAO;
	}

	public void setSanadHesabdariItemTemplateDAO(
			SanadHesabdariItemTemplateDAO sanadHesabdariItemTemplateDAO) {
		this.sanadHesabdariItemTemplateDAO = sanadHesabdariItemTemplateDAO;
	}

}