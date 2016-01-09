package com.github.fellowship_of_the_bus
package interlopers
package game

import lib.game.{IDMap,IDFactory}

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

class PowerUp(xc: Float, yc: Float) extends GameObject(xc, yc) {
  type IDKind = PowerUpID

  def id = PowerShots  // TODO: generalize

  def height = 30f
  def width = 30f

  // TODO: randomize direction
  val velocity = (2f, 0f)
}

object PowerUp {
  // TODO: randomize position
  def apply() = new PowerUp(0, 0)
}

