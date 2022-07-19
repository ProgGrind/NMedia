package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewModel.PostViewModel

class NewPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)

        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

        arguments?.textArg?.let (binding.edit::setText)

//        binding.edit.setText(activity?.intent?.getStringExtra(Intent.EXTRA_TEXT))
        binding.edit.requestFocus()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        activity?.setResult(Activity.RESULT_OK, activity?.intent)

        binding.ok.setOnClickListener {
            val text = binding.edit.text
            if (!text.isNullOrBlank()) {
                val postContent = text.toString()
                viewModel.onSaveButtonClicked(postContent)
            }
            findNavController().navigateUp()
        }

        binding.cancelButton.setOnClickListener{
            findNavController().navigateUp()
        }
        return binding.root
    }

    companion object{
        var Bundle.textArg: String? by StringArg
    }


//    object ResultContract : ActivityResultContract<String?, String?>() {
//        override fun createIntent(context: Context, input: String?) =
//            Intent(context, NewPostFragment::class.java)
//                .putExtra(Intent.EXTRA_TEXT, input)
//        override fun parseResult(resultCode: Int, intent: Intent?): String? {
//            if (resultCode !== Activity.RESULT_OK) return null
//            intent ?: return null
//
//            return intent.getStringExtra(POST_CONTENT_EXTRA_KEY)
//        }
//    }
}