package com.robertogianassi.abl.data

import androidx.lifecycle.LiveData
import dev.mfazio.abl.api.services.getDefaultABLService
import com.robertogianassi.abl.scoreboard.ScheduledGame
import com.robertogianassi.abl.standings.TeamStanding
import com.robertogianassi.abl.util.convertToScheduledGames
import com.robertogianassi.abl.util.convertToTeamStandings
import com.robertogianassi.abl.util.toGameDateString
import dev.mfazio.abl.api.services.AndroidBaseballLeagueService
import dev.mfazio.abl.api.services.getDefaultABLService
import java.io.IOException
import java.time.LocalDate

class BaseballRepository(private val baseballDao: BaseballDao) {

    fun getStandings(): LiveData<List<TeamStanding>> =
        baseballDao.getStandings()

    suspend fun updateStandings(): ResultStatus {
        val standingsResult = safeApiRequest {
            apiService.getStandings()
        }

        return if (
            standingsResult.success &&
            standingsResult.result?.any() == true
        ) {
            baseballDao.updateStandings(
                standingsResult.result.convertToTeamStandings(
                    baseballDao.getCurrentStandings()
                )
            )
            ResultStatus.Success
        } else {
            standingsResult.status
        }
    }

    fun getGamesForDate(date: LocalDate): LiveData<List<ScheduledGame>> =
        baseballDao.getGamesForDate("${date.toGameDateString()}%")

    suspend fun updateGamesForDate(date: LocalDate): ResultStatus {
        val gamesResult = safeApiRequest {
            apiService.getGames(requestedDate = date)
        }

        return if (gamesResult.success && gamesResult.result?.any() == true) {
            baseballDao.insertOrUpdateGames(
                gamesResult.result.convertToScheduledGames()
            )
            ResultStatus.Success
        } else {
            gamesResult.status
        }
    }

    enum class ResultStatus {
        Unknown,
        Success,
        NetworkException,
        RequestException,
        GeneralException
    }

    inner class ApiResult<T>(
        val result: T? = null,
        val status: ResultStatus = ResultStatus.Unknown
    ) {
        val success = status == ResultStatus.Success
    }

    private suspend fun <T> safeApiRequest(
        apiFunction: suspend () -> T
    ): ApiResult<T> =
        try {
            val result = apiFunction()
            ApiResult(result, ResultStatus.Success)
        } catch (ex: retrofit2.HttpException) {
            ApiResult(status = ResultStatus.RequestException)
        } catch (ex: IOException) {
            ApiResult(status = ResultStatus.NetworkException)
        } catch (ex: Exception) {
            ApiResult(status = ResultStatus.GeneralException)
        }

    companion object {
        private val apiService  = getDefaultABLService()

        @Volatile
        private var instance: BaseballRepository? = null

        fun getInstance(baseballDao: BaseballDao) =
            this.instance ?: synchronized(this) {
                instance ?: BaseballRepository(baseballDao).also {
                    instance = it
                }
            }
    }
}