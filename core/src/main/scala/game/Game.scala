package com.github.fellowship_of_the_bus
package interlopers
package game

import lib.util.{TimerListener,TickTimer}

class Game extends TimerListener {
  val player = new Player(100, 400)
  var enemies = List[Enemy]()
  var alliedProjectiles = List[Projectile]()
  var enemyProjectiles = List[Projectile]()
  var powerUps = List[PowerUp]()

  var score = 0
  var numHit = 0
  var numShot = 0

  /** removes inactive game objects */
  def cleanup() = {
    alliedProjectiles = alliedProjectiles.filter(_.active)
    enemyProjectiles = enemyProjectiles.filter(_.active)
    enemies = enemies.filter(_.active)
    powerUps = powerUps.filter(_.active)
  }

  addTimer(new TickTimer(120, cleanup _))
}
