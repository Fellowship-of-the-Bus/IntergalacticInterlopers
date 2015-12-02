package com.github.fellowship_of_the_bus
package interlopers

import lib.util.{rand,openFileAsStream}

import rapture.json._
import rapture.json.jsonBackends.jackson._

trait IDFactory[IDKind] {
  def fromString(name: String): IDKind = nameMap(name)

  def ids: Vector[IDKind]
  def random(): IDKind = ids(rand(ids.length))

  private lazy val nameMap: Map[String, IDKind] = ids.map(x => (x.toString, x)).toMap
}

class IDMap[IDKind, ValueKind](fileName: String) (implicit extractor: Extractor[ValueKind, Json], factory: IDFactory[IDKind]) {
  def ids = factory.ids
  val idmap = readMap()

  def random(): IDKind = factory.random()
  def randomValue(): ValueKind = idmap(random())
  def apply(id: IDKind): ValueKind = idmap(id)

  private def readMap() = {
    val json = Json.parse(scala.io.Source.fromInputStream(openFileAsStream(fileName)).mkString)
    json.as[Map[String,ValueKind]].map({ case (k, v) => (factory.fromString(k), v) })
  }

  override def toString() = (ids, idmap).toString
}

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
