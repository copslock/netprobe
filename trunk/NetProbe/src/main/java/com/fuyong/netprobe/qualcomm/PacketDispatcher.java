package com.fuyong.netprobe.qualcomm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by f00151473 on 2014/6/10.
 */
public class PacketDispatcher implements Qualcomm.PacketListener {
    private static PacketDispatcher instance;
    private Map<Integer, List<PacketListener>> mListeners = new HashMap<Integer, List<PacketListener>>();
    private PacketBase packetBase = new PacketBase();

    private PacketDispatcher() {
    }

    synchronized public static PacketDispatcher getInstance() {
        if (null == instance) {
            instance = new PacketDispatcher();
        }
        return instance;
    }

    public void registerPacketListener(int logcode, PacketListener packetListener) {
        if (null == packetListener) {
            return;
        }
        List<PacketListener> packetListeners = mListeners.get(logcode);
        if (null == packetListeners) {
            packetListeners = new ArrayList<PacketListener>();
            packetListeners.add(packetListener);
            mListeners.put(logcode, packetListeners);
        } else if (!packetListeners.contains(packetListener)) {
            packetListeners.add(packetListener);
        }
    }

    public void unregisterPacketListener(int logcode, PacketListener packetListener) {
        List<PacketListener> packetListeners = mListeners.get(logcode);
        if (null != packetListeners) {
            packetListeners.remove(packetListener);
        }
    }

    @Override
    public void update(byte[] packetData) {
        packetBase.setPacketData(packetData);
        if (!packetBase.isValid()) {
            return;
        }
        int logcode = packetBase.getLogCode();
        List<PacketListener> packetListeners = mListeners.get(logcode);
        if (null != packetListeners) {
            PacketBase packet = PacketFactory.getInstance().getPacket(logcode, packetData);
            if (null == packet) {
                return;
            }
            for (PacketListener listener : packetListeners) {
                listener.update(packet);
            }
        }
    }

    public interface PacketListener {
        void update(PacketBase packet);
    }
}
