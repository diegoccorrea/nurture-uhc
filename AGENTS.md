# Nurture-UHC — Agent Instructions

## Build System

- **Primary**: Gradle Kotlin DSL (`build.gradle.kts`). `pom.xml` is present but not used.
- Run with `./gradlew build` — produces a shaded JAR at `build/libs/polaris-uhc-*.jar`.
- `pom.xml` is kept for compatibility but Gradle is the source of truth.

## Key Commands

- `./gradlew build` — clean + compile + shadow JAR
- `./gradlew shadowJar` — shaded JAR only
- `./gradlew tasks` — list all available tasks

## Tech Stack

- Paper API `1.21.11-R0.1-SNAPSHOT`
- Java 21 (toolchain enforced)
- Lombok `1.18.44` with annotation processor
- HikariCP, FastBoard, FastInv, Reflections, SignGUI, PlaceholderAPI, ProtocolLib, Chunky, mcdev-utils

## Registration

- Main class: `us.polarismc.polarisuhc.Main`
- All managers singletoned via `Main` fields; no service locator needed.
- Commands defined in `src/main/resources/plugin.yml` — update there AND in code when adding commands.

## Resource Filtering

`tasks.processResources` filters `yml, yaml, json, properties` with `expand(project.properties)`. This means `plugin.yml` uses `${project.version}` and it gets replaced at build time. Edit **source** resources, not `build/` output.

## Shading

- `signgui` library is relocated to `us.polarismc.polarisuhc.signgui`.
- Other deps are `compileOnly` (Paper API, PlaceholderAPI, ProtocolLib, Chunky) — not shaded.

## Patterns

- State managers: `UHCManager`, `TeamManager`, `PlayerManager`, `GameFlowManager`, `ArenaManager`, `HubManager`, `ChannelManager`, `InfoManager`, `ScenarioManager`
- Game phases driven by `GameTimer` + `GameEvent` system
- Custom crafts, scenarios, team colors/prefixes all configurable

## Testing

### Stack

- `org.mockbukkit.mockbukkit:mockbukkit-v1.21:4.110.0` — mocks the Bukkit API (Server, PluginManager, etc.)
- `org.junit.jupiter:junit-jupiter:5.11.0` — JUnit 5 for test structure and assertions

### File Location

All test files live in `src/test/java/us/polarismc/polarisuhc/`.

### Conventions

- Use the **manual MockBukkit API** — `MockBukkit.mock()` in `@BeforeEach` and `MockBukkit.unmock()` in `@AfterEach`.
- Load the plugin via `MockBukkit.load(YourPlugin.class)` — this triggers `onEnable()`.
- Do NOT use `MockBukkitExtension` — the manual pattern gives explicit control over server lifecycle.
- Use reflection + `setAccessible(true)` for package-private fields.

### Example: Verifying plugin starts

```java
class MainTest {
    private ServerMock server;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void onEnableDoesNotThrowAndInstanceIsNonNull() {
        Main plugin = MockBukkit.load(Main.class);
        assertNotNull(Main.getInstance(), "Main.getInstance() should not be null after onEnable()");
    }
}
```

### What to Test

- **Startup**: plugin loads, `onEnable()` completes without throwing, `Main.getInstance()` is non-null.
- **Commands**: dispatch a command via `MockBukkit.getServer().getCommandMap().dispatch(...)` and verify expected behavior.
- **Listeners**: simulate events via `MockBukkit.getServer().getPluginManager().callEvent(...)` and verify state changes.

### Running Tests

```bash
./gradlew test       # run all tests
./gradlew test --info  # verbose output including MockBukkit logs
```

## Skill

The repo has `minecraft-plugin-development` skill installed (`.agents/skills/minecraft-plugin-development/SKILL.md`). For any Bukkit/Paper task, load it — it covers registration, listeners, schedulers, state modeling, and build gotchas that apply here.

## Conventions

- Always check `plugin.yml` when adding/removing commands
- Keep `plugin.yml` `main` and `api-version` in sync if the main class or target version changes
- Validate state cleanup on `onDisable` (plugin has `start.disable()`)
- Prefer UUID over Player as map keys for persistent state
