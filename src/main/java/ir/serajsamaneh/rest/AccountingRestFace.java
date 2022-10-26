package ir.serajsamaneh.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity;
import ir.serajsamaneh.accounting.accountstemplate.AccountsTemplateEntity;
import ir.serajsamaneh.accounting.accountstemplate.AccountsTemplateService;
import ir.serajsamaneh.accounting.common.KolMoeenTafsiliVO;
import ir.serajsamaneh.accounting.enumeration.SanadStateEnum;
import ir.serajsamaneh.accounting.exception.NoSaalMaaliFoundException;
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
import ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity;
import ir.serajsamaneh.accounting.sanadtype.SanadTypeEntity;
import ir.serajsamaneh.core.base.BaseForm;
import ir.serajsamaneh.core.common.OrganVO;
import ir.serajsamaneh.core.common.SaalMaaliVO;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.user.UserEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.enumeration.YesNoEnum;
import ir.serajsamaneh.erpcore.contacthesab.ContactHesabEntity;
import ir.serajsamaneh.erpcore.contacthesab.ContactHesabService;
import ir.serajsamaneh.erpcore.util.AutomaticSanadUtil;
import ir.serajsamaneh.erpcore.util.HesabRelationsUtil;
import ir.serajsamaneh.erpcore.util.HesabTemplateRelationsUtil;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/accountingRestFace")
public class AccountingRestFace extends BaseForm{

	@Autowired
	HesabKolService hesabKolService;
	@Autowired
	HesabMoeenService hesabMoeenService;
	@Autowired
	HesabTafsiliService hesabTafsiliService;
	@Autowired
	SanadHesabdariService sanadHesabdariService;
	@Autowired
	ContactHesabService contactHesabService;
	@Autowired
	SaalMaaliService saalMaaliService;
	@Autowired
	AccountsTemplateService accountsTemplateService;


