package io.flaterlab.meshexam.librariy.mesh.common.dto

sealed class MeshResult<T> {

    data class Success<T>(
        val data: T,
    ) : MeshResult<T>()

    data class Error<T>(
        val cause: Throwable,
    ) : MeshResult<T>()
}