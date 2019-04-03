package pl.edu.agh.pgedlek.ElevatorSystem

import pl.edu.agh.pgedlek.Utils.{Direction, PickupRequest, RequestQueue, STOP}

object ElevatorSystem {
  def getAllElevators(elevators: Seq[Elevator]): Unit = {
    println("Status:")
    elevators.foreach { elevator =>
      print(s"Elevator-${elevator.elevatorId} at [${elevator.currFloor}] floor. ")
      if (elevator.goalFloorNumbers.nonEmpty)
        println(s"Elevator is going ${elevator.direction} now.")
      else println(s"Elevator is waiting now.")
    }
  }
}

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
  
  override def step(): Unit = {
    if(requestQueue.queue.nonEmpty) {
      val pickupRequestMaybe = requestQueue.dequeue()
      update(chooseElevator(pickupRequestMaybe), pickupRequestMaybe.head.destFloor, pickupRequestMaybe.head.direction )
    }
    else elevators.foreach(
      elevator =>
        if (elevator.goalFloorNumbers.nonEmpty){
          val nextFloorNumber = elevator.goalFloorNumbers.headOption.getOrElse(elevator.currFloor)
          update(elevator, nextFloorNumber, elevator.direction)
        }
      )
  }

  def chooseElevator(pickupRequestMaybe: Option[PickupRequest]): Elevator = {
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
    val emptyIdent = Elevator

    if(movingElevator.isDefined && emptyElevator.isDefined)
      if (movingElevator.head.distanceToFloor(pickupRequestMaybe.head) <= emptyElevator.head.distanceToFloor(pickupRequestMaybe.head))
        movingElevator.head
      else
        emptyElevator.head
    else if (movingElevator.isEmpty && emptyElevator.isDefined)
      emptyElevator.head
    else if (movingElevator.isDefined && emptyElevator.isEmpty)
      movingElevator.head
    else
      elevators.head
  }
}