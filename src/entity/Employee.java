package entity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Employee extends Person{
	private double salary;
	private int[] cards;
	private double[] disks;
	private Integer[] iis;
	private PC pc;
    private PC pcs[];
    private List<PC> pclist;
    private Map<String, PC> pcMap;
    private Map<String, List<PC>> difficult;

    private String[] tt;

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public int[] getCards() {
		return cards;
	}

	public void setCards(int[] cards) {
		this.cards = cards;
	}
	
	public String toString(){
		return "iis:"+Arrays.toString(iis)+";disks:"+Arrays.toString(disks)+";cards:"+Arrays.toString(cards)+";salary:"+salary+";"+super.toString();
	}

	public double[] getDisks() {
		return disks;
	}

	public void setDisks(double[] disks) {
		this.disks = disks;
	}

	public Integer[] getIis() {
		return iis;
	}

	public void setIis(Integer[] iis) {
		this.iis = iis;
	}

	public PC getPc() {
		return pc;
	}

	public void setPc(PC pc) {
		this.pc = pc;
	}

    public String[] getTt() {
        return tt;
    }

    public void setTt(String[] tt) {
        this.tt = tt;
    }

    public PC[] getPcs() {
        return pcs;
    }

    public void setPcs(PC[] pcs) {
        this.pcs = pcs;
    }

    public List<PC> getPclist() {
        return pclist;
    }

    public void setPclist(List<PC> pclist) {
        this.pclist = pclist;
    }

    public Map<String, PC> getPcMap() {
        return pcMap;
    }

    public void setPcMap(Map<String, PC> pcMap) {
        this.pcMap = pcMap;
    }

    public Map<String, List<PC>> getDifficult() {
        return difficult;
    }

    public void setDifficult(Map<String, List<PC>> difficult) {
        this.difficult = difficult;
    }
}
