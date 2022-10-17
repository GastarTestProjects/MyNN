package org.xe72.simplecreature

import org.xe72.nets.custom.NeuralNet
import org.xe72.simplecreature.view.CreatureForm

fun main() {

    val nn = NeuralNet(0.5, 8, 10, 10, 4)
    NetTeacher(nn).startTeaching(100_000)
    val creature = Creature(nn)

    val f = CreatureForm(creature)
    Thread(f).start()
}