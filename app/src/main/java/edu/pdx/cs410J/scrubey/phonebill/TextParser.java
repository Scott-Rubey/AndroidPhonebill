package edu.pdx.cs410J.scrubey.phonebill;

import edu.pdx.cs410J.PhoneBillParser;

import java.io.*;

public class TextParser implements PhoneBillParser<PhoneBill> {
    protected final Reader reader;

    /**
     * Creates <code>TextParser</code> object
     * @param reader the <code>Reader</code> object passed in from Project4
     */
    public TextParser(Reader reader){
        this.reader = reader;
    }

    /**
     * Delegates functionality for parsing text file
     * @return <code>PhoneBill</code> object created from parsed file
     */
    public PhoneBill parse(){
        PhoneBill bill = null;
        BufferedReader br = new BufferedReader(this.reader);

        try {
            //parse customer's name, create new bill object under that name
            String customer = parseCustomer(br);
            bill = new PhoneBill(customer);

            //find list of phone calls (if any), create phonecall objects to add to phonebill's list
            Boolean errorsFound = getCallInfo(br, bill);

            reader.close();
        } catch (IOException e) {
            System.out.print("\nFile not found");
        }

        return bill;
    }

    /**
     * Parses the customer's name from the text file
     * @param br <code>BufferedReader</code> object for reading text file
     * @return customer's name
     */
    public String parseCustomer(BufferedReader br){
        String line = null;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return line;
    }

    /**
     * Parses call info (caller, callee, start/end date, start/end time) from text file
     * @param br <code>BufferedReader</code> object
     * @param bill Current <code>PhoneBill</code> object
     * @return True if a call was parsed, false if no call was found in the parsed bill
     */
    public Boolean getCallInfo(BufferedReader br, PhoneBill bill){
        String line = null;

        //move to next line, parse call info
        Boolean foundCall = false;
        String[] callInfo;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(line != null && !line.equals("")){
            //split line based on comma-space delimiter
            callInfo = line.split(" |, ");
            for(String arg:callInfo) {
                arg.trim();
            }

            //check parsed args for errors
            Boolean formatOK = chkArgFormatting(callInfo);
            if(!formatOK){
                System.err.print("PhoneBill information formatted incorrectly");
                System.exit(1);
            }

            addCallToBill(callInfo, bill);
            foundCall = true;

            try {
                line = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return foundCall;
    }

    /**
     * Error checks the parsed call info
     * @param callInfo <code>String</code> array containing call info (caller, callee, et al)
     * @return True if all error checks pass, false otherwise
     */
    public Boolean chkArgFormatting(String[] callInfo){
        Boolean pass = false;
        String caller = null;
        String callee = null;
        String startDate = null;
        String startTime = null;
        String startSuffix = null;
        String endDate = null;
        String endTime = null;
        String endSuffix = null;

        //check for proper number of arguments
        if(callInfo.length == 8)
            pass = true;

        if(pass) {
            caller = callInfo[0];
            callee = callInfo[1];
            startDate = callInfo[2];
            startTime = callInfo[3];
            startSuffix = callInfo[4];
            endDate = callInfo[5];
            endTime = callInfo[6];
            endSuffix = callInfo[7];
        }

        //verify caller/callee are formatted correctly
        if(pass)
            pass = ErrorChecker.checkPhNumFormat(caller);
        if(pass)
            pass = ErrorChecker.checkPhNumFormat(callee);

        //verify startDate/EndDate are formatted correctly
        if(pass)
            pass = ErrorChecker.chkDateArgs(startDate);
        if(pass)
            pass = ErrorChecker.chkDateArgs(endDate);

        //verify startTime/endTime are formatted correctly
        if(pass)
            pass = ErrorChecker.chkTimeArgs(startTime);
        if(pass)
            pass = ErrorChecker.chkTimeArgs(endTime);

        //verify startSuffix/endSuffix are formatted correctly
        if(pass)
            pass = ErrorChecker.chkAMPM(startSuffix);
        if(pass)
            pass = ErrorChecker.chkAMPM(endSuffix);

        return pass;
    }

    /**
     * Adds a <code>PhoneCall</code> to the current <code>PhoneBill</code> object's call list
     * @param callInfo <code>String</code> array containing call info
     * @param bill the current <code>PhoneBill</code> object
     */
    public void addCallToBill(String[] callInfo, PhoneBill bill){
        //add args to new phonecall object
        String caller = callInfo[0];
        String callee = callInfo[1];
        String startDate = callInfo[2];
        String startTime = callInfo[3];
        String startSuffix = callInfo[4];
        String endDate = callInfo[5];
        String endTime = callInfo[6];
        String endSuffix = callInfo[7];

        String start = startDate + " " + startTime + " " + startSuffix;
        String end = endDate + " " + endTime + " " + endSuffix;

        PhoneCall call = new PhoneCall(caller, callee, start, end);

        //one last error check -- verify end time of call isn't prior to start time
        boolean pass = call.chkEndAfterStart();
        if(!pass){
            System.err.println("\nParsing error: call's end time is prior to start time\n");
            System.exit(1);
        }

        bill.addPhoneCall(call);
    }

    protected PhoneBillCollection parseCollection(){
        PhoneBillCollection coll = new PhoneBillCollection();
        PhoneBill bill = null;
        BufferedReader br = new BufferedReader(this.reader);
        String customer;

        try {
            do {
                //parse customer's name, create new bill object under that name
                customer = parseCustomer(br);
                if(customer != null) {
                    bill = new PhoneBill(customer);

                    //find list of phone calls (if any), create phonecall objects to add to phonebill's list
                    Boolean callFound = getCallInfo(br, bill);
                    coll.addPhoneBill(bill);
                }
            } while (customer != null);

            reader.close();
        } catch(IOException e){
            System.out.print("\nFile not found");
        }

        return coll;
    }
}
