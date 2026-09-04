# GitHub 与 Modrinth 合规报告

审查日期：2026-08-25

本报告记录 Dynamic Player Model `1.0.7+26.2` 的许可证、第三方来源、
Minecraft 品牌使用和 Modrinth 发布准备情况。它是工程审计结论，不是律师
出具的法律意见，也不能替代平台审核。

## 1. 结论

当前源码可以继续采用 MIT License，并可作为独立的客户端 Fabric Mod 公开。
发布 JAR 只包含本项目代码、语言文件、配置元数据、项目图标和必要许可证，
没有打包 Minecraft、Fabric API、Mod Menu 或三个参考 Mod 的二进制。

在创建公开 GitHub 仓库并补上真实源码、问题反馈和联系链接后，项目具备提交
Modrinth 审核的基础条件。建议首个公开版本标记为 beta，直到盔甲缩放和完整
兼容矩阵完成实际画面测试。

## 2. 参考项目许可证边界

| 项目 | 审查到的许可证 | 本项目使用方式 | 处理结果 |
| --- | --- | --- | --- |
| View Model | MIT | 第一人称变换结构有适配 | 保留作者、版权、许可证和精确源码版本 |
| ScaleMe 3.3.0 | GPL-3.0 | 仅研究功能与兼容性 | 未复制或打包代码、资源、配置或二进制 |
| More Player Models 1.20.4.20240405 | JAR 标注 CC BY-NC | 仅作视觉与功能参考 | 未复制或打包代码、模型、纹理、界面或图标 |

View Model 的 MIT 原文保存在 `LICENSES/View-Model-MIT.txt`。精确审查提交和
改编文件记录在 `THIRD_PARTY_NOTICES.md`，并同时嵌入主 JAR 与源码 JAR。

ScaleMe 的 GPL 代码没有并入本项目，因此不会把当前独立实现自动变成 GPL
衍生发行物。More Player Models 的非商业许可内容同样没有并入，避免把其
CC BY-NC 限制带入本项目的代码和发布包。

## 3. Minecraft 与品牌要求

项目名称不以 Minecraft 开头，不使用 Minecraft 官方 Logo，也不声称官方
批准。README 和 Modrinth 描述顶部均包含明确的非官方声明。

发布物只分发 Mod JAR，不分发修改后的 Minecraft 客户端、游戏文件或 Mojang
资源。项目图标是为本项目制作的独立图稿，不是从 Minecraft 或参考 Mod 中
提取的图片。MIT 许可证只覆盖项目拥有的原创部分，不授权任何 Minecraft 或
Microsoft 商标及底层游戏权利。

公开发布前仍需提供真实的维护者与联系方法。建议创建 GitHub 仓库后，把源码
地址和 Issues 地址加入 `fabric.mod.json`、Modrinth 项目链接及
`SECURITY.md`。

## 4. Modrinth 内容与元数据

- 标题、摘要和英文描述明确说明实际功能。
- 客户端标记为 Required，服务端标记为 Unsupported。
- Minecraft 版本固定为 26.2，Loader 固定为 Fabric。
- Fabric API 同时在 Fabric 元数据和 Modrinth 清单中标为必需依赖。
- Mod Menu 标为可选依赖。
- 没有遥测、分析、更新检查、远程配置、账号收集或后台网络请求。
- 没有自动输入、宏、自动点击、自动移动、数据包发送或服务器状态修改。
- 不宣传为作弊、反作弊绕过、Hypixel 批准或“绝对不会封号”。

第一人称极小物品和隐藏空手会降低屏幕遮挡。它们仍是本地渲染选项，但在某些
服务器规则下可能被解释为优势。公开描述必须保留这一限制说明；在 Hypixel
等服务器追求最低风险时，应关闭第一人称 View Model 功能。

## 5. 仓库和构建完整性

- 根许可证：`LICENSE`
- 第三方声明：`THIRD_PARTY_NOTICES.md`
- View Model MIT 原文：`LICENSES/View-Model-MIT.txt`
- 发布说明：`MODRINTH_DESCRIPTION.md`
- 发布操作清单：`PUBLISHING_CHECKLIST.md`
- 贡献权利声明：`CONTRIBUTING.md`
- 安全与隐私说明：`SECURITY.md`
- 固定 Gradle Wrapper：9.6.1，包含发行包 SHA-256 校验
- 固定 Fabric Loom：1.17.17
- GitHub Actions：Java 25 干净构建、Wrapper 验证、JAR 上传

最终两次干净构建结果一致：

- 主 JAR SHA-256:
  `0d2f24e12e44d366943cdc0fc8a91e33206b575b2ac360c2b108baf253cf4823`
- 源码 JAR SHA-256:
  `e66865dd9f43ca490db2801a1b0d9b119583427a29740ebd7e3227c9061d4be3`

## 6. 发布前必须完成

1. 创建公开 GitHub 仓库并填写真实的 source、issues 和联系地址。
2. 从干净提交构建，确保 GitHub Release、Modrinth 和本地校验的 SHA-256
   一致。
3. 只上传当前主 JAR；源码 JAR 只能作为 source 类型附加文件。
4. 不上传参考 JAR、反编译代码、设计草稿、日志、账号截图或 Minecraft 文件。
5. 使用项目自有截图，并为 Modrinth 图库填写准确标题和替代文本。
6. 首次公开版本使用 beta，完成盔甲和兼容性实际画面测试后再改为 release。
7. JAR 保持免费下载；启用平台收益、赞助、付费访问或其他商业化前，单独复核
   发布时有效的 Minecraft EULA 和 Usage Guidelines。

## 7. 依据

- Minecraft Usage Guidelines:
  https://www.minecraft.net/usage-guidelines
- Minecraft EULA:
  https://www.minecraft.net/eula
- Modrinth Content Rules:
  https://modrinth.com/legal/rules
- Modrinth Terms:
  https://modrinth.com/legal/terms
- Modrinth Additional Files:
  https://support.modrinth.com/en/articles/8793363-additional-files
- View Model source:
  https://github.com/I-No-oNe/View-Model
- ScaleMe source:
  https://github.com/KdGaming0/ScaleMe
