package com.movetoplay.domain.use_case

import com.movetoplay.domain.utils.TypesRegularExpressions

class RegularExpressionsUseCase {
    // Возвращает правду если поле проходит под условия проверки
    operator fun invoke(field: String, type: TypesRegularExpressions) : Boolean{
        return when(type){
            TypesRegularExpressions.Email ->{
                val address = field.split('@')
                if(address.size == 2){
                    address[1].split('.').size > 1
                }else{
                    false
                }
            }
            TypesRegularExpressions.Password -> {
                field.length >= 6
            }
            is TypesRegularExpressions.RepeatPassword -> {
                type.whatToCompareWith == field
            }
        }
    }
}