package ir.serajsamaneh.accounting.hesabmoeentemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.DataModel;
import javax.inject.Named;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import ir.serajsamaneh.accounting.base.BaseAccountingForm;
import ir.serajsamaneh.accounting.exception.NoSaalMaaliFoundException;
import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.erpcore.util.HesabTemplateRelationsUtil;
@Named("hesabMoeenTemplate")
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class HesabMoeenTemplateForm extends BaseAccountingForm<HesabMoeenTemplateEntity, Long> {

	@Override
	protected HesabMoeenTemplateService getMyService() {
		return hesabMoeenTemplateService;
	}

	HesabMoeenTemplateService hesabMoeenTemplateService;

	public void setHesabMoeenTemplateService(HesabMoeenTemplateService hesabMoeenTemplateService) {
		this.hesabMoeenTemplateService = hesabMoeenTemplateService;
	}

	public HesabMoeenTemplateService getHesabMoeenTemplateService() {
		return hesabMoeenTemplateService;
	}

	public String localSave() {
		HesabTemplateRelationsUtil.resetMoeenKolTemplateMap(getCurrentOrganVO().getId());
		HesabTemplateRelationsUtil.resetmoeenTafsiliTemplateMap(getCurrentOrganVO().getId());
		HesabTemplateRelationsUtil.resetAccountingMarkazTemplateMap(getCurrentOrganVO().getId());
		getMyService().save(entity);
		return getLocalViewUrl();
	}

	public DataModel<HesabMoeenTemplateEntity> getLocalArchiveDataModel() {
		return getLocalDataModel();
	}

	public Boolean getIsCreated() {
		return null;
	}

//	String moeenTafsiliInstanceIds;
//
//	public String getMoeenTafsiliInstanceIds() {
//		return moeenTafsiliInstanceIds;
//	}
//
//	public void setMoeenTafsiliInstanceIds(String moeenTafsiliInstanceIds) {
//		this.moeenTafsiliInstanceIds = moeenTafsiliInstanceIds;
//	}

	public Map<Long, ListOrderedMap<String, Object>> getMoeenKolTemplateMap() {
		try {
			return HesabTemplateRelationsUtil.getMoeenKolTemplateMap(getCurrentUserActiveSaalMaali().getOrganId(),
					getCurrentOrganVO().getTopOrgansIdList());
		} catch (NoSaalMaaliFoundException e) {
			return new HashMap<>();
		}
	}

	public Map<Long, List<ListOrderedMap<String, Object>>> getMoeenTafsiliTemplateMap() {
		try {
			return HesabTemplateRelationsUtil.getMoeenTafsiliTemplateMap(getCurrentUserActiveSaalMaali().getOrganId(),
					getCurrentOrganVO().getTopOrgansIdList());
		} catch (NoSaalMaaliFoundException e) {
			return new HashMap<>();
		}
	}

	public Map<Long, List<ListOrderedMap<String, Object>>> getAccountingMarkazTemplateMap() {
		try {
			return HesabTemplateRelationsUtil
					.getAccountingMarkazTemplateMap(getCurrentUserActiveSaalMaali().getOrganId());
		} catch (NoSaalMaaliFoundException e) {
			return new HashMap<Long, List<ListOrderedMap<String, Object>>>();
		}

	}

	@Override
	public List<? extends BaseEntity> getJsonList(String property, String term, boolean all,
			Map<String, String> params) {

		try {
			String isHierarchical = params.get("isHierarchical");

			params.put("isLocal", "false");

			if (isHierarchical != null && isHierarchical.equals("true")) {

				List<Long> topOrganList = getCurrentOrganVO().getTopOrgansIdList();
				getFilter().put("organId@in", topOrganList);

//				getFilter().put("organ.code@startlk", getCurrentUserActiveSaalMaali().getOrgan().getCode());
				// getFilter().put("saalMaali.id@eq",getCurrentUserActiveSaalMaali().getId());
				params.put("isLocal", "false");
			}
		} catch (NoSaalMaaliFoundException e) {
			return new ArrayList<>();
		}

		return super.getJsonList(property, term, all, params);
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
}