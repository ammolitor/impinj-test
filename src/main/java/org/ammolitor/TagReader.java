package org.ammolitor;

import com.impinj.octane.*;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


public class TagReader {

    public static void main(String[] args) {

        try {
            String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            String appConfigPath = rootPath + "config.properties";
            Properties appProps = new Properties();
            appProps.load(new FileInputStream(appConfigPath));
            String hostname = appProps.getProperty("hostname");

            if (hostname == null) {
                throw new Exception("Must specify the \"hostname\" property");
            }

            ImpinjReader reader = new ImpinjReader(hostname, "r420");

            // Connect
            System.out.println("Connecting to " + hostname);
            reader.connect(hostname);

            // Get the default settings
            Settings settings = reader.queryDefaultSettings();

            ReportConfig rc = settings.getReport();
            rc.setIncludeAntennaPortNumber(true);
            rc.setIncludeChannel(true);
            rc.setIncludeCrc(true);
            rc.setIncludeAntennaPortNumber(true);
            rc.setIncludeChannel(true);
            rc.setIncludeCrc(true);
            rc.setIncludeDopplerFrequency(true);
            rc.setIncludeFastId(true);
            rc.setIncludeFirstSeenTime(true);
            rc.setIncludeGpsCoordinates(true);
            rc.setIncludeLastSeenTime(true);
            rc.setIncludePcBits(true);
            rc.setIncludePeakRssi(true);
            rc.setIncludePhaseAngle(true);
            rc.setIncludeSeenCount(true);
            rc.setMode(ReportMode.Individual);

            // set just one specific antenna for this example
            AntennaConfigGroup ac = settings.getAntennas();
            ac.disableAll();
            ac.getAntenna((short) 1).setEnabled(true);
            ac.getAntenna((short) 1).setTxPowerinDbm(27);

            // Apply the new settings
            reader.applySettings(settings);

            // connect a listener
            reader.setTagReportListener(new TagReportListenerImpl());

            // Start the reader
            reader.start();

            int sleeptime = 10;
            System.out.println("Starting Reader for " + sleeptime + " seconds");
            TimeUnit.SECONDS.sleep(sleeptime);

            System.out.println("Stopping  " + hostname);
            reader.stop();

            System.out.println("Disconnecting from " + hostname);
            reader.disconnect();

            System.out.println("Done");
        } catch (OctaneSdkException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }
}
