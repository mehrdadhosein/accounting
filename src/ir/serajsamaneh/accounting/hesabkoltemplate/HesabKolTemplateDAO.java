package ir.serajsamaneh.accounting.hesabkoltemplate;

import ir.serajsamaneh.accounting.enumeration.HesabScopeEnum;
import ir.serajsamaneh.accounting.enumeration.HesabTypeEnum;
import ir.serajsamaneh.accounting.enumeration.MahyatGroupEnum;
import ir.serajsamaneh.accounting.enumeration.MahyatKolEnum;
import ir.serajsamaneh.accounting.hesabgroup.HesabGroupDAO;
import ir.serajsamaneh.accounting.hesabgrouptemplate.HesabGroupTemplateDAO;
import ir.serajsamaneh.accounting.hesabgrouptemplate.HesabGroupTemplateEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateDAO;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateDAO;
import ir.serajsamaneh.core.base.BaseHibernateDAO;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.erpcore.util.HesabTemplateRelationsUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HesabKolTemplateDAO  extends BaseHibernateDAO<HesabKolTemplateEntity,Long> {

	HesabGroupDAO hesabGroupDAO;

	public HesabGroupDAO getHesabGroupDAO() {
		return hesabGroupDAO;
	}

	public void setHesabGroupDAO(HesabGroupDAO hesabGroupDAO) {
		this.hesabGroupDAO = hesabGroupDAO;
	}



	
	@Transactional
	public HesabKolTemplateEntity getHesabKolTemplateByCode(String hesabCode, OrganEntity currentOrgan) {

		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", hesabCode);
		localFilter.put("organ.id@eq", currentOrgan.getId());
		List<HesabKolTemplateEntity> dataList = getDataList(null, localFilter,null, null, FlushMode.MANUAL,false);
		if (dataList.size() == 1)
			return dataList.get(0);
		else if (dataList.size() == 0)
			return null;
		else
			throw new IllegalStateException();
	}
	
	@Override
	public void saveOrUpdate(HesabKolTemplateEntity entity) {
		if(entity.getHidden() == null)
			entity.setHidden(false);

		if(entity.getOrgan()!=null && entity.getOrgan().getId()!=null)
			HesabTemplateRelationsUtil.resetKolMoeenTemplateMap(entity.getOrgan());

		checkHesabTemplateUniqueNess(entity);
		
		super.saveOrUpdate(entity);
	}

	private void checkHesabTemplateUniqueNess(HesabKolTemplateEntity entity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eq", entity.getOrgan().getId());
		checkUniqueNess(entity, HesabKolTemplateEntity.PROP_CODE, entity.getCode(), localFilter, false);
		checkUniqueNess(entity, HesabKolTemplateEntity.PROP_NAME, entity.getName(), localFilter, false);
	}

	@Override
	public void save(HesabKolTemplateEntity entity) {
		if(entity.getHidden() == null)
			entity.setHidden(false);

		if(entity.getOrgan()!=null && entity.getOrgan().getId()!=null)
			HesabTemplateRelationsUtil.resetKolMoeenTemplateMap(entity.getOrgan());

		checkHesabTemplateUniqueNess(entity);
		
		super.save(entity);
	}

}