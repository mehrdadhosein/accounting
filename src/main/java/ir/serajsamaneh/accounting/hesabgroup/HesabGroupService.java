package ir.serajsamaneh.accounting.hesabgroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ir.serajsamaneh.accounting.hesabgrouptemplate.HesabGroupTemplateEntity;
import ir.serajsamaneh.accounting.hesabgrouptemplate.HesabGroupTemplateService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.common.OrganVO;
import ir.serajsamaneh.core.exception.DuplicateException;
import ir.serajsamaneh.core.exception.FieldMustContainOnlyNumbersException;
import ir.serajsamaneh.core.util.SerajMessageUtil;
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class HesabGroupService extends BaseEntityService<HesabGroupEntity, Long> {

	@Override
	protected HesabGroupDAO getMyDAO() {
		return hesabGroupDAO;
	}

	@Autowired
	HesabGroupDAO hesabGroupDAO;
	@Autowired
	HesabGroupTemplateService hesabGroupTemplateService;

	@Override
	public String getDifferences(HesabGroupEntity entity) {
		String diffes = "";
		HesabGroupEntity oldEntity = (HesabGroupEntity) entity.getOldEntity();

		if (entity.getType() != null && !entity.getType().equals(oldEntity.getType()))
			diffes += "[" + SerajMessageUtil.getMessage("Hesab" + "_" + HesabGroupEntity.PROP_TYPE) + " : "
					+ oldEntity.getType() + "" + " --> " + entity.getType() + "" + "]";

		if (entity.getLength() != null && !entity.getLength().equals(oldEntity.getLength()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabGroup" + "_" + HesabGroupEntity.PROP_LENGTH) + " : "
					+ oldEntity.getLength() + "" + " --> " + entity.getLength() + "" + "]";

		if (entity.getName() != null && !entity.getName().equals(oldEntity.getName()))
			diffes += "[" + SerajMessageUtil.getMessage("HesabGroup" + "_" + HesabGroupEntity.PROP_NAME) + " : "
					+ oldEntity.getName() + "" + " --> " + entity.getName() + "" + "]";

		return diffes;
	}

	public HesabGroupEntity getHesabGroupByName(String hesabGroupName) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq", hesabGroupName);
		List<HesabGroupEntity> dataList = getDataList(null, localFilter, FlushMode.MANUAL);
		if (dataList.size() == 1)
			return dataList.get(0);
		else if (dataList.size() == 0)
			return null;
		else
			throw new IllegalStateException();
	}

	@Transactional
	public HesabGroupEntity getHesabGroupByCode(String hesabGroupCode) {
		return getMyDAO().getHesabGroupByCode(hesabGroupCode);
	}

	public HesabGroupEntity loadHesabByCode(Long code, SaalMaaliEntity saalMaaliEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		localFilter.put("organId@eq", saalMaaliEntity.getOrganId());
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		HesabGroupEntity hesabGroupEntity = load(null, localFilter);
		return hesabGroupEntity;
	}

	@Transactional
	private void commonSave(HesabGroupEntity entity, SaalMaaliEntity activeSaalMaaliEntity, OrganVO organVO) {

		if (!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("HesabGroup_code"));

		if (entity.getSaalMaali() == null || entity.getSaalMaali().getId() == null)
			entity.setSaalMaali(activeSaalMaaliEntity);

		checkHesabUniqueNess(entity, activeSaalMaaliEntity, organVO.getId());

		createHesabGroupTemplateFromHesabGroup(entity, organVO);

	}

	private void checkHesabUniqueNess(HesabGroupEntity entity, SaalMaaliEntity activeSaalMaaliEntity, Long organId) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organId@eq", organId);
		localFilter.put("saalMaali.id@eq", activeSaalMaaliEntity.getId());
		checkUniqueNess(entity, HesabGroupEntity.PROP_NAME, entity.getName(), localFilter, false);
		checkUniqueNess(entity, HesabGroupEntity.PROP_CODE, entity.getCode(), localFilter, false);
	}

	@Transactional
	private void createHesabGroupTemplateFromHesabGroup(HesabGroupEntity entity, OrganVO organVO) {
		HesabGroupTemplateEntity hesabGroupTemplateEntity = hesabGroupTemplateService.load(entity.getCode(),
				organVO.getId());
		if (hesabGroupTemplateEntity == null) {
			hesabGroupTemplateService.createHesabGroupTemplate(entity.getCode(), entity.getName(),
					entity.getMahyatGroup().name(), organVO.getId(), organVO.getName());
		}
	}

	@Transactional
	public void importFromHesabGroupTemplateList(SaalMaaliEntity activeSaalMaaliEntity, OrganVO currentOrganVO) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organId@eq", activeSaalMaaliEntity.getOrganId());
		List<HesabGroupTemplateEntity> dataList = hesabGroupTemplateService.getDataList(null, localFilter);

		for (HesabGroupTemplateEntity hesabGroupTemplateEntity : dataList) {
			HesabGroupEntity hesabGroupEntity = loadHesabGroupByTemplate(hesabGroupTemplateEntity,
					activeSaalMaaliEntity);
			if (hesabGroupEntity == null) {
				try {
					createHesabGroup(activeSaalMaaliEntity, hesabGroupTemplateEntity, currentOrganVO);
				} catch (DuplicateException e) {
					System.out.println(e.getMessage());
//					e.printStackTrace();
				}
			}
		}
	}

	private HesabGroupEntity createHesabGroup(SaalMaaliEntity activeSaalMaaliEntity,
			HesabGroupTemplateEntity hesabGroupTemplateEntity, OrganVO currentOrganVO) {
		HesabGroupEntity hesabGroupEntity = loadHesabGroupByTemplate(hesabGroupTemplateEntity, activeSaalMaaliEntity);
		if (hesabGroupEntity == null) {
			hesabGroupEntity = populateHesabGroupEntity(activeSaalMaaliEntity, hesabGroupTemplateEntity);
			save(hesabGroupEntity, activeSaalMaaliEntity, currentOrganVO);
		}
		return hesabGroupEntity;
	}

	@Transactional
	public void save(HesabGroupEntity entity, SaalMaaliEntity activeSaalMaaliEntity, OrganVO currentOrganVO) {
		commonSave(entity, activeSaalMaaliEntity, currentOrganVO);

		save(entity);
		boolean isNew = (entity.getID() != null ? false : true);
		logAction(isNew, entity);
	}

	private HesabGroupEntity populateHesabGroupEntity(SaalMaaliEntity activeSaalMaaliEntity,
			HesabGroupTemplateEntity hesabGroupTemplateEntity) {
		HesabGroupEntity hesabGroupEntity;
		hesabGroupEntity = new HesabGroupEntity();

		hesabGroupEntity.setCode(hesabGroupTemplateEntity.getCode());
		hesabGroupEntity.setMahyatGroup(hesabGroupTemplateEntity.getMahyatGroup());
		hesabGroupEntity.setType(hesabGroupTemplateEntity.getType());
		hesabGroupEntity.setName(hesabGroupTemplateEntity.getName());
		hesabGroupEntity.setOrganId(activeSaalMaaliEntity.getOrganId());
		hesabGroupEntity.setSaalMaali(activeSaalMaaliEntity);
		hesabGroupEntity.setHesabGroupTemplate(hesabGroupTemplateEntity);
		return hesabGroupEntity;
	}

	private HesabGroupEntity loadHesabGroupByTemplate(HesabGroupTemplateEntity hesabGroupTemplateEntity,
			SaalMaaliEntity activeSaalMaaliEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("hesabGroupTemplate.id@eq", hesabGroupTemplateEntity.getId());
//		localFilter.put("organId@eq",activeSaalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq", activeSaalMaaliEntity.getId());
		HesabGroupEntity hesabGroupEntity = load(null, localFilter);
		return hesabGroupEntity;
	}

}