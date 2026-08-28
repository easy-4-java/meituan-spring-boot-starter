package io.github.easy4j.meituan.spring.boot;

import io.github.easy4j.meituan.config.MeituanConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 验证 {@code meituan.enabled=false} 时 Starter 整体关闭。
 */
class MeituanAutoConfigurationDisabledTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(MeituanAutoConfiguration.class));

    @Test
    void shouldNotRegisterAnythingWhenDisabled() {
        runner.withPropertyValues("meituan.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(MeituanAutoConfiguration.class);
                    assertThat(context).doesNotHaveBean(MeituanConfig.class);
                });
    }
}
