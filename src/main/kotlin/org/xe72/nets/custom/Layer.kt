package org.xe72.nets.custom

import kotlin.random.Random

class Layer(neuronCount: Int) {
    val neurons: List<Neuron>

    init {
        val tmpNeurons = mutableListOf<Neuron>()
        repeat(neuronCount) { tmpNeurons.add(Neuron()) }
        neurons = tmpNeurons.toList()
    }

    /**
     * Связываем нейроны слоев друг с другом и задаём случайные веса
     */
    fun setNextLayer(nextLayer: Layer) {
        nextLayer.neurons.forEach {nextNeuron ->
            nextNeuron.prevNeurons.putAll(this.neurons.map {
                it.nextNeurons.add(nextNeuron) // Заодно устанавливаем связь от нейрона к нейрону следующего уровня
                it to Random.nextDouble()
            })
        }
    }
}