package ir.serajsamaneh.accounting.enumeration;

public enum MarkazhazineType {
	
	TOLIDI,EDARI,KHADAMATI;
	public Integer value(){
		return this.ordinal();
	}
	public static MarkazhazineType getName(Integer value) {
		for(MarkazhazineType en : MarkazhazineType.values()){
			if(en.ordinal() == value)
				return en;
		}
		throw new IllegalStateException();

	}
	@Override
	public String toString() {
		return this.name();
	}

}
