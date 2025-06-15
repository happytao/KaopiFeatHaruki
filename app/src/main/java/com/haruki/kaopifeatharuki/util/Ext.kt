package com.haruki.kaopifeatharuki.util

import com.haruki.kaopifeatharuki.application.BaseApplication
import android.util.TypedValue


val Int.dp: Float
get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    BaseApplication.appContext.resources.displayMetrics
)

val String.name2Id:Int
    get() = when(this) {
        "ichika" -> 1
        "saki" -> 2
        "honami" -> 3
        "shihou" -> 4
        "minori" -> 5
        "haruka" -> 6
        "airi" -> 7
        "shizuku" -> 8
        "kohane" -> 9
        "an" -> 10
        "akito" -> 11
        "touya" -> 12
        "tsukasa" -> 13
        "emu" -> 14
        "neinei" -> 15
        "rui" -> 16
        "kanade" -> 17
        "mafuyu" -> 18
        "ena" -> 19
        "mizuki" -> 20
        "miku" -> 21
        "rin" -> 22
        "ren" -> 23
        "luka" -> 24
        "meiko" -> 25
        "kaito" -> 26
        else -> 0
    }

val Int.id2Name: String
    get() = when(this) {
        1 -> "ichika"
        2 -> "saki"
        3 -> "honami"
        4 -> "shihou"
        5 -> "minori"
        6 -> "haruka"
        7 -> "airi"
        8 -> "shizuku"
        9 -> "kohane"
        10 -> "an"
        11 -> "akito"
        12 -> "touya"
        13 -> "tsukasa"
        14 -> "emu"
        15 -> "neinei"
        16 -> "rui"
        17 -> "kanade"
        18 -> "mafuyu"
        19 -> "ena"
        20 -> "mizuki"
        21 -> "miku"
        22 -> "rin"
        23 -> "ren"
        24 -> "luka"
        25 -> "meiko"
        26 -> "kaito"
        else -> "unknown"
    }


