package ir.serajsamaneh.accounting.base;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.DataModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ir.serajsamaneh.accounting.exception.NoSaalMaaliFoundException;
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
import ir.serajsamaneh.core.util.XMLUtil;
import serajcomponent.DateConverter;

public abstract class BaseAccountingForm<T extends BaseEntity<U>, U extends Serializable>
		extends BaseEntityForm<T, U> {
//	@Override
//	public Boolean getSearchAction() {
//		return true;
//	}
	
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
		if(getCurrentOrgan() == null || getCurrentOrgan().getId() == null)
			return getEmptyDataModel();
		getFilter().put("organ.id@eq",	getCurrentOrgan().getId());
		return getDataModel();
	}
	
	public Integer getLocalDataModelCount() {
		if(getCurrentOrgan() == null || getCurrentOrgan().getId() == null)
			return -1;
		getFilter().put("organ.id@eq",	getCurrentOrgan().getId());
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
	
//	protected SaalMaaliEntity getActiveSaalMaali() {
//		return getSaalMaaliService().getActiveSaalmaali(getCurrentOrgan());
//	}

	public SaalMaaliEntity getCurrentUserActiveSaalMaali() {
		if(getCurrentOrgan().getId() == null)
			throw new NoOrganFoundException("");
		SaalMaaliEntity currentUserSaalMaaliEntity = getSaalMaaliService().getUserActiveSaalMaali(getCurrentOrgan(), /*getTopOrgan(),*/ getCurrentUser());
		
		return currentUserSaalMaaliEntity;
	}
	
	public Boolean getIsCurrentUserActiveSaalMaaliConfigured() {
		if(getCurrentOrgan().getId() == null)
			return false;
		try {
			SaalMaaliEntity currentUserSaalMaaliEntity = getSaalMaaliService().getUserActiveSaalMaali(getCurrentOrgan(), /*getTopOrgan(),*/ getCurrentUser());
		}catch(SerajException e) { 
			System.out.println(e.getDesc());
			return false;
		}
		
		return true;
	}
	

//	public  List<Integer> getLevels(){
//		return SanadHesabdariUtil.getLevels(getCurrentOrgan());
//	}
	
//	public  Integer getLevelsSize(){
//		return getLevels().size();
//	}
	
	protected void populateTopOrgansIdListFilter() {
		List<Long> topOrganList = getTopOrgansIdList(getOrganService().load(getCurrentOrgan().getId()));
		getFilter().put("organ.id@in", topOrganList);
	}

//	public Boolean getHasCurrentDateSaalMaali(){
//		try{
//			getSaalMaaliService().getSaalmaaliByDate(DateConverter.getCurrentDate(), getCurrentOrgan());
//		}catch(NoSaalMaaliFoundException e){
//			return false;
//		}catch(NoOrganFoundException e){
//			return false;
//		}
//		return true;
//	}
}
