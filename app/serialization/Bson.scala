package serialization

import com.mongodb.DBObject
import com.mongodb.casbah.commons.Implicits._
import com.mongodb.casbah.commons.MongoDBObject
import com.novus.salat.transformers.CustomTransformer
import models._

trait Bson {

  object SourceTransformer extends CustomTransformer[Source, DBObject] {

    def deserialize(dbo: DBObject) = {
      dbo.as[String]("type") match {
        case "gnip" => GnipSource(dbo.as[List[String]]("rules"))
        case "datasift" => DatasiftSource(dbo.as[String]("csdl"))
        case _ => throw new Exception("unknown source")
      }
    }

    def serialize(source: Source) = {
      source match {
        case g: GnipSource => MongoDBObject("type" -> "gnip", "rules" -> g.rules)
        case d: DatasiftSource => MongoDBObject("type" -> "datasift", "csdl" -> d.csdl)
        case _ => MongoDBObject("type" -> "unknown")
      }
    }
  }

}
