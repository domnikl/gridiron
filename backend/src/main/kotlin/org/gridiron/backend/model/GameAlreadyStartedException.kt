package org.gridiron.backend.model

class GameAlreadyStartedException(game: Game) :
    RuntimeException("Game '${game.uuid}' already started")
