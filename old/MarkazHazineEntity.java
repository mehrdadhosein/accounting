package ir.serajsamaneh.accounting.markazhazine;

import ir.serajsamaneh.accounting.enumeration.MarkazhazineType;
import ir.serajsamaneh.accounting.markazhazine.BaseMarkazHazineEntity;

public class MarkazHazineEntity extends BaseMarkazHazineEntity {
	private static final long serialVersionUID = 1L;

	private MarkazhazineType markaztype;

	public MarkazhazineType getMarkaztype() {
		return markaztype;
	}

	public void setMarkaztype(MarkazhazineType markaztype) {
		this.markaztype = markaztype;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public MarkazHazineEntity() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public MarkazHazineEntity(java.lang.Long id) {
		super(id);
	}

	/* [CONSTRUCTOR MARKER END] */

}