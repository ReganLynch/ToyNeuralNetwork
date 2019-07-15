import java.util.Arrays;

public class Tensor {
	
	private float[] values;
	
	public Tensor(float[] values) {
		this.values = values;
	}
	
	public Tensor multiply(float mult_val) {
		for(int i = 0; i < this.values.length; i++) {
			this.values[i] *= mult_val;
		}
		return this;
	}
	
	public Tensor add(float add_val) {
		for(int i = 0; i < this.values.length; i++) {
			this.values[i] += add_val;
		}
		return this;
	}
	
	public Tensor add(Tensor in_tensor) {
		if(in_tensor.getValues().length != this.values.length) {
			throw new IllegalArgumentException("CANNOT ADD TENSORS - DO NOT HAVE SAME SHAPE");
		}else {
			for(int i = 0; i < this.values.length; i++) {
				this.values[i] += in_tensor.getValues()[i];
			}
		}
		return this;
	}
	
	public Tensor subtract(Tensor in_tensor) {
		if(in_tensor.getValues().length != this.values.length) {
			throw new IllegalArgumentException("CANNOT SUBTRACT TENSORS - DO NOT HAVE SAME SHAPE");
		}else {
			for(int i = 0; i < this.values.length; i++) {
				this.values[i] -= in_tensor.getValues()[i];
			}
		}
		return this;
	}
	
	public Tensor activate(ActivationTypes activator) {
		for(int i = 0; i < this.values.length; i++) {
			this.values[i] = activator.activate(this.values[i]);
		}
		return this;
	}
	
	public Tensor activatePrime(ActivationTypes activator) {
		for(int i = 0; i < this.values.length; i++) {
			this.values[i] = activator.activatePrime(this.values[i]);
		}
		return this;
	}
	
	public float[] getValues() {
		return this.values;
	}
	
	public void setValues(float[] in_values) {
		this.values = in_values;
	}
	
	public float getAverage() {
		float sum = 0;
		for(int i = 0; i < this.values.length; i++) {
			sum += this.values[i];
		}
		return sum / this.values.length;
	}
	
	
	//for debugging purposes
	public void print() {
		System.out.println(Arrays.toString(this.values));
	}

}
