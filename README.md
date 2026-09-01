# meituan-spring-boot-starter

[English](./README.md) | [简体中文](./README.zh-CN.md)

[![Spring Boot 3.1.12](https://img.shields.io/badge/Spring%20Boot-3.1.12%20Line-31x-green)](https://github.com/easy-4-java/meituan-spring-boot-starter) [![Java](https://img.shields.io/badge/Java-17-orange)](https://github.com/easy-4-java/meituan-spring-boot-starter) [![License](https://img.shields.io/badge/license-Apache%202.0-green)](https://www.apache.org/licenses/LICENSE-2.0.txt)

> Spring Boot starter for the Meituan Open Platform multi-tenant SDK
> ([meituan-sdk-extension](https://github.com/easy-4-java/meituan-spring-boot-starter)): one
> dependency wires config binding, tenant storage, the official-client factory, the
> tenant-aware executor and all business services. This branch targets
> Spring Boot **3.1.12** on JDK **17**.

## Table of Contents

- [1. Project Overview](#1-project-overview)
- [2. Features & Status](#2-features--status)
- [3. Requirements & Compatibility](#3-requirements--compatibility)
- [4. Architecture & Modules](#4-architecture--modules)
- [5. Installation](#5-installation)
- [6. Quick Start](#6-quick-start)
- [7. Configuration](#7-configuration)
- [8. Core Usage / API](#8-core-usage--api)
- [9. Testing & Build](#9-testing--build)
- [10. Versioning & Branches](#10-versioning--branches)
- [11. Contributing & License](#11-contributing--license)

## 1. Project Overview

The Meituan integration is split in two artifacts:

- **`meituan-sdk-extension`** — framework-free core: tenant config model, pluggable
  tenant storage, per-tenant official-client factory/cache and 14 typed business
  facades over the official `MtOpJavaSDK`.
- **`meituan-spring-boot-starter`** (this repo) — the Spring Boot integration:
  binds `meituan.*` properties, registers every bean with
  `@ConditionalOnMissingBean` overrides, and can be switched off entirely with
  `meituan.enabled=false`.

One branch per Spring Boot version line — pick the branch that matches your
Boot version (see [section 10](#10-versioning--branches)).

## 2. Features & Status

| Area | Status |
| :--- | :--- |
| `meituan.*` configuration binding (`MeituanProperties`) | ✅ |
| Auto-registration of config / tenant storage / client factory / executor | ✅ |
| Auto-registration of all 14 `Meituan*Service` beans | ✅ |
| `@ConditionalOnMissingBean` overrides for every bean | ✅ |
| `meituan.enabled` master switch (`matchIfMissing = true`) | ✅ |
| `spring.factories` + `AutoConfiguration.imports` dual registration (Boot 2.3 – 4.x) | ✅ |
| Configuration metadata via `spring-boot-configuration-processor` | ✅ |

## 3. Requirements & Compatibility

| Dependency | Version (this branch) |
| :--- | :--- |
| Java | 17+ |
| Spring Boot | 3.1.12 |
| Core SDK | `io.github.easy4j:meituan-sdk-extension` |
| Official SDK | `com.sankuai.sjst:MtOpJavaSDK` (vendored by the core repo under `libs/`) |

## 4. Architecture & Modules

| Package / Resource | Responsibility |
| :--- | :--- |
| `io.github.easy4j.meituan.spring.boot.MeituanProperties` | `meituan.*` binding (prefix `meituan`) |
| `io.github.easy4j.meituan.spring.boot.MeituanAutoConfiguration` | Bean registration entry |
| `META-INF/spring.factories` | Boot 2.3-era auto-configuration registration |
| `META-INF/spring/...AutoConfiguration.imports` | Boot 2.7+ auto-configuration registration |

Beans registered (all overridable): `MeituanConfig`, `MeituanTenantConfigStorage`
(in-memory, seeded from `meituan.tenants.*`), `MeituanClientFactory`,
`MeituanRequestExecutor`, and the 14 business services (catering, daocan,
delivery, distribution, freetry, kemanman, kuailv, live, pay, retail, store,
tools, travel, waimai).

## 5. Installation

```xml
<dependency>
    <groupId>io.github.easy4j</groupId>
    <artifactId>meituan-spring-boot-starter</artifactId>
    <version>3.1.x.20260831-SNAPSHOT</version>
</dependency>
```

## 6. Quick Start

Add the starter, configure one tenant, inject a service:

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

## 7. Configuration

```yaml
meituan:
  enabled: true                # master switch, default true
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

## 8. Core Usage / API

Business APIs live in the core SDK — see
[meituan-sdk-extension](https://github.com/easy-4-java/meituan-sdk-extension)
for the full facade/executor documentation. From Spring you simply inject any
`Meituan*Service` (or `MeituanRequestExecutor`) and pass the `tenantId` per call.

## 9. Testing & Build

```bash
mvn -B clean verify
```

CI resolves the JDK from the branch name and installs the core SDK from source
before verifying, so no private repository access is required. JaCoCo enforces
90% line coverage on this starter.

## 10. Versioning & Branches

One branch per Spring Boot line; the version is `<line>.20260831-SNAPSHOT`:

| Branch | Spring Boot | Compile JDK | Core SDK line | Version |
| :--- | :--- | :--- | :--- | :--- |
| `feature/1.0.x` | 2.3.12.RELEASE | 1.8 | `feature/1.0.x` | `1.0.x.20260831-SNAPSHOT` |
| `feature/2.0.x` | 2.7.18 | 1.8 | `feature/1.0.x` | `2.0.x.20260831-SNAPSHOT` |
| `feature/3.0.x` | 3.0.13 | 17 | `feature/2.0.x` | `3.0.x.20260831-SNAPSHOT` |
| `feature/3.1.x` | 3.1.12 | 17 | `feature/2.0.x` | `3.1.x.20260831-SNAPSHOT` |
| `feature/3.2.x` | 3.2.12 | 17 | `feature/2.0.x` | `3.2.x.20260831-SNAPSHOT` |
| `feature/3.3.x` | 3.3.13 | 17 | `feature/2.0.x` | `3.3.x.20260831-SNAPSHOT` |
| `feature/3.4.x` | 3.4.13 | 17 | `feature/2.0.x` | `3.4.x.20260831-SNAPSHOT` |
| `feature/3.5.x` | 3.5.16 | 17 | `feature/2.0.x` | `3.5.x.20260831-SNAPSHOT` |
| `feature/4.0.x` | 4.0.7 | 21 | `feature/3.0.x` | `4.0.x.20260831-SNAPSHOT` |
| `feature/4.1.x` | 4.1.0 | 21 | `feature/3.0.x` | `4.1.x.20260831-SNAPSHOT` |

## 11. Contributing & License

Licensed under [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt).
The underlying `MtOpJavaSDK` remains proprietary to Meituan (三快科技). Issues and
PRs are welcome at
[github.com/easy-4-java/meituan-spring-boot-starter](https://github.com/easy-4-java/meituan-spring-boot-starter).
