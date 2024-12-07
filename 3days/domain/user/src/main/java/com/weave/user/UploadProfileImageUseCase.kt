package com.weave.user

import com.weave.domain.repository.UserRepository
import com.weave.model.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class UploadProfileImageUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(file: File): Flow<NetworkResult<Unit>> = flow {
        repository.getProfileImageUploadUrl()
            .collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val (imageId, uploadUrl) = result.data

                        repository.uploadProfileImage(uploadUrl, file)
                            .collect { uploadResult ->
                                if (uploadResult is NetworkResult.Success) {
                                    repository.completeProfileImageUpload(imageId)
                                        .collect { emit(it) }
                                } else {
                                    emit(uploadResult)
                                }
                            }
                    }

                    is NetworkResult.Error -> emit(result)
                    is NetworkResult.Loading -> emit(result)
                }
            }
    }
}