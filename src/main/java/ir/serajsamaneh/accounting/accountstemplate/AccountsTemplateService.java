package ir.serajsamaneh.accounting.accountstemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ir.serajsamaneh.accounting.accountingmarkaz.AccountingMarkazEntity;
import ir.serajsamaneh.accounting.base.BaseAccountingService;
import ir.serajsamaneh.accounting.sanadhesabdariitemtemplate.SanadHesabdariItemTemplateEntity;
import ir.serajsamaneh.accounting.sanadhesabdariitemtemplate.SanadHesabdariItemTemplateService;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.util.ActionLogUtil;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.core.util.XMLUtil;
import ir.serajsamaneh.enumeration.ActionTypeEnum;
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AccountsTemplateService extends BaseAccountingService<AccountsTemplateEntity, Long> {

	@Override
	protected AccountsTemplateDAO getMyDAO() {
		return accountsTemplateDAO;
	}

	@Autowired
	AccountsTemplateDAO accountsTemplateDAO;
	@Autowired
	SanadHesabdariItemTemplateService sanadHesabdariItemTemplateService;

	@Override
	@Transactional
	public void save(AccountsTemplateEntity entity) {
		String action = (entity.getId() != null ? SerajMessageUtil.getMessage(ActionTypeEnum.EDIT.nameWithClass())
				: SerajMessageUtil.getMessage(ActionTypeEnum.CREATE.nameWithClass()));
		super.save(entity);
		ActionLogUtil.logAction(action, SerajMessageUtil.getMessage(this.getEntityName() + "_title"), "", "",
				entity.getCompleteInfo());
	}

	@Transactional
	public void updateTemplate(AccountsTemplateEntity entity,
			List<SanadHesabdariItemTemplateEntity> sanadHesabdariItemList) {
		SanadHesabdariItemTemplateEntity sanadHesabdariItemTemplateBedehkar = entity
				.getSanadHesabdariItemTemplateBedehkar();
		if (entity.getId() != null && sanadHesabdariItemTemplateBedehkar != null
				&& sanadHesabdariItemTemplateBedehkar.getId() != null
				&& !sanadHesabdariItemList.contains(sanadHesabdariItemTemplateBedehkar)) {
			entity.setSanadHesabdariItemTemplateBedehkar(null);
			sanadHesabdariItemTemplateService.delete(sanadHesabdariItemTemplateBedehkar.getId());
		}

		SanadHesabdariItemTemplateEntity sanadHesabdariItemTemplateBestankar = entity
				.getSanadHesabdariItemTemplateBestankar();
		if (entity.getId() != null && sanadHesabdariItemTemplateBestankar != null
				&& sanadHesabdariItemTemplateBestankar.getId() != null
				&& !sanadHesabdariItemList.contains(sanadHesabdariItemTemplateBestankar)) {
			entity.setSanadHesabdariItemTemplateBestankar(null);
			sanadHesabdariItemTemplateService.delete(sanadHesabdariItemTemplateBestankar.getId());
		}

		for (SanadHesabdariItemTemplateEntity sanadHesabdariItemTemplateEntity : sanadHesabdariItemList) {
			if (sanadHesabdariItemTemplateEntity.getTemplateType().equals("Bedehkar")) {
				sanadHesabdariItemTemplateService.save(sanadHesabdariItemTemplateEntity);
				entity.setSanadHesabdariItemTemplateBedehkar(sanadHesabdariItemTemplateEntity);
			} else if (sanadHesabdariItemTemplateEntity.getTemplateType().equals("Bestankar")) {
				sanadHesabdariItemTemplateService.save(sanadHesabdariItemTemplateEntity);
				entity.setSanadHesabdariItemTemplateBestankar(sanadHesabdariItemTemplateEntity);
			}
		}

		save(entity);

	}

	public AccountsTemplateEntity loadTemplateByActionIdANDAccountingMarkaz(String actionId,
			AccountingMarkazEntity accountingMarkaz, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("actionId@eq", actionId);
		localFilter.put("organId@eq", organId);
		localFilter.put("accountingMarkaz.id@eq", accountingMarkaz.getId());
		List<AccountsTemplateEntity> dataList = getDataList(localFilter, FlushMode.MANUAL, true);
		if (dataList.size() == 1)
			return dataList.get(0);
		else if (dataList.size() == 0)
			return null;
		else
			throw new FatalException("More Than One records Found");
	}

	public AccountsTemplateEntity loadTemplateByActionId(String actionId, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("actionId@eq", actionId);
		localFilter.put("organId@eq", organId);
		List<AccountsTemplateEntity> dataList = getDataList(localFilter, FlushMode.MANUAL, true);
		if (dataList.size() == 1)
			return dataList.get(0);
		else if (dataList.size() == 0)
			return null;
		else
			throw new FatalException("More Than One records Found");
	}

	@Transactional
	public void createAutomaticSanadTemplates(Long organId) {
		getLogger().info("initializing accounts tempaltes");
		try {
			createDefaultAutomaticSanadTemplates(organId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Transactional
	private void createDefaultAutomaticSanadTemplates(Long organId) throws IOException {
		URL resource = getClass().getResource("/config/automaticSanadTemplates");
		if (resource == null)
			return;
		File dir = new File(resource.getFile());

		FilenameFilter filter = new WildcardFileFilter("automaticSanadTemplate-*.xml");
		String[] list = dir.list(filter);

		for (String fileName : list) {
			InputStream fileInputStream = null;
			String localFilePath = dir.getAbsolutePath() + "/" + fileName;
			try {
				fileInputStream = new FileInputStream(localFilePath);
				// parse XML file -> XML document will be build
				Document doc = XMLUtil.parseFile(fileInputStream);
				NodeList rootNodes = doc.getElementsByTagName("accountsTemplate");
				Node item = rootNodes.item(0);
				Element accounts = (Element) item;

				NodeList childNodes = accounts.getChildNodes();
				createDefaultAutomaticSanadTemplates(childNodes, organId);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
//				throw new IllegalStateException();
			} finally {
				if (fileInputStream != null)
					fileInputStream.close();
			}
		}

	}

	@Transactional
	public void createDefaultAutomaticSanadTemplates(NodeList childNodes, Long organId) {
		for (int s = 0; s < childNodes.getLength(); s++) {
			Node accountNode = childNodes.item(s);
			if (accountNode.getNodeType() == Node.ELEMENT_NODE) {
				Element nodeElem = (Element) accountNode;
				createAutomaticSanadTemplate(nodeElem, organId);
			}
		}
	}

	@Transactional
	private void createAutomaticSanadTemplate(Element automaticSanadTemplateElem, Long organId) {
		String automaticSanadTemplateName = automaticSanadTemplateElem.getAttribute("actionName");
		String automaticSanadTemplateActionId = automaticSanadTemplateElem.getAttribute("actionId");
		String automaticSanadTemplateDescription = automaticSanadTemplateElem.getAttribute("description");
		Boolean createBedehkar = Boolean.valueOf(automaticSanadTemplateElem.getAttribute("createBedehkar"));
		Boolean createBestankar = Boolean.valueOf(automaticSanadTemplateElem.getAttribute("createBestankar"));

		AccountsTemplateEntity accountsTemplateEntity = getAccountsTemplateByActionId(automaticSanadTemplateActionId,
				organId);
		if (accountsTemplateEntity == null)
			accountsTemplateEntity = new AccountsTemplateEntity();
		accountsTemplateEntity.setActionName(automaticSanadTemplateName);
		accountsTemplateEntity.setActionId(automaticSanadTemplateActionId);
		accountsTemplateEntity.setDescription(automaticSanadTemplateDescription);
		accountsTemplateEntity.setOrganId(organId);

		List<SanadHesabdariItemTemplateEntity> sanadHesabdariItemList = new ArrayList<SanadHesabdariItemTemplateEntity>();
		if (createBedehkar) {
			SanadHesabdariItemTemplateEntity itemTemplateEntity = accountsTemplateEntity
					.getSanadHesabdariItemTemplateBedehkar();
			if (itemTemplateEntity == null) {
				itemTemplateEntity = new SanadHesabdariItemTemplateEntity();
				itemTemplateEntity.setTemplateType("Bedehkar");
			}
			sanadHesabdariItemList.add(itemTemplateEntity);
		}
		if (createBestankar) {
			SanadHesabdariItemTemplateEntity itemTemplateEntity = accountsTemplateEntity
					.getSanadHesabdariItemTemplateBestankar();
			if (itemTemplateEntity == null) {
				itemTemplateEntity = new SanadHesabdariItemTemplateEntity();
				itemTemplateEntity.setTemplateType("Bestankar");
			}
			sanadHesabdariItemList.add(itemTemplateEntity);
		}

		updateTemplate(accountsTemplateEntity, sanadHesabdariItemList);
		getLogger().info("accountsTemplateEntity created : " + accountsTemplateEntity);
	}

	private AccountsTemplateEntity getAccountsTemplateByActionId(String automaticSanadTemplateActionId, Long organId) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("actionId@eq", automaticSanadTemplateActionId);
		localFilter.put("organId@eq", organId);
		List<AccountsTemplateEntity> dataList = getDataList(localFilter, "", null, FlushMode.MANUAL, false);
		return (ir.serajsamaneh.accounting.accountstemplate.AccountsTemplateEntity) getUniqueResult(dataList);
	}
}