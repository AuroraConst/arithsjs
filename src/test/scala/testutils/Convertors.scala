package com.axiom.testutils



/**
  * FileReader object to read files from the file system and creates a string dsl for platorm independent paths
  */
import typings.std.Map as TsMap
import scala.collection.mutable
object Convertors:

  extension [K,V] (m:TsMap[K, V])
    def toScalaMap: Map[K, V] = 
      var scalaMutMap = mutable.Map[K, V]()
      m.forEach{(v, k, _) => scalaMutMap += (k -> v)}
      scalaMutMap.toMap

  