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

	HesabMoeenTemplateDAO hesabMoeenTemplateDAO;
	HesabGroupDAO hesabGroupDAO;
	HesabTafsiliTemplateDAO hesabTafsiliTemplateDAO;
	HesabGroupTemplateDAO hesabGroupTemplateDAO;


	public HesabGroupTemplateDAO getHesabGroupTemplateDAO() {
		return hesabGroupTemplateDAO;
	}

	public void setHesabGroupTemplateDAO(HesabGroupTemplateDAO hesabGroupTemplateDAO) {
		this.hesabGroupTemplateDAO = hesabGroupTemplateDAO;
	}

	public HesabTafsiliTemplateDAO getHesabTafsiliTemplateDAO() {
		return hesabTafsiliTemplateDAO;
	}

	public void setHesabTafsiliTemplateDAO(
			HesabTafsiliTemplateDAO hesabTafsiliTemplateDAO) {
		this.hesabTafsiliTemplateDAO = hesabTafsiliTemplateDAO;
	}

	public HesabGroupDAO getHesabGroupDAO() {
		return hesabGroupDAO;
	}

	public void setHesabGroupDAO(HesabGroupDAO hesabGroupDAO) {
		this.hesabGroupDAO = hesabGroupDAO;
	}

	public HesabMoeenTemplateDAO getHesabMoeenTemplateDAO() {
		return hesabMoeenTemplateDAO;
	}

	public void setHesabMoeenTemplateDAO(HesabMoeenTemplateDAO hesabMoeenTemplateDAO) {
		this.hesabMoeenTemplateDAO = hesabMoeenTemplateDAO;
	}

	@Transactional
	public void createDefaultAccounts(NodeList childNodes, OrganEntity organEntity) {
		for (int s = 0; s < childNodes.getLength(); s++) {
			Node accountNode = childNodes.item(s);
			if (accountNode.getNodeType() == Node.ELEMENT_NODE) {
				Element nodeElem = (Element) accountNode;
				if (nodeElem.getTagName().equals("HesabGroup")) {
					createHesabGroup(nodeElem, organEntity);
				}
				else if (nodeElem.getTagName().equals("HesabKol")) {
					createHesabKolTemplate(nodeElem, organEntity);
				}
				else if (nodeElem.getTagName().equals("HesabMoeen")) {
					createHesabMoeenTemplate(nodeElem, organEntity);
				}
				else if (nodeElem.getTagName().equals("HesabTafsili")) {
					createHesabTafsiliTemplate(nodeElem, organEntity);
				}

			}
		}
	}
	
	@Transactional(readOnly = false)
	private void createHesabTafsiliTemplate(Element hesabTafsiliElem, OrganEntity organEntity) {
		String hesabMoeenCode = hesabTafsiliElem.getAttribute("hesabMoeen");
		String hesabTafsiliCode = hesabTafsiliElem.getAttribute("code");
		String hesabTafsiliName = hesabTafsiliElem.getAttribute("name");
		getHesabTafsiliTemplateDAO().save(hesabTafsiliCode, hesabTafsiliName, hesabMoeenCode, organEntity);
		getLogger().info("hesabTafsili created : "+hesabTafsiliCode);
		
	}

	@Transactional(readOnly = false)
	private void createHesabMoeenTemplate(Element hesabMoeenElem, OrganEntity organ) {
		String hesabKolCode = hesabMoeenElem.getAttribute("hesabKol");
		String hesabMoeenCode = hesabMoeenElem.getAttribute("code");
		String hesabMoeenName = hesabMoeenElem.getAttribute("name");
		
		HesabMoeenTemplateEntity hesabMoeenTemplateEntity = getHesabMoeenTemplateDAO().getHesabMoeenTemplateByCode(hesabMoeenCode, organ);
		if (hesabMoeenTemplateEntity == null)
			hesabMoeenTemplateEntity = new HesabMoeenTemplateEntity();
		hesabMoeenTemplateEntity.setScope(HesabScopeEnum.GLOBAL);
		hesabMoeenTemplateEntity.setName(hesabMoeenName);
		hesabMoeenTemplateEntity.setCode(hesabMoeenCode);
		hesabMoeenTemplateEntity.setHidden(false);
		hesabMoeenTemplateEntity.setHesabKolTemplate(getHesabKolTemplateByCode(hesabKolCode, organ));
		if(hesabMoeenTemplateEntity.getHesabKolTemplate() == null)
			System.out.println(hesabMoeenTemplateEntity.getHesabKolTemplate());
		
		hesabMoeenTemplateEntity.setOrgan(organ);
		
		getHesabMoeenTemplateDAO().saveOrUpdate(hesabMoeenTemplateEntity);
		getLogger().info("hesabMoeen created : "+hesabMoeenCode);
		
	}

	@Transactional(readOnly = false)
	private void createHesabKolTemplate(Element hesbaKolElem, OrganEntity organEntity) {
		String hesabKolCode = hesbaKolElem.getAttribute("code");
		String hesabKolName = hesbaKolElem.getAttribute("name");
		String hesabGroupCode = hesbaKolElem.getAttribute("HesabGroup");
		String mahyatKol = hesbaKolElem.getAttribute("mahyat");
		
		createHesabKolTemplate(hesabKolCode, hesabKolName, hesabGroupCode, mahyatKol, organEntity);
	}

	@Transactional(readOnly = false)
	public HesabKolTemplateEntity createHesabKolTemplate(String hesabKolCode, String hesabKolName,
			String hesabGroupCode, String mahyatKol, OrganEntity organEntity) {
		HesabGroupTemplateEntity hesabGroupTemplateEntity = getHesabGroupTemplateDAO().getHesabGroupByCode(hesabGroupCode);
		HesabKolTemplateEntity hesabKolTemplateEntity = getHesabKolTemplateByCode(hesabKolCode, organEntity);
		if (hesabKolTemplateEntity == null){
			hesabKolTemplateEntity = new HesabKolTemplateEntity();
			hesabKolTemplateEntity.setHidden(false);
		}
		hesabKolTemplateEntity.setName(hesabKolName);
		hesabKolTemplateEntity.setCode(hesabKolCode);
		hesabKolTemplateEntity.setHesabGroupTemplate(hesabGroupTemplateEntity);
		hesabKolTemplateEntity.setMahyatKol(MahyatKolEnum.valueOf(mahyatKol));
		hesabKolTemplateEntity.setOrgan(organEntity);
		saveOrUpdate(hesabKolTemplateEntity);
		getLogger().info("hesabKol created : "+hesabKolCode);
		return hesabKolTemplateEntity;
	}
	

	@Transactional(readOnly = false)
	private void createHesabGroup(Element hesbaGroupElem, OrganEntity organ) {
		String typeEnum = hesbaGroupElem.getAttribute("type");
		String code = hesbaGroupElem.getAttribute("code");
		String mahyatEnum = hesbaGroupElem.getAttribute("mahyat");
		String hesabGroupName = hesbaGroupElem.getAttribute("name");
		HesabGroupTemplateEntity hesabGroupTemplateEntity = getHesabGroupTemplateDAO().getHesabGroupByCode(code, organ);
		if (hesabGroupTemplateEntity == null)
			hesabGroupTemplateEntity = new HesabGroupTemplateEntity();
		hesabGroupTemplateEntity.setName(hesabGroupName);
		hesabGroupTemplateEntity.setMahyatGroup(MahyatGroupEnum.valueOf(mahyatEnum));
		hesabGroupTemplateEntity.setType(HesabTypeEnum.valueOf(typeEnum));
		hesabGroupTemplateEntity.setCode(code);
		hesabGroupTemplateEntity.setOrgan(organ);
		getHesabGroupTemplateDAO().saveOrUpdate(hesabGroupTemplateEntity);
		getLogger().info("hesabGroupTempate created : "+hesabGroupName);
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