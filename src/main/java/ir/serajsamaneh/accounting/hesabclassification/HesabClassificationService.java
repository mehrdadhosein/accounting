package ir.serajsamaneh.accounting.hesabclassification;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateService;
import ir.serajsamaneh.core.base.BaseEntityService;
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class HesabClassificationService extends BaseEntityService<HesabClassificationEntity, Long> {

	@Override
	protected HesabClassificationDAO getMyDAO() {
		return hesabClassificationDAO;
	}

	@Autowired
	HesabMoeenTemplateService hesabMoeenTemplateService;
	@Autowired
	HesabTafsiliTemplateService hesabTafsiliTemplateService;
	@Autowired
	HesabClassificationDAO hesabClassificationDAO;

	@Transactional
	public void save(HesabClassificationEntity entity, List<Long> moeenIds, List<Long> tafsiliIds) {

		if (entity.getHesabTafsiliTemplate() == null)
			entity.setHesabTafsiliTemplate(new HashSet<HesabTafsiliTemplateEntity>());
		else
			entity.getHesabTafsiliTemplate().clear();

		for (Long tafsiliId : tafsiliIds) {
			entity.getHesabTafsiliTemplate().add(hesabTafsiliTemplateService.load(tafsiliId));
		}

		if (entity.getHesabMoeenTemplate() == null)
			entity.setHesabMoeenTemplate(new HashSet<HesabMoeenTemplateEntity>());
		else
			entity.getHesabMoeenTemplate().clear();

		for (Long moeenId : moeenIds) {
			entity.getHesabMoeenTemplate().add(hesabMoeenTemplateService.load(moeenId));
		}

		save(entity);

	}

}