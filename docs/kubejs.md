# KubeJS

On NeoForge, Psi ships a KubeJS plugin: every Psi event can be listened to from a script, and you can define
new Spell Pieces without touching Java. The plugin lives in `vazkii.psi.neoforge.compat.kubejs` and is only
class-loaded when KubeJS is installed (`kubejs` is an optional dependency, `[2101.7.0,)`). Psi builds against
`kubejs_version=2101.7.2-build.374` and `rhino_version=2101.2.7-build.81`, as pinned in `gradle.properties`.
The Fabric build has no KubeJS integration; datapacks work there.

Script pieces have no client prediction: whatever they do happens on the server only.

## Events

The event group is `PsiEvents`. Each handler wraps the Java event of the same name 1:1, with the same getters
and setters plus `isCanceled()` and `setCanceled(boolean)`; `event.cancel()` sets the Java event canceled.
Every event is posted on the script side the Java event fired on, so a server-side cast reaches
`server_scripts`, a client-side one reaches `client_scripts`, and `startup_scripts` listeners see both.

| Handler | Wraps | Members |
|---|---|---|
| `PsiEvents.assembleCad` | `AssembleCADEvent` | `getAssembler()`, `getCad()`, `setCad(stack)`, `getPlayer()` |
| `PsiEvents.cadStat` | `CADStatEvent` | `getStat()`, `getStatProvider()`, `getCad()`, `getComponent()`, `getStatValue()`, `setStatValue(int)` |
| `PsiEvents.cadTake` | `CADTakeEvent` | `getCancellationMessage()`, `setCancellationMessage(key)`, `getSound()`, `setSound(float)`, `getAssembler()`, `getCad()`, `getPlayer()` |
| `PsiEvents.postCadCraft` | `PostCADCraftEvent` | `getAssembler()`, `getCad()` |
| `PsiEvents.regenPsi` | `RegenPsiEvent` | `getPlayer()`, `getPlayerData()`, `getCad()`, the psi and regen amounts and their setters |
| `PsiEvents.spellCast` | `SpellCastEvent` | `getSpell()`, `getContext()`, `getPlayer()`, `getPlayerData()`, `getCad()`, `getBullet()` |
| `PsiEvents.preSpellCast` | `PreSpellCastEvent` | `getCost()`/`setCost`, `getSound()`/`setSound`, `getParticles()`/`setParticles`, `getCooldown()`/`setCooldown`, `getSpell()`/`setSpell`, `getContext()`/`setContext`, `getCancellationMessage()`/`setCancellationMessage`, `getPlayer()`, `getPlayerData()`, `getCad()`, `getBullet()` |
| `PsiEvents.pieceExecuted` | `PieceExecutedEvent` | `getPiece()`, `getPlayer()` |
| `PsiEvents.pieceKnowledge` | `PieceKnowledgeEvent` | `getPieceGroup()`, `getPieceName()`, `getPlayer()`, `getData()`, `isUnlocked()` |
| `PsiEvents.pieceGroupAdvancementComplete` | `PieceGroupAdvancementComplete` | `getPieceGroup()`, `getPiece()`, `getPlayer()` |
| `PsiEvents.loopcastEnd` | `LoopcastEndEvent` | `getPlayer()`, `getPlayerData()`, `getHand()`, `getLoopcastAmount()` |
| `PsiEvents.detonation` | `DetonationEvent` | `getPlayer()`, `getFocalPoint()`, `getRange()`, `getCharges()`, `addCharge(c)`, `removeCharge(c)` |
| `PsiEvents.programmerPopulate` | `ProgrammerPopulateEvent` | `getPlayer()`, `getSpellPieceRegistry()`, `setSpellPieceRegistry(r)` |
| `PsiEvents.psiArmor` | `PsiArmorEvent` | `getPlayer()`, `getType()`, `getDamage()`, `getAttacker()` |

A `kubejs/server_scripts/psi.js` that meddles with two of them:

```js
// Every Core is twice as complex as it says on the tin.
PsiEvents.cadStat(event => {
  if (event.getStat().getSerializedName() === 'complexity') {
    event.setStatValue(event.getStatValue() * 2)
  }
})

// No casting while swimming. The message is a translation key, shown in red chat.
PsiEvents.preSpellCast(event => {
  if (event.getPlayer().isInWater()) {
    event.setCancellationMessage('mypack.psi.no_casting_underwater')
    event.cancel()
  }
})
```

`cadStat` fires on every stat read, after the `psi:cad_component` lookup. When the number you want is a
constant, a datapack entry ([cad-components.md](cad-components.md)) is the cheaper way to get it.

