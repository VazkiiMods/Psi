#!/usr/bin/python3

from sys            import argv, stderr, stdout
from json           import load as load_json
from re             import sub, search, compile as re_compile
from collections    import defaultdict
from generate_book  import entry_pat

if len(argv) < 2:
    print("Usage: {} <mappings file> [<output file> [<error file]]".format(argv[0]))
    exit()

# note: `names` maps $(item) names to entry names; `paths` maps entry names to link names

def resolve_file(index, mode, default=None):
    return open(argv[index], mode) if len(argv) > index else default

with resolve_file(1, "r") as fin:
    data = load_json(fin)
unknowns = defaultdict(lambda: set()) # so they're buffered all after stdout

item_pattern = re_compile(r"(?<!\))\$\((\w+)\)([^$]+)\$\([0r]\)(?!\$)")

if __name__ == "__main__":
    with open(data["file"], "r") as fin, \
            resolve_file(2, "w", stdout) as out:
        for line in fin:
            line = line[:-1] # strip newline
            if not line.strip():
                print(line, file=out)
                continue
            matcher = entry_pat.fullmatch(line)
            if matcher:
                suffix = matcher.group(1)
            def item_cb(matcher):
                whole = matcher.group(0)
                key = matcher.group(1)
                phrase = matcher.group(2)
                if key not in data: return whole
                if phrase in data[key]["ignore"]: return whole
                if phrase not in data[key]["names"]:
                    unknowns[key].add('\t\t\t"{}",'.format(phrase))
                    return whole
                if search(r"/{}(#|$)".format(suffix), data[key]["names"][phrase]):
                    return whole
                return "$(l:{})$({}){}$(0)$(/l)".format(data[key]["names"][phrase], key, phrase)
            print(sub(item_pattern, item_cb, line), file=out)
    with resolve_file(3, "w", stderr) as err:
        if len(unknowns):
            print("===UNKNOWN ITEMS:===", file=err)
            for key in unknowns.keys():
                print("-> $({}):".format(key), file=err)
                for warn in sorted(unknowns[key]):
                    print(warn, file=err)
        if errors:
            print("===ERRORS:===", file=err)
            for bad in sorted(errors):
                print(bad, file=err)
