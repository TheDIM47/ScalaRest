package test

import java.io.{File, FileNotFoundException}

import org.scalatest.FlatSpec

import scala.util.Random

class ParserSpec extends FlatSpec {

  it should "produce empty iterator on empty" in {
    assert(Csv.parse(io.Source.fromString("")).toList.isEmpty)
  }

  it should "produce empty iterator on non-empty" in {
    assert(Csv.parse(io.Source.fromString(",")).toList.isEmpty)
  }

  it should "produce empty iterator on non-empty spaces" in {
    assert(Csv.parse(io.Source.fromString("   ")).toList.isEmpty)
  }

  it should "produce List(Log()) on valid" in {
    assert(List(Log(1, 2)) === Csv.parse(io.Source.fromString("1,2")).toList)
  }

  it should "produce List(Log()) on valid with extra" in {
    assert(List(Log(1, 2)) === Csv.parse(io.Source.fromString("1,2,3,4,5,")).toList)
  }

  it should "produce valid List(Log) on valid multiline" in {
    assert(List(Log(1, 2), Log(3, 4)) === Csv.parse(io.Source.fromString("1,2\n3,4")).toList)
  }

  val current = System.getProperty("user.dir")
  val csvFiles = List(new File(current, "/data/topic_a/history/2015-09-01-02-02-02/offsets.csv"),
    new File(current, "/data/topic_a/history/2015-09-01-05-05-05/offsets.csv"))

  it should "parse valid files from list" in {
    csvFiles.foreach(f => Csv.parse(io.Source.fromFile(f)))
  }

  it should "produce FileNotFoundException on invalid File" in {
    intercept[FileNotFoundException] {
      Csv.parse(io.Source.fromFile(new File(Random.nextString(20))))
    }
  }

}