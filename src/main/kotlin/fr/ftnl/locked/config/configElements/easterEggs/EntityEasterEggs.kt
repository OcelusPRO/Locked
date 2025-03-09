package fr.ftnl.locked.config.configElements.easterEggs

data class EntityEasterEggs(
    val enableZombieEggs: Boolean = true,
    val zombieMenNames: List<String> = listOf(
        "Alexander",
        "Matthew",
        "Robert",
        "Eugene",
        "David",
        "Jayson",
        "Maurice",
        "Christopher",
        "Harold",
        "Thomas",
        "Stephen",
        "Reginald",
    ),
    val zombieWomenNames: List<String> = listOf(
        "Marilyn",
        "Marie",
        "Lori",
        "Connie",
        "Melissa",
        "Angela",
        "Patricia",
        "Janice",
        "Omega",
        "Henrietta",
        "Margo",
        "Marjorie",
    ),
    val specialZombieChances: Int = 5,
    val zombieWomenDeathMessages: List<String> = listOf(
        "%s... Je l'ai connu, c'etait ma voisine pendant plusieurs années.",
        "Ce n'est pas une grosse perte, %s était une vraie chieuse.",
        "Je ne sais pas si %s est vraiment morte, elle était déjà un peu zombie avant."
    ),
    val zombieMenDeathMessages: List<String> = listOf(
        "%s... Un collègue de travail, je ne l'aimais pas trop mais c'est triste.",
        "T'avait une sale gueule, mais au fond je t'aimais bien %s.",
        "Tu t'es bien battu %s, je te le concède. Mais l'apocalypse a eu raison de toi."
    )
)
