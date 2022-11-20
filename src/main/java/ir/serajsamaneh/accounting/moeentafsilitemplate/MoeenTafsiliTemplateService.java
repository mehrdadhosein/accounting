package ir.serajsamaneh.accounting.moeentafsilitemplate;

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

	MoeenTafsiliTemplateDAO moeenTafsiliTemplateDAO;

	public void setMoeenTafsiliTemplateDAO(MoeenTafsiliTemplateDAO moeenTafsiliTemplateDAO) {
		this.moeenTafsiliTemplateDAO = moeenTafsiliTemplateDAO;
	}

	public MoeenTafsiliTemplateDAO getMoeenTafsiliTemplateDAO() {
		return moeenTafsiliTemplateDAO;
	}

}