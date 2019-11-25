package ir.serajsamaneh.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.hesabkol.HesabKolService;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.accounting.sanadhesabdari.SanadHesabdariEntity;
import ir.serajsamaneh.accounting.sanadhesabdari.SanadHesabdariService;
import ir.serajsamaneh.accounting.sanadhesabdari.SanadHesabdariUtil;
import ir.serajsamaneh.core.base.BaseForm;
import ir.serajsamaneh.core.common.OrganVO;
import ir.serajsamaneh.core.common.SaalMaaliVO;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.core.util.SpringUtils;
import ir.serajsamaneh.erpcore.util.HesabRelationsUtil;
import ir.serajsamaneh.erpcore.util.HesabTemplateRelationsUtil;
import net.sf.jasperreports.engine.JRException;

@Component
@Path("accountingRestFace")
public class AccountingRestFace extends BaseForm{

	HesabKolService hesabKolService;
	HesabMoeenService hesabMoeenService;
	HesabTafsiliService hesabTafsiliService;
	SanadHesabdariService sanadHesabdariService;
	
	@Autowired
	SaalMaaliService saalMaaliService;
	
	public SaalMaaliService getSaalMaaliService() {
		return saalMaaliService;
	}


	public SanadHesabdariService getSanadHesabdariService() {
		if(sanadHesabdariService == null)
			sanadHesabdariService = SpringUtils.getBean("sanadHesabdariService");
		return sanadHesabdariService;
	}


	public HesabKolService getHesabKolService() {
		if(hesabKolService == null)
			hesabKolService = SpringUtils.getBean("hesabKolService");
		return hesabKolService;
	}


	public void setHesabKolService(HesabKolService hesabKolService) {
		this.hesabKolService = hesabKolService;
	}


	public HesabMoeenService getHesabMoeenService() {
		if(hesabMoeenService == null)
			hesabMoeenService = SpringUtils.getBean("hesabMoeenService");
		return hesabMoeenService;
	}


	public void setHesabMoeenService(HesabMoeenService hesabMoeenService) {
		this.hesabMoeenService = hesabMoeenService;
	}


	public HesabTafsiliService getHesabTafsiliService() {
		if(hesabTafsiliService == null)
			hesabTafsiliService = SpringUtils.getBean("hesabTafsiliService");
		return hesabTafsiliService;
	}


	public void setHesabTafsiliService(HesabTafsiliService hesabTafsiliService) {
		this.hesabTafsiliService = hesabTafsiliService;
	}


	@Path("deleteHesab")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteHesab(@QueryParam("hesabId")Long hesabId,
			 				  @QueryParam("hesabType")String hesabType){
		try{
			if(hesabType.equals(HesabKolEntity.class.getSimpleName()))
				getHesabKolService().delete(hesabId);
			else if(hesabType.equals(HesabMoeenEntity.class.getSimpleName()))
				getHesabMoeenService().delete(hesabId);
			else if(hesabType.equals(HesabTafsiliEntity.class.getSimpleName()))
				getHesabTafsiliService().delete(hesabId);
			return SerajMessageUtil.getMessage("SUCCESSFUL_ACTION");
		}catch (Exception e) {
			//e.printStackTrace();
			//return SerajMessageUtil.getMessage("fault");
			return "ERROR";
		}
	}
	
	@Path("disableHesab")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String disableHesab(@QueryParam("hesabId")Long hesabId,
			@QueryParam("hesabType")String hesabType){
		try{
			if(hesabType.equals(HesabKolEntity.class.getSimpleName()))
				getHesabKolService().disableHesab(hesabId);
			else if(hesabType.equals(HesabMoeenEntity.class.getSimpleName()))
				getHesabMoeenService().disableHesab(hesabId);
			else if(hesabType.equals(HesabTafsiliEntity.class.getSimpleName()))
				getHesabTafsiliService().disableHesab(hesabId);
			return SerajMessageUtil.getMessage("SUCCESSFUL_ACTION");
		}catch (Exception e) {
			//e.printStackTrace();
			//return SerajMessageUtil.getMessage("fault");
			return "ERROR";
		}
	}

//	@Path("printSanadHesabdari")
//	@GET
//	@Produces({"application/pdf"})
//	public Response printSanadHesabdari(@QueryParam("sanadHesabdariId")Long sanadHesabdariId)
//	{
//		try {
//			SanadHesabdariEntity sanadHesabdariEntity = getSanadHesabdariService().load(sanadHesabdariId);
//			byte[] tempPDF=SanadHesabdariUtil.printSanad(sanadHesabdariEntity);
//			
//			ResponseBuilder rb = new Builder();
//			rb.entity(tempPDF);
//			rb.header("Content-Disposition", "inline; filename=sanadHesabdari.pdf");
//			return rb.build();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//		
//	}

