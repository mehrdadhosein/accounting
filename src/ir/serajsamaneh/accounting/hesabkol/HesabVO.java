package ir.serajsamaneh.accounting.hesabkol;

import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;

import java.util.ArrayList;
import java.util.List;

public class HesabVO {

	private java.lang.String entityId;
	private java.lang.String code;
	private java.lang.String name;
	private java.lang.String description;
	private java.lang.Double bedehkar;
	private java.lang.Double bestankr;
	private java.lang.String hesabType;
	private java.lang.String icon;
	Integer saalMaali;
	private HesabVO parent;
	private List<HesabVO> childs = new ArrayList<HesabVO>();
	
	public HesabVO(HesabKolEntity hesabKolEntity, String hesabType, String icon) {
		this.entityId = hesabKolEntity.getId().toString();
		this.code = hesabKolEntity.getCode();
		this.name=hesabKolEntity.getName();
		this.description=hesabKolEntity.getDescription();
		this.bedehkar=hesabKolEntity.getBedehkar();
		this.bestankr=hesabKolEntity.getBestankr();
		this.hesabType=hesabType;
		this.icon = icon;
		this.saalMaali=hesabKolEntity.getSaalMaali().getSaal();
	}
	public HesabVO(HesabMoeenEntity hesabMoeenEntity, String hesabType, String icon) {
		this.entityId = hesabMoeenEntity.getId().toString();
		this.code = hesabMoeenEntity.getCode();
		this.name=hesabMoeenEntity.getName();
		this.description=hesabMoeenEntity.getDescription();
		this.bedehkar=hesabMoeenEntity.getBedehkar();
		this.bestankr=hesabMoeenEntity.getBestankr();
		this.hesabType=hesabType;
		this.icon = icon;
		this.saalMaali=hesabMoeenEntity.getSaalMaali().getSaal();
	}
	public HesabVO(HesabTafsiliEntity hesabTafsili, String hesabType, String icon) {
		this.entityId = hesabTafsili.getId().toString();
		this.code = hesabTafsili.getCode();
		this.name=hesabTafsili.getName();
		this.description=hesabTafsili.getDescription();
		this.bedehkar=hesabTafsili.getBedehkar();
		this.bestankr=hesabTafsili.getBestankr();
		this.hesabType=hesabType;
		this.icon = icon;
		this.saalMaali=hesabTafsili.getSaalMaali().getSaal();
	}
	public java.lang.String getEntityId() {
		return entityId;
	}
	public void setEntityId(java.lang.String entityId) {
		this.entityId = entityId;
	}
	public java.lang.String getCode() {
		return code;
	}
	public void setCode(java.lang.String code) {
		this.code = code;
	}
	public java.lang.String getName() {
		return name;
	}
	public void setName(java.lang.String name) {
		this.name = name;
	}
	public java.lang.String getDescription() {
		return description;
	}
	public void setDescription(java.lang.String description) {
		this.description = description;
	}
	public java.lang.Double getBedehkar() {
		return bedehkar;
	}
	public void setBedehkar(java.lang.Double bedehkar) {
		this.bedehkar = bedehkar;
	}
	public java.lang.Double getBestankr() {
		return bestankr;
	}
	public void setBestankr(java.lang.Double bestankr) {
		this.bestankr = bestankr;
	}
	public java.lang.String getHesabType() {
		return hesabType;
	}
	public void setHesabType(java.lang.String hesabType) {
		this.hesabType = hesabType;
	}
	public HesabVO getParent() {
		return parent;
	}
	public void setParent(HesabVO parent) {
		this.parent = parent;
	}
	public List<HesabVO> getChilds() {
		return childs;
	}
	public void setChilds(List<HesabVO> childs) {
		this.childs = childs;
	}
	public java.lang.String getIcon() {
		return icon;
	}
	public void setIcon(java.lang.String icon) {
		this.icon = icon;
	}
	public Integer getSaalMaali() {
		return saalMaali;
	}
	public void setSaalMaali(Integer saalMaali) {
		this.saalMaali = saalMaali;
	}

}
