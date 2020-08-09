package org.gridiron.backend

import org.gridiron.backend.model.Game
import org.gridiron.backend.model.GameRepository
import org.gridiron.backend.model.TeamRepository
import java.util.logging.Logger

class IcsImporter(
    private val icsClient: IcsClient,
    private val teamRepository: TeamRepository,
    private val gameRepository: GameRepository,
    private val logger: Logger
) {
    suspend fun import() {
        icsClient.games().forEach { game ->
            val away = teamRepository.findByName(game.away)
            val home = teamRepository.findByName(game.home)

            if (gameRepository.exists(game.start, away, home)) {
                return@forEach
            }

            val g = Game.create(
                gameRepository.generateId(),
                away,
                home,
                game.start
            )

            gameRepository.save(g)

            logger.info("Imported game from ICS: $g")
        }
    }
}
