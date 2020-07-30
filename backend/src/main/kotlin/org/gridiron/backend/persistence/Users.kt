package org.gridiron.backend.persistence

import org.jetbrains.exposed.sql.Table

object Users : Table(name = "users") {
    val uuid = uuid("uuid")
    val email = varchar("email", 180).uniqueIndex()
    val username = varchar("login", 70).uniqueIndex()
    val password = varchar("password", 100)
    val isAdmin = bool("is_admin").default(false)
    val active = bool("active").default(false)

    override val primaryKey = PrimaryKey(uuid)
}
