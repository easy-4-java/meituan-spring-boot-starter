# meituan-spring-boot-starter

[English](./README.md) | [简体中文](./README.zh-CN.md)

[![Java](https://img.shields.io/badge/Java-8-orange)](https://github.com/easy-4-java/meituan-spring-boot-starter) [![License](https://img.shields.io/badge/license-Apache%202.0-green)](https://www.apache.org/licenses/LICENSE-2.0.txt)

> 美团开放平台多租户 SDK（[meituan-sdk-extension](https://github.com/easy-4-java/meituan-sdk-extension)）
> 的 Spring Boot Starter：一个依赖完成 `meituan.*` 配置绑定、租户存储、官方 client
> 工厂、租户感知执行器与全部业务 service 的装配。本分支面向 Spring Boot
> **2.3.12.RELEASE** / JDK **8**。

## 目录

- [1. 项目概述](#1-项目概述)
- [2. 功能与状态](#2-功能与状态)
- [3. 环境要求与兼容性](#3-环境要求与兼容性)
- [4. 架构与模块](#4-架构与模块)
- [5. 安装](#5-安装)
- [6. 快速开始](#6-快速开始)
- [7. 配置](#7-配置)
- [8. 核心用法 / API](#8-核心用法--api)
- [9. 测试与构建](#9-测试与构建)
- [10. 版本与分支](#10-版本与分支)
- [11. 贡献与许可](#11-贡献与许可)

## 1. 项目概述

美团集成拆分为两个构件：

- **`meituan-sdk-extension`** —— 无框架核心：租户配置模型、可插拔租户存储、
  按租户创建并缓存的官方 client 工厂，以及基于官方 `MtOpJavaSDK` 的 14 个
  强类型业务门面。
- **`meituan-spring-boot-starter`**（本仓库）—— Spring Boot 集成层：绑定
  `meituan.*` 配置，以 `@ConditionalOnMissingBean` 注册全部 Bean（均可覆盖），
  并可通过 `meituan.enabled=false` 整体关闭。

每个 Spring Boot 版本线一条分支 —— 请按你的 Boot 版本选择分支
（见[第 10 节](#10-版本与分支)）。

## 2. 功能与状态

| 能力 | 状态 |
| :--- | :--- |
| `meituan.*` 配置绑定（`MeituanProperties`） | ✅ |
| 配置 / 租户存储 / client 工厂 / 执行器自动注册 | ✅ |
| 全部 14 个 `Meituan*Service` Bean 自动注册 | ✅ |
| 每个 Bean 均可 `@ConditionalOnMissingBean` 覆盖 | ✅ |
| `meituan.enabled` 总开关（缺省开启） | ✅ |
| `spring.factories` + `AutoConfiguration.imports` 双注册（Boot 2.3 – 4.x） | ✅ |
| `spring-boot-configuration-processor` 生成配置元数据 | ✅ |

## 3. 环境要求与兼容性

| 依赖 | 版本（本分支） |
| :--- | :--- |
| Java | 1.8+ |
| Spring Boot | 2.3.12.RELEASE |
| 核心 SDK | `io.github.easy4j:meituan-sdk-extension` |
| 官方 SDK | `com.sankuai.sjst:MtOpJavaSDK`（由核心仓库以 `libs/` 内置） |

## 4. 架构与模块

| 包 / 资源 | 职责 |
| :--- | :--- |
| `io.github.easy4j.meituan.spring.boot.MeituanProperties` | `meituan.*` 配置绑定（前缀 `meituan`） |
| `io.github.easy4j.meituan.spring.boot.MeituanAutoConfiguration` | Bean 注册入口 |
| `META-INF/spring.factories` | Boot 2.3 时代的自动装配注册 |
| `META-INF/spring/...AutoConfiguration.imports` | Boot 2.7+ 的自动装配注册 |

注册的 Bean（全部可覆盖）：`MeituanConfig`、`MeituanTenantConfigStorage`
（内存实现，由 `meituan.tenants.*` 播种）、`MeituanClientFactory`、
`MeituanRequestExecutor`，以及 14 个业务 service（餐饮、到店餐饮、配送、
分销、免费试、客满满、快驴、直播、支付、零售、门店、工具、酒旅、外卖）。

## 5. 安装

```xml
<dependency>
    <groupId>io.github.easy4j</groupId>
    <artifactId>meituan-spring-boot-starter</artifactId>
    <version>1.0.x.20260630-SNAPSHOT</version>
</dependency>
```

## 6. 快速开始

引入 starter，配置一个租户，然后直接注入 service：

```java
@Service
public class OrderService {

    private final MeituanWaimaiService waimaiService;

    public OrderService(MeituanWaimaiService waimaiService) {
        this.waimaiService = waimaiService;
    }

    public MeituanResponse<?> queryOrder(String orderId, String tenantId) {
        return waimaiService.orderQueryById(request, tenantId);
    }
}
```

## 7. 配置

```yaml
meituan:
  enabled: true                # 总开关，默认 true
  server-url: https://api-open-cater.meituan.com
  charset: UTF-8
  version: "2"
  connect-timeout: 5000
  read-timeout: 10000
  tenants:
    tenant-a:
      app-id: app-a
      developer-id: 100000
      sign-key: your-sign-key
      app-auth-token: token-a
      business-id: 16
```

## 8. 核心用法 / API

业务 API 位于核心 SDK —— 完整的门面/执行器文档见
[meituan-sdk-extension](https://github.com/easy-4-java/meituan-spring-boot-starter)。
在 Spring 中直接注入任意 `Meituan*Service`（或 `MeituanRequestExecutor`），
调用时传入 `tenantId` 即可。

## 9. 测试与构建

```bash
mvn -B clean verify
```

CI 会根据分支名推导 JDK，并在校验前从源码安装核心 SDK，因此无需访问任何
私有仓库。JaCoCo 对本 starter 强制 90% 行覆盖率。

## 10. 版本与分支

每个 Spring Boot 版本线一条分支；版本号为 `<line>.20260630-SNAPSHOT`：

| 分支 | Spring Boot | 编译 JDK | 核心 SDK 分支 | 版本 |
| :--- | :--- | :--- | :--- | :--- |
| `feature/1.0.x` | 2.3.12.RELEASE | 1.8 | `feature/1.0.x` | `1.0.x.20260630-SNAPSHOT` |
| `feature/2.0.x` | 2.7.18 | 1.8 | `feature/1.0.x` | `2.0.x.20260630-SNAPSHOT` |
| `feature/3.0.x` | 3.0.13 | 17 | `feature/2.0.x` | `3.0.x.20260630-SNAPSHOT` |
| `feature/3.1.x` | 3.1.12 | 17 | `feature/2.0.x` | `3.1.x.20260630-SNAPSHOT` |
| `feature/3.2.x` | 3.2.12 | 17 | `feature/2.0.x` | `3.2.x.20260630-SNAPSHOT` |
| `feature/3.3.x` | 3.3.13 | 17 | `feature/2.0.x` | `3.3.x.20260630-SNAPSHOT` |
| `feature/3.4.x` | 3.4.13 | 17 | `feature/2.0.x` | `3.4.x.20260630-SNAPSHOT` |
| `feature/3.5.x` | 3.5.16 | 17 | `feature/2.0.x` | `3.5.x.20260630-SNAPSHOT` |
| `feature/4.0.x` | 4.0.7 | 21 | `feature/3.0.x` | `4.0.x.20260630-SNAPSHOT` |
| `feature/4.1.x` | 4.1.0 | 21 | `feature/3.0.x` | `4.1.x.20260630-SNAPSHOT` |

## 11. 贡献与许可

基于 [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt) 开源。
底层 `MtOpJavaSDK` 仍为美团（三快科技）专有软件。欢迎在
[github.com/easy-4-java/meituan-spring-boot-starter](https://github.com/easy-4-java/meituan-spring-boot-starter)
提交 Issue 与 PR。
