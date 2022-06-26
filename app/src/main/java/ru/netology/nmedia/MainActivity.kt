package ru.netology.nmedia

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val postService = PostService()
        val viewModel: PostViewModel by viewModels()

        val adapter = PostsAdapter(
            onLikeClicked = {post ->
                viewModel.onLikeClicked(post)
            },
            onShareClicked = { post ->
                viewModel.onShareClicked(post)
            }
        )
        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(this) {  posts ->
            adapter.submitList(posts)
        }
    }




}

//like.setImageResource(
//if (post.likedByMe) R.drawable.ic_liked_24dp else R.drawable.ic_like_24dp
//)
////                    getVisibility(likes as AppCompatTextView, post.likes)
//
//
//
////                    getVisibility(shares as AppCompatTextView, post.shares)
//
//like.setOnClickListener {
//    viewModel.onLikeClicked(post)
//    likes.text = postService.countTranslator(post.likes)
//}
//
//share.setOnClickListener {
//    viewModel.onShareClicked(post)
//    shares.text = postService.countTranslator(post.shares)
//    share.setImageResource(R.drawable.ic_shared_24dp)
//    share.postDelayed({share.setImageResource(R.drawable.ic_share_24dp) },
//        300)
//}