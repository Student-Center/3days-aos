package com.weave.data.mapper

import com.weave.network.model.Location
import com.weave.model.domain.myprofile.Location as LocationEntity

val Location.toDomain
    get() = LocationEntity(
        id = id, region = region, subRegion = subRegion
    )