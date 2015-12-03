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

case class ProjectileAttributes(damage: Int, speed: Float)

trait ProjectileID extends ID 
case object NoShot extends ProjectileID
case object PBullet extends ProjectileID
case object Bullet extends ProjectileID
case object Missile extends ProjectileID
case object projectiles extends IDMap[ProjectileID, ProjectileAttributes]("projectiles.json")


class Projectile(xc: Float, yc: Float) extends GameObject(xc, yc) {
  type IDKind = ProjectileID
  def id = Bullet  // TODO: generalize

  def width = 10f
  def height = 10f

  val attributes = projectiles(id)

  def damage = attributes.damage

  // TODO: diagonal projectiles?
  val velocity = (0f, attributes.speed)
}
