
public class InputLayer extends Layer{

	private int num_outputs;
	
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
	
	public int getNumOutputs() {
		return this.num_outputs;
	}

}
