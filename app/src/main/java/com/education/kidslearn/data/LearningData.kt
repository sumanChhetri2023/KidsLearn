package com.education.kidslearn.data

import androidx.compose.ui.graphics.Color

data class LearningCategory(
    val name: String,
    val emoji: String,
    val color: Color
)

data class LearningItem(
    val name: String,
    val emoji: String,
    val color: Color,
    val assetPath: String? = null
)

val categories = listOf(
    LearningCategory("Animals", "🐘", Color(0xFFE3F2FD)),
    LearningCategory("Birds", "🦜", Color(0xFFFFF3E0)),
    LearningCategory("Vehicles", "🚗", Color(0xFFE8F5E9)),
    LearningCategory("Fruits", "🍎", Color(0xFFFFEBEE)),
    LearningCategory("Flowers", "🌻", Color(0xFFFFF9C4)),
    LearningCategory("Clothes", "👕", Color(0xFFEDE7F6)),
    LearningCategory("Insects", "🦋", Color(0xFFF3E5F5)),
    LearningCategory("Food", "🍕", Color(0xFFFFE0B2)),
    LearningCategory("Numbers", "🔢", Color(0xFFE0F7FA)),
    LearningCategory("Shapes", "⭐", Color(0xFFFCE4EC)),
    LearningCategory("Quiz", "🎯", Color(0xFFE0F2F1))
)

private val animals = listOf(
    LearningItem("Lion", "🦁", Color(0xFFFFF3E0), "animals/lion.png"),
    LearningItem("Elephant", "🐘", Color(0xFFE3F2FD), "animals/elephant.png"),
    LearningItem("Panda", "🐼", Color(0xFFF5F5F5), "animals/panda.png"),
    LearningItem("Monkey", "🐒", Color(0xFFFFF8E1), "animals/monkey.png"),
    LearningItem("Rabbit", "🐰", Color(0xFFFCE4EC), "animals/rabbit.png"),
    LearningItem("Tiger", "🐯", Color(0xFFFFE0B2), "animals/tiger.png"),
    LearningItem("Dog", "🐶", Color(0xFFFFF3E0), "animals/dog.png"),
    LearningItem("Cat", "🐱", Color(0xFFEDE7F6), "animals/cat.png"),
    LearningItem("Cow", "🐮", Color(0xFFE8F5E9), "animals/cow.png"),
    LearningItem("Horse", "🐴", Color(0xFFFFF8E1), "animals/horse.png"),
    LearningItem("Goat", "🐐", Color(0xFFE0F7FA), "animals/goat.png"),
    LearningItem("Sheep", "🐑", Color(0xFFF3E5F5), "animals/sheep.png"),
    LearningItem("Giraffe", "🦒", Color(0xFFFFF9C4), "animals/giraffe.png"),
    LearningItem("Zebra", "🦓", Color(0xFFF5F5F5), "animals/zebra.png"),
    LearningItem("Bear", "🐻", Color(0xFFFFE0B2), "animals/bear.png"),
    LearningItem("Fox", "🦊", Color(0xFFFFF3E0), "animals/fox.png"),
    LearningItem("Deer", "🦌", Color(0xFFE8F5E9), "animals/deer.png"),
    LearningItem("Wolf", "🐺", Color(0xFFE3F2FD), "animals/wolf.png"),
    LearningItem("Kangaroo", "🦘", Color(0xFFFFF8E1), "animals/kangaroo.png"),
    LearningItem("Hippopotamus", "🦛", Color(0xFFEDE7F6), "animals/hippo.png")
)

