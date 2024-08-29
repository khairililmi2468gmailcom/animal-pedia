package com.example.animalpedia

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeOp.ResizeMethod
import org.tensorflow.lite.support.tensorbuffer.TensorBufferFloat
import java.nio.ByteBuffer
import java.nio.ByteOrder

class TFLiteHelper(private val context: Context) {

    private lateinit var interpreter: Interpreter
    private val inputShape = intArrayOf(1, 240, 240, 3)
    private val outputShape = intArrayOf(1, 90) // Adjust the output shape based on your model

    private val labels: Map<Int, String> = mapOf(
        0 to "antelope", 1 to "badger", 2 to "bat", 3 to "bear", 4 to "bee",
        5 to "beetle", 6 to "bison", 7 to "boar", 8 to "butterfly", 9 to "cat",
        10 to "caterpillar", 11 to "chimpanzee", 12 to "cockroach", 13 to "cow",
        14 to "coyote", 15 to "crab", 16 to "crow", 17 to "deer", 18 to "dog",
        19 to "dolphin", 20 to "donkey", 21 to "dragonfly", 22 to "duck",
        23 to "eagle", 24 to "elephant", 25 to "flamingo", 26 to "fly",
        27 to "fox", 28 to "goat", 29 to "goldfish", 30 to "goose", 31 to "gorilla",
        32 to "grasshopper", 33 to "hamster", 34 to "hare", 35 to "hedgehog",
        36 to "hippopotamus", 37 to "hornbill", 38 to "horse", 39 to "hummingbird",
        40 to "hyena", 41 to "jellyfish", 42 to "kangaroo", 43 to "koala",
        44 to "ladybugs", 45 to "leopard", 46 to "lion", 47 to "lizard",
        48 to "lobster", 49 to "mosquito", 50 to "moth", 51 to "mouse",
        52 to "octopus", 53 to "okapi", 54 to "orangutan", 55 to "otter",
        56 to "owl", 57 to "ox", 58 to "oyster", 59 to "panda", 60 to "parrot",
        61 to "pelecaniformes", 62 to "penguin", 63 to "pig", 64 to "pigeon",
        65 to "porcupine", 66 to "possum", 67 to "raccoon", 68 to "rat",
        69 to "reindeer", 70 to "rhinoceros", 71 to "sandpiper", 72 to "seahorse",
        73 to "seal", 74 to "shark", 75 to "sheep", 76 to "snake", 77 to "sparrow",
        78 to "squid", 79 to "squirrel", 80 to "starfish", 81 to "swan", 82 to "tiger",
        83 to "turkey", 84 to "turtle", 85 to "whale", 86 to "wolf", 87 to "wombat",
        88 to "woodpecker", 89 to "zebra"
    )

    init {
        loadModel()
    }

    private fun loadModel() {
        try {
            val model = FileUtil.loadMappedFile(context, "tflite_model_float16.tflite")
            val options = Interpreter.Options()
            interpreter = Interpreter(model, options)

            Log.d("TFLiteHelper", "Model loaded successfully.")
        } catch (e: Exception) {
            Log.e("TFLiteHelper", "Error reading model", e)
        }
    }

    fun classifyImage(bitmap: Bitmap): Pair<String, Float> {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 240, 240, true)
        val inputFeature0 = TensorBuffer.createFixedSize(inputShape, DataType.FLOAT32)
        val tImage = TensorImage(DataType.FLOAT32)
        tImage.load(resizedBitmap)
        interpreter.run(tImage.buffer, inputFeature0.buffer.rewind())

        val outputFeature0 = inputFeature0.floatArray
        val indexMax = outputFeature0.indices.maxByOrNull { outputFeature0[it] } ?: -1
        val label = labels[indexMax] ?: "Unknown"
        val confidence = outputFeature0[indexMax]

        return Pair(label, confidence)
    }

    fun close() {
        interpreter.close()
    }
}
