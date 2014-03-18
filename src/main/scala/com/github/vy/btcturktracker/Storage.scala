package com.github.vy.btcturktracker

import java.io.File
import java.util.Calendar
import Util.RichFile

class Storage(val outDir: String) {

  protected val lastTradeIdFile = new File(outDir, "last_trade_id.txt")

  protected var _lastTradeId: Option[String] = {
    if (!lastTradeIdFile.exists()) None
    else Some(lastTradeIdFile.lines.next().trim)
  }

  def lastTradeId: Option[String] = _lastTradeId

  def lastTradeId_=(tradeId: String) {
    lastTradeIdFile.withPrintStream()(ps => ps.print(tradeId))
    _lastTradeId = Some(tradeId)
  }

  def getOutputFile(filename: String): File = {
    val calendar = Calendar.getInstance()
    val year = f"${calendar.get(Calendar.YEAR)}%04d"
    val month = f"${calendar.get(Calendar.MONTH)+1}%02d"
    val day = f"${calendar.get(Calendar.DAY_OF_MONTH)}%02d"
    val fileDir = Seq(year, month, day).foldLeft(new File(outDir)) {
      (parent: File, child: String) => new File(parent, child) }
    fileDir.mkdirs()
    if (!fileDir.exists()) throw new RuntimeException(s"Failed creating directory: $fileDir")
    new File(fileDir, filename)
  }

}
