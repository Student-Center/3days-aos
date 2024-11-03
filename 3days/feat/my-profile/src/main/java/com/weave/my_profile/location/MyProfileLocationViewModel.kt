package com.weave.my_profile.location

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.weave.design_system.R
import com.weave.design_system.component.SnackBarType
import com.weave.location.GetLocationRegionsUseCase
import com.weave.location.GetLocationsByRegionUseCase
import com.weave.model.domain.myprofile.Location
import com.weave.utils.base.BaseViewModel
import com.weave.utils.base.UIAction
import com.weave.utils.base.UIEffect
import com.weave.utils.base.UIIntent
import com.weave.utils.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LocationAction : UIAction {
    data object GetRegions : LocationAction()
    data class GetLocations(val regionName: String) : LocationAction()
    data class RestoreSelectedLocations(val locations: List<Location>) : LocationAction()
    data class SelectLocation(val location: Location) : LocationAction()
    data class SelectRegionName(val regionName: String) : LocationAction()
    data object ValidateInput : LocationAction()
}

sealed class LocationIntent : UIIntent {
    data object GetRegions : LocationIntent()
    data class GetLocations(val regionName: String) : LocationIntent()
    data class RestoreSelectedLocations(val locations: List<Location>) : LocationIntent()
    data class SelectLocation(val location: Location) : LocationIntent()
    data class SelectRegionName(val regionName: String) : LocationIntent()
    data object ValidateInput : LocationIntent()
}

data class LocationState(
    val errorMessage: String = "",
    val locations: List<MyProfileLocation> = listOf(),
    val selectedLocations: List<Location> = listOf(),
    var selectedRegionName: String = "",
) : UIState

sealed class LocationEffect : UIEffect {
    data object NavigateToNextScreen : LocationEffect()
    data class ShowToast(val message: String, val type: SnackBarType) : LocationEffect()
}

data class MyProfileLocation(
    val regionName: String,
    var locations: List<Location> = listOf()
)

@HiltViewModel
class MyProfileLocationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getRegionsUseCase: GetLocationRegionsUseCase,
    private val getLocationsUseCase: GetLocationsByRegionUseCase
) :
    BaseViewModel<LocationAction, LocationIntent, LocationState, LocationEffect>(initialState = LocationState()) {
    override fun actionPredicate(action: LocationAction): LocationIntent {
        return when (action) {
            is LocationAction.GetRegions -> LocationIntent.GetRegions
            is LocationAction.GetLocations -> LocationIntent.GetLocations(action.regionName)
            is LocationAction.RestoreSelectedLocations -> LocationIntent.RestoreSelectedLocations(
                action.locations
            )

            is LocationAction.SelectLocation -> LocationIntent.SelectLocation(action.location)
            is LocationAction.SelectRegionName -> LocationIntent.SelectRegionName(action.regionName)
            is LocationAction.ValidateInput -> LocationIntent.ValidateInput
        }
    }

    override fun collectIntent(intent: LocationIntent) {
        when (intent) {
            is LocationIntent.GetRegions -> getRegions()
            is LocationIntent.GetLocations -> getLocations(intent.regionName)
            is LocationIntent.RestoreSelectedLocations -> restoreSelectedLocations(intent.locations)
            is LocationIntent.SelectLocation -> selectLocation(intent.location)
            is LocationIntent.SelectRegionName -> selectRegionName(intent.regionName)
            is LocationIntent.ValidateInput -> validateInput()
        }
    }

    private fun validateInput() {
        if (uiState.selectedLocations.isNotEmpty()) {
            setEffect { LocationEffect.NavigateToNextScreen }
        } else {
            setState { copy(errorMessage = context.getString(R.string.my_profile_location_not_selected_error_message)) }
            setEffect {
                LocationEffect.ShowToast(
                    context.getString(R.string.my_profile_location_not_selected_error_message),
                    SnackBarType.ERROR
                )
            }
        }
    }

    private fun restoreSelectedLocations(locations: List<Location>) {
        setState { copy(selectedLocations = locations) }
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

        if (regionName.isNotBlank()) setAction(LocationAction.GetLocations(regionName))
    }

    private fun getRegions() {
        if (uiState.locations.isNotEmpty()) return

        viewModelScope.launch {
            getRegionsUseCase.invoke().mapMerge().collect { regions ->
                if (regions != null) {
                    setState { copy(locations = regions.map { region -> MyProfileLocation(region) }) }

                    if (uiState.locations.isNotEmpty()) setAction(
                        LocationAction.SelectRegionName(uiState.locations[0].regionName)
                    )
                } else if (!isLoading) {
                    setEffect {
                        LocationEffect.ShowToast(
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
                    val updatedLocations = uiState.locations.map { myProfileLocation ->
                        if (myProfileLocation.regionName == regionName) {
                            myProfileLocation.copy(locations = locations)
                        } else {
                            myProfileLocation
                        }
                    }

                    setState { copy(locations = updatedLocations) }
                } else if (!isLoading) {
                    setEffect {
                        LocationEffect.ShowToast(
                            message = context.getString(R.string.my_profile_location_fetch_data_failure_message),
                            type = SnackBarType.ERROR
                        )
                    }
                }
            }
        }
    }

}