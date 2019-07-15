import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;

/**
 * @author Regan Lynch
 * 
 * 			NeuralNetDrawer class
 * 
 * 				- a pretty hacky and ugly class to draw what a given neural network looks like.
 * 				- displays all connections, nodes, weights and biases 
 * 				- was created with the main purpose of debugging while developing
 *
 */
@SuppressWarnings("serial")
public class NeuralNetDrawer extends JFrame{

	private static final int FRAME_WIDTH = 900;
	private static final int FRAME_HEIGHT = 900;
	
	private static final Color NODE_COLOR = new Color(70, 240, 0);
	private static final Color CONNECTION_COLOR = new Color(0, 0, 0);
	private static final Color BIAS_COLOR = new Color(255, 0, 0);
	private static final Color WEIGHT_COLOR = new Color(66, 203, 245);
	private static final Color TEXT_COLOR = new Color(0, 0, 0);
	
	private NeuralNetwork neural_net;
	private int node_x_gap;
	private int node_r;
	
	public NeuralNetDrawer(NeuralNetwork nn) {
		this.neural_net = nn;
		this.node_x_gap = FRAME_WIDTH / (this.getNumberOfLayers() + 1);
		this.node_r = this.node_x_gap / 2;
	}
	
	public void start(String title) {
		this.setTitle(title);
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.paint(this.getGraphics());		//force the frame to draw now (not ideal)
	}
	
	public void paint(Graphics g) {
		//first paint the input layer
		int x_pos = this.node_x_gap;
		int y_pos = 0;
		int curr_y_gap = FRAME_HEIGHT / (this.neural_net.getInputLayer().getNodes().length + 1);
		//keep a list of all xy locations for nodes
		int node_index = 0;
		int[][] node_positions = new int[this.neural_net.getNumberTotalNodes()][2];
		ArrayList<int[]> connections = new ArrayList<int[]>();
		ArrayList<Float> weights = new ArrayList<Float>();
		int[][] input_layer_xys = new int[this.neural_net.getInputLayer().getNodes().length][2];
		ArrayList<String> biases = new ArrayList<String>();
		//get position of all input nodes
		for(int i = 0; i < this.neural_net.getInputLayer().getNodes().length; i++) {
			y_pos += curr_y_gap;
			node_positions[node_index] = new int[] {x_pos, y_pos};
			node_index++;
			input_layer_xys[i] = new int[] {x_pos, y_pos};
			biases.add(Double.toString(this.neural_net.getInputLayer().getNodes()[i].getBias()));
		}
		//calculate positions of hidden layer nodes
		for (int i = 0; i < this.neural_net.getHiddenLayers().length; i++) {	//loop through all hidden layers
			x_pos += this.node_x_gap;
			y_pos = 0;
			curr_y_gap = FRAME_HEIGHT / (this.neural_net.getHiddenLayers()[i].getNumNodes() + 1);
			//keep track of node position
			int [][] new_xys = new int[this.neural_net.getHiddenLayers()[i].getNumNodes()][2];
			for(int j = 0; j < this.neural_net.getHiddenLayers()[i].getNumNodes(); j++) {
				y_pos += curr_y_gap;
				//calculate position
				node_positions[node_index] = new int[] {x_pos, y_pos};
				node_index++;
				for (int k = 0; k < input_layer_xys.length; k++) {
					connections.add(new int[] {input_layer_xys[k][0], input_layer_xys[k][1], x_pos, y_pos});
				}
				//add weights from previous layer
				for(int k = 0; k < this.neural_net.getHiddenLayers()[i].getInputLayer().getNumNodes(); k++) {
					weights.add(this.neural_net.getHiddenLayers()[i].getInputLayer().getNodes()[k].getWeights()[j]);
				}
				biases.add(Double.toString(this.neural_net.getHiddenLayers()[i].getNodes()[j].getBias()));
				new_xys[j] = new int[] {x_pos, y_pos};
			}
			input_layer_xys = new_xys;
		}
		//calculate the output layer
		x_pos += this.node_x_gap;
		y_pos = 0;
		curr_y_gap = FRAME_HEIGHT / (this.neural_net.getOutputLayer().getNumNodes() + 1);
		for(int i = 0; i < this.neural_net.getOutputLayer().getNumNodes(); i++) {
			y_pos += curr_y_gap;
			//draw node
			node_positions[node_index] = new int[] {x_pos, y_pos};
			node_index++;
			for(int k = 0; k < input_layer_xys.length; k++) {
				connections.add(new int[] {input_layer_xys[k][0], input_layer_xys[k][1], x_pos, y_pos});
			}
			for(int k = 0; k < this.neural_net.getOutputLayer().getInputLayer().getNumNodes(); k++ ) {
				weights.add(this.neural_net.getOutputLayer().getInputLayer().getNodes()[k].getWeights()[i]);
			}
			biases.add(Double.toString(this.neural_net.getOutputLayer().getNodes()[i].getBias()));
		}
		//draw all connections and weights
		for(int i = 0; i < connections.size(); i++) {
			this.drawLine(connections.get(i)[0], connections.get(i)[1], connections.get(i)[2], connections.get(i)[3], g);
			int[] pt_on_line = this.getPointAlongLine(connections.get(i)[0], connections.get(i)[1], connections.get(i)[2], connections.get(i)[3], this.node_r/2 + 10);
			this.drawWeight(Double.toString(weights.get(i)), 0.8, pt_on_line[0], pt_on_line[1], 35, 12, g);
		}
		//draw all nodes
		for(int i = 0; i < node_positions.length; i++) {
			this.drawCircle(node_positions[i][0], node_positions[i][1], g);
		}
		//draw all the biases
		for (int i = this.neural_net.getInputLayer().getNumNodes(); i < biases.size(); i++) {
			this.drawBias(biases.get(i), 0.85, node_positions[i][0], node_positions[i][1] - 10, 33, 12, g);
		}
		//add legend
		this.drawBias("Bias", 1, 20, FRAME_HEIGHT - 20, 50, 20, g);
		this.drawWeight("Weight", 1, 20, FRAME_HEIGHT - 50, 50, 20, g);
	}
	
	
	
