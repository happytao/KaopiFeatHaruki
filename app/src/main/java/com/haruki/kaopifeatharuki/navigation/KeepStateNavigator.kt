package com.haruki.kaopifeatharuki.navigation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.replace
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.savedstate.SavedState
import java.util.Stack

@Navigator.Name("keep_state_fragment")
class KeepStateNavigator(
    val mContext: Context,
    val mFragmentManager: FragmentManager,
    val mContainerId: Int
) : Navigator<FragmentNavigator.Destination>() {

    companion object {
        private const val TAG = "KeepStateNavigator"
        private val KEY_BACK_STACK_IDS = "androidx-nav-fragment:navigator:backStackIds"
    }

    private val mBackStack = ArrayDeque<FragmentNavigator.Destination>()

    override fun createDestination(): FragmentNavigator.Destination {
        return FragmentNavigator.Destination(this)
    }

    override fun navigate(
        destination: FragmentNavigator.Destination,
        args: SavedState?,
        navOptions: NavOptions?,
        navigatorExtras: Extras?
    ): NavDestination? {
        Log.i(TAG,"destination id : ${destination.id}")
        if (mFragmentManager.isStateSaved) {
            Log.i(
                TAG,
                "Ignoring navigate() call: FragmentManager has already"
                        + " saved its state"
            )
            return null
        }
        var className = destination.className
        if (className[0] == '.') {
            className = mContext.packageName + className
        }
        val ft = mFragmentManager.beginTransaction()

        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

        val frg = mFragmentManager.primaryNavigationFragment
        if (frg != null) {
            ft.hide(frg)
        }

        val tag = destination.id.toString()
        var fragment = mFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            fragment = mFragmentManager.getFragmentFactory().instantiate(mContext.classLoader, className)
            fragment.arguments = args
            ft.add(mContainerId, fragment, tag)
        } else {
            ft.show(fragment)
        }
        ft.setPrimaryNavigationFragment(fragment)

        @IdRes val destId = destination.id
        val initialNavigation = mBackStack.isEmpty()

        var np = navOptions
        np = NavOptions.Builder().setLaunchSingleTop(true).build()
        val isSingleTopReplacement = (!initialNavigation
                && np.shouldLaunchSingleTop())

        val isAdded: Boolean
        isAdded = if (initialNavigation) {
            true
        } else if (isSingleTopReplacement) {
            !mBackStack.popUntil { it == destination }
        } else {
            true
        }
        if (navigatorExtras is FragmentNavigator.Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key, value)
            }
        }
        ft.setReorderingAllowed(true)
        ft.commit()
        mBackStack.add(destination)
        (return if (isAdded) {
            Log.i(TAG,"is Added mBackStack: ${mBackStack.map { it.label }}")
            destination
        } else {
            Log.i(TAG,"null mBackStack: ${mBackStack.map { it.label }}")
            null
        })

    }

    override fun popBackStack(): Boolean {
        Log.i(TAG,"popBackStack")
        mBackStack.removeLastOrNull()
        if (mBackStack.isEmpty()) {
            Log.i(TAG,"mBackStack empty")
            return false
        }
        val currentFragment = mFragmentManager.primaryNavigationFragment ?: return false
        val lastFragment = mFragmentManager.findFragmentByTag(mBackStack.last().id.toString()) ?: return false
        val ft = mFragmentManager.beginTransaction()
        ft.hide(currentFragment)
        ft.show(lastFragment)
        ft.setPrimaryNavigationFragment(lastFragment)
        ft.setReorderingAllowed(true)
        ft.commit()
        Log.i(TAG,"mBackStack: ${mBackStack.map { it.label }}")
        return true
    }



    override fun popBackStack(popUpTo: NavBackStackEntry, savedState: Boolean) {
        Log.i(TAG,"popBackStack popUpTo ${popUpTo.destination.label}")

        super.popBackStack(popUpTo, savedState)
    }




    private fun generateBackStackName(backStackIndex: Int, destId: Int): String {
        return "$backStackIndex-$destId"
    }

    fun <T> ArrayDeque<T>.popUntil(predicate: (T) -> Boolean): Boolean {
        // 先检查是否存在目标元素
        if (!this.any(predicate)) return false

        // 存在目标元素时，弹出直到目标元素（包括目标元素）
        while (this.isNotEmpty()) {
            val element = this.removeLast()
            if (predicate(element)) break
        }
        return true
    }

    fun getBackStackSize() = mBackStack.size

}