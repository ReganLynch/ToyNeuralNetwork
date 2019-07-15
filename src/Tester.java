import java.util.Arrays;

/**
 * @author Regan Lynch
 * 
 * 		Tester class
 * 
 * 			- class to highlight some functionality and use cases of the ToyNeuralNet
 *
 */
public class Tester {

	public static void main(String[] args){
		
		//---------------
		//create a neural network with 2 input nodes, 2 hidden nodes, and 1 output node to learn the XOR truth table
			//using the SIGMOID activation function for the hidden nodes and output nodes
		NeuralNetwork xor_nn = new NeuralNetwork("2, 2, 1", ActivationTypes.SIGMOID, ActivationTypes.SIGMOID);
		
		//train on each of the 4 input / output combinations of XOR 1000 times 
		double[][] xor_inputs = new double[][] {{1, 1}, {1, 0}, {0, 1}, {0, 0}};
		double[][] xor_outputs = new double[][] {{0}, {1}, {1}, {0}};
		xor_nn.train(xor_inputs, xor_outputs, 1, 1000);
		
		System.out.println("XOR Predictions: ");
		System.out.println("predicting 1, 1 -> " + Arrays.toString(xor_nn.predict(new double[] {1, 1} )));
		System.out.println("predicting 1, 0 -> " + Arrays.toString(xor_nn.predict(new double[] {1, 0} )));
		System.out.println("predicting 0, 1 -> " + Arrays.toString(xor_nn.predict(new double[] {0, 1} )));
		System.out.println("predicting 0, 0 -> " + Arrays.toString(xor_nn.predict(new double[] {0, 0} )));
		
		xor_nn.display("my neural network after learning XOR!");
		//---------------
		
		System.out.println();
		
		//---------------
		//create a neural network with more than three layers to learn one input/output combination very well
		NeuralNetwork nn = new NeuralNetwork("3, 3, 2, 4, 2", ActivationTypes.RELU, ActivationTypes.SIGMOID);
		
		nn.train(new double[] {1,  0.5,  1}, new double[] {0.75, 0.34}, 1, 100);	//training on these inputs specifically 100 times
		
		System.out.println("Neural Netowrk trained specifically on 1, 0.5, 1 -> 0.75, 0.34: ");
		System.out.println("Predicting input 1, 0.5, 1 -> " + Arrays.toString(nn.predict(new double[] {1, 0.5, 1})));
		
		nn.display("Neural Network specifically trained");
		//---------------
		
	}

}
