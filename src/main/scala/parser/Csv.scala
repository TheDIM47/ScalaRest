package test

import scala.collection.mutable.ArrayBuffer

/**
  * Log file record
  * @param partition partition number
  * @param count number of messages
  */
case class Log(partition: Int, count: Long)

object Log {
  def apply(p: String, c: String): Log = Log(p.toInt, c.toLong)

  def apply(a: Array[String]): Log = apply(a(0), a(1))
}

object Csv {
  def parse(src: io.Source, ch: Char = ','): List[Log] = {
    using(src) { source =>
      val v = for {
        line <- source.getLines
        arr = line.split(ch).map(_.trim)
        if arr.length > 1
      } yield Log(arr)
      v.toList
    }
  }

  import scala.language.reflectiveCalls

  private def using[A <: {def close() : Unit}, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }
}
