// $Id: Sniffer.java,v 1.1 2002/02/18 21:49:49 pcharles Exp $

/***************************************************************************
 * Copyright (C) 2001, Rex Tsai <chihchun@kalug.linux.org.tw>              *
 * Distributed under the Mozilla Public License                            *
 *   http://www.mozilla.org/NPL/MPL-1.1.txt                                *
 ***************************************************************************/

package com.fuyong.netprobe.pcap;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.fuyong.netprobe.MyAppDirs;
import com.fuyong.netprobe.common.Log;

import net.sourceforge.jpcap.capture.*;
import net.sourceforge.jpcap.net.*;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapDumper;
import org.jnetpcap.PcapHandler;
import org.jnetpcap.PcapIf;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


/**
 * jpcap Tutorial - Sniffer example
 *
 * @author Rex Tsai
 * @version $Revision: 1.1 $
 * @lastModifiedBy $Author: pcharles $
 * @lastModifiedAt $Date: 2002/02/18 21:49:49 $
 */
public class Jpcap extends Service {
    private static final int INFINITE = -1;
    private static final int PACKET_COUNT = INFINITE;
  /*
    private static final String HOST = "203.239.110.20";
    private static final String FILTER =
      "host " + HOST + " and proto TCP and port 23";
  */

    private static final String FILTER =
            // "port 23";
            "";
    private PacketCapture mPcap;

    public Jpcap() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        capture();
        jnetpcap();
        return super.onStartCommand(intent, flags, startId);
    }

//    public static void main(String[] args) {
//        Jpcap jpcap = new Jpcap();
//        jpcap.capture();
//        while (true) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean capture() {
        boolean ret = false;
        // Initialize jpcap
        mPcap = new PacketCapture();
        String device = null;
        try {
            device = mPcap.findDevice();
            mPcap.open(device, true);
            Log.info("[Jpcap]Using device: " + device);
            mPcap.open(device, true);
            mPcap.setFilter(FILTER, true);
            mPcap.addPacketListener(new PacketHandler());
            mPcap.capture(PACKET_COUNT);
            Log.info("[Jpcap]start tcp/ip capture");
            ret = true;
        } catch (CaptureDeviceNotFoundException e) {
            Log.e("Jpcap", e);
        } catch (CaptureDeviceOpenException e) {
            Log.e("Jpcap", e);
        } catch (InvalidFilterException e) {
            Log.e("Jpcap", e);
        } catch (CapturePacketException e) {
            Log.e("Jpcap", e);
        }
        return ret;
    }

    public void jnetpcap() {
        List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs
        StringBuilder errbuf = new StringBuilder();     // For any error msgs

        /***************************************************************************
         * First get a list of devices on this system
         **************************************************************************/
        int r = Pcap.findAllDevs(alldevs, errbuf);
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
            System.err.printf("Can't read list of devices, error is %s\n",
                    errbuf.toString());
            return;
        }
        PcapIf device = alldevs.get(1); // We know we have atleast 1 device

        /***************************************************************************
         * Second we open up the selected device
         **************************************************************************/
        int snaplen = 64 * 1024;           // Capture all packets, no trucation
        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
        int timeout = 10 * 1000;           // 10 seconds in millis
        Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);
        if (pcap == null) {
            System.err.printf("Error while opening device for capture: %s\n",
                    errbuf.toString());
            return;
        }

        /***************************************************************************
         * Third we create a PcapDumper and associate it with the pcap capture
         ***************************************************************************/
        String ofile = MyAppDirs.getAppRootDir() + "tmp-capture-file.cap";
        PcapDumper dumper = pcap.dumpOpen(ofile); // output file

        /***************************************************************************
         * Fouth we create a packet handler which receives packets and tells the
         * dumper to write those packets to its output file
         **************************************************************************/
        PcapHandler<PcapDumper> dumpHandler = new PcapHandler<PcapDumper>() {

            public void nextPacket(PcapDumper dumper, long seconds, int useconds,
                                   int caplen, int len, ByteBuffer buffer) {

                dumper.dump(seconds, useconds, caplen, len, buffer);
            }
        };

        /***************************************************************************
         * Fifth we enter the loop and tell it to capture 10 packets. We pass
         * in the dumper created in step 3
         **************************************************************************/
        pcap.loop(-1, dumpHandler, dumper);

//        File file = new File(ofile);
//        System.out.printf("%s file has %d bytes in it!\n", ofile, file.length());
//
//
//        /***************************************************************************
//         * Last thing to do is close the dumper and pcap handles
//         **************************************************************************/
//        dumper.close(); // Won't be able to delete without explicit close
//        pcap.close();
//
//        if (file.exists()) {
//            file.delete(); // Cleanup
//        }
    }
}


class PacketHandler implements PacketListener {
    public void packetArrived(Packet packet) {
        try {

            if (packet instanceof TCPPacket) {
                TCPPacket tcpPacket = (TCPPacket) packet;
                byte[] data = tcpPacket.getTCPData();

                String srcHost = tcpPacket.getSourceAddress();
                String dstHost = tcpPacket.getDestinationAddress();
                String isoData = new String(data, "ISO-8859-1");

                System.out.println(srcHost + " -> " + dstHost + ": " + isoData);
            }
        } catch (Exception e) {
            Log.e("Jpcap", e);
        }
    }


}
