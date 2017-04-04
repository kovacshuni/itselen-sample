package com.hunorkovacs.itselen.simple

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.typesafe.config.ConfigFactory
import io.circe.config.parser
import org.typelevel.log4cats._
import org.typelevel.log4cats.slf4j._
import io.circe.syntax._

object ItsElenApp:

  private val logger: SelfAwareStructuredLogger[IO] = LoggerFactory[IO].getLogger

  def main(args: Array[String]): Unit =
    val ops = for
      config <- IO(ConfigFactory.load()).flatMap(typesafeConfig => parser.decodePathF[IO, ItsElenConfig](typesafeConfig, "itselen-sample"))
      _      <- logger.info(s"configuration: ${config.asJson.spaces2}")
      _      <- IO(println("hi"))
    yield ()
    ops.unsafeRunSync()
