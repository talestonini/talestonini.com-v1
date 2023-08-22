package com.talestonini

import java.time._
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern

import org.scalatest._
import org.scalatest.matchers.should.Matchers._

class LocalDateSpec extends BaseSpec {

  "a datetime string" should {

    "be parseable into a local date" in {
      val str      = "2019-11-10T09:55:34.276Z"
      val datetime = LocalDateTime.parse(str, ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
      datetime.format(ofPattern("dd/MM/yyyy")) shouldEqual "10/11/2019"
    }

  }

}
