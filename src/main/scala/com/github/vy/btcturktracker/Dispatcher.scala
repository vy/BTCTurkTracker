package com.github.vy.btcturktracker

import org.slf4j.LoggerFactory
import scala.collection.mutable
import scala.util.{Failure, Success}

class Dispatcher(storage: Storage) {

  private val log = LoggerFactory.getLogger(classOf[Dispatcher])

  object HTTPRequest {

    import dispatch._, Defaults._

    protected val http = Http.configure(_
      .setMaxRequestRetry(0)
      .setRequestTimeoutInMs(5000))

    protected def sendRequest(request: Request): Future[String] =
      http(url(request.url) OK as.String)

  }

  protected class HTTPRequest(val request: Request) {

    protected val future = HTTPRequest.sendRequest(request)

    val timestamp = Util.time

    def isCompleted = future.isCompleted

    def get = future.value.get

    override def toString = f"HTTPRequest[$request @ ${Util.millisToString(timestamp)}]"

  }

  protected val httpRequests = new mutable.Queue[HTTPRequest]()

  protected val requests =
    Iterator.continually(Seq(
      new TickerRequest(),
      new OrderBookRequest(),
      new TradesRequest(storage.lastTradeId))).flatten

  def schedule() {
    val request = requests.next()
    httpRequests.enqueue(new HTTPRequest(request))
    log.trace(s"Enqueued request: $request")
  }

  def consume() {
    while (!httpRequests.isEmpty && httpRequests.head.isCompleted) {
      val httpRequest = httpRequests.dequeue()
      httpRequest.get match {
        case Success(v) =>
          httpRequest.request.readResponse(v).write(storage)
          log.trace(s"Consumed HTTP request: $httpRequest")
        case Failure(e) =>
          log.warn(s"HTTP request failure: $httpRequest", e)
      }
    }
  }

}
