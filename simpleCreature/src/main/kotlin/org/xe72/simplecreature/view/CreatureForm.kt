package org.xe72.simplecreature.view

import org.xe72.simplecreature.Creature
import org.xe72.simplecreature.additional.ObjectTypes
import org.xe72.simplecreature.additional.ObjectsAroundState
import java.awt.Color
import java.awt.Graphics
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.image.BufferedImage
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.exp

class CreatureForm(val creature: Creature) : JFrame(), Runnable, MouseListener {

    private val w = 1280
    private val h = 720
    private val img = BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

    private val customSigmoid = {x: Double -> 2 / (1 + exp(-x)) - 1}

    private var creatureX = w / 2
    private var creatureY = h / 2

    // TODO: Сделать универсальный общий список для всех видов объектов
    private val foodList = mutableListOf<Pair<Int, Int>>()
    private val enemiesList = mutableListOf<Pair<Int, Int>>()

    init {
        this.setSize(w + 16, h + 38)
        this.isVisible = true
        defaultCloseOperation = EXIT_ON_CLOSE
        this.setLocation(50, 50)
        this.add(JLabel(ImageIcon(img)))
        addMouseListener(this)
    }

    override fun run() {
        while (true) {
            Thread.sleep(2)
            creature.makeTurn(::scanner, ::mover)
            foodList.remove(creatureX to creatureY)
            this.repaint()
        }
    }

    // Просто для практики посмотреть как работает переопределение операторов
    operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>): Pair<Int, Int> {
        return (this.first - other.first) to (this.second - other.second)
    }

    private fun scanner(type: ObjectTypes): ObjectsAroundState {
        if (type == ObjectTypes.FOOD) {
            var up = 0.0; var right = 0.0; var down = 0.0; var left = 0.0
            foodList.forEach {
                val vector = it - (creatureX to creatureY)
                if (abs(vector.first) >= abs(vector.second) && vector.first > 0) {
                    right += 1 - vector.first / w.toDouble()
                } else if (abs(vector.first) >= abs(vector.second) && vector.first < 0) {
                    left += 1 - vector.first.absoluteValue / w.toDouble()
                } else if (abs(vector.first) < abs(vector.second) && vector.second > 0) {
                    down += 1 - vector.second.absoluteValue / w.toDouble()
                } else if (abs(vector.first) < abs(vector.second) && vector.second < 0) {
                    up += 1 - vector.second.absoluteValue / w.toDouble()
                }
            }
            return ObjectsAroundState(customSigmoid(up), customSigmoid(right), customSigmoid(down), customSigmoid(left))
        } else {
            // TODO: Переделать на универсальный
            return ObjectsAroundState(0.0, 0.0, 0.0, 0.0)
        }
    }

    private fun mover(x: Int, y: Int): Boolean {
        val oldX = creatureX
        val oldY = creatureY
        creatureX = (creatureX + x).coerceIn(0..w)
        creatureY = (creatureY + y).coerceIn(0..h)
        return oldX != creatureX || oldY != creatureY
    }

    override fun paint(g: Graphics) {
        g.drawImage(img, 0, 0, w, h, this)

        foodList.forEach {(x, y) ->
            g.color = Color.WHITE
            g.fillOval(x - 3, y - 3, 26, 26)
            g.color = Color.BLUE
            g.fillOval(x, y, 20, 20)
        }

        g.color = Color.WHITE
        g.fillOval(creatureX - 3, creatureY - 3, 26, 26)
        g.color = Color.RED
        g.fillOval(creatureX, creatureY, 20, 20)

    }

    override fun mouseReleased(e: MouseEvent?) {
    }

    override fun mouseEntered(e: MouseEvent?) {
    }

    override fun mouseClicked(e: MouseEvent) {
        foodList.add(e.x to e.y)
    }

    override fun mouseExited(e: MouseEvent?) {
    }

    override fun mousePressed(e: MouseEvent) {
    }
}