package com.kostku.app.data.repository

import com.kostku.app.data.local.RoomDao
import com.kostku.app.data.local.RoomStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

data class RoomSummary(
    val occupied: Int = 0,
    val empty: Int = 0,
    val maintenance: Int = 0
)

class RoomRepository(private val roomDao: RoomDao) {
    fun observeSummary(): Flow<RoomSummary> = combine(
        roomDao.observeCount(RoomStatus.OCCUPIED),
        roomDao.observeCount(RoomStatus.EMPTY),
        roomDao.observeCount(RoomStatus.MAINTENANCE)
    ) { occupied, empty, maintenance -> RoomSummary(occupied, empty, maintenance) }
}
