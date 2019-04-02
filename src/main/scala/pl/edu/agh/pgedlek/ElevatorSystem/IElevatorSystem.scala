package pl.edu.agh.pgedlek.ElevatorSystem

import pl.edu.agh.pgedlek.Utils.{Direction, PickupRequest}

trait IElevatorSystem {

  def status(): Seq[Elevator]

  def update(elevator: Elevator, destination: Int, direction: Direction): Unit

  def pickup(pickupRequest: PickupRequest): Unit

  def step(): Unit

}
