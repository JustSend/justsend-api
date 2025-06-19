package com.justsend.api.external

import com.justsend.api.dto.DebinRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class ExternalApiClient(
  @Value("\${external.api.url}") private val baseUrl: String
) {
  private val client: WebClient = WebClient.builder()
    .baseUrl(baseUrl)
    .build()

  fun validate(req: DebinRequest): ValidationResponse {
    return client.post()
      .uri("/debin")
      .bodyValue(req)
      .retrieve()
      .bodyToMono(ValidationResponse::class.java)
      .block()!!
  }
}

data class ValidationResponse(
  val valid: Boolean,
  val status: String,
  val message: String
)
