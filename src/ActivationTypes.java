
import java.lang.reflect.Method;

/**
 * @author Regan Lynch
 * 
 * 			ActivationTypes Enumerations
 *			
 *				- defines the different kinds of pre-determined activation types
 */
public enum ActivationTypes{
	
	/**
	 * 	the sigmoid activation type
	 */
	SIGMOID("sigmoid", "sigmoidPrime", "sigmoidRandomizer"),
	
	/**
	 * 	the rectified linear unit activation type
	 */
	RELU("relu", "reluPrime", "sigmoidRandomizer" );
	
	
	private Method activation_func;
	private Method activation_func_prime;
	private Method weight_initializer;
	
	/**
	 * @param activation_func			the name of the function that performs the activation
	 * @param activation_prime_func		the name of the function that performs the derivitive of the activation function
	 * @param weight_initializer		the name of the function that returns a pseudo-random number for weight / bias initialization for this activation type
	 */
	private ActivationTypes(String activation_func, String activation_prime_func, String weight_initializer ){ 
		try {
			this.activation_func = ActivationFunctions.class.getMethod(activation_func, new Class[] {double.class});
			this.activation_func_prime = ActivationFunctions.class.getMethod(activation_prime_func, new Class[] {double.class});
			this.weight_initializer = ActivationFunctions.class.getMethod(weight_initializer);
		}catch(Exception e) {
			throw new IllegalArgumentException("Invalid activation / activation prime function");
		}
	}
	
	//----------------------------------------------------------------------------
	/**
	 * @param in_val		the value to be activated
	 * @return				the activated value
	 */
	public float activate(double in_val) {
		float new_val = 0;
		try {
			new_val = (float)this.activation_func.invoke(new Object(), in_val);
		} catch (Exception e) {
			System.out.println("ERROR CALLING ACTIVATION FUNCTION");
			e.printStackTrace();
		} 
		return new_val;
	}
	
	//----------------------------------------------------------------------------
	/**
	 * @param in_val	the value to be passed to the derivitive of the activation function
	 * @return			the value passed back from the derivitive of the activation function
	 */
	public float activatePrime(double in_val) {
		float new_val = 0;
		try {
			new_val = (float)this.activation_func_prime.invoke(new Object(), in_val);
		} catch (Exception e) {
			System.out.println("ERROR CALLING ACTIVATION PRIME FUNCTION");
			e.printStackTrace();
		} 
		return new_val;
	}
	
	//----------------------------------------------------------------------------
	/**
	 * @return		the generated random initialization value for this activation type
	 */
	public float randomizer() {
		float new_val = 0;
		try {
			new_val = (float)this.weight_initializer.invoke(new Object());
		} catch (Exception e) {
			System.out.println("ERROR CALLING ACTIVATION PRIME FUNCTION");
			e.printStackTrace();
		} 
		return new_val;
	}


}
