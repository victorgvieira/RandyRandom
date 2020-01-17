package com.example.android.sampleroomerror.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.sampleroomerror.data.database.dao.ContractDAO
import com.example.android.sampleroomerror.data.database.entity.ContractEntity
import com.example.android.sampleroomerror.utils.CustomConverters

@TypeConverters(CustomConverters::class)
@Database(
    entities = [
        ContractEntity::class
    ], version = 3, exportSchema = false
)
abstract class MyDatabase : RoomDatabase() {
    abstract fun contractDAO(): ContractDAO
}
