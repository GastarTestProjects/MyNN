package org.xe72.view

import org.xe72.nnet.NeuralNet
import java.awt.Color
import java.awt.Graphics
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.image.BufferedImage
import java.util.*
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel

/**
 * Практически не менял. Взял тут: https://github.com/Elco-/SimpleNN
 */
class FormDots : JFrame(), Runnable, MouseListener {
    private val w = 1280
    private val h = 720
    private val img = BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)
    private val pimg = BufferedImage(w / 8, h / 8, BufferedImage.TYPE_INT_RGB)
    private var frame = 0
    private val nn: NeuralNet
    var points: MutableList<CustomPoint> = ArrayList()

    var shouldRecalculate = true

    init {
//        val sigmoid =
//            UnaryOperator { x: Double? -> 1 / (1 + Math.exp(-x!!)) }
//        val dsigmoid = UnaryOperator { y: Double -> y * (1 - y) }
        nn = NeuralNet(0.05, 2, 5, 5, 5, 2)
        this.setSize(w + 16, h + 38)
        this.isVisible = true
        defaultCloseOperation = EXIT_ON_CLOSE
        this.setLocation(50, 50)
        this.add(JLabel(ImageIcon(img)))
        addMouseListener(this)
    }

    override fun run() {
        while (true) {
//            if (shouldRecalculate) {
                this.repaint()
//            }
            //            try { Thread.sleep(17); } catch (InterruptedException e) {}
        }
    }

    override fun paint(g: Graphics) {
        if (points.size > 0) {
            for (k in 0 until 10000) {
                val p = points[(Math.random() * points.size).toInt()]
                val nx = p.x.toDouble() / w
                val ny = p.y.toDouble() / h
//                nn.feedForward(listOf(nx, ny))
                val targets = Array<Double>(2) { 0.0 }
                if (p.type == 0) targets[0] = 1.0 else targets[1] = 1.0
//                nn.backpropagation(targets)

                nn.feedLearningData(listOf(nx, ny), listOf(*targets))
            }
        }
        for (i in 0 until w / 8) {
            for (j in 0 until h / 8) {
                val nx = i.toDouble() / w * 8
                val ny = j.toDouble() / h * 8
                val outputs = nn.feedForward(listOf(nx, ny))
                var green =
                    Math.max(0.0, Math.min(1.0, outputs[0] - outputs[1] + 0.5))
                var blue = 1 - green
                green = 0.3 + green * 0.5
                blue = 0.5 + blue * 0.5
                val color = 100 shl 16 or ((green * 255).toInt() shl 8) or (blue * 255).toInt()
                pimg.setRGB(i, j, color)
            }
        }
        val ig = img.graphics
        ig.drawImage(pimg, 0, 0, w, h, this)
        for (p in points) {
            ig.color = Color.WHITE
            ig.fillOval(p.x - 3, p.y - 3, 26, 26)
            if (p.type == 0) ig.color = Color.GREEN else ig.color = Color.BLUE
            ig.fillOval(p.x, p.y, 20, 20)

//            val outputs = nn.feedForward(listOf(p.x.toDouble() / w, p.y.toDouble() / h))
//            println((if (p.type == 0) "1 0" else "0 1") + " - " + outputs)
//
//            val targets = Array<Double>(2) { 0.0 }
//            if (p.type == 0) targets[0] = 1.0 else targets[1] = 1.0
//            nn.feedLearningData(listOf(p.x.toDouble() / w, p.y.toDouble() / h), listOf(*targets))
//            nn.printCurrentNetState()
        }
//        println()
        g.drawImage(img, 8, 30, w, h, this)
        frame++

        shouldRecalculate = false
    }

    override fun mouseClicked(e: MouseEvent) {}
    override fun mousePressed(e: MouseEvent) {
        var type = 0
        if (e.button == 3) type = 1
        points.add(CustomPoint(e.x - 16, e.y - 38, type))
        shouldRecalculate = true
    }

    override fun mouseReleased(e: MouseEvent) {}
    override fun mouseEntered(e: MouseEvent) {}
    override fun mouseExited(e: MouseEvent) {}
}