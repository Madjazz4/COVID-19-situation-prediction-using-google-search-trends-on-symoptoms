import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.util.Collections;
import java.util.ArrayList;
import java.lang.Double;

public class ProcessReducer
    extends Reducer<Text, Text, Text, Text> {
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
      ArrayList<String> valueList = new ArrayList<String>();
      ArrayList<String> stateList = new ArrayList<String>();
      for (Text val: values){
        valueList.add(val.toString());
        stateList.add(val.toString().split(",")[0]);
      }

      for (String state: stateList) {
        String result = state;
        for (int i = 1; i<9; i++){
          ArrayList<Double> myList = new ArrayList<Double>();


          for (String valll : valueList) {
            String[] tokens = valll.split(",");
            myList.add(Double.parseDouble(tokens[i]));
          }

          if (myList.size()>10){

          ArrayList<Double> sortedList = new ArrayList<Double>(myList);
          Collections.sort(sortedList,Collections.reverseOrder());
          int temp = 0;
          for (int n = 0; n < 10; n++){
            Double target = sortedList.get(n);

            if (stateList.get(myList.indexOf(target)) == state){
              result += ",1";
              temp = 1;
              break;
            }

          }

          if (temp == 0){
            result += ",0";}

        }
      }
        Text valuee = new Text(result);
        context.write(key,valuee);
      }
    }
  }
