package ir.serajsamaneh.accounting.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.DataModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ir.serajsamaneh.accounting.hesabkol.HesabKolService;
import ir.serajsamaneh.accounting.hesabkol.HesabVO;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.base.BaseEntityForm;
import ir.serajsamaneh.core.exception.NoOrganFoundException;
import ir.serajsamaneh.core.exception.SerajException;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.organ.OrganService;
import ir.serajsamaneh.core.security.SecurityUtil;
import ir.serajsamaneh.core.user.UserEntity;
import ir.serajsamaneh.core.user.UserService;
import ir.serajsamaneh.core.util.XMLUtil;

public abstract class BaseAccountingForm<T extends BaseEntity<U>, U extends Serializable>
		extends BaseEntityForm<T, U> {
//	@Override
//	public Boolean getSearchAction() {
//		return true;
//	}
	@Autowired
	protected UserService userService;
	@Autowired
	protected OrganService organService;
	
	SaalMaaliService saalMaaliService;
	HesabKolService hesabKolService;
	HesabMoeenService hesabMoeenService;
	HesabTafsiliService hesabTafsiliService;

	
	public HesabKolService getHesabKolService() {
		return hesabKolService;
	}

	public void setHesabKolService(HesabKolService hesabKolService) {
		this.hesabKolService = hesabKolService;
	}

	public HesabMoeenService getHesabMoeenService() {
		return hesabMoeenService;
	}

	public void setHesabMoeenService(HesabMoeenService hesabMoeenService) {
		this.hesabMoeenService = hesabMoeenService;
	}

	public HesabTafsiliService getHesabTafsiliService() {
		return hesabTafsiliService;
	}

	public void setHesabTafsiliService(HesabTafsiliService hesabTafsiliService) {
		this.hesabTafsiliService = hesabTafsiliService;
	}

	public SaalMaaliService getSaalMaaliService() {
		return saalMaaliService;
	}

	public void setSaalMaaliService(SaalMaaliService saalMaaliService) {
		this.saalMaaliService = saalMaaliService;
	}
	
	public DataModel<T> getLocalDataModel() {
		if(getCurrentOrganVO() == null || getCurrentOrganVO().getId() == null)
			return getEmptyDataModel();
		getFilter().put("organ.id@eq",	getCurrentOrganVO().getId());
		return getDataModel();
	}
	
	public Integer getLocalDataModelCount() {
		if(getCurrentOrganVO() == null || getCurrentOrganVO().getId() == null)
			return -1;
		getFilter().put("organ.id@eq",	getCurrentOrganVO().getId());
		return getMyService().getRowCount(null, getFilter());
	}
	
	protected String createDHTMLXTreeXML(List<HesabVO> list) {


		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			// root elements
			Document doc = docBuilder.newDocument();
			
			Element treeElement = doc.createElement("tree");
			treeElement.setAttribute("id","0");
			doc.appendChild(treeElement);
			
			for (HesabVO hesabVO : list) {
				Element rootElement = doc.createElement("item");
				rootElement.setAttribute("id", hesabVO.getCode());
				rootElement.setAttribute("text", hesabVO.getName()+"("+hesabVO.getCode()+")");
				rootElement.setAttribute("im0", hesabVO.getIcon());
				rootElement.setAttribute("im1", hesabVO.getIcon());
				rootElement.setAttribute("im2", hesabVO.getIcon());
				
				createUserDataElements(doc, hesabVO, rootElement);
				
				treeElement.appendChild(rootElement);
				
				List<HesabVO> childs = hesabVO.getChilds();
				if(childs!=null)
					createNodeChilds(doc, rootElement, childs);				
			}

			// write the content into xml file
			String treeXml = XMLUtil.convertDocumentToXML(doc);
			return treeXml;
		} catch (TransformerException e) {
			e.printStackTrace();
			return null;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	private void createNodeChilds(Document doc, Element rootElement, List<HesabVO> childs) {
		for (HesabVO hesabVO : childs) {
			Element treeItem = doc.createElement("item");
			treeItem.setAttribute("id", new Integer(hesabVO.hashCode()).toString());
			treeItem.setAttribute("text", hesabVO.getName()+"("+hesabVO.getCode()+")");
			treeItem.setAttribute("im0", hesabVO.getIcon());
			treeItem.setAttribute("im1", hesabVO.getIcon());
			treeItem.setAttribute("im2", hesabVO.getIcon());
			rootElement.appendChild(treeItem);
			
			createUserDataElements(doc, hesabVO, treeItem);
			
			createNodeChilds(doc, treeItem, hesabVO.getChilds());
		}
	}
	

	private void createUserDataElements(Document doc, HesabVO hesabVO, Element treeItem) {
		Element hesabTypeElem = doc.createElement("userdata");
		hesabTypeElem.setAttribute("name", "hesabType");
		hesabTypeElem.setTextContent(hesabVO.getHesabType());
		treeItem.appendChild(hesabTypeElem);
		
		Element hesabIdElem = doc.createElement("userdata");
		hesabIdElem.setAttribute("name", "entityId");
		hesabIdElem.setTextContent(hesabVO.getEntityId());
		treeItem.appendChild(hesabIdElem);
		
		Element codeElem = doc.createElement("userdata");
		codeElem.setAttribute("name", "code");
		codeElem.setTextContent(hesabVO.getCode());
		treeItem.appendChild(codeElem);
	}

	public SaalMaaliEntity getCurrentUserActiveSaalMaali() {
		if(getCurrentOrganVO().getId() == null)
			throw new NoOrganFoundException("");
		SaalMaaliEntity currentUserSaalMaaliEntity = getSaalMaaliService().getUserActiveSaalMaali(getCurrentOrganVO(), getCurrentUserVO().getId());
		
		return currentUserSaalMaaliEntity;
	}
	
	public Boolean getIsCurrentUserActiveSaalMaaliConfigured() {
		if(getCurrentOrganVO().getId() == null)
			return false;
		try {
			getSaalMaaliService().getUserActiveSaalMaali(getCurrentOrganVO(), getCurrentUserVO().getId());
		}catch(SerajException e) { 
			System.out.println(e.getDesc());
			return false;
		}
		
		return true;
	}

	
	protected void populateTopOrgansIdListFilter() {
		List<Long> topOrganList = getCurrentOrganVO().getTopOrgansIdList();
		getFilter().put("organ.id@in", topOrganList);
	}

	private UserEntity currentUser;
	private OrganEntity currentOrgan;
	
	public UserEntity getCurrentUser() {
		if(currentUser == null || currentUser.getId() == null)
			currentUser = userService.load(SecurityUtil.getUserDetails().getUserEntity().getId());
		return currentUser;
	}
	
	public OrganEntity getCurrentOrgan() {
		if(currentOrgan == null || currentOrgan.getId() == null)
			currentOrgan = organService.load(getCurrentUserVO().getOrgan().getId());
		return currentOrgan;
	}
	
	public List<Long> getRelatedOrganIds(){
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("code@startlk",	getCurrentOrganVO().getTopParentCode());
		List<OrganEntity> dataList = organService.getDataList(filter);
		
		List<Long> organIds = new ArrayList<Long>();
		for (OrganEntity entity : dataList) {
			organIds.add(entity.getId());
		}
		return organIds;
	}
}
