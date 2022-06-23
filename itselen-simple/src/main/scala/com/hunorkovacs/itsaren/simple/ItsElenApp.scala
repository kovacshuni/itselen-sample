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
    val circles: List[Circle]     = List(Circle(1.0d), Circle(2.0d))
    val shapes: List[Shape]       = circles
    val circleOpt: Option[Circle] = Option(Circle(3.0d))
    val shapeOpt: Option[Shape]   = circleOpt
    val circleMon: MyMon[Circle]  = MyMon(Circle(4.0d))
    val shapeMon: MyMon[Shape]    = MyMon(Circle(4.0d))
    val mycvShape: MyCv[Shape]    = new MyCv[Shape] {
      override def write(s: Shape): String = s.name
    }
    val mycvCircle: MyCv[Circle]  = mycvShape

    println(mycvCircle.write(Circle(4.0d)))
    println(format(Circle(4.0d), mycvCircle))
    println(format(Circle(4.0d), mycvShape))
    // println(format(Circle(4.0D).asInstanceOf[Shape], mycvCircle)) // can't do this
    // MyCv[Shape] is a subtype of MyCv[Circle] because Circle is a subtype of Shape

    val myIvCircle = new MyIv[Circle] {
      override def ivWrite(c: Circle): String = c.name
    }
    val myIvShape  = new MyIv[Shape] {
      override def ivWrite(s: Shape): String = s.name
    }
    println(ivFormat(Circle(4.0d), myIvCircle))
    println(ivFormat(Circle(4.0d).asInstanceOf[Shape], myIvShape))
    println(ivFormat(Circle(4.0d), myIvShape))
    // println(ivFormat(Circle(4.0d).asInstanceOf[Shape], myIvCircle)) //nothing converts to nothing


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

  sealed trait Shape {
    def name: String
  }

  case class Circle(radius: Double) extends Shape {
    override def name: String = s"Circle with radius=$radius"
  }

  case class MyMon[+A](a: A) // covariance

  trait MyCv[-A] { // contravariance
    def write(a: A): String
  }

  class ShapeWriter extends MyCv[Shape] {
    override def write(shape: Shape): String = s"Shape ${shape.name}"
  }

  def format[A](a: A, aCv: MyCv[A]): String = aCv.write(a)

  trait MyIv[A] {
    def ivWrite(a: A): String
  }

  def ivFormat[A](a: A, aIv: MyIv[A]): String = aIv.ivWrite(a)
