import java.io.File

import model.FileModel
import test.Csv

val current = "/home/dim/NetBeansProjects/ScalaRest/"
val baseOk = new File(current, "data/")
val baseErr = new File(current, "xxx/")
val topic = "topic_a"
val timestamp = "2015-09-01-05-05-05"

// По топику и таймстемпу запуска:
//    список партиций и число сообщений по каждой партиции
val files = FileModel.listByTopicAndTime(baseOk.getCanonicalPath, topic, timestamp)
assert(files.size == 1)
val s = files.flatMap(f => Csv.parse(io.Source.fromFile(f)))
s.foldLeft(Map[Int, Long]())((m, v) =>
  m + (v.partition -> (v.count + m.getOrElse(v.partition, 0L)))
)

//  .foldLeft(Map[String,Long]())((m,v) => m.)
def getStatByPartition(base: String, topic: String, timestamp: String): Map[Int, Long] = {
  val files = FileModel.listByTopicAndTime(base, topic, timestamp)
  files.flatMap(f => Csv.parse(io.Source.fromFile(f))).foldLeft(Map[Int, Long]())((m, v) =>
    m + (v.partition -> (v.count + m.getOrElse(v.partition, 0L)))
  )
}
getStatByPartition(baseOk.getCanonicalPath, "topic_a", "2015-09-01-05-05-05")