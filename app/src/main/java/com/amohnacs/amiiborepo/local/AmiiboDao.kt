package com.amohnacs.amiiborepo.local

import androidx.room.*
import com.amohnacs.amiiborepo.model.Amiibo
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface AmiiboDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAmiibo(amiibos: List<Amiibo>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAmiibo(amiibo: Amiibo): Single<Long>

    @Delete
    fun deleteAmiibo(amiibo: Amiibo): Completable

    @Update
    fun updateAmiibo(amiibo: Amiibo): Completable

    @Query("SELECT * FROM Amiibo WHERE userCreated IS NULL")
    fun getAmiibos(): Single<List<Amiibo>>

    @Query("SELECT * FROM Amiibo WHERE userCreated = :userCreated")
    fun getUserAmiibos(userCreated: Boolean = true): Single<List<Amiibo>>

    @Query("SELECT * FROM Amiibo WHERE isPurchased = :isPurchased")
    fun getAmiibosByPurchasedState(isPurchased: Boolean): Single<List<Amiibo>>

    @Query("SELECT * FROM Amiibo WHERE tail = :amiiboTail")
    fun getAmiibo(amiiboTail: String): Single<Amiibo>
}