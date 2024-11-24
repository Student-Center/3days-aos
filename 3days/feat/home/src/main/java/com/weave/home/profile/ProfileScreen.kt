package com.weave.home.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.weave.design_system.DaysTheme
import com.weave.design_system.extension.applyShadow
import com.weave.design_system.extension.noRippleClickable
import com.weave.home.R
import com.weave.home.profile.widget.ProfileWidgetSection
import com.weave.model.domain.myprofile.Company
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.model.domain.user.ProfileWidget
import com.weave.model.domain.user.ProfileWidgetType

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    innerPadding: PaddingValues,
    moveToMyProfileEdit: (ProfileEditType, Any?) -> Unit
) {
    LaunchedEffect(Unit) {
        if (viewModel.uiState.name.isBlank()) viewModel.setAction(ProfileAction.FetchData)
    }

    ProfileScreenContent(
        uiState = viewModel.uiState,
        innerPadding = innerPadding,
        moveToMyProfileEdit = { type ->
            when (type) {
                ProfileEditType.JOB_OCCUPATION -> {
                    moveToMyProfileEdit(
                        ProfileEditType.JOB_OCCUPATION,
                        viewModel.uiState.occupation ?: JobOccupation.OTHER
                    )
                }

                ProfileEditType.COMPANY -> {
                    moveToMyProfileEdit(
                        ProfileEditType.COMPANY,
                        viewModel.uiState.company
                    )
                }

                ProfileEditType.LOCATION -> {
                    moveToMyProfileEdit(
                        ProfileEditType.LOCATION,
                        viewModel.uiState.locations
                    )
                }
            }
        }
    )
}

@Composable
private fun ProfileScreenContent(
    innerPadding: PaddingValues,
    uiState: ProfileState,
    moveToMyProfileEdit: (ProfileEditType) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = innerPadding.calculateBottomPadding())
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Text(
                    text = "My Profile",
                    style = DaysTheme.typography.enMedium20.toTextStyle(),
                    color = DaysTheme.colors.grey500
                )
            }

            item {
                ProfileSection(
                    uiState = uiState,
                    moveToMyProfileEdit = { moveToMyProfileEdit(it) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }

            item {
                ProfileWidgetSection(
                    modifier = Modifier.padding(horizontal = 2.dp),
                    profileWidgets = uiState.profileWidgets,
                    onWidgetEdit = {},
                    onWidgetDelete = {},
                    onBlankWidgetClick = {}
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun ProfileSection(
    uiState: ProfileState,
    moveToMyProfileEdit: (ProfileEditType) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxWidth()
                .height(431.dp)
                .applyShadow(RoundedCornerShape(24.dp))
                .background(
                    color = DaysTheme.colors.white,
                    shape = RoundedCornerShape(24.dp)
                ),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ProfileSectionDeco()
            ProfileSectionDeco()
        }

        Column(
            modifier = Modifier
                .padding(top = 50.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth()
                .height(431.dp)
        ) {
            Spacer(modifier = Modifier.height(62.dp))

            Text(
                text = uiState.name,
                style = DaysTheme.typography.semiBold28.toTextStyle(),
                color = DaysTheme.colors.black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
            )

            Text(
                text = "${uiState.birthYear}년생",
                style = DaysTheme.typography.medium14.toTextStyle(),
                color = DaysTheme.colors.grey200,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
            )

            Spacer(modifier = Modifier.height(20.dp))

            ProfileItem(
                type = "직군",
                occupation = uiState.occupation,
                moveToMyProfileEdit = { moveToMyProfileEdit(ProfileEditType.JOB_OCCUPATION) }
            )

            Spacer(modifier = Modifier.height(6.dp))

            ProfileItem(
                type = "직장",
                company = uiState.company,
                moveToMyProfileEdit = { moveToMyProfileEdit(ProfileEditType.COMPANY) }
            )

            Spacer(modifier = Modifier.height(6.dp))

            ProfileLocation(
                locations = uiState.locations.map { it.second },
                moveToMyProfileEdit = { moveToMyProfileEdit(ProfileEditType.LOCATION) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = text(),
                style = DaysTheme.typography.regular12.toTextStyle(),
                color = DaysTheme.colors.grey300,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding()
            )
        }

        ProfileImage(profileImage = uiState.profileUrl)
    }
}

@Composable
private fun ProfileImage(
    profileImage: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        SubcomposeAsyncImage(
            model = profileImage,
            contentDescription = "",
            loading = {
                CircularProgressIndicator()
            },
            error = {
                Image(
                    painter = painterResource(id = R.drawable.png_profile_bg),
                    contentDescription = "",
                    modifier = Modifier
                        .size(102.dp)
                        .applyShadow(
                            shape = RoundedCornerShape(42.95.dp)
                        )
                )
            },
            success = { state ->
                Image(
                    painter = state.painter,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(102.dp)
                        .applyShadow(
                            shape = RoundedCornerShape(42.95.dp)
                        )
                        .clip(RoundedCornerShape(42.95.dp))
                )
            }
        )

        Image(
            painter = painterResource(id = R.drawable.png_profile_bg_deco),
            contentDescription = "",
            modifier = Modifier.size(102.dp)
        )
    }
}

@Composable
private fun text() = buildAnnotatedString {
    append("⏳서로의 ")
    withStyle(style = SpanStyle(color = DaysTheme.colors.blue300, fontWeight = FontWeight.W600)) {
        append("프로필은 2일차, 사진은 3일차")
    }
    append("에 볼 수 있어요")
}

@Composable
private fun ProfileItem(
    type: String,
    occupation: JobOccupation? = null,
    company: Company? = null,
    moveToMyProfileEdit: () -> Unit
) {

    val bgColor = if (type == "직군") DaysTheme.colors.green50 else DaysTheme.colors.pink50
    val borderColor = if (type == "직군") Color(0xFFE6EFDF) else Color(0xFFEFDFE5)
    val iconId = if (type == "직군") R.drawable.ic_round_business else R.drawable.ic_round_building
    val textColor = if (type == "직군") DaysTheme.colors.green500 else DaysTheme.colors.pink500
    val value = if (type == "직군") occupation?.koValue ?: "새회사" else company?.name ?: "새회사"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(color = bgColor)
            .border(width = 1.dp, color = borderColor)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = "",
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = type,
                style = DaysTheme.typography.medium14.toTextStyle(),
                color = textColor
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                style = DaysTheme.typography.medium14.toTextStyle(),
                color = textColor
            )

            Spacer(modifier = Modifier.width(2.dp))

            Icon(
                imageVector = Icons.Rounded.Edit,
                contentDescription = "",
                tint = DaysTheme.colors.black.copy(alpha = 0.3f),
                modifier = Modifier
                    .size(14.dp)
                    .noRippleClickable { moveToMyProfileEdit() }
            )
        }
    }
}

