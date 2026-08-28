package io.github.easy4j.meituan.spring.boot;

import io.github.easy4j.meituan.client.MeituanRequestExecutor;
import io.github.easy4j.meituan.config.MeituanConfig;
import io.github.easy4j.meituan.service.MeituanWaimaiService;
import io.github.easy4j.meituan.tenant.MeituanTenantConfigStorage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Starter 自动配置基础测试：验证 {@code meituan.*} 配置绑定与核心 Bean 注册。
 */
@SpringBootTest(classes = MeituanAutoConfiguration.class, properties = {
        "meituan.enabled=true",
        "meituan.server-url=https://api-open-cater.meituan.com",
        "meituan.charset=UTF-8",
        "meituan.version=2",
        "meituan.connect-timeout=5000",
        "meituan.read-timeout=10000",
        "meituan.need-ssl-check=true",
        "meituan.tenants.tenant-a.app-id=app-a",
        "meituan.tenants.tenant-a.developer-id=100000",
        "meituan.tenants.tenant-a.sign-key=your-sign-key",
        "meituan.tenants.tenant-a.app-auth-token=token-a",
        "meituan.tenants.tenant-a.business-id=16"
})
class MeituanAutoConfigurationTest {

    @Autowired
    private MeituanProperties properties;

    @Autowired
    private MeituanConfig meituanConfig;

    @Autowired
    private MeituanTenantConfigStorage tenantConfigStorage;

    @Autowired
    private MeituanRequestExecutor requestExecutor;

    @Autowired
    private MeituanWaimaiService waimaiService;

    @Test
    void shouldBindMeituanProperties() {
        assertEquals("https://api-open-cater.meituan.com", properties.getServerUrl());
        assertEquals("UTF-8", properties.getCharset());
        assertEquals("2", properties.getVersion());
        assertEquals(5000, properties.getConnectTimeout());
        assertEquals(10000, properties.getReadTimeout());
        assertTrue(properties.getNeedSslCheck());
        assertEquals(1, properties.getTenants().size());
        assertEquals("app-a", properties.getTenants().get("tenant-a").getAppId());
        assertEquals(Long.valueOf(100000L), properties.getTenants().get("tenant-a").getDeveloperId());
        assertEquals("your-sign-key", properties.getTenants().get("tenant-a").getSignKey());
        assertEquals("token-a", properties.getTenants().get("tenant-a").getAppAuthToken());
        assertEquals(Integer.valueOf(16), properties.getTenants().get("tenant-a").getBusinessId());
    }

    @Test
    void shouldRegisterCoreBeans() {
        assertNotNull(meituanConfig);
        assertEquals("https://api-open-cater.meituan.com", meituanConfig.getServerUrl());
        assertNotNull(tenantConfigStorage);
        assertNotNull(requestExecutor);
        assertNotNull(waimaiService);
    }

    @Test
    void shouldResolveTenantConfigFromStorage() {
        assertTrue(tenantConfigStorage.findByTenantId("tenant-a").isPresent());
        assertEquals("token-a", tenantConfigStorage.findByTenantId("tenant-a").get().getAppAuthToken());
    }
}
