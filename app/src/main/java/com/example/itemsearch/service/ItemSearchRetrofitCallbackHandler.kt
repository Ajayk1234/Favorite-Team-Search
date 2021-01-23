package com.example.itemsearch.service

import com.example.itemsearch.*
import com.example.itemsearch.models.ServiceException
import com.example.itemsearch.models.ServiceResult
import com.example.itemsearch.utils.StringWrapper
import com.example.itemsearch.utils.asSingleString
import retrofit2.Response

object ItemSearchRetrofitCallbackHandler {
    /**
     * Function to invoke service [call], catch exceptions like IOExceptions to be logged
     * when the service call return, check the meta data for response code
     * any exceptions occur or the service call does not return successful code or response data,
     * would return default error message
     * else will return the response data
     *
     * @param call retrofit suspend function to be invoked with return type of [Response] with metadata
     *
     * @return generic message or data of Type of [T]
     */
    suspend fun <T> processCall(
        call: suspend () -> Response<T>
    ): ServiceResult<T> {
        val genericMessage = R.string.generic_service_error_message.asSingleString()
        return try {

            val serviceCallback = call()
            val body = serviceCallback.body()
            if (serviceCallback.isSuccessful && body != null) {
                ServiceResult.Success(body)
            } else {
                processGenericExceptionMessage(
                    serviceCallback.message().orEmpty(),
                    serviceCallback.code().toString()
                )
                getGenericServiceError(
                    "${serviceCallback.code()}",
                    genericMessage
                )
            }

        } catch (exception: Exception) {
            when (exception) {
                is NoConnectivityException -> {
                    processGenericExceptionMessage(
                        "No Connectivity",
                        null
                    )
                    getConnectivityServiceError()
                }
                else -> {
                    processGenericExceptionMessage(
                        exception.localizedMessage ?: "",
                        null
                    )
                    getGenericServiceError(
                        null,
                        genericMessage
                    )
                }
            }

        }
    }

    private fun getConnectivityServiceError(): ServiceResult.Error {
        val error = R.string.generic_connectivity_error_message.asSingleString()
        return ServiceResult.Error(
            ServiceException(
                null,
                error
            )
        )
    }

    private fun getGenericServiceError(
        errorCode: String?,
        errorMessage: StringWrapper
    ): ServiceResult.Error {
        return ServiceResult.Error(
            ServiceException(
                errorCode,
                errorMessage
            )
        )
    }

    internal fun processGenericExceptionMessage(message: String, code: String?) {
        val codeMessage = if (code == null) "" else "with code: $code; "
    }

}
