# Changelog

All notable changes to Dynamic Player Model are documented here.

## [1.0.7+26.2] - 2026-08-25

### Changed

- Ported the existing client-only rendering features to Minecraft 26.2.
- Updated Fabric API compatibility to `0.156.0+26.2` and optional Mod Menu
  compatibility to `20.0.1`.
- Included the transparent-background project icon.

## [1.0.7+26.1.2] - 2026-08-04

### Added

- A one-hand or two-hand first-person map mode in both View Model settings tabs.

### Fixed

- Bypassed every View Model transform and empty-hand override while either hand
  holds a map, preventing custom hand matrices from corrupting vanilla map
  rendering.

### Changed

- Replaced the opaque near-black mod-icon background with a genuine transparent
  alpha channel while preserving the original pixel-art foreground.

## [1.0.6+26.1.2] - 2026-07-31

### Added

- Public GitHub and Modrinth documentation.
- View Model MIT attribution and third-party notices.
- Privacy, security, contribution, and publishing documentation.
- Gradle Wrapper and GitHub Actions build validation.
- Reproducible archive settings and legal files in binary and source JARs.
- Prominent unofficial-project disclaimer and project-artwork provenance.

### Changed

- Clarified that multiplayer-server compliance is not guaranteed.
- Clarified that ScaleMe and More Player Models are behavior references only.

## [1.0.5+26.1.2] - 2026-07-31

### Fixed

- Prevented the F8 shortcut from nesting duplicate configuration screens.
- Removed duplicate configuration writes when closing the settings screen.
- Repaired malformed configuration files by writing valid defaults.

## [1.0.4+26.1.2] - 2026-07-31

### Changed

- Restored the selected 512 by 512 project icon.

## [1.0.3+26.1.2] - 2026-07-31

### Added

- Alternative icon iteration retained only during local design review.

## [1.0.2+26.1.2] - 2026-07-31

### Fixed

- Reworked armor scaling around per-slot pose transforms.
- Disabled armor scaling by default and migrated older configurations safely.

## [1.0.1+26.1.2] - 2026-07-31

### Added

- First-person scale range from 1% to 100%.
- Empty-hand hiding setting and rebindable shortcut.
- First-person empty-hand compacting controls.

## [1.0.0+26.1.2] - 2026-07-31

### Added

- Initial third-person player-part scaling.
- Grounded feet and connected body-part anchors.
- First-person main-hand and off-hand transforms.
- Rebindable settings shortcut and Mod Menu integration.
