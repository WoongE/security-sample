package kim.wonung.security_sample.adapter.inbound.graphql.scalar

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class InstantCoercing : Coercing<Instant, String> {
    private val formatter = DateTimeFormatter.ISO_INSTANT

    override fun serialize(dataFetcherResult: Any): String {
        return when (dataFetcherResult) {
            is Instant -> formatter.format(dataFetcherResult)
            else -> throw CoercingSerializeException("Expected an Instant but was ${dataFetcherResult.javaClass.name}")
        }
    }

    override fun parseValue(input: Any): Instant {
        return try {
            when (input) {
                is String -> Instant.parse(input)
                else -> throw CoercingParseValueException("Expected an ISO-8601 formatted date string but was ${input.javaClass.name}")
            }
        } catch (e: DateTimeParseException) {
            throw CoercingParseValueException("Invalid ISO-8601 date format: ${e.message}", e)
        }
    }

    override fun parseLiteral(input: Any): Instant {
        return try {
            if (input is StringValue) {
                Instant.parse(input.value)
            } else {
                throw CoercingParseLiteralException("Expected an ISO-8601 formatted date string but was $input")
            }
        } catch (e: DateTimeParseException) {
            throw CoercingParseLiteralException("Invalid ISO-8601 date format: ${e.message}", e)
        }
    }
}
