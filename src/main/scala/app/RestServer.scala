package app

import java.io.File

import akka.actor.ActorSystem
import model.{StatModel, FileModel}
import spray.http.MediaTypes
import spray.json.{JsonFormat, RootJsonFormat, DefaultJsonProtocol}
import spray.routing.SimpleRoutingApp

import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
//import spray.httpx.SprayJsonSupport
//import spray.httpx.unmarshalling.FromRequestUnmarshaller

object UserJsonProtocol extends DefaultJsonProtocol {
  implicit val statFormat = jsonFormat4(model.StatModel.Stat)
}

// http://localhost:9080/
object RestServer extends App with SimpleRoutingApp {

  val baseDir: File = if (args.length == 0) FileModel.defaultBaseDir else new File(args(0))

  implicit val system = ActorSystem()

  import system.dispatcher

  // GET http://myhost:9080/topics
  def paths =
  //  - Список топиков, данные по которым есть данные в этой директории
    path("topics") {
      get {
        respondWithMediaType(MediaTypes.`application/json`) {
          complete {
            FileModel.listTopics(baseDir.getCanonicalPath)
          }
        }
      }
    } ~
      //  - По топику:  таймстемп последнего запуска
      path("stamp" / Segment) { topic =>
        respondWithMediaType(MediaTypes.`application/json`) {
          complete {
            FileModel.lastTopicRuntime(baseDir.getCanonicalPath, topic)
          }
        }
      } ~
      //  - По топику  для последнего запуска, статистику: суммарное число сообщений во всех патрициях,
      //    максимальное/минимальное число сообщений, среднее число сообщений
      path("stat" / Segment) { topic =>
        respondWithMediaType(MediaTypes.`application/json`) {
          complete {
            import UserJsonProtocol._
            StatModel.getStat(baseDir.getCanonicalPath, topic)
          }
        }
      } ~
      //  - По топику и таймстемпу запуска: список партиций и число сообщений по каждой партиции
      path("map" / Segment / Segment) { (topic, timestamp) =>
        respondWithMediaType(MediaTypes.`application/json`) {
          complete {
            val m = StatModel.getStatByPartition(baseDir.getCanonicalPath, topic, timestamp)
            val r :Map[String, String] = m.map(e => (e._1.toString, e._2.toString))
            r
          }
        }
      }

  startServer(interface = "localhost", port = 9080) {
    paths
  }
}
