package com.sunnysyed.avant.api;

import com.sunnysyed.avant.api.model.APIError;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by SuNnY on 4/22/16.
 */
public class ErrorUtils {
    /**
     * Create a parsable way to load the specific error message from the api
     * @param response
     * @return APIError containing error message
     */
    public static APIError parseError(Response<?> response) {
        Converter<ResponseBody, APIError> converter =
                AvantApi.getRetrofit().responseBodyConverter(APIError.class, new Annotation[0]);
        APIError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new APIError();
        }
        return error;
    }
}