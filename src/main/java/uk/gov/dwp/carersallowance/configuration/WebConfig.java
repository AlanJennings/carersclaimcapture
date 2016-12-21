package uk.gov.dwp.carersallowance.configuration;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import uk.gov.dwp.carersallowance.handler.FilteredRequestMappingHandlerMapping;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"uk.gov.dwp.carersallowance", "gov.dwp.carers"})
public class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/javascript/**").addResourceLocations("classpath:/resources/javascript/", "/resources/javascript/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/resources/images/", "/resources/images/");
        registry.addResourceHandler("/stylesheet/**").addResourceLocations("classpath:/resources/stylesheet/", "/resources/stylesheet/");
        registry.addResourceHandler("/icons/**").addResourceLocations("classpath:/resources/icons/", "/resources/icons/");
        registry.addResourceHandler("/assets/images/**").addResourceLocations("classpath:/resources/images/", "/resources/images/");
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    // TODO remove or block this page, before deployment
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
      registry.addViewController("/").setViewName("/index");
    }

    @Bean
    public FilteredRequestMappingHandlerMapping addRequestHandler() {
        FilteredRequestMappingHandlerMapping filteredRequestMappingHandlerMapping = new FilteredRequestMappingHandlerMapping();
        filteredRequestMappingHandlerMapping.setOrder(0);
        filteredRequestMappingHandlerMapping.setExclude("/javascript/**");
        filteredRequestMappingHandlerMapping.setExclude("/stylesheet/**");
        filteredRequestMappingHandlerMapping.setExclude("/images/**");
        filteredRequestMappingHandlerMapping.setExclude("/icons/**");
        filteredRequestMappingHandlerMapping.setExclude("/assets/images/**");
        filteredRequestMappingHandlerMapping.setExclude("/async-submitting");
        return filteredRequestMappingHandlerMapping;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        final LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        return localeChangeInterceptor;
    }

    @Bean(name = "localeResolver")
    public CookieLocaleResolver localeResolver() {
        final CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        final Locale defaultLocale = new Locale("en");
        localeResolver.setDefaultLocale(defaultLocale);
        localeResolver.setCookieName("c3LocaleCookie");
        localeResolver.setCookieMaxAge(3600);
        return localeResolver;
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:messages","classpath:circs_messages");
        messageSource.setCacheSeconds(10); //reload messages every 10 seconds
        return messageSource;
    }
}