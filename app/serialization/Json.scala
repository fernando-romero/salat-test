package serialization

import play.api.libs.json._
import models._

trait Json {

  implicit val gnipSourceFmt = Json.format[GnipSource]
  implicit val datasiftSourceFmt = Json.format[DatasiftSource]

  implicit val sourceFmt = new Format[Source] {

    def writes(s: Source): JsValue = s match {
      case g: GnipSource => Json.obj("type" -> "gnip", "rules" -> g.rules)
      case d: DatasiftSource => Json.obj("type" -> "datasift", "csdl" -> d.csdl)
      case _ => Json.obj("type" -> "unknown")
    }

    def reads(json: JsValue): JsResult[Source] = {
      (json \ "type").asOpt[String].map {
        case "gnip" => json.validate[GnipSource]
        case "datasift" => json.validate[DatasiftSource]
        case _ => JsError()
      }.getOrElse(JsError())
    }
  }

  implicit val focussetFmt = Json.format[Focusset]
}
