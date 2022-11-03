package ir.serajsamaneh;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ir.serajsamaneh.core.base.SerajInterceptor;

//@PropertySource("classpath:database.properties")
@Configuration
@EnableTransactionManagement
public class HibernateConfiguration {

	@Value("${hibernate.connection.url}")
	String url;
	
	@Value("${hibernate.connection.driver_class}")
	String driverClassName;
	
	@Value("${hibernate.default_schema}")
	String defaultSchema;
	
	@Value("${hibernate.connection.username}")
	String userName;
	
	@Value("${hibernate.connection.password}")
	String password;
	
	@Value("${datasource.initialSize:100}")
	Integer initialSize;
	
	@Value("${datasource.maxTotal:500}")
	Integer maxTotal;
	
	@Value("${datasource.minIdle:240}")
	Integer minIdle;

	@Value("${hibernate.hbm2ddl.auto}")
	String hbm2ddl;
	
	@Value("${hibernate.dialect}")
	String dialect;
	
	@Value("${hibernate.show_sql}")
	String showSql;
	
	 @Autowired
	 private ResourceLoader rl;
	 
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
    	try {
	        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
	        sessionFactory.setDataSource(dataSource());
	        sessionFactory.setPackagesToScan("ir.serajsamaneh","ir.sibaar");
	        Resource[] resources = null;
	//      Resource[] resources = new Resource[] { new ClassPathResource("/config/hibernate/*.hbm.xml") };
			resources = ResourcePatternUtils.getResourcePatternResolver(rl)
					      .getResources("classpath:/config/hibernate/*.hbm.xml");
			sessionFactory.setMappingLocations(resources);
			
			Resource[] jarResources = ResourcePatternUtils.getResourcePatternResolver(rl)
				      .getResources("/WEB-INF/lib/commoncore*.jar");
			sessionFactory.setMappingJarLocations(jarResources);
	        sessionFactory.setHibernateProperties(hibernateProperties());
	        sessionFactory.setEntityInterceptor(serajInterceptor());
	        
	        return sessionFactory;
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	return null;
    }
    
    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new org.apache.commons.dbcp2.BasicDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxTotal(maxTotal);
        dataSource.setMinIdle(minIdle);
        return dataSource;
    }
    
    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager
          = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
    
    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", hbm2ddl);
        hibernateProperties.setProperty("hibernate.dialect", dialect);
        hibernateProperties.setProperty("hibernate.show_sql", showSql);
//        hibernateProperties.setProperty("hibernate.default_schema",defaultSchema );
        hibernateProperties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        hibernateProperties.setProperty("net.sf.ehcache.configurationResourceName", "ehcache.xml");
        hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", "true");
        hibernateProperties.setProperty("hibernate.cache.use_query_cache", "true");
        hibernateProperties.setProperty("hibernate.mapping.precedence", "class,hbm");

        return hibernateProperties;
    }
    
    @Bean
    public SerajInterceptor serajInterceptor() {
    	SerajInterceptor serajInterceptor = new SerajInterceptor();
        return serajInterceptor;
    }
}
