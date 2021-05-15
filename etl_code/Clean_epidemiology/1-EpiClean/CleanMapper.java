import java.io.IOException;
import java.util.*;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
public class CleanMapper
    extends Mapper<LongWritable, Text, Text, Text> {
      private Text word = new Text();

      public void map(LongWritable key, Text value, Context context
                      ) throws IOException, InterruptedException {

        String line = value.toString();
        String[] tokens = line.split(",",0);
        if (((tokens[1].length()>2) && (tokens[1].length()<6) && (tokens[1].contains("US")))||(!(tokens[0].contains("2")))){
        Text keyy = new Text(tokens[0]);
        Text valuee = new Text(","+tokens[1]+","+tokens[2]);
        context.write(keyy,valuee);
        }
      }
}
