package org.gridiron.backend.model

class GameAlreadyEndedException(game: Game) :
    RuntimeException("Game '${game.uuid}' already ended")
