
import java.util.Random;


/**
 * @author Regan Lynch
 * 
 * 	An Object-Oriented approach to a neural network
 *
 */
public class NeuralNetwork {
	
	//keep track of number of nodes in each layer
	private int num_input_nodes;
	private int num_hidden_layers;
	private int[] hidden_format;
	private int num_output_nodes;
	
	//keeping track of the layers in this network
	private InputLayer input_layer;
	private HiddenLayer[] hidden_layers;
	private OutputLayer output_layer;
	
	//the hidden layer activation type of this network
	private ActivationTypes hidden_activator;
	//the output layer activation type of this network
	private ActivationTypes output_activator;
	
	//the factor by which a mutation effects the weight/bias
	private static final float MUTATION_FACTOR = 0.5f;
	
	
	//----------------------------------------------------------------------------------------------------------------
	/**
	 * 	Neural Network Constructor
	 * 
	 * @param shape				the dimentions of the neural network (ex: "2, 3, 1")
	 * @param activation_type	the activatin type for the neural network
	 */
	public NeuralNetwork(String shape, ActivationTypes hidden_activation_type, ActivationTypes output_activation_type) {
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
		this.hidden_activator = hidden_activation_type;
		this.output_activator = output_activation_type;
		this.setUpNetwork(n_input_nodes, hidden_form, n_output_nodes);
	}
	
