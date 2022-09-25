package hit.android.stock.utlitiy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtility {

    public static boolean isDateValid(String date) {
        try {
            if (date != null && !date.isEmpty()) {
                LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            }
            return true;
        } catch (Exception e) {

        }
        return false;
    }

}
