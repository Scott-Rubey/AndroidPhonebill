package edu.pdx.cs410J.scrubey.phonebill;

import android.widget.EditText;

import java.util.HashSet;
import java.util.Set;

public class ErrorChecker {

    public static boolean custChk(EditText custBox){
        boolean pass = true;

        if (custBox.getText().length() == 0) {
            custBox.setError("Please enter customer name");
            pass = false;
        }

        return pass;
    }

    public static boolean callChk(EditText callBox){
        boolean pass = true;

        //handle callBox errors
        if (callBox.getText().length() == 0) {
            callBox.setError("Please enter caller number");
            pass = false;
        }
        else if (!ErrorChecker.checkPhNumFormat(callBox.getText().toString())) {
            callBox.setError("Phone number entered incorrectly");
            pass = false;
        }

        return pass;
    }

    public static boolean dateChk(EditText dateBox){
        boolean pass = true;

        //handle dateBox errors
        if(dateBox.getText().length() == 0){
            dateBox.setError("Please enter date");
            pass = false;
        }
        else if(!ErrorChecker.chkDateArgs(dateBox.getText().toString())){
            dateBox.setError("Date entered incorrectly");
            pass = false;
        }

        return pass;
    }

    public static boolean timeChk(EditText timeBox){
        boolean pass = true;

        //handle dateBox errors
        if(timeBox.getText().length() == 0){
            timeBox.setError("Please enter time");
            pass = false;
        }
        else if(!ErrorChecker.chkTimeArgs(timeBox.getText().toString())){
            timeBox.setError("Time entered incorrectly");
            pass = false;
        }

        return pass;
    }

    public static boolean suffixChk(EditText suffixBox){
        boolean pass = true;

        //if field is blank or incorrect, print error message
        if(suffixBox.getText().length() == 0 || !ErrorChecker.chkAMPM(suffixBox.getText().toString())) {
            suffixBox.setError("Please enter AM or PM");
            pass = false;
        }

        return pass;
    }

    /**
     * Checks to see if phone number was entered in correct format
     *
     * @param ph the <code>String</code> object containing the phone number
     * @return true/false value indicating whether the phone number is formatted properly
     */
    public static boolean checkPhNumFormat(String ph) {
        boolean pass = true;

        //create set of acceptable digits 0-9
        Set<Character> digits = createDigitSet();

        //if phone number is not proper length
        if (ph.length() != 12)
            pass = false;

        //check that the dash '-' is in the appropriate places
        if (pass && ((ph.charAt(3) != '-') || (ph.charAt(7) != '-')))
            pass = false;

        //if passing to this point and characters at non-dash positions aren't in range 0-9, return false
        if (pass) {
            for (int i = 0; i < 12; ++i) {
                if (i != 3 && i != 7 && !digits.contains(ph.charAt(i)))
                    pass = false;
            }
        }

        return pass;
    }

    /**
     * Checks for proper formatting of date arguments
     *
     * @param date <code>String</code> object containing the date entered by the user
     * @return returns true if date is formatted properly, otherwise false
     */
    public static boolean chkDateArgs(String date) {
        boolean pass = true;

        //verify date has correct number of characters
        pass = chkDateLength(date);

        //verify date has only 2 slashes
        if (pass)
            pass = chkDateSlashes(date);

        //verify any char that's not '/' is a digit
        if (pass)
            pass = chkDateDigits(date);

        //verify month is formatted properly
        if (pass)
            pass = chkMonthFormat(date);

        //verify day is formatted properly
        if (pass)
            pass = chkDayFormat(date);

        //verify year is formatted properly
        if (pass)
            pass = chkYearFormat(date);

        return pass;
    }

    /**
     * Verifies that date <code>String</code>is proper number of characters
     *
     * @param date <code>String</code> object containing the date entered by the user
     * @return true if date is in correct date range, false if not
     */
    public static boolean chkDateLength(String date) {
        boolean pass = true;

        if (date.length() > 10 || date.length() < 6)
            pass = false;

        return pass;
    }

    /**
     * Verifies that there are only two '/'s in the date <code>String</code>
     *
     * @param date <code>String</code> object containing the date entered by the user
     * @return true if there are two '/'s, false otherwise
     */
    public static boolean chkDateSlashes(String date) {
        boolean pass = true;
        int count = 0;

        for (int i = 0; i < date.length(); ++i) {
            if (date.charAt(i) == '/')
                ++count;
        }

        if (count != 2)
            pass = false;

        return pass;
    }

    /**
     * Verifies any <code>char</code> that's not a '/' is a digit 0 - 9
     *
     * @param date <code>String</code> object containing the date entered by the user
     * @return true if all characters are correct combination of digits and '/'
     */
    public static boolean chkDateDigits(String date) {
        boolean pass = true;
        Set<Character> digits = createDigitSet();

        for (int i = 0; i < date.length(); ++i) {
            if (date.charAt(i) == '/')
                continue;

                //otherwise, if the character is not a numerical digit...
            else if (!(digits.contains(date.charAt(i))))
                pass = false;
        }

        return pass;
    }

