package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.post.Post

class InMemoryPostRepository : PostRepository {

    private var nextId = GENERATED_POSTS_AMOUNT.toLong()

    private var posts
        get() = checkNotNull(data.value)
        set(value) {
            data.value = value
        }

    override val data: MutableLiveData<List<Post>>

    init {
        val initialPosts = List(GENERATED_POSTS_AMOUNT) { index ->
            Post(
                id = index + 1L,
                author = "Netology",
                content = "Some random content ${index + 1}",
                published = "19.06.2022",
                likedByMe = false
            )
        }
        data = MutableLiveData(initialPosts)
    }

    override fun like(postId: Long) {
        posts = posts.map { post ->
            if (post.id == postId) {
                when (post.likedByMe) {
                    true -> post.copy(likes = post.likes - 1, likedByMe = !post.likedByMe)
                    false -> post.copy(likes = post.likes + 1, likedByMe = !post.likedByMe)
                }
            } else post
        }
    }

    override fun share(postId: Long) {
        posts = posts.map { post ->
            if (post.id == postId) post.copy(shares = post.shares + 1)
            else post
        }
    }

    override fun delete(postId: Long) {
        posts = posts.filterNot { post ->  post.id == postId }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun insert(post: Post) {
        posts = listOf(
            post.copy(
                id = ++nextId
            )
        ) + posts
    }

    private fun update(post: Post) {
         posts = posts.map {
            if (it.id == post.id) post else it
         }
    }

    private companion object {
        const val GENERATED_POSTS_AMOUNT = 1000
    }
}


