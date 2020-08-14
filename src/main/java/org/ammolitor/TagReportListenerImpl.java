package org.ammolitor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TagReportListenerImpl implements TagReportListener {

    public void onTagReportedOld(ImpinjReader reader, TagReport report) {
        List<Tag> tags = report.getTags();

        for (Tag t : tags) {
            HashMap<String, Object> output = new HashMap<String, Object>();
            output.putIfAbsent("epc", t.getEpc().toString());
            output.putIfAbsent("timestamp", t.getLastSeenTime().ToString());
            output.putIfAbsent("doppler", t.getRfDopplerFrequency());
            output.putIfAbsent("rssi", t.getPeakRssiInDbm());
            output.putIfAbsent("freq", t.getChannelInMhz());
            output.putIfAbsent("phase_angle", t.getPhaseAngleInRadians());
            output.putIfAbsent("fast_id", t.getTid().toHexString());
            output.putIfAbsent("model", t.getModelDetails().getModelName());
            output.putIfAbsent("epcsize", t.getModelDetails().getEpcSizeBits());
            output.putIfAbsent("usermemsize", t.getModelDetails().getUserMemorySizeBits());
            GsonBuilder gsonMapBuilder = new GsonBuilder();
            Gson gsonObject = gsonMapBuilder.create();
            String JSONObject = gsonObject.toJson(output);
            System.out.println(JSONObject);
        }
    }

    private static void writeBuffered(List<String> records, int bufSize) throws IOException {
        File file = new File("tag_data.log");
        FileWriter writer = new FileWriter(file, true);
        BufferedWriter bufferedWriter = new BufferedWriter(writer, bufSize);

        write(records, bufferedWriter);
    }

    private static void write(List<String> records, Writer writer) throws IOException {
        for (String record : records) {
            writer.write(record);
        }
        writer.flush();
        writer.close();
    }
    @Override
    public void onTagReported(ImpinjReader reader, TagReport report) {
        List<Tag> tags = report.getTags();
        List<String> data = new ArrayList<>();

        for (Tag t : tags) {
            System.out.println(t.getEpc().toString());
            data.add("{ \"epc\": "       + "\"" + t.getEpc().toString() + "\", " +
                   "\"timestamp\": "   + t.getLastSeenTime().ToString() + ", " +
                   "\"doppler\": "     + t.getRfDopplerFrequency() + ", " +
                   "\"rssi\": "        + t.getPeakRssiInDbm() + ", " +
                   "\"freq\": "        + t.getChannelInMhz() + ", " +
                   "\"phase_angle\": " + t.getPhaseAngleInRadians() + ", " +
                   "\"fast_id\": "     + "\"" + t.getTid().toHexString() + "\", " +
                   "\"model\": "       + "\"" + t.getModelDetails().getModelName() + "\", " +
                   "\"epcsize\": "     + t.getModelDetails().getEpcSizeBits() + ", " +
                   "\"usermemsize\": " + t.getModelDetails().getUserMemorySizeBits() +
                   " }\n");
        }
        try {
            writeBuffered(data,1048576);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
