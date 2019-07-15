
import java.util.Random;


public class NeuralNetwork {
	
	private int num_input_nodes;
	private int num_hidden_layers;
	private int[] hidden_format;
	private int num_output_nodes;
	
	private InputLayer input_layer;
	private HiddenLayer[] hidden_layers;
	private OutputLayer output_layer;
	
	private ActivationTypes activator;
	
	private static final float MUTATION_FACTOR = 0.5f;
	
	//----------------------------------------------------------------------------------------------------------------
	public NeuralNetwork(String shape, ActivationTypes activation_type) {
		String shape_copy = shape;
		shape_copy = shape_copy.replaceAll(" ", "");
		String[] split = shape_copy.split(",");
		if(split.length < 3) {
			throw new IllegalArgumentException("must define number of input nodes, hidden nodes and output nodes");
		}
		int n_input_nodes, n_output_nodes;
		try {
			n_input_nodes = Integer.parseInt(split[0]);
			n_output_nodes = Integer.parseInt(split[split.length-1]);
		}catch(Exception e) {
			throw new IllegalArgumentException("only integers are permitted do define shape");
		}
		int[] hidden_form = new int[split.length-2];
		for(int i = 1; i < split.length - 1; i++) {
			try {
				hidden_form[i-1] = Integer.parseInt(split[i]);
			}catch(Exception e){
				throw new IllegalArgumentException("only integers are permitted do define shape");
			}
		}
		this.activator = activation_type;
		this.setUpNetwork(n_input_nodes, hidden_form, n_output_nodes);
	}
	
	//----------------------------------------------------------------------------------------------------------------
	private void setUpNetwork(int num_input_nodes, int[] hidden_format, int num_output_nodes) {
		if (hidden_format == null) {
			throw new IllegalArgumentException("must specify the dimentions of the hidden layers");
		}else if(num_input_nodes <= 0 || hidden_format.length <= 0 || num_output_nodes <= 0){
			throw new IllegalArgumentException("number of nodes in a given layer must be greater than 0");
		}else {
			for(int i = 0; i < hidden_format.length; i++) {
				if(hidden_format[i] <= 0) {
					throw new IllegalArgumentException("number of nodes in a given layer must be greater than 0");
				}
			}
		}
		this.num_input_nodes = num_input_nodes;
		this.num_hidden_layers = hidden_format.length;
		this.num_output_nodes = num_output_nodes;
		this.hidden_format = hidden_format;
		//initialize the input layer
		this.input_layer = new InputLayer(this.num_input_nodes, this.hidden_format[0], this.activator);
		//initialize each hidden layer
		this.hidden_layers = new HiddenLayer[this.num_hidden_layers];
		if (this.num_hidden_layers > 1) {
			this.hidden_layers[0] = new HiddenLayer(this.hidden_format[0], this.hidden_format[1], this.activator);
			this.hidden_layers[num_hidden_layers - 1] = new HiddenLayer(this.hidden_format[num_hidden_layers - 1], num_output_nodes, this.activator);
		}else {
			this.hidden_layers[0] = new HiddenLayer(this.hidden_format[0], num_output_nodes, this.activator);
		}
		for (int i = 1; i < num_hidden_layers - 1; i++) {
			this.hidden_layers[i] = new HiddenLayer(this.hidden_format[i], this.hidden_format[i+1], this.activator);
		}
		//initialize the output layer
		this.output_layer = new OutputLayer(this.num_output_nodes, this.hidden_format[num_hidden_layers - 1], this.activator);
		//set up the layer inputs
		this.setLayerInputs();
	}
	