	@RequestMapping(method = RequestMethod.GET, path = "/deleteHesab", produces = "application/json")
	public String deleteHesab(@RequestParam("hesabId")Long hesabId,
			 				  @RequestParam("hesabType")String hesabType){
		try{
			if(hesabType.equals(HesabKolEntity.class.getSimpleName()))
				hesabKolService.delete(hesabId);
			else if(hesabType.equals(HesabMoeenEntity.class.getSimpleName()))
				hesabMoeenService.delete(hesabId);
			else if(hesabType.equals(HesabTafsiliEntity.class.getSimpleName()))
				hesabTafsiliService.delete(hesabId);
			return SerajMessageUtil.getMessage("SUCCESSFUL_ACTION");
		}catch (Exception e) {
			//e.printStackTrace();
			//return SerajMessageUtil.getMessage("fault");
			return "ERROR";
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/disableHesab", produces = "application/json")
	public String disableHesab(@RequestParam("hesabId")Long hesabId,
			@RequestParam("hesabType")String hesabType){
		try{
			if(hesabType.equals(HesabKolEntity.class.getSimpleName()))
				hesabKolService.disableHesab(hesabId);
			else if(hesabType.equals(HesabMoeenEntity.class.getSimpleName()))
				hesabMoeenService.disableHesab(hesabId);
			else if(hesabType.equals(HesabTafsiliEntity.class.getSimpleName()))
				hesabTafsiliService.disableHesab(hesabId);
			return SerajMessageUtil.getMessage("SUCCESSFUL_ACTION");
		}catch (Exception e) {
			//e.printStackTrace();
			//return SerajMessageUtil.getMessage("fault");
			return "ERROR";
		}
	}


	@RequestMapping(method = RequestMethod.GET, path = "/printSanadHesabdari", produces = "application/pdf")
	public ResponseEntity<ByteArrayResource> printSanadHesabdari(@RequestParam("sanadHesabdariId")Long sanadHesabdariId)
	{
		try {
			ByteArrayOutputStream  byteArrayOutputStream = new ByteArrayOutputStream();
			SanadHesabdariEntity sanadHesabdariEntity = sanadHesabdariService.load(sanadHesabdariId);
			byte[] tempPDF;
			tempPDF = SanadHesabdariUtil.printSanad(sanadHesabdariEntity);
			byteArrayOutputStream.write(tempPDF);


		    ByteArrayResource resource = new ByteArrayResource(tempPDF);
		    HttpHeaders headers = new HttpHeaders(); 
		    headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
		    headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=sanadHesabdari.pdf");
		    return ResponseEntity.ok()
		            .headers(headers)
		            .contentLength(tempPDF.length)
		            .contentType(MediaType.APPLICATION_OCTET_STREAM)
		            .body(resource);
		    
		} catch (JRException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(method = RequestMethod.GET, path = "/getRootHesabsMap", produces = "application/json")
	public List<ListOrderedMap<String, Object>> getRootHesabsMap()
	{
		try {
			SaalMaaliEntity activeSaalMaali = saalMaaliService.getUserActiveSaalMaali(getCurrentOrganVO(), getCurrentUserVO().getId());
			return HesabRelationsUtil.getRootHesabs(activeSaalMaali, getCurrentUserVO().getOrgan().getId());
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/getRootHesabTemplatesMap", produces = "application/json")
	public List<ListOrderedMap<String, String>> getRootHesabTemplatesMap()
	{
		try {
			return HesabTemplateRelationsUtil.getRootHesabs(getCurrentOrganVO().getId(), getCurrentOrganVO().getTopOrgansIdList());
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(method = RequestMethod.GET, path = "/getActiveSaalMaali", produces = "application/json")
	public String getActiveSaalMaali(@RequestParam("mainOrganJson")String mainOrganJson) {
		Jsonb jsonb = JsonbBuilder.create();
		OrganVO organVO = jsonb.fromJson(mainOrganJson, OrganVO.class);
		SaalMaaliEntity userActiveSaalMaali = saalMaaliService.getActiveSaalmaali(organVO);
		
		SaalMaaliVO saalMaaliVO = new SaalMaaliVO(userActiveSaalMaali);
//		saalMaaliVO.setId(userActiveSaalMaali.getId());
//		saalMaaliVO.setOrganId(userActiveSaalMaali.getOrganId());
//		saalMaaliVO.setOrganName(userActiveSaalMaali.getOrganName());
//		saalMaaliVO.setSaal(userActiveSaalMaali.getSaal());
//		saalMaaliVO.setStartDateLong(userActiveSaalMaali.getStartDate().getTime());
//		saalMaaliVO.setEndDateLong(userActiveSaalMaali.getEndDate().getTime());
		
		String result = jsonb.toJson(saalMaaliVO);
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/getUserActiveSaalMaali", produces = "application/json")
	public String getUserActiveSaalMaali(@RequestParam("mainOrganJson")String mainOrganJson, @RequestParam("userId")Long userId) {
		Jsonb jsonb = JsonbBuilder.create();
		OrganVO organVO = jsonb.fromJson(mainOrganJson, OrganVO.class);
		SaalMaaliEntity userActiveSaalMaali = saalMaaliService.getUserActiveSaalMaali(organVO, userId);
		
		SaalMaaliVO saalMaaliVO = new SaalMaaliVO(userActiveSaalMaali);
//		saalMaaliVO.setId(userActiveSaalMaali.getId());
//		saalMaaliVO.setOrganId(userActiveSaalMaali.getOrganId());
//		saalMaaliVO.setOrganName(userActiveSaalMaali.getOrganName());
//		saalMaaliVO.setSaal(userActiveSaalMaali.getSaal());
//		saalMaaliVO.setStartDateLong(userActiveSaalMaali.getStartDate().getTime());
//		saalMaaliVO.setEndDateLong(userActiveSaalMaali.getEndDate().getTime());
		
		String result = jsonb.toJson(saalMaaliVO);
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/getSaalmaaliByDate", produces = "application/json")
	public SaalMaaliVO getSaalmaaliByDate(@RequestParam("mainOrganJson")String mainOrganJson, @RequestParam("date")Long date) {
		Jsonb jsonb = JsonbBuilder.create();
		OrganVO organVO = jsonb.fromJson(mainOrganJson, OrganVO.class);
		SaalMaaliEntity userActiveSaalMaali = saalMaaliService.getSaalmaaliByDate(new Date(date), organVO);
		
		SaalMaaliVO saalMaaliVO = new SaalMaaliVO(userActiveSaalMaali);
//		saalMaaliVO.setId(userActiveSaalMaali.getId());
//		saalMaaliVO.setOrganId(userActiveSaalMaali.getOrganId());
//		saalMaaliVO.setOrganName(userActiveSaalMaali.getOrganName());
//		saalMaaliVO.setSaal(userActiveSaalMaali.getSaal());
//		saalMaaliVO.setStartDateLong(userActiveSaalMaali.getStartDate().getTime());
//		saalMaaliVO.setEndDateLong(userActiveSaalMaali.getEndDate().getTime());
		return saalMaaliVO;
	}
	
	public SanadHesabdariItemEntity createBestankarArticle(Long saalMaaliId, Double amount, String sharhBestankar,
			Long extraHesabTafsiliOneTemplateId, Long extraHesabTafsiliTwoTemplateId, Long bestankarHesabTafsiliTemplateId,
			Long bestankarHesabMoeenTemplateId) {
		HesabMoeenEntity hesabMoeenEntity = 	AutomaticSanadUtil.getHesabMoeenByTemplateId(bestankarHesabMoeenTemplateId, saalMaaliId);
		HesabTafsiliEntity hesabTafsiliEntity = AutomaticSanadUtil.getHesabTafsiliByTemplateId(bestankarHesabTafsiliTemplateId, saalMaaliId);
		HesabKolEntity hesabKolEntity = hesabMoeenEntity.getHesabKol();

		if(hesabKolEntity==null || hesabKolEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("HesabKol_notDefinedForOrgan",bestankarHesabMoeenTemplateId));
		if(hesabMoeenEntity==null || hesabMoeenEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("HesabMoeen_notDefinedForOrgan",bestankarHesabMoeenTemplateId));

		HesabTafsiliEntity extraHesabTafsiliOne = AutomaticSanadUtil.getHesabTafsiliByTemplateId(extraHesabTafsiliOneTemplateId, saalMaaliId);
		HesabTafsiliEntity extraHesabTafsiliTwo = AutomaticSanadUtil.getHesabTafsiliByTemplateId(extraHesabTafsiliTwoTemplateId, saalMaaliId);
		
		
		SanadHesabdariItemEntity bestankarArticle = AutomaticSanadUtil.createBestankarArticle(hesabKolEntity, hesabMoeenEntity, hesabTafsiliEntity, extraHesabTafsiliOne, amount, null, sharhBestankar, extraHesabTafsiliTwo);
		return bestankarArticle;
	}

	public SanadHesabdariItemEntity createBestankarArticle(Long saalMaaliId, Long organId,
			String templateActionName, String sharh, Double amount) {
		AccountsTemplateEntity accountsTemplateEntity = accountsTemplateService.loadTemplateByActionId(templateActionName, organId);
		if(accountsTemplateEntity == null)
			throw new FatalException(SerajMessageUtil.getMessage("template_not_found")+" : "+SerajMessageUtil.getMessage(templateActionName)+"["+templateActionName+"]");
		SanadHesabdariItemEntity bestankarArticle = AutomaticSanadUtil.createBestankarArticle(amount, accountsTemplateEntity, null, null, sharh, saalMaaliId);
		return bestankarArticle;
	}
	
	public SanadHesabdariItemEntity createBestankarArticle(Long saalMaaliId, String havelehSharh,
			Long bestankarMoeenId, Long bestankarTafsiliOneId, Long bestankarTafsiliTwoId, Long contactId, Double amount) {
		ContactHesabEntity contactHesab = contactHesabService.getContactHesabByContactId(contactId, saalMaaliId);
		
		KolMoeenTafsiliVO kolMoeenTafsiliVO = AutomaticSanadUtil.extractKolMoeenTafsili(bestankarMoeenId, bestankarTafsiliOneId, bestankarTafsiliTwoId, contactHesab);
		AccountingMarkazEntity accountingMarkazEntity = AutomaticSanadUtil.getAccountingMarkazByTemplate(contactHesab.getAccountingMarkazTemplate(), saalMaaliId);
		
		SanadHesabdariItemEntity bestankarArticle = AutomaticSanadUtil.createBestankarArticle(kolMoeenTafsiliVO.getHesabKolEntity(), kolMoeenTafsiliVO.getHesabMoeenEntity(), kolMoeenTafsiliVO.getHesabTafsiliEntityONE(), kolMoeenTafsiliVO.getHesabTafsiliEntityTWO(), amount, accountingMarkazEntity, havelehSharh);
		return bestankarArticle;
	}

	
	public SanadHesabdariItemEntity createBestankarArticle(Long saalMaaliId, Long organId,
			String templateActionName, Long contactId, String bestankarSharh, Double amount) {
		ContactHesabEntity contactHesab = contactHesabService.getContactHesabByContactId(contactId, saalMaaliId);
		AccountsTemplateEntity accountsTemplateEntity = accountsTemplateService.loadTemplateByActionId(templateActionName, organId);
		if(accountsTemplateEntity == null)
			throw new FatalException(SerajMessageUtil.getMessage("template_not_found")+" : "+SerajMessageUtil.getMessage(templateActionName)+"["+templateActionName+"]");
		
		SanadHesabdariItemEntity bestankarArticle = AutomaticSanadUtil.createBestankarArticle(amount, accountsTemplateEntity, contactHesab.getHesabTafsiliOne(), null, bestankarSharh, saalMaaliId);
		return bestankarArticle;
	}
	
	public SanadHesabdariItemEntity createBedehkarArticle(Long saalMaaliId, String sharhBedehkar,
			Long extraHesabTafsiliOneTemplateId, Long extraHesabTafsiliTwoTemplateId, Long bedehkarHesabTafsiliTemplateId,
			Long bedehkarHesabMoeenTemplateId, Double amount) {
		HesabMoeenEntity hesabMoeenEntity = 	AutomaticSanadUtil.getHesabMoeenByTemplateId(bedehkarHesabMoeenTemplateId, saalMaaliId);
		HesabTafsiliEntity hesabTafsiliEntity = AutomaticSanadUtil.getHesabTafsiliByTemplateId(bedehkarHesabTafsiliTemplateId, saalMaaliId);
		HesabKolEntity hesabKolEntity = hesabMoeenEntity.getHesabKol();

		if(hesabKolEntity==null || hesabKolEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("HesabKol_notDefinedForOrgan",bedehkarHesabMoeenTemplateId));
		if(hesabMoeenEntity==null || hesabMoeenEntity.getId() == null)
			throw new FatalException(SerajMessageUtil.getMessage("HesabMoeen_notDefinedForOrgan",bedehkarHesabMoeenTemplateId));
		
		
		HesabTafsiliEntity extraHesabTafsiliOne = AutomaticSanadUtil.getHesabTafsiliByTemplateId(extraHesabTafsiliOneTemplateId, saalMaaliId);
		HesabTafsiliEntity extraHesabTafsiliTwo = AutomaticSanadUtil.getHesabTafsiliByTemplateId(extraHesabTafsiliTwoTemplateId, saalMaaliId);
		
		SanadHesabdariItemEntity bedehkarArticle = AutomaticSanadUtil.createBedehkarArticle(hesabKolEntity, hesabMoeenEntity, hesabTafsiliEntity, extraHesabTafsiliOne, amount, null, sharhBedehkar, extraHesabTafsiliTwo);
		return bedehkarArticle;
	}
	
	public SanadHesabdariItemEntity createBedehkarArticle(Long saalMaaliId, Double amount, String bedehkarSharh,
			Long bedehkarMoeenId, Long bedehkarTafsiliId, Long bedehkarTafsiliTwoId, Long contactId) {
		ContactHesabEntity contactHesab = contactHesabService.getContactHesabByContactId(contactId, saalMaaliId);
		
		KolMoeenTafsiliVO kolMoeenTafsiliVO = AutomaticSanadUtil.extractKolMoeenTafsili(bedehkarMoeenId, bedehkarTafsiliId, bedehkarTafsiliTwoId, contactHesab);
		AccountingMarkazEntity accountingMarkazEntity = AutomaticSanadUtil.getAccountingMarkazByTemplate(contactHesab.getAccountingMarkazTemplate(), saalMaaliId);
		
		
		SanadHesabdariItemEntity bedehkarArticle = AutomaticSanadUtil.createBedehkarArticle(kolMoeenTafsiliVO.getHesabKolEntity(), kolMoeenTafsiliVO.getHesabMoeenEntity(), kolMoeenTafsiliVO.getHesabTafsiliEntityONE(), kolMoeenTafsiliVO.getHesabTafsiliEntityTWO(), amount, accountingMarkazEntity, bedehkarSharh);
		return bedehkarArticle;
	}
	
	public SanadHesabdariItemEntity createBedehkarArticle(Long saalMaaliId, Long organId,
			String templateActionName, String bestankarSharh, Double amount) {
		AccountsTemplateEntity accountsTemplateEntity = accountsTemplateService.loadTemplateByActionId(templateActionName, organId);
		if(accountsTemplateEntity == null)
			throw new FatalException(SerajMessageUtil.getMessage("template_not_found")+" : "+SerajMessageUtil.getMessage(templateActionName)+"["+templateActionName+"]");

		SanadHesabdariItemEntity bedehkarArticle = AutomaticSanadUtil.createBedehkarArticle(amount, accountsTemplateEntity, null, null, bestankarSharh, saalMaaliId);
		return bedehkarArticle;
	}
	

	
	public SanadHesabdariEntity createSanadHesabdari(Long organId,
			Date sanadHesabdariDate,
			List<SanadHesabdariItemEntity> articles, String description, SanadTypeEntity sanadType, SanadStateEnum sanadStateEnum, SaalMaaliEntity saalMaaliId, 
			YesNoEnum deletable, int numberOfDecimals, UserEntity currentUser, String organDesc, Boolean checkUniqueness) {
		AutomaticSanadUtil.createSanadHesabdari(organId, sanadHesabdariDate, articles, description, sanadType, sanadStateEnum, saalMaaliId, deletable, numberOfDecimals, currentUser, organDesc);
		return null;
	}
	
	public static List<SanadHesabdariItemEntity> createMergedArticles(List<SanadHesabdariItemEntity> articles, boolean concatDescriptions, Long organId) {
		return null;
	}
	public SanadHesabdariEntity mergeTempSanadHesabdaris(Long organId,Date sanadHesabdariDate, SanadTypeEntity sanadTypeEntity, String description, SaalMaaliEntity saalMaali, int numberOfDecimals, UserEntity currentUser, String organDesc){
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/getMoeenKolTemplateMap", produces = "application/json")
	public Map<Long, ListOrderedMap<String, Object>> getMoeenKolTemplateMap() {
		try{
			SaalMaaliEntity currentUserSaalMaaliEntity = saalMaaliService.getUserActiveSaalMaali(getCurrentOrganVO(), getCurrentUserVO().getId());
			return HesabTemplateRelationsUtil.getMoeenKolTemplateMap(currentUserSaalMaaliEntity.getOrganId(), getCurrentOrganVO().getTopOrgansIdList());
		}catch(NoSaalMaaliFoundException e){
			return new HashMap<>();
		}
	}

	@RequestMapping(method = RequestMethod.GET, path = "/getMoeenTafsiliTemplateMap", produces = "application/json")
	public Map<Long, List<ListOrderedMap<String, Object>>> getMoeenTafsiliTemplateMap() {
		try{
			SaalMaaliEntity currentUserSaalMaaliEntity = saalMaaliService.getUserActiveSaalMaali(getCurrentOrganVO(), getCurrentUserVO().getId());
			return HesabTemplateRelationsUtil.getMoeenTafsiliTemplateMap(currentUserSaalMaaliEntity.getOrganId(), getCurrentOrganVO().getTopOrgansIdList()); 
		}catch(NoSaalMaaliFoundException e){
			return new HashMap<>();
		}
	}

	@RequestMapping(method = RequestMethod.GET, path = "/getTafsiliMoeenTemplateMap", produces = "application/json")
	public Map<Long, List<ListOrderedMap<String, Object>>> getTafsiliMoeenTemplateMap() {
		return HesabTemplateRelationsUtil.getTafsiliMoeenTemplateMap(getCurrentOrganVO().getId(), getCurrentOrganVO().getTopOrgansIdList());
	}

	@RequestMapping(method = RequestMethod.GET, path = "/getTafsiliChildTemplateMap", produces = "application/json")
	public Map<Long, List<ListOrderedMap<String, Object>>> getTafsiliChildTemplateMap() {
		return HesabTemplateRelationsUtil.getTafsiliChildTemplateMap(getCurrentOrganVO().getId(), getCurrentOrganVO().getTopOrgansIdList());
	}
}
