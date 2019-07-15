
import java.lang.reflect.Method;

public enum ActivationTypes{
	
	SIGMOID("sigmoid", "sigmoidPrime", "sigmoidRandomizer");
	
	
	private Method activation_func;
	private Method activation_func_prime;
	private Method weight_initializer;
	
	private ActivationTypes(String activation_func, String activation_prime_func, String weight_initializer ){ 
		try {
			this.activation_func = ActivationFunctions.class.getMethod(activation_func, new Class[] {double.class});
			this.activation_func_prime = ActivationFunctions.class.getMethod(activation_prime_func, new Class[] {double.class});
			this.weight_initializer = ActivationFunctions.class.getMethod(weight_initializer);
		}catch(Exception e) {
			throw new IllegalArgumentException("Invalid activation / activation prime function");
		}
	}
	
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
	
	public float randomizer() {
		float new_val = 0;
		try {
			new_val = (float)this.weight_initializer.invoke(new Object());
		} catch (Exception e) {
			System.out.println("ERROR CALLING ACTIVATION INITIALIZER FUNCTION");
			e.printStackTrace();
		} 
		return new_val;
	}


}
