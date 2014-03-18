package com.github.vy.btcturktracker

import java.io.{File, FileOutputStream, PrintStream}
import java.text.SimpleDateFormat
import java.util.Date
import scala.io.Source

object Util {

  implicit class RichFile(file: File) {

    def withPrintStream[T](append: Boolean = false)(body: PrintStream => T): T = {
      val os = new FileOutputStream(file, append)
      try { body(new PrintStream(os)) }
      finally { os.close() }
    }

    def lines: Iterator[String] = Source.fromFile(file).getLines()

  }

  def millisToString(time: Long): String = {
    val date: Date = new Date(time)
    val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    sdf.format(date)
  }

  def millisToString(time: Double): String = millisToString((time * 1000).toLong)

  def time: Double = System.currentTimeMillis() / 1000.0

}
