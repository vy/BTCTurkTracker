package com.github.vy.btcturktracker

import org.scalatest.FunSuite

class ResponseSuite extends FunSuite {

  test("TickerResponse should be deserialized from JSON") {
    val tr = TickerResponse.read(
      """{"high":1418.73,
        | "last":1402.28,
        | "timestamp":1394716996.0,
        | "bid":1407.00,
        | "volume":63.53,
        | "low":1378.21,
        | "ask":1416.80}""".stripMargin)
    assert(tr.timestamp.equals(1394716996.0))
  }

  test("OrderBookResponse should be deserialized from JSON") {
    val obr = OrderBookResponse.read(
      """{"timestamp":1394721669.0,
        | "bids":[["1414.01","0.00267561"],["1414.00","0.32894032"]],
        | "asks":[["1416.98","0.29032230"],["1416.99","0.12033763"]]
        | }""".stripMargin)
    assert(obr.timestamp.equals(1394721669.0))
    assert(obr.bids(0).sameElements(Array(1414.01, 0.00267561)))
    assert(obr.bids(1).sameElements(Array(1414.00, 0.32894032)))
    assert(obr.asks(0).sameElements(Array(1416.98, 0.29032230)))
    assert(obr.asks(1).sameElements(Array(1416.99, 0.12033763)))
  }

  test("TradesResponse should be deserialized from JSON") {
    val tr = TradesResponse.read(
      """[{"date":969.0,"tid":"5321c4b13ab4750c7c4c3bcd","price":1416.98,"amount":0.14044303}
        | ]""".stripMargin)
    assert(tr.trades.length.equals(1))
    val org = tr.trades.head
    val ref = new Trade("5321c4b13ab4750c7c4c3bcd", 1416.98, 0.14044303, 969.0)
    assert(org.tid.equals(ref.tid))
    assert(org.date.equals(ref.date))
    assert(org.price.equals(ref.price))
    assert(org.amount.equals(ref.amount))
  }

}
