package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.post.Post

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
            }
            binding.menu.setOnClickListener { popupMenu.show() }

            binding.play.setOnClickListener {
                listener.onPlayVideoClicked(post)
            }
            binding.video.setOnClickListener {
                listener.onPlayVideoClicked(post)
            }
            itemView.setOnClickListener {
                listener.onPostClicked(post)
            }

        }

        fun bind(post: Post) {
            this.post = post

            with(binding) {
                author.text = post.author
                content.text = post.content
                published.text = post.published
                like.text = countTranslator(post.likes)
                like.isChecked = post.likedByMe
                share.text = countTranslator(post.shares)
                videoGroup.visibility =
                    if (post.videoURL.isBlank()) View.GONE else View.VISIBLE
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

//private fun getVisibility(T: TextView, t: Int) =
//    if(t == 0) T.setTextColor(null)
//    else T.setTextColor(Color.GRAY)