# Task Tracker CLI

A lightweight command-line application for managing your tasks (to-do, in-progress, done) backed by a JSON file.

### Prerequisites

* Java 11+ (JDK installed and java on your PATH)
* A Unix-like shell (macOS/Linux) or PowerShell/CMD on Windows
---
### Distribution Structure

After building with the AppAssembler plugin, your distribution directory will look like:
```text
appassembler/
├── bin/
│   └── task-cli         # Launcher script (make sure it’s executable)
└── repo/
├── your-artifact.jar
└── (any dependency JARs)
```

Tip: You can zip the entire app/ folder for shipping:
```text
cd target/app
zip -r task-cli-distribution.zip bin lib
```
---
### Installation

##### Unzip (if you’ve archived):
```text
unzip task-cli-distribution.zip -d ~/task-cli
```

##### Add to PATH (optional):
```text
export PATH="$HOME/task-cli/bin:$PATH"
```
##### Verify installation:
```text
task-cli --help
```
### Basic Usage

All commands share this basic syntax:

```text
task-cli <command> [arguments]
```
---
#### Available Commands
| Command                          | Description                           |
|----------------------------------|---------------------------------------|
| `add <description>`              | Add a new task with the given text    |
| `update <id> <new description>`  | Update an existing task’s description |
| `delete <id>`                    | Remove a task by its ID               |
| `mark  <todo, inprogress, done>` | Change a task’s status                |
| `list  <todo, inprogress, done>` | List tasks (optionally filtered)      |
---
### Examples

#### Add tasks
```text
task-cli add "Buy groceries"
task-cli add "Write unit tests"
```

Adds two tasks (IDs 1 and 2).

#### List all tasks
```text
task-cli list
[1] Buy groceries (Status: TODO, created: ..., updated: ...)
[2] Write unit tests (Status: TODO, created: ..., updated: ...)
```

#### Mark a task in-progress
```text
task-cli mark 2 inprogress
Marked: [2] Write unit tests (Status: IN_PROGRESS, created: ..., updated: ...)
```

#### List only in-progress tasks
```text
task-cli list inprogress
```

#### Update task description
```text
task-cli update 1 "Buy groceries and snacks"
```

#### Delete a task
```text
task-cli delete 1
```
---
### Storage Format

Tasks are stored in a JSON array in tasks.json (or your custom file). Each entry:

```json
{
"id": 3,
"description": "Example",
"status": "DONE",
"createdAt": "2025-04-24T10:30:00Z",
"updatedAt": "2025-04-24T12:45:00Z"
}
```
---
### Troubleshooting

* File not found on startup: Ensure the launcher has write permission to its working directory. It auto-creates an empty file on first run.
* No output for list: Run without a filter (task-cli list) to confirm tasks exist.
---
Enjoy tracking your tasks from the terminal with ease!