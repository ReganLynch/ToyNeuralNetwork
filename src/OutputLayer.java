
public class OutputLayer extends Layer {

	private int num_inputs;
	
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

	
	public int getNumInputs() {
		return this.num_inputs;
	}
	
}
