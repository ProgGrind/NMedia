package ru.netology.nmedia

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.post_list_item.view.*
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.util.hideKeyboard
import ru.netology.nmedia.util.showKeyboard
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel: PostViewModel by viewModels()

        val adapter = PostsAdapter(viewModel)
        binding.postsRecycleViewer.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
        binding.saveButton.setOnClickListener {
            with(binding.contentEditText) {
                val content = text.toString()
                viewModel.onSaveButtonClicked(content)

                clearFocus()
                hideKeyboard()
            }
        }

        binding.cancelButton.setOnClickListener {
            viewModel.onCancelEditClicked()
        }

        viewModel.currentPost.observe(this) { currentPost ->
            with(binding.contentEditText) {
                val content = currentPost?.content
                setText(content)
                if (content !== null) {
                    requestFocus()
                    showKeyboard()
                    group.visibility = View.VISIBLE
                } else {
                    clearFocus()
                    hideKeyboard()
                    group.visibility = View.GONE
                }
            }
        }
    }
}