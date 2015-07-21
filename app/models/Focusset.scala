package models

import java.util.Date
import scala.concurrent.Future

case class Focusset(label: String, createdAt: Date, sources: List[Source])

trait Source {
  def start: Future[Source]
  def stop: Future[Source]
}

case class GnipSource(rules: List[String]) extends Source {
  def start = Future.successful(this)
  def stop = Future.successful(this)
}

case class DatasiftSource(csdl: String) extends Source {
  def start = Future.successful(this)
  def stop = Future.successful(this)
}
