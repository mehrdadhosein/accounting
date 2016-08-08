package ir.serajsamaneh.accounting.hesabmoeentemplate;

import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity;
import ir.serajsamaneh.core.base.BaseHibernateDAO;
import ir.serajsamaneh.erpcore.util.HesabTemplateRelationsUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;

public class HesabMoeenTemplateDAO  extends BaseHibernateDAO<HesabMoeenTemplateEntity,Long> {



	public HesabMoeenTemplateEntity getGlobalHesabMoeenTemplateByCode(String hesabCode) {
		
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", hesabCode);
		List<HesabMoeenTemplateEntity> dataList = getDataList(null, localFilter,null, null, FlushMode.MANUAL,false);
		if (dataList.size() == 1)
			return dataList.get(0);
		else if (dataList.size() == 0)
			return null;
		else
			throw new IllegalStateException();
	}

	private void checkHesabTemplateUniqueNess(HesabMoeenTemplateEntity entity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eq", entity.getOrgan().getId());
		checkUniqueNess(entity, HesabMoeenTemplateEntity.PROP_CODE, entity.getCode(), localFilter, false);
		checkUniqueNess(entity, HesabMoeenTemplateEntity.PROP_NAME, entity.getName(), localFilter, false);
	}
	
	@Override
	public void saveOrUpdate(HesabMoeenTemplateEntity entity) {
		
		if(entity.getOrgan()!=null && entity.getOrgan().getId()!=null){
			HesabTemplateRelationsUtil.resetKolMoeenTemplateMap(entity.getOrgan());
			HesabTemplateRelationsUtil.resetMoeenKolTemplateMap(entity.getOrgan());
			HesabTemplateRelationsUtil.resetmoeenTafsiliTemplateMap(entity.getOrgan());
			HesabTemplateRelationsUtil.resetAccountingMarkazTemplateMap(entity.getOrgan());
		}

		checkHesabTemplateUniqueNess(entity);
		super.saveOrUpdate(entity);
	}

	@Override
	public void save(HesabMoeenTemplateEntity entity) {

		if(entity.getOrgan()!=null && entity.getOrgan().getId()!=null){
			HesabTemplateRelationsUtil.resetKolMoeenTemplateMap(entity.getOrgan());
			HesabTemplateRelationsUtil.resetMoeenKolTemplateMap(entity.getOrgan());
			HesabTemplateRelationsUtil.resetmoeenTafsiliTemplateMap(entity.getOrgan());
			HesabTemplateRelationsUtil.resetAccountingMarkazTemplateMap(entity.getOrgan());
		}
		checkHesabTemplateUniqueNess(entity);
		super.save(entity);
	}
}