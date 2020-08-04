package org.gridiron.backend.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mindrot.jbcrypt.BCrypt
import java.util.UUID

class UserTest {
    private val username = "foobar"
    private val password = "12345678"
    private val salt = "\$2a\$10\$66Hi9dn3jE7oVCWNB3gHa."
    private val email = "foo@bar.com"

    private val user = User.register(
        UUID.randomUUID(),
        username,
        password,
        salt,
        email
    )

    @Test
    fun `can not be created without a username`() {
        listOf("", "a", "ab").forEach {
            val e = assertThrows<IllegalArgumentException> {
                User.register(
                    UUID.randomUUID(),
                    it,
                    password,
                    salt,
                    email
                )
            }

            assertEquals("Username must not be shorter than 3 chars", e.message)
        }
    }

    @Test
    fun `can not be created with a username longer than 70 chars`() {
        val name = "".padEnd(71, 'a')
        val e = assertThrows<IllegalArgumentException> {
            User.register(
                UUID.randomUUID(),
                name,
                password,
                BCrypt.gensalt(),
                email
            )
        }

        assertEquals("Username must not be longer than 70 chars", e.message)
    }

    @Test
    fun `can not register without an email`() {
        val e = assertThrows<IllegalArgumentException> {
            User.register(
                UUID.randomUUID(),
                username,
                password,
                salt,
                ""
            )
        }

        assertEquals("Email must not be blank", e.message)
    }

    @Test
    fun `can not register without a password`() {
        listOf("", "a", "1234567").forEach {
            val e = assertThrows<IllegalArgumentException> {
                User.register(
                    UUID.randomUUID(),
                    username,
                    it,
                    salt,
                    email
                )
            }

            assertEquals("Password must not be shorter than 8 chars", e.message)
        }
    }

    @Test
    fun `can not register without a salt`() {
        val e = assertThrows<IllegalArgumentException> {
            User.register(
                UUID.randomUUID(),
                username,
                password,
                "",
                email
            )
        }

        assertEquals("Salt must not be blank", e.message)
    }

    @Test
    fun `password is being hashed on register`() {
        assertEquals("\$2a\$10\$66Hi9dn3jE7oVCWNB3gHa.xaw68Urs.k7JLz/kcNfwE2Cxr/1vx5G", user.password)
    }

    @Test
    fun `can check password`() {
        assertTrue(user.authenticate(password))
        assertFalse(user.authenticate("swordfish"))
    }

    @Test
    fun `can score in bets`() {
        assertEquals(0, user.score)
        user.scored(42)
        assertEquals(42, user.score)
    }
}
