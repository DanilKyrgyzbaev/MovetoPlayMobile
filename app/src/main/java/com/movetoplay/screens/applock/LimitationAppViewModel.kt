package com.movetoplay.screens.applock

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.data.model.user_apps.UserAppsBody
import com.movetoplay.domain.repository.UserAppsRepository
import com.movetoplay.domain.utils.RequestStatus
import com.movetoplay.pref.Pref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LimitationAppViewModel @Inject constructor(val repository: UserAppsRepository) : ViewModel() {

    var response = MutableLiveData<RequestStatus>()

    fun sendLimitedApps(apps: UserAppsBody) {
        Log.e("TAG", "sendLimitedApps: ${apps.apps}", )
        viewModelScope.launch {
            repository.postLimitedApps(Pref.userToken, apps).collect {
                Log.e("TAG", "sendLimitedApps: $it", )
                when (it) {
                    is RequestStatus.Loading<*> -> {
                        //progress.visibility = View.VISIBLE
                        Log.e("TAG", "initListeners: LOADING", )
                    }
                    is RequestStatus.Success<*> -> {
                        Log.e("TAG","initlisteners SUCCESS")
                        //  progress.visibility = View.GONE
                        //finish()
                    }
                    is RequestStatus.Error<*> -> {
                        Log.e("TAG","initlisteners SUCCESS")
                        // progress.visibility = View.GONE
                       // Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Log.e("TAG","initlisteners SUCCESS")
                        // progress.visibility = View.GONE
                    }
            }
            }
        }
    }


}