import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Regan Lynch
 * 
 * 	class for serializing a neural network to a file, and do turn a file of a network back into a neural network
 *
 */
public abstract class NeuralNetSerializer {
	
	/**
	 * 	serializes a neural network to a standard text file, using a proprietary format
	 * 
	 * @param nn			the neural network to serialize
	 * @param outFileName   the path of the file to output to
	 */
	public static void SerializeNeuralNet(NeuralNetwork nn, String outFileName) {
	    
		File outFile = null;
		PrintWriter fileWriter = null;
		
		try {
			outFile = new File(outFileName);
			outFile.createNewFile();
			fileWriter = new PrintWriter(outFileName);
		} catch (IOException e) {
			System.out.println("an error occured serializing the neural network");
			return;
		}
	    
		//clear all contents of the file
		fileWriter.print("");
		
		//print the header information for the neural network
		fileWriter.print(nn.getInputLayer().getNumNodes() + ",");
		for(int i = 0; i < nn.getNumHiddenLayers(); i++) {
			fileWriter.print(nn.getHiddenLayers()[i].getNumNodes() + ",");			
		}
		fileWriter.println(nn.getOutputLayer().getNumNodes());
		
		//print the input node information, the node number and their weights
		for(int i = 0; i < nn.getInputLayer().getNumNodes(); i++) {
			fileWriter.println("INPUT_NODE: " + (i + 1));
			fileWriter.println("WEIGHTS: " + Arrays.toString(nn.getInputLayer().getNodes()[i].getWeights()));
		}
		
		//print the hidden layer node information
		for(int i = 0; i < nn.getNumHiddenLayers(); i++) {
			fileWriter.println("HIDDEN_LAYER: " + (i + 1));
			for(int j = 0; j < nn.getHiddenLayers()[i].getNumNodes(); j++) {
				fileWriter.println("HIDDEN_NODE: " + (j + 1));
				fileWriter.println("BIAS: " + nn.getHiddenLayers()[i].getNodes()[j].getBias());
				fileWriter.println("WEIGHTS: " + Arrays.toString(nn.getHiddenLayers()[i].getNodes()[j].getWeights()));
			}
		}
		
		//print the output node information
		for(int i = 0; i < nn.getOutputLayer().getNumNodes(); i ++) {
			fileWriter.println("OUTPUT_NODE: " + (i + 1));
			fileWriter.println("BIAS: " + nn.getOutputLayer().getNodes()[i].getBias());			
		}
		
		fileWriter.close();
	}
	
	
	
	
	/**
	 * 	creates a neural network object from a given neural network containing text file
	 * 
	 * @return  the neural network created from the file
	 */
	public static NeuralNetwork DeserializeNeuralNet(String inFileName) {
		
		File inFile = new File(inFileName);
		if(!inFile.exists()) {
			return null;
		}
		
		Scanner fileScanner = null;
		try {
			fileScanner = new Scanner(inFile);
		} catch (Exception e) {
			return null;
		}
		
		//TODO: Fill In deserialization
		
		fileScanner.close();
		return null;
	}
	
}























