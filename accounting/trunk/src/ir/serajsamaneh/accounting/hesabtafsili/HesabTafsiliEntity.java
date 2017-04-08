package ir.serajsamaneh.accounting.hesabtafsili;

import ir.serajsamaneh.accounting.enumeration.HesabScopeEnum;
import ir.serajsamaneh.accounting.enumeration.TafsilTypeEnum;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.moeentafsili.MoeenTafsiliEntity;

import java.util.ArrayList;
import java.util.List;



public class HesabTafsiliEntity extends BaseHesabTafsiliEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public HesabTafsiliEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public HesabTafsiliEntity (java.lang.Long id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	List<HesabMoeenEntity> hesabMoeenList = null;
	public List<HesabMoeenEntity> getHesabMoeenList(){
//		if(hesabMoeenList == null){
			hesabMoeenList = new ArrayList<HesabMoeenEntity>();
			if(getMoeenTafsili()!=null)
				for(MoeenTafsiliEntity moeenTafsiliEntity : getMoeenTafsili())
					hesabMoeenList.add(moeenTafsiliEntity.getHesabMoeen());
//		}
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
}