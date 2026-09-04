# Dynamic Player Model 最终审计报告

## 1. 审计对象

| 项目 | 内容 |
| --- | --- |
| 审计日期 | 2026-08-25 |
| Mod 版本 | 1.0.7+26.2 |
| Minecraft | 26.2 |
| Fabric Loader | 0.19.3 |
| Java | 25 |
| 发布文件 | `release/DynamicPlayerModel-1.0.7+26.2.jar` |
| SHA-256 | `0d2f24e12e44d366943cdc0fc8a91e33206b575b2ac360c2b108baf253cf4823` |

审计范围包括全部 Java 源码、三个 Mixin、配置迁移与设置界面、发布 JAR 内容、许可证和第三方来源、Minecraft 26.2 目标方法字节码、开发客户端启动日志、用户既有完整 Mod 实例日志、Modrinth 规则、Minecraft 发布规范，以及 Hypixel 官方规则。

## 2. 结论

1. 未发现仍可由静态检查或启动验证确认的高严重度、崩溃级或数据破坏级缺陷。
2. 已确认并修复地图被自定义第一人称矩阵破坏的问题；持图时全部 View Model 变换自动停用，并增加原版单手/双手地图模式。
3. Mod 自身的 Watchdog 协议风险为低：没有自定义数据包、网络监听、自动点击、宏、移动、攻击、交互或物品操作代码。
4. Hypixel 规则风险不能判定为零。第三人称仅缩放自己且不改相机、碰撞箱和属性，最接近官方允许的纯外观类别；第一人称极小物品、位置移动和隐藏空手会减少画面遮挡，属于没有得到 Hypixel 单独批准的模糊区。
5. 发布候选 JAR 可交付。追求最低 Hypixel 风险时，应关闭“启用视角模型”，保持“其他玩家”和“盔甲”关闭。

## 3. 审计发现

### M1：第一人称减少遮挡存在规则解释风险

**严重度：中，属于服务器政策风险，不是反作弊数据异常。**

`ItemInHandRendererMixin` 只取消空手渲染或给当前手的 PoseStack 加视觉矩阵；`ViewModelTransform` 允许把手和物品缩到 1%、移动和旋转。它不改变相机位置、FOV、攻击距离或服务器数据，但极小物品和隐藏空手会减少原版画面遮挡。

Hypixel 的 Allowed Modifications 允许纯外观修改，但要求不得带来显著优势、不得改变玩家视角以看到原本看不到的内容；不明确属于允许类别的 Mod 应默认视为不允许。Hypixel 同时声明不会逐版本批准 Mod，所有使用均由玩家自行承担风险。

用户当前 Skyblock 实例配置为：

- `viewModelEnabled: true`
- `mainHandScale: 0.10`
- 主手位置约为 `X -0.30 / Y -0.11 / Z -0.40`
- `hideEmptyHands: false`

其中 10% 主手与明显移位不是最保守的 Hypixel 配置。最低风险做法是直接关闭“启用视角模型”；仅关闭“隐藏空手”并不能消除极小物品减少遮挡的模糊性。

### L1：盔甲开启状态仍缺少完整世界内视觉矩阵回归

**严重度：低，默认不触发。**

旧盔甲错位实现已被替换为每个装备槽独立的 PoseStack 变换。Minecraft 26.2 字节码确认四个槽位依次调用同一 `renderArmorPiece`，当前 Mixin 在每次调用范围内压栈和出栈，不再修改共享盔甲模型部件。

数学锚点和调用生命周期检查通过，但本轮没有重新拍摄头盔、胸甲、护腿、靴子在多组头身比例下的完整视觉矩阵。为避免影响普通使用，`scaleArmor` 的默认值和旧配置迁移值均为 `false`。在完成逐项视觉回归前，建议保持关闭。

### L2：1.0.7 尚未重新跑完整 121 Mod 组合

**严重度：低。**

用户实例的 1.0.4 日志显示 121 个 Mod 一同加载、连接 `mc.hypixel.net` 并运行，未出现 Dynamic Player Model 的 Mixin、类加载、PoseStack 或崩溃错误。1.0.7 修改了 `ItemInHandRenderer` 的地图分支，因此旧日志只能作为相邻兼容证据，不能代替当前版本的完整组合回归。

因此不能把“1.0.7 已逐项运行所有其他 Mod 功能”写成已完成。日志中的资源模型越界、亮度越界和 Skyblocker HTTP 超时不含本 Mod 调用栈，不归因于本 Mod。

## 4. 本轮修复

