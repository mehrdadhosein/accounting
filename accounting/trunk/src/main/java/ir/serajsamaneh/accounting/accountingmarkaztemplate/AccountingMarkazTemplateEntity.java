package ir.serajsamaneh.accounting.accountingmarkaztemplate;

import ir.serajsamaneh.accounting.enumeration.HesabScopeEnum;
import ir.serajsamaneh.accounting.enumeration.TafsilTypeEnum;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.moeenaccountingmarkaz.MoeenAccountingMarkazEntity;
import ir.serajsamaneh.accounting.moeenaccountingmarkaztemplate.MoeenAccountingMarkazTemplateEntity;

import java.util.ArrayList;
import java.util.List;



public class AccountingMarkazTemplateEntity extends BaseAccountingMarkazTemplateEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public AccountingMarkazTemplateEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public AccountingMarkazTemplateEntity (java.lang.Long id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	List<HesabMoeenTemplateEntity> hesabMoeenList = null;
	public List<HesabMoeenTemplateEntity> getHesabMoeenList(){
		if(hesabMoeenList == null){
			hesabMoeenList = new ArrayList<HesabMoeenTemplateEntity>();
			if(getMoeenAccountingMarkazTemplate()!=null)
				for(MoeenAccountingMarkazTemplateEntity moeenTafsiliEntity : getMoeenAccountingMarkazTemplate())
					hesabMoeenList.add(moeenTafsiliEntity.getHesabMoeenTemplate());
		}
		return hesabMoeenList;
	}
	@Override
	public String toString() {
		if(getId()!=null)
			return getName()+" ("+getCode()+")";
		return "";
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


	public String getNameWithCode() {
		if(getId() != null)
			return getName()+" ("+getCode()+")";
		return "";
	}
}