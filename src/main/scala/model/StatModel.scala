package model

import test.Csv

object StatModel {

  /**
    * По топику для последнего запуска, статистику:
    *    суммарное число сообщений во всех патрициях, максимальное/минимальное число сообщений, среднее число сообщений
    * Represent statistic for last run
    *
    * @param sum sum of all messages
    * @param max max message count
    * @param min min message count
    * @param avg average message count
    */
  case class Stat(sum: Long, max: Long, min: Long, avg: Double)

  /**
    * По топику для последнего запуска, статистику:
    *    суммарное число сообщений во всех патрициях, максимальное/минимальное число сообщений, среднее число сообщений
    * Return statistic for given topic and last run time
    *
    * @param base base directory
    * @param topic topic name
    * @return Stat. data
    */
  def getStat(base: String, topic: String): Stat = {
    val files = FileModel.listByTopicAndLastTime(base, topic)
    val map = files.flatMap(f => Csv.parse(io.Source.fromFile(f))).foldLeft(Map[Int, Long]())((m, v) =>
      m + (v.partition -> (v.count + m.getOrElse(v.partition, 0L)))
    )
    Stat(
      map.values.sum,
      map.values.min,
      map.values.max,
      map.values.sum / map.size
    )
  }

  /**
    * По топику и таймстемпу запуска: список партиций и число сообщений по каждой партиции
    * Return map of partitions and summary count of messages
    *
    * @param base base directory
    * @param topic topic name
    * @param timestamp timestamp
    * @return map of partition -> sum(count)
    */
  def getStatByPartition(base: String, topic: String, timestamp: String): Map[Int, Long] = {
    val files = FileModel.listByTopicAndTime(base, topic, timestamp)
    files.flatMap(f => Csv.parse(io.Source.fromFile(f))).foldLeft(Map[Int, Long]())((m, v) =>
      m + (v.partition -> (v.count + m.getOrElse(v.partition, 0L)))
    )
  }

}
