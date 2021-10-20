#!/usr/bin/env python3
import os

from typing import List, Optional, Iterable, Union

import cv2
import numpy as np
import colorsys

TXT_RED = "\033[91m"
TXT_YELLOW = "\033[93m"
TXT_RESET = "\033[0m"
TXT_BOLD = "\033[1m"

WHITE = [255, 255, 255]
GRAY = [91, 91, 91]
SHADE = [0, 0, 0]
BACKGROUND = [21, 21, 21]

LIGHT_EDGE = [42, 42, 42]
DARK_EDGE = [11, 11, 11]
DARK_CORNER = [5, 5, 5]

FILL_COLORS = [
    [GRAY, LIGHT_EDGE, BACKGROUND],
    [LIGHT_EDGE, BACKGROUND, DARK_EDGE],
    [BACKGROUND, DARK_EDGE, DARK_CORNER]
]

O = 0
B = 8
I = 9

# 0 - Transparent
# 1 - Gray
# 2 - Light Edge
# 3 - Background
# 4 - Dark Edge
# 5 - Dark Corner
# 8 - Boundary
# 9 - Main Body

COLOR_MAPPING = {
    1: GRAY,
    2: LIGHT_EDGE,
    3: BACKGROUND,
    4: DARK_EDGE,
    5: DARK_CORNER
}

ALL_SHAPES = [
    [
        [1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3],
        [2, B, B, B, B, B, B, B, B, B, B, B, B, B, B, 4],
        [2, B, I, I, I, I, I, I, I, I, I, I, I, I, B, 4],
        [2, B, I, I, I, I, I, I, I, I, I, I, I, I, B, 4],
        [2, B, I, I, I, I, I, I, I, I, I, I, I, I, B, 4],
        [2, B, I, I, I, I, I, I, I, I, I, I, I, I, B, 4],
        [2, B, I, I, I, I, I, I, I, I, I, I, I, I, B, 4],
        [2, B, I, I, I, I, I, I, I, I, I, I, I, I, B, 4],
        [2, B, I, I, I, I, I, I, I, I, I, I, I, I, B, 4],
        [2, B, I, I, I, I, I, I, I, I, I, I, I, I, B, 4],
        [2, B, I, I, I, I, I, I, I, I, I, I, I, I, B, 4],
        [2, B, I, I, I, I, I, I, I, I, I, I, I, I, B, 4],
        [2, B, I, I, I, I, I, I, I, I, I, I, I, I, B, 4],
        [2, B, I, I, I, I, I, I, I, I, I, I, I, I, B, 4],
        [2, B, B, B, B, B, B, B, B, B, B, B, B, B, B, 4],
        [3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5]
    ], [
        [O, O, O, O, O, 2, 2, 2, 2, 2, 3, O, O, O, O, O],
        [O, O, O, O, O, 2, B, B, B, B, 4, O, O, O, O, O],
        [O, O, O, O, O, 2, B, I, I, B, 4, O, O, O, O, O],
        [O, O, O, 1, 2, 2, B, I, I, B, 4, 4, 4, O, O, O],
        [O, O, O, 2, B, B, I, I, I, I, B, B, 4, O, O, O],
        [2, 2, 2, 2, B, I, I, I, I, I, I, B, 4, 4, 4, 4],
        [2, B, B, B, I, I, I, I, I, I, I, I, B, B, B, 4],
        [2, B, I, I, I, I, I, I, I, I, I, I, I, I, B, 4],
        [2, B, I, I, I, I, I, I, I, I, I, I, I, I, B, 4],
        [2, B, B, B, I, I, I, I, I, I, I, I, B, B, B, 4],
        [2, 2, 2, 2, B, I, I, I, I, I, I, B, 4, 4, 4, 5],
        [O, O, O, 2, B, B, I, I, I, I, B, B, 4, O, O, O],
        [O, O, O, 2, 2, 2, B, I, I, B, 4, 4, 4, O, O, O],
        [O, O, O, O, O, 2, B, I, I, B, 4, O, O, O, O, O],
        [O, O, O, O, O, 2, B, B, B, B, 4, O, O, O, O, O],
        [O, O, O, O, O, 3, 4, 4, 4, 4, 5, O, O, O, O, O]
    ], [
        [1, 2, 2, 2, 2, 2, 3, O, O, 1, 2, 2, 2, 2, 2, 3],
        [2, B, B, B, B, B, 4, O, O, 2, B, B, B, B, B, 4],
        [2, B, I, I, I, B, 4, O, O, 2, B, I, I, I, B, 4],
        [2, B, I, I, I, B, 4, O, O, 2, B, I, I, I, B, 4],
        [2, B, I, I, I, B, 4, O, O, 2, B, I, I, I, B, 4],
        [2, B, B, B, B, B, 4, O, O, 2, B, B, B, B, B, 4],
        [3, 4, 4, 4, 4, 4, 5, O, O, 3, 4, 4, 4, 4, 4, 5],
        [O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O],
        [O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O],
        [1, 2, 2, 2, 2, 2, 3, O, O, 1, 2, 2, 2, 2, 2, 3],
        [2, B, B, B, B, B, 4, O, O, 2, B, B, B, B, B, 4],
        [2, B, I, I, I, B, 4, O, O, 2, B, I, I, I, B, 4],
        [2, B, I, I, I, B, 4, O, O, 2, B, I, I, I, B, 4],
        [2, B, I, I, I, B, 4, O, O, 2, B, I, I, I, B, 4],
        [2, B, B, B, B, B, 4, O, O, 2, B, B, B, B, B, 4],
        [3, 4, 4, 4, 4, 4, 5, O, O, 3, 4, 4, 4, 4, 4, 5]
    ]
]

