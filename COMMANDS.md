# Lua VM Controller Commands

This document describes all commands available in the **Lua VM Controller namespace**.

As of **v2.0.0-beta**, all controller commands are available directly under the `/lua` namespace.

---

# Command Namespace

    /lua

This namespace provides access to controller commands, Lua VM management, and runtime utilities.

### Help

    /lua help


Displays an overview of available controller commands.

---

# VM Management

### Reload Lua VMs

    /lua reload


Reloads all dynamic Lua environments.

Affected VMs:

- Event VMs
- Command VMs

This allows scripts to be updated **without restarting the server**.

---

# Generate Namespace

    /lua generate <command>


Commands used to generate runtime data or documentation.

---

### Generate Lua API Documentation

    /lua generate documentation


Automatically generates a complete documentation of all Lua-accessible methods exposed by the **Java ↔ Lua bridge**.

The generated documentation can be used for:

- Lua development
- AI coding assistants
- script editors
- API reference

---

# List Namespace
    
    /lua list <command>


Commands used to inspect the current Lua runtime environment.

---

### List Commands

    /lua list commands


Lists all currently registered Lua commands.

---

### List Events

   /lua list events

 
Displays all available event bindings.

---

### List Lua Files

    /lua list files


Lists all detected Lua script files.

---

### List Runtime Tasks

    /lua list runtimetasks


Displays currently running runtime tasks.

---

### List Scheduler Tasks

    /lua list scheduler


Lists all scheduled Lua tasks.

---

### List Timers

    /lua list timers


Shows active Lua timers.

---

### List VMs

    /lua list vms


Displays all currently running Lua VM instances.

---

# Command VM Usage

Lua command VMs allow dynamic command registration under the namespace:

    /lua <command>

Example (not included, you can create it by yourself at minecraft-lua-controller/lua/worlds/<worldname>/commands/HealCommand.lua):
    
    /lua heal
    /lua heal <player>

Commands can be implemented entirely in Lua scripts and are **reloadable at runtime**.

---

# Notes

- Event VMs and Command VMs are **hot reloadable**
- Asset Loading VMs run only during startup
- The controller automatically compiles Lua scripts into VM runtimes
