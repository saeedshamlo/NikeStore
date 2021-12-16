package com.sevenlearn.nike.feature.product.comment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.google.android.material.snackbar.Snackbar
import com.sevenlearn.nike.R
import com.sevenlearn.nike.common.*
import com.sevenlearn.nike.data.Comment
import com.sevenlearn.nike.data.Product
import com.sevenlearn.nike.feature.product.CommentAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_comment_list.*
import kotlinx.android.synthetic.main.activity_comment_list.commentsRv
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.item_add_comment.*
import kotlinx.android.synthetic.main.item_add_comment.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CommentListActivity : NikeActivity() {
    val viewModel: CommentListViewModel by viewModel {
        parametersOf(
            intent.extras!!.getInt(
                EXTRA_KEY_ID
            )
        )
    }
    lateinit var adapter: CommentAdapter
    val compositeDisposable = CompositeDisposable()

    lateinit var customView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_list)

        viewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }

        viewModel.commentsLiveData.observe(this) {
            adapter = CommentAdapter(true)
            commentsRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            adapter.comments = it as ArrayList<Comment>
            commentsRv.adapter = adapter
        }

        commentListToolbar.onBackButtonClickListener = View.OnClickListener {
            finish()
        }
        val product = intent.getParcelableExtra<Product>(EXTRA_KEY_DATA)


        insertCommentBtn.setOnClickListener {
            customView = LayoutInflater.from(this).inflate(R.layout.item_add_comment, null, false)
            val dialog = MaterialAlertDialogBuilder(this)

            dialog.setView(customView)
                .setPositiveButton("ثبت"){ dialogs, _ ->
                val title = customView.titleCommentEt.text.toString()
                val content = customView.contentCommentEt.text.toString()
                if (title.isNotEmpty() && content.isNotEmpty()) {
                    if (product != null) {
                        viewModel.addComment(product.id, title, content)
                            .asyncNetworkRequest()
                            .subscribe(object :
                                NikeSingleObserver<Comment>(compositeDisposable) {
                                override fun onSuccess(t: Comment) {
                                    adapter.comments.add(0, t)
                                    dialogs.dismiss()
                                    commentsRv.smoothScrollToPosition(0)
                                    adapter.notifyItemInserted(0)

                                }
                            })
                    }
                }else{
                    dialogs.dismiss()
                }
            }

            dialog.show()
            if (product != null) {
                customView.commentIV.setImageURI(product.image)
                customView.commentTitleTv.text = product.title
            }


        }
    }
}


