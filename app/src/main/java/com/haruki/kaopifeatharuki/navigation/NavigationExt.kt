package com.haruki.kaopifeatharuki.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestinationBuilder
import androidx.navigation.NavDestinationDsl
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.get
import com.haruki.kaopifeatharuki.R
import com.haruki.kaopifeatharuki.fragment.AboutFragment
import com.haruki.kaopifeatharuki.fragment.CardDetailFragment
import com.haruki.kaopifeatharuki.fragment.CardListFragment
import com.haruki.kaopifeatharuki.fragment.EventFragment
import com.haruki.kaopifeatharuki.fragment.MusicFragment
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.KType

@Serializable data object CardList
@Serializable data object CardDetail
@Serializable data object Music
@Serializable data object Event
@Serializable data object About

inline fun <reified F : Fragment, reified T : Any> NavGraphBuilder.keepStateFragment(
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    builder: KeepStateFragmentNavigatorDestinationBuilder.() -> Unit
): Unit =
    destination(
        KeepStateFragmentNavigatorDestinationBuilder(
            provider[KeepStateNavigator::class],
            T::class,
            typeMap,
            F::class
        )
            .apply(builder)
    )

fun NavGraphBuilder.buildGraph(){

    keepStateFragment<CardListFragment, CardList> {
        label = "卡片列表"
    }

    keepStateFragment<CardDetailFragment, CardDetail> {
        label = "卡片详情"

    }
    keepStateFragment<MusicFragment, Music> {
        label = "音乐"
    }

    keepStateFragment<EventFragment, Event> {
        label = "活动"
    }

    keepStateFragment<AboutFragment, About> {
        label = "关于"
    }


}

fun <T : Any> NavController.navigateTo(route: T,
                             navOptions: NavOptions?,
                             navigatorExtras: Navigator.Extras?) {
    val navOption: NavOptions = navOptions ?: NavOptions.Builder().setLaunchSingleTop(true)
        .build()
    navigate(route, navOption, navigatorExtras)
}

@NavDestinationDsl
class KeepStateFragmentNavigatorDestinationBuilder :
    NavDestinationBuilder<FragmentNavigator.Destination> {
    private var fragmentClass: KClass<out Fragment>

    public constructor(
        navigator: Navigator<FragmentNavigator.Destination>,
        route: KClass<out Any>,
        typeMap: Map<KType, @JvmSuppressWildcards NavType<*>>,
        fragmentClass: KClass<out Fragment>,
    ) : super(navigator, route, typeMap) {
        this.fragmentClass = fragmentClass
    }


    override fun build(): FragmentNavigator.Destination =
        super.build().also { destination ->
            destination.setClassName(fragmentClass.java.name)
        }
}



