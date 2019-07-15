
public class Node {
	
		private float bias;
		private Tensor tensor;
		private float weights[];
		private boolean is_input_node;
		private int num_out_connections;
		private final int node_number;

		public Node(int num_out_connections, boolean is_input_node, int node_number, ActivationTypes activator) {
			this.num_out_connections = num_out_connections;
			this.is_input_node = is_input_node;
			this.weights = new float[num_out_connections];
			this.node_number = node_number;
			
			//initialize the out weights
			for (int i = 0; i < this.num_out_connections; i++) {
				this.weights[i] = activator.randomizer();
			}
			//set the initial bias (if its an input node than there is no bias)
			if(!this.is_input_node) {
				this.bias = activator.randomizer();
			}else {
				this.bias = 0;
			}
		}
		
		public void setWeight(int index, float value) {
			if(index < 0 || index >= this.weights.length) {
				throw new IllegalArgumentException("INVALID WEIGHT INDEX");
			}
			this.weights[index] = value;
		}
		
		public int getNodeNumber() {
			return this.node_number;
		}
		
		public float getBias() {
			return this.bias;
		}
		
		public void setBias(float new_bias){
			this.bias = new_bias;
		}
		
		public float[] getWeights() {
			return this.weights;
		}
		
		public void setWeights(float[] new_weights) {
			this.weights = new_weights;
		}
		
		public Tensor getTensor() {
			return this.tensor;
		}
		
		public void setTensor(Tensor in_tensor) {
			this.tensor = in_tensor;
		}
		
		public boolean isInputNode() {
			return this.is_input_node;
		}
}

