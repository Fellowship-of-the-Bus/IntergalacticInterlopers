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

case class ProjectileAttributes(damage: Int, speed: Float,
  width: Float, height: Float)

trait ProjectileID extends ID 
case object NoShot extends ProjectileID
case object PBullet extends ProjectileID
case object Bullet extends ProjectileID
case object Missile extends ProjectileID
case object projectiles extends IDMap[ProjectileID, ProjectileAttributes]("projectiles.json")


class Projectile(val id: ProjectileID, xc: Float, yc: Float) extends GameObject(xc, yc) {
  type IDKind = ProjectileID
  
  def width = attributes.width
  def height = attributes.height

  val attributes = projectiles(id)

  def damage = attributes.damage

  // TODO: diagonal projectiles?
  val velocity = (0f, attributes.speed)
}

object Projectile {
  def apply(shotType: ProjectileID, x: Float, y: Float) = {
    val direction = shotType match {
      // enemy shots go down
      case Bullet | Missile => 1

      // player shots go up
      case PBullet => -1
    }
    val dy = direction * projectiles(shotType).height
    new Projectile(shotType, x, y+dy)
  }  
}
