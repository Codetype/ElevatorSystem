package pl.edu.agh.pgedlek.ElevatorSystem

import org.scalatest.WordSpec
import org.scalatest.Matchers
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import org.scalatest.concurrent.Eventually
import pl.edu.agh.pgedlek.Utils.{DOWN, UP}

class ElevatorTest extends WordSpec with MockitoSugar with Eventually with Matchers{
  "Elevator all methods" when {

    "test the status method" should {
      "check status of the elevator" in {
        val mockElevator = mock[Elevator]
        when(mockElevator.status()).thenReturn(mockElevator)

        mockElevator.status() should be (mockElevator)
      }
    }

    "test the update method" should {
      "update elevator with some data" in {
        val elevator = Elevator(9, 11, UP, Set.empty)
        val updatedElevator = elevator.update(elevator.nextFloor(20), 20, UP)

        updatedElevator.currFloor should be(12)
      }
    }

    "test the nextFloor method" should {
      "which floor is next one" in {
        val elevator = Elevator(1, 14, DOWN, Set.empty)
        val nextLevel = elevator.nextFloor(1)

        nextLevel should be(13)
      }
    }

    "test the checkDirection method" should {
      "check current direction of the elevator" in {
        val elevator = Elevator(1, 12, DOWN, Set.empty)
        val newDirection = elevator.checkDirection(14)

        newDirection should be(UP)
      }
    }
  }

  /*
val pickupRequest = PickupRequest(10, DOWN)

val mockQueue = mock[Queue[PickupRequest]]
when(mockQueue.enqueue(pickupRequest)).thenReturn(mockQueue)

val pickupRequestQueue = new RequestQueue(mockQueue)
pickupRequestQueue.enqueue(pickupRequest)

verify(mockQueue).enqueue(pickupRequest)

mockQueue.dequeueOption should be(None)
 */
}
