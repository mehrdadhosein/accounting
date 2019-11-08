package ir.serajsamaneh.accounting.month;

import java.io.Serializable;

import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.organ.OrganEntity;
import serajcomponent.DateConverter;
import serajcomponent.SerajDateTimePickerType;

import java.util.HashSet;



/**
 * This is an object that contains data related to the tb_month table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_month"
 */

public abstract class BaseMonthEntity  extends BaseEntity<Long>   implements Serializable {

	public static String REF = "MonthEntity";
	public static String PROP_NAME = "name";
	public static String PROP_DESCRIPTION = "description";
	public static String PROP_END_DATE = "endDate";
	public static String PROP_START_DATE = "startDate";
	public static String PROP_ID = "id";
	public static String PROP_SAAL_MAALI = "saalMaali";
	public static String PROP_MONTH = "month";


	// constructors
	public BaseMonthEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMonthEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseMonthEntity (
		java.lang.Long id,
		ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity saalMaali) {

		this.setId(id);
		this.setSaalMaali(saalMaali);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String name;
	private java.lang.String description;
	private java.util.Date startDate;
	private java.util.Date endDate;
	private Integer radif;




	// many to one
	private ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity saalMaali;
	private OrganEntity organ;



	public OrganEntity getOrgan() {
		return organ;
	}

	public void setOrgan(OrganEntity organ) {
		this.organ = organ;
	}

	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="month_id"
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


	public Integer getRadif() {
		return radif;
	}

	public void setRadif(Integer radif) {
		this.radif = radif;
	}

 
	/**
	 * Return the value associated with the column: name
	 */
	public java.lang.String getName () {
		return name;
	}

	/**
	 * Set the value related to the column: name
	 * @param name the name value
	 */
	public void setName (java.lang.String name) {
		this.name = name;
	}



 
	/**
	 * Return the value associated with the column: description
	 */
	public java.lang.String getDescription () {
		return description;
	}

	/**
	 * Set the value related to the column: description
	 * @param description the description value
	 */
	public void setDescription (java.lang.String description) {
		this.description = description;
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
		this.startDate = startDate;
	}



	public String  getStartDateFA() {
			if(startDate == null)
			return null;
			return DateConverter.toShamsiDate(startDate,SerajDateTimePickerType.Date );
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
			return DateConverter.toShamsiDate(endDate,SerajDateTimePickerType.Date );
	}

	/**
	 * Return the value associated with the column: saal_maali_id
	 */
	public ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity getSaalMaali () {
		return saalMaali;
	}

	/**
	 * Set the value related to the column: saal_maali_id
	 * @param saalMaali the saal_maali_id value
	 */
	public void setSaalMaali (ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity saalMaali) {
		this.saalMaali = saalMaali;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof ir.serajsamaneh.accounting.month.MonthEntity)) return false;
		else {
			ir.serajsamaneh.accounting.month.MonthEntity monthEntity = (ir.serajsamaneh.accounting.month.MonthEntity) obj;
			if (null == this.getId() || null == monthEntity.getId()) return false;
			else return (this.getId().equals(monthEntity.getId()));
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