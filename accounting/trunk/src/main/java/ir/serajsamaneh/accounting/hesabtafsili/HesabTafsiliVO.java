package ir.serajsamaneh.accounting.hesabtafsili;

public class HesabTafsiliVO {
	
	public HesabTafsiliVO(HesabTafsiliEntity hesabTafsiliEntity) {
		setCode(hesabTafsiliEntity.getCode());
		setName(hesabTafsiliEntity.getName());
	}
	
	String moeen;
	Long code;
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
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}

}
