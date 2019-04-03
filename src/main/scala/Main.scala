import pl.edu.agh.pgedlek.ElevatorSystem.ElevatorSystem
import pl.edu.agh.pgedlek.Utils.{Direction, PickupRequest, RequestQueue}

import scala.collection.immutable.Queue
import scala.util.{Failure, Try}

object Main {

    def main(args: Array[String]) {
        val trySimulate = Try {
            simulate
        }
        trySimulate match {
            case Failure(throwable) =>
                println(s"Failure occurred.")
                System.exit(-1);
            case _ =>
        }
    }

    private def build(numberOfElevators: Int, numberOfFloors: Int) = {
        val queue = Queue[PickupRequest]()
        implicit val requestSystem = new RequestQueue(queue)
        new ElevatorSystem(numberOfElevators, numberOfFloors)
    }

    private def simulate:Unit = {
        val sc = new java.util.Scanner(System.in)
        println("Please type number of elevators in building (max 16): ")
        val numberOfElevators = sc.nextInt()

        println("Please type number of floors in the building: ")
        val numberOfFloors = sc.nextInt()

        println("Available options: ")
        println("pickup <floor> [-1|1] => to send pickup request")
        println("step => to execute step of simulation")
        println("status => to print elevator system status")
        println("exit => to end simulation")

        val elevatorSystem = build(numberOfElevators, numberOfFloors)

        while (true) {
            val option = sc.next()

            option match {
                case "status" =>
                    ElevatorSystem.getAllElevators(elevatorSystem.status())
                case "step" => elevatorSystem.step()
                case "pickup" =>
                    val pickupFloor = sc.nextInt

                    val direction = Direction(sc.nextInt())

                    val pickupRequest = PickupRequest(pickupFloor, direction)
                    elevatorSystem.pickup(pickupRequest)
                case "exit" =>
                    System.exit(0)
                case _ =>
                    println("Invalid operation.")
            }
        }
    }

}
