package com.github.fellowship_of_the_bus
package interlopers
package game

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

trait EnemyID extends ID
case object Drone extends EnemyID
case object Fighter extends EnemyID
case object CosmicBee extends EnemyID
case object SpaceTurtle extends EnemyID
case object GalacticDragon extends EnemyID
case object CyberSalmon extends EnemyID

object enemies extends IDMap[EnemyID, EnemyAttributes]("data/enemies.json")

class Enemy(xc: Float, yc: Float, val id: EnemyID) extends GameObject(xc, yc) {
  type IDKind = EnemyID
  val attributes = enemies(id)

  private var hp = attributes.maxHp

  def width = attributes.width
  def height = attributes.height

  // multiply in random value
  val velocity = (attributes.speed.x, attributes.speed.y)
}


