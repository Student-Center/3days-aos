package com.weave.my_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.weave.design_system.component.Gender
import com.weave.model.domain.myprofile.Company
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.model.domain.myprofile.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyProfileSharedViewModel @Inject constructor(

) : ViewModel() {
    var registerToken: String = ""
    var genderState : Gender = Gender.EMPTY
    val birthYear = mutableListOf("", "", "", "")
    var company: Company? = null
    var isMatchSameCompany: Boolean? = null
    var occupation: JobOccupation? = null
    var locations: List<Location> = listOf()
    var nickname: String = ""
    var upperAge: Int? = null
    var underAge: Int? = null
}