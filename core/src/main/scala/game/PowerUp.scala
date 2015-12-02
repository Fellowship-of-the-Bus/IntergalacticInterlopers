package com.github.fellowship_of_the_bus
package interlopers
package game

object PowerUpImplicits {
  implicit object PowerUpID extends IDFactory[PowerUpID] {
    val ids = Vector(PowerHP, PowerShots)
  }
}
import PowerUpImplicits._

case class PowerUpAttributes()

trait PowerUpID extends ID
case object PowerHP extends PowerUpID
case object PowerShots extends PowerUpID
case object powerups extends IDMap[PowerUpID, PowerUpAttributes]("powerups.json")

