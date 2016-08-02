package ir.serajsamaneh.accounting.enumeration;


public enum BedBesEnum {
	BEDEHKAR,BESTANKAR;
	public Integer value(){
		return this.ordinal();
	}
	public static BedBesEnum getName(Integer value) {
		for(BedBesEnum en : BedBesEnum.values()){
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