	//----------------------------------------------------------------------------------------------------------------
	/**
	 * 	deep copy constructor for a neural network object
	 * 
	 * @param other_nn		the neural netwok to copy
	 */
	public NeuralNetwork(NeuralNetwork other_nn) {
		this.num_input_nodes = other_nn.num_input_nodes;
		this.num_hidden_layers = other_nn.num_hidden_layers;
		this.num_output_nodes = other_nn.num_output_nodes;
		this.hidden_activator = other_nn.hidden_activator;
		this.output_activator = other_nn.output_activator;
		this.hidden_layers = new HiddenLayer[this.num_hidden_layers];
		
		//initialize hidden layer format
		this.hidden_format = new int[other_nn.hidden_format.length];
		for(int i = 0; i < other_nn.hidden_format.length; i++) {
			this.hidden_format[i] = other_nn.hidden_format[i];
		}
		
		//initialize the layers
		this.input_layer = new InputLayer(other_nn.input_layer);
		for(int i = 0; i < this.num_hidden_layers; i++) {
			this.hidden_layers[i] = new HiddenLayer(other_nn.hidden_layers[i]);
		}
		this.output_layer = new OutputLayer(other_nn.output_layer);
		
		//initialize each layer's input layer
		this.output_layer.setInputLayer(this.hidden_layers[this.num_hidden_layers-1]);
		for(int i = this.num_hidden_layers-1; i > 0; i--) {
			this.hidden_layers[i].setInputLayer(this.hidden_layers[i-1]);
		}
		this.hidden_layers[0].setInputLayer(this.input_layer);
	}
	
	
	//----------------------------------------------------------------------------------------------------------------
	/**
	 * 	Initializes the layers, nodes and weights of the neural network
	 * 
	 * @param num_input_nodes		the number of input nodes
	 * @param hidden_format			the format shape of the hidden layers
	 * @param num_output_nodes		the number of output nodes
	 */
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
		this.input_layer = new InputLayer(this.num_input_nodes, this.hidden_format[0], this.hidden_activator);
		//initialize each hidden layer
		this.hidden_layers = new HiddenLayer[this.num_hidden_layers];
		if (this.num_hidden_layers > 1) {
			this.hidden_layers[0] = new HiddenLayer(this.hidden_format[0], this.hidden_format[1], this.hidden_activator);
			this.hidden_layers[num_hidden_layers - 1] = new HiddenLayer(this.hidden_format[num_hidden_layers - 1], num_output_nodes, this.hidden_activator);
		}else {
			this.hidden_layers[0] = new HiddenLayer(this.hidden_format[0], num_output_nodes, this.hidden_activator);
		}
		for (int i = 1; i < num_hidden_layers - 1; i++) {
			this.hidden_layers[i] = new HiddenLayer(this.hidden_format[i], this.hidden_format[i+1], this.hidden_activator);
		}
		//initialize the output layer
		this.output_layer = new OutputLayer(this.num_output_nodes, this.hidden_format[num_hidden_layers - 1], this.hidden_activator);
		//set up the layer inputs
		this.setLayerInputs();
	}
	
	//----------------------------------------------------------------------------------------------------------------
	/**	
	 * 	FeedForward algorithm for this neural network.
	 */
	private void feedForward() {
		//loop through the hidden layers
		for(int i = 0; i < this.num_hidden_layers; i++) {
			//loop through each node in the hidden layer
			for(int j = 0; j < this.getHiddenLayers()[i].getNumNodes(); j++) {
				//loop through each node in this layers input layer
				float calc_value = 0;
				for(int k = 0; k < this.getHiddenLayers()[i].getInputLayer().getNumNodes(); k++) {
					float weight = this.getHiddenLayers()[i].getInputLayer().getNodes()[k].getWeights()[this.getHiddenLayers()[i].getNodes()[j].getNodeNumber()];
					float value = this.getHiddenLayers()[i].getInputLayer().getNodes()[k].getValue();
					calc_value += weight * value;
				}
				//add bias
				calc_value += this.getHiddenLayers()[i].getNodes()[j].getBias();
				//perform activation function
				calc_value = this.hidden_activator.activate(calc_value);
				//set the value
				this.getHiddenLayers()[i].getNodes()[j].setValue(calc_value);
			}
		}
		//loop through output nodes
		for(int i = 0; i < this.num_output_nodes; i++) {
			float calc_value = 0;
			//loop through each output node
			for(int j = 0; j < this.getOutputLayer().getInputLayer().getNumNodes(); j++) {
				float weight = this.getOutputLayer().getInputLayer().getNodes()[j].getWeights()[this.getOutputLayer().getNodes()[i].getNodeNumber()];
				float value = this.getOutputLayer().getInputLayer().getNodes()[j].getValue();
				calc_value += weight * value;
			}
			//add bias
			calc_value += this.getOutputLayer().getNodes()[i].getBias();
			//perform activation function
			calc_value = this.output_activator.activate(calc_value);
			//set the value
			this.getOutputLayer().getNodes()[i].setValue(calc_value);
		}
	}
	
	//-------------------------------------------------------------------------------------------------------------
	/**
	 * 	performs the backpropogation algorithm on this neural network to minimize errors	
	 * 
	 * @param targets			the target values
	 * @param learning_rate		the rate at which the neural net learns (ie the step size)
	 */
	private void backPropogate(float[] targets, float learning_rate) {
		//set the initial output errors
		float[] output_errors = new float[this.num_output_nodes];
		float[] final_outputs = this.output_layer.getNodeValues();
		for(int i = 0; i < output_errors.length; i++) {
			output_errors[i] = targets[i] - final_outputs[i];
			this.output_layer.getNodes()[i].setBias(this.output_layer.getNodes()[i].getBias() + (output_errors[i] * learning_rate));
		}
		//loop backwards through all hidden layers
		for(int i = this.num_hidden_layers - 1; i >= 0; i--) {
			float[] current_hidden_errors = new float[this.hidden_layers[i].getNumNodes()];
			//loop through each node in the current layer
			for(int j = 0; j < this.hidden_layers[i].getNumNodes(); j++) {
				//loop through each weight in the current node
				float[] new_weights = this.hidden_layers[i].getNodes()[j].getWeights();
				for(int k = 0; k < new_weights.length; k++) {
					current_hidden_errors[j] += output_errors[k] * this.hidden_layers[i].getNodes()[j].getWeights()[k];
					new_weights[k] += (output_errors[k] * this.hidden_layers[i].getNodes()[j].getValue()) * learning_rate;
				}
				current_hidden_errors[j] *= this.hidden_activator.activatePrime(this.hidden_layers[i].getNodes()[j].getValue());
				this.hidden_layers[i].getNodes()[j].setWeights(new_weights);
				this.hidden_layers[i].getNodes()[j].setBias(this.hidden_layers[i].getNodes()[j].getBias() + (current_hidden_errors[j] * learning_rate));
			}
			output_errors = current_hidden_errors;
		}
		//loop through each node in the input layer
		for(int i = 0; i < this.num_input_nodes; i++) {
			//loop through each weight in the node
			float[] new_weights = this.input_layer.getNodes()[i].getWeights();
			for(int k = 0; k < new_weights.length; k++) {
				new_weights[k] += (output_errors[k] * this.input_layer.getNodes()[i].getValue()) * learning_rate;
			}
			this.input_layer.getNodes()[i].setWeights(new_weights);
		}
	}
	
	//-------------------------------------------------------------------------------------------------------------
	/**
	 * 	predicts the output values, based on the inputs by feeding forward through the neural network
	 * 
	 * @param inputs	the inputs to the input nodes
	 * @return	values in the output nodes
	 */
	public float[] predict(float[] inputs) {
		this.setInputs(inputs);
		this.feedForward();
		return this.output_layer.getNodeValues();
	}
	
	//--------------------------------------------------------------------------------------------------------------

	/**
	 * 	predicts the output values, based on the inputs by feeding forward through the neural network
	 * 
	 * @param inputs the inputs to the input nodes
	 * @return values in the output nodes
	 */
	public double[] predict(double[] inputs) {
		float[] float_inputs = new float[inputs.length];
		for(int i = 0; i < inputs.length; i++) {
			float_inputs[i] = (float)inputs[i];
		}
		float[] float_outs = this.predict(float_inputs);
		double[] double_outs = new double[this.num_output_nodes];
		for(int i = 0; i < float_outs.length; i++) {
			double_outs[i] = (double)float_outs[i];
		}
		return double_outs;
	}
	
	//--------------------------------------------------------------------------------------------------------------
	/**
	 * 	mutates (changes) the weights and biases of this neural network, based on the mutation_rate
	 * 
	 * @param mutation_rate		the frequency of the mutations
	 */
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
	/**
	 * 	trains the neural network to learn a given output, for a given input
	 * 
	 * @param inputs			the given inputs 
	 * @param target_outputs	the desired outputs
	 * @param learning_rate		the rate at which the neural net can learn (1 is 100%)
	 * @param epochs			the number of learning iterations
	 */
	public void train(float[] inputs, float[] target_outputs, float learning_rate, int epochs) {
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
	/**
	 * 	trains the neural network to learn a given output, for a given input
	 * 
	 * @param inputs			the given inputs 
	 * @param target_outputs	the desired outputs
	 * @param learning_rate		the rate at which the neural net can learn (1 is 100%)
	 * @param epochs			the number of learning iterations
	 */
	public void train(double[] inputs, double[] target_outputs,double learning_rate, int epochs) {
		float[] float_inputs = new float[inputs.length];
		float[] float_targets = new float[target_outputs.length];
		for(int i = 0; i < inputs.length; i++) {
			float_inputs[i] = (float)inputs[i];
		}
		for(int i = 0; i < target_outputs.length; i++) {
			float_targets[i] = (float)target_outputs[i];
		}
		this.train(float_inputs, float_targets, (float)learning_rate, epochs);
	}
	
	//---------------------------------------------------------------------------------------------------------------
	/**
	 * 	wrapper for other train method, to enable the training of multiple inputs and outputs at the same time
	 * 
	 * @param inputs			2d array of inputs to the array (multiple sets of inputs)
	 * @param targets			2d array of target values for the given outputs
	 * @param learning_rate		the rate at which the neural net can learn (1 is at speed 100%)
	 * @param epochs			the number of training iterations
	 */
	public void train(double[][] inputs, double[][] targets, double learning_rate, int epochs) {
		float[][] float_inputs = new float[inputs.length][inputs[0].length];
		float[][] float_targets = new float[targets.length][targets[0].length];
		float float_lr = (float)learning_rate;
		for(int i = 0; i < float_inputs.length; i++) {
			for(int j = 0; j < float_inputs[i].length; j++) {
				float_inputs[i][j] = (float)inputs[i][j];
			}
		}
		for(int i = 0; i < float_targets.length; i++) {
			for(int j = 0; j < float_targets[i].length; j++) {
				float_targets[i][j] = (float)targets[i][j];
			}
		}
		//train each of the input/output combinations one after another, many times (epochs times)
		for(int i = 0; i < epochs; i++) {
			for(int j = 0; j < float_inputs.length; j++) {
				this.train(inputs[j], targets[j], float_lr, 1);
			}
		}
	}
	
	//---------------------------------------------------------------------------------------------------------------
	/**
	 * 	tells each layer which layer is the layer leading to it
	 */
	private void setLayerInputs() {
		this.output_layer.setInputLayer(this.hidden_layers[this.num_hidden_layers - 1]);
		for(int i = this.num_hidden_layers - 1; i >= 1; i-- ) {
			this.hidden_layers[i].setInputLayer(this.hidden_layers[i - 1]);
		}
		this.hidden_layers[0].setInputLayer(this.input_layer);
	}
	
	
	//----------------------------------------------------------------------------------------------------------------
	/**
	 * 	draws a visual representation of the neural network, drawing all weights and biases
	 * 
	 * @param title 	the title of the displayed networl
	 */
	public void display(String title) {
		NeuralNetDrawer drawer = new NeuralNetDrawer(this);
		drawer.start(title);
	}
	
	//----------------------------------------------------------------------------------------------------------------
	/**
	 * @return	the input layer
	 */
	public InputLayer getInputLayer() {
		return this.input_layer;
	}
	
	//----------------------------------------------------------------------------------------------------------------
	/**
	 * @return	array of this network's hidden layers
	 */
	public HiddenLayer[] getHiddenLayers() {
		return this.hidden_layers;
	}
	
	//----------------------------------------------------------------------------------------------------------------
	/**
	 * @return	the output layer for this network
	 */
	public OutputLayer getOutputLayer() {
		return this.output_layer;
	}
	
	//----------------------------------------------------------------------------------------------------------------
	/**
	 * @return	the number of hidden layers in this network
	 */
	public int getNumHiddenLayers() {
		return this.num_hidden_layers;
	}
	
	//----------------------------------------------------------------------------------------------------------------
	/**
	 * @return	the total number of nodes in this neural network
	 */
	public int getNumberTotalNodes() {
		int num_nodes = this.num_input_nodes + this.num_output_nodes;
		for(int i = 0; i < this.getNumHiddenLayers(); i++) {
			num_nodes += this.hidden_layers[i].getNumNodes();
		}
		return num_nodes;
	}
	
	//----------------------------------------------------------------------------------------------------------------
	/**
	 * 	sets the input values of the network
	 * 
	 * @param inputs	the input values to the network
	 */
	private void setInputs(float[] inputs) {
		if (inputs.length != this.num_input_nodes) {
			throw new IllegalArgumentException("Prediction input shape does not match the number of input nodes"); 
		}
		for(int i = 0; i < this.num_input_nodes; i++) {
			this.getInputLayer().getNodes()[i].setValue(inputs[i]);
		}
	}

}











