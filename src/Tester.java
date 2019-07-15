import java.util.Arrays;

//import java.util.Arrays;

public class Tester {

	public static void main(String[] args){
		
		NeuralNetwork nn = new NeuralNetwork("2, 3, 4", ActivationTypes.SIGMOID);
						
		double[][] ins = new double[][] {{1, 1, 0, 0}, {1, 0, 1, 0}};
		double[] outs = new double[] {0, 1, 1, 0};
		
		
		nn.display("nn");
		
//		System.out.println("Initial Predictions: ");
//		print2DArray(nn.predict(ins));
		
		nn.train(ins, outs, 1, 1);
		
//		System.out.println("Final Predictions: ");
//		print2DArray(nn.predict(ins));

		
	}
	
	
	//helps in printing output
	public static void print2DArray(double[][] arr) {
		for(int i = 0; i < arr.length; i++) {
			System.out.println("  " + Arrays.toString(arr[i]));
		}
	}


}