private val birds = listOf(
    LearningItem("Parrot", "🦜", Color(0xFFE8F5E9), assetPath = "birds/parrot.png"),
    LearningItem("Peacock", "🦚", Color(0xFFE3F2FD), assetPath = "birds/peacock.png"),
    LearningItem("Owl", "🦉", Color(0xFFFFF3E0), assetPath = "birds/owl.png"),
    LearningItem("Eagle", "🦅", Color(0xFFEDE7F6), assetPath = "birds/eagle.png"),
    LearningItem("Duck", "🦆", Color(0xFFFFF9C4), assetPath = "birds/duck.png"),
    LearningItem("Chicken", "🐔", Color(0xFFFFEBEE), assetPath = "birds/chicken.png"),
    LearningItem("Rooster", "🐓", Color(0xFFFFF3E0), assetPath = "birds/rooster.png"),
    LearningItem("Swan", "🦢", Color(0xFFE0F7FA), assetPath = "birds/swan.png"),
    LearningItem("Flamingo", "🦩", Color(0xFFFCE4EC), assetPath = "birds/flamingo.png"),
    LearningItem("Penguin", "🐧", Color(0xFFE3F2FD), assetPath = "birds/penguin.png"),
    LearningItem("Dove", "🕊️", Color(0xFFF5F5F5), assetPath = "birds/dove.png"),
    LearningItem("Turkey", "🦃", Color(0xFFFFE0B2), assetPath = "birds/turkey.png"),
    LearningItem("Woodpecker", "🐦", Color(0xFFE8F5E9), assetPath = "birds/woodpecker.png"),
    LearningItem("Kingfisher", "🐦", Color(0xFFE0F7FA), assetPath = "birds/kingfisher.png"),
    LearningItem("Crow", "🐦‍⬛", Color(0xFFEDE7F6), assetPath = "birds/crow.png"),
    LearningItem("Sparrow", "🐦", Color(0xFFFFF8E1), assetPath = "birds/sparrow.png"),
    LearningItem("Pigeon", "🕊️", Color(0xFFE3F2FD), assetPath = "birds/pigeon.png"),
    LearningItem("Hummingbird", "🐦", Color(0xFFF3E5F5), assetPath = "birds/hummingbird.png"),
    LearningItem("Ostrich", "🪶", Color(0xFFFFF3E0), assetPath = "birds/ostrich.png"),
    LearningItem("Crane", "🐦", Color(0xFFE8F5E9), assetPath = "birds/crane.png")
)

private val vehicles = listOf(
    LearningItem("Car", "🚗", Color(0xFFE3F2FD), "vehicles/car.png"),
    LearningItem("Bus", "🚌", Color(0xFFFFF9C4), "vehicles/bus.png"),
    LearningItem("Train", "🚆", Color(0xFFE8F5E9), "vehicles/train.png"),
    LearningItem("Airplane", "✈️", Color(0xFFE0F7FA), "vehicles/airplane.png"),
    LearningItem("Bicycle", "🚲", Color(0xFFFCE4EC), "vehicles/bicycle.png"),
    LearningItem("Motorcycle", "🏍️", Color(0xFFFFE0B2), "vehicles/motorcycle.png"),
    LearningItem("Truck", "🚚", Color(0xFFEDE7F6), "vehicles/truck.png"),
    LearningItem("Boat", "⛵", Color(0xFFE0F7FA), "vehicles/boat.png"),
    LearningItem("Helicopter", "🚁", Color(0xFFFFF3E0), "vehicles/helicopter.png"),
    LearningItem("Tractor", "🚜", Color(0xFFE8F5E9), "vehicles/tractor.png"),
    LearningItem("Rocket", "🚜", Color(0xFFE8F5E9), "vehicles/rocket.png"),
    LearningItem("Submarine", "🚜", Color(0xFFE8F5E9), "vehicles/submarine.png"),
    LearningItem("Scooter", "🚜", Color(0xFFE8F5E9), "vehicles/scooter.png"),
    LearningItem("Ambulance", "🚜", Color(0xFFE8F5E9), "vehicles/ambulance.png"),
    LearningItem("Fire truck", "🚜", Color(0xFFE8F5E9), "vehicles/firetruck.png"),
    LearningItem("Skateboard", "🚜", Color(0xFFE8F5E9), "vehicles/skateboard.png"),
    LearningItem("Hot Air Balloon", "🚜", Color(0xFFE8F5E9), "vehicles/hotairballoon.png"),
    LearningItem("Monster truck", "🚜", Color(0xFFE8F5E9), "vehicles/monstertruck.png"),
    LearningItem("Jet Ski", "🚜", Color(0xFFE8F5E9), "vehicles/jetski.png"),
    LearningItem("Ice Cream truck", "🚜", Color(0xFFE8F5E9), "vehicles/icecreamtruck.png")
)