## Script-defined Spell Pieces

You register pieces in `kubejs/startup_scripts` on the registry `psi:spell_piece_registry_type_key`. There
are four builder types, and you can name them by full id (`psi:trick`) or by path (`trick`):

| Type | What you get | Mandatory calls beyond the five stats |
|---|---|---|
| `crafting_trick` | A crafting Trick that `psi:trick_crafting` recipes can name | none |
| `trick` | A Trick that runs a callback | `param(...)` or `noParams()`, `execute(...)` |
| `operator` | An Operator that evaluates to a value | `param(...)` or `noParams()`, `returns(...)`, `foldable(...)`, `execute(...)` |
| `selector` | The `operator` builder, registered as a Selector | as `operator` |

Every builder call is mandatory, so there's nothing to guess about defaults. All four need `complexity(int)`,
`potency(int)`, `cost(int)`, `projection(int)` and `bandwidth(int)`. Forget anything and registration fails
with a list of what you forgot:

```
Spell piece 'kubejs:shout' is missing mandatory builder calls: bandwidth, execute
```

Missing params show up in that list as `param(...) or noParams()`.

### One complete example per kind

```js
StartupEvents.registry('psi:spell_piece_registry_type_key', event => {
  // Crafting Trick. Its behaviour is entirely the psi:trick_crafting recipes
  // that name it in their "piece" field.
  event.create('kubejs:infuse', 'crafting_trick')
    .complexity(1).potency(100).cost(1200).projection(1).bandwidth(0)

  // Trick. Whatever the callback returns is ignored.
  event.create('kubejs:shout', 'trick')
    .param('target', 'entity', false)
    .param('number', 'number', true)
    .complexity(1).potency(0).cost(5).projection(1).bandwidth(0)
    .execute((ctx, params) => {
      let times = params.number == null ? 1 : params.number
      for (let i = 0; i < times; i++) {
        ctx.caster.sendSystemMessage(Component.literal('Hey ' + params.target.getName().getString()))
      }
    })

  // Operator. Evaluates to a value of the declared type.
  event.create('kubejs:mul', 'operator')
    .param('number1', 'number', false)
    .param('number2', 'number', false)
    .returns('number')
    .foldable(true)
    .complexity(1).potency(0).cost(0).projection(0).bandwidth(0)
    .execute((ctx, params) => params.number1 * params.number2)

  // Selector. Same builder as an Operator.
  event.create('kubejs:caster_health', 'selector')
    .noParams()
    .returns('number')
    .foldable(false)
    .complexity(1).potency(0).cost(0).projection(0).bandwidth(0)
    .execute((ctx, params) => ctx.caster.getHealth())
})
```

### `param(name, type, canDisable)`

- `name` is the key you read the value under in `params`. Declaring a name twice throws
  `Spell piece '<id>' declares param 'x' twice`. The Programmer labels the parameter `psi.spellparam.<name>`;
  Psi ships labels for its own names (`target`, `number`, `number1` to `number4`, `vector1` to `vector4`,
  `position`, `min`, `max`, `power`, `x`, `y`, `z`, `radius`, `distance`, `time`, `base`, `ray`, `vector`,
  `axis`, `angle`, `list`, `list1`, `list2`, `direction`, `toggle`, `mask`, and a few more). For a new name,
  add `"psi.spellparam.<name>"` to your lang file.
- `type` is one of `number`, `vector`, `entity`, `entity_list`, `any`. Anything else throws
  `Unknown spell value type 'x', expected one of: number, vector, entity, entity_list, any`.
- `canDisable` says whether the caster may leave that side unset. A disabled or unconnected parameter
  arrives as `null`.

A piece with no inputs at all has to say so with `noParams()`.

### `returns(type)` and `foldable(bool)`, Operators and Selectors only

`returns` takes the same type names as `param`. Tricks evaluate to nothing and have neither call.

`foldable(true)` lets the compiler run your callback at compile time when its inputs are constants (a Constant:
Number feeding `kubejs:mul`, say). In that call `ctx` is `null`, and each parameter is the constant value of
the piece connected to it, or `null` when that piece isn't a constant. A callback that touches `ctx` needs
`foldable(false)`. Anything thrown while folding becomes a compile error at the piece's position.

### The callback

`execute((ctx, params) => ...)` receives:

- `ctx`, the `SpellContext` (`ctx.caster`, `ctx.focalPoint`, `ctx.loopcastIndex` and friends), or `null`
  while folding;
- `params`, a name -> value map of the evaluated inputs. `number` arrives as a Java `Number`, `vector` as
  `vazkii.psi.api.internal.Vector3`, `entity` as `Entity`, `entity_list` as
  `vazkii.psi.api.spell.wrapper.EntityListWrapper`.

