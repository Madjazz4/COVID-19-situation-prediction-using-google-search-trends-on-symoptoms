import java.io.IOException;
import java.util.*;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
public class LibsvmMapper
    extends Mapper<LongWritable, Text, Text, Text> {
      private Text word = new Text();

      public void map(LongWritable key, Text value, Context context
                      ) throws IOException, InterruptedException {

        String line = value.toString();
        String[] tokens = line.split(",",-1);
        int length = tokens.length;
        for (int i =2; i<length; i++){
            tokens[i] = " "+String.valueOf(i-1)+":"+tokens[i];
          }
        if ((tokens[1].equals("1"))||(tokens[1].equals("0"))){
        Text keyy = new Text(tokens[1]);
        Text valuee = new Text(tokens[2]+tokens[3]+tokens[4]+tokens[5]+tokens[6]+tokens[7]+tokens[8]+tokens[9]);
        context.write(keyy,valuee);
      }
    }
  }
