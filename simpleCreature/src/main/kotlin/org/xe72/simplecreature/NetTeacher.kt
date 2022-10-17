package org.xe72.simplecreature

import org.xe72.nets.INeuralNet
import kotlin.random.Random

class NetTeacher(val nn: INeuralNet) {

    fun startTeaching(iterCount: Int = 10000) {
        repeat(iterCount) {
            val foodCoord = mutableListOf(0.0, 0.0, 0.0, 0.0)
            val enemyCoord = mutableListOf(0.0, 0.0, 0.0, 0.0)
            val targetValues = mutableListOf(0.0, 0.0, 0.0, 0.0)
            val value = Random.nextDouble()
            val direction = Random.nextInt(4)
            foodCoord[direction] = value
            targetValues[direction] = 1.0
            nn.feedLearningData(foodCoord.plus(enemyCoord), targetValues)
        }
    }
}