package ir.serajsamaneh.accounting.sanadhesabdariitemtemplate;

import java.util.Set;

import ir.serajsamaneh.accounting.articletafsili.ArticleTafsiliEntity;
import ir.serajsamaneh.accounting.articletafsilitemplate.ArticleTafsiliTemplateEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.enumeration.YesNoEnum;




public class SanadHesabdariItemTemplateEntity extends BaseSanadHesabdariItemTemplateEntity {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public SanadHesabdariItemTemplateEntity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public SanadHesabdariItemTemplateEntity (java.lang.Long id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	public String toString() {
//		return getBedehkar()>0 ? 
//				(getHesabKol()+"["+ SerajMessageUtil.getMessage("SanadHesabdariItem_bedehkar") +"("+ getBedehkar().toString()+")]") 
//				: (getHesabKol()+"["+ SerajMessageUtil.getMessage("SanadHesabdariItem_bestankar") +"("+ getBestankar().toString()+")]");
		if(getId()!=null)
			return getId().toString();
		return "";
	}
	
	String entityId;
	YesNoEnum applyAutomaticTafsili;
//	MarkazHazineEntity markazHazine;
	
	public YesNoEnum getApplyAutomaticTafsili() {
		return applyAutomaticTafsili;
	}

	public void setApplyAutomaticTafsili(YesNoEnum applyAutomaticTafsili) {
		this.applyAutomaticTafsili = applyAutomaticTafsili;
	}

	public String getHesabKolCode() {
		return getHesabKolTemplate().getCode();
	}

	public String getHesabMoeenCode() {
		return getHesabMoeenTemplate().getCode();
	}
	
	public Long getHesabTafsiliCode() {
		return getHesabTafsiliTemplate().getCode();
	}

/*	
	public MarkazHazineEntity getMarkazHazine() {
		return markazHazine;
	}
	public void setMarkazHazine(MarkazHazineEntity markazHazine) {
		this.markazHazine = markazHazine;
	}*/
	
	
	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	String applyAutomaticTafsiliName;
	public String getApplyAutomaticTafsiliName() {
		if(applyAutomaticTafsiliName == null)
			applyAutomaticTafsiliName = SerajMessageUtil.getMessage(getApplyAutomaticTafsili().nameWithClass());
		return applyAutomaticTafsiliName;
	}
	
	public ArticleTafsiliTemplateEntity getArticleTafsiliByLevel(Integer level) {
		Set<ArticleTafsiliTemplateEntity> set = getArticleTafsiliTemplate();
		if(set == null)
			return null;
		for (ArticleTafsiliTemplateEntity articleTafsiliEntity : set) {
			if(articleTafsiliEntity.getLevel().intValue() == level)
				return articleTafsiliEntity;
		}
		return null;
		
	}

}