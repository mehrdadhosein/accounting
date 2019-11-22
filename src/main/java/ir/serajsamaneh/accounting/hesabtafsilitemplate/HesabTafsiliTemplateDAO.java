package ir.serajsamaneh.accounting.hesabtafsilitemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.FlushMode;
import org.springframework.transaction.annotation.Transactional;

import ir.serajsamaneh.accounting.enumeration.HesabScopeEnum;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateDAO;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity;
import ir.serajsamaneh.core.base.BaseHibernateDAO;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.erpcore.util.HesabTemplateRelationsUtil;

public class HesabTafsiliTemplateDAO  extends BaseHibernateDAO<HesabTafsiliTemplateEntity,Long> {

	HesabMoeenTemplateDAO hesabMoeenTemplateDAO;


	public HesabMoeenTemplateDAO getHesabMoeenTemplateDAO() {
		return hesabMoeenTemplateDAO;
	}

	public void setHesabMoeenTemplateDAO(HesabMoeenTemplateDAO hesabMoeenTemplateDAO) {
		this.hesabMoeenTemplateDAO = hesabMoeenTemplateDAO;
	}

	@Transactional
	public void save(Long hesabTafsiliCode, String hesabTafsiliName, String hesabMoeenCode, Long organId, Integer level, String organName) {
		HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity = getGlobalHesabTafsiliByCode(hesabTafsiliCode);
		if (hesabTafsiliTemplateEntity == null){
			hesabTafsiliTemplateEntity = new HesabTafsiliTemplateEntity();
			hesabTafsiliTemplateEntity.setHidden(false);
		}
		hesabTafsiliTemplateEntity.setScope(HesabScopeEnum.GLOBAL);
		hesabTafsiliTemplateEntity.setName(hesabTafsiliName);
		hesabTafsiliTemplateEntity.setCode(hesabTafsiliCode);
		hesabTafsiliTemplateEntity.setOrganId(organId);
		hesabTafsiliTemplateEntity.setOrganName(organName);
		hesabTafsiliTemplateEntity.setHidden(false);
		hesabTafsiliTemplateEntity.setLevel(level);
		
		HesabMoeenTemplateEntity moeenTemplateEntity = getHesabMoeenTemplateDAO().getHesabMoeenTemplateByCode(hesabMoeenCode, organId);
		
		if (hesabTafsiliTemplateEntity.getMoeenTafsiliTemplate() == null) {
			hesabTafsiliTemplateEntity.setMoeenTafsiliTemplate(new HashSet<MoeenTafsiliTemplateEntity>());
		}
		hesabTafsiliTemplateEntity.getMoeenTafsiliTemplate().clear();

		Set<MoeenTafsiliTemplateEntity> hesabTafsiliTemplateEntitis = new HashSet<MoeenTafsiliTemplateEntity>();
		
		if(moeenTemplateEntity!=null && moeenTemplateEntity.getId()!=null){
			MoeenTafsiliTemplateEntity moeenTafsiliTemplateEntity = new MoeenTafsiliTemplateEntity();
			moeenTafsiliTemplateEntity.setHesabMoeenTemplate(getHesabMoeenTemplateDAO().load(moeenTemplateEntity.getId()));
			moeenTafsiliTemplateEntity.setHesabTafsiliTemplate(hesabTafsiliTemplateEntity);
			moeenTafsiliTemplateEntity.setLevel(1);
			if(moeenTafsiliTemplateEntity.getHesabMoeenTemplate() == null)
				throw new FatalException();
			hesabTafsiliTemplateEntitis.add(moeenTafsiliTemplateEntity);
		}
		
		
		if(hesabTafsiliTemplateEntity.getMoeenTafsiliTemplate() != null)
			for(MoeenTafsiliTemplateEntity moeenTafsiliEntity : hesabTafsiliTemplateEntity.getMoeenTafsiliTemplate()){
				MoeenTafsiliTemplateEntity moeenTafsiliTemplateEntity = new MoeenTafsiliTemplateEntity();
				moeenTafsiliTemplateEntity.setHesabMoeenTemplate(getHesabMoeenTemplateDAO().load(moeenTafsiliEntity.getHesabMoeenTemplate().getId()));
				moeenTafsiliTemplateEntity.setHesabTafsiliTemplate(hesabTafsiliTemplateEntity);
				moeenTafsiliTemplateEntity.setLevel(moeenTafsiliEntity.getLevel());
				hesabTafsiliTemplateEntitis.add(moeenTafsiliTemplateEntity);				
			}

		hesabTafsiliTemplateEntity.getMoeenTafsiliTemplate().addAll(hesabTafsiliTemplateEntitis);
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("scope@eq", HesabScopeEnum.GLOBAL);
		checkUniqueNess(hesabTafsiliTemplateEntity, HesabTafsiliTemplateEntity.PROP_CODE, hesabTafsiliTemplateEntity.getCode(),	localFilter, false);
		saveOrUpdate(hesabTafsiliTemplateEntity);
	}

	private void checkHesabTemplateUniqueNess(HesabTafsiliTemplateEntity entity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organId@eq", entity.getOrganId());
		checkUniqueNess(entity, HesabTafsiliTemplateEntity.PROP_CODE, entity.getCode(), localFilter, false);
		checkUniqueNess(entity, HesabTafsiliTemplateEntity.PROP_NAME, entity.getName(), localFilter, false);
	}
	
	public HesabTafsiliTemplateEntity getGlobalHesabTafsiliByCode(Long hesabCode){
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", hesabCode);
		List<HesabTafsiliTemplateEntity> dataList = getDataList(null, localFilter,null, null, FlushMode.MANUAL,false);
		if(dataList.size() == 1)
			return dataList.get(0);
		else if(dataList.size() == 0)
			return null;
		else throw new IllegalStateException();
	}

	@Override
	public void saveOrUpdate(HesabTafsiliTemplateEntity entity) {
		if(entity.getOrganId()!=null){
			HesabTemplateRelationsUtil.resetTafsiliMoeenTemplateMap(entity.getOrganId());
			HesabTemplateRelationsUtil.resetmoeenTafsiliTemplateMap(entity.getOrganId());
			HesabTemplateRelationsUtil.resetTafsiliChildTemplateMap(entity.getOrganId());
			HesabTemplateRelationsUtil.resetTafsiliAccountingMarkazChildTemplateMap(entity.getOrganId());
		}
		
		checkHesabTemplateUniqueNess(entity);
		super.saveOrUpdate(entity);
	}

	@Override
	public void save(HesabTafsiliTemplateEntity entity) {
		if(entity.getOrganId()!=null){
			HesabTemplateRelationsUtil.resetTafsiliMoeenTemplateMap(entity.getOrganId());
			HesabTemplateRelationsUtil.resetmoeenTafsiliTemplateMap(entity.getOrganId());
			HesabTemplateRelationsUtil.resetTafsiliChildTemplateMap(entity.getOrganId());
			HesabTemplateRelationsUtil.resetTafsiliAccountingMarkazChildTemplateMap(entity.getOrganId());
		}
		checkHesabTemplateUniqueNess(entity);
		super.save(entity);
	}
}