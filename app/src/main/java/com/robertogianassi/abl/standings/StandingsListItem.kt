package com.robertogianassi.abl.standings

import com.robertogianassi.abl.teams.Division

sealed class StandingsListItem {
    abstract val id: String

    data class TeamItem(val uiTeamStanding: UITeamStanding) : StandingsListItem() {
        override val id = uiTeamStanding.teamId
    }

    data class Header(val division: Division) : StandingsListItem() {
        override val id = division.name
    }
}