private val fruits = listOf(
    LearningItem("Apple", "🍎", Color(0xFFFFEBEE), "fruits/apple.png"),
    LearningItem("Banana", "🍌", Color(0xFFFFF9C4), "fruits/banana.png"),
    LearningItem("Orange", "🍊", Color(0xFFFFF3E0), "fruits/orange.png"),
    LearningItem("Mango", "🥭", Color(0xFFFFE0B2), "fruits/mango.png"),
    LearningItem("Grapes", "🍇", Color(0xFFEDE7F6), "fruits/grapes.png"),
    LearningItem("Watermelon", "🍉", Color(0xFFE8F5E9), "fruits/watermelon.png"),
    LearningItem("Pineapple", "🍍", Color(0xFFFFF9C4), "fruits/pineapple.png"),
    LearningItem("Strawberry", "🍓", Color(0xFFFCE4EC), "fruits/strawberry.png"),
    LearningItem("Pear", "🍐", Color(0xFFE8F5E9), "fruits/pear.png"),
    LearningItem("Peach", "🍑", Color(0xFFFFE0B2), "fruits/peach.png"),
    LearningItem("Avocado", "🍑", Color(0xFFFFE0B2), "fruits/avocado.png"),
    LearningItem("Cherry", "🍑", Color(0xFFFFE0B2), "fruits/cherry.png"),
    LearningItem("Coconut", "🍑", Color(0xFFFFE0B2), "fruits/coconut.png"),
    LearningItem("Guava", "🍑", Color(0xFFFFE0B2), "fruits/guava.png"),
    LearningItem("Kiwi", "🍑", Color(0xFFFFE0B2), "fruits/kiwi.png"),
    LearningItem("Papaya", "🍑", Color(0xFFFFE0B2), "fruits/papaya.png"),
    LearningItem("Plum", "🍑", Color(0xFFFFE0B2), "fruits/plum.png"),
    LearningItem("Pomegranate", "🍑", Color(0xFFFFE0B2), "fruits/pomegranate.png"),
    LearningItem("Raspberry", "🍑", Color(0xFFFFE0B2), "fruits/raspberries.png")
)

