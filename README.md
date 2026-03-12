# de.rsev.minecraft.forge.controller

A modular **Minecraft Lua controller** providing a **runtime Lua VM environment** for dynamic scripting, command registration, and event handling across multiple server platforms.

This repository acts as the **root repository** for the project.  
The current beta release is **v2.0.0-beta**.

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

    /lua heal
    /lua heal <playername>

---

## Supported Platforms

The project currently provides dedicated platform modules for:

- Forge 1.20.1
- Paper 1.20.1

The Paper module can be used on **PaperMC**, **Bukkit**, and **Spigot-compatible** server setups.

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

All controller and Lua-related commands are organized under:

    /lua

The help command is available directly as:

    /lua help


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

Release channels may be organized like this:

    versions/beta/1-0-0
    versions/1-0-1
    versions/stable/1-0-2

Platform-specific builds are created from the `mc-*` modules and compiled independently against the shared `common` module.

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

