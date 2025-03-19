package IspitSeptemvri.Kolokviumski.K30;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class DurationConverter {
    public static String convert(long duration) {
        long minutes = duration / 60;
        duration %= 60;
        return String.format("%02d:%02d", minutes, duration);
    }
}

interface ICallState {

    void answer(long timestamp);

    void end(long timestamp);

    void hold(long timestamp);

    void resume(long timestamp);

}

abstract class CallState implements ICallState {

    Call call;

    public CallState(Call call) {
        this.call = call;
    }

}

class CallStarted extends CallState {

    public CallStarted(Call call) {
        super(call);
    }

    @Override
    public void answer(long timestamp) {
        call.callStarted = timestamp;
        call.callState = new CallAnswered(call);
    }

    @Override
    public void end(long timestamp) {
        call.callStarted = timestamp;
        call.callEnded = timestamp;
        call.callState = new CallEnded(call);
    }

    @Override
    public void hold(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void resume(long timestamp) {
        throw new RuntimeException();
    }

}

class CallAnswered extends CallState {

    public CallAnswered(Call call) {
        super(call);
    }

    @Override
    public void answer(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void end(long timestamp) {
        call.callEnded = timestamp;
        call.callState = new CallEnded(call);
    }

    @Override
    public void hold(long timestamp) {
        call.callOnHold = timestamp;
        call.callState = new CallOnHold(call);
    }

    @Override
    public void resume(long timestamp) {
        throw new RuntimeException();
    }

}

class CallOnHold extends CallState {

    public CallOnHold(Call call) {
        super(call);
    }

    @Override
    public void answer(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void end(long timestamp) {
        call.callEnded = timestamp;
        call.timeInHold += (timestamp - call.callOnHold);
        call.callState = new CallEnded(call);
    }

    @Override
    public void hold(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void resume(long timestamp) {
        call.timeInHold += (timestamp - call.callOnHold);
        call.callState = new CallAnswered(call);
    }

}

class CallEnded extends CallState {

    public CallEnded(Call call) {
        super(call);
    }

    @Override
    public void answer(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void end(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void hold(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void resume(long timestamp) {
        throw new RuntimeException();
    }

}

class Call {

    String uuid;
    String dialer;
    String receiver;
    long timestamp;
    long callStarted;
    long callEnded;
    long callOnHold;
    long timeInHold;
    CallState callState = new CallStarted(this);

    public Call(String uuid, String dialer, String receiver, long timestamp) {
        this.uuid = uuid;
        this.dialer = dialer;
        this.receiver = receiver;
        this.timestamp = timestamp;
        this.callStarted = 0;
        this.callEnded = 0;
        this.callOnHold = 0;
        this.timeInHold = 0;
    }

    public void updateCall(long timestamp, String action) {
        if (action.equals("ANSWER")) {
            callState.answer(timestamp);
        } else if (action.equals("HOLD")) {
            callState.hold(timestamp);
        } else if (action.equals("RESUME")) {
            callState.resume(timestamp);
        } else if (action.equals("END")) {
            callState.end(timestamp);
        }
    }

//    public String durationOfCall() {
//        return DurationConverter.convert(callEnded - callStarted - timeInHold);
//    }

    public long durationInSeconds() {
        return callEnded - callStarted - timeInHold;
    }

    public String durationOfCall() {
        return DurationConverter.convert(durationInSeconds());
    }

    public String toString(String number) {
        String toPrint = "";
        if (number.equals(dialer)) {
            toPrint = String.format("D %s %d %d %s",
                    receiver, callStarted, callEnded, durationOfCall());
        } else {
            toPrint = String.format("R %s %d %d %s",
                    dialer, callStarted, callEnded, durationOfCall());
        }
        if (callStarted == callEnded) {
            if (number.equals(dialer)) {
                toPrint = String.format("D %s %d MISSED CALL %s",
                        receiver, callStarted, durationOfCall());
            } else {
                toPrint = String.format("R %s %d MISSED CALL %s",
                        dialer, callStarted, durationOfCall());
            }
        }
        return toPrint;
    }

    public int getCallStarted() {
        return (int) callStarted;
    }

}

class TelcoApp {

    Map<String, List<Call>> callsByNumber;
    Map<String, Call> callsByUuid;

    public TelcoApp() {
        this.callsByNumber = new HashMap<>();
        this.callsByUuid = new HashMap<>();
    }

    public void addCall(String uuid, String dialer, String receiver, long timestamp) {
        callsByNumber.putIfAbsent(dialer, new ArrayList<>());
        callsByNumber.putIfAbsent(receiver, new ArrayList<>());

        Call call = new Call(uuid, dialer, receiver, timestamp);
        callsByNumber.get(dialer).add(call);
        callsByNumber.get(receiver).add(call);

        callsByUuid.put(uuid, call);
    }

    public void updateCall(String uuid, long timestamp, String action) {
        callsByUuid.get(uuid).updateCall(timestamp, action);
    }

    public void printChronologicalReport(String phoneNumber) {
        callsByNumber.get(phoneNumber).stream()
                .sorted(Comparator.comparing(Call::getCallStarted))
                .forEach(c -> System.out.println(c.toString(phoneNumber)));
    }

    public void printReportByDuration(String phoneNumber) {
        callsByNumber.get(phoneNumber).stream()
                .sorted(Comparator.comparing(Call::durationOfCall).thenComparing(Call::getCallStarted).reversed())
                .forEach(c -> System.out.println(c.toString(phoneNumber)));
    }

    public void printCallsDuration() {
//        callsByUuid.values().stream()
//                .sorted(Comparator.comparing(Call::durationOfCall).reversed())
//                .map(c -> String.format("%s <-> %s : %s", c.dialer, c.receiver, c.durationOfCall()))
//                .forEach(System.out::println);
        Map<String, Long> totalDurations = new HashMap<>();

        // Iterate over all calls
        for (Call call : callsByUuid.values()) {
            // Calculate the duration in seconds
            long durationInSeconds = call.durationInSeconds();

            // Create a unique key for the pair in the format "dialer -> receiver"
            String key = String.format("%s -> %s", call.dialer, call.receiver);

            // Add the duration to the map, summing it if the pair already exists
            totalDurations.put(key, totalDurations.getOrDefault(key, 0L) + durationInSeconds);
        }

        // Sort the map entries by duration in descending order
        totalDurations.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry -> {
                    String[] phones = entry.getKey().split(" -> ");
                    String dialer = phones[0];
                    String receiver = phones[1];
                    String formattedDuration = DurationConverter.convert(entry.getValue());
                    System.out.println(String.format("%s <-> %s : %s", dialer, receiver, formattedDuration));
                });
    }

}

public class TelcoTest2 {
    public static void main(String[] args) {
        TelcoApp app = new TelcoApp();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            String command = parts[0];

            if (command.equals("addCall")) {
                String uuid = parts[1];
                String dialer = parts[2];
                String receiver = parts[3];
                long timestamp = Long.parseLong(parts[4]);
                app.addCall(uuid, dialer, receiver, timestamp);
            } else if (command.equals("updateCall")) {
                String uuid = parts[1];
                long timestamp = Long.parseLong(parts[2]);
                String action = parts[3];
                app.updateCall(uuid, timestamp, action);
            } else if (command.equals("printChronologicalReport")) {
                String phoneNumber = parts[1];
                app.printChronologicalReport(phoneNumber);
            } else if (command.equals("printReportByDuration")) {
                String phoneNumber = parts[1];
                app.printReportByDuration(phoneNumber);
            } else {
                app.printCallsDuration();
            }
        }

    }
}