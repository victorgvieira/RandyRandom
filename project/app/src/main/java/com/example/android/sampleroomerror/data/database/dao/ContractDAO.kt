package com.example.android.sampleroomerror.data.database.dao

import androidx.room.*
import com.example.android.sampleroomerror.data.database.entity.ContractEntity
import com.example.android.sampleroomerror.utils.CustomConverters
import io.reactivex.Completable
import io.reactivex.Single

@TypeConverters(CustomConverters::class)
@Dao
abstract class ContractDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun saveAll(vararg list: ContractEntity): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun save(item: ContractEntity): Long

    @Update
    abstract fun update(item: ContractEntity)

    @Delete
    abstract fun delete(item: ContractEntity)

    @Query("SELECT * FROM contract")
    abstract fun getAll(): Single<List<ContractEntity>>

    @Query("SELECT * FROM contract WHERE id = :id")
    abstract fun getItem(id: Long): Single<ContractEntity>

    @Query("UPDATE contract SET image_path = :pathList WHERE id = :id")
    protected abstract fun _saveImagePath(id: Long, @TypeConverters(CustomConverters::class) pathList: List<String>): Int

    open fun saveImagePath(id: Long, path: String): Completable = Completable.create {
        val contract = getItem(id).blockingGet()
        val newList =
            contract.imageListPath.plusElement(path).plusElement("aaaaaaaaaaaaaaa")
        val result = _saveImagePath(
            id,
            newList
        )
        if (result > 0)
            it.onComplete()
        else {
            it.onError(Exception("Nenhum registro modificado"))
        }
    }

}