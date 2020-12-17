package edu.pdx.cs410J.scrubey.phonebill;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PhoneBillCollection implements Serializable {
    private HashMap<String, PhoneBill> phoneBills = new HashMap<>();

    protected void addPhoneCall(String customer, String caller, String callee, String start, String end){
        PhoneCall call = new PhoneCall(caller, callee, start, end);

        //if an active phonebill already exists...
        if(phoneBills.containsKey(customer)){
            PhoneBill bill = phoneBills.get(customer);
            bill.addPhoneCall(call);
        }
        //otherwise, if a new phonebill must be created...
        else{
            PhoneBill bill = new PhoneBill(customer);
            bill.addPhoneCall(call);
            phoneBills.put(customer, bill);
        }
    }

    protected void addPhoneBill(PhoneBill bill){
        phoneBills.put(bill.getCustomer(), bill);
    }

    protected PhoneBill getPhoneBill(String customer) {
        return this.phoneBills.get(customer);
    }

    public PhoneBill searchPhoneBills(String customer, String start, String end) {
        TreeSet<PhoneCall> toDelete = new TreeSet<>();

        PhoneBill bill = getPhoneBill(customer);

        if(bill != null) {
            for (PhoneCall call : bill.getPhoneCalls()) {
                if (!(call.getStartTime().after(convToDateObj(start)) && call.getStartTime().before(convToDateObj(end))))
                    toDelete.add(call);
            }
            //remove all phone calls outside the given parameters from the original bill
            bill.getPhoneCalls().removeAll(toDelete);
        }

        return bill;
    }

    /**
     * Parses the given string object into a <code>Date</code> object
     * @param s the <code>String</code> representation of the date to be parsed
     * @return the parsed <code>Date</code>
     */
    private Date convToDateObj(String s){
        Date dateTime = null;

        try {
            dateTime = new SimpleDateFormat("MM/dd/yyyy hh:mm a").parse(s);
        } catch (ParseException e) {
            System.err.println("\nError parsing date");
        }

        return dateTime;
    }

    protected HashMap<String, PhoneBill> getPhoneBills(){
        return this.phoneBills;
    }
}
