package ir.serajsamaneh.accounting.hesabkol;

import ir.serajsamaneh.accounting.enumeration.MahyatKolEnum;
import ir.serajsamaneh.core.util.SerajMessageUtil;




public class HesabKolEntity extends BaseHesabKolEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public HesabKolEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public HesabKolEntity (java.lang.Long id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	@Override
	public String toString() {
		if(getId()!=null)
			return getName()+" ("+getCode()+")";
		return "";
	}
	
	
	private MahyatKolEnum mahyatKol;
	Boolean hidden;
	
	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public MahyatKolEnum getMahyatKol() {
		return mahyatKol;
	}

	public void setMahyatKol(MahyatKolEnum mahyatKol) {
		this.mahyatKol = mahyatKol;
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
		return SerajMessageUtil.getMessage("HesabKol_hesabGroup")+":"+this.getHesabGroup()+" , "+
			   SerajMessageUtil.getMessage("HesabKol_name")+":"+this.getName()+" , "+
			   SerajMessageUtil.getMessage("HesabKol_code")+":"+this.getCode()+" , "+
			   SerajMessageUtil.getMessage("HesabKol_description")+":"+this.getDescription()+" , "+
			   SerajMessageUtil.getMessage("HesabKol_hidden")+":"+this.getHidden()+" , "+
			   SerajMessageUtil.getMessage("HesabKol_mahyatKol")+":"+SerajMessageUtil.getMessage(this.getMahyatKol().nameWithClass());
	}
}