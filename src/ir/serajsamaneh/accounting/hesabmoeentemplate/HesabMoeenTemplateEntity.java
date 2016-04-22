package ir.serajsamaneh.accounting.hesabmoeentemplate;

import ir.serajsamaneh.accounting.enumeration.HesabScopeEnum;
import ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity;
import ir.serajsamaneh.accounting.moeentafsili.MoeenTafsiliEntity;
import ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;

import java.util.ArrayList;
import java.util.List;



public class HesabMoeenTemplateEntity extends BaseHesabMoeenTemplateEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public HesabMoeenTemplateEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public HesabMoeenTemplateEntity (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */


/*[CONSTRUCTOR MARKER END]*/

	Boolean hidden;
	private HesabScopeEnum scope;
	
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


	List<HesabTafsiliTemplateEntity> tafsiliTemplateList = null;
	public List<HesabTafsiliTemplateEntity> getTafsiliTemplateList(){
		if(tafsiliTemplateList == null){
			tafsiliTemplateList = new ArrayList<HesabTafsiliTemplateEntity>();
			if(getMoeenTafsiliTemplate()!=null)
				for(MoeenTafsiliTemplateEntity moeenTafsiliTemplateEntity : getMoeenTafsiliTemplate())
					tafsiliTemplateList.add(moeenTafsiliTemplateEntity.getHesabTafsiliTemplate());
		}
		return tafsiliTemplateList;
	}
	
	@Override
	public String getCompleteInfo() {
		return SerajMessageUtil.getMessage("HesabMoeenTemplate_hesabKolTemplate")+":"+this.getHesabKolTemplate()+" , "+
			   SerajMessageUtil.getMessage("HesabMoeenTemplate_code")+":"+this.getCode()+" , "+
			   SerajMessageUtil.getMessage("HesabMoeenTemplate_name")+":"+this.getName()+" , "+
			   SerajMessageUtil.getMessage("HesabMoeenTemplate_description")+":"+this.getDescription()+" , "+
			   SerajMessageUtil.getMessage("HesabMoeenTemplate_hidden")+":"+this.getHidden()+" , "+
			   SerajMessageUtil.getMessage("HesabMoeenTemplate_mahyatMoeen")+":"+this.getMahyatMoeen()+" , "+
			   SerajMessageUtil.getMessage("HesabMoeenTemplate_scope")+":"+SerajMessageUtil.getMessage(this.getScope().name());
	}
}