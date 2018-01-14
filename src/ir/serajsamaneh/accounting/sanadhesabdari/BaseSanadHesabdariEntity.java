package ir.serajsamaneh.accounting.sanadhesabdari;

import java.io.Serializable;

import ir.serajsamaneh.accounting.sanadtype.SanadTypeEntity;
import ir.serajsamaneh.core.base.BaseEntity;
import serajcomponent.DateConverter;
import serajcomponent.SerajDateTimePickerType;



/**
 * This is an object that contains data related to the tb_sanad_hesabdari table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_sanad_hesabdari"
 */

public abstract class BaseSanadHesabdariEntity  extends BaseEntity<Long>   implements Serializable {

	public static String REF = "SanadHesabdariEntity";
	public static String PROP_TEMP_SERIAL = "tempSerial";
	public static String PROP_DESCRIPTION = "description";
	public static String PROP_SERIAL = "serial";
	public static String PROP_ORGAN = "organ";
	public static String PROP_SANAD_TYPE = "sanadType";
	public static String PROP_STATE = "state";
	public static String PROP_TARIKH_SANAD = "tarikhSanad";
	public static String PROP_ID = "id";
	public static String PROP_SAAL_MAALI = "saalMaali";
	public static String PROP_ATF_NUMBER = "atfNumber";
	public static String PROP_FARIE_NUMBER = "farieNumber";
	public static String PROP_SANAD_FUNCTION = "sanadFunction";


	// constructors
	public BaseSanadHesabdariEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseSanadHesabdariEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseSanadHesabdariEntity (
		java.lang.Long id,
		ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity saalMaali,
		ir.serajsamaneh.core.organ.OrganEntity organ) {

		this.setId(id);
		this.setSaalMaali(saalMaali);
		this.setOrgan(organ);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.Long serial;
	private java.lang.String description;
	private java.util.Date tarikhSanad;
	private java.lang.Long tempSerial;
	private java.lang.Long atfNumber;
	private java.lang.Long farieNumber;

	Double bedehkarSum;
	Double bestankarSum;
	// many to one
	private ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity saalMaali;
	private ir.serajsamaneh.core.organ.OrganEntity organ;
	
	SanadTypeEntity sanadType;

	// collections
	private java.util.Set<ir.serajsamaneh.core.file.FileEntity> zamimeh;



	public SanadTypeEntity getSanadType() {
		return sanadType;
	}

	public void setSanadType(SanadTypeEntity sanadType) {
		this.sanadType = sanadType;
	}

	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="sanad_hesabdari_id"
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


	public Double getBedehkarSum() {
		return bedehkarSum;
	}

	public void setBedehkarSum(Double bedehkarSum) {
		this.bedehkarSum = bedehkarSum;
	}

	public Double getBestankarSum() {
		return bestankarSum;
	}

	public void setBestankarSum(Double bestankarSum) {
		this.bestankarSum = bestankarSum;
	}

	/**
	 * Return the value associated with the column: serial
	 */
	public java.lang.Long getSerial () {
		return serial;
	}

	/**
	 * Set the value related to the column: serial
	 * @param serial the serial value
	 */
	public void setSerial (java.lang.Long serial) {
		this.serial = serial;
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
	 * Return the value associated with the column: tarikh_sanad
	 */
	public java.util.Date getTarikhSanad () {
		return tarikhSanad;
	}

	/**
	 * Set the value related to the column: tarikh_sanad
	 * @param tarikhSanad the tarikh_sanad value
	 */
	public void setTarikhSanad (java.util.Date tarikhSanad) {
		this.tarikhSanad = tarikhSanad;
	}



	public String  getTarikhSanadFA() {
			if(tarikhSanad == null)
			return null;
			return DateConverter.toShamsiDate(tarikhSanad,SerajDateTimePickerType.Date );
	}

	/**
	 * Return the value associated with the column: temp_serial
	 */
	public java.lang.Long getTempSerial () {
		return tempSerial;
	}

	/**
	 * Set the value related to the column: temp_serial
	 * @param tempSerial the temp_serial value
	 */
	public void setTempSerial (java.lang.Long tempSerial) {
		this.tempSerial = tempSerial;
	}



 
	/**
	 * Return the value associated with the column: atf_number
	 */
	public java.lang.Long getAtfNumber () {
		return atfNumber;
	}

	/**
	 * Set the value related to the column: atf_number
	 * @param atfNumber the atf_number value
	 */
	public void setAtfNumber (java.lang.Long atfNumber) {
		this.atfNumber = atfNumber;
	}



 
	/**
	 * Return the value associated with the column: farie_number
	 */
	public java.lang.Long getFarieNumber () {
		return farieNumber;
	}

	/**
	 * Set the value related to the column: farie_number
	 * @param farieNumber the farie_number value
	 */
	public void setFarieNumber (java.lang.Long farieNumber) {
		this.farieNumber = farieNumber;
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
	 * Return the value associated with the column: zamimeh
	 */
	public java.util.Set<ir.serajsamaneh.core.file.FileEntity> getZamimeh () {
		return zamimeh;
	}

	/**
	 * Set the value related to the column: zamimeh
	 * @param zamimeh the zamimeh value
	 */
	public void setZamimeh (java.util.Set<ir.serajsamaneh.core.file.FileEntity> zamimeh) {
		this.zamimeh = zamimeh;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof ir.serajsamaneh.accounting.sanadhesabdari.SanadHesabdariEntity)) return false;
		else {
			ir.serajsamaneh.accounting.sanadhesabdari.SanadHesabdariEntity sanadHesabdariEntity = (ir.serajsamaneh.accounting.sanadhesabdari.SanadHesabdariEntity) obj;
			if (null == this.getId() || null == sanadHesabdariEntity.getId()) return false;
			else return (this.getId().equals(sanadHesabdariEntity.getId()));
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