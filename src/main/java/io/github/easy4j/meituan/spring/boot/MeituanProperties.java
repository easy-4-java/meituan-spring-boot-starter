package io.github.easy4j.meituan.spring.boot;

import io.github.easy4j.meituan.config.MeituanTenantConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@code meituan.*} 配置项绑定对象。
 * <p>全局参数只保存服务地址、协议、超时等所有租户共享的客户端默认值；
 * {@code developerId}、{@code signKey}、{@code appAuthToken} 等应用密钥属于租户配置，放在 {@code tenants} 中。</p>
 */
@ConfigurationProperties(MeituanProperties.PREFIX)
@Data
public class MeituanProperties {

    /**
     * 配置前缀。
     */
    public static final String PREFIX = "meituan";

    /**
     * 美团开放平台服务地址。
     */
    private String serverUrl;

    /**
     * 请求字符集。
     */
    private String charset;

    /**
     * 接口协议版本号。
     */
    private String version;

    /**
     * 连接超时时间，单位毫秒。
     */
    private Integer connectTimeout;

    /**
     * 读取超时时间，单位毫秒。
     */
    private Integer readTimeout;

    /**
     * 是否启用 SSL 证书校验。
     */
    private Boolean needSslCheck;

    /**
     * 租户配置集合，key 为业务侧租户标识，value 为该租户的美团授权配置。
     */
    private Map<String, MeituanTenantConfig> tenants = new LinkedHashMap<>();

}
