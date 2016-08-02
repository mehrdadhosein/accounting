package ir.serajsamaneh.accounting.hesabkoltemplate;

import ir.serajsamaneh.accounting.enumeration.MahyatKolEnum;
import ir.serajsamaneh.accounting.hesabkoltemplate.BaseHesabKolTemplateEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;



public class HesabKolTemplateEntity extends BaseHesabKolTemplateEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public HesabKolTemplateEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public HesabKolTemplateEntity (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */


/*[CONSTRUCTOR MARKER END]*/

	private Boolean hidden;
	private MahyatKolEnum mahyatKol;

	public MahyatKolEnum getMahyatKol() {
		return mahyatKol;
	}

	public void setMahyatKol(MahyatKolEnum mahyatKol) {
		this.mahyatKol = mahyatKol;
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
	
	@Override
	public String getCompleteInfo() {
		return SerajMessageUtil.getMessage("HesabKolTemplate_hesabGroup")+":"+this.getHesabGroupTemplate()+" , "+
			   SerajMessageUtil.getMessage("HesabKolTemplate_code")+":"+this.getCode()+" , "+
			   SerajMessageUtil.getMessage("HesabKolTemplate_name")+":"+this.getName()+" , "+
			   SerajMessageUtil.getMessage("HesabKolTemplate_description")+":"+this.getDescription()+" , "+
			   SerajMessageUtil.getMessage("HesabKolTemplate_hidden")+":"+this.getHidden()+" , "+
			   SerajMessageUtil.getMessage("HesabKolTemplate_mahyatKol")+":"+SerajMessageUtil.getMessage(this.getMahyatKol().nameWithClass());
	}
}