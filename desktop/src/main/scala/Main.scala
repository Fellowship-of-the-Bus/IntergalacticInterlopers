package com.github.fellowship_of_the_bus.interlopers

import com.badlogic.gdx.backends.lwjgl._

object Main extends App {
  println(game.enemies)

  val cfg = new LwjglApplicationConfiguration()
  cfg.title = "Intergalactic Interlopers"
  cfg.height = 480
  cfg.width = 800
  cfg.forceExit = false
  cfg.resizable = false
  cfg.foregroundFPS = 60
  new LwjglApplication(IntergalacticInterlopers, cfg)
}

