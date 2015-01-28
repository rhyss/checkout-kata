import org.scalacheck.Gen.containerOfN
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}

class TillSpec extends FlatSpec with GeneratorDrivenPropertyChecks with Matchers {

  "an empty basket" should "have a cost of 0" in {
    forAll(priceRulesGen) { priceRules: PriceRules =>
      println(s"PriceRules: $priceRules")
      val till = new Till(priceRules)
      till.checkout(Basket()).cost should equal(0)
    }
  }

  "a basket with items but no special offer items" should "cost the sum of every item's cost" in {
    forAll(priceRulesGen, basketGen) { (priceRules: PriceRules, basket: Basket) =>
      println(s"PriceRules: $priceRules, basket: $basket")
      val till = new Till(priceRules)
      till.checkout(basket).cost should be(
        basket.items.foldLeft(0){
          (acc, item) => acc + priceRules.unitPrices(item).value
        }
      )
    }
  }

//  val testData =
//    for {
//      skus <- SkusGen
//      pricing <-
//    }

  val skuGen = for (code <- Gen.choose('a', 'z')) yield Sku(code)

  val skusGen = Gen.containerOf[Set,Sku](skuGen)

  val priceGen = for (c <- Arbitrary.arbitrary[Int]) yield Price(c)

  val priceRulesGen = for {
    skus <- skusGen
    prices <- containerOfN[List,Price](skus.size, priceGen)
  } yield PriceRules(skus.zip(prices).toMap)

  val itemsGen = for {
    priceRules <- priceRulesGen
    price <- priceRules.unitPrices
  } yield price._1

  val basketGen = for(items <- itemsGen) yield Basket(items.toList)
}


