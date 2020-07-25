package org.gridiron.backend

import org.jetbrains.exposed.sql.Table

object Teams : Table() {
    val uuid = uuid("uuid")
    val name = varchar("name", 180).uniqueIndex()

    override val primaryKey = PrimaryKey(uuid)
}
