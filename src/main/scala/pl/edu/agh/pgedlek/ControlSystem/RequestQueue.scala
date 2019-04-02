package pl.edu.agh.pgedlek.ControlSystem

import pl.edu.agh.pgedlek.Utils.{PickupRequest}

import scala.collection.immutable.Queue

class RequestQueue(var queue: Queue[PickupRequest]) {

  def enqueue(pickupRequest: PickupRequest): Unit = {
    queue = queue.enqueue(pickupRequest)
  }

  def dequeue(): Option[PickupRequest] = {
    val maybePickupRequest = queue.dequeueOption
    maybePickupRequest match {
      case Some((pickupRequest, updatedQueue)) =>
        queue = updatedQueue
        Some(pickupRequest)
      case _ => None
    }
  }

  def getQueue(): Unit = {
    queue.foreach(item =>
      println(item)
    )
  }
}