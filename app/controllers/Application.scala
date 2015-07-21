package controllers

import play.api._
import play.api.libs.json._
import play.api.Play.current
import play.api.mvc._
import models._
import com.mongodb.casbah.Imports._
import com.novus.salat._
import java.util.Date

class Application extends Controller with serialization.Json with serialization.Bson {

  implicit val ctx: Context = {
    val c = new Context() {
      val name = "Custom Context"
      registerCustomTransformer(SourceTransformer)
    }
    c.registerClassLoader(Play.classloader)
    c
  }

  val focussets = MongoClient("localhost", 27017)("ekho-test")("focussets")

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def createFocusset(label: String) = Action {
    val focusset = Focusset(
      label = label,
      createdAt = new Date(),
      sources = List(GnipSource(List("rule1", "rule2")), DatasiftSource("this is a csdl"))
    )
    val dbo = grater[Focusset].asDBObject(focusset)
    focussets.insert(dbo)
    Ok(Json.toJson(focusset))
  }

  def getFocussets = Action {
    val fs: List[Focusset] = focussets.find.map(dbo => grater[Focusset].asObject(dbo)).toList
    Ok(Json.toJson(fs))
  }
}
