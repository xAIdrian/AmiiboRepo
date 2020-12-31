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

    @Query("DELETE FROM Amiibo WHERE tail in (:tail)")
    fun deleteAmiibos(tail: List<String>): Single<Int>

    @Update
    fun updateAmiibo(amiibo: Amiibo): Completable

    @Query("SELECT * FROM Amiibo")
    fun getAmiibos(): Single<List<Amiibo>>

    @Query("SELECT * FROM Amiibo WHERE userCreated = :userCreated")
    fun getUserAmiibos(userCreated: Boolean = true): Single<List<Amiibo>>

    @Query("SELECT * FROM Amiibo WHERE isPurchased = :isPurchased")
    fun getAmiibosByPurchasedState(isPurchased: Boolean): Single<List<Amiibo>>

    @Query("SELECT * FROM Amiibo WHERE tail = :amiiboTail")
    fun getAmiibo(amiiboTail: String): Single<Amiibo>
}