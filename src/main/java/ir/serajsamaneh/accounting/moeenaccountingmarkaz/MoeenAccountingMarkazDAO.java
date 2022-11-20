package ir.serajsamaneh.accounting.moeenaccountingmarkaz;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import ir.serajsamaneh.core.base.BaseHibernateDAO;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MoeenAccountingMarkazDAO extends BaseHibernateDAO<MoeenAccountingMarkazEntity, Long> {

}