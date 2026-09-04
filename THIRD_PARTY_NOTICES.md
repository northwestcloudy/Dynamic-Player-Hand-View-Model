# Third-Party Notices

This document records the external projects reviewed while developing Dynamic
Player Model and distinguishes adapted material from behavior-only references.

## View Model

- Project: View Model
- Author: I-No-oNe
- Source: https://github.com/I-No-oNe/View-Model
- Audited branch: `26.1.x`
- Audited commit: `0c74b573c9239f2f6890ca24e58f03b679fb77fc`
- Adapted source file:
  `src/main/java/net/i_no_am/viewmodel/mixin/MixinItemInHandRenderer.java`
- License: MIT
- Copyright: Copyright (c) 2024 I-No-oNe

Dynamic Player Model's first-person position, rotation, and scale feature adapts
MIT-licensed implementation structure from View Model. The implementation was
reduced to visual transforms, separated from third-person model scaling, and
extended with local configuration, smaller scale limits, compact empty hands,
and empty-hand visibility controls.

The required MIT copyright and permission notice is preserved in
`LICENSES/View-Model-MIT.txt` and is included in distributed JAR files.

## ScaleMe

- Project: ScaleMe
- Author: KdGaming0 / Kd_Gaming1
- Source: https://github.com/KdGaming0/ScaleMe
- Audited commit: `2ac21cef349d4ae9472e4086a501300dfb65f724`
- License reported by the audited source and 3.3.0 JAR: GPL-3.0

ScaleMe was reviewed as a behavior and compatibility reference. Dynamic Player
Model does not copy or package ScaleMe source code, bytecode, resources, icons,
translations, configuration files, or dependencies. Dynamic Player Model uses
a distinct per-part anchor algorithm rather than ScaleMe's global avatar
renderer scale.

ScaleMe is therefore not incorporated into this MIT-licensed distribution.

## More Player Models

- Project: More Player Models
- Author: Noppes
- Website: http://www.kodevelopment.nl/minecraft/moreplayermodels/
- Audited binary version: `1.20.4.20240405`
- License reported by the audited JAR: CC BY-NC

More Player Models was used only as a visual and feature reference. Dynamic
Player Model does not copy or package its source code, bytecode, models,
textures, GUI assets, icons, translations, presets, or other resources.

No More Player Models material is relicensed under this project's MIT License.

## Build dependencies

Dynamic Player Model builds against Fabric Loader, Fabric API, Minecraft, and
the Mod Menu API. These libraries are not repackaged in the release JAR. Users
install Fabric API separately; Mod Menu is optional.

## Project artwork

The icon distributed at `assets/dynamic_player_model/icon.png` was created for
this project. The project's original contributions to it are licensed under the
MIT License. It was not extracted from Minecraft or any reviewed third-party
mod. No rights are claimed in Minecraft or Microsoft trademarks or underlying
game elements. Design drafts and reference-mod icons are not included in the
repository or release JAR.

## Trademarks and affiliation

Minecraft is a trademark of Microsoft Corporation. Product and project names
belong to their respective owners. Reference and compatibility statements do
not imply endorsement, sponsorship, or affiliation.
