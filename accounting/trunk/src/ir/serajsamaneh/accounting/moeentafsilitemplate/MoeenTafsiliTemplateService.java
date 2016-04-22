package ir.serajsamaneh.accounting.moeentafsilitemplate;


import ir.serajsamaneh.core.base.BaseEntityService;

public class MoeenTafsiliTemplateService  extends BaseEntityService<MoeenTafsiliTemplateEntity,Long>  {


	
	@Override
	protected MoeenTafsiliTemplateDAO getMyDAO() {
		return moeenTafsiliTemplateDAO;
	}
	
	


MoeenTafsiliTemplateDAO moeenTafsiliTemplateDAO;

public void setMoeenTafsiliTemplateDAO(MoeenTafsiliTemplateDAO moeenTafsiliTemplateDAO) {
	this.moeenTafsiliTemplateDAO = moeenTafsiliTemplateDAO;
}
public MoeenTafsiliTemplateDAO getMoeenTafsiliTemplateDAO() {
	return moeenTafsiliTemplateDAO;
}



}