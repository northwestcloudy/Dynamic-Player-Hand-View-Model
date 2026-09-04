# Dynamic Player Model

> **非官方社区项目：** 本项目不是 Minecraft 官方产品，未得到 Mojang 或
> Microsoft 的批准，也不与其存在关联。

Dynamic Player Model 是面向 Minecraft 26.2 的轻量 Fabric 纯客户端 Mod，
用于调整第三人称玩家头身比例，以及第一人称主手、副手和物品的显示矩阵。

本 Mod 只修改渲染，不修改碰撞箱、实体尺寸、眼睛高度、攻击或交互距离、
移动与战斗属性、物品数据或服务端状态。

## 主要功能

- 头部与身体可分别在 5% 到 100% 之间调整。
- 缩放后脚底保持落地，头、身体、手臂和腿保持连接。
- 可选让第三人称盔甲和手持物品跟随模型比例。
- 主手和副手可分别调整大小、三轴位置和三轴旋转。
- 第一人称物品最小可缩放到 1%，最大不超过原版。
- 可缩短或隐藏空手手臂。
- 持有地图时自动绕过 View Model 变换，并可选择单手或双手地图显示。
- 默认按 `F8` 打开设置，也支持 Mod Menu 和自定义按键。

## 安装要求

- Minecraft 26.2
- Java 25 或更高版本
- Fabric Loader 0.19.3 或更高版本
- Fabric API 0.156.0+26.2 或兼容的新版本
- Mod Menu 20.0.1 为可选依赖

安装本 Mod 前，需要移除独立的 View Model 和 ScaleMe，避免重复修改相同的
渲染路径。

地图始终使用原版矩阵，不应用自定义的第一人称缩放、位移、旋转或空手隐藏。
双手模式仅在主手持地图且副手为空时生效；副手已有物品或地图在副手时，保留
原版单手地图显示。

## 多人服务器说明

本 Mod 不发送自定义数据包、不模拟按键，也不修改玩法状态。但第一人称极小
物品或隐藏空手会减少屏幕遮挡，不同服务器可能有不同解释。

本项目没有得到 Hypixel 或任何其他服务器的批准、认可或安全保证。追求保守
配置时，应关闭其他玩家缩放、盔甲缩放和第一人称 View Model 功能。

## 隐私

本 Mod 不包含遥测、统计、更新检查、远程配置、账号收集或后台网络请求，只
读写本地的 `config/dynamic_player_model.json`。

## 构建

使用 Java 25 执行：

```bash
./gradlew clean build
```

产物位于 `build/libs/`。

## 协议与参考项目

本项目使用 [MIT License](LICENSE)。

第一人称矩阵功能对 I-No-oNe 的 MIT 项目
[View Model](https://github.com/I-No-oNe/View-Model) 进行了适配，原始版权
声明保存在 [LICENSES/View-Model-MIT.txt](LICENSES/View-Model-MIT.txt)。

ScaleMe 和 More Player Models 仅作为行为参考；本仓库和发布 JAR 不包含
它们的代码、资源、图标、翻译或二进制文件。完整说明见
[THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md)。

项目图标专为 Dynamic Player Model 制作，不是从 Minecraft 或其他 Mod 中
提取的资源。MIT 许可证只覆盖本项目拥有的原创部分；第三方商标和底层游戏
权利仍归各自权利人所有。
