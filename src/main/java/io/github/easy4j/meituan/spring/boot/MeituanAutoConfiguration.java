package io.github.easy4j.meituan.spring.boot;

import io.github.easy4j.meituan.client.DefaultMeituanRequestExecutor;
import io.github.easy4j.meituan.client.MeituanClientFactory;
import io.github.easy4j.meituan.client.MeituanRequestExecutor;
import io.github.easy4j.meituan.config.MeituanConfig;
import io.github.easy4j.meituan.config.MeituanTenantConfig;
import io.github.easy4j.meituan.service.*;
import io.github.easy4j.meituan.service.impl.*;
import io.github.easy4j.meituan.tenant.InMemoryMeituanTenantConfigStorage;
import io.github.easy4j.meituan.tenant.MeituanTenantConfigStorage;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * SDK 单入口自动初始化配置。
 * <p>业务服务只需要引入 {@code meituan-sdk-extension}，Spring Boot 会通过
 * {@code AutoConfiguration.imports} 加载该类，完成官方 client、多租户存储、请求执行器和所有业务 service 的注册。</p>
 */
@Configuration
@ConditionalOnClass(MeituanClientFactory.class)
@ConditionalOnProperty(prefix = MeituanProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(MeituanProperties.class)
public class MeituanAutoConfiguration {

    /**
     * 创建共享美团配置对象。
     *
     * @param properties 配置文件绑定对象
     * @return 用于创建官方 SDK client 的共享配置
     */
    @Bean
    @ConditionalOnMissingBean
    public MeituanConfig meituanConfig(MeituanProperties properties) {
        MeituanConfig config = new MeituanConfig();
        config.setServerUrl(properties.getServerUrl());
        config.setCharset(properties.getCharset());
        config.setVersion(properties.getVersion());
        config.setConnectTimeout(properties.getConnectTimeout());
        config.setReadTimeout(properties.getReadTimeout());
        config.setNeedSslCheck(properties.getNeedSslCheck());
        return config;
    }

    /**
     * 创建默认租户配置存储。
     * <p>默认从配置文件读取租户 Map；业务系统需要动态租户时，可以提供自己的
     * {@link MeituanTenantConfigStorage} bean 覆盖该默认实现。</p>
     *
     * @param properties 配置文件绑定对象
     * @return 租户配置存储
     */
    @Bean
    @ConditionalOnMissingBean
    public MeituanTenantConfigStorage meituanTenantConfigStorage(MeituanProperties properties) {
        return new InMemoryMeituanTenantConfigStorage(properties.getTenants());
    }

    /**
     * 创建官方 client 工厂。
     *
     * @param meituanConfig 平台级美团配置
     * @return 官方 SDK client 工厂
     */
    @Bean
    @ConditionalOnMissingBean
    public MeituanClientFactory meituanClientFactory(MeituanConfig meituanConfig) {
        return new MeituanClientFactory(meituanConfig);
    }

    /**
     * 创建多租户请求执行器。
     *
     * @param meituanClientFactory 官方 SDK client 工厂
     * @param tenantConfigStorage 租户配置存储
     * @return 多租户请求执行器
     */
    @Bean
    @ConditionalOnMissingBean
    public MeituanRequestExecutor meituanRequestExecutor(
            MeituanClientFactory meituanClientFactory,
            MeituanTenantConfigStorage tenantConfigStorage
    ) {
        return new DefaultMeituanRequestExecutor(meituanClientFactory, tenantConfigStorage);
    }

    /**
     * 创建餐饮系统业务 service。
     */
    @Bean
    @ConditionalOnBean(MeituanRequestExecutor.class)
    @ConditionalOnMissingBean
    public MeituanCateringService meituanCateringService(MeituanRequestExecutor requestExecutor) {
        return new MeituanCateringServiceImpl(requestExecutor);
    }

    /**
     * 创建到店餐饮业务 service。
     */
    @Bean
    @ConditionalOnBean(MeituanRequestExecutor.class)
    @ConditionalOnMissingBean
    public MeituanDaocanService meituanDaocanService(MeituanRequestExecutor requestExecutor) {
        return new MeituanDaocanServiceImpl(requestExecutor);
    }

    /**
     * 创建配送服务业务 service。
     */
    @Bean
    @ConditionalOnBean(MeituanRequestExecutor.class)
    @ConditionalOnMissingBean
    public MeituanDeliveryService meituanDeliveryService(MeituanRequestExecutor requestExecutor) {
        return new MeituanDeliveryServiceImpl(requestExecutor);
    }

    /**
     * 创建站外分销业务 service。
     */
    @Bean
    @ConditionalOnBean(MeituanRequestExecutor.class)
    @ConditionalOnMissingBean
    public MeituanDistributionService meituanDistributionService(MeituanRequestExecutor requestExecutor) {
        return new MeituanDistributionServiceImpl(requestExecutor);
    }

    /**
     * 创建免费试业务 service。
     */
    @Bean
    @ConditionalOnBean(MeituanRequestExecutor.class)
    @ConditionalOnMissingBean
    public MeituanFreetryService meituanFreetryService(MeituanRequestExecutor requestExecutor) {
        return new MeituanFreetryServiceImpl(requestExecutor);
    }

    /**
     * 创建客满满业务 service。
     */
    @Bean
    @ConditionalOnBean(MeituanRequestExecutor.class)
    @ConditionalOnMissingBean
    public MeituanKemanmanService meituanKemanmanService(MeituanRequestExecutor requestExecutor) {
        return new MeituanKemanmanServiceImpl(requestExecutor);
    }

    /**
     * 创建快驴业务 service。
     */
    @Bean
    @ConditionalOnBean(MeituanRequestExecutor.class)
    @ConditionalOnMissingBean
    public MeituanKuailvService meituanKuailvService(MeituanRequestExecutor requestExecutor) {
        return new MeituanKuailvServiceImpl(requestExecutor);
    }

    /**
     * 创建美团平台直播业务 service。
     */
    @Bean
    @ConditionalOnBean(MeituanRequestExecutor.class)
    @ConditionalOnMissingBean
    public MeituanLiveService meituanLiveService(MeituanRequestExecutor requestExecutor) {
        return new MeituanLiveServiceImpl(requestExecutor);
    }

    /**
     * 创建美团收单业务 service。
     */
    @Bean
    @ConditionalOnBean(MeituanRequestExecutor.class)
    @ConditionalOnMissingBean
    public MeituanPayService meituanPayService(MeituanRequestExecutor requestExecutor) {
        return new MeituanPayServiceImpl(requestExecutor);
    }

    /**
     * 创建服务零售业务 service。
     */
    @Bean
    @ConditionalOnBean(MeituanRequestExecutor.class)
    @ConditionalOnMissingBean
    public MeituanRetailService meituanRetailService(MeituanRequestExecutor requestExecutor) {
        return new MeituanRetailServiceImpl(requestExecutor);
    }

    /**
     * 创建门店基础信息直连业务 service。
     */
    @Bean
    @ConditionalOnBean(MeituanRequestExecutor.class)
    @ConditionalOnMissingBean
    public MeituanStoreService meituanStoreService(MeituanRequestExecutor requestExecutor) {
        return new MeituanStoreServiceImpl(requestExecutor);
    }

    /**
     * 创建工具型服务业务 service。
     */
    @Bean
    @ConditionalOnBean(MeituanRequestExecutor.class)
    @ConditionalOnMissingBean
    public MeituanToolsService meituanToolsService(MeituanRequestExecutor requestExecutor) {
        return new MeituanToolsServiceImpl(requestExecutor);
    }

    /**
     * 创建酒旅经营宝业务 service。
     */
    @Bean
    @ConditionalOnBean(MeituanRequestExecutor.class)
    @ConditionalOnMissingBean
    public MeituanTravelService meituanTravelService(MeituanRequestExecutor requestExecutor) {
        return new MeituanTravelServiceImpl(requestExecutor);
    }

    /**
     * 创建外卖餐饮业务 service。
     */
    @Bean
    @ConditionalOnBean(MeituanRequestExecutor.class)
    @ConditionalOnMissingBean
    public MeituanWaimaiService meituanWaimaiService(MeituanRequestExecutor requestExecutor) {
        return new MeituanWaimaiServiceImpl(requestExecutor);
    }


}
