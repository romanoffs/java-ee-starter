package com.start.config;

import com.google.common.collect.Iterables;
import lombok.extern.java.Log;
import lombok.val;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Set;
import java.util.stream.Collectors;

@Provider
@Log
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException exception) {
        log.warning(exception.getMessage());
        if (exception instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) exception).getConstraintViolations();

            val errorMessages = constraintViolations.stream().collect(Collectors.toMap(
                    error -> Iterables.getLast(error.getPropertyPath()).getName(),
                    ConstraintViolation::getMessage)
            );

            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(errorMessages)
                    .build();
        }

        return Response
                .status(Response.Status.BAD_REQUEST)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity("ERROR")
                .build();
    }
}
