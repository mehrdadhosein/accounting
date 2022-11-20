package ir.serajsamaneh.erpcore.contacthesab;

import java.util.Arrays;

import javax.faces.model.DataModel;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import ir.serajsamaneh.accounting.base.BaseAccountingForm;
import ir.serajsamaneh.core.contact.contact.ContactService;
import ir.serajsamaneh.core.exception.NoRecordFoundException;
@Named("contactHesab")
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class ContactHesabForm extends BaseAccountingForm<ContactHesabEntity, Long> {

	@Override
	protected ContactHesabService getMyService() {
		return contactHesabService;
	}

	ContactHesabService contactHesabService;
	ContactService contactService;

	public ContactService getContactService() {
		return contactService;
	}

	public void setContactService(ContactService contactService) {
		this.contactService = contactService;
	}

	public void setContactHesabService(ContactHesabService contactHesabService) {
		this.contactHesabService = contactHesabService;
	}

	public ContactHesabService getContactHesabService() {
		return contactHesabService;
	}

	@Override
	public ContactHesabEntity getEntity() {
		if (entity == null) {
			entity = super.getEntity();
			String contactId = getRequest().getParameter("contactId");
			if (StringUtils.hasText(contactId)) {
				ContactHesabEntity contactHesabByContactId = null;
				try {
					contactHesabByContactId = contactHesabService.getContactHesabByContactId(Long.valueOf(contactId),
							getCurrentUserActiveSaalMaali().getId());
				} catch (NoRecordFoundException e) {
					System.out.println(e.getMessage());
				}
				if (contactHesabByContactId != null) {
					Long contactHesabId = contactHesabByContactId.getId();
					setID(contactHesabId);
				} else {
					entity.setContact(getContactService().load(Long.valueOf(contactId)));
				}
			}
		}
		return entity;
	}

	public String localSave() {
		save();
		return null;
	}

	@Override
	public String save() {
		getEntity().setSaalMaali(getCurrentUserActiveSaalMaali());
		return super.save();
	}

	public DataModel<ContactHesabEntity> getLocalDataModel() {
		return getDataModel();
	}

	public DataModel<ContactHesabEntity> getLocalArchiveDataModel() {
		return getDataModel();
	}

	public Boolean getIsCreated() {
		return null;
	}

	public DataModel<ContactHesabEntity> getActiveSaalMaaliDataModel() {
		getFilter().put("saalMaali.id@eq", getCurrentUserActiveSaalMaali().getId());
		getFilter().put("hesabMoeen.id@isNotNullORhesabTafsiliOne.id@isNotNullORhesabTafsiliTwo.id@isNotNull",
				Arrays.asList("ding", "ding", "ding"));
		return super.getDataModel();
	}
}