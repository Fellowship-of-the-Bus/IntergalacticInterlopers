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

  val spawn = {
    var enemyPower = 5
    () => {
      var curEP = enemyPower

      // TODO: add back in sliding window

      while (curEP > 0) {
        val e = Enemy()
        curEP -= e.difficulty
        enemies = e :: enemies
      }
      powerUps = PowerUp() :: powerUps

      enemyPower += 1
    }
  }

  override def tick(delta: Long) = {
    // tick timers
    super.tick(delta: Long)

    // move all objects
    for {
      xs <- List(alliedProjectiles, enemyProjectiles, powerUps, enemies)
      x <- xs
      if (x.active)
    } x.move()

    if (player.active) {
      val shot = player.shoot
      shot match {
        case Some(s) =>
          alliedProjectiles = s :: alliedProjectiles
          numShot += 1
          // TODO: sound effects
        case _ => ()
      }
    }
    player.tick(delta)

    // collision
    for (e <- enemies; if (e.active)) {

    }
  }

  addTimer(new TickTimer(120, cleanup _))
  addTimer(new TickTimer(120, spawn))
}
