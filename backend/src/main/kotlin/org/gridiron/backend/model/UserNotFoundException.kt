package org.gridiron.backend.model

import java.util.UUID

class UserNotFoundException(uuid: UUID?) :
    RuntimeException("User could not be found '$uuid'")
