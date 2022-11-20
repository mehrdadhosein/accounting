package ir.serajsamaneh.accounting.sanadtype;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import ir.serajsamaneh.accounting.base.BaseAccountingDAO;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SanadTypeDAO extends BaseAccountingDAO<SanadTypeEntity, Long> {

}