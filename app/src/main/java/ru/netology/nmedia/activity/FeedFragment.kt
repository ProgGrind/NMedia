package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.activity.PostFragment.Companion.idArg
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.viewModel.PostViewModel

class FeedFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel: PostViewModel by viewModels()
        val binding = FragmentFeedBinding.inflate(inflater, container, false)
        val adapter = PostsAdapter(viewModel)

        binding.postsRecycleViewer.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        binding.addButton.setOnClickListener{
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        viewModel.editPostContentScreenEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(
                R.id.action_feedFragment_to_newPostFragment,
                Bundle().apply { textArg = it }
            )
        }

        viewModel.navigateToPostScreenEvent.observe(viewLifecycleOwner) {
            viewModel.currentPost.value?.id
            findNavController().navigate(
                R.id.action_feedFragment_to_postFragment,
                Bundle().apply { idArg = it }
            )
        }

        viewModel.playVideoURL.observe(viewLifecycleOwner) { videoURL ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoURL))
            startActivity(intent)
        }

        viewModel.sharePostContent.observe(viewLifecycleOwner) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(
                intent, getString(R.string.chooser_share_post)
            )
            startActivity(shareIntent)
        }

        return binding.root
    }
}
