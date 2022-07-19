package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_post.view.*
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.PostsAdapter.ViewHolder
import ru.netology.nmedia.adapter.countTranslator
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.util.LongArg
import ru.netology.nmedia.viewModel.PostViewModel
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.adapter.PostInteractionListener as PostInteractionListener

class PostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentPostBinding.inflate(inflater, container, false)

        val viewModel: PostViewModel by viewModels()

        val viewHolder = ViewHolder(binding.postLayout, viewModel)

        val postId = arguments?.idArg

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { postId == it.id } ?: return@observe
            viewHolder.bind(post)

            with(binding) {

                postLayout.like.setOnClickListener {
                    viewModel.onLikeClicked(post)
                }
                postLayout.play.setOnClickListener {
                    viewModel.onPlayVideoClicked(post)
                }
                postLayout.video.setOnClickListener {
                    viewModel.onPlayVideoClicked(post)
                }

                postLayout.menu.setOnClickListener {
                    PopupMenu(binding.root.context, postLayout.menu).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { menuItem ->
                            when (menuItem.itemId) {
                                R.id.remove -> {
                                    viewModel.onDeleteClicked(post)
                                    findNavController().navigateUp()
                                    true
                                }
                                R.id.edit -> {
                                    findNavController().navigate(
                                        R.id.action_postFragment_to_newPostFragment,
                                        Bundle().apply { textArg = post.content }
                                    )
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }
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
        }
        return binding.root
    }

    companion object{
        var Bundle.idArg: Long? by LongArg
    }
}