	//----------------------------------------------------------------------------------------------------------------
	private void feedForward() {
		//loop through the hidden layers
		for(int i = 0; i < this.num_hidden_layers; i++) {
			//loop through each node in the hidden layer
			for(int j = 0; j < this.getHiddenLayers()[i].getNumNodes(); j++) {
				//loop through each node in this layers input layer
				float[] initial_vals = new float[this.input_layer.getNodes()[0].getTensor().getValues().length];
				Tensor calc_tensor = new Tensor(initial_vals);
				for(int k = 0; k < this.getHiddenLayers()[i].getInputLayer().getNumNodes(); k++) {
					float weight = this.getHiddenLayers()[i].getInputLayer().getNodes()[k].getWeights()[this.getHiddenLayers()[i].getNodes()[j].getNodeNumber()];
					Tensor curr_tensor  = this.getHiddenLayers()[i].getInputLayer().getNodes()[k].getTensor();
					curr_tensor.multiply(weight);
					calc_tensor.add(curr_tensor);
				}
				//add bias
				calc_tensor.add(this.getHiddenLayers()[i].getNodes()[j].getBias());
				//perform activation function
				calc_tensor.activate(this.activator);
				//set the value
				this.getHiddenLayers()[i].getNodes()[j].setTensor(calc_tensor);
			}
		}
		//loop through output nodes
		for(int i = 0; i < this.num_output_nodes; i++) {
			float[] initial_vals = new float[this.input_layer.getNodes()[0].getTensor().getValues().length];
			Tensor calc_tensor = new Tensor(initial_vals);
			//loop through each output node
			for(int j = 0; j < this.getOutputLayer().getInputLayer().getNumNodes(); j++) {
				float weight = this.getOutputLayer().getInputLayer().getNodes()[j].getWeights()[this.getOutputLayer().getNodes()[i].getNodeNumber()];
				Tensor curr_tensor = this.getOutputLayer().getInputLayer().getNodes()[j].getTensor();
				curr_tensor.multiply(weight);
				calc_tensor.add(curr_tensor);
			}
			//add bias
			calc_tensor.add(this.output_layer.getNodes()[i].getBias());
			//perform activation function
			calc_tensor.activate(this.activator);
			//set the value
			this.getOutputLayer().getNodes()[i].setTensor(calc_tensor);
		}
	}
	
	//-------------------------------------------------------------------------------------------------------------
	private void backPropogate(Tensor[] targets, float learning_rate) {
		//set the initial output errors
		Tensor[] output_errors = new Tensor[this.num_output_nodes];
		Tensor[] final_outputs = this.output_layer.getNodeTensors();
		for(int i = 0; i < output_errors.length; i++) {
			output_errors[i] = targets[i].subtract(final_outputs[i]);
			this.output_layer.getNodes()[i].setBias(this.output_layer.getNodes()[i].getBias() + (output_errors[i].getAverage() * learning_rate));
			
			output_errors[i].print();
		}
		
		
//		//loop backwards through all hidden layers
//		for(int i = this.num_hidden_layers - 1; i >= 0; i--) {
//			float[] current_hidden_errors = new float[this.hidden_layers[i].getNumNodes()];
//			//loop through each node in the current layer
//			for(int j = 0; j < this.hidden_layers[i].getNumNodes(); j++) {
//				//loop through each weight in the current node
//				float[] new_weights = this.hidden_layers[i].getNodes()[j].getWeights();
//				for(int k = 0; k < new_weights.length; k++) {
////					current_hidden_errors[j] += output_errors[k] * this.hidden_layers[i].getNodes()[j].getWeights()[k];
//					
////					new_weights[k] += (output_errors[k] * this.hidden_layers[i].getNodes()[j].getValue()) * learning_rate;
//					
//				}
////				current_hidden_errors[j] *= this.activator.activatePrime(this.hidden_layers[i].getNodes()[j].getValue());
//				this.hidden_layers[i].getNodes()[j].setWeights(new_weights);
////				this.hidden_layers[i].getNodes()[j].setBias(this.hidden_layers[i].getNodes()[j].getBias() + (current_hidden_errors[j] * learning_rate));
//
//			}
//			output_errors = current_hidden_errors;
//		}
//		
//		
//		//loop through each node in the input layer
//		for(int i = 0; i < this.num_input_nodes; i++) {
//			//loop through each weight in the node
//			float[] new_weights = this.input_layer.getNodes()[i].getWeights();
//			for(int k = 0; k < new_weights.length; k++) {
//				
////				new_weights[k] += (output_errors[k] * this.input_layer.getNodes()[i].getValue()) * learning_rate;
//				
//			}
//			this.input_layer.getNodes()[i].setWeights(new_weights);
//		}
		
	}
	
