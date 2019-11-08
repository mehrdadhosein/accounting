package ir.serajsamaneh.accounting.accountingmarkaz;

import java.io.Serializable;
import ir.serajsamaneh.core.base.BaseEntity;
import serajcomponent.DateConverter;
import serajcomponent.SerajDateTimePickerType;

import java.util.HashSet;



/**
 * This is an object that contains data related to the tb_hesab_tafsili table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_hesab_tafsili"
 */

public abstract class BaseAccountingMarkazEntity  extends BaseEntity<Long>   implements Serializable {

	public static String REF = "HesabTafsiliEntity";
	public static String PROP_HESAB_TAFSILI_TEMPLATE = "hesabTafsiliTemplate";
	public static String PROP_NAME = "name";
	public static String PROP_BESTANKR = "bestankr";
	public static String PROP_DESCRIPTION = "description";
	public static String PROP_ORGAN = "organ";
	public static String PROP_TAFSIL_TYPE = "tafsilType";
	public static String PROP_BEDEHKAR = "bedehkar";
	public static String PROP_HIDDEN = "hidden";
	public static String PROP_ID = "id";
	public static String PROP_SAAL_MAALI = "saalMaali";
	public static String PROP_CODE = "code";
	public static String PROP_SCOPE = "scope";


	// constructors
	public BaseAccountingMarkazEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseAccountingMarkazEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseAccountingMarkazEntity (
		java.lang.Long id,
		ir.serajsamaneh.core.organ.OrganEntity organ,
		ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity saalMaali) {

		this.setId(id);
		this.setOrgan(organ);
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
	private java.lang.Integer level;
	// many to one
	private ir.serajsamaneh.core.organ.OrganEntity organ;
	private ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity saalMaali;
	private ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity hesabTafsiliTemplate;

	// collections
	private java.util.Set<ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity> childs;
	private java.util.Set<ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity> parents;
	private java.util.Set<ir.serajsamaneh.accounting.moeenaccountingmarkaz.MoeenAccountingMarkazEntity> moeenAccountingMarkaz;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="hesab_tafsili_id"
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



 
	public java.lang.Integer getLevel() {
		return level;
	}

	public void setLevel(java.lang.Integer level) {
		this.level = level;
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
	 * Return the value associated with the column: hesab_tafsili_template_id
	 */
	public ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity getHesabTafsiliTemplate () {
		return hesabTafsiliTemplate;
	}

	/**
	 * Set the value related to the column: hesab_tafsili_template_id
	 * @param hesabTafsiliTemplate the hesab_tafsili_template_id value
	 */
	public void setHesabTafsiliTemplate (ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity hesabTafsiliTemplate) {
		this.hesabTafsiliTemplate = hesabTafsiliTemplate;
	}



	/**
	 * Return the value associated with the column: childs
	 */
	public java.util.Set<ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity> getChilds () {
		return childs;
	}

	/**
	 * Set the value related to the column: childs
	 * @param childs the childs value
	 */
	public void setChilds (java.util.Set<ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity> childs) {
		this.childs = childs;
	}

	public void addTochilds (ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity hesabTafsiliEntity) {
		if (null == getChilds()) setChilds(new java.util.TreeSet<ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity>());
		getChilds().add(hesabTafsiliEntity);
	}



	/**
	 * Return the value associated with the column: parents
	 */
	public java.util.Set<ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity> getParents () {
		return parents;
	}

	/**
	 * Set the value related to the column: parents
	 * @param parents the parents value
	 */
	public void setParents (java.util.Set<ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity> parents) {
		this.parents = parents;
	}

	public void addToparents (ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity hesabTafsiliEntity) {
		if (null == getParents()) setParents(new java.util.TreeSet<ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity>());
		getParents().add(hesabTafsiliEntity);
	}



	/**
	 * Return the value associated with the column: moeenAccountingMarkaz
	 */
	public java.util.Set<ir.serajsamaneh.accounting.moeenaccountingmarkaz.MoeenAccountingMarkazEntity> getMoeenAccountingMarkaz () {
		return moeenAccountingMarkaz;
	}

	/**
	 * Set the value related to the column: moeenAccountingMarkaz
	 * @param moeenAccountingMarkaz the moeenAccountingMarkaz value
	 */
	public void setMoeenAccountingMarkaz (java.util.Set<ir.serajsamaneh.accounting.moeenaccountingmarkaz.MoeenAccountingMarkazEntity> moeenAccountingMarkaz) {
		this.moeenAccountingMarkaz = moeenAccountingMarkaz;
	}

	public void addTomoeenAccountingMarkaz (ir.serajsamaneh.accounting.moeenaccountingmarkaz.MoeenAccountingMarkazEntity moeenAccountingMarkazEntity) {
		if (null == getMoeenAccountingMarkaz()) setMoeenAccountingMarkaz(new java.util.TreeSet<ir.serajsamaneh.accounting.moeenaccountingmarkaz.MoeenAccountingMarkazEntity>());
		getMoeenAccountingMarkaz().add(moeenAccountingMarkazEntity);
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity)) return false;
		else {
			ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity hesabTafsiliEntity = (ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity) obj;
			if (null == this.getId() || null == hesabTafsiliEntity.getId()) return false;
			else return (this.getId().equals(hesabTafsiliEntity.getId()));
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