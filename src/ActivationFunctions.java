
/**
 * @author Regan Lynch
 *
 *
 *			ActivationFunctions class
 *
 *				- defines methods for different activation types
 */
public class ActivationFunctions {
	
	//----------------------------------------------------------------------------
	/**
	 * 	sigmoid activation function
	 * 
	 * @param in_val 	value to be activated by the sigmoid function
	 * @return
	 */
	public static float sigmoid(double in_val) {
		return (float)(1 / (1 + Math.pow(Math.E, -in_val)));
	}
	
	//----------------------------------------------------------------------------
	/**
	 * 	the derivitive of the sigmoid activation function	
	 * 
	 * @param in_val	the value passed to the derivitive of the sigmoid activation function
	 * @return			the value returned from the derivitive of the sigmoid activation function
	 */
	public static float sigmoidPrime(double in_val){
		return (float)(in_val * (1 - in_val));
	}
	
	//----------------------------------------------------------------------------
	/**
	 * 	with sigmoid activation, its best to initialize weights / biases in the range 0.4-0.6 (or generally just near 0.5)
	 * 
	 * @return		random value in range 0.4-0.6
	 */
	public static float sigmoidRandomizer() {
		return (float)(0.4 + (0.6 - 0.4) * Math.random());
	}
	
	//----------------------------------------------------------------------------
	/**
	 * 	rectified linear unit activation function
	 * 
	 * @param in_val	the value to be passed to the the rectified linear unit activation function
	 * @return			the value to be returned to the the rectified linear unit activation function
	 */
	public static float relu(double in_val) {
		return (in_val > 0)? (float)in_val: 0;
	}
	
	//----------------------------------------------------------------------------
	/**
	 * 	derivitive of the rectified linear unit activation function
	 * 
	 * @param in_val	the value to be passed to the derivitive of the relu activation function
	 * @return			the value to be returned to the derivitive of the relu activation function
	 */
	public static float reluPrime(double in_val) {
		if(in_val > 0) {
			return 1;
		}else if(in_val < 0) {
			return 0;
		}
		return 0.5f;
	}
	
	
	
	

}
