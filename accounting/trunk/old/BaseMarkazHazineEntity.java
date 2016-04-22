package ir.serajsamaneh.accounting.markazhazine;

import ir.serajsamaneh.core.base.BaseEntity;

import java.io.Serializable;

import org.jbpm.pvm.internal.jobexecutor.GetNextDueDateCmd;


/**
 * This is an object that contains data related to the tb_markaz_hazine table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_markaz_hazine"
 */

public abstract class BaseMarkazHazineEntity  extends BaseEntity<Long> implements Serializable {

	public static String REF = "MarkazHazineEntity";
	public static String PROP_ORGAN = "organ";
	public static String PROP_HESAB_TAFSILI = "hesabTafsili";
	public static String PROP_ONVAN = "onvan";
	public static String PROP_ID = "id";
	public static String PROP_MARKAZTYPE = "markaztype";


	// constructors
	public BaseMarkazHazineEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMarkazHazineEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	
	private java.lang.String onvan;

	// many to one
	private ir.serajsamaneh.core.organ.OrganEntity organ;
	private ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity hesabTafsili;
	
	
	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="markaz_hazine_id"
     */
	public java.lang.Long getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Long id) {
		this.id = id;
		setID(id);
		this.hashCode = Integer.MIN_VALUE;
	}







	/**
	 * Return the value associated with the column: onvan
	 */
	public java.lang.String getOnvan () {
		return onvan;
	}

	/**
	 * Set the value related to the column: onvan
	 * @param onvan the onvan value
	 */
	public void setOnvan (java.lang.String onvan) {
		this.onvan = onvan;
	}



	/**
	 * Return the value associated with the column: organ_id
	 */
	public ir.serajsamaneh.core.organ.OrganEntity getOrgan () {
		return organ;
	}

	/**
	 * Set the value related to the column: organ_id
	 * @param organ the organ_id value
	 */
	public void setOrgan (ir.serajsamaneh.core.organ.OrganEntity organ) {
		this.organ = organ;
	}



	/**
	 * Return the value associated with the column: hesab_tafsili_id
	 */
	public ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity getHesabTafsili () {
		return hesabTafsili;
	}

	/**
	 * Set the value related to the column: hesab_tafsili_id
	 * @param hesabTafsili the hesab_tafsili_id value
	 */
	public void setHesabTafsili (ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity hesabTafsili) {
		this.hesabTafsili = hesabTafsili;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof ir.serajsamaneh.accounting.markazhazine.MarkazHazineEntity)) return false;
		else {
			ir.serajsamaneh.accounting.markazhazine.MarkazHazineEntity markazHazineEntity = (ir.serajsamaneh.accounting.markazhazine.MarkazHazineEntity) obj;
			if (null == this.getId() || null == markazHazineEntity.getId()) return false;
			else return (this.getId().equals(markazHazineEntity.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}


	public String toString () {
		if(getOnvan()!=null)
			return getOnvan();
		return "";
	}


}