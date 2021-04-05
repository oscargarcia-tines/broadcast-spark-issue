import org.apache.spark.sql.functions.broadcast
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

object myObject extends App {

  //Starting the session
  val spark = SparkSession
    .builder()
    .master("local")
    .appName("Test")
    .getOrCreate()

  //2 datasets,
  //1st with numbers 1 to 1000
  //2nd dataset only the even numbers
  val dataLeft =  ( 1 to 1000).
    map( x => Row(x, "NUMBER: " + x.toString))

  val leftDF = spark.createDataFrame(spark.sparkContext.parallelize(dataLeft),
    StructType(
      List(
        StructField("id", IntegerType),
        StructField("description", StringType)
      )
    ))

  val dataRight = ( 1 to 1000).
    filter( x => x % 2 == 0).
    map( x => Row(x, "IS EVEN"))

  val rightDF = spark.createDataFrame(spark.sparkContext.parallelize(dataRight),
    StructType(
      List(
        StructField("id", IntegerType),
        StructField("type", StringType)
      )
    ))

  //We join the dataframes with the right dataframe broadcasted and we print the explain
  println("Not persisted:")

  leftDF.join(broadcast(rightDF), leftDF("id") === rightDF("id"), "leftOuter").explain()


  //We join the dataframes with the right dataframe broadcasted and persisted, we print the explain
  println("Persisted:")

  rightDF.persist()

  leftDF.join(broadcast(rightDF), leftDF("id") === rightDF("id"), "leftOuter").explain()

}
