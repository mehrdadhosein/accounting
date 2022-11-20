package ir.serajsamaneh.accounting.moeenaccountingmarkaztemplate;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import ir.serajsamaneh.core.base.BaseHibernateDAO;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MoeenAccountingMarkazTemplateDAO extends BaseHibernateDAO<MoeenAccountingMarkazTemplateEntity, Long> {

}