# ToyNeuralNetwork
An object-oriented implementation of a neural network in Java

  - I am well aware that an object-oriented approach to implementing a neural network results in suboptimal performance to say
     the least, but I wanted to implement a neural network in this way to gain a better understanding of how exactly they work.
  - This project was a great learning experience for me, and I plan to use this neural network api in the future for a game
     playing AI

## How To Initialize 
You can initialize a Neural Network with the following:
```java
  NeuralNetwork my_nn = new NeuralNetwork("2, 3, 1", ActivationTypes.RELU, ActivationTypes.SIGMOID);
```
The three arguments passed to create a neural network are as follows:
   1) the shape of the neural network. "2, 3, 1" means 2 input neurons, 3 hidden neurons, 1 output neuron
        - this can be any comma separated string of positive integers (more than 3 layers are allowed)
   2) the activation function for the hidden layers
   3) the activation function for the output layer
   
## How To Train
You can train the neural network like so:
```java
  //initialize the network
  NeuralNetwork xor_nn = new NeuralNetwork("2, 2, 1", ActivationTypes.SIGMOID, ActivationTypes.SIGMOID);
 
 //train on each of the 4 input / output combinations of XOR 1000 times 
  double[][] xor_inputs = new double[][] {{1, 1}, {1, 0}, {0, 1}, {0, 0}};
  double[][] xor_outputs = new double[][] {{0}, {1}, {1}, {0}};
  xor_nn.train(xor_inputs, xor_outputs, 1, 1000);
```
You can also train the network via an extrnal method, such as a NEAT algorithm, using the implmented 'mutate' function.

## How To Predict Output From Inputs
After training, the network will have learned the inputs (hopefully!). You can ask the network to predict the output(s) for a given input:
```java
  //prediciting outputs from the trained neural net above
  xor_nn.predict(new double[] {1, 1}); // -> 0.002728967694565654
  xor_nn.predict(new double[] {1, 0}); // -> 0.9957643747329712
  xor_nn.predict(new double[] {0, 1}); // -> 0.9971928596496582
  xor_nn.predict(new double[] {0, 0}); // -> 0.002999254036694765
```
This shows that the neural network learned the XOR gate successfully.

## Viewing The Structure of The Neural Network
You can use the NeuralNework.display() method to paint the structure of the current neural network object, including all its connections, weights, and biases
```java
  //initialize the network
  NeuralNetwork test_nn = new NeuralNetwork("2, 2, 1", ActivationTypes.SIGMOID, ActivationTypes.SIGMOID);
  //display the network
  test_nn.display("my neural net!")
```
This would produce an image similar to this one:

![nn_pic1](https://user-images.githubusercontent.com/45275820/61254359-9f0ecd80-a73a-11e9-8a3b-17219e696857.png)
