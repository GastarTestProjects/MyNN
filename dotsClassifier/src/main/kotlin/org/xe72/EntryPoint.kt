package org.xe72

import org.xe72.nets.custom.NeuralNet
import org.xe72.nets.deeplearning.DeepLearningNet
import org.xe72.view.FormDots

fun main() {
    val nn = NeuralNet(0.05, 2, 5, 5, 2)
//    val nn = DeepLearningNet()

    val f = FormDots(nn)
    Thread(f).start()
}