@Composable
private fun ProfileLocation(
    locations: List<String> = listOf(),
    moveToMyProfileEdit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(color = DaysTheme.colors.blue50)
            .border(width = 1.dp, color = Color(0xFFDFE8EF))
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = "",
                    tint = DaysTheme.colors.blue500,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "활동 지역",
                    style = DaysTheme.typography.medium14.toTextStyle(),
                    color = DaysTheme.colors.blue500
                )
            }

            Icon(
                imageVector = Icons.Rounded.Edit,
                contentDescription = "",
                tint = DaysTheme.colors.black.copy(alpha = 0.3f),
                modifier = Modifier
                    .size(14.dp)
                    .noRippleClickable { moveToMyProfileEdit() }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(bottom = if (locations.isNotEmpty()) 16.dp else 50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            locations.forEach { location ->
                LocationChip(location = location)
            }
        }
    }
}

@Composable
private fun LocationChip(
    location: String
) {
    Box(
        modifier = Modifier
            .height(34.dp)
            .background(
                color = DaysTheme.colors.white,
                shape = RoundedCornerShape(52.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFFDFE8EF),
                shape = RoundedCornerShape(52.dp)
            )
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = location,
                style = DaysTheme.typography.medium14.toTextStyle(),
                color = DaysTheme.colors.blue500
            )
        }
    }
}

@Composable
private fun ProfileSectionDeco() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .size(12.dp)
                .background(
                    color = Color(0xFFEBEBEB),
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = Color(0xFFD2D2D2),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .size(12.dp)
                .background(
                    color = Color(0xFFEBEBEB),
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = Color(0xFFD2D2D2),
                    shape = CircleShape
                )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EE)
@Composable
private fun ProfileScreenPreview() {
    val uiState = ProfileState(
        name = "위브",
        birthYear = 2000,
        occupation = JobOccupation.SPORTS,
        profileWidgets = ProfileWidgetType.entries.map {
            ProfileWidget(
                it, it.getExample()
            )
        }
    )

    ProfileScreenContent(
        uiState = uiState,
        innerPadding = PaddingValues(),
        moveToMyProfileEdit = { type ->
            when (type) {
                ProfileEditType.JOB_OCCUPATION -> {}
                ProfileEditType.COMPANY -> {}
                ProfileEditType.LOCATION -> {}
            }
        }
    )
}