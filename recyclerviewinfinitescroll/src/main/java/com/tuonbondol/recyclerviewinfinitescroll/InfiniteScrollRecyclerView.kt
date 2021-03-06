package com.tuonbondol.recyclerviewinfinitescroll

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tuonbondol.networkutil.isNetworkConnected
import org.jetbrains.anko.toast

/****
 *
 * @author TUON BONDOL
 *
 */

class InfiniteScrollRecyclerView(
        val mContext: Context, val mRecyclerView: RecyclerView,
        val mLayoutManager: LinearLayoutManager,
        val mRecyclerViewAdapterCallback: RecyclerViewAdapterCallback) {

    var mPastVisibleItems: Int = 0
    var mVisibleItemCount: Int = 0
    var mTotalItemCount: Int = 0
    var mInfiniteLoadingStatus: Boolean = false

    init {
        infiniteRecyclerView()
    }

    private fun infiniteRecyclerView() {
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    mVisibleItemCount = mRecyclerView.childCount
                    mTotalItemCount = mLayoutManager.itemCount
                    mPastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()
                    if (mInfiniteLoadingStatus) {
                        if (mVisibleItemCount + mPastVisibleItems >= mTotalItemCount) {
                            mInfiniteLoadingStatus = false
                            if (isNetworkConnected(mContext)) {
                                mRecyclerViewAdapterCallback.onLoadMoreData()
                            } else {
                                mInfiniteLoadingStatus = true
                                mContext.toast(mContext.resources.getString(R.string.no_internet_connection))
                            }
                        }
                    }
                }
            }
        })
    }

    fun setLoadingStatus(status: Boolean) {
        mInfiniteLoadingStatus = status
    }

    interface RecyclerViewAdapterCallback {
        fun onLoadMoreData()
    }
}