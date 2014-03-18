package com.github.vy.btcturktracker

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import Util.RichFile

sealed trait Response {
  val timestamp: Double = Util.time
  def write(storage: Storage): Unit
}

sealed class TickerResponse
(val low: Double,
 val high: Double,
 val last: Double,
 val bid: Double,
 val ask: Double,
 val volume: Double,
 override val timestamp: Double) extends Response {

  def write(storage: Storage) {
    storage.getOutputFile("ticker.tab").withPrintStream(append=true) { ps =>
        ps.println((f"$timestamp%.1f" :: List(low, high, last, bid, ask, volume).map(a => f"$a%.2f"))
          .mkString("\t"))
    }
  }

}

object TickerResponse {
  def read(content: String): TickerResponse =
    ObjectMapper.read(content, classOf[TickerResponse])
}

sealed class OrderBookResponse
(val bids: Array[Array[Double]],
 val asks: Array[Array[Double]],
 override val timestamp: Double) extends Response {

  def write(storage: Storage) {
    if (!bids.isEmpty || !asks.isEmpty) {
      storage.getOutputFile("order-book.tab").withPrintStream(append=true) { ps =>
        ps.println(Seq(
          f"$timestamp%.1f",
          bids.map(bid => f"${bid(0)}%.2f:${bid(1)}%.8f").mkString(","),
          asks.map(ask => f"${ask(0)}%.2f:${ask(1)}%.8f").mkString(",")).mkString("\t"))
      }
    }
  }

}

object OrderBookResponse {
  def read(content: String): OrderBookResponse =
    ObjectMapper.read(content, classOf[OrderBookResponse])
}

sealed class Trade
(val tid: String,
 val price: Double,
 val amount: Double,
 val date: Double)

@JsonIgnoreProperties(value = Array("timestamp"))
sealed class TradesResponse(val trades: Array[Trade]) extends Response {

  def write(storage: Storage) {
    if (!trades.isEmpty) {
      storage.getOutputFile("trades.tab").withPrintStream(append=true) { ps =>
        ps.println(trades.map(trade =>
          Seq(f"${trade.date}%.1f", trade.tid, f"${trade.price}%.2f", f"${trade.amount}%.8f").mkString(","))
            .mkString("\t"))
      }
      storage.lastTradeId = trades.last.tid
    }
  }

}

object TradesResponse {
  def read(content: String): TradesResponse =
    ObjectMapper.read(s"""{"trades": $content}""", classOf[TradesResponse])
}
