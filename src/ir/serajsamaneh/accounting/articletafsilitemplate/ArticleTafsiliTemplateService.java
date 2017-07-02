package ir.serajsamaneh.accounting.articletafsilitemplate;

import ir.serajsamaneh.core.base.BaseEntityService;

public class ArticleTafsiliTemplateService extends BaseEntityService<ArticleTafsiliTemplateEntity, Long> {

	@Override
	protected ArticleTafsiliTemplateDAO getMyDAO() {
		return articleTafsiliTemplateDAO;
	}

	ArticleTafsiliTemplateDAO articleTafsiliTemplateDAO;

	public ArticleTafsiliTemplateDAO getArticleTafsiliTemplateDAO() {
		return articleTafsiliTemplateDAO;
	}

	public void setArticleTafsiliTemplateDAO(ArticleTafsiliTemplateDAO articleTafsiliTemplateDAO) {
		this.articleTafsiliTemplateDAO = articleTafsiliTemplateDAO;
	}

}