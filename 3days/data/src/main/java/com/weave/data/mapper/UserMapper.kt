package com.weave.data.mapper

import com.weave.model.domain.user.MyInfo
import com.weave.network.model.BirthYearRange
import com.weave.network.model.Gender
import com.weave.network.model.GetMyUserInfoResponse
import com.weave.network.model.JobOccupation
import com.weave.network.model.PreferDistance
import com.weave.network.model.ProfileWidget
import com.weave.network.model.ProfileWidgetType
import com.weave.network.model.UserDesiredPartner
import com.weave.network.model.UserProfile

val GetMyUserInfoResponse.toDomain
    get() = MyInfo(
        id = id,
        name = name,
        phoneNumber = phoneNumber,
        profile = profile.toDomain,
        desiredPartner = desiredPartner.toDomain,
        profileWidgets = profileWidgets.map { it.toDomain }
    )

val UserProfile.toDomain
    get() = com.weave.model.domain.user.UserProfile(
        gender = gender.toDomain,
        birthYear = birthYear,
        jobOccupation = jobOccupation.toDomain,
        locationIds = locationIds,
        companyId = companyId
    )

val Gender.toDomain
    get() = com.weave.model.domain.user.Gender.entries.find { it.value == this.value }
        ?: com.weave.model.domain.user.Gender.MALE

val JobOccupation.toDomain
    get() = com.weave.model.domain.myprofile.JobOccupation.entries.find { it.enValue == this.value }
        ?: com.weave.model.domain.myprofile.JobOccupation.OTHER

val UserDesiredPartner.toDomain
    get() = com.weave.model.domain.user.UserDesiredPartner(
        birthYearRange = birthYearRange.toDomain,
        jobOccupations = jobOccupations.map { it.toDomain },
        preferDistance = preferDistance.toDomain,
        allowSameCompany = allowSameCompany
    )

val BirthYearRange.toDomain
    get() = com.weave.model.domain.user.BirthYearRange(
        start = start,
        end = end
    )

val PreferDistance.toDomain
    get() = com.weave.model.enum.PreferDistance.entries.find { it.value == this.value }
        ?: com.weave.model.enum.PreferDistance.ANYWHERE

val ProfileWidget.toDomain
    get() = com.weave.model.domain.user.ProfileWidget(
        type = type.toDomain,
        content = content
    )

val ProfileWidgetType.toDomain
    get() = com.weave.model.domain.user.ProfileWidgetType.entries.find { it.value == this.value }
        ?: com.weave.model.domain.user.ProfileWidgetType.HOBBY

val MyInfo.toDTO
    get() = GetMyUserInfoResponse(
        id = id,
        name = name,
        phoneNumber = phoneNumber,
        profile = profile.toDTO,
        desiredPartner = desiredPartner.toDTO,
        profileWidgets = profileWidgets.map { it.toDTO }
    )

val com.weave.model.domain.user.UserProfile.toDTO
    get() = UserProfile(
        gender = gender.toDTO,
        birthYear = birthYear,
        jobOccupation = jobOccupation.toDTO,
        locationIds = locationIds,
        companyId = companyId
    )

val com.weave.model.domain.user.Gender.toDTO
    get() = Gender.entries.find { it.value == this.value }
        ?: Gender.MALE

val com.weave.model.domain.myprofile.JobOccupation.toDTO
    get() = JobOccupation.entries.find { it.value == this.enValue }
        ?: JobOccupation.OTHER

val com.weave.model.domain.user.UserDesiredPartner.toDTO
    get() = UserDesiredPartner(
        birthYearRange = birthYearRange.toDTO,
        jobOccupations = jobOccupations.map { it.toDTO },
        preferDistance = preferDistance.toDTO,
        allowSameCompany = allowSameCompany
    )

val com.weave.model.domain.user.BirthYearRange.toDTO
    get() = BirthYearRange(
        start = start,
        end = end
    )

val com.weave.model.enum.PreferDistance.toDTO
    get() = PreferDistance.entries.find { it.value == this.value }
        ?: PreferDistance.ANYWHERE

val com.weave.model.domain.user.ProfileWidget.toDTO
    get() = ProfileWidget(
        type = type.toDTO,
        content = content
    )

val com.weave.model.domain.user.ProfileWidgetType.toDTO
    get() = ProfileWidgetType.entries.find { it.value == this.value }
        ?: ProfileWidgetType.HOBBY