package ir.serajsamaneh.accounting.hesabtafsili;

import ir.serajsamaneh.core.base.BaseEntity;

import java.io.Serializable;



/**
 * This is an object that contains data related to the tb_hesab_tafsili table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_hesab_tafsili"
 */

public abstract class BaseHesabTafsiliEntity  extends BaseEntity<Long>   implements Serializable {

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
	public static String PROP_HESAB_CLASSIFICATION = "hesabClassification";


	// constructors
	public BaseHesabTafsiliEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseHesabTafsiliEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseHesabTafsiliEntity (
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
	private java.lang.Long code;
	private java.lang.String name;
	private java.lang.String description;
	private java.lang.Double bedehkar;
	private java.lang.Double bestankr;
//	private java.lang.Boolean isShenavar;
	private java.lang.Integer level;

	// many to one
	private ir.serajsamaneh.core.organ.OrganEntity organ;
	private ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity saalMaali;
	private ir.serajsamaneh.accounting.hesabtafsilitemplate.HesabTafsiliTemplateEntity hesabTafsiliTemplate;
	private ir.serajsamaneh.accounting.hesabclassification.HesabClassificationEntity hesabClassification;

	// collections
	private java.util.Set<ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity> childAccountingMarkaz;
	private java.util.Set<ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity> childs;
	private java.util.Set<ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity> parents;
	private java.util.Set<ir.serajsamaneh.accounting.moeentafsili.MoeenTafsiliEntity> moeenTafsili;



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



 
	public java.lang.Integer getLevel() {
		return level;
	}

	public void setLevel(java.lang.Integer level) {
		this.level = level;
	}
//
//	public java.lang.Boolean getIsShenavar() {
//		return isShenavar;
//	}
//
//	public void setIsShenavar(java.lang.Boolean isShenavar) {
//		this.isShenavar = isShenavar;
//	}

	/**
	 * Return the value associated with the column: code
	 */
	public java.lang.Long getCode () {
		return code;
	}

	/**
	 * Set the value related to the column: code
	 * @param code the code value
	 */
	public void setCode (java.lang.Long code) {
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



	public ir.serajsamaneh.accounting.hesabclassification.HesabClassificationEntity getHesabClassification() {
		return hesabClassification;
	}

	public void setHesabClassification(
			ir.serajsamaneh.accounting.hesabclassification.HesabClassificationEntity hesabClassification) {
		this.hesabClassification = hesabClassification;
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



	public java.util.Set<ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity> getChildAccountingMarkaz() {
		return childAccountingMarkaz;
	}

	public void setChildAccountingMarkaz(
			java.util.Set<ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity> childAccountingMarkaz) {
		this.childAccountingMarkaz = childAccountingMarkaz;
	}

	/**
	 * Return the value associated with the column: childs
	 */
	public java.util.Set<ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity> getChilds () {
		return childs;
	}

	/**
	 * Set the value related to the column: childs
	 * @param childs the childs value
	 */
	public void setChilds (java.util.Set<ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity> childs) {
		this.childs = childs;
	}

	public void addTochilds (ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity hesabTafsiliEntity) {
		if (null == getChilds()) setChilds(new java.util.TreeSet<ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity>());
		getChilds().add(hesabTafsiliEntity);
	}



	/**
	 * Return the value associated with the column: parents
	 */
	public java.util.Set<ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity> getParents () {
		return parents;
	}

	/**
	 * Set the value related to the column: parents
	 * @param parents the parents value
	 */
	public void setParents (java.util.Set<ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity> parents) {
		this.parents = parents;
	}

	public void addToparents (ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity hesabTafsiliEntity) {
		if (null == getParents()) setParents(new java.util.TreeSet<ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity>());
		getParents().add(hesabTafsiliEntity);
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

	public void addTomoeenTafsili (ir.serajsamaneh.accounting.moeentafsili.MoeenTafsiliEntity moeenTafsiliEntity) {
		if (null == getMoeenTafsili()) setMoeenTafsili(new java.util.TreeSet<ir.serajsamaneh.accounting.moeentafsili.MoeenTafsiliEntity>());
		getMoeenTafsili().add(moeenTafsiliEntity);
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity)) return false;
		else {
			ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity hesabTafsiliEntity = (ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity) obj;
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