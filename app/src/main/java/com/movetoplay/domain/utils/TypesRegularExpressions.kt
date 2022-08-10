package com.movetoplay.domain.utils

sealed class TypesRegularExpressions {
    object Email : TypesRegularExpressions()
    object Password : TypesRegularExpressions()
    class RepeatPassword(val whatToCompareWith: String) : TypesRegularExpressions()
}