package ir.serajsamaneh.accounting.hesabkoltemplate;

import ir.serajsamaneh.accounting.hesabgroup.HesabGroupService;
import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.util.XMLUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HesabKolTemplateService extends
		BaseEntityService<HesabKolTemplateEntity, Long> {

	@Override
	protected HesabKolTemplateDAO getMyDAO() {
		return hesabKolTemplateDAO;
	}

	HesabKolTemplateDAO hesabKolTemplateDAO;
	HesabMoeenTemplateService hesabMoeenTemplateService;
	HesabTafsiliTemplateService hesabTafsiliTemplateService;
	HesabGroupService hesabGroupService;
	SaalMaaliService saalMaaliService;




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

	public HesabGroupService getHesabGroupService() {
		return hesabGroupService;
	}

	public void setHesabGroupService(HesabGroupService hesabGroupService) {
		this.hesabGroupService = hesabGroupService;
	}

	public SaalMaaliService getSaalMaaliService() {
		return saalMaaliService;
	}

	public void setSaalMaaliService(SaalMaaliService saalMaaliService) {
		this.saalMaaliService = saalMaaliService;
	}

	public void setHesabKolTemplateDAO(HesabKolTemplateDAO hesabKolTemplateDAO) {
		this.hesabKolTemplateDAO = hesabKolTemplateDAO;
	}

	public HesabKolTemplateDAO getHesabKolTemplateDAO() {
		return hesabKolTemplateDAO;
	}

	@Transactional(readOnly = false)
	public void init() {
//		getLogger().info("initializing accounts");
//		createDefaultAccounts();
	}
	
	InputStream fileInputStream;

	@Transactional(readOnly = false)
	public void createDefaultAccounts(OrganEntity organEntity) throws NumberFormatException {
		URL resource = getClass().getResource("/config/accounts");
		if (resource == null)
			return;
		File dir = new File(resource.getFile());

		FilenameFilter filter = new WildcardFileFilter("general-codingByMahdian.xml");
		String[] list = dir.list(filter);

		for (String fileName : list) {
			String localFilePath = dir.getAbsolutePath() + "/" + fileName;
			try {
				fileInputStream = new FileInputStream(localFilePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new IllegalStateException();
			}
			// parse XML file -> XML document will be build
			Document doc = XMLUtil.parseFile(fileInputStream);
			NodeList rootNodes = doc.getElementsByTagName("accounts");
			Node item = rootNodes.item(0);
			Element accounts = (Element) item;

			NodeList childNodes = accounts.getChildNodes();
			getMyDAO().createDefaultAccounts(childNodes, organEntity);
		}

	}

	@Transactional(readOnly = false)
	public HesabKolTemplateEntity createHesabKolTemplate(String hesabKolCode, String hesabKolName,
			String hesabGroupCode, String mahyatKol, OrganEntity organEntity) {
		return getMyDAO().createHesabKolTemplate(hesabKolCode, hesabKolName, hesabGroupCode, mahyatKol, organEntity);
	}

	
	@Override
	public void saveOrUpdateStateLess(HesabKolTemplateEntity entity) {
		if(entity.getHidden() == null)
			entity.setHidden(false);
		super.saveOrUpdateStateLess(entity);
	}
	
	@Transactional
	public HesabKolTemplateEntity getHesabKolTemplateByCode(String hesabCode, OrganEntity organEntity) {
		return getMyDAO().getHesabKolTemplateByCode(hesabCode, organEntity);
	}

	public HesabKolTemplateEntity loadHierarchical(String code, OrganEntity organEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		//localFilter.put("organ.id@eqORorgan@isNull", Arrays.asList(organ.getId(),"ding"));
		localFilter.put("organ.code@startlk", getTopOrgan(organEntity).getCode());
		return load(null, localFilter);
	}

	public HesabKolTemplateEntity loadLocal(String code, OrganEntity organEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		//localFilter.put("organ.id@eqORorgan@isNull", Arrays.asList(organ.getId(),"ding"));
		localFilter.put("organ.id@eq", organEntity.getId());
		return load(null, localFilter);
	}
	

	public List<HesabKolTemplateEntity> getCurrentHesabKolTemplateList(OrganEntity organEntity) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		//localFilter.put("organ.id@eq",organEntity.getId());
		localFilter.put("organ.code@startlk", getTopOrgan(organEntity).getCode());
		localFilter.put("hidden@eq",Boolean.FALSE);
		List<HesabKolTemplateEntity> hesabKolList = getDataList(null, localFilter, HesabKolEntity.PROP_CODE, true,false);
		return hesabKolList;
	}
	@Transactional
	public void save(HesabKolTemplateEntity entity) {
		Boolean isNew=(entity.getId()!=null?false:true);
		super.save(entity);
		logAction(isNew, entity);
	}

	public HesabKolTemplateEntity loadByCodeInCurrentOrgan(String code, OrganEntity organEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("code@eq", code);
		//localFilter.put("organ.id@eqORorgan@isNull", Arrays.asList(organ.getId(),"ding"));
		localFilter.put("organ.id@eq", organEntity.getId());
		return load(null, localFilter);
	}
	
	public HesabKolTemplateEntity loadByNameInCurrentOrgan(String name, OrganEntity organEntity) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("name@eq", name);
		//localFilter.put("organ.id@eqORorgan@isNull", Arrays.asList(organ.getId(),"ding"));
		localFilter.put("organ.id@eq", organEntity.getId());
		return load(null, localFilter);
	}
}