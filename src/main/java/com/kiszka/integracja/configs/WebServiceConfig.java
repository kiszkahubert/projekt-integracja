package com.kiszka.integracja.configs;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig {
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext context) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(context);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }
    @Bean(name = "conflicts")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema conflictsSchema) {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("ConflictsPort");
        definition.setLocationUri("/ws");
        definition.setTargetNamespace("http://kiszka.com/integracja");
        definition.setSchema(conflictsSchema);
        return definition;
    }
    @Bean
    public XsdSchema conflictsSchema() {
        return new SimpleXsdSchema(new org.springframework.core.io.ClassPathResource("conflicts.xsd"));
    }
}
