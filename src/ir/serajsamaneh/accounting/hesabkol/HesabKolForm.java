package ir.serajsamaneh.accounting.hesabkol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ir.serajsamaneh.accounting.base.BaseAccountingForm;
import ir.serajsamaneh.accounting.hesabgroup.HesabGroupEntity;
import ir.serajsamaneh.accounting.hesabgroup.HesabGroupService;
import ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateService;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.exception.DuplicateException;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.core.util.SpringUtils;
import ir.serajsamaneh.core.util.XMLUtil;
import ir.serajsamaneh.erpcore.contacthesab.ContactHesabEntity;
import ir.serajsamaneh.erpcore.contacthesab.ContactHesabService;
import ir.serajsamaneh.erpcore.util.HesabRelationsUtil;
import ir.serajsamaneh.erpcore.util.HesabTreeUtil;


public class HesabKolForm extends BaseAccountingForm<HesabKolEntity,Long> {




	@Override
	protected HesabKolService getMyService() {
		return hesabKolService;
	}
	HesabGroupService HesabGroupService;
	SaalMaaliService saalMaaliService;
	HesabMoeenService hesabMoeenService;
	ContactHesabService contactHesabService;
	HesabMoeenTemplateService hesabMoeenTemplateService;
	HesabTafsiliTemplateService hesabTafsiliTemplateService;
	HesabKolTemplateService hesabKolTemplateService;
	

	public HesabKolTemplateService getHesabKolTemplateService() {
		return hesabKolTemplateService;
	}

	public void setHesabKolTemplateService(HesabKolTemplateService hesabKolTemplateService) {
		this.hesabKolTemplateService = hesabKolTemplateService;
	}

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

	public ContactHesabService getContactHesabService() {
		return contactHesabService;
	}

	public void setContactHesabService(ContactHesabService contactHesabService) {
		this.contactHesabService = contactHesabService;
	}

	public HesabMoeenService getHesabMoeenService() {
		return hesabMoeenService;
	}

	public void setHesabMoeenService(HesabMoeenService hesabMoeenService) {
		this.hesabMoeenService = hesabMoeenService;
	}

	public SaalMaaliService getSaalMaaliService() {
		return saalMaaliService;
	}

	public void setSaalMaaliService(SaalMaaliService saalMaaliService) {
		this.saalMaaliService = saalMaaliService;
	}

	@Override
	public DataModel<HesabKolEntity> getDataModel() {
		setSearchAction(true);
		populateTopOrgansIdListFilter();

		//getFilter().put("organ.code@startlk", getTopOrgan().getCode());
		getFilter().put("saalMaali.id@eq",getCurrentUserActiveSaalMaali().getId());
		return super.getDataModel();
	}
	
	@Override
	public DataModel<HesabKolEntity> getHierarchicalDataModel() {
		setSearchAction(true);
		getFilter().put("saalMaali.id@eq",getCurrentUserActiveSaalMaali().getId());
		return super.getHierarchicalDataModel();
	}
	
	@Override
	public Integer getHierarchicalDataModelCount() {
		getFilter().put("saalMaali.id@eq",getCurrentUserActiveSaalMaali().getId());
		return super.getHierarchicalDataModelCount();
	}
	
	public List<SelectItem> getHesabGroup(){
		Map<String, Object> filter=new HashMap<String, Object>();
		//filter.put("organ.id@eq",getCurrentOrgan().getId());
		List<HesabGroupEntity> list =getHesabGroupService().getDataList(null, filter);
		
		List<SelectItem> selectItemList = new ArrayList<SelectItem>();
		selectItemList.add(new SelectItem("", "------------"));
		for(HesabGroupEntity hesabGroupEntity: list){
			selectItemList.add(new SelectItem(hesabGroupEntity.getId(),hesabGroupEntity.getName()));
		}
		return selectItemList;
	}
	
	
	
