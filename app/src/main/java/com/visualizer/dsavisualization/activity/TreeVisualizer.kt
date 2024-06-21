package com.visualizer.dsavisualization.activity

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.visualizer.dsavisualization.R
import com.visualizer.dsavisualization.model.TreeNode
import com.visualizer.dsavisualization.view.BinaryTreeView

class TreeVisualizer : AppCompatActivity() {

    private lateinit var binaryTreeView: BinaryTreeView
    private lateinit var traversalResultTextView: androidx.appcompat.widget.AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tree_visualizer)

        binaryTreeView = findViewById(R.id.binaryTreeView)
        traversalResultTextView = findViewById(R.id.traversalResultTextView)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val buildTreeButton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.buildTreeButton)
        val inOrderButton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.inOrderButton)
        val preOrderButton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.preOrderButton)
        val postOrderButton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.postOrderButton)

        buildTreeButton.setOnClickListener {
            val inputText = inputEditText.text.toString()
            if (inputText.isBlank()) {
                Toast.makeText(this, "Please enter values to build the tree", Toast.LENGTH_SHORT).show()
            } else {
                val values = parseInput(inputText)
                if (values != null && values.isNotEmpty()) {
                    val root = buildTree(values)
                    binaryTreeView.setTree(root)
                } else {
                    Toast.makeText(this, "Please enter values in correct format", Toast.LENGTH_SHORT).show()
                }
            }
        }

        inOrderButton.setOnClickListener {
            binaryTreeView.treeRoot?.let {
                val traversalResult = mutableListOf<TreeNode<String>>()
                inOrderTraversal(it, traversalResult)
                animateTraversal(traversalResult) {
                    traversalResultTextView.text = "In-Order: ${traversalResult.joinToString(", ") { node -> node.value }}"
                }
            }
        }

        preOrderButton.setOnClickListener {
            binaryTreeView.treeRoot?.let {
                val traversalResult = mutableListOf<TreeNode<String>>()
                preOrderTraversal(it, traversalResult)
                animateTraversal(traversalResult) {
                    traversalResultTextView.text = "Pre-Order: ${traversalResult.joinToString(", ") { node -> node.value }}"
                }
            }
        }

        postOrderButton.setOnClickListener {
            binaryTreeView.treeRoot?.let {
                val traversalResult = mutableListOf<TreeNode<String>>()
                postOrderTraversal(it, traversalResult)
                animateTraversal(traversalResult) {
                    traversalResultTextView.text = "Post-Order: ${traversalResult.joinToString(", ") { node -> node.value }}"
                }
            }
        }
    }

    private fun parseInput(input: String): List<String>? {
        val regex = Regex("^(\\w+|null|N)([,\\s](\\w+|null|N))*\$")
        return if (regex.matches(input)) {
            input.split(Regex("[,\\s]+")).map { it.trim() }
        } else {
            null
        }
    }

    private fun buildTree(values: List<String>): TreeNode<String>? {
        if (values.isEmpty()) return null

        val root = TreeNode(values[0])
        val queue = mutableListOf<TreeNode<String>?>()
        queue.add(root)

        var index = 1
        while (index < values.size) {
            val currentNode = queue.removeAt(0)

            if (currentNode != null) {
                if (index < values.size) {
                    val leftValue = values[index]
                    if (leftValue != "null" && leftValue != "N") {
                        currentNode.left = TreeNode(leftValue)
                        queue.add(currentNode.left)
                    } else {
                        queue.add(null)
                    }
                    index++
                }

                if (index < values.size) {
                    val rightValue = values[index]
                    if (rightValue != "null" && rightValue != "N") {
                        currentNode.right = TreeNode(rightValue)
                        queue.add(currentNode.right)
                    } else {
                        queue.add(null)
                    }
                    index++
                }
            }
        }

        return root
    }

    private fun inOrderTraversal(node: TreeNode<String>?, result: MutableList<TreeNode<String>>) {
        if (node == null) return
        inOrderTraversal(node.left, result)
        result.add(node)
        inOrderTraversal(node.right, result)
    }

    private fun preOrderTraversal(node: TreeNode<String>?, result: MutableList<TreeNode<String>>) {
        if (node == null) return
        result.add(node)
        preOrderTraversal(node.left, result)
        preOrderTraversal(node.right, result)
    }

    private fun postOrderTraversal(node: TreeNode<String>?, result: MutableList<TreeNode<String>>) {
        if (node == null) return
        postOrderTraversal(node.left, result)
        postOrderTraversal(node.right, result)
        result.add(node)
    }

    private fun animateTraversal(nodes: List<TreeNode<String>>, onComplete: () -> Unit) {
        binaryTreeView.startTraversalAnimation(nodes, onComplete)
    }
}