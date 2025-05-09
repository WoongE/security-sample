package kim.wonung.security_sample.adapter.inbound.graphql.exception

import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.graphql.execution.ErrorType
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component

@Component
class GraphQLExceptionHandler : DataFetcherExceptionResolverAdapter() {
    
    override fun resolveToSingleError(ex: Throwable, env: DataFetchingEnvironment): GraphQLError? {
        return when (ex) {
            is IllegalArgumentException -> toGraphQLError(ex, ErrorType.BAD_REQUEST, env)
            is AccessDeniedException -> toGraphQLError(ex, ErrorType.FORBIDDEN, env)
            else -> toGraphQLError(ex, ErrorType.INTERNAL_ERROR, env)
        }
    }

    private fun toGraphQLError(
        exception: Throwable,
        errorType: ErrorType,
        env: DataFetchingEnvironment
    ): GraphQLError {
        return GraphqlErrorBuilder.newError()
            .message(exception.message ?: "An error occurred")
            .errorType(errorType)
            .path(env.executionStepInfo.path)
            .location(env.field.sourceLocation)
            .build()
    }
}
