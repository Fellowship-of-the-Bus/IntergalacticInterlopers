package com.github.fellowship_of_the_bus
package interlopers
package game

import lib.game.CenteredCoordinates

abstract class GameObject(var x: Float, var y: Float) extends CenteredCoordinates {
  type IDKind <: ID
  def id: IDKind

  // generic
  private var isActive = true
  def active = isActive
  def inactivate() = isActive = false

  def velocity: (Float, Float)

  def move() = {
    val (dx, dy) = velocity
    x = x + dx
    x = y + dy
  }
}
