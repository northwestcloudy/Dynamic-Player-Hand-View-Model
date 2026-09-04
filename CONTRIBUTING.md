# Contributing

Contributions are welcome when they keep the mod client-side, visual-only, and
small in scope.

## Development

1. Use a Java 25 JDK.
2. Create a branch from the current default branch.
3. Run `./gradlew clean build`.
4. Test the development client when changing Mixins or rendering behavior.
5. Describe user-visible changes in `CHANGELOG.md`.

## Contribution requirements

- Do not submit copied or decompiled code unless its license explicitly permits
  the use and all required notices are included.
- Identify any external code, asset, or substantial implementation reference in
  the pull request.
- Do not add telemetry, remote requests, custom server packets, gameplay
  automation, hitbox changes, reach changes, or combat advantages.
- Do not claim that the mod is approved or guaranteed safe on a multiplayer
  server.
- Keep View Model attribution and its MIT license intact.
- Do not add More Player Models code or assets under this project's MIT license.
- GPL-licensed code cannot be merged without first evaluating whether the whole
  project must be relicensed.

By contributing, you confirm that you have the right to submit the contribution
and agree to license it under this project's MIT License.
