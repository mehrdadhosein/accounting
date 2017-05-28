package ir.serajsamaneh.accounting.enumeration;


public enum SanadFunctionEnum {
	OMOMI,EFTETAHIE,BASTAN_HESABHA,EKHTETAMIE,TADIL_MAHIAT_AVAL_DORE, MonthlySummary,EFTETAHIESummary,BASTAN_HESABHASummary,EKHTETAMIESummary;
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
	
    public final String nameWithClass() {
        return getClass().getSimpleName()+"_"+name();
    }


}
