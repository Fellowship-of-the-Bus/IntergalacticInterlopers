package com.github.fellowship_of_the_bus.interlopers

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.glutils._
import com.badlogic.gdx.graphics.g2d._
import com.badlogic.gdx._
import com.badlogic.gdx.graphics.glutils.ShapeRenderer._

class IntergalacticInterlopers extends Game {
  class DemoScreen extends Screen {
    val camera = new OrthographicCamera(Gdx.graphics.getWidth.toFloat, Gdx.graphics.getHeight.toFloat)
    camera.position.set(Gdx.graphics.getWidth/2f, Gdx.graphics.getHeight/2f, 0)
    camera.update()
    val mainBatch = new SpriteBatch
    val shapeRenderer = new ShapeRenderer
    shapeRenderer.setProjectionMatrix(camera.combined)
    def render(delta: Float) = {
      Gdx.gl.glClearColor(1, 1, 0, 1)
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
      Gdx.gl.glLineWidth(20)

      mainBatch.begin()

      shapeRenderer.begin(ShapeType.Line)
      shapeRenderer.setColor(0, 0, 0, 1)
      shapeRenderer.circle(160, 284, 100)
      shapeRenderer.circle(120, 314, 18)
      shapeRenderer.circle(200, 314, 18)
      shapeRenderer.polyline(Array(100, 260, 160, 220, 220, 260.0f))
      shapeRenderer.end()

      mainBatch.end()
    }

    def resize(width: Int, height: Int) = {}
    def show() = {}
    def hide() = {}
    def pause() = {}
    def resume() = {}
    def dispose() = {}
  }
  override def create() = {
    this.setScreen(new DemoScreen)
  }
}
