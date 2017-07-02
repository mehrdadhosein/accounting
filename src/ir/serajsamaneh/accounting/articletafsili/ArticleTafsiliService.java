package ir.serajsamaneh.accounting.articletafsili;

import ir.serajsamaneh.core.base.BaseEntityService;

public class ArticleTafsiliService extends BaseEntityService<ArticleTafsiliEntity, Long> {

	@Override
	protected ArticleTafsiliDAO getMyDAO() {
		return articleTafsiliDAO;
	}

	ArticleTafsiliDAO articleTafsiliDAO;

	public void setArticleTafsiliDAO(ArticleTafsiliDAO articleTafsiliDAO) {
		this.articleTafsiliDAO = articleTafsiliDAO;
	}

	public ArticleTafsiliDAO getArticleTafsiliDAO() {
		return articleTafsiliDAO;
	}

}