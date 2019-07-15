
/**
 * @author Regan Lynch
 * 
 * 	Generic Layer Class
 * 		Extended by:
 * 			- InputLayer
 * 			- HiddenLayer
 *			- OutputLayer
 */
public class Layer {

	private int num_nodes;
	private Node[] nodes;
	private Layer input_layer;
	
	//----------------------------------------------------------------------------
	/**
	 * 	Layer constructoe
	 * 
	 * @param num_nodes			the number of nodes in this layer
	 */
	public Layer(int num_nodes) {
		this.num_nodes = num_nodes;
	}
	
	//----------------------------------------------------------------------------
	/**
	 * 	deep copy constructor
	 * 
	 * @param copyLayer		the layer to be copied
	 */
	public Layer(Layer copyLayer) {
		this.num_nodes = copyLayer.num_nodes;
		this.nodes = new Node[copyLayer.nodes.length];
		for(int i = 0; i < this.nodes.length; i++) {
			this.nodes[i] = new Node(copyLayer.nodes[i]);
		}
	}
	
	//----------------------------------------------------------------------------
	/**
	 * 	sets the inputs layer for this layer
	 * 	
	 * @param input_layer	the layer to be input to this layer
	 */
	public void setInputLayer(Layer input_layer) {
		this.input_layer = input_layer;
	}
	
	//----------------------------------------------------------------------------
	/**
	 * @return		the input layer for this layer
	 */
	public Layer getInputLayer() {
		return this.input_layer;
	}
	
	//----------------------------------------------------------------------------
	/**
	 * @return		array of all nodes in this layer
	 */
	public Node[] getNodes() {
		return this.nodes;
	}
	
	//----------------------------------------------------------------------------
	/**
	 * @param new_nodes		the nodes to be in this layer
	 */
	public void setNodes(Node[] new_nodes) {
		this.nodes = new_nodes;
		this.num_nodes = new_nodes.length;
	}
	
	//----------------------------------------------------------------------------
	/**
	 * @return		the number of nodes in this layer
	 */
	public int getNumNodes() {
		return this.num_nodes;
	}
	
	//----------------------------------------------------------------------------
	/**
	 * @return		an array of the values in each node in this layer
	 */
	public float[] getNodeValues() {
		float[] node_values = new float[this.num_nodes];
		for(int i = 0; i < this.num_nodes; i++) {
			node_values[i] = this.nodes[i].getValue();
		}
		return node_values;
	}
	
}//end class layer
