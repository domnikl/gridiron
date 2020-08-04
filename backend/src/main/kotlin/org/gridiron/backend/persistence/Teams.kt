package org.gridiron.backend.persistence

import org.jetbrains.exposed.sql.Table

object Teams : Table(name = "teams") {
    val uuid = uuid("uuid")
    val name = varchar("name", 100).uniqueIndex()

    override val primaryKey = PrimaryKey(uuid)
}
