
/**
 * @author Regan Lynch
 * 
 * 		InputLayer Class
 * 			- defines an input layer object
 *
 */
public class InputLayer extends Layer{
	
	private int num_outputs;
	
	/**
	 * @param num_nodes			the number of nodes in this layer
	 * @param num_outputs		the number of nodes that each node in this layer has a connection to
	 * @param activator			the activation type of this layer
	 */
	public InputLayer(int num_nodes, int num_outputs, ActivationTypes activator) {
		super(num_nodes);
		this.num_outputs = num_outputs;
	
		//initialize this layer's nodes
		Node[] new_nodes = new Node[num_nodes];
		for(int i  = 0; i < num_nodes; i++) {
			new_nodes[i] = new Node(num_outputs, true, i, activator);
		}
		this.setNodes(new_nodes);
		
	}
	
	//-------------------------------------------------------------------------------------------
	/**
	 * 	deep copy constructor
	 * 
	 * @param copyLayer		layer to be copied
	 */
	public InputLayer(InputLayer copyLayer) {
		super(copyLayer);
		this.num_outputs = copyLayer.num_outputs;
	}
	
	//----------------------------------------------------------------------------
	/**
	 * @return		the number of nodes that each node in this layer is connected to (in the next layer)
	 */
	public int getNumOutputs() {
		return this.num_outputs;
	}

}
