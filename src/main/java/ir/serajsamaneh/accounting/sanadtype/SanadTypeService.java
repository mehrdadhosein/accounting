package ir.serajsamaneh.accounting.sanadtype;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ir.serajsamaneh.accounting.base.BaseAccountingService;
import ir.serajsamaneh.core.util.SerajMessageUtil;
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SanadTypeService extends BaseAccountingService<SanadTypeEntity, Long> {

	@Override
	protected SanadTypeDAO getMyDAO() {
		return sanadTypeDAO;
	}

	SanadTypeDAO sanadTypeDAO;

	public void setSanadTypeDAO(SanadTypeDAO sanadTypeDAO) {
		this.sanadTypeDAO = sanadTypeDAO;
	}

	public SanadTypeDAO getSanadTypeDAO() {
		return sanadTypeDAO;
	}

	@Override
	public String getDifferences(SanadTypeEntity entity) {
		String diffes = "";
		SanadTypeEntity oldEntity = (SanadTypeEntity) entity.getOldEntity();
		if (entity.getName() != null && !entity.getName().equals(oldEntity.getName()))
			diffes += "[" + SerajMessageUtil.getMessage("SanadType" + "_" + SanadTypeEntity.PROP_NAME) + " : "
					+ oldEntity.getName() + "" + " --> " + entity.getName() + "" + "]";

		return diffes;
	}

	@Transactional
	public void save(SanadTypeEntity entity) {
		Boolean isNew = (entity.getID() != null ? false : true);
		super.save(entity);
		logAction(isNew, entity);
	}
}