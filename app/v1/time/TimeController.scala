package v1.time

import java.util.Random

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer
import akka.util.ByteString
import javax.inject.Inject
import kamon.Kamon
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

class TimeController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  private val rand = new Random()
  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  def get() = Action.async {
    Kamon.currentSpan().takeSamplingDecision()
    Thread.sleep(rand.nextInt(200).toLong)
    getNowTime.map { time =>
      Ok(Json.toJson(Map("time" -> time)))
    }
  }

  private def getNowTime = {
    val started = Kamon.timer("remoteHttpCall", "Timing of the remote worldclock invocation")
      .withTag("testApp", "true")
      .start()
    val req = HttpRequest(uri = "http://worldclockapi.com/api/json/utc/now")
    val responseFuture: Future[HttpResponse] = Http().singleRequest(req)
    responseFuture.flatMap { response =>
      response.entity.dataBytes.runFold(ByteString(""))(_ ++ _).map(_.utf8String).map { body =>
        val result = Json.parse(body)("currentDateTime").as[String]
        started.stop()
        result
      }
    }
  }
}
