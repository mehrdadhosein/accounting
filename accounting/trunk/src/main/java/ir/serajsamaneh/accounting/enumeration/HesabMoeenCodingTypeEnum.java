package ir.serajsamaneh.accounting.enumeration;

public enum HesabMoeenCodingTypeEnum {
	MANUAL,
	SERIAL,
	VARIABLE_HIERARCHICAL,
	CONSTANT_HIERARCHICAL;
	
	public Integer value() {
		return this.ordinal();
	}

	public static HesabMoeenCodingTypeEnum getName(Integer value) {
		for (HesabMoeenCodingTypeEnum en : HesabMoeenCodingTypeEnum.values()) {
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
