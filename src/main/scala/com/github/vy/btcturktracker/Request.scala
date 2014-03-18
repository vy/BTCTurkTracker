package com.github.vy.btcturktracker

sealed trait Request {
  val url: String
  def readResponse(content: String): Response
}

class TickerRequest extends Request {
  val url = "https://www.btcturk.com/api/ticker"
  def readResponse(content: String) = TickerResponse.read(content)
  override def toString = classOf[TickerResponse].getSimpleName
}

class OrderBookRequest extends Request {
  val url = "https://www.btcturk.com/api/orderbook"
  def readResponse(content: String) = OrderBookResponse.read(content)
  override def toString = classOf[OrderBookRequest].getSimpleName
}

class TradesRequest(val sinceId: Option[String]) extends Request {
  val url: String = "https://www.btcturk.com/api/trades" + sinceId.map {
    id => s"?sinceid=$id"
  }.getOrElse("")
  def readResponse(content: String) = TradesResponse.read(content)
  override def toString = classOf[TradesRequest].getSimpleName +
    sinceId.map(id => s"(sinceId:$id)").getOrElse("")
}
