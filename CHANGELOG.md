# Changelog

## v2.0.0-beta

- Fixed Lua self handling so `Player:sendMessage(msg)` now works cleanly.
- Added `mc-1.20.1-paper` as a new platform module for PaperMC, Bukkit, and Spigot.
- Made the Lua runtime platform-agnostic so the working directory is defined by the active platform and passed to the common module through an adapter.
- Improved the Gradle build so each platform and Minecraft version is built as a separate target, with every `mc-*` module compiled independently against `common`.
- Simplified the command namespace by removing `/rsev` as the root namespace. Commands can now be called directly via `/lua`.
- extend lua loading VM for asset loading
