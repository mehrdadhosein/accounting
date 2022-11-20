package ir.serajsamaneh;

import java.util.HashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ir.serajsamaneh.core.common.EnumListProvider;

@Configuration
public class AccountingBeansConfiguration {


	@Bean(name = "accountingEnums")
	public EnumListProvider getAccountingEnumListProvider() {
		EnumListProvider enumListProvider = new EnumListProvider();
		HashMap<String, String> enumerationClasses = new HashMap<String, String>();

		enumerationClasses.put("BedBesEnum", "ir.serajsamaneh.accounting.enumeration.BedBesEnum");
		enumerationClasses.put("SanadStateEnum", "ir.serajsamaneh.accounting.enumeration.SanadStateEnum");
		enumerationClasses.put("SanadFunctionEnum", "ir.serajsamaneh.accounting.enumeration.SanadFunctionEnum");
		enumerationClasses.put("HesabLevelEnum", "ir.serajsamaneh.accounting.enumeration.HesabLevelEnum");
		enumerationClasses.put("MarkazHazineType", "ir.serajsamaneh.accounting.enumeration.MarkazhazineType");
		enumerationClasses.put("SanadTypeEnum", "ir.serajsamaneh.accounting.enumeration.SanadTypeEnum");
		enumerationClasses.put("SaalMaaliStatusEnum", "ir.serajsamaneh.enumeration.SaalMaaliStatusEnum");
		enumerationClasses.put("HesabTafsiliCodingTypeEnum",
				"ir.serajsamaneh.accounting.enumeration.HesabTafsiliCodingTypeEnum");
		enumerationClasses.put("HesabMoeenCodingTypeEnum",
				"ir.serajsamaneh.accounting.enumeration.HesabMoeenCodingTypeEnum");

		enumListProvider.setEnumerationClasses(enumerationClasses);
		return enumListProvider;
	}

}
