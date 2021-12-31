package com.robertogianassi.abl.teams

import androidx.lifecycle.*
import com.robertogianassi.abl.standings.TeamStanding
import com.robertogianassi.abl.standings.UITeamStanding

class SingleTeamViewModel : ViewModel() {

    val team = MutableLiveData<UITeam>()

    private val standings: LiveData<List<TeamStanding>>

    val teamStanding = MediatorLiveData<UITeamStanding>()

    init {
        standings = MutableLiveData<List<TeamStanding>>().apply {
            value = TeamStanding.mockTeamStandings
        }

        teamStanding.addSource(this.team) { uiTeam ->
            updateUIStanding(uiTeam, standings.value)
        }

        teamStanding.addSource(this.standings) { standings ->
            updateUIStanding(team.value, standings)
        }
    }

    fun setTeam(teamId: String) {
        UITeam.allTeams.firstOrNull { it.teamId == teamId }?.let { team ->
            this.team.value = team
        }
    }

    private fun updateUIStanding(uiTeam: UITeam?, teamStandings: List<TeamStanding>?) {
        if(uiTeam != null && teamStandings != null) {
            this.teamStanding.value = UITeamStanding.fromTeamAndStandings(uiTeam, teamStandings)
        }
    }
}