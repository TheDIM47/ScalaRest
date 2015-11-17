package model

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

import scala.annotation.tailrec

/**
  * Storage specification:
  * <BASE_DIR>/<topic_name>/history/<run_timestamp>/offsets.csv
  *
  * Run_TimeStamp format:
  * YYYY-MM-DD-HH-mm-ss with leading zeros
  */
object FileModel {
  val defaultBaseDir = new File(System.getProperty("user.dir"), "data/")

  /**
    * List files by topic and last run time
    *
    * @param base base directory
    * @param topic topic name
    * @return list of files in run_timestamp folder
    */
  def listByTopicAndLastTime(base: String, topic: String): Seq[File] = {
    lastTopicRuntime(base, topic) match {
      case Some(timestamp) => listByTopicAndTime(base, topic, timestamp)
      case None => Nil
    }
  }

  /**
    * List of files by topic and run time
    *
    * @param base base directory
    * @param topic topic name
    * @param timestamp timestamp
    * @return list of files in run_timestamp folder
    */
  def listByTopicAndTime(base: String, topic: String, timestamp: String): Seq[File] =
    listByTopicAndTime(new File(s"$base/$topic/history/$timestamp/"))

  def listByTopicAndTime(base: File): Seq[File] = recursiveFiles(base)

  /**
    * Return last runtime date as string for specified topic
    * Function validate date format for run_timestamp
    *
    * @param base base directory
    * @param topic topic name
    * @return Some(last runtime date) or None
    */
  def lastTopicRuntime(base: String, topic: String): Option[String] =
    lastTopicRuntime(new File(s"$base/$topic/history/"))

  def lastTopicRuntime(base: File): Option[String] = {
    val blen = base.getCanonicalPath.length + 1
    recursiveFiles(base).map(_.getCanonicalPath.substring(blen))
      .collect(extract(2, 0))
      .filter(x => dateFormatter.parse(x).getTime > 0)
      .reduceOption((x, y) => if (x > y) x else y)
  }

  /**
    * Список топиков, данные по которым есть данные в этой директории
    * Topics in base directory which have data (offsets.csv)
    *
    * @param baseDir base directory
    * @return list of topics in base directory
    */
  def listTopics(baseDir: String): Seq[String] = listTopics(new File(baseDir))

  def listTopics(base: File): Seq[String] = {
    val blen = base.getCanonicalPath.length + 1
    recursiveFiles(base).map(_.getCanonicalPath.substring(blen))
      .collect(extract(4, 0))
      .filter(_.length > 0)
      .distinct
  }

  /**
    * Return last runtime date for all topics
    * Function validate date format for run_timestamp
    *
    * @param baseDir base directory
    * @return Some(last runtime date) or None
    */
  def lastRuntime(baseDir: String): Option[Date] = lastRuntime(new File(baseDir))

  def lastRuntime(base: File): Option[Date] = {
    val blen = base.getCanonicalPath.length + 1
    val ord = Ordering.by((_: Date).getTime)
    recursiveFiles(base).map(_.getCanonicalPath.substring(blen))
      .collect(extract(4, 2))
      .filter(_.length == dateFormatLen)
      .map(x => dateFormatter.parse(x))
      .reduceOption(ord.max)
  }

  // Convert Array[T] to List[T] with null handle
  private def n2n[T](a: Array[T]): List[T] = if (a == null) Nil else a.toList

  // split path to Array of String, filter to exact array size and extract N-th part of array
  private def extract(total: Int, part: Int): PartialFunction[String, String] = {
    case s: String =>
      val a = s.split(pathSeparator)
      if (a.size == total) a(part) else ""
  }

  // simple recursive fail scanner
  def recursiveFiles(f: File): Seq[File] = {
    @tailrec
    def scan(dir: List[File], res: List[File]): Seq[File] = dir match {
      case x :: xs =>
        val (dirs, files) = n2n(x.listFiles()).partition(_.isDirectory)
        scan(xs ++ dirs, if (files.isEmpty) res else files ++ res)
      case _ => res
    }
    scan(List(f), Nil)
  }


  private val pathSeparator = System.getProperty("file.separator")
  private val dateFormat = "YYYY-MM-DD-HH-mm-ss"
  private val dateFormatter = new SimpleDateFormat("YYYY-MM-DD-HH-mm-ss")
  private val dateFormatLen = dateFormat.length
}
