package pl.edu.agh.pgedlek.ElevatorSystem

import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.concurrent.Eventually
import org.scalatest.mockito.MockitoSugar
import pl.edu.agh.pgedlek.Utils.{DOWN, PickupRequest, RequestQueue, UP}

import scala.collection.immutable.Queue

class ElevatorSystemTest extends WordSpec with MockitoSugar with Eventually with Matchers with GeneratorDrivenPropertyChecks {
  override implicit val generatorDrivenConfig: PropertyCheckConfiguration =
    PropertyCheckConfiguration(minSuccessful = 2, minSize = 1, sizeRange = 10, maxDiscardedFactor = 100d)

  private val requestGen = for {
    from <- Gen.choose(0, 20)
    direction <- Gen.oneOf(UP, DOWN)
  } yield PickupRequest(from, direction)

  private val requestsGen = Gen.listOf(
    Gen.nonEmptyListOf(requestGen)
      .flatMap(req => Gen.choose(0, 10).map((req, _)))
  )

  "ElevatorSystem simulation test" when {
      val queue = Queue[PickupRequest]()
      implicit val requestQueue = new RequestQueue(queue)
      val elevatorSystem = new ElevatorSystem(16, 100)

      val requests = requestsGen.sample.head

      requests.foreach(
        request =>
          request._1.foreach(
            req =>
            elevatorSystem.pickup(req)
          )
      )

      var it = 2*requests.head._1.size

      while(it > 0){
        elevatorSystem.step()
        it -= 1
      }

      ElevatorSystem.getAllElevators(elevatorSystem.status())
  }

}
