package org.xe72.simplecreature.additional

data class ObjectsAroundState(var top: Double, var right: Double, var bottom: Double, var left: Double) {
    fun toList(): List<Double> =
        listOf(top, right, bottom, left)
}