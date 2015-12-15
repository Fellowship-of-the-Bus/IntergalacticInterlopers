package com.github.fellowship_of_the_bus.interlopers

import java.net.{InetSocketAddress, InetAddress, SocketAddress}
import com.github.fellowship_of_the_bus.lib.net._
import com.badlogic.gdx.{Game => GdxGame, Gdx}
import GdxWrappers._

case class Player(fileName: String, var x: Int, var y: Int) {
  private val texture = Image(fileName, x, y)
  def img = texture.copy(x = x, y = y)
}

abstract class InterlopersScreen extends Screen {
  def game = IntergalacticInterlopers
  val port = 12345
}

object InitialScreen extends InterlopersScreen {
  implicit object InterlopersKeyMapping extends DefaultKeyMapping {
    override val actions = List(
      Keys.H -> (() => changeScreen(HostScreen)),
      Keys.J -> (() => changeScreen(ClientScreen))
    )
  }

  def update(delta: Long): Unit = {
    Input.actions
  }

  def render(): Unit = {
    val batch = new Batch
    batch.setProjectionMatrix(game.camera.combined) // need a better way to do this
    batch.add(Label(s"Press 'h' to host and 'j' to join", 100, 100))
    batch.draw()
  }
}

object ClientScreen extends InterlopersScreen {
  val socket = new UDPSocket(port, 512)

  val tf = TextField("hello")
  val stage = Stage()
  Input.setInputProcessor(stage)
  stage.addActor(tf);
  tf.setMessageText("Enter host IP address")
  tf.keyTyped { (textField: TextField, key: Char) =>
    if (key == '\r') {
      val text = tf.getText
      val ip = new InetSocketAddress(text, port)
      println("Sending a message!")
      socket.send(s"HELLO")(ip)
    }
  }

  def update(delta: Long) = {
    for ((msg, sender) <- socket.receive) {
      println(s"got $msg from $sender")
      changeScreen(new GameScreen(1, socket.connect(sender)))
    }
  }

  def render() = {
    stage.draw()
  }
}

object HostScreen extends InterlopersScreen {
  lazy val lanAddrs = IP.allLocalIPs
  lazy val publicAddr = IP.publicIP

  val socket = new UDPSocket(port, 512)

  def update(delta: Long) = {
    for ((msg, sender) <- socket.receive) {
      socket.send("Hello to you too")(sender)
      println(s"got $msg from $sender")
      changeScreen(new GameScreen(0, socket.connect(sender)))
    }
  }

  def render() = {
    val batch = new Batch
    batch.setProjectionMatrix(game.camera.combined);
    batch.add(Label(s"LAN: $lanAddrs", 100, 100))
    batch.add(Label(s"Public IP: $publicAddr", 100, 200))
    batch.draw()
  }
}

class GameScreen(me: Int, socket: ConnectedSocket) extends InterlopersScreen {
  val players = Array(Player("img/Player.png", 50, 50),
    Player("img/PlayerR.png", 150, 150))

  implicit object GameKeyMapping extends DefaultKeyMapping {
//    val actions = ...
  }

  def update(delta: Long) = {
    val p = players(me)

    val (dx, dy) = Input.movement
    p.x += 5*dx
    p.y += 5*dy

    if (dx != 0 || dy != 0) {
      socket.send(s"${p.x} ${p.y}")
    }

    for ((msg) <- socket.receive) {
      val part = msg.split(" ")
      players(1-me).x = part(0).toInt
      players(1-me).y = part(1).toInt
    }
  }

  def render() = {
    val batch = new Batch
    batch.setProjectionMatrix(game.camera.combined);
    for (p <- players) {
      batch.add(p.img)
    }
    batch.draw()
  }
}

object IntergalacticInterlopers extends GdxGame {
  val camera = OrthographicCamera();

  override def create() = {
    camera.setToOrtho(true, 800, 480);
    camera.update();

    this.setScreen(InitialScreen)
  }
}
