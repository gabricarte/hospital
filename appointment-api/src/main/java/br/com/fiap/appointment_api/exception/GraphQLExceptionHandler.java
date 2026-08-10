package br.com.fiap.appointment_api.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(
            Throwable exception,
            DataFetchingEnvironment environment
    ) {

        if (exception instanceof ResourceNotFoundException) {
            return GraphqlErrorBuilder.newError(environment)
                    .message(exception.getMessage())
                    .errorType(graphql.ErrorType.ValidationError)
                    .build();
        }

        if (exception instanceof BusinessException) {
            return GraphqlErrorBuilder.newError(environment)
                    .message(exception.getMessage())
                    .errorType(graphql.ErrorType.ValidationError)
                    .build();
        }

        return GraphqlErrorBuilder.newError(environment)
                .message("Erro interno do servidor.")
                .errorType(graphql.ErrorType.DataFetchingException)
                .build();
    }
}