| 问题 | 原因 | 修复 |
| --- | --- | --- |
| 设置页内按 F8 可无限嵌套 | 每次热键都用当前 Screen 创建新设置页 | 当前 Screen 已是本 Mod 设置页时忽略重复打开 |
| 关闭设置页写两次 JSON | `onClose()` 与 `removed()` 都调用保存 | 只保留 `removed()` 保存 |
| 损坏 JSON 每次启动都再次报错 | 异常分支只恢复内存默认值 | 恢复默认值后立即写回可解析 JSON |
| 地图显示受 View Model 矩阵影响 | 地图与普通手持物品共用缩放、位移和旋转入口 | 任一手持图时同时绕过两只手的全部 View Model 变换和空手覆盖 |
| 地图手势不能选择 | 原版根据主手地图与空副手自动选择双手分支 | 设置页可选择原版单手或双手；副手有物品时始终保留单手 |

相关源码：

- `DynamicPlayerModelClient.java:41`
- `DynamicPlayerModelConfigScreen.java:179`
- `PlayerScaleConfig.java`
- `ItemInHandRendererMixin.java`
- `ViewModelTransform.java`

## 5. 服务端与反作弊审计

| 检查项 | 结果 | 证据 |
| --- | --- | --- |
| 运行环境 | 仅客户端 | `fabric.mod.json` 的 `environment` 为 `client` |
| 自定义网络包 | 不存在 | 源码和发布类依赖扫描无 networking、packet、payload 或 connection 调用 |
| Mod 列表上报 | 不存在 | 没有 join 回调、品牌包或自定义通道 |
| 自动操作 | 不存在 | 没有 attack、interact、clickSlot、按键模拟或宏逻辑 |
| 碰撞箱与实体尺寸 | 不修改 | 仅写 `ModelPart` 比例/位置和 `PoseStack` |
| 相机与视角 | 不修改 | 没有 Camera、FOV、eye height 或第三人称相机调用 |
| 触及与战斗属性 | 不修改 | 没有 reach、attribute、damage、speed 或 cooldown 调用 |
| 后台线程/HTTP | 不存在 | 没有线程、执行器、URL、Socket 或 HTTP 客户端 |
| 每 Tick 工作 | 仅消费两个快捷键队列 | 未按键时不扫描玩家、实体或文件 |

从 Watchdog 的服务器数据视角看，本 Mod 不产生区别于原版的移动、攻击、交互或自定义协议数据，所以没有发现会直接触发“异常数据包”检测的路径。

## 6. 模型正确性检查

| 要求 | 结果 |
| --- | --- |
| 脚踩地面 | 以原版脚底 Y 为固定锚点，反算缩放后腿与躯干位置 |
| 头连接身体 | 头部枢轴设置为缩放后躯干顶部 |
| 手臂连接肩部 | 使用缩放后躯干顶部和肩部偏移 |
| 外层皮肤 | 帽子、袖子、裤腿和外套作为父部件子层继承比例，避免双重缩放 |
| 第三人称手持物 | 通过手臂 `translateToHand` 的比例继承；关闭开关时临时恢复原版比例 |
| 第一/第三人称独立 | 第一人称只注入 `ItemInHandRenderer`，第三人称只修改 `PlayerModel` |
| 地图兼容 | `MAP_ID` 存在时绕过自定义矩阵；单手/双手只选择原版地图分支 |
| 碰撞箱不变 | 渲染模型变化不写实体尺寸或服务器属性 |

Minecraft 26.2 目标字节码还确认了 `AvatarRenderState.id` 可与本地玩家实体 ID 比较，当前“自己/其他玩家”判断方式有效。

## 7. 兼容性结论

发布元数据明确阻止独立 `viewmodel` 和 `scaleme` 同时加载，避免双重修改同一渲染路径。Mod Menu 为可选入口；没有 Mod Menu 时核心 Mod 仍可加载。

用户当前完整实例包含 Catharsis、Chat Heads、ClearWaterLava、Fabric API、Fabric Language Kotlin、Firmament、Iris、Lithium、MaLiLib、MidnightLib、Mod Menu、Odin、Skyblocker、SkyHanni、Sodium、Sodium Extra、Tweakeroo、Xaero's Minimap、Xaero's World Map 等。1.0.4 的真实实例日志未显示本 Mod 冲突。

任何未来 Mod 若也取消 `ItemInHandRenderer.submitArmWithItem`，或在 `PlayerModel.setupAnim` 尾部覆盖部件比例，仍可能产生 Mixin 顺序相关的视觉差异；当前列表中未观察到对应崩溃。

