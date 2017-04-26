package ir.serajsamaneh.accounting.sanadhesabdari;

import ir.serajsamaneh.accounting.enumeration.SanadFunctionEnum;
import ir.serajsamaneh.accounting.enumeration.SanadStateEnum;
import ir.serajsamaneh.core.file.FileEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.enumeration.YesNoEnum;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SanadHesabdariEntity extends BaseSanadHesabdariEntity {
	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
	public SanadHesabdariEntity() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public SanadHesabdariEntity(java.lang.Long id) {
		super(id);
	}

	/* [CONSTRUCTOR MARKER END] */
	private ir.serajsamaneh.core.user.UserEntity tanzimKonnadeSanad;
	private ir.serajsamaneh.core.user.UserEntity taiedKonnadeSanad;
	private ir.serajsamaneh.core.user.UserEntity daeemKonnadeSanad;

	private SanadStateEnum state;
	private SanadFunctionEnum sanadFunction;
	private YesNoEnum deletable;
	public YesNoEnum getDeletable() {
		return deletable;
	}

	public void setDeletable(YesNoEnum deletable) {
		this.deletable = deletable;
	}

	private java.util.List<ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity> sanadHesabdariItem;


	public SanadFunctionEnum getSanadFunction() {
		return sanadFunction;
	}

	public void setSanadFunction(SanadFunctionEnum sanadFunction) {
		this.sanadFunction = sanadFunction;
	}

	public java.util.List<ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity> getSanadHesabdariItem() {
		return sanadHesabdariItem;
	}

	public void setSanadHesabdariItem(
			java.util.List<ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity> sanadHesabdariItem) {
		this.sanadHesabdariItem = sanadHesabdariItem;
	}


	public void addTosanadHesabdariItem (ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity sanadHesabdariItemEntity) {
		if (null == getSanadHesabdariItem()) setSanadHesabdariItem(new java.util.ArrayList<ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity>());
		getSanadHesabdariItem().add(sanadHesabdariItemEntity);
	}
	
	public ir.serajsamaneh.core.user.UserEntity getTanzimKonnadeSanad() {
		return tanzimKonnadeSanad;
	}

	public void setTanzimKonnadeSanad(
			ir.serajsamaneh.core.user.UserEntity tanzimKonnadeSanad) {
		this.tanzimKonnadeSanad = tanzimKonnadeSanad;
	}

	public ir.serajsamaneh.core.user.UserEntity getTaiedKonnadeSanad() {
		return taiedKonnadeSanad;
	}

	public void setTaiedKonnadeSanad(
			ir.serajsamaneh.core.user.UserEntity taiedKonnadeSanad) {
		this.taiedKonnadeSanad = taiedKonnadeSanad;
	}

	public ir.serajsamaneh.core.user.UserEntity getDaeemKonnadeSanad() {
		return daeemKonnadeSanad;
	}

	public void setDaeemKonnadeSanad(
			ir.serajsamaneh.core.user.UserEntity daeemKonnadeSanad) {
		this.daeemKonnadeSanad = daeemKonnadeSanad;
	}

	public String getSanadFunctionName() {
		if (getSanadFunction() != null)
			return SerajMessageUtil.getMessage(getSanadFunction().nameWithClass());
		return "";
	}


	public SanadStateEnum getState() {
		return state;
	}

	public void setState(SanadStateEnum state) {
		this.state = state;
	}

	List<FileEntity> zamimehList;

	public List<FileEntity> getZamimehList() {
		if (zamimehList == null) {
			zamimehList = new ArrayList<FileEntity>();
			if (getZamimeh() != null)
				zamimehList.addAll(getZamimeh());
		}
		return zamimehList;

	}

	public void setZamimehList(List<FileEntity> zamimehList) {
		this.zamimehList = zamimehList;
	}

	@Override
	public String toString() {
		if (getSerial() != null)
			return getSerial().toString();
		else if(getTempSerial() != null)
			return getTempSerial().toString();
		return "";
	}
	
	public Boolean getIsEkhtetamieh(){
		return getSanadFunction().equals(SanadFunctionEnum.EKHTETAMIE);
	}
	
	public Boolean getIsEftetahieh(){
		return getSanadFunction().equals(SanadFunctionEnum.EFTETAHIE);
	}

	public Boolean getIsTemporalSanad(){
		return getSanadFunction().equals(SanadFunctionEnum.BASTAN_HESABHA);
	}
	
	public Boolean getIsSanadDeletable(){
		if(getDeletable()==null)
			return true;
		return getDeletable().equals(YesNoEnum.YES);
	}


	@Override
	public String getCompleteInfo() {
		String tempSerial = getTempSerial()!=null ? getTempSerial().toString() : "";
		String serial = getSerial()!=null ? getSerial().toString() : "";
		String sanadType = getSanadType()!=null ? getSanadType().getDesc() : "";
		String completeInfo = "[" 
				+ SerajMessageUtil.getMessage("SanadHesabdari" + "_"
						+ SanadHesabdariEntity.PROP_TEMP_SERIAL)
						+ " : "
						+ tempSerial
						+ " \n\r"
				+ SerajMessageUtil.getMessage("SanadHesabdari" + "_"
						+ SanadHesabdariEntity.PROP_SERIAL)
						+ " : "
						+ serial
						+ " \n\r"
				+ SerajMessageUtil.getMessage("SanadHesabdari" + "_"
						+ SanadHesabdariEntity.PROP_TARIKH_SANAD)
						+ " : "
						+ getTarikhSanadFA()
						+ " \n\r"
				+ SerajMessageUtil.getMessage("SanadHesabdari" + "_"
						+ SanadHesabdariEntity.PROP_SANAD_TYPE)
						+ ":"
						+ sanadType
						+ " \n\r"
				+ SerajMessageUtil.getMessage("SanadHesabdari" + "_"
						+ SanadHesabdariEntity.PROP_DESCRIPTION)
						+ ":"
						+ getDescription()
						+ " \n\r"
				+ SerajMessageUtil.getMessage("SanadHesabdari" + "_"
						+ SanadHesabdariEntity.PROP_SAAL_MAALI) + ":" + getSaalMaali()
				+ " " + "]";

		return completeInfo;
	}

	public boolean getIsSanadHesabdariManualyDeletable(){
		return !getIsEkhtetamieh() && !getIsEftetahieh()  && !getIsTemporalSanad() && getIsSanadDeletable();		
	}

	public String getBedehkarSumFormatted() {
		if(getBedehkarSum() == null)
			return "";
		return new DecimalFormat("###,###").format(getBedehkarSum());
	}
	
	public String getBestankarSumFormatted() {
		if(getBestankarSum() == null)
			return "";

		return new DecimalFormat("###,###").format(getBestankarSum());
	}
}