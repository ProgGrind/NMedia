package ru.netology.nmedia

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import kotlinx.android.synthetic.main.post_list_item.*
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.post.PostService
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postService = PostService()

        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                content.text = post.content
                published.text = post.published

                //лайки переводятся в нужное число и становятся видимыми
                likes.text = postService.countTranslator(post.likes)
                like.setImageResource(getLikeIconResId(post.likedByMe))
                getVisibility(likes, post.likes)

                shares.text = postService.countTranslator(post.shares)
                getVisibility(shares, post.shares)
            }
        }

        binding.postListItem.like.setOnClickListener {
            viewModel.onLikeClicked()
        }

        binding.postListItem.share.setOnClickListener {
            viewModel.onShareClicked()
            share.setImageResource(R.drawable.ic_shared_24dp)
            share.postDelayed({binding.postListItem.share.setImageResource(R.drawable.ic_share_24dp) },
                300)
        }
    }

    @DrawableRes
    private fun getLikeIconResId(liked: Boolean) =
        if (liked)
            R.drawable.ic_liked_24dp else R.drawable.ic_like_24dp

    private fun getVisibility(T: AppCompatTextView, t: Int) =
        if(t == 0) T.visibility = View.INVISIBLE
        else T.visibility = View.VISIBLE
}

