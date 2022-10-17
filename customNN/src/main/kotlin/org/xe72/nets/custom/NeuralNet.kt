package org.xe72.nets.custom

import org.xe72.nets.INeuralNet
import kotlin.math.exp

// Теоретическая часть: http://neuralnetworksanddeeplearning.com/chap1.html
// Для реализаии использовал статью: https://mattmazur.com/2015/03/17/a-step-by-step-backpropagation-example/
class NeuralNet(val learningRate: Double, vararg neuronsOnLayerCounts: Int) : INeuralNet {

    // Может сделать mutable?
    var layers: List<Layer>

    private val sigmoid = { x: Double -> 1 / (1 + exp(-x)) }

    init {
        val tmpLayers = mutableListOf<Layer>()
        neuronsOnLayerCounts.forEach {
            val curLayer = Layer(it)
            tmpLayers.add(curLayer)
            tmpLayers.getOrNull(tmpLayers.size - 2)?.setNextLayer(curLayer)
        }
        layers = tmpLayers.toList()
    }

    /**
     * Закинуть данные для обучения. Входные и ожидаемые выходные
     */
    // TODO: Добавить проверки на значения и их количество
    override fun feedLearningData(inputs: List<Double>, targetOutputs: List<Double>) {
        feedForward(listOf(inputs)) // TODO: Переделать входные параметры на List<List<Double>>
        backpropagation(targetOutputs)
    }

    /**
     * ВВод входных данных и получение выходных
     */
    // TODO: Добавить проверки на значения и их количество
    override fun feedForward(inputs: List<List<Double>>): List<List<Double>> {
        val result = mutableListOf<List<Double>>()
        inputs.forEach { currentInputs ->
            layers[0].neurons.forEachIndexed { index, neuron -> neuron.value = currentInputs[index] }
            layers.slice(1 until layers.size).forEach { layer ->
                layer.neurons.forEach { neuron ->
                    neuron.value = sigmoid(neuron.prevNeurons.map { it.key.value * it.value }.sum())
                }
            }
            result.add(layers.last().neurons.map { it.value })
        }

        return result
    }

    /**
     * Изменение весов в соответствии с ожидаемыми данными
     */
    private fun backpropagation(targetOutputs: List<Double>) {

        val newWeights = mutableMapOf<Pair<Neuron, Neuron>, Double>()
        val errors = mutableMapOf<Neuron, Double>()

        // Установка значений ошибок для выходного уровня
        layers.last().neurons.forEachIndexed { index, neuron ->
            errors[neuron] = neuron.value - targetOutputs[index]
        }

        // Расчет новых весов
        layers.reversed().forEach { layer ->
            layer.neurons.forEach { curNeuron ->
                val outValue = curNeuron.value
                val part1 = errors[curNeuron]!!
                val part2 = outValue * (1 - outValue)
                curNeuron.prevNeurons.forEach { (prevNeuron, weight) ->
                    val delta = part1 * part2 * prevNeuron.value
                    newWeights[curNeuron to prevNeuron] = weight - learningRate * delta
                    errors[prevNeuron] = errors.getOrDefault(prevNeuron, 0.0) + part1 * part2 * weight
                }
            }
        }

        // Установка новых весов
        layers.forEach { layer ->
            layer.neurons.forEach { curNeuron ->
                curNeuron.nextNeurons.forEach {
                    it.prevNeurons[curNeuron] = newWeights[it to curNeuron]!!
                }
            }
        }
    }

    // TODO: Вынести в интерфейс?
    fun printCurrentNetState() {
        layers.forEach { layer ->
            println(layer.neurons.map { it.value })
            println(layer.neurons.flatMap { it.prevNeurons.values })
        }
    }
}