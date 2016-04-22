package ir.serajsamaneh.rest;

import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.hesabkol.HesabKolService;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliService;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.core.util.SpringUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("accountingRestFace")
public class AccountingRestFace {

	HesabKolService hesabKolService;
	HesabMoeenService hesabMoeenService;
	HesabTafsiliService hesabTafsiliService;
	
	
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
}
