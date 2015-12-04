package com.github.fellowship_of_the_bus
package interlopers
package game

import lib.util.{TimerListener, ManualTimerManager, TickTimer, FireN}

trait Shooter extends GameObject {
  def shotType: ProjectileID
  def numShot: Int
  def shotInterval: Int
  def shotDelay: Int

  /** controls how often shots can be fired - essentially a reload timer */
  private val cooldown = new TimerListener {}

  /** controls how many shots can be fired in rapid succession */
  private val burst = new ManualTimerManager {}

  def tick(delta: Long) = {
    cooldown.tick(delta)
    burst.tick(delta)
  }

  private def tryShoot() = {
    def doShoot() = {
      shot = Some(Projectile(shotType, x, y))
    }

    // don't start a new burst during a burst, and don't start
    // a new burst while the cooldown is active
    if (! burst.ticking && ! cooldown.ticking) {
      burst.addTimer(new TickTimer(shotDelay, doShoot _, FireN(numShot)))
    }

    shot = None
    burst.fire()

    // if burst finished, start the cooldown
    if (! burst.ticking) {
      cooldown.addTimer(new TickTimer(shotInterval, () => ()))
    }    
  }

  private var shot: Option[Projectile] = None
  def shoot() = {
    // if something doesn't shoot, don't bother with any of this
    if (shotType != NoShot) {
      tryShoot()
    }

    // if burst fired, a Projectile will have been 
    // placed into shot, otherwise it will be None
    shot
  }
}
