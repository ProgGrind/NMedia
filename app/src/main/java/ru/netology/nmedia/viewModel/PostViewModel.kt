package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.FilePostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.data.impl.SQLiteRepository
import ru.netology.nmedia.data.impl.SharedPrefsPostRepository
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel(
    application: Application
) : AndroidViewModel(application),
    PostInteractionListener {

    private val repository: PostRepository =
        SQLiteRepository(
            dao = AppDb.getInstance(
                context = application
            ).postDao
        )

    val data get() = repository.data

    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostScreenEvent = SingleLiveEvent<Long>()
    val editPostContentScreenEvent = SingleLiveEvent<String>()
    val playVideoURL = SingleLiveEvent<String>()

    val currentPost = MutableLiveData<Post?>(null)

    fun onSaveButtonClicked(content: String) {
        if (content.isBlank()) return

        val post = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Me",
            content = content,
            published = "Today"
        )
        repository.save(post)
        currentPost.value = null
    }

    // region PostInteractionListener

    override fun onLikeClicked(post: Post) =
        repository.like(post.id)

    override fun onShareClicked(post: Post) {
        sharePostContent.value = post.content
    }

    override fun onDeleteClicked(post: Post) =
        repository.delete(post.id)

    override fun onEditClicked(post: Post) {
        currentPost.value = post
        editPostContentScreenEvent.value = post.content
    }

    override fun onPostClicked(post: Post) {
        currentPost.value = post
        navigateToPostScreenEvent.value = post.id
    }

    // endregion PostInteractionListener

    override fun onPlayVideoClicked(post: Post) {
        playVideoURL.value = post.videoURL
    }
}