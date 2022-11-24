package ir.serajsamaneh.accounting.sanadhesabdariitem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity;
import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazService;
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
import ir.serajsamaneh.core.util.SerajMessageUtil;
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SanadHesabdariItemService extends BaseEntityService<SanadHesabdariItemEntity, Long> {

	@Override
	protected SanadHesabdariItemDAO getMyDAO() {
		return sanadHesabdariItemDAO;
	}

	@Autowired
	SanadHesabdariItemDAO sanadHesabdariItemDAO;
	@Autowired
	HesabKolService hesabKolService;
	@Autowired
	HesabMoeenService hesabMoeenService;
	@Autowired
	HesabTafsiliService hesabTafsiliService;
	@Autowired
	SaalMaaliService saalMaaliService;
	@Autowired
	SanadHesabdariService sanadHesabdariService;
	@Autowired
	AccountingMarkazService accountingMarkazService;


	@Transactional(readOnly = true)
	public List<SanadHesabdariItemVO> getTarazKolAzmayeshi(SaalMaaliEntity saalMaaliEntity, Date fromDate, Date toDate,
			List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds, List<Long> articleTafsiliIds,
			List<Long> accountingMarkazIds, HesabTypeEnum hesabType, Long organId, Long fromSerial, Long toSerial,
			Map<String, Object> sanadhesabdariItemFilter, int numberOfDecimals) {
		return getTarazKolAzmayeshi(saalMaaliEntity, fromDate, toDate, hesabKolIds, moeenIds, tafsiliIds,
				articleTafsiliIds, accountingMarkazIds, hesabType, organId, fromSerial, toSerial,
				sanadhesabdariItemFilter, true, true, numberOfDecimals);
	}

	@Transactional(readOnly = true)
	public List<SanadHesabdariItemVO> getTarazKolAzmayeshi(SaalMaaliEntity saalMaaliEntity, Date fromDate, Date toDate,
			List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds, List<Long> articleTafsiliIds,
			List<Long> accountingMarkazIds, HesabTypeEnum hesabType, Long organId, Long fromSerial, Long toSerial,
			Map<String, Object> sanadhesabdariItemFilter, boolean displayZeroMandehInTaraz,
			boolean beforeClosingAccounts, int numberOfDecimals) {
		List<SanadHesabdariItemVO> tarazKolAzmayeshiList = new ArrayList<SanadHesabdariItemVO>();
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter = populateTarazFilter(saalMaaliEntity, fromDate, toDate, hesabKolIds, moeenIds, tafsiliIds,
				articleTafsiliIds, accountingMarkazIds, hesabType, organId, fromSerial, toSerial,
				sanadhesabdariItemFilter, beforeClosingAccounts);

		List<Object[]> rawList = getMyDAO().getTarazKolAzmayeshi(localFilter);
		Map<Long, SanadHesabdariItemVO> tarazKolAzmayeshiMandeh = getTarazKolAzmayeshiMandeh(saalMaaliEntity, fromDate,
				hesabKolIds, moeenIds, tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organId,
				beforeClosingAccounts, numberOfDecimals);

		for (int i = 0; i < rawList.size(); i++) {
			Object[] object = rawList.get(i);
			SanadHesabdariItemVO entity = new SanadHesabdariItemVO();
			Object[] a = (Object[]) object;
			Long hesabKolId = Long.valueOf(a[0].toString());
			HesabKolEntity hesabKolEntity = hesabKolService.load(hesabKolId);
			entity.setHesabKolDesc(hesabKolEntity.getDesc());
			entity.setHesabKolName(hesabKolEntity.getName());
			entity.setHesabKolCode(hesabKolEntity.getCode());
			entity.setHesabKolID(hesabKolEntity.getId().toString());
			entity.setHesabKolMahyat(hesabKolEntity.getMahyatKol());

			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);

			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);
			if (tarazKolAzmayeshiMandeh.get(hesabKolId) != null) {
				entity.setMandehBedehkarEbtedayDore(tarazKolAzmayeshiMandeh.get(hesabKolId).getMandehBedehkar());
				entity.setMandehBestankarEbtedayDore(tarazKolAzmayeshiMandeh.get(hesabKolId).getMandehBestankar());
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore() + entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore() + entity.getBestankar());
			} else {
				entity.setMandehBedehkarEbtedayDore(0d);
				entity.setMandehBestankarEbtedayDore(0d);
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore() + entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore() + entity.getBestankar());
			}

			if (entity.getBedehkar() - entity.getBestankar() > 0) {
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
				if (entity.getHesabKolMahyat().equals(MahyatKolEnum.Bedehkar)) {
					entity.setMandehByMahiyatHesabStr(new DecimalFormat().format(entity.getMandehBedehkar()));
					entity.setMandehByMahiyatHesabDbl(entity.getMandehBedehkar());
				} else if (entity.getHesabKolMahyat().equals(MahyatKolEnum.Bestankar)) {
					entity.setMandehByMahiyatHesabStr(
							"(" + new DecimalFormat().format(entity.getMandehBedehkar()) + ")");
					entity.setMandehByMahiyatHesabDbl(entity.getMandehBedehkar() * -1);
				}
			} else if (entity.getBestankar() - entity.getBedehkar() > 0) {
				entity.setMandehBestankar(entity.getBestankar() - entity.getBedehkar());

				if (entity.getHesabKolMahyat().equals(MahyatKolEnum.Bedehkar)) {
					entity.setMandehByMahiyatHesabStr(
							"(" + new DecimalFormat().format(entity.getMandehBestankar()) + ")");
					entity.setMandehByMahiyatHesabDbl(entity.getMandehBestankar() * -1);
				} else if (entity.getHesabKolMahyat().equals(MahyatKolEnum.Bestankar)) {
					entity.setMandehByMahiyatHesabStr(new DecimalFormat().format(entity.getMandehBestankar()));
					entity.setMandehByMahiyatHesabDbl(entity.getMandehBestankar());
				}

			}

			if (entity.getMandehBestankar() != 0 || entity.getMandehBedehkar() != 0)
				tarazKolAzmayeshiList.add(entity);
			else if (displayZeroMandehInTaraz)
				tarazKolAzmayeshiList.add(entity);
		}
		return tarazKolAzmayeshiList;
	}

	// this method is used to calculate the MANDEH EBTEDAYTE DORE
	@Transactional(readOnly = true)
	private Map<Long, SanadHesabdariItemVO> getTarazKolAzmayeshiMandeh(SaalMaaliEntity saalMaaliEntity, Date fromDate,
			List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds, List<Long> articleTafsiliIds,
			List<Long> accountingMarkazIds, HesabTypeEnum hesabType, Long organId, boolean beforeClosingAccounts,
			int numberOfDecimals) {
		Map<Long, SanadHesabdariItemVO> tarazKolAzmayeshiMap = new HashMap<Long, SanadHesabdariItemVO>();
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter = populateMandehTarazFilter(saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds,
				articleTafsiliIds, accountingMarkazIds, hesabType, organId, beforeClosingAccounts);

		List<Object[]> rawList = getMyDAO().getTarazKolAzmayeshi(localFilter);
		for (Object[] object : rawList) {
			SanadHesabdariItemVO entity = new SanadHesabdariItemVO();
			Object[] a = (Object[]) object;
			Long hesabKolId = Long.valueOf(a[0].toString());
			HesabKolEntity hesabKolEntity = hesabKolService.load(hesabKolId);
			entity.setHesabKolDesc(hesabKolEntity.getDesc());
			entity.setHesabKolName(hesabKolEntity.getName());
			entity.setHesabKolCode(hesabKolEntity.getCode());

			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);

			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);
			if (entity.getBedehkar() - entity.getBestankar() > 0)
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
			else if (entity.getBestankar() - entity.getBedehkar() > 0)
				entity.setMandehBestankar(entity.getBestankar() - entity.getBedehkar());
			tarazKolAzmayeshiMap.put(hesabKolId, entity);
		}

		if (organId == null)
			applySanadEftetahiehOnTarazKolAzmayeshiMandeh(tarazKolAzmayeshiMap, saalMaaliEntity, numberOfDecimals);
		else
			applySanadEftetahiehOnTarazKolAzmayeshiMandeh(tarazKolAzmayeshiMap, saalMaaliEntity, organId,
					numberOfDecimals);
		return tarazKolAzmayeshiMap;
	}

	@Transactional(readOnly = true)
	private void applySanadEftetahiehOnTarazKolAzmayeshiMandeh(Map<Long, SanadHesabdariItemVO> tarazKolAzmayeshiMap,
			SaalMaaliEntity saalMaaliEntity, int numberOfDecimals) {
		List<SanadHesabdariEntity> sanadEftetahiehha = sanadHesabdariService.getSanadEftetahiehha(saalMaaliEntity);
		for (SanadHesabdariEntity sanadEftetahieh : sanadEftetahiehha) {
			applySanadEftetahiehOnTarazKolAzmayeshiMandeh(tarazKolAzmayeshiMap, sanadEftetahieh, numberOfDecimals);
		}
	}

	@Transactional(readOnly = true)
	private void applySanadEftetahiehOnTarazKolAzmayeshiMandeh(Map<Long, SanadHesabdariItemVO> tarazKolAzmayeshiMap,
			SaalMaaliEntity saalMaaliEntity, Long organId, int numberOfDecimals) {
		SanadHesabdariEntity sanadEftetahieh = sanadHesabdariService.getSanadEftetahieh(saalMaaliEntity, organId);
		if (sanadEftetahieh == null || sanadEftetahieh.getSanadHesabdariItem() == null)
			return;
		applySanadEftetahiehOnTarazKolAzmayeshiMandeh(tarazKolAzmayeshiMap, sanadEftetahieh, numberOfDecimals);
	}

	@Transactional(readOnly = true)
	private void applySanadEftetahiehOnTarazKolAzmayeshiMandeh(Map<Long, SanadHesabdariItemVO> tarazKolAzmayeshiMap,
			SanadHesabdariEntity sanadEftetahieh, int numberOfDecimals) {
		List<SanadHesabdariItemEntity> sanadHesabdariItems = sanadEftetahieh.getSanadHesabdariItem();
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItems) {
			HesabKolEntity hesabKolEntity = sanadHesabdariItemEntity.getHesabKol();
			Long hesabKolId = hesabKolEntity.getId();
			SanadHesabdariItemVO itemEntity = tarazKolAzmayeshiMap.get(hesabKolId);
			if (itemEntity == null) {

				tarazKolAzmayeshiMap.put(hesabKolId,
						new SanadHesabdariItemVO(sanadHesabdariItemEntity, numberOfDecimals));
			} else {
				itemEntity.setBestankar(itemEntity.getBestankar() + sanadHesabdariItemEntity.getBestankar());
				itemEntity.setBedehkar(itemEntity.getBedehkar() + sanadHesabdariItemEntity.getBedehkar());
			}

		}
	}

	@Transactional(readOnly = true)
	private void applySanadEftetahiehOnTarazMoeenAzmayeshiMandeh(Map<Long, SanadHesabdariItemVO> tarazMoeenAzmayeshiMap,
			SaalMaaliEntity saalMaaliEntity, int numberOfDecimals) {
		List<SanadHesabdariEntity> sanadEftetahiehha = sanadHesabdariService.getSanadEftetahiehha(saalMaaliEntity);
		for (SanadHesabdariEntity sanadEftetahieh : sanadEftetahiehha) {
			applySanadEftetahiehOnTarazMoeenAzmayeshiMandeh(tarazMoeenAzmayeshiMap, sanadEftetahieh, numberOfDecimals);
		}
	}

	@Transactional(readOnly = true)
	private void applySanadEftetahiehOnTarazMoeenAzmayeshiMandeh(Map<Long, SanadHesabdariItemVO> tarazMoeenAzmayeshiMap,
			SaalMaaliEntity saalMaaliEntity, Long organId, int numberOfDecimals) {
		SanadHesabdariEntity sanadEftetahieh = sanadHesabdariService.getSanadEftetahieh(saalMaaliEntity, organId);
		if (sanadEftetahieh == null || sanadEftetahieh.getSanadHesabdariItem() == null)
			return;
		applySanadEftetahiehOnTarazMoeenAzmayeshiMandeh(tarazMoeenAzmayeshiMap, sanadEftetahieh, numberOfDecimals);
	}

	@Transactional(readOnly = true)
	private void applySanadEftetahiehOnTarazMoeenAzmayeshiMandeh(Map<Long, SanadHesabdariItemVO> tarazMoeenAzmayeshiMap,
			SanadHesabdariEntity sanadEftetahieh, int numberOfDecimals) {
		List<SanadHesabdariItemEntity> sanadHesabdariItems = sanadEftetahieh.getSanadHesabdariItem();
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItems) {
			HesabMoeenEntity hesabMoeenEntity = sanadHesabdariItemEntity.getHesabMoeen();
			Long hesabMoeenId = hesabMoeenEntity.getId();
			SanadHesabdariItemVO itemEntity = tarazMoeenAzmayeshiMap.get(hesabMoeenId);
			if (itemEntity == null) {
//				sanadHesabdariItemEntity.setMandehBedehkar(0d);
//				sanadHesabdariItemEntity.setMandehBestankar(0d);
				tarazMoeenAzmayeshiMap.put(hesabMoeenId,
						new SanadHesabdariItemVO(sanadHesabdariItemEntity, numberOfDecimals));
			} else {
				itemEntity.setBestankar(itemEntity.getBestankar() + sanadHesabdariItemEntity.getBestankar());
				itemEntity.setBedehkar(itemEntity.getBedehkar() + sanadHesabdariItemEntity.getBedehkar());
			}

		}
	}

	@Transactional(readOnly = true)
	private void applySanadEftetahiehOnTarazTafsiliAzmayeshiMandeh(
			Map<Long, SanadHesabdariItemVO> tarazTafsiliAzmayeshiMap, SaalMaaliEntity saalMaaliEntity,
			int numberOfDecimals) {
		List<SanadHesabdariEntity> sanadEftetahiehha = sanadHesabdariService.getSanadEftetahiehha(saalMaaliEntity);
		for (SanadHesabdariEntity sanadEftetahieh : sanadEftetahiehha) {
			applySanadEftetahiehOnTarazTafsiliAzmayeshiMandeh(tarazTafsiliAzmayeshiMap, sanadEftetahieh,
					numberOfDecimals);
		}
	}

	@Transactional(readOnly = true)
	private void applySanadEftetahiehOnTarazTafsiliAzmayeshiMandeh(
			Map<Long, SanadHesabdariItemVO> tarazTafsiliAzmayeshiMap, SaalMaaliEntity saalMaaliEntity, Long organId,
			int numberOfDecimals) {
		SanadHesabdariEntity sanadEftetahieh = sanadHesabdariService.getSanadEftetahieh(saalMaaliEntity, organId);
		if (sanadEftetahieh == null || sanadEftetahieh.getSanadHesabdariItem() == null)
			return;
		applySanadEftetahiehOnTarazTafsiliAzmayeshiMandeh(tarazTafsiliAzmayeshiMap, sanadEftetahieh, numberOfDecimals);
	}

	@Transactional(readOnly = true)
	private void applySanadEftetahiehOnTarazTafsiliAzmayeshiMandeh(
			Map<Long, SanadHesabdariItemVO> tarazTafsiliAzmayeshiMap, SanadHesabdariEntity sanadEftetahieh,
			int numberOfDecimals) {
		List<SanadHesabdariItemEntity> sanadHesabdariItems = sanadEftetahieh.getSanadHesabdariItem();
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItems) {
			HesabTafsiliEntity hesabTafsiliEntity = sanadHesabdariItemEntity.getHesabTafsili();
			if (hesabTafsiliEntity == null || hesabTafsiliEntity.getId() == null)
				continue;
			Long hesabTafsiliId = hesabTafsiliEntity.getId();
			SanadHesabdariItemVO itemEntity = tarazTafsiliAzmayeshiMap.get(hesabTafsiliId);
			if (itemEntity == null) {
//				sanadHesabdariItemEntity.setMandehBedehkar(0d);
//				sanadHesabdariItemEntity.setMandehBestankar(0d);

				tarazTafsiliAzmayeshiMap.put(hesabTafsiliId,
						new SanadHesabdariItemVO(sanadHesabdariItemEntity, numberOfDecimals));
			} else {
				itemEntity.setBestankar(itemEntity.getBestankar() + sanadHesabdariItemEntity.getBestankar());
				itemEntity.setBedehkar(itemEntity.getBedehkar() + sanadHesabdariItemEntity.getBedehkar());
			}

		}
	}

	@Transactional(readOnly = true)
	private void applySanadEftetahiehOnTarazMarkazAzmayeshiMandeh(
			Map<Long, SanadHesabdariItemVO> tarazMarkazAzmayeshiMap, SaalMaaliEntity saalMaaliEntity,
			int numberOfDecimals) {
		List<SanadHesabdariEntity> sanadEftetahiehha = sanadHesabdariService.getSanadEftetahiehha(saalMaaliEntity);
		for (SanadHesabdariEntity sanadEftetahieh : sanadEftetahiehha) {
			applySanadEftetahiehOnTarazMarkazAzmayeshiMandeh(tarazMarkazAzmayeshiMap, sanadEftetahieh,
					numberOfDecimals);
		}
	}

	@Transactional(readOnly = true)
	private void applySanadEftetahiehOnTarazMarkazAzmayeshiMandeh(
			Map<Long, SanadHesabdariItemVO> tarazMarkazAzmayeshiMap, SaalMaaliEntity saalMaaliEntity, Long organId,
			int numberOfDecimals) {
		SanadHesabdariEntity sanadEftetahieh = sanadHesabdariService.getSanadEftetahieh(saalMaaliEntity, organId);
		if (sanadEftetahieh == null || sanadEftetahieh.getSanadHesabdariItem() == null)
			return;
		applySanadEftetahiehOnTarazMarkazAzmayeshiMandeh(tarazMarkazAzmayeshiMap, sanadEftetahieh, numberOfDecimals);
	}

	@Transactional(readOnly = true)
	private void applySanadEftetahiehOnTarazMarkazAzmayeshiMandeh(
			Map<Long, SanadHesabdariItemVO> tarazMarkazAzmayeshiMap, SanadHesabdariEntity sanadEftetahieh,
			int numberOfDecimals) {
		List<SanadHesabdariItemEntity> sanadHesabdariItems = sanadEftetahieh.getSanadHesabdariItem();
		for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItems) {
			AccountingMarkazEntity accountingMarkazEntity = sanadHesabdariItemEntity.getAccountingMarkaz();

			if (accountingMarkazEntity == null || accountingMarkazEntity.getId() == null)
				continue;

			Long accountingMarkazId = accountingMarkazEntity.getId();
			SanadHesabdariItemVO itemEntity = tarazMarkazAzmayeshiMap.get(accountingMarkazId);
			if (itemEntity == null)
				tarazMarkazAzmayeshiMap.put(accountingMarkazId,
						new SanadHesabdariItemVO(sanadHesabdariItemEntity, numberOfDecimals));
			else {
				itemEntity.setBestankar(itemEntity.getBestankar() + sanadHesabdariItemEntity.getBestankar());
				itemEntity.setBedehkar(itemEntity.getBedehkar() + sanadHesabdariItemEntity.getBedehkar());
			}

		}
	}

	@Transactional(readOnly = true)
	private Map<Long, SanadHesabdariItemVO> getTarazMoeenAzmayeshiMandeh(SaalMaaliEntity saalMaaliEntity, Date fromDate,
			List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds, List<Long> articleTafsiliIds,
			List<Long> accountingMarkazIds, HesabTypeEnum hesabType, Long organId, boolean beforeClosingAccounts,
			int numberOfDecimals) {
		Map<Long, SanadHesabdariItemVO> tarazMoeenAzmayeshiMap = new HashMap<Long, SanadHesabdariItemVO>();
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter = populateMandehTarazFilter(saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds,
				articleTafsiliIds, accountingMarkazIds, hesabType, organId, beforeClosingAccounts);

		List<Object[]> rawList = getMyDAO().getTarazMoeenAzmayeshi(localFilter);
		for (Object[] a : rawList) {
			SanadHesabdariItemVO entity = new SanadHesabdariItemVO();
			Long hesabMoeenId = Long.valueOf(a[0].toString());
			HesabMoeenEntity hesabMoeenEntity = hesabMoeenService.load(hesabMoeenId);
			entity.setHesabMoeenDesc(hesabMoeenEntity.getDesc());
			entity.setHesabMoeenCode(hesabMoeenEntity.getCode());
			entity.setHesabMoeenName(hesabMoeenEntity.getName());

			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);

			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);
			if (entity.getBedehkar() - entity.getBestankar() > 0)
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
			else if (entity.getBestankar() - entity.getBedehkar() > 0)
				entity.setMandehBestankar(entity.getBestankar() - entity.getBedehkar());
			tarazMoeenAzmayeshiMap.put(hesabMoeenId, entity);
		}

		if (organId == null)
			applySanadEftetahiehOnTarazMoeenAzmayeshiMandeh(tarazMoeenAzmayeshiMap, saalMaaliEntity, numberOfDecimals);
		else
			applySanadEftetahiehOnTarazMoeenAzmayeshiMandeh(tarazMoeenAzmayeshiMap, saalMaaliEntity, organId,
					numberOfDecimals);

		return tarazMoeenAzmayeshiMap;
	}

	@Transactional(readOnly = true)
	private Map<Long, SanadHesabdariItemVO> getTarazTafsiliAzmayeshiMandeh(SaalMaaliEntity saalMaaliEntity,
			Date fromDate, List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds,
			List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, Long organId,
			boolean beforeClosingAccounts, int numberOfDecimals) {
		Map<Long, SanadHesabdariItemVO> tarazTafsiliAzmayeshiMap = new HashMap<Long, SanadHesabdariItemVO>();
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter = populateMandehTarazFilter(saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds,
				articleTafsiliIds, accountingMarkazIds, hesabType, organId, beforeClosingAccounts);

		List<Object[]> rawList = getMyDAO().getTarazTafsiliAzmayeshi(localFilter);
		for (Object[] object : rawList) {
			SanadHesabdariItemVO entity = new SanadHesabdariItemVO();
			Object[] a = (Object[]) object;

			Long hesabTafsiliId = -1l;
			if (a[0] != null) {
				hesabTafsiliId = Long.valueOf(a[0].toString());
				HesabTafsiliEntity hesabTafsiliEntity = hesabTafsiliService.load(hesabTafsiliId);
				entity.setHesabTafsiliDesc(hesabTafsiliEntity.getDesc());
				entity.setHesabTafsiliOneName(hesabTafsiliEntity.getName());
				entity.setHesabTafsiliCode(hesabTafsiliEntity.getCode().toString());
				entity.setHesabTafsiliOneID(hesabTafsiliEntity.getId().toString());
			} else {
				HesabTafsiliEntity nullHesabTafsili = new HesabTafsiliEntity();
				nullHesabTafsili.setName(SerajMessageUtil.getMessage("common_undefined"));
				entity.setHesabTafsiliOneName(nullHesabTafsili.getName());
			}

			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);

			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);
			if (entity.getBedehkar() - entity.getBestankar() > 0)
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
			else if (entity.getBestankar() - entity.getBedehkar() > 0)
				entity.setMandehBestankar(entity.getBestankar() - entity.getBedehkar());
			tarazTafsiliAzmayeshiMap.put(hesabTafsiliId, entity);
		}

		if (organId == null)
			applySanadEftetahiehOnTarazTafsiliAzmayeshiMandeh(tarazTafsiliAzmayeshiMap, saalMaaliEntity,
					numberOfDecimals);
		else
			applySanadEftetahiehOnTarazTafsiliAzmayeshiMandeh(tarazTafsiliAzmayeshiMap, saalMaaliEntity, organId,
					numberOfDecimals);

		return tarazTafsiliAzmayeshiMap;
	}

	@Transactional(readOnly = true)
	private Map<Long, SanadHesabdariItemVO> getTarazTafsiliAzmayeshiTwoMandeh(SaalMaaliEntity saalMaaliEntity,
			Date fromDate, List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds,
			List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, Long organId,
			boolean beforeClosingAccounts, int numberOfDecimals) {
		Map<Long, SanadHesabdariItemVO> tarazTafsiliAzmayeshiMap = new HashMap<Long, SanadHesabdariItemVO>();
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter = populateMandehTarazFilter(saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds,
				articleTafsiliIds, accountingMarkazIds, hesabType, organId, beforeClosingAccounts);

		List<Object[]> rawList = getMyDAO().getTarazTafsiliAzmayeshiTwo(localFilter);
		for (Object[] object : rawList) {
			SanadHesabdariItemVO entity = new SanadHesabdariItemVO();
			Object[] a = (Object[]) object;

			Long hesabTafsiliId = -1l;
			if (a[0] != null) {
				hesabTafsiliId = Long.valueOf(a[0].toString());
				HesabTafsiliEntity hesabTafsiliEntity = hesabTafsiliService.load(hesabTafsiliId);
				entity.setHesabTafsiliDesc(hesabTafsiliEntity.getDesc());
				entity.setHesabTafsiliTwoName(hesabTafsiliEntity.getName());
				entity.setHesabTafsiliCode(hesabTafsiliEntity.getCode().toString());
				entity.setHesabTafsiliTwoID(hesabTafsiliEntity.getId().toString());
			} else {
				HesabTafsiliEntity nullHesabTafsili = new HesabTafsiliEntity();
				nullHesabTafsili.setName(SerajMessageUtil.getMessage("common_undefined"));
				entity.setHesabTafsiliTwoName(nullHesabTafsili.getName());
			}

			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);

			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);
			if (entity.getBedehkar() - entity.getBestankar() > 0)
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
			else if (entity.getBestankar() - entity.getBedehkar() > 0)
				entity.setMandehBestankar(entity.getBestankar() - entity.getBedehkar());
			tarazTafsiliAzmayeshiMap.put(hesabTafsiliId, entity);
		}

		if (organId == null)
			applySanadEftetahiehOnTarazTafsiliAzmayeshiMandeh(tarazTafsiliAzmayeshiMap, saalMaaliEntity,
					numberOfDecimals);
		else
			applySanadEftetahiehOnTarazTafsiliAzmayeshiMandeh(tarazTafsiliAzmayeshiMap, saalMaaliEntity, organId,
					numberOfDecimals);

		return tarazTafsiliAzmayeshiMap;
	}

	@Transactional(readOnly = true)
	private Map<Long, SanadHesabdariItemVO> getTarazTafsiliShenavarAzmayeshiMandeh(SaalMaaliEntity saalMaaliEntity,
			Date fromDate, List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds,
			List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, Long organId,
			Integer level, boolean beforeClosingAccounts, int numberOfDecimals) {
		Map<Long, SanadHesabdariItemVO> tarazTafsiliAzmayeshiMap = new HashMap<Long, SanadHesabdariItemVO>();
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter = populateMandehTarazFilter(saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds,
				articleTafsiliIds, accountingMarkazIds, hesabType, organId, beforeClosingAccounts);

		localFilter.put("articleTafsili.level@eq", level);
		List<Object[]> rawList = getMyDAO().getTarazTafsiliAzmayeshi(localFilter);
		for (Object[] object : rawList) {
			SanadHesabdariItemVO entity = new SanadHesabdariItemVO();
			Object[] a = (Object[]) object;

			Long hesabTafsiliId = -1l;
			if (a[0] != null) {
				hesabTafsiliId = Long.valueOf(a[0].toString());
				HesabTafsiliEntity hesabTafsiliEntity = hesabTafsiliService.load(hesabTafsiliId);
				entity.setHesabTafsiliDesc(hesabTafsiliEntity.getDesc());
				entity.setHesabTafsiliOneName(hesabTafsiliEntity.getName());
				entity.setHesabTafsiliCode(hesabTafsiliEntity.getCode().toString());
			} else {
				HesabTafsiliEntity nullHesabTafsili = new HesabTafsiliEntity();
				nullHesabTafsili.setName(SerajMessageUtil.getMessage("common_undefined"));
				entity.setHesabTafsiliOneName(nullHesabTafsili.getName());
			}

			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);

			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);
			if (entity.getBedehkar() - entity.getBestankar() > 0)
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
			else if (entity.getBestankar() - entity.getBedehkar() > 0)
				entity.setMandehBestankar(entity.getBestankar() - entity.getBedehkar());
			tarazTafsiliAzmayeshiMap.put(hesabTafsiliId, entity);
		}

		if (organId == null)
			applySanadEftetahiehOnTarazTafsiliAzmayeshiMandeh(tarazTafsiliAzmayeshiMap, saalMaaliEntity,
					numberOfDecimals);
		else
			applySanadEftetahiehOnTarazTafsiliAzmayeshiMandeh(tarazTafsiliAzmayeshiMap, saalMaaliEntity, organId,
					numberOfDecimals);

		return tarazTafsiliAzmayeshiMap;
	}

	@Transactional(readOnly = true)
	private Map<Long, SanadHesabdariItemVO> getTarazAccountingMarkazAzmayeshiMandeh(SaalMaaliEntity saalMaaliEntity,
			Date fromDate, List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds,
			List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, Long organId,
			boolean beforeClosingAccounts, int numberOfDecimals) {
		Map<Long, SanadHesabdariItemVO> tarazAccountingMarkazAzmayeshiMap = new HashMap<Long, SanadHesabdariItemVO>();
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter = populateMandehTarazFilter(saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds,
				articleTafsiliIds, accountingMarkazIds, hesabType, organId, beforeClosingAccounts);

//		localFilter.put("articleTafsili.level@eq", 2);
		List<Object[]> rawList = getMyDAO().getTarazAccountingMarkazAzmayeshi(localFilter);
		for (Object[] object : rawList) {
			SanadHesabdariItemVO entity = new SanadHesabdariItemVO();
			Object[] a = (Object[]) object;

			Long accountingMarkazId = -1l;
			if (a[0] != null) {
				accountingMarkazId = Long.valueOf(a[0].toString());
				AccountingMarkazEntity accountingMarkazEntity = accountingMarkazService.load(accountingMarkazId);
				entity.setAccountingMarkazName(accountingMarkazEntity.getName());
				entity.setAccountingMarkazCode(accountingMarkazEntity.getCode());
				entity.setAccountingMarkazDesc(accountingMarkazEntity.getDesc());
			} else {
				AccountingMarkazEntity nullAccountingMarkazEntity = new AccountingMarkazEntity();
				nullAccountingMarkazEntity.setName(SerajMessageUtil.getMessage("common_undefined"));
				entity.setAccountingMarkazName(nullAccountingMarkazEntity.getName());
			}

			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);

			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);
			if (entity.getBedehkar() - entity.getBestankar() > 0)
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
			else if (entity.getBestankar() - entity.getBedehkar() > 0)
				entity.setMandehBestankar(entity.getBestankar() - entity.getBedehkar());
			tarazAccountingMarkazAzmayeshiMap.put(accountingMarkazId, entity);
		}

		if (organId == null)
			applySanadEftetahiehOnTarazMarkazAzmayeshiMandeh(tarazAccountingMarkazAzmayeshiMap, saalMaaliEntity,
					numberOfDecimals);
		else
			applySanadEftetahiehOnTarazMarkazAzmayeshiMandeh(tarazAccountingMarkazAzmayeshiMap, saalMaaliEntity,
					organId, numberOfDecimals);

		return tarazAccountingMarkazAzmayeshiMap;
	}

	@Transactional(readOnly = true)
	public List<SanadHesabdariItemVO> getTarazMoeenAzmayeshi(SaalMaaliEntity saalMaaliEntity, Date fromDate,
			Date toDate, List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds,
			List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, Long organId,
			Long fromSerial, Long toSerial, Map<String, Object> sanadhesabdariItemFilter, boolean beforeClosingAccounts,
			int numberOfDecimals) {
		return getTarazMoeenAzmayeshi(saalMaaliEntity, fromDate, toDate, hesabKolIds, moeenIds, tafsiliIds,
				articleTafsiliIds, accountingMarkazIds, hesabType, organId, fromSerial, toSerial,
				sanadhesabdariItemFilter, true, beforeClosingAccounts, numberOfDecimals);
	}

	@Transactional(readOnly = true)
	public List<SanadHesabdariItemVO> getTarazMoeenAzmayeshi(SaalMaaliEntity saalMaaliEntity, Date fromDate,
			Date toDate, List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds,
			List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, Long organId,
			Long fromSerial, Long toSerial, Map<String, Object> sanadhesabdariItemFilter,
			boolean displayZeroMandehInTaraz, boolean beforeClosingAccounts, int numberOfDecimals) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter = populateTarazFilter(saalMaaliEntity, fromDate, toDate, hesabKolIds, moeenIds, tafsiliIds,
				articleTafsiliIds, accountingMarkazIds, hesabType, organId, fromSerial, toSerial,
				sanadhesabdariItemFilter, beforeClosingAccounts);
		List<SanadHesabdariItemVO> tarazMoeenAzmayeshiList = new ArrayList<SanadHesabdariItemVO>();

		List<Object[]> rawList = getMyDAO().getTarazMoeenAzmayeshi(localFilter);
		Map<Long, SanadHesabdariItemVO> tarazMoeenAzmayeshiMandeh = getTarazMoeenAzmayeshiMandeh(saalMaaliEntity,
				fromDate, hesabKolIds, moeenIds, tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organId,
				beforeClosingAccounts, numberOfDecimals);
		for (Object object : rawList) {
			SanadHesabdariItemVO entity = new SanadHesabdariItemVO();
			Object[] a = (Object[]) object;
			Long hesabMoeenId = Long.valueOf(a[0].toString());
			HesabMoeenEntity hesabMoeenEntity = hesabMoeenService.load(hesabMoeenId);

			entity.setHesabMoeenDesc(hesabMoeenEntity.getDesc());
			entity.setHesabMoeenName(hesabMoeenEntity.getName());
			entity.setHesabMoeenCode(hesabMoeenEntity.getCode());
			entity.setHesabMoeenID(hesabMoeenEntity.getId().toString());

			entity.setHesabKolDesc(hesabMoeenEntity.getHesabKol().getDesc());
			entity.setHesabKolName(hesabMoeenEntity.getHesabKol().getName());
			entity.setHesabKolCode(hesabMoeenEntity.getHesabKol().getCode());
			entity.setHesabKolMahyat(hesabMoeenEntity.getHesabKol().getMahyatKol());

			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);

			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);

			if (tarazMoeenAzmayeshiMandeh.get(hesabMoeenId) != null) {
				entity.setMandehBedehkarEbtedayDore(tarazMoeenAzmayeshiMandeh.get(hesabMoeenId).getMandehBedehkar());
				entity.setMandehBestankarEbtedayDore(tarazMoeenAzmayeshiMandeh.get(hesabMoeenId).getMandehBestankar());
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore() + entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore() + entity.getBestankar());
			} else {
				entity.setMandehBedehkarEbtedayDore(0d);
				entity.setMandehBestankarEbtedayDore(0d);
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore() + entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore() + entity.getBestankar());
			}

			if (entity.getBedehkar() - entity.getBestankar() > 0) {
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
				if (entity.getHesabKolMahyat().equals(MahyatKolEnum.Bedehkar)) {
					entity.setMandehByMahiyatHesabStr(new DecimalFormat().format(entity.getMandehBedehkar()));
					entity.setMandehByMahiyatHesabDbl(entity.getMandehBedehkar());
				} else if (entity.getHesabKolMahyat().equals(MahyatKolEnum.Bestankar)) {
					entity.setMandehByMahiyatHesabStr(
							"(" + new DecimalFormat().format(entity.getMandehBedehkar()) + ")");
					entity.setMandehByMahiyatHesabDbl(entity.getMandehBedehkar() * -1);
				}
			} else if (entity.getBestankar() - entity.getBedehkar() > 0) {
				entity.setMandehBestankar(entity.getBestankar() - entity.getBedehkar());

				if (entity.getHesabKolMahyat().equals(MahyatKolEnum.Bedehkar)) {
					entity.setMandehByMahiyatHesabStr(
							"(" + new DecimalFormat().format(entity.getMandehBestankar()) + ")");
					entity.setMandehByMahiyatHesabDbl(entity.getMandehBestankar() * -1);
				} else if (entity.getHesabKolMahyat().equals(MahyatKolEnum.Bestankar)) {
					entity.setMandehByMahiyatHesabStr(new DecimalFormat().format(entity.getMandehBestankar()));
					entity.setMandehByMahiyatHesabDbl(entity.getMandehBestankar());
				}

			}

			if (entity.getMandehBestankar() != 0 || entity.getMandehBedehkar() != 0)
				tarazMoeenAzmayeshiList.add(entity);
			else if (displayZeroMandehInTaraz)
				tarazMoeenAzmayeshiList.add(entity);
		}
		return tarazMoeenAzmayeshiList;
	}

	@Transactional(readOnly = true)
	public List<SanadHesabdariItemVO> getTarazTafsiliAzmayeshiTwo(SaalMaaliEntity saalMaaliEntity, Date fromDate,
			Date toDate, List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds,
			List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, Long organId,
			Long fromSerial, Long toSerial, Map<String, Object> sanadhesabdariItemFilter, boolean beforeClosingAccounts,
			int numberOfDecimals) {
		return getTarazTafsiliTwoAzmayeshi(saalMaaliEntity, fromDate, toDate, hesabKolIds, moeenIds, tafsiliIds,
				articleTafsiliIds, accountingMarkazIds, hesabType, organId, fromSerial, toSerial,
				sanadhesabdariItemFilter, true, beforeClosingAccounts, numberOfDecimals);
	}

	@Transactional(readOnly = true)
	public List<SanadHesabdariItemVO> getTarazTafsiliAzmayeshi(SaalMaaliEntity saalMaaliEntity, Date fromDate,
			Date toDate, List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds,
			List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, Long organId,
			Long fromSerial, Long toSerial, Map<String, Object> sanadhesabdariItemFilter, boolean beforeClosingAccounts,
			int numberOfDecimals) {
		return getTarazTafsiliAzmayeshi(saalMaaliEntity, fromDate, toDate, hesabKolIds, moeenIds, tafsiliIds,
				articleTafsiliIds, accountingMarkazIds, hesabType, organId, fromSerial, toSerial,
				sanadhesabdariItemFilter, true, beforeClosingAccounts, numberOfDecimals);
	}

	@Transactional(readOnly = true)
	public List<SanadHesabdariItemVO> getTarazTafsiliAzmayeshi(SaalMaaliEntity saalMaaliEntity, Date fromDate,
			Date toDate, List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds,
			List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, Long organId,
			Long fromSerial, Long toSerial, Map<String, Object> sanadhesabdariItemFilter,
			boolean displayZeroMandehInTaraz, boolean beforeClosingAccounts, int numberOfDecimals) {
		List<SanadHesabdariItemVO> tarazTafsiliAzmayeshiList = new ArrayList<SanadHesabdariItemVO>();
		Map<String, Object> filter = populateTarazFilter(saalMaaliEntity, fromDate, toDate, hesabKolIds, moeenIds,
				tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organId, fromSerial, toSerial,
				sanadhesabdariItemFilter, beforeClosingAccounts);

		List<Object[]> rawList = getMyDAO().getTarazTafsiliAzmayeshi(filter);
		Map<Long, SanadHesabdariItemVO> tarazTafsiliAzmayeshiMandeh = getTarazTafsiliAzmayeshiMandeh(saalMaaliEntity,
				fromDate, hesabKolIds, moeenIds, tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organId,
				beforeClosingAccounts, numberOfDecimals);

		for (Object[] object : rawList) {
			SanadHesabdariItemVO entity = new SanadHesabdariItemVO();
			Object[] a = (Object[]) object;
			Long hesabTafsiliId = -1l;
			if (a[0] != null) {
				hesabTafsiliId = Long.valueOf(a[0].toString());
				HesabTafsiliEntity hesabTafsiliEntity = hesabTafsiliService.load(hesabTafsiliId);
				entity.setHesabTafsiliDesc(hesabTafsiliEntity.getDesc());
				entity.setHesabTafsiliOneName(hesabTafsiliEntity.getName());
				entity.setHesabTafsiliCode(hesabTafsiliEntity.getCode().toString());
				entity.setHesabTafsiliOneID(hesabTafsiliEntity.getId().toString());
			} else {
				HesabTafsiliEntity nullHesabTafsili = new HesabTafsiliEntity();
				nullHesabTafsili.setDesc(SerajMessageUtil.getMessage("common_undefined"));
				entity.setHesabTafsiliOneName(nullHesabTafsili.getName());
			}

			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);

			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);

			if (tarazTafsiliAzmayeshiMandeh.get(hesabTafsiliId) != null) {
				entity.setMandehBedehkarEbtedayDore(
						tarazTafsiliAzmayeshiMandeh.get(hesabTafsiliId).getMandehBedehkar());
				entity.setMandehBestankarEbtedayDore(
						tarazTafsiliAzmayeshiMandeh.get(hesabTafsiliId).getMandehBestankar());
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore() + entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore() + entity.getBestankar());
			} else {
				entity.setMandehBedehkarEbtedayDore(0d);
				entity.setMandehBestankarEbtedayDore(0d);
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore() + entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore() + entity.getBestankar());
			}

			if (entity.getBedehkar() - entity.getBestankar() > 0) {
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());

				entity.setMandehByMahiyatHesabStr(new DecimalFormat().format(entity.getMandehBedehkar()));
				entity.setMandehByMahiyatHesabDbl(entity.getMandehBedehkar());

			} else if (entity.getBestankar() - entity.getBedehkar() > 0) {
				entity.setMandehBestankar(entity.getBestankar() - entity.getBedehkar());

				entity.setMandehByMahiyatHesabStr(new DecimalFormat().format(entity.getMandehBestankar()));
				entity.setMandehByMahiyatHesabDbl(entity.getMandehBestankar());

			}

			if (entity.getMandehBestankar() != 0 || entity.getMandehBedehkar() != 0)
				tarazTafsiliAzmayeshiList.add(entity);
			else if (displayZeroMandehInTaraz)
				tarazTafsiliAzmayeshiList.add(entity);
		}
		return tarazTafsiliAzmayeshiList;
	}

	@Transactional(readOnly = true)
	public List<SanadHesabdariItemVO> getTarazTafsiliTwoAzmayeshi(SaalMaaliEntity saalMaaliEntity, Date fromDate,
			Date toDate, List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds,
			List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, Long organId,
			Long fromSerial, Long toSerial, Map<String, Object> sanadhesabdariItemFilter,
			boolean displayZeroMandehInTaraz, boolean beforeClosingAccounts, int numberOfDecimals) {
		List<SanadHesabdariItemVO> tarazTafsiliAzmayeshiList = new ArrayList<SanadHesabdariItemVO>();
		Map<String, Object> filter = populateTarazFilter(saalMaaliEntity, fromDate, toDate, hesabKolIds, moeenIds,
				tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organId, fromSerial, toSerial,
				sanadhesabdariItemFilter, beforeClosingAccounts);

		List<Object[]> rawList = getMyDAO().getTarazTafsiliAzmayeshiTwo(filter);
		Map<Long, SanadHesabdariItemVO> tarazTafsiliAzmayeshiTwoMandeh = getTarazTafsiliAzmayeshiTwoMandeh(
				saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds, articleTafsiliIds, accountingMarkazIds,
				hesabType, organId, beforeClosingAccounts, numberOfDecimals);

		for (Object[] object : rawList) {
			SanadHesabdariItemVO entity = new SanadHesabdariItemVO();
			Object[] a = (Object[]) object;
			Long hesabTafsiliId = -1l;
			if (a[0] != null) {
				hesabTafsiliId = Long.valueOf(a[0].toString());
				HesabTafsiliEntity hesabTafsiliEntity = hesabTafsiliService.load(hesabTafsiliId);
				entity.setHesabTafsiliDesc(hesabTafsiliEntity.getDesc());
				entity.setHesabTafsiliTwoName(hesabTafsiliEntity.getName());
				entity.setHesabTafsiliCode(hesabTafsiliEntity.getCode().toString());
				entity.setHesabTafsiliTwoID(hesabTafsiliEntity.getId().toString());
			} else {
				HesabTafsiliEntity nullHesabTafsili = new HesabTafsiliEntity();
				nullHesabTafsili.setDesc(SerajMessageUtil.getMessage("common_undefined"));
				entity.setHesabTafsiliTwoName(nullHesabTafsili.getName());
			}

			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);

			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);

			if (tarazTafsiliAzmayeshiTwoMandeh.get(hesabTafsiliId) != null) {
				entity.setMandehBedehkarEbtedayDore(
						tarazTafsiliAzmayeshiTwoMandeh.get(hesabTafsiliId).getMandehBedehkar());
				entity.setMandehBestankarEbtedayDore(
						tarazTafsiliAzmayeshiTwoMandeh.get(hesabTafsiliId).getMandehBestankar());
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore() + entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore() + entity.getBestankar());
			} else {
				entity.setMandehBedehkarEbtedayDore(0d);
				entity.setMandehBestankarEbtedayDore(0d);
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore() + entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore() + entity.getBestankar());
			}

			if (entity.getBedehkar() - entity.getBestankar() > 0) {
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());

				entity.setMandehByMahiyatHesabStr(new DecimalFormat().format(entity.getMandehBedehkar()));
				entity.setMandehByMahiyatHesabDbl(entity.getMandehBedehkar());

			} else if (entity.getBestankar() - entity.getBedehkar() > 0) {
				entity.setMandehBestankar(entity.getBestankar() - entity.getBedehkar());

				entity.setMandehByMahiyatHesabStr(new DecimalFormat().format(entity.getMandehBestankar()));
				entity.setMandehByMahiyatHesabDbl(entity.getMandehBestankar());

			}

			if (entity.getMandehBestankar() != 0 || entity.getMandehBedehkar() != 0)
				tarazTafsiliAzmayeshiList.add(entity);
			else if (displayZeroMandehInTaraz)
				tarazTafsiliAzmayeshiList.add(entity);
		}
		return tarazTafsiliAzmayeshiList;
	}

	@Transactional(readOnly = true)
	public List<SanadHesabdariItemVO> getTarazTafsiliShenavarAzmayeshi(SaalMaaliEntity saalMaaliEntity, Date fromDate,
			Date toDate, List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds,
			List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, Long organId,
			Long fromSerial, Long toSerial, Map<String, Object> sanadhesabdariItemFilter, Integer level,
			boolean displayZeroMandehInTaraz, boolean beforeClosingAccounts, int numberOfDecimals) {
		List<SanadHesabdariItemVO> tarazTafsiliAzmayeshiList = new ArrayList<SanadHesabdariItemVO>();
		Map<String, Object> filter = populateTarazFilter(saalMaaliEntity, fromDate, toDate, hesabKolIds, moeenIds,
				tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organId, fromSerial, toSerial,
				sanadhesabdariItemFilter, beforeClosingAccounts);

		filter.put("articleTafsili.level@eq", level);

		List<Object[]> rawList = getMyDAO().getTarazShenavarAzmayeshi(filter);
		Map<Long, SanadHesabdariItemVO> tarazTafsiliAzmayeshiMandeh = getTarazTafsiliShenavarAzmayeshiMandeh(
				saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds, articleTafsiliIds, accountingMarkazIds,
				hesabType, organId, level, beforeClosingAccounts, numberOfDecimals);

		for (Object[] object : rawList) {
			SanadHesabdariItemVO entity = new SanadHesabdariItemVO();
			Object[] a = (Object[]) object;
			Long hesabTafsiliId = -1l;
			if (a[0] != null) {
				hesabTafsiliId = Long.valueOf(a[0].toString());
				HesabTafsiliEntity hesabTafsiliEntity = hesabTafsiliService.load(hesabTafsiliId);
				entity.setHesabTafsiliDesc(hesabTafsiliEntity.getDesc());
				entity.setHesabTafsiliOneName(hesabTafsiliEntity.getName());
				entity.setHesabTafsiliCode(hesabTafsiliEntity.getCode().toString());
				entity.setHesabTafsiliOneID(hesabTafsiliEntity.getId().toString());
			} else {
				HesabTafsiliEntity nullHesabTafsili = new HesabTafsiliEntity();
				nullHesabTafsili.setDesc(SerajMessageUtil.getMessage("common_undefined"));
				entity.setHesabTafsiliOneName(nullHesabTafsili.getName());
			}

			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);

			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);

			if (tarazTafsiliAzmayeshiMandeh.get(hesabTafsiliId) != null) {
				entity.setMandehBedehkarEbtedayDore(
						tarazTafsiliAzmayeshiMandeh.get(hesabTafsiliId).getMandehBedehkar());
				entity.setMandehBestankarEbtedayDore(
						tarazTafsiliAzmayeshiMandeh.get(hesabTafsiliId).getMandehBestankar());
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore() + entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore() + entity.getBestankar());
			} else {
				entity.setMandehBedehkarEbtedayDore(0d);
				entity.setMandehBestankarEbtedayDore(0d);
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore() + entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore() + entity.getBestankar());
			}

			if (entity.getBedehkar() - entity.getBestankar() > 0)
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
			else if (entity.getBestankar() - entity.getBedehkar() > 0)
				entity.setMandehBestankar(entity.getBestankar() - entity.getBedehkar());

			if (entity.getMandehBestankar() != 0 || entity.getMandehBedehkar() != 0)
				tarazTafsiliAzmayeshiList.add(entity);
			else if (displayZeroMandehInTaraz)
				tarazTafsiliAzmayeshiList.add(entity);
		}
		return tarazTafsiliAzmayeshiList;
	}

	@Transactional(readOnly = true)
	public List<SanadHesabdariItemVO> getTarazAccountingMarkazAzmayeshi(SaalMaaliEntity saalMaaliEntity, Date fromDate,
			Date toDate, List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds,
			List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, Long organId,
			Long fromSerial, Long toSerial, Map<String, Object> sanadhesabdariItemFilter,
			boolean displayZeroMandehInTaraz, boolean beforeClosingAccounts, int numberOfDecimals) {
		List<SanadHesabdariItemVO> tarazAccountingMarkazAzmayeshiList = new ArrayList<SanadHesabdariItemVO>();
		Map<String, Object> filter = populateTarazFilter(saalMaaliEntity, fromDate, toDate, hesabKolIds, moeenIds,
				tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organId, fromSerial, toSerial,
				sanadhesabdariItemFilter, beforeClosingAccounts);

		List<Object[]> rawList = getMyDAO().getTarazAccountingMarkazAzmayeshi(filter);
		Map<Long, SanadHesabdariItemVO> tarazAccountingMarkazAzmayeshiMandeh = getTarazAccountingMarkazAzmayeshiMandeh(
				saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds, articleTafsiliIds, accountingMarkazIds,
				hesabType, organId, beforeClosingAccounts, numberOfDecimals);

		for (Object[] object : rawList) {
			SanadHesabdariItemVO entity = new SanadHesabdariItemVO();
			Object[] a = (Object[]) object;
			Long accoutingMarkazId = -1l;
			if (a[0] != null) {
				accoutingMarkazId = Long.valueOf(a[0].toString());
				AccountingMarkazEntity accoutingMarkazEntity = accountingMarkazService.load(accoutingMarkazId);
				entity.setAccountingMarkazName(accoutingMarkazEntity.getName());
				entity.setAccountingMarkazDesc(accoutingMarkazEntity.getDesc());
				entity.setAccountingMarkazCode(accoutingMarkazEntity.getCode());
			} else {
				AccountingMarkazEntity nullAccountingMarkaz = new AccountingMarkazEntity();
				nullAccountingMarkaz.setDesc(SerajMessageUtil.getMessage("common_undefined"));
				entity.setAccountingMarkazName(nullAccountingMarkaz.getName());
			}

			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);

			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);

			if (tarazAccountingMarkazAzmayeshiMandeh.get(accoutingMarkazId) != null) {
				entity.setMandehBedehkarEbtedayDore(
						tarazAccountingMarkazAzmayeshiMandeh.get(accoutingMarkazId).getMandehBedehkar());
				entity.setMandehBestankarEbtedayDore(
						tarazAccountingMarkazAzmayeshiMandeh.get(accoutingMarkazId).getMandehBestankar());
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore() + entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore() + entity.getBestankar());
			} else {
				entity.setMandehBedehkarEbtedayDore(0d);
				entity.setMandehBestankarEbtedayDore(0d);
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore() + entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore() + entity.getBestankar());
			}

			if (entity.getBedehkar() - entity.getBestankar() > 0)
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
			else if (entity.getBestankar() - entity.getBedehkar() > 0)
				entity.setMandehBestankar(entity.getBestankar() - entity.getBedehkar());

			if (entity.getMandehBestankar() != 0 || entity.getMandehBedehkar() != 0)
				tarazAccountingMarkazAzmayeshiList.add(entity);
			else if (displayZeroMandehInTaraz)
				tarazAccountingMarkazAzmayeshiList.add(entity);
		}
		return tarazAccountingMarkazAzmayeshiList;
	}

	@Transactional(readOnly = true)
	public List<SanadHesabdariItemEntity> getTarazAccountingMarkazShenavarAzmayeshi(SaalMaaliEntity saalMaaliEntity,
			Date fromDate, Date toDate, List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds,
			List<Long> articleTafsiliIds, List<Long> accountingMarkazIds, HesabTypeEnum hesabType, Long organId,
			Long fromSerial, Long toSerial, Map<String, Object> sanadhesabdariItemFilter,
			boolean displayZeroMandehInTaraz, boolean beforeClosingAccounts, int numberOfDecimals) {
		List<SanadHesabdariItemEntity> tarazAccountingMarkazAzmayeshiList = new ArrayList<SanadHesabdariItemEntity>();
		Map<String, Object> filter = populateTarazFilter(saalMaaliEntity, fromDate, toDate, hesabKolIds, moeenIds,
				tafsiliIds, articleTafsiliIds, accountingMarkazIds, hesabType, organId, fromSerial, toSerial,
				sanadhesabdariItemFilter, beforeClosingAccounts);

		filter.put("articleTafsili.level@eq", 2);
		List<Object[]> rawList = getMyDAO().getTarazAccountingMarkazShenavarAzmayeshi(filter);
		Map<Long, SanadHesabdariItemVO> tarazAccountingMarkazAzmayeshiMandeh = getTarazAccountingMarkazAzmayeshiMandeh(
				saalMaaliEntity, fromDate, hesabKolIds, moeenIds, tafsiliIds, articleTafsiliIds, accountingMarkazIds,
				hesabType, organId, beforeClosingAccounts, numberOfDecimals);

		for (Object[] object : rawList) {
			SanadHesabdariItemEntity entity = new SanadHesabdariItemEntity();
			Object[] a = (Object[]) object;
			Long accoutingMarkazId = -1l;
			if (a[0] != null) {
				accoutingMarkazId = Long.valueOf(a[0].toString());
				AccountingMarkazEntity accoutingMarkazEntity = accountingMarkazService.load(accoutingMarkazId);
				entity.setAccountingMarkaz(accoutingMarkazEntity);
			} else {
				AccountingMarkazEntity nullAccountingMarkaz = new AccountingMarkazEntity();
				nullAccountingMarkaz.setDesc(SerajMessageUtil.getMessage("common_undefined"));
				entity.setAccountingMarkaz(nullAccountingMarkaz);
			}

			entity.setBestankar((Double) a[2]);
			entity.setBedehkar((Double) a[3]);

			entity.setMandehBedehkar(0d);
			entity.setMandehBestankar(0d);

			if (tarazAccountingMarkazAzmayeshiMandeh.get(accoutingMarkazId) != null) {
				entity.setMandehBedehkarEbtedayDore(
						tarazAccountingMarkazAzmayeshiMandeh.get(accoutingMarkazId).getMandehBedehkar());
				entity.setMandehBestankarEbtedayDore(
						tarazAccountingMarkazAzmayeshiMandeh.get(accoutingMarkazId).getMandehBestankar());
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore() + entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore() + entity.getBestankar());
			} else {
				entity.setMandehBedehkarEbtedayDore(0d);
				entity.setMandehBestankarEbtedayDore(0d);
				entity.setOperationSummaryBedehkar(entity.getMandehBedehkarEbtedayDore() + entity.getBedehkar());
				entity.setOperationSummaryBestankar(entity.getMandehBestankarEbtedayDore() + entity.getBestankar());
			}

			if (entity.getBedehkar() - entity.getBestankar() > 0)
				entity.setMandehBedehkar(entity.getBedehkar() - entity.getBestankar());
			else if (entity.getBestankar() - entity.getBedehkar() > 0)
				entity.setMandehBestankar(entity.getBestankar() - entity.getBedehkar());

			if (entity.getMandehBestankar() != 0 || entity.getMandehBedehkar() != 0)
				tarazAccountingMarkazAzmayeshiList.add(entity);
			else if (displayZeroMandehInTaraz)
				tarazAccountingMarkazAzmayeshiList.add(entity);
		}
		return tarazAccountingMarkazAzmayeshiList;
	}

	private Map<String, Object> populateTarazFilter(SaalMaaliEntity saalMaaliEntity, Date fromDate, Date toDate,
			List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds, List<Long> articleTafsiliIds,
			List<Long> accountingMarkazIds, HesabTypeEnum hesabType, Long organId, Long fromSerial, Long toSerial,
			Map<String, Object> sanadhesabdariItemFilter, boolean beforeClosingAccounts) {
		Map<String, Object> localFilter = new HashMap<String, Object>();

		if (beforeClosingAccounts)
			localFilter.put("sanadHesabdari.sanadFunction@notIn",
					Arrays.asList(SanadFunctionEnum.BASTAN_HESABHA, SanadFunctionEnum.EKHTETAMIE));

		localFilter.putAll(sanadhesabdariItemFilter);

		localFilter.put("sanadHesabdari.state@eqORsanadHesabdari.state@eq",
				Arrays.asList(SanadStateEnum.DAEM, SanadStateEnum.BARRESI_SHODE));
		localFilter.put("sanadHesabdari.sanadFunction@neq@sanadHesabdari.sanadFunction@neq",
				Arrays.asList(SanadFunctionEnum.EFTETAHIE, SanadFunctionEnum.EKHTETAMIE));

		localFilter.put("sanadHesabdari.serial@ge", fromSerial);
		localFilter.put("sanadHesabdari.serial@le", toSerial);

		if (organId != null)
			localFilter.put("sanadHesabdari.organId@eq", organId);

		localFilter.put("hesabKol.hesabGroup.type@eq", hesabType);
		if (moeenIds != null && !moeenIds.isEmpty())
			localFilter.put("hesabMoeen.id@in", moeenIds);

		if (hesabKolIds != null && !hesabKolIds.isEmpty())
			localFilter.put("hesabKol.id@in", hesabKolIds);

		if (tafsiliIds != null && !tafsiliIds.isEmpty())
			localFilter.put("hesabTafsili.id@in", tafsiliIds);

		if (articleTafsiliIds != null && !articleTafsiliIds.isEmpty())
			localFilter.put("articleTafsili.hesabTafsili.id@in", articleTafsiliIds);

		if (accountingMarkazIds != null && !accountingMarkazIds.isEmpty())
			localFilter.put("accountingMarkaz.id@in", accountingMarkazIds);

		if (saalMaaliEntity != null && saalMaaliEntity.getId() != null) {
			localFilter.put("sanadHesabdari.saalMaali.id@eq", saalMaaliEntity.getId());
			if (fromDate == null)
				localFilter.put("sanadHesabdari.tarikhSanad@ge",
						saalMaaliService.getFirstDateOfSaalMaali(saalMaaliEntity));
			else
				localFilter.put("sanadHesabdari.tarikhSanad@ge", fromDate);
			if (toDate == null)
				localFilter.put("sanadHesabdari.tarikhSanad@le",
						saalMaaliService.getLastDateOfSaalMaali(saalMaaliEntity));
			else
				localFilter.put("sanadHesabdari.tarikhSanad@le", toDate);
		} else {
			localFilter.put("sanadHesabdari.tarikhSanad@ge", fromDate);
			localFilter.put("sanadHesabdari.tarikhSanad@le", toDate);
		}
		return localFilter;
	}

	private Map<String, Object> populateMandehTarazFilter(SaalMaaliEntity saalMaaliEntity, Date fromDate,
			List<Long> hesabKolIds, List<Long> moeenIds, List<Long> tafsiliIds, List<Long> articleTafsiliIds,
			List<Long> accountingMarkazIds, HesabTypeEnum hesabType, Long organId, boolean beforeClosingAccounts) {
		Map<String, Object> localFilter = new HashMap<String, Object>();

		if (beforeClosingAccounts)
			localFilter.put("sanadHesabdari.sanadFunction@notIn",
					Arrays.asList(SanadFunctionEnum.BASTAN_HESABHA, SanadFunctionEnum.EKHTETAMIE));

		localFilter.put("sanadHesabdari.state@eqORsanadHesabdari.state@eq",
				Arrays.asList(SanadStateEnum.DAEM, SanadStateEnum.BARRESI_SHODE));
		localFilter.put("sanadHesabdari.sanadFunction@neq@sanadHesabdari.sanadFunction@neq",
				Arrays.asList(SanadFunctionEnum.EFTETAHIE, SanadFunctionEnum.EKHTETAMIE));

		if (organId != null)
			localFilter.put("sanadHesabdari.organId@eq", organId);

		localFilter.put("hesabKol.hesabGroup.type@eq", hesabType);
		if (moeenIds != null && !moeenIds.isEmpty())
			localFilter.put("hesabMoeen.id@in", moeenIds);

		if (hesabKolIds != null && !hesabKolIds.isEmpty())
			localFilter.put("hesabKol.id@in", hesabKolIds);

		if (tafsiliIds != null && !tafsiliIds.isEmpty())
			localFilter.put("hesabTafsili.id@in", tafsiliIds);

		if (articleTafsiliIds != null && !articleTafsiliIds.isEmpty())
			localFilter.put("articleTafsili.hesabTafsili.id@in", articleTafsiliIds);

		if (accountingMarkazIds != null && !accountingMarkazIds.isEmpty())
			localFilter.put("accountingMarkaz.id@in", accountingMarkazIds);

		if (saalMaaliEntity != null && saalMaaliEntity.getId() != null) {
			localFilter.put("sanadHesabdari.saalMaali.id@eq", saalMaaliEntity.getId());
			if (fromDate != null)
				localFilter.put("sanadHesabdari.tarikhSanad@le", fromDate);
			else
				localFilter.put("sanadHesabdari.tarikhSanad@le",
						saalMaaliService.getFirstDateOfSaalMaali(saalMaaliEntity));
			localFilter.put("sanadHesabdari.tarikhSanad@ge", saalMaaliService.getFirstDateOfSaalMaali(saalMaaliEntity));
		} else {
			if (fromDate != null)
				localFilter.put("sanadHesabdari.tarikhSanad@le", fromDate);
		}

		return localFilter;
	}

	public List<SanadHesabdariItemEntity> getIncomeGroupByKol(Map<String, Object> filter) {
		return getItemsGroupByKol(filter, HesabTypeEnum.INCOME, null, null, null);
	}

	public List<SanadHesabdariItemEntity> getExpenseGroupByKol(Map<String, Object> filter) {
		return getItemsGroupByKol(filter, HesabTypeEnum.EXPENSE, null, null, null);
	}

	@Transactional(readOnly = true)
	public List<SanadHesabdariItemEntity> getItemsGroupByKol(Map<String, Object> filter, HesabTypeEnum hesabTypeEnum,
			Long hesabKolId, Date from, Date to) {
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
	public void duplicateEntityWithoutId(SanadHesabdariItemEntity destEntity, SanadHesabdariItemEntity srcEntity) {
		super.duplicateEntityWithoutId(destEntity, srcEntity);

//		Set<ArticleTafsiliEntity> articleTafsili = srcEntity.getArticleTafsili();
//		if(articleTafsili!=null){
//			destEntity.getArticleTafsili().clear();
//			for (ArticleTafsiliEntity articleTafsiliEntity : articleTafsili) {
//				ArticleTafsiliEntity destArticleTafsiliEntity = new ArticleTafsiliEntity();
//				destArticleTafsiliEntity.setHesabTafsili(articleTafsiliEntity.getHesabTafsili());
//				destArticleTafsiliEntity.setLevel(articleTafsiliEntity.getLevel());
//				destArticleTafsiliEntity.setSanadHesabdariItem(destEntity);
//				destArticleTafsiliEntity.setDesc(articleTafsiliEntity.getHesabTafsili().getDesc());
//				destEntity.getArticleTafsili().add(destArticleTafsiliEntity);
//			}
//		}

//		Set<ArticleAccountingMarkazEntity> articleAccountingMarkaz = srcEntity.getArticleAccountingMarkaz();
//		if(articleAccountingMarkaz !=null){
//			destEntity.getArticleAccountingMarkaz().clear();
//			for (ArticleAccountingMarkazEntity articleAccountingMarkazEntity : articleAccountingMarkaz) {
//				ArticleAccountingMarkazEntity destArticleAccountingMarkazEntity = new ArticleAccountingMarkazEntity();;
//				destArticleAccountingMarkazEntity.setAccountingMarkaz(articleAccountingMarkazEntity.getAccountingMarkaz());
//				destArticleAccountingMarkazEntity.setLevel(articleAccountingMarkazEntity.getLevel());
//				destArticleAccountingMarkazEntity.setSanadHesabdariItem(destEntity);
//				destArticleAccountingMarkazEntity.setDesc(articleAccountingMarkazEntity.getAccountingMarkaz().getDesc());
//				destEntity.getArticleAccountingMarkaz().add(destArticleAccountingMarkazEntity);
//			}
//		}
	}
}