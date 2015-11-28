package com.github.fellowship_of_the_bus.interlopers

import com.badlogic.gdx.backends.lwjgl._

object Main extends App {
  val cfg = new LwjglApplicationConfiguration()
  cfg.title = "Intergalactic Interlopers"
  cfg.height = 568
  cfg.width = 320
  cfg.forceExit = false
  new LwjglApplication(new IntergalacticInterlopers(), cfg)
}

