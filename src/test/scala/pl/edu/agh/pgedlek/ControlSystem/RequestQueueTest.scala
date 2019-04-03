package pl.edu.agh.pgedlek.ControlSystem

import org.scalatest.WordSpec
import org.scalatest.Matchers
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import org.scalatest.concurrent.Eventually
import pl.edu.agh.pgedlek.Utils.{DOWN, PickupRequest, RequestQueue}

import scala.collection.immutable.Queue

class RequestQueueTest extends WordSpec with MockitoSugar with Eventually with Matchers {
  "RequestQueue dequeue/enqueue methods" when {
    "test the enqueue method" should {
      "enqueue new element in the mock queue" in {
        val pickupRequest = PickupRequest(10, DOWN)

        val mockQueue = mock[Queue[PickupRequest]]
        when(mockQueue.enqueue(pickupRequest)).thenReturn(mockQueue)

        val pickupRequestQueue = new RequestQueue(mockQueue)
        pickupRequestQueue.enqueue(pickupRequest)

        verify(mockQueue).enqueue(pickupRequest)
      }
    }

    "test the dequeue method" should {
      "dequeue empty mock queue" in {
        val mockQueue = mock[Queue[PickupRequest]]

        when(mockQueue.dequeueOption).thenReturn(None)

        mockQueue.dequeueOption should be(None)
      }
    }

    "test the dequeue method" should {
      "dequeue mock queue with some elements" in {
        val pickupRequest = PickupRequest(10, DOWN)

        val mockQueue = mock[Queue[PickupRequest]]
        val mockQueueWithElement = mock[Queue[PickupRequest]]
        when(mockQueue.enqueue(pickupRequest)).thenReturn(mockQueueWithElement)

        when(mockQueue.dequeueOption).thenReturn(Some(pickupRequest, mockQueue))

        mockQueue.dequeueOption should be(Some(pickupRequest, mockQueue))
      }
    }
  }
}


