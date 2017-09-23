package ir.serajsamaneh.accounting.articletafsilitemplate;

import javax.faces.model.DataModel;

import ir.serajsamaneh.core.base.BaseEntityForm;

public class ArticleTafsiliTemplateForm extends BaseEntityForm<ArticleTafsiliTemplateEntity, Long> {

	@Override
	protected ArticleTafsiliTemplateService getMyService() {
		return articleTafsiliTemplateService;
	}

	ArticleTafsiliTemplateService articleTafsiliTemplateService;

	public ArticleTafsiliTemplateService getArticleTafsiliTemplateService() {
		return articleTafsiliTemplateService;
	}

	public void setArticleTafsiliTemplateService(ArticleTafsiliTemplateService articleTafsiliTemplateService) {
		this.articleTafsiliTemplateService = articleTafsiliTemplateService;
	}

	public String localSave() {
		save();
		return getLocalViewUrl();
	}

	public DataModel<ArticleTafsiliTemplateEntity> getLocalArchiveDataModel() {
		return getLocalDataModel();
	}

	public Boolean getIsCreated() {
		return null;
	}

}