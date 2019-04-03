package pl.edu.agh.pgedlek.ElevatorSystem

import pl.edu.agh.pgedlek.Utils.{Direction, STOP, DOWN, UP, PickupRequest}

case class Elevator(elevatorId: Int, currFloor: Int, direction: Direction, goalFloorNumbers: Set[Int])  {

  def status(): Elevator = this

  def update(newFloorNumber: Int, goalFloor: Int, newDirection: Direction): Elevator = {
    val filteredGoalFloorNumbers = goalFloorNumbers
        .filterNot(floorNumber => floorNumber == newFloorNumber)
    val updatedGoalFloorNumbers = filteredGoalFloorNumbers.+(goalFloor)

    Elevator(elevatorId, newFloorNumber, newDirection, updatedGoalFloorNumbers)
  }

  def nextFloor(goalFloor: Int): Int = {
    checkDirection(goalFloor) match {
      case DOWN =>
        currFloor - 1
      case UP =>
        currFloor + 1
      case _ =>
        currFloor
    }
  }

  def checkDirection(goalFloor: Int) : Direction = {
    goalFloor - currFloor match {
      case diff if diff < 0 =>
        DOWN
      case diff if diff > 0 =>
        UP
      case _ =>
        STOP
    }
  }

  def distanceToFloor(requestFloor: PickupRequest) : Int = {
    Math.abs(requestFloor.destFloor - currFloor)
  }

  def isTheSameDirection(requestFloor: PickupRequest) : Boolean = {
    checkDirection(requestFloor.destFloor) == direction
  }
}


