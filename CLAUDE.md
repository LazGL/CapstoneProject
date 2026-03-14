# CLAUDE.md — AI Assistant Guide for SoundUp / CapstoneProject

## Project Overview

**SoundUp** is a Java console application that simulates a running session. It tracks a runner's heart rate (BPM) in real time and dynamically selects music from categorized playlists to match the runner's current exertion level.

**Team:** Lazlo, Erif, Charlotte
**Language:** Java 21 (no build tool, no external dependencies)

---

## Repository Structure

```
CapstoneProject/
├── SoundUp.java          # Main entry point — authentication & race loop
├── User.java             # Runner model — BPM tracking & playlist triggering
├── Playlist.java         # Music loader & BPM-based song selector
├── Music.java            # Data model (name, BPM, author)
├── Chrono.java           # Stopwatch utility
├── HighBpmMusic.txt      # Songs at 144–210 BPM
├── MiddleBpmMusic.txt    # Songs at 102–138 BPM
├── LowBpmMusic.txt       # Songs at 20–92 BPM
├── musics.txt            # Legacy music data file (unused by current code)
├── NewFrame.form         # NetBeans Swing GUI form (work in progress)
├── CapstoneProject.zip   # Distribution archive
└── README.md             # Project task list
```

---

## Build & Run

### Compile
```bash
javac *.java
```
All five source files must be compiled together (they share the default package).

### Run
```bash
java SoundUp
```

The application will prompt for:
1. **Username** — any string
2. **Password** — any string (no real validation)
3. **Distance objective (km)** — used to calculate simulated run duration

The simulation then runs 100 BPM update iterations at 500 ms intervals (≈50 seconds total), selecting a new song every 10 iterations.

---

## Class Responsibilities

| Class | Role |
|---|---|
| `SoundUp` | Entry point. Reads credentials and distance, creates a `User`, starts the race loop. |
| `User` | Holds user profile. Generates/updates BPM randomly each iteration. Computes a 5-sample rolling average. Calls `Playlist` every 10 iterations. |
| `Playlist` | Reads music data from the three `.txt` files on construction. Selects a song matching the current average BPM bracket. |
| `Music` | Plain data object: `name`, `bpm`, `author` with getters/setters. |
| `Chrono` | Start/stop/elapsed stopwatch with human-readable output. |

---

## BPM Logic

- Every 500 ms a new BPM value is randomly generated (simulating heart rate sensor data).
- The last **5 BPM readings** are averaged.
- Every **10 iterations** the average BPM is compared to threshold ranges:
  - **High BPM** — above the middle range → `HighBpmMusic.txt`
  - **Middle BPM** — within the middle range → `MiddleBpmMusic.txt`
  - **Low BPM** — below the middle range → `LowBpmMusic.txt`
- A random song from the matching playlist is displayed in the console.

---

## Data File Format

Each `.txt` playlist file uses one song per line with pipe-delimited fields:

```
SongName|AuthorName|BPM
```

Example (`HighBpmMusic.txt`):
```
Taki Taki|DJ Snake|158
No Me Ames|Reik|144
```

The `Playlist` class parses these with `String.split("\\|")`.

---

## Code Conventions

- **Package:** Default package (no `package` declaration).
- **Naming:** `PascalCase` for classes, `camelCase` for methods and variables.
- **One class per file.**
- **No external libraries** — standard Java library only (`java.io`, `java.util`, `java.lang`).
- **Comments:** Minimal; some comments are in French (team's primary language).
- **No logging framework** — output via `System.out.println`.

---

## GUI Component (In Progress)

`NewFrame.form` is a NetBeans-generated Swing form (XML). It defines:
- A `JFrame` (~400×800 px)
- A **Sign Up** `JButton`
- A `JTextField` (likely for username input)
- Uses `AbsoluteLayout` and Comic Sans MS font

The GUI is **not yet wired to the core logic**. If connecting it, the entry point should construct `NewFrame` instead of running the console loop in `SoundUp.main()`.

---

## Testing

There is **no automated test suite**. Testing is manual:
- Compile and run with `javac *.java && java SoundUp`
- Observe console output for BPM updates and music selections
- Verify that playlist switching occurs every 10 iterations
- Verify `Chrono` output format is human-readable (e.g., `0h 0min 50s`)

When adding tests, JUnit 5 is the recommended framework for this Java version.

---

## Known Issues & Incomplete Features

- **Task #6 (presentation scenario):** Not started per README.
- **GUI not connected** to business logic.
- **`musics.txt`** appears unused — `Playlist` only reads the three BPM-specific files.
- **No input validation** on username/password or distance.
- **No `.gitignore`** — compiled `.class` files may be committed.
- **README** contains test/scratch lines (lines 22–25) that should be cleaned up.

---

## Development Workflow

### Branch Conventions
- Work on feature branches; merge to `main` when complete.
- Active AI-assisted work uses branches prefixed with `claude/`.

### Making Changes
1. Edit `.java` source files directly (no build tool configuration needed).
2. Recompile with `javac *.java` after any change.
3. Run `java SoundUp` to smoke-test interactively.
4. Commit with a descriptive message referencing the task number when applicable (e.g., `fix #5 — correct BPM threshold for high playlist`).

### Adding Music
Add entries to the appropriate `.txt` file following the `Name|Author|BPM` format. No code changes are needed — `Playlist` reads the files at runtime.

### Adding a New BPM Category
1. Create a new `.txt` data file.
2. Add a new `ArrayList<Music>` field in `Playlist`.
3. Update `Playlist` constructor to load the new file.
4. Add a new BPM threshold condition in the selection method.
5. Update `User` if the triggering logic needs to change.

---

## Environment

- **Java:** 21 (`java -version` → `21.0.10`)
- **IDE:** NetBeans (optional — project works with any editor + terminal)
- **No environment variables required** by the application itself.
- Container/CI environments may inject `JAVA_TOOL_OPTIONS` with proxy settings — this is expected and harmless.
