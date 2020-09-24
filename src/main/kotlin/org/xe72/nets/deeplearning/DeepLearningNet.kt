package org.xe72.nets.deeplearning

import org.datavec.api.records.reader.RecordReader
import org.deeplearning4j.datasets.iterator.DoublesDataSetIterator
import org.deeplearning4j.datasets.iterator.impl.EmnistDataSetIterator
import org.deeplearning4j.datasets.iterator.impl.IrisDataSetIterator
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.layers.DenseLayer
import org.deeplearning4j.nn.conf.layers.OutputLayer
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.nd4j.common.primitives.Pair
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.cpu.nativecpu.NDArray
import org.nd4j.linalg.dataset.DataSet
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator
import org.nd4j.linalg.learning.config.AdaGrad
import org.nd4j.linalg.lossfunctions.LossFunctions
import org.xe72.nets.INeuralNet


class DeepLearningNet : INeuralNet {
    val model: MultiLayerNetwork

    init {
        val conf = NeuralNetConfiguration.Builder()
            .seed(12345)
            .weightInit(WeightInit.XAVIER)
            .updater(AdaGrad(0.5))
            .activation(Activation.RELU)
            .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
            .l2(0.0001)
            .list()
            .layer(
                0,
                DenseLayer.Builder().nIn(2).nOut(5).weightInit(WeightInit.XAVIER)
                    .activation(Activation.RELU) //First hidden layer
                    .build()
            )
            .layer(
                1, DenseLayer.Builder()
                    .nIn(5).nOut(5)
                    .weightInit(WeightInit.XAVIER)
                    .activation(Activation.RELU)
                    .build()
            )
            .layer(
                2,
                OutputLayer.Builder().nIn(5).nOut(2).weightInit(WeightInit.XAVIER)
                    .activation(Activation.SOFTMAX) //Output layer
                    .lossFunction(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                    .build()
            )
            .build()

        model = MultiLayerNetwork(conf)
        model.init()
        model.setListeners(ScoreIterationListener(10)) //Print score every 10 parameter updates

    }

    override fun feedLearningData(inputs: List<Double>, targetOutputs: List<Double>) {

//        var dataArr = NDArray()
////        dataArr.add()
//        DataSetIter
//        val dataSet = DataSet()
////        inputs.forEach {
////            dataSet.
////        }
////        ListDataSetIterator<Double>(inputs)
//        model.fit()

//        val balanced = EmnistDataSetIterator.Set.BALANCED
//        val trainSet = EmnistDataSetIterator(balanced, 128, true)
//        val testSet = EmnistDataSetIterator(balanced, 128, false)
//        val iris = IrisDataSetIterator()
//        RecordReader

        val dataSetIterator =
            DoublesDataSetIterator(mutableListOf(Pair(inputs.toDoubleArray(), targetOutputs.toDoubleArray())), 1)

        model.fit(dataSetIterator)
    }

    override fun feedForward(inputs: List<Double>): List<Double> {
        val dataSetIterator =
            DoublesDataSetIterator(mutableListOf(Pair(inputs.toDoubleArray(), doubleArrayOf())), 1)
        val output = model.output(dataSetIterator)
        return listOf(output.getDouble(0), output.getDouble(1))
    }
}