package org.gridiron.backend.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ScoreTest {
    @Test
    fun `can detect if home won`() {
        assertTrue(Score(0, 1).homeWon)
        assertFalse(Score(1, 1).homeWon)
        assertFalse(Score(1, 0).homeWon)
    }

    @Test
    fun `can detect if away won`() {
        assertTrue(Score(1, 0).awayWon)
        assertFalse(Score(1, 1).awayWon)
        assertFalse(Score(0, 1).awayWon)
    }

    @Test
    fun `can detect tie game`() {
        assertTrue(Score(1, 1).tie)
        assertFalse(Score(1, 0).tie)
        assertFalse(Score(0, 1).tie)
    }

    @Test
    fun `can calculate diff between two scores`() {
        val score1 = Score(10, 25)
        val score2 = Score(24, 20)

        assertEquals(19, score1 - score2)
    }
}
