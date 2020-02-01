package com.start.config;

import com.start.utils.ApiResponse;
import lombok.val;

import javax.ejb.EJBAccessException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException e) {

        if (e instanceof EJBAccessException ||
                e instanceof NotAcceptableException ||
                e instanceof NotAllowedException ||
                e instanceof ForbiddenException
        ) {
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(ApiResponse.accessDenied())
                    .build();
        }

        if (e instanceof NotAuthorizedException) {
            val apiResponse = ApiResponse
                    .builder()
                    .message(e.getMessage())
                    .status(Response.Status.UNAUTHORIZED.getStatusCode())
                    .build();
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(apiResponse)
                    .build();
        }

        if (e instanceof SecurityException) {
            val apiResponse = ApiResponse.accessDenied();
            apiResponse.setMessage(e.getMessage());
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(apiResponse)
                    .build();
        }

        if (e instanceof NotFoundException) {
            val apiResponse = ApiResponse
                    .builder()
                    .message(Response.Status.NOT_FOUND.getReasonPhrase())
                    .status(Response.Status.NOT_FOUND.getStatusCode())
                    .build();
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(apiResponse)
                    .build();
        }

        val error = ApiResponse.error();
        error.setMessage(e.getMessage());
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(error)
                .build();
    }
}
