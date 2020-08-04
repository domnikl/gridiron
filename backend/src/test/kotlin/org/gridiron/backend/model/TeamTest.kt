package org.gridiron.backend.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class TeamTest {
    @Test
    fun `can not be created without a name`() {
        assertThrows<IllegalArgumentException> {
            Team(UUID.randomUUID(), "")
        }

        assertThrows<IllegalArgumentException> {
            Team(UUID.randomUUID(), "KR")
        }
    }

    @Test
    fun `can not be created with a name longer than 100 chars`() {
        val name = "".padEnd(101, 'a')

        assertThrows<IllegalArgumentException> {
            Team(UUID.randomUUID(), name)
        }
    }

    @Test
    fun `can get name`() {
        assertEquals("Foobar football team", Team(UUID.randomUUID(), "Foobar football team").name)
    }
}
