package ir.serajsamaneh.accounting.accountingmarkaz;

import java.util.ArrayList;
import java.util.List;

import ir.serajsamaneh.accounting.enumeration.HesabScopeEnum;
import ir.serajsamaneh.accounting.enumeration.TafsilTypeEnum;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.moeenaccountingmarkaz.MoeenAccountingMarkazEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;



public class AccountingMarkazEntity extends BaseAccountingMarkazEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public AccountingMarkazEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public AccountingMarkazEntity (java.lang.Long id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	List<HesabMoeenEntity> hesabMoeenList = null;
	public List<HesabMoeenEntity> getHesabMoeenList(){
		if(hesabMoeenList == null){
			hesabMoeenList = new ArrayList<HesabMoeenEntity>();
			if(getMoeenAccountingMarkaz()!=null)
				for(MoeenAccountingMarkazEntity moeenTafsiliEntity : getMoeenAccountingMarkaz())
					hesabMoeenList.add(moeenTafsiliEntity.getHesabMoeen());
		}
		return hesabMoeenList;
	}
	@Override
	public String toString() {
		return getName();
	}
	
	private TafsilTypeEnum tafsilType;
	private HesabScopeEnum scope;
	private Boolean hidden;


	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public HesabScopeEnum getScope() {
		return scope;
	}

	public void setScope(HesabScopeEnum scope) {
		this.scope = scope;
	}

	public TafsilTypeEnum getTafsilType() {
		return tafsilType;
	}

	public void setTafsilType(TafsilTypeEnum tafsilType) {
		this.tafsilType = tafsilType;
	}
	

	public void increaseBedehar(Double value){
		setBedehkar(getBedehkar()+value);
	}
	
	public void increaseBestankar(Double value){
		setBestankr(getBestankr()+value);
	}

	public String getNameWithCode() {
		if(getId() != null)
			return getName()+" ("+getCode()+")";
		return "";
	}
	
	@Override
	public String getCompleteInfo() {
		return SerajMessageUtil.getMessage("AccountingMarkaz_code")+":"+this.getCode()+" , "+
			   SerajMessageUtil.getMessage("AccountingMarkaz_name")+":"+this.getName()+" , "+
			   SerajMessageUtil.getMessage("AccountingMarkaz_description")+":"+this.getDescription()+" , "+
			   SerajMessageUtil.getMessage("AccountingMarkaz_moeens")+":"+this.getHesabMoeenList()+" , "+
			   SerajMessageUtil.getMessage("AccountingMarkaz_childs")+":"+this.getChilds()+" , "+
			   SerajMessageUtil.getMessage("AccountingMarkaz_hidden")+":"+this.getHidden();
	}
}