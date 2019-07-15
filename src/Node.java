
/**
 * @author Regan Lynch
 * 
 * 		Node Class
 * 		
 * 			- defines a node object
 */
public class Node {
	
		private float bias;
		private float value;
		private float weights[];
		private boolean is_input_node;
		private int num_out_connections;
		private int node_number;

		/**
		 * @param num_out_connections		the number of connections that the node makes to the next layer
		 * @param is_input_node				if this node is an input node or not
		 * @param node_number				the identifier of which node this is in its layer
		 * @param activator					the activation type for this node
		 */
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
		
		//-------------------------------------------------------------------------------------------
		/**
		 * 	copy constructor
		 * 
		 * @param copyNode  the node to be copied to this object
		 */
		public Node(Node copyNode) {
			this.num_out_connections = copyNode.num_out_connections;
			this.is_input_node = copyNode.is_input_node;
			this.node_number = copyNode.node_number;
			this.bias = copyNode.bias;
			this.value = copyNode.value;
			this.weights = new float[copyNode.weights.length];
			for(int i = 0; i < copyNode.weights.length; i++) {
				this.weights[i] = copyNode.weights[i];
			}
		}
		
		//-------------------------------------------------------------------------------------------
		/**
		 * @param index		the index of the weight is this nodes list of weights
		 * @param value		the new weight
		 */
		public void setWeight(int index, float value) {
			if(index < 0 || index >= this.weights.length) {
				throw new IllegalArgumentException("INVALID WEIGHT INDEX");
			}
			this.weights[index] = value;
		}
		
		//-------------------------------------------------------------------------------------------
		/**
		 * @return		the index of this node in its layer
		 */
		public int getNodeNumber() {
			return this.node_number;
		}
		
		//-------------------------------------------------------------------------------------------
		/**
		 * @return		the bias of this node
		 */
		public float getBias() {
			return this.bias;
		}
		
		//-------------------------------------------------------------------------------------------
		/**
		 * @param new_bias		the new bias
		 */
		public void setBias(float new_bias){
			this.bias = new_bias;
		}
		
		//-------------------------------------------------------------------------------------------
		/**
		 * @return		the weights of this node
		 */
		public float[] getWeights() {
			return this.weights;
		}
		
		//-------------------------------------------------------------------------------------------
		/**
		 * @param new_weights		the new weights
		 */
		public void setWeights(float[] new_weights) {
			this.weights = new_weights;
		}
		
		//-------------------------------------------------------------------------------------------
		/**
		 * @return		returns the current value of this node
		 */
		public float getValue() {
			return this.value;
		}
		
		//-------------------------------------------------------------------------------------------
		/**
		 * @param new_value		the new value
		 */
		public void setValue(float new_value) {
			this.value = new_value;
		}
		
		//-------------------------------------------------------------------------------------------
		/**
		 * @return		if this node is an input node or not
		 */
		public boolean isInputNode() {
			return this.is_input_node;
		}
}

