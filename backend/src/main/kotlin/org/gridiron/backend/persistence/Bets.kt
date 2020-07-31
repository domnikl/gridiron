package org.gridiron.backend.persistence

import org.jetbrains.exposed.sql.Table

object Bets : Table(name = "bets") {
    val game = uuid("game")
    val user = uuid("user")
    val away = integer("away")
    val home = integer("home")

    override val primaryKey = PrimaryKey(game, user)
}
