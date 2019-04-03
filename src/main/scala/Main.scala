import java.util.InputMismatchException

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
                throwable.fillInStackTrace()
                simulate

            case _ =>
        }
    }

    private def build(numberOfElevators: Int, numberOfFloors: Int) = {
        val queue = Queue[PickupRequest]()
        implicit val requestQueue = new RequestQueue(queue)
        new ElevatorSystem(numberOfElevators, numberOfFloors)
    }

    private def simulate  = {
        val sc = new java.util.Scanner(System.in)
        println("Please type number of elevators in building (max 16): ")
        var numberOfElevators = 0
        var numberOfFloors = 0
        try{
            numberOfElevators = sc.nextInt()
            while(numberOfElevators > 16){
                println("I'm sorry, maximum number of elevators is 16")
                numberOfElevators = sc.nextInt()
            }

            println("Please type number of floors in the building: ")
            numberOfFloors = sc.nextInt()
        }
        catch {
            case input: InputMismatchException => handleWrongInput(input)
        }
        println("Available options: ")
        println("pickup <floor> [-1|1] => to send pickup request UP | DOWN")
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
                    val direction = sc.nextInt()
                    if (!pickupFloor.isValidInt || !direction.isValidInt) {
                        println("command pickup requires two integer arguments: <floor number> <direction>")
                    } else {
                        if (pickupFloor <= numberOfFloors) {
                            val pickupRequest = PickupRequest(pickupFloor, Direction(direction))
                            elevatorSystem.pickup(pickupRequest)
                        }
                        else println(s"I'm sorry, but building has only $numberOfFloors floors")
                    }
                case "exit" =>
                    System.exit(0)
                case _ =>
                    println("Invalid operation.")
            }
        }
    }

    def handleWrongInput(value: Any): Unit = {
        System.err.println("Permitted input value, which causes restart of the simulation")
        simulate
    }

}
