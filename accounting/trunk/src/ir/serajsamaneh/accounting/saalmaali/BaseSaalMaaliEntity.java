package ir.serajsamaneh.accounting.saalmaali;

import java.io.Serializable;
import ir.serajsamaneh.core.base.BaseEntity;
import serajcomponent.DateConverter;
import serajcomponent.SerajDateTimePickerType;

import java.util.HashSet;



/**
 * This is an object that contains data related to the tb_saal_maali table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_saal_maali"
 */

public abstract class BaseSaalMaaliEntity  extends BaseEntity<Long>   implements Serializable {

	public static String REF = "SaalMaaliEntity";
	public static String PROP_END_DATE = "endDate";
	public static String PROP_ORGAN = "organ";
	public static String PROP_START_DATE = "startDate";
	public static String PROP_IS_ACTIVE = "isActive";
	public static String PROP_ID = "id";
	public static String PROP_SAAL = "saal";
	public static String PROP_CURRENT = "current";


	// constructors
	public BaseSaalMaaliEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseSaalMaaliEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseSaalMaaliEntity (
		java.lang.Long id,
		ir.serajsamaneh.core.organ.OrganEntity organ) {

		this.setId(id);
		this.setOrgan(organ);
		initialize();
	}


	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.Integer saal;
	private java.util.Date startDate;
	private java.util.Date endDate;

	// many to one
	private ir.serajsamaneh.core.organ.OrganEntity organ;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="saal_maali_id"
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
	 * Return the value associated with the column: saal
	 */
	public java.lang.Integer getSaal () {
		return saal;
	}

	/**
	 * Set the value related to the column: saal
	 * @param saal the saal value
	 */
	public void setSaal (java.lang.Integer saal) {
		this.saal = saal;
	}



 
	/**
	 * Return the value associated with the column: start_date
	 */
	public java.util.Date getStartDate () {
		return startDate;
	}

	/**
	 * Set the value related to the column: start_date
	 * @param startDate the start_date value
	 */
	public void setStartDate (java.util.Date startDate) {
		
		this.startDate =startDate; 
		
	}



	public String  getStartDateFA() {
			if(startDate == null)
			return null;
			return DateConverter.toShamsiDate(startDate,SerajDateTimePickerType.DateHorMin );
	}
 
	/**
	 * Return the value associated with the column: end_date
	 */
	public java.util.Date getEndDate () {
		return endDate;
	}

	/**
	 * Set the value related to the column: end_date
	 * @param endDate the end_date value
	 */
	public void setEndDate (java.util.Date endDate) {
		this.endDate = endDate;
	}



	public String  getEndDateFA() {
			if(endDate == null)
			return null;
			return DateConverter.toShamsiDate(endDate,SerajDateTimePickerType.DateHorMin );
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




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity)) return false;
		else {
			ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity saalMaaliEntity = (ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity) obj;
			if (null == this.getId() || null == saalMaaliEntity.getId()) return false;
			else return (this.getId().equals(saalMaaliEntity.getId()));
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
		return super.toString();
	}


}