	//-------------------------------------------------------------------------------------------------------------
	public Tensor[] predict(Tensor[] inputs) {
		this.setInputs(inputs);
		this.feedForward();
		return this.output_layer.getNodeTensors();
	}
	
	//--------------------------------------------------------------------------------------------------------------
	//predict wrapper to be able to get inputs from doubles (the neural net uses floats to save on memory and computation time)
	public double[][] predict(double[][] inputs) {
		Tensor[] tensor_inputs = new Tensor[inputs.length];
		for(int i = 0; i < inputs.length; i++) {
			float[] curr_float_vals = new float[inputs[i].length];
			for(int j = 0; j < curr_float_vals.length; j++) {
				curr_float_vals[j] = (float)inputs[i][j];
			}
			tensor_inputs[i] = new Tensor(curr_float_vals);
		}
		Tensor[] predictions = this.predict(tensor_inputs);
		double[][] double_predictions = new double[predictions.length][predictions[0].getValues().length];
		for(int i = 0; i < predictions.length; i++) {
			double[] curr_predictions = new double[predictions[i].getValues().length];
			for(int j = 0; j < curr_predictions.length; j++) {
				curr_predictions[j] = (double)predictions[i].getValues()[j];
			}
			double_predictions[i] = curr_predictions;
		}
		return double_predictions;
	}
	
	//--------------------------------------------------------------------------------------------------------------
	public void mutate(float mutation_rate) {
		Random random_generator = new Random();
		//loop through each input node, adjusting weights for each of need be
		for(int i = 0; i < this.num_input_nodes; i++) {
			for(int j = 0; j < this.getInputLayer().getNodes()[i].getWeights().length; j++) {
				if(Math.random() < mutation_rate) {
					float new_weight = this.getInputLayer().getNodes()[i].getWeights()[j] + (float)(random_generator.nextGaussian() * MUTATION_FACTOR);
					this.getInputLayer().getNodes()[i].setWeight(j, new_weight);
				}
			}
		}
		//loop through each hidden layer
		for(int i = 0; i < this.num_hidden_layers; i++) {
			//loop through each hidden layer node
			for(int j = 0; j < this.getHiddenLayers()[i].getNumNodes(); j++) {
				if(Math.random() < mutation_rate) {
					float new_bias = this.getHiddenLayers()[i].getNodes()[j].getBias() + (float)(random_generator.nextGaussian() * MUTATION_FACTOR);
					this.getHiddenLayers()[i].getNodes()[j].setBias(new_bias);
				}
				//loop through each weight of the nodes
				for(int k = 0; k < this.getHiddenLayers()[i].getNodes()[j].getWeights().length; k++) {
					if (Math.random() < mutation_rate) {
						float new_weight = this.getHiddenLayers()[i].getNodes()[j].getWeights()[k] + (float)(random_generator.nextGaussian() * MUTATION_FACTOR);
						this.getHiddenLayers()[i].getNodes()[j].setWeight(k, new_weight);
					}
				}
			}
		}
		for(int i = 0; i < this.num_output_nodes; i++) {
			if(Math.random() < mutation_rate) {
				float new_bias = this.getOutputLayer().getNodes()[i].getBias() + (float)(random_generator.nextGaussian() * MUTATION_FACTOR);
				this.getOutputLayer().getNodes()[i].setBias(new_bias);
			}
		}
	}
	
