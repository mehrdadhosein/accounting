package ir.serajsamaneh.accounting.hesabgroup;

import ir.serajsamaneh.accounting.enumeration.HesabTypeEnum;
import ir.serajsamaneh.accounting.enumeration.MahyatGroupEnum;
import ir.serajsamaneh.core.util.SerajMessageUtil;

public class HesabGroupEntity extends BaseHesabGroupEntity {
	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
	public HesabGroupEntity() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public HesabGroupEntity(java.lang.Long id) {
		super(id);
	}

	/* [CONSTRUCTOR MARKER END] */

	HesabTypeEnum type;

	public HesabTypeEnum getType() {
		return type;
	}

	public void setType(HesabTypeEnum type) {
		this.type = type;
	}

	private MahyatGroupEnum mahyatGroup;

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
		return SerajMessageUtil.getMessage("HesabGroup_code") + ":" + this.getCode() + " , "
				+ SerajMessageUtil.getMessage("HesabGroup_name") + ":" + this.getName() + " , "
				+ SerajMessageUtil.getMessage("HesabGroup_type") + ":"
				+ SerajMessageUtil.getMessage(this.getType().nameWithClass()) + " , "
				+ SerajMessageUtil.getMessage("HesabGroup_mahyatGroup") + ":"
				+ SerajMessageUtil.getMessage(this.getMahyatGroup().nameWithClass());
	}

}