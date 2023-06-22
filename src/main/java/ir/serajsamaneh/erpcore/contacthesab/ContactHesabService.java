package ir.serajsamaneh.erpcore.contacthesab;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ir.serajsamaneh.accounting.accountingmarkaztemplate.AccountingMarkazTemplateEntity;
import ir.serajsamaneh.accounting.base.BaseAccountingService;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.core.contact.contact.ContactEntity;
import ir.serajsamaneh.core.contact.contact.ContactService;
import ir.serajsamaneh.core.exception.DuplicateException;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.exception.NoRecordFoundException;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ContactHesabService extends BaseAccountingService<ContactHesabEntity, Long> {

	@Override
	protected ContactHesabDAO getMyDAO() {
		return contactHesabDAO;
	}
	@Autowired
	ContactService contactService;
	@Autowired
	ContactHesabDAO contactHesabDAO;

	public HesabTafsiliEntity getHesabTafsiliByContact(ContactEntity contactEntity, SaalMaaliEntity saalMaali) {

		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("contact.id@eq", contactEntity.getId());
		localFilter.put("saalMaali.id@eq", saalMaali.getId());
		ContactHesabEntity contactHesabEntity = load(localFilter, FlushMode.MANUAL);
		if (contactHesabEntity == null)
			throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_NoTafsiliDefinedFor", contactEntity));
		return contactHesabEntity.getHesabTafsiliOne();
	}

	public ContactHesabEntity getContactHesabByContactId(Long contactId, Long saalMaaliId) {
//		SaalMaaliEntity saalMaaliEntity = getActiveSaalMaali();
		return getContactHesabByContactIdAndSaalMaali(contactId, saalMaaliId);
	}

//	public ContactHesabEntity getContactHesabByContactId(Long contactId) {
//		SaalMaaliEntity saalMaaliEntity = getActiveSaalMaali(getCurrentOrgan());
//		return getContactHesabByContactIdAndSaalMaali(contactId, saalMaaliEntity);
//	}

	public ContactHesabEntity getContactHesabByContactIdAndSaalMaali(Long contactId, Long saalMaaliId) {
		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("contact.id@eq", contactId);
		localFilter.put("saalMaali.id@eq", saalMaaliId);
		ContactHesabEntity contactHesabEntity = load(localFilter, FlushMode.MANUAL);
		if (contactHesabEntity == null) {
			SaalMaaliEntity saalMaaliEntity = saalMaaliService.load(saalMaaliId);
			throw new NoRecordFoundException(SerajMessageUtil.getMessage("Contact_NoHesabDefinedforSaalMaali",
					contactService.load(contactId), saalMaaliEntity.getNameWithOrgan()));
		}
		return contactHesabEntity;
	}

	@Override
	public void delete(Long id) {
		ContactHesabEntity entity = load(id);
		ContactEntity contact = contactService.load(entity.getContact().getId());
		contact.setHesabTafsili(null);
		contact.setHesabMoeen(null);
		contactService.save(contact);
		super.delete(id);
	}

	@Override
	@Transactional
	public void save(ContactHesabEntity entity) {
		if (entity.getHesabTafsiliOne() != null && entity.getHesabTafsiliOne().getId() != null) {
			Map<String, Object> localFilter = new HashMap<String, Object>();
			try {
				localFilter.put("hesabTafsiliOne.id@eq", entity.getHesabTafsiliOne().getId());
				localFilter.put("saalMaali.id@eq", entity.getSaalMaali().getId());
				checkUniqueNess(entity, localFilter, false, FlushMode.MANUAL);

			} catch (DuplicateException e) {
				List<ContactHesabEntity> dataList = getDataList(localFilter);
				throw new DuplicateException(e.getDesc() + " " + dataList);
			}
		}

		Map<String, Object> contactFilter = new HashMap<String, Object>();
		contactFilter.put("contact.id@eq", entity.getContact().getId());
		contactFilter.put("saalMaali.id@eq", entity.getSaalMaali().getId());
		checkUniqueNess(entity, contactFilter, false, FlushMode.MANUAL);

		super.save(entity);
		ContactEntity contact = entity.getContact();
		if (entity.getHesabTafsiliOne() != null && entity.getHesabTafsiliOne().getId() != null)
			contact.setHesabTafsili(entity.getHesabTafsiliOne().getDesc());

		if (entity.getHesabMoeen() != null && entity.getHesabMoeen().getId() != null)
			contact.setHesabMoeen(entity.getHesabMoeen().getDesc());
		contactService.save(contact);

	}

	public List<ContactHesabEntity> getListBySaalMaali(SaalMaaliEntity saalMaali) {

		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("saalMaali.id@eq", saalMaali.getId());
		List<ContactHesabEntity> dataList = getDataList(localFilter);
		return dataList;
	}

	public List<ContactHesabEntity> getListBySaalMaali(SaalMaaliEntity saalMaali, OrganEntity organEntity) {

		HashMap<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("saalMaali.id@eq", saalMaali.getId());
		localFilter.put("contact.organId@eq", organEntity.getId());
		List<ContactHesabEntity> dataList = getDataList(localFilter);
		return dataList;
	}

	@Transactional
	public ContactHesabEntity createContactHesab(AccountingMarkazTemplateEntity accountingMarkazTemplate,
			ContactEntity contact, HesabMoeenEntity hesabMoeen, HesabTafsiliEntity hesabTafsili,
			SaalMaaliEntity saalMaali) {
		ContactHesabEntity contactHesabEntity = new ContactHesabEntity();
		contactHesabEntity.setAccountingMarkazTemplate(accountingMarkazTemplate);
		contactHesabEntity.setContact(contact);
		contactHesabEntity.setHesabMoeen(hesabMoeen);
		contactHesabEntity.setHesabTafsiliOne(hesabTafsili);
		contactHesabEntity.setSaalMaali(saalMaali);
		save(contactHesabEntity);
		return contactHesabEntity;
	}
}