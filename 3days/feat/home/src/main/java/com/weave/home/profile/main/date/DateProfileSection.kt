package com.weave.home.profile.main.date

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.extension.noRippleClickable
import com.weave.home.R
import com.weave.home.profile.ProfileEditType
import com.weave.home.profile.UserInfo
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.model.domain.user.BirthYearRange
import com.weave.model.domain.user.UserDesiredPartner
import com.weave.model.enum.PreferDistance

object DateProfileConstants {
    object Colors {
        val CardBackground = Color(0xFFFBF0FF)
        val DotSelected = Color(0xFFDA96F3)
    }

    object Dimensions {
        const val CARD_HEIGHT = 172
        const val CARD_CORNER_RADIUS = 24
        const val CARD_BORDER_WIDTH = 7
    }
}

@Composable
fun DateProfileSection(
    userInfo: UserInfo,
    onEditPartnerInfo: (ProfileEditType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SectionTitle()
        Spacer(modifier = Modifier.height(12.dp))
        DateProfileCard(
            userInfo = userInfo,
            onEditPartnerInfo = onEditPartnerInfo
        )
    }
}

@Composable
private fun SectionTitle() {
    Text(
        text = "Date Profile",
        style = DaysTheme.typography.enMedium20.toTextStyle(),
        color = DaysTheme.colors.grey400
    )
}

@Composable
fun DateProfileCard(
    userInfo: UserInfo,
    onEditPartnerInfo: (ProfileEditType) -> Unit
) {
    val types = ProfileEditType.entries.filter { it.isPartnerType }
    val pagerState = rememberPagerState(pageCount = { types.size })

    DateProfileCardContainer {
        DateProfilePager(
            types = types,
            pagerState = pagerState,
            userInfo = userInfo,
            onEditPartnerInfo = onEditPartnerInfo
        )
    }
}

@Composable
private fun DateProfileCardContainer(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(DateProfileConstants.Dimensions.CARD_HEIGHT.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .background(
                    color = DateProfileConstants.Colors.CardBackground,
                    shape = RoundedCornerShape(DateProfileConstants.Dimensions.CARD_CORNER_RADIUS.dp)
                )
                .border(
                    width = DateProfileConstants.Dimensions.CARD_BORDER_WIDTH.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(DateProfileConstants.Dimensions.CARD_CORNER_RADIUS.dp)
                )
        ) {
            content()
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Image(
                modifier = Modifier.size(52.dp),
                painter = painterResource(id = R.drawable.ic_reading_glasses_border),
                contentDescription = ""
            )
        }
    }
}

@Composable
private fun DateProfilePager(
    types: List<ProfileEditType>,
    pagerState: PagerState,
    userInfo: UserInfo,
    onEditPartnerInfo: (ProfileEditType) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.matchParentSize()
        ) { page ->
            DateProfileContent(
                modifier = Modifier.fillMaxSize(),
                type = types[page],
                userInfo = userInfo,
                onEditPartnerInfo = onEditPartnerInfo
            )
        }

        PaginationDots(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun PaginationDots(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { index ->
            PaginationDot(isSelected = index == pagerState.currentPage)
            if (index < pagerState.pageCount - 1) {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Composable
private fun PaginationDot(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(5.dp)
            .clip(CircleShape)
            .background(
                if (isSelected) DateProfileConstants.Colors.DotSelected
                else DaysTheme.colors.grey400.copy(alpha = 0.12f)
            )
    )
}

@Composable
private fun DateProfileContent(
    modifier: Modifier = Modifier,
    type: ProfileEditType,
    userInfo: UserInfo,
    onEditPartnerInfo: (ProfileEditType) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 26.dp)
            .padding(top = 25.dp, bottom = 16.dp),
    ) {
        ProfileTypeHeader(type = type, onEditClick = { onEditPartnerInfo(type) })

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            userInfo.desiredPartner?.let { partner ->
                when (type) {
                    ProfileEditType.PARTNER_AGE -> PartnerAgeView(partner.birthYearRange)
                    ProfileEditType.PARTNER_JOB_OCCUPATION -> PartnerJobView(partner.jobOccupations)
                    ProfileEditType.PARTNER_DISTANCE -> PartnerDistanceView(partner.preferDistance)
                    else -> PartnerAgeView(partner.birthYearRange)
                }
            }
        }
    }
}

@Composable
private fun ProfileTypeHeader(
    type: ProfileEditType,
    onEditClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "나의 ${type.title}",
            style = DaysTheme.typography.semiBold14.toTextStyle(),
            color = DaysTheme.colors.grey500
        )
        Spacer(modifier = Modifier.width(4.dp))
        EditButton(onClick = onEditClick)
    }
}

@Composable
private fun EditButton(onClick: () -> Unit) {
    Icon(
        imageVector = Icons.Rounded.Edit,
        contentDescription = "Edit",
        tint = DaysTheme.colors.black.copy(alpha = 0.3f),
        modifier = Modifier
            .size(14.dp)
            .noRippleClickable(onClick)
    )
}

@Composable
private fun PartnerAgeView(birthYearRange: BirthYearRange) {
    Column(verticalArrangement = Arrangement.Center) {
        AgePreferenceRow(
            icon = com.weave.design_system.R.drawable.ic_pointing_up,
            prefix = "내 나이보다 ",
            value = "위로 " + (birthYearRange.end?.let { "${it}살" } ?: "상관없어요"),
            valueColor = DaysTheme.colors.green500
        )
        Spacer(modifier = Modifier.height(8.dp))
        AgePreferenceRow(
            icon = com.weave.design_system.R.drawable.ic_pointing_down,
            prefix = "내 나이보다 ",
            value = "아래로 " + (birthYearRange.start?.let { "${it}살" } ?: "상관없어요"),
            valueColor = DaysTheme.colors.pink500
        )
    }
}

@Composable
private fun AgePreferenceRow(
    icon: Int,
    prefix: String,
    value: String,
    valueColor: Color
) {
    Row {
        Image(
            modifier = Modifier.size(18.dp),
            painter = painterResource(id = icon),
            contentDescription = null
        )
        Text(
            text = buildAnnotatedString {
                append(prefix)
                withStyle(SpanStyle(color = valueColor, fontWeight = FontWeight.W500)) {
                    append(value)
                }
            },
            style = DaysTheme.typography.regular14.toTextStyle(),
            color = DaysTheme.colors.grey300
        )
    }
}

@Composable
private fun PartnerJobView(jobOccupations: List<JobOccupation>) {
    Column(verticalArrangement = Arrangement.Center) {
        // Implement job occupation view
    }
}

@Composable
private fun PartnerDistanceView(distance: PreferDistance) {
    Column(verticalArrangement = Arrangement.Center) {
        // Implement distance view
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EE)
@Composable
private fun DateProfileSectionPreview() {
    Box(modifier = Modifier.padding(20.dp)) {
        DateProfileSection(
            userInfo = UserInfo(
                name = "김위브",
                jobOccupation = JobOccupation.OTHER,
                locations = emptyList(),
                desiredPartner = UserDesiredPartner(
                    birthYearRange = BirthYearRange(null, 3),
                    jobOccupations = listOf(JobOccupation.SPORTS, JobOccupation.ARTS_DESIGN),
                    preferDistance = PreferDistance.ANYWHERE
                )
            ),
            onEditPartnerInfo = {}
        )
    }
}