private val flowers = listOf(
    LearningItem("Rose", "🌹", Color(0xFFFFEBEE), "flowers/rose.png"),
    LearningItem("Sunflower", "🌻", Color(0xFFFFF9C4), "flowers/sunflower.png"),
    LearningItem("Tulip", "🌷", Color(0xFFFCE4EC), "flowers/tulip.png"),
    LearningItem("Lotus", "🪷", Color(0xFFF3E5F5), "flowers/lotus.png"),
    LearningItem("Daisy", "🌼", Color(0xFFFFF9C4), "flowers/daisy.png"),
    LearningItem("Hibiscus", "🌺", Color(0xFFFFEBEE), "flowers/hibiscus.png"),
    LearningItem("Lily", "🪷", Color(0xFFEDE7F6), "flowers/lily.png"),
    LearningItem("Dahlia", "🌼", Color(0xFFE8F5E9), "flowers/dahlia.png"),
    LearningItem("Jasmine", "🌼", Color(0xFFE8F5E9), "flowers/jasmine.png"),
    LearningItem("Dandelion", "🌼", Color(0xFFE8F5E9), "flowers/dandelion.png"),
    LearningItem("Aster", "🌼", Color(0xFFE8F5E9), "flowers/aster.png"),
    LearningItem("Bluebell", "🌼", Color(0xFFE8F5E9), "flowers/bluebell.png"),
    LearningItem("Carnation", "🌼", Color(0xFFE8F5E9), "flowers/carnation.png"),
    LearningItem("Pansy", "🌼", Color(0xFFE8F5E9), "flowers/pansy.png"),
    LearningItem("Cherry Blossom", "🌼", Color(0xFFE8F5E9), "flowers/cherryblossom.png"),
    LearningItem("Daffodil", "🌼", Color(0xFFE8F5E9), "flowers/daffodil.png"),
    LearningItem("Lavender", "🌼", Color(0xFFE8F5E9), "flowers/lavender.png"),
    LearningItem("Marigold", "🌼", Color(0xFFE8F5E9), "flowers/marigold.png"),
    LearningItem("Orchid", "🌼", Color(0xFFE8F5E9), "flowers/orchid.png"),
    LearningItem("Poppy", "🌼", Color(0xFFE8F5E9), "flowers/poppy.png")
)

private val clothes = listOf(
    LearningItem("Shirt", "👕", Color(0xFFE3F2FD), "clothes/shirt.png"),
    LearningItem("Trousers", "👖", Color(0xFFEDE7F6), "clothes/trousers.png"),
    LearningItem("Dress", "👗", Color(0xFFFCE4EC), "clothes/dress.png"),
    LearningItem("Sweater", "🧥", Color(0xFFE3F2FD), "clothes/sweater.png"),
    LearningItem("T-shirt", "👟", Color(0xFFE8F5E9), "clothes/tshirt.png"),
    LearningItem("Hat", "👒", Color(0xFFFFF9C4), "clothes/hat.png"),
    LearningItem("Socks", "🧦", Color(0xFFFFE0B2), "clothes/socks.png"),
    LearningItem("Jacket", "🧥", Color(0xFFE3F2FD), "clothes/jacket.png"),
    LearningItem("Skirt", "🧥", Color(0xFFE3F2FD), "clothes/skirt.png"),
    LearningItem("Cap", "🧥", Color(0xFFE3F2FD), "clothes/cap.png"),
    LearningItem("Gloves", "🧥", Color(0xFFE3F2FD), "clothes/gloves.png"),
    LearningItem("Coat", "🧥", Color(0xFFE3F2FD), "clothes/coat.png"),
    LearningItem("Mittens", "🧥", Color(0xFFE3F2FD), "clothes/mittens.png"),
    LearningItem("Pajamas", "🧥", Color(0xFFE3F2FD), "clothes/pajama.png"),
    LearningItem("Raincoat", "🧥", Color(0xFFE3F2FD), "clothes/raincoat.png"),
    LearningItem("Scarf", "🧥", Color(0xFFE3F2FD), "clothes/scarf.png"),
    LearningItem("Vest", "👗", Color(0xFFF3E5F5), "clothes/vest.png"),
    LearningItem("Shorts", "🧥", Color(0xFFE3F2FD), "clothes/shorts.png"),
    LearningItem("Tracksuit", "🧥", Color(0xFFE3F2FD), "clothes/tracksuit.png"),
    LearningItem("Swimwear", "🧥", Color(0xFFE3F2FD), "clothes/swimwear.png")
)

