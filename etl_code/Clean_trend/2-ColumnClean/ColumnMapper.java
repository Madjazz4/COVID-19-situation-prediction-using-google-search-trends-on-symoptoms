import java.io.IOException;
import java.util.*;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
public class ColumnMapper
    extends Mapper<LongWritable, Text, Text, Text> {
      private Text word = new Text();

      public void map(LongWritable key, Text value, Context context
                      ) throws IOException, InterruptedException {

        String line = value.toString();
        String[] tokens = line.split(",",0);
        Text keyy = new Text(tokens[0]);
        Text valuee = new Text(","+tokens[142]+","+tokens[93]+","+tokens[139]+","+tokens[367]+","+tokens[353]+","+tokens[364]+","+tokens[82]+","+tokens[76]);
        context.write(keyy,valuee);
        }
}
