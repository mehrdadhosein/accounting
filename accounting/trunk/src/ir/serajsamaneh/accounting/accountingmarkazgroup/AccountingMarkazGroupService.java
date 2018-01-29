package ir.serajsamaneh.accounting.accountingmarkazgroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import ir.serajsamaneh.core.base.BaseEntityService;

public class AccountingMarkazGroupService extends BaseEntityService<AccountingMarkazGroupEntity, Long> {

	@Override
	protected AccountingMarkazGroupDAO getMyDAO() {
		return accountingMarkazGroupDAO;
	}

	AccountingMarkazGroupDAO accountingMarkazGroupDAO;

	public AccountingMarkazGroupDAO getAccountingMarkazGroupDAO() {
		return accountingMarkazGroupDAO;
	}

	public void setAccountingMarkazGroupDAO(AccountingMarkazGroupDAO accountingMarkazGroupDAO) {
		this.accountingMarkazGroupDAO = accountingMarkazGroupDAO;
	}

	@Override
	@Transactional(readOnly = false)
	public void save(AccountingMarkazGroupEntity entity) {

		generateGroupKalaCode(entity);
		
		checkUniqueNess(entity, AccountingMarkazGroupEntity.PROP_CODE, entity.getCode(), new HashMap<String, Object>(), false);
		super.saveAndLog(entity);
	}

	private void generateGroupKalaCode(AccountingMarkazGroupEntity entity) {
		if (!StringUtils.hasText(entity.getCode())) {
			Map<String, Object> filter = new HashMap<String, Object>();
			if (entity.getParent() == null
					|| entity.getParent().getId() == null)
				filter.put("parent.id@isNull", "ding");
			else
				filter.put("parent.id@eq", entity.getParent().getId());
			List<AccountingMarkazGroupEntity> adjacentList = getDataList(null, filter,
					"", null, FlushMode.MANUAL, false);
			AccountingMarkazGroupEntity parentEntity = null;
			if (entity.getParent() != null
					&& entity.getParent().getID() != null)
				parentEntity = load(entity.getParent().getID());
			String parentCode = parentEntity == null ? "" : parentEntity
					.getCode();
			Integer maxCode = 0;
			for (AccountingMarkazGroupEntity groupKalaEntity : adjacentList) {
				String adjacentCode = "";
				if (groupKalaEntity.getCode() != null)
					adjacentCode = groupKalaEntity.getCode().replaceFirst(
							parentCode, "");
				if (StringUtils.hasText(adjacentCode)
						&& new Integer(adjacentCode).intValue() > maxCode)
					maxCode = new Integer(adjacentCode).intValue();
			}
			maxCode++;
			if (maxCode < 10)
				entity.setCode(parentCode + "0" + maxCode);
			else
				entity.setCode(parentCode + maxCode);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void update(AccountingMarkazGroupEntity entity) {
		generateGroupKalaCode(entity);
		checkUniqueNess(entity, AccountingMarkazGroupEntity.PROP_CODE, entity.getCode(), new HashMap<String, Object>(), false);
		super.update(entity);
	}

	


	public Boolean checkEquality(AccountingMarkazGroupEntity e1, AccountingMarkazGroupEntity e2){
		if(e1.getId()==null && e2.getId()==null)
			return true;
		if(e1.getId()!=null && e2.getId()==null)
			return false;
		if(e1.getId()==null && e2.getId()!=null)
			return false;
		return e1.getId().equals(e2.getId());
		
	}
}