	private int getNumberOfLayers() {
		return this.neural_net.getNumHiddenLayers() + 2;
	}
	
	//helper function to draw circles
	private void drawCircle(int x, int y, Graphics g) {
		g.setColor(NODE_COLOR);
		g.fillOval(x - this.node_r/2, y - this.node_r/2, this.node_r, this.node_r);
	}
	
	//helper function to draw connection lines
	private void drawLine(int x1, int y1, int x2, int y2, Graphics g) {
		g.setColor(CONNECTION_COLOR);	
		g.drawLine(x1, y1, x2, y2);
	}
	
	private void drawBias(String text, double font_factor, int x, int y, int w, int h, Graphics g) {
		Font currentFont = g.getFont();
		float initial_font_size = currentFont.getSize();
		Font newFont = currentFont.deriveFont((float) (currentFont.getSize() * font_factor));
		g.setFont(newFont);
		if (text.length() > 7) {
			text = text.substring(0, 5);
		}
		g.setColor(BIAS_COLOR);
		g.fillRect(x, y - h, w, h);
		g.setColor(TEXT_COLOR);
		g.drawString(text, x+5, y - h/3);
		newFont = currentFont.deriveFont(initial_font_size);
		g.setFont(newFont);
	}
	
	private void drawWeight(String text, double font_factor, int x, int y, int w, int h, Graphics g) {
		Font currentFont = g.getFont();
		float initial_font_size = currentFont.getSize();
		Font newFont = currentFont.deriveFont((float) (currentFont.getSize() * font_factor));
		g.setFont(newFont);
		if (text.length() > 7) {
			text = text.substring(0, 5);
		}
		g.setColor(WEIGHT_COLOR);
		g.fillRect(x, y - h, w, h);
		g.setColor(TEXT_COLOR);
		g.drawString(text, x+5, y - h/3);
		newFont = currentFont.deriveFont(initial_font_size);
		g.setFont(newFont);
	}
	
	private int[] getPointAlongLine(int x1, int y1, int x2, int y2, int distance) {
		int[] new_point = new int[2];
		new_point[0] = (int)(x1 + ((x2 - x1) / Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2))) * distance );
		new_point[1] = (int)(y1 + ((y2 - y1) / Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2))) * distance );
		return new_point;
		
	}
	

	
}
