package com.kkk.moviereview.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kkk.moviereview.data.db.dao.MovieDao
import com.kkk.moviereview.data.db.typeconverter.IntListTypeConverter
import com.kkk.moviereview.network.networkresponse.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)
@TypeConverters(IntListTypeConverter::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        private var instance: MyDatabase? = null

        fun getInstance(context: Context): MyDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, MyDatabase::class.java, "MyDBName")
                    .build()
            }
            return instance as MyDatabase
        }
    }
}