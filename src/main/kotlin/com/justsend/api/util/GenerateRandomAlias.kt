package com.justsend.api.util

import net.datafaker.Faker

private val faker = Faker()

fun generateAlias(): String {
  val color = faker.color().name().lowercase()
  val animal = faker.animal().name().lowercase()
  val number = (100..999).random()
  return "$color-$animal-$number"
}
