package org.gridiron.backend.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class TeamTest {
    @Test
    fun `can not be created without a name`() {
        assertThrows<IllegalArgumentException> {
            Team(UUID.randomUUID(), "", null)
        }

        assertThrows<IllegalArgumentException> {
            Team(UUID.randomUUID(), "KR", null)
        }
    }

    @Test
    fun `can not be created with a name longer than 100 chars`() {
        val name = "".padEnd(101, 'a')

        assertThrows<IllegalArgumentException> {
            Team(UUID.randomUUID(), name, null)
        }
    }

    @Test
    fun `can get name`() {
        assertEquals("Foobar football team", Team(UUID.randomUUID(), "Foobar football team", null).name)
    }
}
