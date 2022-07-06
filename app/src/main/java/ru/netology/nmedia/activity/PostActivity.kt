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

        binding.edit.setText(intent.getStringExtra(Intent.EXTRA_TEXT))
        binding.edit.requestFocus()
        setResult(Activity.RESULT_OK, intent)

        binding.ok.setOnClickListener {
            val intent = Intent()
            val text = binding.edit.text
            if (text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = text.toString()
                intent.putExtra(POST_CONTENT_EXTRA_KEY, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }

        binding.cancelButton.setOnClickListener{
            finish()
        }
    }

    private companion object {
        const val POST_CONTENT_EXTRA_KEY = "postContent"
    }

    object ResultContract : ActivityResultContract<String?, String?>() {
        override fun createIntent(context: Context, input: String?) =
            Intent(context, PostActivity::class.java)
                .putExtra(Intent.EXTRA_TEXT, input)
        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            if (resultCode !== Activity.RESULT_OK) return null
            intent ?: return null

            return intent.getStringExtra(POST_CONTENT_EXTRA_KEY)
        }
    }
}