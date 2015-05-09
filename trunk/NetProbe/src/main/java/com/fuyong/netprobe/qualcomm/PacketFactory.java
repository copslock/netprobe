package com.fuyong.netprobe.qualcomm;


import com.fuyong.netprobe.common.Log;
import com.fuyong.netprobe.qualcomm.gsm.GSM5071Packet;
import com.fuyong.netprobe.qualcomm.gsm.GSM5076Packet;
import com.fuyong.netprobe.qualcomm.gsm.GSM513APacket;
import com.fuyong.netprobe.qualcomm.wcdma.WCDMA4179Packet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by f00151473 on 2014/6/10.
 */
public class PacketFactory {
    private static PacketFactory instance;
    private Map<Integer, PacketBase> packetMap = new HashMap<Integer, PacketBase>();

    private PacketFactory() {
//        register(0x51FC, new GSM51FCPacket());
        register(0x513A, new GSM513APacket());
        register(0x5071, new GSM5071Packet());
        register(0x5076, new GSM5076Packet());
        register(0x4179, new WCDMA4179Packet());
    }

    synchronized static public PacketFactory getInstance() {
        if (null == instance) {
            instance = new PacketFactory();
        }
        return instance;
    }

    public void register(int logcode, PacketBase packet) {
        if (packetMap.containsKey(logcode)) {
            Log.getLogger(Log.MY_APP).warn("PacketFactory.register():logcode existed");
            return;
        }
        packetMap.put(logcode, packet);
    }

    public PacketBase getPacket(int logcode, byte[] packetData) {
        PacketBase packet = packetMap.get(logcode);
        if (null != packet) {
            packet.setPacketData(packetData);
        }
        return packet;
    }
}
