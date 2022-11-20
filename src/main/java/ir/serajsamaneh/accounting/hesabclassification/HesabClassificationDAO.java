package ir.serajsamaneh.accounting.hesabclassification;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import ir.serajsamaneh.core.base.BaseHibernateDAO;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class HesabClassificationDAO extends BaseHibernateDAO<HesabClassificationEntity, Long> {

}