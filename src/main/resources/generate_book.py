#!/usr/bin/python3

from sys    import argv, stdin, stderr, stdout
from json   import dump as dump_json, load as load_json, loads
from re     import sub, compile as re_compile
from os     import makedirs, environ

i18n = False
bookpfx = None
lang = sub("\..*", "", environ.get("LANG", "en_us")).lower()
modname = None
basepath = None
imagepath = "textures/gui/entries/{}.png"

if len(argv) < 2:
    print("Usage: {} [<input file>] [<base lang file>] [<output file>]".format(argv[0]), file=stderr)
    exit(11)

# note: `names` maps $(item) names to entry names; `paths` maps entry names to link names

def resolve_file(index, mode, default=None):
    return open(argv[index], mode) if len(argv) > index and argv[index] != "-" else default

lang_file = {}
fin = resolve_file(2, "r")
if fin:
    lang_file = load_json(fin) or {}
    fin.close()


camelcased = lambda snakecase: sub("_([a-z])", lambda match: match.group(1).upper(), sub("_([0-9])", r"\1", snakecase))

book_pat = re_compile(r"@BOOK\s*\((\w+)\)(?: DATA@@({[^@]+})@@)?((?: MAP\{[^-]+->[^]]*\})+)(?: i18n:(\S+))?(?: icon:(\S+))? ([^;]*)(?:;\s*(.*))?")
section_pat = re_compile(r"##(\w+) icon:(\S+) ([^;]*)(?:;\s*(.*))?")
entry_pat = re_compile(r"#(\w+)(\+?)(?: gate:(\w+))?(?: turnin:(\w+))?(?: icon:(\S+))? (.*)")
anchor_pat = re_compile(r"#!(\w+)\s*(?:\(([^)]+)\))?\s+(.*)")
page_patterns = [
    ("crafting",             re_compile(r"@RECIPE\s*\((\w+)\)(?:: (.*))?"),                 lambda match: (match.group(2), {"recipe": modpfx + match.group(1)})),
    ("crafting_multi",       re_compile(r"@RECIPES\s*\((\w*):([\w;]+)\)(?:: (.*))?"),       lambda match: (match.group(3), {"recipes": [modpfx + match.group(1) + rec for rec in match.group(2).split(";")]})),
    ("spotlight",            re_compile(r"@ITEM\s*\((\w+)(\+)?\)(?:: (.*))?"),              lambda match: (match.group(3), {"item": modpfx + "{}".format(match.group(1)), "link_recipe": bool(match.group(2))})),
    ("spellpiece_spotlight", re_compile(r"@PIECE\s*\((\w+)\)(?:: (.*))?"),                  lambda match: (match.group(2), {"title": modname + ".spellpiece." + match.group(1), "spellpiece": modpfx + "{}".format(match.group(1))})),
    ("image",                re_compile(r"@IMAGE\s*(B)?\(([\w,]+)\)(?:: (.*))?"),           lambda match: (match.group(3), {"images": [modpfx + imagepath.format(fn) for fn in match.group(2).split(",")], "border": bool(match.group(1))})),
    ("link",                 re_compile(r"@URL\s*\(([^)]+)\)\s*([^:]+)\s*(?:: (.*))?"),     lambda match: (match.group(3), {"url": match.group(1), "link_text": match.group(2)})),
    ("relations",            re_compile(r"@LIST\s*\((?:([\w/]+):)?([\w,/#]+)\)\s*([^:]+)\s*(?:: (.*))?"), lambda match: (match.group(4), {"entries": [(match.group(1) or "") + it for it in match.group(2).split(",")], "title": match.group(3)})),
    ("text",                 re_compile(".*"),                          lambda match: (match.group(0), {}))
]

def langput(langkey, value):
    if bookpfx:
        langkey = bookpfx + langkey
        lang_file[langkey] = value
        return langkey
    return value

def dump_entry(entry_path, entry_data):
    with open("{}/entries/{}.json".format(basepath, entry_path), "w") as entry_file:
        dump_json(entry_data, entry_file, indent=2)

