package v1.time

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer
import akka.util.ByteString
import javax.inject.Inject
import kamon.Kamon
import play.api.Logger
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}

case class TimeResponse( time: String)

class RemoteTimeClient @Inject()(implicit ec: ExecutionContext) {

  private val logger = Logger(getClass)
  implicit val timeResponseReads = Json.reads[TimeResponse]
  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  def fetch: Future[String] = {
    logger.info(">>> Here we go fetching the remote time!")

    val started = Kamon.timer("localHttpCall", "Timing of the local remote invocation")
      .withTag("testApp", "true")
      .start()

    val req = HttpRequest(uri = "http://localhost:9000/v1/time")
    val responseFuture: Future[HttpResponse] = Http().singleRequest(req)
    responseFuture.flatMap { response =>
      response.entity.dataBytes.runFold(ByteString(""))(_ ++ _).map(_.utf8String).map { body =>
        val result = Json.parse(body)("time").as[String]
        started.stop()
        result
      }
    }
  }
}

