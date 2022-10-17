package org.xe72.simplecreature

import org.xe72.nets.INeuralNet
import org.xe72.simplecreature.additional.ObjectTypes
import org.xe72.simplecreature.additional.ObjectsAroundState
import kotlin.math.abs

class Creature(val nn: INeuralNet) {

    fun makeTurn(scanner: (ObjectTypes) -> ObjectsAroundState, mover: (Int, Int) -> Boolean) {
        val nnResult =
            nn.feedForward(listOf(scanner(ObjectTypes.FOOD).toList().plus(scanner(ObjectTypes.ENEMY).toList())))[0]

        val moveVector = calcMovement(nnResult)
        mover(moveVector.first, moveVector.second)
    }

    private fun calcMovement(nnResult: List<Double>): Pair<Int, Int> {
        val moveX =
            when {
                abs(nnResult[1] - nnResult[3]) < 0.5 -> 0
                nnResult[1] > nnResult[3] -> 1
                else -> -1
            }
        val moveY =
            when {
                abs(nnResult[2] - nnResult[0]) < 0.5 -> 0
                nnResult[2] > nnResult[0] -> 1
                else -> -1
            }
        return moveX to moveY
    }
}