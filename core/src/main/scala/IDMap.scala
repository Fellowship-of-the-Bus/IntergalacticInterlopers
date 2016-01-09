package com.github.fellowship_of_the_bus
package interlopers

import lib.util.{rand,openFileAsStream}
import lib.game.{IDMap,IDFactory}

import rapture.json._
import rapture.json.jsonBackends.jackson._

trait ID

// need to decide what an image looks like
// case class ImageAttributes(img: Image)

// image only
trait ImageID
case object GameOver extends ImageID
case object Heart extends ImageID
case object TopBorder extends ImageID
case object Background extends ImageID
// case object images extends IDMap[ImageID, ImageAttributes]("images.json", GameOver, Heart, TopBorder, Background)
