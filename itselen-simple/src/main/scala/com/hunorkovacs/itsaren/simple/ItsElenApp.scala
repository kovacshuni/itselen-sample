package com.hunorkovacs.itselen.simple

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.Eq
import cats.instances.long._
import cats.syntax.eq._
import cats.syntax.option._
import java.util.Date
import org.typelevel.log4cats._
import org.typelevel.log4cats.slf4j._

object ItsElenApp:

  private val logger: SelfAwareStructuredLogger[IO] = LoggerFactory[IO].getLogger

  def main(args: Array[String]): Unit =
    println("hi")

  implicit val dateEq: Eq[Date] =
    Eq.instance[Date] { (date1, date2) => date1.getTime == date2.getTime }

  final case class Cat(name: String, age: Int, color: String)

  val cat1 = Cat("Garfield", 38, "orange and black")
  val cat2 = Cat("Heathcliff", 33, "orange and black")

  implicit val catEq: Eq[Cat] =
    Eq.instance[Cat] { (cat1, cat2) =>
      cat1.age == cat2.age &&
      cat1.color == cat2.color &&
      cat1.name == cat2.name
    }
