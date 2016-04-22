package ir.serajsamaneh.accounting.hesabmoeentemplate;

import java.io.Serializable;

import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.organ.OrganEntity;
import serajcomponent.DateConverter;
import serajcomponent.SerajDateTimePickerType;

import java.util.HashSet;



/**
 * This is an object that contains data related to the tb_hesab_moeen_template table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_hesab_moeen_template"
 */

public abstract class BaseHesabMoeenTemplateEntity  extends BaseEntity<Long>   implements Serializable {

	public static String REF = "HesabMoeenTemplateEntity";
	public static String PROP_NAME = "name";
	public static String PROP_DESCRIPTION = "description";
	public static String PROP_HIDDEN = "hidden";
	public static String PROP_ID = "id";
	public static String PROP_HESAB_KOL_TEMPLATE = "hesabKolTemplate";
	public static String PROP_MAHYAT_MOEEN = "mahyatMoeen";
	public static String PROP_CODE = "code";
	public static String PROP_SCOPE = "scope";


	// constructors
	public BaseHesabMoeenTemplateEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseHesabMoeenTemplateEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseHesabMoeenTemplateEntity (
		java.lang.Long id,
		ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateEntity hesabKolTemplate) {

		this.setId(id);
		this.setHesabKolTemplate(hesabKolTemplate);
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
	private java.lang.Integer mahyatMoeen;
	// many to one
	private ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateEntity hesabKolTemplate;
	OrganEntity organ;
	
	public OrganEntity getOrgan() {
		return organ;
	}

	public void setOrgan(OrganEntity organ) {
		this.organ = organ;
	}



	// collections
	private java.util.Set<ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity> moeenTafsiliTemplate;



	public java.lang.Integer getMahyatMoeen() {
		return mahyatMoeen;
	}

	public void setMahyatMoeen(java.lang.Integer mahyatMoeen) {
		this.mahyatMoeen = mahyatMoeen;
	}

	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="hesab_moeen_template_id"
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
	 * Return the value associated with the column: hesab_kol_template_id
	 */
	public ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateEntity getHesabKolTemplate () {
		return hesabKolTemplate;
	}

	/**
	 * Set the value related to the column: hesab_kol_template_id
	 * @param hesabKolTemplate the hesab_kol_template_id value
	 */
	public void setHesabKolTemplate (ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateEntity hesabKolTemplate) {
		this.hesabKolTemplate = hesabKolTemplate;
	}



	/**
	 * Return the value associated with the column: moeenTafsiliTemplate
	 */
	public java.util.Set<ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity> getMoeenTafsiliTemplate () {
		return moeenTafsiliTemplate;
	}

	/**
	 * Set the value related to the column: moeenTafsiliTemplate
	 * @param moeenTafsiliTemplate the moeenTafsiliTemplate value
	 */
	public void setMoeenTafsiliTemplate (java.util.Set<ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity> moeenTafsiliTemplate) {
		this.moeenTafsiliTemplate = moeenTafsiliTemplate;
	}

	public void addTomoeenTafsiliTemplate (ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity moeenTafsiliTemplateEntity) {
		if (null == getMoeenTafsiliTemplate()) setMoeenTafsiliTemplate(new java.util.TreeSet<ir.serajsamaneh.accounting.moeentafsilitemplate.MoeenTafsiliTemplateEntity>());
		getMoeenTafsiliTemplate().add(moeenTafsiliTemplateEntity);
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity)) return false;
		else {
			ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity hesabMoeenTemplateEntity = (ir.serajsamaneh.accounting.hesabmoeentemplate.HesabMoeenTemplateEntity) obj;
			if (null == this.getId() || null == hesabMoeenTemplateEntity.getId()) return false;
			else return (this.getId().equals(hesabMoeenTemplateEntity.getId()));
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