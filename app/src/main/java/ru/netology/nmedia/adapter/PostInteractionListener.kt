package ru.netology.nmedia.adapter

import ru.netology.nmedia.post.Post

interface PostInteractionListener {
    fun onLikeClicked(post: Post)

    fun onShareClicked(post: Post)

    fun onDeleteClicked(post: Post)

    fun onEditClicked(post: Post)

    fun onPlayVideoClicked(post: Post)

    fun onPostClicked(post: Post)
}