	//---------------------------------------------------------------------------------------------------------------
	public void train(Tensor[] inputs, Tensor[] target_outputs, float learning_rate, int epochs) {
		if(inputs.length != this.num_input_nodes) {
			throw new IllegalArgumentException("NUMBER OF INPUTS MUST MATCH THE NUMBER OF INPUT NODES");
		}else if(target_outputs.length != this.num_output_nodes) {
			throw new IllegalArgumentException("NUMBER OF OUTPUTS MUST MATCH THE NUMBER OF OUTPUT NODES");
		}
		//set the initial inputs
		this.setInputs(inputs);
		//begin training
		for(int epoch = 0; epoch < epochs; epoch++) {
			this.feedForward();
			this.backPropogate(target_outputs, learning_rate);
		}
	}
	
	//---------------------------------------------------------------------------------------------------------------
	//wrapper for other train method
	public void train(double[][] inputs, double[][] target_outputs, double learning_rate, int epochs) {
		float[][] float_inputs = new float[inputs.length][inputs[0].length];
		float[][] float_outputs = new float[target_outputs.length][target_outputs[0].length];
		Tensor[] tensor_inputs = new Tensor[inputs.length];
		Tensor[] tensor_outputs = new Tensor[target_outputs.length];
		for(int i = 0; i < inputs.length; i++) {
			for(int j = 0; j < inputs[0].length; j++) {
				float_inputs[i][j] = (float)inputs[i][j];
			}
			tensor_inputs[i] = new Tensor(float_inputs[i]);
		}
		for(int i = 0; i < target_outputs.length; i++) {
			for(int j = 0; j < target_outputs[0].length; j++) {
				float_outputs[i][j] = (float)target_outputs[i][j];
			}
			tensor_outputs[i] = new Tensor(float_outputs[i]);
		}
		this.train(tensor_inputs, tensor_outputs, (float)learning_rate, epochs);
	}
	
	//---------------------------------------------------------------------------------------------------------------
	private void setLayerInputs() {
		this.output_layer.setInputLayer(this.hidden_layers[this.num_hidden_layers - 1]);
		for(int i = this.num_hidden_layers - 1; i >= 1; i-- ) {
			this.hidden_layers[i].setInputLayer(this.hidden_layers[i - 1]);
		}
		this.hidden_layers[0].setInputLayer(this.input_layer);
	}
	
	
	//----------------------------------------------------------------------------------------------------------------
	public void display(String title) {
		NeuralNetDrawer drawer = new NeuralNetDrawer(this);
		drawer.start(title);
	}
	
	//----------------------------------------------------------------------------------------------------------------
	public InputLayer getInputLayer() {
		return this.input_layer;
	}
	
	//----------------------------------------------------------------------------------------------------------------
	public HiddenLayer[] getHiddenLayers() {
		return this.hidden_layers;
	}
	
	//----------------------------------------------------------------------------------------------------------------
	public OutputLayer getOutputLayer() {
		return this.output_layer;
	}
	
	//----------------------------------------------------------------------------------------------------------------
	public int getNumHiddenLayers() {
		return this.num_hidden_layers;
	}
	
	//----------------------------------------------------------------------------------------------------------------
	public int getNumberTotalNodes() {
		int num_nodes = this.num_input_nodes + this.num_output_nodes;
		for(int i = 0; i < this.getNumHiddenLayers(); i++) {
			num_nodes += this.hidden_layers[i].getNumNodes();
		}
		return num_nodes;
	}
	
	private void setInputs(Tensor[] inputs) {
		if (inputs.length != this.num_input_nodes) {
			throw new IllegalArgumentException("Prediction input shape does not match the number of input nodes"); 
		}
		for(int i = 0; i < this.num_input_nodes; i++) {
			this.getInputLayer().getNodes()[i].setTensor(inputs[i]);;
		}
	}

}











