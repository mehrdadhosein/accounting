package ir.serajsamaneh.accounting.accountstemplate;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import ir.serajsamaneh.accounting.base.BaseAccountingDAO;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AccountsTemplateDAO extends BaseAccountingDAO<AccountsTemplateEntity, Long> {

}