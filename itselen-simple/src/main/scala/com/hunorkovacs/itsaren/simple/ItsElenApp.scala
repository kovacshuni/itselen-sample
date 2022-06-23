package com.hunorkovacs.itselen.simple

import scala.collection.immutable.Set
import cats.Monoid
import cats.instances.string._
import cats.instances.int._
import cats.instances.option._
import cats.instances.either._
import cats.syntax.semigroup._

object ItsElenApp:

  def main(args: Array[String]): Unit =
    implicit def setMonoid[A]: MyMonoid[Set[A]] = setsUnionMonoid[A]
    println(MyMonoid[Set[Int]].combine(Set(1, 2), Set(3, 4)))
    println(Monoid[String].combine("Hi ", "there"))
    println(Monoid[Int].combine(32, 10))
    println(Monoid[Option[Int]].combine(Option(1), Option(2)))
    println(Right(100).asInstanceOf[Either[Any, Int]] |+| Right(100).asInstanceOf[Either[Any, Int]] |+| Monoid[Either[Any, Int]].empty)
    println(add(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)))
    println(add(List(Option(1), Option(2), Option(3))))

    implicit val implOrderMonoid = orderMonoid
    println(add(List(Order(49.12, 32), Order(39.11, 1))))


  def add[A: Monoid](items: List[A]): A =
    items.fold(Monoid[A].empty)(Monoid[A].combine)

case class Order(totalCost: Double, quantity: Double)

trait MySemigroup[A] {
  def combine(x: A, y: A): A
}

trait MyMonoid[A] extends MySemigroup[A]:
  def empty: A

def associativeLaw[A](x: A, y: A, z: A)(implicit m: MySemigroup[A]): Boolean =
  m.combine(x, m.combine(y, z)) == m.combine(m.combine(x, y), z)

def identityLaw[A](x: A)(implicit m: MyMonoid[A]): Boolean =
  m.combine(x, m.empty) == x && m.combine(m.empty, x) == x

object MyMonoid:
  def apply[A](implicit monoid: MyMonoid[A]) = monoid

class BooleanAndMonoid extends MyMonoid[Boolean]:
  override def combine(x: Boolean, y: Boolean): Boolean = x && y

  override def empty: Boolean = true

class BooleanOrMonoid extends MyMonoid[Boolean]:
  override def combine(x: Boolean, y: Boolean): Boolean = x || y

  override def empty: Boolean = false

class BooleanEitherMonoid extends MyMonoid[Boolean]:
  override def combine(x: Boolean, y: Boolean): Boolean = (x && !y) || (!x && y)

  override def empty: Boolean = false

class BooleanXNorMonoid extends MyMonoid[Boolean]:
  override def combine(x: Boolean, y: Boolean): Boolean = (!x || y) && (x || !y)

  override def empty: Boolean = true

def setsUnionMonoid[A]: MyMonoid[Set[A]] = new MyMonoid[Set[A]]:
  override def combine(a: Set[A], b: Set[A]): Set[A] = a.union(b)

  override def empty: Set[A] = Set.empty

def setsIntersectionSemigroup[A]: MySemigroup[Set[A]] = new MySemigroup[Set[A]]:
  override def combine(a: Set[A], b: Set[A]): Set[A] = a.intersect(b)

def symDiffMonoid[A]: MyMonoid[Set[A]] = new MyMonoid[Set[A]]:
  override def combine(a: Set[A], b: Set[A]): Set[A] =
    (a diff b).union(b diff a)

  override def empty: Set[A] = Set.empty

def orderMonoid: Monoid[Order] = new Monoid[Order]:
  override def combine(x: Order, y: Order): Order = Order(x.totalCost + y.totalCost, x.quantity + y.quantity)

  override def empty: Order = Order(0.0D, 0.0D)
