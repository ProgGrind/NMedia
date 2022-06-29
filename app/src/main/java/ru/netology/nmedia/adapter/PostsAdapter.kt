package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.post.Post

typealias OnButtonListener = (Post) -> Unit

class PostsAdapter(
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostListItemBinding.inflate(
            inflater, parent, false
        )
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    class ViewHolder(
        private val binding: PostListItemBinding,
        listener: PostInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.menu).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onDeleteClicked(post)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.like.setOnClickListener {
                listener.onLikeClicked(post)
            }
            binding.share.setOnClickListener {
                listener.onShareClicked(post)
                binding.share.setImageResource(R.drawable.ic_shared_24dp)
                binding.share.postDelayed({binding.share.setImageResource(
                    R.drawable.ic_share_24dp) },300
                )
            }

        }

        fun bind(post: Post) {
            this.post = post

            with(binding) {
                author.text = post.author
                content.text = post.content
                published.text = post.published
                likes.text = countTranslator(post.likes)
                shares.text = countTranslator(post.shares)
                like.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked_24dp else R.drawable.ic_like_24dp
                )
                getVisibility(likes as AppCompatTextView, post.likes)
                getVisibility(shares as AppCompatTextView, post.shares)
                menu.setOnClickListener { popupMenu.show() }
            }
        }
    }
    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem

    }
}

fun countTranslator(T: Int): String {
    val counting = when (T) {
        in 1000..1099 -> buildString {
            append((T).toString()[0])
            append(" K")
        }
        in 1100..999999 -> buildString {
            append((T / 1000).toString())
            append(".")
            append((T / 100).toString().last())
            append(" K")
        }
        in 1_000_000..1_099_999 -> buildString {
            append((T).toString()[0])
            append(" M")
        }
        in 1_100_000..999_999_999 -> buildString {
            append((T / 1_000_000).toString())
            append(".")
            append((T / 100_000).toString().last())
            append(" M")
        }
        else -> (T).toString()
    }
    return counting
}

private fun getVisibility(T: AppCompatTextView, t: Int) =
    if(t == 0) T.visibility = View.INVISIBLE
    else T.visibility = View.VISIBLE