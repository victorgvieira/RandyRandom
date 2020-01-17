package com.example.android.sampleroomerror.data.database.entity

import androidx.room.*
import com.example.android.sampleroomerror.utils.CustomConverters


@Entity(
    tableName = "contract",
    primaryKeys = ["id"]
)
@TypeConverters(CustomConverters::class)
data class ContractEntity constructor(
    @ColumnInfo(name = "id") var id: Long = 0,
    @ColumnInfo(name = "active") var active: Boolean = false,
    @ColumnInfo(name = "canceled") var canceled: Boolean = false,
    @ColumnInfo(name = "category") var category: Long = 0,
    @ColumnInfo(name = "type") var type: String = "",
    @ColumnInfo(name = "image_path")
    var imageListPath: List<String> = emptyList()
)