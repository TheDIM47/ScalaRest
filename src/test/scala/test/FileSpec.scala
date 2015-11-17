package test

import java.io.File

import model.FileModel
import org.scalatest.FlatSpec

class FileSpec extends FlatSpec {
  val current = System.getProperty("user.dir")
  val baseOk = new File(current, "data/")
  val baseErr = new File(current, "xxx/")

  "List Topics" should "produce empty list on wrong dir" in {
    assert(FileModel.listTopics(baseErr).isEmpty)
  }

  "List Topics" should "produce non empty list on base dir" in {
    assert(FileModel.listTopics(baseOk).nonEmpty)
  }

  "List By Topic And Last Runtime" should "produce List on valid dir" in {
    assert(FileModel.listByTopicAndLastTime(baseOk.getCanonicalPath, "topic_a").nonEmpty)
  }

  "List By Topic And Last Runtime" should "produce List with single file on valid dir" in {
    assert(1 == FileModel.listByTopicAndLastTime(baseOk.getCanonicalPath, "topic_a").size)
  }

  "List By Topic And Runtime" should "produce List with single file on valid dir" in {
    assert(1 == FileModel.listByTopicAndTime(baseOk.getCanonicalPath, "topic_a", "2015-09-01-05-05-05").size)
  }

  "List By Topic And Last Runtime" should "produce Nil on empty topic" in {
    assert(FileModel.listByTopicAndLastTime(baseOk.getCanonicalPath, "topic_z").isEmpty)
  }

  "List By Topic And Last Runtime" should "produce Nil on bad dir" in {
    assert(FileModel.listByTopicAndLastTime(baseErr.getCanonicalPath, "topic_a").isEmpty)
  }

  "Last Runtime (all)" should "produce None on wrong dir" in {
    assert(None === FileModel.lastRuntime(baseErr))
  }

  "Last Runtime (all)" should "produce Some(Date) on base dir" in {
    assert(FileModel.lastRuntime(baseOk).isDefined)
  }

  "Last Topic Runtime" should "by topic produce None on wrong dir" in {
    assert(None === FileModel.lastTopicRuntime(baseErr.getCanonicalPath, "topic_a"))
  }

  "Last Topic Runtime" should "by topic produce None on wrong topic" in {
    assert(None === FileModel.lastTopicRuntime(baseOk.getCanonicalPath, "topic_zzz_qqq"))
  }

  "Last Topic Runtime" should "by topic produce None on empty topic" in {
    assert(None === FileModel.lastTopicRuntime(baseOk.getCanonicalPath, "topic_z"))
  }

  "Last Topic Runtime" should "by topic produce Some(String) on valid topic" in {
    assert(FileModel.lastTopicRuntime(baseOk.getCanonicalPath, "topic_a").isDefined)
  }

}
