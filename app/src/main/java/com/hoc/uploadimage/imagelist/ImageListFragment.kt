package com.hoc.uploadimage.imagelist

import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hoc.uploadimage.R
import com.hoc.uploadimage.utils.observe
import kotlinx.android.synthetic.main.fragment_image_list.*
import org.koin.android.architecture.ext.viewModel

class ItemDecorate(val spacing: Int, val spanCount: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        outRect.left = spacing * column / spanCount
        outRect.right = spacing * (spanCount - 1 - column) / spanCount
        if (position >= spanCount) outRect.top = spacing
    }
}

class ImageListFragment : Fragment() {
    private val viewModel by viewModel<ListImageViewModel>()
    private val listImageAdapter = ListImageAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_image_list, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerListImages.run {
            adapter = listImageAdapter
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            addItemDecoration(ItemDecorate(4, 2))
        }

        swipeLayout.setOnRefreshListener(viewModel::onRefresh)

        viewModel.message.observe(this, ::showErrorMessage)
        viewModel.pagedList.observe(this, {
            listImageAdapter.submitList(it)
        })
        viewModel.isLoading.observe(this) {
            if (!it) swipeLayout.isRefreshing = false
        }
    }

    private fun showErrorMessage(message: String) {
        view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show() }
    }
}