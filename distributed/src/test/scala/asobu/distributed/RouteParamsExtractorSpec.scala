package asobu.distributed

import asobu.dsl.ExtractResult
import asobu.dsl.util.RecordOps.FieldKVs
import cats.sequence.Traverser
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.core.routing.RouteParams
import shapeless.Witness
import shapeless.record._
import RouteParamsExtractor._
import asobu.dsl.CatsInstances._

class RouteParamsExtractorSpec extends Specification {
  "generates from Record T" >> { implicit ex: ExecutionEnv ⇒
    type Rec = Record.`'x -> Int, 'y -> String, 'z -> Boolean`.T

    val rpe = RouteParamsExtractor[Rec]
    val result = rpe.run(RouteParams(Map("x" → Right("3")), Map.empty))
    result.isLeft must beTrue.await

    val result2 = rpe.run(RouteParams(Map("x" → Right("3"), "y" → Right("a"), "z" → Right("true")), Map.empty))
    result2.getOrElse(null) must be_==(Record(x = 3, y = "a", z = true)).await
  }
}