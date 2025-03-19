package ZimskiSemestar.VtorKolokvium.Kolokviumski.Z30;

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

    void hold(long timestamp);

    void resume(long timestamp);

    void end(long timestamp);
}

abstract class CallState implements ICallState {
    Call call;

    public CallState(Call call) {
        this.call = call;
    }
}

class CallStartedState extends CallState {

    public CallStartedState(Call call) {
        super(call);
    }

    @Override
    public void answer(long timestamp) {
        call.callStarted = timestamp;
        call.state = new InProgressCallState(call);
    }

    @Override
    public void hold(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void resume(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void end(long timestamp) {
        call.callStarted = timestamp;
        call.callEnded = timestamp;
        call.state = new TerminatedCallState(call);
    }
}

class InProgressCallState extends CallState {

    public InProgressCallState(Call call) {
        super(call);
    }

    @Override
    public void answer(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void hold(long timestamp) {
        call.holdStarted = timestamp;
        call.state = new CallOnHold(call);
    }

    @Override
    public void resume(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void end(long timestamp) {
        call.callEnded = timestamp;
        call.state = new TerminatedCallState(call);
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
    public void hold(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void resume(long timestamp) {
        call.totalTimeInHold += (timestamp - call.holdStarted);
        //call.holdStarted = -1;
        call.state = new InProgressCallState(call);
    }

    @Override
    public void end(long timestamp) {
        call.totalTimeInHold += (timestamp - call.holdStarted);
        call.callEnded = timestamp;
        call.state = new TerminatedCallState(call);
    }
}

class TerminatedCallState extends CallState {

    public TerminatedCallState(Call call) {
        super(call);
    }

    @Override
    public void answer(long timestamp) {
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

    @Override
    public void end(long timestamp) {
        throw new RuntimeException();
    }
}

class Call {
    String uuid;
    String dialer;
    String receiver;
    long timestampCalled;
    long callStarted;
    long callEnded;
    long totalTimeInHold = 0;
    long holdStarted = -1;
    CallState state = new CallStartedState(this);

    public Call(String uuid, String dialer, String receiver, long timestampCalled) {
        this.uuid = uuid;
        this.dialer = dialer;
        this.receiver = receiver;
        this.timestampCalled = timestampCalled;
    }

    public void answer(long timestamp) {
        state.answer(timestamp);
    }

    public void hold(long timestamp) {
        state.hold(timestamp);
    }

    public void resume(long timestamp) {
        state.resume(timestamp);
    }

    public void end(long timestamp) {
        state.end(timestamp);
    }

    public long totalTime() {
        return callEnded - callStarted - totalTimeInHold;
    }

    public String getUuid() {
        return uuid;
    }

    public String getDialer() {
        return dialer;
    }

    public String getReceiver() {
        return receiver;
    }

    public long getTimestampCalled() {
        return timestampCalled;
    }

    public long getCallStarted() {
        return callStarted;
    }

    public long getCallEnded() {
        return callEnded;
    }

    public long getTotalTimeInHold() {
        return totalTimeInHold;
    }

    public long getHoldStarted() {
        return holdStarted;
    }

    public CallState getState() {
        return state;
    }
}

class TelcoApp {
    Map<String, List<Call>> callsByNumber;
    Map<String, Call> callsByUuid;

    public TelcoApp() {
        callsByNumber = new LinkedHashMap<>();
        callsByUuid = new LinkedHashMap<>();
    }

    void addCall(String uuid, String dialer, String receiver, long timestamp) {
        Call call = new Call(uuid, dialer, receiver, timestamp);

        callsByNumber.putIfAbsent(dialer, new ArrayList<>());
        callsByNumber.putIfAbsent(receiver, new ArrayList<>());

        callsByNumber.get(dialer).add(call);
        callsByNumber.get(receiver).add(call);

        callsByUuid.put(uuid, call);
    }

    void updateCall(String uuid, long timestamp, String action) {
        if (action.equals("ANSWER")) {
            callsByUuid.get(uuid).answer(timestamp);
        } else if (action.equals("HOLD")) {
            callsByUuid.get(uuid).hold(timestamp);
        } else if (action.equals("RESUME")) {
            callsByUuid.get(uuid).resume(timestamp);
        } else if (action.equals("END")) {
            callsByUuid.get(uuid).end(timestamp);
        }
    }

    void printChronologicalReport(String phoneNumber) {
        List<Call> calls = callsByNumber.get(phoneNumber);

        methodForPrinting(phoneNumber, (List<Call>) calls);
    }

    void printReportByDuration(String phoneNumber) {
        List<Call> calls = callsByNumber.get(phoneNumber)
                .stream().sorted(Comparator.comparing(Call::totalTime).thenComparing(Call::getCallStarted).reversed())
                .collect(Collectors.toList());

        methodForPrinting(phoneNumber, (List<Call>) calls);
    }

    private void methodForPrinting(String phoneNumber, List<Call> calls) {
        calls.forEach(call -> {
            boolean isDialer = call.dialer.equals(phoneNumber);
            String role = isDialer ? "D" : "R";
            String counterpart = isDialer ? call.receiver : call.dialer;
            String callDuration = DurationConverter.convert(call.totalTime());
            String message = callDuration.equals("00:00") ?
                    String.format("%s %s %d MISSED CALL %s", role, counterpart, call.callStarted, callDuration) :
                    String.format("%s %s %d %d %s", role, counterpart, call.callStarted, call.callEnded, callDuration);
            System.out.println(message);
        });

        //        calls.forEach(call -> {
//            if (call.dialer.equals(phoneNumber)) {
//                if (DurationConverter.convert(call.totalTime()).equals("00:00")) {
//                    {
//                        System.out.println(
//                                String.format("D %s %d MISSED CALL %s",
//                                        call.receiver, call.callStarted, DurationConverter.convert(call.totalTime()))
//                        );
//                    }
//                } else {
//                    System.out.println(
//                            String.format("D %s %d %d %s",
//                                    call.receiver, call.callStarted, call.callEnded, DurationConverter.convert(call.totalTime()))
//                    );
//                }
//            } else {
//                if (DurationConverter.convert(call.totalTime()).equals("00:00")) {
//                    System.out.println(
//                            String.format("R %s %d MISSED CALL %s",
//                                    call.dialer, call.callStarted, DurationConverter.convert(call.totalTime()))
//                    );
//                } else {
//                    System.out.println(
//                            String.format("R %s %d %d %s",
//                                    call.dialer, call.callStarted, call.callEnded, DurationConverter.convert(call.totalTime()))
//                    );
//                }
//            }
//        });
    }

    void printCallsDuration() {
        Map<String, Long> durationMap = new HashMap<>();

        for (Call call : callsByUuid.values()) {
            String key = call.dialer + " <-> " + call.receiver;
            durationMap.put(key, durationMap.getOrDefault(key, 0L) + call.totalTime());
        }

        durationMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry -> {
                    String key = entry.getKey();
                    long totalDuration = entry.getValue();
                    System.out.println(String.format("%s : %s", key, DurationConverter.convert(totalDuration)));
                });

//        callsByUuid.values().stream()
//                .sorted(Comparator.comparing(Call::totalTime).thenComparing(Call::getCallStarted).reversed())
//                .forEach(c -> {
//                    System.out.println(String.format("%s <-> %s : %s",
//                            c.dialer, c.receiver, DurationConverter.convert(c.totalTime())));
//                });
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
