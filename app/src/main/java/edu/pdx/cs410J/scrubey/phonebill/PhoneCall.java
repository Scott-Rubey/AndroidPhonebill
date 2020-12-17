package edu.pdx.cs410J.scrubey.phonebill;

import edu.pdx.cs410J.AbstractPhoneCall;

import java.io.Serializable;
import java.text.*;
import java.util.Date;

/**
 * This class is represents a <code>PhoneCall</code>.
 */
public class PhoneCall extends AbstractPhoneCall implements Comparable<PhoneCall>, Serializable {

    protected String callerNumber;
    protected String calleeNumber;
    protected String start;
    protected String end;

    /**
     * Creates a new <code>PhoneCall</code>
     *
     * @param caller
     *        The 10-digit telephone number of the caller in nnn-nnn-nnnn format
     * @param callee
     *        The 10-digit telephone number of the callee in nnn-nnn-nnnn format
     * @param start
     *        The starting date/time of the telephone call
     * @param end
     *        The ending date/time of the telephone call
     */
    protected PhoneCall(String caller, String callee, String start, String end){
        this.callerNumber = caller;
        this.calleeNumber = callee;
        this.start = start;
        this.end = end;
    }

    /**
     * @return the caller's telephone number
     */
    @Override
    public String getCaller() {
        return this.callerNumber;
    }

    /**
     * @return the callee's telephone number
     */
    @Override
    public String getCallee() {
        return this.calleeNumber;
    }

    /**
     * Combines startDate and startTime strings and converts to a <code>Date</code> object
     * @return <code>Date</code> object containing the start date/time
     */
    @Override
    public Date getStartTime(){
        Date startTime = null;

        try {
            startTime = new SimpleDateFormat("MM/dd/yyyy hh:mm a").parse(this.start);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return startTime;
    }

    /**
     * Combines endDate and endTime strings and converts to a <code>Date</code> object
     * @return <code>Date</code> object containing the end date/time
     */
    @Override
    public Date getEndTime(){
        Date endTime = null;

        try {
            endTime = new SimpleDateFormat("MM/dd/yyyy hh:mm a").parse(this.end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return endTime;
    }

    /**
     * @return the start time of the telephone call as a <code>String</code>
     */
    @Override
    public String getStartTimeString() {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(getStartTime());
    }

    /**
     * @return the end time of the telephone call as a <code>String</code>
     */
    @Override
    public String getEndTimeString() {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(getEndTime());
    }

    /**
     * Used to sort based on startTime, then phone number
     * @param toCompare the object for comparison against the current object
     * @return positive number if compares higher, lower if it compares lower, 0 if equal
     */
    public int compareTo(PhoneCall toCompare){
        int comp = getStartTime().compareTo(toCompare.getStartTime());
        if(comp == 0)
            comp = getCaller().compareTo(toCompare.getCaller());

        return comp;
    }

    /**
     * Verifies end time is greater than start time
     * @return true if end time is after start time, false otherwise
     */
    public boolean chkEndAfterStart(){
        return getStartTime().before(getEndTime());
    }
}
