# Publishing Checklist

## GitHub repository

- [ ] Create a public repository owned by the actual maintainer.
- [ ] Add the final public repository URL to `fabric.mod.json` under `contact`
      in a later release; do not add a URL before it exists.
- [ ] Identify `northwestcloudymac` as the maintainer/publisher and provide a
      direct email address or GitHub issue tracker as the public contact method.
- [ ] Commit source, Gradle Wrapper, documentation, project icon, and legal
      files.
- [ ] Do not commit `build/`, `run/`, `release/`, `.gradle/`, local logs,
      reference JARs, screenshots containing account data, or design drafts.
- [ ] Enable the included GitHub Actions build.
- [ ] Configure a private security-reporting method or GitHub private
      vulnerability reporting, then update `SECURITY.md`.
- [ ] Create the release from a clean commit and attach the main JAR and source
      JAR produced by that commit.
- [ ] Use a version tag matching the Mod version, for example
      `v1.0.7+26.2`.

## Modrinth project

Use these values:

| Field | Value |
| --- | --- |
| Project title | Dynamic Player Model |
| Summary | Client-side visual player proportions and first-person hand transforms. |
| Project type | Mod |
| License | MIT |
| Client side | Required |
| Server side | Unsupported |
| Loader | Fabric |
| Game version | 26.2 |
| Required dependency | Fabric API |
| Optional dependency | Mod Menu |
| Source link | The public GitHub repository created above |
| Issue link | The public GitHub issue tracker, when enabled |

- [ ] Use `MODRINTH_DESCRIPTION.md` as the English project description.
- [ ] Keep the unofficial Minecraft disclaimer near the top of the public
      description.
- [ ] Upload one primary file:
      `dynamic-player-model-1.0.7+26.2.jar`.
- [ ] The generated `-sources.jar` may be uploaded as an additional source
      file; do not upload old versions as additional files.
- [ ] Mark the release channel honestly. Use beta until armor scaling and the
      full compatibility matrix have completed visual testing.
- [ ] List Fabric API as required and Mod Menu as optional in the version's
      Dependencies section, not only in the description.
- [ ] Use only project-owned screenshots. Give each gallery image a relevant
      title and alt text.
- [ ] Do not upload View Model, ScaleMe, More Player Models, Minecraft, or
      Fabric binaries inside the project file.
- [ ] Do not claim Hypixel approval, guaranteed anti-cheat safety, or universal
      compatibility.
- [ ] Verify that the uploaded SHA-256 matches the artifact built from the
      tagged GitHub commit.
- [ ] Keep the JAR freely downloadable. Review the current Minecraft EULA and
      Usage Guidelines separately before enabling rewards, paid access,
      sponsorship, or any other monetization.

## Release verification

```bash
./gradlew clean build
unzip -t build/libs/dynamic-player-model-1.0.7+26.2.jar
shasum -a 256 build/libs/dynamic-player-model-1.0.7+26.2.jar
```

The legal files expected inside the JAR are:

- `META-INF/LICENSE_dynamic-player-model`
- `META-INF/THIRD_PARTY_NOTICES_dynamic-player-model.md`
- `META-INF/licenses/View-Model-MIT.txt`
