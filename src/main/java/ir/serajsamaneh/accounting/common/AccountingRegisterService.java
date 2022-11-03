package ir.serajsamaneh.accounting.common;

import org.springframework.stereotype.Service;

import ir.serajsamaneh.core.contact.contact.ContactEntity;
import ir.serajsamaneh.core.user.DefaultRegisterService;
import ir.serajsamaneh.core.user.UserEntity;

@Service
public class AccountingRegisterService extends DefaultRegisterService{

	@Override
	public ContactEntity createUserByCodeMelli(String codeMelli, String firstName, String lastName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserEntity createUserByCodeMelliAndMobile(String codeMelli, String mobile) {
		// TODO Auto-generated method stub
		return null;
	}

}
