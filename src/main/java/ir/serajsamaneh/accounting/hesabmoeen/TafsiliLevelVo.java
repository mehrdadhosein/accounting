package ir.serajsamaneh.accounting.hesabmoeen;

import java.util.List;

import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.core.base.BaseValueObject;

public class TafsiliLevelVo extends BaseValueObject {

	Integer level;
	String hesabTafsiliListStr;
	String hesabTafsiliListIds = "";

	public TafsiliLevelVo(Integer level, List<HesabTafsiliEntity> tafsiiList) {
		setLevel(level);
		for (HesabTafsiliEntity hesabTafsiliEntity : tafsiiList)
			hesabTafsiliListIds = hesabTafsiliListIds + hesabTafsiliEntity.getId() + ',';
		hesabTafsiliListIds = hesabTafsiliListIds.substring(0, hesabTafsiliListIds.length());
		setHesabTafsiliListStr(tafsiiList.toString());
	}

	public TafsiliLevelVo() {
		// TODO Auto-generated constructor stub
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getHesabTafsiliListStr() {
		return hesabTafsiliListStr;
	}

	public void setHesabTafsiliListStr(String hesabTafsiliListStr) {
		this.hesabTafsiliListStr = hesabTafsiliListStr;
	}

	public String getHesabTafsiliListIds() {
		return hesabTafsiliListIds;
	}

	public void setHesabTafsiliListIds(String hesabTafsiliListIds) {
		this.hesabTafsiliListIds = hesabTafsiliListIds;
	}

}
