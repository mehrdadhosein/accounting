package ir.serajsamaneh.accounting.enumeration;

public enum SanadStateEnum {
	YADDASHT,MOVAGHAT,BARRESI_SHODE,DAEM,TEMP,MERGED, EBTAL;
	public Integer value(){
		return this.ordinal();
	}
	public static SanadStateEnum getName(Integer value) {
		for(SanadStateEnum en : SanadStateEnum.values()){
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
