package edu.pdx.cs410J.scrubey.phonebill;
import edu.pdx.cs410J.AbstractPhoneBill;

import java.io.Serializable;
import java.util.*;

/**
 * This class is represents a <code>PhoneBill</code>.
 */
public class PhoneBill extends AbstractPhoneBill<PhoneCall> implements Comparable<PhoneBill>, Serializable {

    protected String customer;
    protected TreeSet<PhoneCall> calls;

    /**
     * Builds a <code>PhoneBill</code> class object
     * @param customer The customer's name.  May be one or more words, and may contain
     *                 letters, numbers and symbols.
     */
    protected PhoneBill(String customer){
        this.customer = customer;
        this.calls = new TreeSet<>();
    }

    /**
     * @return the customer's name
     */
    @Override
    public String getCustomer(){
        return this.customer;
    }

    /**
     * Adds the <code>PhoneCall</code> object to the bill
     */
    @Override
    public void addPhoneCall(PhoneCall call){
        this.calls.add(call);
    }

    /**
     * @return the list of calls attributed to the bill
     */
    @Override
    public TreeSet<PhoneCall> getPhoneCalls(){
        return this.calls;
    }

    /**
     * Used to sort based on customer name, then phone number
     * @param toCompare the object for comparison against the current object
     * @return positive number if compares higher, lower if it compares lower, 0 if equal
     */
    public int compareTo(PhoneBill toCompare){
        return getCustomer().compareTo(toCompare.getCustomer());
    }
}
