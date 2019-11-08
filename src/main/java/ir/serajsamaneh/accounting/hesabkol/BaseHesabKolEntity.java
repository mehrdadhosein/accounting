package ir.serajsamaneh.accounting.hesabkol;

import ir.serajsamaneh.core.base.BaseEntity;

import java.io.Serializable;



/**
 * This is an object that contains data related to the tb_hesab_kol table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_hesab_kol"
 */

public abstract class BaseHesabKolEntity  extends BaseEntity<Long>   implements Serializable {

	public static String REF = "HesabKolEntity";
	public static String PROP_NAME = "name";
	public static String PROP_BESTANKR = "bestankr";
	public static String PROP_DESCRIPTION = "description";
	public static String PROP_ORGAN = "organ";
	public static String PROP_HESAB_GROUP = "hesabGroup";
	public static String PROP_MAHYAT_KOL = "mahyatKol";
	public static String PROP_BEDEHKAR = "bedehkar";
	public static String PROP_HIDDEN = "hidden";
	public static String PROP_ID = "id";
	public static String PROP_HESAB_KOL_TEMPLATE = "hesabKolTemplate";
	public static String PROP_SAAL_MAALI = "saalMaali";
	public static String PROP_CODE = "code";


	// constructors
	public BaseHesabKolEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseHesabKolEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseHesabKolEntity (
		java.lang.Long id,
		ir.serajsamaneh.accounting.hesabgroup.HesabGroupEntity hesabGroup,
		ir.serajsamaneh.core.organ.OrganEntity organ,
		ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity saalMaali) {

		this.setId(id);
		this.setHesabGroup(hesabGroup);
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

	// many to one
	private ir.serajsamaneh.accounting.hesabgroup.HesabGroupEntity hesabGroup;
	private ir.serajsamaneh.core.organ.OrganEntity organ;
	private ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity saalMaali;
	private ir.serajsamaneh.accounting.hesabkoltemplate.HesabKolTemplateEntity hesabKolTemplate;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="hesab_kol_id"
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
	 * Return the value associated with the column: hesab_group_id
	 */
	public ir.serajsamaneh.accounting.hesabgroup.HesabGroupEntity getHesabGroup () {
		return hesabGroup;
	}

	/**
	 * Set the value related to the column: hesab_group_id
	 * @param hesabGroup the hesab_group_id value
	 */
	public void setHesabGroup (ir.serajsamaneh.accounting.hesabgroup.HesabGroupEntity hesabGroup) {
		this.hesabGroup = hesabGroup;
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




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof ir.serajsamaneh.accounting.hesabkol.HesabKolEntity)) return false;
		else {
			ir.serajsamaneh.accounting.hesabkol.HesabKolEntity hesabKolEntity = (ir.serajsamaneh.accounting.hesabkol.HesabKolEntity) obj;
			if (null == this.getId() || null == hesabKolEntity.getId()) return false;
			else return (this.getId().equals(hesabKolEntity.getId()));
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