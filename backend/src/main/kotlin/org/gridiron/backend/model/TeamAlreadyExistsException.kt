package org.gridiron.backend.model

class TeamAlreadyExistsException(team: Team) :
    RuntimeException("Team '${team.name}' already exists")
