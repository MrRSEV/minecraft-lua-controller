# Lua VM Controller Commands

This document describes all commands available in the **Lua VM Controller namespace**.

The command system uses a **structured namespace hierarchy**.

---

# Root Namespace

    /rsev


The root namespace only contains a help command.

### Help

    /rsev help


Displays an overview of available controller commands.

---

# Lua Namespace

    /rsev lua


This namespace provides access to **Lua VM management and runtime utilities**.

---

# VM Management

### Reload Lua VMs

    /rsev lua reload


Reloads all dynamic Lua environments.

Affected VMs:

- Event VMs
- Command VMs

This allows scripts to be updated **without restarting the server**.

---

# Generate Namespace

    /rsev lua generate <command>


Commands used to generate runtime data or documentation.

---

### Generate Lua API Documentation

    /rsev lua generate documentation


Automatically generates a complete documentation of all Lua-accessible methods exposed by the **Java ↔ Lua bridge**.

The generated documentation can be used for:

- Lua development
- AI coding assistants
- script editors
- API reference

---

# List Namespace
    
    /rsev lua list <command>


Commands used to inspect the current Lua runtime environment.

---

### List Commands

    /rsev lua list commands


Lists all currently registered Lua commands.

---

### List Events

   /rsev lua list events

 
Displays all available event bindings.

---

### List Lua Files

    /rsev lua list files


Lists all detected Lua script files.

---

### List Runtime Tasks

    /rsev lua list runtimetasks


Displays currently running runtime tasks.

---

### List Scheduler Tasks

    /rsev lua list scheduler


Lists all scheduled Lua tasks.

---

### List Timers

    /rsev lua list timers


Shows active Lua timers.

---

### List VMs

    /rsev lua list vms


Displays all currently running Lua VM instances.

---

# Command VM Usage

Lua command VMs allow dynamic command registration under the namespace:

    /rsev lua <command>

Example (not included, you can create it by yorself at rsev_controller/lua/worlds/<worldname>/commands/HealCommand.lua):
    
    /rsev lua heal
    /rsev lua heal <player>

Commands can be implemented entirely in Lua scripts and are **reloadable at runtime**.

---

# Notes

- Event VMs and Command VMs are **hot reloadable**
- Asset Loading VMs run only during startup
- The controller automatically compiles Lua scripts into VM runtimes