package Bigdata.Twitter

import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.Encoder
import org.apache.spark.sql.streaming.OutputMode._
import org.apache.spark.sql.streaming.Trigger
import org.elasticsearch.spark.streaming.EsSparkStreaming
import org.elasticsearch.spark.sql._
import org.apache.spark.sql.streaming.OutputMode

class Monitor(val socketConfig: SocketConfig, val elasticSearchConfig: ElasticSearchConfig) {
   
  def start(){
      val spark = SparkSession
         .builder
         .appName("TwitterMonitor")
         .master("local[2]")
         .getOrCreate()
       
    import spark.implicits._
     
    val socketData = spark.readStream
       .format("socket")
       .option("host", socketConfig.host)
       .option("port", socketConfig.port)
       .load()

   spark.sparkContext.setLogLevel("ERROR")
   
   val socketDataText = socketData.select($"value" as "text")
     .filter(c => c(0).toString.trim.length() > 0)
     .as[SocketData]

   val tweets = socketDataText.flatMap(t => t.text.split(" ").map(p => Tweety(p.trim.toUpperCase(), t.text)))
      .filter(c => c.hashTag.length() > 0 && c.hashTag(0) == '#')

   val query = tweets.writeStream
     .format("org.elasticsearch.spark.sql")
     .outputMode(OutputMode.Append())
     .option("checkpointLocation", elasticSearchConfig.checkPointLocation)
     .start(elasticSearchConfig.destinationPath)
   
   query.awaitTermination()
  }
}