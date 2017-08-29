package ir.serajsamaneh.accounting.hesabmoeen;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity;
import ir.serajsamaneh.accounting.enumeration.HesabScopeEnum;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.moeenaccountingmarkaz.MoeenAccountingMarkazEntity;
import ir.serajsamaneh.accounting.moeentafsili.MoeenTafsiliEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;

import java.util.ArrayList;
import java.util.List;




public class HesabMoeenEntity extends BaseHesabMoeenEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public HesabMoeenEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public HesabMoeenEntity (java.lang.Long id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	@Override
	public String toString() {
		if(getId()!=null)
			return getName()+" ("+getCode()+")";
		return "";
	}

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

	List<HesabTafsiliEntity> tafsiliList = null;
	public List<HesabTafsiliEntity> getTafsiliList(){
		if(tafsiliList == null){
			tafsiliList = new ArrayList<HesabTafsiliEntity>();
			if(getMoeenTafsili()!=null)
				for(MoeenTafsiliEntity moeenTafsiliEntity : getMoeenTafsili())
					tafsiliList.add(moeenTafsiliEntity.getHesabTafsili());
		}
		return tafsiliList;
	}

	
	List<AccountingMarkazEntity> accountingMarkazList = null;
	public List<AccountingMarkazEntity> getAccountingMarkazList(){
		if(accountingMarkazList == null){
			accountingMarkazList = new ArrayList<AccountingMarkazEntity>();
			if(getMoeenAccountingMarkaz()!=null)
				for(MoeenAccountingMarkazEntity moeenAccountingMarkazEntity : getMoeenAccountingMarkaz())
					accountingMarkazList.add(moeenAccountingMarkazEntity.getAccountingMarkaz());
		}
		return accountingMarkazList;
	}
	

	public void increaseBedehar(Double value){
		setBedehkar(getBedehkar()+value);
	}
	
	public void increaseBestankar(Double value){
		setBestankr(getBestankr()+value);
	}
	
	
	public String getNameWithCode(){
		if(getId() != null)
			return getName()+" ("+getCode()+")";
		return "";
	}
	
	@Override
	public String getCompleteInfo() {
		String hesabMoeenName = SerajMessageUtil.getMessage("HesabMoeen_name")+":"+this.getName()+" , ";
		String hesabKolName = SerajMessageUtil.getMessage("HesabMoeen_hesabKol")+":"+this.getHesabKol()+" , ";
		String hesabMoeenCode = SerajMessageUtil.getMessage("HesabMoeen_code")+":"+this.getCode()+" , ";
		String desc = this.getDescription()!=null ? SerajMessageUtil.getMessage("HesabMoeen_description")+":"+this.getDescription()+" , " : "";
//		String hidden = SerajMessageUtil.getMessage("HesabMoeen_hidden")+":"+this.getHidden();
		String message= hesabMoeenName+hesabKolName+hesabMoeenCode+desc+   hidden;
		if (!this.getTafsiliList().isEmpty()) {
			message+=",[";
			for (HesabTafsiliEntity tafsili : getTafsiliList()) {
				message+=tafsili.toString()+",";
			}
			message+="]";
		}
		return message;
	}
}