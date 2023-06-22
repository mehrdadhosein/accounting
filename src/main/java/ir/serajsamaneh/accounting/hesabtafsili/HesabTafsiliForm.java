package ir.serajsamaneh.accounting.hesabtafsili;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity;
import ir.serajsamaneh.accounting.base.BaseAccountingForm;
import ir.serajsamaneh.accounting.exception.NoSaalMaaliFoundException;
import ir.serajsamaneh.accounting.hesabclassification.HesabClassificationEntity;
import ir.serajsamaneh.accounting.hesabclassification.HesabClassificationService;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.core.util.StringUtil;
import ir.serajsamaneh.erpcore.contacthesab.ContactHesabService;
import ir.serajsamaneh.erpcore.util.HesabRelationsUtil;
import ir.serajsamaneh.erpcore.util.HesabTemplateRelationsUtil;
@Named("hesabTafsili")
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class HesabTafsiliForm extends BaseAccountingForm<HesabTafsiliEntity, Long> {

	@Override
	protected HesabTafsiliService getMyService() {
		return hesabTafsiliService;
	}

	HesabMoeenService hesabMoeenService;

	HesabTafsiliService hesabTafsiliService;
	SaalMaaliService saalMaaliService;
//	ContactService contactService;
	ContactHesabService contactHesabService;
	HesabClassificationService hesabClassificationService;

	public HesabClassificationService getHesabClassificationService() {
		return hesabClassificationService;
	}

	public void setHesabClassificationService(HesabClassificationService hesabClassificationService) {
		this.hesabClassificationService = hesabClassificationService;
	}

	public ContactHesabService getContactHesabService() {
		return contactHesabService;
	}

	public void setContactHesabService(ContactHesabService contactHesabService) {
		this.contactHesabService = contactHesabService;
	}

//	public ContactService getContactService() {
//		return contactService;
//	}
//
//	public void setContactService(ContactService contactService) {
//		this.contactService = contactService;
//	}

	public SaalMaaliService getSaalMaaliService() {
		return saalMaaliService;
	}

	public void setSaalMaaliService(SaalMaaliService saalMaaliService) {
		this.saalMaaliService = saalMaaliService;
	}

//	public DataModel<HesabTafsiliEntity> getLocalShenavarDataModel() {
//		getFilter().put("isShenavar@eq",Boolean.TRUE);
//		return getLocalDataModel();
//	}

	public DataModel<HesabTafsiliEntity> getLocalTafsiliDataModel() {
//		getFilter().put("isShenavar@eq",Boolean.FALSE);
		return getLocalDataModel();
	}

	public DataModel<HesabTafsiliEntity> getLocalDataModel() {
		setSearchAction(true);

		populateTopOrgansIdListFilter();

//		getFilter().put("organ.code@startlk", getTopOrgan().getCode());
		getFilter().put("saalMaali.id@eq", getCurrentUserActiveSaalMaali().getId());
		return getDataModel();
	}

	public DataModel<HesabTafsiliEntity> getUpsetTafsiliDataModel() {
//		getFilter().put("isShenavar@eq",Boolean.FALSE);
		return getUpsetDataModel();
	}

//	public DataModel<HesabTafsiliEntity> getUpsetShenavarDataModel() {
//		getFilter().put("isShenavar@eq",Boolean.TRUE);
//		return getUpsetDataModel();
//	}

	@Override
	public DataModel<HesabTafsiliEntity> getUpsetDataModel() {
		setSearchAction(true);
//		populateTopOrgansIdListFilter();
		getFilter().put("saalMaali.id@eq", getCurrentUserActiveSaalMaali().getId());
		return super.getUpsetDataModel();
	}

	@Override
	public Integer getUpsetDataModelCount() {

		populateTopOrgansIdListFilter();
		getFilter().put("saalMaali.id@eq", getCurrentUserActiveSaalMaali().getId());
		return super.getHierarchicalDataModelCount();
	}

	public void setHesabTafsiliService(HesabTafsiliService hesabTafsiliService) {
		this.hesabTafsiliService = hesabTafsiliService;
	}

	public HesabTafsiliService getHesabTafsiliService() {
		return hesabTafsiliService;
	}

	List<Long> moeenIds;
	List<Long> childTafsiliIds;
	List<Long> parentTafsiliIds;
	List<Long> childAccountingMarkazIds;

	public List<Long> getChildAccountingMarkazIds() {
		if (childAccountingMarkazIds == null) {
			childAccountingMarkazIds = new ArrayList<Long>();
			Set<AccountingMarkazEntity> childAccountingMarkazSet = getEntity().getChildAccountingMarkaz();
			if (childAccountingMarkazSet != null) {
				for (AccountingMarkazEntity accountingMarkazEntity : childAccountingMarkazSet) {
					childAccountingMarkazIds.add(accountingMarkazEntity.getId());
				}
			}
		}
		return childAccountingMarkazIds;
	}

	public void setChildAccountingMarkazIds(List<Long> childAccountingMarkazIds) {
		this.childAccountingMarkazIds = childAccountingMarkazIds;
	}

	public List<Long> getChildTafsiliIds() {
		if (childTafsiliIds == null) {
			childTafsiliIds = new ArrayList<Long>();
			Set<HesabTafsiliEntity> tafsilies = getEntity().getChilds();
			if (tafsilies != null) {
				for (HesabTafsiliEntity tafsiliEntity : tafsilies) {
					childTafsiliIds.add(tafsiliEntity.getId());
				}
			}
		}
		return childTafsiliIds;
	}

	public void setChildTafsiliIds(List<Long> childTafsiliIds) {
		this.childTafsiliIds = childTafsiliIds;
	}

	public List<Long> getParentTafsiliIds() {
		if (parentTafsiliIds == null) {
			parentTafsiliIds = new ArrayList<Long>();
			Set<HesabTafsiliEntity> tafsilies = getEntity().getParents();
			if (tafsilies != null) {
				for (HesabTafsiliEntity tafsiliEntity : tafsilies) {
					parentTafsiliIds.add(tafsiliEntity.getId());
				}
			}
		}
		return parentTafsiliIds;
	}

	public void setParentTafsiliIds(List<Long> parentTafsiliIds) {
		this.parentTafsiliIds = parentTafsiliIds;
	}

	public List<Long> getMoeenIds() {
		if (moeenIds == null) {
			moeenIds = new ArrayList<Long>();
			List<HesabMoeenEntity> moeens = getEntity().getHesabMoeenList();
			if (moeens != null) {
				for (HesabMoeenEntity moeen : moeens) {
					moeenIds.add(moeen.getId());
				}
			}
		}
		return moeenIds;
	}

	public void setMoeenIds(List<Long> moeenIds) {
		this.moeenIds = moeenIds;
	}

	@Override
	public String save() {
		getEntity().setOrganId(getCurrentOrganVO().getId());
		getEntity().setOrganName(getCurrentOrganVO().getName());
		if (getEntity().getHesabClassification() != null && getEntity().getHesabClassification().getId() != null)
			getEntity().setHesabClassification(
					hesabClassificationService.load(getEntity().getHesabClassification().getId()));

		getMyService().save(getEntity(), getMoeenIds(), getChildTafsiliIds(), getParentTafsiliIds(),
				getChildAccountingMarkazIds(), getCurrentUserActiveSaalMaali(), getCurrentOrganVO().getId(),
				getCurrentOrganVO().getTopOrgansIdList(), getCurrentOrganVO().getTopParentCode(),
				getCurrentOrganVO().getName());
		resetHesabRelations();
		HesabRelationsUtil.resetTafsiliAccountingMarkazChildMap(getCurrentUserActiveSaalMaali(),
				getCurrentOrganVO().getId());
		addInfoMessage("SUCCESSFUL_ACTION");
		return getViewUrl();
	}

	private void resetHesabRelations() {
		List<Long> subsetOrganIds = getRelatedOrganIds();
		for (Long organId : subsetOrganIds) {
//			OrganEntity organEntity = organService.load(organId);
			HesabRelationsUtil.resetTafsiliMoeenMap(getCurrentUserActiveSaalMaali(), organId);
			HesabRelationsUtil.resetmoeenTafsiliOneMap(getCurrentUserActiveSaalMaali(), organId);
			HesabRelationsUtil.resetmoeenTafsiliTwoMap(getCurrentUserActiveSaalMaali(), organId);
			HesabRelationsUtil.resetTafsiliChildMap(getCurrentUserActiveSaalMaali(), organId);
			HesabRelationsUtil.resetTafsiliAccountingMarkazChildMap(getCurrentUserActiveSaalMaali(), organId);
			HesabRelationsUtil.resetRootHesabsMap(getCurrentUserActiveSaalMaali(), organId);

			HesabTemplateRelationsUtil.resetMoeenKolTemplateMap(organId);
			HesabTemplateRelationsUtil.resetmoeenTafsiliTemplateMap(organId);
			HesabTemplateRelationsUtil.resetAccountingMarkazTemplateMap(organId);

		}
	}

	public String localTafsilSave() {
		getEntity().setOrganId(getCurrentOrganVO().getId());
		getEntity().setOrganName(getCurrentOrganVO().getName());
//		getEntity().setIsShenavar(Boolean.FALSE);
		save();
		return getLocalViewUrl();
	}

//	public String localShenavarSave() {
//		getEntity().setOrgan(getCurrentOrgan());
//		getEntity().setIsShenavar(Boolean.TRUE);
//		save();
//		return getLocalViewUrl();
//	}

	public DataModel<HesabTafsiliEntity> getActiveDataModel() {
		getFilter().put("hidden@eq", Boolean.FALSE);
		getFilter().put("hesabMoeen.hidden@eq", Boolean.FALSE);
		return super.getDataModel();
	}

	public List<SelectItem> getHesabMoeenSelectItems() {
		List<HesabMoeenEntity> moeenList = hesabMoeenService.getDataList();
		List<SelectItem> resultList = new ArrayList<SelectItem>();

		for (HesabMoeenEntity hesabMoeenEntity : moeenList) {
			resultList.add(new SelectItem(hesabMoeenEntity.getId(), hesabMoeenEntity.getName()));
		}

		return resultList;
	}

	public List<SelectItem> getLocalHesabTafsiliSelectItems() {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("organId@eq", getCurrentOrganVO().getId());
		List<HesabTafsiliEntity> list = getMyService().getDataList(filter);
		List<SelectItem> resultList = new ArrayList<SelectItem>();
		for (HesabTafsiliEntity entity : list) {
			resultList.add(new SelectItem(entity.getId(), entity.getName()));
		}

		return resultList;
	}

	@Override
	public List<? extends BaseEntity> getJsonList(String property, String term, boolean all,
			Map<String, String> params) {
		try {
			String isLocalUser = params.get("isCurrentOrgan");
//			String isShenavar = params.get("isShenavar");
			String tafsiliLevel = params.get("tafsiliLevel");

			this.getFilter().put("saalMaali.id@eq", getCurrentUserActiveSaalMaali().getId());

			if (isLocalUser != null && isLocalUser.equals("true")) {
				getFilter().put("organId@eq", getCurrentOrganVO().getId());
			}

//			if (isShenavar !=null && isShenavar.equals("true")){
//				getFilter().put("isShenavar@eq",Boolean.TRUE);
//			}else if (isShenavar !=null && isShenavar.equals("false")){
//				getFilter().put("isShenavar@eq",Boolean.FALSE);
//			}

			if (StringUtil.hasText(tafsiliLevel)) {
				getFilter().put("level@eq", Integer.valueOf(tafsiliLevel));
			} else {
				// throw new FatalException("tafsili level is null");
				return new ArrayList<>();
			}

			String isHierarchical = params.get("isHierarchical");
			String hidden = params.get("hidden");

			if (StringUtils.hasText(hidden) && hidden.equals("false")) {
				this.getFilter().put("hidden@eq", false);
			}

			if (isHierarchical != null && isHierarchical.equals("true")) {
				List<Long> topOrganList = getCurrentOrganVO().getTopOrgansIdList();
				getFilter().put("organId@in", topOrganList);

				params.put("isLocal", "false");
			}

			return super.getJsonList(property, term, all, params);
		} catch (NoSaalMaaliFoundException e) {
			// e.printStackTrace();
			System.out.println(e.getMessage());
			return new ArrayList<>();
		}
	}

	public Map<Long, List<ListOrderedMap<String, Object>>> getTafsiliMoeenMap() {
		return HesabRelationsUtil.getTafsiliMoeenMap(getCurrentUserActiveSaalMaali(), getCurrentOrganVO().getId(),
				getCurrentOrganVO().getTopOrgansIdList());
	}

	public Map<Long, List<ListOrderedMap<String, Object>>> getTafsiliChildMap() {
		return HesabRelationsUtil.getTafsiliChildMap(getCurrentUserActiveSaalMaali(), getCurrentOrganVO().getId(),
				getCurrentOrganVO().getTopOrgansIdList());
	}

	public Map<Long, List<ListOrderedMap<String, Object>>> getTafsiliAccountingMarkazChildMap() {
		return HesabRelationsUtil.getTafsiliAccountingMarkazChildMap(getCurrentUserActiveSaalMaali(),
				getCurrentOrganVO().getId(), getCurrentOrganVO().getTopOrgansIdList());
	}

	public String importFromHesabTafsiliTemplateList() {
		try {
			getMyService().importFromHesabTafsiliTemplateList(getCurrentUserActiveSaalMaali(),
					getCurrentOrganVO().getId(), getCurrentOrganVO().getTopOrgansIdList(),
					getCurrentOrganVO().getTopParentCode(), getCurrentOrganVO().getName());
			setDataModel(null);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String delete() {
		if (!getIsForMyOrgan())
			throw new FatalException(SerajMessageUtil.getMessage("common_deleteNotAllowed"));
		return super.delete();
	}

	public boolean getIsForMyOrgan() {
		if (getEntity() == null || getEntity().getId() == null)
			return true;
		return getEntity().getOrganId().equals(getCurrentOrganVO().getId());
	}

//	public String localShenavarEdit() {
//		return getFacesUrl("/hesabtafsili/hesabTafsili.shenavar.LocalEdit.xhtml");
//	}

	List<HesabClassificationEntity> hesabClassifications;

	public List<HesabClassificationEntity> getHesabClassifications() {
		if (hesabClassifications == null) {
			if (getEntity().getLevel() != null) {
				Map<String, Object> localFilter = new HashMap<>();
				localFilter.put("level@eq", getEntity().getLevel());
				localFilter.put("organId@eq", getCurrentOrganVO().getId());
				hesabClassifications = hesabClassificationService.getDataList(localFilter);
			} else
				hesabClassifications = new ArrayList<>();
		}
		return hesabClassifications;
	}

	public void setHesabClassifications(List<HesabClassificationEntity> hesabClassifications) {
		this.hesabClassifications = hesabClassifications;
	}

	public void handleChangelevel(AjaxBehaviorEvent vce) {
//		Integer level= (Integer) ((UIOutput) vce.getSource()).getValue();
//		if(level == null)
//			getEntity().setLevel(null);
		// System.out.println(getEntity().getLevel());
		setHesabClassifications(null);
	}
}