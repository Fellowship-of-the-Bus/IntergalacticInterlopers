package com.github.fellowship_of_the_bus
package interlopers
package game

import lib.game.{IDMap,IDFactory}

object EnemyImplicits {
  implicit object EnemyID extends IDFactory[EnemyID] {
    val ids = Vector(Drone, Fighter, CosmicBee, SpaceTurtle, GalacticDragon, CyberSalmon)
  }
}
import EnemyImplicits._
import ProjectileImplicits.extractor

case class Speed(x: Float, y: Float)

case class EnemyAttributes(
  maxHp: Int, difficulty: Int,
  shotType: ProjectileID, shotInterval: Int, numShot: Int, shotDelay: Int,
  speed: Speed,
  width: Float, height: Float)

sealed trait EnemyID extends ID
final case object Drone extends EnemyID
final case object Fighter extends EnemyID
final case object CosmicBee extends EnemyID
final case object SpaceTurtle extends EnemyID
final case object GalacticDragon extends EnemyID
final case object CyberSalmon extends EnemyID

object enemies extends IDMap[EnemyID, EnemyAttributes]("data/enemies.json")

class Enemy(val id: EnemyID, xc: Float, yc: Float) extends GameObject(xc, yc) {
  type IDKind = EnemyID
  val attributes = enemies(id)

  private var hp = attributes.maxHp

  def width = attributes.width
  def height = attributes.height

  // multiply in random value
  val velocity = (attributes.speed.x, attributes.speed.y)

  val difficulty = attributes.difficulty
}

object Enemy {
  def apply() = {
    val id = enemies.random
    val x = 0  // TODO: randomize
    val y = 0
    new Enemy(id, x, y)
  }
}
