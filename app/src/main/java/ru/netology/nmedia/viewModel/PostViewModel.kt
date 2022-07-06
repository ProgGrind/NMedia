package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel : ViewModel(), PostInteractionListener {

    private val repository: PostRepository = InMemoryPostRepository()

    val data get() = repository.data

    //    val shareEvent = SingleLiveEvent<Post>()
    val sharePostContent = SingleLiveEvent<String>()
    val editPostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreenEvent = SingleLiveEvent<Unit>()
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

    fun onCancelEditClicked() {
        currentPost.value = null
    }

    // region PostInteractionListener

    override fun onLikeClicked(post: Post) =
        repository.like(post.id)

    override fun onShareClicked(post: Post) {
//        repository.share(post.id)
        sharePostContent.value = post.content
    }

    override fun onDeleteClicked(post: Post) =
        repository.delete(post.id)

    override fun onEditClicked(post: Post) {
        currentPost.value = post
        navigateToPostContentScreenEvent.call()
    }

    // endregion PostInteractionListener

    fun onCreateNewPost() {
        navigateToPostContentScreenEvent.call()
    }

    override fun onPlayVideoClicked(post: Post) {
        playVideoURL.value = post.videoURL
    }
}