package ir.serajsamaneh.accounting.saalmaali;

import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.enumeration.SaalMaaliStatusEnum;
import serajcomponent.DateConverter;
import serajcomponent.SerajDateTimePickerType;




public class SaalMaaliEntity extends BaseSaalMaaliEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public SaalMaaliEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public SaalMaaliEntity (java.lang.Long id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	@Override
	public String toString() {
		if(getId()!=null)
			return getSaal().toString()+"("+getOrgan()+")";
		return "";
	}
	
	public String getLog() {
		return "[id=" 
				+ getId() 
				+ " , " + SerajMessageUtil.getMessage("SaalMaali_saal")+ "=" + getSaal()
				+ " , " + SerajMessageUtil.getMessage("SaalMaali_startDate")+ "=" +DateConverter.toShamsiDate(getStartDate(),SerajDateTimePickerType.DateHorMin )
				+ " , " + SerajMessageUtil.getMessage("SaalMaali_end")+ "=" +DateConverter.toShamsiDate(getEndDate(), SerajDateTimePickerType.DateHorMin) 
				+ " , " + SerajMessageUtil.getMessage("SaalMaali_organ")+ "=" + getOrgan()  
				+ " , " + SerajMessageUtil.getMessage("SaalMaali_isActive")+ "=" +getIsActive().toString()
				+ " , " + SerajMessageUtil.getMessage("SaalMaali_status")+ "=" +SerajMessageUtil.getMessage(getStatus().nameWithClass())
				+ "]";
	}
	
	private java.lang.Boolean isActive;

	public java.lang.Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(java.lang.Boolean isActive) {
		this.isActive = isActive;
	}

	SaalMaaliStatusEnum status;

	public SaalMaaliStatusEnum getStatus() {
		return status;
	}

	public void setStatus(SaalMaaliStatusEnum status) {
		this.status = status;
	}
	
	public String getStatusDesc(){
		if(getStatus()!= null){
			if(getStatus().equals(SaalMaaliStatusEnum.InProgress))
					return SerajMessageUtil.getMessage("SaalMaali_statusDesc_InProgress");
			else if(getStatus().equals(SaalMaaliStatusEnum.TemporalAccountsClosed))
				return SerajMessageUtil.getMessage("SaalMaali_statusDesc_TemporalAccountsClosed");
			else if(getStatus().equals(SaalMaaliStatusEnum.PermanentAccountsClosed))
				return SerajMessageUtil.getMessage("SaalMaali_statusDesc_PermanentAccountsClosed");
			else if(getStatus().equals(SaalMaaliStatusEnum.SanadEkhtetamiehCreated))
				return SerajMessageUtil.getMessage("SaalMaali_statusDesc_SanadEkhtetamiehCreated");
		}
		return "";
	}
	
	
	public Boolean getIsInProgress(){
		return getStatus().equals(SaalMaaliStatusEnum.InProgress);
	}
	
	public Boolean getIsTemporalAccountsClosed(){
		return getStatus().equals(SaalMaaliStatusEnum.TemporalAccountsClosed);
	}
	
	public Boolean getIsSanadEkhtetamiehCreated(){
		return getStatus().equals(SaalMaaliStatusEnum.SanadEkhtetamiehCreated);
	}
	
	public String getNameWithOrgan(){
		if(getId()!=null)
			return getSaal()+" - "+getOrgan().getName();
		return "";
	}
	
}