ERR = [0, 0, 255]


def color_equal(this: Union[Iterable[int], np.ndarray], that: Union[Iterable[int], np.ndarray]) -> bool:
    max_len = min(len(this), len(that))
    return np.array_equal(this[:max_len], that[:max_len])


def color_not_equal(this: Union[Iterable[int], np.ndarray], that: Union[Iterable[int], np.ndarray]) -> bool:
    max_len = min(len(this), len(that))
    return not np.array_equal(this[:max_len], that[:max_len])


def with_alpha(color: List[int], do: bool = True):
    if not do or len(color) == 4:
        return color
    color = color[:]
    color.append(255)
    return color


def is_mod_color(color: List[int]):
    hsv = colorsys.rgb_to_hsv(color[2] / 255.0, color[1] / 255.0, color[0] / 255.0)
    return 0.15 <= hsv[1] <= 0.35 and hsv[2] == 1


def target_color(value: int, alpha: bool) -> Optional[List[int]]:
    if value == 8 or value == 9:
        return

    if value == 0:
        return [0, 0, 0, 0]

    return with_alpha(COLOR_MAPPING[value], alpha)


def find_shape(texture: np.ndarray) -> Optional[List[List[int]]]:
    max_shape = None
    max_potential = 0
    for potential_shape in ALL_SHAPES:
        potential = 0
        for row_index in range(16):
            row = potential_shape[row_index]
            for col_index in range(16):
                value = row[col_index]
                color_at = texture[row_index, col_index]
                if value == 0:
                    potential += (len(color_at) == 4 and color_at[3] == 0)
                    potential -= (len(color_at) != 4 or color_at[3] == 255)
                elif value < 8:
                    potential -= (len(color_at) == 4 and color_at[3] != 255)
                    potential += (color_equal(color_at, COLOR_MAPPING[value])) * 2 - 1
        if potential > max_potential:
            max_potential = potential
            max_shape = potential_shape
    return max_shape


def find_warnings(texture: np.ndarray, tex_shape: List[List[int]]) -> List[str]:
    out = []

    for x in range(16):
        for y in range(16):
            color_at = texture[y, x]

            target_value = tex_shape[y][x]

            target = target_color(target_value, texture.shape[2] == 4)
            if target is not None and color_not_equal(color_at, target):
                if len(target) != 4 or len(color_at) != 4 or color_at[3] != target[3]:
                    out.append("The edge wasn't the expected color at ({0}, {1})".format(str(x), str(y)))
                img[y, x] = target

            within = target_value == 9
            if within and (color_equal(color_at, WHITE) or is_mod_color(color_at)):
                shadow_color = texture[y + 1, x + 1]
                if color_not_equal(shadow_color, WHITE) and \
                        color_not_equal(shadow_color, GRAY) and \
                        color_not_equal(shadow_color, SHADE) and \
                        not is_mod_color(shadow_color):
                    out.append("The shadows weren't populated as expected at ({0}, {1})".format(str(x), str(y)))
                    img[y + 1, x + 1] = with_alpha(SHADE, texture.shape[2] == 4)

            within = target_value == 8 or target_value == 9
            if within and color_equal(color_at, SHADE):
                shader_color = texture[y - 1, x - 1]
                if color_not_equal(shader_color, WHITE) and not is_mod_color(shader_color):
                    out.append("Stray shadow at ({0}, {1})".format(str(x), str(y)))
                    img[y, x] = with_alpha(BACKGROUND, texture.shape[2] == 4)

    return out


