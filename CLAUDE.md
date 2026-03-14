# CLAUDE.md — AI Assistant Guide for SoundUp / CapstoneProject

## Project Overview

**SoundUp** is a Java Swing application that simulates a running session. It tracks a runner's heart rate (BPM) in real time and dynamically selects music from categorized playlists to match the runner's current exertion level.

**Team:** Lazlo, Erif, Charlotte
**Language:** Java 21 — no build tool, no external dependencies

---

## Repository Structure

```
CapstoneProject/
├── SoundUp.java          # Main entry point — launches the GUI
├── NewFrame.java         # Swing GUI — all panels, navigation, background thread
├── User.java             # Runner model — BPM simulation, RaceListener callback
├── Playlist.java         # Music loader & BPM-based song selector
├── Music.java            # Data model (name, BPM, author)
├── Chrono.java           # Stopwatch utility
├── RaceListener.java     # Callback interface for GUI ↔ simulation communication
├── RaceHistory.java      # Saves/loads race history to races.csv
├── HighBpmMusic.txt      # Songs at 144–210 BPM
├── MiddleBpmMusic.txt    # Songs at 102–138 BPM
├── LowBpmMusic.txt       # Songs at 20–92 BPM
├── musics.txt            # Legacy data file (unused)
├── NewFrame.form         # NetBeans GUI form (reference only — NewFrame.java is the source of truth)
├── races.csv             # Race history (created at runtime, not tracked in git)
└── README.md             # Project task list
```

---

## Build & Run

### Compile
```bash
javac *.java
```

### Run
```bash
java SoundUp
```

A Swing window opens (400×800 px). No console interaction required.

---

## Application Flow

```
Sign In / Sign Up
      ↓
  Main Menu
  ↙       ↘
Race       Past Races
Setup      (loads races.csv)
  ↓
During Race
(background thread, live BPM + song updates)
  ↓
Summary dialog → Main Menu
```

---

## Class Responsibilities

| Class | Role |
|---|---|
| `SoundUp` | Entry point. Calls `SwingUtilities.invokeLater` to open `NewFrame`. |
| `NewFrame` | Full GUI. Uses `CardLayout` to switch between 6 panels. Starts the race simulation in a daemon background thread. |
| `User` | Holds user profile. Generates BPM each 500 ms. Calls `RaceListener` callbacks instead of printing. |
| `Playlist` | Reads the 3 `.txt` music files using `System.getProperty("user.dir")`. Returns a `Music` object from `chooseMusic()`. |
| `Music` | Plain data object: `name`, `bpm`, `author` with getters/setters. |
| `Chrono` | Start/stop/elapsed stopwatch. |
| `RaceListener` | Interface with `onUpdate(bpm, avgBpm, km, song)` and `onFinished(km, seconds)`. |
| `RaceHistory` | Appends races to `races.csv`; loads them back for the Past Races table. |

---

## BPM Logic (`User.updateBpm`)

- Takes `distanceKm` — iteration count = `max(10, (int)(distanceKm / 0.04))`.
- Every 500 ms: BPM changes randomly based on current range:
  - `< 100 bpm` → change -1 to +7
  - `100–139 bpm` → change -5 to +9
  - `140–(223-age) bpm` → change -5 to +2
- Rolling 5-sample BPM average updated each iteration.
- Every 10 iterations: `playlist.chooseMusic(avgBpm)` selects a new song.
- Calls `listener.onUpdate()` every iteration.
- Calls `listener.onFinished()` when loop ends or thread is interrupted (early stop).

---

## GUI Threading

The simulation runs on a **daemon thread** so the Swing EDT stays free.
All GUI updates from `RaceListener` callbacks go through `SwingUtilities.invokeLater()`.

Early stop (user presses "End of the race") interrupts the thread via `Thread.interrupt()`.
The `InterruptedException` in `Thread.sleep()` triggers `listener.onFinished()` with current stats.

---

## Data File Format

Each `.txt` playlist file — **3 lines per song**, repeating:

```
SongName
AuthorName
BPM
```

Example (`HighBpmMusic.txt`):
```
Selfish Love
DJ snake
147
```

File path resolved at runtime: `new File(System.getProperty("user.dir"), "HighBpmMusic.txt")`.

---

## Race History Format (`races.csv`)

One race per line, comma-separated:
```
distanceKm,durationSeconds,speedKmh
```
Example:
```
5.00,3750,4.8
```

Appended after each race. Loaded by `RaceHistory.load()` for the Past Races table.

---

## Code Conventions

- **Package:** Default package (no `package` declaration).
- **Naming:** `PascalCase` for classes, `camelCase` for methods/variables.
- **One class per file.**
- **No external libraries** — standard Java only (`java.awt`, `javax.swing`, `java.io`, `java.util`).
- GUI updates always via `SwingUtilities.invokeLater()`.
- Background threads always set as daemon: `thread.setDaemon(true)`.

---

## Testing

No automated test suite. Manual smoke test:

1. `javac *.java && java SoundUp`
2. Sign in with any name → Main Menu appears.
3. Start a race with distance 0.4 km (≈10 iterations, ~5 seconds).
4. Verify BPM, average, distance, and song labels update live.
5. Let it finish → summary dialog appears → `races.csv` is created.
6. "See past races" → table shows the saved race.
7. Run a second race → table shows 2 rows.
8. Press "End of the race" mid-run → summary still appears, race is saved.

---

## Development Workflow

### Adding Music
Add entries to the appropriate `.txt` file (3 lines: name, author, bpm). No code changes needed.

### Adding a New BPM Category
1. Create a new `.txt` data file.
2. Add a new `ArrayList<Music>` field in `Playlist` and load it in `readPlaylist()`.
3. Add a new threshold condition in `chooseMusic()`.

### Changing the Simulation Speed
Edit the `millis` variable in `User.updateBpm()` (default: 500 ms per iteration).

### Adding User Persistence (future)
Store `userName` + `age` to a `users.csv` and load on sign-in, similar to `RaceHistory`.

---

## Environment

- **Java:** 21 (`java -version` → `21.0.10`)
- **IDE:** Any editor + terminal; or NetBeans (`.form` file for reference)
- **No environment variables required** by the application.
- Compile and run from the project directory so relative file paths resolve correctly.
