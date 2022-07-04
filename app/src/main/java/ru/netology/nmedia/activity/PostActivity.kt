package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityNewPostBinding
import ru.netology.nmedia.viewModel.PostViewModel

class PostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: PostViewModel by viewModels()

        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.currentPost.observe(this) { currentPost ->
        with(binding.edit) {
            setText(currentPost?.content)
            requestFocus()
            }
        }

        binding.ok.setOnClickListener{
            onOkButtonClicked(binding.edit.text?.toString())
        }

        binding.cancelButton.setOnClickListener{
            finish()
        }

    }

    private fun onOkButtonClicked(postContent: String?) {
        val intent = Intent()

        if (postContent.isNullOrBlank()) {
            setResult(Activity.RESULT_CANCELED, intent)
        } else {
            intent.putExtra(POST_CONTENT_EXTRA_KEY, postContent)
            setResult(Activity.RESULT_OK, intent)
        }
        finish()
    }

    private companion object {
        const val POST_CONTENT_EXTRA_KEY = "postContent"
    }

    object ResultContract : ActivityResultContract<Unit, String?>() {
        override fun createIntent(context: Context, input: Unit) =
            Intent(context, PostActivity::class.java)

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            if (resultCode !== Activity.RESULT_OK) return null
            intent ?: return null

            return intent.getStringExtra(POST_CONTENT_EXTRA_KEY)
        }
    }
}