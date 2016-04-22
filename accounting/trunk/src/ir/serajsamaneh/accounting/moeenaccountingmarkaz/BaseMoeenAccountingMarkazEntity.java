package ir.serajsamaneh.accounting.moeenaccountingmarkaz;

import java.io.Serializable;
import ir.serajsamaneh.core.base.BaseEntity;
import serajcomponent.DateConverter;
import serajcomponent.SerajDateTimePickerType;

import java.util.HashSet;



/**
 * This is an object that contains data related to the tb_moeen_tafsili table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_moeen_tafsili"
 */

public abstract class BaseMoeenAccountingMarkazEntity  extends BaseEntity<Long>   implements Serializable {

	public static String REF = "MoeenTafsiliEntity";
	public static String PROP_HESAB_MOEEN = "hesabMoeen";
	public static String PROP_HESAB_TAFSILI = "accountingMarkaz";
	public static String PROP_ID = "id";
	public static String PROP_LEVEL = "level";


	// constructors
	public BaseMoeenAccountingMarkazEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMoeenAccountingMarkazEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}



	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.Integer level;

	// many to one
	private ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity hesabMoeen;
	private ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity accountingMarkaz;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="moeen_tafsili_id"
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
	 * Return the value associated with the column: level
	 */
	public java.lang.Integer getLevel () {
		return level;
	}

	/**
	 * Set the value related to the column: level
	 * @param level the level value
	 */
	public void setLevel (java.lang.Integer level) {
		this.level = level;
	}




	/**
	 * Return the value associated with the column: hesab_moeen_id
	 */
	public ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity getHesabMoeen () {
		return hesabMoeen;
	}

	/**
	 * Set the value related to the column: hesab_moeen_id
	 * @param hesabMoeen the hesab_moeen_id value
	 */
	public void setHesabMoeen (ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity hesabMoeen) {
		this.hesabMoeen = hesabMoeen;
	}


	public ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity getAccountingMarkaz() {
		return accountingMarkaz;
	}

	public void setAccountingMarkaz(
			ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity accountingMarkaz) {
		this.accountingMarkaz = accountingMarkaz;
	}

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof ir.serajsamaneh.accounting.moeenaccountingmarkaz.MoeenAccountingMarkazEntity)) return false;
		else {
			ir.serajsamaneh.accounting.moeenaccountingmarkaz.MoeenAccountingMarkazEntity moeenAccountingMarkazEntity = (ir.serajsamaneh.accounting.moeenaccountingmarkaz.MoeenAccountingMarkazEntity) obj;
			if (null == this.getId() || null == moeenAccountingMarkazEntity.getId()) return false;
			else return (this.getId().equals(moeenAccountingMarkazEntity.getId()));
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