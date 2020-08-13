package org.gridiron.backend.persistence

import org.jetbrains.exposed.sql.Table

object Users : Table(name = "users") {
    val uuid = uuid("uuid")
    val email = varchar("email", 180).uniqueIndex()
    val username = varchar("login", 70).uniqueIndex()
    val password = varchar("password", 100)
    val roles = varchar("roles", 255).default("")
    val score = integer("score").default(0)

    override val primaryKey = PrimaryKey(uuid)
}
