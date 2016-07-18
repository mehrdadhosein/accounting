package ir.serajsamaneh.accounting.enumeration;


public enum SanadFunctionEnum {
	OMOMI,EFTETAHIE,BASTAN_HESABHA,EKHTETAMIE,TADIL_MAHIAT_AVAL_DORE;
	public Integer value(){
		return this.ordinal();
	}
	public static SanadFunctionEnum getName(Integer value) {
		for(SanadFunctionEnum en : SanadFunctionEnum.values()){
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
