package org.gridiron.backend.persistence

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object Games : Table(name = "games") {
    val uuid = uuid("uuid")
    val team1 = uuid("team1") // references Team.uuid
    val team2 = uuid("team2") // references Team.uuid
    val start = datetime("start")

    override val primaryKey = PrimaryKey(uuid)
}
