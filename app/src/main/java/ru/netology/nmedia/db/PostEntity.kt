package ru.netology.nmedia.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
class PostEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likes: Int = 0,

    @ColumnInfo(name = "likedByMe")
    val likedByMe: Boolean = false,
    @ColumnInfo(name = "shares")
    val shares: Int = 0,

    @ColumnInfo(name = "shared")
    var shared: Boolean,
    val videoURL: String = ""
)