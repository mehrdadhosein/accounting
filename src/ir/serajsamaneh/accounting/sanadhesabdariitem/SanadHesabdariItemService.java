package ir.serajsamaneh.accounting.sanadhesabdariitem;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity;
import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazService;
import ir.serajsamaneh.accounting.articleaccountingmarkaz.ArticleAccountingMarkazEntity;
import ir.serajsamaneh.accounting.articleaccountingmarkaz.ArticleAccountingMarkazService;
import ir.serajsamaneh.accounting.articletafsili.ArticleTafsiliEntity;
import ir.serajsamaneh.accounting.articletafsili.ArticleTafsiliService;
import ir.serajsamaneh.accounting.enumeration.HesabTypeEnum;
import ir.serajsamaneh.accounting.enumeration.MahyatKolEnum;
import ir.serajsamaneh.accounting.enumeration.SanadFunctionEnum;
import ir.serajsamaneh.accounting.enumeration.SanadStateEnum;
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
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SanadHesabdariItemService extends
		BaseEntityService<SanadHesabdariItemEntity, Long> {

	@Override
	protected SanadHesabdariItemDAO getMyDAO() {
		return sanadHesabdariItemDAO;
	}

	SanadHesabdariItemDAO sanadHesabdariItemDAO;
	HesabKolService hesabKolService;
	HesabMoeenService hesabMoeenService;
	HesabTafsiliService hesabTafsiliService;
	SaalMaaliService saalMaaliService;
	SanadHesabdariService sanadHesabdariService;
	AccountingMarkazService accountingMarkazService;
	ArticleTafsiliService articleTafsiliService;
	ArticleAccountingMarkazService articleAccountingMarkazService;

	public ArticleAccountingMarkazService getArticleAccountingMarkazService() {
		return articleAccountingMarkazService;
	}

	public void setArticleAccountingMarkazService(
			ArticleAccountingMarkazService articleAccountingMarkazService) {
		this.articleAccountingMarkazService = articleAccountingMarkazService;
	}

	public ArticleTafsiliService getArticleTafsiliService() {
		return articleTafsiliService;
	}

	public void setArticleTafsiliService(ArticleTafsiliService articleTafsiliService) {
		this.articleTafsiliService = articleTafsiliService;
	}

	public AccountingMarkazService getAccountingMarkazService() {
		return accountingMarkazService;
	}

	public void setAccountingMarkazService(
			AccountingMarkazService accountingMarkazService) {
		this.accountingMarkazService = accountingMarkazService;
	}

	public SanadHesabdariService getSanadHesabdariService() {
		return sanadHesabdariService;
	}

	public void setSanadHesabdariService(SanadHesabdariService sanadHesabdariService) {
		this.sanadHesabdariService = sanadHesabdariService;
	}

	public SaalMaaliService getSaalMaaliService() {
		return saalMaaliService;
	}

	public void setSaalMaaliService(SaalMaaliService saalMaaliService) {
		this.saalMaaliService = saalMaaliService;
	}

	public HesabTafsiliService getHesabTafsiliService() {
		return hesabTafsiliService;
	}

	public void setHesabTafsiliService(HesabTafsiliService hesabTafsiliService) {
		this.hesabTafsiliService = hesabTafsiliService;
	}

	public HesabMoeenService getHesabMoeenService() {
		return hesabMoeenService;
	}

	public void setHesabMoeenService(HesabMoeenService hesabMoeenService) {
		this.hesabMoeenService = hesabMoeenService;
	}

	public HesabKolService getHesabKolService() {
		return hesabKolService;
	}

	public void setHesabKolService(HesabKolService hesabKolService) {
		this.hesabKolService = hesabKolService;
	}

	public void setSanadHesabdariItemDAO(
			SanadHesabdariItemDAO sanadHesabdariItemDAO) {
		this.sanadHesabdariItemDAO = sanadHesabdariItemDAO;
	}

	public SanadHesabdariItemDAO getSanadHesabdariItemDAO() {
		return sanadHesabdariItemDAO;
	}

	public List<SanadHesabdariItemEntity> getTarazKolAzmayeshi(SaalMaaliEntity saalMaaliEntity, Date fromDate, Date toDate, List<Long> hesabKolIds, List<Long> moeenIds,List<Long> tafsiliIds, List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, OrganEntity organEntity) {
		List<SanadHesabdariItemEntity> tarazKolAzmayeshiList = new ArrayList<SanadHesabdariItemEntity>();
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter = populateTarazFilter(saalMaaliEntity, fromDate, toDate, hesabKolIds, moeenIds, tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organEntity);
		
//		localFilter.put("sanadHesabdari.state@eqORsanadHesabdari.state@eq",Arrays.asList(SanadStateEnum.DAEM,SanadStateEnum.BARRESI_SHODE));
//		localFilter.put("sanadHesabdari.sanadFunction@neq@sanadHesabdari.sanadFunction@neq",Arrays.asList(SanadFunctionEnum.EFTETAHIE,SanadFunctionEnum.EKHTETAMIE));
//		
//		if(organEntity!=null)
//			localFilter.put("sanadHesabdari.organ.id@eq", organEntity.getId());
//		
//		localFilter.put("hesabKol.hesabGroup.type@eq", hesabType);
//		localFilter.put("hesabKol.id@in", hesabKolIds);
//		
//		if(saalMaaliEntity!=null && saalMaaliEntity.getId() != null){
//			localFilter.put("sanadHesabdari.saalMaali.id@eq", saalMaaliEntity.getId());
//			if(fromDate==null)
//				localFilter.put("sanadHesabdari.tarikhSanad@ge",getSaalMaaliService().getFirstDateOfSaalMaali(saalMaaliEntity));
//			else
//				localFilter.put("sanadHesabdari.tarikhSanad@ge",fromDate);
//			if(toDate==null)
//				localFilter.put("sanadHesabdari.tarikhSanad@le",getSaalMaaliService().getLastDateOfSaalMaali(saalMaaliEntity));
//			else
//				localFilter.put("sanadHesabdari.tarikhSanad@le",toDate);
//		}else{
//			localFilter.put("sanadHesabdari.tarikhSanad@ge",fromDate);
//			localFilter.put("sanadHesabdari.tarikhSanad@le",toDate);
////			return tarazKolAzmayeshiList;
//		}

		List<Object[]> rawList = getMyDAO().getTarazKolAzmayeshi(localFilter);
		Map<Long, SanadHesabdariItemEntity> tarazKolAzmayeshiMandeh = getTarazKolAzmayeshiMandeh(saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organEntity);
		
		for (int i=0; i<rawList.size(); i++) {
			Object[] object = rawList.get(i);
			SanadHesabdariItemEntity entity = new SanadHesabdariItemEntity();
			Object[] a = (Object[])object;
			Long hesabKolId = new Long(a[0].toString());
			entity.setHesabKol(getHesabKolService().load(hesabKolId));
			
//			String kolCode = (String) a[1];
					
			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);
			
			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);
			if(tarazKolAzmayeshiMandeh.get(hesabKolId)!=null){
				entity.setMandehBedehkarEbtedayDore(tarazKolAzmayeshiMandeh.get(hesabKolId).getMandehBedehkar());
				entity.setMandehBestankarEbtedayDore(tarazKolAzmayeshiMandeh.get(hesabKolId).getMandehBestankar());
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore()+entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore()+entity.getBestankar());
			}else{
				entity.setMandehBedehkarEbtedayDore(0d);
				entity.setMandehBestankarEbtedayDore(0d);
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore()+entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore()+entity.getBestankar());
			}
			
			if(entity.getBedehkar() - entity.getBestankar() > 0){
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
				if(entity.getHesabKol().getMahyatKol().equals(MahyatKolEnum.Bedehkar)){
					entity.setMandehByMahiyatHesabStr(new DecimalFormat().format(entity.getMandehBedehkar()));
					entity.setMandehByMahiyatHesabDbl(entity.getMandehBedehkar());
				}
				else if(entity.getHesabKol().getMahyatKol().equals(MahyatKolEnum.Bestankar)){
					entity.setMandehByMahiyatHesabStr("("+new DecimalFormat().format(entity.getMandehBedehkar())+")");
					entity.setMandehByMahiyatHesabDbl(entity.getMandehBedehkar()*-1);
				}
			}
			else if(entity.getBestankar()-entity.getBedehkar() > 0){
				entity.setMandehBestankar(entity.getBestankar()-entity.getBedehkar());
				
				if(entity.getHesabKol().getMahyatKol().equals(MahyatKolEnum.Bedehkar)){
					entity.setMandehByMahiyatHesabStr("("+new DecimalFormat().format(entity.getMandehBestankar())+")");
					entity.setMandehByMahiyatHesabDbl(entity.getMandehBestankar()*-1);
				}
				else if(entity.getHesabKol().getMahyatKol().equals(MahyatKolEnum.Bestankar)){
					entity.setMandehByMahiyatHesabStr(new DecimalFormat().format(entity.getMandehBestankar()));
					entity.setMandehByMahiyatHesabDbl(entity.getMandehBestankar());
				}
				
			}
			tarazKolAzmayeshiList.add(entity);
		}
		return tarazKolAzmayeshiList;
	}


	//this method is used to calculate the MANDEH EBTEDAYTE DORE
	private Map<Long, SanadHesabdariItemEntity> getTarazKolAzmayeshiMandeh(SaalMaaliEntity saalMaaliEntity, Date fromDate, List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds, List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, OrganEntity organEntity) {
		Map<Long, SanadHesabdariItemEntity> tarazKolAzmayeshiMap = new HashMap<Long, SanadHesabdariItemEntity>();
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter = populateMandehTarazFilter(saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organEntity);
		
//		localFilter.put("sanadHesabdari.state@eqORsanadHesabdari.state@eq", Arrays.asList(SanadStateEnum.DAEM,SanadStateEnum.BARRESI_SHODE));
//		localFilter.put("sanadHesabdari.sanadFunction@neq@sanadHesabdari.sanadFunction@neq",Arrays.asList(SanadFunctionEnum.EFTETAHIE,SanadFunctionEnum.EKHTETAMIE));
//		
//		if(organEntity!=null)
//			localFilter.put("sanadHesabdari.organ.id@eq", organEntity.getId());
//		
//		localFilter.put("hesabKol.hesabGroup.type@eq", hesabType);
//		localFilter.put("hesabKol.id@in", hesabKolIds);
//		
//		if(saalMaaliEntity!=null && saalMaaliEntity.getId() != null){
//			localFilter.put("sanadHesabdari.saalMaali.id@eq", saalMaaliEntity.getId());
//			if(fromDate!=null)
//				localFilter.put("sanadHesabdari.tarikhSanad@le",fromDate);
//			else
//				localFilter.put("sanadHesabdari.tarikhSanad@le",getSaalMaaliService().getFirstDateOfSaalMaali(saalMaaliEntity));
//			localFilter.put("sanadHesabdari.tarikhSanad@ge",getSaalMaaliService().getFirstDateOfSaalMaali(saalMaaliEntity));
//		}else{
//			if(fromDate!=null)
//				localFilter.put("sanadHesabdari.tarikhSanad@le",fromDate);
//			else
//				return tarazKolAzmayeshiMap;
//		}
		
		List<Object[]> rawList = getMyDAO().getTarazKolAzmayeshi(localFilter);
		for (Object[] object : rawList) {
			SanadHesabdariItemEntity entity = new SanadHesabdariItemEntity();
			Object[] a = (Object[])object;
			Long hesabKolId = new Long(a[0].toString());
			entity.setHesabKol(getHesabKolService().load(hesabKolId));

//			String kolCode = (String) a[1];
			
			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);

			
			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);
			if(entity.getBedehkar() - entity.getBestankar() > 0)
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
			else if(entity.getBestankar()-entity.getBedehkar() > 0)
				entity.setMandehBestankar(entity.getBestankar()-entity.getBedehkar());
			tarazKolAzmayeshiMap.put(hesabKolId, entity);
		}
		
		if(organEntity == null)
			applySanadEftetahiehOnTarazKolAzmayeshiMandeh(tarazKolAzmayeshiMap, saalMaaliEntity);
		else
			applySanadEftetahiehOnTarazKolAzmayeshiMandeh(tarazKolAzmayeshiMap, saalMaaliEntity, organEntity);
		return tarazKolAzmayeshiMap;
	}
	
	private void applySanadEftetahiehOnTarazKolAzmayeshiMandeh(Map<Long, SanadHesabdariItemEntity> tarazKolAzmayeshiMap, SaalMaaliEntity saalMaaliEntity) {
		List<SanadHesabdariEntity> sanadEftetahiehha = getSanadHesabdariService().getSanadEftetahiehha(saalMaaliEntity);
		for (SanadHesabdariEntity sanadEftetahieh : sanadEftetahiehha) {
			applySanadEftetahiehOnTarazKolAzmayeshiMandeh(tarazKolAzmayeshiMap, sanadEftetahieh);
		}
	}
	
	private void applySanadEftetahiehOnTarazKolAzmayeshiMandeh(Map<Long, SanadHesabdariItemEntity> tarazKolAzmayeshiMap, SaalMaaliEntity saalMaaliEntity, OrganEntity organEntity) {
		SanadHesabdariEntity sanadEftetahieh = getSanadHesabdariService().getSanadEftetahieh(saalMaaliEntity,organEntity);
		if(sanadEftetahieh == null || sanadEftetahieh.getSanadHesabdariItem() == null)
			return;
		applySanadEftetahiehOnTarazKolAzmayeshiMandeh(tarazKolAzmayeshiMap, sanadEftetahieh);
	}

	private void applySanadEftetahiehOnTarazKolAzmayeshiMandeh(
			Map<Long, SanadHesabdariItemEntity> tarazKolAzmayeshiMap,
			SanadHesabdariEntity sanadEftetahieh) {
		List<SanadHesabdariItemEntity> sanadHesabdariItems = sanadEftetahieh.getSanadHesabdariItem();
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItems) {
			HesabKolEntity hesabKolEntity = sanadHesabdariItemEntity.getHesabKol();
			Long hesabKolId = hesabKolEntity.getId();
			SanadHesabdariItemEntity itemEntity = tarazKolAzmayeshiMap.get(hesabKolId);
			if(itemEntity == null){
				sanadHesabdariItemEntity.setMandehBedehkar(0d);
				sanadHesabdariItemEntity.setMandehBestankar(0d);

				tarazKolAzmayeshiMap.put(hesabKolId, sanadHesabdariItemEntity);
			}
			else{
				itemEntity.setBestankar(itemEntity.getBestankar()+sanadHesabdariItemEntity.getBestankar());
				itemEntity.setBedehkar(itemEntity.getBedehkar()+sanadHesabdariItemEntity.getBedehkar());
			}
			
		}
	}
	
	private void applySanadEftetahiehOnTarazMoeenAzmayeshiMandeh(Map<Long, SanadHesabdariItemEntity> tarazMoeenAzmayeshiMap, SaalMaaliEntity saalMaaliEntity) {
		List<SanadHesabdariEntity> sanadEftetahiehha = getSanadHesabdariService().getSanadEftetahiehha(saalMaaliEntity);
		for (SanadHesabdariEntity sanadEftetahieh : sanadEftetahiehha) {
			applySanadEftetahiehOnTarazMoeenAzmayeshiMandeh(tarazMoeenAzmayeshiMap, sanadEftetahieh);
		}
	}
	
	private void applySanadEftetahiehOnTarazMoeenAzmayeshiMandeh(Map<Long, SanadHesabdariItemEntity> tarazMoeenAzmayeshiMap, SaalMaaliEntity saalMaaliEntity, OrganEntity organEntity) {
		SanadHesabdariEntity sanadEftetahieh = getSanadHesabdariService().getSanadEftetahieh(saalMaaliEntity,organEntity);
		if(sanadEftetahieh == null || sanadEftetahieh.getSanadHesabdariItem() == null)
			return;
		applySanadEftetahiehOnTarazMoeenAzmayeshiMandeh(tarazMoeenAzmayeshiMap, sanadEftetahieh);
	}

	private void applySanadEftetahiehOnTarazMoeenAzmayeshiMandeh(
			Map<Long, SanadHesabdariItemEntity> tarazMoeenAzmayeshiMap,
			SanadHesabdariEntity sanadEftetahieh) {
		List<SanadHesabdariItemEntity> sanadHesabdariItems = sanadEftetahieh.getSanadHesabdariItem();
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItems) {
			HesabMoeenEntity hesabMoeenEntity = sanadHesabdariItemEntity.getHesabMoeen();
			Long hesabMoeenId = hesabMoeenEntity.getId();
			SanadHesabdariItemEntity itemEntity = tarazMoeenAzmayeshiMap.get(hesabMoeenId);
			if(itemEntity == null){
				sanadHesabdariItemEntity.setMandehBedehkar(0d);
				sanadHesabdariItemEntity.setMandehBestankar(0d);
				tarazMoeenAzmayeshiMap.put(hesabMoeenId, sanadHesabdariItemEntity);
			}
			else{
				itemEntity.setBestankar(itemEntity.getBestankar()+sanadHesabdariItemEntity.getBestankar());
				itemEntity.setBedehkar(itemEntity.getBedehkar()+sanadHesabdariItemEntity.getBedehkar());
			}
			
		}
	}
	
	
	private void applySanadEftetahiehOnTarazTafsiliAzmayeshiMandeh(Map<Long, SanadHesabdariItemEntity> tarazTafsiliAzmayeshiMap, SaalMaaliEntity saalMaaliEntity) {
		List<SanadHesabdariEntity> sanadEftetahiehha = getSanadHesabdariService().getSanadEftetahiehha(saalMaaliEntity);
		for (SanadHesabdariEntity sanadEftetahieh : sanadEftetahiehha) {
			applySanadEftetahiehOnTarazTafsiliAzmayeshiMandeh(tarazTafsiliAzmayeshiMap, sanadEftetahieh);
		}
	}
	
	private void applySanadEftetahiehOnTarazTafsiliAzmayeshiMandeh(Map<Long, SanadHesabdariItemEntity> tarazTafsiliAzmayeshiMap, SaalMaaliEntity saalMaaliEntity, OrganEntity organEntity) {
		SanadHesabdariEntity sanadEftetahieh = getSanadHesabdariService().getSanadEftetahieh(saalMaaliEntity,organEntity);
		if(sanadEftetahieh == null || sanadEftetahieh.getSanadHesabdariItem() == null)
			return;
		applySanadEftetahiehOnTarazTafsiliAzmayeshiMandeh(tarazTafsiliAzmayeshiMap, sanadEftetahieh);
	}

	private void applySanadEftetahiehOnTarazTafsiliAzmayeshiMandeh(
			Map<Long, SanadHesabdariItemEntity> tarazTafsiliAzmayeshiMap,
			SanadHesabdariEntity sanadEftetahieh) {
		List<SanadHesabdariItemEntity> sanadHesabdariItems = sanadEftetahieh.getSanadHesabdariItem();
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItems) {
			HesabTafsiliEntity hesabTafsiliEntity = sanadHesabdariItemEntity.getHesabTafsili();
			if(hesabTafsiliEntity == null || hesabTafsiliEntity.getId() == null)
				continue;
			Long hesabTafsiliId = hesabTafsiliEntity.getId();
			SanadHesabdariItemEntity itemEntity = tarazTafsiliAzmayeshiMap.get(hesabTafsiliId);
			if(itemEntity == null){
				sanadHesabdariItemEntity.setMandehBedehkar(0d);
				sanadHesabdariItemEntity.setMandehBestankar(0d);

				tarazTafsiliAzmayeshiMap.put(hesabTafsiliId, sanadHesabdariItemEntity);
			}
			else{
				itemEntity.setBestankar(itemEntity.getBestankar()+sanadHesabdariItemEntity.getBestankar());
				itemEntity.setBedehkar(itemEntity.getBedehkar()+sanadHesabdariItemEntity.getBedehkar());
			}
			
		}
	}
	
	private void applySanadEftetahiehOnTarazMarkazAzmayeshiMandeh(Map<Long, SanadHesabdariItemEntity> tarazMarkazAzmayeshiMap, SaalMaaliEntity saalMaaliEntity) {
		List<SanadHesabdariEntity> sanadEftetahiehha = getSanadHesabdariService().getSanadEftetahiehha(saalMaaliEntity);
		for (SanadHesabdariEntity sanadEftetahieh : sanadEftetahiehha) {
			applySanadEftetahiehOnTarazMarkazAzmayeshiMandeh(tarazMarkazAzmayeshiMap, sanadEftetahieh);
		}
	}
	
	private void applySanadEftetahiehOnTarazMarkazAzmayeshiMandeh(Map<Long, SanadHesabdariItemEntity> tarazMarkazAzmayeshiMap, SaalMaaliEntity saalMaaliEntity, OrganEntity organEntity) {
		SanadHesabdariEntity sanadEftetahieh = getSanadHesabdariService().getSanadEftetahieh(saalMaaliEntity,organEntity);
		if(sanadEftetahieh == null || sanadEftetahieh.getSanadHesabdariItem() == null)
			return;
		applySanadEftetahiehOnTarazMarkazAzmayeshiMandeh(tarazMarkazAzmayeshiMap, sanadEftetahieh);
	}

	private void applySanadEftetahiehOnTarazMarkazAzmayeshiMandeh(
			Map<Long, SanadHesabdariItemEntity> tarazMarkazAzmayeshiMap,
			SanadHesabdariEntity sanadEftetahieh) {
		List<SanadHesabdariItemEntity> sanadHesabdariItems = sanadEftetahieh.getSanadHesabdariItem();
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItems) {
			AccountingMarkazEntity accountingMarkazEntity = sanadHesabdariItemEntity.getAccountingMarkaz();
			
			if(accountingMarkazEntity == null || accountingMarkazEntity.getId() == null)
				continue;
			
			Long accountingMarkazId = accountingMarkazEntity.getId();
			SanadHesabdariItemEntity itemEntity = tarazMarkazAzmayeshiMap.get(accountingMarkazId);
			if(itemEntity == null)
				tarazMarkazAzmayeshiMap.put(accountingMarkazId, sanadHesabdariItemEntity);
			else{
				itemEntity.setBestankar(itemEntity.getBestankar()+sanadHesabdariItemEntity.getBestankar());
				itemEntity.setBedehkar(itemEntity.getBedehkar()+sanadHesabdariItemEntity.getBedehkar());
			}
			
		}
	}

	private Map<Long, SanadHesabdariItemEntity> getTarazMoeenAzmayeshiMandeh(SaalMaaliEntity saalMaaliEntity, Date fromDate, List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds, List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, OrganEntity organEntity) {
		Map<Long, SanadHesabdariItemEntity> tarazMoeenAzmayeshiMap = new HashMap<Long, SanadHesabdariItemEntity>();
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter = populateMandehTarazFilter(saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organEntity);
		
//		List<SanadStateEnum> sanadStates = new ArrayList<SanadStateEnum>();
//		sanadStates.add(SanadStateEnum.DAEM);
//		sanadStates.add(SanadStateEnum.BARRESI_SHODE);
//		localFilter.put("sanadHesabdari.state@eqORsanadHesabdari.state@eq", sanadStates);
//		
//		if(organEntity!=null)
//			localFilter.put("sanadHesabdari.organ.id@eq", organEntity.getId());
//		
//		localFilter.put("hesabKol.hesabGroup.type@eq", hesabType);
//		localFilter.put("hesabMoeen.id@in", moeenIds);
//		localFilter.put("hesabKol.id@in", hesabKolIds);
//		
//		if(saalMaaliEntity!=null && saalMaaliEntity.getId() != null){
//			localFilter.put("sanadHesabdari.saalMaali.id@eq", saalMaaliEntity.getId());
//			if(fromDate!=null)
//				localFilter.put("sanadHesabdari.tarikhSanad@le",fromDate);
//			else
//				localFilter.put("sanadHesabdari.tarikhSanad@le",getSaalMaaliService().getFirstDateOfSaalMaali(saalMaaliEntity));
//			localFilter.put("sanadHesabdari.tarikhSanad@ge",getSaalMaaliService().getFirstDateOfSaalMaali(saalMaaliEntity));
//		}else{
//			if(fromDate!=null)
//				localFilter.put("sanadHesabdari.tarikhSanad@le",fromDate);
//			else
//				return tarazMoeenAzmayeshiMap;
//		}
		
		List<Object[]> rawList = getMyDAO().getTarazMoeenAzmayeshi(localFilter);
		for (Object[] a : rawList) {
			SanadHesabdariItemEntity entity = new SanadHesabdariItemEntity();
			Long hesabMoeenId = new Long(a[0].toString());
			entity.setHesabMoeen(getHesabMoeenService().load(hesabMoeenId));
			
//			String moeenCode = (String) a[1];
			
			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);
			
			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);
			if(entity.getBedehkar() - entity.getBestankar() > 0)
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
			else if(entity.getBestankar()-entity.getBedehkar() > 0)
				entity.setMandehBestankar(entity.getBestankar()-entity.getBedehkar());
			tarazMoeenAzmayeshiMap.put(hesabMoeenId, entity);
		}
		
		if(organEntity == null)
			applySanadEftetahiehOnTarazMoeenAzmayeshiMandeh(tarazMoeenAzmayeshiMap, saalMaaliEntity);
		else
			applySanadEftetahiehOnTarazMoeenAzmayeshiMandeh(tarazMoeenAzmayeshiMap, saalMaaliEntity, organEntity);
		
		return tarazMoeenAzmayeshiMap;
	}
	
	
	private Map<Long, SanadHesabdariItemEntity> getTarazTafsiliAzmayeshiMandeh(SaalMaaliEntity saalMaaliEntity, Date fromDate, List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds, List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, OrganEntity organEntity) {
		Map<Long, SanadHesabdariItemEntity> tarazTafsiliAzmayeshiMap = new HashMap<Long, SanadHesabdariItemEntity>();
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter = populateMandehTarazFilter(saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organEntity);
		
//		List<SanadStateEnum> sanadStates = new ArrayList<SanadStateEnum>();
//		sanadStates.add(SanadStateEnum.DAEM);
//		sanadStates.add(SanadStateEnum.BARRESI_SHODE);
//		localFilter.put("sanadHesabdari.state@eqORsanadHesabdari.state@eq", sanadStates);
//		
//		if(organEntity!=null)
//			localFilter.put("sanadHesabdari.organ.id@eq", organEntity.getId());
//		
//		localFilter.put("hesabKol.hesabGroup.type@eq", hesabType);
//		localFilter.put("hesabMoeen.id@in", moeenIds);
//		localFilter.put("hesabKol.id@in", hesabKolIds);
//		
//		if(saalMaaliEntity!=null && saalMaaliEntity.getId() != null){
//			localFilter.put("sanadHesabdari.saalMaali.id@eq", saalMaaliEntity.getId());
//			if(fromDate!=null)
//				localFilter.put("sanadHesabdari.tarikhSanad@le",fromDate);
//			else
//				localFilter.put("sanadHesabdari.tarikhSanad@le",getSaalMaaliService().getFirstDateOfSaalMaali(saalMaaliEntity));
//			localFilter.put("sanadHesabdari.tarikhSanad@ge",getSaalMaaliService().getFirstDateOfSaalMaali(saalMaaliEntity));
//		}else{
//			if(fromDate!=null)
//				localFilter.put("sanadHesabdari.tarikhSanad@le",fromDate);
//			else
//				return tarazTafsiliAzmayeshiMap;
//		}
		
		List<Object[]> rawList = getMyDAO().getTarazTafsiliAzmayeshi(localFilter);
		for (Object[] object : rawList) {
			SanadHesabdariItemEntity entity = new SanadHesabdariItemEntity();
			Object[] a = (Object[])object;
			
			Long hesabTafsiliId = -1l;
			if(a[0]!=null){
				hesabTafsiliId = new Long(a[0].toString());
				entity.setHesabTafsili(getHesabTafsiliService().load(hesabTafsiliId));
			}else{
				HesabTafsiliEntity nullHesabTafsili = new HesabTafsiliEntity();
				nullHesabTafsili.setName(SerajMessageUtil.getMessage("common_undefined"));
				entity.setHesabTafsili(nullHesabTafsili);
			}
			
//			String tafsiliCode = (String) a[1];
			
			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);
			
			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);
			if(entity.getBedehkar() - entity.getBestankar() > 0)
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
			else if(entity.getBestankar()-entity.getBedehkar() > 0)
				entity.setMandehBestankar(entity.getBestankar()-entity.getBedehkar());
			tarazTafsiliAzmayeshiMap.put(hesabTafsiliId, entity);
		}
		
		if(organEntity == null)
			applySanadEftetahiehOnTarazTafsiliAzmayeshiMandeh(tarazTafsiliAzmayeshiMap, saalMaaliEntity);
		else
			applySanadEftetahiehOnTarazTafsiliAzmayeshiMandeh(tarazTafsiliAzmayeshiMap, saalMaaliEntity, organEntity);
		
		return tarazTafsiliAzmayeshiMap;
	}
	
	private Map<Long, SanadHesabdariItemEntity> getTarazTafsiliShenavarAzmayeshiMandeh(SaalMaaliEntity saalMaaliEntity, Date fromDate, List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds, List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, OrganEntity organEntity) {
		Map<Long, SanadHesabdariItemEntity> tarazTafsiliAzmayeshiMap = new HashMap<Long, SanadHesabdariItemEntity>();
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter = populateMandehTarazFilter(saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organEntity);
		
//		List<SanadStateEnum> sanadStates = new ArrayList<SanadStateEnum>();
//		sanadStates.add(SanadStateEnum.DAEM);
//		sanadStates.add(SanadStateEnum.BARRESI_SHODE);
//		localFilter.put("sanadHesabdari.state@eqORsanadHesabdari.state@eq", sanadStates);
//		
//		if(organEntity!=null)
//			localFilter.put("sanadHesabdari.organ.id@eq", organEntity.getId());
//		
//		localFilter.put("hesabKol.hesabGroup.type@eq", hesabType);
//		localFilter.put("hesabMoeen.id@in", moeenIds);
//		localFilter.put("hesabKol.id@in", hesabKolIds);
//		
//		if(saalMaaliEntity!=null && saalMaaliEntity.getId() != null){
//			localFilter.put("sanadHesabdari.saalMaali.id@eq", saalMaaliEntity.getId());
//			if(fromDate!=null)
//				localFilter.put("sanadHesabdari.tarikhSanad@le",fromDate);
//			else
//				localFilter.put("sanadHesabdari.tarikhSanad@le",getSaalMaaliService().getFirstDateOfSaalMaali(saalMaaliEntity));
//			localFilter.put("sanadHesabdari.tarikhSanad@ge",getSaalMaaliService().getFirstDateOfSaalMaali(saalMaaliEntity));
//		}else{
//			if(fromDate!=null)
//				localFilter.put("sanadHesabdari.tarikhSanad@le",fromDate);
//			else
//				return tarazTafsiliAzmayeshiMap;
//		}
		
		localFilter.put("articleTafsili.level@eq", 2);
		List<Object[]> rawList = getMyDAO().getTarazTafsiliAzmayeshi(localFilter);
		for (Object[] object : rawList) {
			SanadHesabdariItemEntity entity = new SanadHesabdariItemEntity();
			Object[] a = (Object[])object;
			
			Long hesabTafsiliId = -1l;
			if(a[0]!=null){
				hesabTafsiliId = new Long(a[0].toString());
				entity.setHesabTafsili(getHesabTafsiliService().load(hesabTafsiliId));
			}else{
				HesabTafsiliEntity nullHesabTafsili = new HesabTafsiliEntity();
				nullHesabTafsili.setName(SerajMessageUtil.getMessage("common_undefined"));
				entity.setHesabTafsili(nullHesabTafsili);
			}
			
//			String tafsiliCode = (String) a[1];
			
			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);
			
			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);
			if(entity.getBedehkar() - entity.getBestankar() > 0)
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
			else if(entity.getBestankar()-entity.getBedehkar() > 0)
				entity.setMandehBestankar(entity.getBestankar()-entity.getBedehkar());
			tarazTafsiliAzmayeshiMap.put(hesabTafsiliId, entity);
		}
		
		if(organEntity == null)
			applySanadEftetahiehOnTarazTafsiliAzmayeshiMandeh(tarazTafsiliAzmayeshiMap, saalMaaliEntity);
		else
			applySanadEftetahiehOnTarazTafsiliAzmayeshiMandeh(tarazTafsiliAzmayeshiMap, saalMaaliEntity, organEntity);
		
		return tarazTafsiliAzmayeshiMap;
	}
	
	private Map<Long, SanadHesabdariItemEntity> getTarazAccountingMarkazAzmayeshiMandeh(SaalMaaliEntity saalMaaliEntity, Date fromDate, List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds, List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, OrganEntity organEntity) {
		Map<Long, SanadHesabdariItemEntity> tarazAccountingMarkazAzmayeshiMap = new HashMap<Long, SanadHesabdariItemEntity>();
		Map<String, Object> localFilter =  new HashMap<String, Object>();
		localFilter = populateMandehTarazFilter(saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organEntity);

		
//		List<SanadStateEnum> sanadStates = new ArrayList<SanadStateEnum>();
//		sanadStates.add(SanadStateEnum.DAEM);
//		sanadStates.add(SanadStateEnum.BARRESI_SHODE);
//		filter.put("sanadHesabdari.state@eqORsanadHesabdari.state@eq", sanadStates);
//		
//		if(organEntity!=null)
//			filter.put("sanadHesabdari.organ.id@eq", organEntity.getId());
//		
//		filter.put("hesabKol.hesabGroup.type@eq", hesabType);
//		filter.put("hesabMoeen.id@in", moeenIds);
//		filter.put("hesabKol.id@in", hesabKolIds);
//		
//		if(saalMaaliEntity!=null && saalMaaliEntity.getId() != null){
//			filter.put("sanadHesabdari.saalMaali.id@eq", saalMaaliEntity.getId());
//			if(fromDate!=null)
//				filter.put("sanadHesabdari.tarikhSanad@le",fromDate);
//			else
//				filter.put("sanadHesabdari.tarikhSanad@le",getSaalMaaliService().getFirstDateOfSaalMaali(saalMaaliEntity));
//			filter.put("sanadHesabdari.tarikhSanad@ge",getSaalMaaliService().getFirstDateOfSaalMaali(saalMaaliEntity));
//		}else{
//			if(fromDate!=null)
//				filter.put("sanadHesabdari.tarikhSanad@le",fromDate);
//			else
//				return tarazAccountingMarkazAzmayeshiMap;
//		}
		
		localFilter.put("articleTafsili.level@eq", 2);
		List<Object[]> rawList = getMyDAO().getTarazAccountingMarkazShenavarAzmayeshi(localFilter);
		for (Object[] object : rawList) {
			SanadHesabdariItemEntity entity = new SanadHesabdariItemEntity();
			Object[] a = (Object[])object;
			
			Long accountingMarkazId = -1l;
			if(a[0]!=null){
				accountingMarkazId = new Long(a[0].toString());
				entity.setAccountingMarkaz(getAccountingMarkazService().load(accountingMarkazId));
			}else{
				AccountingMarkazEntity nullAccountingMarkazEntity = new AccountingMarkazEntity();
				nullAccountingMarkazEntity.setName(SerajMessageUtil.getMessage("common_undefined"));
				entity.setAccountingMarkaz(nullAccountingMarkazEntity);
			}
			
//			String accountingMarkazCode = (String) a[1];
			
			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);
			
			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);
			if(entity.getBedehkar() - entity.getBestankar() > 0)
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
			else if(entity.getBestankar()-entity.getBedehkar() > 0)
				entity.setMandehBestankar(entity.getBestankar()-entity.getBedehkar());
			tarazAccountingMarkazAzmayeshiMap.put(accountingMarkazId, entity);
		}
		
		if(organEntity == null)
			applySanadEftetahiehOnTarazMarkazAzmayeshiMandeh(tarazAccountingMarkazAzmayeshiMap, saalMaaliEntity);
		else
			applySanadEftetahiehOnTarazMarkazAzmayeshiMandeh(tarazAccountingMarkazAzmayeshiMap, saalMaaliEntity, organEntity);
		
		return tarazAccountingMarkazAzmayeshiMap;
	}
	
	public List<SanadHesabdariItemEntity> getTarazMoeenAzmayeshi(SaalMaaliEntity saalMaaliEntity, Date fromDate, Date toDate, List<Long> hesabKolIds, List<Long> moeenIds,List<Long> tafsiliIds, List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, OrganEntity organEntity) {
		Map<String, Object> localFilter =  new HashMap<String, Object>();
		localFilter = populateTarazFilter(saalMaaliEntity, fromDate, toDate, hesabKolIds, moeenIds, tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organEntity);
		List<SanadHesabdariItemEntity> tarazMoeenAzmayeshiList = new ArrayList<SanadHesabdariItemEntity>();
		
//		List<SanadStateEnum> sanadStates = new ArrayList<SanadStateEnum>();
//		sanadStates.add(SanadStateEnum.DAEM);
//		sanadStates.add(SanadStateEnum.BARRESI_SHODE);
//		localFilter.put("sanadHesabdari.state@eqORsanadHesabdari.state@eq", sanadStates);
//		
//		if(organEntity!=null)
//			localFilter.put("sanadHesabdari.organ.id@eq", organEntity.getId());
//		
//		localFilter.put("hesabKol.hesabGroup.type@eq", hesabType);
//		localFilter.put("hesabMoeen.id@in", moeenIds);
//		localFilter.put("hesabKol.id@in", hesabKolIds);
//		
//		if(saalMaaliEntity!=null && saalMaaliEntity.getId() != null){
//			localFilter.put("sanadHesabdari.saalMaali.id@eq", saalMaaliEntity.getId());
//			if(fromDate==null)
//				localFilter.put("sanadHesabdari.tarikhSanad@ge",getSaalMaaliService().getFirstDateOfSaalMaali(saalMaaliEntity));
//			else
//				localFilter.put("sanadHesabdari.tarikhSanad@ge",fromDate);
//			if(toDate==null)
//				localFilter.put("sanadHesabdari.tarikhSanad@le",getSaalMaaliService().getLastDateOfSaalMaali(saalMaaliEntity));
//			else
//				localFilter.put("sanadHesabdari.tarikhSanad@le",toDate);
//		}else{
//			localFilter.put("sanadHesabdari.tarikhSanad@ge",fromDate);
//			localFilter.put("sanadHesabdari.tarikhSanad@le",toDate);
////			return tarazKolAzmayeshiList;
//		}

		List<Object[]> rawList = getMyDAO().getTarazMoeenAzmayeshi(localFilter);
		Map<Long, SanadHesabdariItemEntity> tarazMoeenAzmayeshiMandeh = getTarazMoeenAzmayeshiMandeh(saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organEntity);
		for (Object object : rawList) {
			SanadHesabdariItemEntity entity = new SanadHesabdariItemEntity();
			Object[] a = (Object[])object;
			Long hesabMoeenId = new Long(a[0].toString());
			HesabMoeenEntity hesabMoeenEntity = getHesabMoeenService().load(hesabMoeenId);
			entity.setHesabMoeen(hesabMoeenEntity);
			entity.setHesabKol(hesabMoeenEntity.getHesabKol());

//			String moeenCode = (String) a[1];
			
			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);
			
			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);

			if(tarazMoeenAzmayeshiMandeh.get(hesabMoeenId)!=null){
				entity.setMandehBedehkarEbtedayDore(tarazMoeenAzmayeshiMandeh.get(hesabMoeenId).getMandehBedehkar());
				entity.setMandehBestankarEbtedayDore(tarazMoeenAzmayeshiMandeh.get(hesabMoeenId).getMandehBestankar());
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore()+entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore()+entity.getBestankar());
			}else{
				entity.setMandehBedehkarEbtedayDore(0d);
				entity.setMandehBestankarEbtedayDore(0d);
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore()+entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore()+entity.getBestankar());
			}

			if(entity.getBedehkar() - entity.getBestankar() > 0)
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
			else if(entity.getBestankar()-entity.getBedehkar() > 0)
				entity.setMandehBestankar(entity.getBestankar()-entity.getBedehkar());
			tarazMoeenAzmayeshiList.add(entity);
		}
		return tarazMoeenAzmayeshiList;
	}
	
	public List<SanadHesabdariItemEntity> getTarazTafsiliAzmayeshi(SaalMaaliEntity saalMaaliEntity, Date fromDate, Date toDate,List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds, List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, OrganEntity organEntity) {
		List<SanadHesabdariItemEntity> tarazTafsiliAzmayeshiList = new ArrayList<SanadHesabdariItemEntity>();
		Map<String, Object> filter = populateTarazFilter(saalMaaliEntity,
				fromDate, toDate, hesabKolIds, moeenIds, tafsiliIds,
				articleTafsiliIds,accountingMarkazIds, hesabType, organEntity);
		
		List<Object[]> rawList = getMyDAO().getTarazTafsiliAzmayeshi(filter);
		Map<Long, SanadHesabdariItemEntity> tarazTafsiliAzmayeshiMandeh = getTarazTafsiliAzmayeshiMandeh(saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organEntity);
		
		for (Object[] object : rawList) {
			SanadHesabdariItemEntity entity = new SanadHesabdariItemEntity();
			Object[] a = (Object[])object;
			Long hesabTafsiliId = -1l;
			if(a[0]!=null){
				hesabTafsiliId = new Long(a[0].toString());
				HesabTafsiliEntity hesabTafsiliEntity = getHesabTafsiliService().load(hesabTafsiliId);
				entity.setHesabTafsili(hesabTafsiliEntity);
			}else{
				HesabTafsiliEntity nullHesabTafsili = new HesabTafsiliEntity();
				nullHesabTafsili.setDesc(SerajMessageUtil.getMessage("common_undefined"));
				entity.setHesabTafsili(nullHesabTafsili);
			}
			
//			String tafsiliCode = (String) a[1];
			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);
			
			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);

			if(tarazTafsiliAzmayeshiMandeh.get(hesabTafsiliId)!=null){
				entity.setMandehBedehkarEbtedayDore(tarazTafsiliAzmayeshiMandeh.get(hesabTafsiliId).getMandehBedehkar());
				entity.setMandehBestankarEbtedayDore(tarazTafsiliAzmayeshiMandeh.get(hesabTafsiliId).getMandehBestankar());
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore()+entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore()+entity.getBestankar());
			}else{
				entity.setMandehBedehkarEbtedayDore(0d);
				entity.setMandehBestankarEbtedayDore(0d);
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore()+entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore()+entity.getBestankar());
			}
			
			if(entity.getBedehkar() - entity.getBestankar() > 0)
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
			else if(entity.getBestankar()-entity.getBedehkar() > 0)
				entity.setMandehBestankar(entity.getBestankar()-entity.getBedehkar());
			tarazTafsiliAzmayeshiList.add(entity);
		}
		return tarazTafsiliAzmayeshiList;
	}

	public List<SanadHesabdariItemEntity> getTarazTafsiliShenavarAzmayeshi(SaalMaaliEntity saalMaaliEntity, Date fromDate, Date toDate,List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds, List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, OrganEntity organEntity) {
		List<SanadHesabdariItemEntity> tarazTafsiliAzmayeshiList = new ArrayList<SanadHesabdariItemEntity>();
		Map<String, Object> filter = populateTarazFilter(saalMaaliEntity,
				fromDate, toDate, hesabKolIds, moeenIds, tafsiliIds,
				articleTafsiliIds,accountingMarkazIds, hesabType, organEntity);
		
		filter.put("articleTafsili.level@eq", 2);
		
		List<Object[]> rawList = getMyDAO().getTarazShenavarAzmayeshi(filter);
		Map<Long, SanadHesabdariItemEntity> tarazTafsiliAzmayeshiMandeh = getTarazTafsiliShenavarAzmayeshiMandeh(saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organEntity);
		
		for (Object[] object : rawList) {
			SanadHesabdariItemEntity entity = new SanadHesabdariItemEntity();
			Object[] a = (Object[])object;
			Long hesabTafsiliId = -1l;
			if(a[0]!=null){
				hesabTafsiliId = new Long(a[0].toString());
				HesabTafsiliEntity hesabTafsiliEntity = getHesabTafsiliService().load(hesabTafsiliId);
				entity.setHesabTafsili(hesabTafsiliEntity);
			}else{
				HesabTafsiliEntity nullHesabTafsili = new HesabTafsiliEntity();
				nullHesabTafsili.setDesc(SerajMessageUtil.getMessage("common_undefined"));
				entity.setHesabTafsili(nullHesabTafsili);
			}
			
//			String tafsiliCode = (String) a[1];
			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);
			
			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);
			
			if(tarazTafsiliAzmayeshiMandeh.get(hesabTafsiliId)!=null){
				entity.setMandehBedehkarEbtedayDore(tarazTafsiliAzmayeshiMandeh.get(hesabTafsiliId).getMandehBedehkar());
				entity.setMandehBestankarEbtedayDore(tarazTafsiliAzmayeshiMandeh.get(hesabTafsiliId).getMandehBestankar());
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore()+entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore()+entity.getBestankar());
			}else{
				entity.setMandehBedehkarEbtedayDore(0d);
				entity.setMandehBestankarEbtedayDore(0d);
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore()+entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore()+entity.getBestankar());
			}
			
			if(entity.getBedehkar() - entity.getBestankar() > 0)
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
			else if(entity.getBestankar()-entity.getBedehkar() > 0)
				entity.setMandehBestankar(entity.getBestankar()-entity.getBedehkar());
			tarazTafsiliAzmayeshiList.add(entity);
		}
		return tarazTafsiliAzmayeshiList;
	}
	
	public List<SanadHesabdariItemEntity> getTarazAccountingMarkazAzmayeshi(SaalMaaliEntity saalMaaliEntity, Date fromDate, Date toDate,List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds, List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, OrganEntity organEntity) {
		List<SanadHesabdariItemEntity> tarazAccountingMarkazAzmayeshiList = new ArrayList<SanadHesabdariItemEntity>();
		Map<String, Object> filter = populateTarazFilter(saalMaaliEntity,
				fromDate, toDate, hesabKolIds, moeenIds, tafsiliIds,
				articleTafsiliIds,accountingMarkazIds, hesabType, organEntity);
		
		
		List<Object[]> rawList = getMyDAO().getTarazAccountingMarkazAzmayeshi(filter);
		Map<Long, SanadHesabdariItemEntity> tarazAccountingMarkazAzmayeshiMandeh = getTarazAccountingMarkazAzmayeshiMandeh(saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organEntity);
		
		for (Object[] object : rawList) {
			SanadHesabdariItemEntity entity = new SanadHesabdariItemEntity();
			Object[] a = (Object[])object;
			Long accoutingMarkazId = -1l;
			if(a[0]!=null){
				accoutingMarkazId = new Long(a[0].toString());
				AccountingMarkazEntity accoutingMarkazEntity = getAccountingMarkazService().load(accoutingMarkazId);
				entity.setAccountingMarkaz(accoutingMarkazEntity);
			}else{
				AccountingMarkazEntity nullAccountingMarkaz = new AccountingMarkazEntity();
				nullAccountingMarkaz.setDesc(SerajMessageUtil.getMessage("common_undefined"));
				entity.setAccountingMarkaz(nullAccountingMarkaz);
			}
			
//			String accountingMarkazCode = (String) a[1];
			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);
			
			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);
			
			if(tarazAccountingMarkazAzmayeshiMandeh.get(accoutingMarkazId)!=null){
				entity.setMandehBedehkarEbtedayDore(tarazAccountingMarkazAzmayeshiMandeh.get(accoutingMarkazId).getMandehBedehkar());
				entity.setMandehBestankarEbtedayDore(tarazAccountingMarkazAzmayeshiMandeh.get(accoutingMarkazId).getMandehBestankar());
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore()+entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore()+entity.getBestankar());
			}else{
				entity.setMandehBedehkarEbtedayDore(0d);
				entity.setMandehBestankarEbtedayDore(0d);
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore()+entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore()+entity.getBestankar());
			}
			
			if(entity.getBedehkar() - entity.getBestankar() > 0)
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
			else if(entity.getBestankar()-entity.getBedehkar() > 0)
				entity.setMandehBestankar(entity.getBestankar()-entity.getBedehkar());
			tarazAccountingMarkazAzmayeshiList.add(entity);
		}
		return tarazAccountingMarkazAzmayeshiList;
	}
	public List<SanadHesabdariItemEntity> getTarazAccountingMarkazShenavarAzmayeshi(SaalMaaliEntity saalMaaliEntity, Date fromDate, Date toDate,List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds, List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, OrganEntity organEntity) {
		List<SanadHesabdariItemEntity> tarazAccountingMarkazAzmayeshiList = new ArrayList<SanadHesabdariItemEntity>();
		Map<String, Object> filter = populateTarazFilter(saalMaaliEntity,
				fromDate, toDate, hesabKolIds, moeenIds, tafsiliIds,
				articleTafsiliIds,accountingMarkazIds, hesabType, organEntity);
		
		filter.put("articleTafsili.level@eq", 2);
		List<Object[]> rawList = getMyDAO().getTarazAccountingMarkazShenavarAzmayeshi(filter);
		Map<Long, SanadHesabdariItemEntity> tarazAccountingMarkazAzmayeshiMandeh = getTarazAccountingMarkazAzmayeshiMandeh(saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organEntity);
		
		for (Object[] object : rawList) {
			SanadHesabdariItemEntity entity = new SanadHesabdariItemEntity();
			Object[] a = (Object[])object;
			Long accoutingMarkazId = -1l;
			if(a[0]!=null){ 
				accoutingMarkazId = new Long(a[0].toString());
				AccountingMarkazEntity accoutingMarkazEntity = getAccountingMarkazService().load(accoutingMarkazId);
				entity.setAccountingMarkaz(accoutingMarkazEntity);
			}else{
				AccountingMarkazEntity nullAccountingMarkaz = new AccountingMarkazEntity();
				nullAccountingMarkaz.setDesc(SerajMessageUtil.getMessage("common_undefined"));
				entity.setAccountingMarkaz(nullAccountingMarkaz);
			}
			
//			String accountingMarkazCode = (String) a[1];
			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);
			
			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);
			
			if(tarazAccountingMarkazAzmayeshiMandeh.get(accoutingMarkazId)!=null){
				entity.setMandehBedehkarEbtedayDore(tarazAccountingMarkazAzmayeshiMandeh.get(accoutingMarkazId).getMandehBedehkar());
				entity.setMandehBestankarEbtedayDore(tarazAccountingMarkazAzmayeshiMandeh.get(accoutingMarkazId).getMandehBestankar());
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore()+entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore()+entity.getBestankar());
			}else{
				entity.setMandehBedehkarEbtedayDore(0d);
				entity.setMandehBestankarEbtedayDore(0d);
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore()+entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore()+entity.getBestankar());
			}
			
			if(entity.getBedehkar() - entity.getBestankar() > 0)
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
			else if(entity.getBestankar()-entity.getBedehkar() > 0)
				entity.setMandehBestankar(entity.getBestankar()-entity.getBedehkar());
			tarazAccountingMarkazAzmayeshiList.add(entity);
		}
		return tarazAccountingMarkazAzmayeshiList;
	}

	private Map<String, Object> populateTarazFilter(
			SaalMaaliEntity saalMaaliEntity, Date fromDate, Date toDate,
			List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds,
			List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType,
			OrganEntity organEntity) {
		Map<String, Object> localFilter =  new HashMap<String, Object>();

		localFilter.put("sanadHesabdari.state@eqORsanadHesabdari.state@eq",Arrays.asList(SanadStateEnum.DAEM,SanadStateEnum.BARRESI_SHODE));
		localFilter.put("sanadHesabdari.sanadFunction@neq@sanadHesabdari.sanadFunction@neq",Arrays.asList(SanadFunctionEnum.EFTETAHIE,SanadFunctionEnum.EKHTETAMIE));

//		List<SanadStateEnum> sanadStates = new ArrayList<SanadStateEnum>();
//		sanadStates.add(SanadStateEnum.DAEM);
//		sanadStates.add(SanadStateEnum.BARRESI_SHODE);
//		localFilter.put("sanadHesabdari.state@eqORsanadHesabdari.state@eq", sanadStates);
		
		if(organEntity!=null)
			localFilter.put("sanadHesabdari.organ.id@eq", organEntity.getId());
		
		localFilter.put("hesabKol.hesabGroup.type@eq", hesabType);
		localFilter.put("hesabMoeen.id@in", moeenIds);
		localFilter.put("hesabKol.id@in", hesabKolIds);
		if(tafsiliIds!=null && !tafsiliIds.isEmpty())
			localFilter.put("hesabTafsili.id@in", tafsiliIds);
		
		if(articleTafsiliIds!=null && !articleTafsiliIds.isEmpty())
			localFilter.put("articleTafsili.hesabTafsili.id@in",articleTafsiliIds);
		
		if(accountingMarkazIds!=null && !accountingMarkazIds.isEmpty())
			localFilter.put("accountingMarkaz.id@in", accountingMarkazIds);
		
		if(saalMaaliEntity!=null && saalMaaliEntity.getId() != null){
			localFilter.put("sanadHesabdari.saalMaali.id@eq", saalMaaliEntity.getId());
			if(fromDate==null)
				localFilter.put("sanadHesabdari.tarikhSanad@ge",getSaalMaaliService().getFirstDateOfSaalMaali(saalMaaliEntity));
			else
				localFilter.put("sanadHesabdari.tarikhSanad@ge",fromDate);
			if(toDate==null)
				localFilter.put("sanadHesabdari.tarikhSanad@le",getSaalMaaliService().getLastDateOfSaalMaali(saalMaaliEntity));
			else
				localFilter.put("sanadHesabdari.tarikhSanad@le",toDate);
		}else{
			localFilter.put("sanadHesabdari.tarikhSanad@ge",fromDate);
			localFilter.put("sanadHesabdari.tarikhSanad@le",toDate);
//			throw new NoSaalMaaliFoundException();
		}
		return localFilter;
	}
	private Map<String, Object> populateMandehTarazFilter(
			SaalMaaliEntity saalMaaliEntity, Date fromDate,
			List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds,
			List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType,
			OrganEntity organEntity) {
		Map<String, Object> localFilter =  new HashMap<String, Object>();

		localFilter.put("sanadHesabdari.state@eqORsanadHesabdari.state@eq", Arrays.asList(SanadStateEnum.DAEM,SanadStateEnum.BARRESI_SHODE));
		localFilter.put("sanadHesabdari.sanadFunction@neq@sanadHesabdari.sanadFunction@neq",Arrays.asList(SanadFunctionEnum.EFTETAHIE,SanadFunctionEnum.EKHTETAMIE));

		if(organEntity!=null)
			localFilter.put("sanadHesabdari.organ.id@eq", organEntity.getId());
		
		localFilter.put("hesabKol.hesabGroup.type@eq", hesabType);
		localFilter.put("hesabMoeen.id@in", moeenIds);
		localFilter.put("hesabKol.id@in", hesabKolIds);
		if(tafsiliIds!=null && !tafsiliIds.isEmpty())
			localFilter.put("hesabTafsili.id@in", tafsiliIds);
		
		if(articleTafsiliIds!=null && !articleTafsiliIds.isEmpty())
			localFilter.put("articleTafsili.hesabTafsili.id@in",articleTafsiliIds);
		
		if(accountingMarkazIds!=null && !accountingMarkazIds.isEmpty())
			localFilter.put("accountingMarkaz.id@in", accountingMarkazIds);
		
		if(saalMaaliEntity!=null && saalMaaliEntity.getId() != null){
			localFilter.put("sanadHesabdari.saalMaali.id@eq", saalMaaliEntity.getId());
			if(fromDate!=null)
				localFilter.put("sanadHesabdari.tarikhSanad@le",fromDate);
			else
				localFilter.put("sanadHesabdari.tarikhSanad@le",getSaalMaaliService().getFirstDateOfSaalMaali(saalMaaliEntity));
			localFilter.put("sanadHesabdari.tarikhSanad@ge",getSaalMaaliService().getFirstDateOfSaalMaali(saalMaaliEntity));
		}else{
			if(fromDate!=null)
				localFilter.put("sanadHesabdari.tarikhSanad@le",fromDate);
		}
		
		return localFilter;
	}
	
	public List<SanadHesabdariItemEntity> getIncomeGroupByKol(Map<String, Object> filter){
		return getItemsGroupByKol(filter, HesabTypeEnum.INCOME,null, null, null);
	}	
	public List<SanadHesabdariItemEntity> getExpenseGroupByKol(Map<String, Object> filter){
		return getItemsGroupByKol(filter, HesabTypeEnum.EXPENSE,null, null, null);
	}
	public List<SanadHesabdariItemEntity> getItemsGroupByKol(Map<String, Object> filter, HesabTypeEnum hesabTypeEnum, Long hesabKolId, Date from, Date to){
		Map<String, Object> hesabKolfilter = new HashMap<String, Object>();
		hesabKolfilter.put("id@eq", hesabKolId);
		hesabKolfilter.put("hesabGroup.type@eq", hesabTypeEnum);
		List<SanadHesabdariItemEntity> items = new ArrayList<SanadHesabdariItemEntity>();
		List<HesabKolEntity> kolList = hesabKolService.getDataList(null, hesabKolfilter);
		for (HesabKolEntity kol : kolList) {
			Double[] aggregate = getMyDAO().getHesabAggregate(kol.getId(), hesabTypeEnum, from, to, filter);
			SanadHesabdariItemEntity item = new SanadHesabdariItemEntity();
			item.setHesabKol(kol);
			item.setBedehkar(aggregate[0]);
			item.setBestankar(aggregate[1]);
			items.add(item);
		}
		return items;
	}

	@Override
	public void duplicateEntityWithoutId(SanadHesabdariItemEntity destEntity,
			SanadHesabdariItemEntity srcEntity) {
		super.duplicateEntityWithoutId(destEntity, srcEntity);
		
		Set<ArticleTafsiliEntity> articleTafsili = srcEntity.getArticleTafsili();
		if(articleTafsili!=null){
			destEntity.getArticleTafsili().clear();
			for (ArticleTafsiliEntity articleTafsiliEntity : articleTafsili) {
				ArticleTafsiliEntity destArticleTafsiliEntity = new ArticleTafsiliEntity();
				destArticleTafsiliEntity.setHesabTafsili(articleTafsiliEntity.getHesabTafsili());
				destArticleTafsiliEntity.setLevel(articleTafsiliEntity.getLevel());
				destArticleTafsiliEntity.setSanadHesabdariItem(destEntity);
				destArticleTafsiliEntity.setDesc(articleTafsiliEntity.getHesabTafsili().getDesc());
				//getArticleTafsiliService().duplicateEntityWithoutId(destArticleTafsiliEntity, articleTafsiliEntity);
				destEntity.getArticleTafsili().add(destArticleTafsiliEntity);
			}
		}
		
		Set<ArticleAccountingMarkazEntity> articleAccountingMarkaz = srcEntity.getArticleAccountingMarkaz();
		if(articleAccountingMarkaz !=null){
			destEntity.getArticleAccountingMarkaz().clear();
			for (ArticleAccountingMarkazEntity articleAccountingMarkazEntity : articleAccountingMarkaz) {
				ArticleAccountingMarkazEntity destArticleAccountingMarkazEntity = new ArticleAccountingMarkazEntity();;
				destArticleAccountingMarkazEntity.setAccountingMarkaz(articleAccountingMarkazEntity.getAccountingMarkaz());
				destArticleAccountingMarkazEntity.setLevel(articleAccountingMarkazEntity.getLevel());
				destArticleAccountingMarkazEntity.setSanadHesabdariItem(destEntity);
				destArticleAccountingMarkazEntity.setDesc(articleAccountingMarkazEntity.getAccountingMarkaz().getDesc());
				//getArticleAccountingMarkazService().duplicateEntityWithoutId(destArticleAccountingMarkazEntity, articleAccountingMarkazEntity);
				destEntity.getArticleAccountingMarkaz().add(destArticleAccountingMarkazEntity);
			}
		}
	}
}