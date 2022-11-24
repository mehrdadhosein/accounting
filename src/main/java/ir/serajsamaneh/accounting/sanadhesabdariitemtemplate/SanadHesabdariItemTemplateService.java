package ir.serajsamaneh.accounting.sanadhesabdariitemtemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import ir.serajsamaneh.core.base.BaseEntityService;
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SanadHesabdariItemTemplateService extends BaseEntityService<SanadHesabdariItemTemplateEntity, Long> {

	@Override
	protected SanadHesabdariItemTemplateDAO getMyDAO() {
		return sanadHesabdariItemTemplateDAO;
	}

	@Autowired
	SanadHesabdariItemTemplateDAO sanadHesabdariItemTemplateDAO;
}