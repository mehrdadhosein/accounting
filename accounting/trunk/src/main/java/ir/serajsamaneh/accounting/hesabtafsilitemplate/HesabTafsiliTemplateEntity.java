package ir.serajsamaneh.accounting.hesabtafsilitemplate;

import ir.serajsamaneh.accounting.enumeration.HesabScopeEnum;
import ir.serajsamaneh.accounting.enumeration.TafsilTypeEnum;
import ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity;
import ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity;

import java.util.ArrayList;
import java.util.List;



public class HesabTafsiliTemplateEntity extends BaseHesabTafsiliTemplateEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public HesabTafsiliTemplateEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public HesabTafsiliTemplateEntity (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */

/*[CONSTRUCTOR MARKER END]*/

	Boolean hidden;
	private HesabScopeEnum scope;
	private TafsilTypeEnum tafsilType;

	public TafsilTypeEnum getTafsilType() {
		return tafsilType;
	}

	public void setTafsilType(TafsilTypeEnum tafsilType) {
		this.tafsilType = tafsilType;
	}

	public HesabScopeEnum getScope() {
		return scope;
	}

	public void setScope(HesabScopeEnum scope) {
		this.scope = scope;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	@Override
	public String toString() {
		if(getId()!=null)
			return getName()+" ("+getCode()+")";
		return "";

	}
	
	List<HesabMoeenTemplateEntity> hesabMoeenTemplateList = null;
	public List<HesabMoeenTemplateEntity> getHesabMoeenTemplateList(){
		if(hesabMoeenTemplateList == null){
			hesabMoeenTemplateList = new ArrayList<HesabMoeenTemplateEntity>();
			if(getMoeenTafsiliTemplate()!=null)
				for(MoeenTafsiliTemplateEntity moeenTafsiliEntity : getMoeenTafsiliTemplate())
					hesabMoeenTemplateList.add(moeenTafsiliEntity.getHesabMoeenTemplate());
		}
		return hesabMoeenTemplateList;
	}
}