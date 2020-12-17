package edu.pdx.cs410J.scrubey.phonebill;

import java.io.PrintWriter;

public class PrettyPrinter {
    protected final PrintWriter writer;

    /**
     * Creates <code>PrettyPrinter</code> object
     *
     * @param writer the <code>PrintWriter</code> object passed in from Project4.java
     */
    public PrettyPrinter(PrintWriter writer) {
        this.writer = writer;
    }

    protected String getBillText(PhoneBill bill) {
        StringBuilder sb = new StringBuilder();
        String customer = bill.getCustomer();

        sb.append("Customer name: " + customer + "\n");

        for(PhoneCall call : bill.getPhoneCalls()){
            int duration = getMinutes(call);
            String caller = call.callerNumber;
            String callee = call.calleeNumber;
            String start = call.getStartTimeString();
            String end = call.getEndTimeString();

            sb.append("\nCaller: " + caller);
            sb.append("\nCallee: " + callee);
            sb.append("\nCall Start: " + start);
            sb.append("\nCall End: " + end);
            sb.append("\nDuration: " + duration + " minutes");
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Calculates the duration of the phone call in minutes
     *
     * @param call The current <code>PhoneCall</code> object whose duration is being calculated
     * @return duration in minutes
     */
    protected int getMinutes(PhoneCall call) {
        long ms = call.getEndTime().getTime() - call.getStartTime().getTime();
        int minutes = (int) ((ms / 1000) / 60);

        return minutes;
    }
}
