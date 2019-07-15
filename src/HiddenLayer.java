
/**
 * @author Regan Lynch
 * 		
 * 		HiddenLayer Class
 * 
 * 			- defines a hidden layer object
 *
 */
public class HiddenLayer extends Layer {
	
	private int num_outputs;

	/**
	 * @param num_nodes		the number of nodes in this layer
	 * @param num_outputs	the number that each node in this layer is connected to
	 * @param activator		the activation type for this layer
	 */
	public HiddenLayer(int num_nodes, int num_outputs, ActivationTypes activator) {
		super(num_nodes);
		this.num_outputs = num_outputs;
		
		//initialize the nodes in this layer
		Node[] new_nodes = new Node[num_nodes];
		for(int i  = 0; i < num_nodes; i++) {
			new_nodes[i] = new Node(num_outputs, false, i, activator);
		}
		this.setNodes(new_nodes);
	}
	
	//-------------------------------------------------------------------------------------------
	/**
	 * 	deep copy constructor
	 * 
	 * @param copyLayer		layer to be copied
	 */
	public HiddenLayer(HiddenLayer copyLayer) {
		super(copyLayer);
		this.num_outputs = copyLayer.num_outputs;
	}

	//----------------------------------------------------------------------------
	/**
	 * @return		the number of nodes that each node in this layer is connected to
	 */
	public int getNumOutputs() {
		return this.num_outputs;
	}
	
}
