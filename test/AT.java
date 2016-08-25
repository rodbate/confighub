import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AT {


    public static void main(String[] args) {




        //周跨年
        //String str = String.format("201(5%s((2[%s-9])|(3[0-1])))|(6010[1-%s])", "12", "8", "3");
        /*Pattern pattern = Pattern.compile("201(512((2[8-9])|(3[0-1])))|(6010[1-3])");

        List<String> list = buildList();

        for (int i = 0; i < list.size(); i++) {
            Matcher matcher = pattern.matcher(list.get(i));

            if (matcher.find()){
                System.out.println(list.get(i));
            }
        }*/

        //周跨月不跨年
        //String str = String.format("201(5%s((2[%s-9])|(3[0-1])))|(6010[1-%s])", "12", "8", "3");

        /*Pattern pattern = Pattern.compile("2016(07((2[7-9])|(30)))|(080[1-3])");

        List<String> list = buildList();

        for (int i = 0; i < list.size(); i++) {
            Matcher matcher = pattern.matcher(list.get(i));

            if (matcher.find()){
                System.out.println(list.get(i));
            }
        }*/

        //周月内  1  20160802---20160808 last < 10
        String reg = "2016080[2-8]";
        //2 first < 10   last >= 10
        reg = "201608(0[7-9])|(1[0-4])";
        //3 first >= 10  last < 20
        reg = "2016081[0-6]";
        //4 first >=20   last < 30
        reg = "2016082[3-9]";
        //5 /*first > 20*/   last >=30
        reg = "201608(2[5-9])|(3[0-1])";


        //月维度
        reg = "201608.*";

        //年维度
        reg = "2016.*";


    }

    public static List<String> buildList(){

        List<String> list = new ArrayList<>();

        list.add("20160725");
        list.add("20160726");
        list.add("20160727");
        list.add("20160728");
        list.add("20160729");
        list.add("20160730");
        list.add("20160801");
        list.add("20160802");
        list.add("20160803");
        list.add("20160804");
        list.add("20160805");




        return list;
    }
}
