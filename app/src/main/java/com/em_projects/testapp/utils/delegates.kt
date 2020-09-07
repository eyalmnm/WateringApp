package com.em_projects.testapp.utils

import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*

// Lazy loading with coroutine support
fun <T> lazyDeferred(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy {
        GlobalScope.async(start = CoroutineStart.LAZY) { block.invoke(this) }
    }
}

// Recycler  View on click listener
fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(adapterPosition, itemViewType)
    }
    return this
}