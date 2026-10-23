# Architecture

Native Jetpack Compose app. Single activity, MVVM, Material 3 Expressive, pure-gray theme.

## Stack

| Layer | Choice |
|---|---|
| UI | Compose BOM 2026.09.00, material3 1.5.0-beta01 (`MaterialExpressiveTheme`, `MotionScheme.expressive()`) |
| Color | One neutral palette in `ui/Palettes.kt` (light / dark / dusk / OLED), custom palettes via `ui/PaletteMaker.kt`, Material You optional |
| Icons | Material Symbols Rounded as generated vectors (`Sym.*`, `tools/gen-symbols.mjs`) |
| Images | Coil 3 |
| Network | OkHttp 5 + kotlinx-serialization (`data/`) |
| Privileged install | Shizuku, root, built-in wireless ADB |

## Flow

```
MainActivity  splash, edge-to-edge, deep links
  └─ OrionRoot(vm)                 ui/Root.kt
       ├─ tabs: Apps, PC, TV, My Apps   (StoreScreen.kt, MyApps.kt)
       ├─ header: search pill, Settings
       └─ overlays: Settings, Release notes, FAQ, Notice, Tutorial
MainViewModel  ui/MainViewModel.kt
  ├─ Store.settings / Store.data    persisted state (data/Stores.kt)
  ├─ Repository / CatalogProcessor  catalog, config, mirrors, cache
  ├─ AppTracker                     install, uninstall, package scan
  └─ Downloads / DownloadService    foreground download engine
```

## Files

- `ui/`: Root, StoreScreen, Detail, MyApps, Settings, Sheets, Gate (forced update and maintenance), Xtra (Guardian, Sentinel, Shizuku), Theme, Palettes, PaletteMaker, Common
- `data/`: Models, Stores, Repository, CatalogProcessor, Logic
- `platform/`: AppTracker, DownloadService, VirusTotal, WirelessAdb

## Checks

- `./gradlew :app:testDebugUnitTest` runs `LogicTest` (versions, image links, repo parsing, categories) and `HapticsTest`.
- `./gradlew :app:assembleDebug` builds the debug APK.

## Known leftovers

Some state fields in `data/Stores.kt` (ad counters, XP, badges) and a few unused composables (`DonationSheet`, `DevConfigEditor`, `ShareLinksSheet`) remain from the upstream app. They are dead code and safe to delete.
