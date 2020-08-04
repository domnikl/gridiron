package org.gridiron.backend.model

import java.util.UUID

class GameNotFoundException(uuid: UUID) :
    RuntimeException("Game could not be found '$uuid'")