private val insects = listOf(
    LearningItem("Butterfly", "🦋", Color(0xFFF3E5F5), "insects/butterfly.png"),
    LearningItem("Centipede", "🦟", Color(0xFFE0F7FA), "insects/centipede.png"),
    LearningItem("Bumblebee", "🐝", Color(0xFFFFF9C4), "insects/bumblebee.png"),
    LearningItem("Ant", "🐜", Color(0xFFFFE0B2), "insects/ant.png"),
    LearningItem("Cicada", "🦟", Color(0xFFE0F7FA), "insects/cicada.png"),
    LearningItem("Ladybug", "🐞", Color(0xFFFFEBEE), "insects/ladybug.png"),
    LearningItem("Grasshopper", "🦗", Color(0xFFE8F5E9), "insects/grasshopper.png"),
    LearningItem("Spider", "🕷️", Color(0xFFEDE7F6), "insects/spider.png"),
    LearningItem("Mosquito", "🦟", Color(0xFFE0F7FA), "insects/mosquito.png"),
    LearningItem("Beetle", "🦟", Color(0xFFE0F7FA), "insects/beetle.png"),
    LearningItem("Caterpillar", "🦟", Color(0xFFE0F7FA), "insects/caterpillar.png"),
    LearningItem("Firefly", "🦟", Color(0xFFE0F7FA), "insects/firefly.png"),
    LearningItem("Housefly", "🦟", Color(0xFFE0F7FA), "insects/housefly.png"),
    LearningItem("Leaf insect", "🦟", Color(0xFFE0F7FA), "insects/leafinsect.png"),
    LearningItem("Cockroach", "🦟", Color(0xFFE0F7FA), "insects/cockroach.png"),
    LearningItem("Praying Mantis", "🦟", Color(0xFFE0F7FA), "insects/mantis.png"),
    LearningItem("Moth", "🦟", Color(0xFFE0F7FA), "insects/moth.png"),
    LearningItem("Cricket", "🦟", Color(0xFFE0F7FA), "insects/cricket.png"),
    LearningItem("Termite", "🦟", Color(0xFFE0F7FA), "insects/termite.png"),
    LearningItem("Dragonfly", "🪰", Color(0xFFE3F2FD), "insects/dragonfly.png")
)

private val food = listOf(
    LearningItem("Pizza", "🍕", Color(0xFFFFE0B2)),
    LearningItem("Burger", "🍔", Color(0xFFFFF3E0)),
    LearningItem("Bread", "🍞", Color(0xFFFFF9C4)),
    LearningItem("Rice", "🍚", Color(0xFFF5F5F5)),
    LearningItem("Egg", "🥚", Color(0xFFFFF8E1)),
    LearningItem("Cheese", "🧀", Color(0xFFFFF9C4)),
    LearningItem("Cake", "🎂", Color(0xFFFCE4EC)),
    LearningItem("Sandwich", "🥪", Color(0xFFE8F5E9))
)

private val numbers = (1..20).map { number ->
    LearningItem(number.toString(), number.toString(), Color(0xFFE0F7FA))
}

private val shapes = listOf(
    LearningItem("Circle", "⚪", Color(0xFFE3F2FD)),
    LearningItem("Square", "🟦", Color(0xFFE8F5E9)),
    LearningItem("Triangle", "🔺", Color(0xFFFFEBEE)),
    LearningItem("Rectangle", "🟩", Color(0xFFE0F7FA)),
    LearningItem("Star", "⭐", Color(0xFFFFF9C4)),
    LearningItem("Heart", "❤️", Color(0xFFFCE4EC)),
    LearningItem("Diamond", "🔷", Color(0xFFEDE7F6)),
    LearningItem("Oval", "🥚", Color(0xFFFFF3E0))
)

/*
 * Each category is defined once above.
 * The Quiz reuses those same lists, so items added to a category
 * are automatically included in the quiz source.
 */
val categoryItems: Map<String, List<LearningItem>> = mapOf(
    "Animals" to animals,
    "Birds" to birds,
    "Vehicles" to vehicles,
    "Fruits" to fruits,
    "Flowers" to flowers,
    "Clothes" to clothes,
    "Insects" to insects,
    "Food" to food,
    "Numbers" to numbers,
    "Shapes" to shapes,
    "Quiz" to (animals + birds + vehicles + fruits + flowers + clothes + insects + food + numbers + shapes)
)
