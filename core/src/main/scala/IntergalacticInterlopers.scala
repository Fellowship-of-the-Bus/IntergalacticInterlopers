package com.github.fellowship_of_the_bus.interlopers

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.glutils._
import com.badlogic.gdx.graphics.g2d._
import com.badlogic.gdx._
import com.badlogic.gdx.graphics.glutils.ShapeRenderer._

class IntergalacticInterlopers extends Game {
  class DemoScreen extends Screen {
    val batch = new SpriteBatch;
    val camera = new OrthographicCamera();
    camera.setToOrtho(false, 800, 480);
    camera.update();
    val player = new Texture("img/Player.png");
    val playerR = new Texture("img/PlayerR.png");
    val shapeRenderer = new ShapeRenderer
    shapeRenderer.setProjectionMatrix(camera.combined)
    var x = 50;
    var y = 50;

    var xR = 150;
    var yR = 150;

    def render(delta: Float) = {
      Gdx.gl.glClearColor(0, 0, 0, 1)
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
      Gdx.gl.glLineWidth(20)

      

      if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
        y = y + 5;
      } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
        y = y - 5;
      }

      if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
        x = x - 5;
      } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
        x = x + 5;
      }

      batch.setProjectionMatrix(camera.combined);
      batch.begin();
      batch.draw(player, x, y)
      batch.draw(playerR, xR, yR)
      batch.end();
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
