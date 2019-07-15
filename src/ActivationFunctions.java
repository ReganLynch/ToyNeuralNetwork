
public class ActivationFunctions {
	
	public static float sigmoid(double in_val) {
		return (float)(1 / (1 + Math.pow(Math.E, -in_val)));
	}
	
	public static float sigmoidPrime(double in_val){
		return (float)(in_val * (1 - in_val));
	}
	
	//for sigmoid activation function, best to initialize values close to 0.5 (here using 0.4-0.6)
	public static float sigmoidRandomizer() {
		return (float)(0.4 + (0.6 - 0.4) * Math.random());
	}

}
