
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

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
		
		//print the activation function types
		fileWriter.println(nn.getHiddenActivationFunctionType().getName());
		fileWriter.println(nn.getOutputActivationFunctionType().getName());
		
		//print the input node information, the node number and their weights
		for(int i = 0; i < nn.getInputLayer().getNumNodes(); i++) {
			//fileWriter.println("INPUT_NODE: " + (i + 1));
			fileWriter.println(Arrays.toString(nn.getInputLayer().getNodes()[i].getWeights()).replaceAll(" ", "").replaceAll("\\]", "").replaceAll("\\[", ""));
		}
		
		//print the hidden layer node information
		for(int i = 0; i < nn.getNumHiddenLayers(); i++) {
			//fileWriter.println("HIDDEN_LAYER: " + (i + 1));
			for(int j = 0; j < nn.getHiddenLayers()[i].getNumNodes(); j++) {
				//fileWriter.println("HIDDEN_NODE: " + (j + 1));
				fileWriter.println(nn.getHiddenLayers()[i].getNodes()[j].getBias());
				fileWriter.println(Arrays.toString(nn.getHiddenLayers()[i].getNodes()[j].getWeights()).replaceAll(" ", "").replaceAll("\\]", "").replaceAll("\\[", ""));
			}
		}
		
		//print the output node information
		for(int i = 0; i < nn.getOutputLayer().getNumNodes(); i ++) {
			//fileWriter.println("OUTPUT_NODE: " + (i + 1));
			fileWriter.println(nn.getOutputLayer().getNodes()[i].getBias());			
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
			System.out.println("no neuralwork file found under the given path name");
			return null;
		}
		
		Scanner fileScanner = null;
		try {
			fileScanner = new Scanner(inFile);
		} catch (Exception e) {
			System.out.println("error readin from neuralnet file");
			return null;
		}
		
		//perform the deserialization
		try {
			
			//create the initial neural network
			String nnShape = fileScanner.nextLine();
			NeuralNetwork nn = new NeuralNetwork(nnShape, ActivationTypes.getActivationType(fileScanner.nextLine()), ActivationTypes.getActivationType(fileScanner.nextLine()));
			
			float[] currWeights;
			float currBias;
			
			//loop through all input nodes (just weights for nodes)
			for(int i = 0; i < nn.getInputLayer().getNumNodes(); i++) {				
				currWeights = getFloatWeights(fileScanner.nextLine());
				nn.getInputLayer().getNodes()[i].setWeights(currWeights);
			}			
			
			//loop through all hidden layers
			for(int i = 0; i < nn.getHiddenLayers().length; i++) {
				//loop through each node in the hidden layer (bias followed by weight)
				for(int j = 0; j < nn.getHiddenLayers()[i].getNumNodes(); j++) {
					currBias = Float.parseFloat(fileScanner.nextLine());
					currWeights = getFloatWeights(fileScanner.nextLine());
					nn.getHiddenLayers()[i].getNodes()[j].setBias(currBias);
					nn.getHiddenLayers()[i].getNodes()[j].setWeights(currWeights);
				}				
			}
						
			//loop though all output nodes (just biases)
			for(int i = 0; i < nn.getOutputLayer().getNumNodes(); i++) {
				currBias = Float.parseFloat(fileScanner.nextLine());
				nn.getOutputLayer().getNodes()[i].setBias(currBias);
			}
			
		}catch(Exception e){
			System.out.println("error reading from neuralnetwork file");
			System.out.println(e.getMessage());
			fileScanner.close();
			return null;
		}
				
		fileScanner.close();
		return null;
	}
	
	
	/**
	 * @return the selected file path
	 */
	public static String chooseFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
        	return chooser.getSelectedFile().getName();
        }
        return "";
	}
	
	
	/**
	 * 	helper function for getting float weights from a string
	 * 
	 * @param inWeights  the string representing the weights array
	 * @return    	the float weight array
	 */
	private static float[] getFloatWeights(String inWeights) {		
		String[] split = inWeights.split(",");
		float[] floatWeights = new float[split.length];
		
		try {
			for(int i = 0; i < split.length; i++) {
				floatWeights[i] = Float.parseFloat(split[i]);
			}
		}catch(Exception e) {
			System.out.println("an error occured creating weights");
			return null;
		}
		return floatWeights;
	}
	
}























