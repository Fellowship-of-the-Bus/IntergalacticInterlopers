package com.github.fellowship_of_the_bus
package interlopers
package game

trait PlayerID extends ID
case object Player1 extends PlayerID
case object Player2 extends PlayerID

class Player(xc: Float, yc: Float) extends GameObject(xc, yc) with Shooter {
  type IDKind = PlayerID
  def id = Player1   // TODO: generalize

  def height: Float = 40f
  def width: Float = 40f

  var numShot = 1
  val shotDelay = 4
  val shotInterval = 20
  val shotType = PBullet

  def move(dx: Float, dy: Float) = {
    x = x + dx
    y = y + dy   
  }

  val velocity = (0f, 0f)

}


