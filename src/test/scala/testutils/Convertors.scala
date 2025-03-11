package com.axiom.testutils

import scala.scalajs.js
import scala.scalajs.js.annotation.*
import scala.util.Try

import scala.scalajs.js.Dynamic.global

/**
  * FileReader object to read files from the file system and creates a string dsl for platorm independent paths
  */
import typings.std.Map as TsMap
import scala.scalajs.js.JSConverters.*
import scala.collection.mutable
object Convertors:

  extension [K,V] (m:TsMap[K, V])
    def toScalaMap: Map[K, V] = 
      var scalaMap = Map[K, V]()
      m.forEach{(v, k, _) => scalaMap += (k -> v)}
      scalaMap.toMap

  