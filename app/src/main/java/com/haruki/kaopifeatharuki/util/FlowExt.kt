package com.haruki.kaopifeatharuki.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


fun<T> Flow<T>.observe(lifecycleOwner: LifecycleOwner,callback:(T) -> Unit) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@observe.collect {
                callback.invoke(it)
            }
        }
    }
}

/**
 * Adapter中使用 自动管理ViewHolder生命周期，ViewHolder需要实现LifeCycleOwner接口并手动设置生命周期
 * @param lifecycleScope 协程作用域 与Adapter的父类Fragment/Activity生命周期同步
 * @param lifeCycle ViewHolder生命周期
 */
fun<T> Flow<T>.observe(lifecycleScope: LifecycleCoroutineScope, lifeCycle:Lifecycle, callback:(T) -> Unit) {
    lifecycleScope.launch {
        lifeCycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@observe.collect {
                callback.invoke(it)
            }
        }
    }
}