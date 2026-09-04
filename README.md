# Dynamic Player Model

> **Unofficial community project:** NOT AN OFFICIAL MINECRAFT PRODUCT. NOT
> APPROVED BY OR ASSOCIATED WITH MOJANG OR MICROSOFT.

Dynamic Player Model is a lightweight, client-side Fabric mod for Minecraft
26.2. It provides visual player-model proportions in third person and
independent first-person hand and item transforms.

The mod changes rendering only. It does not change hitboxes, entity dimensions,
eye height, reach, movement, combat attributes, item data, or server state.

[简体中文说明](README.zh-CN.md)

## Features

- Scale the player head and body independently from 5% to 100%.
- Keep the feet grounded and keep the head, body, arms, and legs connected.
- Optionally scale third-person armor and held items with the model.
- Configure first-person main-hand and off-hand scale, position, and rotation.
- Scale first-person held items down to 1% without enlarging them past vanilla.
- Shorten or hide empty first-person hands.
- Automatically bypass View Model transforms for maps, with a one-hand or
  two-hand map mode.
- Open the settings with a rebindable key, or through Mod Menu.
- Store all settings locally in `config/dynamic_player_model.json`.

## Requirements

- Minecraft 26.2
- Java 25 or newer
- Fabric Loader 0.19.3 or newer
- Fabric API 0.156.0+26.2 or a compatible newer release
- Mod Menu 20.0.1 is optional

## Installation

1. Install Fabric Loader and Fabric API for Minecraft 26.2.
2. Put the Dynamic Player Model JAR in the instance's `mods` directory.
3. Remove standalone View Model or ScaleMe installations. Fabric metadata marks
   them as incompatible because they modify overlapping rendering paths.
4. Start the game and press `F8`, or open the configuration through Mod Menu.

The empty-hand visibility shortcut is unbound by default and can be assigned in
Minecraft's Controls screen.

Maps always use vanilla transforms instead of the configurable View Model
matrix. Two-hand mode applies when a map is in the main hand and the off hand is
empty; otherwise Minecraft's one-hand map rendering is preserved.

## Multiplayer and server rules

Dynamic Player Model does not send custom packets, automate input, or modify
gameplay state. That does not mean every visual configuration is permitted on
every multiplayer server.

Extreme first-person scaling or hiding hands can reduce screen obstruction.
Some servers may treat that as an advantage. Check the rules for each server
and use conservative settings where required. This project is not approved,
endorsed, or guaranteed safe by Hypixel or any other server operator.

For the most conservative multiplayer configuration:

- keep scaling of other players disabled;
- keep armor scaling disabled until its full visual compatibility matrix is
  complete;
- disable the first-person view-model feature.

## Privacy and networking

The mod has no telemetry, analytics, update checker, remote configuration,
account collection, or background network requests. It reads and writes only
its local JSON configuration. See [SECURITY.md](SECURITY.md) for reporting
security issues.

## Compatibility

The mod was designed to coexist with common rendering and performance mods,
including Sodium and Iris. A previous build was also launched in the author's
larger SkyBlock instance. Compatibility cannot be guaranteed for every future
version or for mods that inject into the same three Minecraft rendering
methods.

Standalone View Model and ScaleMe must not be installed at the same time as
Dynamic Player Model.

## Building

Clone the repository and run:

```bash
./gradlew clean build
```

The main JAR and source JAR are written to `build/libs/`. The build requires a
Java 25 JDK. GitHub Actions runs the same command for pushes and pull requests.

## License and acknowledgements

Dynamic Player Model is licensed under the [MIT License](LICENSE).

The first-person transform feature adapts MIT-licensed ideas and implementation
structure from [View Model](https://github.com/I-No-oNe/View-Model) by
I-No-oNe. Its copyright notice and license are preserved in
[LICENSES/View-Model-MIT.txt](LICENSES/View-Model-MIT.txt).

ScaleMe and More Player Models were reviewed as behavioral references. No code,
assets, icons, translations, or binaries from either project are included.
Details and exact source revisions are recorded in
[THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).

The project icon was created for Dynamic Player Model and is not extracted from
Minecraft or another mod. The MIT License applies to the project's original
contributions; third-party trademarks and underlying game rights remain with
their respective owners.

Minecraft is a trademark of Microsoft Corporation. This project is not
affiliated with or endorsed by Microsoft, Mojang Studios, Hypixel, Modrinth,
View Model, ScaleMe, or More Player Models.
