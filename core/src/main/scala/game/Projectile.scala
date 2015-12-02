package com.github.fellowship_of_the_bus
package interlopers
package game

import rapture.json._

object ProjectileImplicits {
  implicit object ProjectileID extends IDFactory[ProjectileID] {
    val ids = Vector(Bullet, Missile, PBullet, NoShot)
  }
  implicit lazy val extractor = 
    Json.extractor[String].map(ProjectileID.fromString(_))
}
import ProjectileImplicits._

case class ProjectileAttributes(damage: Int, speed: Int)

trait ProjectileID extends ID 
case object NoShot extends ProjectileID
case object PBullet extends ProjectileID
case object Bullet extends ProjectileID
case object Missile extends ProjectileID
case object projectiles extends IDMap[ProjectileID, ProjectileAttributes]("projectiles.json")

