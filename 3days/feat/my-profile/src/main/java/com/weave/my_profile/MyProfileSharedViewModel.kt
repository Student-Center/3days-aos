package com.weave.my_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.weave.design_system.component.Gender
import com.weave.model.domain.myprofile.Company
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyProfileSharedViewModel @Inject constructor(

) : ViewModel() {
    var registerToken by mutableStateOf("")
    var genderState by mutableStateOf(Gender.EMPTY)
    val birthYear = mutableStateListOf("", "", "", "")
    var company: Company? = null
    var isMatchSameCompany: Boolean? = null
}