WIP!

# Nurture UHC

<div align="center">
  <img src="./assets/nurture-banner.png" width="600" alt="Banner de polaris.">
</div>

<div align="center">
<img alt="modrinth" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg">
<img alt="discord-plural" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/social/discord-plural_vector.svg">
<img alt="github" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/github_vector.svg">
</div>

UHC gamemode plugin for Paper 1.21.11 servers. Originally created by [Wixent](https://github.com/Wixent) and made open source.

[![Java 21](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![Paper 1.21.11](https://img.shields.io/badge/Paper-1.21.11-blue.svg)](https://papermc.io/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

## Table of Contents

- [Features](#features)
- [Commands](#commands)
  - [Developer Commands](#developer-commands)
  - [Host Commands](#host-commands)
  - [Team Commands](#team-commands)
- [Permissions](#permissions)
- [Scenarios](#scenarios)
- [Installation](#installation)
- [Configuration](#configuration)
- [Game Flow](#game-flow)
- [Credits](#credits)

## Features

- **Scatter System**: Players are randomly scattered across the world at game start
- **Team Management**: Create teams, manage colors, emojis, and shared inventories
- **200+ Scenarios**: Custom game modifiers like CutClean, HasteyBoys, TimeBomb, and more
- **Border System**: Dynamic border shrinking that intensifies during Meetup
- **Custom Crafts**: Craftable items like Totems, Golden Heads, Spectral Arrows
- **PvP & Meetup Phases**: Structured PvP period followed by organized Meetup
- **Scoreboard & TAB**: Real-time game information display
- **MOTD & ActionBars**: Branded server messages and alerts
- **Chat Channels**: Team chat, staff chat, and spectator chat support

## Commands

### Developer Commands

These commands are for testing and debugging. They require `uhc.dev` permission.

| Command        | Aliases | Description                | Usage          |
| -------------- | ------- | -------------------------- | -------------- |
| `/debug`       | -       | Display debug information  | `/debug`       |
| `/gui`         | -       | Open GUI debug menu        | `/gui`         |
| `/hex`         | -       | Test hex color rendering   | `/hex <color>` |
| `/teamhex`     | -       | Test team hex colors       | `/teamhex`     |
| `/testteams`   | -       | Test team creation         | `/testteams`   |
| `/testscatter` | -       | Test scatter functionality | `/testscatter` |
| `/nametag`     | -       | Test nametag rendering     | `/nametag`     |

### Host Commands

These commands manage the UHC game. They require `uhc.host` permission.

| Command           | Aliases | Description                                  | Usage                                   |
| ----------------- | ------- | -------------------------------------------- | --------------------------------------- |
| `/createworld`    | -       | Create overworld/nether/end worlds           | `/createworld <overworld\|nether\|end>` |
| `/togglescenario` | -       | Toggle scenarios on/off                      | `/togglescenario <name>`                |
| `/emojiteam`      | -       | Change a team's emoji                        | `/emojiteam <player> <emoji>`           |
| `/maketeams`      | -       | Broadcast team creation message              | `/maketeams`                            |
| `/solo`           | -       | Create solo teams or assign players to teams | `/solo [player]`                        |
| `/quickstart`     | -       | Start the UHC automatically                  | `/quickstart`                           |
| `/manualstart`    | -       | Start the UHC manually                       | `/manualstart`                          |
| `/toggle`         | -       | Toggle UHC features                          | `/toggle <feature>`                     |
| `/world`          | -       | Teleport to different worlds                 | `/world <world>`                        |

### Team Commands

These commands are available to all players during the game.

| Command          | Aliases           | Description                            | Permission | Usage                     |
| ---------------- | ----------------- | -------------------------------------- | ---------- | ------------------------- |
| `/bookstuff`     | `bs`              | Send book items to teammates           | -          | `/bookstuff`              |
| `/colorteam`     | -                 | Change your team's color               | -          | `/colorteam <color>`      |
| `/minedores`     | `pmminedores`     | Share mined ores with teammates        | -          | `/minedores`              |
| `/ores`          | `pmores`          | Share ores in inventory with teammates | -          | `/ores`                   |
| `/teamchat`      | `chat`, `c`, `tc` | Toggle team chat mode                  | -          | `/teamchat`               |
| `/team`          | -                 | All team-related commands              | -          | `/team <subcommand>`      |
| `/teamlocation`  | `tl`              | Send your location to teammates        | -          | `/teamlocation`           |
| `/teaminventory` | `ti`              | Open shared team inventory             | -          | `/teaminventory [player]` |
| `/scen`          | -                 | Show active scenarios inventory        | -          | `/scen`                   |

## Permissions

| Permission   | Description                              | Default Level |
| ------------ | ---------------------------------------- | ------------- |
| `uhc.host`   | Allows hosting and managing UHC games    | Staff         |
| `uhc.dev`    | Allows access to debug and test commands | Developer     |
| `uhc.player` | Base player permissions                  | Default       |

## Scenarios

Scenarios are game modifiers that change how the UHC plays. Use `/togglescenario <name>` to enable or disable them.

> ⚠️ Scenarios marked with `[In Dev]` may not be fully implemented.

For a detailed explanation of all scenarios (in Spanish and English), visit:

- 🇪🇸 [Scenarios en Español](https://pastebin.com/4wnaGEYB)
- 🇬🇧 [Scenarios in English](https://pastebin.com/y6h9EjDb)

| Scenario           | Effect                                                                                                                       |
| ------------------ | ---------------------------------------------------------------------------------------------------------------------------- |
| **AbsorptionLess** | Absorption hearts are disabled, but golden apples still heal.                                                                |
| **CutClean**       | Ores are auto-smelted and animals drop cooked meat. ⚠️ [In Dev]                                                              |
| **FireLess**       | You cannot take fire or lava damage.                                                                                         |
| **FortuneBabies**  | Every tool has Fortune I. (Incompatible with FortuneBoys, FortuneBoys+)                                                      |
| **FortuneBoys**    | Every tool has Fortune II. (Incompatible with FortuneBabies, FortuneBoys+)                                                   |
| **FortuneBoys+**   | Every tool has Fortune III. (Incompatible with FortuneBabies, FortuneBoys)                                                   |
| **GhastShips**     | Happy ghast that stuns when hit. ⚠️ [In Dev]                                                                                 |
| **GoToHell**       | If you aren't in the Nether during Meetup, you receive damage. Enables Nether in Meetup. ⚠️ [In Dev]                         |
| **GraveRobbers**   | When a player dies, a grave is created at their death location with a sign showing their name.                               |
| **Hades**          | Overworld is disabled. You spawn in the Nether. Various Nether resource changes apply. Enables Nether in Meetup. ⚠️ [In Dev] |
| **HasteyBabies**   | Every tool has Efficiency I and Unbreaking I. (Incompatible with HasteyBoys, HasteyBoys+)                                    |
| **HasteyBoys**     | Every tool has Efficiency III and Unbreaking II. (Incompatible with HasteyBabies, HasteyBoys+)                               |
| **HasteyBoys+**    | Every tool has Efficiency V and Unbreaking III. (Incompatible with HasteyBabies, HasteyBoys)                                 |
| **ShieldLess**     | You cannot use shields.                                                                                                      |
| **Switcheroo**     | When a projectile you shoot hits a player, your positions will be swapped.                                                   |
| **SwordLess**      | You cannot use swords.                                                                                                       |
| **TeamInventory**  | Each team has a shared inventory. Use `/ti` to open it.                                                                      |
| **Timber**         | Breaking a log block causes all blocks above it to break too.                                                                |
| **TimeBomb**       | When a player dies, a TNT chest appears at their death location. After 30 seconds, it explodes. ⚠️ [In Dev]                  |
| **Unbreakable**    | Every tool, weapon or armor piece is unbreakable.                                                                            |
| **Vulture**        | Players receive a Recovery Compass after PvP starts.                                                                         |

## Installation

### Requirements

- **Java 21** or higher
- **Paper 1.21.11** server

### Build from Source

```bash
# Clone the repository
git clone https://github.com/nurture-mc/nurture-uhc.git
cd nurture-uhc

# Build the plugin
./gradlew build

# The JAR will be created at: build/libs/nureture-uhc-*.jar
```

### Install

1. Stop your Paper server
2. Copy `build/libs/nurture-uhc-*.jar` to your server's `plugins/` folder
3. Restart the server
4. Configure via `plugins/nurture-uhc/config.yml`

## Configuration

The plugin is configured via `plugins/polaris-uhc/config.yml`. Here are the main sections:

### World Configuration

```yaml
worlds:
  overworld: uhc # Main game world
  nether: uhc_nether # Nether world
  end: uhc_the_end # End world
  arena: arena # Arena world
  lobby: lobby # Lobby world
```

### Border Settings

```yaml
border:
  overworld: 2000 # Starting border size
  meetup: 200 # Meetup border size
  timer: 5 # Minutes between border shrinks
  tp-border: true # Teleport players at border
  speed: 2.0 # Blocks per minute shrink speed
  border-list:
    - 1000
    - 500
    - 200
```

### Toggle Options

```yaml
toggle:
  advancements: false
  anti-item-destruction: true
  auto-ls: true
  bookshelves: true
  end: false
  explosives: true
  fire-aspect: false
  flame: true
  ghast-drop: true
  horses: true
  mobs: false
  nether: false
  stats: true
  trades: true
```

### Custom Crafts

```yaml
customcrafts:
  totem: true # Craftable Totem of Undying
  mace: false # Craftable Mace
  breeze_rod: true # Craftable Breeze Rod
  trident: true # Craftable Trident
  elytra: false # Craftable Elytra
  golden_head: true # Craftable Golden Head
  spectral_arrows: true
  glistering_melon: true
  tnt_minecart: true
  lectern: true
```

### Durations

```yaml
duration:
  pvp: 15 # PvP duration in minutes
  meetup: 15 # Meetup duration in minutes
  final-heal: 0 # Final heal minutes (0 = disabled)
```

### Rates

```yaml
rates:
  xp-kill: 3 # XP levels per kill
  flint: 30 # Flint drop chance %
  apple: 2 # Apple drop rate %
  glass: 40 # Glass drop rate %
```

### Potion Configuration

```yaml
potions:
  poison: ON
  swiftness: ON
  fire-resistance: ON
  turtle-master: ON
  slowness: ON
  invisibility: ON
  regeneration: OFF
  strength: TIER1
  healing: OFF
  harming: ON
  water-breathing: ON
  weakness: ON
  leaping: ON
  slow-falling: ON
  # ... more potions
```

## Game Flow

The UHC game follows this state machine:

```
IDLE → PRESTARTED → SCATTERING → SCATTERED → STARTED → PVP → MEETUP → FINALIZED
```

| State          | Description                                                     |
| -------------- | --------------------------------------------------------------- |
| **IDLE**       | No game is running. Server is in lobby mode.                    |
| **PRESTARTED** | Game is about to start. Players are gathered, countdown begins. |
| **SCATTERING** | Players are being scattered to random locations.                |
| **SCATTERED**  | All players scattered. Waiting for scatter to complete.         |
| **STARTED**    | Game is active. Players can explore and gather resources.       |
| **PVP**        | PvP is enabled. Players can fight each other.                   |
| **MEETUP**     | Border shrinks to meetup size. Final phase begins.              |
| **FINALIZED**  | Game over. Winner declared.                                     |

### Game Timers

- **PvP Timer**: 15 minutes (configurable) after STARTED before PvP enables
- **Meetup Timer**: 15 minutes (configurable) after PvP starts before border shrinks
- **Final Heal**: 0 minutes (configurable) — grace period before PvP

### Events

The game uses an event system driven by `GameEvent` and `GameTimer`:

- **PvPStartEvent**: Fires when PvP is enabled
- **MeetupStartEvent**: Fires when Meetup begins
- **UHCDeathEvent**: Fires when a player dies during the game

## Credits

- **Author**: [Putindeer](https://github.com/polaris-mc)
- **Contributors**: [tmpst](https://github.com/tmpst), [volcqnn](https://github.com/rodriguezemir)

This is an open source project. Feel free to contribute, report issues, or use it in your own UHC server.

---

For questions or support, please open an issue on the [GitHub repository](https://github.com/polaris-mc/polaris-uhc).
