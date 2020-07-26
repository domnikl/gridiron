package org.gridiron.backend.model

import org.joda.time.DateTime
import java.util.UUID

data class Game(val uuid: UUID, val team1: Team, val team2: Team, val start: DateTime)