    /**
     * Verify the month in mm/dd/yyyy is in proper format
     *
     * @param date <code>String</code> object containing the date entered by the user
     * @return true if month in proper format; can be digits 1 - 12 and include leading zeros
     * false otherwise
     */
    public static boolean chkMonthFormat(String date) {
        boolean pass = false;
        int slashPos = 0;
        Set<Character> digits = createDigitSet();

        //find first slash in date string
        for (int i = 0; i < 3; ++i) {
            if (date.charAt(i) == '/')
                slashPos = i;
        }

        switch (slashPos) {
            //if slash at first index, pass = false
            case 0:
                break;
            case 1:
                //if slash at index 1, digit must be 1 - 9
                if (digits.contains(date.charAt(0)) && date.charAt(0) != '0')
                    pass = true;
                break;
            case 2:
                //if slash is at index 2, 1st digit must be 0 - 1
                //also verifies proper position of '/' character
                if ((date.charAt(0) == '0' || date.charAt(0) == '1') && date.charAt(1) != '/') {
                    //if first digit is '0', second digit can only be 1 - 9.  If it's not, flip 'pass' back to false
                    if (date.charAt(0) == '0' && digits.contains(date.charAt(1)) && date.charAt(1) != '0')
                        pass = true;
                        //if first digit is a '1', second digit can only be 0 - 2
                    else if (date.charAt(0) == '1' && (date.charAt(1) == '0' || date.charAt(1) == '1' || date.charAt(1) == '2'))
                        pass = true;
                }
                break;
        }

        return pass;
    }

    /**
     * Verify the dd in mm/dd/yyyy is in proper format
     *
     * @param date <code>String</code> object containing the date entered by the user
     * @return true if day in proper format; can be digits 1 - 31 and include leading zeros
     * false otherwise
     */
    //verify day is in proper format
    public static boolean chkDayFormat(String date) {
        boolean pass = false;
        Set<Character> digits = createDigitSet();
        boolean oneDigitDay = false;
        boolean twoDigitDay = false;
        int firstPos = 0;
        int secPos = 0;

        //find the index of the first '/'
        boolean found = false;
        for (int i = 0; i < date.length(); ++i) {
            if (date.charAt(i) == '/' && found == false) {
                firstPos = i;
                found = true;
            }
        }

        //find the index of the second '/'
        for (int i = firstPos + 1; i < date.length(); ++i) {
            if (date.charAt(i) == '/')
                secPos = i;
        }

        //find out whether it's a 1 or 2 digit day
        int daySize = secPos - firstPos;
        if (daySize == 2)
            oneDigitDay = true;
        else if (daySize == 3)
            twoDigitDay = true;

        //simple case: if 1 digit day and char is 1-9, ok
        if (oneDigitDay) {
            if (digits.contains(date.charAt(firstPos + 1)) && date.charAt(firstPos + 1) != '0')
                pass = true;
        }

        //harder case: if 2 digit day...cases explore first digit of the 2-digit day
        else if (twoDigitDay) {
            switch (date.charAt(firstPos + 1)) {
                case '0':
                    if (digits.contains(date.charAt(firstPos + 2)) && date.charAt(firstPos + 2) != '0')
                        pass = true;
                    break;
                case '1':
                    if (digits.contains(date.charAt(firstPos + 2)))
                        pass = true;
                    break;
                case '2':
                    if (digits.contains(date.charAt(firstPos + 2)))
                        pass = true;
                    break;
                case '3':
                    if (date.charAt(firstPos + 2) == '0' || date.charAt(firstPos + 2) == '1')
                        pass = true;
                    break;
                default:
                    pass = false;
            }
        } else
            pass = false;

        //final redundant check to verify the day is only 1 or 2 digits long
        if (!oneDigitDay && !twoDigitDay)
            pass = false;

        return pass;
    }

    /**
     * Verify the yyyy in mm/dd/yyyy is in proper format
     *
     * @param date <code>String</code> object containing the date entered by the user
     * @return true if year in proper format; can be digits 0 - 9, must be 4 digits long
     * false otherwise
     */
    public static boolean chkYearFormat(String date) {
        boolean pass = true;
        Set<Character> digits = createDigitSet();

        //verify last four digits are in range 0 - 9
        for (int i = date.length() - 1; i > date.length() - 5; --i) {
            if (!digits.contains(date.charAt(i)))
                pass = false;
        }

        //verify the character prior to the yyyy is a '/'
        if (date.charAt(date.length() - 5) != '/')
            pass = false;

        return pass;
    }

