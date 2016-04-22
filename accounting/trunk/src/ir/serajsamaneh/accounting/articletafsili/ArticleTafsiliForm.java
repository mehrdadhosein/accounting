package ir.serajsamaneh.accounting.articletafsili;

import ir.serajsamaneh.core.base.BaseEntityForm;

import javax.faces.model.DataModel;

public class ArticleTafsiliForm   extends BaseEntityForm<ArticleTafsiliEntity,Long>  {






	@Override
	protected ArticleTafsiliService getMyService() {
		return articleTafsiliService;
	}





	
	ArticleTafsiliService articleTafsiliService;
	
	public void setArticleTafsiliService(ArticleTafsiliService articleTafsiliService) {
		this.articleTafsiliService = articleTafsiliService;
	}
	
	public ArticleTafsiliService getArticleTafsiliService() {
		return articleTafsiliService;
	}


	public String localSave() {
		save();
		return getLocalViewUrl();
	}

	public DataModel getLocalDataModel() {
		return getDataModel();
	}
	
	public DataModel getLocalArchiveDataModel() {
		return getDataModel();
	}	
	
	public Boolean getIsCreated() {
		return null;
	}
	

}