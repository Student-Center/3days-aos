package com.weave.my_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyProfileSharedViewModel @Inject constructor(

): ViewModel() {

    var genderState by mutableStateOf("")
    val birthYear = mutableStateListOf("", "", "", "")
}