    /**
     * Verifies proper formatting for the time arguments
     *
     * @param time <code>String</code> object containing the date arguments, as entered by the user
     * @return true if time is formatted correctly, false otherwise.  Time can be 0:00 through 23:59.
     * Leading zeros are accepted when applicable.
     */
    public static boolean chkTimeArgs(String time) {
        boolean pass = true;
        int colonPos = 0;

        //make sure time is only 4 or 5 characters
        if (!(time.length() == 4 || time.length() == 5))
            pass = false;

        //make sure there's only one ':'
        if (pass) {
            int count = 0;
            for (int i = 0; i < time.length(); ++i) {
                if (time.charAt(i) == ':') {
                    ++count;
                    colonPos = i;
                }
            }
            if (count != 1)
                pass = false;
        }

        if (pass)
            pass = chkHoursFormat(time, colonPos);
        if (pass)
            pass = chkMinutesFormat(time, colonPos);

        return pass;
    }

    /**
     * Verifies hh in hh:mm is formatted correctly
     *
     * @param time     <code>String</code> object containing the date argument, as entered by the user
     * @param colonPos index of the ':' character
     * @return true if hh is formatted correct (1 - 12, leading zeros okay when applicable), false otherwise
     */
    public static boolean chkHoursFormat(String time, int colonPos) {
        boolean pass = true;
        Set<Character> digits = digits1thru9();
        Set<Character> zeroThruTwo = new HashSet<Character>() {{
            add('0');
            add('1');
            add('2');
        }};

        //verify hours is formatted properly
        //start with single digit
        if (colonPos == 1) {
            if (!digits.contains(time.charAt(0)))
                pass = false;
        }
        //verify two-digit hours are formatted properly
        else if (colonPos == 2) {
            switch (time.charAt(0)) {
                case '0':
                    if (!digits.contains(time.charAt(1)))
                        pass = false;
                    break;
                case '1':
                    if (!zeroThruTwo.contains(time.charAt(1)))
                        pass = false;
                    break;
                default:
                    pass = false;
            }
        }
        //if colon isn't in one of those two positions, timestamp is not valid
        else
            pass = false;

        return pass;
    }

    /**
     * Verifies mm in hh:mm is formatted correctly
     *
     * @param time     <code>String</code> object containing the date argument, as entered by the user
     * @param colonPos index of the ':' character
     * @return true if mm is formatted correctly (00 - 59), false otherwise
     */
    public static boolean chkMinutesFormat(String time, int colonPos) {
        boolean pass = true;
        Set<Character> digits = createDigitSet();

        //verify there are two digits in minutes
        int count = 0;
        for (int i = colonPos + 1; i < time.length(); ++i)
            ++count;
        if (count != 2)
            pass = false;

        //if passing to this point, check to make sure they're all numerical digits 0 - 59
        if (pass) {
            switch (time.charAt(colonPos + 1)) {
                case '0':
                    if (!digits.contains(time.charAt(colonPos + 2)))
                        pass = false;
                    break;
                case '1':
                    if (!digits.contains(time.charAt(colonPos + 2)))
                        pass = false;
                    break;
                case '2':
                    if (!digits.contains(time.charAt(colonPos + 2)))
                        pass = false;
                    break;
                case '3':
                    if (!digits.contains(time.charAt(colonPos + 2)))
                        pass = false;
                    break;
                case '4':
                    if (!digits.contains(time.charAt(colonPos + 2)))
                        pass = false;
                    break;
                case '5':
                    if (!digits.contains(time.charAt(colonPos + 2)))
                        pass = false;
                    break;
                default:
                    pass = false;
            }
        }

        return pass;
    }

    /**
     * Verifies time suffix is either AM or PM
     *
     * @param suffix morning/evening argument following the numerical time
     * @return true if suffix is correct (i.e. either AM or PM), false otherwise
     */
    public static boolean chkAMPM(String suffix) {
        boolean pass = false;

        if (suffix.toUpperCase().equals("AM") || suffix.toUpperCase().equals("PM"))
            pass = true;

        return pass;
    }

    /**
     * Creates a set of acceptable digits 0 - 9 of type char
     *
     * @return a <code>HashSet</code> containing all char digits 0 - 9
     */
    public static Set<Character> createDigitSet() {
        Set<Character> digits = new HashSet<Character>() {{
            add('0');
            add('1');
            add('2');
            add('3');
            add('4');
            add('5');
            add('6');
            add('7');
            add('8');
            add('9');
        }};

        return digits;
    }

    /**
     * Creates a set of acceptable digits 1 - 9 of type char
     *
     * @return a <code>HashSet</code> containing all char digits 0 - 9
     */
    public static Set<Character> digits1thru9() {
        Set<Character> digits = new HashSet<Character>() {{
            add('1');
            add('2');
            add('3');
            add('4');
            add('5');
            add('6');
            add('7');
            add('8');
            add('9');
        }};

        return digits;
    }
}
