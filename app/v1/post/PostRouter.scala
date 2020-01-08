package v1.post

import javax.inject.Inject
import kamon.Kamon
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

/**
  * Routes and URLs to the PostResource controller.
  */
class PostRouter @Inject()(controller: PostController) extends SimpleRouter {
  val prefix = "/v1/posts"

  def link(id: PostId): String = {
    import io.lemonlabs.uri.dsl._
    val url = prefix / id.toString
    url.toString()
  }

  override def routes: Routes = {
    case GET(p"/") =>
      Kamon.currentSpan().takeSamplingDecision() // TODO remove when Kamon supports String Interpolating Routing DSL (sird)
      controller.index

    case POST(p"/") =>
      Kamon.currentSpan().takeSamplingDecision() // TODO remove when Kamon supports String Interpolating Routing DSL (sird)
      controller.process

    case GET(p"/$id") =>
      Kamon.currentSpan().takeSamplingDecision() // TODO remove when Kamon supports String Interpolating Routing DSL (sird)
      controller.show(id)
  }

}
