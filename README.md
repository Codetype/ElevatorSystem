# Elevator System

### Installation and configuration
Project was created using sbt and it's recommended to use this tool to manage whole project. 

#### Compile, test & run instructions

to compile project type:
```
sbt compile
```

to run application type:
```
sbt run
```

to run tests type:

```
sbt test
```

### Simulation process

When you start application, you have to provide two arguments:
number of elevators in your building(in the simulation) [1-16] and number of floors.

Then simulation start and you can choose one from below option
```
pickup <floor> [-1|1] => to send pickup request
step => to execute step of simulation
status => to print elevator system status
exit => to end simulation
```

#### Application's data structures, classes and interfaces 
Elevator - describes some of the elevator feature, such a ID, current floor, direction and set of destinations.
 
ElevatorSystem - class, which extends trait IElevatorSystem to manage from 1 to 16 elevators. 
It also shows status of whole elevator system in the simulated building and of course deals with next 
steps of the simulation.

IElevatorSystem - it's a Scala trait, which provides main logic of ElevatorSystem operations 

#### Auxiliary classes:

Direction - describes direction of the elevator

PickupRequest - represents one request from prospective passenger

RequestQueue - stores all not served pickup requests

### Scheduling algorithm

Firstly, I'd implemented simple FCFS(First-Come, First-Served) algorithm, but then I figured out, 
that it's very ineffective way, especially when RequestQueue is very long.

 After short analysis of the elevator, which is located at my university, I found out simple
 algorithm improvement. The main idea is to choose one from the following:
 - empty elevator without any pickup requests
 - elevator is moving in the same direction
 
 If both type of elevators exists, we choose the nearest one.
 This algorithm is more efficient than the previous one, but still open for further improvements.

### Possible further improvements
- Further improvements of the scheduling algorithm,
- Providing feature, which deals with number of passengers,
- Possibility of the creating elevators with 'priority' floors.