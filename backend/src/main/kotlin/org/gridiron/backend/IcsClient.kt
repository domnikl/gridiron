package org.gridiron.backend

import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.component.VEvent
import org.joda.time.DateTime
import java.io.InputStream
import java.net.URL

class IcsClient(
    private val ics: List<URL>,
    private val httpClient: HttpClient
) {
    suspend fun games(): List<Game> {
        return ics.map {
            val response = httpClient.get<HttpResponse>(it)

            require(response.status == HttpStatusCode.OK) { "Status code is ${response.status}" }

            val inputStream = response.receive<InputStream>()
            val builder = CalendarBuilder()
            val calendar = builder.build(inputStream)

            calendar.getComponents<VEvent>("VEVENT").map { event ->
                require(event.startDate.isUtc)

                val (away, home) = event.summary.value.split(" at ")

                Game(
                    DateTime(event.startDate.date),
                    away,
                    home
                )
            }
        }.flatten()
    }

    data class Game(val start: DateTime, val away: String, val home: String)
}
