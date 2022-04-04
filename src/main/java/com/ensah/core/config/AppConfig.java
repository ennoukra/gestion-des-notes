package com.ensah.core.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.support.OpenSessionInViewInterceptor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;


//Configuration d'une application Spring MVC (@EnableWebMvc)
//Avec support des transactions (@EnableTransactionManagement)

@EnableWebMvc // Configuration d'une Application Spring MVC
@Configuration // Classe de configuration qui va contenir des bean à créer automatiquement par
				// Spring
@ComponentScan(basePackages = { "com.ensah" }) // Packages à scanner pour chercher les bean spring de type component
												// (càd controller, repository, service)
@EnableTransactionManagement // support des transactions
public class AppConfig implements WebMvcConfigurer {

	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	public AppConfig() {

		// On enregistre une trace dans le journal
		LOGGER.debug(" configuration init...");
	}

	// set up a logger for diagnostics

	public void configureViewResolvers(ViewResolverRegistry registry) {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/jsp/view/");
		resolver.setSuffix(".jsp");
		resolver.setViewClass(JstlView.class);
		registry.viewResolver(resolver);
	}
	   
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(getDataSource());
		em.setPackagesToScan(new String[] { "com.ensah.core.bo" });

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalProperties());

		return em;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

		return transactionManager;
	}

	Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", "update");
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MariaDB103Dialect");

		return properties;
	}

//
//	// Configuration de la Session Factory de Hibernate
//	@Bean // Nécessaire pour que Spring créra automatiquemnt la sessionFactory
//	public LocalSessionFactoryBean sessionFactory() {
//
//		// Code copié de la documentation
//
//		final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//
//		sessionFactory.setDataSource(getDataSource());
//		sessionFactory.setHibernateProperties(hibernateProperties());
//
//		// TODO: Indiquer vos classes annotées par @Entity ici
//		sessionFactory.setAnnotatedClasses(Absence.class, CadreAdministrateur.class, Compte.class, Conversation.class,
//				Coordination.class, Enseignant.class, Etudiant.class, Filiere.class, Inscription.class,
//				JournalisationEvenements.class, Matiere.class, Message.class, Module.class, Niveau.class,
//				Notification.class, PieceJustificative.class, Role.class, TypeSeance.class, Utilisateur.class, Livre.class, Auteur.class, Emprunt.class);
//
//		// Tracer dans le journal pour des raisons juste de débougage
//		// que la session Factory a été bien crée
//		if (sessionFactory != null) {
//			LOGGER.debug(" sessionFactory created ...");
//		}
//
//		return sessionFactory;
//	}

	// Configuration de la source de données
	@Bean // nécessaire car Spring va créer la datasource automatiquement et l'injecter
			// apres dans la session factory
	public DataSource getDataSource() {

		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName("org.mariadb.jdbc.Driver");
		dataSourceBuilder.url("jdbc:mysql://localhost:3306/gs_notes_database");
		dataSourceBuilder.username("root");
		dataSourceBuilder.password("");
		return dataSourceBuilder.build();

	}

	// Permet de traduire toutes les exceptions de la couche persistance en une
	// seule exception
	// de type PersistenceExceptionTranslationPostProcessor (embalage des execptions
	// de la couche de persistance)
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	// Spring security Custom Success Handler: Permet de définir un gestionnaire
	// personnalisé pour la
	// redirection après authenitication avec succès
	//
	@Bean // nécessaire car c'est Spring qui créer automatiquement cette classe de type
			// MySimpleUrlAuthenticationSuccessHandler
	public AuthenticationSuccessHandler redirectionAfterAuthenticationSuccessHandler() {
		return new RedirectionAfterAuthenticationSuccessHandler();
	}

	// Permet d'éviter les exception Lazy
//	public void addInterceptors(InterceptorRegistry registry) {
//		OpenSessionInViewInterceptor openSessionInViewInterceptor = new OpenSessionInViewInterceptor();
//		openSessionInViewInterceptor.setSessionFactory(sessionFactory().getObject());
//		
//		registry.addWebRequestInterceptor(openSessionInViewInterceptor).addPathPatterns("/**").order(Ordered.HIGHEST_PRECEDENCE);
//	}

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}
}