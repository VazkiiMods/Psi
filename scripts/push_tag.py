import os
import re
from jproperties import Properties


def main():
    build = Properties()
    with open("gradle.properties", "rb") as f:
        build.load(f, "utf-8")

    mc_version, mcv_meta = build["minecraft_version"]
    version, v_meta = build["mod_version"]

    print("MC Version:", mc_version)
    print("Version:", version)

    changelog = ""
    with open("./web/changelog.txt", "r") as f:
        content = f.read()
        content = content.replace('"', "'")
        lines = content.splitlines()
        for line in lines[1:]:
            if not line.startswith("*"):
                break
            changelog = changelog + '-m "' + line + '\n" '

    tag_success = os.system(
        "git tag -a release-{}-{} {}".format(
            mc_version, version, changelog
        )
    )

    if tag_success != 0:
        print("Failed to create tag")
        return
    else:
        print("Created tag")

    build["mod_version"] = str(int(version) + 1)
    with open("gradle.properties", "wb") as f:
        build.store(f, encoding="utf-8")

    os.system("git commit -a -m build")
    os.system(
        "git push origin master release-{}-{}".format(
            mc_version, version
        )
    )


if __name__ == "__main__":
    main()
