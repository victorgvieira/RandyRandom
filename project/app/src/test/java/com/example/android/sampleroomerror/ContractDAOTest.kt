package com.example.android.sampleroomerror

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.android.sampleroomerror.data.database.entity.ContractEntity
import com.example.android.sampleroomerror.data.database.MyDatabase
import com.example.android.sampleroomerror.data.database.dao.ContractDAO
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.junit.runners.model.Statement
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ContractDAOTest {

    class RxTrampolineSchedulerRule : TestRule {
        override fun apply(base: Statement, description: Description): Statement {
            return object : Statement() {
                @Throws(Throwable::class)
                override fun evaluate() {
                    RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
                    RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
                    RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
                    RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
                    try {
                        base.evaluate()
                    } finally {
                        RxJavaPlugins.reset()
                        RxAndroidPlugins.reset()
                    }
                }
            }
        }
    }

    private lateinit var database: MyDatabase
    private lateinit var contractDAO: ContractDAO

    @Rule
    @JvmField
    var testSchedulerRule =
        RxTrampolineSchedulerRule()


    @Before
    fun init() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MyDatabase::class.java
        ).fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
        contractDAO = database.contractDAO()
    }

    @After
    fun closeDatabase() {
        database.clearAllTables()
        database.close()
    }


    @Test
    fun `assert save contract image completes`() {
        val contractIdForCheck = 33333L
        val contract = ContractEntity(
            id = contractIdForCheck,
            imageListPath = listOf(
                "imagem_qualquer_coisa",
                "aaaaaaaaaa , bbbbbbbbbbbbbbbbbbbb",
                "111111111111, 222222222222, 3333333333333333"
            )
        )
        assert(contractDAO.save(contract) > 0)

        val image1 = "imagem_qualquer_coisa"
        val image2 = "aaaaaaaaaa , bbbbbbbbbbbbbbbbbbbb"
        val image3 = "111111111111, 222222222222, 3333333333333333"

        contractDAO.saveImagePath(contractIdForCheck, image1)
            .andThen(contractDAO.getItem(contractIdForCheck))
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue {
                val imageList = it.imageListPath
                imageList.size == 1
            }
            .assertValue {
                val imageList = it.imageListPath
                println(imageList[0])
                imageList[0] == image1
            }

//        contractDAO.saveImagePath(contractIdForCheck, image2)
//            .andThen(contractDAO.getItem(contractIdForCheck))
//            .test()
//            .assertComplete()
//            .assertNoErrors()
//            .assertValue {
//                val imageList = it.imageListPath
//                imageList.size == 2
//            }
//            .assertValue {
//                val imageList = it.imageListPath
//                println(imageList[1])
//                imageList[1] == image2
//            }
//
//        contractDAO.saveImagePath(contractIdForCheck, image3)
//            .andThen(contractDAO.getItem(contractIdForCheck))
//            .test()
//            .assertComplete()
//            .assertNoErrors()
//            .assertValue {
//                val imageList = it.imageListPath
//                imageList.size == 3
//            }
//            .assertValue {
//                val imageList = it.imageListPath
//                println(imageList[2])
//                imageList[2] == image3
//            }
    }
}