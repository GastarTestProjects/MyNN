package org.xe72.nets

interface INeuralNet {
    fun feedLearningData(inputs: List<Double>, targetOutputs: List<Double>)

    fun feedForward(inputs: List<Double>): List<Double>
}