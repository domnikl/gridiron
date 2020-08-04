package org.gridiron.backend.model

class UserAlreadyExistsException(user: User) :
    RuntimeException("User '${user.username}' already exists")
