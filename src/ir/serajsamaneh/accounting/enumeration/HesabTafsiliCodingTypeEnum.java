package ir.serajsamaneh.accounting.enumeration;

public enum HesabTafsiliCodingTypeEnum {
	MANUAL,
	SERIAL,
	VARIABLE_HIERARCHICAL,
	CONSTANT_HIERARCHICAL;
	
	public Integer value() {
		return this.ordinal();
	}

	public static HesabTafsiliCodingTypeEnum getName(Integer value) {
		for (HesabTafsiliCodingTypeEnum en : HesabTafsiliCodingTypeEnum.values()) {
			if (en.ordinal() == value)
				return en;
		}
		throw new IllegalStateException();

	}
	
	public Integer getOrdinal(){
		return new Integer(this.ordinal());
	}
	
    public final String nameWithClass() {
        return getClass().getSimpleName()+"_"+name();
    }
}
