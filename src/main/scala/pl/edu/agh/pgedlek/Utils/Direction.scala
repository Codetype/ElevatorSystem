package pl.edu.agh.pgedlek.Utils

sealed trait Direction

object Direction {
  def apply(direction: Int): Direction = {
    if (direction > 0) UP
    else if(direction < 0) DOWN
    else STOP
  }
}

case object UP extends Direction

case object STOP extends Direction

case object DOWN extends Direction
