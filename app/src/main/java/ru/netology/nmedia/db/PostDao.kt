package ru.netology.nmedia.db

import ru.netology.nmedia.post.Post

interface PostDao {
    fun getAll(): List<Post>
    fun save(post:Post): Post
    fun likeById(id: Long)
    fun removeById(id: Long)
}