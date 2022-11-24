package ir.serajsamaneh.accounting.moeentafsilitemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import ir.serajsamaneh.core.base.BaseEntityService;
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MoeenTafsiliTemplateService extends BaseEntityService<MoeenTafsiliTemplateEntity, Long> {

	@Override
	protected MoeenTafsiliTemplateDAO getMyDAO() {
		return moeenTafsiliTemplateDAO;
	}
	@Autowired
	MoeenTafsiliTemplateDAO moeenTafsiliTemplateDAO;

}