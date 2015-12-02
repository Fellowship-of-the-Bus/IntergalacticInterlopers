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

case class EnemyAttributes(
  maxHp: Int, difficulty: Int, 
  shotType: ProjectileID, shotInterval: Int, numShot: Int, shotDelay: Int)

trait EnemyID extends ID
case object Drone extends EnemyID
case object Fighter extends EnemyID
case object CosmicBee extends EnemyID
case object SpaceTurtle extends EnemyID
case object GalacticDragon extends EnemyID
case object CyberSalmon extends EnemyID

object enemies extends IDMap[EnemyID, EnemyAttributes]("data/enemies.json")

