package ir.serajsamaneh.accounting.hesabgroup;

import ir.serajsamaneh.accounting.hesabgrouptemplate.HesabGroupTemplateEntity;
import ir.serajsamaneh.accounting.hesabgrouptemplate.HesabGroupTemplateService;
import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.exception.DuplicateException;
import ir.serajsamaneh.core.exception.FieldMustContainOnlyNumbersException;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.springframework.transaction.annotation.Transactional;

public class HesabGroupService extends
		BaseEntityService<HesabGroupEntity, Long> {

	
	
	@Override
	protected HesabGroupDAO getMyDAO() {
		return hesabGroupDAO;
	}

	HesabGroupDAO hesabGroupDAO;
	HesabGroupTemplateService hesabGroupTemplateService; 
	
	public HesabGroupTemplateService getHesabGroupTemplateService() {
		return hesabGroupTemplateService;
	}

	public void setHesabGroupTemplateService(
			HesabGroupTemplateService hesabGroupTemplateService) {
		this.hesabGroupTemplateService = hesabGroupTemplateService;
	}

	public void setHesabGroupDAO(HesabGroupDAO hesabGroupDAO) {
		this.hesabGroupDAO = hesabGroupDAO;
	}

	public HesabGroupDAO getHesabGroupDAO() {
		return hesabGroupDAO;
	}

	@Override
	public String getDifferences(HesabGroupEntity entity) {
		String diffes = "";
		HesabGroupEntity oldEntity = (HesabGroupEntity) entity.getOldEntity();

		if (entity.getType() != null
				&& !entity.getType().equals(oldEntity.getType()))
			diffes += "["
					+ SerajMessageUtil.getMessage("Hesab" + "_"
							+ HesabGroupEntity.PROP_TYPE) + " : "
					+ oldEntity.getType() + "" + " --> " + entity.getType()
					+ "" + "]";

		if (entity.getLength() != null
				&& !entity.getLength().equals(oldEntity.getLength()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabGroup" + "_"
							+ HesabGroupEntity.PROP_LENGTH) + " : "
					+ oldEntity.getLength() + "" + " --> " + entity.getLength()
					+ "" + "]";

		if (entity.getName() != null
				&& !entity.getName().equals(oldEntity.getName()))
			diffes += "["
					+ SerajMessageUtil.getMessage("HesabGroup" + "_"
							+ HesabGroupEntity.PROP_NAME) + " : "
					+ oldEntity.getName() + "" + " --> " + entity.getName()
					+ "" + "]";

		return diffes;
	}

	public HesabGroupEntity getHesabGroupByName(String hesabGroupName) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq", hesabGroupName);
		List<HesabGroupEntity> dataList = getDataList(null, localFilter,
				FlushMode.MANUAL);
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

	public HesabGroupEntity loadHesabByCode(String code,
			SaalMaaliEntity saalMaaliEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		localFilter.put("organ.id@eq", saalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		HesabGroupEntity hesabGroupEntity = load(null, localFilter);
		return hesabGroupEntity;
	}

	@Transactional
	public void save(HesabGroupEntity entity,
			SaalMaaliEntity activeSaalMaaliEntity) {
		commonSave(entity, activeSaalMaaliEntity, activeSaalMaaliEntity.getOrgan());

		super.save(entity);
		boolean isNew = (entity.getID()!=null?false:true);
		logAction(isNew, entity);
	}

	@Transactional
	private void commonSave(HesabGroupEntity entity,
			SaalMaaliEntity activeSaalMaaliEntity, OrganEntity organEntity) {

		if(!isLong(entity.getCode()))
			throw new FieldMustContainOnlyNumbersException(SerajMessageUtil.getMessage("HesabGroup_code"));
		
		if (entity.getSaalMaali() == null	|| entity.getSaalMaali().getId() == null)
			entity.setSaalMaali(activeSaalMaaliEntity);

		checkHesabUniqueNess(entity, activeSaalMaaliEntity, organEntity);

		createHesabGroupTemplateFromHesabGroup(entity, activeSaalMaaliEntity.getOrgan());

	}
	

	private void checkHesabUniqueNess(HesabGroupEntity entity,
			SaalMaaliEntity activeSaalMaaliEntity, OrganEntity organEntity) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eq", organEntity.getId());
		localFilter.put("saalMaali.id@eq", activeSaalMaaliEntity.getId());
		checkUniqueNess(entity, HesabGroupEntity.PROP_NAME, entity.getName(),
				localFilter, false);
		checkUniqueNess(entity, HesabGroupEntity.PROP_CODE, entity.getCode(),
				localFilter, false);
	}
	
	@Transactional
	private void createHesabGroupTemplateFromHesabGroup(HesabGroupEntity entity, OrganEntity organEntity) {
		HesabGroupTemplateEntity hesabGroupTemplateEntity = getHesabGroupTemplateService().load(entity.getCode(), organEntity);
		if(hesabGroupTemplateEntity == null){
			getHesabGroupTemplateService().createHesabGroupTemplate(entity.getCode(), entity.getName(), entity.getMahyatGroup().name(), organEntity);
		}
	}

	@Transactional
	public void importFromHesabGroupTemplateList(SaalMaaliEntity activeSaalMaaliEntity, OrganEntity currentOrgan) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eq",activeSaalMaaliEntity.getOrgan().getId());
		List<HesabGroupTemplateEntity> dataList = getHesabGroupTemplateService().getDataList(null, localFilter);
		
		for (HesabGroupTemplateEntity hesabGroupTemplateEntity : dataList) {
			HesabGroupEntity hesabGroupEntity = loadHesabGroupByTemplate(hesabGroupTemplateEntity, activeSaalMaaliEntity);
			if(hesabGroupEntity == null){
				try{
					createHesabGroup(activeSaalMaaliEntity, hesabGroupTemplateEntity, currentOrgan);
				}catch(DuplicateException e){
					e.printStackTrace();
				}
			}
		}
	}

	private HesabGroupEntity createHesabGroup(SaalMaaliEntity activeSaalMaaliEntity,
			HesabGroupTemplateEntity hesabGroupTemplateEntity, OrganEntity currentOrgan) {
		HesabGroupEntity hesabGroupEntity = loadHesabGroupByTemplate(hesabGroupTemplateEntity, activeSaalMaaliEntity);
		if(hesabGroupEntity == null){
			hesabGroupEntity = populateHesabGroupEntity(activeSaalMaaliEntity, hesabGroupTemplateEntity);
			save(hesabGroupEntity, activeSaalMaaliEntity, currentOrgan);
		}
		return hesabGroupEntity;	
	}
	
	@Transactional
	public void save(HesabGroupEntity entity,SaalMaaliEntity activeSaalMaaliEntity, OrganEntity currentOrgan) {
		commonSave(entity, activeSaalMaaliEntity, currentOrgan);

		save(entity);
		boolean isNew=(entity.getID()!=null?false:true);
		logAction(isNew, entity);
	}

	private HesabGroupEntity populateHesabGroupEntity(SaalMaaliEntity activeSaalMaaliEntity,
			HesabGroupTemplateEntity  hesabGroupTemplateEntity) {
		HesabGroupEntity hesabGroupEntity;
		hesabGroupEntity = new HesabGroupEntity();
		
		hesabGroupEntity.setCode(hesabGroupTemplateEntity.getCode());
		hesabGroupEntity.setMahyatGroup(hesabGroupTemplateEntity.getMahyatGroup());
		hesabGroupEntity.setType(hesabGroupTemplateEntity.getType());
		hesabGroupEntity.setName(hesabGroupTemplateEntity.getName());
		hesabGroupEntity.setOrgan(new OrganEntity(activeSaalMaaliEntity.getOrgan().getId()));
		hesabGroupEntity.setSaalMaali(activeSaalMaaliEntity);
		hesabGroupEntity.setHesabGroupTemplate(hesabGroupTemplateEntity);
		return hesabGroupEntity;
	}
	
	private HesabGroupEntity loadHesabGroupByTemplate(HesabGroupTemplateEntity hesabGroupTemplateEntity,
			SaalMaaliEntity activeSaalMaaliEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("hesabGroupTemplate.id@eq",hesabGroupTemplateEntity.getId());
//		localFilter.put("organ.id@eq",activeSaalMaaliEntity.getOrgan().getId());
		localFilter.put("saalMaali.id@eq",activeSaalMaaliEntity.getId());
		HesabGroupEntity hesabGroupEntity = load(null, localFilter);
		return hesabGroupEntity;
	}

}