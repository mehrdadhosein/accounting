package ir.serajsamaneh.accounting.hesabclassification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;

import ir.serajsamaneh.accounting.base.BaseAccountingForm;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity;

public class HesabClassificationForm extends BaseAccountingForm<HesabClassificationEntity, Long> {

	HesabMoeenService hesabMoeenService;
	HesabClassificationService hesabClassificationService;

	public HesabMoeenService getHesabMoeenService() {
		return hesabMoeenService;
	}

	public void setHesabMoeenService(HesabMoeenService hesabMoeenService) {
		this.hesabMoeenService = hesabMoeenService;
	}

	@Override
	protected HesabClassificationService getMyService() {
		return hesabClassificationService;
	}

	public void setHesabClassificationService(HesabClassificationService hesabClassificationService) {
		this.hesabClassificationService = hesabClassificationService;
	}

	public HesabClassificationService getHesabClassificationService() {
		return hesabClassificationService;
	}

	@Override
	public DataModel<HesabClassificationEntity> getLocalDataModel() {
		setSearchAction(true);
		return super.getLocalDataModel();
	}
	public List<SelectItem> getLocalHesabClassification() {

		List<SelectItem> localHesabClassificationList = new ArrayList<SelectItem>();

		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("organ.id@eq", getCurrentUser().getOrgan().getId());
		List<HesabClassificationEntity> dataList = getMyService().getDataList(null, localFilter);
		SelectItem item = new SelectItem(null, "---------");
		localHesabClassificationList.add(item);
		for (HesabClassificationEntity hesabClassificationEntity : dataList) {
			item = new SelectItem(hesabClassificationEntity.getId(), hesabClassificationEntity.getName());
			localHesabClassificationList.add(item);
		}
		return localHesabClassificationList;
	}

	public String localSave() {
		getEntity().setOrgan(getCurrentOrgan());
		save();
		return getLocalViewUrl();
	}

	List<Long> moeenIds;
	List<Long> tafsiliIds;

	public List<Long> getTafsiliIds() {
		if (tafsiliIds == null) {
			tafsiliIds = new ArrayList<Long>();
			Set<HesabTafsiliTemplateEntity> tafsilies = getEntity().getHesabTafsiliTemplate();
			if (tafsilies != null) {
				for (HesabTafsiliTemplateEntity tafsiliEntity : tafsilies) {
					tafsiliIds.add(tafsiliEntity.getId());
				}
			}
		}
		return tafsiliIds;
	}

	public void setTafsiliIds(List<Long> childTafsiliIds) {
		this.tafsiliIds = childTafsiliIds;
	}

	public List<Long> getMoeenIds() {
		if (moeenIds == null) {
			moeenIds = new ArrayList<Long>();
			Set<HesabMoeenTemplateEntity> moeens = getEntity().getHesabMoeenTemplate();
			if (moeens != null) {
				for (HesabMoeenTemplateEntity moeen : moeens) {
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
		getMyService().save(getEntity(), getMoeenIds(), getTafsiliIds());
		return getViewUrl();
	}

	public DataModel<HesabClassificationEntity> getLocalArchiveDataModel() {
		return getLocalDataModel();
	}

	public Boolean getIsCreated() {
		return null;
	}

}