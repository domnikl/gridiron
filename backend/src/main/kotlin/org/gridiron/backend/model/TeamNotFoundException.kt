package org.gridiron.backend.model

import java.util.UUID

class TeamNotFoundException(uuid: UUID) :
    RuntimeException("Team could not be found '$uuid'")
