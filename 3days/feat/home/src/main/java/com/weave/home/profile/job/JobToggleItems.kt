package com.weave.home.profile.job

import com.weave.design_system.R
import com.weave.model.domain.myprofile.JobOccupation

data class JobToggleItem(
    val text: String,
    val resourceId: Int
)

val toggleItems = listOf(
    JobToggleItem(JobOccupation.entries[0].koValue, R.drawable.ic_business),
    JobToggleItem(JobOccupation.entries[1].koValue, R.drawable.ic_marketing),
    JobToggleItem(JobOccupation.entries[2].koValue, R.drawable.ic_research),
    JobToggleItem(JobOccupation.entries[3].koValue, R.drawable.ic_tech),
    JobToggleItem(JobOccupation.entries[4].koValue, R.drawable.ic_finance),
    JobToggleItem(JobOccupation.entries[5].koValue, R.drawable.ic_gear),
    JobToggleItem(JobOccupation.entries[6].koValue, R.drawable.ic_education),
    JobToggleItem(JobOccupation.entries[7].koValue, R.drawable.ic_legal),
    JobToggleItem(JobOccupation.entries[8].koValue, R.drawable.ic_security),
    JobToggleItem(JobOccupation.entries[9].koValue, R.drawable.ic_medical),
    JobToggleItem(JobOccupation.entries[10].koValue, R.drawable.ic_media),
    JobToggleItem(JobOccupation.entries[11].koValue, R.drawable.ic_design),
    JobToggleItem(JobOccupation.entries[12].koValue, R.drawable.ic_sports),
    JobToggleItem(JobOccupation.entries[13].koValue, R.drawable.ic_building),
    JobToggleItem(JobOccupation.entries[14].koValue, R.drawable.ic_train),
    JobToggleItem(JobOccupation.entries[15].koValue, R.drawable.ic_leafy),
    JobToggleItem(JobOccupation.entries[16].koValue, R.drawable.ic_speech),
    JobToggleItem(JobOccupation.entries[17].koValue, R.drawable.ic_others)
)