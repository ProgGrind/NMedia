package ru.netology.nmedia.post

class PostService {

//    private val likes = mutableListOf<Post>()

//    fun liking (post: Post): Int {
//        if (post.likedByMe) {
//            post.likes++
//            likes.add(post)
//        } else if (!post.likedByMe) {
//            post.likes--
//            likes.remove(post)
//        }
//        return post.likes
//    }
//
//    fun sharing (post: Post): Int {
//        post.shares++
//        return post.shares
//    }

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
}