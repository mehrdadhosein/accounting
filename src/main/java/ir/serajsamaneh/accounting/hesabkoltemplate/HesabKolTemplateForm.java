package ir.serajsamaneh.accounting.hesabkoltemplate;

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
@Named("hesabKolTemplate")
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class HesabKolTemplateForm extends BaseAccountingForm<HesabKolTemplateEntity, Long> {

	@Override
	protected HesabKolTemplateService getMyService() {
		return hesabKolTemplateService;
	}

	HesabKolTemplateService hesabKolTemplateService;

	public void setHesabKolTemplateService(HesabKolTemplateService hesabKolTemplateService) {
		this.hesabKolTemplateService = hesabKolTemplateService;
	}

	public HesabKolTemplateService getHesabKolTemplateService() {
		return hesabKolTemplateService;
	}

	public String localSave() {
		HesabTemplateRelationsUtil.resetKolMoeenTemplateMap(getCurrentOrganVO().getId());
		getMyService().save(entity);
		return getLocalViewUrl();
	}

	public DataModel<HesabKolTemplateEntity> getLocalArchiveDataModel() {
		return getLocalDataModel();
	}

	public Boolean getIsCreated() {
		return null;
	}

	public Map<Long, List<ListOrderedMap<String, Object>>> getKolMoeenTemplateMap() {
		try {
			return HesabTemplateRelationsUtil.getKolMoeenTemplateMap(getCurrentUserActiveSaalMaali().getOrganId(),
					getCurrentOrganVO().getTopParentCode(), getCurrentOrganVO().getTopOrgansIdList());
		} catch (NoSaalMaaliFoundException e) {
			return new HashMap<Long, List<ListOrderedMap<String, Object>>>();
		}
	}

	@Override
	public List<? extends BaseEntity> getJsonList(String property, String term, boolean all,
			Map<String, String> params) {

		String isHierarchical = params.get("isHierarchical");

		params.put("isLocal", "false");

		if (isHierarchical != null && isHierarchical.equals("true")) {
			List<Long> topOrganList = getCurrentOrganVO().getTopOrgansIdList();
			getFilter().put("organId@in", topOrganList);

//			this.getFilter().put("organ.code@startlk", getCurrentUserActiveSaalMaali().getOrgan().getCode());
//			this.getFilter().put("saalMaali.id@eq",getCurrentUserActiveSaalMaali().getId());
			params.put("isLocal", "false");
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