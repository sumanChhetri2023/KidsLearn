package com.education.kidslearn.data

import androidx.compose.ui.graphics.Color
import java.util.Locale

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

val categoryItems: Map<String, List<LearningItem>> = mapOf(
    "Animals" to animals,
    "Birds" to birds,
    "Quiz" to (animals + birds + listOf(
        LearningItem("Car", "🚗", Color(0xFFE3F2FD), "vehicles/car.png"),
        LearningItem("Bus", "🚌", Color(0xFFFFF9C4), "vehicles/bus.png"),
        LearningItem("Train", "🚆", Color(0xFFE8F5E9), "vehicles/train.png"),
        LearningItem("Airplane", "✈️", Color(0xFFE0F7FA), "vehicles/airplane.png"),
        LearningItem("Bicycle", "🚲", Color(0xFFFCE4EC), "vehicles/bicycle.png"),
        LearningItem("Motorcycle", "🏍️", Color(0xFFFFE0B2), "vehicles/motorcycle.png"),
        LearningItem("Truck", "🚚", Color(0xFFEDE7F6), "vehicles/truck.png"),
        LearningItem("Boat", "⛵", Color(0xFFE0F7FA), "vehicles/boat.png"),
        LearningItem("Helicopter", "🚁", Color(0xFFFFF3E0), "vehicles/helicopter.png"),
        LearningItem("Tractor", "🚜", Color(0xFFE8F5E9), "vehicles/tractor.png")
    )),

    "Vehicles" to listOf(
        LearningItem("Car", "🚗", Color(0xFFE3F2FD), assetPath = "vehicles/car.png"),
        LearningItem("Bus", "🚌", Color(0xFFFFF9C4), assetPath = "vehicles/bus.png"),
        LearningItem("Train", "🚆", Color(0xFFE8F5E9), assetPath = "vehicles/train.png"),
        LearningItem("Airplane", "✈️", Color(0xFFE0F7FA), assetPath = "vehicles/airplane.png"),
        LearningItem("Bicycle", "🚲", Color(0xFFFCE4EC), assetPath = "vehicles/bicycle.png"),
        LearningItem("Motorcycle", "🏍️", Color(0xFFFFE0B2), assetPath = "vehicles/motorcycle.png"),
        LearningItem("Truck", "🚚", Color(0xFFEDE7F6), assetPath = "vehicles/truck.png"),
        LearningItem("Boat", "⛵", Color(0xFFE3F2FD), assetPath = "vehicles/boat.png"),
        LearningItem("Helicopter", "🚁", Color(0xFFFFF3E0), assetPath = "vehicles/helicopter.png"),
        LearningItem("Tractor", "🚜", Color(0xFFE8F5E9), assetPath = "vehicles/tractor.png")
    ),

    "Fruits" to listOf(
        LearningItem("Apple", "🍎", Color(0xFFFFEBEE)),
        LearningItem("Banana", "🍌", Color(0xFFFFF9C4)),
        LearningItem("Orange", "🍊", Color(0xFFFFF3E0)),
        LearningItem("Mango", "🥭", Color(0xFFFFE0B2)),
        LearningItem("Grapes", "🍇", Color(0xFFEDE7F6)),
        LearningItem("Watermelon", "🍉", Color(0xFFE8F5E9)),
        LearningItem("Pineapple", "🍍", Color(0xFFFFF9C4)),
        LearningItem("Strawberry", "🍓", Color(0xFFFCE4EC)),
        LearningItem("Pear", "🍐", Color(0xFFE8F5E9)),
        LearningItem("Peach", "🍑", Color(0xFFFFE0B2))
    ),

    "Flowers" to listOf(
        LearningItem("Rose", "🌹", Color(0xFFFFEBEE)),
        LearningItem("Sunflower", "🌻", Color(0xFFFFF9C4)),
        LearningItem("Tulip", "🌷", Color(0xFFFCE4EC)),
        LearningItem("Lotus", "🪷", Color(0xFFF3E5F5)),
        LearningItem("Daisy", "🌼", Color(0xFFFFF9C4)),
        LearningItem("Hibiscus", "🌺", Color(0xFFFFEBEE)),
        LearningItem("Lily", "🪷", Color(0xFFEDE7F6)),
        LearningItem("Jasmine", "🌼", Color(0xFFE8F5E9))
    ),

    "Clothes" to listOf(
        LearningItem("Shirt", "👕", Color(0xFFE3F2FD)),
        LearningItem("Trousers", "👖", Color(0xFFEDE7F6)),
        LearningItem("Dress", "👗", Color(0xFFFCE4EC)),
        LearningItem("Shoes", "👟", Color(0xFFE8F5E9)),
        LearningItem("Hat", "👒", Color(0xFFFFF9C4)),
        LearningItem("Socks", "🧦", Color(0xFFFFE0B2)),
        LearningItem("Jacket", "🧥", Color(0xFFE3F2FD)),
        LearningItem("Skirt", "👗", Color(0xFFF3E5F5))
    ),

    "Insects" to listOf(
        LearningItem("Butterfly", "🦋", Color(0xFFF3E5F5)),
        LearningItem("Bee", "🐝", Color(0xFFFFF9C4)),
        LearningItem("Ant", "🐜", Color(0xFFFFE0B2)),
        LearningItem("Ladybug", "🐞", Color(0xFFFFEBEE)),
        LearningItem("Grasshopper", "🦗", Color(0xFFE8F5E9)),
        LearningItem("Spider", "🕷️", Color(0xFFEDE7F6)),
        LearningItem("Mosquito", "🦟", Color(0xFFE0F7FA)),
        LearningItem("Dragonfly", "🪰", Color(0xFFE3F2FD))
    ),

    "Food" to listOf(
        LearningItem("Pizza", "🍕", Color(0xFFFFE0B2)),
        LearningItem("Burger", "🍔", Color(0xFFFFF3E0)),
        LearningItem("Bread", "🍞", Color(0xFFFFF9C4)),
        LearningItem("Rice", "🍚", Color(0xFFF5F5F5)),
        LearningItem("Egg", "🥚", Color(0xFFFFF8E1)),
        LearningItem("Cheese", "🧀", Color(0xFFFFF9C4)),
        LearningItem("Cake", "🎂", Color(0xFFFCE4EC)),
        LearningItem("Sandwich", "🥪", Color(0xFFE8F5E9))
    ),

    "Numbers" to (1..20).map { number ->
        LearningItem(
            number.toString(),
            number.toString(),
            Color(0xFFE0F7FA)
        )
    },

    "Shapes" to listOf(
        LearningItem("Circle", "⚪", Color(0xFFE3F2FD)),
        LearningItem("Square", "🟦", Color(0xFFE8F5E9)),
        LearningItem("Triangle", "🔺", Color(0xFFFFEBEE)),
        LearningItem("Rectangle", "🟩", Color(0xFFE0F7FA)),
        LearningItem("Star", "⭐", Color(0xFFFFF9C4)),
        LearningItem("Heart", "❤️", Color(0xFFFCE4EC)),
        LearningItem("Diamond", "🔷", Color(0xFFEDE7F6)),
        LearningItem("Oval", "🥚", Color(0xFFFFF3E0))
    )
)

