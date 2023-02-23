package com.example.calculator1

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    var addFlag = false
    var decFlag = true

    fun numButtonOnClick(view: View) {
        if (view is Button) {
            if (view.text == ".") {
                if (decFlag) {
                    binding.currentTextView.append(view.text)
                }
                decFlag = false
            }
            else {
                binding.currentTextView.append(view.text)
            }
            addFlag = true
        }
    }

    fun opButtonOnClick(view: View) {
        if (view is Button && addFlag) {
            binding.currentTextView.append(view.text)
            addFlag = false
            decFlag = true
        }
    }

    fun clrButtonOnClick(view: View) {
        binding.currentTextView.text = ""
        binding.resultTextView.text = ""
    }

    fun delButtonOnClick(view: View) {
        val length = binding.currentTextView.length()
        if (length > 0) {
            binding.currentTextView.text = binding.currentTextView.text.subSequence(0, length - 1)
        }
    }

    fun equalButtonOnClick(view: View) {
        binding.resultTextView.text = calc()
    }

    fun calc(): String {
        val numOper = oper()
        if (numOper.isEmpty()) {
            return ""
        }

        val multDivd = multDivd(numOper)
        if (multDivd.isEmpty()) {
            return ""
        }

        val addSub = addSub(multDivd)

        return addSub.toString()
    }

    private fun addSub(passedList: MutableList<Any>): Float {
        var res = passedList[0] as Float

        for (x in passedList.indices) {
            if (passedList[x] is Char && x != passedList.lastIndex) {
                val op  = passedList[x]
                val nt = passedList[x + 1]
                if (op == '+') {
                    res += nt as Float
                }
                if (op == '-') {
                    res -= nt as Float
                }
            }
        }

        return res
    }

    private fun multDivd(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList

        while (list.contains('*') || list.contains('/')) {
            list = calcMD(list)
        }
        return list
    }

    private fun calcMD(passedList: MutableList<Any>): MutableList<Any> {
        val nList = mutableListOf<Any>()

        var reIndex = passedList.size

        for (x in passedList.indices) {
            if (passedList[x] is Char && x != passedList.lastIndex && x < reIndex) {
                val op = passedList[x]
                val pr = passedList[x - 1] as Float
                val nt = passedList[x + 1] as Float

                when (op) {
                    '*' -> {
                        nList.add(pr * nt)
                        reIndex = x + 1
                    }
                    '/' -> {
                        nList.add(pr / nt)
                        reIndex = x + 1
                    }
                    else -> {
                        nList.add(pr)
                        nList.add(op)
                    }

                }

            }

            if (x > reIndex) {
                nList.add(passedList[x])
            }

        }

        return nList
    }

    fun oper(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var current = ""

        for (char in binding.currentTextView.text) {
            if (char.isDigit() || char == '.') {
                current += char
            }
            else {
                list.add(current.toFloat())
                current = ""
                list.add(char)
            }
        }

        if (current != "") {
            list.add(current.toFloat())
        }

        return list
    }

}