def find_soft_warnings(texture: np.ndarray, tex_shape: List[List[int]]) -> List[str]:
    out = []

    for x in range(16):
        for y in range(16):
            color_at = texture[y, x]
            target_value = tex_shape[y][x]

            within = target_value == 8
            if within and (color_equal(color_at, WHITE) or is_mod_color(color_at)):
                out.append("The boundaries had color at ({0}, {1})".format(str(x), str(y)))

    return out


def find_errors(texture: np.ndarray, tex_shape: List[List[int]]) -> List[str]:
    out = []

    for x in range(16):
        for y in range(16):
            color_at = texture[y, x]
            target_value = tex_shape[y][x]
            within = target_value == 8 or target_value == 9
            if within and \
                    color_not_equal(color_at, WHITE) and \
                    color_not_equal(color_at, GRAY) and \
                    color_not_equal(color_at, SHADE) and \
                    color_not_equal(color_at, BACKGROUND) and \
                    not is_mod_color(color_at):
                if color_equal(color_at, [98, 98, 98]):
                    img[y, x] = with_alpha(GRAY, texture.shape[2] == 4)
                else:
                    out.append("There was an invalid color {0} at ({1}, {2})".format(str(color_at), str(x), str(y)))
                    img[y, x] = with_alpha(ERR, texture.shape[2] == 4)

    return out


if __name__ == "__main__":
    files = [f for f in os.listdir(os.getcwd()) if f.endswith(".png")]
    for file_name in files:
        img = cv2.imread(file_name, cv2.IMREAD_UNCHANGED)

        was_gray = len(img.shape) == 2 or img.shape[2] == 1

        if was_gray:
            img = cv2.cvtColor(img, cv2.COLOR_GRAY2RGB)


        if img.shape[0] != 16 or img.shape[1] != 16:
            print(TXT_BOLD + "Searching: " + TXT_RESET + file_name)
            print(TXT_RED + TXT_BOLD + "Error: " + TXT_RESET + "Invalid dimensions")
            print()
        else:
            shape = find_shape(img)
            if not shape:
                print(TXT_BOLD + "Searching: " + TXT_RESET + file_name)
                print(TXT_RED + TXT_BOLD + "Error: " + TXT_RESET + "Can't interpret this file's shape")
                print()
            else:
                alpha_state = img.shape[2] == 4
                target_alpha_state = False
                for i in shape:
                    if target_alpha_state:
                        break
                    for j in i:
                        if j == 0:
                            target_alpha_state = True
                            break

                if target_alpha_state and not alpha_state:
                    img = cv2.cvtColor(img, cv2.COLOR_RGB2RGBA)
                elif not target_alpha_state and alpha_state:
                    img = cv2.cvtColor(img, cv2.COLOR_RGBA2RGB)

                errors = find_errors(img, shape)
                soft_warnings = find_soft_warnings(img, shape)
                warnings = find_warnings(img, shape)

                if was_gray:
                    warnings.append("Converted from grayscale")

                if target_alpha_state and not alpha_state:
                    warnings.append("Added alpha channel")
                elif not target_alpha_state and alpha_state:
                    warnings.append("Removed alpha channel")

                if errors or warnings or soft_warnings:
                    print(TXT_BOLD + "Searching: " + TXT_RESET + file_name)

                for error in errors:
                    print(TXT_RED + TXT_BOLD + "Error: " + TXT_RESET + error)
                for warning in warnings:
                    print(TXT_YELLOW + TXT_BOLD + "Warning: " + TXT_RESET + warning)
                for warning in soft_warnings:
                    print(TXT_YELLOW + "Info: " + TXT_RESET + warning)

                if errors or warnings:
                    print(TXT_BOLD + "Writing: " + TXT_RESET + file_name)
                    cv2.imwrite(file_name, img)

                if errors or warnings or soft_warnings:
                    print()



