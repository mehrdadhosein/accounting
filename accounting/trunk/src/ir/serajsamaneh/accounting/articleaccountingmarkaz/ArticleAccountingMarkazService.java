package ir.serajsamaneh.accounting.articleaccountingmarkaz;

import ir.serajsamaneh.core.base.BaseEntityService;

public class ArticleAccountingMarkazService extends
		BaseEntityService<ArticleAccountingMarkazEntity, Long> {

	@Override
	protected ArticleAccountingMarkazDAO getMyDAO() {
		return articleAccountingMarkazDAO;
	}

	ArticleAccountingMarkazDAO articleAccountingMarkazDAO;

	public ArticleAccountingMarkazDAO getArticleAccountingMarkazDAO() {
		return articleAccountingMarkazDAO;
	}

	public void setArticleAccountingMarkazDAO(
			ArticleAccountingMarkazDAO articleAccountingMarkazDAO) {
		this.articleAccountingMarkazDAO = articleAccountingMarkazDAO;
	}


}