package com.github.fellowship_of_the_bus
package interlopers
package game

trait PlayerID extends ID
case object Player1 extends PlayerID
case object Player2 extends PlayerID

class Player(xc: Float, yc: Float) extends GameObject(xc, yc) {
  type IDKind = PlayerID
  def id = Player1   // TODO: generalize

  def height: Float = 40f
  def width: Float = 40f

  def move(dx: Float, dy: Float) = {
    x = x + dx
    y = y + dy   
  }

  val velocity = (0f, 0f)
}


