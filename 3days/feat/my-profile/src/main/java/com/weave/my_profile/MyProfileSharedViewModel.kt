package com.weave.my_profile

import androidx.lifecycle.ViewModel
import com.weave.design_system.component.Gender
import com.weave.model.domain.myprofile.Company
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.model.domain.myprofile.Location
import com.weave.model.domain.user.BirthYearRange
import com.weave.model.domain.user.RegisterInfo
import com.weave.model.domain.user.UserDesiredPartner
import com.weave.model.domain.user.UserProfile
import com.weave.model.enum.PreferDistance
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyProfileSharedViewModel @Inject constructor(

) : ViewModel() {
    var registerToken: String = ""
    var phoneNumber: String = ""
    var genderState: Gender = Gender.EMPTY
    val birthYear = mutableListOf("", "", "", "")
    var company: Company? = null
    var isMatchSameCompany: Boolean? = null
    var occupation: JobOccupation? = null
    var locations: List<Location> = listOf()
    var nickname: String = ""

    var upperAge: Int? = null
    var underAge: Int? = null
    var partnerOccupations: List<JobOccupation> = listOf()
    var distance: PreferDistance? = null

    fun getRegisterInfo(): RegisterInfo {
        val userProfile = UserProfile(
            gender = com.weave.model.domain.user.Gender.entries.find { it.value == genderState.enValue }
                ?: com.weave.model.domain.user.Gender.MALE,
            birthYear = birthYear.joinToString("").toIntOrNull() ?: 2000,
            jobOccupation = occupation ?: JobOccupation.OTHER,
            locationIds = locations.map { it.id },
            companyId = company?.id
        )

        val desiredPartner = UserDesiredPartner(
            birthYearRange = BirthYearRange(underAge, upperAge),
            jobOccupations = partnerOccupations,
            preferDistance = distance ?: PreferDistance.ANYWHERE
        )

        return RegisterInfo(
            name = nickname,
            phoneNumber = phoneNumber,
            profile = userProfile,
            desiredPartner = desiredPartner
        )
    }
}