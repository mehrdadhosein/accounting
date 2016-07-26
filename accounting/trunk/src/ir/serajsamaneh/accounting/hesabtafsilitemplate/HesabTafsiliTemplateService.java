package ir.serajsamaneh.accounting.hesabtafsilitemplate;

import ir.serajsamaneh.accounting.enumeration.HesabScopeEnum;
import ir.serajsamaneh.accounting.enumeration.TafsilTypeEnum;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.moeentafsili.MoeenTafsiliEntity;
import ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.organ.OrganEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.FlushMode;
import org.springframework.transaction.annotation.Transactional;

public class HesabTafsiliTemplateService extends
		BaseEntityService<HesabTafsiliTemplateEntity, Long> {

	@Override
	protected HesabTafsiliTemplateDAO getMyDAO() {
		return hesabTafsiliTemplateDAO;
	}

	HesabTafsiliTemplateDAO hesabTafsiliTemplateDAO;
	HesabMoeenTemplateService hesabMoeenTemplateService;

	public HesabMoeenTemplateService getHesabMoeenTemplateService() {
		return hesabMoeenTemplateService;
	}

	public void setHesabMoeenTemplateService(
			HesabMoeenTemplateService hesabMoeenTemplateService) {
		this.hesabMoeenTemplateService = hesabMoeenTemplateService;
	}

	public void setHesabTafsiliTemplateDAO(
			HesabTafsiliTemplateDAO hesabTafsiliTemplateDAO) {
		this.hesabTafsiliTemplateDAO = hesabTafsiliTemplateDAO;
	}

	public HesabTafsiliTemplateDAO getHesabTafsiliTemplateDAO() {
		return hesabTafsiliTemplateDAO;
	}

//	@Transactional
//	public void saveGlobal(String hesabTafsiliCode, String hesabTafsiliName, String hesabMoeenCode, OrganEntity organEntity) {
//		getMyDAO().saveGlobal(hesabTafsiliCode, hesabTafsiliName, hesabMoeenCode, organEntity);
//	}
	
	
	public HesabTafsiliTemplateEntity getGlobalHesabTafsiliByCode(String hesabCode){
		return getMyDAO().getGlobalHesabTafsiliByCode(hesabCode);
	}
	
	@Transactional
	public void save(HesabTafsiliTemplateEntity entity, List<Long> moeenIds, List<Long> childTafsiliIds, OrganEntity organEntity) {
		if (entity.getMoeenTafsiliTemplate() == null) {
			entity.setMoeenTafsiliTemplate(new HashSet<MoeenTafsiliTemplateEntity>());
		}
		if (entity.getChilds() == null) {
			entity.setChilds(new HashSet<HesabTafsiliTemplateEntity>());
		}
		entity.getMoeenTafsiliTemplate().clear();
		for (Long moeenId : moeenIds) {
			MoeenTafsiliTemplateEntity moeenTafsiliTemplateEntity = new MoeenTafsiliTemplateEntity();
			moeenTafsiliTemplateEntity.setHesabMoeenTemplate(getHesabMoeenTemplateService().load(moeenId));
			moeenTafsiliTemplateEntity.setHesabTafsiliTemplate(entity);
			if(moeenTafsiliTemplateEntity.getHesabMoeenTemplate() == null)
				throw new FatalException();
			entity.getMoeenTafsiliTemplate().add(moeenTafsiliTemplateEntity);
		}
		
		entity.getChilds().clear();
		for (Long tafsiliId : childTafsiliIds) {
			entity.getChilds().add(load(tafsiliId));
		}
		
		checkHesabUniqueNess(entity, organEntity);
		
		save(entity);
	}
	
	@Override
	@Transactional
	public void save(HesabTafsiliTemplateEntity entity) {
		checkHesabUniqueNess(entity, entity.getOrgan());
		super.save(entity);
	}
	
	private void checkHesabUniqueNess(HesabTafsiliTemplateEntity entity,	OrganEntity organ) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eq", organ.getId());
		checkUniqueNess(entity, HesabTafsiliEntity.PROP_NAME, entity.getName(),	localFilter, false);
		checkUniqueNess(entity, HesabTafsiliEntity.PROP_CODE, entity.getCode(),	localFilter, false);
	}

	
	@Transactional
	public HesabTafsiliTemplateEntity createHesabTafsiliTemplate(Long hesabTafsiliCode, String hesabTafsiliName, OrganEntity organ, TafsilTypeEnum tafsilType, String description) {
		
		HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity = new HesabTafsiliTemplateEntity();
		hesabTafsiliTemplateEntity.setScope(HesabScopeEnum.LCAOL);
		hesabTafsiliTemplateEntity.setName(hesabTafsiliName);
		hesabTafsiliTemplateEntity.setCode(hesabTafsiliCode.toString());
		hesabTafsiliTemplateEntity.setOrgan(organ);
		hesabTafsiliTemplateEntity.setTafsilType(tafsilType);
		hesabTafsiliTemplateEntity.setDescription(description);
		hesabTafsiliTemplateEntity.setHidden(false);
		save(hesabTafsiliTemplateEntity);
		return hesabTafsiliTemplateEntity;
	}

	public HesabTafsiliTemplateEntity loadByCode(String code, OrganEntity organEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		//localFilter.put("organ.id@eqORorgan@isNull", Arrays.asList(organ.getId(),"ding"));
		localFilter.put("organ.code@startlk", getTopOrgan(organEntity).getCode());
		return load(null, localFilter);
	}

	public HesabTafsiliTemplateEntity loadByName(String name, OrganEntity organEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq", name);
		//localFilter.put("organ.id@eqORorgan@isNull", Arrays.asList(organ.getId(),"ding"));
		localFilter.put("organ.code@startlk", getTopOrgan(organEntity).getCode());
		return load(null, localFilter);
	}
	
	public List<HesabTafsiliTemplateEntity> getActiveTafsilis(OrganEntity organEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		//localFilter.put("organ.id@eqORorgan.id@isNull", Arrays.asList(organEntity.getId(),"ding"));
		localFilter.put("organ.code@startlk", getTopOrgan(organEntity).getCode());
		localFilter.put("hidden@eq", Boolean.FALSE);
		return getDataList(null, localFilter);
	}

}