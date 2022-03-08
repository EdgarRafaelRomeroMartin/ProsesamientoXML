
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
public class PruebaDeDate {
    public static void main(String[] args)throws Exception {
        String sDate1="1998-12-31";
        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
        ZoneId timeZone = ZoneId.systemDefault();
        LocalDate getLocalDate = date1.toInstant().atZone(timeZone).toLocalDate();
        System.out.println(getLocalDate.getMonth());
    }
}