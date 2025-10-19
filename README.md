# WkKit (Fork Version)

![WkKit Logo](https://img.shields.io/badge/Minecraft-Plugin-blue?style=flat-square) ![Version](https://img.shields.io/badge/version-2.0.0--fork-green?style=flat-square) ![License](https://img.shields.io/badge/license-MIT-lightgrey?style=flat-square)

> **高效、强大、可定制的Minecraft礼包管理插件**
> 
> **🔔 这是 WkKit 的 Fork 版本，进行了重要功能调整**

---

## 🔄 Fork 版本说明

本版本基于原版 WkKit 1.4.5 进行修改，主要变更如下：

### ✅ 新增功能
- **集成 SweetMail**：邮件系统现在使用 [SweetMail](https://github.com/MrXiaoM/SweetMail) 插件
  - 更强大的邮件管理功能
  - 更好的界面和用户体验
  - 支持更多邮件类型和附件

### 🗑️ 移除功能
- **移除内置邮件系统**：原有的 `/wkkit mail` 命令已移除，请使用 `/mail` (SweetMail)
- **移除自动更新检测**：不再自动检查和下载更新，避免与原版冲突

### 📝 重要变更
- **版本号**：2.0.0-fork (标识为 fork 版本)
- **邮件依赖**：需要安装 SweetMail 插件才能使用邮件功能
- **优雅降级**：未安装 SweetMail 时会友好提示，不影响其他功能

### 🔗 依赖要求
- **必需**：Spigot/Paper 1.12-1.21+
- **可选**：SweetMail (用于邮件功能)
- **可选**：Vault (用于经济系统)
- **可选**：PlaceholderAPI (用于变量支持)
- **可选**：MythicMobs (用于神话怪物物品)

---

## ✨ 项目简介
WkKit 是一款专为 Minecraft 服务器打造的礼包/福利发放插件，支持多种礼包管理、CDK兑换、GUI交互、MySQL数据存储、NBT物品操作等功能，兼容 1.12-1.21+，助力服务器福利系统高效运作。

---

## 🚀 功能亮点
- **多礼包管理**：支持自定义礼包、批量发放、冷却与次数限制
- **GUI 菜单交互**：直观易用的礼包领取界面，支持自定义材质与模型ID
- **CDK兑换系统**：支持生成/兑换礼包码，灵活配置
- **SweetMail 邮件系统**：集成专业邮件插件，功能更强大 
- **MySQL/本地存储**：支持MySQL数据库与本地文件双模式，数据安全可靠
- **NBT物品支持**：全面适配NBT-API，支持自定义物品属性、头颅皮肤等
- **新人礼包/自动发放**：支持新玩家自动发放礼包，严格检测模式
- **多语言/自定义提示**：支持多语言与自定义消息
- **高兼容性**：适配多版本 Spigot/Paper/Bukkit/Folia 服务端

---

## 🛠️ 安装与配置

### 基础安装
1. **下载插件**：将 `WkKit-2.0.0-fork.jar` 放入服务器的 `plugins` 文件夹。
2. **安装 SweetMail**（可选但推荐）：
   - 下载 [SweetMail](https://github.com/MrXiaoM/SweetMail/releases) 最新版本
   - 放入 `plugins` 文件夹
   - 如果不安装，邮件功能将被禁用
3. **重启服务器**，自动生成配置文件。
4. **编辑配置**：根据需要修改配置文件：
   ```yaml
   MySQL:
     Enable: false
     databasename: 'name'
     username: 'user'
     password: 'pw'
     port: '3306'
     ip: 'localhost'
     useSSL: false
     tablePrefix: 'wkkit_'
   ```
5. **重载插件**：使用 `/wkkit reload` 应用新配置。

### 邮件系统配置
- 邮件功能由 SweetMail 管理，使用 `/mail` 命令查看邮件
- SweetMail 配置文件位于 `plugins/SweetMail/config.yml`
- 详细配置请参考 [SweetMail 文档](https://github.com/MrXiaoM/SweetMail)

---

## 📦 指令与权限

### 主要指令
- `/wkkit` - 主命令，显示帮助
- `/wkkit reload` - 重载配置
- `/wkkit create <name>` - 创建礼包
- `/wkkit delete <name>` - 删除礼包
- `/wkkit send <kit> <target> [数量]` - 发送礼包
- `/wkkit give <kit> <player> [模式]` - 直接给予礼包
- `/wkkit cdk create <数量> <礼包> <标记>` - 创建CDK
- `/wkkit cdk exchange <CDK>` - 兑换CDK
- `/mail` - 查看邮件（需要 SweetMail）

### 变更说明
- ❌ 移除：`/wkkit mail` (改用 `/mail`)
- ❌ 移除：`/wkkit update` (自动更新功能)

详细指令与权限请见 [原版 Wiki](https://github.com/WekyJay/WkKit/wiki)

---

## 🖼️ 宣传封面
> ![GUI示例](https://img.shields.io/badge/GUI-Preview-yellow?style=flat-square)
> 
> ![Photo1](https://wekyjay.github.io/WkKit_WiKi/zh_CN/images/coverimg.jpg)

---

## 📝 更新日志

### v2.0.0-fork (当前版本)
- ✨ 集成 SweetMail 邮件系统
- 🗑️ 移除内置邮件系统
- 🗑️ 移除自动更新检测功能
- 🔧 优化邮件发送逻辑，支持优雅降级
- 📝 更新文档和说明

详见原版 [CHANGELOG](https://github.com/WekyJay/WkKit) 或 Releases 页面。

---

## 🤝 联系与支持

### 原版作者
- **作者**：WekyJay
- **原版仓库**：[WkKit](https://github.com/WekyJay/WkKit)
- **QQ1️⃣群**：945144520
- **QQ2️⃣群**：60484123

### Fork 版本
- **Fork 说明**：本版本为社区 Fork，非官方版本
- **主要改动**：集成 SweetMail 邮件系统
- **反馈建议**：如遇问题请先确认是否与 SweetMail 集成有关

---

## ⚠️ 重要提示

1. **数据迁移**：从旧版本升级时，原有邮件数据无法自动迁移到 SweetMail
2. **插件依赖**：强烈建议安装 SweetMail 以获得完整功能
3. **版本识别**：本版本号为 `2.0.0-fork`，与原版不同
4. **不兼容性**：此 Fork 版本与原版在邮件系统上不兼容

---

## 📚 相关链接
- [原版 WkKit](https://github.com/WekyJay/WkKit)
- [SweetMail 插件](https://github.com/MrXiaoM/SweetMail)
- [SweetMail 文档](https://github.com/MrXiaoM/SweetMail/wiki)

---

> 本 Fork 版本已适配 SweetMail 邮件系统，欢迎使用和反馈！