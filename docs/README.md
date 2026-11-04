# Psi: the pack maker's reference manual

Welcome to the part of Psi that you can rewrite without a compiler. Most of what used to be hard-coded numbers
and Java lists (the stats on every CAD component, which Spell Pieces belong to which group, which pieces exist
at all) now lives in datapack registries, and NeoForge packs get a KubeJS plugin on top. This guide tells you
where each thing lives and what the rules are; the linked entries go into detail.

| What you can change | Where it lives | Loader | Entry |
|---|---|---|---|
| CAD component stats, models and dye colors | `data/<item_ns>/psi/cad_component/<item_path>.json` | NeoForge + Fabric | [cad-components.md](cad-components.md) |
| Spell Piece groups and their unlock rules | `data/<ns>/psi/spell_piece_group/<name>.json` | NeoForge + Fabric | [spell-pieces.md](spell-pieces.md) |
| Disabling Spell Pieces | `data/<piece_ns>/psi/spell_piece_settings/<piece_path>.json` | NeoForge + Fabric | [spell-pieces.md](spell-pieces.md) |
| Events and script-defined Spell Pieces | `kubejs/startup_scripts`, `kubejs/server_scripts` | NeoForge only | [kubejs.md](kubejs.md) |

All three registries are synced datapack registries: the server loads them and hands every client a copy, so
your players don't need to install anything. Psi's own defaults are generated into
`Xplat/src/generated/resources/data/psi/psi/`, and a datapack file at the same path replaces the default.

## Three things to know before you start

1. Datapack registries are read when the world loads (dedicated server start, or opening a singleplayer
   world). `/reload` does not touch them; you'll need to leave the world and come back.
2. Turning a random item into a CAD component means giving it a `psi:cad_component` entry. Psi also generates
   `psi:cad_component/<type>` item tags (`assembly`, `core`, `socket`, `battery`, `dye`) for recipes and other
   mods to read, but it never checks them itself; add your item to the tag if you like, and know that the
   registry entry is what does the work.
3. Script pieces (KubeJS) have no client prediction; whatever they do happens on the server only.
