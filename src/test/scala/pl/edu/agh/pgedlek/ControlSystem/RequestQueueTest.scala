package pl.edu.agh.pgedlek.ControlSystem

import org.scalatest.WordSpec
import org.scalatest.Matchers
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import org.scalatest.concurrent.Eventually

import pl.edu.agh.pgedlek.Utils.{PickupRequest, UP}

import scala.collection.immutable.Queue

class RequestQueueTest extends WordSpec with MockitoSugar with Eventually with Matchers {
  "RequestQueue" when {
    "calling the enqueue method" should {
      "will enqueue the pickupRequest in the queue" in {
        val mockQueue = mock[Queue[PickupRequest]]
        val pickupRequest = PickupRequest(10, UP)
        when(mockQueue.enqueue(pickupRequest)).thenReturn(mockQueue)
        val pickupRequestQueue = new RequestQueue(mockQueue)

        pickupRequestQueue.enqueue(pickupRequest)

        verify(mockQueue).enqueue(pickupRequest)
      }
    }
  }
}


