package org.gridiron.backend.model

import kotlin.math.absoluteValue

data class Score(val away: Int, val home: Int) {
    val tie = away == home
    val awayWon = away > home
    val homeWon = home > away

    operator fun minus(other: Score): Int {
        return (away - other.away).absoluteValue + (home - other.home).absoluteValue
    }
}
