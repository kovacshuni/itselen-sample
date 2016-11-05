package com.hunorkovacs.itsmycrib

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{MethodRejection, RejectionHandler}
import org.slf4j.LoggerFactory
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import scala.collection.mutable
import scala.io.StdIn

object ItsMyCrib extends App {

  private val logger = LoggerFactory.getLogger(getClass)
  implicit private val sys = ActorSystem("itsmycrib")
  implicit private val mat = ActorMaterializer()
  import sys.dispatcher

  private val cribs = mutable.Map[String, CribPost]()

  import CribPostFormat.cribPostFormat
  val route =
    handleRejections(myRejectionHandler) {
      path("crib") {
        post {
          entity(as[CribPost]) { cribPost =>
            complete {
              val id = java.util.UUID.randomUUID.toString
              cribs.+=((id, cribPost))
              HttpResponse(status = Created, entity = id)
            }
          }
        }
      } ~
      path("crib" / Remaining ) { id =>
        get {
          complete {
            cribs.get(id) match {
              case Some(c) => c
              case None => NotFound
            }
          }
        }
      }
    }

  private def myRejectionHandler =
    RejectionHandler.newBuilder()
      .handleAll[MethodRejection] { methodRejections ⇒
      val names = methodRejections.map(_.supported.name)
      complete(HttpResponse(status = MethodNotAllowed, entity = s"Can't do that! Supported: ${names mkString " or "}!"))
    }
      .handleNotFound {
        complete(HttpResponse(status = NotFound, entity = "Not here!"))
      }
      .result()

  private val httpBindingF = Http().bindAndHandle(route, "localhost", 8080)
  if (logger.isInfoEnabled)
    logger.info("Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine()
  httpBindingF
    .flatMap(_.unbind())
    .onComplete(_ ⇒ sys.terminate())

  import spray.json.{DefaultJsonProtocol, RootJsonFormat}
  import spray.json.DefaultJsonProtocol._

  case class CribPost(address: String, phone: String)

  object CribPostFormat {

    implicit val cribPostFormat: RootJsonFormat[CribPost] = DefaultJsonProtocol.jsonFormat2(CribPost)
  }
}
