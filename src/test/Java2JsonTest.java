package test;

import com.yuanmie.json.Java2Json;
import entity.Employee;
import entity.PC;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Java2JsonTest {
    /**
     *
     * @param args
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
    public static void main(String args[]) throws SecurityException,
            NoSuchFieldException {
        Employee e = new Employee();
        e.setAge(20);
        e.setId("1111");
        e.setName("bond");
        e.setSalary(100000.59999);
        e.setSex("male");
        e.setCards(new int[]{1, 22, 333});
        e.setDisks(new double[]{1.1, 2, 22, 3, 44});
        e.setIis(new Integer[]{1, 11111, 222222});
        e.setTt(new String[]{"hhh", "kkk"});


        PC pc = new PC();
        pc.setBrand("dell");
        pc.setPrice(5000);
        e.setPc(null);
        e.setPcs(new PC[]{pc, pc});
        Map<String, PC> pcmaps = new HashMap<String, PC>();
        pcmaps.put("one", pc);
        pcmaps.put("two", pc);
        e.setPclist(Arrays.asList(new PC[]{pc, pc}));
        e.setPcMap(pcmaps);
        Map<String, List<PC>> difficults = new HashMap<String, List<PC>>();
        difficults.put("hello", Arrays.asList(new PC[]{pc, pc}));
        difficults.put("world", Arrays.asList(new PC[]{pc, pc}));
        e.setDifficult(difficults);

        System.out.println(Java2Json.convert(e));
    }
}
