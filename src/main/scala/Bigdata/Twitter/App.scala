package Bigdata.Twitter

object App {
   def main(args : Array[String]): Unit = {
     
     val socketConfig = SocketConfig(args(0),args(1).toInt)
     
     val elasticSearchConfig = ElasticSearchConfig("spark/twitter", args(2))
     
     val monitor = new Monitor(socketConfig, elasticSearchConfig);
     monitor.start()
  }
}
 

