# Security Policy

## Supported versions

Security fixes are provided for the latest published version of Dynamic Player
Model for Minecraft 26.2.

## Reporting a vulnerability

Do not include account tokens, session data, personal information, or private
server details in a public issue. Contact the project maintainer privately
through the security-reporting method configured on the public GitHub
repository.

The mod is expected to perform no network requests and send no custom Minecraft
packets. Reports showing unexpected network access, credential access, arbitrary
file access, or server-facing behavior are treated as security issues.

## Data handling

Dynamic Player Model stores only local visual settings in
`config/dynamic_player_model.json`. It has no telemetry, analytics, update
checker, remote configuration, advertising SDK, or account-data collection.
