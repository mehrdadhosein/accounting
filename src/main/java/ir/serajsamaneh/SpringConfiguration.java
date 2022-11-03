package ir.serajsamaneh;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.ResourceBundleMessageSource;

import ir.serajsamaneh.core.common.EnumListProvider;

@Configuration
@PropertySources({
    @PropertySource("classpath:/application.properties"),
    @PropertySource(value = "classpath:/application-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
})

@ComponentScan("ir")
public class SpringConfiguration {
	
	@Value("${spring.application.name}")
	String name;

    @Bean
    public MessageSource messageSource () {
    	System.out.println(name);
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("ApplicationResources_fa");
        return messageSource;
    }
    
    @Bean
    public EnumListProvider getEnumListProvider() {
    	EnumListProvider enumListProvider = new EnumListProvider();
    	HashMap<String, String> enumerationClasses = new HashMap<String, String>();

		enumerationClasses.put("BedBesEnum","ir.serajsamaneh.accounting.enumeration.BedBesEnum");
		enumerationClasses.put("SanadStateEnum","ir.serajsamaneh.accounting.enumeration.SanadStateEnum");
		enumerationClasses.put("SanadFunctionEnum","ir.serajsamaneh.accounting.enumeration.SanadFunctionEnum");
		enumerationClasses.put("HesabLevelEnum","ir.serajsamaneh.accounting.enumeration.HesabLevelEnum");
		enumerationClasses.put("MarkazHazineType","ir.serajsamaneh.accounting.enumeration.MarkazhazineType");
		enumerationClasses.put("SanadTypeEnum","ir.serajsamaneh.accounting.enumeration.SanadTypeEnum");
		enumerationClasses.put("SaalMaaliStatusEnum","ir.serajsamaneh.enumeration.SaalMaaliStatusEnum");
		enumerationClasses.put("HesabTafsiliCodingTypeEnum","ir.serajsamaneh.accounting.enumeration.HesabTafsiliCodingTypeEnum");
		enumerationClasses.put("HesabMoeenCodingTypeEnum","ir.serajsamaneh.accounting.enumeration.HesabMoeenCodingTypeEnum");
		
		enumListProvider.setEnumerationClasses(enumerationClasses);
		return enumListProvider;
    }

}
