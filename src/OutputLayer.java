
/**
 * @author Regan Lynch
 * 
 * 		OutputLayer Class
 *		
 *			- defines an output layer object
 */
public class OutputLayer extends Layer {

	private int num_inputs;
	
	/**
	 * @param num_nodes		the number of nodes in this layer
	 * @param num_inputs	the number of nodes that each node in this layer is connected to
	 * @param activator		the activation type for this layer
	 */
	public OutputLayer(int num_nodes, int num_inputs, ActivationTypes activator) {
		super(num_nodes);
		this.num_inputs = num_inputs;
		
		//initialize this layer's nodes
		Node[] new_nodes = new Node[num_nodes];
		for(int i  = 0; i < num_nodes; i++) {
			new_nodes[i] = new Node(0, false, i, activator);
		}
		this.setNodes(new_nodes);
	}
	
	//-------------------------------------------------------------------------------------------
	/**
	 * 	deep copy constructor
	 * 
	 * @param copyLayer		the layer to be copied
	 */
	public OutputLayer(OutputLayer copyLayer) {
		super(copyLayer);
		this.num_inputs = copyLayer.num_inputs;
	}

	//----------------------------------------------------------------------------
	/**
	 * @return		the number of nodes that each node in this layer is connected to
	 */
	public int getNumInputs() {
		return this.num_inputs;
	}
	
}