if __name__ == "__main__":
    with resolve_file(1, "r", stdin) as fin:
        section = None
        secnum = 0
        entry = None
        entry_key = None
        entry_data = None
        for line in fin:
            line = line[:-1] # strip newline
            if not line.strip() or line.strip().startswith("//"):
                continue
            if line == "__END__":
                break
            if line.strip().startswith("@MOD "):
                modname = line.strip()[5:].strip()
                modpfx = modname + ":"
                continue
            matcher = book_pat.fullmatch(line)
            if matcher:
                bookid, extradata, mapstr, i18pfx, icon, langname, langdesc = matcher.groups()
                if not modname:
                    print("E: Found book {} before a modname".format(bookid), file=stderr)
                    exit(22)
                if i18pfx:
                    bookpfx = modname + "." + i18pfx
                    i18n = True
                else: bookpfx = None
                basepath = "assets/" + modname + "/patchouli_books/" + bookid + "/" + lang
                makedirs(basepath + "/categories", exist_ok=True)
                book_json = {
                    "name": langput("name", langname),
                    "landing_text": langput("landing_text", langdesc) if langdesc else langput("name", langname),
                    "i18n": bool(i18pfx),
                }
                if extradata: book_json.update(loads(extradata))
                if icon: book_json["model"] = modpfx + icon
                if mapstr:
                    maps = mapstr[5:-1].split("] MAP[")
                    mdata = {}
                    for mp in maps:
                        k, v = mp.split("->", maxsplit=1)
                        mdata[k] = v
                    book_json["macros"] = mdata
                with open("data/" + modname + "/patchouli_books/" + bookid + "/" + "book.json", "w") as book_file:
                    dump_json(book_json, book_file, indent=2)
                continue
            matcher = section_pat.fullmatch(line)
            if matcher:
                section, icon, langname, langdesc = matcher.groups()
                if not bookid:
                    print("E: Found section {} before a book".format(section), file=stderr)
                    exit(33)
                langkey = "category." + camelcased(section)
                makedirs(basepath + "/entries/" + section, exist_ok=True)
                with open("{}/categories/{}.json".format(basepath, section), "w") as category_file:
                    dump_json({
                        "name": langput(langkey, langname),
                        "description": langput(langkey + ".desc", langdesc) if langdesc else langput(langkey, langname),
                        "icon": modpfx + icon,
                        "sortnum": secnum
                    }, category_file, indent=2)
                secnum += 1
                continue
            matcher = entry_pat.fullmatch(line)
            if matcher:
                if entry_data:
                    dump_entry(entry, entry_data)
                name, important, gate, turnin, icon, langname = matcher.groups()
                if not section:
                    print("E: Found entry {} before a section".format(name), file=stderr)
                    exit(44)
                entry = section + "/" + name
                entry_key = camelcased(name)
                entry_data = {
                    "name": langput("entry." + entry_key, langname),
                    "category": modpfx + section,
                    "icon": (modpfx + icon) if icon else "minecraft:air"
                }
                if important: entry_data["priority"] = True
                if gate: entry_data["advancement"] = modpfx + "main/" + icon
                if turnin: entry_data["turnin"] = modpfx + "main/" + icon
                entry_data["pages"] = []
                pageidx = 0
                continue
            if not entry:
                print("E: Found pageline before an entry:", line, file=stderr)
                exit(55)
            anchor = None
            title = None
            matcher = anchor_pat.fullmatch(line)
            if matcher:
                anchor, title, line = matcher.groups()
            for page, regex, cb in page_patterns:
                matcher = regex.fullmatch(line)
                if matcher:
                    base = {"type": page}
                    line, add = cb(matcher)
                    if title: base["title"] = langput("subsec." + entry_key + "." + str(pageidx), title)
                    if line:
                        base["text"] = langput("page." + entry_key + "." + str(pageidx), line)
                        pageidx += 1
                    base.update(add)
                    if anchor: base["anchor"] = anchor
                    entry_data["pages"].append(base)
                    break
            else:
                print("E: Unknown page type for line:", line, file=stderr)
                exit(66)
        if entry_data:
            dump_entry(entry, entry_data)
    if i18n:
        with resolve_file(3, "w", stdout) as out:
            dump_json(lang_file, out, indent=2)
