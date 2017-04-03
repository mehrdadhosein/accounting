package ir.serajsamaneh.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;

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
import ir.serajsamaneh.core.security.SecurityUtil;
import ir.serajsamaneh.core.user.UserEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.core.util.SpringUtils;
import ir.serajsamaneh.erpcore.util.HesabRelationsUtil;

@Component
@Path("accountingRestFace")
public class AccountingRestFace {

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

	@Path("printSanadHesabdari")
	@GET
	@Produces({"application/pdf"})
	public Response printSanadHesabdari(@QueryParam("sanadHesabdariId")Long sanadHesabdariId)
	{
		try {
			SanadHesabdariEntity sanadHesabdariEntity = getSanadHesabdariService().load(sanadHesabdariId);
			byte[] tempPDF=SanadHesabdariUtil.printSanad(sanadHesabdariEntity);
			
			ResponseBuilder rb = new ResponseBuilderImpl();
			rb.entity(tempPDF);
			rb.header("Content-Disposition", "inline; filename=sanadHesabdari.pdf");
			return rb.build();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public UserEntity getCurrentUser() {
		return SecurityUtil.getUserDetails().getUserEntity();
	}
	
	@Path("getRootHesabsMap")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ListOrderedMap> getRootHesabsMap()
	{
		try {
			SaalMaaliEntity activeSaalMaali = getSaalMaaliService().getUserActiveSaalMaali(getCurrentUser().getOrgan(), getCurrentUser());
			return HesabRelationsUtil.getRootHesabs(activeSaalMaali, getCurrentUser().getOrgan());
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
