
Repositório criado para demostração de uma aplicação Scala rodando no Spark para processamento de Tweets, também estamos utilizando uma aplicação Python para direcinamento das mensagens do twitter via socket para serem consumidas pela aplicação Spark, que fara o processamentos dos dados irá armazena-los no Slasticsearch para exibição dos dados via Kibana.

# Instruções para execução

## Requisitos

- [Python 3.6](https://www.python.org/download/releases/3.0/)
    - [Tweepy](https://github.com/tweepy/tweepy)
- [Scala 2.11.12](https://www.scala-lang.org/download/2.11.12.html)
- [SBT 0.13.17](https://www.scala-sbt.org/download.html)
- [Spark 2.3.2](http://spark.apache.org/downloads.html)
- [ElasticSearch](https://www.elastic.co/downloads/elasticsearch)
- [Kibana](https://www.elastic.co/downloads/kibana)
- [ES-Hadoop](https://www.elastic.co/downloads/hadoop)
- [JDK 1.8](https://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html)

## Variáveis de Ambiente

Meu ~/.bash_profile


```bash
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/
export SPARK_HOME=/Users/lfreire/server/spark-2.3.2-bin-hadoop2.7
export SBT_HOME=/Users/lfreire/server/sbt
export SCALA_HOME=/Users/lfreire/server/scala-2.11.12
export PATH=$JAVA_HOME/bin:$SBT_HOME/bin:$SBT_HOME/lib:$SCALA_HOME/bin:$SCALA_HOME/lib:$PATH
export PATH=$JAVA_HOME/bin:$SPARK_HOME:$SPARK_HOME/bin:$SPARK_HOME/sbin:$PATH
export PYSPARK_PYTHON=python3

PATH=“/Library/Frameworks/Python.framework/Version/3.6/bin:${PATH}”
export PATH
```

## Chaves API do Twitter

As chaves devem ser configuras no arquivo twitter.conf, temos um exemplo chamado twitter.conf.example

## Execução 

Ligando o Twitter Streaming

```bash
python3 twitter_streaming.py <host> <porta> <filtro>
```

Execução da aplicação em Scala no Spark

```bash
spark-submit --class Bigdata.Twitter.App bigdata-spark-twitter.jar <host socket> <porta socket> <pasta para checkpoint>
```
