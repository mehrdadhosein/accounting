package ir.serajsamaneh.accounting.enumeration;


public enum HesabLevelEnum {
	KOL,MOEIN,TAFZILI;
	public Integer value(){
		return this.ordinal();
	}
	public static HesabLevelEnum getName(Integer value) {
		for(HesabLevelEnum en : HesabLevelEnum.values()){
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
