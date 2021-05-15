import java.io.IOException;
import java.util.*;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
public class ProcessMapper
    extends Mapper<LongWritable, Text, Text, Text> {
      private Text word = new Text();

      public void map(LongWritable key, Text value, Context context
                      ) throws IOException, InterruptedException {

        String line = value.toString();
        String[] tokens = line.split(",",-1);
        for (int i = 0; i<tokens.length; i++){
          if (tokens[i].equals("")){
            tokens[i] = "0";
          }
        }
        if (tokens[0].contains("US")){
        Text keyy = new Text(tokens[0].substring(0,10));
        Text valuee = new Text(tokens[0].substring(11,16)+","+tokens[1]+","+tokens[2]+","+tokens[3]+","+tokens[4]+","+tokens[5]+","+tokens[6]+","+tokens[7]+","+tokens[8]);
        context.write(keyy,valuee);
      }
        }
}
