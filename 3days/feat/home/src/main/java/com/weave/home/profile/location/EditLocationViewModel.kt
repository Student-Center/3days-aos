package com.weave.home.profile.location

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.weave.design_system.R
import com.weave.design_system.component.SnackBarType
import com.weave.location.GetLocationRegionsUseCase
import com.weave.location.GetLocationsByRegionUseCase
import com.weave.model.domain.myprofile.Location
import com.weave.user.UpdateMyInfoUseCase
import com.weave.utils.base.BaseViewModel
import com.weave.utils.base.UIAction
import com.weave.utils.base.UIEffect
import com.weave.utils.base.UIIntent
import com.weave.utils.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

sealed class EditLocationAction : UIAction {
    data class FetchData(val locations: List<Pair<UUID, String>>) : EditLocationAction()
    data object GetRegions : EditLocationAction()
    data class GetLocations(val regionName: String) : EditLocationAction()
    data class SelectLocation(val location: Location) : EditLocationAction()
    data class SelectRegionName(val regionName: String) : EditLocationAction()
    data object ValidateInput : EditLocationAction()
}

sealed class EditLocationIntent : UIIntent {
    data class FetchData(val locations: List<Pair<UUID, String>>) : EditLocationIntent()
    data object GetRegions : EditLocationIntent()
    data class GetLocations(val regionName: String) : EditLocationIntent()
    data class SelectLocation(val location: Location) : EditLocationIntent()
    data class SelectRegionName(val regionName: String) : EditLocationIntent()
    data object ValidateInput : EditLocationIntent()
}

data class EditLocationState(
    val errorMessage: String = "",
    val initLocations: List<Pair<UUID, String>> = listOf(),
    val locations: List<EditLocation> = listOf(),
    val selectedLocations: List<Location> = listOf(),
    var selectedRegionName: String = "",
) : UIState

sealed class EditLocationEffect : UIEffect {
    data class NavigateToProfile(val isSuccess: Boolean) : EditLocationEffect()
    data class ShowToast(val message: String, val type: SnackBarType) : EditLocationEffect()
}

data class EditLocation(
    val regionName: String,
    var locations: List<Location> = listOf()
)

@HiltViewModel
class EditLocationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val updateMyInfoUseCase: UpdateMyInfoUseCase,
    private val getRegionsUseCase: GetLocationRegionsUseCase,
    private val getLocationsUseCase: GetLocationsByRegionUseCase
) :
    BaseViewModel<EditLocationAction, EditLocationIntent, EditLocationState, EditLocationEffect>(
        initialState = EditLocationState()
    ) {
    override fun actionPredicate(action: EditLocationAction): EditLocationIntent {
        return when (action) {
            is EditLocationAction.FetchData -> EditLocationIntent.FetchData(action.locations)
            is EditLocationAction.GetRegions -> EditLocationIntent.GetRegions
            is EditLocationAction.GetLocations -> EditLocationIntent.GetLocations(action.regionName)
            is EditLocationAction.SelectLocation -> EditLocationIntent.SelectLocation(action.location)
            is EditLocationAction.SelectRegionName -> EditLocationIntent.SelectRegionName(action.regionName)
            is EditLocationAction.ValidateInput -> EditLocationIntent.ValidateInput
        }
    }

    override fun collectIntent(intent: EditLocationIntent) {
        when (intent) {
            is EditLocationIntent.FetchData -> fetchData(intent.locations)
            is EditLocationIntent.GetRegions -> getRegions()
            is EditLocationIntent.GetLocations -> getLocations(intent.regionName)
            is EditLocationIntent.SelectLocation -> selectLocation(intent.location)
            is EditLocationIntent.SelectRegionName -> selectRegionName(intent.regionName)
            is EditLocationIntent.ValidateInput -> validateInput()
        }
    }

    private fun fetchData(data: List<Pair<UUID, String>>) =
        setState {
            copy(
                initLocations = data,
                selectedLocations = data.map {
                    Location(
                        id = it.first,
                        region = "",
                        subRegion = it.second
                    )
                }
            )
        }

    private fun validateInput() {
        if (uiState.selectedLocations.isNotEmpty() && uiState.initLocations.toSet() != uiState.selectedLocations.toSet()) {
            viewModelScope.launch {
                updateMyInfoUseCase.invoke(
                    locationIds = uiState.selectedLocations.map { it.id }
                ).mapMerge().collect { result ->
                    if (result != null) {
                        setEffect {
                            EditLocationEffect.ShowToast(
                                message = "내 활동 지역이 변경되었어요",
                                type = SnackBarType.DEFAULT
                            )
                        }
                        setEffect { EditLocationEffect.NavigateToProfile(true) }
                    } else if (!isLoading) {
                        setEffect {
                            EditLocationEffect.ShowToast(
                                message = "다시 시도해 주세요",
                                type = SnackBarType.ERROR
                            )
                        }
                    }
                }
            }
        } else {
            setState { copy(errorMessage = context.getString(R.string.my_profile_location_not_selected_error_message)) }
            setEffect {
                EditLocationEffect.ShowToast(
                    context.getString(R.string.my_profile_location_not_selected_error_message),
                    SnackBarType.ERROR
                )
            }
        }
    }

    private fun selectLocation(location: Location) {
        val newLocations = uiState.selectedLocations.toMutableList()

        if (newLocations.contains(location)) {
            newLocations.remove(location)
        } else {
            newLocations.add(location)
        }

        setState { copy(selectedLocations = newLocations) }
    }

    private fun selectRegionName(regionName: String) {
        setState {
            copy(selectedRegionName = regionName)
        }

        if (regionName.isNotBlank()) setAction(EditLocationAction.GetLocations(regionName))
    }

    private fun getRegions() {
        if (uiState.locations.isNotEmpty()) return

        viewModelScope.launch {
            getRegionsUseCase.invoke().mapMerge().collect { regions ->
                if (regions != null) {
                    setState { copy(locations = regions.map { region -> EditLocation(region) }) }

                    if (uiState.locations.isNotEmpty()) setAction(
                        EditLocationAction.SelectRegionName(uiState.locations[0].regionName)
                    )
                } else if (!isLoading) {
                    setEffect {
                        EditLocationEffect.ShowToast(
                            message = context.getString(R.string.my_profile_location_fetch_data_failure_message),
                            type = SnackBarType.ERROR
                        )
                    }
                }
            }
        }
    }

    private fun getLocations(regionName: String) {
        if (uiState.locations.find { it.regionName == regionName }?.locations?.isNotEmpty() == true) return

        viewModelScope.launch {
            getLocationsUseCase.invoke(regionName).mapMerge().collect { locations ->
                if (locations != null) {
                    val updatedLocations = uiState.locations.map { location ->
                        if (location.regionName == regionName) {
                            location.copy(locations = locations)
                        } else {
                            location
                        }
                    }

                    val updatedSelectedLocation = uiState.selectedLocations.map { location ->
                        if (location.region.isBlank()) {
                            val region = locations.find { it.id == location.id }?.region ?: ""
                            location.copy(region = region)
                        } else {
                            location
                        }
                    }

                    setState {
                        copy(
                            locations = updatedLocations,
                            selectedLocations = updatedSelectedLocation
                        )
                    }
                } else if (!isLoading) {
                    setEffect {
                        EditLocationEffect.ShowToast(
                            message = context.getString(R.string.my_profile_location_fetch_data_failure_message),
                            type = SnackBarType.ERROR
                        )
                    }
                }
            }
        }
    }

}