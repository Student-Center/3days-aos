package com.weave.home.profile.main

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.weave.design_system.component.SnackBarType
import com.weave.design_system.extension.applyShadow
import com.weave.design_system.extension.noRippleClickable
import com.weave.home.R
import com.weave.home.profile.ProfileEditType
import com.weave.home.profile.UserInfo
import com.weave.home.profile.main.date.DateProfileSection
import com.weave.home.profile.widget.AddProfileWidgetSheet
import com.weave.home.profile.widget.EditProfileWidgetSheet
import com.weave.home.profile.widget.ProfileWidgetSection
import com.weave.home.profile.widget.ProfileWidgetSelectSheet
import com.weave.model.domain.myprofile.Company
import com.weave.model.domain.myprofile.JobOccupation
import com.weave.model.domain.user.ProfileWidget
import com.weave.model.domain.user.ProfileWidgetType
import com.weave.utils.image.ImageUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    snackBarViewModel: SnackBarViewModel = hiltViewModel(),
    innerPadding: PaddingValues,
    moveToProfileEdit: (ProfileEditType, UserInfo?) -> Unit
) {
    val context = LocalContext.current

    var profileOptionSelectorState by remember { mutableStateOf(false) }
    var imageConfirmViewState by remember { mutableStateOf<File?>(null) }

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden }
    )

    var openBottomSheet2 by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState2 = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden }
    )

    var openBottomSheet3 by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState3 = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden }
    )

    var widgetType by remember { mutableStateOf<ProfileWidgetType?>(null) }
    var widget by remember { mutableStateOf<ProfileWidget?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { selectedUri ->
            val imageFile = ImageUtils.createImageFile(context, selectedUri)
            imageFile?.let {
                imageConfirmViewState = it
            } ?: viewModel.setEffect {
                ProfileEffect.ShowToast(
                    message = "5MB 이하 이미지만 가능해요",
                    type = SnackBarType.ERROR
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        if (viewModel.uiState.name.isBlank()) viewModel.setAction(ProfileAction.FetchData)
    }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ProfileEffect.DismissSheet -> {
                    openBottomSheet = false
                    openBottomSheet2 = false
                    openBottomSheet3 = false

                    bottomSheetState.hide()
                    bottomSheetState2.hide()
                    bottomSheetState3.hide()
                }

                is ProfileEffect.ShowToast -> {
                    snackBarViewModel.showSnackBar(
                        message = effect.message,
                        type = effect.type
                    )
                }
            }
        }
    }

    ProfileScreenContent(
        uiState = viewModel.uiState,
        launcher = launcher,
        type = widgetType,
        widget = widget,
        profileOptionSelectorState = profileOptionSelectorState,
        imageConfirmViewState = imageConfirmViewState,
        sheetState = bottomSheetState,
        openBottomSheet = openBottomSheet,
        sheetState2 = bottomSheetState2,
        openBottomSheet2 = openBottomSheet2,
        sheetState3 = bottomSheetState3,
        openBottomSheet3 = openBottomSheet3,
        innerPadding = innerPadding,
        onDismissSheetRequest = {
            when (it) {
                1 -> {
                    openBottomSheet = false
                    scope
                        .launch { bottomSheetState.hide() }
                        .invokeOnCompletion {
                            if (!bottomSheetState.isVisible) {
                                openBottomSheet = false
                            }
                        }
                }

                2 -> {
                    openBottomSheet2 = false
                    scope
                        .launch { bottomSheetState2.hide() }
                        .invokeOnCompletion {
                            if (!bottomSheetState2.isVisible) {
                                openBottomSheet2 = false
                            }
                        }
                }

                3 -> {
                    openBottomSheet3 = false
                    scope
                        .launch { bottomSheetState3.hide() }
                        .invokeOnCompletion {
                            if (!bottomSheetState3.isVisible) {
                                openBottomSheet3 = false
                            }
                        }
                }
            }

        },
        onChangeSheet = {
            openBottomSheet = !openBottomSheet
        },
        onAddType = {
            widgetType = it
            openBottomSheet2 = !openBottomSheet2
        },
        onEditWidget = {
            widget = it
            openBottomSheet3 = !openBottomSheet3
        },
        onWidgetAdd = { type, content ->
            viewModel.setAction(ProfileAction.AddProfileWidget(type, content))
        },
        onWidgetEdit = { type, content ->
            viewModel.setAction(ProfileAction.EditProfileWidget(type, content))
        },
        onWidgetDelete = { type ->
            viewModel.setAction(ProfileAction.DeleteProfileWidget(type))
        },
        onClickImageSelector = {
            profileOptionSelectorState = it
        },
        onDeleteProfileImage = {
            viewModel.setAction(ProfileAction.DeleteProfileImage)
        },
        onChangeProfileImage = {
            viewModel.setAction(ProfileAction.UploadProfileImage(it))
        },
        onDismissConfirmView = {
            imageConfirmViewState = null
        },
        moveToProfileEdit = { type ->
            when (type) {
                ProfileEditType.JOB_OCCUPATION -> {
                    moveToProfileEdit(
                        ProfileEditType.JOB_OCCUPATION,
                        viewModel.uiState.userInfo
                    )
                }

                ProfileEditType.COMPANY -> {
                    moveToProfileEdit(
                        ProfileEditType.COMPANY,
                        viewModel.uiState.userInfo
                    )
                }

                ProfileEditType.LOCATION -> {
                    moveToProfileEdit(
                        ProfileEditType.LOCATION,
                        viewModel.uiState.userInfo
                    )
                }

                ProfileEditType.PARTNER_AGE -> {
                    moveToProfileEdit(
                        ProfileEditType.PARTNER_AGE,
                        viewModel.uiState.userInfo
                    )
                }

                ProfileEditType.PARTNER_JOB_OCCUPATION -> {
                    moveToProfileEdit(
                        ProfileEditType.PARTNER_JOB_OCCUPATION,
                        viewModel.uiState.userInfo
                    )
                }

                ProfileEditType.PARTNER_DISTANCE -> {
                    moveToProfileEdit(
                        ProfileEditType.PARTNER_DISTANCE,
                        viewModel.uiState.userInfo
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreenContent(
    innerPadding: PaddingValues,
    uiState: ProfileState,
    launcher: ActivityResultLauncher<String>,
    type: ProfileWidgetType?,
    widget: ProfileWidget?,
    profileOptionSelectorState: Boolean,
    imageConfirmViewState: File?,
    sheetState: SheetState,
    openBottomSheet: Boolean,
    sheetState2: SheetState,
    openBottomSheet2: Boolean,
    sheetState3: SheetState,
    openBottomSheet3: Boolean,
    onChangeSheet: () -> Unit,
    onAddType: (ProfileWidgetType) -> Unit,
    onEditWidget: (ProfileWidget) -> Unit,
    onDismissSheetRequest: (Int) -> Unit,
    moveToProfileEdit: (ProfileEditType) -> Unit,
    onWidgetAdd: (ProfileWidgetType, String) -> Unit,
    onWidgetEdit: (ProfileWidgetType, String) -> Unit,
    onWidgetDelete: (ProfileWidgetType) -> Unit,
    onClickImageSelector: (Boolean) -> Unit,
    onDismissConfirmView: () -> Unit,
    onDeleteProfileImage: () -> Unit,
    onChangeProfileImage: (File) -> Unit,
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
                DateProfileSection(
                    userInfo = uiState.userInfo,
                    onEditPartnerInfo = moveToProfileEdit
                )

                Spacer(modifier = Modifier.height(80.dp))
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
                    moveToMyProfileEdit = { moveToProfileEdit(it) },
                    onClickImageSelector = { onClickImageSelector(true) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }

            item {
                ProfileWidgetSection(
                    modifier = Modifier.padding(horizontal = 2.dp),
                    profileWidgets = uiState.profileWidgets,
                    onWidgetEdit = onEditWidget,
                    onWidgetDelete = onWidgetDelete,
                    onBlankWidgetClick = { onChangeSheet() }
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        Box(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            DaysTheme.colors.bgDefault,
                            DaysTheme.colors.bgDefault.copy(alpha = 0.1f)
                        )
                    )
                )
        )

        if (imageConfirmViewState != null) {
            val scope = rememberCoroutineScope()

            ProfileConfirmDialog(
                imageFile = imageConfirmViewState,
                onConfirm = {
                    onChangeProfileImage(it)
                    scope.launch {
                        delay(1000)
                        onDismissConfirmView()
                    }
                },
                onDismiss = onDismissConfirmView
            )
        }

        if (profileOptionSelectorState) {
            ImageOptionDialog(
                onClickChange = {
                    onClickImageSelector(false)
                    launcher.launch("image/*")
                },
                onClickDelete = {
                    onClickImageSelector(false)
                    onDeleteProfileImage()
                },
                onDismiss = {
                    onClickImageSelector(false)
                }
            )
        }

        if (openBottomSheet) {
            ProfileWidgetSelectSheet(
                sheetState = sheetState,
                myProfileWidgets = uiState.profileWidgets.map { it.type },
                onDismissRequest = { onDismissSheetRequest(1) },
                onSelectedType = { type -> onAddType(type) }
            )
        }

        if (openBottomSheet2 && type != null) {
            AddProfileWidgetSheet(
                sheetState = sheetState2,
                profileWidgetType = type,
                onDismissRequest = { onDismissSheetRequest(2) },
                onAddRequest = onWidgetAdd
            )
        }

        if (openBottomSheet3 && widget != null) {
            EditProfileWidgetSheet(
                sheetState = sheetState3,
                profileWidget = widget,
                onDismissRequest = { onDismissSheetRequest(3) },
                onEditRequest = onWidgetEdit
            )
        }
    }
}

@Composable
private fun ProfileSection(
    uiState: ProfileState,
    moveToMyProfileEdit: (ProfileEditType) -> Unit,
    onClickImageSelector: () -> Unit
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

        ProfileImage(
            profileImage = uiState.profileImages.firstOrNull()?.url?.toString() ?: "",
            onClickImageSelector = onClickImageSelector
        )
    }
}

@Composable
private fun ProfileImage(
    profileImage: String,
    onClickImageSelector: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        SubcomposeAsyncImage(
            model = profileImage,
            contentDescription = "",
            loading = { ProfileImageContent() },
            error = { ProfileImageContent() },
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

        Box(
            modifier = Modifier,
            contentAlignment = Alignment.TopEnd
        ) {
            Image(
                painter = painterResource(id = R.drawable.png_profile_bg_deco),
                contentDescription = "",
                modifier = Modifier.size(102.dp)
            )

            Box(
                modifier = Modifier
                    .offset(x = 6.dp, y = (-6).dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .applyShadow(CircleShape)
                    .background(Color.White)
                    .noRippleClickable {
                        onClickImageSelector()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = com.weave.design_system.R.drawable.ic_camera),
                    contentDescription = "",
                    tint = DaysTheme.colors.grey300,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ProfileImageContent() {
    Image(
        painter = painterResource(id = R.drawable.png_profile_bg),
        contentDescription = "",
        modifier = Modifier
            .size(102.dp)
            .applyShadow(
                shape = RoundedCornerShape(42.95.dp)
            )
    )
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFFF5F1EE)
@Composable
private fun ProfileScreenPreview() {
    val uiState = ProfileState(
        name = "위브",
        birthYear = 2000,
        occupation = JobOccupation.SPORTS,
        profileWidgets = ProfileWidgetType.entries.take(3).map {
            ProfileWidget(
                it, it.getExample()
            )
        }
    )

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden }
    )

    val openBottomSheet2 by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState2 = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden }
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {}

    val widgetType by remember { mutableStateOf<ProfileWidgetType?>(null) }
    val widget by remember { mutableStateOf<ProfileWidget?>(null) }

    ProfileScreenContent(
        uiState = uiState,
        launcher = launcher,
        type = widgetType,
        profileOptionSelectorState = false,
        imageConfirmViewState = null,
        widget = widget,
        sheetState = bottomSheetState,
        openBottomSheet = openBottomSheet,
        sheetState2 = bottomSheetState2,
        openBottomSheet2 = openBottomSheet2,
        sheetState3 = bottomSheetState2,
        openBottomSheet3 = openBottomSheet2,
        innerPadding = PaddingValues(),
        onDismissSheetRequest = {
            openBottomSheet = false
            scope
                .launch { bottomSheetState.hide() }
                .invokeOnCompletion {
                    if (!bottomSheetState.isVisible) {
                        openBottomSheet = false
                    }
                }
        },
        onChangeSheet = {
            openBottomSheet = !openBottomSheet
        },
        onAddType = {},
        onEditWidget = {},
        onWidgetEdit = { _, _ -> },
        onWidgetDelete = {},
        onWidgetAdd = { _, _ ->
            openBottomSheet = true
            scope
                .launch { bottomSheetState.show() }
                .invokeOnCompletion {
                    if (bottomSheetState.isVisible) {
                        openBottomSheet = true
                    }
                }
        },
        onChangeProfileImage = {},
        onDismissConfirmView = {},
        onDeleteProfileImage = {},
        onClickImageSelector = {},
        moveToProfileEdit = { type ->
            when (type) {
                ProfileEditType.JOB_OCCUPATION -> {}
                ProfileEditType.COMPANY -> {}
                ProfileEditType.LOCATION -> {}
                else -> {}
            }
        }
    )
}