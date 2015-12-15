package com.github.fellowship_of_the_bus.interlopers

import com.badlogic.gdx.{Gdx, Input => GdxInput, InputMultiplexer, InputProcessor}
import com.badlogic.gdx.{Game => GdxGame, Screen => GdxScreen, Input => GdxInput, Gdx}
import com.badlogic.gdx.graphics.{OrthographicCamera => GdxOrthographicCamera,
  Texture, Color, GL20}
import com.badlogic.gdx.graphics.g2d.{BitmapFont, TextureRegion, SpriteBatch}
import com.badlogic.gdx.scenes.scene2d.ui.{TextField => GdxTextField}
import com.badlogic.gdx.scenes.scene2d.ui.TextField.{TextFieldListener,TextFieldStyle}
import com.badlogic.gdx.scenes.scene2d.utils.{TextureRegionDrawable}
import com.badlogic.gdx.scenes.scene2d.{Stage => GdxStage}
import scala.language.implicitConversions

object GdxWrappers {
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
      ()
    }
  }

  object Image {
    def apply(fileName: String, x: Float, y: Float): Image =
      Image(new Texture(fileName), x, y)
  }
  case class Image(img: Texture, x: Float, y: Float) extends Drawable {
    def draw(batch: SpriteBatch): Unit = {
      batch.draw(img, x, y)
    }
  }

  object TextField {
    val background = new TextureRegionDrawable(new TextureRegion(new Texture("img/white.png")))
    val cursor = new TextureRegionDrawable(new TextureRegion(new Texture("img/bar.png")))
    val select = new TextureRegionDrawable(new TextureRegion(new Texture("img/blue.png")))
    val font = new BitmapFont()

    val style = new TextFieldStyle(font, Color.BLACK, cursor, select, background)

    def apply(text: String): TextField = new GdxTextField(text, style)
    implicit def tf2gdx(textField: TextField) = textField.tf
  }
  implicit class TextField(val tf: GdxTextField) extends AnyVal {
    def keyTyped(f: (TextField, Char) => Unit) = tf.setTextFieldListener(
      new TextFieldListener() {
        override def keyTyped(textField: GdxTextField, key: Char) = f(textField, key)
      })
  }

  object OrthographicCamera {
    def apply() = new GdxOrthographicCamera()
  }

  object Stage {
    def apply() = new GdxStage()
  }

  abstract class Screen() extends GdxScreen {
    private var nextScreen: Option[Screen] = None

    def update(delta: Long): Unit
    def render(): Unit
    def game(): GdxGame

    def render(delta: Float) = {
      Gdx.gl.glClearColor(0, 0, 0, 1)
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
      Gdx.gl.glLineWidth(20)

      update(delta.toLong)
      render()
      for (screen <- nextScreen) {
        game.setScreen(screen)
        nextScreen = None
      }
    }

    def resize(width: Int, height: Int) = {}
    def show() = {}
    def hide() = {}
    def pause() = {}
    def resume() = {}
    def dispose() = nextScreen = None

    def changeScreen(screen: Screen) = nextScreen = Some(screen)
  }

  lazy val Input = Gdx.input

  object Keys {
    val Up = GdxInput.Keys.UP
    val Down = GdxInput.Keys.DOWN
    val Left = GdxInput.Keys.LEFT
    val Right = GdxInput.Keys.RIGHT

    val H = GdxInput.Keys.H
    val J = GdxInput.Keys.J

    val W = GdxInput.Keys.W
    val A = GdxInput.Keys.A
    val S = GdxInput.Keys.S
    val D = GdxInput.Keys.D
  }

  type Key = Int
  trait KeyMapping {
    def up: List[Key]
    def down: List[Key]
    def left: List[Key]
    def right: List[Key]
    def actions: List[(Key, () => Unit)]
  }

  val isKeyDown = ()
  object ArrowKeyMapping extends KeyMapping {
    val up = List(Keys.Up)
    val down = List(Keys.Down)
    val left = List(Keys.Left)
    val right = List(Keys.Right)
    val actions = List[(Key, () => Unit)]()
  }
  object WasdKeyMapping extends KeyMapping {
    val up = List(Keys.W)
    val down = List(Keys.S)
    val left = List(Keys.A)
    val right = List(Keys.D)
    val actions = List[(Key, () => Unit)]()
  }
  class ComposeKeyMappings(mappings: List[KeyMapping]) extends KeyMapping {
    val up = mappings.foldLeft(List[Key]())((res, m) => m.up ++ res)
    val down = mappings.foldLeft(List[Key]())((res, m) => m.down ++ res)
    val left = mappings.foldLeft(List[Key]())((res, m) => m.left ++ res)
    val right = mappings.foldLeft(List[Key]())((res, m) => m.right ++ res)
    val actions = mappings.foldLeft(List[(Key, () => Unit)]())((res, m) => m.actions ++ res)
  }
  class DefaultKeyMapping
    extends ComposeKeyMappings(List(ArrowKeyMapping, WasdKeyMapping))

  implicit class InputOps(val input: GdxInput) {
    def movement(implicit mapping: KeyMapping): (Int, Int) = {
      def helper(neg: List[Key], pos: List[Key]): Int = {
        val n = neg.exists(input.isKeyPressed(_))
        val p = pos.exists(input.isKeyPressed(_))

        if (n && ! p) -1
        else if (p && ! n) 1
        else 0
      }

      (helper(mapping.left, mapping.right), helper(mapping.down, mapping.up))
    }

    def actions(implicit mapping: KeyMapping): Unit = for {
      (key, action) <- mapping.actions
      if (Input.isKeyPressed(key))
    } action()
  }
}
