package ru.netology.nmedia

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.annotation.DrawableRes
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.post_list_item.*
import kotlinx.android.synthetic.main.post_list_item.view.*
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.post.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 0L,
            author = "Maxim",
            content = "Events",
            published = "13.06.2022"
        )

        val postService = PostService()

        binding.render(post)
        binding.postListItem.like.setOnClickListener {
            post.likedByMe = !post.likedByMe
            postService.liking(post)
            postListItem.likes.text = postService.countTranslator(post.likes)
            binding.postListItem.like.setImageResource(getLikeIconResId(post.likedByMe))
            if (post.likes == 0) postListItem.likes.visibility = View.INVISIBLE
                else postListItem.likes.visibility = View.VISIBLE
        }

        binding.postListItem.share.setOnClickListener {
            binding.postListItem.share.setImageResource(R.drawable.ic_shared_24dp)
            postService.sharing(post)
            postListItem.shares.text = postService.countTranslator(post.shares)
            postListItem.share.postDelayed({binding.postListItem.share.setImageResource(R.drawable.ic_share_24dp) }, 300
            )
        }
    }

    private fun ActivityMainBinding.render(post: Post) {
        author.text = post.author
        content.text = post.content
        published.text = post.published
        like.setImageResource(getLikeIconResId(post.likedByMe))
    }

    @DrawableRes
    private fun getLikeIconResId(liked: Boolean) =
        if (liked)
            R.drawable.ic_liked_24dp else R.drawable.ic_like_24dp
}
