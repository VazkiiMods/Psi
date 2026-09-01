# Spell Pieces: groups and settings

Every Spell Piece belongs to a group, and the group decides when a piece counts as learned. Groups used to be
a Java list; they're now a datapack registry, alongside a second registry that lets you switch individual
pieces off. Both load when the world loads, and `/reload` leaves them alone.

## Piece groups (`psi:spell_piece_group`)

```
data/<namespace>/psi/spell_piece_group/<name>.json
```

A group is a set of pieces the Programmer presents together, plus the rule that unlocks it. The group's id
(`<namespace>:<name>`) doubles as the id of the advancement Psi treats as "you know this group", found at
`data/<namespace>/advancement/<name>.json`.

### Schema

```json
{
  "main": "<piece id>",
  "pieces": ["<piece id>", "..."],
  "unlock": { "type": "psi:execute_main" }
}
```

| Field | Required | Notes |
|---|---|---|
| `main` | yes | The piece the group is presented by. |
| `pieces` | yes | The other members; `[]` is allowed. |
| `unlock` | no | Defaults to `{"type": "psi:execute_main"}`. |

`unlock.type` is one of two things:

- `psi:execute_main`, with no other fields
- `psi:advancement`, with `"advancement": "<advancement id>"`

### `psi:execute_main`

The first time you execute `main`, Psi posts `PieceGroupAdvancementComplete` for the group. Psi doesn't grant
the group's advancement itself; that event is the hook an addon or script uses to do so. Nothing is enforced
here: every piece in the group can be placed, compiled and cast whether or not you hold the advancement. All
29 of Psi's own groups work this way. One of them, straight from the generated data:

`data/psi/psi/spell_piece_group/tutorial1.json`

```json
{
  "main": "psi:selector_caster",
  "pieces": [
    "psi:trick_debug",
    "psi:trick_debug_spamless"
  ]
}
```

### `psi:advancement`

The group unlocks when you complete the named advancement (Psi also checks on login, for advancements you
already hold). Executing `main` does nothing for these groups. `PieceGroupAdvancementComplete` is posted when
the advancement completes, as above.

These groups are enforced, which is the whole point of them. Until you hold the advancement, every piece in
the group (`main` included) is, for you:

- hidden from the Programmer's piece menu;
- refused by the compiler with `The piece <name> is locked` (`psi.spellerror.locked_piece`), so a Spell
  Bullet or Spell Drive holding it goes inert;
- rejected on clipboard import with `Import error: The piece <name> is locked.` (`psimisc.locked_piece`).

Creative-mode players skip the lock. The check is per player, so on a shared server two mages with different
advancements get different answers about the same Spell.

Your client decides what to show from its own copy of advancement progress, and the server only sends it
advancements that have a `display` (an advancement with no `display` is never sent, even after you complete
it). Gate on one without a `display` and the piece menu stays empty for you forever, which is a fine prank
and a poor progression system. Psi's own group advancement stubs have no `display`, so don't point
`psi:advancement` at them; make your own.

Gating a new group on a custom advancement:

`data/mypack/psi/spell_piece_group/lightning.json`

```json
{
  "main": "psi:trick_smite",
  "pieces": [
    "psi:trick_blaze",
    "psi:trick_torrent"
  ],
  "unlock": {
    "type": "psi:advancement",
    "advancement": "mypack:storm_caller"
  }
}
```

A piece belongs to the first group Psi finds it in. To move a Psi piece into your own gated group, override
the Psi group that currently lists it (a same-path file with the piece removed) and list it in yours.

### Psi's groups

The 29 defaults, with every piece's membership, are in
`Xplat/src/generated/resources/data/psi/psi/spell_piece_group/`; a file at the same path in your datapack
replaces that group. The data comes from `Xplat/src/main/java/vazkii/psi/data/PsiSpellPieceGroups.java`.

## Piece settings (`psi:spell_piece_settings`)

```
data/<piece namespace>/psi/spell_piece_settings/<piece path>.json
```

The entry id is the piece id. A piece with no entry is enabled.

### Schema

```json
{
  "enabled": false
}
```

`enabled` is required; `true` means the same as having no file.

### What `enabled: false` does

For everyone, Creative mode included:

- the piece disappears from the Programmer's piece menu;
- compilation fails with `The piece <name> is disabled` (`psi.spellerror.disabled_piece`), so every Spell
  Bullet, Spell Drive and Flash Ring already holding it goes inert (the item stays; the Spell no longer
  compiles);
- clipboard import is rejected with `Import error: The piece <name> is disabled.` (`psimisc.disabled_piece`).

Spells containing the piece still deserialize, so re-enabling it brings those Spells back.

### Example

Nobody gets Trick: Explode:

`data/psi/psi/spell_piece_settings/trick_explode.json`

```json
{
  "enabled": false
}
```

Piece ids are the `psi.spellpiece.<path>` keys in `Xplat/src/main/resources/assets/psi/lang/en_us.json`, or
the values listed in the generated group files above.
