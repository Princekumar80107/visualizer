# DSA Visualization

## Description
The DSA (Data Structures and Algorithms) Visualization app is designed to help users understand and visualize fundamental data structures and algorithms. It provides interactive and animated visualizations for binary trees, bubble sort, and selection sort algorithms, making complex concepts easier to grasp.

## Working Features
### Binary Tree Visualization
- **Input**: Users can input values to create a binary tree.
- **Display**: The tree is displayed with nodes and edges, and node values are shown within the nodes.
- **Interactions**:
  - Scrollable and zoomable view to accommodate larger trees.
  - Traversal animations (e.g., in-order traversal) highlight nodes in a specific order, changing the node color and displaying the traversal result.

### Bubble Sort Visualization
- **Input**: Users can input an array of values.
- **Display**: The bubble sort algorithm is visualized step-by-step, comparing and swapping elements as needed.
- **Animation**:
  - Slowed down to clearly show each comparison and swap.
  - Currently compared elements are highlighted in red, and sorted elements return to their default color.

### Selection Sort Visualization
- **Input**: Users can input an array of values.
- **Display**: The selection sort algorithm is visualized, showing each step of finding the minimum element and swapping it with the first unsorted element.
- **Animation**:
  - Current minimum element is highlighted in blue, and the index to be swapped is highlighted in green.
  - Animation shows the swapping process and highlights each step.

## User Interface (UI)
### Main Activity
- Simple and clean interface with buttons to navigate to different visualizers (Binary Tree, Bubble Sort, Selection Sort).
- Each visualizer has an input field for users to enter values.

### Binary Tree Visualizer
- Editable text box for user input to build the tree.
- A view that displays the binary tree with nodes and edges.
- Scrollable and zoomable view to manage large trees.
- Buttons to start traversal animations (e.g., in-order, pre-order, post-order).

### Bubble Sort Visualizer
- Input field for the array values.
- A view that displays the array elements in boxes.
- Animated sorting visualization showing comparisons and swaps.
- Button to start the sorting animation.

### Selection Sort Visualizer
- Input field for the array values.
- A view that displays the array elements in boxes.
- Animated sorting visualization showing the selection and swapping of minimum elements.
- Button to start the sorting animation.

### Insertion Sort Visualizer
- Input field for the array values.
- A view that displays the array elements in boxes.
- Animated sorting visualization showing the smaller and placing it to its correct position.
- Button to start the sorting animation.
## Technologies Used
- **Kotlin**: Primary programming language.
- **Android Views and Custom Views**: For creating and managing the visualizations.
- **Handlers and Animations**: For managing the step-by-step visualization of sorting algorithms.
- **ScaleGestureDetector**: For handling zooming functionality in the binary tree visualizer.
- **AppCompatDelegate**: For handling day/night theme switching.

The DSA Visualization app offers a practical and interactive way to learn and understand basic data structures and algorithms through visual representation and animations.
