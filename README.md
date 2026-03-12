# de.rsev.minecraft.forge.controller

A modular **Minecraft Forge controller mod** providing a **runtime Lua VM environment** for dynamic scripting, command registration, and event handling.

This repository acts as the **root repository** for the project.  
Specific versions of the mod will be organized in **separate subdirectories**.

The mod introduces a **Lua Runtime Compiler and VM management system** that allows Lua scripts to be compiled, executed, and dynamically reloaded during runtime. This enables advanced server-side automation such as quests, dynamic commands, event-driven logic, and custom gameplay systems.

---

## Overview

The controller provides a **triple Lua VM architecture**, designed for different runtime purposes.

### Asset Loading VM

A Lua VM that runs during the **early asset loading phase**, before the Forge freeze stage.

This environment is intended for:

- initialization logic
- static script preparation
- early resource configuration

---

### Event VMs

Dynamically reloadable Lua VMs that react to **Minecraft or mod-defined events**.

They are particularly useful for implementing systems such as:

- quests
- custom gameplay mechanics
- event-driven automation
- gameplay extensions

Event VMs can be **reloaded at runtime without restarting the server**.

---

### Command VMs

Command VMs allow **dynamic registration of custom commands implemented in Lua**.

Example: (This Command is not included, it could be defined in a Lua Script at rsev_controller/lua/worlds/<worldname>/commands/HelpCommand.lua)

    /rsev lua heal
    /rsev lua heal <playername>

---

### Lua VM Logging

I've got implemented an own Logger for Stack Error throwings and easy debugging.

If You execute system.print("YOUR MESSAGE HERE") in your Lua Script, 

your Message will be show at rsev_controller/lua/log/latest.log

---

## Lua API Documentation Generator

The mod includes a **Lua API documentation generator**.

This system can automatically generate a full documentation of all Lua-accessible methods exposed through the **Java ↔ Lua binary bridge**.

This documentation is especially useful for:

- AI coding assistants
- script development
- automated tooling
- external editors

---

## Runtime Reloading

Dynamic Lua environments can be reloaded during runtime.

The following VM types support hot reload:

- Event VMs
- Command VMs

This allows **rapid iteration without restarting the server**.

---

## Command Namespace Architecture

The controller uses a **structured command namespace system**.

Root namespace: 
   
    /rsev


The root namespace contains only the help command:
    
    /rsev help


All Lua-related commands are organized under:

    /rsev lua


This namespace provides tools for:

- Lua VM management
- runtime inspection
- Lua API documentation generation
- script reloading
- system diagnostics

A full list of hardcoded/implementet commands can be found at each version under:

➡ **[COMMANDS.md](COMMANDS.md)**

**ATTENTION: Some Commands could be different from Version to Version**

## Repository Structure

This repository is the **project root**.

Future mod versions will be organized like this:

    versions/beta/1-0-0
    versions/1-0-1
    versions/stable/1-0-2

Each version directory will contain the full Forge mod source.

---

## Target Environment

| Component | Version |
|-----------|--------|
| Minecraft | 1.20.1 |
| Forge | 47.4.16 |
| Java Runtime | JRE 17 |

---

## License

See LICENSE file for details.

