# Dynamic Player Model

> **Unofficial community project:** NOT AN OFFICIAL MINECRAFT PRODUCT. NOT
> APPROVED BY OR ASSOCIATED WITH MOJANG OR MICROSOFT.

Dynamic Player Model is a lightweight, client-side Fabric mod for Minecraft
26.2. It changes third-person player-model proportions and provides separate
first-person hand and item transforms.

It changes rendering only. Hitboxes, entity dimensions, eye height, reach,
movement, combat attributes, item data, and server state remain unchanged.

## Features

- Independent head and body scale from 5% to 100%
- Grounded feet and connected head, body, arms, and legs
- Optional third-person armor and held-item scaling
- Separate main-hand and off-hand scale, position, and rotation
- First-person item scale from 1% to 100%
- Compact or hidden empty first-person hands
- Automatic vanilla map-transform fallback with one-hand or two-hand map mode
- Rebindable settings key, default `F8`
- Optional Mod Menu configuration entry
- English and Simplified Chinese translations

## Requirements

- Minecraft 26.2
- Fabric Loader 0.19.3 or newer
- Fabric API 0.156.0+26.2 or compatible
- Java 25 or newer
- Mod Menu is optional

Remove standalone View Model or ScaleMe before installing this mod. They modify
overlapping rendering paths and are marked as incompatible.

Maps bypass all configurable View Model transforms. Two-hand mode is used only
for a main-hand map with an empty off hand; other map states retain vanilla
one-hand rendering.

## Multiplayer notice

This mod does not send custom packets or automate gameplay. However, extreme
first-person scaling or hidden hands can reduce screen obstruction, and server
rules differ. This project is not approved, endorsed, or guaranteed safe by
Hypixel or any other server operator.

For conservative multiplayer use, disable scaling of other players, armor
scaling, and the first-person view-model feature.

## Privacy

No telemetry, analytics, advertising, update checker, remote configuration, or
background network requests are included. The mod reads and writes only its
local JSON configuration.

## License and acknowledgements

Dynamic Player Model is MIT licensed. The first-person transform feature adapts
MIT-licensed implementation structure from View Model by I-No-oNe, with the
original notice preserved in the source repository and JAR. ScaleMe and More
Player Models were behavior references only; their code and assets are not
included.

The project icon was created for Dynamic Player Model and is not extracted from
Minecraft or another mod. The MIT License covers the project's original
contributions; third-party trademarks and underlying game rights remain with
their respective owners.
