[BTCTurkTracker](http://github.com/vy/BTCTurkTracker) is a Scala application
that periodically collects data from [BTCTurk](http://btcturk.com)
[API](https://btcturk.com/yardim/api-home-page).

Usage
-----

You can build a JAR out of Scala sources using `sbt`:

    $ git clone http://github.com/vy/BTCTurkTracker
    $ cd BTCTurkTracker
    $ sbt assembly

`assembly` task creates a `BTCTurkTracker-assembly-<VERSION>.jar` under
`target/scala-<VERSION>` directory. You can use this jar as follows.

    $ java -jar target/scala-2.10/BTCTurkTracker-assembly-0.1.jar -h
      Usage: <main class> [options]
        Options:
          -h, --help

             Default: false
        * -o, --outputDir
             Data storage directory.
          -p, --updatePeriod
             Data update period (in seconds)
             Default: 3

Note that [BTCTurk API](https://btcturk.com/yardim/api-home-page) allows
at most 1 request per second. `BTCTurkTracker` performs 3 requests in
sequential order: `ticker`, `orderbook`, and `trades`. In order to not
exceed the maximum request threshold, `updatePeriod` parameter must be
larger than or equal to `3`.

    $ java -jar target/scala-2.10/BTCTurkTracker-assembly-0.1.jar -o /tmp/btc -p 3

Alternatively, you can start `BTCTurkTracker` using the shell script
provided under `bin` directory.

    $ bin/BTCTurkTracker.sh
    Usage: bin/BTCTurkTracker.sh <BTCTurkTracker.jar> <outputDirectory> [<updatePeriod>]

Shell script contains some extra JVM flags for optimization.

Output
------

When you start `BTCTurkTracker`, it starts to periodically collect data
to the specified directory. The contents of the `outputDirectory` is as
follows:

    /last_trade_id.txt
    /2014/03/17/order-book.tab
    /2014/03/17/ticker.tab
    /2014/03/17/trades.tab
    /2014/03/18/order-book.tab
    /2014/03/18/ticker.tab
    /2014/03/18/trades.tab
    ...

Each file contains the tabulated raw data collected through the corresponding
API call. Formats of the generated files are as follows.

`last_trade_id.txt` contains the last received trade id from the `trades`
API call. A sample content of the file is presented below.

    $ cat /tmp/btc/last_trade_id.txt
    5327149b3ab4750c7c4c3d73

`ticker.tab` contains the list of `ticker` API call responses. Each line
contains the following TAB separated fields:

- `<timestamp>`
- `<low>`
- `<high>`
- `<last>`
- `<bid>`
- `<ask>`
- `<volume>`

A sample snippet of `ticker.tab` is as follows.

    $ head -n 1 /tmp/btc/2014/03/17/ticker.tab
    1395070197.0    1373.47    1406.11    1387.10    1384.00    1387.10    48.15

`order-book.tab` contains the list of `orderbook` API call responses. Each
line contains the following TAB separated fields:

- `<timestamp>`
- `<bid_0_price>:<bid_0_amount>,<bid_1_price>:<bid_1_amount>,...`
- `<ask_0_price>:<ask_0_amount>,<ask_1_price>:<ask_1_amount>,...`

A sample snippet of `order-book.tab` is as follows.

    $ head -n 1 /tmp/btc/2014/03/17/order-book.tab
    1395070282.0    1384.00:0.72632458,1382.01:0.00126072,...    1798.01:2.29470410,1799.00:0.72592064,...

`trades.tab` lists the `trades` API call responses. Each line contains a
TAB separated list of trades, where each trade is represented as follows:
`<timestamp>,<tid>,<price>,<amount>`.

License
-------

The [BTCTurkTracker](https://github.com/vy/BTCTurkTracker/) by
[Volkan Yazıcı](http://vlkan.com/) is licensed under the
[Creative Commons Attribution 4.0 International License](http://creativecommons.org/licenses/by/4.0/).

[![Creative Commons Attribution 4.0 International License](http://i.creativecommons.org/l/by/4.0/80x15.png)](http://creativecommons.org/licenses/by/4.0/)
