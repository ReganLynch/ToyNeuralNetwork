
public class Layer {

	private int num_nodes;
	private Node[] nodes;
	private Layer input_layer;
	
	/**
	 * @param num_nodes			the number of nodes in this layer
	 */
	public Layer(int num_nodes) {
		this.num_nodes = num_nodes;
	}//end constructor
	
	
	public void setInputLayer(Layer input_layer) {
		this.input_layer = input_layer;
	}
	
	public Layer getInputLayer() {
		return this.input_layer;
	}
	
	public Node[] getNodes() {
		return this.nodes;
	}
	
	public void setNodes(Node[] new_nodes) {
		this.nodes = new_nodes;
	}
	
	public int getNumNodes() {
		return this.num_nodes;
	}
	
	public Tensor[] getNodeTensors() {
		Tensor[] node_tensors = new Tensor[this.num_nodes];
		for(int i = 0; i < this.num_nodes; i++) {
			node_tensors[i] = this.nodes[i].getTensor();
		}
		return node_tensors;
	}
	
}//end class layer
