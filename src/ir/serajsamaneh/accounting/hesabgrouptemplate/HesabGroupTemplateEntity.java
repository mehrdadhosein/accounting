package ir.serajsamaneh.accounting.hesabgrouptemplate;

import ir.serajsamaneh.accounting.enumeration.HesabTypeEnum;
import ir.serajsamaneh.accounting.enumeration.MahyatGroupEnum;
import ir.serajsamaneh.core.util.SerajMessageUtil;



public class HesabGroupTemplateEntity extends BaseHesabGroupTemplateEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public HesabGroupTemplateEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public HesabGroupTemplateEntity (java.lang.Long id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	MahyatGroupEnum mahyatGroup;
	HesabTypeEnum type;

	public HesabTypeEnum getType() {
		return type;
	}

	public void setType(HesabTypeEnum type) {
		this.type = type;
	}

	public MahyatGroupEnum getMahyatGroup() {
		return mahyatGroup;
	}

	public void setMahyatGroup(MahyatGroupEnum mahyatGroup) {
		this.mahyatGroup = mahyatGroup;
	}

	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public String getCompleteInfo() {
		return SerajMessageUtil.getMessage("HesabGroupTemplate_name")+":"+this.getName()+" , "+
			   SerajMessageUtil.getMessage("HesabGroupTemplate_code")+":"+this.getCode()+" , "+
			   SerajMessageUtil.getMessage("HesabGroup_type")+":"+SerajMessageUtil.getMessage(this.getType().nameWithClass())+" , "+
			   SerajMessageUtil.getMessage("HesabGroup_mahyatGroup")+":"+SerajMessageUtil.getMessage(this.getMahyatGroup().nameWithClass());
	}
}