The return value has to match `returns`: `number` -> any `Number`, `vector` -> `Vector3`, `entity` ->
`Entity`, `entity_list` -> `EntityListWrapper`, `any` -> anything. `undefined` and `null` are accepted and
become `null`. A Trick's return is discarded.

When it goes wrong, the caster sees a spell runtime error:

| Cause | Message (`en_us`) |
|---|---|
| Return value of the wrong type | `Script piece kubejs:mul returned String, expected number` (`psi.spellerror.script_return_type`) |
| Any JS throw or Java exception | `Script piece kubejs:shout failed: <message>` (`psi.spellerror.script`); the full stack trace also lands in the script console of the side it ran on |
| `throw Psi.error(key, ...args)` | The translation of `key` with `args`; nothing is logged |

`Psi.error('psi.spellerror.nulltarget')` borrows one of Psi's own messages; any key from your lang file works
just as well.

### Stats, names and textures

- The five stats feed the Spell's metadata exactly like a Java piece. A stat of `0` shows no label in the
  Programmer.
- The name key is `<namespace>.spellpiece.<path>` and the description is
  `<namespace>.spellpiece.<path>.desc`. Either write them into a lang file, or let the builder
  generate them: `.displayName('Operator: Multiply Twice')` and `.description('Multiplies two numbers.')`
  fill those two keys for you (Psi names its pieces "Trick: Debug", "Operator: Sum"; follow suit).
  The lang file route, for `kubejs:mul`, in `kubejs/assets/kubejs/lang/en_us.json`:

  ```json
  {
    "kubejs.spellpiece.mul": "Operator: Multiply Twice",
    "kubejs.spellpiece.mul.desc": "Multiplies two numbers."
  }
  ```

- The texture is `assets/<namespace>/textures/spell/<path>.png`, 16x16
  (`kubejs/assets/kubejs/textures/spell/mul.png`). Psi's block atlas config stitches every
  `textures/spell/` directory it can find, so there's nothing to register; a missing file renders as the
  missing texture, which you will notice.

### Crafting Tricks and recipes

A `crafting_trick` piece is matched by any `psi:trick_crafting` recipe whose `piece` is its id:

`kubejs/data/kubejs/recipe/diamond_from_coal.json`

```json
{
  "type": "psi:trick_crafting",
  "piece": "kubejs:infuse",
  "input": { "item": "minecraft:coal_block" },
  "output": { "id": "minecraft:diamond", "count": 1 },
  "cad": { "id": "psi:cad_assembly_iron", "count": 1 }
}
```

Crafting only checks `input` and `piece`; `cad` is the Assembly JEI shows as the catalyst. The piece has no
`execute`, since the recipes are its behaviour.

### Groups and locking

A script piece belongs to no group until a `psi:spell_piece_group` entry lists it, and a
`psi:spell_piece_settings` entry can disable it like any other piece. See [spell-pieces.md](spell-pieces.md)
and the registry section below.

## ClassFilter policy

Scripts can reach `vazkii.psi.api.*` (`Vector3`, `SpellContext`, `PsiAPI` and so on). `vazkii.psi.common`,
`vazkii.psi.client` and `vazkii.psi.neoforge` are denied.

## Datapack registries from scripts

The three registries need no Psi-specific KubeJS code, because `ServerEvents.registry` exposes every datapack
registry and `event.createFromJson(id, json)` runs your object through Psi's codec. The rules are those of the
JSON files (same field names, same load-time errors), and so is the timing: they take effect at world load, and `/reload` leaves them alone.

`kubejs/server_scripts/psi_registries.js`:

```js
// Disable Trick: Explode (data/psi/psi/spell_piece_settings/trick_explode.json).
ServerEvents.registry('psi:spell_piece_settings', event => {
  event.createFromJson('psi:trick_explode', { enabled: false })
})

// A group gated on an advancement (data/kubejs/psi/spell_piece_group/arithmetic.json).
ServerEvents.registry('psi:spell_piece_group', event => {
  event.createFromJson('kubejs:arithmetic', {
    main: 'kubejs:mul',
    pieces: ['kubejs:caster_health'],
    unlock: { type: 'psi:advancement', advancement: 'kubejs:learned_arithmetic' }
  })
})

// A Diamond as a Core (data/minecraft/psi/cad_component/diamond.json).
ServerEvents.registry('psi:cad_component', event => {
  event.createFromJson('minecraft:diamond', {
    type: 'core',
    stats: { complexity: 30, projection: 5 }
  })
})
```
