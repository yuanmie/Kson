package entity;
import java.util.List;
import java.util.Map;


public class Entity {
	private Boolean one;
	private Boolean two;
	private Double three;
	private List four;
	private Map<String, Object> five;

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	private String str;
	
	
	public Entity() {
		super();
	}
	public boolean getOne() {
		return one;
	}
	public void setOne(Boolean one) {
		this.one = one;
	}
	public boolean getTwo() {
		return two;
	}
	public void setTwo(Boolean two) {
		this.two = two;
	}
	public double getThree() {
		return three;
	}
	public void setThree(double three) {
		this.three = three;
	}
	public List getFour() {
		return four;
	}
	public void setFour(List four) {
		this.four = four;
	}
	public Map<String, Object> getFive() {
		return five;
	}
	public void setFive(Map<String, Object> five) {
		this.five = five;
	}
	
	public String toString(){
		return "one : " +  one.toString() +
				"\ntwo : " + two.toString() + 
				"\nthree : " + three.toString() + 
				"\nfour : " + four.toString() +
				"\nstr : " + str.toString() +
				"\nfive : " + five.toString();
	}

}
