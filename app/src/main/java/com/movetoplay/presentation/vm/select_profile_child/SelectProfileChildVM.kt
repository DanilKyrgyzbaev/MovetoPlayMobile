package com.movetoplay.presentation.vm.select_profile_child

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.domain.model.Child
import com.movetoplay.domain.model.Gender
import com.movetoplay.domain.use_case.select_profile_child.GetListChildUseCase
import com.movetoplay.domain.use_case.select_profile_child.SelectProfileChildUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SelectProfileChildVM @Inject constructor(
    private val selectProfileChildUseCase : SelectProfileChildUseCase
) : ViewModel(){

    private val listChild = mutableListOf<Child>()
    private val _state: MutableState<StateSelectProfileChild?> = mutableStateOf(null)
    val state: State<StateSelectProfileChild?> get() = _state
    val listNameChild get() = listChild.map { "${it.name} - ${it.age}" }

    init {
        viewModelScope.async(Dispatchers.IO) {
            val answer = selectProfileChildUseCase.getListChild()
            withContext(Dispatchers.Main){
                listChild.addAll(answer)
                _state.value = listChild.getOrNull(0)?.let {
                    StateSelectProfileChild(
                        isEdit = false,
                        child = it,
                        index = 0
                    )
                } ?: StateSelectProfileChild(
                    isEdit = true,
                    child = Child(name = "", gender = Gender.MAN, age = 0),
                    index = -1
                )
            }
        }.start()
    }

    fun onEvent(event: EventSelectProfileChild){
        when(event){
            is EventSelectProfileChild.SelectProfileChild ->{
                _state.value = StateSelectProfileChild(
                    isEdit = false,
                    child = listChild[event.index],
                    index = event.index
                )
            }
            EventSelectProfileChild.AddNewProfileChild ->{
                _state.value = StateSelectProfileChild(
                    isEdit = true,
                    child = Child(name = "", gender = Gender.MAN, age = 0),
                    index = -1
                )
            }
            EventSelectProfileChild.ProfileSelected -> {
                viewModelScope.async {
                    selectProfileChildUseCase.saveProfileChild(_state.value!!.child)
                }.start()
            }
        }
    }
}