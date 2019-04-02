package pl.edu.agh.pgedlek.ElevatorSystem

import pl.edu.agh.pgedlek.Utils.{Direction, PickupRequest, STOP}
import pl.edu.agh.pgedlek.ControlSystem.RequestQueue

class ElevatorSystem(numberOfElevators: Int, numberOfFloors: Int)
                    (implicit requestQueue: RequestQueue) extends IElevatorSystem {

  private var elevators = for (i <- 1 until numberOfElevators + 1) yield Elevator(i, 0, STOP, Set.empty)

  override def update(elevator: Elevator, destination: Int, direction: Direction): Unit = {
    val elevatorDidUpdate = elevator.update(elevator.nextFloor(destination), destination, direction)
    val elevatorMaybeUpdate = elevators.find(_.elevatorId == elevator.elevatorId)
    elevatorMaybeUpdate.foreach(updatedElevator =>
      elevators = elevators.updated(elevators.indexOf(updatedElevator), elevatorDidUpdate))
  }

  override def status(): Seq[Elevator] = {
    elevators
  }

  override def pickup(pickupRequest: PickupRequest): Unit = {
    requestQueue.enqueue(pickupRequest)
  }

  /*override def step(): Unit = {
    elevators.foreach { elevator =>
      val maybePickupRequest = requestQueue.dequeue()
      maybePickupRequest match {
        case Some(pickupRequest) =>
          update(elevator, pickupRequest.currFloor)
        case _ if elevator.goalFloorNumbers.isEmpty =>
        case _ =>
          val nextGoalFloor = elevator.goalFloorNumbers.headOption.getOrElse(elevator.floorNumber)
          update(elevator, nextGoalFloor)
      }
    }
  }*/

  override def step(): Unit = {
    if(requestQueue.queue.nonEmpty) {
      val pickupRequestMaybe = requestQueue.dequeue()
      chooseElevator(pickupRequestMaybe)
    }
    else elevators.foreach(
      elevator =>
        if (elevator.goalFloorNumbers.nonEmpty){
          val nextFloorNumber = elevator.goalFloorNumbers.headOption.getOrElse(elevator.currFloor)
          update(elevator, nextFloorNumber, elevator.direction)
        }
      )
  }

  def chooseElevator(pickupRequestMaybe: Option[PickupRequest]): Unit = {
    val emptyElevator =
      elevators
        .filter(_.goalFloorNumbers.isEmpty)
        .sortBy(_.distanceToFloor(pickupRequestMaybe.head))
        .headOption

    val movingElevator =
      elevators
        .filter(_.direction.eq(pickupRequestMaybe.head.direction))
        .sortBy(_.distanceToFloor(pickupRequestMaybe.head))
        .headOption

    val destFloor = pickupRequestMaybe.head.destFloor

    (emptyElevator, movingElevator) match {
      case (Some(emptyElevator), None) => update(emptyElevator, destFloor, emptyElevator.checkDirection(destFloor))
      case (None, Some(movingElevator)) => update(movingElevator, destFloor, movingElevator.checkDirection(destFloor))
      case (Some(emptyElevator), Some(movingElevator)) =>
        if (movingElevator.distanceToFloor(pickupRequestMaybe.head) <= emptyElevator.distanceToFloor(pickupRequestMaybe.head))
          update(movingElevator, destFloor, movingElevator.checkDirection(destFloor))
        else
          update(emptyElevator, destFloor, emptyElevator.checkDirection(destFloor))
    }

  }
}

object ElevatorSystem {
  def printElevatorStates(elevators: Seq[Elevator]): Unit = {
    println("Status:")
    elevators.foreach { elevator =>
      print(s"Elevator-${elevator.elevatorId} at [${elevator.currFloor}] floor. ")
      if (elevator.goalFloorNumbers.nonEmpty)
        println(s"Elevator is going ${elevator.direction} now.")
      else println(s"Elevator is waiting now.")
    }
  }
}