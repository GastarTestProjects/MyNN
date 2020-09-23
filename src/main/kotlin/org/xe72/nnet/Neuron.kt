package org.xe72.nnet

class Neuron() {
    var value = 0.0

    var nextNeurons = mutableListOf<Neuron>()
    var prevNeurons = mutableMapOf<Neuron, Double>()
}