package com.github.vy.btcturktracker

import com.beust.jcommander.{JCommander, ParameterException, IParameterValidator, Parameter}
import com.beust.jcommander.validators.PositiveInteger
import java.io.File

object Main {

  protected class WritableDirectory extends IParameterValidator {

    override def validate(name: String, value: String) {
      val file = new File(value)
      file.mkdirs()
      if (!file.exists())
        throw new ParameterException(
          s"Parameter $name should be a writable directory. (Found: $value)")
    }

  }

  protected object CLIArguments {

    @Parameter(
      names = Array("-h", "--help"),
      help = true)
    var help: Boolean = false

    @Parameter(
      names = Array("-p", "--updatePeriod"),
      description = "Data update period (in seconds)",
      validateWith = classOf[PositiveInteger])
    var updatePeriod: Int = 3

    @Parameter(
      names = Array("-o", "--outputDir"),
      description = "Data storage directory.",
      validateWith = classOf[WritableDirectory],
      required = true)
    var outputDirectory: String = null

  }

  def main(args: Array[String]) {
    val jc = new JCommander(CLIArguments, args: _*)
    if (CLIArguments.help) jc.usage()
    else {
      val storage = new Storage(CLIArguments.outputDirectory)
      val dispatcher = new Dispatcher(storage)
      while (true) {
        dispatcher.consume()
        dispatcher.schedule()
        Thread.sleep(CLIArguments.updatePeriod * 1000)
      }
    }
  }

}
