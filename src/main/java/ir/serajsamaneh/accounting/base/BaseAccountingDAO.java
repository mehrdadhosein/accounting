package ir.serajsamaneh.accounting.base;

import java.io.Serializable;

import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.base.BaseHibernateDAO;

public class BaseAccountingDAO<T extends BaseEntity<U>, U extends Serializable> extends BaseHibernateDAO<T, U> {

}
