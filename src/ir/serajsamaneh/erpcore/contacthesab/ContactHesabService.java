package ir.serajsamaneh.erpcore.contacthesab;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.springframework.transaction.annotation.Transactional;

public class ContactHesabService extends
		BaseAccountingService<ContactHesabEntity, Long> {

	@Override
	protected ContactHesabDAO getMyDAO() {
		return contactHesabDAO;
	}

	ContactService contactService;
	public ContactService getContactService() {
		return contactService;
	}

	public void setContactService(ContactService contactService) {
		this.contactService = contactService;
	}

	ContactHesabDAO contactHesabDAO;

	public void setContactHesabDAO(ContactHesabDAO contactHesabDAO) {
		this.contactHesabDAO = contactHesabDAO;
	}

	public ContactHesabDAO getContactHesabDAO() {
		return contactHesabDAO;
	}

	public HesabTafsiliEntity getHesabTafsiliByContact(ContactEntity contactEntity, SaalMaaliEntity saalMaali) {
		
		HashMap<String,Object> localFilter = new HashMap<String, Object>();
		localFilter.put("contact.id@eq", contactEntity.getId());
		localFilter.put("saalMaali.id@eq", saalMaali.getId());
		ContactHesabEntity contactHesabEntity = load(null, localFilter, FlushMode.MANUAL);
		if(contactHesabEntity == null)
			throw new FatalException(SerajMessageUtil.getMessage("SanadHesabdari_NoTafsiliDefinedFor",contactEntity));
		return contactHesabEntity.getHesabTafsiliOne();
	}

	public ContactHesabEntity getContactHesabByContactId(Long contactId, SaalMaaliEntity saalMaaliEntity) {
//		SaalMaaliEntity saalMaaliEntity = getActiveSaalMaali();
		return getContactHesabByContactIdAndSaalMaali(contactId, saalMaaliEntity);
	}

//	public ContactHesabEntity getContactHesabByContactId(Long contactId) {
//		SaalMaaliEntity saalMaaliEntity = getActiveSaalMaali(getCurrentOrgan());
//		return getContactHesabByContactIdAndSaalMaali(contactId, saalMaaliEntity);
//	}
	
	public ContactHesabEntity getContactHesabByContactIdAndSaalMaali(Long contactId,	SaalMaaliEntity saalMaali) {
		HashMap<String,Object> localFilter = new HashMap<String, Object>();
		localFilter.put("contact.id@eq", contactId);
		localFilter.put("saalMaali.id@eq", saalMaali.getId());
		ContactHesabEntity contactHesabEntity = load(null, localFilter, FlushMode.MANUAL);
		if(contactHesabEntity == null)
			throw new NoRecordFoundException(SerajMessageUtil.getMessage("Contact_NoHesabDefinedforSaalMaali",getContactService().load(contactId), saalMaali.getNameWithOrgan()));
		
		return contactHesabEntity;
	}

	
	@Override
	public void delete(Long id) {
		ContactHesabEntity entity = load(id);
		ContactEntity contact = getContactService().load(entity.getContact().getId());
		contact.setHesabTafsili(null);
		contact.setHesabMoeen(null);
		getContactService().save(contact);
		super.delete(id);
	}
	@Override
	@Transactional
	public void save(ContactHesabEntity entity) {
		if(entity.getHesabTafsiliOne()!=null && entity.getHesabTafsiliOne().getId()!=null){
			Map<String, Object> localFilter = new HashMap<String, Object>();
			try{
				localFilter.put("hesabTafsiliOne.id@eq", entity.getHesabTafsiliOne().getId());
				localFilter.put("saalMaali.id@eq", entity.getSaalMaali().getId());
				checkUniqueNess(entity, localFilter, false, FlushMode.MANUAL);
			}catch(DuplicateException e){
				List<ContactHesabEntity> dataList = getDataList(null, localFilter);
				throw new DuplicateException(e.getDesc()+" "+dataList);
			}
		}
		
		super.save(entity);
		ContactEntity contact = entity.getContact();
		if(entity.getHesabTafsiliOne()!=null && entity.getHesabTafsiliOne().getId()!=null)
			contact.setHesabTafsili(entity.getHesabTafsiliOne().getDesc());
		
		if(entity.getHesabMoeen()!=null && entity.getHesabMoeen().getId()!=null)
			contact.setHesabMoeen(entity.getHesabMoeen().getDesc());
		getContactService().save(contact);
		
	}

	public List<ContactHesabEntity> getListBySaalMaali(SaalMaaliEntity saalMaali) {
		
		HashMap<String,Object> localFilter = new HashMap<String, Object>();
		localFilter.put("saalMaali.id@eq", saalMaali.getId());
		List<ContactHesabEntity> dataList = getDataList(null, localFilter);
		return dataList;
	}

	public List<ContactHesabEntity> getListBySaalMaali(SaalMaaliEntity saalMaali, OrganEntity organEntity) {
		
		HashMap<String,Object> localFilter = new HashMap<String, Object>();
		localFilter.put("saalMaali.id@eq", saalMaali.getId());
		localFilter.put("contact.organ.id@eq", organEntity.getId());
		List<ContactHesabEntity> dataList = getDataList(null, localFilter);
		return dataList;
	}
	
	@Transactional
	public ContactHesabEntity createContactHesab(AccountingMarkazTemplateEntity accountingMarkazTemplate, ContactEntity contact, HesabMoeenEntity hesabMoeen, HesabTafsiliEntity hesabTafsili, SaalMaaliEntity saalMaali){
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