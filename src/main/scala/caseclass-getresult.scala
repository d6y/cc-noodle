import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._

object Example extends App {

  import slick.driver.H2Driver.api._
  import shapeless._
  import slickless._

  import slick.jdbc.{ GetResult, PositionedResult }

  implicit object hnilGetResult extends GetResult[HNil] {
    def apply(r: PositionedResult) = HNil
  }

  implicit def hlistConsGetResult[H, T <: HList]
    (implicit
      h: GetResult[H],
      t: GetResult[T]
    ) =
      new GetResult[H :: T] {
        def apply(r: PositionedResult) = (r << h) :: t(r)
      }

  implicit def caseClassGetResult[C, H <: HList]
    (implicit
      gen:       Generic.Aux[C, H],
      getResult: GetResult[H]
    ): GetResult[C] =
      new GetResult[C] {
        def apply(r: PositionedResult) = gen.from(getResult(r))
      }

  /*

  Definition of GetResult:

  https://github.com/slick/slick/blob/e9ab33083bfa1ae642a93d4e52b4ac87b42dc917/slick/src/main/scala/slick/jdbc/GetResult.fm

  trait GetResult[+T] extends (PositionedResult => T) { self =>
    override def andThen[A](g: T => A): GetResult[A] = new GetResult[A] { def apply(rs: PositionedResult): A = g(self.apply(rs)) }
  }

  */

  // Two case classes...
  case class SimpleInt(x: Int)
  case class SimpleString(x: String)

  // Their correspoding HList representations exist:
  implicitly[GetResult[String :: HNil]]
  implicitly[GetResult[Int :: HNil]]

  // Create the Generic instances for the case classes:
  implicit val genSimpleString = Generic[SimpleString]
  implicit val genSimpleInt = Generic[SimpleInt]

  // PROBLEM:
  // With both the above generics available (genSimpleString, genSimpleInt),
  // the following will fail:
  implicitly[GetResult[SimpleInt]]
  // "could not find implicit value for parameter e: slick.jdbc.GetResult[CaseClassGetResult.SimpleInt]"
  // Comment out the genSimpleString and we succeed.

/*
  Manually, we can create the instance:

  val gr0: GetResult[SimpleInt] =
    caseClassGetResult[SimpleInt, Int :: HNil](
      Generic[SimpleInt],
      GetResult[Int :: HNil])

  Also OK:

  val gr1: GetResult[SimpleInt] = caseClassGetResult[SimpleInt, Int :: HNil]
  implicitly[GetResult[SimpleInt]](gr1)
*/

}