package ir.serajsamaneh.accounting.sanadtype;

import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.organ.OrganEntity;

import java.io.Serializable;


/**
 * This is an object that contains data related to the tb_sanad_type table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="tb_sanad_type"
 */

public abstract class BaseSanadTypeEntity  extends BaseEntity<Long>   implements Serializable {

	public static String REF = "SanadTypeEntity";
	public static String PROP_NAME = "name";
	public static String PROP_ID = "id";


	// constructors
	public BaseSanadTypeEntity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseSanadTypeEntity (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String name;


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
     *  column="sanad_type_id"
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




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof ir.serajsamaneh.accounting.sanadtype.SanadTypeEntity)) return false;
		else {
			ir.serajsamaneh.accounting.sanadtype.SanadTypeEntity sanadTypeEntity = (ir.serajsamaneh.accounting.sanadtype.SanadTypeEntity) obj;
			if (null == this.getId() || null == sanadTypeEntity.getId()) return false;
			else return (this.getId().equals(sanadTypeEntity.getId()));
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