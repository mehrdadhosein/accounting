package ir.serajsamaneh.accounting.hesabclassification;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliService;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateService;
import ir.serajsamaneh.core.base.BaseEntityService;

public class HesabClassificationService extends
		BaseEntityService<HesabClassificationEntity, Long> {

	
	HesabMoeenTemplateService hesabMoeenTemplateService;
	HesabTafsiliTemplateService hesabTafsiliTemplateService;
	
	public HesabMoeenTemplateService getHesabMoeenTemplateService() {
		return hesabMoeenTemplateService;
	}

	public void setHesabMoeenTemplateService(
			HesabMoeenTemplateService hesabMoeenTemplateService) {
		this.hesabMoeenTemplateService = hesabMoeenTemplateService;
	}

	public HesabTafsiliTemplateService getHesabTafsiliTemplateService() {
		return hesabTafsiliTemplateService;
	}

	public void setHesabTafsiliTemplateService(
			HesabTafsiliTemplateService hesabTafsiliTemplateService) {
		this.hesabTafsiliTemplateService = hesabTafsiliTemplateService;
	}


	@Override
	protected HesabClassificationDAO getMyDAO() {
		return hesabClassificationDAO;
	}

	HesabClassificationDAO hesabClassificationDAO;

	public void setHesabClassificationDAO(
			HesabClassificationDAO hesabClassificationDAO) {
		this.hesabClassificationDAO = hesabClassificationDAO;
	}

	public HesabClassificationDAO getHesabClassificationDAO() {
		return hesabClassificationDAO;
	}

	@Transactional
	public void save(HesabClassificationEntity entity, List<Long> moeenIds,
			List<Long> tafsiliIds) {
		
		if(entity.getHesabTafsiliTemplate() == null)
			entity.setHesabTafsiliTemplate(new HashSet<HesabTafsiliTemplateEntity>());
		else
			entity.getHesabTafsiliTemplate().clear();
		
		for (Long tafsiliId : tafsiliIds) {
			entity.getHesabTafsiliTemplate().add(getHesabTafsiliTemplateService().load(tafsiliId));
		}
		
		if(entity.getHesabMoeenTemplate() == null)
			entity.setHesabMoeenTemplate(new HashSet<HesabMoeenTemplateEntity>());
		else
			entity.getHesabMoeenTemplate().clear();
		
		for (Long moeenId : moeenIds) {
			entity.getHesabMoeenTemplate().add(getHesabMoeenTemplateService().load(moeenId));
		}
		
		save(entity);

	}

}