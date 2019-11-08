package ir.serajsamaneh.accounting.base;

import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.base.BaseHibernateDAO;

import java.io.Serializable;

public class BaseAccountingDAO <T extends BaseEntity<U>, U extends Serializable> extends BaseHibernateDAO<T, U>{

}
