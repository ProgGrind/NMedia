package ru.netology.nmedia.data.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.post.Post

class SQLiteRepository(
    private val dao: PostDao
) : PostRepository {

    private val posts
        get() = checkNotNull(data.value) {
            "Data value should be not null"
        }

    override val data =  MutableLiveData(dao.getAll())

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        data.value = if (id == 0L) {
            listOf(saved) + posts
        } else posts.map {
            if (it.id !== id) it else saved
        }
    }

    override fun like(id: Long) {
        dao.likeById(id)
        data.value = posts.map {
            if (it.id !== id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
            )
        }
        data.value = posts
    }

    override fun share(postId: Long) {
        data.value = posts.map { post ->
            if (post.id == postId) post.copy(shares = post.shares + 1)
            else post
        }
    }

    override fun delete(id: Long) {
        dao.removeById(id)
        data.value = posts.filter { it.id !== id }
    }
}