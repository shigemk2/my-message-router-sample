package com.example

import akka.actor.Actor.Receive
import akka.actor._

object MessageRouterDriver extends CompletableApp(20) {
}

class AlternatingRouter(processor1: ActorRef, processor2: ActorRef) extends Actor {
  var alternate = 1

  def alternateProcessor() = {
    if (alternate == 1) {
      alternate = 2
      processor1
    } else {
      alternate = 1
      processor2
    }
  }

  override def receive: Receive = {
    case message: Any =>
      val processor = alternateProcessor()
      println(s"AlternatingRouter: routing $message to ${processor.path.name}")
      processor ! message
      MessageRouterDriver.completedStep()
  }
}