## 8. 验证结果

| 验证 | 结果 |
| --- | --- |
| Java 25 `clean build` | 通过 |
| Gradle `test/check` | 通过；项目目前没有独立自动化测试源 |
| Fabric 1.0.7 开发客户端启动 | 通过 |
| 三处 Mixin 运行时加载 | 通过 |
| 资源、声音、纹理图集初始化 | 通过 |
| 发布 JAR ZIP 完整性 | 通过 |
| 发布元数据版本/客户端环境 | 通过 |
| 法律文件嵌入主 JAR 和源码 JAR | 通过 |
| 两次干净构建哈希一致 | 通过 |
| 地图分支 Mixin 运行时匹配 | 通过；MixinExtras 初始化且无注入错误 |
| 单手/双手地图世界内截图 | 未执行；自动化接口未识别独立 Java 游戏窗口 |
| Gradle 全量弃用警告检查 | 通过，无警告 |
| 源码网络与自动化关键词扫描 | 通过，无命中 |
| 发布类依赖扫描 | 通过，无网络/协议/自动操作依赖 |
| 1.0.4 完整 121 Mod 实例 | 通过启动和 Hypixel 连接日志检查 |
| 1.0.7 完整 121 Mod 实例 | 未重新执行 |
| 盔甲四槽多比例视觉回归 | 未完成，默认关闭 |
| Hypixel 官方单独批准 | 不存在；Hypixel 不提供此类批准 |

开发客户端中的用户属性和 Realms 鉴权错误来自离线开发账号 `FabricMC`，不影响 Mod 初始化，也不是本 Mod 网络行为。

## 9. Hypixel 风险分级

| 功能/设置 | 风险判断 | 建议 |
| --- | --- | --- |
| 无自定义包、无自动化 | 低 Watchdog 风险 | 保持当前实现 |
| 仅缩放自己的第三人称模型 | 低但非零政策风险 | 可保留；不改变相机和命中箱 |
| 缩放其他玩家 | 低至中政策风险 | Hypixel 上保持关闭 |
| 盔甲缩放 | 规则风险低，视觉回归不足 | 保持关闭 |
| 第一人称 100%、零位移、零旋转 | 较低政策风险 | 仍不如直接关闭明确 |
| 第一人称极小物品或明显移位 | 中等政策风险 | Hypixel 上不建议使用 |
| 隐藏空手 | 中等政策风险 | Hypixel 上保持关闭 |

Hypixel 官方规则依据：

- [Hypixel Allowed Modifications](https://support.hypixel.net/hc/en-us/articles/6472550754962-Hypixel-Allowed-Modifications)
- [Hypixel Server Rules](https://support.hypixel.net/hc/en-us/articles/4427624493330-Hypixel-Server-Rules)
- [About the Hypixel Watchdog System](https://support.hypixel.net/hc/en-us/articles/360019613300-About-the-Hypixel-Watchdog-System)
- [Hypixel SkyBlock Rules](https://support.hypixel.net/hc/en-us/articles/4508088842898-Hypixel-SkyBlock-Rules)

官方说明包括：所有 Mod 均自行承担风险；不明确属于允许类别的功能应默认视为不允许；自动化和改变客户端与服务器通信严格禁止；Watchdog 会关注异常服务器数据和不可能行为；不存在 100% 安全保证。

## 10. 推荐的 Hypixel 保守配置

| 设置 | 建议值 |
| --- | --- |
| 启用 | 开，仅保留第三人称自己缩放；绝对保守时整个 Mod 关闭 |
| 自己 | 开 |
| 其他玩家 | 关 |
| 盔甲 | 关 |
| 启用视角模型 | 关 |
| 隐藏空手手臂 | 关 |

这套设置降低规则解释风险，但仍不是 Hypixel 的官方批准或封号免责保证。若目标是官方所述的最低可能风险，只能使用原版客户端。

## 11. GitHub 与 Modrinth 发布边界

本项目可继续使用 MIT License。第一人称实现对 View Model 的 MIT 代码结构
有适配，因此已在源码、仓库和发布 JAR 中保留原作者及完整 MIT 声明。ScaleMe
和 More Player Models 仅作行为参考，未包含其代码、资源或二进制。

公开发布前仍需创建真实 GitHub 仓库并填写 source、issues 和维护者联系链接。
完整依据、平台字段和操作清单见 `LEGAL_AND_PUBLISHING_REPORT.zh-CN.md` 与
`PUBLISHING_CHECKLIST.md`。
