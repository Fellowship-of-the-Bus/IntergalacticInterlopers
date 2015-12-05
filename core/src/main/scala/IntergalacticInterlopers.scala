package com.github.fellowship_of_the_bus.interlopers

import java.net.InetAddress

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.glutils._
import com.badlogic.gdx.graphics.g2d._
import com.badlogic.gdx._
import com.badlogic.gdx.scenes.scene2d.ui._
import com.badlogic.gdx.scenes.scene2d.ui.TextField._
import com.badlogic.gdx.scenes.scene2d.utils._
import com.badlogic.gdx.scenes.scene2d._

import com.github.fellowship_of_the_bus.lib.net._

case class Player(fileName: String, var x: Int, var y: Int) {
  val texture = new Texture(fileName)
}

class IntergalacticInterlopers extends Game {
  class DemoScreen extends Screen {
    val batch = new SpriteBatch;
    val camera = new OrthographicCamera();
    camera.setToOrtho(false, 800, 480);
    camera.update();

    val players = Array(Player("img/Player.png", 50, 50), 
      Player("img/PlayerR.png", 150, 150))
    val shapeRenderer = new ShapeRenderer
    shapeRenderer.setProjectionMatrix(camera.combined)

    var xR = 150;
    var yR = 150;
    val stage = new Stage()
    Gdx.input.setInputProcessor(stage)

    val tfBackground = new TextureRegionDrawable(new TextureRegion( new Texture("img/white.png")))
    val tfCursor = new TextureRegionDrawable(new TextureRegion( new Texture("img/bar.png")))
    val tfSelect = new TextureRegionDrawable(new TextureRegion( new Texture("img/blue.png")))
    val font = new BitmapFont()
    val tf = new TextField("hello", new TextFieldStyle(font, Color.BLACK, tfCursor, tfSelect, tfBackground))
    stage.addActor(tf);

    var me = -1
    var gameStarted = false
    var isHost = false
    var isClient = false

    val socket = new UDPSocket(12345, 512)

    def render(delta: Float) = {
      Gdx.gl.glClearColor(0, 0, 0, 1)
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
      Gdx.gl.glLineWidth(20)
      if (gameStarted) {

        {
          val p = players(me)
          if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            p.y = p.y + 5;
          } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            p.y = p.y - 5;
          }

          if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            p.x = p.x - 5;
          } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            p.x = p.x + 5;
          }
        }

        stage.draw()
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (p <- players) {
          batch.draw(p.texture, p.x, p.y)
        }
        batch.end();
      } else if (Gdx.input.isKeyPressed(Input.Keys.H)) {
        isHost = true
      } else if (Gdx.input.isKeyPressed(Input.Keys.C)) {
        isClient = true
      } else if (isHost) {
          val font = new BitmapFont()
          batch.begin()
          font.draw(batch, s"IP: ${InetAddress.getLocalHost.getHostAddress}", 100, 100)
          batch.end()
      } else if (isClient) {
        
      }
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
