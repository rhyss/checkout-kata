class Till(rules: PriceRules) {
  def checkout(basket: Basket): Receipt = Receipt(0)
}

case class PriceRules(unitPrices: Map[Sku, Price])

case class Basket(items: List[Sku] = Nil)

case class Sku(value: Char)

case class Receipt(cost: Int) {
}

case class Price(value: Int)