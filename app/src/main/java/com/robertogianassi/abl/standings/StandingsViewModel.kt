package com.robertogianassi.abl.standings

import android.app.Application
import androidx.lifecycle.*
import com.robertogianassi.abl.data.BaseballDatabase
import com.robertogianassi.abl.data.BaseballRepository
import com.robertogianassi.abl.util.getErrorMessage
import kotlinx.coroutines.launch

class StandingsViewModel(application: Application) :
    AndroidViewModel(application) {

    private val repo: BaseballRepository

    val standings: LiveData<List<UITeamStanding>>
    val errorMessage = MutableLiveData("")

    init {
        repo = BaseballDatabase
            .getDatabase(application, viewModelScope)
            .baseballDao()
            .let { dao ->
                BaseballRepository.getInstance(dao)
            }

        standings =
            Transformations.map(repo.getStandings()) { teamStandings ->
                teamStandings.mapNotNull { teamStanding ->
                    UITeamStanding.fromTeamIdAndStandings(
                        teamStanding.teamId,
                        teamStandings
                    )
                }
            }
    }

    fun refreshStandings() {
        viewModelScope.launch {
            repo.updateStandings().getErrorMessage(getApplication())
                ?.let { message -> errorMessage.value = message }
        }
    }
}