	@Override
	public String save() {
		getEntity().setOrgan(getCurrentOrgan()); 
		getMyService().save(getEntity(), getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		resetHesabRelations();
		addInfoMessage("SUCCESSFUL_ACTION");
		return getViewUrl();
	}

	private void resetHesabRelations() {
		List<Long> subsetOrganIds = getSubsetOrganIds(getCurrentOrgan());
		for (Long organId : subsetOrganIds) {
			OrganEntity organEntity = getOrganService().load(organId);
			HesabRelationsUtil.resetKolMoeenMap(getCurrentUserActiveSaalMaali(), organEntity);
		}
	}

	
	HesabKolService hesabKolService;
	
	public void setHesabKolService(HesabKolService hesabKolService) {
		this.hesabKolService = hesabKolService;
	}
	
	public HesabKolService getHesabKolService() {
		return hesabKolService;
	}

	public HesabGroupService getHesabGroupService() {
		return SpringUtils.getBean("hesabGroupService");
	}

	public void setHesabGroupService(HesabGroupService hesabGroupService) {
		HesabGroupService = hesabGroupService;
	}

	public String importFromHesabKolTemplateList(){
		createDefaultAccounts(getCurrentUserActiveSaalMaali().getOrgan());
		getHesabGroupService().importFromHesabGroupTemplateList(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		getMyService().importFromHesabKolTemplateList(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		getHesabMoeenService().importFromHesabMoeenTemplateList(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		getHesabTafsiliService().importFromHesabTafsiliTemplateList(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
		

		setDataModel(null); 
		addInfoMessage("SUCCESSFUL_ACTION");
		return null;
	}
	
	public void createDefaultAccounts(OrganEntity organEntity) throws NumberFormatException {
		InputStream fileInputStream;
		URL resource = getClass().getResource("/config/accounts");
		if (resource == null)
			return;
		File dir = new File(resource.getFile());

		FilenameFilter filter = new WildcardFileFilter("general-accounts.xml");
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
			createDefaultAccounts(childNodes, organEntity);
		}

	}
	
	public void createDefaultAccounts(NodeList childNodes, OrganEntity organEntity) {
		for (int s = 0; s < childNodes.getLength(); s++) {
			try{
				Node accountNode = childNodes.item(s);
				if (accountNode.getNodeType() == Node.ELEMENT_NODE) {
					Element nodeElem = (Element) accountNode;
					if (nodeElem.getTagName().equals("HesabGroup")) {
						getHesabKolTemplateService().createHesabGroup(nodeElem, organEntity);
					}
					else if (nodeElem.getTagName().equals("HesabKol")) {
						getHesabKolTemplateService().createHesabKolTemplate(nodeElem, organEntity);
					}
					else if (nodeElem.getTagName().equals("HesabMoeen")) {
						getHesabKolTemplateService().createHesabMoeenTemplate(nodeElem, organEntity);
					}
					else if (nodeElem.getTagName().equals("HesabTafsili")) {
						getHesabKolTemplateService().createHesabTafsiliTemplate(nodeElem, organEntity);
					}
	
				}
			}catch(DuplicateException e){
				System.out.println(e.getDesc());
			}
		}
	}
	

	static HashMap<Long, String> organHesabHierarchy = new HashMap<Long, String>();
	public String getHesabHieararchy(){
		try{
			List<HesabVO> list = null;
			String treeXml="";
			organHesabHierarchy.clear();
			if(organHesabHierarchy.get(getCurrentOrgan().getId()) == null){
				list = getMyService().createHesabHierarchy(getCurrentOrgan(), getCurrentUserActiveSaalMaali());
				String organTreeXml = createDHTMLXTreeXML(list);
				organHesabHierarchy.put(getCurrentOrgan().getId(), organTreeXml);
			}
			treeXml = organHesabHierarchy.get(getCurrentOrgan().getId());
			return treeXml;
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	






	
	@Override
	public List<? extends BaseEntity> getJsonList(String property, String term,
			boolean all, Map<String, String> params) {
//		String currentSaalMaali=params.get("currentSaalMaali");
//		if(currentSaalMaali!=null && currentSaalMaali.equals("true")){
//			getFilter().put("saalMaali.id@eq", getCurrentUsertActiveSaalMaali().getId());
//		}
		String isHierarchical = params.get("isHierarchical");
		getFilter().put("saalMaali.id@eq", getCurrentUserActiveSaalMaali().getId());

		if (isHierarchical !=null && isHierarchical.equals("true")){
			
			List<Long> topOrganList = getTopOrgansIdList(getCurrentOrgan());
			getFilter().put("organ.id@in", topOrganList);
			
//			this.getFilter().put("organ.code@startlk", getCurrentUserActiveSaalMaali().getOrgan().getCode());
			params.put("isLocal","false");
		}
		
		return super.getJsonList(property, term, all, params);
	}

	public Map<Long, List<ListOrderedMap>> getKolMoeenMap() {
		return HesabRelationsUtil.getKolMoeenMap(getCurrentUserActiveSaalMaali(), getCurrentOrgan());
	}
	
	public String getRelatedHesabTreeByContact(Long contactId){
		
//		List<HesabVO> hesabVOs = new ArrayList<HesabVO>();
		
		String treeXml="";
		try{
			ContactHesabEntity contactHesabEntity = getContactHesabService().getContactHesabByContactId(contactId, getCurrentUserActiveSaalMaali());
			
			HesabTafsiliEntity hesabShenavar = contactHesabEntity.getHesabShenavar();
			HesabTafsiliEntity hesabTafsiliEntity = contactHesabEntity.getHesabTafsili();
			HesabMoeenEntity hesabMoeenEntity = contactHesabEntity.getHesabMoeen();
			Long hesabTafsiliId = hesabTafsiliEntity!=null ? hesabTafsiliEntity.getId() : null;
			Long hesabShenavarId = hesabShenavar!=null ? hesabShenavar.getId() : null;
			Long hesabMoeenId = hesabMoeenEntity!=null ? hesabMoeenEntity.getId() : null;
			treeXml = getRelatedHesabTreeByTafsili(hesabShenavarId, hesabTafsiliId, hesabMoeenId);
			return treeXml;
			
			
//			HesabTreeUtil.addHesabTafsilisToHesabHierarchy(hesabVOs, hesabTafsiliEntity, false, false);
//			
//			Set<HesabTafsiliEntity> parents = hesabTafsiliEntity.getParents();
//			for (HesabTafsiliEntity tafsiliEntity : parents) {
//				HesabTreeUtil.addHesabTafsilisToHesabHierarchy(hesabVOs, tafsiliEntity, false, false);
//			}
		}catch(FatalException e){
			System.out.println(e.getDesc());
			e.printStackTrace();
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new FatalException();
		}
		
		
		//		String treeXml = createDHTMLXTreeXML(hesabVOs);
	}

	public String getRelatedHesabTreeByTafsiliTemplate(Long hesabTafsiliTemplateId, Long hesabMoeenTemplateId){
		if(hesabMoeenTemplateId == null)
			throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_HesabMoeenNotDefined"));
 		
		Long hesabTafsiliId = null;
		Long hesabMoeenId = null;
		
		if(hesabTafsiliTemplateId !=null){
			HesabTafsiliTemplateEntity hesabTafsiliTemplateEntity = getHesabTafsiliTemplateService().load(hesabTafsiliTemplateId);
			HesabTafsiliEntity hesabTafsiliEntity = getHesabTafsiliService().loadHesabTafsiliByCode(new Long(hesabTafsiliTemplateEntity.getCode()), getCurrentUserActiveSaalMaali());
			hesabTafsiliId = hesabTafsiliEntity.getId();
		}
		
		if(hesabMoeenTemplateId !=null){
			HesabMoeenTemplateEntity hesabMoeenTemplateEntity = getHesabMoeenTemplateService().load(hesabMoeenTemplateId);
			HesabMoeenEntity hesabMoeenEntity = getHesabMoeenService().getHesabMoeenByCodeAndSaalMaali(hesabMoeenTemplateEntity.getCode(), getCurrentUserActiveSaalMaali());
			hesabMoeenId = hesabMoeenEntity.getId();
		}
		
		return getRelatedHesabTreeByTafsili(null, hesabTafsiliId, hesabMoeenId); 
	}
	public String getRelatedHesabTreeByTafsili(Long hesabShenavarId, Long hesabTafsiliId, Long hesabMoeenId){
		
		List<HesabVO> hesabVOs = new ArrayList<HesabVO>();
		
		try{
			
			if(hesabShenavarId!=null){
				HesabTafsiliEntity hesabTafsiliShenavar = getHesabTafsiliService().load(hesabShenavarId);
				
				Set<HesabTafsiliEntity> parents = hesabTafsiliShenavar.getParents();
				for (HesabTafsiliEntity tafsiliEntity : parents) {
					HesabTreeUtil.addHesabShenavarsToHesabMoeen(hesabVOs, tafsiliEntity, hesabTafsiliShenavar);
				}
			}else if(hesabTafsiliId!=null){
				HesabTafsiliEntity hesabTafsiliEntity = getHesabTafsiliService().load(hesabTafsiliId);
				HesabTreeUtil.addHesabTafsilisToHesabMoeen(hesabVOs, hesabTafsiliEntity);
				
				Set<HesabTafsiliEntity> parents = hesabTafsiliEntity.getParents();
				for (HesabTafsiliEntity tafsiliEntity : parents) {
					HesabTreeUtil.addHesabShenavarsToHesabMoeen(hesabVOs, tafsiliEntity, hesabTafsiliEntity);
				}
				
			}else if(hesabMoeenId!=null){
				HesabMoeenEntity hesabMoeenEntity = getHesabMoeenService().load(hesabMoeenId);
				HesabKolEntity hesabKolEntity = hesabMoeenEntity.getHesabKol();
				
				HesabVO hesabKolVO = HesabTreeUtil.addHesabKolToHesabVOs(hesabKolEntity, hesabVOs);
				HesabVO hesabMoeenVO = new HesabVO(hesabMoeenEntity, HesabMoeenEntity.class.getSimpleName(), "folder-vectors-icon.png");
				hesabMoeenVO.setParent(hesabKolVO);
				hesabKolVO.getChilds().add(hesabMoeenVO);
				
				HesabTreeUtil.addHesabMoeenToHesabKol(hesabKolVO, hesabMoeenEntity, hesabVOs);
			}
		}catch(FatalException e){
			System.out.println(e.getMessage());
			//throw new FatalException();
		}catch (Exception e) {
			e.printStackTrace();
			//throw new FatalException();
		}
		
		
		String treeXml = createDHTMLXTreeXML(hesabVOs);
		return treeXml;
	}
	
	public String getRelatedHesabTreeByOrgan(Long organId){
		
		List<HesabVO> hesabVOs = new ArrayList<HesabVO>();
		
		try{
			ContactHesabEntity contactHesabEntity = getContactHesabService().getContactHesabByContactId(getOrganService().load(organId).getContact().getId(), getCurrentUserActiveSaalMaali());
			
			HesabTafsiliEntity hesabTafsiliEntity = contactHesabEntity.getHesabTafsili();
			HesabMoeenEntity hesabMoeenEntity = contactHesabEntity.getHesabMoeen();
			
			if(hesabTafsiliEntity!=null){
				HesabTreeUtil.addHesabTafsilisToHesabMoeen(hesabVOs, hesabTafsiliEntity);
				
				Set<HesabTafsiliEntity> parents = hesabTafsiliEntity.getParents();
				for (HesabTafsiliEntity tafsiliEntity : parents) {
					HesabTreeUtil.addHesabTafsilisToHesabMoeen(hesabVOs, tafsiliEntity);
				}
			}else if(hesabMoeenEntity!=null){
				HesabKolEntity hesabKolEntity = hesabMoeenEntity.getHesabKol();
				
				HesabVO hesabKolVO = HesabTreeUtil.addHesabKolToHesabVOs(hesabKolEntity, hesabVOs);
				HesabVO hesabMoeenVO = new HesabVO(hesabMoeenEntity, HesabMoeenEntity.class.getSimpleName(), "folder-vectors-icon.png");
				hesabMoeenVO.setParent(hesabKolVO);
				hesabKolVO.getChilds().add(hesabMoeenVO);
				
				HesabTreeUtil.addHesabMoeenToHesabKol(hesabKolVO, hesabMoeenEntity, hesabVOs);				
			}
			
		}catch(FatalException e){
			System.out.println(e.getMessage());
//			throw new FatalException();
		}catch (Exception e) {
			e.printStackTrace();
//			throw new FatalException();
		}
		
		
		String treeXml = createDHTMLXTreeXML(hesabVOs);
		return treeXml;
	}

	@Override
	public String delete() {
		if(!getIsForMyOrgan())
			throw new FatalException(SerajMessageUtil.getMessage("common_deleteNotAllowed"));
		return super.delete();
	}
	
	public boolean getIsForMyOrgan() {
		if(getEntity() == null || getEntity().getId() == null)
			return true;
		return getEntity().getOrgan().getId().equals(getCurrentOrgan().getId());
	}

}