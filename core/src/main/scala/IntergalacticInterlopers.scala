package com.github.fellowship_of_the_bus.interlopers

import java.net.{InetSocketAddress, InetAddress, SocketAddress}


import com.badlogic.gdx.{Game => GdxGame, Screen => GdxScreen, Input, Gdx}
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.g2d._
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.ui.TextField._
import com.badlogic.gdx.scenes.scene2d.utils._
import com.badlogic.gdx.scenes.scene2d.Stage

import com.github.fellowship_of_the_bus.lib.net._

import scala.language.implicitConversions

case class Player(fileName: String, var x: Int, var y: Int) {
  private val texture = new Texture(fileName)
  def img = Image(texture, x, y)
}

object Batch {
  implicit def batch2SpriteBatch(batch: Batch): SpriteBatch = batch.batch
}

class Batch(private val batch: SpriteBatch) {
  def this() = this(new SpriteBatch)
  private var toDraw = List[Drawable]()
  def add(drawables: Drawable*): Unit = {
    toDraw = toDraw ++ drawables
  }
  def draw(): Unit = {
    batch.begin()
    for (drawable <- toDraw) {
      drawable.draw(batch)
    }
    batch.end()
  }
}

trait Drawable {
  def draw(batch: SpriteBatch): Unit
}
object Label {
  val defaultFont = new BitmapFont(true)
}
case class Label(text: String, x: Float, y: Float, font: BitmapFont = Label.defaultFont)
 extends Drawable {
  def draw(batch: SpriteBatch): Unit = {
    font.draw(batch, text, x, y)
  }
}
case class Image(img: Texture, x: Float, y: Float) extends Drawable {
  def this(fileName: String, x: Float, y: Float) = this(new Texture(fileName), x, y)

  def draw(batch: SpriteBatch): Unit = {
    batch.draw(img, x, y)
  }
}

object TextField {
  val background = new TextureRegionDrawable(new TextureRegion( new Texture("img/white.png")))
  val cursor = new TextureRegionDrawable(new TextureRegion( new Texture("img/bar.png")))
  val select = new TextureRegionDrawable(new TextureRegion( new Texture("img/blue.png")))
  val font = new BitmapFont()

  val defaultStyle = new TextFieldStyle(font, Color.BLACK, cursor, select, background)

  def apply(text: String): TextField = new TextField(text, defaultStyle)
}

abstract class Screen() extends GdxScreen {
  def update(delta: Long): Unit
  def render(): Unit

  def render(delta: Float) = {
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    Gdx.gl.glLineWidth(20)

    update(delta.toLong)
    render()
  }

  def resize(width: Int, height: Int) = {}
  def show() = {}
  def hide() = {}
  def pause() = {}
  def resume() = {}
  def dispose() = {}
}

class IntergalacticInterlopers extends GdxGame {
  class DemoScreen extends Screen {
    lazy val lanAddr = IP.localIP
    lazy val publicAddr = IP.publicIP

    val camera = new OrthographicCamera();
    camera.setToOrtho(true, 800, 480);
    camera.update();

    val players = Array(Player("img/Player.png", 50, 50),
      Player("img/PlayerR.png", 150, 150))

    val stage = new Stage()
    Gdx.input.setInputProcessor(stage)

    val tf = TextField("hello")

    implicit var ip: SocketAddress = null
    val port = 12345

    stage.addActor(tf);
    tf.setMessageText("Enter host IP address")
    tf.setTextFieldListener(new TextFieldListener() {
        override def keyTyped(textField: TextField, key: Char) = {
          if (key == '\r') {
            val text = tf.getText
            ip = new InetSocketAddress(text, port)
            println("Sending a message!")
            socket.send(s"HELLO")
          }
        }
      })
    var me = -1
    var gameStarted = false
    var isHost = false
    var isClient = false

    val socket = new UDPSocket(port, 512)

    def update(delta: Long): Unit = {
      if (gameStarted) {
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

        socket.send(s"${p.x} ${p.y}")
        for ((msg, sender) <- socket.receive) {
          val part = msg.split(" ")
          players(1-me).x = part(0).toInt
          players(1-me).y = part(1).toInt
        }
      } else if (Gdx.input.isKeyPressed(Input.Keys.H)) {
        isHost = true
        isClient = false
      } else if (Gdx.input.isKeyPressed(Input.Keys.J)) {
        isClient = true
        isHost = false
      } else if (isHost) {
        for ((msg, sender) <- socket.receive) {
          ip = sender
          socket.send("Hello to you too")
          println(s"got $msg from $sender")
          me = 0
          gameStarted = true
        }
      } else if (isClient) {
        for ((msg, sender) <- socket.receive) {
          println(s"got $msg from $sender")
          me = 1
          gameStarted = true
        }
      }
    }

    def render(): Unit = {
      val batch = new Batch;
      batch.setProjectionMatrix(camera.combined);

      if (gameStarted) {
        for (p <- players) {
          batch.add(p.img)
        }
      } else if (isClient) {
        stage.draw()
      } else if (isHost) {
        batch.add(Label(s"LAN: $lanAddr  Public IP: $publicAddr", 100, 100))
      } else {
        batch.add(Label(s"Press 'h' to host and 'j' to join", 100, 100))
      }

      batch.draw()
    }
  }

  override def create() = {
    this.setScreen(new DemoScreen)
  }
}
