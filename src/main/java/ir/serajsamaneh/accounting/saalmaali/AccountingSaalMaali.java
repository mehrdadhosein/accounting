package ir.serajsamaneh.accounting.saalmaali;

import javax.faces.model.DataModel;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import ir.serajsamaneh.accounting.hesabgroup.HesabGroupService;
import ir.serajsamaneh.accounting.hesabkol.HesabKolService;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliService;
import ir.serajsamaneh.core.exception.RequiredFieldNotSetException;
import ir.serajsamaneh.core.util.SerajMessageUtil;

@Named("accountingSaalMaali")
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class AccountingSaalMaali extends SaalMaaliForm {

	@Autowired
	HesabKolService hesabKolService;
	@Autowired
	HesabGroupService hesabGroupService;
	@Autowired
	HesabMoeenService hesabMoeenService;
	@Autowired
	HesabTafsiliService hesabTafsiliService;
	@Autowired
	SaalMaaliService  saalMaaliService;

	public void copyHesabsFromSourceSaalMaaliToDestSaalMaali(){
		if(getSrcSaalMaali().getId() == null)
			throw new RequiredFieldNotSetException(SerajMessageUtil.getMessage("AccountingSystemConfig_srcSaalMaali"));

//		if(getDestSaalMaali().getId() == null)
//			throw new RequiredFieldNotSetException(SerajMessageUtil.getMessage("AccountingSystemConfig_destSaalMaali"));
		
		srcSaalMaali = saalMaaliService.load(getSrcSaalMaali().getId());
//		destSaalMaali = getSaalMaaliService().load(getDestSaalMaali().getId());
		
		hesabKolService.copyHesabKolsFromSourceSaalMaaliToDestSaalMaali(getSrcSaalMaali(), getEntity(), getCurrentOrganVO().getTopOrgansIdList());
		hesabKolService.copyHesabMoeensFromSourceSaalMaaliToDestSaalMaali(getSrcSaalMaali(), getEntity(), getCurrentOrganVO().getTopOrgansIdList());
		hesabKolService.copyHesabTafsilissFromSourceSaalMaaliToDestSaalMaali(getSrcSaalMaali(), getEntity(), getCurrentOrganVO().getTopOrgansIdList(), getCurrentOrganVO().getTopParentCode());
		hesabKolService.copyHesabTafsiliRelatedEntities(getSrcSaalMaali(), getEntity(), getCurrentOrganVO().getTopOrgansIdList());
		hesabKolService.copyAccountingMarkazhaFromSourceSaalMaaliToDestSaalMaali(getSrcSaalMaali(), getEntity(), getCurrentOrganVO().getTopParentCode());
		hesabKolService.copycontactHesabsFromSourceSaalMaaliToDestSaalMaali(getSrcSaalMaali(), getEntity());
		addInfoMessage("SUCCESSFUL_ACTION");
	}
	
	@Override
	public DataModel<SaalMaaliEntity> getLocalDataModel() {
		setSearchAction(true);
		return super.getLocalDataModel();
	}

	public String importFromHesabKolTemplateList(){
		hesabKolService.createDefaultAccounts(getCurrentOrganVO().getId(), getCurrentOrganVO().getName());
		hesabGroupService.importFromHesabGroupTemplateList(getEntity(), getCurrentOrganVO());
		hesabKolService.importFromHesabKolTemplateList(getEntity(), getCurrentOrganVO().getId(), getCurrentOrganVO().getTopOrgansIdList(), getCurrentOrganVO().getName());
		hesabMoeenService.importFromHesabMoeenTemplateList(getEntity(), getCurrentOrganVO().getId(), getCurrentOrganVO().getTopOrgansIdList(), getCurrentOrganVO().getName());
		hesabTafsiliService.importFromHesabTafsiliTemplateList(getEntity(), getCurrentOrganVO().getId(), getCurrentOrganVO().getTopOrgansIdList(), getCurrentOrganVO().getTopParentCode(), getCurrentOrganVO().getName());
		

		setDataModel(null); 
		addInfoMessage("SUCCESSFUL_ACTION");
		return null;
	}
}
