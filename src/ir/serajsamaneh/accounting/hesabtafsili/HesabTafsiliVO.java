package ir.serajsamaneh.accounting.hesabtafsili;

public class HesabTafsiliVO {
	
	public HesabTafsiliVO(HesabTafsiliEntity hesabTafsiliEntity) {
		setCode(hesabTafsiliEntity.getCode());
		setName(hesabTafsiliEntity.getName());
	}
	
	String moeen;
	String code;
	String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMoeen() {
		return moeen;
	}
	public void setMoeen(String moeen) {
		this.moeen = moeen;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

}
