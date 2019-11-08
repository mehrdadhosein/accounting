package ir.serajsamaneh.accounting.hesabmoeen;

import java.io.Serializable;
import ir.serajsamaneh.core.base.BaseEntity;
import serajcomponent.DateConverter;
import serajcomponent.SerajDateTimePickerType;

import java.util.HashSet;



/**
 * This is an object that contains data related to the tb_hesab_moeen table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_hesab_moeen"
 */

public abstract class BaseHesabMoeenEntity  extends BaseEntity<Long>   implements Serializable {

	public static String REF = "HesabMoeenEntity";
	public static String PROP_BESTANKR = "bestankr";
	public static String PROP_DESCRIPTION = "description";
	public static String PROP_HIDDEN = "hidden";
	public static String PROP_SAAL_MAALI = "saalMaali";
	public static String PROP_CODE = "code";
//	public static String PROP_MAHYAT_MOEEN = "mahyatMoeen";
	public static String PROP_NAME = "name";
	public static String PROP_HESAB_KOL = "hesabKol";
	public static String PROP_ORGAN = "organ";
	public static String PROP_BEDEHKAR = "bedehkar";
	public static String PROP_ID = "id";
	public static String PROP_SCOPE = "scope";
	public static String PROP_HESAB_MOEEN_TEMPLATE = "hesabMoeenTemplate";


	// constructors
	public BaseHesabMoeenEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseHesabMoeenEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseHesabMoeenEntity (
		java.lang.Long id,
		ir.serajsamaneh.accounting.hesabkol.HesabKolEntity hesabKol,
		ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity saalMaali) {

		this.setId(id);
		this.setHesabKol(hesabKol);
		this.setSaalMaali(saalMaali);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String code;
	private java.lang.String name;
	private java.lang.String description;
	private java.lang.Double bedehkar;
	private java.lang.Double bestankr;
//	private java.lang.Integer mahyatMoeen;

	// many to one
	private ir.serajsamaneh.accounting.hesabkol.HesabKolEntity hesabKol;
	private ir.serajsamaneh.core.organ.OrganEntity organ;
	private ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity saalMaali;
	private ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity hesabMoeenTemplate;

	// collections
	private java.util.Set<ir.serajsamaneh.accounting.moeentafsili.MoeenTafsiliEntity> moeenTafsili;
	private java.util.Set<ir.serajsamaneh.accounting.moeenaccountingmarkaz.MoeenAccountingMarkazEntity> moeenAccountingMarkaz;



//	public java.lang.Integer getMahyatMoeen() {
//		return mahyatMoeen;
//	}
//
//	public void setMahyatMoeen(java.lang.Integer mahyatMoeen) {
//		this.mahyatMoeen = mahyatMoeen;
//	}

	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="hesab_moeen_id"
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
	 * Return the value associated with the column: code
	 */
	public java.lang.String getCode () {
		return code;
	}

	/**
	 * Set the value related to the column: code
	 * @param code the code value
	 */
	public void setCode (java.lang.String code) {
		this.code = code;
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
	 * Return the value associated with the column: bedehkar
	 */
	public java.lang.Double getBedehkar () {
		return bedehkar;
	}

	/**
	 * Set the value related to the column: bedehkar
	 * @param bedehkar the bedehkar value
	 */
	public void setBedehkar (java.lang.Double bedehkar) {
		this.bedehkar = bedehkar;
	}



 
	/**
	 * Return the value associated with the column: bestankr
	 */
	public java.lang.Double getBestankr () {
		return bestankr;
	}

	/**
	 * Set the value related to the column: bestankr
	 * @param bestankr the bestankr value
	 */
	public void setBestankr (java.lang.Double bestankr) {
		this.bestankr = bestankr;
	}




	/**
	 * Return the value associated with the column: hesab_kol_id
	 */
	public ir.serajsamaneh.accounting.hesabkol.HesabKolEntity getHesabKol () {
		return hesabKol;
	}

	/**
	 * Set the value related to the column: hesab_kol_id
	 * @param hesabKol the hesab_kol_id value
	 */
	public void setHesabKol (ir.serajsamaneh.accounting.hesabkol.HesabKolEntity hesabKol) {
		this.hesabKol = hesabKol;
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
	 * Return the value associated with the column: hesab_moeen_template_id
	 */
	public ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity getHesabMoeenTemplate () {
		return hesabMoeenTemplate;
	}

	/**
	 * Set the value related to the column: hesab_moeen_template_id
	 * @param hesabMoeenTemplate the hesab_moeen_template_id value
	 */
	public void setHesabMoeenTemplate (ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity hesabMoeenTemplate) {
		this.hesabMoeenTemplate = hesabMoeenTemplate;
	}



	/**
	 * Return the value associated with the column: moeenTafsili
	 */
	public java.util.Set<ir.serajsamaneh.accounting.moeentafsili.MoeenTafsiliEntity> getMoeenTafsili () {
		return moeenTafsili;
	}

	/**
	 * Set the value related to the column: moeenTafsili
	 * @param moeenTafsili the moeenTafsili value
	 */
	public void setMoeenTafsili (java.util.Set<ir.serajsamaneh.accounting.moeentafsili.MoeenTafsiliEntity> moeenTafsili) {
		this.moeenTafsili = moeenTafsili;
	}

	public java.util.Set<ir.serajsamaneh.accounting.moeenaccountingmarkaz.MoeenAccountingMarkazEntity> getMoeenAccountingMarkaz() {
		return moeenAccountingMarkaz;
	}

	public void setMoeenAccountingMarkaz(
			java.util.Set<ir.serajsamaneh.accounting.moeenaccountingmarkaz.MoeenAccountingMarkazEntity> moeenAccountingMarkaz) {
		this.moeenAccountingMarkaz = moeenAccountingMarkaz;
	}

	public void addTomoeenTafsili (ir.serajsamaneh.accounting.moeentafsili.MoeenTafsiliEntity moeenTafsiliEntity) {
		if (null == getMoeenTafsili()) setMoeenTafsili(new java.util.TreeSet<ir.serajsamaneh.accounting.moeentafsili.MoeenTafsiliEntity>());
		getMoeenTafsili().add(moeenTafsiliEntity);
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity)) return false;
		else {
			ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity hesabMoeenEntity = (ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity) obj;
			if (null == this.getId() || null == hesabMoeenEntity.getId()) return false;
			else return (this.getId().equals(hesabMoeenEntity.getId()));
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