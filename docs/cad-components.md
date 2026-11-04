# CAD components

A CAD is built from up to five components (an Assembly, a Core, a Socket, a Battery and a Colorizer), and
each one contributes its own stats. Those stats used to be numbers baked into Psi's Java; now they're read
from the `psi:cad_component` registry, so you can rebalance every Core in the game, or turn a Diamond into
one, from a datapack.

## Where the files go

The entry id is the item id. Put a file at

```
data/<item_namespace>/psi/cad_component/<item_path>.json
```

and the item `<item_namespace>:<item_path>` becomes a component. `data/minecraft/psi/cad_component/diamond.json`
describes `minecraft:diamond`; `data/psi/psi/cad_component/cad_core_basic.json` describes `psi:cad_core_basic`
(and, since Psi ships that file, your copy replaces it).

The registry loads when the world loads and is synced to clients. `/reload` won't pick up a change; rejoin
the world.

## Schema

```json
{
  "type": "assembly | core | socket | battery | dye",
  "stats": { "<stat>": <int> },
  "model": "<namespace>:item/<path>",
  "color": "#RRGGBB"
}
```

| Field | Applies to | Required | Notes |
|---|---|---|---|
| `type` | all | yes | One of `assembly`, `core`, `socket`, `battery`, `dye`. |
| `stats` | `assembly`, `core`, `socket`, `battery` | no (default `{}`) | Only the stats owned by `type` are accepted (see below). A missing stat reads as `0`. |
| `model` | `assembly` | no (default `psi:item/cad_iron`) | The baked model the assembled CAD renders with. |
| `color` | `dye` | yes | `"#RRGGBB"` string or a packed RGB integer (`16711680`). |

### Which stats belong to which type

Each stat has one home. Ask a Core for `efficiency` and the entry refuses to load.

| `type` | Stat keys |
|---|---|
| `assembly` | `efficiency`, `potency` |
| `core` | `complexity`, `projection` |
| `socket` | `bandwidth`, `sockets`, `saved_vectors` |
| `battery` | `overflow` |
| `dye` | none |

A stat under the wrong type fails the whole entry with this in the log:

```
Stat complexity belongs to core components, not assembly
```

`-1` means infinite; the tooltip shows it as `∞`. The Creative Assembly uses `-1` for `efficiency` and
`potency`, `overflow: -1` is a bottomless Battery, and `saved_vectors: -1` gives you 255 memory slots.

### About `model`

The game only bakes models it has been told about. Psi registers `psi:item/cad_iron`, `psi:item/cad_gold`,
`psi:item/cad_psimetal`, `psi:item/cad_ebony_psimetal`, `psi:item/cad_ivory_psimetal` and
`psi:item/cad_creative`; pick one of those. Any other id renders as the missing model unless some mod
registers it.

## One example per type

These are Psi's own generated defaults, so you can copy them as-is.

`data/psi/psi/cad_component/cad_assembly_iron.json`

```json
{
  "type": "assembly",
  "model": "psi:item/cad_iron",
  "stats": {
    "efficiency": 70,
    "potency": 100
  }
}
```

`data/psi/psi/cad_component/cad_core_basic.json`

```json
{
  "type": "core",
  "stats": {
    "complexity": 14,
    "projection": 1
  }
}
```

`data/psi/psi/cad_component/cad_socket_basic.json`

```json
{
  "type": "socket",
  "stats": {
    "bandwidth": 5,
    "saved_vectors": 7,
    "sockets": 4
  }
}
```

`data/psi/psi/cad_component/cad_battery_basic.json`

```json
{
  "type": "battery",
  "stats": {
    "overflow": 100
  }
}
```

`data/psi/psi/cad_component/cad_colorizer_red.json`

```json
{
  "type": "dye",
  "color": "#FF0000"
}
```

## Overriding a Psi default

Drop a file at the same path in your datapack and yours wins. To make the Basic Core a little less basic:

`data/psi/psi/cad_component/cad_core_basic.json`

```json
{
  "type": "core",
  "stats": {
    "complexity": 20,
    "projection": 2
  }
}
```

Psi's 36 defaults (6 Assemblies, 5 Cores, 5 Sockets, 3 Batteries, 17 Colorizers) sit in
`Xplat/src/generated/resources/data/psi/psi/cad_component/`; that directory is the list of what you can
override. You won't find the Rainbow and Psi Colorizers there, since their color changes over time and stays
in Java.

## Turning an item into a component

Any item can be a component; it doesn't need to know it is one. A Diamond as a Core:

`data/minecraft/psi/cad_component/diamond.json`

```json
{
  "type": "core",
  "stats": {
    "complexity": 30,
    "projection": 5
  }
}
```

After a world rejoin the Diamond fits the CAD Assembler's Core slot and shows the component tooltip when you
hold Shift. If you also want it in `#psi:cad_component/core` for your recipes, add it to the tag yourself;
Psi never reads the tag to decide what is a component.

## Existing CADs

A CAD only stores the items sitting in its slots. Every stat read looks the component up in the registry on
the spot, so changing a JSON changes every CAD already in someone's inventory the next time the world loads. `CADStatEvent` still fires after that lookup, for the
cases where a number in a file isn't enough.

## For addon authors

The lookup order in `vazkii.psi.api.cad.CADComponentLookup` is: registry entry first, then the
`ICADComponent`, `ICADAssembly` and `ICADColorizer` interfaces. Once an item has a registry entry its Java
stats are never consulted.

Deprecated, and only read for items without an entry:

- `ICADComponent.getCADStatValue(ItemStack, EnumCADStat)`
- `ItemCADComponent.addStatToStack(Item, EnumCADStat, int)` and `ItemCADComponent.addStat`

Ship JSON instead. An `ItemCADComponent` subclass only needs `getComponentType`; the stats come from the
registry. A Colorizer whose color is dynamic keeps implementing `ICADColorizer.getColor` and gets no JSON
entry.
