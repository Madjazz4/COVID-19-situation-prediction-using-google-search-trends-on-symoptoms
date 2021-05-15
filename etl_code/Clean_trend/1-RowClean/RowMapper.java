import java.io.IOException;
import java.util.*;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
public class RowMapper
    extends Mapper<LongWritable, Text, Text, Text> {
      private Text word = new Text();

      public void map(LongWritable key, Text value, Context context
                      ) throws IOException, InterruptedException {

        String line = value.toString();
        String[] tokens = line.split(",",0);
        if (((tokens[1].length()>2) && (tokens[1].length()<6) && (tokens[1].contains("US")))||(!(tokens[0].contains("2")))){
          Text keyy = new Text(tokens[0]);
          List<String> list_token = new ArrayList<String>(Arrays.asList(tokens));
          Text valuee = new Text(String.join(",",list_token.subList(1,tokens.length)));
          context.write(keyy,valuee);
        }
      }
}
