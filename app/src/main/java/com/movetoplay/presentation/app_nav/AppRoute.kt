package com.movetoplay.presentation.app_nav

sealed class AppRoute(val route: String){
    object Splash : AppRoute("splash_route")
    object Starting : AppRoute("starting_route")
    object ChildrenContent : AppRoute("children_content_route")
    object ParentContent : AppRoute("parent_content_route")
}