	@Path("printSanadHesabdari")
	@GET
	@Produces({"application/pdf"})
	public Response printSanadHesabdari(@QueryParam("sanadHesabdariId")Long sanadHesabdariId)
	{
		try {
			ByteArrayOutputStream  byteArrayOutputStream = new ByteArrayOutputStream();
			SanadHesabdariEntity sanadHesabdariEntity = getSanadHesabdariService().load(sanadHesabdariId);
			byte[] tempPDF;
			tempPDF = SanadHesabdariUtil.printSanad(sanadHesabdariEntity);
			byteArrayOutputStream.write(tempPDF);
			return Response
		            .ok(tempPDF, MediaType.APPLICATION_OCTET_STREAM)
		            .header("Content-Disposition", "inline; filename=sanadHesabdari.pdf").type("application/pdf")
		            .build();			
		} catch (JRException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
//		StreamingOutput fileStream =  new StreamingOutput() 
//		{
//			@Override
//			public void write(java.io.OutputStream output) throws IOException, WebApplicationException 
//			{
//				try 
//				{
//					SanadHesabdariEntity sanadHesabdariEntity = getSanadHesabdariService().load(sanadHesabdariId);
//					byte[] tempPDF=SanadHesabdariUtil.printSanad(sanadHesabdariEntity);
//
//					output.write(tempPDF);
//					output.flush();
//				} 
//				catch (Exception e) 
//				{
//					e.printStackTrace();
//					throw new WebApplicationException("error !!");
//				}
//			}
//		};

	}

//	public UserVO getCurrentUser() {
//		return SecurityUtil.getUserDetails().getUserEntity();
//	}
//	
	@Path("getRootHesabsMap")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ListOrderedMap<String, Object>> getRootHesabsMap()
	{
		try {
			SaalMaaliEntity activeSaalMaali = getSaalMaaliService().getUserActiveSaalMaali(getCurrentOrganVO(), getCurrentUserVO().getId());
			return HesabRelationsUtil.getRootHesabs(activeSaalMaali, getCurrentUserVO().getOrgan().getId());
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	@Path("getRootHesabTemplatesMap")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ListOrderedMap<String, String>> getRootHesabTemplatesMap()
	{
		try {
			return HesabTemplateRelationsUtil.getRootHesabs(getCurrentOrganVO().getId(), getCurrentOrganVO().getTopOrgansIdList());
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Path("getUserActiveSaalMaali")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public SaalMaaliVO getUserActiveSaalMaali(String mainOrganJson, Long userId) {
		Jsonb jsonb = JsonbBuilder.create();
		OrganVO organVO = jsonb.fromJson(mainOrganJson, OrganVO.class);
		SaalMaaliEntity userActiveSaalMaali = getSaalMaaliService().getUserActiveSaalMaali(organVO, userId);
		
		SaalMaaliVO saalMaaliVO = new SaalMaaliVO();
		saalMaaliVO.setId(userActiveSaalMaali.getId());
		saalMaaliVO.setOrganId(userActiveSaalMaali.getOrganId());
		saalMaaliVO.setOrganName(userActiveSaalMaali.getOrganName());
		saalMaaliVO.setSaal(userActiveSaalMaali.getSaal());
		saalMaaliVO.setStartDate(userActiveSaalMaali.getStartDate());
		saalMaaliVO.setEndDate(userActiveSaalMaali.getEndDate());
		return saalMaaliVO;
	}
}
