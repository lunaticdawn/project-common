package com.project.cmn.datasource.jta;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;

@Slf4j
@AutoConfiguration
@ConditionalOnProperty(prefix = "project.xa-datasource", value = "enabled", havingValue = "true")
public class RegistryXADataSource implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {
    private XADataSourceConfig xaDataSourceConfig;

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        xaDataSourceConfig = XADataSourceConfig.init(environment);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        log.info("# RegistryXADataSource");
        // Atomikos 라이브러리가 자동으로 JTA를 구성해주기 때문에 Transaction에 대한 설정은 별도로 하지 않는다.
        this.registerXADataSource(registry);
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.debug("# postProcessBeanFactory");
    }

    /**
     * JTA 사용을 위해 {@link AtomikosDataSourceBean}에 대한 정의를 등록한다.
     *
     * @param registry {@link BeanDefinitionRegistry}
     */
    private void registerXADataSource(BeanDefinitionRegistry registry) {
        AbstractBeanDefinition beanDefinition;

        for (XADataSourceItem item : xaDataSourceConfig.getItemList()) {
            if (!item.isEnabled()) {
                continue;
            }

            beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(AtomikosDataSourceBean.class)
                    .addPropertyValue("uniqueResourceName", item.getUniqueResourceName())
                    .addPropertyValue("xaDataSourceClassName", item.getXaDataSourceClassName())
                    .addPropertyValue("xaProperties", item.getProperties())
                    .getBeanDefinition();

            log.info("# AtomikosDataSourceBean({}) Register.", item.getUniqueResourceName());
            registry.registerBeanDefinition(item.getUniqueResourceName(), beanDefinition);
        }
    }
}
